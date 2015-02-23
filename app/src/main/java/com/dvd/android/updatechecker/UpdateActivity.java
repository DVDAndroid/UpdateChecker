package com.dvd.android.updatechecker;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

public class UpdateActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
			String ver = "KitKat ";
			openActivity(ver);
		} else {
			if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
				String ver = "Lollipop ";
				openActivity(ver);
			}
		}

	}

	private void openActivity(String ver) {

		Toast.makeText(
				getApplicationContext(),
				getApplicationContext().getString(R.string.curr_ver) + "\n"
						+ "\n" + "Android " + ver + Build.VERSION.RELEASE
						+ "\n" + "              API:" + Build.VERSION.SDK_INT,
				Utils.duration).show();

		try {
			startActivity(new Intent(Intent.ACTION_MAIN).setClassName(
					"com.google.android.gms",
					"com.google.android.gms.update.SystemUpdateActivity"));

		} catch (ActivityNotFoundException ex) {
			Toast.makeText(getApplicationContext(),
					"error! please update gsm!", Utils.duration).show();
		}
	}

	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		if (level == TRIM_MEMORY_UI_HIDDEN) {
			this.finish();
		}

	}
}