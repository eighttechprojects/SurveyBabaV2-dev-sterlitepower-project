package com.surveybaba.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.surveybaba.FormDetailsActivity;
import com.surveybaba.MapsActivity;
import com.surveybaba.Modules.GISSurveyModule.GISSurveyActivity;
import com.surveybaba.Modules.GISSurveyModule.GISSurveyFormDetailsActivity;
import com.surveybaba.ProjectActivity;
import com.surveybaba.service.ForgroundLocationService;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (ForgroundLocationService.ACTION_SEND_NO_LOCATION_ALERT.equals(action)) {
            if (context instanceof FormDetailsActivity)
                ((FormDetailsActivity) context).revokeLocation();
//            else if (context instanceof ProjectActivity)
//                ((ProjectActivity) context).revokeLocation();
            else if (context instanceof MapsActivity)
                ((MapsActivity) context).revokeLocation();
            else if (context instanceof GISSurveyActivity)
                ((GISSurveyActivity) context).revokeLocation();
//            else if (context instanceof GISSurveyFormDetailsActivity)
//                ((GISSurveyFormDetailsActivity) context).revokeLocation();

            Log.e("track_gps: ",action);
        }
    }
}
