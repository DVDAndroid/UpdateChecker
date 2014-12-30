package com.dvd.android.updatechecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.github.mrengineer13.snackbar.SnackBar;
import com.melnykov.fab.FloatingActionButton;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class SysInfoActivity extends PreferenceActivity {

	private static final String[] sysinfolist_pref = new String[] {
			Utils.KEY_SYS_INFO_BOARD, Utils.KEY_SYS_INFO_BOOTLOADER,
			Utils.KEY_SYS_INFO_CPU, Utils.KEY_SYS_INFO_DEVICE,
			Utils.KEY_SYS_INFO_DISPLAY, Utils.KEY_SYS_INFO_FINGERPRINT,
			Utils.KEY_SYS_INFO_HARDWARE, Utils.KEY_SYS_INFO_HOST,
			Utils.KEY_SYS_INFO_ID, Utils.KEY_SYS_INFO_MANUFACTURER,
			Utils.KEY_SYS_INFO_MODEL, Utils.KEY_SYS_INFO_PRODUCT,
			Utils.KEY_SYS_INFO_RADIO, Utils.KEY_SYS_INFO_SERIAL,
			Utils.KEY_SYS_INFO_TAGS, Utils.KEY_SYS_INFO_TIME,
			Utils.KEY_SYS_INFO_TYPE, Utils.KEY_SYS_INFO_USER,
			Utils.KEY_SYS_INFO_CODENAME, Utils.KEY_SYS_INFO_INCREMENTAL,
			Utils.KEY_SYS_INFO_API, Utils.KEY_SYS_INFO_KERNEL,
			Utils.KEY_SYS_INFO_ROOT, Utils.KEY_SYS_INFO_XPOSED,
			Utils.KEY_SYS_INFO_BUSYBOX };

	long[] mHits = new long[3];
	boolean copy_mod = true;
	private String l;

	@SuppressWarnings({ "deprecation", "static-access" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.sys_info);
		setContentView(R.layout.sysui);

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
				if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
					String ver = "Lollipop ";
					release.setSummary(ver + Build.VERSION.RELEASE);
				}
			}

			SharedPreferences prefs = getPreferenceManager()
					.getDefaultSharedPreferences(this);

			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			SystemBarTintManager.SystemBarConfig config = tintManager
					.getConfig();

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

			if (prefs.getBoolean(Utils.KEY_CHECK_BOX_RAND_COLOR_ACT, true)) {

				prefs.edit()
						.putString(Utils.KEY_LIST_PREFERENCE_COLOR,
								prefs.getString("colorSysInfo", null)).apply();
			}

			Utils.applyColor(SysInfoActivity.this,
					prefs.getString(Utils.KEY_LIST_PREFERENCE_COLOR, null));

			try {
				setSummaries();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
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
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			cpu.setSummary(Build.CPU_ABI + ", " + Build.CPU_ABI2);
		} else {
			StringBuilder builder = new StringBuilder();
			for (String s : Build.SUPPORTED_ABIS) {
				builder.append(s + "  ");
			}
			cpu.setSummary(builder);
		}

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
						.exec("su -v").getInputStream())).readLine());

		xposed.setSummary(Boolean.valueOf(Utils.hasXposed()).toString());
		busybox.setSummary(Boolean.valueOf(Utils.hasBusybox()).toString()
				+ "  "
				+ new BufferedReader(new InputStreamReader(Runtime.getRuntime()
						.exec("busybox").getInputStream())).readLine());

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();

		if (Build.VERSION.SDK_INT == 19) {
			findViewById(android.R.id.content).setPadding(0,
					config.getPixelInsetTop(true), config.getPixelInsetRight(),
					config.getPixelInsetBottom());
		}

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingactionbutton);
		fab.attachToListView(getListView());
		l = "";

		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP
				|| Build.VERSION.RELEASE.equals("L"))
			fab.setImageResource(R.drawable.ic_menu_copy_material);
		else {
			l = "\n\n\n\n";
			fab.setImageResource(R.drawable.ic_menu_copy);
		}

		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				@SuppressWarnings("static-access")
				SharedPreferences prefs;
				getPreferenceManager();
				prefs = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext());

				if (prefs.getBoolean("copy_mod", true)) {
					Toast.makeText(getApplicationContext(),
							getString(R.string.what_mod_copy),
							Toast.LENGTH_LONG).show();
					prefs.edit().putBoolean("copy_mod", false).apply();
				}

				for (String p : sysinfolist_pref) {
					findPreference(p).setSelectable(copy_mod);
				}

				copy_mod = !copy_mod;

			}
		});

		fab.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				Toast.makeText(getApplicationContext(),
						getString(R.string.what_mod_copy), Toast.LENGTH_LONG)
						.show();
				return false;
			}
		});

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
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {

		SharedPreferences prefs = getPreferenceManager()
				.getDefaultSharedPreferences(this);

		if (!copy_mod) {

			SnackBar.Builder snb = new SnackBar.Builder(this);
			snb.withMessage(getString(R.string.clipboard_message) + "\n"
					+ preference.getTitle() + ": " + preference.getSummary()
					+ l);

			snb.withActionMessageId(android.R.string.ok);
			snb.withStyle(SnackBar.Style.INFO);
			snb.withTextColorId(android.R.color.holo_green_dark);
			snb.withDuration(SnackBar.MED_SNACK);
			snb.show();

			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			ClipData clip = ClipData.newPlainText("Clipoard",
					preference.getTitle() + ": " + preference.getSummary());
			clipboard.setPrimaryClip(clip);
		}

		if (preference.getKey().equals(Utils.KEY_SYS_INFO_RELEASE)) {
			System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
			mHits[mHits.length - 1] = SystemClock.uptimeMillis();
			if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {

				if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
					startActivity(new Intent(
							this,
							com.dvd.android.updatechecker.egg.kk.PlatLogoActivity.class));
				} else {
					if (Build.VERSION.RELEASE.equals("L")) {

						startActivity(new Intent(
								this,
								com.dvd.android.updatechecker.egg.l_preview.PlatLogoActivity.class));
					} else {
						if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {

							if (prefs.getString(Utils.KEY_CHOOSE_PLAT, null)
									.equals("2")) {
								startActivity(new Intent(
										this,
										com.dvd.android.updatechecker.egg.ll.PlatLogoActivity.class));
							} else {
								startActivity(new Intent(
										this,
										com.dvd.android.updatechecker.egg.l_preview.PlatLogoActivity.class));
							}
						}
					}
				}
			}
		}

		return false;
	}
}