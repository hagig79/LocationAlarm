package com.example.locationalarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyService extends BroadcastReceiver implements LocationListener {

	private Context context;
	private LocationManager locationManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("", "onReceive");

		this.context = context;

		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, this);

	}

	@Override
	public void onLocationChanged(Location location) {

		locationManager.removeUpdates(this);

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		double lat = pref.getFloat("latitude", 0);
		double lon = pref.getFloat("longitude", 0);

		float[] results = new float[3];
		Location.distanceBetween(location.getLatitude(),
				location.getLongitude(), lat, lon, results);

		if (results[0] < 100) {

			Intent intent2 = new Intent(context, MainActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(context, 2,
					intent2, PendingIntent.FLAG_UPDATE_CURRENT);

			NotificationCompat.Builder builder = new NotificationCompat.Builder(
					context);
			builder.setSmallIcon(R.drawable.ic_launcher)
					.setContentIntent(contentIntent).setTicker("時間だよー")
					.setContentText("時間だよー").setContentTitle("通知")
					.setDefaults(Notification.DEFAULT_SOUND)
					.setAutoCancel(true).setWhen(System.currentTimeMillis());

			Notification notification = builder.build();
			int id = 3;

			NotificationManager nm = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			nm.notify(id, notification);
		}

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO 自動生成されたメソッド・スタブ

	}
}
