package com.duy.compass.location;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.StrictMode;
import androidx.annotation.Nullable;
import android.util.Log;

import com.duy.compass.location.model.LocationData;
import com.duy.compass.location.model.Sunshine;
import com.duy.compass.utils.DLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherManager {


    private static final String TAG = "WeatherManager";
    private Context mContext;

    public WeatherManager(Context context) {
        this.mContext = context;
    }

    public static Sunshine getSunTimeFromJson(String forecastJsonStr) throws JSONException {
        DLog.d(TAG, "getSunTimeFromJson() called with: forecastJsonStr = [" + forecastJsonStr + "]");

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONObject sysJson = forecastJson.getJSONObject("sys");
        long sunrise = sysJson.getLong("sunrise");
        long sunset = sysJson.getLong("sunset");
        return new Sunshine(sunset * 1000, sunrise * 1000);
    }

    public static String getWeatherForecastData(double lon, double lat) {
        //These two need to be declared outside the try/catch
        //so that they can be closed in the finally block
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        //Wil contain the raw JSON response as a string
        String forecastJsonStr = null;

        String format = "json";
        String units_value = "metric";//standard, metric, and imperial
        int numDays = 14;
        String key = "96d5755e7051fe523779274495e5919e";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            final String forecastBaseUrl = "http://api.openweathermap.org/data/2.5/weather?";
            final String longitudeParam = "lon";
            final String latitudeParam = "lat";
            final String units_key = "units";
            final String apiKey = "appid";

            Uri builtUri = Uri.parse(forecastBaseUrl).buildUpon()
                    .appendQueryParameter(longitudeParam, String.valueOf(lon))
                    .appendQueryParameter(latitudeParam, String.valueOf(lat))
                    .appendQueryParameter(units_key, units_value)
                    .appendQueryParameter(apiKey, key)
                    .build();

            URL url = new URL(builtUri.toString());
            Log.e(TAG, "URL: "+url.toString());
            InputStream stream = url.openStream();

            String line;
            StringBuilder result = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(stream));

            while ((line = reader.readLine()) != null) {
                // Read and save each line of the stream
                result.append(line);
            }
            forecastJsonStr = result.toString();
            Log.e(TAG, "URL Resp: "+forecastJsonStr);
            // Close the stream
            reader.close();
            stream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return forecastJsonStr;
    }

    @Nullable
    public static LocationData getWeatherData(Location location, LocationData weatherData) {
        DLog.d(TAG, "getWeatherData() called with: location = [" + location + "]");
        try {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            String weatherForecast = getWeatherForecastData(lon, lat);
            return getWeatherDataFromJson(weatherForecast, weatherData);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     * <p>
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private static LocationData getWeatherDataFromJson(String forecastJsonStr, LocationData locationData)
            throws JSONException {
        DLog.d(TAG, "getWeatherDataFromJson() called with: forecastJsonStr = [" + forecastJsonStr + "]");

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONObject mainData = forecastJson.getJSONObject("main");

        locationData.setTemp((float) mainData.getDouble("temp"));
        locationData.setTempMax((float) mainData.getDouble("temp_max"));
        locationData.setTempMin((float) mainData.getDouble("temp_min"));
        locationData.setHumidity((float) mainData.getDouble("humidity"));
        locationData.setPressure((float) mainData.getDouble("pressure"));

        JSONObject sysJson = forecastJson.getJSONObject("sys");
        long sunrise = sysJson.getLong("sunrise");
        long sunset = sysJson.getLong("sunset");
        Sunshine sunshine = new Sunshine(sunset * 1000, sunrise * 1000);
        locationData.setSunshine(sunshine);

        JSONArray weatherArray = forecastJson.getJSONArray("weather");
        JSONObject weatherObject = weatherArray.getJSONObject(0);
        int id = weatherObject.getInt("id");
        locationData.setId(id);

        return locationData;
    }

    public Context getContext() {
        return mContext;
    }

}
