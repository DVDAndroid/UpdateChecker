package com.dvd.updatechecker;

import java.util.Random;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	public static ListPreference mListPreferenceColor;

	PackageInfo pInfo;
	SharedPreferences prefs;

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

			prefs.edit().putString(Utils.KEY_LIST_PREFERENCE_ICONS, "2")
					.commit();

			prefs.edit().putBoolean(Utils.KEY_CHECK_BOX_NO_ADD, true).commit();
			prefs.edit().putBoolean(Utils.KEY_CHECK_BOX_RAND_COLOR, true)
					.commit();
			prefs.edit().putBoolean(Utils.KEY_CHECK_BOX_RAND_COLOR_ACT, true)
					.commit();

			prefs.edit().putString(Utils.KEY_KK_LETTER, "K").commit();
			prefs.edit()
					.putString(Utils.KEY_KK_TEXT,
							"ANDROID " + Build.VERSION.RELEASE).commit();
			prefs.edit()
					.putString(Utils.KEY_KK_INTERPOLATOR,
							getString(R.string.def_anim)).commit();
			prefs.edit().putString(Utils.KEY_KK_CLICKS, "6").commit();
			prefs.edit()
					.putString(Utils.KEY_KK_SYSUI, getString(R.string.none))
					.commit();

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
				alertDialogBuilder.setMessage(getApplicationContext()
						.getString(R.string.module_ok));
				alertDialogBuilder.setPositiveButton(android.R.string.ok, null);
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.setCancelable(false);
				alertDialog.show();

			}

			prefs.edit().putBoolean("welcome", false).commit();
		}

		if (prefs.getBoolean(Utils.KEY_CHECK_BOX_RAND_COLOR_ACT, true)) {

			Random rnd = new Random();
			int colorMain = getRandomWithExclusion(rnd, 17170450, 17170461);

			int colorInfo = getRandomWithExclusion(rnd, 17170450, 17170461,
					colorMain);
			int colorSysInfo = getRandomWithExclusion(rnd, 17170450, 17170461,
					colorMain, colorInfo);
			int colorSettings = getRandomWithExclusion(rnd, 17170450, 17170461,
					colorMain, colorInfo, colorSysInfo);

			String sColorMain = Integer.toString(colorMain);
			String sColorInfo = Integer.toString(colorInfo);
			String sColorSysInfo = Integer.toString(colorSysInfo);
			String sColorSettings = Integer.toString(colorSettings);

			mListPreferenceColor.setValue(sColorMain);
			prefs.edit().putString(Utils.KEY_LIST_PREFERENCE_COLOR, sColorMain)
					.commit();
			prefs.edit().putString("colorInfo", sColorInfo).commit();
			prefs.edit().putString("colorSysInfo", sColorSysInfo).commit();
			prefs.edit().putString("colorSettings", sColorSettings).commit();

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

		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TextView tv1 = (TextView) findViewById(R.id.version);
		tv1.setText(" " + getApplicationContext().getString(R.string.vers)
				+ " " + pInfo.versionName + "-" + pInfo.versionCode);

		TextView tv2 = (TextView) findViewById(R.id.credit);
		tv2.setText("by " + getApplicationContext().getString(R.string.my_name)
				+ " ");

		applyColor(mListPreferenceColor.getValue().toString());
	}

	@Override
	@SuppressWarnings("deprecation")
	protected void onResume() {
		super.onResume();

		mListPreferenceColor.setSummary(getApplicationContext().getString(
				R.string.curr_icon)
				+ " " + mListPreferenceColor.getEntry().toString());

		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
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

		applyColor(mListPreferenceColor.getValue().toString());

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
			intent.setComponent(new ComponentName("com.dvd.updatechecker",
					"com.dvd.updatechecker.InfoActivity"));
			startActivity(intent);
			;
			return true;
		}
		if (id == R.id.action_exit) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}