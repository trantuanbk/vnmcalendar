package chau.nguyen.calendar.ui;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import chau.nguyen.BackgroundManager;
import chau.nguyen.calendar.widget.VerticalScrollView;

public class ScrollableDayView extends VerticalScrollView {
	private OnDateChangedListener onDateChangedListener;
	
	public ScrollableDayView(Context context) {
		super(context);		
		this.setOnScreenSelectedListener(onScreenSelectedListener);
	}
	
	public void setOnDateChangedListener(OnDateChangedListener onDateChangedListener) {
		this.onDateChangedListener = onDateChangedListener;
	}

	public void setDate(Date date) {
		if (this.getChildCount() == 3) {											
			DayView previousView = (DayView)getChildAt(0);
			previousView.setDate(addMonths(date, -1));
			
			DayView currentView = (DayView)getChildAt(1);
			currentView.setDate(date);
			
			DayView nextView = (DayView)getChildAt(2);
			nextView.setDate(addMonths(date, 1));
    	} else {
    		this.removeAllViews();
    		
	    	DayView previousView = new DayView(getContext());
			previousView.setDate(addMonths(date, 1));
			previousView.setBackgroundDrawable(BackgroundManager.getRandomBackground());
			this.addView(previousView);
			 
			DayView currentView = new DayView(getContext());
			currentView.setDate(date);
			currentView.setBackgroundDrawable(BackgroundManager.getRandomBackground());
			this.addView(currentView);
			 
			DayView nextView = new DayView(getContext());
			nextView.setDate(addMonths(date, 1));
			nextView.setBackgroundDrawable(BackgroundManager.getRandomBackground());
			this.addView(nextView);
    	}
				
		this.setOnScreenSelectedListener(null);
		this.showScreen(1);
		this.setOnScreenSelectedListener(onScreenSelectedListener);
	}
	
	public Date getDate() {
		DayView currentView = (DayView)getChildAt(1);
		return currentView.getDisplayDate();
	}
	
	protected void prepareOtherViews(int selectedIndex) {
    	DayView currentView = (DayView)this.getChildAt(selectedIndex);
    	Date currentDate = currentView.getDisplayDate();    	
    	if (selectedIndex == 0) {
    		// remove last view, add new view at the beginning
    		DayView previousView = new DayView(getContext());
    		previousView.setDate(addMonths(currentDate, -1));
    		previousView.setBackgroundDrawable(BackgroundManager.getRandomBackground());
    		this.prependView(previousView);    	

    		if (this.getChildCount() > 2) {
    			this.removeViewAt(2);
    		}
    	} else if (selectedIndex == 2) {    		
    		// remove first view, append new view at the end
    		DayView nextView = new DayView(getContext());
    		nextView.setDate(addMonths(currentDate, +1));
    		nextView.setBackgroundDrawable(BackgroundManager.getRandomBackground());    		
    		this.addView(nextView);
    		
    		if (this.getChildCount() > 3) {
    			this.removeFirstView();
    		}
    	}
    	
    	if (this.onDateChangedListener != null) {
    		this.onDateChangedListener.onDateChanged(currentDate);
    	}
	}
	
	private Date addMonths(Date date, int months) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.add(Calendar.MONTH, months);
    	return cal.getTime();
    }
	
	private OnScreenSelectedListener onScreenSelectedListener = new OnScreenSelectedListener() {
		public void onSelected(int selectedIndex) {				
			prepareOtherViews(selectedIndex);
		}
    };
	
	public interface OnDateChangedListener {
		void onDateChanged(Date date);
	}
}
