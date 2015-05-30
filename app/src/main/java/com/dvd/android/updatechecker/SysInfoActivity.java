package com.dvd.android.updatechecker;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import com.github.mrengineer13.snackbar.SnackBar;
import com.melnykov.fab.FloatingActionButton;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
	private SharedPreferences prefs;
	private FloatingActionButton fab;

	private Class KITKAT_PLATLOGO = com.dvd.android.updatechecker.egg.kk.PlatLogoActivity.class;
	private Class L_PLATLOGO = com.dvd.android.updatechecker.egg.l_preview.PlatLogoActivity.class;
	private Class LOLLIPOP_PLATLOGO = com.dvd.android.updatechecker.egg.ll.PlatLogoActivity.class;

	@SuppressWarnings({ "deprecation", "static-access" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.sys_info);
		setContentView(R.layout.sysui);

		prefs = getPreferenceManager().getDefaultSharedPreferences(this);

		if (getActionBar() != null) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		Preference release = findPreference(Utils.KEY_SYS_INFO_RELEASE);
		release.setEnabled(prefs.getBoolean("setts_opened", false));

		String ver;
		fab = (FloatingActionButton) findViewById(R.id.floatingactionbutton);
		fab.attachToListView(getListView());

		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
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

		switch (Build.VERSION.SDK_INT) {
			case Build.VERSION_CODES.KITKAT:
				ver = "KitKat ";
				fab.setImageResource(R.drawable.ic_menu_copy);
				break;
			case Build.VERSION_CODES.LOLLIPOP:
			case Build.VERSION_CODES.LOLLIPOP_MR1:
				ver = "Lollipop ";
				fab.setImageResource(R.drawable.ic_menu_copy_material);
				break;
			default:
				ver = "";
				fab.setImageResource(R.drawable.ic_menu_copy);
				break;
		}

		switch (Build.VERSION.RELEASE) {
			case "L":
			case "M":
				ver = "";
				fab.setImageResource(R.drawable.ic_menu_copy_material);
				break;
		}

		release.setSummary(ver + Build.VERSION.RELEASE);

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

		Utils.applyColor(this, prefs.getString("colorSysInfo", "#ff0000"));

		try {
			setSummaries();
		} catch (IOException e) {
			e.printStackTrace();
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

		kernel.setSummary(Utils.getFormattedKernelVersion());
		board.setSummary(Build.BOARD);
		bootloader.setSummary(Build.BOOTLOADER);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			cpu.setSummary(Build.CPU_ABI + ", " + Build.CPU_ABI2);
		} else {
			StringBuilder builder = new StringBuilder();
			for (String s : Build.SUPPORTED_ABIS) {
				builder.append(s).append("  ");
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
		xposed.setSummary(Boolean.toString(Utils.isPackageInstalled(this,
				"de.robv.android.xposed.installer"))
				+ (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP
						|| Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1 ? " (?)"
						: ""));
		busybox.setSummary(Boolean.toString(Utils.hasBusybox())
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
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	@SuppressLint("NewApi")
	@SuppressWarnings({ "static-access", "deprecation" })
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {

		prefs = getPreferenceManager().getDefaultSharedPreferences(this);
		final SystemBarTintManager tintManager = new SystemBarTintManager(this);

		if (!copy_mod) {
			if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT)
				tintManager.setNavigationBarTintEnabled(true);
			final SnackBar.Builder snb = new SnackBar.Builder(this);

			snb.withMessage(getString(R.string.clipboard_message) + "\n"
					+ preference.getTitle() + ": " + preference.getSummary());
			snb.withVisibilityChangeListener(new SnackBar.OnVisibilityChangeListener() {
				@Override
				public void onShow(int i) {
					fab.hide(true);
					switch (Build.VERSION.SDK_INT) {
						case Build.VERSION_CODES.KITKAT:
							tintManager.setNavigationBarTintColor(Color
									.parseColor("#323232"));
							break;
						case Build.VERSION_CODES.LOLLIPOP:
						case Build.VERSION_CODES.LOLLIPOP_MR1:
							getWindow().setNavigationBarColor(
									Color.parseColor("#323232"));
							break;
					}
				}

				@Override
				public void onHide(int i) {
					fab.show();
					switch (Build.VERSION.SDK_INT) {
						case Build.VERSION_CODES.KITKAT:
							tintManager.setNavigationBarTintColor(Color
									.parseColor("#00000000"));
							break;
						case Build.VERSION_CODES.LOLLIPOP:
						case Build.VERSION_CODES.LOLLIPOP_MR1:
							getWindow().setNavigationBarColor(
									Color.parseColor("#4d000000"));
							break;
					}
				}
			});
			snb.withActionMessageId(android.R.string.ok);
			snb.withStyle(SnackBar.Style.INFO);
			snb.withTextColorId(android.R.color.holo_green_dark);
			snb.show();

			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			ClipData clip = ClipData.newPlainText("Clipboard",
					preference.getTitle() + ": " + preference.getSummary());
			clipboard.setPrimaryClip(clip);
		}

		if (preference.getKey().equals(Utils.KEY_SYS_INFO_RELEASE)) {
			System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
			mHits[mHits.length - 1] = SystemClock.uptimeMillis();
			if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {

				switch (Build.VERSION.SDK_INT) {
					case Build.VERSION_CODES.KITKAT:
						startActivity(new Intent(this, KITKAT_PLATLOGO));
						break;
					case Build.VERSION_CODES.LOLLIPOP:
					case Build.VERSION_CODES.LOLLIPOP_MR1:
						if (prefs.getString(Utils.KEY_CHOOSE_PLAT, null)
								.equals("2")) {
							startActivity(new Intent(this, LOLLIPOP_PLATLOGO));
						} else {
							startActivity(new Intent(this, L_PLATLOGO));
						}
						break;
					default:
						if (Build.VERSION.RELEASE.equals("L")) {
							startActivity(new Intent(this, L_PLATLOGO));
						}
						break;
				}
			}
		}
		return false;
	}
}