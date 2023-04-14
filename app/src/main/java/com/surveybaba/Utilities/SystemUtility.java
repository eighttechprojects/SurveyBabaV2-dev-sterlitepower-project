package com.surveybaba.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.SphericalUtil;
import com.google.protobuf.DescriptorProtos;
import com.surveybaba.R;

import java.util.ArrayList;
import java.util.List;

import static com.surveybaba.Utilities.Utility.convertDistanceFromMeter;

public class SystemUtility {
    public static String DEFAULT_DURATION_SEC = "10";//sec
    public static int DEFAULT_LINE_SIZE = 7;
    public static int DEFAULT_TEXT_SIZE = 10;
    public static int DEFAULT_LINE_COLOR = Color.BLUE;

    public static void setFullscreenToggle(Activity context, boolean fullscreen) {
        WindowManager.LayoutParams attrs = context.getWindow().getAttributes();
        if (fullscreen) {
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        } else {
            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        }
        context.getWindow().setAttributes(attrs);
        Utility.saveData(context, Utility.IS_FULL_SCREEN, fullscreen);
    }

    public static void setScreenOrientation(Activity context, boolean isPortrait) {
        context.setRequestedOrientation(isPortrait ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public static void setKeepScreenAwakeAlwaysToggle(Activity context, boolean toggle) {
        if (toggle) {
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        Utility.saveData(context, Utility.IS_ALWAYS_KEEP_SCREEN_AWAKE, toggle);
    }

    public static void setMapCursorCenter(String position, ImageView imageView) {
        if (Utility.isEmptyString(position))
            position = "0";
        Integer[] arrDrawables = {R.drawable.my_loc_2, R.drawable.ictarget01, R.drawable.ictarget02, R.drawable.ictarget03, R.drawable.ictarget04, R.drawable.ictarget05, R.drawable.ictarget06, R.drawable.ictarget07};
        imageView.setImageResource(arrDrawables[Integer.parseInt(position)]);
    }

    public static long getHidePanelDuration(Activity mActivity) {
        String duration = Utility.getSavedData(mActivity, Utility.HIDE_PANEL_DURATION);
        if (Utility.isEmptyString(duration))
            duration = DEFAULT_DURATION_SEC;
        else if (Integer.parseInt(duration) <= Integer.parseInt(DEFAULT_DURATION_SEC))
            duration = DEFAULT_DURATION_SEC;
        return Long.parseLong(duration) * 1000L;
    }

    public static int getLineSize(Activity mActivity) {
        String lineSize = Utility.getSavedData(mActivity, Utility.LINE_SIZE);
        int lineSizeResult = DEFAULT_LINE_SIZE;
        if (!Utility.isEmptyString(lineSize))
            lineSizeResult = Integer.parseInt(lineSize);
        return lineSizeResult;
    }

    public static int getTextSizeValue(Activity mActivity) {
        String textSize = Utility.getSavedData(mActivity, Utility.TEXT_SIZE);
        int textSizeVal = DEFAULT_TEXT_SIZE;
        if (!Utility.isEmptyString(textSize))
            textSizeVal = Integer.parseInt(textSize);
        return textSizeVal;
    }

    public static int getTextSizeDimen(Activity mActivity) {
        int textSizeVal = getTextSizeValue(mActivity);
        if (textSizeVal <= 10) {
            textSizeVal = R.dimen._10ssp;
        } else if (textSizeVal <= 12) {
            textSizeVal = R.dimen._12ssp;
        } else if (textSizeVal <= 14) {
            textSizeVal = R.dimen._14ssp;
        } else if (textSizeVal <= 16) {
            textSizeVal = R.dimen._16ssp;
        } else if (textSizeVal <= 18) {
            textSizeVal = R.dimen._18ssp;
        } else {
            textSizeVal = R.dimen._20ssp;
        }
        return textSizeVal;
    }
//
//    public static String getColorHexCode(Activity mActivity)
//    {
//        String default_color = Utility.COLOR_CODE.PINK;
//        String color = Utility.getSavedData(mActivity, Utility.LINE_COLOR_CODE);
//        if(Utility.isEmptyString(color))
//            color = default_color;
//        return color;
//    }
    // PolyLine
    public static String getPolyLineColorHexCode(Activity mActivity){
    String default_color = Utility.COLOR_CODE.BLUE;
    String color = Utility.getSavedData(mActivity, Utility.LAYER_LINE_COLOR);
    try {
        if (!Utility.isEmptyString(color) && color.startsWith("#")) {
            return color;
        }else {
            return default_color;
        }
    } catch (Exception e) {
        e.printStackTrace();
        color = default_color;
    }
    return color;
    }
    // PolyLine
    public static String getPolyLineColorTransparentCode(Activity mActivity)
    {
        String color = getPolyLineColorHexCode(mActivity);
        color = color.replace("#", "#4D");
        return color;
    }


    // Polygon
    public static String getColorHexCode(Activity mActivity){
        String default_color = Utility.COLOR_CODE.YELLOW;
        String color = Utility.getSavedData(mActivity, Utility.LAYER_LINE_COLOR);
        try {
            if (!Utility.isEmptyString(color) && color.startsWith("#")) {
                  return color;
            }else {
                return default_color;
            }
        } catch (Exception e) {
            e.printStackTrace();
            color = default_color;
        }
        return color;
    }
    // Polygon
    public static String getColorTransparentCode(Activity mActivity)
    {
        String color = getColorHexCode(mActivity);
        color = color.replace("#", "#4D");
        return color;
    }


    public static String getLayerTransparentColorCode(Activity mActivity)
    {
        String default_color = Utility.COLOR_CODE.PINK;
        String color = Utility.getSavedData(mActivity, Utility.LAYER_LINE_COLOR);
        if(Utility.isEmptyString(color))
            color = default_color;
        color = color.replace("#", "#4D");
        return color;
    }


    public static void setTextSize(Activity mActivity, TextView txtDistance) {
        String defTextSize = Utility.getSavedData(mActivity, Utility.TEXT_SIZE);
        float textSize = mActivity.getResources().getDimension(R.dimen._10ssp);
        if(!Utility.isEmptyString(defTextSize))
        {
            switch (Integer.parseInt(defTextSize))
            {
                case 10:
                    textSize = mActivity.getResources().getDimension(R.dimen._4ssp);
                    break;
                case 11:
                    textSize = mActivity.getResources().getDimension(R.dimen._5ssp);
                    break;
                case 12:
                    textSize = mActivity.getResources().getDimension(R.dimen._6ssp);
                    break;
                case 13:
                    textSize = mActivity.getResources().getDimension(R.dimen._7ssp);
                    break;
                case 14:
                    textSize = mActivity.getResources().getDimension(R.dimen._8ssp);
                    break;
                case 15:
                    textSize = mActivity.getResources().getDimension(R.dimen._9ssp);
                    break;
                case 16:
                    textSize = mActivity.getResources().getDimension(R.dimen._10ssp);
                    break;
                case 17:
                    textSize = mActivity.getResources().getDimension(R.dimen._11ssp);
                    break;
                case 18:
                    textSize = mActivity.getResources().getDimension(R.dimen._12ssp);
                    break;
                case 19:
                    textSize = mActivity.getResources().getDimension(R.dimen._13ssp);
                    break;
                case 20:
                    textSize = mActivity.getResources().getDimension(R.dimen._14ssp);
                    break;
            }
        }
        txtDistance.setTextSize(textSize);
    }

    public static LatLng getCentreLatLngFrom2LatLng(LatLng position1, LatLng position2)
    {
        double dLon = Math.toRadians(position1.longitude - position2.longitude);

        double lat1 = Math.toRadians(position2.latitude);
        double lat2 = Math.toRadians(position1.latitude);
        double lon1 = Math.toRadians(position2.longitude);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

        lat3 = Math.toDegrees(lat3);
        lon3 = Math.toDegrees(lon3);

        return new LatLng(lat3, lon3);
    }

    public static BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0,60, 60);
        Bitmap bitmap = Bitmap.createBitmap(60, 60, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    public static BitmapDescriptor getProjectMarkerIcon(Context context)
    {
        Canvas canvas = new Canvas();
        Drawable drawable = context.getDrawable(R.drawable.bg_marker_circle_project_icon);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static BitmapDescriptor getCustomMarkerIconForDistance(Activity context, String distance)
    {
        LinearLayout distanceMarkerLayout = (LinearLayout) context.getLayoutInflater().inflate(R.layout.custom_icon_info, null);

        distanceMarkerLayout.setDrawingCacheEnabled(true);
        distanceMarkerLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        distanceMarkerLayout.layout(0, 0, distanceMarkerLayout.getMeasuredWidth(), distanceMarkerLayout.getMeasuredHeight());
        distanceMarkerLayout.buildDrawingCache(true);

        TextView positionDistance = (TextView) distanceMarkerLayout.findViewById(R.id.txtTitle);

//        positionDistance.setText(distance);
        positionDistance.setText("");

        Bitmap flagBitmap = Bitmap.createBitmap(distanceMarkerLayout.getDrawingCache());
        distanceMarkerLayout.setDrawingCacheEnabled(false);
        BitmapDescriptor flagBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(flagBitmap);
        return flagBitmapDescriptor;
    }

    public static BitmapDescriptor getRoundedMarkerIconGreen(Activity activity)
    {
        Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.bg_marker_circle_green);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    public static BitmapDescriptor getRoundedMarkerIconRed(Activity activity)
    {
        Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.bg_marker_circle_red);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }




    public static Marker addProjectMarkerToMap(GoogleMap mMap, LatLng latLng, Activity mActivity)
    {
        return mMap.addMarker(
                new MarkerOptions()
                        .icon(SystemUtility.getProjectMarkerIcon(mActivity))
                        .position(latLng)
                        .draggable(false)
                        .anchor(.5f, .5f));
    }

    public static Marker addBigMarkerToMap(GoogleMap mMap, LatLng latLng, Activity mActivity)
    {
        return mMap.addMarker(
                new MarkerOptions()
                        .icon(SystemUtility.getRoundedMarkerIconGreen(mActivity))
                        .position(latLng)
                        .draggable(true)
                        .anchor(.5f, .5f));
    }
    public static Marker addViewLayerMarkerToMap(GoogleMap mMap, LatLng latLng, Activity mActivity)
    {
        return mMap.addMarker(
                new MarkerOptions()
                        .icon(SystemUtility.getRoundedMarkerIconGreen(mActivity))
                        .position(latLng)
                        .draggable(true)
                        .anchor(0.5f, .5f));
    }
    public static Marker addSmallMarkerToMap(GoogleMap mMap, LatLng latLng, String displayDistance, Activity mActivity)
    {
        return mMap.addMarker(
                new MarkerOptions()
                        .icon(SystemUtility.getCustomMarkerIconForDistance(mActivity, displayDistance))
                        .position(latLng)
                        .draggable(true)
                        .anchor(.5f, .75f));
    }

    public static Marker addTempOrangeMarkerToMap(GoogleMap mMap, LatLng latLng)
    {
        return mMap.addMarker(
                new MarkerOptions().
                        icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).
                        position(latLng));
    }

    public static Marker updateToBigMarkerOnMap(Marker marker, Activity mActivity)
    {
        marker.setIcon(SystemUtility.getRoundedMarkerIconGreen(mActivity));
        marker.setAnchor(.5f, .5f);
        marker.setTag(marker.getPosition().latitude+", "+marker.getPosition().longitude);
        return marker;
    }

    public static Marker updateToSmallMarkerOnMap(Marker marker, LatLng centrePosition, String display_distance, Activity mActivity)
    {
        marker.setPosition(centrePosition);
        marker.setIcon(SystemUtility.getCustomMarkerIconForDistance(mActivity, display_distance));
        marker.setTag(display_distance);
        return marker;
    }

    public static String getTotalDistanceFromAllVertices(List<LatLng> latLngList, Activity mActivity)
    {
        int totalDistance = 0;
        for(int i=0; i<latLngList.size(); i++)
        {
            if(i%2==0 && (i+2)<latLngList.size())
            {
                LatLng latLng1 = latLngList.get(i);
                LatLng latLng2 = latLngList.get(i+2);
                double distance = SphericalUtil.computeDistanceBetween(latLng1, latLng2);
                totalDistance+=distance;
            }
        }
        return convertDistanceFromMeter(mActivity, totalDistance);
    }

    private String getDistanceBetween2Points(LatLng latLng1, LatLng latLng2, Activity mActivity) {
        double distance = SphericalUtil.computeDistanceBetween(latLng1, latLng2);
        return convertDistanceFromMeter(mActivity, distance);
    }

    public static ArrayList<ArrayList<LatLng>> getClonedList(ArrayList<ArrayList<LatLng>> originalList)
    {
        Gson gson = new Gson();
        String listAsJson = gson.toJson(originalList);
        return gson.fromJson(listAsJson, new TypeToken<ArrayList<ArrayList<LatLng>>>() {}.getType());
    }

    public static ArrayList<LatLng> getClonedListLatLng(ArrayList<LatLng> originalList)
    {
        Gson gson = new Gson();
        String listAsJson = gson.toJson(originalList);
        return gson.fromJson(listAsJson, new TypeToken<ArrayList<LatLng>>() {}.getType());
    }

    //---------------------------------------------- Internet Connect ------------------------------------------------------------------------------------------------------------------------

    public static boolean isInternetConnected(Context context) {
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.v("connectivity", e.toString());
        }
        return connected;
    }


}
