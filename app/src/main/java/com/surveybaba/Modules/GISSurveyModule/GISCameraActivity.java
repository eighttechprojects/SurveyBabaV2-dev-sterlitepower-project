package com.surveybaba.Modules.GISSurveyModule;

import static com.surveybaba.Utilities.Utility.PASS_LAT;
import static com.surveybaba.Utilities.Utility.PASS_LONG;
import static com.surveybaba.Utilities.Utility.PASS_SURVEY_ID;
import static com.surveybaba.Utilities.Utility.PASS_WORK_ID;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.surveybaba.LocationAssist.LocationAssistant;
import com.surveybaba.R;
import com.surveybaba.Utilities.ImageFileUtils;
import com.surveybaba.Utilities.SystemPermission;
import com.surveybaba.Utilities.Utility;
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
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class GISCameraActivity extends AppCompatActivity implements View.OnClickListener , LocationAssistant.Listener{

    // TAG
    private static final String TAG = "GISCameraActivity";
    // Activity
    private Activity mActivity;
    // Permission Code
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int REQUEST_TAKE_PHOTO = 110;
    // ALERT Message
    private static final String NEED_CAMERA_PERMISSION_ALERT   = "Need Camera Permission to Access Camera. \n\nGo to Setting -> App info -> SurveyBaba -> Permissions -> Allow Camera";
    // System Permission
    private SystemPermission systemPermission;
    // Location
    private LocationAssistant assistant;
    private Location mCurrentLocation;
    // Database
    private DataBaseHelper dataBaseHelper;
    // Button
    private Button cameraSubmitButton,cameraCancelButton;
    // Edit Text
    private EditText cameraDescription;
    // ImageView
    private ImageView cameraImage;
    //  File
    private File destFile, destFileTemp;
    // Image File Utils
    private ImageFileUtils imageFileUtils;
    // Progress Dialog Bar
    private ProgressDialog progressDialog;
    // Camera
    LinearLayout llPreview;
    RelativeLayout llMain;
    ImageView imgPreview;
    TextView txtGeoTag;
    long totalSize = 0;
    boolean isLocation = false;
    // Camera Data
    String cameraLat = "";
    String cameraLon = "";
    String imageDatetime="";
    String survey_id = "";
    String work_id = "";

//------------------------------------------------------- On Create ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giscamera);


        mActivity = this;
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
        // Init Extra
        initExtra();
        //set On Click Listener
        setOnClickListener();
        // base Application
        BaseApplication baseApplication = (BaseApplication) getApplication();
        SystemPermission systemPermission = new SystemPermission(this);
        if(systemPermission.isLocation()){
            baseApplication.startMyService();
        }

    }

//------------------------------------------------------- initExtra ----------------------------------------------------------------------------------------------------------------------

    private void initExtra() {
        Intent intent = getIntent();
        cameraLat = intent.getStringExtra(PASS_LAT);
        cameraLon = intent.getStringExtra(PASS_LONG);
        survey_id = intent.getStringExtra(PASS_SURVEY_ID);
        work_id    = intent.getStringExtra(PASS_WORK_ID);
        Log.e(TAG, "Camera Lat: "+ cameraLat +" Camera Long: " + cameraLon +" SurveyID: " + survey_id + " WorkID: "+ work_id);
    }


//------------------------------------------------------- Init ---------------------------------------------------------------------------------------------------------------------------

    private void init(){
        // Button
        cameraSubmitButton = findViewById(R.id.cameraSubmitButton);
        cameraCancelButton = findViewById(R.id.cameraCancelButton);
        // Edit text
        cameraDescription  = findViewById(R.id.cameraDescription);
        // ImageView
        cameraImage        = findViewById(R.id.cameraImage);
        // Linear layout
        llMain             = findViewById(R.id.llMain);
        llPreview          = findViewById(R.id.llPreview);
        // Image Preview
        imgPreview         = findViewById(R.id.imgPreview);
        // TextView
        txtGeoTag          = findViewById(R.id.txtGeoTag);
    }
    private void updatePreviewUI(boolean isUpdate) {
        llMain.setVisibility(isUpdate ? View.GONE : View.VISIBLE);
        llPreview.setVisibility(isUpdate ? View.VISIBLE : View.GONE);
    }

//------------------------------------------------------- setOnClickListener ---------------------------------------------------------------------------------------------------------------------------

    private void setOnClickListener(){
        cameraSubmitButton.setOnClickListener(this);
        cameraCancelButton.setOnClickListener(this);
        cameraImage.setOnClickListener(this);
    }

//------------------------------------------------------- On Click ----------------------------------------------------------------------------------------------------------------------

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.cameraSubmitButton:
                cameraSubmit();
                break;

            case R.id.cameraCancelButton:
                setResult(RESULT_CANCELED);
                finish();
                break;

            case R.id.cameraImage:
                if(systemPermission.isExternalStorage()){
                    Camera();
                }
                break;
        }
    }

//------------------------------------------------------- Camera Menu ----------------------------------------------------------------------------------------------------------------------


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Back Button
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


//------------------------------------------------------- Camera Submit ----------------------------------------------------------------------------------------------------------------------

    private void cameraSubmit(){

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

    private void saveToLocalDatabase()
    {
        String user_id = Utility.getSavedData(mActivity, Utility.LOGGED_USERID);
        String desc = cameraDescription.getText().toString();
        dataBaseHelper.insertGISMapImage(user_id,survey_id,work_id,desc,cameraLat,cameraLon,imageDatetime, URL_Utility.APP_VERSION, destFile.getAbsolutePath(),destFile.getName());
        dataBaseHelper.insertGISMapImageLocal(user_id,survey_id,work_id,desc,cameraLat,cameraLon,imageDatetime, URL_Utility.APP_VERSION, destFile.getAbsolutePath(),destFile.getName());
        imageDatetime = "";
        cameraLat = "";
        cameraLon = "";
        survey_id = "";
        work_id = "";
        destFile = null;
        Glide.with(mActivity).load(destFile).error(R.drawable.icon_camera).into(cameraImage);
        cameraDescription.setText("");
        saveSuccessfullyMessage();
        Log.e(TAG,"Camera Image Save to Local Database");
    }

//------------------------------------------------------- Image Upload ------------------------------------------------------------

    private void imageUpload() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Image Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        new GISCameraActivity.FileUploadToServe().execute();
    }


    private void saveSuccessfullyMessage() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
        alertDialog.setMessage("Image Save Successfully");
        alertDialog.setPositiveButton(getResources().getString(R.string.lbl_Ok), (dialog, which) -> {
            setResult(RESULT_OK);
            finish();
            dialog.dismiss();
        });
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
        destFileTemp = imageFileUtils.getDestinationFile(imageFileUtils.getRootDirFile(mActivity));
        Uri photoURI = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".provider", destFileTemp);
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
                AlertDialog alertDialog = new AlertDialog.Builder(mActivity)
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
                    Log.e(TAG,"Camera Image Capture");
                    // Set location Data here
                    getGeoTagData();
                    //txtGeoTag.setText(getGeoTagData());
                    Bitmap bitmapPreview = ImageFileUtils.handleSamplingAndRotationBitmap(mActivity, Uri.fromFile(destFileTemp));
                    File destFileTemp2 = imageFileUtils.getDestinationFileImageInput(imageFileUtils.getRootDirFile(this));
                    ImageFileUtils.saveBitmapToFile(bitmapPreview, destFileTemp2);
                    imgPreview.setImageBitmap(bitmapPreview);
                    updatePreviewUI(true);
                    new Handler().postDelayed(() -> {
                        updatePreviewUI(false);
                        destFile = imageFileUtils.getDestinationFileImageInput(imageFileUtils.getRootDirFile(mActivity));
                        if (ImageFileUtils.takeScreenshot(llPreview, destFile)) {
                            Log.e("Picture", "screenshot capture success");
                        } else {
                            destFile = destFileTemp2;
                            Log.e("Picture", "screenshot capture failed");
                        }
                        Glide.with(mActivity).load(destFile).error(R.drawable.icon_camera).into(cameraImage);
                    }, 400);
                }
                catch (Exception e){
                    Log.e(TAG,"Camera Image Not Capture");
                    e.printStackTrace();
                }
            }
            else{
                Log.e(TAG,"Camera Image Not Capture");
                Toast.makeText(this, "Image Not Capture", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getGeoTagData() {
        StringBuilder stringBuilder = new StringBuilder();
        if (mCurrentLocation != null){
            //imageLat = "" + mCurrentLocation.getLatitude();
            //imageLon = "" + mCurrentLocation.getLongitude();
            stringBuilder.append("Latitude : " + "").append(mCurrentLocation.getLatitude());
            stringBuilder.append("\n");
            stringBuilder.append("Longitude : " + "").append(mCurrentLocation.getLongitude());
            stringBuilder.append("\n");
        }
        imageDatetime  = getRecordDate();
        stringBuilder.append("Date: ").append(getRecordDate());
    }

    public String getRecordDate()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return df.format(c.getTime());
    }


//------------------------------------------------------- Location ---------------------------------------------------------------------------------------------------------------------------

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
        new AlertDialog.Builder(this).setMessage(R.string.switchOnLocationShort).setPositiveButton(R.string.lbl_Ok, (dialog, which) -> {
            dialog.dismiss();
            assistant.changeLocationSettings();
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
            progressDialog.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }
        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL_Utility.WS_UPLOAD_GIS_CAMERA_IMAGE);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(num -> publishProgress((int) ((num / (float) totalSize) * 100)));
                // Normal data Upload!
                File sourceFile = new File(destFile.getPath());
                String data = "";
                JSONObject params = new JSONObject();
                try {
                    params.put(URL_Utility.PARAM_LOGIN_USER_ID, (Utility.getSavedData(mActivity, Utility.LOGGED_USERID)));
                    params.put(URL_Utility.PARAM_SURVEY_ID, survey_id);
                    params.put(URL_Utility.PARAM_WORK_ID, work_id);
                    params.put(URL_Utility.PARAM_VERSION, (URL_Utility.APP_VERSION));
                    params.put(URL_Utility.PARAM_LATITUDE, (cameraLat));
                    // Login Token
                    params.put(URL_Utility.PARAM_UNIQUE_NUMBER,Utility.getToken());
                    params.put(URL_Utility.PARAM_LONGITUDE, (cameraLon));
                    params.put(URL_Utility.PARAM_DATETIME, (imageDatetime));
                    params.put(URL_Utility.PARAM_IMAGE_DESC, (cameraDescription.getText().toString().trim()));
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
                        String user_id = Utility.getSavedData(mActivity, Utility.LOGGED_USERID);
                        String desc = cameraDescription.getText().toString();
                        dataBaseHelper.insertGISMapImageLocal(user_id,survey_id,work_id,desc,cameraLat,cameraLon,imageDatetime, URL_Utility.APP_VERSION, destFile.getAbsolutePath(),destFile.getName());
                        saveSuccessfullyMessage();
                        imageDatetime = "";
                        cameraLat = "";
                        cameraLon = "";
                        destFile = null;
                        Glide.with(mActivity).load(R.drawable.icon_camera).error(R.drawable.icon_camera).into(cameraImage);
                        cameraDescription.setText("");
                    }
                    else{
                        // when image not upload to server!
                        saveToLocalDatabase();
                    }
                } catch (JSONException e) {
                    Toast.makeText(mActivity, "Internet Issue try to Save offline", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            super.onPostExecute(result);
        }
    }


}