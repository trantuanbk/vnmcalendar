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
import chau.nguyen.EventManager;
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
	private VNMMonthActivity monthActivity;
	private boolean reDrawScreen = true;
	private EventManager eventManager;
	
	MonthViewRenderer.Config config;	
	MonthViewRenderer renderer;		
	 
	public VNMMonthViewer(VNMMonthActivity context, AttributeSet attrs, INavigator navigator) {
		super(context, attrs);
		init(context, navigator);
	}
	
	private void init(VNMMonthActivity context, INavigator navigator) {
		this.monthActivity = context;
		this.eventManager = new EventManager(this.monthActivity.getContentResolver());
		this.displayDate = new Date();		
		this.navigator = navigator;
		
		config = new Config();
		config.date = this.displayDate;
		config.cellBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.cell_bg);
		config.cellEventBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.event_cell_bg);
		config.headerBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.cell_header_bg);
		config.cellHighlightBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.cell_highlight_bg);
		config.titleBackground = config.cellBackground;
		config.titleTextColor = context.getResources().getColor(R.color.titleColor);
		config.headerTextColor = context.getResources().getColor(R.color.headerColor);
		config.dayColor = context.getResources().getColor(R.color.dayColor);
		config.todayColor = config.dayColor;
		config.dayOfWeekColor = context.getResources().getColor(R.color.dayOfWeekColor);
		config.weekendColor = context.getResources().getColor(R.color.weekendColor);
		config.holidayColor = context.getResources().getColor(R.color.holidayColor);
		config.otherDayColor = context.getResources().getColor(R.color.otherDayColor);		
		renderer = new MonthViewRenderer(config, this.eventManager);
		
		this.setDrawingCacheEnabled(true);
		
		this.gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {			
			public boolean onSingleTapUp(MotionEvent e) {
				float x = e.getX();
				float y = e.getY();
				int selectedDayOfMonth = guessDaySelected(x, y);
				if (selectedDayOfMonth == 0) return true;
				Calendar cal = Calendar.getInstance();
				cal.setTime(displayDate);
				cal.set(Calendar.DAY_OF_MONTH, selectedDayOfMonth);
				Date selectedDate = cal.getTime();
				VNMMonthViewer.this.monthActivity.showDateInDayView(selectedDate);
				return true;
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
                VNMMonthViewer.this.navigator.gotoDate(calendar.getTime());
                
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
	
	private int guessDaySelected(float x, float y) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(displayDate);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.getTime();
		float cellHeight = getHeight() / 8;
		float cellWidth = getWidth() / 7;
		int xOrder = (int) (x / cellWidth) + 1;
		int yOrder = (int) (y / cellHeight) - 2; // title & day of week row
		int leadSpaces = MonthViewRenderer.getDayOfWeekVNLocale(cal.get(Calendar.DAY_OF_WEEK)) - 1;
		int dayOfMonth = 7 * yOrder + xOrder - leadSpaces;
		int daysInMonth = MonthViewRenderer.dom[cal.get(Calendar.MONTH)];
		if (cal.get(Calendar.YEAR) % 4  == 0) dayOfMonth++;
		if (dayOfMonth <= 0 || dayOfMonth > daysInMonth)
			return 0;
		else 
			return dayOfMonth;
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
	}
	
	public void animationStart() {
		this.reDrawScreen = true;
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
