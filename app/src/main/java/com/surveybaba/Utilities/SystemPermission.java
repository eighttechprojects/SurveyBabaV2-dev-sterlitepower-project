package com.surveybaba.Utilities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.surveybaba.R;

public class SystemPermission
{
    private static final int REQUEST_PERMISSIONS = 1234;
    Activity mContext;
    Utility utility;
    public SystemPermission(Activity mContext)
    {
        this.mContext = mContext;
        utility = new Utility(mContext);
    }


    public boolean isExternalStorage()
    {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                utility.setPermissionDenied(mContext, utility.permission_ext_storage, true);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(R.string.permission_storage_title);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setMessage(R.string.permission_storage_description);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mContext.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
                    }
                });
                builder.show();
            }
            else
            {   // became - shouldshowrequestRationale = true from here on permission denial.
                if((utility.isPermissionDenied(mContext, utility.permission_ext_storage)))
                {
                    // redirect to settings
                    utility.showToast(mContext, mContext.getString(R.string.permission_storage_description), null);
                    redirectToPermissionSettings(mContext);
                }
                else {
                    ActivityCompat.requestPermissions(mContext,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_PERMISSIONS);
                }
            }
        }
        return false;
    }

    public boolean isCamera()
    {
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mContext,
                    Manifest.permission.CAMERA))
            {
                utility.setPermissionDenied(mContext, utility.permission_camera, true);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(R.string.permission_camera_title);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setMessage(R.string.permission_camera_description);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                        mContext.requestPermissions(
                                new String[]
                                        {Manifest.permission.CAMERA}
                                , REQUEST_PERMISSIONS);
                    }
                });
                builder.show();
            }
            else
            {   // became - shouldshowrequestRationale = true from here on permission denial.
                if((utility.isPermissionDenied(mContext, utility.permission_camera)))
                {
                    // redirect to settings
                    utility.showToast(mContext, mContext.getString(R.string.permission_camera_description), null);
                    redirectToPermissionSettings(mContext);
                }
                else {
                    ActivityCompat.requestPermissions(mContext,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_PERMISSIONS);
                }
            }
        }
        return false;
    }

    public boolean fineLocation()
    {
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION))
            {
                utility.setPermissionDenied(mContext, utility.permission_fine_location, true);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(R.string.permission_fine_location_title);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setMessage(R.string.permission_location_description);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                        mContext.requestPermissions(
                                new String[]
                                        {Manifest.permission.ACCESS_FINE_LOCATION}
                                , REQUEST_PERMISSIONS);
                    }
                });
                builder.show();
            }
            else
            {   // became - shouldshowrequestRationale = true from here on permission denial.
                if((utility.isPermissionDenied(mContext, utility.permission_fine_location)))
                {
                    // redirect to settings
                    utility.showToast(mContext, mContext.getString(R.string.permission_location_description), null);
                    redirectToPermissionSettings(mContext);
                }
                else {
                    ActivityCompat.requestPermissions(mContext,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_PERMISSIONS);
                }
            }
        }
        return false;
    }

    public boolean coarseLocation()
    {
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION))
            {
                utility.setPermissionDenied(mContext, utility.permission_coarse_location, true);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(R.string.permission_fine_location_title);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setMessage(R.string.permission_location_description);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                        mContext.requestPermissions(
                                new String[]
                                        {Manifest.permission.ACCESS_COARSE_LOCATION}
                                , REQUEST_PERMISSIONS);
                    }
                });
                builder.show();
            }
            else
            {   // became - shouldshowrequestRationale = true from here on permission denial.
                if((utility.isPermissionDenied(mContext, utility.permission_coarse_location)))
                {
                    // redirect to settings
                    utility.showToast(mContext, mContext.getString(R.string.permission_location_description), null);
                    redirectToPermissionSettings(mContext);
                }
                else {
                    ActivityCompat.requestPermissions(mContext,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_PERMISSIONS);
                }
            }
        }
        return false;
    }


    public boolean isLocation(){
        if( (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) )
        {
            return true;
        }
        else
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, Manifest.permission.ACCESS_COARSE_LOCATION))
            {
                utility.setPermissionDenied(mContext, utility.permission_coarse_location, true);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(R.string.permission_fine_location_title);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setMessage(R.string.permission_location_description);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                        mContext.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS);
                    }
                });
                builder.show();
            }
            else
            {   // became - shouldshowrequestRationale = true from here on permission denial.
                if((utility.isPermissionDenied(mContext, utility.permission_coarse_location)))
                {
                    // redirect to settings
                    utility.showToast(mContext, mContext.getString(R.string.permission_location_description), null);
                    redirectToPermissionSettings(mContext);
                }
                else {
                    ActivityCompat.requestPermissions(mContext,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_PERMISSIONS);
                }
            }
        }
        return false;
    }

    private void redirectToPermissionSettings(Activity mContext) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        mContext.startActivity(intent);
    }


    public static boolean isInternetConnected(Context context) {
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
            return connected;
        } catch (Exception e) {
            //System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }

    public static void buildAlertMessageNoGps(Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Your GPS seems to be disabled. Please Turn On GPS to Access Google Map")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)));
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public static boolean isGPSOn(Context context){
        final LocationManager manager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}
