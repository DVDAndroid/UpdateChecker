package com.dvd.android.updatechecker;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
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

		prefs.edit().putBoolean("setts_opened", true).apply();

		initPrefs();

		if (!mCheckBoxRandomColors.isChecked()) {
			prefs.edit()
					.putString(Utils.KEY_LIST_PREFERENCE_COLOR,
							prefs.getString("colorSettings", null)).apply();
			mCheckBoxRandomColorsAct.setEnabled(false);
		}

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			mCheckBoxActionBar.setEnabled(false);
		}

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

		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
			PreferenceCategory fakeHeader = new PreferenceCategory(this);
			getPreferenceScreen().addPreference(fakeHeader);
			addPreferencesFromResource(R.xml.pref_kk);

		} else {
			if ((Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP)
					|| Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
				PreferenceCategory fakeHeader = new PreferenceCategory(this);
				getPreferenceScreen().addPreference(fakeHeader);
				addPreferencesFromResource(R.xml.pref_l);
			}
		}

		Utils.applyColor(this, prefs.getString("colorSettings", "#ff0000"));

		switch (Build.VERSION.SDK_INT) {
			case Build.VERSION_CODES.KITKAT:
				findViewById(android.R.id.content).setPadding(0,
						config.getPixelInsetTop(true),
						config.getPixelInsetRight(),
						config.getPixelInsetBottom());
				break;
			case Build.VERSION_CODES.LOLLIPOP:
			case Build.VERSION_CODES.LOLLIPOP_MR1:
				findPreference(Utils.KEY_L_SYSUI).setEnabled(
						!prefs.getString(Utils.KEY_CHOOSE_PLAT, null).equals(
								"2"));
				break;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {

		initPrefs();

		switch (Build.VERSION.SDK_INT) {
			case Build.VERSION_CODES.KITKAT:
				mKkLetter.setSummary(mKkLetter.getText());
				mKkText.setSummary(mKkText.getText());
				mKkInterpolator.setSummary(mKkInterpolator.getEntry()
						.toString());
				mKkClicks.setSummary(mKkClicks.getText());
				mKkSysUi.setSummary(mKkSysUi.getEntry().toString());
				break;
			case Build.VERSION_CODES.LOLLIPOP:
			case Build.VERSION_CODES.LOLLIPOP_MR1:
				mLLollipopChooser.setSummary(mLLollipopChooser.getEntry()
						.toString());
				mLSysUi.setSummary(mLSysUi.getEntry().toString());
				break;
		}

		mListPreferenceIcons.setSummary(getApplicationContext().getString(
				R.string.curr_icon)
				+ " " + mListPreferenceIcons.getEntry().toString());

		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
		super.onResume();
	}

	@SuppressWarnings("deprecation")
	private void initPrefs() {
		mListPreferenceIcons = (ListPreference) getPreferenceScreen()
				.findPreference(Utils.KEY_LIST_PREFERENCE_ICONS);

		mCheckBoxRandomColors = (CheckBoxPreference) getPreferenceScreen()
				.findPreference(Utils.KEY_CHECK_BOX_RAND_COLOR);

		mCheckBoxRandomColorsAct = (CheckBoxPreference) getPreferenceScreen()
				.findPreference(Utils.KEY_CHECK_BOX_RAND_COLOR_ACT);

		switch (Build.VERSION.SDK_INT) {
			case Build.VERSION_CODES.KITKAT:
				mKkLetter = (EditTextPreference) getPreferenceScreen()
						.findPreference(Utils.KEY_KK_LETTER);
				mKkText = (EditTextPreference) getPreferenceScreen()
						.findPreference(Utils.KEY_KK_TEXT);
				mKkInterpolator = (ListPreference) getPreferenceScreen()
						.findPreference(Utils.KEY_KK_INTERPOLATOR);
				mKkClicks = (EditTextPreference) getPreferenceScreen()
						.findPreference(Utils.KEY_KK_CLICKS);
				mKkSysUi = (ListPreference) getPreferenceScreen()
						.findPreference(Utils.KEY_KK_SYSUI);
				break;
			case Build.VERSION_CODES.LOLLIPOP:
			case Build.VERSION_CODES.LOLLIPOP_MR1:
				mLSysUi = (ListPreference) getPreferenceScreen()
						.findPreference(Utils.KEY_L_SYSUI);
				mLLollipopChooser = (ListPreference) getPreferenceScreen()
						.findPreference(Utils.KEY_CHOOSE_PLAT);
				break;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

		SharedPreferences prefs = getPreferenceManager()
				.getDefaultSharedPreferences(this);

		if ((Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP)
				|| Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
			findPreference(Utils.KEY_L_SYSUI).setEnabled(
					!prefs.getString(Utils.KEY_CHOOSE_PLAT, null).equals("2"));
		}

		initPrefs();

		switch (Build.VERSION.SDK_INT) {
			case Build.VERSION_CODES.KITKAT:
				mKkLetter.setSummary(mKkLetter.getText());
				mKkText.setSummary(mKkText.getText());
				mKkInterpolator.setSummary(mKkInterpolator.getEntry()
						.toString());
				mKkClicks.setSummary(mKkClicks.getText());
				mKkSysUi.setSummary(mKkSysUi.getEntry().toString());
				break;
			case Build.VERSION_CODES.LOLLIPOP:
			case Build.VERSION_CODES.LOLLIPOP_MR1:
				mLSysUi.setSummary(mLSysUi.getEntry().toString());
				mLLollipopChooser.setSummary(mLLollipopChooser.getEntry()
						.toString());
				break;
		}

		if (key.equals(Utils.KEY_CHECK_BOX_RAND_COLOR)) {

			if (mCheckBoxRandomColors.isChecked()) {
				mCheckBoxRandomColorsAct.setEnabled(true);
			} else {
				prefs.edit()
						.putString(
								Utils.KEY_LIST_PREFERENCE_COLOR,
								prefs.getString(
										Utils.KEY_LIST_PREFERENCE_COLOR, null))
						.apply();
				mCheckBoxRandomColorsAct.setChecked(false);
				mCheckBoxRandomColorsAct.setEnabled(false);
			}
		}

		if (key.equals(Utils.KEY_CHECK_BOX_HIDE_ICON)) {
			int checked;

			checked = prefs.getBoolean(Utils.KEY_CHECK_BOX_HIDE_ICON, true) ? 2
					: 1;

			getPackageManager().setComponentEnabledSetting(
					new ComponentName(this, getPackageName()
							+ ".UpdateActivity-sh"), checked,
					PackageManager.DONT_KILL_APP);

			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			ResolveInfo resolveInfo = getPackageManager().resolveActivity(
					intent, PackageManager.MATCH_DEFAULT_ONLY);
			String currentHomePackage = resolveInfo.activityInfo.packageName;

			ActivityManager am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
			am.killBackgroundProcesses(currentHomePackage);
		}

		if (key.equals(Utils.KEY_LIST_PREFERENCE_ICONS)) {

			mListPreferenceIcons.setSummary(getApplicationContext().getString(
					R.string.curr_icon)
					+ " " + mListPreferenceIcons.getEntry().toString());

			getPackageManager().setComponentEnabledSetting(
					new ComponentName(this, getPackageName()
							+ ".MainActivity-JB"),
					PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
					PackageManager.DONT_KILL_APP);

			getPackageManager().setComponentEnabledSetting(
					new ComponentName(this, getPackageName()
							+ ".MainActivity-KK"),
					PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
					PackageManager.DONT_KILL_APP);

			getPackageManager().setComponentEnabledSetting(
					new ComponentName(this, getPackageName()
							+ ".MainActivity-L"),
					PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
					PackageManager.DONT_KILL_APP);

			String act = "L";
			switch (prefs.getString(Utils.KEY_LIST_PREFERENCE_ICONS, "3")) {
				case "1":
					act = "JB";
					break;
				case "2":
					act = "KK";
					break;
				case "3":
					act = "L";
					break;
			}

			getPackageManager().setComponentEnabledSetting(
					new ComponentName(this, getPackageName() + ".MainActivity-"
							+ act),
					PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 0);

		}

	}

}
