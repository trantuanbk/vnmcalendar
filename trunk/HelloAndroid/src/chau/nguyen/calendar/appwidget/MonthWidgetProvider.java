package chau.nguyen.calendar.appwidget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
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
	MonthViewRenderer.Config config;
	private Bitmap bitmap;	
	private Canvas canvas;	
	
	private void init(Context context, AppWidgetProviderInfo providerInfo) {		
		config = new MonthViewRenderer.Config();
		config.cellBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.cell_bg);
		config.cellHeaderBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.cell_header_bg);
		config.cellHighlightBackground= BitmapFactory.decodeResource(context.getResources(), R.drawable.cell_highlight_bg);
		config.dayColor = context.getResources().getColor(R.color.widgetDayColor);
		config.dayOfWeekColor = context.getResources().getColor(R.color.widgetDayOfWeekColor);
		config.weekendColor = context.getResources().getColor(R.color.widgetWeekendColor);
		config.holidayColor = context.getResources().getColor(R.color.widgetHolidayColor);
				
		Bitmap backgroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_bg);		
		bitmap = Bitmap.createBitmap(backgroundBitmap.getWidth(), backgroundBitmap.getHeight(), Bitmap.Config.ARGB_8888);		
		canvas = new Canvas(bitmap);
	}
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {		
        final int N = appWidgetIds.length;
        // render MonthView                
        if (config == null) {
        	AppWidgetProviderInfo info = appWidgetManager.getAppWidgetInfo(appWidgetIds[0]);
        	init(context, info);
        }
        Uri bitmapUri = renderWidget(context);        
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
		String bitmapFileName = context.getCacheDir() + "/monthWidgetCache.png";
		File file = new File(bitmapFileName);		
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
