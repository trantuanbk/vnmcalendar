package chau.nguyen;

import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;
import android.widget.Gallery.LayoutParams;
import chau.nguyen.calendar.ui.VNMMonthViewer;

public class VNMMonthActivity extends VNMCalendarViewActivity {
	public static final String SELECTED_DATE_RETURN = "selectedDateReturn";
	private static int MENU_DAY_VIEW = 1;
	//private static int MENU_SETTINGS = 2;
	
	protected Animation inYearAnimationPast;
	protected Animation inYearAnimationFuture;
	protected Animation outYearAnimationPast;
	protected Animation outYearAnimationFuture;
	private LinearLayout monthView;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vnm_month_activity);
        this.switcher = (ViewSwitcher)findViewById(R.id.monthSwitcher);
        this.switcher.setFactory(this);
        this.monthView = (LinearLayout)findViewById(R.id.monthView);
        this.monthView.setBackgroundResource(BackgroundManager.getRandomBackgroundId());
        
        this.inYearAnimationPast = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
        this.outYearAnimationPast = AnimationUtils.loadAnimation(this, R.anim.slide_up_out);
        this.inYearAnimationFuture = AnimationUtils.loadAnimation(this, R.anim.slide_down_in);
        this.outYearAnimationFuture = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);        
        this.inYearAnimationFuture.setAnimationListener(this);
        this.inYearAnimationPast.setAnimationListener(this);
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
    
	public View makeView() {
		final VNMMonthViewer viewer = new VNMMonthViewer(this, this);
		viewer.setLayoutParams(new ViewSwitcher.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		return viewer;
	}

	public void gotoTime(Date date) {
		this.monthView.setBackgroundResource(BackgroundManager.getRandomBackgroundId());
		
		VNMMonthViewer currentView = (VNMMonthViewer)this.switcher.getCurrentView();
		Date currentDate = currentView.getDisplayDate(); 
		if (date.after(currentDate)) {
			if (date.getMonth() == currentDate.getMonth()) {
				this.switcher.setInAnimation(this.inYearAnimationPast);
				this.switcher.setOutAnimation(this.outYearAnimationPast);
			} else {
				this.switcher.setInAnimation(this.inAnimationPast);
				this.switcher.setOutAnimation(this.outAnimationPast);
			}
		} else {
			if (date.getMonth() == currentDate.getMonth()) {
				this.switcher.setInAnimation(this.inYearAnimationFuture);
				this.switcher.setOutAnimation(this.outYearAnimationFuture);
			} else {
				this.switcher.setInAnimation(this.inAnimationFuture);
				this.switcher.setOutAnimation(this.outAnimationFuture);
			}
		}
		
		VNMMonthViewer next = (VNMMonthViewer)this.switcher.getNextView();
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
