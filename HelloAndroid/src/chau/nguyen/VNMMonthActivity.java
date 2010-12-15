package chau.nguyen;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import chau.nguyen.calendar.ui.ScrollableMonthView;
import chau.nguyen.calendar.ui.MonthView.OnDateSelectedListener;
import chau.nguyen.calendar.ui.ScrollableMonthView.OnDateChangedListener;
import chau.nguyen.calendar.widget.HorizontalScrollView;
import chau.nguyen.calendar.widget.HorizontalScrollView.OnScreenSelectedListener;

public class VNMMonthActivity extends Activity {
	public static final String SELECTED_DATE_RETURN = "selectedDateReturn";
	private static int MENU_DAY_VIEW = 1;
	//private static int MENU_SETTINGS = 2;		
	
	private HorizontalScrollView scrollView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        BackgroundManager.init(this);
        
        this.scrollView = new HorizontalScrollView(this);
        this.setContentView(this.scrollView);
        
        this.scrollView.setOnScreenSelectedListener(new OnScreenSelectedListener() {
			public void onSelected(int selectedIndex) {				
				prepareOtherViews(selectedIndex);
			}
        });
                      
        showDate(new Date());                       
    }
    
    private void showDate(Date date) {
    	if (this.scrollView.getChildCount() > 0) {
    		this.scrollView.removeAllViews();
    	}
    	
    	ScrollableMonthView previousView = new ScrollableMonthView(this);
    	previousView.setOnDateChangedListener(onDateChangedListener);
    	previousView.setOnDateSelectedListener(onDateSelectedListener);
		previousView.setDate(addMonths(date, 1));
		previousView.setBackgroundDrawable(BackgroundManager.getRandomBackground());
		this.scrollView.addView(previousView);
		 
		ScrollableMonthView currentView = new ScrollableMonthView(this);
		currentView.setOnDateChangedListener(onDateChangedListener);
		currentView.setOnDateSelectedListener(onDateSelectedListener);
		currentView.setDate(date);
		currentView.setBackgroundDrawable(BackgroundManager.getRandomBackground());
		this.scrollView.addView(currentView);
		 
		ScrollableMonthView nextView = new ScrollableMonthView(this);
		nextView.setOnDateChangedListener(onDateChangedListener);
		nextView.setOnDateSelectedListener(onDateSelectedListener);
		nextView.setDate(addMonths(date, 1));
		nextView.setBackgroundDrawable(BackgroundManager.getRandomBackground());
		this.scrollView.addView(nextView);
		         
		this.scrollView.showScreen(1);
    }
    
    protected void prepareOtherViews(int selectedIndex) {
    	ScrollableMonthView currentView = (ScrollableMonthView)this.scrollView.getChildAt(selectedIndex);
    	Date currentDate = currentView.getDate();
    	if (selectedIndex == 0) {
    		// remove last view, add new view at the beginning
    		ScrollableMonthView previousView = new ScrollableMonthView(this);
    		previousView.setOnDateChangedListener(onDateChangedListener);
    		previousView.setOnDateSelectedListener(onDateSelectedListener);
    		previousView.setDate(addMonths(currentDate, -1));
    		previousView.setBackgroundDrawable(BackgroundManager.getRandomBackground());
    		this.scrollView.prependView(previousView);
    		
    		if (this.scrollView.getChildCount() > 2) {
    			this.scrollView.removeViewAt(2);
    		}
    	} else if (selectedIndex == 2) {
    		// remove first view, append new view at the end
    		ScrollableMonthView nextView = new ScrollableMonthView(this);
    		nextView.setOnDateChangedListener(onDateChangedListener);
    		nextView.setOnDateSelectedListener(onDateSelectedListener);
    		nextView.setDate(addMonths(currentDate, +1));
    		nextView.setBackgroundDrawable(BackgroundManager.getRandomBackground());    		
    		this.scrollView.addView(nextView);
    					
    		if (this.scrollView.getChildCount() > 3) {
    			this.scrollView.removeFirstView();
    		}
    	}
	}
    
    private Date addMonths(Date date, int months) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.add(Calendar.MONTH, months);
    	return cal.getTime();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, MENU_DAY_VIEW, 0, "Xem theo ngày").setIcon(android.R.drawable.ic_menu_day);
    	//menu.add(0, MENU_SETTINGS, 0, "Tùy chọn").setIcon(android.R.drawable.ic_menu_preferences);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == MENU_DAY_VIEW) {
    		this.finish();
    	}    	
    	return true;
    }
    
	private OnDateChangedListener onDateChangedListener = new OnDateChangedListener() {
		public void onDateChanged(Date date) {
			ScrollableMonthView previousView = (ScrollableMonthView)scrollView.getChildAt(0);
			previousView.setDate(addMonths(date, -1));

    		ScrollableMonthView nextView = (ScrollableMonthView)scrollView.getChildAt(2);
    		nextView.setDate(addMonths(date, +1));			
		}		
	};
	
	private OnDateSelectedListener onDateSelectedListener = new OnDateSelectedListener() {
		public void onDateSelected(Date date) {
			Intent data = new Intent();			
	    	data.putExtra(SELECTED_DATE_RETURN, date.getTime());
	    	setResult(RESULT_OK, data);
	    	finish();
		}		
	};
}
