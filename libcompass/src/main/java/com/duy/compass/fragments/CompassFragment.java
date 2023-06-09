package com.duy.compass.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.compass.R;
import com.duy.compass.location.LocationHelper;
import com.duy.compass.sensor.SensorListener;
import com.duy.compass.sensor.view.AccelerometerView;
import com.duy.compass.sensor.view.CompassView2;
import com.duy.compass.utils.Utility;
import com.duy.compass.location.database.CompassPref;
import com.duy.compass.location.model.LocationData;
import com.duy.compass.location.model.Sunshine;

import java.util.Locale;

import static com.duy.compass.utils.Utility.getDirectionText;

/**
 * Created by Duy on 10/17/2017.
 */

public class CompassFragment extends BaseFragment implements SensorListener.OnValueChangedListener,
        LocationHelper.LocationDataChangeListener {
    public static final String TAG = "CompassFragment";
    private static final int REQUEST_ENABLE_GPS = 1002;
    private TextView mTxtAddress;
    private TextView mTxtSunrise, mTxtSunset;
    private TextView mTxtPitch, mTxtRoll;
    private TextView mTxtLonLat, mTxtAltitude;
    private TextView mTxtPressure, mTxtHumidity, mTxtTemp;
    private ImageView mImgWeather;
    private LocationHelper mLocationHelper;
    private CompassView2 mCompassView;
    private AccelerometerView mAccelerometerView;
    private SensorListener mSensorListener;
    private CompassPref mCompassPref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompassPref = new CompassPref(getActivity());
    }

    public static CompassFragment newInstance() {

        Bundle args = new Bundle();

        CompassFragment fragment = new CompassFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindView();

        mLocationHelper = new LocationHelper(this);
        mLocationHelper.setLocationValueListener(this);
        mLocationHelper.onCreate();

        mSensorListener = new SensorListener(getContext());
        mSensorListener.setOnValueChangedListener(this);

        if (!Utility.isNetworkAvailable(getContext())) {
            Toast.makeText(getContext(), "No internet access", Toast.LENGTH_SHORT).show();
        } else {
            LocationManager manager = (LocationManager) getContext()
                    .getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            }
        }

        onUpdateLocationData(null);
    }

    private void bindView() {
        mTxtAddress = (TextView) findViewById(R.id.txt_address);
        mTxtAddress.setSelected(true);

        mTxtSunrise = (TextView) findViewById(R.id.txt_sunrise);
        mTxtSunset = (TextView) findViewById(R.id.txt_sunset);

        mTxtLonLat = (TextView) findViewById(R.id.txt_lon_lat);
        mTxtAltitude = (TextView) findViewById(R.id.txt_altitude);

        mCompassView = (CompassView2) findViewById(R.id.compass_view);
        mAccelerometerView = (AccelerometerView) findViewById(R.id.accelerometer_view);

        mTxtPressure = (TextView) findViewById(R.id.txt_pressure);
        mTxtHumidity = (TextView) findViewById(R.id.txt_humidity);
        mImgWeather = (ImageView) findViewById(R.id.img_weather);
        mTxtTemp = (TextView) findViewById(R.id.txt_temp);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mSensorListener != null) {
            mSensorListener.start();
        }
    }

    @Override
    public void onStop() {
        if (mSensorListener != null) {
            mSensorListener.stop();
        }
        super.onStop();

    }


    @Override
    public void onRotationChanged(float azimuth, float roll, float pitch) {
        String str = ((int) azimuth) + "° " + getDirectionText(azimuth);
        mCompassView.getSensorValue().setRotation(azimuth, roll, pitch);
        mAccelerometerView.getSensorValue().setRotation(azimuth, roll, pitch);

    }

    @Override
    public void onMagneticFieldChanged(float value) {
        mCompassView.getSensorValue().setMagneticField(value);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_compass;
    }

    //https://stackoverflow.com/questions/39336461/how-can-i-enable-or-disable-the-gps-programmatically-on-android-6-x
    private void buildAlertMessageNoGps() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, REQUEST_ENABLE_GPS);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_GPS:
                mLocationHelper.onCreate();
                break;
        }
    }


    @Override
    public void onUpdateLocationData(@Nullable LocationData locationData) {
        if (locationData == null && mCompassPref!=null) {
            locationData = mCompassPref.getLastedLocationData();
        }
        if (locationData != null) {
            //sunshine
            Sunshine sunshine = locationData.getSunshine();
            if (sunshine != null) {
                mTxtSunrise.setText(sunshine.getReadableSunriseTime());
                mTxtSunset.setText(sunshine.getReadableSunsetTime());
            }

            //weather
            int resId = Utility.getIconResourceForWeatherCondition(locationData.getId());
            if (resId != -1) mImgWeather.setImageResource(resId);
            mTxtPressure.setText(String.format(Locale.US, "%s hPa", locationData.getPressure()));
            mTxtHumidity.setText(String.format(Locale.US, "%s %%", locationData.getHumidity()));
            mTxtTemp.setText(Utility.formatTemperature(getContext(), locationData.getTemp()));

            //address
            mTxtAddress.setText(locationData.getAddressLine());

            //location
            float longitude = (float) locationData.getLongitude();
            float latitude = (float) locationData.getLatitude();
            String lonStr = Utility.formatDms(longitude) + " " + getDirectionText(longitude);
            String latStr = Utility.formatDms(latitude) + " " + getDirectionText(latitude);
            mTxtLonLat.setText(String.format("%s\n%s", lonStr, latStr));

            //altitude
            double altitude = locationData.getAltitude();
            mTxtAltitude.setText(String.format(Locale.US, "%d m", (long) altitude));
        }
    }
}
