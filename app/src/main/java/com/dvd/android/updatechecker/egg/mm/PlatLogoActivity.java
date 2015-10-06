/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dvd.android.updatechecker.egg.mm;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;

import com.dvd.android.updatechecker.R;

@SuppressWarnings("all")
@SuppressLint("all")
@TargetApi(Build.VERSION_CODES.M)
public class PlatLogoActivity extends Activity {
	FrameLayout mLayout;
	int mTapCount;
	int mKeyCount;
	PathInterpolator mInterpolator = new PathInterpolator(0f, 0f, 0.5f, 1f);

	/**
	 * Convert HSB components to an ARGB color. Alpha set to 0xFF. hsv[0] is Hue
	 * [0 .. 1) hsv[1] is Saturation [0...1] hsv[2] is Value [0...1] If hsv
	 * values are out of range, they are pinned.
	 *
	 * @param hsb
	 *            3 element array which holds the input HSB components.
	 * @return the resulting argb color
	 *
	 * @hide Pending API council
	 */
	public static int HSBtoColor(float[] hsb) {
		return HSBtoColor(hsb[0], hsb[1], hsb[2]);
	}

	/**
	 * Convert HSB components to an ARGB color. Alpha set to 0xFF. hsv[0] is Hue
	 * [0 .. 1) hsv[1] is Saturation [0...1] hsv[2] is Value [0...1] If hsv
	 * values are out of range, they are pinned.
	 *
	 * @param h
	 *            Hue component
	 * @param s
	 *            Saturation component
	 * @param b
	 *            Brightness component
	 * @return the resulting argb color
	 *
	 * @hide Pending API council
	 */
	public static int HSBtoColor(float h, float s, float b) {
		h = constrain(h, 0.0f, 1.0f);
		s = constrain(s, 0.0f, 1.0f);
		b = constrain(b, 0.0f, 1.0f);

		float red = 0.0f;
		float green = 0.0f;
		float blue = 0.0f;

		final float hf = (h - (int) h) * 6.0f;
		final int ihf = (int) hf;
		final float f = hf - ihf;
		final float pv = b * (1.0f - s);
		final float qv = b * (1.0f - s * f);
		final float tv = b * (1.0f - s * (1.0f - f));

		switch (ihf) {
			case 0: // Red is the dominant color
				red = b;
				green = tv;
				blue = pv;
				break;
			case 1: // Green is the dominant color
				red = qv;
				green = b;
				blue = pv;
				break;
			case 2:
				red = pv;
				green = b;
				blue = tv;
				break;
			case 3: // Blue is the dominant color
				red = pv;
				green = qv;
				blue = b;
				break;
			case 4:
				red = tv;
				green = pv;
				blue = b;
				break;
			case 5: // Red is the dominant color
				red = b;
				green = pv;
				blue = qv;
				break;
		}

		return 0xFF000000 | (((int) (red * 255.0f)) << 16)
				| (((int) (green * 255.0f)) << 8) | ((int) (blue * 255.0f));
	}

	public static float constrain(float amount, float low, float high) {
		return amount < low ? low : (amount > high ? high : amount);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mLayout = new FrameLayout(this);
		setContentView(mLayout);
	}

	@Override
	public void onAttachedToWindow() {
		final DisplayMetrics dm = getResources().getDisplayMetrics();
		final float dp = dm.density;
		final int size = (int) (Math
				.min(Math.min(dm.widthPixels, dm.heightPixels), 600 * dp)
				- 100 * dp);

		final View im = new View(this);
		im.setTranslationZ(20);
		im.setScaleX(0.5f);
		im.setScaleY(0.5f);
		im.setAlpha(0f);
		im.setOutlineProvider(new ViewOutlineProvider() {
			@Override
			public void getOutline(View view, Outline outline) {
				final int pad = (int) (8 * dp);
				outline.setOval(pad, pad, view.getWidth() - pad,
						view.getHeight() - pad);
			}
		});
		final float hue = (float) Math.random();
		final Paint bgPaint = new Paint();

		bgPaint.setColor(HSBtoColor(hue, 0.4f, 1f));
		final Paint fgPaint = new Paint();
		fgPaint.setColor(HSBtoColor(hue, 0.5f, 1f));
		final Drawable M = getDrawable(R.drawable.platlogo_m);
		final Drawable platlogo = new Drawable() {
			@Override
			public void setAlpha(int alpha) {
			}

			@Override
			public void setColorFilter(@Nullable ColorFilter colorFilter) {
			}

			@Override
			public int getOpacity() {
				return PixelFormat.TRANSLUCENT;
			}

			@Override
			public void draw(Canvas c) {
				final float r = c.getWidth() / 2f;
				c.drawCircle(r, r, r, bgPaint);
				c.drawArc(0, 0, 2 * r, 2 * r, 135, 180, false, fgPaint);
				M.setBounds(0, 0, c.getWidth(), c.getHeight());
				M.draw(c);
			}
		};
		im.setBackground(new RippleDrawable(ColorStateList.valueOf(0xFFFFFFFF),
				platlogo, null));
		im.setOutlineProvider(new ViewOutlineProvider() {
			@Override
			public void getOutline(View view, Outline outline) {
				outline.setOval(0, 0, view.getWidth(), view.getHeight());
			}
		});
		im.setClickable(true);
		im.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mTapCount == 0) {
					showMarshmallow(im);
				}
				im.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						if (mTapCount < 5)
							return false;

						im.post(new Runnable() {
							@Override
							public void run() {
								try {
									startActivity(new Intent(Intent.ACTION_MAIN)
											.setFlags(
													Intent.FLAG_ACTIVITY_NEW_TASK
															| Intent.FLAG_ACTIVITY_CLEAR_TASK
															| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
											// .addCategory("com.android.internal.category.PLATLOGO")
											.setClassName(
													"com.dvd.android.updatechecker",
													"com.dvd.android.updatechecker.egg.mm.MLandActivity"));
								} catch (ActivityNotFoundException ex) {
									Log.e("PlatLogoActivity", "No more eggs.");
								}
								finish();
							}
						});
						return true;
					}
				});
				mTapCount++;
			}
		});

		// Enable hardware keyboard input for TV compatibility.
		im.setFocusable(true);
		im.requestFocus();
		im.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode != KeyEvent.KEYCODE_BACK
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					if (mKeyCount == 0) {
						showMarshmallow(im);
					}
					++mKeyCount;
					if (mKeyCount > 2) {
						if (mTapCount > 5) {
							im.performLongClick();
						} else {
							im.performClick();
						}
					}
					return true;
				} else {
					return false;
				}
			}
		});

		mLayout.addView(im,
				new FrameLayout.LayoutParams(size, size, Gravity.CENTER));

		im.animate().scaleX(1f).scaleY(1f).alpha(1f)
				.setInterpolator(mInterpolator).setDuration(500)
				.setStartDelay(800).start();
	}

	public void showMarshmallow(View im) {
		final Drawable fg = getDrawable(R.drawable.platlogo_m);
		fg.setBounds(0, 0, im.getWidth(), im.getHeight());
		fg.setAlpha(0);
		im.getOverlay().add(fg);

		final Animator fadeIn = ObjectAnimator.ofInt(fg, "alpha", 255);
		fadeIn.setInterpolator(mInterpolator);
		fadeIn.setDuration(300);
		fadeIn.start();
	}
}