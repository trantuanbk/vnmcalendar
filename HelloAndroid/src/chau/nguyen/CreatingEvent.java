package chau.nguyen;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import chau.nguyen.calendar.VNMDate;
import chau.nguyen.calendar.VietCalendar;

public class CreatingEvent implements Runnable {
	private static Uri EVENTS_URI = Uri.parse("content://calendar/events");
	private static Uri REMINDERS_URI = Uri.parse("content://calendar/reminders");
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
	private ContentResolver contentResolver;
	private String[] calIds;
	private String groupId = UUID.randomUUID().toString();
	
	@Override
	public void run() {
		if (calIds != null && calIds.length > 0) {
			for (String calId : calIds) {
				addEvent(calId);
			}			
		}
	}
	
	private void addEvent(String calId) {
		Calendar calStart = Calendar.getInstance();
		VNMDate temp = VietCalendar.convertSolar2LunarInVietnamese(calStart.getTime());
		int currentYear = temp.getYear();
		if (currentYear > this.startDate.getYear()) {
			this.startDate.setYear(currentYear);
		}		
		if (currentYear > this.endDate.getYear()) {
			this.endDate.setYear(currentYear);
		}
		
		ArrayList<ContentValues> values = new ArrayList<ContentValues>();
		switch (this.repeat) {
			case VNMEventDetailsActivity.YEARLY_REPEAT:
				for (int i = 0; i <= numberYears; i++) {
					values.add(createEvent(calId, VietCalendar.addYear(this.startDate, i), VietCalendar.addYear(this.endDate, i)));
				}
				break;			
			case VNMEventDetailsActivity.MONTHLY_REPEAT:
				for (int i = 0; i <= 12 * numberYears; i++) {
					values.add(createEvent(calId, VietCalendar.addMonth(this.startDate, i), VietCalendar.addMonth(this.endDate, i)));
				}
				break;
			default:
				values.add(createEvent(calId, this.startDate, this.endDate));
				break;
		}	
		
		contentResolver.bulkInsert(EVENTS_URI, values.toArray(new ContentValues[0]));		
		// query event_ids
		values.clear();
		Cursor managedCursor = contentResolver.query(
                EVENTS_URI,
                new String[] { "_id" },    // Which columns to return.
                "htmlUri = ?",          // WHERE clause.
                new String[] { groupId },          // WHERE clause value substitution
                null);   // Sort order.
		if (managedCursor.moveToFirst()) {
			do {
				int id = managedCursor.getInt(0);
				ContentValues reminder = new ContentValues();
				reminder.put("event_id", id);
				reminder.put("method", 1);
				reminder.put("minutes", reminderMinutes);
				values.add(reminder);
			} while (managedCursor.moveToNext());
		}		
		contentResolver.bulkInsert(REMINDERS_URI, values.toArray(new ContentValues[0]));
	}
	
	private ContentValues createEvent(String calId, VNMDate startDate, VNMDate endDate) {		
		ContentValues event = new ContentValues();
		event.put("calendar_id", calId);
		event.put("title", this.title);
		event.put("description", this.description);
		event.put("eventLocation", this.eventLocation);
		event.put("htmlUri", groupId);
		
		Date solarStartDate = VietCalendar.convertLunar2Solar(startDate);
		Date solarEndDate = VietCalendar.convertLunar2Solar(endDate);
		
//		Log.i("Event", "startLunarDay: " + startDate.getDayOfMonth() + "/" + startDate.getMonth() + "/" + startDate.getYear());
//		Log.i("Event", "startSolarDay: " + solarStartDate);		
//		Log.i("Event", "endLunarDay: " + endDate.getDayOfMonth() + "/" + endDate.getMonth() + "/" + endDate.getYear());		
//		Log.i("Event", "endSolarDay: " + solarEndDate);
		
		long startTime = solarStartDate.getTime();
		long endTime = solarEndDate.getTime();
		event.put("dtstart", startTime);
		event.put("dtend", endTime);		
		event.put("hasAlarm", 1);
		return event;
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
	public ContentResolver getContentResolver() {
		return contentResolver;
	}
	public void setContentResolver(ContentResolver cr) {
		this.contentResolver = cr;
	}
	public String[] getCalIds() {
		return calIds;
	}
	public void setCalIds(String... calIds) {
		this.calIds = calIds;
	}
}
