package chau.nguyen.calendar.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import chau.nguyen.MonthActivity;
import chau.nguyen.R;
import chau.nguyen.calendar.VietCalendar;

public class VNMDayViewer extends LinearLayout {
	
	private TextView dayOfMonthText;
	private TextView dayOfWeekText;
	private TextView noteText;
	
	protected TextView vnmHourText;
	protected TextView vnmHourInText;
	protected TextView vnmDayOfMonthText;
	protected TextView vnmDayOfMonthInText;
	protected TextView vnmMonthText;
	protected TextView vnmMonthInText;
	protected TextView vnmYearText;
	protected TextView vnmYearInText;
	
	protected Button nextButton;
	protected Button previousButton;
	
	private Date displayDate;
	
	private MonthActivity monthActivity;
	
	
	static private String[] dayInVietnamese;
	
	static {
		dayInVietnamese = new String[] {"Chủ nhật", "Thứ hai", "Thứ ba", "Thứ tư", "Thứ năm", "Thứ sáu", "Thứ bảy"};
	}

	public VNMDayViewer(MonthActivity context) {
		super(context);
		init(context);
	}
	
	public VNMDayViewer(MonthActivity context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(MonthActivity monthActivity) {
		// Inflate the view from the layout resource.
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li;
		li = (LayoutInflater)getContext().getSystemService(infService);
		li.inflate(R.layout.vnm_day_viewer, this, true);
		
		this.monthActivity = monthActivity;
		
		this.dayOfMonthText = (TextView)findViewById(R.id.dayOfMonthText);
		this.dayOfWeekText = (TextView)findViewById(R.id.dayOfWeekText);
		this.noteText = (TextView)findViewById(R.id.noteText);
		
		this.vnmHourText = (TextView) findViewById(R.id.vnmHourText);
		this.vnmHourInText = (TextView) findViewById(R.id.vnmHourInText);
		this.vnmDayOfMonthText = (TextView) findViewById(R.id.vnmDayOfMonthText);
		this.vnmDayOfMonthInText = (TextView) findViewById(R.id.vnmDayOfMonthInText);
		this.vnmMonthText = (TextView) findViewById(R.id.vnmMonthText);
		this.vnmMonthInText = (TextView) findViewById(R.id.vnmMonthInText);
		this.vnmYearText = (TextView) findViewById(R.id.vnmYearText);
		this.vnmYearInText = (TextView) findViewById(R.id.vnmYearInText);
		
		this.nextButton = (Button)findViewById(R.id.nextButton);
        this.previousButton = (Button)findViewById(R.id.previousButton);
		
		this.displayDate = new Date();
		this.setDate(this.displayDate);
		
		this.dayOfMonthText.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:
					
					break;

				default:
					break;
				}
				return true;
			}
		});
		
		this.nextButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(displayDate);
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				Date afterDate = calendar.getTime();
				VNMDayViewer.this.monthActivity.gotoTime(afterDate);
			}
			
		});
		
		this.previousButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(displayDate);
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				Date beforeDate = calendar.getTime();
				VNMDayViewer.this.monthActivity.gotoTime(beforeDate);
			}
			
		});
	}
	
	public void setDate(Date date) {
		this.displayDate = date;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		this.dayOfMonthText.setText(dayOfMonth + "");
		this.dayOfWeekText.setText(dayInVietnamese[dayOfWeek - 1]);
		
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		int month = calendar.get(Calendar.MONTH) + 1;
		
		int year = calendar.get(Calendar.YEAR);
		TimeZone tz = calendar.getTimeZone();
		double timeZone = tz.getRawOffset() / 3600000;		
		int[] lunars = VietCalendar.convertSolar2Lunar(dayOfMonth, month, year, timeZone);
		
		this.vnmHourText.setText(hour + ":" + minute);
		this.vnmDayOfMonthText.setText(lunars[0] + "");
		this.vnmMonthText.setText(lunars[1] + "");
		this.vnmYearText.setText(lunars[2] + "");
		
		String[] vnmCalendarTexts = VietCalendar.getCanChiInfo(lunars[0], lunars[1], lunars[2], dayOfMonth, month, year);
		
		this.vnmHourInText.setText(vnmCalendarTexts[VietCalendar.HOUR]);
		this.vnmDayOfMonthInText.setText(vnmCalendarTexts[VietCalendar.DAY]);
		this.vnmMonthInText.setText(vnmCalendarTexts[VietCalendar.MONTH]);
		this.vnmYearInText.setText(vnmCalendarTexts[VietCalendar.YEAR]);
	}
	
//	public void next() {
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(this.displayDate);
//		calendar.add(Calendar.DAY_OF_MONTH, 1);
//		Date afterDate = calendar.getTime();
//		setDate(afterDate);
//	}
	
//	public void back() {
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(this.displayDate);
//		calendar.add(Calendar.DAY_OF_MONTH, -1);
//		Date beforeDate = calendar.getTime();
//		setDate(beforeDate);
//	}
	
	public void setNote(String note) {
		this.noteText.setText(note);
	}
	
	public Date getDisplayDate() {
		return displayDate;
	}

}
