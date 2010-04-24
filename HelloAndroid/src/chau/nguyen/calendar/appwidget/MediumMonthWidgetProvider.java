package chau.nguyen.calendar.appwidget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import chau.nguyen.R;
import chau.nguyen.calendar.ui.MonthViewRenderer;

public class MediumMonthWidgetProvider extends MonthWidgetProvider {
	private static MonthViewRenderer.Config CONFIG = null;
	private static String BITMAP_CACHE_FILENAME = null;
		
	protected String getBitmapCachePrefix() {
		return "mediumMonthWidgetCache";
	}	
	protected String getBitmapCacheFileName() {
		return BITMAP_CACHE_FILENAME;
	}
	protected void setBitmapCacheFileName(String bitmapCacheFileName) {
		BITMAP_CACHE_FILENAME = bitmapCacheFileName;
	}
	protected MonthViewRenderer.Config getConfig() {
		return CONFIG;
	}
	
	protected MonthViewRenderer.Config init(Context context) {		
		CONFIG = new MonthViewRenderer.Config();		
		CONFIG.autoCalculateOffsets = false;
		
		Resources resources = context.getResources();
		CONFIG.titleOffsetX = resources.getDimensionPixelOffset(R.dimen.mediumMonthWidgetTitleOffsetX);;
		CONFIG.titleOffsetY = resources.getDimensionPixelOffset(R.dimen.mediumMonthWidgetTitleOffsetY);;
		CONFIG.titleWidth = resources.getDimensionPixelOffset(R.dimen.mediumMonthWidgetTitleWidth);
		CONFIG.titleHeight = resources.getDimensionPixelOffset(R.dimen.mediumMonthWidgetTitleHeight);
		CONFIG.titleTextSize = resources.getDimensionPixelSize(R.dimen.mediumMonthWidgetTitleTextSize);	
		CONFIG.titleTextColor = resources.getColor(R.color.mediumMonthWidgetTitleTextColor);
		
		CONFIG.headerOffsetX = resources.getDimensionPixelOffset(R.dimen.mediumMonthWidgetHeaderOffsetX);;
		CONFIG.headerOffsetY = resources.getDimensionPixelOffset(R.dimen.mediumMonthWidgetHeaderOffsetY);;
		CONFIG.headerWidth = resources.getDimensionPixelOffset(R.dimen.mediumMonthWidgetHeaderWidth);
		CONFIG.headerHeight = resources.getDimensionPixelOffset(R.dimen.mediumMonthWidgetHeaderHeight);
		CONFIG.headerTextSize = resources.getDimensionPixelSize(R.dimen.mediumMonthWidgetHeaderTextSize);
		CONFIG.headerTextColor = resources.getColor(R.color.mediumMonthWidgetHeaderTextColor);
		//config.headerBackground = BitmapFactory.decodeResource(resources, R.drawable.cell_header_bg);
		//config.renderHeader = false;
		
		CONFIG.cellOffsetX = resources.getDimensionPixelOffset(R.dimen.mediumMonthWidgetCellOffsetX);;
		CONFIG.cellOffsetY = resources.getDimensionPixelOffset(R.dimen.mediumMonthWidgetCellOffsetY);;
		CONFIG.cellWidth = resources.getDimensionPixelOffset(R.dimen.mediumMonthWidgetCellWidth);
		CONFIG.cellHeight = resources.getDimensionPixelOffset(R.dimen.mediumMonthWidgetCellHeight);
		CONFIG.cellMainTextSize = resources.getDimensionPixelSize(R.dimen.mediumMonthWidgetCellMainTextSize);
		CONFIG.cellSubTextSize = resources.getDimensionPixelSize(R.dimen.mediumMonthWidgetCellSubTextSize);		
		//config.cellBackground = BitmapFactory.decodeResource(resources, R.drawable.mediumMonthWidget_cell_bg);		
		CONFIG.cellHighlightBackground= BitmapFactory.decodeResource(resources, R.drawable.widget_cell_highlight_bg);
		
		CONFIG.dayColor = resources.getColor(R.color.mediumMonthWidgetDayColor);
		CONFIG.dayOfWeekColor = resources.getColor(R.color.mediumMonthWidgetDayOfWeekColor);
		CONFIG.weekendColor = resources.getColor(R.color.mediumMonthWidgetWeekendColor);
		CONFIG.holidayColor = resources.getColor(R.color.mediumMonthWidgetHolidayColor);
				
		CONFIG.width = resources.getDimensionPixelSize(R.dimen.mediumMonthWidgetWidth);
		CONFIG.height = resources.getDimensionPixelSize(R.dimen.mediumMonthWidgetHeight);
		
		return CONFIG;
	}
}
