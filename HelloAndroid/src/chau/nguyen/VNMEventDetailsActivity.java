package chau.nguyen;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemSelectedListener;
import chau.nguyen.calendar.VNMDate;
import chau.nguyen.calendar.VietCalendar;
import chau.nguyen.calendar.ui.VNMDatePickerDialog;
import chau.nguyen.calendar.ui.VNMDatePickerDialog.OnDateSetListener;

public class VNMEventDetailsActivity extends Activity {
	private static Map<Integer, String> repeations;
	private static final int NO_REPEAT = 0;
	private static final int MONTHLY_REPEAT = 1;
	private static final int YEARLY_REPEAT = 2;
	
	private static Map<Integer, String> reminds;
	private static final int REMIND_10_MINUTES = 0; // ten minutes
	private static final int REMIND_1_HOUR = 1; // one hour = 60 minutes
	private static final int REMIND_1_DAY = 2; // one day = 1440 minutes
	
	private static Map<Integer, String> numberYears;
	private static final int ONE_YEAR = 0;
	private static final int TWO_YEARS = 1;
	private static final int FIVE_YEARS = 2;
	private static final int TEN_YEARS = 3;
	private static final int TWENTY_YEARS = 4;
	
	private static Map<Integer, CalTable> calendars;
	private VNMDate startDate;
	private VNMDate endDate;
	protected EditText titleEditText;
	protected EditText locationEditText;
	protected EditText descriptionEditText;
	protected Button startDateButton;
	protected Button startTimeButton;
	protected Button endDateButton;
	protected Button endTimeButton;
	protected Button saveButton;
	protected Button discardButton;
	protected Spinner repeatsDropDown;
	protected Spinner remindersDropDown;
	protected Spinner calendarsDropDown;
	protected Spinner numberYearsDropDown;
	
	static {
		reminds = new HashMap<Integer, String>();
		reminds.put(REMIND_10_MINUTES, "Nhắc trước 10 phút");
		reminds.put(REMIND_1_HOUR, "Nhắc trước 1 giờ");
		reminds.put(REMIND_1_DAY, "Nhắc trước 1 ngày");
		
		repeations = new HashMap<Integer, String>();
		repeations.put(NO_REPEAT, "Chỉ một lần");
		repeations.put(MONTHLY_REPEAT, "Hàng tháng");
		repeations.put(YEARLY_REPEAT, "Hàng năm");
		
		numberYears = new HashMap<Integer, String>();
		numberYears.put(ONE_YEAR, "Trong 1 năm");
		numberYears.put(TWO_YEARS, "Trong 2 năm");
		numberYears.put(FIVE_YEARS, "Trong 5 năm");
		numberYears.put(TEN_YEARS, "Trong 10 năm");
		numberYears.put(TWENTY_YEARS, "Trong 20 năm");
		
		calendars = new HashMap<Integer, CalTable>();
	}
	
	private static long getRemindTime(int id) {
		switch (id) {
			case REMIND_1_DAY:
				return 1440;
			case REMIND_1_HOUR:
				return 60;
			default:
				return 10;
		}
	}
	
	private static int getNumberYears(int index) {
		switch (index) {
		case ONE_YEAR:
			return 1;
			
		case TWO_YEARS:
			return 2;
			
		case TEN_YEARS:
			return 10;
			
		case TWENTY_YEARS:
			return 20;

		default:
			return 5;
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vnm_event_detail);
		init();
	}
	
	private void init() {
		this.titleEditText = (EditText)findViewById(R.id.titleEditText);
		this.descriptionEditText = (EditText)findViewById(R.id.descriptionEditText);
		this.locationEditText = (EditText)findViewById(R.id.locationEditText);
		this.startDateButton = (Button)findViewById(R.id.startDateButton);
		this.startTimeButton = (Button)findViewById(R.id.startTimeButton);
		this.endDateButton = (Button)findViewById(R.id.endDateButton);
		this.endTimeButton = (Button)findViewById(R.id.endTimeButton);
		this.saveButton = (Button)findViewById(R.id.saveButton);
		this.discardButton = (Button)findViewById(R.id.discardButton);
		this.remindersDropDown = (Spinner)findViewById(R.id.remindersDropDown);
		this.repeatsDropDown = (Spinner)findViewById(R.id.repeatsDropDown);
		this.calendarsDropDown = (Spinner)findViewById(R.id.calendarsDropDown);
		this.numberYearsDropDown = (Spinner)findViewById(R.id.numberYearsDropDown);
		Calendar cal = Calendar.getInstance();
		
		this.startDate =  VietCalendar.convertSolar2LunarInVietnamese(cal.getTime());
		setDate(this.startDateButton, this.startDate);
		setTime(this.startTimeButton, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
		cal.add(Calendar.HOUR_OF_DAY, 1);
		this.endDate = VietCalendar.convertSolar2LunarInVietnamese(cal.getTime());
		setDate(this.endDateButton, this.endDate);
		setTime(this.endTimeButton, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
		this.startDateButton.setOnClickListener(new DateClickListener(this.startDateButton));
		this.endDateButton.setOnClickListener(new DateClickListener(this.endDateButton));
		this.startTimeButton.setOnClickListener(new TimeClickListener(this.startTimeButton));
		this.endTimeButton.setOnClickListener(new TimeClickListener(this.endTimeButton));
		
		ArrayAdapter<String> repeatAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		for (Integer item : repeations.keySet()) {
			repeatAdapter.insert(repeations.get(item), item);
		}
		repeatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.repeatsDropDown.setAdapter(repeatAdapter);
		this.repeatsDropDown.setSelection(NO_REPEAT);
		this.repeatsDropDown.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long arg3) {
				repeatsDropDown.setSelection(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		ArrayAdapter<String> remindAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		for (Integer item : reminds.keySet()) {
			remindAdapter.insert(reminds.get(item), item);
		}
		remindAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.remindersDropDown.setAdapter(remindAdapter);
		this.remindersDropDown.setSelection(REMIND_10_MINUTES);
		this.remindersDropDown.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long arg3) {
				remindersDropDown.setSelection(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		ArrayAdapter<String> numberYearsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		for (Integer item : numberYears.keySet()) {
			numberYearsAdapter.insert(numberYears.get(item), item);
		}
		numberYearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.numberYearsDropDown.setAdapter(numberYearsAdapter);
		this.numberYearsDropDown.setSelection(FIVE_YEARS);
		this.numberYearsDropDown.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long arg3) {
				numberYearsDropDown.setSelection(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		String[] projection = new String[] { "_id", "name" };
		Uri cals = Uri.parse("content://calendar/calendars");
		Cursor managedCursor = managedQuery(cals, projection, "selected=1", null, null);
		if (managedCursor.moveToFirst()) {
			 String calId;
			 String calName;
			 int idColumn = managedCursor.getColumnIndex("_id");
			 int nameColumn = managedCursor.getColumnIndex("name");
			 int index = 0;
			 do {
			    calId = managedCursor.getString(idColumn);
			    calName = managedCursor.getString(nameColumn);
			    calendars.put(index, new CalTable(calId, calName));
			    index++;
			 } while (managedCursor.moveToNext());
			 calendars.put(index, new CalTable(CalTable.ALL, "Tất cả"));
		}
		ArrayAdapter<String> calAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		for (Integer item : calendars.keySet()) {
			calAdapter.insert(calendars.get(item).name, item);
		}
		calAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.calendarsDropDown.setAdapter(calAdapter);
		this.calendarsDropDown.setSelection(calAdapter.getCount() - 1);
		this.calendarsDropDown.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long arg3) {
				calendarsDropDown.setSelection(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		this.saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ProgressDialog dialog = ProgressDialog.show(VNMEventDetailsActivity.this, "", 
                        "Tạo sự kiện...", true);
				dialog.show();
				Integer selected = calendarsDropDown.getSelectedItemPosition();
				String calId = ((CalTable)calendars.get(selected)).id;
				if (CalTable.ALL.equals(calId)) {
					for (int i = 0; i < calendars.size() - 1; i++) {
						addEvent(((CalTable)calendars.get((Integer)i)).id);
					}
				} else {
					addEvent(calId);
				}
				finish();
			}
			
		});
		
		this.discardButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
			
		});
	}
	
	private void addEvent(String calId) {
		ContentValues event = new ContentValues();
		event.put("calendar_id", calId);
		event.put("title", this.titleEditText.getText().toString());
		event.put("description", this.descriptionEditText.getText().toString());
		event.put("eventLocation", this.locationEditText.getText().toString());
		Calendar calStart = Calendar.getInstance();
		VNMDate temp = VietCalendar.convertSolar2LunarInVietnamese(calStart.getTime());
		int currentYear = temp.getYear();
		if (currentYear > this.startDate.getYear()) {
			this.startDate.setYear(currentYear);
		}
		
		if (currentYear > this.endDate.getYear()) {
			this.endDate.setYear(currentYear);
		}
		int numberYears = getNumberYears(this.numberYearsDropDown.getSelectedItemPosition());
		switch (this.repeatsDropDown.getSelectedItemPosition()) {
		case YEARLY_REPEAT:
			for (int i = 0; i <= numberYears; i++) {
				createEvent(event, VietCalendar.addYear(this.startDate, i), VietCalendar.addYear(this.endDate, i));
			}
			break;
		
		case MONTHLY_REPEAT:
			for (int i = 0; i <= 12 * numberYears; i++) {
				createEvent(event, VietCalendar.addMonth(this.startDate, i), VietCalendar.addMonth(this.endDate, i));
			}
		break;
		default:
			createEvent(event, this.startDate, this.endDate);
			break;
		}
	}
	
	private void createEvent(ContentValues event, VNMDate startDate, VNMDate endDate) {
		ContentResolver cr = getContentResolver();
		Log.i("Event", "startLunarDay: " + startDate.getDayOfMonth() + "/" + startDate.getMonth() + "/" + startDate.getYear());
		Date solarStartDate = VietCalendar.convertLunar2Solar(startDate);
		Log.i("Event", "startSolarDay: " + solarStartDate);
		
		Log.i("Event", "endLunarDay: " + endDate.getDayOfMonth() + "/" + endDate.getMonth() + "/" + endDate.getYear());
		Date solarEndDate = VietCalendar.convertLunar2Solar(endDate);
		Log.i("Event", "startSolarDay: " + solarEndDate);
		long startTime = solarStartDate.getTime();
		long endTime = solarEndDate.getTime();
		event.put("dtstart", startTime);
		event.put("dtend", endTime);
		
		event.put("hasAlarm", 1);
		try {
			Uri eventsUri = Uri.parse("content://calendar/events");
			Uri newEvent = cr.insert(eventsUri, event);
			long id = Long.parseLong(newEvent.getLastPathSegment());
			if(newEvent != null) {
				long reminderMinutes = getRemindTime(remindersDropDown.getSelectedItemPosition());

				ContentValues values = new ContentValues();
				values.put("event_id", id);
				values.put("method", 1);
				values.put("minutes", reminderMinutes);
				cr.insert(Uri.parse("content://calendar/reminders"), values);

//				ContentValues alertValues = new ContentValues();
//				alertValues.put("event_id", id);
//				alertValues.put("begin", startTime);
//				alertValues.put("end", endTime);
//				alertValues.put( "alarmTime", startTime);
//				alertValues.put( "state", 0 );
//				alertValues.put( "minutes", reminderMinutes );
//				cr.insert( Uri.parse( "content://calendar/calendar_alerts" ), alertValues );
				} 
		} catch (Exception e) {
			Log.e("AddingCalendarEvent", e.getMessage());
		}
	}
	
	private void setDate(TextView view, VNMDate date) {
		view.setText(date.getDayOfMonth() + "-" + date.getMonth() + "-" + date.getYear() + " (Âm lịch)");
	}
	
	private void setTime(TextView view, int hourOfDay, int minute) {
		String hour = hourOfDay + "";
		String minuteInText = minute + "";
		if (hourOfDay < 10)
			hour = "0" + hour;
		if (minute < 10)
			minuteInText = "0" + minuteInText;
			
		view.setText(hour + ":" + minuteInText);
	}
	
	private class DateClickListener implements View.OnClickListener {
		 private View view;
		 public DateClickListener(View view) {
			 this.view = view;
		 }
		 public void onClick(View v) {
			 
			 if (view == startDateButton) {
				 VNMDatePickerDialog dialog = new VNMDatePickerDialog(VNMEventDetailsActivity.this, new DateSetListener(DateSetListener.START_DATE), startDate.getYear(), startDate.getMonth() - 1, startDate.getDayOfMonth());
				 dialog.setRadioGroupDateTypeVisible(false);
				 dialog.show();
			 } else {
				 VNMDatePickerDialog dialog = new VNMDatePickerDialog(VNMEventDetailsActivity.this, new DateSetListener(DateSetListener.END_DATE), endDate.getYear(), endDate.getMonth() - 1, endDate.getDayOfMonth());
				 dialog.setRadioGroupDateTypeVisible(false);
				 dialog.show();
			 }
		 }
	}
	
	private class DateSetListener implements OnDateSetListener {
		private static final String START_DATE = "startDate";
		private static final String END_DATE = "endDate";
		private String dialog;
		
		public DateSetListener(String dialog) {
			this.dialog = dialog;
		}

		@Override
		public void onDateSet(DatePicker view, boolean isSolarSelected,
				int year, int monthOfYear, int dayOfMonth) {
			monthOfYear ++;
			if (this.dialog.equals(START_DATE)) {
				startDate.setDayOfMonth(dayOfMonth);
				startDate.setMonth(monthOfYear);
				startDate.setYear(year);
				setDate(startDateButton, startDate);
			} else {
				endDate.setDayOfMonth(dayOfMonth);
				endDate.setMonth(monthOfYear);
				endDate.setYear(year);
				setDate(endDateButton, endDate);
			}
		}
		
	}
	
	private class TimeClickListener implements View.OnClickListener {
		 private View view;
		 public TimeClickListener(View view) {
			 this.view = view;
		 }
		 public void onClick(View v) {
			 if (view == startTimeButton) {
				 new TimePickerDialog(VNMEventDetailsActivity.this, new TimeSetListener(TimeSetListener.START_TIME), startDate.getHourOfDay(), startDate.getMinute(), true).show();
			 } else {
				 new TimePickerDialog(VNMEventDetailsActivity.this, new TimeSetListener(TimeSetListener.END_TIME), endDate.getHourOfDay(), endDate.getMinute(), true).show();
			 }
		 }
	}
	
	private class TimeSetListener implements OnTimeSetListener {
		private static final String START_TIME = "startTime";
		private static final String END_TIME = "endTime";
		private String dialog;
		
		public TimeSetListener(String dialog) {
			this.dialog = dialog;
		}

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			if (START_TIME.equals(this.dialog)) {
				startDate.setHourOfDay(hourOfDay);
				startDate.setMinute(minute);
				setTime(startTimeButton, hourOfDay, minute);
			} else {
				endDate.setHourOfDay(hourOfDay);
				endDate.setMinute(minute);
				setTime(endTimeButton, hourOfDay, minute);
			}
		}
		
	}
	
	private class CalTable {
		public static final String ALL = "ALL";
		public String id;
		public String name;
		public CalTable(String id, String name) {
			this.id = id;
			this.name = name;
		}
	}
}
