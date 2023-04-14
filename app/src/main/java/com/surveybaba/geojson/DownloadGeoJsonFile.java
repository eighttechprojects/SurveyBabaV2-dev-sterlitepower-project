package com.surveybaba.geojson;

import android.app.Activity;
import android.os.AsyncTask;

import com.surveybaba.model.BinLayerProject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class DownloadGeoJsonFile extends AsyncTask<String, Void, String> {

    Activity activity;
    onDownloadGeoJson onDownloadGeoJson;
    BinLayerProject binLayerProject;


//------------------------------------------------------- Constructor  ------------------------------------------------------------

    public DownloadGeoJsonFile(Activity activity, onDownloadGeoJson onDownloadGeoJson) {
        this.activity = activity;
        this.onDownloadGeoJson = onDownloadGeoJson;
        binLayerProject = new BinLayerProject();
    }


    @Override
    protected String doInBackground(String... params) {
        try {
            // Open a stream from the URL
            String urlNew = params[0];
            if(urlNew.startsWith("http:"))
            {
                urlNew = urlNew.replace("http:", "https:");
            }
            InputStream stream = new URL(urlNew).openStream();

            String line;
            StringBuilder result = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            while ((line = reader.readLine()) != null) {
                // Read and save each line of the stream
                result.append(line);
            }

            // Close the stream
            reader.close();
            stream.close();
            binLayerProject.setUrl(params[0]);
            binLayerProject.setType(params[1]);
            binLayerProject.setDisplay(params[2]);
            binLayerProject.setFormId(params[3]);
            binLayerProject.setFormName(params[4]);
//          return new GeoJsonLayer(getMap(), new JSONObject(result.toString()));
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String geoJsonReponse) {
        binLayerProject.setGeoJson(geoJsonReponse);
        onDownloadGeoJson.getData(binLayerProject);
    }

//------------------------------------------------------- Interface  ------------------------------------------------------------

    public interface onDownloadGeoJson {
        void getData(BinLayerProject binLayerProject);
    }
}