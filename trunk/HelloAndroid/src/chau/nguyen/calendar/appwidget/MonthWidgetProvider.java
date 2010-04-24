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
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import chau.nguyen.R;
import chau.nguyen.VNMDayActivity;
import chau.nguyen.calendar.content.LocalFileContentProvider;
import chau.nguyen.calendar.ui.MonthViewRenderer;

public class MonthWidgetProvider extends AppWidgetProvider {
	private static MonthViewRenderer.Config CONFIG = null;	
	private static String BITMAP_CACHE_FILENAME = null;
	
	protected MonthViewRenderer.Config init(Context context) {		
		CONFIG = new MonthViewRenderer.Config();		
		CONFIG.autoCalculateOffsets = false;
		
		Resources resources = context.getResources();
		CONFIG.titleOffsetX = resources.getDimensionPixelOffset(R.dimen.widgetTitleOffsetX);;
		CONFIG.titleOffsetY = resources.getDimensionPixelOffset(R.dimen.widgetTitleOffsetY);;
		CONFIG.titleWidth = resources.getDimensionPixelOffset(R.dimen.widgetTitleWidth);
		CONFIG.titleHeight = resources.getDimensionPixelOffset(R.dimen.widgetTitleHeight);
		CONFIG.titleTextSize = resources.getDimensionPixelSize(R.dimen.widgetTitleTextSize);	
		CONFIG.titleTextColor = resources.getColor(R.color.widgetTitleTextColor);
		
		CONFIG.headerOffsetX = resources.getDimensionPixelOffset(R.dimen.widgetHeaderOffsetX);;
		CONFIG.headerOffsetY = resources.getDimensionPixelOffset(R.dimen.widgetHeaderOffsetY);;
		CONFIG.headerWidth = resources.getDimensionPixelOffset(R.dimen.widgetHeaderWidth);
		CONFIG.headerHeight = resources.getDimensionPixelOffset(R.dimen.widgetHeaderHeight);
		CONFIG.headerTextSize = resources.getDimensionPixelSize(R.dimen.widgetHeaderTextSize);
		CONFIG.headerTextColor = resources.getColor(R.color.widgetHeaderTextColor);
		//config.headerBackground = BitmapFactory.decodeResource(resources, R.drawable.cell_header_bg);
		//config.renderHeader = false;
		
		CONFIG.cellOffsetX = resources.getDimensionPixelOffset(R.dimen.widgetCellOffsetX);;
		CONFIG.cellOffsetY = resources.getDimensionPixelOffset(R.dimen.widgetCellOffsetY);;
		CONFIG.cellWidth = resources.getDimensionPixelOffset(R.dimen.widgetCellWidth);
		CONFIG.cellHeight = resources.getDimensionPixelOffset(R.dimen.widgetCellHeight);
		CONFIG.cellMainTextSize = resources.getDimensionPixelSize(R.dimen.widgetCellMainTextSize);
		CONFIG.cellSubTextSize = resources.getDimensionPixelSize(R.dimen.widgetCellSubTextSize);		
		//config.cellBackground = BitmapFactory.decodeResource(resources, R.drawable.widget_cell_bg);		
		CONFIG.cellHighlightBackground= BitmapFactory.decodeResource(resources, R.drawable.widget_cell_highlight_bg);
		
		CONFIG.dayColor = resources.getColor(R.color.widgetDayColor);
		CONFIG.dayOfWeekColor = resources.getColor(R.color.widgetDayOfWeekColor);
		CONFIG.weekendColor = resources.getColor(R.color.widgetWeekendColor);
		CONFIG.holidayColor = resources.getColor(R.color.widgetHolidayColor);
						
		CONFIG.width = resources.getDimensionPixelSize(R.dimen.widgetWidth);
		CONFIG.height = resources.getDimensionPixelSize(R.dimen.widgetHeight);	
		
		return CONFIG;
	}
	
	protected MonthViewRenderer.Config getConfig() {
		return CONFIG;
	}	
	protected String getBitmapCacheFileName() {
		return BITMAP_CACHE_FILENAME;
	}
	protected void setBitmapCacheFileName(String bitmapCacheFileName) {
		BITMAP_CACHE_FILENAME = bitmapCacheFileName;
	}	
	protected String getBitmapCachePrefix() {
		return "monthWidgetCache";
	}
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {		
        final int N = appWidgetIds.length;
        AppWidgetProviderInfo info = appWidgetManager.getAppWidgetInfo(appWidgetIds[0]);
        // render MonthView    
        MonthViewRenderer.Config config = getConfig();
        if (config == null) {        	        	
        	config = init(context);
        }
        Uri bitmapUri = null;
        // do we need to update the widgets?
        Calendar today = Calendar.getInstance();
        if (config.date != null) {
	        Calendar currentDate = Calendar.getInstance();
	        currentDate.setTime(config.date);
	        if (MonthViewRenderer.isSameDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH),
	        		currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH))) {
	        	bitmapUri = Uri.parse(LocalFileContentProvider.constructUri(getBitmapCacheFileName()));
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
            Log.d("DEBUG", "InitialLayout: " + info.initialLayout);
            RemoteViews views = new RemoteViews(context.getPackageName(), info.initialLayout);            
        	views.setImageViewUri(R.id.monthViewImage, bitmapUri);
        	views.setOnClickPendingIntent(R.id.monthViewImage, pendingIntent);
        	
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
		
	protected Uri renderWidget(Context context) {
		MonthViewRenderer.Config config = getConfig();
		Bitmap bitmap = Bitmap.createBitmap(config.width, config.height, Bitmap.Config.ARGB_8888);		
		config.date = new Date();		
		MonthViewRenderer monthViewRenderer = new MonthViewRenderer(config);
		monthViewRenderer.render(new Canvas(bitmap));
		// write the bitmap to temporary file
		String bitmapCacheFileName = getBitmapCacheFileName();
		if (bitmapCacheFileName != null) {
			new File(bitmapCacheFileName).delete();
		}
		bitmapCacheFileName = context.getCacheDir() + "/" + getBitmapCachePrefix() + config.date.getTime() + ".png";		
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
        setBitmapCacheFileName(bitmapCacheFileName);
		return Uri.parse(LocalFileContentProvider.constructUri(file.getAbsolutePath()));
	}
}
