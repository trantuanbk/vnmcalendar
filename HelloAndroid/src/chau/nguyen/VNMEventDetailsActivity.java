package chau.nguyen;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import chau.nguyen.calendar.VietCalendar;
import chau.nguyen.calendar.ui.VNMDatePickerDialog;
import chau.nguyen.calendar.ui.VNMDatePickerDialog.OnDateSetListener;

public class VNMEventDetailsActivity extends Activity {
	private Date startDate;
	private Date endDate;
	private int startTime;
	private int endTime;
	protected EditText titleEditText;
	protected Button startDateButton;
	protected Button startTimeButton;
	protected Button endDateButton;
	protected Button endTimeButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vnm_event_detail);
		this.titleEditText = (EditText)findViewById(R.id.titleEditText);
		this.startDateButton = (Button)findViewById(R.id.startDateButton);
		this.startTimeButton = (Button)findViewById(R.id.startTimeButton);
		this.endDateButton = (Button)findViewById(R.id.endDateButton);
		this.endTimeButton = (Button)findViewById(R.id.endTimeButton);
		Calendar cal = Calendar.getInstance();
		this.startDate = cal.getTime();
		this.startTime = cal.get(Calendar.HOUR_OF_DAY);
		this.startTimeButton.setText(this.startTime + " giờ");
		setDate(this.startDateButton, cal.getTime());
		cal.add(Calendar.DAY_OF_MONTH, 1);
		this.endDate = cal.getTime();
		this.endTime = cal.get(Calendar.HOUR_OF_DAY);
		this.endTimeButton.setText(this.endTime + " giờ");
		setDate(this.endDateButton, cal.getTime());
		this.startDateButton.setOnClickListener(new DateClickListener(this.startDateButton));
		this.endDateButton.setOnClickListener(new DateClickListener(this.endDateButton));
		
	}
	
	private void setDate(TextView view, Date date) {
		view.setText(VietCalendar.formatVietnameseDate(date));
	}
	
	private void setTime(TextView view, int time) {
		view.setText(time + " giờ");
	}
	private class DateClickListener implements View.OnClickListener {
		 private View view;
		 public DateClickListener(View view) {
			 this.view = view;
		 }
		 public void onClick(View v) {
			 Calendar cal = Calendar.getInstance();
			 int year = cal.get(Calendar.YEAR);
			 int month = cal.get(Calendar.MONTH);
			 int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
			 if (view == startDateButton) {
				 new VNMDatePickerDialog(VNMEventDetailsActivity.this, new DateSetListener(DateSetListener.START_DATE), year, month, dayOfMonth).show();
			 } else {
				 new VNMDatePickerDialog(VNMEventDetailsActivity.this, new DateSetListener(DateSetListener.END_DATE), year, month, dayOfMonth).show();
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
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, monthOfYear);
			cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			if (this.dialog.equals(START_DATE)) {
				setDate(startDateButton, cal.getTime());
			} else {
				setDate(endDateButton, cal.getTime());
			}
		}
		
	}
}
