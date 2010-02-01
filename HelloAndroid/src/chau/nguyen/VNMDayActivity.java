package chau.nguyen;

import java.util.Date;

import android.os.Bundle;
import android.view.View;
import android.widget.ViewSwitcher;
import android.widget.Gallery.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;
import chau.nguyen.calendar.ui.MainMenu;
import chau.nguyen.calendar.ui.VNMDayViewer;
import chau.nguyen.calendar.ui.MainMenu.SwitchViewOption;

public class VNMDayActivity extends VNMCalendarViewActivity implements ViewFactory, INavigator {
	protected MainMenu menu;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vnm_day_activity);
        this.switcher = (ViewSwitcher)findViewById(R.id.switcher);
        this.switcher.setFactory(this);
        this.menu = (MainMenu)findViewById(R.id.dayMainMenu);
        this.menu.setVnmCalendarActivity(this);
        this.menu.setSwitchOption(SwitchViewOption.SWITCH_DAY_MONTH);
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
		this.switcher.showNext();
	}
}