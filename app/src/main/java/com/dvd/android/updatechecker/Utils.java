package com.dvd.android.updatechecker;

import java.io.File;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.annotation.SuppressLint;
import android.app.Activity;
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

	public static String KEY_MATERIAL_DARK = "17170460";
	public static String KEY_MATERIAL_LIGHT = "17170461";

	public static String KEY_ICON_JB = "1";
	public static String KEY_ICON_KK = "2";
	public static String KEY_ICON_L = "3";

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

	public static String KEY_AUTO_UP = "no_auto_upd";

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

	public static void applyColor(Activity a, String color) {

		int actionBarColor = 0;

		switch (color) {
		case "1":
			actionBarColor = Color.parseColor("#f44336");
			break;
		case "2":
			actionBarColor = Color.parseColor("#e91e63");
			break;
		case "3":
			actionBarColor = Color.parseColor("#9c27b0");
			break;
		case "4":
			actionBarColor = Color.parseColor("#673ab7");
			break;
		case "5":
			actionBarColor = Color.parseColor("#3f51b5");
			break;
		case "6":
			actionBarColor = Color.parseColor("#2196f3");
			break;
		case "7":
			actionBarColor = Color.parseColor("#03a9f4");
			break;
		case "8":
			actionBarColor = Color.parseColor("#00bcd4");
			break;
		case "9":
			actionBarColor = Color.parseColor("#009688");
			break;
		case "10":
			actionBarColor = Color.parseColor("#4caf50");
			break;
		case "11":
			actionBarColor = Color.parseColor("#8bc34a");
			break;
		case "12":
			actionBarColor = Color.parseColor("#cddc39");
			break;
		case "13":
			actionBarColor = Color.parseColor("#ffeb3b");
			break;
		case "14":
			actionBarColor = Color.parseColor("#ffc107");
			break;
		case "15":
			actionBarColor = Color.parseColor("#ff9800");
			break;
		case "16":
			actionBarColor = Color.parseColor("#ff5722");
			break;
		case "17":
			actionBarColor = Color.parseColor("#795548");
			break;
		case "18":
			actionBarColor = Color.parseColor("#9e9e9e");
			break;
		case "19":
			actionBarColor = Color.parseColor("#607d8b");
			break;
		}

		a.getActionBar().setBackgroundDrawable(
				new ColorDrawable(actionBarColor));

		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
			a.getWindow().setStatusBarColor(actionBarColor);
			a.getWindow().setNavigationBarColor(Color.parseColor("#4d000000"));
		} else {
			SystemBarTintManager tintManager = new SystemBarTintManager(a);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintColor(actionBarColor);
		}
	}

}
