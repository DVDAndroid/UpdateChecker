package com.dvd.updatechecker;

import java.io.File;

import android.annotation.SuppressLint;
import android.widget.Toast;

public class Utils {

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
	public static String KEY_EGGSTER = "eggster";

	public static String KEY_KK_LETTER = "kk_letter";
	public static String KEY_KK_TEXT = "kk_text";
	public static String KEY_KK_INTERPOLATOR = "kk_interpolator";
	public static String KEY_KK_CLICKS = "kk_clicks";
	public static String KEY_KK_SYSUI = "kk_sysui";

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

		if (new File("/system/xbin/su").exists()
				|| new File("/system/bin/su").exists()
				|| new File("/system/app/Superuser.apk").exists()
				|| new File("/system/priv-app/Superuser.apk").exists()) {
			return true;
		}

		return false;

	}

	@SuppressLint("SdCardPath")
	public static boolean hasXposed() {

		if (new File("/data/data/de.robv.android.xposed.installer").exists()) {
			return true;
		}

		return false;

	}

	public static boolean hasBusybox() {

		if (new File("/system/xbin/busybox").exists()
				|| new File("/system/bin/busybox").exists()) {
			return true;
		}

		return false;

	}

}
