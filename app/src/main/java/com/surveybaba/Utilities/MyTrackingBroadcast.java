package com.surveybaba.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.surveybaba.service.ForgroundLocationService;
import com.volly.BaseApplication;

public class MyTrackingBroadcast extends BroadcastReceiver {

    //BaseApplication baseApplication;
    @Override
    public void onReceive(Context context, Intent intent) {

       ////// baseApplication = (BaseApplication) context.getApplicationContext();

//        if(Utility.getBooleanSavedData(context, Utility.IS_USER_TRACKING)) {
//            baseApplication.startMyService();
//            Toast.makeText(context, "trigger", Toast.LENGTH_SHORT).show();
//        }
    //    Log.e("trigger: ","service");
        //Toast.makeText(context, "trigger", Toast.LENGTH_SHORT).show();
    }
}
