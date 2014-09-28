package com.dvd.android.updatechecker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String phoneNumber = getResultData();
		if (phoneNumber == null) {
			phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
		}

		if (phoneNumber.equals("873*243*1")) {
			setResultData(null);

			Intent i = new Intent(context, MainActivity.class);
			i.putExtra("extra_phone", phoneNumber);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}

		if (phoneNumber.equals("873*243*2")) {
			setResultData(null);

			Intent i = new Intent(context, UpdateActivity.class);
			i.putExtra("extra_phone", phoneNumber);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}

	}

}