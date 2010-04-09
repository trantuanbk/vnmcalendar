package chau.nguyen;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ViewSwitcher;
import android.widget.Gallery.LayoutParams;
import chau.nguyen.calendar.ui.VNMMonthViewer;

public class VNMMonthActivity extends VNMCalendarViewActivity {
	private static int MENU_DAY_VIEW = 1;
	private static int MENU_SETTINGS = 2;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vnm_month_activity);
        this.switcher = (ViewSwitcher)findViewById(R.id.monthSwitcher);
        this.switcher.setFactory(this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, MENU_DAY_VIEW, 0, "Xem theo ngày").setIcon(android.R.drawable.ic_menu_day);
    	menu.add(0, MENU_SETTINGS, 0, "Tùy chọn").setIcon(android.R.drawable.ic_menu_preferences);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == MENU_DAY_VIEW) {
    		this.finish();
    	}    	
    	return true;
    }
    
	public View makeView() {
		final VNMMonthViewer viewer = new VNMMonthViewer(this, this);
		viewer.setLayoutParams(new ViewSwitcher.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		return viewer;
	}

	public void gotoTime(Date date) {
		VNMMonthViewer currentView = (VNMMonthViewer)this.switcher.getCurrentView();
		Date currentDate = currentView.getDisplayDate();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		int currentMonth = calendar.get(Calendar.MONTH);
		int currentYear = calendar.get(Calendar.YEAR);
		calendar.setTime(date);
		int nextMonth = calendar.get(Calendar.MONTH);
		int nextYear = calendar.get(Calendar.YEAR);
		if ((nextMonth + nextYear * 12) > (currentMonth + currentYear * 12)) {
			this.switcher.setInAnimation(this.inAnimationPast);
			this.switcher.setOutAnimation(this.outAnimationPast);
		} else {
			this.switcher.setInAnimation(this.inAnimationFuture);
			this.switcher.setOutAnimation(this.outAnimationFuture);
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
