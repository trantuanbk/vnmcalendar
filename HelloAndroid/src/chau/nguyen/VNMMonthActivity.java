package chau.nguyen;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.ViewSwitcher;
import android.widget.Gallery.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;
import chau.nguyen.calendar.ui.MainMenu;
import chau.nguyen.calendar.ui.VNMMonthViewer;
import chau.nguyen.calendar.ui.MainMenu.SwitchViewOption;

public class VNMMonthActivity extends VNMCalendarViewActivity implements ViewFactory, INavigator {
	private MainMenu menu;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vnm_month_activity);
        this.switcher = (ViewSwitcher)findViewById(R.id.monthSwitcher);
        this.switcher.setFactory(this);
        this.menu = (MainMenu)findViewById(R.id.monthMainMenu);
        this.menu.setVnmCalendarActivity(this);
        this.menu.setSwitchOption(SwitchViewOption.SWITCH_MONTH_DAY);
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

}
