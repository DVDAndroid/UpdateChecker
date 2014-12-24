package com.dvd.android.updatechecker;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	public static ListPreference mListPreferenceIcons;
	public static CheckBoxPreference mCheckBoxActionBar;
	public static CheckBoxPreference mCheckBoxRandomColors;
	public static CheckBoxPreference mCheckBoxRandomColorsAct;

	public static EditTextPreference mKkLetter;
	public static EditTextPreference mKkText;
	public static ListPreference mKkInterpolator;
	public static EditTextPreference mKkClicks;
	public static ListPreference mKkSysUi;
	public static ListPreference mLSysUi;
	public static ListPreference mLLollipopChooser;

	@SuppressWarnings({ "deprecation", "static-access" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);

		SharedPreferences prefs = getPreferenceManager()
				.getDefaultSharedPreferences(this);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		mListPreferenceIcons = (ListPreference) getPreferenceScreen()
				.findPreference(Utils.KEY_LIST_PREFERENCE_ICONS);

		mCheckBoxRandomColors = (CheckBoxPreference) getPreferenceScreen()
				.findPreference(Utils.KEY_CHECK_BOX_RAND_COLOR);

		mCheckBoxRandomColorsAct = (CheckBoxPreference) getPreferenceScreen()
				.findPreference(Utils.KEY_CHECK_BOX_RAND_COLOR_ACT);

		if (!mCheckBoxRandomColors.isChecked()) {
			prefs.edit()
					.putString(Utils.KEY_LIST_PREFERENCE_COLOR,
							prefs.getString("colorSettings", null)).commit();
			mCheckBoxRandomColorsAct.setEnabled(false);
		}

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			mCheckBoxActionBar.setEnabled(false);
		}

		if (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, null).equals(
				Utils.KEY_ICON_JB)) {

			getActionBar().setIcon(R.drawable.ic_launcher_settings_jb);

		}

		if (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, null).equals(
				Utils.KEY_ICON_KK)) {

			getActionBar().setIcon(R.drawable.ic_launcher_settings_kk);

		}

		if (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, null).equals(
				Utils.KEY_ICON_L)) {

			getActionBar().setIcon(R.drawable.ic_launcher_settings_l);

		}

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();

		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
			PreferenceCategory fakeHeader = new PreferenceCategory(this);
			getPreferenceScreen().addPreference(fakeHeader);
			addPreferencesFromResource(R.xml.pref_kk);

		} else {
			if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
				PreferenceCategory fakeHeader = new PreferenceCategory(this);
				getPreferenceScreen().addPreference(fakeHeader);
				addPreferencesFromResource(R.xml.pref_l);
			}
		}

		if (prefs.getBoolean(Utils.KEY_CHECK_BOX_RAND_COLOR_ACT, true)) {

			prefs.edit()
					.putString(Utils.KEY_LIST_PREFERENCE_COLOR,
							prefs.getString("colorSettings", null)).commit();
		}

		Utils.applyColor(SettingsActivity.this,
				prefs.getString(Utils.KEY_LIST_PREFERENCE_COLOR, null));

		if (Build.VERSION.SDK_INT == 19) {
			findViewById(android.R.id.content).setPadding(0,
					config.getPixelInsetTop(true), config.getPixelInsetRight(),
					config.getPixelInsetBottom());
		}

		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
			findPreference(Utils.KEY_L_SYSUI).setEnabled(
					!prefs.getString(Utils.KEY_CHOOSE_PLAT, null).equals("2"));
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {

		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
			mKkLetter = (EditTextPreference) getPreferenceScreen()
					.findPreference(Utils.KEY_KK_LETTER);

			mKkText = (EditTextPreference) getPreferenceScreen()
					.findPreference(Utils.KEY_KK_TEXT);

			mKkInterpolator = (ListPreference) getPreferenceScreen()
					.findPreference(Utils.KEY_KK_INTERPOLATOR);

			mKkClicks = (EditTextPreference) getPreferenceScreen()
					.findPreference(Utils.KEY_KK_CLICKS);

			mKkSysUi = (ListPreference) getPreferenceScreen().findPreference(
					Utils.KEY_KK_SYSUI);

			mKkLetter.setSummary(mKkLetter.getText());
			mKkText.setSummary(mKkText.getText());
			mKkInterpolator.setSummary(mKkInterpolator.getEntry().toString());
			mKkClicks.setSummary(mKkClicks.getText());
			mKkSysUi.setSummary(mKkSysUi.getEntry().toString());
		} else {
			mLSysUi = (ListPreference) getPreferenceScreen().findPreference(
					Utils.KEY_L_SYSUI);
			mLSysUi.setSummary(mLSysUi.getEntry().toString());

			mLLollipopChooser = (ListPreference) getPreferenceScreen()
					.findPreference(Utils.KEY_CHOOSE_PLAT);
			mLLollipopChooser.setSummary(mLLollipopChooser.getEntry()
					.toString());
		}
		mListPreferenceIcons.setSummary(getApplicationContext().getString(
				R.string.curr_icon)
				+ " " + mListPreferenceIcons.getEntry().toString());

		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
		super.onResume();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
		super.onPause();
		// Unregister the listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
		}

		if (id == R.id.restart) {
			restart();
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

		SharedPreferences prefs = getPreferenceManager()
				.getDefaultSharedPreferences(this);

		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
			findPreference(Utils.KEY_L_SYSUI).setEnabled(
					!prefs.getString(Utils.KEY_CHOOSE_PLAT, null).equals("2"));
		}

		mListPreferenceIcons = (ListPreference) getPreferenceScreen()
				.findPreference(Utils.KEY_LIST_PREFERENCE_ICONS);

		mCheckBoxRandomColors = (CheckBoxPreference) getPreferenceScreen()
				.findPreference(Utils.KEY_CHECK_BOX_RAND_COLOR);

		mCheckBoxRandomColorsAct = (CheckBoxPreference) getPreferenceScreen()
				.findPreference(Utils.KEY_CHECK_BOX_RAND_COLOR_ACT);

		mLLollipopChooser = (ListPreference) getPreferenceScreen()
				.findPreference(Utils.KEY_CHOOSE_PLAT);

		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
			mKkLetter = (EditTextPreference) getPreferenceScreen()
					.findPreference(Utils.KEY_KK_LETTER);

			mKkText = (EditTextPreference) getPreferenceScreen()
					.findPreference(Utils.KEY_KK_TEXT);

			mKkInterpolator = (ListPreference) getPreferenceScreen()
					.findPreference(Utils.KEY_KK_INTERPOLATOR);

			mKkClicks = (EditTextPreference) getPreferenceScreen()
					.findPreference(Utils.KEY_KK_CLICKS);

			mKkSysUi = (ListPreference) getPreferenceScreen().findPreference(
					Utils.KEY_KK_SYSUI);

			mKkLetter.setSummary(mKkLetter.getText().toString());
			mKkText.setSummary(mKkText.getText().toString());
			mKkInterpolator.setSummary(mKkInterpolator.getEntry().toString());
			mKkClicks.setSummary(mKkClicks.getText().toString());
			mKkSysUi.setSummary(mKkSysUi.getEntry().toString());
		} else {

			mLSysUi = (ListPreference) getPreferenceScreen().findPreference(
					Utils.KEY_L_SYSUI);
			mLSysUi.setSummary(mLSysUi.getEntry().toString());

			mLLollipopChooser.setSummary(mLLollipopChooser.getEntry()
					.toString());
		}

		if (key.equals(Utils.KEY_CHECK_BOX_RAND_COLOR)) {

			if (mCheckBoxRandomColors.isChecked()) {
				mCheckBoxRandomColorsAct.setEnabled(true);
			} else {
				prefs.edit().putString(Utils.KEY_LIST_PREFERENCE_COLOR,
						prefs.getString(Utils.KEY_LIST_PREFERENCE_COLOR, null));
				mCheckBoxRandomColorsAct.setChecked(false);
				mCheckBoxRandomColorsAct.setEnabled(false);
			}
		}

		if (key.equals(Utils.KEY_CHECK_BOX_HIDE_ICON)) {

			if (prefs.getBoolean(Utils.KEY_CHECK_BOX_HIDE_ICON, true)) {

				getPackageManager()
						.setComponentEnabledSetting(
								new ComponentName(
										"com.dvd.android.updatechecker",
										"com.dvd.android.updatechecker.UpdateActivity-sh"),
								PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
								PackageManager.DONT_KILL_APP);

				restart();

			} else {

				getPackageManager()
						.setComponentEnabledSetting(
								new ComponentName(
										"com.dvd.android.updatechecker",
										"com.dvd.android.updatechecker.UpdateActivity-sh"),
								PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
								PackageManager.DONT_KILL_APP);

				restart();

			}

		}

		if (key.equals(Utils.KEY_LIST_PREFERENCE_ICONS)) {

			mListPreferenceIcons.setSummary(getApplicationContext().getString(
					R.string.curr_icon)
					+ " " + mListPreferenceIcons.getEntry().toString());

			getPackageManager().setComponentEnabledSetting(
					new ComponentName("com.dvd.android.updatechecker",
							"com.dvd.android.updatechecker.MainActivity-JB"),
					PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
					PackageManager.DONT_KILL_APP);

			getPackageManager().setComponentEnabledSetting(
					new ComponentName("com.dvd.android.updatechecker",
							"com.dvd.android.updatechecker.MainActivity-KK"),
					PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
					PackageManager.DONT_KILL_APP);

			getPackageManager().setComponentEnabledSetting(
					new ComponentName("com.dvd.android.updatechecker",
							"com.dvd.android.updatechecker.MainActivity-L"),
					PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
					PackageManager.DONT_KILL_APP);

			if (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, null).equals(
					"1")) {

				getPackageManager()
						.setComponentEnabledSetting(
								new ComponentName(
										"com.dvd.android.updatechecker",
										"com.dvd.android.updatechecker.MainActivity-JB"),
								PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
								PackageManager.DONT_KILL_APP);

				restart();

			}

			if (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, null).equals(
					"2")) {

				getPackageManager()
						.setComponentEnabledSetting(
								new ComponentName(
										"com.dvd.android.updatechecker",
										"com.dvd.android.updatechecker.MainActivity-KK"),
								PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
								PackageManager.DONT_KILL_APP);

				restart();

			}

			if (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, null).equals(
					"3")) {

				getPackageManager()
						.setComponentEnabledSetting(
								new ComponentName(
										"com.dvd.android.updatechecker",
										"com.dvd.android.updatechecker.MainActivity-L"),
								PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
								PackageManager.DONT_KILL_APP);

				restart();

			}

		}

	}

	public void restart() {
		PendingIntent RESTART_INTENT = PendingIntent.getActivity(
				this.getBaseContext(), 0, new Intent(getIntent()), 0);

		AlarmManager mgr = (AlarmManager) this
				.getSystemService(Context.ALARM_SERVICE);
		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
				RESTART_INTENT);
		System.exit(2);
	}
}
