package com.example.otereshchenko.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DebugReceiver extends BroadcastReceiver {

    @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("MyReceiver", "onReceive");
        }
    }