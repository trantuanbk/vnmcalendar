package chau.nguyen.calendar.appwidget;

import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import chau.nguyen.R;
import chau.nguyen.calendar.ui.MonthViewRenderer;

public class ThemeManager {
	static ThemeManager instance = null;
	
	HashMap<String, Integer> themeMap;
	public ThemeManager() {
		themeMap = new HashMap<String, Integer>();
		themeMap.put("light", R.raw.light);
		themeMap.put("dark", R.raw.dark);
		themeMap.put("small_light", R.raw.small_light);
		themeMap.put("small_dark", R.raw.small_dark);
	}
	
	public static MonthViewRenderer.Config getConfig(Context context, String theme) {
		if (instance == null) {
			instance = new ThemeManager();
		}
		theme = theme.toLowerCase();
		Log.d("DEBUG", "Theme name: " + theme);
		return MonthViewRenderer.Config.load(context.getResources().openRawResource(instance.themeMap.get(theme)));
	}
}