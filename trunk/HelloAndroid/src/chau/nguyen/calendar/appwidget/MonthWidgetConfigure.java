package chau.nguyen.calendar.appwidget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

public class MonthWidgetConfigure extends Activity {
	static final int SELECT_THEME_DIALOG = 1;
	static final String TAG = "MonthWidgetConfigure";

    private static final String PREFS_NAME
            = "chau.nguyen.calendar.appwidget.MonthWidgetProvider";
    private static final String PREF_PREFIX_KEY = "month_widget_theme_";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetPrefix;

    public MonthWidgetConfigure() {
        super();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if they press the back button.
        setResult(RESULT_CANCELED);

        // Find the widget id from the intent. 
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }                

        // If they gave us an intent without the widget id, just bail.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
        
        showDialog(SELECT_THEME_DIALOG);
    }
    
    protected String getThemeName(String theme) {
    	return theme;
    }        
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	Dialog dialog;
        switch(id) {
	        case SELECT_THEME_DIALOG:
	        	final String[] items = {"Light", "Dark"};
	        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        	builder.setTitle("Pick a theme");
	        	builder.setItems(items, new DialogInterface.OnClickListener() {
	        	    public void onClick(DialogInterface dialog, int item) {
	        	    	MonthWidgetConfigure.this.setWidgetTheme(items[item]);
	        	    }
	        	});
	        	builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						MonthWidgetConfigure.this.finish();
					}	        		
	        	});
	        	dialog = builder.create();
	            break;
	        default:
	            dialog = null;
        }
        return dialog;
    }
    
    private void setWidgetTheme(String theme) {
        // When the button is clicked, save the string in our prefs and return that they
        // clicked OK.            
        saveWidgetPref(this, mAppWidgetId, theme);

        // Push widget update to surface with newly set prefix
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        MonthWidgetProvider.updateAppWidget(this, appWidgetManager, mAppWidgetId, getThemeName(theme));

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();        
    }

    // Write the theme to the SharedPreferences object for this widget
    static void saveWidgetPref(Context context, int appWidgetId, String theme) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, theme);
        prefs.commit();
    }

    // Read the theme from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    public static String loadWidgetPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String themeName = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (themeName != null) {
            return themeName;
        } else {
            return "light";
        }
    }
}
