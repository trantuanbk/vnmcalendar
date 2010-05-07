package chau.nguyen.calendar.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;
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
		if (config.autoCalculateOffsets) {
			config.calculate(canvas.getWidth(), canvas.getHeight());
		}
		int leadSpaces = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(config.date);
		int mm = calendar.get(Calendar.MONTH);
		int yy = calendar.get(Calendar.YEAR);		
		
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
    		   drawTitle(canvas, config.titleOffsetX, config.titleOffsetY, config.titleWidth, config.titleHeight, mm, yy);
			} else if (i == 1) {
			   if (config.renderHeader) {
	    		   for (int j = 0; j < 7; j++) {
	    			   drawHeader(canvas, config.headerOffsetX + j * config.headerWidth, config.headerOffsetY, config.headerWidth, config.headerHeight, j);
	    		   }
			   }
			} else if (i == 2) {    		   
    		   for (int j = 0; j < 7 && count <= daysInMonth; j++) {    			          		   
    			   if (j >= leadSpaces) {
    				   boolean highlight = isSameDate(todayYear, todayMonth, todayDay, yy, mm, count);
    				   drawCellContent(canvas, config.cellOffsetX + j * config.cellWidth, config.cellOffsetY + (i - 2) * config.cellHeight, config.cellWidth, config.cellHeight, count, mm + 1, yy, j, highlight);
    				   count++;
    			   } else {        				               		   
    				   drawCellContent(canvas, config.cellOffsetX + j * config.cellWidth, config.cellOffsetY + (i - 2) * config.cellHeight, config.cellWidth, config.cellHeight, 0, 0, yy, j, false);   
    			   }
    		   }
			} else {
    		   for (int j = 0; j < 7; j++) {
    			   if (count <= daysInMonth) {
    				   boolean highlight = isSameDate(todayYear, todayMonth, todayDay, yy, mm, count);
    				   drawCellContent(canvas, config.cellOffsetX + j * config.cellWidth, config.cellOffsetY + (i - 2) * config.cellHeight, config.cellWidth, config.cellHeight, count, mm + 1, yy, j, highlight);    				   
    				   count++;
    			   } else {
    				   drawCellContent(canvas, config.cellOffsetX + j * config.cellWidth, config.cellOffsetY + (i - 2) * config.cellHeight, config.cellWidth, config.cellHeight, 0, 0, yy, j, false);
    			   }
        	   }
			}
		}
	}
	
	private void drawTitle(Canvas canvas, int cellX, int cellY, int cellWidth, int cellHeight, int month, int year) {
		Paint paint = new Paint();
		paint.setShadowLayer(1, 0, 0, Color.GRAY);
		paint.setColor(config.titleTextColor);
		paint.setTextAlign(Align.CENTER);
		paint.setAntiAlias(true);
		paint.setTextSize(config.titleTextSize);		
		if (config.titleBackground != null) {
			paint.setDither(true);
			canvas.drawBitmap(config.titleBackground, new Rect(0, 0, config.titleBackground.getWidth(), config.titleBackground.getHeight()),
					new Rect(cellX + 1, cellY + 1, cellX + cellWidth, cellY + cellHeight), paint);
		}
		Rect textBounds = new Rect();
		paint.getTextBounds("Tháng", 0, 1, textBounds);
		float x = cellX + cellWidth / 2;
		float y = cellY + cellHeight - (cellHeight - textBounds.height()) / 2;		
		canvas.drawText("Tháng " + (month + 1) + " - " + year, x, y, paint);
	}
	
	private void drawHeader(Canvas canvas, int cellX, int cellY, int cellWidth, int cellHeight, int j) {
		Paint paint = new Paint();
		paint.setShadowLayer(1, 0, 0, Color.GRAY);
		paint.setColor(config.headerTextColor);
		paint.setTextAlign(Align.CENTER);
		paint.setAntiAlias(true);
		paint.setTextSize(config.headerTextSize);
		if (config.headerBackground != null) {
			paint.setDither(true);
			canvas.drawBitmap(config.headerBackground, new Rect(0, 0, config.headerBackground.getWidth(), config.headerBackground.getHeight()),
				new Rect(cellX + 1, cellY + 1, cellX + cellWidth, cellY + cellHeight), paint);
		}
		Rect textBounds = new Rect();
		paint.getTextBounds("Bảy", 0, 2, textBounds);
		
		float x = cellX + cellWidth / 2;
		float y = cellY + cellHeight - (cellHeight - textBounds.height()) / 2;
		if (j == 6) {
			paint.setColor(config.weekendColor);
		}	
		canvas.drawText(dow[j], x, y, paint);
	}
	
	private void drawCellContent(Canvas canvas, int cellX, int cellY, int cellWidth, int cellHeight, 
			int day, int month, int year, int dayOfWeek, boolean highlight) {		
		Paint paint = new Paint();
		paint.setShadowLayer(1, 0, 0, Color.GRAY);
		paint.setColor(config.dayColor);		
		if (dayOfWeek == 6) {
			paint.setColor(config.weekendColor);
		}
		paint.setAntiAlias(true);
		paint.setTextAlign(Align.CENTER);		
		paint.setTextSize(config.cellMainTextSize);
		paint.setDither(true);		
		
		Bitmap bitmap = highlight ? config.cellHighlightBackground : config.cellBackground;
		if (bitmap != null) {
			Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			Rect destRect = new Rect(cellX + 1, cellY + 1,  cellX + cellWidth, cellY + cellHeight);
			canvas.drawBitmap(bitmap, srcRect, destRect, paint);
		}
		
		float x = cellX + cellWidth / 2;
		float y = cellY + cellHeight / 2;
		
		if (day > 0) {
			int[] lunars = VietCalendar.convertSolar2LunarInVietnam(day, month, year);
			Holiday holiday = VietCalendar.getHoliday(lunars[VietCalendar.DAY], lunars[VietCalendar.MONTH], day, month);
			if (holiday != null) {
				paint.setColor(config.holidayColor);
			}
			String dayText = "" + day;
			canvas.drawText(dayText, x, y, paint);
			
//			Paint strokePaint = new Paint();
//		    strokePaint.setColor(Color.WHITE);
//		    strokePaint.setAntiAlias(true);
//		    strokePaint.setTextAlign(Align.CENTER);
//		    strokePaint.setTextSize(config.cellMainTextSize);		    
//		    strokePaint.setStyle(Paint.Style.STROKE);
//		    strokePaint.setStrokeWidth(2);
//		    canvas.drawText(dayText, x, y, strokePaint);
		    
		    canvas.drawText(dayText, x, y, paint);
			//Path path = new Path();
			//paint.getTextPath(dayText, 0, dayText.length(), x, y, path);
			//paint.setColor(Color.WHITE);
			//canvas.drawPath(path, paint);
			
			paint.setColor(config.dayColor);
			paint.setTextSize(config.cellSubTextSize);
			paint.setTextAlign(Align.RIGHT);
			if (lunars[0] == 1) {
				canvas.drawText(lunars[VietCalendar.DAY] + "/" + lunars[VietCalendar.MONTH], cellX + cellWidth - 5, y + config.cellSubTextSize + 2, paint);
			} else {
				canvas.drawText(lunars[VietCalendar.DAY] + "", cellX + cellWidth - 5, y + config.cellSubTextSize + 2, paint);
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
		public boolean autoCalculateOffsets = true;
		
		public int width;
		public int height;
		
		public int titleOffsetX;
		public int titleOffsetY;
		public int titleWidth;
		public int titleHeight;
		public int titleTextSize = 25;
		public int titleTextColor = 0;
		public Bitmap titleBackground;
		
		public boolean renderHeader = true;
		public int headerOffsetX;
		public int headerOffsetY;
		public int headerWidth;
		public int headerHeight;
		public int headerTextSize = 18;
		public int headerTextColor = 0;
		public Bitmap headerBackground;
		
		public int cellOffsetX;
		public int cellOffsetY;
		public int cellWidth;
		public int cellHeight;
		public int cellMainTextSize = 25;
		public int cellSubTextSize = 14;
		public Bitmap cellBackground;
		public Bitmap cellHighlightBackground;
		public Drawable selectedCellDrawable;
		
		public int dayColor = 0;
		public int dayOfWeekColor = 0;
		public int weekendColor = 0;
		public int holidayColor = 0;
		
		public void calculate(int width, int height) {
			this.width = width;
			this.height = height;
			this.cellWidth = width / 7;
			this.cellHeight = height / 8;
			
			int startX = (width - this.cellWidth * 7) / 2;
			int startY = (height - this.cellHeight * 8) / 2;
			
			this.titleOffsetX = startX;
			this.titleOffsetY = startY;
			this.titleWidth = cellWidth * 7;
			this.titleHeight = cellHeight;
			
			this.headerOffsetX = startX;
			this.headerOffsetY = this.titleOffsetY + this.titleHeight;
			this.headerWidth = this.cellWidth;
			this.headerHeight = this.cellHeight;
			
			this.cellOffsetX = startX;
			this.cellOffsetY = this.headerOffsetY + this.headerHeight;
			
			this.cellMainTextSize = (int)((float)cellHeight * 2 / 5);
			this.cellSubTextSize = (int)((float)this.cellMainTextSize * 3 / 5);
			this.headerTextSize = (int)((float)this.cellMainTextSize * 2 / 3);
			this.titleTextSize = (int)((float)this.cellMainTextSize);
		}
	}	
}
