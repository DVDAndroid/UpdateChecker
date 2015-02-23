package com.dvd.android.updatechecker;

import java.util.Random;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.dvd.android.library.colorlistpreference.ColorListPreference;

public class MainActivity extends PreferenceActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener {

	private ColorListPreference mListPreferenceColor;
	private SharedPreferences prefs;
	private PackageInfo pInfo;

	@Override
	@SuppressWarnings({ "deprecation", "static-access" })
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.main);
		setContentView(R.layout.activity_main);

		mListPreferenceColor = (ColorListPreference) getPreferenceScreen()
				.findPreference(Utils.KEY_LIST_PREFERENCE_COLOR);

		prefs = getPreferenceManager().getDefaultSharedPreferences(this);

		if (prefs.getBoolean("welcome", true)) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.welcome), Utils.duration).show();

			prefs.edit().putBoolean("setts_opened", false).apply();

			prefs.edit()
					.putString(Utils.KEY_KK_TEXT,
							"ANDROID " + Build.VERSION.RELEASE).apply();

			prefs.edit().putString("new_ver_line", "NO").apply();

			if (!Build.MODEL.equals("Nexus 5")) {
				Toast.makeText(getApplicationContext(),
						getString(R.string.noNexus5), Toast.LENGTH_LONG).show();
			}

			if (Utils.isPackageInstalled(getApplicationContext(), Utils.TSB_1)
					|| Utils.isPackageInstalled(getApplicationContext(),
							Utils.TSB_2)) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						MainActivity.this);

				alertDialogBuilder.setTitle(getString(R.string.warn));
				alertDialogBuilder.setMessage(getString(R.string.warn) + " "
						+ getString(R.string.module_ok));
				alertDialogBuilder.setPositiveButton(android.R.string.ok, null);
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.setCancelable(false);
				alertDialog.show();

			}
			prefs.edit().putBoolean("welcome", false).apply();
		}

		if (Utils.isPackageInstalled(this, "com.dvd.updatechecker")) {
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
			int colorInfo = Utils.getRandomWithExclusion(rnd, 1, 19, 2, 12, 13);
			int colorSysInfo = Utils.getRandomWithExclusion(rnd, 1, 19,
					colorInfo, 2, 12, 13);
			int colorSettings = Utils.getRandomWithExclusion(rnd, 1, 19,
					colorInfo, colorSysInfo, 2, 12, 13);

			String sColorInfo = Integer.toString(colorInfo);
			String sColorSysInfo = Integer.toString(colorSysInfo);
			String sColorSettings = Integer.toString(colorSettings);

			prefs.edit().putString("colorInfo", sColorInfo).apply();
			prefs.edit().putString("colorSysInfo", sColorSysInfo).apply();
			prefs.edit().putString("colorSettings", sColorSettings).apply();

		} else {
			if (prefs.getBoolean(Utils.KEY_CHECK_BOX_RAND_COLOR, true)) {
				Random rnd = new Random();
				int colorMain = Utils.getRandomWithExclusion(rnd, 1, 19);
				String sColorMain = Integer.toString(colorMain);

				mListPreferenceColor.setValue(sColorMain);
				prefs.edit()
						.putString("colorInfo", mListPreferenceColor.getValue())
						.apply();
				prefs.edit()
						.putString("colorSysInfo",
								mListPreferenceColor.getValue()).apply();
				prefs.edit()
						.putString("colorSettings",
								mListPreferenceColor.getValue()).apply();
			} else {
				prefs.edit()
						.putString("colorInfo", mListPreferenceColor.getValue())
						.apply();
				prefs.edit()
						.putString("colorSysInfo",
								mListPreferenceColor.getValue()).apply();
				prefs.edit()
						.putString("colorSettings",
								mListPreferenceColor.getValue()).apply();
			}
		}

		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		TextView tv1 = (TextView) findViewById(R.id.version);
		tv1.setText(" " + getApplicationContext().getString(R.string.vers)
				+ " " + pInfo.versionName + "-" + pInfo.versionCode);

		TextView tv2 = (TextView) findViewById(R.id.credit);
		tv2.setText("by " + getApplicationContext().getString(R.string.my_name)
				+ " ");

		if (getActionBar() != null) {
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

		Utils.applyColor(this, mListPreferenceColor.getValue());
	}

	@Override
	@SuppressWarnings("deprecation")
	protected void onResume() {
		super.onResume();

		mListPreferenceColor.setSummary(getString(R.string.this_color) + " "
				+ mListPreferenceColor.getEntry().toString());

		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
		super.onPause();

		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

		mListPreferenceColor.setSummary(getString(R.string.this_color) + " "
				+ mListPreferenceColor.getEntry().toString());

		Utils.applyColor(this, mListPreferenceColor.getValue());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case R.id.action_info:
				startActivity(new Intent(Intent.ACTION_MAIN).setClass(this,
						InfoActivity.class));
				return true;
			case R.id.action_exit:
				finish();
		}
		return super.onOptionsItemSelected(item);
	}

	public void check(View view) {
		startActivity(new Intent(Intent.ACTION_MAIN).setClass(this,
				UpdateActivity.class));
	}
}
