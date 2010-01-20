package chau.nguyen.calendar.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Context;
import android.graphics.Canvas;
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
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int leadSpaces = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.displayDate);
		int yy = calendar.get(Calendar.YEAR);
		int mm = calendar.get(Calendar.MONTH);
		GregorianCalendar cal = new GregorianCalendar(yy, mm, 1);

		System.out.println("Su Mo Tu We Th Fr Sa");

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
	    			   if (j < leadSpaces) {
	    				   System.out.print("   ");
	    			   } else {
	    				   System.out.print(" " + count + " ");
	    				   count++;
	    			   }
	    		   }
	    		   System.out.println();
	    	   } else {
	    		   for (int j = 0; j < 7 && count <= daysInMonth; j++) {
					   if (count <= 9) {
						   System.out.print(" " + count + " ");
					   } else {
						   System.out.print(count + " ");
					   }
					   count++;
	        	   }
	    		   System.out.println();
	    	   }
	    	   
	       }
	}
	
	
	public void setDisplayDate(Date displayDate) {
		this.displayDate = displayDate;
	}
	
	public Date getDisplayDate() {
		return displayDate;
	}
}
