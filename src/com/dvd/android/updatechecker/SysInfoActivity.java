package com.dvd.android.updatechecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.MenuItem;

public class SysInfoActivity extends PreferenceActivity {

	long[] mHits = new long[3];

	@SuppressWarnings({ "deprecation", "static-access" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.sys_info);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		Preference release = findPreference(Utils.KEY_SYS_INFO_RELEASE);

		if ((Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN
				| Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1 | Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2)) {
			String ver = "JellyBean ";
			release.setSummary(ver + Build.VERSION.RELEASE);
		} else {

			if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
				String ver = "KitKat ";
				release.setSummary(ver + Build.VERSION.RELEASE);
			} else {
				release.setSummary(String.valueOf(Build.VERSION.CODENAME));
			}
		}

		SharedPreferences prefs = getPreferenceManager()
				.getDefaultSharedPreferences(this);

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();

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

			findViewById(android.R.id.content).setPadding(
					config.getPixelInsetRight(), config.getPixelInsetTop(true),
					config.getPixelInsetRight(), 0);
		}

		if (prefs.getBoolean(Utils.KEY_CHECK_BOX_RAND_COLOR_ACT, true)) {

			prefs.edit()
					.putString(Utils.KEY_LIST_PREFERENCE_COLOR,
							prefs.getString("colorSysInfo", null)).commit();
		}

		applyColor(prefs.getString(Utils.KEY_LIST_PREFERENCE_COLOR, null));

		try {
			setSummaries();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "deprecation" })
	public void setSummaries() throws IOException {

		Preference board = findPreference(Utils.KEY_SYS_INFO_BOARD);
		Preference bootloader = findPreference(Utils.KEY_SYS_INFO_BOOTLOADER);
		Preference cpu = findPreference(Utils.KEY_SYS_INFO_CPU);
		Preference device = findPreference(Utils.KEY_SYS_INFO_DEVICE);
		Preference display = findPreference(Utils.KEY_SYS_INFO_DISPLAY);
		Preference fingerprint = findPreference(Utils.KEY_SYS_INFO_FINGERPRINT);
		Preference hardware = findPreference(Utils.KEY_SYS_INFO_HARDWARE);
		Preference host = findPreference(Utils.KEY_SYS_INFO_HOST);
		Preference id = findPreference(Utils.KEY_SYS_INFO_ID);
		Preference manufacturer = findPreference(Utils.KEY_SYS_INFO_MANUFACTURER);
		Preference model = findPreference(Utils.KEY_SYS_INFO_MODEL);
		Preference product = findPreference(Utils.KEY_SYS_INFO_PRODUCT);
		Preference radio = findPreference(Utils.KEY_SYS_INFO_RADIO);
		Preference serial = findPreference(Utils.KEY_SYS_INFO_SERIAL);
		Preference tags = findPreference(Utils.KEY_SYS_INFO_TAGS);
		Preference time = findPreference(Utils.KEY_SYS_INFO_TIME);
		Preference type = findPreference(Utils.KEY_SYS_INFO_TYPE);
		Preference user = findPreference(Utils.KEY_SYS_INFO_USER);
		Preference codename = findPreference(Utils.KEY_SYS_INFO_CODENAME);
		Preference incremental = findPreference(Utils.KEY_SYS_INFO_INCREMENTAL);

		Preference api = findPreference(Utils.KEY_SYS_INFO_API);
		Preference kernel = findPreference(Utils.KEY_SYS_INFO_KERNEL);
		Preference root = findPreference(Utils.KEY_SYS_INFO_ROOT);
		Preference xposed = findPreference(Utils.KEY_SYS_INFO_XPOSED);
		Preference busybox = findPreference(Utils.KEY_SYS_INFO_BUSYBOX);

		kernel.setSummary((System.getProperty("os.version")));
		board.setSummary(Build.BOARD);
		bootloader.setSummary(Build.BOOTLOADER);
		cpu.setSummary(Build.CPU_ABI + ", " + Build.CPU_ABI2);
		device.setSummary(Build.DEVICE);
		display.setSummary(Build.DISPLAY);
		fingerprint.setSummary(Build.FINGERPRINT);
		hardware.setSummary(Build.HARDWARE);
		host.setSummary(Build.HOST);
		id.setSummary(Build.ID);
		manufacturer.setSummary(Build.MANUFACTURER);
		model.setSummary(Build.MODEL);
		product.setSummary(Build.PRODUCT);
		radio.setSummary(Build.getRadioVersion());
		serial.setSummary(Build.SERIAL);
		tags.setSummary(Build.TAGS);
		time.setSummary(String.valueOf(Build.TIME));
		type.setSummary(Build.TYPE);
		user.setSummary(Build.USER);
		codename.setSummary(Build.VERSION.CODENAME);
		incremental.setSummary(Build.VERSION.INCREMENTAL);
		api.setSummary(String.valueOf(Build.VERSION.SDK_INT));

		root.setSummary(Boolean.valueOf(Utils.hasRoot()).toString()
				+ "  "
				+ new BufferedReader(new InputStreamReader(Runtime.getRuntime()
						.exec("su -v").getInputStream())).readLine().toString());

		xposed.setSummary(Boolean.valueOf(Utils.hasXposed()).toString());
		busybox.setSummary(Boolean.valueOf(Utils.hasBusybox()).toString()
				+ "  "
				+ new BufferedReader(new InputStreamReader(Runtime.getRuntime()
						.exec("busybox").getInputStream())).readLine()
						.toString());

		boolean land = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

		if (Build.VERSION.SDK_INT < 19 | land) {
			getPreferenceScreen().removePreference(findPreference("null"));

			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			SystemBarTintManager.SystemBarConfig config = tintManager
					.getConfig();

			findViewById(android.R.id.content).setPadding(0,
					config.getPixelInsetTop(true), config.getPixelInsetRight(),
					config.getPixelInsetBottom());

		}

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
		if (preference.getKey().equals(Utils.KEY_SYS_INFO_RELEASE)) {
			System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
			mHits[mHits.length - 1] = SystemClock.uptimeMillis();
			if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {

				if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
					startActivity(new Intent(
							this,
							com.dvd.android.updatechecker.egg.kk.PlatLogoActivity.class));
				} else {
					if (Build.VERSION.SDK_INT == 20) {
						// TODO: 20 <=> Build.VERSION_CODES.L
						startActivity(new Intent(
								this,
								com.dvd.android.updatechecker.egg.l.PlatLogoActivity.class));
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

}