package chau.nguyen.calendar.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;
import android.util.Log;
import chau.nguyen.calendar.VietCalendar;
import chau.nguyen.calendar.VietCalendar.Holiday;

public class MonthViewRenderer {
	Config config;	
	
	public final static int dom[] = { 
		31, 28, 31, /* jan, feb, mar */
		30, 31, 30, /* apr, may, jun */
		31, 31, 30, /* jul, aug, sep */
		31, 30, 31 /* oct, nov, dec */
	};	
	
	private final static String dow[] = {
		"Hai", "Ba", "Tư", "Năm", "Sáu", "Bảy", "CN"
	};
	
	public MonthViewRenderer(Config config) {
		this.config = config;
	}
	
	public void render(Canvas canvas) {	
		float cellWidth = canvas.getWidth() / 7;
		float cellHeight = canvas.getHeight() / 8;
		int startX = (canvas.getWidth() - (int)cellWidth * 7) / 2;
		int startY = (canvas.getHeight() - (int)cellHeight * 8) / 2;
		int leadSpaces = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(config.date);
		int mm = calendar.get(Calendar.MONTH);
		int yy = calendar.get(Calendar.YEAR);
		
		Log.d("DEBUG", "Cell size: " + cellWidth + " x " + cellHeight);
		
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
		for (int i = 0; i < 8; i++) {
			if (i == 0) {
    		   drawTitle(canvas, startX, startY, cellWidth * 7, cellHeight, mm, yy);
			} else if (i == 1) {
    		   for (int j = 0; j < 7; j++) {
    			   drawHeader(canvas, startX + j * cellWidth, startY + i * cellHeight, cellWidth, cellHeight, j);
    		   }
			} else if (i == 2) {    		   
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
	
	private void drawTitle(Canvas canvas, float cellX, float cellY, float cellWidth, float cellHeight, int month, int year) {
		Paint paint = new Paint();
		paint.setColor(config.dayOfWeekColor);
		paint.setTextAlign(Align.CENTER);
		paint.setAntiAlias(true);
		paint.setTextSize(25);		
		if (config.titleHeaderBackground != null) {
			paint.setDither(true);
			canvas.drawBitmap(config.titleHeaderBackground, new Rect(0, 0, config.titleHeaderBackground.getWidth(), config.titleHeaderBackground.getHeight()),
					new Rect((int)cellX + 1, (int)cellY + 1,  (int)cellX + (int)cellWidth, (int)cellY + (int)cellHeight), paint);
		}
		Rect textBounds = new Rect();
		paint.getTextBounds("Tháng", 0, 1, textBounds);
		float x = cellX + cellWidth / 2;
		float y = cellY + cellHeight - (cellHeight - textBounds.height()) / 2;		
		canvas.drawText("Tháng " + (month + 1) + " - " + year, x, y, paint);
	}
	
	private void drawHeader(Canvas canvas, float cellX, float cellY, float cellWidth, float cellHeight, int j) {
		Paint paint = new Paint();
		paint.setColor(config.dayOfWeekColor);
		paint.setTextAlign(Align.CENTER);
		paint.setAntiAlias(true);
		paint.setTextSize(18);
		paint.setDither(true);
		canvas.drawBitmap(config.cellHeaderBackground, new Rect(0, 0, config.cellHeaderBackground.getWidth(), config.cellHeaderBackground.getHeight()),
				new Rect((int)cellX + 1, (int)cellY + 1,  (int)cellX + (int)cellWidth, (int)cellY + (int)cellHeight), paint);
		
		Rect textBounds = new Rect();
		paint.getTextBounds("Bảy", 0, 2, textBounds);
		
		float x = cellX + cellWidth / 2;
		float y = cellY + cellHeight - (cellHeight - textBounds.height()) / 2;
		if (j == 6) {
			paint.setColor(config.weekendColor);
		}	
		canvas.drawText(dow[j], x, y, paint);
	}
	
	private void drawCellContent(Canvas canvas, float cellX, float cellY, float cellWidth, float cellHeight, 
			int day, int month, int year, int dayOfWeek, boolean highlight) {		
		Paint paint = new Paint();
		paint.setColor(config.dayColor);
		if (dayOfWeek == 6) {
			paint.setColor(config.weekendColor);
		}
		paint.setAntiAlias(true);
		paint.setTextAlign(Align.CENTER);	
		paint.setTextSize(25);
		paint.setDither(true);		
		
		Bitmap bitmap = highlight ? config.cellHighlightBackground : config.cellBackground;
		Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		Rect destRect = new Rect((int)cellX + 1, (int)cellY + 1,  (int)cellX + (int)cellWidth, (int)cellY + (int)cellHeight);
		canvas.drawBitmap(bitmap, srcRect, destRect, paint);		
		
		float x = cellX + cellWidth / 2;
		float y = cellY + cellHeight / 2;
		
		if (day > 0) {
			int[] lunars = VietCalendar.convertSolar2LunarInVietnam(day, month, year);
			Holiday holiday = VietCalendar.getHoliday(lunars[0], lunars[1], day, month);
			if (holiday != null) {
				paint.setColor(config.holidayColor);
			}
			canvas.drawText(day + "", x, y, paint);
			paint.setTextSize(14);
			if (lunars[0] == 1) {
				canvas.drawText(lunars[0] + "/" + lunars[1], x + 10, y + 15, paint);
			} else {
				canvas.drawText(lunars[0] + "", x + 10, y + 15, paint);
			}
		}
	}		
	
	public static int getDayOfWeekVNLocale(int dayOfWeekUSLocale) {
		if (dayOfWeekUSLocale == 1) return 7;
		else return dayOfWeekUSLocale - 1;
	}

	public static boolean isSameDate(int year1, int month1, int day1, int year2, int month2, int day2) {
		return year1 == year2 && month1 == month2 && day1 == day2;
	}	
	
	public static class Config {
		public Date date;
		public Date selectedDate;
		
		public Bitmap titleHeaderBackground;
		public Bitmap cellBackground;
		public Bitmap cellHeaderBackground;
		public Bitmap cellHighlightBackground;
		public Drawable selectedCellDrawable;
		public int dayColor = 0;
		public int dayOfWeekColor = 0;
		public int weekendColor = 0;
		public int holidayColor = 0;
		
		public Config() {			
		}
	}	
}
