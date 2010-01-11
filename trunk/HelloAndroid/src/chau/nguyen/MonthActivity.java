package chau.nguyen;

import java.util.Date;

import android.app.Activity;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewSwitcher;
import android.widget.Gallery.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;
import chau.nguyen.calendar.ui.VNMDayViewer;

public class MonthActivity extends Activity implements ViewFactory, INavigator {
	private Animation inAnimationPast;
	private Animation inAnimationFuture;
	private Animation outAnimationPast;
	private Animation outAnimationFuture;
	protected ViewSwitcher switcher;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.switcher = (ViewSwitcher)findViewById(R.id.switcher);
        this.switcher.setFactory(this);
        inAnimationPast = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
        outAnimationPast = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
        inAnimationFuture = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
        outAnimationFuture = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
        
    }
    
	public View makeView() {
		VNMDayViewer viewer = new VNMDayViewer(this, this);
		viewer.setLayoutParams(new ViewSwitcher.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return viewer;
	}

	public void gotoTime(Date date) {
		VNMDayViewer currentView = (VNMDayViewer)this.switcher.getCurrentView();
		Date currentDate = currentView.getDisplayDate();
		
		if (date.after(currentDate)) {
			switcher.setInAnimation(this.inAnimationPast);
			switcher.setOutAnimation(this.outAnimationPast);
		} else if (date.before(currentDate)) {
			switcher.setInAnimation(this.inAnimationFuture);
			switcher.setOutAnimation(this.outAnimationFuture);
		}
		
		VNMDayViewer next = (VNMDayViewer)this.switcher.getNextView();
		next.setDate(date);
		switcher.showNext();
	}
	
	
}