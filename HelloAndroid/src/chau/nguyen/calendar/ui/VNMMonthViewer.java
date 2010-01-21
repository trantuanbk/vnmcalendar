package chau.nguyen.calendar.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import chau.nguyen.calendar.VietCalendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.View;

public class VNMMonthViewer extends View {
	private Date displayDate;
	
	private final static int dom[] = { 
		31, 28, 31, /* jan, feb, mar */
		30, 31, 30, /* apr, may, jun */
		31, 31, 30, /* jul, aug, sep */
		31, 30, 31 /* oct, nov, dec */
	};
	 
	public VNMMonthViewer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public VNMMonthViewer(Context context) {
		super(context);
		init();
	}

	public void init() {
		this.displayDate = new Date();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float cellHeight = getHeight() / 7;
		float cellWidth = getWidth() / 7;
		int leadSpaces = 0;
		GregorianCalendar cal = new GregorianCalendar(new Locale("vi_VN"));
		cal.setTime(this.displayDate);
		int mm = cal.get(Calendar.MONTH);
		int yy = cal.get(Calendar.YEAR);
		// Compute how much to leave before before the first day of the month.
		// getDay() returns 0 for Sunday.
		leadSpaces = cal.get(Calendar.DAY_OF_WEEK) - 1;

		// total days in month
		int daysInMonth = dom[mm];

		if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1) {
			++daysInMonth;
		}
	       
		int count = 1;
	       for (int i = 0; i < 6 && count <= daysInMonth; i++) {
	    	   if (i == 0) {
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
	
	
	public void setDisplayDate(Date displayDate) {
		this.displayDate = displayDate;
	}
	
	public Date getDisplayDate() {
		return displayDate;
	}
}
