package com.surveybaba.Modules;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fileupload.AndroidMultiPartEntity;
import com.surveybaba.AESCrypt.AESCrypt;
import com.surveybaba.BuildConfig;
import com.surveybaba.Database.DataBaseHelper;
import com.surveybaba.FormBuilder.FormDataModel;
import com.surveybaba.LocationAssist.LocationAssistant;
import com.surveybaba.R;
import com.surveybaba.Utilities.ImageFileUtils;
import com.surveybaba.Utilities.SystemPermission;
import com.surveybaba.Utilities.SystemUtility;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.CameraModule;
import com.surveybaba.model.GpsTrackingModule;
import com.surveybaba.model.TrackingStatusData;
import com.volly.BaseApplication;
import com.volly.URL_Utility;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener ,LocationAssistant.Listener {

    private static final String TAG = "CameraActivity";
    private Button dbCameraSubmitButton,dbCameraCancelButton;
    private EditText dbCameraDescription;
    private ImageView dbCameraImage;
    // Permission Code
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int REQUEST_TAKE_PHOTO = 110;
    // ALERT Message
    private static final String NEED_CAMERA_PERMISSION_ALERT   = "Need Camera Permission to Access Camera. \n\nGo to Setting -> App info -> SurveyBaba -> Permissions -> Allow Camera";
    // System Permission
    private SystemPermission systemPermission;
    // image File
    private File destFile, destFileTemp;
    private ImageFileUtils imageFileUtils;
    // Location
    private LocationAssistant assistant;
    private Location mCurrentLocation;
    // Database
    private DataBaseHelper dataBaseHelper;
    // Location for Image
    LinearLayout llPreview;
    RelativeLayout llMain;
    ImageView imgPreview;
    TextView txtGeoTag;

    // Progress Dialog Bar
    private ProgressDialog progressDialog;
    String imageLat = "";
    String imageLon = "";
    String imageDatetime="";
    long totalSize = 0;
    boolean isLocation = false;
    boolean isSync = false;

    Toolbar toolbar;

    private BaseApplication baseApplication;
//------------------------------------------------------- On Create ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbcamera);

        // Add Back Button on ToolBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Location
        assistant = new LocationAssistant(this, this, LocationAssistant.Accuracy.HIGH, 0, false);
        assistant.setVerbose(true);
        // Database
        dataBaseHelper = new DataBaseHelper(this);
        // System Permission
        systemPermission = new SystemPermission(this);
        // Image File Utils
        imageFileUtils = new ImageFileUtils();
        // Init
        init();
        //set On Click Listener
        setOnClickListener();
        // base Application
        baseApplication = (BaseApplication) getApplication();
        SystemPermission systemPermission = new SystemPermission(this);
        if(systemPermission.isLocation()){
            baseApplication.startMyService();
        }

        if(isDataNotSync()){
            Utility.SyncYourDataAlert(this);
        }
    }

//------------------------------------------------------- Init ---------------------------------------------------------------------------------------------------------------------------

    private void init(){
        // Button
        dbCameraSubmitButton = findViewById(R.id.dbCameraSubmitButton);
        dbCameraCancelButton = findViewById(R.id.dbCameraCancelButton);
        // Edit text
        dbCameraDescription  = findViewById(R.id.dbCameraDescription);
        // ImageView
        dbCameraImage        = findViewById(R.id.dbCameraImage);
        // Linear layout
        llMain               = findViewById(R.id.llMain);
        llPreview            = findViewById(R.id.llPreview);
        // Image Preview
        imgPreview           = findViewById(R.id.imgPreview);
        // TextView
        txtGeoTag            = findViewById(R.id.txtGeoTag);
    }

    private void updatePreviewUI(boolean isUpdate) {
        llMain.setVisibility(isUpdate ? View.GONE : View.VISIBLE);
        llPreview.setVisibility(isUpdate ? View.VISIBLE : View.GONE);
    }

//------------------------------------------------------- setOnClickListener ---------------------------------------------------------------------------------------------------------------------------

    private void setOnClickListener(){
        dbCameraSubmitButton.setOnClickListener(this);
        dbCameraCancelButton.setOnClickListener(this);
        dbCameraImage.setOnClickListener(this);
    }

//------------------------------------------------------- On Click ----------------------------------------------------------------------------------------------------------------------

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.dbCameraSubmitButton:
                dbCameraSubmit();
                break;

            case R.id.dbCameraCancelButton:
                finish();
                break;

            case R.id.dbCameraImage:
                if(systemPermission.isExternalStorage()){
                    Camera();
                }
                break;
        }
    }

//------------------------------------------------------- Camera Menu ----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.db_camera_menu, menu);
        return true;
    }

     @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Back Button
         if (item.getItemId() == android.R.id.home) {
             finish();
         }
        if (item.getItemId() == R.id.dbCameraImageSync) {
              SyncAllData();
            //dbCameraSync();
        }
        return super.onOptionsItemSelected(item);
    }


//------------------------------------------------------- Camera Submit ----------------------------------------------------------------------------------------------------------------------

    private void dbCameraSubmit(){

        if(destFile == null) {
            Utility.showInfoDialog(this, getResources().getString(R.string.select_image));
            return;
        }
        // Internet Connected
        if(SystemPermission.isInternetConnected(this)){
            imageUpload();
        }
        // Internet Not Connected
        else{
            saveToLocalDatabase();
        }

    }

    private void saveToLocalDatabase(){
        String user_id = Utility.getSavedData(CameraActivity.this, Utility.LOGGED_USERID);
        String desc = dbCameraDescription.getText().toString();
        dataBaseHelper.insertCameraImage(user_id,desc,imageLat,imageLon,imageDatetime, URL_Utility.APP_VERSION, destFile.getAbsolutePath(),destFile.getName());
        saveSuccessfullyMessage();
        destFile = null;
        Glide.with(CameraActivity.this).load(destFile).error(R.drawable.icon_camera).into(dbCameraImage);
        dbCameraDescription.setText("");
        Log.e(TAG,"Save to Local Database");

    }

    //------------------------------------------------------- Image Upload ------------------------------------------------------------

    private void imageUpload() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.lbl_Image_upload));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
     //   progressDialog.setProgress(0);
     //   progressDialog.setMax(100);
        progressDialog.show();
        new CameraActivity.FileUploadToServe().execute();
    }


    //-------------------------------------------------------  ------------------------------------------------------------

    private void saveSuccessfullyMessage() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Image Save Successfully");
        alertDialog.setPositiveButton(getResources().getString(R.string.lbl_Ok), (dialog, which) -> dialog.dismiss());
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void syncSuccessfullyMessage() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Image Sync Successfully");
        alertDialog.setPositiveButton(getResources().getString(R.string.lbl_Ok), (dialog, which) -> dialog.dismiss());
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void saveNotSuccessfullyMessage() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Image Not Save Successfully Due to Internet Connection issue");
        alertDialog.setPositiveButton(getResources().getString(R.string.lbl_Ok), (dialog, which) -> dialog.dismiss());
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

 //------------------------------------------------------- Camera ----------------------------------------------------------------------------------------------------------------------
    // Camera
    private void Camera(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
        else
        {
            OpenCamera();
        }
    }

    private void OpenCamera()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        destFileTemp = imageFileUtils.getDestinationFile(imageFileUtils.getRootDirFile(this));
        Uri photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", destFileTemp);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
    }

//------------------------------------------------------- On Request Permission Result ------------------------------------------------------------------------------------------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Camera Permission
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Permission Granted
                OpenCamera();
            }
            else{
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_logo_surveybaba)
                        .setTitle("Permission Alert")
                        .setMessage(NEED_CAMERA_PERMISSION_ALERT)
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, i) -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        })
                        .create();
                alertDialog.show();
            }
        }
    }

//------------------------------------------------------- On Activity Result ------------------------------------------------------------------------------------------------------

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Take Photo
        if(requestCode == REQUEST_TAKE_PHOTO){

            if(resultCode == RESULT_OK){
                try {
                    // Set location Data here
                    txtGeoTag.setText(getGeoTagData());
                    Bitmap bitmapPreview = ImageFileUtils.handleSamplingAndRotationBitmap(CameraActivity.this, Uri.fromFile(destFileTemp));
                    File destFileTemp2 = imageFileUtils.getDestinationFileImageInput(imageFileUtils.getRootDirFile(this));
                    ImageFileUtils.saveBitmapToFile(bitmapPreview, destFileTemp2);
                    imgPreview.setImageBitmap(bitmapPreview);
                    updatePreviewUI(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updatePreviewUI(false);
                            destFile = imageFileUtils.getDestinationFileImageInput(imageFileUtils.getRootDirFile(CameraActivity.this));
                            if (ImageFileUtils.takeScreenshot(llPreview, destFile)) {
                                Log.e("Picture", "screenshot capture success");
                            } else {
                               destFile = destFileTemp2;
                               Log.e("Picture", "screenshot capture failed");
                            }
                            Glide.with(CameraActivity.this).load(destFile).error(R.drawable.icon_camera).into(dbCameraImage);
                        }
                    }, 400);

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(this, "Image Not Capture", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private StringBuilder getGeoTagData() {
        StringBuilder stringBuilder = new StringBuilder();
        if (mCurrentLocation != null){
            imageLat = "" + mCurrentLocation.getLatitude();
            imageLon = "" + mCurrentLocation.getLongitude();
            stringBuilder.append("Latitude : " + "").append(mCurrentLocation.getLatitude());
            stringBuilder.append("\n");
            stringBuilder.append("Longitude : " + "").append(mCurrentLocation.getLongitude());
            stringBuilder.append("\n");
        }
        imageDatetime  = getRecordDate();
        stringBuilder.append("Date: ").append(getRecordDate());
        return stringBuilder;
    }

    public String getRecordDate()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return df.format(c.getTime());
    }

//------------------------------------------------------- Camera Sync ----------------------------------------------------------------------------------------------------------------------


    private boolean isDataNotSync(){
        ArrayList<FormDataModel>      formDataList    = dataBaseHelper.getProjectFormList();
        ArrayList<FormDataModel>      gisFormDataList = dataBaseHelper.getGISSurveyFormList();
        ArrayList<GpsTrackingModule>  gpsTrackingList = dataBaseHelper.getGpsTracking();
    //    ArrayList<TrackingStatusData> trackingList    = dataBaseHelper.getTrackingStatusDetails();
        ArrayList<CameraModule>       cameraImageList = dataBaseHelper.getCameraImage();
        ArrayList<CameraModule>       timeLineList    = dataBaseHelper.getTimeLineImage();
        ArrayList<CameraModule>       mapCameraList   = dataBaseHelper.getMapCameraImage();
        ArrayList<CameraModule>       gisCameraList   = dataBaseHelper.getGISMapCameraImage();

        // here when there is not data present in local database then logout directly!
     //   if(cameraImageList.size() == 0 && gpsTrackingList.size() == 0 && formDataList.size() == 0 && trackingList.size() == 0 && timeLineList.size() == 0 && mapCameraList.size() == 0 && gisCameraList.size() == 0 && gisFormDataList.size() == 0){
        if(cameraImageList.size() == 0 && gpsTrackingList.size() == 0 && formDataList.size() == 0  && timeLineList.size() == 0 && mapCameraList.size() == 0 && gisCameraList.size() == 0 && gisFormDataList.size() == 0){
            Log.e(TAG, "Sync Service No Data Found in Local DataBase");
            return false;
        }
        else{
            return true;
        }
    }

    private void SyncAllData(){
    ArrayList<FormDataModel>      formDataList    = dataBaseHelper.getProjectFormList();
    ArrayList<FormDataModel>      gisFormDataList = dataBaseHelper.getGISSurveyFormList();
    ArrayList<GpsTrackingModule>  gpsTrackingList = dataBaseHelper.getGpsTracking();
    ArrayList<TrackingStatusData> trackingList    = dataBaseHelper.getTrackingStatusDetails();
    ArrayList<CameraModule>       cameraImageList = dataBaseHelper.getCameraImage();
    ArrayList<CameraModule>       timeLineList    = dataBaseHelper.getTimeLineImage();
    ArrayList<CameraModule>       mapCameraList   = dataBaseHelper.getMapCameraImage();
    ArrayList<CameraModule>       gisCameraList   = dataBaseHelper.getGISMapCameraImage();

    // here when there is not data present in local database then logout directly!
    if(cameraImageList.size() == 0 && gpsTrackingList.size() == 0 && formDataList.size() == 0 && trackingList.size() == 0 && timeLineList.size() == 0 && mapCameraList.size() == 0 && gisCameraList.size() == 0 && gisFormDataList.size() == 0){
        Log.e(TAG, "Sync Service No Data Found in Local DataBase");
        Utility.getOKDialogBox(this, "Sync", "Data Already Sync", DialogInterface::dismiss);
    }
    else{
        if(SystemPermission.isInternetConnected(this)){
            baseApplication.startSyncService();
        }
    }
}

    @Override
    public void onNeedLocationPermission() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.app_name);
        dialogBuilder.setMessage("Need\nPermission");
        dialogBuilder.setPositiveButton("Ok", (dialog, which) -> {
            dialog.dismiss();
            assistant.requestLocationPermission();
        });
        dialogBuilder.setCancelable(true);
        dialogBuilder.show();
        assistant.requestAndPossiblyExplainLocationPermission();
    }

    @Override
    public void onExplainLocationPermission() {
        assistant.requestLocationPermission();
    }

    @Override
    public void onLocationPermissionPermanentlyDeclined(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {
        new AlertDialog.Builder(this).setMessage(R.string.permissionPermanentlyDeclined).setPositiveButton(R.string.lbl_Ok, fromDialog).show();

    }

    @Override
    public void onNeedLocationSettingsChange() {
        new AlertDialog.Builder(this).setMessage(R.string.switchOnLocationShort).setPositiveButton(R.string.lbl_Ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                assistant.changeLocationSettings();
            }
        }).show();
    }

    @Override
    public void onFallBackToSystemSettings(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.switchOnLocationLong)
                .setPositiveButton(R.string.lbl_Ok, fromDialog)
                .show();
    }

    @Override
    public void onNewLocationAvailable(Location location) {
        if (location == null ){
            showProgressBar();
            isLocation = false;
        }
        else{
            if(!isLocation){
                dismissProgressbar();
            }
            isLocation = true;
            mCurrentLocation = location;
        }
    }

    @Override
    public void onMockLocationsDetected(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

    }

    @Override
    public void onError(LocationAssistant.ErrorType type, String message) {

    }

//------------------------------------------------------- progressBar ----------------------------------------------------------------------------------------------------------------------

    private void showProgressBar() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Fetch Location....");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }
    }

    private void dismissProgressbar() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    //------------------------------------------------------- on Resume ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onResume() {
        super.onResume();
        SystemUtility.setFullscreenToggle(this, Utility.getBooleanSavedData(this, Utility.IS_FULL_SCREEN));
        SystemUtility.setKeepScreenAwakeAlwaysToggle(this, Utility.getBooleanSavedData(this, Utility.IS_ALWAYS_KEEP_SCREEN_AWAKE));
        assistant.start();
    }

//------------------------------------------------------- on Pause ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onPause() {
        assistant.stop();
        super.onPause();
    }

//------------------------------------------------------- on back Pressed ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        finish();
    }

//------------------------------------------------------- File Upload to Server ------------------------------------------------------------

    private class FileUploadToServe extends AsyncTask<Void, Integer, String> {

        public FileUploadToServe() { }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            super.onPreExecute();
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
            //progressDialog.setProgress(progress[0]);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }
        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL_Utility.WS_UPLOAD_CAMERA_IMAGE);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(num -> publishProgress((int) ((num / (float) totalSize) * 100)));
                    // Normal data Upload!
                    File sourceFile = new File(destFile.getPath());
                    String data = "";
                    JSONObject params = new JSONObject();
                    try {
                        params.put(URL_Utility.PARAM_LOGIN_USER_ID, (Utility.getSavedData(CameraActivity.this, Utility.LOGGED_USERID)));
                        params.put(URL_Utility.ACTION_APP_VERSION, (URL_Utility.APP_VERSION));
                        params.put(URL_Utility.PARAM_LATITUDE, (imageLat));
                        // Login Token
                        params.put(URL_Utility.PARAM_LOGIN_TOKEN,Utility.getSavedData(CameraActivity.this,Utility.LOGGED_TOKEN));
                        params.put(URL_Utility.PARAM_LONGITUDE, (imageLon));
                        params.put(URL_Utility.PARAM_DATETIME, (imageDatetime));
                        params.put(URL_Utility.PARAM_IMAGE_DESC, (dbCameraDescription.getText().toString().trim()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        Log.e(TAG, params.toString());
                        data = AESCrypt.encrypt(params.toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    entity.addPart(URL_Utility.PARAM_IMAGE_DATA, new FileBody(sourceFile));
                    entity.addPart("data", new StringBody(data));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    responseString = EntityUtils.toString(r_entity);
                    // Get Response!
                    Log.e("Response",AESCrypt.decryptResponse(responseString));
                }
                else {
                    responseString = "Error occurred! Http Status Code: " + statusCode;
                }
            } catch (IOException e) {
                responseString = e.toString();
            }
            return responseString;
        }
        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            Log.e(TAG,result);
            String response = AESCrypt.decryptResponse(result);
            if(!response.equals("")) {
                try {
                    JSONObject mLoginObj = new JSONObject(response);
                    String status = mLoginObj.optString("status");
                    if (status.equalsIgnoreCase("Success")) {
                        saveSuccessfullyMessage();
                        destFile = null;
                        Glide.with(CameraActivity.this).load(R.drawable.icon_camera).error(R.drawable.icon_camera).into(dbCameraImage);
                        dbCameraDescription.setText("");
                    }
                    else{
                        // when image not upload to server!
                        saveToLocalDatabase();
                    }
                } catch (JSONException e) {
                    Toast.makeText(CameraActivity.this, "Internet Issue try to Save offline", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            super.onPostExecute(result);
        }
    }
}