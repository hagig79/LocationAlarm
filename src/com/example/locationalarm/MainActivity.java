package com.example.locationalarm;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class MainActivity extends ActionBarActivity implements LocationListener {

	private LocationManager locationManager;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Criteria criteria = new Criteria();
				// criteria.setAccuracy(Criteria.ACCURACY_COARSE);
				// criteria.setCostAllowed(false);

				String provider = locationManager.getBestProvider(criteria,
						true);

				Log.d("", "provider = " + provider);

				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 0, 0,
						MainActivity.this);

				pd = new ProgressDialog(MainActivity.this);
				// インジケータのメッセージ
				pd.setMessage("登録中です");
				// インジケータのタイプ
				pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				// インジケータを表示
				pd.show();
			}
		});

		Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ServiceManager maker = new ServiceManager(MainActivity.this);
				maker.cancel();
			}
		});
		updateButton();

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (locationManager != null) {
			locationManager.removeUpdates(this);
		}
	}

	public boolean isRegisteredService() {
		ServiceManager maker = new ServiceManager(MainActivity.this);
		return maker.isRegisteredService();
	}

	public void updateButton() {

		Button button1 = (Button) findViewById(R.id.button1);
		Button button2 = (Button) findViewById(R.id.button2);
		if (isRegisteredService()) {
			button1.setEnabled(false);
			button2.setEnabled(true);
		} else {
			button1.setEnabled(true);
			button2.setEnabled(false);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d("", "onLocationChanged");
		Log.d("", location.getLatitude() + "," + location.getLongitude());
		pd.dismiss();
		locationManager.removeUpdates(this);

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = pref.edit();
		editor.putFloat("latitude", (float) location.getLatitude());
		editor.putFloat("longitude", (float) location.getLongitude());
		editor.commit();

		TimePicker picker = (TimePicker) findViewById(R.id.timePicker1);
		int hour = picker.getCurrentHour();
		int minute = picker.getCurrentMinute();

		ServiceManager maker = new ServiceManager(MainActivity.this);
		maker.schedule(hour, minute);

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
