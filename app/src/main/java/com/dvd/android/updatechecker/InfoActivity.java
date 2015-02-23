package com.dvd.android.updatechecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class InfoActivity extends PreferenceActivity {

	PackageInfo pInfo;
	private SharedPreferences prefs;

	@SuppressWarnings({ "deprecation", "static-access" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.info);

		prefs = getPreferenceManager().getDefaultSharedPreferences(this);

		if (getActionBar() != null) {
			getActionBar().setDisplayHomeAsUpEnabled(true);

			switch (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, "3")) {
				case "1":
					getActionBar().setIcon(R.mipmap.ic_launcher_settings_jb);
					break;
				case "2":
					getActionBar().setIcon(R.mipmap.ic_launcher_settings_kk);
					break;
				case "3":
					getActionBar().setIcon(R.mipmap.ic_launcher_settings_l);
					break;
			}
		}

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();

		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(Utils.NOTIFICATION_ID);

		Preference ver = findPreference("ver");
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		ver.setSummary(getString(R.string.vers) + " " + pInfo.versionName
				+ " - " + pInfo.versionCode);

		if (Build.VERSION.SDK_INT == 19) {
			findViewById(android.R.id.content).setPadding(0,
					config.getPixelInsetTop(true), config.getPixelInsetRight(),
					config.getPixelInsetBottom());
		}

		Utils.applyColor(this, prefs.getString("colorInfo", "#ff0000"));

	}

	@Override
	@SuppressWarnings("deprecated")
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		if (preference.getKey().equals(Utils.KEY_SYSTEMBARTINT)) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://github.com/jgilfelt/SystemBarTint"));
			startActivity(browserIntent);
		}
		if (preference.getKey().equals(Utils.KEY_ANDROID_OPENSOURCE)) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://android.googlesource.com/"));
			startActivity(browserIntent);
		}
		if (preference.getKey().equals(Utils.KEY_STACKOVERFLOW)) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://stackoverflow.com"));
			startActivity(browserIntent);
		}
		if (preference.getKey().equals(Utils.KEY_NINEOLDANDROIDS)) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://github.com/JakeWharton/NineOldAndroids"));
			startActivity(browserIntent);
		}
		if (preference.getKey().equals(Utils.KEY_SNACK_BAR)) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://github.com/MrEngineer13/SnackBar"));
			startActivity(browserIntent);
		}
		if (preference.getKey().equals(Utils.KEY_FAB)) {
			Intent browserIntent = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("https://github.com/futuresimple/android-floating-action-button"));
			startActivity(browserIntent);
		}
		if (preference.getKey().equals(Utils.KEY_EGGSTER)) {
			Intent browserIntent = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("http://repo.xposed.info/module/areeb.xposed.eggster"));
			startActivity(browserIntent);
		}
		if (preference.getKey().equals(Utils.KEY_GITHUB)) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://github.com/dvdandroid/UpdateChecker"));
			startActivity(browserIntent);
		}
		if (preference.getKey().equals(Utils.KEY_CHECKING)) {
			final AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle(getString(R.string.myappsupdater));
			alert.setNegativeButton(getString(android.R.string.cancel),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			alert.setCancelable(false);

			if (Utils.isPackageInstalled(getApplicationContext(),
					"com.dvd.android.myappsupdater")) {
				alert.setMessage(String.format(
						getString(R.string.descr_myappsupdater),
						getString(R.string.open)));
				alert.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								startActivity(new Intent(Intent.ACTION_MAIN)
										.setClassName(
												"com.dvd.android.myappsupdater",
												"com.dvd.android.myappsupdater.MainActivity"));
							}
						});
			} else {
				alert.setMessage(String.format(
						getString(R.string.descr_myappsupdater),
						getString(R.string.install)));
				alert.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								new DownloadApp()
										.execute("https://github.com/DVDAndroid/dvdandroid.github.io/raw/master/apks/MyAppsUpdater.apk");
							}
						});
			}
			alert.show();
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}

	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		Intent shortcutIntent = new Intent(getApplicationContext(),
				MainActivity.class);
		Intent shortcutIntent1 = new Intent(getApplicationContext(),
				UpdateActivity.class);
		shortcutIntent.setAction(Intent.ACTION_MAIN);
		shortcutIntent1.setAction(Intent.ACTION_MAIN);

		Intent addIntent = new Intent();
		Intent addIntent1 = new Intent();

		switch (id) {
			case R.id.sett:
				startActivity(new Intent(Intent.ACTION_MAIN).setClass(this,
						SettingsActivity.class));
				break;
			case R.id.action_add:
				addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
						R.string.app_name);
				addIntent1.putExtra(Intent.EXTRA_SHORTCUT_NAME,
						R.string.verifica);
				switch (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, null)) {
					case "1":
						addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
								Intent.ShortcutIconResource.fromContext(
										getApplicationContext(),
										R.mipmap.ic_launcher_settings_jb));
						break;
					case "2":
						addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
								Intent.ShortcutIconResource.fromContext(
										getApplicationContext(),
										R.mipmap.ic_launcher_settings_kk));
						break;
					case "3":
						addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
								Intent.ShortcutIconResource.fromContext(
										getApplicationContext(),
										R.mipmap.ic_launcher_settings_l));
						break;
				}

				addIntent1.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
						Intent.ShortcutIconResource.fromContext(
								getApplicationContext(),
								R.mipmap.ic_launcher_gsm));

				addIntent
						.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
				addIntent1.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
						shortcutIntent1);

				addIntent
						.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
				addIntent1
						.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

				getApplicationContext().sendBroadcast(addIntent);

				String text = String.format(
						getResources().getString(R.string.add_short_text),
						getString(R.string.app_name));

				Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG)
						.show();

				if (prefs.getBoolean(Utils.KEY_CHECK_BOX_NO_ADD, false)) {
					getApplicationContext().sendBroadcast(addIntent1);

					String text1 = String.format(
							getResources().getString(R.string.add_short_text),
							getString(R.string.title_activity_update));

					Toast.makeText(getApplicationContext(), text1,
							Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.action_remove:

				addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
						getApplicationContext().getString(R.string.app_name));
				addIntent1.putExtra(
						Intent.EXTRA_SHORTCUT_NAME,
						getApplicationContext().getString(
								R.string.title_activity_update));

				addIntent
						.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
				addIntent1.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
						shortcutIntent1);

				addIntent
						.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
				addIntent1
						.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
				getApplicationContext().sendBroadcast(addIntent);
				getApplicationContext().sendBroadcast(addIntent1);
				break;
			case R.id.sys_info:
				startActivity(new Intent(Intent.ACTION_MAIN).setClass(this,
						SysInfoActivity.class));
				break;
			case android.R.id.home:
				finish();
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	public class DownloadApp extends AsyncTask<String, Void, Boolean> {
		private Context context;
		private ProgressDialog progressDialog;

		public void setContext(Activity a) {
			this.context = a.getApplicationContext();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressDialog = ProgressDialog.show(InfoActivity.this, "",
					"Download APK...", true);
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			try {
				URL url = new URL(arg0[0]);
				HttpURLConnection c = (HttpURLConnection) url.openConnection();
				c.setRequestMethod("GET");
				c.setDoOutput(true);
				c.connect();

				File file = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/download/");
				File outputFile = new File(file, context.getResources()
						.getString(R.string.app_name) + ".apk");

				FileOutputStream fos = new FileOutputStream(outputFile);
				InputStream is = c.getInputStream();

				byte[] buffer = new byte[1024];
				int l;
				while ((l = is.read(buffer)) != -1) {
					fos.write(buffer, 0, l);
				}
				fos.flush();
				fos.getFD().sync();
				fos.close();
				is.close();

				return true;
			} catch (FileNotFoundException e) {
				progressDialog
						.setOnDismissListener(new DialogInterface.OnDismissListener() {
							@Override
							public void onDismiss(DialogInterface dialog) {
								Toast.makeText(getApplicationContext(),
										"an error is occurred",
										Toast.LENGTH_SHORT).show();
							}
						});
				return false;
			} catch (IllegalArgumentException e) {
				return false;
			} catch (IOException e) {
				progressDialog
						.setOnDismissListener(new DialogInterface.OnDismissListener() {

							@Override
							public void onDismiss(DialogInterface dialog) {
								Toast.makeText(getApplicationContext(),
										getString(R.string.no_int),
										Toast.LENGTH_SHORT).show();
							}

						});
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			progressDialog.dismiss();

			if (result) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File(Environment
						.getExternalStorageDirectory().getAbsolutePath()
						+ "/download/"
						+ context.getResources().getString(R.string.app_name)
						+ ".apk")), "application/vnd.android.package-archive");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		}
	}

}
