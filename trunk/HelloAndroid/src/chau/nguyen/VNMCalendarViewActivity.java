package chau.nguyen;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewSwitcher;
import android.widget.ViewSwitcher.ViewFactory;

public abstract class VNMCalendarViewActivity extends Activity implements ViewFactory, INavigator {
	protected Animation inAnimationPast;
	protected Animation inAnimationFuture;
	protected Animation outAnimationPast;
	protected Animation outAnimationFuture;
	protected ViewSwitcher switcher;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.inAnimationPast = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
        this.outAnimationPast = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
        this.inAnimationFuture = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
        this.outAnimationFuture = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
        	
    }

}
