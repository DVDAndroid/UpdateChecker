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

		openActivity();
	}

	private void openActivity() {

		Toast.makeText(getApplicationContext(),
				getApplicationContext().getString(R.string.curr_ver) + "\n"
						+ "\n" + "Android " + Utils.getAndroidVersion()
						+ Build.VERSION.RELEASE + "\n" + "              API:"
						+ Build.VERSION.SDK_INT,
				Utils.duration).show();

		try {
			startActivity(new Intent(Intent.ACTION_MAIN).setClassName(
					"com.google.android.gms",
					"com.google.android.gms.update.SystemUpdateActivity"));

		} catch (ActivityNotFoundException ex) {
			Toast.makeText(getApplicationContext(), "error! please update gsm!",
					Utils.duration).show();
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