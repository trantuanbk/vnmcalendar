package chau.nguyen.calendar.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import chau.nguyen.INavigator;
import chau.nguyen.R;
import chau.nguyen.VNMMonthActivity;
import chau.nguyen.calendar.VietCalendar;
import chau.nguyen.calendar.VietCalendar.Holiday;

public class VNMMonthViewer extends View {
	private static int HORIZONTAL_FLING_THRESHOLD = 5;
	private static int VERTICAL_FLING_THRESHOLD = 5;
	private Date displayDate;
	private INavigator navigator;
	private GestureDetector gestureDetector;
	private VNMMonthActivity monthActivity;
	private Canvas mCanvas;
	private Bitmap mBitmap;
	private boolean reDrawScreen = true;
	
	Bitmap cellBackground;
	Bitmap cellHeaderBackground;
	Bitmap cellHighlightBackground;
	
	int dayColor = 0;
	int dayOfWeekColor = 0;
	int weekendColor = 0;
	int holidayColor = 0;
	
	private final static int dom[] = { 
		31, 28, 31, /* jan, feb, mar */
		30, 31, 30, /* apr, may, jun */
		31, 31, 30, /* jul, aug, sep */
		31, 30, 31 /* oct, nov, dec */
	};
	 
	public VNMMonthViewer(VNMMonthActivity context, AttributeSet attrs, INavigator navigator) {
		super(context, attrs);
		init(context, navigator);
	}
	
	private void init(VNMMonthActivity context, INavigator navigator) {
		this.displayDate = new Date();
		this.navigator = navigator;
		this.monthActivity = context;
		this.cellBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.cell_bg);
		this.cellHeaderBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.cell_header_bg);
		this.cellHighlightBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.cell_highlight_bg);
		this.dayColor = context.getResources().getColor(R.color.dayColor);
		this.dayOfWeekColor = context.getResources().getColor(R.color.dayOfWeekColor);
		this.weekendColor = context.getResources().getColor(R.color.weekendColor);
		this.holidayColor = context.getResources().getColor(R.color.holidayColor);
		
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

                if (distanceX > distanceY) {
	                // Switch to a different month
	                Calendar calendar = Calendar.getInstance();
	    			calendar.setTime(displayDate);
	    			calendar.set(Calendar.DAY_OF_MONTH, 1);
	    			calendar.getTime();
	                if (velocityX < 0) {
						calendar.add(Calendar.MONTH, 1);
						Date afterDate = calendar.getTime();
						VNMMonthViewer.this.navigator.gotoTime(afterDate);
					} else {
						calendar.add(Calendar.MONTH, -1);
						Date beforeDate = calendar.getTime();
						VNMMonthViewer.this.navigator.gotoTime(beforeDate);
					}
                } else {
                	Calendar calendar = Calendar.getInstance();
	    			calendar.setTime(displayDate);
	    			calendar.set(Calendar.DAY_OF_MONTH, 1);
	    			calendar.getTime();
	                if (velocityY < 0) {
						calendar.add(Calendar.YEAR, 1);
						Date afterDate = calendar.getTime();
						VNMMonthViewer.this.navigator.gotoTime(afterDate);
					} else {
						calendar.add(Calendar.YEAR, -1);
						Date beforeDate = calendar.getTime();
						VNMMonthViewer.this.navigator.gotoTime(beforeDate);
					}
                }
                // count++; why increase this counter?
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
	            doDraw(bitmapCanvas);
	            reDrawScreen = false;
			}
		}
		canvas.drawBitmap(mBitmap, 0, 0, null);
	}
	
	private void doDraw(Canvas canvas) {
		float cellHeight = getHeight() / 7;
		float cellWidth = getWidth() / 7;
		int startX = (getWidth() - (int)cellWidth * 7) / 2;
		int startY = (getHeight() - (int)cellHeight * 7) / 2;
		int leadSpaces = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.displayDate);
		int mm = calendar.get(Calendar.MONTH);
		int yy = calendar.get(Calendar.YEAR);
		this.monthActivity.setTitle("Tháng " + (mm + 1) + " - " + " Năm " + yy);
		GregorianCalendar cal = new GregorianCalendar(yy, mm, 1);
		
		// Compute how much to leave before before the first day of the month.
		// getDay() returns 0 for Sunday.
		leadSpaces = getDayOfWeekVNLocale(cal.get(Calendar.DAY_OF_WEEK)) - 1;

		// total days in month
		int daysInMonth = dom[mm];

		if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1) {
			++daysInMonth;
		}				
	       
		Calendar todayCal = Calendar.getInstance();
		int todayYear = todayCal.get(Calendar.YEAR);
		int todayMonth = todayCal.get(Calendar.MONTH);
		int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);
		
		int count = 1;	       
		for (int i = 0; i < 7; i++) {
    	   if (i == 0) {
    		   for (int j = 0; j < 7; j++) {
    			   drawHeader(canvas, startX + j * cellWidth, startY + i * cellHeight, cellWidth, cellHeight, j + 2);
    		   }
    	   } else if (i == 1) {    		   
    		   for (int j = 0; j < 7 && count <= daysInMonth; j++) {    			          		   
    			   if (j >= leadSpaces) {
    				   drawCellContent(canvas, startX + j * cellWidth, startY + i * cellHeight, cellWidth, cellHeight, count, mm + 1, yy, j, false);
    				   count++;
    			   } else {        				   
            		   boolean highlight = isSameDate(todayYear, todayMonth, todayDay, yy, mm, count);
    				   drawCellContent(canvas, startX + j * cellWidth, startY + i * cellHeight, cellWidth, cellHeight, 0, 0, yy, j, highlight);   
    			   }
    		   }
    	   } else {
    		   for (int j = 0; j < 7; j++) {
    			   if (count <= daysInMonth) {
    				   boolean highlight = isSameDate(todayYear, todayMonth, todayDay, yy, mm, count);
    				   drawCellContent(canvas, startX + j * cellWidth, startY + i * cellHeight, cellWidth, cellHeight, count, mm + 1, yy, j, highlight);
    				   count++;
    			   } else {
    				   drawCellContent(canvas, startX + j * cellWidth, startY + i * cellHeight, cellWidth, cellHeight, 0, 0, yy, j, false);
    			   }
        	   }
    	   }
		}
	}
	
	private void drawCellContent(Canvas canvas, float cellX, float cellY, float cellWidth, float cellHeight, 
			int day, int month, int year, int dayOfWeek, boolean highlight) {		
		Paint paint = new Paint();
		paint.setColor(dayColor);
		if (dayOfWeek == 6) {
			paint.setColor(weekendColor);
		}
		paint.setAntiAlias(true);
		paint.setTextAlign(Align.CENTER);	
		paint.setTextSize(25);
		paint.setDither(true);		
		
		Rect srcRect = new Rect(0, 0, 1, 1);
		Rect destRect = new Rect((int)cellX + 1, (int)cellY + 1,  (int)cellX + (int)cellWidth, (int)cellY + (int)cellHeight);
		canvas.drawBitmap((highlight ? cellHighlightBackground : cellBackground), srcRect, destRect, paint);		
		
		float x = cellX + cellWidth / 2;
		float y = cellY + cellHeight / 2;
		
		if (day > 0) {
			int[] lunars = VietCalendar.convertSolar2LunarInVietnam(day, month, year);
			Holiday holiday = VietCalendar.getHoliday(lunars[0], lunars[1], day, month);
			if (holiday != null) {
				paint.setColor(holidayColor);
			}
			canvas.drawText(day + "", x, y, paint);
			//paint.setTextAlign(Align.CENTER);
			paint.setTextSize(14);
			if (lunars[0] == 1) {
				canvas.drawText(lunars[0] + "/" + lunars[1], x + 10, y + 15, paint);
			} else {
				canvas.drawText(lunars[0] + "", x + 10, y + 15, paint);
			}
		}
	}
	
	private void drawHeader(Canvas canvas, float cellX, float cellY, float cellWidth, float cellHeight, int j) {
		Paint paint = new Paint();
		paint.setColor(dayOfWeekColor);
		paint.setTextAlign(Align.CENTER);
		paint.setAntiAlias(true);
		paint.setTextSize(25);
		paint.setDither(true);
		canvas.drawBitmap(cellHeaderBackground, new Rect(0, 0, 1, 1),
				new Rect((int)cellX + 1, (int)cellY + 1,  (int)cellX + (int)cellWidth, (int)cellY + (int)cellHeight), paint);
		
		float x = cellX + cellWidth / 2;
		float y = cellY + cellHeight / 2;
		if (j != 8) {
			canvas.drawText("T" + j, x, y, paint);
		} else {
			paint.setColor(weekendColor);
			canvas.drawText("CN", x, y, paint);
		}
		
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
		this.invalidate();
	}
	
	public Date getDisplayDate() {
		return displayDate;
	}
	
	private int getDayOfWeekVNLocale(int dayOfWeekUSLocale) {
		if (dayOfWeekUSLocale == 1) return 7;
		else return dayOfWeekUSLocale - 1;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.gestureDetector.onTouchEvent(event);
		return true;
	}

	private boolean isSameDate(int year1, int month1, int day1, int year2, int month2, int day2) {
		return year1 == year2 && month1 == month2 && day1 == day2;
	}
}
