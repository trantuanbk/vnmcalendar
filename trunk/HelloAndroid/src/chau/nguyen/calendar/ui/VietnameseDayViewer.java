package chau.nguyen.calendar.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TextView;
import chau.nguyen.R;
import chau.nguyen.calendar.VietCalendar;

public class VietnameseDayViewer extends TableLayout {
	protected TextView vnmHourText;
	protected TextView vnmHourInText;
	protected TextView vnmDayOfMonthText;
	protected TextView vnmDayOfMonthInText;
	protected TextView vnmMonthText;
	protected TextView vnmMonthInText;
	protected TextView vnmYearText;
	protected TextView vnmYearInText;
	

	public VietnameseDayViewer(Context context) {
		super(context);
		init();
	}

	public VietnameseDayViewer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		// Inflate the view from the layout resource.
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li;
		li = (LayoutInflater)getContext().getSystemService(infService);
		li.inflate(R.layout.vietnamese_day_viewer, this, true);
		
		this.vnmHourText = (TextView) findViewById(R.id.vnmHourText);
		this.vnmHourInText = (TextView) findViewById(R.id.vnmHourInText);
		this.vnmDayOfMonthText = (TextView) findViewById(R.id.vnmDayOfMonthText);
		this.vnmDayOfMonthInText = (TextView) findViewById(R.id.vnmDayOfMonthInText);
		this.vnmMonthText = (TextView) findViewById(R.id.vnmMonthText);
		this.vnmMonthInText = (TextView) findViewById(R.id.vnmMonthInText);
		this.vnmYearText = (TextView) findViewById(R.id.vnmYearText);
		this.vnmYearInText = (TextView) findViewById(R.id.vnmYearInText);
	}
	
	public void setDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
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
}
