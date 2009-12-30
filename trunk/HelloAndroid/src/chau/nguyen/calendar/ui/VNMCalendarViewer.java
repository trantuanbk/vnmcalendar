package chau.nguyen.calendar.ui;

import java.util.Date;

import chau.nguyen.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class VNMCalendarViewer extends LinearLayout {
	private DayViewer dayOfMonthViewer;
	private VietnameseDayViewer vnmDayOfMonthViewer;
	private Date displayDate;

	public VNMCalendarViewer(Context context) {
		super(context);
		init();
	}
	
	public VNMCalendarViewer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		// Inflate the view from the layout resource.
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li;
		li = (LayoutInflater)getContext().getSystemService(infService);
		li.inflate(R.layout.vnm_calendar_viewer, this, true);
		
		this.dayOfMonthViewer = (DayViewer)findViewById(R.id.dayOfMonthViewer);
		this.vnmDayOfMonthViewer = (VietnameseDayViewer)findViewById(R.id.vnmDayOfMonthViewer);
		this.displayDate = new Date();
		this.dayOfMonthViewer.setDate(this.displayDate);
		this.vnmDayOfMonthViewer.setDate(this.displayDate);
	}

}
