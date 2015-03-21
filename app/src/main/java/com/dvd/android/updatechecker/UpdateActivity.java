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

		String ver;
		switch (Build.VERSION.SDK_INT) {
			case Build.VERSION_CODES.KITKAT:
				ver = "KitKat";
				break;
			case Build.VERSION_CODES.LOLLIPOP:
			case Build.VERSION_CODES.LOLLIPOP_MR1:
				ver = "Lollipop ";
				break;
			default:
				ver = "";
				break;
		}

		openActivity(ver);
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
			finish();
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