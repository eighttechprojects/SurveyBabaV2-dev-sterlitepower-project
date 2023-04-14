package com.surveybaba.InternetConnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.surveybaba.Utilities.Utility;

public abstract class ConnectivityReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

            try{
                if(!isConnected(context)){
                    onNetworkChange(Utility.NO_NETWORK_CONNECTED);
                }
                else if(isConnected(context)){
                    onNetworkChange(Utility.NETWORK_CONNECTED);
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
    }

    // Check network connect or not!
    public static boolean isConnected(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }

    protected abstract void onNetworkChange(String alert);

}