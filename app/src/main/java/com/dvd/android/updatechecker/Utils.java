package com.dvd.android.updatechecker;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.widget.Toast;

public class Utils {

	public static int NOTIFICATION_ID = 21;
	public static String KEY_LIST_PREFERENCE_COLOR = "listPrefcolor";
	public static String KEY_LIST_PREFERENCE_ICONS = "select_icons";
	public static String KEY_CHECK_BOX_NO_ADD = "no_add";
	public static String KEY_CHECK_BOX_HIDE_ICON = "hide_icon";
	public static String KEY_CHECK_BOX_RAND_COLOR = "random_colors";
	public static String KEY_CHECK_BOX_RAND_COLOR_ACT = "random_colors_act";

	public static String KEY_SYSTEMBARTINT = "systembartint";
	public static String KEY_ANDROID_OPENSOURCE = "android_opensource";
	public static String KEY_STACKOVERFLOW = "stackoverflow";
	public static String KEY_NINEOLDANDROIDS = "nineoldandroids";
	public static String KEY_SNACK_BAR = "snack_bar";
	public static String KEY_FAB = "android-floating-action-button";
	public static String KEY_EGGSTER = "eggster";
	public static String KEY_CHECKING = "checking";
	public static String KEY_GITHUB = "this_on_github";

	public static String KEY_KK_LETTER = "kk_letter";
	public static String KEY_KK_TEXT = "kk_text";
	public static String KEY_KK_INTERPOLATOR = "kk_interpolator";
	public static String KEY_KK_CLICKS = "kk_clicks";
	public static String KEY_KK_SYSUI = "kk_sysui";

	public static String KEY_L_SYSUI = "l_sysui";

	public static String KEY_CHOOSE_PLAT = "platlogo";

	public static String KEY_SYS_INFO_BOARD = "board";
	public static String KEY_SYS_INFO_BOOTLOADER = "bootloader";
	public static String KEY_SYS_INFO_CPU = "cpu";
	public static String KEY_SYS_INFO_DEVICE = "device";
	public static String KEY_SYS_INFO_DISPLAY = "display";
	public static String KEY_SYS_INFO_FINGERPRINT = "fingerprint";
	public static String KEY_SYS_INFO_HARDWARE = "hardware";
	public static String KEY_SYS_INFO_HOST = "host";
	public static String KEY_SYS_INFO_ID = "id";
	public static String KEY_SYS_INFO_MANUFACTURER = "manufacturer";
	public static String KEY_SYS_INFO_MODEL = "model";
	public static String KEY_SYS_INFO_PRODUCT = "product";
	public static String KEY_SYS_INFO_RADIO = "radio";
	public static String KEY_SYS_INFO_SERIAL = "serial";
	public static String KEY_SYS_INFO_TAGS = "tags";
	public static String KEY_SYS_INFO_TIME = "time";
	public static String KEY_SYS_INFO_TYPE = "type";
	public static String KEY_SYS_INFO_USER = "user";
	public static String KEY_SYS_INFO_CODENAME = "codename";
	public static String KEY_SYS_INFO_INCREMENTAL = "incremental";
	public static String KEY_SYS_INFO_RELEASE = "release";
	public static String KEY_SYS_INFO_API = "api";
	public static String KEY_SYS_INFO_KERNEL = "kernel";
	public static String KEY_SYS_INFO_ROOT = "root";
	public static String KEY_SYS_INFO_XPOSED = "xposed";
	public static String KEY_SYS_INFO_BUSYBOX = "busybox";

	public static String TSB_1 = "com.mohammadag.colouredstatusbar";
	public static String TSB_2 = "com.woalk.apps.xposed.ttsb";

	public static int duration = Toast.LENGTH_SHORT;

	public static boolean hasRoot() {
		return new File("/system/xbin/su").exists()
				|| new File("/system/bin/su").exists()
				|| new File("/system/app/Superuser.apk").exists()
				|| new File("/system/priv-app/Superuser.apk").exists();
	}

	@SuppressLint("SdCardPath")
	public static boolean hasXposed() {
		return new File("/data/data/de.robv.android.xposed.installer").exists();
	}

	public static boolean hasBusybox() {
		return new File("/system/xbin/busybox").exists()
				|| new File("/system/bin/busybox").exists();
	}

	public static boolean isPackageInstalled(Context context, String packagename) {
		PackageManager pm = context.getPackageManager();
		try {
			pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}

	public static int getRandomWithExclusion(Random rnd, int start, int end,
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

	public static void applyColor(Activity activity, String id) {

		String[] colors_values = activity.getApplicationContext()
				.getResources().getStringArray(R.array.colorsValues);
		ArrayList<String> colors_values_array = new ArrayList<>(
				Arrays.asList(colors_values));

		String color = colors_values_array.get(Integer.parseInt(id) - 1);

		if (activity.getActionBar() != null) {
			activity.getActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor(color)));
		}

		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
			String icon = null;
			SharedPreferences preferences = activity.getApplicationContext()
					.getSharedPreferences(
							activity.getPackageName() + "_preferences", 0);
			switch (preferences.getString(Utils.KEY_LIST_PREFERENCE_ICONS, "3")) {
				case "1":
					icon = "jb";
					break;
				case "2":
					icon = "kk";
					break;
				case "3":
					icon = "l";
					break;
			}

			Bitmap bm = BitmapFactory.decodeResource(
					activity.getResources(),
					activity.getResources().getIdentifier(
							"ic_launcher_settings_" + icon, "mipmap",
							activity.getPackageName()));
			ActivityManager.TaskDescription tDesc = new ActivityManager.TaskDescription(
					activity.getString(R.string.app_name), bm, (darkenColor(
							Color.parseColor(color), 0.85f)));
			activity.setTaskDescription(tDesc);

			activity.getWindow().setStatusBarColor(
					darkenColor(Color.parseColor(color), 0.85f));
			activity.getWindow().setNavigationBarColor(
					Color.parseColor("#4d000000"));
		} else {
			SystemBarTintManager tintManager = new SystemBarTintManager(
					activity);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintColor(Color.parseColor(color));
		}
	}

	/**
	 * @author PeterCxy @
	 *         https://github.com/PeterCxy/Lolistat/blob/aide/app/src/
	 *         main/java/info/papdt/lolistat/support/Utility.java
	 */
	public static int darkenColor(int color, float factor) {
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		hsv[2] *= factor;
		return Color.HSVToColor(hsv);
	}
}
