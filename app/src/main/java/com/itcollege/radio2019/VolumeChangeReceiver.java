package com.itcollege.radio2019;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class VolumeChangeReceiver extends BroadcastReceiver {
    private static String TAG = VolumeChangeReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive intent action" + intent.getAction());

        switch (intent.getAction()) {
            case "":
                break;
        }
    }
}
