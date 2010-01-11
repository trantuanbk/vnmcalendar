package chau.nguyen.calendar.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import chau.nguyen.INavigator;
import chau.nguyen.MonthActivity;
import chau.nguyen.R;
import chau.nguyen.calendar.VietCalendar;

public class VNMDayViewer extends LinearLayout {
	private static int HORIZONTAL_FLING_THRESHOLD = 5;
	protected INavigator navigator;
	
	private TextView dayOfMonthText;
	private TextView dayOfWeekText;
	private TextView noteText;
	
	protected TextView vnmHourText;
	protected TextView vnmHourInText;
	protected TextView vnmDayOfMonthText;
	protected TextView vnmDayOfMonthInText;
	protected TextView vnmMonthText;
	protected TextView vnmMonthInText;
	protected TextView vnmYearText;
	protected TextView vnmYearInText;
	
	private Date displayDate;
	
	private MonthActivity monthActivity;
	private GestureDetector gestureDetector;
	
	static private String[] dayInVietnamese;
	
	static {
		dayInVietnamese = new String[] {"Chủ nhật", "Thứ hai", "Thứ ba", "Thứ tư", "Thứ năm", "Thứ sáu", "Thứ bảy"};
	}

	public VNMDayViewer(MonthActivity context, INavigator navigator) {
		super(context);
		init(context, navigator);
	}
	
	public VNMDayViewer(MonthActivity context, INavigator navigator, AttributeSet attrs) {
		super(context, attrs);
		init(context, navigator);
	}
	
	private void init(MonthActivity monthActivity, INavigator navigator) {
		// Inflate the view from the layout resource.
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li;
		li = (LayoutInflater)getContext().getSystemService(infService);
		li.inflate(R.layout.vnm_day_viewer, this, true);
		
		this.monthActivity = monthActivity;
		this.navigator = navigator;
		
		this.dayOfMonthText = (TextView)findViewById(R.id.dayOfMonthText);
		this.dayOfWeekText = (TextView)findViewById(R.id.dayOfWeekText);
		this.noteText = (TextView)findViewById(R.id.noteText);
		
		this.vnmHourText = (TextView) findViewById(R.id.vnmHourText);
		this.vnmHourInText = (TextView) findViewById(R.id.vnmHourInText);
		this.vnmDayOfMonthText = (TextView) findViewById(R.id.vnmDayOfMonthText);
		this.vnmDayOfMonthInText = (TextView) findViewById(R.id.vnmDayOfMonthInText);
		this.vnmMonthText = (TextView) findViewById(R.id.vnmMonthText);
		this.vnmMonthInText = (TextView) findViewById(R.id.vnmMonthInText);
		this.vnmYearText = (TextView) findViewById(R.id.vnmYearText);
		this.vnmYearInText = (TextView) findViewById(R.id.vnmYearInText);
		
        //this.gLibrary = GestureLibraries.fromRawResource(this.monthActivity, R.raw.gestures);
        //this.gLibrary.load();
        
        //this.dayViewGesture = (GestureOverlayView)findViewById(R.id.dayViewGestures);
        //this.dayViewGesture.addOnGesturePerformedListener(this);
		
		this.displayDate = new Date();
		this.setDate(this.displayDate);
		
		this.dayOfMonthText.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:
					
					break;

				default:
					break;
				}
				return true;
			}
		});
		
		
		
		gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                    float velocityY) {
            	
                // The user might do a slow "fling" after touching the screen
                // and we don't want the long-press to pop up a context menu.
                // Setting mLaunchDayView to false prevents the long-press.
                int distanceX = Math.abs((int) e2.getX() - (int) e1.getX());
                int distanceY = Math.abs((int) e2.getY() - (int) e1.getY());
                if (distanceX < HORIZONTAL_FLING_THRESHOLD || distanceX < distanceY) {
                    return false;
                }

                // Switch to a different month
                Calendar calendar = Calendar.getInstance();
    			calendar.setTime(displayDate);
                if (velocityX < 0) {
					calendar.add(Calendar.DAY_OF_MONTH, 1);
					Date afterDate = calendar.getTime();
					VNMDayViewer.this.monthActivity.gotoTime(afterDate);
				} else {
					calendar.add(Calendar.DAY_OF_MONTH, -1);
					Date beforeDate = calendar.getTime();
					VNMDayViewer.this.monthActivity.gotoTime(beforeDate);
				}

                return true;
            }

        });
		
	}
	
	public void setDate(Date date) {
		this.displayDate = date;
		this.monthActivity.setTitle(this.getDisplayDayText());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		this.dayOfMonthText.setText(dayOfMonth + "");
		this.dayOfWeekText.setText(dayInVietnamese[dayOfWeek - 1]);
		
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		int month = calendar.get(Calendar.MONTH) + 1;
		
		int year = calendar.get(Calendar.YEAR);
		TimeZone tz = calendar.getTimeZone();
		double timeZone = tz.getRawOffset() / 3600000;		
		int[] lunars = VietCalendar.convertSolar2Lunar(dayOfMonth, month, year, timeZone);
		
		this.vnmHourText.setText(hour + ":" + minute);
		this.vnmDayOfMonthText.setText(lunars[0] + "");
		this.vnmMonthText.setText(lunars[1] + "");
		this.vnmYearText.setText(lunars[2] + "");
		
		String[] vnmCalendarTexts = VietCalendar.getCanChiInfo(lunars[0], lunars[1], lunars[2], dayOfMonth, month, year);
		
		this.vnmHourInText.setText(vnmCalendarTexts[VietCalendar.HOUR]);
		this.vnmDayOfMonthInText.setText(vnmCalendarTexts[VietCalendar.DAY]);
		this.vnmMonthInText.setText(vnmCalendarTexts[VietCalendar.MONTH]);
		this.vnmYearInText.setText(vnmCalendarTexts[VietCalendar.YEAR]);
	}
	
//	public void next() {
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(this.displayDate);
//		calendar.add(Calendar.DAY_OF_MONTH, 1);
//		Date afterDate = calendar.getTime();
//		setDate(afterDate);
//	}
	
//	public void back() {
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(this.displayDate);
//		calendar.add(Calendar.DAY_OF_MONTH, -1);
//		Date beforeDate = calendar.getTime();
//		setDate(beforeDate);
//	}
	
	public void setNote(String note) {
		this.noteText.setText(note);
	}
	
	public Date getDisplayDate() {
		return displayDate;
	}

//	@Override
//	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
//		ArrayList<Prediction> predictions = this.gLibrary.recognize(gesture);
//		if (predictions.size() > 0) {
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(displayDate);
//			
//			for (Prediction prediction : predictions) {
//				if (prediction.score > 1) {
//					//Toast.makeText(this, prediction.name, Toast.LENGTH_SHORT).show();
//					if ("slide-to-left".equals(prediction.name)) {
//						calendar.add(Calendar.DAY_OF_MONTH, 1);
//						Date afterDate = calendar.getTime();
//						VNMDayViewer.this.monthActivity.gotoTime(afterDate);
//					} else if ("slide-to-right".equals(prediction.name)) {
//						calendar.add(Calendar.DAY_OF_MONTH, -1);
//						Date beforeDate = calendar.getTime();
//						VNMDayViewer.this.monthActivity.gotoTime(beforeDate);
//					}
//				}
//			}
//		}
//		
//	}
	
	
	
	public String getDisplayDayText() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return simpleDateFormat.format(this.displayDate);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}

}
