package chau.nguyen.calendar.appwidget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import chau.nguyen.R;
import chau.nguyen.calendar.ui.MonthViewRenderer;

public class SmallMonthWidgetProvider extends MonthWidgetProvider {
	private static MonthViewRenderer.Config CONFIG = null;
	private static String BITMAP_CACHE_FILENAME = null;
		
	protected String getBitmapCachePrefix() {
		return "smallMonthWidgetCache";
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
		CONFIG.titleOffsetX = resources.getDimensionPixelOffset(R.dimen.smallMonthWidgetTitleOffsetX);;
		CONFIG.titleOffsetY = resources.getDimensionPixelOffset(R.dimen.smallMonthWidgetTitleOffsetY);;
		CONFIG.titleWidth = resources.getDimensionPixelOffset(R.dimen.smallMonthWidgetTitleWidth);
		CONFIG.titleHeight = resources.getDimensionPixelOffset(R.dimen.smallMonthWidgetTitleHeight);
		CONFIG.titleTextSize = resources.getDimensionPixelSize(R.dimen.smallMonthWidgetTitleTextSize);	
		CONFIG.titleTextColor = resources.getColor(R.color.smallMonthWidgetTitleTextColor);
		
		CONFIG.headerOffsetX = resources.getDimensionPixelOffset(R.dimen.smallMonthWidgetHeaderOffsetX);;
		CONFIG.headerOffsetY = resources.getDimensionPixelOffset(R.dimen.smallMonthWidgetHeaderOffsetY);;
		CONFIG.headerWidth = resources.getDimensionPixelOffset(R.dimen.smallMonthWidgetHeaderWidth);
		CONFIG.headerHeight = resources.getDimensionPixelOffset(R.dimen.smallMonthWidgetHeaderHeight);
		CONFIG.headerTextSize = resources.getDimensionPixelSize(R.dimen.smallMonthWidgetHeaderTextSize);
		CONFIG.headerTextColor = resources.getColor(R.color.smallMonthWidgetHeaderTextColor);
		//config.headerBackground = BitmapFactory.decodeResource(resources, R.drawable.cell_header_bg);
		//config.renderHeader = false;
		
		CONFIG.cellOffsetX = resources.getDimensionPixelOffset(R.dimen.smallMonthWidgetCellOffsetX);;
		CONFIG.cellOffsetY = resources.getDimensionPixelOffset(R.dimen.smallMonthWidgetCellOffsetY);;
		CONFIG.cellWidth = resources.getDimensionPixelOffset(R.dimen.smallMonthWidgetCellWidth);
		CONFIG.cellHeight = resources.getDimensionPixelOffset(R.dimen.smallMonthWidgetCellHeight);
		CONFIG.cellMainTextSize = resources.getDimensionPixelSize(R.dimen.smallMonthWidgetCellMainTextSize);
		CONFIG.cellSubTextSize = resources.getDimensionPixelSize(R.dimen.smallMonthWidgetCellSubTextSize);		
		//config.cellBackground = BitmapFactory.decodeResource(resources, R.drawable.smallMonthWidget_cell_bg);		
		CONFIG.cellHighlightBackground= BitmapFactory.decodeResource(resources, R.drawable.widget_cell_highlight_bg);
		
		CONFIG.dayColor = resources.getColor(R.color.smallMonthWidgetDayColor);
		CONFIG.dayOfWeekColor = resources.getColor(R.color.smallMonthWidgetDayOfWeekColor);
		CONFIG.weekendColor = resources.getColor(R.color.smallMonthWidgetWeekendColor);
		CONFIG.holidayColor = resources.getColor(R.color.smallMonthWidgetHolidayColor);
				
		CONFIG.width = resources.getDimensionPixelSize(R.dimen.smallMonthWidgetWidth);
		CONFIG.height = resources.getDimensionPixelSize(R.dimen.smallMonthWidgetHeight);
		
		return CONFIG;
	}
}
