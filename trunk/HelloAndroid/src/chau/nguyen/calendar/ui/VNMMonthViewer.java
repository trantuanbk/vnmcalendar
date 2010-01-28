package chau.nguyen.calendar.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import chau.nguyen.INavigator;
import chau.nguyen.VNMMonthViewActivity;
import chau.nguyen.calendar.VietCalendar;

public class VNMMonthViewer extends View {
	private static int VERTICAL_FLING_THRESHOLD = 5;
	private Date displayDate;
	private INavigator navigator;
	private GestureDetector gestureDetector;
	private VNMMonthViewActivity monthActivity;
	
	private final static int dom[] = { 
		31, 28, 31, /* jan, feb, mar */
		30, 31, 30, /* apr, may, jun */
		31, 31, 30, /* jul, aug, sep */
		31, 30, 31 /* oct, nov, dec */
	};
	 
	public VNMMonthViewer(VNMMonthViewActivity context, AttributeSet attrs, INavigator navigator) {
		super(context, attrs);
		init(context, navigator);
	}
	
	private void init(VNMMonthViewActivity context, INavigator navigator) {
		this.displayDate = new Date();
		this.navigator = navigator;
		this.monthActivity = context;
		this.gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
			
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
					float distanceY) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent e) {
				//performLongClick();
			}
			
			@Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                    float velocityY) {
                // The user might do a slow "fling" after touching the screen
                // and we don't want the long-press to pop up a context menu.
                // Setting mLaunchDayView to false prevents the long-press.
                int distanceX = Math.abs((int) e2.getX() - (int) e1.getX());
                int distanceY = Math.abs((int) e2.getY() - (int) e1.getY());
                if (distanceX < VERTICAL_FLING_THRESHOLD || distanceX < distanceY) {
                    return false;
                }

                // Switch to a different month
                Calendar calendar = Calendar.getInstance();
    			calendar.setTime(displayDate);
                if (velocityX < 0) {
					calendar.add(Calendar.MONTH, 1);
					Date afterDate = calendar.getTime();
					VNMMonthViewer.this.navigator.gotoTime(afterDate);
				} else {
					calendar.add(Calendar.MONTH, -1);
					Date beforeDate = calendar.getTime();
					VNMMonthViewer.this.navigator.gotoTime(beforeDate);
				}

                return true;
            }
			
			@Override
			public boolean onDown(MotionEvent e) {
				return true;
			}
		});
	}

	public VNMMonthViewer(VNMMonthViewActivity context, INavigator navigator) {
		super(context);
		init(context, navigator);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float cellHeight = getHeight() / 7;
		float cellWidth = getWidth() / 7;
		int leadSpaces = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.displayDate);
		int mm = calendar.get(Calendar.MONTH);
		int yy = calendar.get(Calendar.YEAR);
		this.monthActivity.setTitle("Tháng " + (mm + 1));
		GregorianCalendar cal = new GregorianCalendar(yy, mm, 1);
		
		// Compute how much to leave before before the first day of the month.
		// getDay() returns 0 for Sunday.
		leadSpaces = getDayOfWeekVNLocale(cal.get(Calendar.DAY_OF_WEEK)) - 1;

		// total days in month
		int daysInMonth = dom[mm];

		if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1) {
			++daysInMonth;
		}
	       
		int count = 1;
	       for (int i = 0; i < 6 && count <= daysInMonth; i++) {
	    	   if (i == 0) {
	    		   for (int j = 0; j < 7; j++) {
	    			   drawHeader(canvas, j * cellWidth + cellWidth / 2, i * cellHeight + cellHeight / 2, j + 2);
	    		   }
	    	   } else if (i == 1) {
	    		   for (int j = 0; j < 7 && count <= daysInMonth; j++) {
	    			   if (j >= leadSpaces) {
	    				   drawCellContent(canvas, j * cellWidth + cellWidth / 2, i * cellHeight + cellHeight / 2, count, mm + 1, yy);
	    				   count++;
	    			   }
	    		   }
	    	   } else {
	    		   for (int j = 0; j < 7 && count <= daysInMonth; j++) {
	    			   drawCellContent(canvas, j * cellWidth + cellWidth / 2, i * cellHeight + cellHeight / 2, count, mm + 1, yy);
					   count++;
	        	   }
	    	   }
	    	   
	       }
	}
	
	private void drawCellContent(Canvas canvas, float x, float y, int day, int month, int year) {
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(25);
		canvas.drawText(day + "", x, y, paint);
		
		int[] lunars = VietCalendar.convertSolar2LunarInVietnam(day, month, year);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(16);
		if (lunars[0] == 1) {
			canvas.drawText(lunars[0] + "/" + lunars[1], x + 10, y + 15, paint);
		} else {
			canvas.drawText(lunars[0] + "", x + 10, y + 15, paint);
		}
		
	}
	
	private void drawHeader(Canvas canvas, float x, float y, int j) {
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(25);
		if (j != 8) {
			canvas.drawText("T" + j, x, y, paint);
		} else {
			paint.setColor(Color.RED);
			canvas.drawText("CN", x, y, paint);
		}
		
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

}
