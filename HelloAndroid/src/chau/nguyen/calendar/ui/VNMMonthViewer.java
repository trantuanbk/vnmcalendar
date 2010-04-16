package chau.nguyen.calendar.ui;

import java.util.Calendar;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import chau.nguyen.INavigator;
import chau.nguyen.R;
import chau.nguyen.VNMMonthActivity;
import chau.nguyen.calendar.ui.MonthViewRenderer.Config;

public class VNMMonthViewer extends View {
	private static int HORIZONTAL_FLING_THRESHOLD = 5;
	private static int VERTICAL_FLING_THRESHOLD = 5;
	private Date displayDate;
	private INavigator navigator;
	private GestureDetector gestureDetector;
	private Canvas mCanvas;
	private Bitmap mBitmap;
	private boolean reDrawScreen = true;
	
	MonthViewRenderer.Config config;	
	MonthViewRenderer renderer;		
	 
	public VNMMonthViewer(VNMMonthActivity context, AttributeSet attrs, INavigator navigator) {
		super(context, attrs);
		init(context, navigator);
	}
	
	private void init(VNMMonthActivity context, INavigator navigator) {
		this.displayDate = new Date();		
		this.navigator = navigator;
		
		config = new Config();
		config.date = this.displayDate;
		config.cellBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.cell_bg);
		config.cellHeaderBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.cell_header_bg);
		config.cellHighlightBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.cell_highlight_bg);
		config.titleHeaderBackground = config.cellBackground;
		config.dayColor = context.getResources().getColor(R.color.dayColor);
		config.dayOfWeekColor = context.getResources().getColor(R.color.dayOfWeekColor);
		config.weekendColor = context.getResources().getColor(R.color.weekendColor);
		config.holidayColor = context.getResources().getColor(R.color.holidayColor);		
		renderer = new MonthViewRenderer(config);
		
		this.gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {			
			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}
			public void onShowPress(MotionEvent e) {
			}
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
					float distanceY) {
				return false;
			}
			public void onLongPress(MotionEvent e) {
			}
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // The user might do a slow "fling" after touching the screen
                // and we don't want the long-press to pop up a context menu.
                // Setting mLaunchDayView to false prevents the long-press.
                int distanceX = Math.abs((int) e2.getX() - (int) e1.getX());
                int distanceY = Math.abs((int) e2.getY() - (int) e1.getY());
                if (distanceX < HORIZONTAL_FLING_THRESHOLD && distanceY < VERTICAL_FLING_THRESHOLD) {
                    return false;
                }

                Calendar calendar = Calendar.getInstance();
    			calendar.setTime(displayDate);
    			calendar.set(Calendar.DAY_OF_MONTH, 1);
                if (distanceX > distanceY) {
	                if (velocityX < 0) {
						calendar.add(Calendar.MONTH, 1);						
					} else {
						calendar.add(Calendar.MONTH, -1);
					}
                } else {
	                if (velocityY < 0) {
						calendar.add(Calendar.YEAR, 1);
					} else {
						calendar.add(Calendar.YEAR, -1);
					}
                }                
                VNMMonthViewer.this.navigator.gotoTime(calendar.getTime());
                
                return true;
            }
			
			@Override
			public boolean onDown(MotionEvent e) {
				return true;
			}
			
		});		
	}

	public VNMMonthViewer(VNMMonthActivity context, INavigator navigator) {
		super(context);
		init(context, navigator);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (reDrawScreen) {
			if (mCanvas == null) {
				mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
				mCanvas = new Canvas(mBitmap);
			} 
			if (mCanvas != null){
				final Canvas bitmapCanvas = mCanvas;
	            bitmapCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
	            renderer.render(bitmapCanvas);
	            reDrawScreen = false;
			}
		}
		canvas.drawBitmap(mBitmap, 0, 0, null);
	}	
	
	public void animationFinished() {
		this.reDrawScreen = true;
		invalidate();
	}
	
	public void animationStart() {
		this.reDrawScreen = true;
		invalidate();
	}
	
	public void setDisplayDate(Date displayDate) {
		this.displayDate = displayDate;
		this.config.date = displayDate;
		this.invalidate();
	}
	
	public Date getDisplayDate() {
		return displayDate;
	}	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.gestureDetector.onTouchEvent(event);
		return true;
	}	
}
