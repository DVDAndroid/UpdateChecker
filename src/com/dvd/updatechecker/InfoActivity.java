package com.dvd.updatechecker;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

public class InfoActivity extends PreferenceActivity {

	PackageInfo pInfo;

	@SuppressWarnings({ "deprecation", "static-access" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.info);

		ActionBar actionBar = getActionBar();

		actionBar.setDisplayHomeAsUpEnabled(true);

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();

		SharedPreferences prefs = getPreferenceManager()
				.getDefaultSharedPreferences(this);

		if (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, null).equals(
				Utils.KEY_ICON_JB)) {

			actionBar.setIcon(R.drawable.ic_launcher_settings_jb);

		}

		if (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, null).equals(
				Utils.KEY_ICON_KK)) {

			actionBar.setIcon(R.drawable.ic_launcher_settings_kk);

		}

		if (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, null).equals(
				Utils.KEY_ICON_L)) {

			actionBar.setIcon(R.drawable.ic_launcher_settings_l);

		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			findViewById(android.R.id.content).setPadding(0,
					config.getPixelInsetTop(true), config.getPixelInsetRight(),
					config.getPixelInsetBottom());
		}

		Preference ver = findPreference("ver");
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ver.setSummary(prefs.getString("ver", pInfo.versionName + "-"
				+ pInfo.versionCode));

		if (prefs.getBoolean(Utils.KEY_CHECK_BOX_RAND_COLOR_ACT, true)) {

			prefs.edit()
					.putString(Utils.KEY_LIST_PREFERENCE_COLOR,
							prefs.getString("colorInfo", null)).commit();

		}

		applyColor(prefs.getString(Utils.KEY_LIST_PREFERENCE_COLOR, null));

	}

	public void applyColor(String color) {

		if (color.equals(Utils.KEY_MATERIAL_DARK)) {
			int actionBarColor = Color.parseColor("#ff37464E");

			this.getActionBar().setBackgroundDrawable(
					new ColorDrawable(actionBarColor));

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				SystemBarTintManager tintManager = new SystemBarTintManager(
						this);
				tintManager.setStatusBarTintEnabled(true);
				tintManager.setStatusBarTintColor(actionBarColor);
			}

		} else {
			if (color.equals(Utils.KEY_MATERIAL_LIGHT)) {
				int actionBarColor = Color.parseColor("#ff78909C");

				this.getActionBar().setBackgroundDrawable(
						new ColorDrawable(actionBarColor));

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
					SystemBarTintManager tintManager = new SystemBarTintManager(
							this);
					tintManager.setStatusBarTintEnabled(true);
					tintManager.setStatusBarTintColor(actionBarColor);
				}

			} else {
				int actionBarColor = this.getResources().getColor(
						Integer.valueOf(color));
				this.getActionBar().setBackgroundDrawable(
						new ColorDrawable(actionBarColor));

				if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)) {
					SystemBarTintManager tintManager = new SystemBarTintManager(
							this);
					tintManager.setStatusBarTintEnabled(true);
					tintManager.setStatusBarTintColor(actionBarColor);
				}
			}
		}

	}

	@Override
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

		if (preference.getKey().equals(Utils.KEY_EGGSTER)) {
			Intent browserIntent = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("http://repo.xposed.info/module/areeb.xposed.eggster"));
			startActivity(browserIntent);
		}

		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}

	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		SharedPreferences prefs = getPreferenceManager()
				.getDefaultSharedPreferences(this);

		if (id == R.id.sett) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setComponent(new ComponentName("com.dvd.updatechecker",
					"com.dvd.updatechecker.SettingsActivity"));
			startActivity(intent);
			;
		}

		if (id == R.id.action_add) {

			Intent shortcutIntent = new Intent(getApplicationContext(),
					MainActivity.class);
			Intent shortcutIntent1 = new Intent(getApplicationContext(),
					UpdateActivity.class);
			shortcutIntent.setAction(Intent.ACTION_MAIN);
			shortcutIntent1.setAction(Intent.ACTION_MAIN);

			Intent addIntent = new Intent();
			Intent addIntent1 = new Intent();

			addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, R.string.app_name);
			addIntent1.putExtra(Intent.EXTRA_SHORTCUT_NAME, R.string.verifica);

			if (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, null).equals(
					"1")) {

				addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
						Intent.ShortcutIconResource.fromContext(
								getApplicationContext(),
								R.drawable.ic_launcher_settings_jb));

			}

			if (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, null).equals(
					"2")) {

				addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
						Intent.ShortcutIconResource.fromContext(
								getApplicationContext(),
								R.drawable.ic_launcher_settings_kk));

			}

			if (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, null).equals(
					"3")) {

				addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
						Intent.ShortcutIconResource.fromContext(
								getApplicationContext(),
								R.drawable.ic_launcher_settings_l));

			}

			addIntent1.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
					Intent.ShortcutIconResource
							.fromContext(getApplicationContext(),
									R.drawable.ic_launcher_gsm));

			addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
			addIntent1.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent1);

			addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
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

			return true;
		}

		if (id == R.id.action_remove) {

			Intent shortcutIntent = new Intent(getApplicationContext(),
					MainActivity.class);
			Intent shortcutIntent1 = new Intent(getApplicationContext(),
					UpdateActivity.class);

			shortcutIntent.setAction(Intent.ACTION_MAIN);
			shortcutIntent1.setAction(Intent.ACTION_MAIN);

			Intent addIntent = new Intent();
			Intent addIntent1 = new Intent();

			addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
					getApplicationContext().getString(R.string.app_name));
			addIntent1.putExtra(
					Intent.EXTRA_SHORTCUT_NAME,
					getApplicationContext().getString(
							R.string.title_activity_update));

			addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
			addIntent1.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent1);

			addIntent
					.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
			addIntent1
					.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
			getApplicationContext().sendBroadcast(addIntent);
			getApplicationContext().sendBroadcast(addIntent1);

		}

		if (id == R.id.sys_info) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setComponent(new ComponentName("com.dvd.updatechecker",
					"com.dvd.updatechecker.SysInfoActivity"));
			startActivity(intent);
			;
		}

		if (id == android.R.id.home) {
			finish();
		}

		return super.onOptionsItemSelected(item);
	}

}