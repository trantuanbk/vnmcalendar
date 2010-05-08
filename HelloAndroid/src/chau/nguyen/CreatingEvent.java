package chau.nguyen;

import java.util.Calendar;
import java.util.Date;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import chau.nguyen.calendar.VNMDate;
import chau.nguyen.calendar.VietCalendar;

public class CreatingEvent implements Runnable {
	public final static int STATE_DONE = 0;
	public final static int STATE_RUNNING = 1;
	public final static String STATUS = "status";
	private String title;
	private String description;
	private String eventLocation;
	private VNMDate startDate;
	private VNMDate endDate;
	private int numberYears;
	private int repeat; //type of repeat
	private long reminderMinutes;
	private ContentResolver cr;
	private String[] calIds;
	private Handler mHandler;
	
	public CreatingEvent(Handler mHandler) {
		this.mHandler = mHandler;
	}
	@Override
	public void run() {
		Bundle brunning = new Bundle();
		brunning.putInt(STATUS, STATE_RUNNING);
		Message msgruning = mHandler.obtainMessage();
		msgruning.setData(brunning);
		mHandler.sendMessage(msgruning);
		if (calIds != null && calIds.length > 0) {
			for (String calId : calIds) {
				addEvent(calId);
			}
			
		}
		Bundle bdone = new Bundle();
		bdone.putInt(STATUS, STATE_DONE);
		Message mdone = mHandler.obtainMessage();
		mdone.setData(bdone);
		mHandler.sendMessage(mdone);
	}
	
	private void addEvent(String calId) {
		ContentValues event = new ContentValues();
		event.put("calendar_id", calId);
		event.put("title", this.title);
		event.put("description", this.description);
		event.put("eventLocation", this.eventLocation);
		Calendar calStart = Calendar.getInstance();
		VNMDate temp = VietCalendar.convertSolar2LunarInVietnamese(calStart.getTime());
		int currentYear = temp.getYear();
		if (currentYear > this.startDate.getYear()) {
			this.startDate.setYear(currentYear);
		}
		
		if (currentYear > this.endDate.getYear()) {
			this.endDate.setYear(currentYear);
		}
		switch (this.repeat) {
		case VNMEventDetailsActivity.YEARLY_REPEAT:
			for (int i = 0; i <= numberYears; i++) {
				createEvent(event, VietCalendar.addYear(this.startDate, i), VietCalendar.addYear(this.endDate, i));
			}
			break;
		
		case VNMEventDetailsActivity.MONTHLY_REPEAT:
			for (int i = 0; i <= 12 * numberYears; i++) {
				createEvent(event, VietCalendar.addMonth(this.startDate, i), VietCalendar.addMonth(this.endDate, i));
			}
		break;
		default:
			createEvent(event, this.startDate, this.endDate);
			break;
		}
	}
	
	private void createEvent(ContentValues event, VNMDate startDate, VNMDate endDate) {
		Log.i("Event", "startLunarDay: " + startDate.getDayOfMonth() + "/" + startDate.getMonth() + "/" + startDate.getYear());
		Date solarStartDate = VietCalendar.convertLunar2Solar(startDate);
		Log.i("Event", "startSolarDay: " + solarStartDate);
		
		Log.i("Event", "endLunarDay: " + endDate.getDayOfMonth() + "/" + endDate.getMonth() + "/" + endDate.getYear());
		Date solarEndDate = VietCalendar.convertLunar2Solar(endDate);
		Log.i("Event", "startSolarDay: " + solarEndDate);
		long startTime = solarStartDate.getTime();
		long endTime = solarEndDate.getTime();
		event.put("dtstart", startTime);
		event.put("dtend", endTime);
		
		event.put("hasAlarm", 1);
		try {
			Uri eventsUri = Uri.parse("content://calendar/events");
			Uri newEvent = cr.insert(eventsUri, event);
			long id = Long.parseLong(newEvent.getLastPathSegment());
			if(newEvent != null) {

				ContentValues values = new ContentValues();
				values.put("event_id", id);
				values.put("method", 1);
				values.put("minutes", reminderMinutes);
				cr.insert(Uri.parse("content://calendar/reminders"), values);

//				ContentValues alertValues = new ContentValues();
//				alertValues.put("event_id", id);
//				alertValues.put("begin", startTime);
//				alertValues.put("end", endTime);
//				alertValues.put( "alarmTime", startTime);
//				alertValues.put( "state", 0 );
//				alertValues.put( "minutes", reminderMinutes );
//				cr.insert( Uri.parse( "content://calendar/calendar_alerts" ), alertValues );
				} 
		} catch (Exception e) {
			Log.e("AddingCalendarEvent", e.getMessage());
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEventLocation() {
		return eventLocation;
	}

	public void setEventLocation(String eventLocation) {
		this.eventLocation = eventLocation;
	}

	public VNMDate getStartDate() {
		return startDate;
	}

	public void setStartDate(VNMDate startDate) {
		this.startDate = startDate;
	}

	public VNMDate getEndDate() {
		return endDate;
	}

	public void setEndDate(VNMDate endDate) {
		this.endDate = endDate;
	}

	public int getNumberYears() {
		return numberYears;
	}

	public void setNumberYears(int numberYears) {
		this.numberYears = numberYears;
	}

	public int getRepeat() {
		return repeat;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	public long getReminderMinutes() {
		return reminderMinutes;
	}

	public void setReminderMinutes(long reminderMinutes) {
		this.reminderMinutes = reminderMinutes;
	}

	public ContentResolver getCr() {
		return cr;
	}

	public void setCr(ContentResolver cr) {
		this.cr = cr;
	}

	public String[] getCalIds() {
		return calIds;
	}

	public void setCalIds(String... calIds) {
		this.calIds = calIds;
	}
	
}
