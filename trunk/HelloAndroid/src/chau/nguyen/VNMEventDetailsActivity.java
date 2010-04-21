package chau.nguyen;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import chau.nguyen.calendar.VietCalendar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class VNMEventDetailsActivity extends Activity {
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
		this.startDateButton.setText(VietCalendar.formatVietnameseDate(cal.getTime()));
		
	}
}
