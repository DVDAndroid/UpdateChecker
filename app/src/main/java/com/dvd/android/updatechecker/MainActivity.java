package com.dvd.android.updatechecker;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	public static ListPreference mListPreferenceColor;
	PackageInfo pInfo;

	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		if (level == TRIM_MEMORY_UI_HIDDEN) {
			this.finish();
		}
	}

	@Override
	@SuppressLint("CutPasteId")
	@SuppressWarnings({ "deprecation", "static-access" })
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.main);
		setContentView(R.layout.activity_main);

		mListPreferenceColor = (ListPreference) getPreferenceScreen()
				.findPreference(Utils.KEY_LIST_PREFERENCE_COLOR);

		final SharedPreferences prefs = getPreferenceManager()
				.getDefaultSharedPreferences(this);

		if (prefs.getBoolean("welcome", true)) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.welcome), Utils.duration).show();

			prefs.edit().putString(Utils.KEY_LIST_PREFERENCE_ICONS, "3")
					.apply();

			prefs.edit().putString(Utils.KEY_CHOOSE_PLAT, "2").apply();
			prefs.edit().putBoolean(Utils.KEY_AUTO_UP, true).apply();
			prefs.edit().putString("new_ver_line", "NO").apply();
			prefs.edit().putBoolean(Utils.KEY_CHECK_BOX_NO_ADD, true).apply();
			prefs.edit().putBoolean(Utils.KEY_CHECK_BOX_RAND_COLOR, true)
					.apply();
			prefs.edit().putBoolean(Utils.KEY_CHECK_BOX_RAND_COLOR_ACT, true)
					.apply();

			prefs.edit().putString(Utils.KEY_KK_LETTER, "K").apply();
			prefs.edit()
					.putString(Utils.KEY_KK_TEXT,
							"ANDROID " + Build.VERSION.RELEASE).apply();
			prefs.edit()
					.putString(Utils.KEY_KK_INTERPOLATOR,
							getString(R.string.def_anim)).apply();
			prefs.edit().putString(Utils.KEY_KK_CLICKS, "6").apply();
			prefs.edit()
					.putString(Utils.KEY_KK_SYSUI, getString(R.string.none))
					.apply();

			prefs.edit().putString(Utils.KEY_L_SYSUI, getString(R.string.none))
					.apply();

			if (!Build.MODEL.equals("Nexus 5")) {
				Toast.makeText(getApplicationContext(),
						getString(R.string.noNexus5), Toast.LENGTH_LONG).show();
			}

			if (isPackageInstalled(Utils.TSB_1)
					|| isPackageInstalled(Utils.TSB_2)) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						MainActivity.this);

				alertDialogBuilder.setTitle(getApplicationContext().getString(
						R.string.warn));
				alertDialogBuilder
						.setMessage(getApplicationContext().getString(
								R.string.warn)
								+ " "
								+ getApplicationContext().getString(
										R.string.module_ok));
				alertDialogBuilder.setPositiveButton(android.R.string.ok, null);
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.setCancelable(false);
				alertDialog.show();

			}

			prefs.edit().putBoolean("welcome", false).apply();
		}

		if (isPackageInstalled("com.dvd.updatechecker")) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					MainActivity.this);

			alertDialogBuilder.setTitle(getApplicationContext().getString(
					R.string.warn));
			alertDialogBuilder.setMessage(getString(R.string.old_ver));
			alertDialogBuilder.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int id) {

							Uri packageURI = Uri.parse("package:"
									+ "com.dvd.updatechecker");
							Intent uninstallIntent = new Intent(
									Intent.ACTION_DELETE, packageURI);
							startActivity(uninstallIntent);

						}
					});

			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.setCancelable(false);
			alertDialog.show();
		}

		if (prefs.getBoolean(Utils.KEY_CHECK_BOX_RAND_COLOR_ACT, true)) {

			Random rnd = new Random();
			int colorMain = getRandomWithExclusion(rnd, 1, 19, 2, 18, 12, 13);

			int colorInfo = getRandomWithExclusion(rnd, 1, 19, colorMain, 2,
					18, 12, 13);
			int colorSysInfo = getRandomWithExclusion(rnd, 1, 19, colorMain,
					colorInfo, 2, 18, 12, 13);
			int colorSettings = getRandomWithExclusion(rnd, 1, 19, colorMain,
					colorInfo, colorSysInfo, 2, 18, 12, 13);

			String sColorMain = Integer.toString(colorMain);
			String sColorInfo = Integer.toString(colorInfo);
			String sColorSysInfo = Integer.toString(colorSysInfo);
			String sColorSettings = Integer.toString(colorSettings);

			mListPreferenceColor.setValue(sColorMain);
			prefs.edit().putString(Utils.KEY_LIST_PREFERENCE_COLOR, sColorMain)
					.apply();
			prefs.edit().putString("colorInfo", sColorInfo).apply();
			prefs.edit().putString("colorSysInfo", sColorSysInfo).apply();
			prefs.edit().putString("colorSettings", sColorSettings).apply();

		} else {
			if (prefs.getBoolean(Utils.KEY_CHECK_BOX_RAND_COLOR, true)) {
				Random rnd = new Random();
				int colorMain = getRandomWithExclusion(rnd, 17170450, 17170461);
				String sColorMain = Integer.toString(colorMain);

				mListPreferenceColor.setValue(sColorMain);
			}
		}

		if (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, null).equals(
				Utils.KEY_ICON_JB)) {
			try {
				getActionBar().setIcon(R.drawable.ic_launcher_settings_jb);
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}

		if (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, null).equals(
				Utils.KEY_ICON_KK)) {

			getActionBar().setIcon(R.drawable.ic_launcher_settings_kk);

		}

		if (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, null).equals(
				Utils.KEY_ICON_L)) {

			getActionBar().setIcon(R.drawable.ic_launcher_settings_l);

		}

		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		TextView tv1 = (TextView) findViewById(R.id.version);
		tv1.setText(" " + getApplicationContext().getString(R.string.vers)
				+ " " + pInfo.versionName + "-" + pInfo.versionCode);

		TextView tv2 = (TextView) findViewById(R.id.credit);
		tv2.setText("by " + getApplicationContext().getString(R.string.my_name)
				+ " ");

		if (prefs.getBoolean(Utils.KEY_AUTO_UP, true)) {

			setProgressBarIndeterminateVisibility(true);

			DownloadFilesTask task = new DownloadFilesTask();
			task.execute("test");

		}

		Utils.applyColor(MainActivity.this, mListPreferenceColor.getValue());

	}

	@Override
	@SuppressWarnings("deprecation")
	protected void onResume() {
		super.onResume();

		mListPreferenceColor.setSummary(getApplicationContext().getString(
				R.string.this_color)
				+ " " + mListPreferenceColor.getEntry().toString());

		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		setProgressBarIndeterminateVisibility(false);

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
		super.onPause();

		// Unregister the listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	public void check(View view) {

		startActivity(new Intent(Intent.ACTION_MAIN).setClassName(
				getPackageName(), getPackageName() + ".UpdateActivity"));

	}

	public int getRandomWithExclusion(Random rnd, int start, int end,
			int... exclude) {
		int random = start + rnd.nextInt(end - start + 1 - exclude.length);
		for (int ex : exclude) {
			if (random < ex) {
				break;
			}
			random++;
		}
		return random;
	}

	private boolean isPackageInstalled(String packagename) {
		PackageManager pm = getPackageManager();
		try {
			pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

		mListPreferenceColor.setSummary(getApplicationContext().getString(
				R.string.this_color)
				+ " " + mListPreferenceColor.getEntry().toString());

		Utils.applyColor(MainActivity.this, mListPreferenceColor.getValue());

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
		if (id == R.id.action_info) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setComponent(new ComponentName(
					"com.dvd.android.updatechecker",
					"com.dvd.android.updatechecker.InfoActivity"));
			startActivity(intent);
			return true;
		}
		if (id == R.id.action_exit) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private class DownloadFilesTask extends AsyncTask<String, Integer, Long> {

		@Override
		protected Long doInBackground(String... params) {
			final String urlLink = "https://sites.google.com/site/dvdandroid99/ver.txt?attredirects=0&d=1";
			final String fileName = "ver.txt";
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(MainActivity.this);
			try {

				try {
					URL url = new URL(urlLink);
					URLConnection connection = url.openConnection();
					connection.connect();
					InputStream input = new BufferedInputStream(
							url.openStream());
					OutputStream output = new FileOutputStream(getFilesDir()
							+ "/" + fileName);

					byte data[] = new byte[1024];
					int count;
					while ((count = input.read(data)) != -1) {
						output.write(data, 0, count);
					}

					output.flush();
					output.close();
					input.close();

				} catch (IOException e1) {
					deleteFile(getFilesDir() + "/" + fileName);
				}

				try {
					pInfo = getPackageManager().getPackageInfo(
							getPackageName(), 0);
				} catch (NameNotFoundException e1) {
					e1.printStackTrace();
				}

				BufferedReader br = null;
				try {
					br = new BufferedReader(new FileReader(new File(
							getFilesDir() + "/" + fileName)));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				String line = "";
				try {
					line = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (!line.equals(pInfo.versionName)) {

					prefs.edit().putString("new_ver_line", line).apply();

					if (line.contains("alpha") || line.contains("beta")) {
						if (line.contains("alpha")) {
							prefs.edit().putString("st", "alpha").apply();
						}
						if (line.contains("beta")) {
							prefs.edit().putString("st", "beta").apply();
						}
					} else {
						prefs.edit().putString("st", "null").apply();
						long when = System.currentTimeMillis();
						NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
						Intent intent = new Intent(getApplicationContext(),
								InfoActivity.class);
						PendingIntent pending = PendingIntent.getActivity(
								getApplicationContext(), Utils.NOTIFICATION_ID,
								intent, 0);
						Notification notification;

						int priority = 0;
						if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
							priority = 2;
						}

						notification = new Notification.Builder(
								getApplicationContext())
								.setStyle(
										new Notification.BigTextStyle()
												.bigText(getString(R.string.yes_up_not)))
								.setContentTitle(getString(R.string.app_name))
								.setContentText(getString(R.string.yes_up_not))
								.setSmallIcon(R.drawable.ic_launcher_gsm)
								.setContentIntent(pending)
								.setWhen(when)
								.setPriority(priority)

								.setDefaults(
										Notification.DEFAULT_SOUND
												| Notification.DEFAULT_VIBRATE)

								.setAutoCancel(true).build();

						notification.flags |= Notification.FLAG_AUTO_CANCEL;
						nm.notify(Utils.NOTIFICATION_ID, notification);
					}
				} else {
					prefs.edit().putString("new_ver_line", "NO").apply();
				}

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (RuntimeException e) {
				Toast.makeText(MainActivity.this.getApplicationContext(),
						getString(R.string.no_int), Toast.LENGTH_SHORT).show();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Long result) {

			MainActivity.this.setProgressBarIndeterminateVisibility(false);

		}
	}
}