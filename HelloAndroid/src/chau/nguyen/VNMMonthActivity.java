package chau.nguyen;

import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.ViewSwitcher;
import chau.nguyen.calendar.ui.VNMMonthViewer;

public class VNMMonthActivity extends VNMCalendarViewActivity {
	public static final String SELECTED_DATE_RETURN = "selectedDateReturn";
	private static int MENU_DAY_VIEW = 1;
	//private static int MENU_SETTINGS = 2;		
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        BackgroundManager.init(this);
        
        this.switcher = new ViewSwitcher(this);
        setContentView(this.switcher);
        
        this.switcher.addView(new VNMMonthViewer(this, this));
        this.switcher.getCurrentView().setBackgroundDrawable(BackgroundManager.getRandomBackground());
        this.switcher.addView(new VNMMonthViewer(this, this));
        this.inAnimationLeft.initialize(this.switcher.getWidth(), this.switcher.getHeight(), this.switcher.getWidth(), this.switcher.getHeight());
        this.inAnimationRight.initialize(this.switcher.getWidth(), this.switcher.getHeight(), this.switcher.getWidth(), this.switcher.getHeight());
        this.outAnimationLeft.initialize(this.switcher.getWidth(), this.switcher.getHeight(), this.switcher.getWidth(), this.switcher.getHeight());
        this.outAnimationRight.initialize(this.switcher.getWidth(), this.switcher.getHeight(), this.switcher.getWidth(), this.switcher.getHeight());                       
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
    
    public void showDateInDayView(Date date) {
    	Intent data = new Intent();
    	data.putExtra(SELECTED_DATE_RETURN, date.getTime());
    	setResult(RESULT_OK, data);
    	this.finish();
    	//this.finishActivity(VNMDayActivity.SELECT_DATE);
    }    

	public void gotoTime(Date date) {		
		VNMMonthViewer currentView = (VNMMonthViewer)this.switcher.getCurrentView();
		Date currentDate = currentView.getDisplayDate(); 
		if (date.after(currentDate)) {
			if (date.getMonth() == currentDate.getMonth()) {
				this.switcher.setInAnimation(this.inAnimationUp);
				this.switcher.setOutAnimation(this.outAnimationUp);
			} else {
				this.switcher.setInAnimation(this.inAnimationLeft);
				this.switcher.setOutAnimation(this.outAnimationLeft);
			}
		} else {
			if (date.getMonth() == currentDate.getMonth()) {
				this.switcher.setInAnimation(this.inAnimationDown);
				this.switcher.setOutAnimation(this.outAnimationDown);
			} else {
				this.switcher.setInAnimation(this.inAnimationRight);
				this.switcher.setOutAnimation(this.outAnimationRight);
			}
		}
		
		VNMMonthViewer next = (VNMMonthViewer)this.switcher.getNextView();
		next.setBackgroundDrawable(BackgroundManager.getRandomBackground());
		next.setDisplayDate(date);
		this.switcher.showNext();
	}

	@Override
	public void onAnimationEnd(Animation animation) {		
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	@Override
	public void onAnimationStart(Animation animation) {
		VNMMonthViewer currentView = (VNMMonthViewer)this.switcher.getCurrentView();
		currentView.animationStart();		
	}
}
