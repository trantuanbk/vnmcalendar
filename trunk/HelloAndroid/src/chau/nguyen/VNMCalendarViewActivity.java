package chau.nguyen;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.Animation.AnimationListener;
import android.widget.ViewSwitcher;
import chau.nguyen.interpolator.QuartInterpolator;
import chau.nguyen.interpolator.EasingType.Type;

public abstract class VNMCalendarViewActivity extends Activity implements INavigator, AnimationListener {
	protected Animation inAnimationLeft;
	protected Animation inAnimationRight;
	protected Animation outAnimationLeft;
	protected Animation outAnimationRight;
	protected Animation inAnimationUp;
	protected Animation inAnimationDown;
	protected Animation outAnimationUp;
	protected Animation outAnimationDown;	
	
	protected Interpolator interpolator;
	protected ViewSwitcher switcher;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        interpolator = new QuartInterpolator(Type.OUT);
        
        this.inAnimationLeft = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
        this.outAnimationLeft = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
        this.inAnimationRight = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
        this.outAnimationRight = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
        this.inAnimationUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
        this.outAnimationUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_out);
        this.inAnimationDown = AnimationUtils.loadAnimation(this, R.anim.slide_down_in);
        this.outAnimationDown = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);        
        
        this.inAnimationLeft.setInterpolator(interpolator);
        this.outAnimationLeft.setInterpolator(interpolator);
        this.inAnimationRight.setInterpolator(interpolator);
        this.outAnimationRight.setInterpolator(interpolator);
        this.inAnimationUp.setInterpolator(interpolator);
        this.outAnimationUp.setInterpolator(interpolator);
        this.inAnimationDown.setInterpolator(interpolator);
        this.outAnimationDown.setInterpolator(interpolator);
        
        this.inAnimationRight.setAnimationListener(this);
        this.inAnimationLeft.setAnimationListener(this);
        this.inAnimationDown.setAnimationListener(this);
        this.inAnimationUp.setAnimationListener(this);
    }
}
