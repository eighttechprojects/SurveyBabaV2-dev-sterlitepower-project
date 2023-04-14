package com.fileupload;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.surveybaba.R;
import com.volly.URL_Utility;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VideoFileUploadToServe extends AsyncTask<Void, Integer, Boolean> {

    private ProgressDialog progressDialog;
    private String sourceFilePath;
    private Context context;
    private long totalSize = 0;
    private onVideoUpload onVideoUpload;
    public interface onVideoUpload
    {
        void getResult(boolean isSuccess);
    }

    public VideoFileUploadToServe(Context context, String sourceFilePath, onVideoUpload onVideoUpload) {
        this.context = context;
        this.sourceFilePath = sourceFilePath;
        this.onVideoUpload = onVideoUpload;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(context.getResources().getString(R.string.lbl_File_upload));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setProgress(0);
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        progressDialog.setProgress(progress[0]);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return uploadFile();
    }

    @SuppressWarnings("deprecation")
    private boolean uploadFile() {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(URL_Utility.BASE_VIDEO_URL);
        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                @Override
                public void transferred(long num) {
                    publishProgress((int) ((num / (float) totalSize) * 100));
                }
            });
            File sourceFile = new File(sourceFilePath);
            entity.addPart(URL_Utility.PARAM_VIDEO, new FileBody(sourceFile));
            entity.addPart(URL_Utility.PARAM_UNIQ_ID, new StringBody(getDateDDMMYYYYHHMMSS()));
            totalSize = entity.getContentLength();
            httppost.setEntity(entity);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                responseString = EntityUtils.toString(r_entity);
//                onVideoUpload.getResult(responseString, true);
                return true;
            } else {
                responseString = "Error occurred! Http Status Code: " + statusCode;
//                onVideoUpload.getResult(responseString, false);
                return false;
            }
        } catch (ClientProtocolException e) {
            responseString = e.toString();
            return false;
//            onVideoUpload.getResult(responseString, false);
        } catch (IOException e) {
            responseString = e.toString();
            return false;
//            onVideoUpload.getResult(responseString, false);
        }
//        return responseString;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if(progressDialog!=null)
            progressDialog.dismiss();
        onVideoUpload.getResult(result);
    }

    private String getDateDDMMYYYYHHMMSS() {
        return new SimpleDateFormat("dd_MM_yyyy_HHmmssSSS", Locale.US).format(new Date());
    }
}