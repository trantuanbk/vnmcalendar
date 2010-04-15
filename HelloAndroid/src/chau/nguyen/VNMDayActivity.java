package chau.nguyen;

import java.util.Calendar;
import java.util.Date;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;
import android.widget.Gallery.LayoutParams;
import chau.nguyen.calendar.ui.VNMDayViewer;

public class VNMDayActivity extends VNMCalendarViewActivity {
	private static int MENU_SELECT_DATE = 1;
	private static int MENU_SELECT_TODAY = 2;
	private static int MENU_MONTH_VIEW = 3;
	private static int MENU_ADD_EVENT = 4;
	//private static int MENU_SETTINGS = 4;
	private static int MENU_ABOUT = 5;
	public static final int DATE_DIALOG_ID = 0;
	public static final int ABOUT_DIALOG = 1;
	private LinearLayout dayView;
	
	private Date date;
	
	private LinearLayout dayView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vnm_day_activity);
        
        this.date = new Date();
        this.dayView = (LinearLayout)findViewById(R.id.dayView);
        this.dayView.setBackgroundDrawable(getResources().getDrawable(R.drawable.body));
        this.switcher = (ViewSwitcher)findViewById(R.id.switcher);
        this.switcher.setFactory(this);
        
        this.dayView = (LinearLayout)findViewById(R.id.dayView);
        this.dayView.setBackgroundDrawable(getResources().getDrawable(R.drawable.body));
        
        this.inMonthAnimationPast = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
        this.outMonthAnimationPast = AnimationUtils.loadAnimation(this, R.anim.slide_up_out);
        this.inMonthAnimationFuture = AnimationUtils.loadAnimation(this, R.anim.slide_down_in);
        this.outMonthAnimationFuture = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);        
        this.inMonthAnimationFuture.setAnimationListener(this);
        this.inMonthAnimationPast.setAnimationListener(this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, MENU_SELECT_DATE, 0, "Chọn ngày").setIcon(android.R.drawable.ic_menu_day);
    	menu.add(0, MENU_SELECT_TODAY, 0, "Hôm nay").setIcon(android.R.drawable.ic_menu_today);    	
    	menu.add(0, MENU_MONTH_VIEW, 0, "Xem tháng").setIcon(android.R.drawable.ic_menu_month);
    	menu.add(0, MENU_ADD_EVENT, 0, "Thêm sự kiện").setIcon(android.R.drawable.ic_menu_add);
    	menu.add(0, MENU_ABOUT, 0, "Giới thiệu");
    	//menu.add(0, MENU_SETTINGS, 0, "Tùy chọn").setIcon(android.R.drawable.ic_menu_preferences);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == MENU_MONTH_VIEW) {
    		showMonthView();
    	} else if (item.getItemId() == MENU_SELECT_DATE) {
    		selectDate();
    	} else if (item.getItemId() == MENU_SELECT_TODAY) {
    		gotoTime(new Date());
    	} else if (item.getItemId() == MENU_ABOUT) {
    		showDialog(ABOUT_DIALOG);
    	} else if (item.getItemId() == MENU_ADD_EVENT) {
    		addEvent();
    	}
    	return true;
    }
    
	public View makeView() {
		final VNMDayViewer viewer = new VNMDayViewer(this, this);
		viewer.setLayoutParams(new ViewSwitcher.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		return viewer;
	}

	public void gotoTime(Date date) {
		VNMDayViewer currentView = (VNMDayViewer)this.switcher.getCurrentView();
		Date currentDate = currentView.getDisplayDate();
		
		if (date.after(currentDate)) {
			this.switcher.setInAnimation(this.inAnimationPast);
			this.switcher.setOutAnimation(this.outAnimationPast);
		} else if (date.before(currentDate)) {
			if (dayOfMonth == currentDayOfMonth) {
				this.switcher.setInAnimation(this.inMonthAnimationFuture);
				this.switcher.setOutAnimation(this.outMonthAnimationFuture);
			} else {
				this.switcher.setInAnimation(this.inAnimationFuture);
				this.switcher.setOutAnimation(this.outAnimationFuture);
			}
		}
		
		VNMDayViewer next = (VNMDayViewer)this.switcher.getNextView();
		next.setDate(date);
		this.date = date;
		this.switcher.showNext();
	}
	
	public void selectDate() {
		showDialog(DATE_DIALOG_ID);
	}
	
	public void showMonthView() {
		Intent monthIntent = new Intent(this, VNMMonthActivity.class);
		startActivity(monthIntent);
	}	
	
	public void addEvent() {
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setClassName("com.android.calendar", "com.android.calendar.EditEvent");
		Calendar eventCal = Calendar.getInstance();
		eventCal.setTime(this.date);
		eventCal.set(Calendar.HOUR_OF_DAY, 8);
		eventCal.set(Calendar.MINUTE, 0);
		intent.putExtra("beginTime", eventCal.getTimeInMillis());
		eventCal.set(Calendar.HOUR_OF_DAY, 9);
        intent.putExtra("endTime", eventCal.getTimeInMillis()); 
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public void onAnimationEnd(Animation animation) {
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    	case DATE_DIALOG_ID:
	    		Calendar cal = Calendar.getInstance();
	    		cal.setTime(this.date);
	    		return new VNMDatePickerDialog(this, mDateSetListener, 
	    				cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));	                    

	    }
	    return null;
	}
	
	// the callback received when the user "sets" the date in the dialog
	private VNMDatePickerDialog.OnDateSetListener mDateSetListener = new VNMDatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			VNMDayViewer currentView = (VNMDayViewer)switcher.getCurrentView();
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, monthOfYear);
			cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			currentView.setDate(cal.getTime());
		}
	};
}