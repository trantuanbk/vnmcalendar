package chau.nguyen.calendar.ui;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import chau.nguyen.R;

public class DayViewer extends LinearLayout {
	private TextView dayOfMonthText;
	private TextView dayOfWeekText;
	private TextView noteText;
	static private String[] dayInVietnamese;
	
	static {
		dayInVietnamese = new String[] {"Chủ nhật", "Thứ hai", "Thứ ba", "Thứ tư", "Thứ năm", "Thứ sáu", "Thứ bảy"};
	}

	public DayViewer(Context context) {
		super(context);
		init();
	}
	
	public DayViewer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		// Inflate the view from the layout resource.
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li;
		li = (LayoutInflater)getContext().getSystemService(infService);
		li.inflate(R.layout.day_viewer, this, true);
		
		this.dayOfMonthText = (TextView)findViewById(R.id.dayOfMonthText);
		this.dayOfWeekText = (TextView)findViewById(R.id.dayOfWeekText);
		this.noteText = (TextView)findViewById(R.id.noteText);
	}
	
	public void setDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		this.dayOfMonthText.setText(dayOfMonth + "");
		this.dayOfWeekText.setText(dayInVietnamese[dayOfWeek - 1]);
	}
	
	public void setNote(String note) {
		this.noteText.setText(note);
	}

}
