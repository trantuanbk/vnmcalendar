package chau.nguyen;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.DatePicker;
import android.widget.ViewSwitcher;
import android.widget.Gallery.LayoutParams;
import chau.nguyen.calendar.ui.VNMDayViewer;

public class VNMDayActivity extends VNMCalendarViewActivity {
	private static int MENU_SELECT_DATE = 1;
	private static int MENU_SELECT_TODAY = 2;
	private static int MENU_MONTH_VIEW = 3;
	//private static int MENU_SETTINGS = 4;
	public static final int DATE_DIALOG_ID = 0;
	
	private Date date;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vnm_day_activity);
        this.date = new Date();
        this.switcher = (ViewSwitcher)findViewById(R.id.switcher);
        this.switcher.setFactory(this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, MENU_SELECT_DATE, 0, "Chọn ngày").setIcon(android.R.drawable.ic_menu_day);
    	menu.add(0, MENU_SELECT_TODAY, 0, "Hôm nay").setIcon(android.R.drawable.ic_menu_today);
    	menu.add(0, MENU_MONTH_VIEW, 0, "Xem tháng").setIcon(android.R.drawable.ic_menu_month);
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
			this.switcher.setInAnimation(this.inAnimationFuture);
			this.switcher.setOutAnimation(this.outAnimationFuture);
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
	    		return new DatePickerDialog(this, mDateSetListener, 
	    				cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));	                    
	    }
	    return null;
	}
	
	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
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