package com.surveybaba;

import static com.surveybaba.Utilities.Utility.getUploadFilePath;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.fileupload.AndroidMultiPartEntity;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikelau.croperino.CroperinoConfig;
import com.surveybaba.ADAPTER.FormRenderAdapter;
import com.surveybaba.AESCrypt.AESCrypt;
import com.surveybaba.DTO.PathVO;
import com.surveybaba.Database.DataBaseHelper;
import com.surveybaba.FormBuilder.FormDataModel;
import com.surveybaba.FormBuilder.FormDetailData;
import com.surveybaba.FormBuilder.OtherFormData;
import com.surveybaba.LocationAssist.LocationAssistant;
import com.surveybaba.Utilities.ImageFileUtils;
import com.surveybaba.Utilities.MyBroadcastReceiver;
import com.surveybaba.Utilities.SystemPermission;
import com.surveybaba.Utilities.SystemUtility;
import com.surveybaba.Utilities.Utility;
import com.volly.BaseApplication;
import com.volly.URL_Utility;
import com.volly.WSResponseInterface;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class FormDetailsActivity extends AppCompatActivity implements LocationAssistant.Listener , WSResponseInterface {

    //TAG
    private String TAG = "FormDetailsActivity";
    // REQUEST Code
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_CAPTURE_IMAGE_REQUEST_CODE = 101;
    // Location
    private LocationAssistant assistant;
    private Location mCurrentLocation;
    // Activity
    private Activity mActivity;
    // Base Application
    BaseApplication baseApplication;
    // Utility
    private Utility utility;
    // Image File
    private ImageFileUtils imageFileUtils;
    // Permission
    private SystemPermission systemPermission;
    // Database
    private DataBaseHelper dataBaseHelper;
    // Recycler View
    private RecyclerView rcvFormUI;
    // Adapter
    private FormRenderAdapter formRenderAdapter;
    // boolean
    private boolean isServiceStarted;
    public static boolean isFileUpload   = true;
    public static boolean isCameraUpload = true;
    public static boolean isVideoUpload  = true;
    public static boolean isAudioUpload  = true;
    // File
    private File destFileTemp;
    // static
    private int positionCaptureImagePojo;
    // String
    private String received_geomArray,received_geomType,imageStoragePath;
    private String work_id = "",layer_id = "",project_id = "", form_id = "", form_name = "", geom_id = "";
    private String line_color = "";
    private String line_type = "";
    private String icon = "";
    private String icon_width = "";
    private String icon_heigth = "";
    private String layer_type = "";
    private String formbg_color ="";
    private String form_logo ="";
    private String form_sno = "";
    private String unique_number ="", datetime = "";
    public static final String TYPE_FILE   = "file";
    public static final String TYPE_CAMERA = "cameraUploader";
    public static final String TYPE_VIDEO  = "videoUploader";
    public static final String TYPE_AUDIO  = "audioUploader";
    String col_nameFile   = "";
    String col_nameCamera = "";
    String col_nameVideo  = "";
    String col_nameAudio  = "";
    // TextView
    private TextView tvUsername,txtGeoTag;
    // Linear Layout
    private LinearLayout llBackupManager, llSetting, llLogout,llViewContainer,llMain,llPreview,llBackupManagerView, llSettingView;
    private LinearLayout llFormLogoLayout;
    // Relative Layout
    private RelativeLayout formLayout;
    // ImageView
    ImageView imgPreview;
    private ImageView imgFormLogo;
    // Button
    private Button btnSubmit, btnExit;
    private Map<String, String> imageData;
    private ArrayList<PathVO> imagePathAll;
    private ArrayList<FormDetailData> listFormDetailsData = new ArrayList<>();
    // progress Dialog
    public static ProgressDialog progressDialog;
    // Dialog
    AlertDialog alertDialog;
    // String Builder
    StringBuilder sb_FilePath  = new StringBuilder();
    StringBuilder sb_VideoPath = new StringBuilder();
    StringBuilder sb_AudioPath = new StringBuilder();
    StringBuilder sb_CameraPath = new StringBuilder();
    StringBuilder sb_FilePathLocal  = new StringBuilder();
    StringBuilder sb_VideoPathLocal = new StringBuilder();
    StringBuilder sb_AudioPathLocal = new StringBuilder();
    StringBuilder sb_CameraPathLocal = new StringBuilder();
    long totalSize = 0;



//------------------------------------------------------- on Create ----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_details);
        // Tool bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Form");
        // Activity
        mActivity = this;
        // base Application
        baseApplication = (BaseApplication) getApplication();
        // Location
        assistant = new LocationAssistant(FormDetailsActivity.this, FormDetailsActivity.this, LocationAssistant.Accuracy.HIGH, 0, true);
        assistant.setVerbose(true);
        // Utility
        utility = new Utility(this);
        // Image File
        imageFileUtils = new ImageFileUtils();
        // Permission
        systemPermission = new SystemPermission(this);
        // init
        init();
        // Database
        initDatabase();
        // extra
        initExtra();
        // bind Data
        bindData();

    }
//------------------------------------------------------- init ----------------------------------------------------------------------------------------------------------------------

    private void init() {
        imageData        = new HashMap<>();
        imagePathAll     = new ArrayList<>();
        llViewContainer  = findViewById(R.id.llViewContainer);
        rcvFormUI        = findViewById(R.id.rcvFormUI);
        btnSubmit        = findViewById(R.id.btnSubmit);
        btnExit          = findViewById(R.id.btnExit);
        formLayout       = findViewById(R.id.formLayout);
        imgFormLogo      = findViewById(R.id.formLogo);
        llFormLogoLayout = findViewById(R.id.formLogoLayout);
        llPreview        = findViewById(R.id.llPreview);
        llMain           = findViewById(R.id.llMain);
        txtGeoTag        = findViewById(R.id.txtGeoTag);
        imgPreview       = findViewById(R.id.imgPreview);

        updatePreviewUI(false);
        llFormLogoLayout.setVisibility(View.GONE);

    }

//------------------------------------------------------- initDatabase ----------------------------------------------------------------------------------------------------------------------

    private void initDatabase() {
        dataBaseHelper = new DataBaseHelper(FormDetailsActivity.this);
    }

//------------------------------------------------------- initExtra ----------------------------------------------------------------------------------------------------------------------

    private void initExtra() {
        Intent intent = getIntent();
        layer_id     = intent.getStringExtra(Utility.PASS_LAYER_ID);
        project_id   = intent.getStringExtra(Utility.PASS_PROJECT_ID);
        form_id      = intent.getStringExtra(Utility.PASS_FORM_ID);
        work_id      = intent.getStringExtra(Utility.PASS_WORK_ID);
        icon         = intent.getStringExtra(Utility.PASS_ICON);
        icon_width   = intent.getStringExtra(Utility.PASS_ICON_WIDTH);
        icon_heigth  = intent.getStringExtra(Utility.PASS_ICON_HEIGHT);
        layer_type   = intent.getStringExtra(Utility.PASS_LAYER_TYPE);
        line_color   = intent.getStringExtra(Utility.PASS_LINE_COLOR);
        line_type    = intent.getStringExtra(Utility.PASS_LINE_TYPE);
      //  Log.e(TAG, "Line Type: "+line_type);
        formbg_color = intent.getStringExtra(Utility.PASS_FORM_BG_COLOR);
        form_logo    = intent.getStringExtra(Utility.PASS_FORM_LOGO);
        form_sno     = intent.getStringExtra(Utility.PASS_FORM_SNO);

        if (intent.getExtras().containsKey(Utility.PASS_GEOM_ARRAY)) {
            received_geomArray = intent.getExtras().getString(Utility.PASS_GEOM_ARRAY);
            //Log.e(TAG,"Geom Array "+received_geomArray);
        }
        if (intent.getExtras().containsKey(Utility.PASS_GEOM_TYPE)) {
            received_geomType = intent.getExtras().getString(Utility.PASS_GEOM_TYPE);
            if(Utility.isEmptyString(received_geomType)){
                received_geomType = "Line";
            }
            //Log.e(TAG,"Geom Type "+received_geomType);
        }

        // From background Color
        if(!Utility.isEmptyString(formbg_color)){
            int color = Color.parseColor(formbg_color);
            formLayout.setBackgroundColor(color);
        }
        else{
            formLayout.setBackgroundColor(Color.WHITE);
        }
        // Form Logo
        if(!Utility.isEmptyString(form_logo)){
            llFormLogoLayout.setVisibility(View.VISIBLE);
            Glide.with(this).load(Utility.decodeBase64Image(form_logo)).error(R.drawable.ic_login_user_icon).into(imgFormLogo);
        }
        else{
            llFormLogoLayout.setVisibility(View.GONE);
        }
        // Form Sno
        if(!Utility.isEmptyString(form_sno)){
            Utility.saveData(this,Utility.FORM_SNO, form_sno.equalsIgnoreCase("t"));
        }
        else{
            Utility.saveData(this,Utility.FORM_SNO, false);
        }

        Utility.saveData(mActivity,Utility.BarCodeResult,"");

    }

//------------------------------------------------------- Bind data ----------------------------------------------------------------------------------------------------------------------

    private void bindData() {

        String formDetails = "";
        dataBaseHelper.open();
        Cursor curDetails = dataBaseHelper.executeCursor(dataBaseHelper.getProjectLayersListQry(layer_id,work_id));
        if (curDetails != null && curDetails.getCount() > 0) {
            curDetails.moveToFirst();
            formDetails = curDetails.getString(curDetails.getColumnIndex("form_data"));
            Log.e(TAG, "formDetails - " + formDetails);
        }
        getOpenForm(formDetails);
        dataBaseHelper.close();

    }

//------------------------------------------------------- Get Open Form ----------------------------------------------------------------------------------------------------------------------

    private void getOpenForm(String dataResponce) {
        imageData = new HashMap<>();
        imagePathAll = new ArrayList<>();
        if (Utility.isEmptyString(dataResponce)) {
            Utility.showInfoDialog(FormDetailsActivity.this, getResources().getString(R.string.lbl_No_Data_Found));
            return;
        }
        try {
            JSONArray jsonResponse = new JSONArray(dataResponce);
            try {
                Gson gson = new Gson();
                java.lang.reflect.Type listType = new TypeToken<ArrayList<FormDetailData>>() {}.getType();
                listFormDetailsData = gson.fromJson(jsonResponse.toString(), listType);

                formRenderAdapter = new FormRenderAdapter(mActivity, listFormDetailsData, (path, position) -> {
                    if (path != null){
                        destFileTemp = new File(path);
                        Log.e(TAG, destFileTemp.getName());
                    }
                    positionCaptureImagePojo = position;
                });
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                rcvFormUI.setLayoutManager(linearLayoutManager);
                rcvFormUI.setAdapter(formRenderAdapter);

            } catch (Exception e) {
                Utility.showInfoDialog(FormDetailsActivity.this, getResources().getString(R.string.lbl_Something_wrong_please_try_again));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//------------------------------------------------------- Update Preview UI ----------------------------------------------------------------------------------------------------------------------

    private void updatePreviewUI(boolean isUpdate) {
        llMain.setVisibility(isUpdate ? View.GONE : View.VISIBLE);
        llPreview.setVisibility(isUpdate ? View.VISIBLE : View.GONE);
    }

//------------------------------------------------------- on Activity Result ----------------------------------------------------------------------------------------------------------------------

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
        }
        // Bar Code Request
        else if(requestCode == Utility.PICK_BAR_CODE_RESULT_CODE){
            if(resultCode == Activity.RESULT_OK){
                try{
                     if(!Utility.isEmptyString(Utility.getSavedData(mActivity, Utility.BarCodeResult))){
                        Log.e(TAG,"Bar Code Result Form: " +Utility.getSavedData(mActivity, Utility.BarCodeResult));
                        listFormDetailsData.get(positionCaptureImagePojo).setFill_value(Utility.getSavedData(mActivity, Utility.BarCodeResult));
                        formRenderAdapter.notifyDataSetChanged();
                     }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        // Camera Photo Request
        else if (requestCode == Utility.REQUEST_TAKE_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    // Process captured file path to sampling and rotate fix
                    // Save to another filePath
                    // Display it as bitmap from this new Path
                    // Take screenshot in a new path
                    // Upload this new path pic
                    txtGeoTag.setText(getGeoTagData());
                    Bitmap bitmapPreview = ImageFileUtils.handleSamplingAndRotationBitmap(mActivity, Uri.fromFile(destFileTemp));
                    File destFileTemp2 = imageFileUtils.getDestinationFileImageInput(imageFileUtils.getRootDirFile(mActivity) ); //, listFormDetailsData.get(positionCaptureImagePojo).getFill_value());
                    ImageFileUtils.saveBitmapToFile(bitmapPreview, destFileTemp2);
                    imgPreview.setImageBitmap(bitmapPreview);
                    updatePreviewUI(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            File destFile = imageFileUtils.getDestinationFileImageInput(imageFileUtils.getRootDirFile(mActivity)); //, listFormDetailsData.get(positionCaptureImagePojo).getFill_value());
                            if (ImageFileUtils.takeScreenshot(llPreview, destFile)) {
                                Log.e("Picture", "screenshot capture success");
                            } else {
                                destFile = destFileTemp2;
                                Log.e("Picture", "screenshot capture failed");
                            }
                            sb_CameraPathLocal = new StringBuilder();
                            sb_CameraPathLocal.append("local").append("#").append(destFile.getAbsolutePath());
                            listFormDetailsData.get(positionCaptureImagePojo).setFill_value(sb_CameraPathLocal.toString());
                            sb_CameraPath.append(destFile.getPath());
                            Log.e(TAG,destFile.getName());
                            formRenderAdapter.notifyDataSetChanged();
                            updatePreviewUI(false);
                        }
                    }, 400);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // Gallery Photo
        else if (requestCode == Utility.PICK_IMAGE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    assert data != null;
                    Uri uri = data.getData();
                    File sourceFile = new File(imageFileUtils.getPathUri(mActivity, uri));
                    destFileTemp = imageFileUtils.getDestinationFile(imageFileUtils.getRootDirFile(mActivity));
                    imageFileUtils.copyFile(sourceFile, destFileTemp);
                    // Process captured file path to sampling and rotate fix
                    // Save to another filePath
                    // Display it as bitmap from this new Path
                    // Take screenshot in a new path
                    // Upload this new path pic
                    txtGeoTag.setText(getGeoTagData());
                    Bitmap bitmapPreview = ImageFileUtils.handleSamplingAndRotationBitmap(mActivity, Uri.fromFile(destFileTemp));
                    File destFileTemp2 = imageFileUtils.getDestinationFileImageInput(imageFileUtils.getRootDirFile(mActivity));//, listFormDetailsData.get(positionCaptureImagePojo).getInput_id());
                    ImageFileUtils.saveBitmapToFile(bitmapPreview, destFileTemp2);
                    imgPreview.setImageBitmap(bitmapPreview);
                    updatePreviewUI(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            File destFile = imageFileUtils.getDestinationFileImageInput(imageFileUtils.getRootDirFile(mActivity)); // ,, listFormDetailsData.get(positionCaptureImagePojo).getInput_id());
                            if (ImageFileUtils.takeScreenshot(llPreview, destFile)) {
                                Log.e("Picture", "screenshot capture success");
                            } else {
                                destFile = destFileTemp2;
                                Log.e("Picture", "screenshot capture failed");
                            }
                            listFormDetailsData.get(positionCaptureImagePojo).setFill_value(destFile.getAbsolutePath());
                            formRenderAdapter.notifyDataSetChanged();
                            updatePreviewUI(false);
                        }
                    }, 400);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // Crop Image
        else if (requestCode == CroperinoConfig.REQUEST_CROP_PHOTO) {
            try {
                File destFile = imageFileUtils.getDestinationFileImageInput(imageFileUtils.getRootDirFile(mActivity));//, listFormDetailsData.get(positionCaptureImagePojo).getInput_id());
                imageFileUtils.copyFile(destFileTemp, destFile);
                listFormDetailsData.get(positionCaptureImagePojo).setFill_value(destFile.getAbsolutePath());
                formRenderAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Video Capture
//        else if (requestCode == Utility.REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {
//
//            File destFile = new File(destFileTemp.getAbsolutePath());
//            listFormDetailsData.get(positionCaptureImagePojo).setFill_value(destFile.getAbsolutePath());
//            formRenderAdapter.notifyDataSetChanged();
//        }
        else if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle b = data.getExtras();
                if (b != null) {
                    String mLatitude = b.getString("Latitude");
                    String mLongitude = b.getString("Longitude");
                    // mSelectedLatLng = new LatLng(Double.parseDouble(mLatitude), Double.parseDouble(mLongitude));
                }
            } else if (resultCode == 0) {
                System.out.println("RESULT CANCELLED");
            }
        }
        // File
        else if (requestCode == Utility.PICK_FILE_RESULT_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                Uri uri = data.getData();
                // Multiple Files Selected
                if(null != data.getClipData()){
                    ArrayList<Uri> multipleFileList = new ArrayList<>();
                    int n = data.getClipData().getItemCount(); // size
                    for(int i=0; i<n; i++){
                        Uri multipleUri = data.getClipData().getItemAt(i).getUri();
                        multipleFileList.add(multipleUri);
                    }
                    sb_FilePath = new StringBuilder();
                    sb_FilePathLocal = new StringBuilder();
                    for(int i=0; i<multipleFileList.size(); i++){

                        File sourceFile = new File(imageFileUtils.getPathUri(mActivity, multipleFileList.get(i)));
                        File destFile = imageFileUtils.getDestinationFileDoc(imageFileUtils.getRootDirFileDoc(mActivity), ImageFileUtils.getExtFromUri(FormDetailsActivity.this, multipleFileList.get(i)));
                        try{
                            imageFileUtils.copyFile(sourceFile, destFile);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        if(destFile != null){
                            sb_FilePath.append(destFile.getPath());
                            sb_FilePathLocal.append("local").append("%").append(destFile.getName()).append("#").append(destFile.getPath());
                            if(i < multipleFileList.size() - 1){
                                sb_FilePath.append(",");
                                sb_FilePathLocal.append(",");
                            }
                        }
                    }
                    listFormDetailsData.get(positionCaptureImagePojo).setFill_value(sb_FilePathLocal.toString());
                    Log.e(TAG,listFormDetailsData.get(positionCaptureImagePojo).getFill_value());
                    formRenderAdapter.notifyDataSetChanged();
                }
                // Single File Selected
                else{
                    File sourceFile = new File(imageFileUtils.getPathUri(mActivity, uri));
                    File destFile = imageFileUtils.getDestinationFileDoc(imageFileUtils.getRootDirFileDoc(mActivity), ImageFileUtils.getExtFromUri(FormDetailsActivity.this, uri));
                    try{
                        imageFileUtils.copyFile(sourceFile, destFile);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    if(destFile != null){
                        sb_FilePath = new StringBuilder();
                        sb_FilePathLocal = new StringBuilder();
                        sb_FilePath.append(destFile.getPath());
                        sb_FilePathLocal.append("local").append("%").append(destFile.getName()).append("#").append(destFile.getPath());
                        listFormDetailsData.get(positionCaptureImagePojo).setFill_value(sb_FilePathLocal.toString());
                        Log.e(TAG,listFormDetailsData.get(positionCaptureImagePojo).getFill_value());
                        formRenderAdapter.notifyDataSetChanged();
                    }
                }
//                new AsyncTask<Uri, Void, File>() {
//                    @Override
//                    protected void onPreExecute() {
//                        super.onPreExecute();
//                        startMyDialog("Preparing your data..Please wait..");
//                    }
//
//                    @Override
//                    protected File doInBackground(Uri... voids) {
//                        File sourceFile = new File(imageFileUtils.getPathUri(mActivity, voids[0]));
//                        File destFile = imageFileUtils.getDestinationFileDoc(imageFileUtils.getRootDirFileDoc(mActivity), ImageFileUtils.getExtFromUri(FormDetailsActivity.this, voids[0]));
//                        try {
//                            imageFileUtils.copyFile(sourceFile, destFile);
//                        }
//                        catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        return destFile;
//                    }
//
//                    @Override
//                    protected void onPostExecute(File aVoid) {
//                        super.onPostExecute(aVoid);
//                        listFormDetailsData.get(positionCaptureImagePojo).setFill_value(aVoid.getAbsolutePath());
//                        Log.e(TAG,listFormDetailsData.get(positionCaptureImagePojo).getFill_value());
//                        formRenderAdapter.notifyDataSetChanged();
//                        dismissMyDialog();
//                    }
//                }.execute(uri);
            }
        }
        // Video Capture
        else if (requestCode == Utility.PICK_VIDEO_FILE_RESULT_CODE ) {
            if(resultCode == Activity.RESULT_OK){
                assert data != null;
                Uri uri = data.getData();
                // Multiple Files Selected
                if(null != data.getClipData()){
                    ArrayList<Uri> multipleFileList = new ArrayList<>();
                    int n = data.getClipData().getItemCount(); // size
                    for(int i=0; i<n; i++){
                        Uri multipleUri = data.getClipData().getItemAt(i).getUri();
                        multipleFileList.add(multipleUri);
                    }
                    sb_VideoPath = new StringBuilder();
                    sb_VideoPathLocal = new StringBuilder();
                    for(int i=0; i<multipleFileList.size(); i++){
                        File sourceFile = new File(imageFileUtils.getPathUri(mActivity, multipleFileList.get(i)));
                        File destFile = imageFileUtils.getDestinationFileDoc(imageFileUtils.getRootDirFileDoc(mActivity), ImageFileUtils.getExtFromUri(FormDetailsActivity.this, multipleFileList.get(i)));
                        try{
                            imageFileUtils.copyFile(sourceFile, destFile);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        if(destFile != null){
                            sb_VideoPath.append(destFile.getPath());
                            sb_VideoPathLocal.append("local").append("%").append(destFile.getName()).append("#").append(destFile.getPath());
                            if(i < multipleFileList.size() - 1){
                                sb_VideoPath.append(",");
                                sb_VideoPathLocal.append(",");
                            }
                        }
                    }
                    listFormDetailsData.get(positionCaptureImagePojo).setFill_value(sb_VideoPathLocal.toString());
                    // listFormDetailsData.get(positionCaptureImagePojo).setMultiple("true");
                    //Log.e(TAG,listFormDetailsData.get(positionCaptureImagePojo).getFill_value());
                    formRenderAdapter.notifyDataSetChanged();
                }
                // Single File Selected
                else{
                    File sourceFile = new File(imageFileUtils.getPathUri(mActivity, uri));
                    File destFile = imageFileUtils.getDestinationFileDoc(imageFileUtils.getRootDirFileDoc(mActivity), ImageFileUtils.getExtFromUri(FormDetailsActivity.this, uri));
                    try{
                        imageFileUtils.copyFile(sourceFile, destFile);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    if(destFile != null){
                        sb_VideoPath = new StringBuilder();
                        sb_VideoPathLocal = new StringBuilder();
                        sb_VideoPath.append(destFile.getPath());
                        sb_VideoPathLocal.append("local").append("%").append(destFile.getName()).append("#").append(destFile.getPath());
                        listFormDetailsData.get(positionCaptureImagePojo).setFill_value(sb_VideoPathLocal.toString());
                        Log.e(TAG,listFormDetailsData.get(positionCaptureImagePojo).getFill_value());
                        formRenderAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
        // Audio File
        else if (requestCode == Utility.PICK_AUDIO_FILE_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                Uri uri = data.getData();
                // Multiple Files Selected
                if(null != data.getClipData()){
                    ArrayList<Uri> multipleFileList = new ArrayList<>();
                    int n = data.getClipData().getItemCount(); // size
                    for(int i=0; i<n; i++){
                        Uri multipleUri = data.getClipData().getItemAt(i).getUri();
                        multipleFileList.add(multipleUri);
                    }
                    sb_AudioPath = new StringBuilder();
                    sb_AudioPathLocal = new StringBuilder();
                    for(int i=0; i<multipleFileList.size(); i++){
                        File sourceFile = new File(imageFileUtils.getPathUri(mActivity, multipleFileList.get(i)));
                        File destFile = imageFileUtils.getDestinationFileDoc(imageFileUtils.getRootDirFileDoc(mActivity), ImageFileUtils.getExtFromUri(FormDetailsActivity.this, multipleFileList.get(i)));
                        try{
                            imageFileUtils.copyFile(sourceFile, destFile);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        if(destFile != null){
                            sb_AudioPath.append(destFile.getPath());
                            sb_AudioPathLocal.append("local").append("%").append(destFile.getName()).append("#").append(destFile.getPath());
                            if(i < multipleFileList.size() - 1){
                                sb_AudioPath.append(",");
                                sb_AudioPathLocal.append(",");
                            }
                        }
                    }
                    listFormDetailsData.get(positionCaptureImagePojo).setFill_value(sb_AudioPathLocal.toString());
                    // listFormDetailsData.get(positionCaptureImagePojo).setMultiple("true");
                    //Log.e(TAG,listFormDetailsData.get(positionCaptureImagePojo).getFill_value());
                    formRenderAdapter.notifyDataSetChanged();
                }
                // Single File Selected
                else{
                    File sourceFile = new File(imageFileUtils.getPathUri(mActivity, uri));
                    File destFile = imageFileUtils.getDestinationFileDoc(imageFileUtils.getRootDirFileDoc(mActivity), ImageFileUtils.getExtFromUri(FormDetailsActivity.this, uri));
                    try{
                        imageFileUtils.copyFile(sourceFile, destFile);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    if(destFile != null){
                        sb_AudioPath = new StringBuilder();
                        sb_AudioPathLocal = new StringBuilder();
                        sb_AudioPath.append(destFile.getPath());
                        sb_AudioPathLocal.append("local").append("%").append(destFile.getName()).append("#").append(destFile.getPath());
                        listFormDetailsData.get(positionCaptureImagePojo).setFill_value(sb_AudioPathLocal.toString());
                        //Log.e(TAG,destFile.getName());
                        Log.e(TAG,listFormDetailsData.get(positionCaptureImagePojo).getFill_value());
                        formRenderAdapter.notifyDataSetChanged();
                    }
                }

//                new AsyncTask<Uri, Void, File>() {
//                    @Override
//                    protected void onPreExecute() {
//                        super.onPreExecute();
//                        startMyDialog("Preparing your data..Please wait..");
//                    }
//
//                    @Override
//                    protected File doInBackground(Uri... voids) {
//                        File sourceFile = new File(imageFileUtils.getPathUri(mActivity, voids[0]));
//                        File destFile = imageFileUtils.getDestinationFileDoc(imageFileUtils.getRootDirAudioFileDoc(mActivity), ImageFileUtils.getExtFromUri(FormDetailsActivity.this, voids[0]));
//                        try {
//                            imageFileUtils.copyFile(sourceFile, destFile);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        return destFile;
//                    }
//
//                    @Override
//                    protected void onPostExecute(File aVoid) {
//                        super.onPostExecute(aVoid);
//                        listFormDetailsData.get(positionCaptureImagePojo).setFill_value(aVoid.getAbsolutePath());
//                        formRenderAdapter.notifyDataSetChanged();
//                        dismissMyDialog();
//                    }
//                }.execute(uri);
            }

        }
    }

//------------------------------------------------------- on Request Permission ----------------------------------------------------------------------------------------------------------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (assistant.onPermissionsUpdated(requestCode, grantResults)) {
        }
    }

//------------------------------------------------------- MENU ----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();
        if (itemID == android.R.id.home) {
            Utility.saveData(mActivity, Utility.BarCodeResult, "");
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        return false;
    }

//------------------------------------------------------- progressBar ----------------------------------------------------------------------------------------------------------------------

    private void startMyDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(FormDetailsActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(message);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }
    }

    private void dismissMyDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

//------------------------------------------------------- Submit Form ----------------------------------------------------------------------------------------------------------------------

    public void onSubmitForm(View v) {
//        ---------- START If we have geom_array  available ----------
        if (!Utility.isEmptyString(getReceived_geomArray()) && !Utility.isEmptyString(getReceived_geomType())) {

            unique_number = String.valueOf(Utility.getToken());
            datetime = Utility.getDateTime();
            OtherFormData otherFormData = new OtherFormData();
            otherFormData.setUserID(Utility.getSavedData(this,Utility.LOGGED_USERID));
            otherFormData.setProjectID(project_id);
            otherFormData.setWorkID(work_id);
            otherFormData.setLayerID(layer_id);
            otherFormData.setFormID(form_id);
            otherFormData.setDatetime(datetime);
            otherFormData.setVersion(URL_Utility.APP_VERSION);
            otherFormData.setUnique_number(unique_number);
            otherFormData.setGeom_array((getReceived_geomArray()));
            otherFormData.setGeom_type(getReceived_geomType());

            String latitude = "", longitude = "";
            if (mCurrentLocation != null) {
                latitude = String.valueOf(mCurrentLocation.getLatitude());
                longitude = String.valueOf(mCurrentLocation.getLongitude());
                otherFormData.setLatitude(latitude);
                otherFormData.setLongitude(longitude);
            }
            FormDataModel formDataModel = new FormDataModel();
            formDataModel.setFormID(form_id);
            formDataModel.setWorkID(work_id);
            formDataModel.setLayerID(layer_id);
            formDataModel.setProjectID(project_id);
            formDataModel.setIcon_width(icon_width);
            formDataModel.setIcon_height(icon_heigth);
            formDataModel.setLine_color(line_color);
            formDataModel.setLine_type(line_type);
            formDataModel.setType(layer_type);
            formDataModel.setFormDetailData(listFormDetailsData);
            formDataModel.setOtherFormData(otherFormData);

            FormDataModel formDataModel1 = new FormDataModel();
            formDataModel1.setFormID(form_id);
            formDataModel1.setWorkID(work_id);
            formDataModel1.setLayerID(layer_id);
            formDataModel1.setProjectID(project_id);
            formDataModel1.setIcon_width(icon_width);
            formDataModel1.setIcon_height(icon_heigth);
            formDataModel1.setLine_color(line_color);
            formDataModel1.setLine_type(line_type);
            formDataModel1.setType(layer_type);
            formDataModel1.setForm_sno(form_sno);
            formDataModel1.setForm_logo(form_logo);
            formDataModel1.setFormbg_color(formbg_color);
            formDataModel1.setFormDetailData(listFormDetailsData);
            formDataModel1.setOtherFormData(otherFormData);

           // Log.e(TAG,new Gson().toJson(formDataModel));

            // here we check for internet connection
            if(SystemUtility.isInternetConnected(mActivity)){
                SaveFormToServe(formDataModel);
            }
            // Here Save to Local Database
            else{
                String col_nameFile  = "";
                String col_nameCamera = "";
                String col_nameVideo  = "";
                String col_nameAudio  = "";

                if(listFormDetailsData != null && listFormDetailsData.size() > 0){
                    for(int i=0; i<listFormDetailsData.size(); i++){
                        if(listFormDetailsData.get(i).getType().equals(TYPE_FILE)){
                            col_nameFile = listFormDetailsData.get(i).getColumn_name();
                        }
                        if(listFormDetailsData.get(i).getType().equals(TYPE_CAMERA)){
                            col_nameCamera = listFormDetailsData.get(i).getColumn_name();
                        }
                        if(listFormDetailsData.get(i).getType().equals(TYPE_VIDEO)){
                            col_nameVideo = listFormDetailsData.get(i).getColumn_name();
                        }
                        if(listFormDetailsData.get(i).getType().equals(TYPE_AUDIO)){
                            col_nameAudio = listFormDetailsData.get(i).getColumn_name();
                        }
                    }
                }

                dataBaseHelper.insertProjectSurveyForm(layer_id,work_id,form_id,unique_number,sb_FilePath.toString(),sb_CameraPath.toString(),sb_VideoPath.toString(),sb_AudioPath.toString(),col_nameFile,col_nameCamera,col_nameAudio,col_nameVideo,new Gson().toJson(formDataModel1), icon);
                dataBaseHelper.insertProjectForm(layer_id,work_id,form_id,unique_number,sb_FilePath.toString(),sb_CameraPath.toString(),sb_VideoPath.toString(),sb_AudioPath.toString(),col_nameFile,col_nameCamera,col_nameAudio,col_nameVideo,new Gson().toJson(formDataModel), icon);
                Log.e(TAG,"Form Save to Local DataBase");
                Utility.getOKDialogBox(mActivity, "Save Successfully", dialog -> {
                    dialog.dismiss();
                    Utility.saveData(mActivity,Utility.BarCodeResult,"");
                    setResult(RESULT_OK);
                    finish();
                });
            }
        }
        else{
            Log.e(TAG, "Empty Data");
        }

    }

    private void SaveFormToServe(FormDataModel formDataModel){
        startMyDialog("Loading...");
        Map<String, String> params = new HashMap<>();
        String data = "";
        Log.e(TAG, new Gson().toJson(formDataModel));
        try {
            data =  AESCrypt.encrypt(new Gson().toJson(formDataModel));
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.put("data",data);
        URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_LAYER_FORM_UPLOAD;
        BaseApplication.getInstance().makeHttpPostRequest(this, responseCode, URL_Utility.WS_LAYER_FORM_UPLOAD, params, false, false);
    }

//------------------------------------------------------- Exit Form ----------------------------------------------------------------------------------------------------------------------

    public void onExitForm(View view) {
        Utility.saveData(mActivity,Utility.BarCodeResult,"");
        setResult(RESULT_CANCELED);
        finish();
    }
//------------------------------------------------------- Camera Image ----------------------------------------------------------------------------------------------------------------------
//
//    private void captureImage() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File file = null;
//        if (file != null) {
//            imageStoragePath = file.getAbsolutePath();
//        }
//        Uri fileUri = Uri.fromFile(file);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
//    }
//
////------------------------------------------------------- Gallery Image ----------------------------------------------------------------------------------------------------------------------
//
//    private void captureGallery() {
//        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(i, GALLERY_CAPTURE_IMAGE_REQUEST_CODE);
//    }
    private StringBuilder getGeoTagData() {
        StringBuilder stringBuilder = new StringBuilder();
        if (mCurrentLocation != null) {
            stringBuilder.append("Latitude : " + "").append(mCurrentLocation.getLatitude());
            stringBuilder.append("\n");
            stringBuilder.append("Longitude : " + "").append(mCurrentLocation.getLongitude());
            stringBuilder.append("\n");
        }
        stringBuilder.append("Date: ").append(dataBaseHelper.getRecordDate());
        return stringBuilder;
    }

//------------------------------------------------------- Location ----------------------------------------------------------------------------------------------------------------------

    @Override
    public void onNeedLocationPermission() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FormDetailsActivity.this);
        dialogBuilder.setTitle(R.string.app_name);
        dialogBuilder.setMessage("Need\nPermission");
        dialogBuilder.setPositiveButton("Ok", (dialog, which) -> {
            dialog.dismiss();
            assistant.requestLocationPermission();
        });
        dialogBuilder.setCancelable(false);
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
        }).setCancelable(false).show();
    }

    @Override
    public void onFallBackToSystemSettings(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.switchOnLocationLong)
                .setPositiveButton(R.string.lbl_Ok, fromDialog)
                .setCancelable(false)
                .show();
    }

    @Override
    public void onMockLocationsDetected(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {
    }

    @Override
    public void onError(LocationAssistant.ErrorType type, String message) {

    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onNewLocationAvailable(Location location) {
        if (location == null) return;
        mCurrentLocation = location;
        ((TextView) findViewById(R.id.txtAccuracy)).setText("LatLong: " + mCurrentLocation.getLatitude() + ", " + mCurrentLocation.getLongitude() + "\nAccuracy: " + mCurrentLocation.getAccuracy() + " mtr");
            if (!isServiceStarted) {
            isServiceStarted = true;
            baseApplication.startMyService();
        }
    }


//------------------------------------------------------- Revoke Location ----------------------------------------------------------------------------------------------------------------------
     public void revokeLocation() {
        Log.e(TAG, "Received revokeLocation");
        if (alertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage(R.string.switchOnLocationShort).setPositiveButton(R.string.lbl_Ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismissRevokeDialog();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            }).setCancelable(false);
            alertDialog = builder.create();
            alertDialog.show();
        }
    }

     void dismissRevokeDialog() {
        if (alertDialog != null && alertDialog.isShowing() && !isFinishing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

//------------------------------------------------------- on Resume ----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onResume() {
        super.onResume();
        SystemUtility.setFullscreenToggle(this, Utility.getBooleanSavedData(this, Utility.IS_FULL_SCREEN));
        SystemUtility.setKeepScreenAwakeAlwaysToggle(this, Utility.getBooleanSavedData(this, Utility.IS_ALWAYS_KEEP_SCREEN_AWAKE));
        assistant.start();
    }

//------------------------------------------------------- on Pause ----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onPause() {
        assistant.stop();
        dismissRevokeDialog();
        super.onPause();
    }

//------------------------------------------------------- On Destroy ----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public String getForm_name() {
        return form_name;
    }
    public Utility getUtility() {
        return utility;
    }
    public void setUtility(Utility utility) {
        this.utility = utility;
    }
    public ImageFileUtils getImageFileUtils() {
        return imageFileUtils;
    }

    public SystemPermission getSystemPermission() {
        return systemPermission;
    }

    public void setSystemPermission(SystemPermission systemPermission) {
        this.systemPermission = systemPermission;
    }

    public String getReceived_geomArray() {
        return received_geomArray;
    }

    public String getReceived_geomType() {
        return received_geomType;
    }


//------------------------------------------------------- On Success Response ----------------------------------------------------------------------------------------------------------------------

    @Override
    public void onSuccessResponse(URL_Utility.ResponseCode responseCode, String response) {
        //Log.e("http", "responseCode: " + responseCode);
        //Log.e("http", "response: " + response);

        if (responseCode == URL_Utility.ResponseCode.WS_LAYER_FORM_UPLOAD) {
            String res = AESCrypt.decryptResponse(response);
            if(!res.equals("")){
                try {
                    JSONObject mObj = new JSONObject(res);
                    String status = mObj.optString("status");
                    Log.e(TAG, "Form Status : " + status);
                    // Status Success
                    if (status.equalsIgnoreCase("Success")){

                        boolean isFile   = false;
                        boolean isCamera = false;
                        boolean isVideo  = false;
                        boolean isAudio  = false;
                        HashMap<String,String> fileData       = new HashMap<>();
                        HashMap<String,String> cameraFileData = new HashMap<>();
                        HashMap<String,String> videoFileData  = new HashMap<>();
                        HashMap<String,String> audioFileData  = new HashMap<>();

                        if(listFormDetailsData != null && listFormDetailsData.size() > 0){

                            for(int i = 0; i < listFormDetailsData.size(); i++){
                                // File Type Data
                                if (listFormDetailsData.get(i).getType().equals(TYPE_FILE)){
                                    isFile = true;
                                    Log.e(TAG, "File Contain in Form");
                                    String fileColumnName = listFormDetailsData.get(i).getColumn_name();
                                    if(!Utility.isEmptyString(listFormDetailsData.get(i).getFill_value())) {
                                        Log.e(TAG,"File Selected");
                                        fileData.put(fileColumnName,getUploadFilePath(listFormDetailsData,i));
                                    }
                                    else{
                                        Log.e(TAG,"File Not Selected");
                                    }
                                }
                                // Camera Type Data
                                if (listFormDetailsData.get(i).getType().equals(TYPE_CAMERA)) {
                                    isCamera = true;
                                    Log.e(TAG, "Camera Contain in Form");
                                    String cameraColumnName = listFormDetailsData.get(i).getColumn_name();
                                    if(!Utility.isEmptyString(listFormDetailsData.get(i).getFill_value())) {
                                        Log.e(TAG, "Camera  File Selected");
                                        cameraFileData.put(cameraColumnName,getUploadFilePath(listFormDetailsData,i));
                                    }
                                    else{
                                        Log.e(TAG, "Camera  File Not Selected");
                                    }
                                }
                                // Video Type Data
                                if (listFormDetailsData.get(i).getType().equals(TYPE_VIDEO)) {
                                    isVideo = true;
                                    Log.e(TAG, "Video Contain in Form");
                                    String videoColumnName = listFormDetailsData.get(i).getColumn_name();
                                    if(!Utility.isEmptyString(listFormDetailsData.get(i).getFill_value())) {
                                        Log.e(TAG, "Video File Selected");
                                        videoFileData.put(videoColumnName,getUploadFilePath(listFormDetailsData,i));
                                    }
                                    else{
                                        Log.e(TAG, "Video File Not Selected");
                                    }
                                }
                                // Audio Type Data
                                if (listFormDetailsData.get(i).getType().equals(TYPE_AUDIO)) {
                                    isAudio = true;
                                    Log.e(TAG, "Audio Contain in Form");
                                    String audioColumnName = listFormDetailsData.get(i).getColumn_name();
                                    if(!Utility.isEmptyString(listFormDetailsData.get(i).getFill_value())) {
                                        Log.e(TAG,"Audio File Selected");
                                        audioFileData.put(audioColumnName,getUploadFilePath(listFormDetailsData,i));
                                    }
                                    else{
                                        Log.e(TAG,"Audio File Not Selected");
                                    }
                                }
                            }
                            // File Upload
                            if (isFile && !fileData.isEmpty() || isCamera && !cameraFileData.isEmpty() || isVideo && !videoFileData.isEmpty() || isAudio && !audioFileData.isEmpty()) {

                                    if (isFile && !fileData.isEmpty()) {
                                        Log.e(TAG, "User Selected File");
                                        new FormDetailsActivity.FileUploadServer(fileData,form_id,unique_number,TYPE_FILE).execute();
                                    }
                                    else{
                                        isFileUpload = true;
                                    }

                                    if (isCamera && !cameraFileData.isEmpty()) {
                                        Log.e(TAG, "User Selected Camera");
                                        new FormDetailsActivity.FileUploadServer(cameraFileData,form_id,unique_number,TYPE_CAMERA).execute();
                                    }
                                    else{
                                        isCameraUpload = true;
                                    }
                                    if (isVideo && !videoFileData.isEmpty()) {
                                        Log.e(TAG, "User Selected Video");
                                        new FormDetailsActivity.FileUploadServer(videoFileData,form_id,unique_number,TYPE_VIDEO).execute();
                                    }
                                    else{
                                        isVideoUpload = true;
                                    }
                                    if (isAudio && !audioFileData.isEmpty()) {
                                        Log.e(TAG, "User Selected Audio");
                                        new FormDetailsActivity.FileUploadServer(audioFileData,form_id,unique_number,TYPE_AUDIO).execute();
                                    }
                                    else{
                                        isAudioUpload = true;
                                    }
                            }
                            else {
                                Log.e(TAG,"User Only Submit Form");
                                col_nameFile   = "";
                                col_nameCamera = "";
                                col_nameAudio  = "";
                                col_nameVideo  = "";
                                dismissMyDialog();
                                SaveToSurveyFormTable();
                                Utility.getOKDialogBox(mActivity, "Save Successfully", dialog -> {
                                    dialog.dismiss();
                                    Utility.saveData(mActivity,Utility.BarCodeResult,"");
                                    setResult(RESULT_OK);
                                    finish();
                                });
                            }

                        }
                    }
                    // Status Failure
                    else {
                        Toast.makeText(mActivity, "Internet Issue try to Save offline", Toast.LENGTH_SHORT).show();
                        dismissMyDialog();
                    }
                }
                catch (JSONException e){
                    Log.e(TAG,"Json Error: "+ e.getMessage());
                    Toast.makeText(mActivity, "Internet Issue try to Save offline", Toast.LENGTH_SHORT).show();
                    dismissMyDialog();
                }
            }
            else{
                Log.e(TAG,"Null Data Form Server");
                Toast.makeText(mActivity, "Upload Not Successfully", Toast.LENGTH_SHORT).show();
                dismissMyDialog();
            }
        }
    }

//------------------------------------------------------- On Error Response ----------------------------------------------------------------------------------------------------------------------

    @Override
    public void onErrorResponse(URL_Utility.ResponseCode responseCode, VolleyError error) {

        if (responseCode == URL_Utility.ResponseCode.WS_LAYER_FORM_UPLOAD)
        {
            String res = AESCrypt.decryptResponse(error.getMessage());
            Log.e(TAG,res);
            dismissMyDialog();
            Toast.makeText(mActivity, "Error"+ res, Toast.LENGTH_SHORT).show();
            // here we store Data into internal Data Base
        }

    }


    private class FileUploadServer extends AsyncTask<Void, Integer, String> {
        HashMap<String,String> filePathData;
        String form_id;
        String unique_number;
        String type;

        public FileUploadServer(HashMap<String,String> filePathData, String form_id, String unique_number,String type) {
            this.filePathData = filePathData;
            this.form_id = form_id;
            this.unique_number = unique_number;
            this.type = type;

            if(type.equals(TYPE_FILE)){
                Log.e(TAG, "File Type ");
                isFileUpload = false;
            }
            else if(type.equals(TYPE_CAMERA) ){
                Log.e(TAG, "Camera Type ");
                isCameraUpload = false;
            }
            else if(type.equals(TYPE_VIDEO) ){
                Log.e(TAG, "Video Type ");
                isVideoUpload = false;
            }
            else if(type.equals(TYPE_AUDIO) ){
                Log.e(TAG, "Audio Type ");
                isAudioUpload = false;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }
        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }
        @SuppressWarnings("deprecation")
        private String uploadFile(){
            String responseString = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL_Utility.WS_FORM_FILE_UPLOAD);
            try {

                if(filePathData != null){
                    if(!filePathData.isEmpty()){
                        // outer Loop
                        for(Map.Entry<String,String> entry: filePathData.entrySet()) {
                            String col_name = entry.getKey();
                            // File Path!
                            String[] path = entry.getValue().split(",");
                            Log.e(TAG, "path: "+entry.getValue());
                            for (String filepath : path) {
                                File sourceFile = new File(filepath);
                                String data = "";
                                JSONObject params = new JSONObject();
                                try {
                                    params.put("formID", form_id);
                                    params.put("unique_number", unique_number);
                                    params.put("column_name", col_name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // Encrypt Data!
                                data = AESCrypt.encrypt(params.toString());
                                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(num -> publishProgress((int) ((num / (float) totalSize) * 100)));
                                entity.addPart(URL_Utility.PARAM_IMAGE_DATA, new FileBody(sourceFile));
                                entity.addPart("data", new StringBody(data));
                                totalSize = entity.getContentLength();
                                httppost.setEntity(entity);
                                HttpResponse response = httpclient.execute(httppost);
                                HttpEntity r_entity = response.getEntity();
                                int statusCode = response.getStatusLine().getStatusCode();

                                if (statusCode == 200) {
                                    responseString = EntityUtils.toString(r_entity);
                                    String res = AESCrypt.decryptResponse(responseString);
                                    //Log.e("Form","RES:  "+res);
                                    if (!res.equals("")) {
                                        try {
                                            JSONObject mLoginObj = new JSONObject(res);
                                            String status = mLoginObj.optString("status");
                                            Log.e(TAG, status);
                                            if (status.equalsIgnoreCase("Success")) {

                                            }
                                            else {
                                                Toast.makeText(mActivity, "Internet Issue try to Save offline", Toast.LENGTH_SHORT).show();
                                                dismissMyDialog();
                                            }
                                        } catch (JSONException e) {
                                            Log.e(TAG, e.getMessage());
                                            Toast.makeText(mActivity, "Internet Issue try to Save offline", Toast.LENGTH_SHORT).show();
                                            dismissMyDialog();
                                        }
                                    } else {
                                        Log.e(TAG,"response null");
                                        Toast.makeText(mActivity, "Internet Issue try to Save offline", Toast.LENGTH_SHORT).show();
                                        dismissMyDialog();
                                    }
                                } else {
                                    responseString = "Error occurred! Http Status Code: " + statusCode;
                                    Log.e(TAG, responseString);
                                    Toast.makeText(mActivity, "Internet Issue try to Save offline", Toast.LENGTH_SHORT).show();
                                    dismissMyDialog();
                                }
                            }
                        }
                    }
                    else{
                        Log.e(TAG,"filePathData is Empty");
                    }

                }
                else{
                    Log.e(TAG,"filePathData null");
                    Toast.makeText(mActivity, "Internet Issue try to Save offline", Toast.LENGTH_SHORT).show();
                    dismissMyDialog();
                }

            } catch (IOException e) {
                Toast.makeText(mActivity, "Internet Issue try to Save offline", Toast.LENGTH_SHORT).show();
                Log.e(TAG, e.getMessage());
                dismissMyDialog();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                dismissMyDialog();
                Toast.makeText(mActivity, "Internet Issue try to Save offline", Toast.LENGTH_SHORT).show();
            }
            return responseString;
        }
        @Override
        protected void onPostExecute(String result) {
            String response = AESCrypt.decryptResponse(result);
            if(!response.equals("")){
                try {
                    JSONObject mLoginObj = new JSONObject(response);
                    String status = mLoginObj.optString("status");
                    if (status.equalsIgnoreCase("Success")){

                        switch (type) {
                            case TYPE_FILE:
                                Log.e(TAG, "File Upload Successfully");
                                isFileUpload = true;
                                break;
                            case TYPE_CAMERA:
                                Log.e(TAG, "Camera File Upload Successfully");
                                isCameraUpload = true;
                                break;
                            case TYPE_VIDEO:
                                Log.e(TAG, "Video File Upload Successfully");
                                isVideoUpload = true;
                                break;
                            case TYPE_AUDIO:
                                Log.e(TAG, "Audio File Upload Successfully");
                                isAudioUpload = true;
                                break;
                        }

                        if((isCameraUpload && isVideoUpload && isAudioUpload && isFileUpload )){
                            Log.e(TAG,"Save File Successfully");
                            dismissMyDialog();
                            SaveToSurveyFormTable();
                            Utility.getOKDialogBox(mActivity, "Save Successfully", dialog -> {
                                dialog.dismiss();
                                Utility.saveData(mActivity,Utility.BarCodeResult,"");
                                setResult(RESULT_OK);
                                finish();
                            });
                        }
                    }
                    else{
                        Log.e(TAG,status);
                        Toast.makeText(mActivity, "Internet Issue try to Save offline", Toast.LENGTH_SHORT).show();
                        dismissMyDialog();
                    }

                } catch (JSONException e) {
                    Toast.makeText(mActivity, "Internet Issue try to Save offline", Toast.LENGTH_SHORT).show();
                    Log.e(TAG,e.getMessage());
                    dismissMyDialog();
                }
            }
            else{
                Log.e(TAG, response);
                Toast.makeText(mActivity, "Internet Issue try to Save offline", Toast.LENGTH_SHORT).show();
                dismissMyDialog();
            }
            super.onPostExecute(result);
        }
    }

//------------------------------------------------------- SaveToSurveyFormTable ------------------------------------------------------------

    private void SaveToSurveyFormTable(){
        if (!Utility.isEmptyString(getReceived_geomArray()) && !Utility.isEmptyString(getReceived_geomType())) {

            unique_number = String.valueOf(Utility.getToken());
            datetime = Utility.getDateTime();
            OtherFormData otherFormData = new OtherFormData();
            otherFormData.setUserID(Utility.getSavedData(this, Utility.LOGGED_USERID));
            otherFormData.setProjectID(project_id);
            otherFormData.setWorkID(work_id);
            otherFormData.setLayerID(layer_id);
            otherFormData.setFormID(form_id);
            otherFormData.setDatetime(datetime);
            otherFormData.setVersion(URL_Utility.APP_VERSION);
            otherFormData.setUnique_number(unique_number);
            otherFormData.setGeom_array((getReceived_geomArray()));
            otherFormData.setGeom_type(getReceived_geomType());

            String latitude = "", longitude = "";
            if (mCurrentLocation != null) {
                latitude = String.valueOf(mCurrentLocation.getLatitude());
                longitude = String.valueOf(mCurrentLocation.getLongitude());
                otherFormData.setLatitude(latitude);
                otherFormData.setLongitude(longitude);
            }
            FormDataModel formDataModel = new FormDataModel();
            formDataModel.setFormID(form_id);
            formDataModel.setWorkID(work_id);
            formDataModel.setLayerID(layer_id);
            formDataModel.setProjectID(project_id);
            formDataModel.setIcon_width(icon_width);
            formDataModel.setIcon_height(icon_heigth);
            formDataModel.setLine_color(line_color);
            formDataModel.setLine_type(line_type);
            formDataModel.setType(layer_type);
            formDataModel.setForm_sno(form_sno);
            formDataModel.setForm_logo(form_logo);
            formDataModel.setFormbg_color(formbg_color);
            formDataModel.setFormDetailData(listFormDetailsData);
            formDataModel.setOtherFormData(otherFormData);

            String col_nameFile = "";
            String col_nameCamera = "";
            String col_nameVideo = "";
            String col_nameAudio = "";

            if (listFormDetailsData != null && listFormDetailsData.size() > 0) {
                for (int i = 0; i < listFormDetailsData.size(); i++) {
                    if (listFormDetailsData.get(i).getType().equals(TYPE_FILE)) {
                        col_nameFile = listFormDetailsData.get(i).getColumn_name();
                    }
                    if (listFormDetailsData.get(i).getType().equals(TYPE_CAMERA)) {
                        col_nameCamera = listFormDetailsData.get(i).getColumn_name();
                    }
                    if (listFormDetailsData.get(i).getType().equals(TYPE_VIDEO)) {
                        col_nameVideo = listFormDetailsData.get(i).getColumn_name();
                    }
                    if (listFormDetailsData.get(i).getType().equals(TYPE_AUDIO)) {
                        col_nameAudio = listFormDetailsData.get(i).getColumn_name();
                    }
                }
            }
            dataBaseHelper.insertProjectSurveyForm(layer_id,work_id,form_id,unique_number,sb_FilePath.toString(),sb_CameraPath.toString(),sb_VideoPath.toString(),sb_AudioPath.toString(),col_nameFile,col_nameCamera,col_nameAudio,col_nameVideo,new Gson().toJson(formDataModel), icon);
        }
    }


}
