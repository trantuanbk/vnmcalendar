package chau.nguyen.calendar.appwidget;

import java.util.Calendar;
import java.util.Date;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import chau.nguyen.R;
import chau.nguyen.VNMDayActivity;
import chau.nguyen.calendar.VietCalendar;
import chau.nguyen.calendar.VietCalendar.Holiday;
import chau.nguyen.calendar.ui.MonthViewRenderer;

public class DayWidgetProvider extends AppWidgetProvider {
	private static Date currentDate = null;
	private static int dayOfWeekColor = 0;
	private static int weekendColor = 0;
	private static int holidayColor = 0;
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {		
        final int N = appWidgetIds.length;
        
        if (currentDate == null) {        	
        	dayOfWeekColor = context.getResources().getColor(R.color.dayOfWeekColor);
    		weekendColor = context.getResources().getColor(R.color.weekendColor);
    		holidayColor = context.getResources().getColor(R.color.holidayColor);
        }
        
        // do we need to update the widgets?
        Calendar todayCal = Calendar.getInstance();
        if (currentDate != null) {
	        Calendar currentCal = Calendar.getInstance();
	        currentCal.setTime(currentDate);
	        if (MonthViewRenderer.isSameDate(todayCal.get(Calendar.YEAR), todayCal.get(Calendar.MONTH), todayCal.get(Calendar.DAY_OF_MONTH),
	        		currentCal.get(Calendar.YEAR), currentCal.get(Calendar.MONTH), currentCal.get(Calendar.DAY_OF_MONTH))) {
	        	return;
	        }
        }
                
        currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH) + 1;		
		int year = calendar.get(Calendar.YEAR);
		int[] lunars = VietCalendar.convertSolar2LunarInVietnam(currentDate);		
		String[] vnmCalendarTexts = VietCalendar.getCanChiInfo(lunars[VietCalendar.DAY], lunars[VietCalendar.MONTH], lunars[VietCalendar.YEAR], dayOfMonth, month, year);
		Holiday holiday = VietCalendar.getHoliday(lunars[0], lunars[1], dayOfMonth, month);
		int dayColor = dayOfWeekColor;
		if (dayOfWeek == 1) {
			dayColor = weekendColor;			
		} else if (holiday != null ) {
			dayColor = holidayColor;			
		}
		
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];            
            Intent intent = new Intent(context, VNMDayActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.day_widget);
            views.setTextViewText(R.id.dayOfMonthText, dayOfMonth + "");
            views.setTextColor(R.id.dayOfMonthText, dayColor);            
            views.setTextViewText(R.id.dayOfWeekText, VietCalendar.getDayOfWeekText(dayOfWeek));
            views.setTextColor(R.id.dayOfWeekText, dayColor);            
            views.setTextViewText(R.id.vnmDayOfMonthText, lunars[VietCalendar.DAY] + "");
            views.setTextViewText(R.id.vnmDayOfMonthInText, vnmCalendarTexts[VietCalendar.DAY] + "");            
            views.setTextViewText(R.id.vnmMonthText, lunars[VietCalendar.MONTH] + "");
            views.setTextViewText(R.id.vnmMonthInText, vnmCalendarTexts[VietCalendar.MONTH] + "");            
            
        	views.setOnClickPendingIntent(R.id.dayWidget, pendingIntent);
        	
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }	
}
