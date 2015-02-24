// Copyright: Giupy 99. DO NOT KANG.

package com.dvd.android.updatechecker.egg.l_preview;

import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.dvd.android.updatechecker.R;

@SuppressWarnings("all")
public class PlatLogoActivity extends Activity {

	@SuppressWarnings("unused")
	@SuppressLint({ "NewApi", "InlinedApi", "RtlHardcoded" })
	@Override
	public void onCreate(Bundle savedInstanceState) {

		FrameLayout Cont = new FrameLayout(this);
		Cont.setBackgroundColor(-1);
		final View rct1 = new View(this), rct2 = new View(this);
		TextView build = new TextView(this);
		final Random r = new Random();

		String sui = getSharedPreferences(
				"com.dvd.android.updatechecker_preferences",
				Context.MODE_PRIVATE).getString("l_sysui",
				getString(R.string.none));
		DisplayMetrics metrics = new DisplayMetrics();
		LayoutParams pos1 = new LayoutParams(r.nextInt(1000), r.nextInt(1000)), pos2 = new LayoutParams(
				r.nextInt(1000), r.nextInt(1000)), label = new LayoutParams(-2,
				-2);

		build.setText("android_L.flv - build 1236599");
		build.setTextSize(18);
		build.setTextColor(Color.BLACK);
		pos1.topMargin = r.nextInt(1000);
		pos1.leftMargin = r.nextInt(500);
		pos2.topMargin = r.nextInt(1000);
		pos2.leftMargin = r.nextInt(500);
		final int p = (int) (4 * metrics.density);
		rct1.setBackgroundColor(Color.RED);
		rct2.setBackgroundColor(Color.BLUE);
		super.onCreate(savedInstanceState);
		if (sui.equals(getString(R.string.translucent))) {
			getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		} else if (sui.equals(getString(R.string.immerge)))
			Cont.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
					| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
					| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); // immerge
		setContentView(Cont);
		label.gravity = Gravity.LEFT | Gravity.BOTTOM;
		if (sui.equals(getString(R.string.translucent)))
			label.bottomMargin = 100;
		Cont.addView(rct2);
		Cont.addView(rct1);
		Cont.addView(build, label);
		rct1.setLayoutParams(pos1);
		rct2.setLayoutParams(pos2);

		new CountDownTimer(Integer.MAX_VALUE, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				LayoutParams newpos1 = new LayoutParams(r.nextInt(1000),
						r.nextInt(1000)), newpos2 = new LayoutParams(
						r.nextInt(1000), r.nextInt(1000));
				newpos1.topMargin = r.nextInt(200);
				newpos1.leftMargin = r.nextInt(200);
				newpos2.topMargin = r.nextInt(200);
				newpos2.leftMargin = r.nextInt(200);
				rct1.setLayoutParams(newpos1);
				rct2.setLayoutParams(newpos2);
			}

			@Override
			public void onFinish() {
			}
		}.start();
		Cont.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				if (Build.VERSION.SDK_INT > 10) {
					try {
						startActivity(new Intent(Intent.ACTION_MAIN)
								.setFlags(
										Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
								.setClassName("com.dvd.android.updatechecker",
										"com.dvd.android.updatechecker.egg.kk.DessertCase"));

					} catch (ActivityNotFoundException ex) {
						android.util.Log.e("PlatLogoActivity",
								"Couldn't catch a break.");
					}
				}
				finish();
				return true;
			}
		});
	}
}