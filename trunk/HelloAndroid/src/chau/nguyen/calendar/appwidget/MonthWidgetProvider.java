package chau.nguyen.calendar.appwidget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.widget.RemoteViews;
import chau.nguyen.R;
import chau.nguyen.VNMDayActivity;
import chau.nguyen.calendar.content.LocalFileContentProvider;
import chau.nguyen.calendar.ui.MonthViewRenderer;

public class MonthWidgetProvider extends AppWidgetProvider {
	private static MonthViewRenderer.Config config = null;
	private static Bitmap bitmap;	
	private static Canvas canvas;
	private static String bitmapCacheFileName = null;
	
	private void init(Context context, AppWidgetProviderInfo providerInfo) {		
		config = new MonthViewRenderer.Config();		
		config.autoCalculateOffsets = false;
		
		Resources resources = context.getResources();
		config.titleOffsetX = resources.getDimensionPixelOffset(R.dimen.widgetTitleOffsetX);;
		config.titleOffsetY = resources.getDimensionPixelOffset(R.dimen.widgetTitleOffsetY);;
		config.titleWidth = resources.getDimensionPixelOffset(R.dimen.widgetTitleWidth);
		config.titleHeight = resources.getDimensionPixelOffset(R.dimen.widgetTitleHeight);
		config.titleTextSize = resources.getDimensionPixelSize(R.dimen.widgetTitleTextSize);	
		config.titleTextColor = resources.getColor(R.color.widgetTitleTextColor);
		
		config.headerOffsetX = resources.getDimensionPixelOffset(R.dimen.widgetHeaderOffsetX);;
		config.headerOffsetY = resources.getDimensionPixelOffset(R.dimen.widgetHeaderOffsetY);;
		config.headerWidth = resources.getDimensionPixelOffset(R.dimen.widgetHeaderWidth);
		config.headerHeight = resources.getDimensionPixelOffset(R.dimen.widgetHeaderHeight);
		config.headerTextSize = resources.getDimensionPixelSize(R.dimen.widgetHeaderTextSize);
		config.headerTextColor = resources.getColor(R.color.widgetHeaderTextColor);
		config.headerBackground = BitmapFactory.decodeResource(resources, R.drawable.cell_header_bg);
		//config.renderHeader = false;
		
		config.cellOffsetX = resources.getDimensionPixelOffset(R.dimen.widgetCellOffsetX);;
		config.cellOffsetY = resources.getDimensionPixelOffset(R.dimen.widgetCellOffsetY);;
		config.cellWidth = resources.getDimensionPixelOffset(R.dimen.widgetCellWidth);
		config.cellHeight = resources.getDimensionPixelOffset(R.dimen.widgetCellHeight);
		config.cellMainTextSize = resources.getDimensionPixelSize(R.dimen.widgetCellMainTextSize);
		config.cellSubTextSize = resources.getDimensionPixelSize(R.dimen.widgetCellSubTextSize);		
		config.cellBackground = BitmapFactory.decodeResource(resources, R.drawable.widget_cell_bg);		
		config.cellHighlightBackground= BitmapFactory.decodeResource(resources, R.drawable.widget_cell_highlight_bg);
		
		config.dayColor = resources.getColor(R.color.widgetDayColor);
		config.dayOfWeekColor = resources.getColor(R.color.widgetDayOfWeekColor);
		config.weekendColor = resources.getColor(R.color.widgetWeekendColor);
		config.holidayColor = resources.getColor(R.color.widgetHolidayColor);
				
		int widgetWidth = resources.getDimensionPixelSize(R.dimen.widgetWidth);
		int widgetHeight = resources.getDimensionPixelSize(R.dimen.widgetHeight);
		bitmap = Bitmap.createBitmap(widgetWidth, widgetHeight, Bitmap.Config.ARGB_8888);		
		canvas = new Canvas(bitmap);
	}
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {		
        final int N = appWidgetIds.length;
        // render MonthView                
        if (config == null) {
        	AppWidgetProviderInfo info = appWidgetManager.getAppWidgetInfo(appWidgetIds[0]);
        	init(context, info);
        }
        Uri bitmapUri = null;
        // do we need to update the widgets?
        Calendar today = Calendar.getInstance();
        if (config.date != null) {
	        Calendar currentDate = Calendar.getInstance();
	        currentDate.setTime(config.date);
	        if (MonthViewRenderer.isSameDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH),
	        		currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH))) {
	        	bitmapUri = Uri.parse(LocalFileContentProvider.constructUri(bitmapCacheFileName));
	        }
        }
        if (bitmapUri == null) {
        	bitmapUri = renderWidget(context);
        }
        
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];        
            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, VNMDayActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.month_widget);            
        	views.setImageViewUri(R.id.monthViewImage, bitmapUri);
        	views.setOnClickPendingIntent(R.id.monthViewImage, pendingIntent);
        	
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

	private Uri renderWidget(Context context) {		
		bitmap.eraseColor(Color.TRANSPARENT);
		config.date = new Date();		
		MonthViewRenderer monthViewRenderer = new MonthViewRenderer(config);
		monthViewRenderer.render(canvas);
		// write the bitmap to temporary file		
		if (bitmapCacheFileName != null) {
			new File(bitmapCacheFileName).delete();
		}
		bitmapCacheFileName = context.getCacheDir() + "/monthWidgetCache" + config.date.getTime() + ".png";
		File file = new File(bitmapCacheFileName);		
		try {			
			file.createNewFile();
            FileOutputStream ostream = new FileOutputStream(file);
            bitmap.compress(CompressFormat.PNG, 100, ostream);
            ostream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }        
		return Uri.parse(LocalFileContentProvider.constructUri(file.getAbsolutePath()));
	}
}
