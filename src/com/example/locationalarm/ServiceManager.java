package com.example.locationalarm;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class ServiceManager {

	private Context context;
	private int requestCode = 1;

	public ServiceManager(Context context) {
		this.context = context;
	}

	public void schedule(int hour, int minute) {
		Intent intent = new Intent(context, MyService.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				requestCode, intent, PendingIntent.FLAG_ONE_SHOT);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 0);

		AlarmManager alarm = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarm.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
	}

	public void cancel() {
		Intent intent = new Intent(context, MyService.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				requestCode, intent, PendingIntent.FLAG_NO_CREATE);

		AlarmManager alarm = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(pendingIntent);
	}

	public boolean isRegisteredService() {
		Intent intent = new Intent(context, MyService.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				requestCode, intent, PendingIntent.FLAG_NO_CREATE);
		return pendingIntent != null;
	}
}
