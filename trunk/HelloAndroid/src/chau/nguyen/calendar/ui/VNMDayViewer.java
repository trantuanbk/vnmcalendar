package chau.nguyen.calendar.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import chau.nguyen.EventManager;
import chau.nguyen.INavigator;
import chau.nguyen.R;
import chau.nguyen.VNMDayActivity;
import chau.nguyen.calendar.VNMDate;
import chau.nguyen.calendar.VietCalendar;
import chau.nguyen.calendar.VietCalendar.Holiday;

public class VNMDayViewer extends LinearLayout implements OnCreateContextMenuListener {
	private static int HORIZONTAL_FLING_THRESHOLD = 5;
	private static int VERTICAL_FLING_THRESHOLD = 5;
	private static final int CREATE_NEW_EVENT = 1;
	private static final int SELECT_DATE = 2;
	private static final int SWITCH_TO_MONTH = 3;
	protected INavigator navigator;
	
	private TextView monthText;
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
	
	private VNMDayActivity dayViewActivity;
	private GestureDetector gestureDetector;
	private ContextMenuClickHandler contextMenuClickHandler;
	private int dayOfWeekColor;
	private int weekendColor;
	private int holidayColor;
	private int eventColor;

	public VNMDayViewer(VNMDayActivity context, INavigator navigator) {
		super(context);
		init(context, navigator);
	}
	
	public VNMDayViewer(VNMDayActivity context, INavigator navigator, AttributeSet attrs) {
		super(context, attrs);
		init(context, navigator);
	}
	
	private void init(VNMDayActivity dayViewActivity, INavigator navigator) {
		// Inflate the view from the layout resource.
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li;
		li = (LayoutInflater)getContext().getSystemService(infService);
		li.inflate(R.layout.vnm_day_viewer, this, true);
		
		this.dayViewActivity = dayViewActivity;
		this.navigator = navigator;
		
		this.monthText = (TextView)findViewById(R.id.monthText);
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
		this.dayOfWeekColor = getResources().getColor(R.color.dayOfWeekColor);
		this.weekendColor = getResources().getColor(R.color.weekendColor);
		this.holidayColor = getResources().getColor(R.color.holidayColor);
		this.eventColor = getResources().getColor(R.color.eventColor);
		
		this.displayDate = new Date();
		this.setDate(this.displayDate);
		
		setOnCreateContextMenuListener(this);
		this.contextMenuClickHandler = new ContextMenuClickHandler();
		this.gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
			public boolean onSingleTapUp(MotionEvent e) {
				VNMDayViewer.this.dayViewActivity.showDayInfo();
				return true;
			}
			public void onShowPress(MotionEvent e) {
			}
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
					float distanceY) {
				return false;
			}
			public void onLongPress(MotionEvent e) {
				performLongClick();
			}
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                    float velocityY) {
                // The user might do a slow "fling" after touching the screen
                // and we don't want the long-press to pop up a context menu.
                // Setting mLaunchDayView to false prevents the long-press.
                int distanceX = Math.abs((int) e2.getX() - (int) e1.getX());
                int distanceY = Math.abs((int) e2.getY() - (int) e1.getY());
                if (distanceX < HORIZONTAL_FLING_THRESHOLD && distanceY < VERTICAL_FLING_THRESHOLD) {
                    return false;
                }

                // Switch to a different month
                Calendar calendar = Calendar.getInstance();
    			calendar.setTime(displayDate);
    			if (distanceX > distanceY) {
    				if (velocityX < 0) {
    					calendar.add(Calendar.DAY_OF_MONTH, 1);
    					Date afterDate = calendar.getTime();
    					VNMDayViewer.this.navigator.gotoDate(afterDate);
    				} else {
    					calendar.add(Calendar.DAY_OF_MONTH, -1);
    					Date beforeDate = calendar.getTime();
    					VNMDayViewer.this.navigator.gotoDate(beforeDate);
    				}
    			} else {
	                if (velocityY < 0) {
						calendar.add(Calendar.MONTH, 1);
						Date afterDate = calendar.getTime();
						VNMDayViewer.this.navigator.gotoDate(afterDate);
					} else {
						calendar.add(Calendar.MONTH, -1);
						Date beforeDate = calendar.getTime();
						VNMDayViewer.this.navigator.gotoDate(beforeDate);
					}
                }
                

                return true;
            }
			
			@Override
			public boolean onDown(MotionEvent e) {
				return true;
			}
		});		
	}
	
	private String getFamousSaying() {
		String[] famousSayings = getResources().getStringArray(R.array.famousSaying);
		int index = Calendar.getInstance().get(Calendar.MILLISECOND) % famousSayings.length;
		return famousSayings[index];
	}
	
	public void setDate(Date date) {
		EventManager eventManager = new EventManager(this.dayViewActivity.getContentResolver());
		String eventSumarize = eventManager.getSumarize(date);
		this.displayDate = date;
		String famousSaying = getFamousSaying();
		this.noteText.setText(famousSaying);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int month = calendar.get(Calendar.MONTH) + 1;
		
		int year = calendar.get(Calendar.YEAR);
		VNMDate vnmDate = VietCalendar.convertSolar2LunarInVietnamese(date);
		Holiday holiday = VietCalendar.getHoliday(vnmDate.getDayOfMonth(), vnmDate.getMonth(), dayOfMonth, month);
		if (dayOfWeek == 1) {
			this.dayOfMonthText.setTextColor(this.weekendColor);
			this.dayOfWeekText.setTextColor(this.weekendColor);
			this.noteText.setTextColor(this.weekendColor);
			this.monthText.setTextColor(this.weekendColor);
		} else if (holiday != null ) {
			this.dayOfMonthText.setTextColor(this.holidayColor);
			this.dayOfWeekText.setTextColor(this.holidayColor);
			this.noteText.setTextColor(this.holidayColor);
			this.monthText.setTextColor(this.holidayColor);
		} else {
			this.dayOfMonthText.setTextColor(this.dayOfWeekColor);
			this.dayOfWeekText.setTextColor(this.dayOfWeekColor);
			this.noteText.setTextColor(this.dayOfWeekColor);
			this.monthText.setTextColor(this.dayOfWeekColor);
		}
		this.monthText.setText("Tháng " + month + " năm " + year);
		this.dayOfMonthText.setText(dayOfMonth + "");
		this.dayOfWeekText.setText(VietCalendar.getDayOfWeekText(dayOfWeek));
		if (holiday != null) {
			this.noteText.setText(holiday.getDescription());
		} else {
			this.noteText.setText(famousSaying);
		}
		
		this.vnmHourText.setText(hour + ":" + minute);
		this.vnmDayOfMonthText.setText(vnmDate.getDayOfMonth() + "");
		this.vnmMonthText.setText(vnmDate.getMonth() + "");
		this.vnmYearText.setText(vnmDate.getYear() + "");
		
		String[] vnmCalendarTexts = VietCalendar.getCanChiInfo(vnmDate.getDayOfMonth(), vnmDate.getMonth(), vnmDate.getYear(), dayOfMonth, month, year);
		
		this.vnmHourInText.setText(vnmCalendarTexts[VietCalendar.HOUR]);
		this.vnmDayOfMonthInText.setText(vnmCalendarTexts[VietCalendar.DAY]);
		this.vnmMonthInText.setText(vnmCalendarTexts[VietCalendar.MONTH]);
		this.vnmYearInText.setText(vnmCalendarTexts[VietCalendar.YEAR]);	
		if (eventSumarize != null && eventSumarize.length() > 0) {
			this.noteText.setTextColor(this.eventColor);
			this.noteText.setText(eventSumarize);
		}
		this.invalidate();
	}		
	
	public void setNote(String note) {
		this.noteText.setText(note);
	}
	
	public Date getDisplayDate() {
		return displayDate;
	}

	public String getDisplayDayText() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return simpleDateFormat.format(this.displayDate);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {		
		return this.gestureDetector.onTouchEvent(event);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		MenuItem item;		
		item = menu.add(0, SELECT_DATE, 0, "Chọn ngày").setIcon(android.R.drawable.ic_menu_day);
		item.setOnMenuItemClickListener(this.contextMenuClickHandler);
		item = menu.add(0, CREATE_NEW_EVENT, 0, "Thêm sự kiện").setIcon(android.R.drawable.ic_menu_add);;
		item.setOnMenuItemClickListener(this.contextMenuClickHandler);
		item = menu.add(0, SWITCH_TO_MONTH, 0, "Hiện thị theo tháng").setIcon(android.R.drawable.ic_menu_month);;
		item.setOnMenuItemClickListener(this.contextMenuClickHandler);
	}
	
	private class ContextMenuClickHandler implements OnMenuItemClickListener {
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			switch (item.getItemId()) {
			case CREATE_NEW_EVENT:
				VNMDayViewer.this.dayViewActivity.addEvent();
				break;
			case SELECT_DATE:
				VNMDayViewer.this.dayViewActivity.selectDate();
				break;
			case SWITCH_TO_MONTH:
				VNMDayViewer.this.dayViewActivity.showMonthView();				
				break;
			default:
				break;
			}
			return false;
		}		
	}
}
