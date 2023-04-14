package com.surveybaba;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.surveybaba.AESCrypt.AESCrypt;
import com.surveybaba.DashBoard.DashBoardBNActivity;
import com.surveybaba.Database.DataBaseHelper;
import com.surveybaba.InternetConnection.ConnectivityReceiver;
import com.surveybaba.Utilities.SystemPermission;
import com.surveybaba.Utilities.SystemUtility;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.BinLayerProject;
import com.surveybaba.model.PointViewLayerModel.PointViewLayerModel;
import com.surveybaba.model.PolygonViewLayerModel.PolygonViewLayerModel;
import com.surveybaba.model.PolylineViewLayerModel.PolylineViewLayerModel;
import com.volly.BaseApplication;
import com.volly.URL_Utility;
import com.volly.URL_Utility.ResponseCode;
import com.volly.WSResponseInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static com.surveybaba.Utilities.Utility.PROJECT_ID_DEFAULT;
import static com.surveybaba.Utilities.Utility.validateIds;


public class LoginActivity extends AppCompatActivity implements WSResponseInterface {

    // TAG
    public static final String TAG = "LoginActivity";
    // Activity
    Activity mActivity;
    // Database
    private DataBaseHelper dataBaseHelper;
    // Base Application
    BaseApplication baseApplication;
    // Broadcast Receiver
    BroadcastReceiver broadcastReceiver;
    // ProgressBar
    private ProgressDialog progressDialog;
    // Boolean
    boolean isNewPasswordVisible = false;
    boolean isConfirmPasswordVisible = false;
    boolean isPasswordVisible = false;
    Boolean is_net_connected = false;
    // Edit Text
    private EditText edtUsername, edtPassword;
    // Button
    Button btnLogin;
    // TextView
    TextView tvRegistration, txtAppVersion,tvForgotPassword;
    // Relative Layout
    private RelativeLayout loginPageLayout;

//---------------------------------------------------------- OnCreate ------------------------------------------------------------

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Hide Action Bar
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        // base Application
        baseApplication = (BaseApplication) getApplication();
        // Activity
        mActivity = this;
        // init Database
        initDatabase();
        // init
        init();
        setDefaultPreferenceData();

        // network check broadcastReceiver!
        broadcastReceiver = new ConnectivityReceiver() {
            @Override
            protected void onNetworkChange(String alert) {
                Snackbar snackbar = Snackbar.make(loginPageLayout, alert, Snackbar.LENGTH_SHORT);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(Color.parseColor("#28bad6"));
                snackbar.show();
//                if(Utility.NO_NETWORK_CONNECTED.equals(alert)){ is_net_connected = false; }
//                else if(Utility.NETWORK_CONNECTED.equals(alert)){ is_net_connected = true; }
            }
        };
        registerNetworkBroadcast();

        // Password Edit Text Touch
        edtPassword.setOnTouchListener((view, event) -> {
            final int Right = 2;
            if(event.getAction() == MotionEvent.ACTION_UP){
                if(event.getRawX() >= edtPassword.getRight() - edtPassword.getCompoundDrawables()[Right].getBounds().width()){
                    int selection = edtPassword.getSelectionEnd();
                    if(isPasswordVisible){
                        edtPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.icon_lock,0,R.drawable.icon_password_not_visible,0);
                        edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        isPasswordVisible = false;
                    }
                    else{
                        edtPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.icon_lock,0,R.drawable.icon_password_visible,0);
                        edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        isPasswordVisible = true;
                    }
                    edtPassword.setSelection(selection);
                    return true;
                }
            }

            return false;

        });

    }

//---------------------------------------------------------- Init Database ------------------------------------------------------------

    private void initDatabase() {
        dataBaseHelper = new DataBaseHelper(LoginActivity.this);
    }

//---------------------------------------------------------- Init  ------------------------------------------------------------

    @SuppressLint("SetTextI18n")
    private void init() {
        txtAppVersion    = findViewById(R.id.txtAppVersion);
        tvRegistration   = findViewById(R.id.tvRegistration);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        edtUsername      = findViewById(R.id.edtUsername);
        edtPassword      = findViewById(R.id.edtPassword);
        btnLogin         = findViewById(R.id.btnLogin);
        loginPageLayout  = findViewById(R.id.login_page_layout);

        // Set On Click Listener
        tvRegistration.setOnClickListener(click);
        tvForgotPassword.setOnClickListener(click);
        btnLogin.setOnClickListener(click);

        //txtAppVersion.setText("App version: " + BuildConfig.VERSION_NAME);
        txtAppVersion.setText("App version: " + URL_Utility.APP_VERSION);
    }

//---------------------------------------------------------- set Default SharePreference Data ------------------------------------------------------------

    private void setDefaultPreferenceData() {
        Utility.saveData(mActivity, Utility.KEY_PROJECT_ID, PROJECT_ID_DEFAULT);
        Utility.saveData(mActivity, Utility.UNIT_DISTANCE_DATA, "" + Utility.UNIT_DISTANCE.METER_KM);
        Utility.saveData(mActivity, Utility.UNIT_AREA_DATA, "" + Utility.UNIT_AREA.YD_2);
        Utility.saveData(mActivity, Utility.UNIT_GPS_ALTITUDE_DATA, "" + Utility.UNIT_GPS.METER);
        Utility.saveData(mActivity, Utility.IS_SCREEN_ORIENTATION_PORTRAIT, true);
        Utility.saveData(mActivity, Utility.TEXT_SIZE, "" + SystemUtility.DEFAULT_TEXT_SIZE);
        Utility.saveData(mActivity, Utility.TOP_PANEL.LAYERS, true);
        Utility.saveData(mActivity, Utility.TOP_PANEL.MEASURE, true);
        Utility.saveData(mActivity, Utility.TOP_PANEL.GPS_COMPASS, false);
    }

//---------------------------------------------------------- On Click ------------------------------------------------------------

    @SuppressLint("NonConstantResourceId")
    private final View.OnClickListener click = v -> {
        int getid = v.getId();
        switch (getid) {
            case R.id.btnLogin:
                if(SystemPermission.isInternetConnected(this)){
                    processToLogin();
                }
                else{
                    Utility.getOKDialogBox(this, "Alert", "Need Internet Connection.", DialogInterface::dismiss);
                }
                break;
            case R.id.tvRegistration:
                //  reDirectRegistration();
                break;

            case R.id.tvForgotPassword:
                forgetPassword();
                break;
        }
    };

//---------------------------------------------------------- Process To Login ------------------------------------------------------------

    private void processToLogin() {
        if (Utility.isEmptyString(edtUsername.getText().toString().trim())) {
            Utility.showInfoDialog(LoginActivity.this, getResources().getString(R.string.Please_enter_username));
            return;
        }
        if (Utility.isEmptyString(edtPassword.getText().toString().trim())) {
            Utility.showInfoDialog(LoginActivity.this, getResources().getString(R.string.Please_enter_password));
            return;
        }
        if(!Utility.isEmptyString(edtUsername.getText().toString().trim()) && Patterns.EMAIL_ADDRESS.matcher(edtUsername.getText().toString()).matches()){
            startMyDialog("Logging In...");
            showProgressBar("Logging In...", true);
            processToLoginAuthentication();
        }
        else{
            Toast.makeText(mActivity, "Please Enter valid Email ID", Toast.LENGTH_SHORT).show();
        }

    }

//---------------------------------------------------------- Get Login ------------------------------------------------------------

    private void processToLoginAuthentication() {

        Map<String, String> params = new HashMap<>();
        String data = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(URL_Utility.PARAM_LOGIN_USERNAME, edtUsername.getText().toString().trim());
            jsonObject.put(URL_Utility.PARAM_LOGIN_PASSWORD, edtPassword.getText().toString().trim());
            jsonObject.put(URL_Utility.ACTION_APP_VERSION, URL_Utility.APP_VERSION);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            data =  AESCrypt.encrypt(jsonObject.toString());
           // Log.e(TAG,data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.put("data",data);
        ResponseCode responseCode = ResponseCode.WS_LOGIN;
        BaseApplication.getInstance().makeHttpPostRequest(LoginActivity.this, responseCode, URL_Utility.WS_LOGIN, params, false, false);
    }

//---------------------------------------------------------- Get Project ------------------------------------------------------------

    private void getUserProject(){
        Map<String, String> params = new HashMap<>();
        String data = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(URL_Utility.PARAM_USED_ID, Utility.getSavedData(LoginActivity.this, Utility.LOGGED_USERID));
            jsonObject.put(URL_Utility.PARAM_LOGIN_TOKEN, Utility.getSavedData(LoginActivity.this, Utility.LOGGED_TOKEN));
            jsonObject.put(URL_Utility.ACTION_APP_VERSION, URL_Utility.APP_VERSION);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        // Log.e(TAG, jsonObject.toString());
        try {
            data =  AESCrypt.encrypt(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.put("data",data);
        ResponseCode responseCode = ResponseCode.WS_USER_PROJECT;
        BaseApplication.getInstance().makeHttpPostRequest(LoginActivity.this, responseCode, URL_Utility.WS_USER_PROJECT, params, false, false);

    }

//---------------------------------------------------------- Get GIS Survey ------------------------------------------------------------

    private void getGISSurvey(){
        Map<String, String> params = new HashMap<>();
        String data = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(URL_Utility.PARAM_USED_ID, Utility.getSavedData(LoginActivity.this, Utility.LOGGED_USERID));
            jsonObject.put(URL_Utility.PARAM_LOGIN_TOKEN, Utility.getSavedData(LoginActivity.this, Utility.LOGGED_TOKEN));
            jsonObject.put(URL_Utility.ACTION_APP_VERSION, URL_Utility.APP_VERSION);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            data =  AESCrypt.encrypt(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.put("data",data);
        ResponseCode responseCode = ResponseCode.WS_GIS_SURVEY;
        BaseApplication.getInstance().makeHttpPostRequest(LoginActivity.this, responseCode, URL_Utility.WS_GIS_SURVEY, params, false, false);

    }

//---------------------------------------------------------- Get User TRACK Status ------------------------------------------------------------

    private void getUserTrackStatus() {
        Map<String, String> params = new HashMap<>();
        String data = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(URL_Utility.PARAM_LOGIN_USER_ID, Utility.getSavedData(LoginActivity.this, Utility.LOGGED_USERID));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.e(TAG, jsonObject.toString());
        try {
            data =  AESCrypt.encrypt(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.put("data",data);
        ResponseCode responseCode = ResponseCode.WS_USER_TRACK_STATUS;
        BaseApplication.getInstance().makeHttpPostRequest(LoginActivity.this, responseCode, URL_Utility.WS_USER_TRACKING_STATUS, params, false, false);
    }

//---------------------------------------------------------- Server onSuccessResponse ------------------------------------------------------------

    @Override
    public void onSuccessResponse(ResponseCode responseCode, String response){

        //Log.e("http", "responseCode: " + responseCode);
        //Log.e("http", "response: " + response);
        // -------------- New ------------------------------

        // Login
        if (responseCode == ResponseCode.WS_LOGIN) {
            // Decrypt Response
            response = AESCrypt.decryptResponse(response);
            Log.e(TAG,"Login API Response: \n"+response);
            if(!response.equals("")) {
                try {
                    JSONObject mLoginObj = new JSONObject(response);
                    String status = mLoginObj.optString("status");
                    Log.e(TAG,"Login Status: "+status);
                    if (status.equalsIgnoreCase("Success")){

                        JSONObject mProfileObj = mLoginObj.getJSONObject("profile");
                        String userid        = mProfileObj.optString("user_id");
                        String username      = mProfileObj.optString("username");
                        String token         = mProfileObj.optString("token");
                        String user_tracking = mProfileObj.optString("user_tracking");
                        String first_name    = mProfileObj.optString("first_name");
                        String last_name     = mProfileObj.optString("last_name");
                        String mobile_number = mProfileObj.optString("mobile_number");
                        String email_id      = mProfileObj.optString("email_id");
                        String profile_image = mProfileObj.optString("profile_image");
                        JSONArray modulesArray = new JSONArray(mLoginObj.getString("mobile_modules"));
                        // Modules
                        StringBuilder sb_modules = new StringBuilder();

                        if(modulesArray.length() > 0){
                            for (int i = 0; i < modulesArray.length(); i++) {
                                String module_name = modulesArray.getJSONObject(i).getString("module_name");
                                String module      = modulesArray.getJSONObject(i).getString("mobile_module");
                                sb_modules.append(module_name).append("#").append(module);
                                if (i < modulesArray.length() - 1) {
                                    sb_modules.append(",");
                                }
                            }
                            //Log.e(TAG, sb_modules.toString());
                        }

                        dataBaseHelper.open();
                        // Map Camera
                        dataBaseHelper.executeCursor(DataBaseHelper.DELETE_TABLE_MAP_CAMERA_IMAGE);
                        dataBaseHelper.executeCursor(DataBaseHelper.DELETE_TABLE_MAP_CAMERA_IMAGE_LOCAL);
                        dataBaseHelper.executeCursor(DataBaseHelper.DELETE_TABLE_MAP_CAMERA_IMAGE_S_LOCAL);
                        // GIS Camera
                        dataBaseHelper.executeCursor(DataBaseHelper.DELETE_TABLE_GIS_MAP_CAMERA_IMAGE);
                        dataBaseHelper.executeCursor(DataBaseHelper.DELETE_TABLE_GIS_MAP_CAMERA_IMAGE_LOCAL);
                        dataBaseHelper.executeCursor(DataBaseHelper.DELETE_TABLE_GIS_MAP_CAMERA_IMAGE_S_LOCAL);
                        // Time Line
                        dataBaseHelper.executeCursor(DataBaseHelper.DELETE_TABLE_TIMELINE_IMAGE_LOCAL);
                        dataBaseHelper.executeCursor(DataBaseHelper.DELETE_TABLE_TIMELINE_IMAGE);
                        dataBaseHelper.executeCursor(DataBaseHelper.DELETE_TABLE_TIMELINE_S_IMAGE_LOCAL);
                        dataBaseHelper.close();

                        // Time Line
                        JSONArray timeLineArray = new JSONArray(mLoginObj.getString("timeline"));
                        if(timeLineArray.length() > 0){
                            for(int i=0; i<timeLineArray.length(); i++){
                                String image             = timeLineArray.getJSONObject(i).getString("image");
                                String imageDescription  = timeLineArray.getJSONObject(i).getString("image_desc");
                                String Lat               = timeLineArray.getJSONObject(i).getString("latitude");
                                String Lon               = timeLineArray.getJSONObject(i).getString("longitude");
                                String Datetime          = timeLineArray.getJSONObject(i).getString("datetime");
                                dataBaseHelper.insertTimeLineSImageLocal(userid,imageDescription,Lat,Lon,Datetime,image);
                            }
                        }
                        
                        Utility.saveData(LoginActivity.this, Utility.LOGGED_USERID, userid);
                        Utility.saveData(LoginActivity.this, Utility.LOGGED_USERNAME, username);
                        Utility.saveData(LoginActivity.this, Utility.LOGGED_TOKEN, token);
                        Utility.saveData(LoginActivity.this, Utility.LOGGED_FIRSTNAME, first_name);
                        Utility.saveData(LoginActivity.this, Utility.PROFILE_FIRSTNAME, first_name);
                        Utility.saveData(LoginActivity.this, Utility.PROFILE_LASTNAME, last_name);
                        Utility.saveData(LoginActivity.this, Utility.PROFILE_MOBILE_NUMBER, mobile_number);
                        Utility.saveData(LoginActivity.this, Utility.PROFILE_IMAGE, profile_image);
                        Utility.saveData(LoginActivity.this, Utility.PROFILE_EMAILID, email_id);
                        Utility.saveData(LoginActivity.this, Utility.MODULES, sb_modules.toString());
                        Utility.saveData(LoginActivity.this, Utility.IS_USER_TRACKING, !Utility.isEmptyString(user_tracking) && user_tracking.equalsIgnoreCase("Yes"));
                        //Log.e(TAG, "Login API Fetch Complete");
                        //startMyDialog("Projects Loading...");
                        showProgressBar("Projects Loading...", false);
                        Log.e(TAG,"Call Project API");
                        getUserProject();
                    }
                    else {
                        dismissMyDialog();
                        Utility.showInfoDialog(LoginActivity.this, "User Not Found");
                    }
                } catch (JSONException e) {
                    Toast.makeText(mActivity, "something is wrong", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.getMessage());
                    dismissMyDialog();
                }
            }
            else{
                Toast.makeText(mActivity, "something is wrong", Toast.LENGTH_SHORT).show();
                dismissMyDialog();
            }
        }
        // Get User Project
        if(responseCode == ResponseCode.WS_USER_PROJECT){
            // Decrypt Response
            //response = AESCrypt.decryptResponse(response);
            response = AESCrypt.decryptResponse(response);
            Log.e(TAG,"Project API Response: \n"+response);
            if(!response.equals("")) {
                try {
                    JSONObject mLoginObj = new JSONObject(response);
                    String status = mLoginObj.optString("status");
                    Log.e(TAG,"Project Status: "+ status);
                    if (status.equalsIgnoreCase("Success")){

                        dataBaseHelper.open();
                       // dataBaseHelper.executeQuery(DataBaseHelper.DELETE_TABLE_PROJECT_USER);
                        dataBaseHelper.executeQuery(DataBaseHelper.DELETE_TABLE_PROJECT_Array);
                        dataBaseHelper.executeQuery(DataBaseHelper.DELETE_TABLE_PROJECT_WORK_Array);
                        dataBaseHelper.executeQuery(DataBaseHelper.DELETE_TABLE_PROJECT_LAYERS);
                        dataBaseHelper.executeCursor(DataBaseHelper.DELETE_TABLE_PROJECT_FORM);
                        dataBaseHelper.executeCursor(DataBaseHelper.DELETE_TABLE_PROJECT_SURVEY_FORM);
                        dataBaseHelper.executeCursor(DataBaseHelper.DELETE_TABLE_PROJECT_GEOFENCE);
                        dataBaseHelper.close();

                        JSONArray jsonArray = new JSONArray(mLoginObj.getString("projects"));
                        if(jsonArray.length() > 0)
                        {
                            for(int i=0; i<jsonArray.length(); i++){
                                String project_id  = jsonArray.getJSONObject(i).getString("project_id");
                                String project_name = jsonArray.getJSONObject(i).getString("project_name");
                                dataBaseHelper.insertProjectArray(project_id,project_name);
                            }

                           // dataBaseHelper.insertProjectUser(response);
                            // Work Array
                            JSONArray workArray  = new JSONArray(mLoginObj.getString("works"));
                            if(workArray.length() > 0) {

                                for (int i = 0; i < workArray.length(); i++) {
                                    String work_project_id = workArray.getJSONObject(i).getString("project_id");
                                    String work_stage_id   = ""; // workArray.getJSONObject(i).getString("stage_id");
                                    String work_activity_id = ""; //workArray.getJSONObject(i).getString("activity_id");
                                    String work_id = workArray.getJSONObject(i).getString("work_id");
                                    String work_name = workArray.getJSONObject(i).getString("work_name");
                                    JSONArray geofence = new JSONArray(workArray.getJSONObject(i).getString("geofence"));
                                    dataBaseHelper.insertProjectWorkArray(work_project_id, work_stage_id, work_activity_id, work_id, work_name);
                                    dataBaseHelper.insertProjectGeoFence(work_project_id, work_stage_id, work_activity_id, work_id, work_name, geofence.toString());
                                    // work Images
                                    JSONArray cameraArray    = new JSONArray(workArray.getJSONObject(i).getString("workimages"));
                                    if(cameraArray.length() > 0){
                                        for(int j=0; j<cameraArray.length(); j++){
                                            String imageDescription  = cameraArray.getJSONObject(j).getString("image_desc");
                                            String Lat               = cameraArray.getJSONObject(j).getString("latitude");
                                            String Lon               = cameraArray.getJSONObject(j).getString("longitude");
                                            String Datetime          = cameraArray.getJSONObject(j).getString("datetime");
                                            String image             = cameraArray.getJSONObject(j).getString("image");
                                            dataBaseHelper.insertMapImageSLocal("",work_project_id,work_id,imageDescription,Lat,Lon,Datetime,image);
                                        }
                                    }
                                }
                            }

                            // Layers Array
                            JSONArray projectLayerArray   = new JSONArray(mLoginObj.getString("layers"));
                            if(projectLayerArray.length() > 0){
                                for(int i=0; i<projectLayerArray.length(); i++){
                                    String layer_id              = projectLayerArray.getJSONObject(i).getString("id");
                                    String project_id            = projectLayerArray.getJSONObject(i).getString("project_id");
                                    //String user_id               = projectLayerArray.getJSONObject(i).getString("user_id");
                                    //String stage_id              = projectLayerArray.getJSONObject(i).getString("stage_id");
                                    //String activity_id           = projectLayerArray.getJSONObject(i).getString("activity_id");
                                    String work_id               = projectLayerArray.getJSONObject(i).getString("work_id");
                                    String form_id               = projectLayerArray.getJSONObject(i).getString("form");
                                    String layer_name            = projectLayerArray.getJSONObject(i).getString("layer_name");
                                    String layer_type            = projectLayerArray.getJSONObject(i).getString("layer_type");
                                    String layer_icon            = projectLayerArray.getJSONObject(i).getString("layer_icon");
                                    String layer_icon_height     = projectLayerArray.getJSONObject(i).getString("icon_height");
                                    String layer_icon_width      = projectLayerArray.getJSONObject(i).getString("icon_width");
                                    String layer_line_color      = projectLayerArray.getJSONObject(i).getString("line_color");
                                    String layer_line_type       = projectLayerArray.getJSONObject(i).getString("line_type");
                                    String form_background_color = projectLayerArray.getJSONObject(i).getString("formbg_color");
                                    String form_sno              = projectLayerArray.getJSONObject(i).getString("form_sno");
                                    String form_logo             = projectLayerArray.getJSONObject(i).getString("form_logo");
                                    String only_view             = projectLayerArray.getJSONObject(i).getString("only_view");
                                    JSONArray latLongArray = new JSONArray(projectLayerArray.getJSONObject(i).getString("latlong"));
                                    JSONArray formData     = new JSONArray(projectLayerArray.getJSONObject(i).getString("formData"));
                                    JSONArray filledForms  = new JSONArray(projectLayerArray.getJSONObject(i).getString("filledForms"));
                                    dataBaseHelper.insertProjectLayers(layer_id,project_id,work_id,form_id,layer_name,layer_type,layer_icon,layer_icon_height,layer_icon_width,layer_line_color,layer_line_type,latLongArray.toString(),formData.toString(),filledForms.toString(),form_background_color,form_logo,form_sno,only_view,false,false,false);
                                }
                            }
                        }
                        Log.e(TAG,"Call GIS Survey API");
                        getGISSurvey();
                    }
                    else {
                        dismissMyDialog();
                        Log.e(TAG, "Project API Status : "+ status);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(mActivity, "something is wrong", Toast.LENGTH_SHORT).show();
                    dismissMyDialog();
                }
            }
            else{
                Log.e(TAG, response);
                Toast.makeText(mActivity, "something is wrong", Toast.LENGTH_SHORT).show();
                dismissMyDialog();
            }
        }
        // GIS Survey
        if(responseCode == ResponseCode.WS_GIS_SURVEY){
            // Decrypt Response
           // response = AESCrypt.decryptResponse(response);
            response = AESCrypt.decryptResponse(response);
            Log.e(TAG,"GIS Survey API Response: \n"+response);
            if(!response.equals("")) {
                try {
                    JSONObject mLoginObj = new JSONObject(response);
                    String status = mLoginObj.optString("status");
                    Log.e(TAG,"GIS Survey Status: "+ status);
                    if (status.equalsIgnoreCase("Success")){

                        dataBaseHelper.open();
                     //   dataBaseHelper.executeCursor(DataBaseHelper.DELETE_TABLE_GIS_SURVEY);
                        dataBaseHelper.executeCursor(DataBaseHelper.DELETE_TABLE_GIS_SURVEY_Array);
                        dataBaseHelper.executeCursor(DataBaseHelper.DELETE_TABLE_GIS_SURVEY_WORK_Array);
                        dataBaseHelper.executeQuery(DataBaseHelper.DELETE_TABLE_GIS_SURVEY_LAYERS);
                        dataBaseHelper.executeQuery(DataBaseHelper.DELETE_TABLE_GIS_SURVEY_ONLINE_LAYERS);
                        dataBaseHelper.executeCursor(DataBaseHelper.DELETE_TABLE_GIS_SURVEY_FORM);
                        dataBaseHelper.executeCursor(DataBaseHelper.DELETE_TABLE_GIS_SURVEY_SURVEY_FORM);
                        dataBaseHelper.executeCursor(DataBaseHelper.DELETE_TABLE_GIS_SURVEY_GEOFENCE);
                        dataBaseHelper.executeCursor(DataBaseHelper.DELETE_TABLE_ONLINE_LAYERS);
                        dataBaseHelper.close();

                        // Survey Array
                        JSONArray jsonArray = new JSONArray(mLoginObj.getString("survey"));
                        if(jsonArray.length() > 0)
                        {
                            for(int i=0; i<jsonArray.length(); i++){
                                String survey_id   = jsonArray.getJSONObject(i).getString("survey_id");
                                String survey_name = jsonArray.getJSONObject(i).getString("survey_name");
                                dataBaseHelper.insertGISSurveyArray(survey_id,survey_name);
                            }
                            //dataBaseHelper.insertGISSurvey(response);
                            // Work Array
                            JSONArray workArray  = new JSONArray(mLoginObj.getString("works"));
                            if (workArray.length() > 0) {
                                for (int i = 0; i < workArray.length(); i++) {
                                    String work_survey_id = workArray.getJSONObject(i).getString("survey_id");
                                    String work_id        = workArray.getJSONObject(i).getString("work_id");
                                    String work_name      = workArray.getJSONObject(i).getString("work_name");
                                    JSONArray geofence    = new JSONArray(workArray.getJSONObject(i).getString("geofence"));
                                    dataBaseHelper.insertGISSurveyWorkArray(work_survey_id,"","",work_id, work_name);
                                    dataBaseHelper.insertGISSurveyGeoFence(work_survey_id, work_id, work_name, geofence.toString());
                                    // work Images
                                    JSONArray cameraArray    = new JSONArray(workArray.getJSONObject(i).getString("workimages"));
                                    if(cameraArray.length() > 0){
                                        for(int j=0; j<cameraArray.length(); j++){
                                            String imageDescription  = cameraArray.getJSONObject(j).getString("image_desc");
                                            String Lat               = cameraArray.getJSONObject(j).getString("latitude");
                                            String Lon               = cameraArray.getJSONObject(j).getString("longitude");
                                            String Datetime          = cameraArray.getJSONObject(j).getString("datetime");
                                            String image             = cameraArray.getJSONObject(j).getString("image");
                                            dataBaseHelper.insertGISMapImageSLocal("",work_survey_id,work_id,imageDescription,Lat,Lon,Datetime,image);
                                        }
                                    }
                                }
                            }

                            // Layers Array
                            JSONArray projectLayerArray   = new JSONArray(mLoginObj.getString("layers"));
                            if(projectLayerArray.length() > 0){
                                for(int i=0; i<projectLayerArray.length(); i++){
                                    String layer_id              = projectLayerArray.getJSONObject(i).getString("id");
                                    String survey_id             = projectLayerArray.getJSONObject(i).getString("survey_id");
                                    //String user_id               = projectLayerArray.getJSONObject(i).getString("user_id");
                                    String work_id               = projectLayerArray.getJSONObject(i).getString("work_id");
                                    String form_id               = projectLayerArray.getJSONObject(i).getString("form");
                                    String layer_name            = projectLayerArray.getJSONObject(i).getString("layer_name");
                                    String layer_type            = projectLayerArray.getJSONObject(i).getString("layer_type");
                                    String layer_icon            = projectLayerArray.getJSONObject(i).getString("layer_icon");
                                    String layer_icon_height     = projectLayerArray.getJSONObject(i).getString("icon_height");
                                    String layer_icon_width      = projectLayerArray.getJSONObject(i).getString("icon_width");
                                    String layer_line_color      = projectLayerArray.getJSONObject(i).getString("line_color");
                                    String layer_line_type       = projectLayerArray.getJSONObject(i).getString("line_type");
                                    String form_background_color = projectLayerArray.getJSONObject(i).getString("formbg_color");
                                    String form_sno              = projectLayerArray.getJSONObject(i).getString("form_sno");
                                    String form_logo             = projectLayerArray.getJSONObject(i).getString("form_logo");
                                    String only_view             = projectLayerArray.getJSONObject(i).getString("only_view");
                                    JSONArray latLongArray = null;
                                    if(only_view.equalsIgnoreCase("f")){
                                        latLongArray = new JSONArray(projectLayerArray.getJSONObject(i).getString("latlong"));
                                    }
                                    String latlongString = projectLayerArray.getJSONObject(i).getString("latlong").toString();
                                    JSONArray formData     = new JSONArray(projectLayerArray.getJSONObject(i).getString("formData"));
                                    JSONArray filledForms  = new JSONArray(projectLayerArray.getJSONObject(i).getString("filledForms"));

                                    if(latLongArray != null){
                                        dataBaseHelper.insertGISSurveyLayers(layer_id,survey_id,work_id,form_id,layer_name,layer_type,layer_icon,layer_icon_height,layer_icon_width,layer_line_color,layer_line_type,latLongArray.toString(),formData.toString(),filledForms.toString(),form_background_color,form_logo,form_sno,only_view,false,false,false);
                                    }
                                    else{
                                        dataBaseHelper.insertGISSurveyLayers(layer_id,survey_id,work_id,form_id,layer_name,layer_type,layer_icon,layer_icon_height,layer_icon_width,layer_line_color,layer_line_type,latlongString,formData.toString(),filledForms.toString(),form_background_color,form_logo,form_sno,only_view,false,false,false);
                                    //   String c = "f";
                                    //    dataBaseHelper.insertOnlineLayer(latlongString,"f");

                                    //  dataBaseHelper.insertGISSurveyOnlineLayers(layer_id,survey_id,work_id,form_id,layer_name,layer_type,layer_icon,layer_icon_height,layer_icon_width,layer_line_color,layer_line_type,latlongString,formData.toString(),filledForms.toString(),form_background_color,form_logo,form_sno,only_view,false,false,false,c);
                                    }
                                }
                            }
                        }
                        getProject();
                    }
                    else {
                        dismissMyDialog();
                        Log.e(TAG, "GIS Survey API Status : "+ status);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "GIS Survey JSON Exception : " + e.getMessage());
                    Toast.makeText(mActivity, "something is wrong", Toast.LENGTH_SHORT).show();
                    dismissMyDialog();
                }
            }
            else{
                Log.e(TAG, response);
                Toast.makeText(mActivity, "something is wrong", Toast.LENGTH_SHORT).show();
                dismissMyDialog();
            }
        }

        // -------------- New ------------------------------

        // -------------- OLD ------------------------------
        // Project
        if (responseCode == ResponseCode.WS_PROJECT) {

            try {
                JSONObject mLoginObj = new JSONObject(response);
                String msg = mLoginObj.getString("msg");
                if (msg.equalsIgnoreCase("Success")) {
                    JSONArray dataArray = new JSONArray(mLoginObj.getString("data"));
                    if (dataArray.length() > 0) {
                        dataBaseHelper.open();
                        dataBaseHelper.executeQuery(DataBaseHelper.DELETE_TABLE_PROJECT);
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject mOArray = dataArray.getJSONObject(i);
                            String id = mOArray.getString("id");
                            String project = mOArray.getString("project");
                            String latitude = mOArray.getString("latitude");
                            String longitude = mOArray.getString("longitude");
                            JSONArray arrLayers = mOArray.optJSONArray("layers");
                           // Log.e(TAG,arrLayers.toString());
                            ArrayList<BinLayerProject> listLayers = new ArrayList<>();
                            if (arrLayers != null){
                                dataBaseHelper.open();
                                dataBaseHelper.executeQuery(DataBaseHelper.DELETE_TABLE_FORM);

                                for (int j = 0; j < arrLayers.length(); j++) {
                                    JSONObject jObjLayer = arrLayers.optJSONObject(j);
                                    if (jObjLayer != null) {
                                        String layerName = jObjLayer.optString("name");
                                        String layerUrl = jObjLayer.optString("layer_url");
                                        String layer_type = jObjLayer.optString("layer_type");
                                        String layer_display = jObjLayer.optString("layer_display");
                                        //----------------------------------
                                        JSONObject jsonForm = jObjLayer.optJSONObject("form");
                                        assert jsonForm != null;
                                        String form_id = jsonForm.optString("form_id");
                                        String formName = jsonForm.optString("description");
                                        dataBaseHelper.insertForm(form_id, formName, id);
                                        //----------------------------------
                                        BinLayerProject binLayerProject = new BinLayerProject();
                                        binLayerProject.setProjectId(id);
                                        binLayerProject.setName(layerName);
                                        binLayerProject.setEnable(true);
                                        binLayerProject.setActive(false);
                                        binLayerProject.setCustomMade(false);
                                        binLayerProject.setIndex(1);
                                        try {
                                            binLayerProject.setUrl(URLDecoder.decode(layerUrl, "utf-8"));
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        binLayerProject.setType(layer_type);
                                        binLayerProject.setDisplay(layer_display);
                                        binLayerProject.setFormId(form_id);
                                        binLayerProject.setFormName(formName);
                                        listLayers.add(binLayerProject);
                                    }
                                }
                            }
                            dataBaseHelper.insertProject(id, project, latitude, longitude, new Gson().toJson(listLayers));
                        }
                        dataBaseHelper.close();
                    }
//                    getForm();

                } else {
                    dismissMyDialog();
                    //Utility.showInfoDialog(LoginActivity.this, msg);
                }
                getFormDetails();
            } catch (JSONException e) {
                e.printStackTrace();
                //dismissMyDialog();

                Utility.showInfoDialog(LoginActivity.this, getResources().getString(R.string.lbl_No_project_assigned_to_the_user));
            }
        }
        // Forms
        if (responseCode == ResponseCode.WS_FORMS) {
            try {
                JSONObject mLoginObj = new JSONObject(response);
                String msg = mLoginObj.getString("msg");
                if (msg.equalsIgnoreCase("Success")) {
                    JSONArray dataArray = new JSONArray(mLoginObj.getString("data"));
                    if (dataArray.length() > 0) {
                        dataBaseHelper.open();
                        dataBaseHelper.executeQuery(DataBaseHelper.DELETE_TABLE_FORM);
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject mOArray = dataArray.getJSONObject(i);
                            String form_id = mOArray.getString("form_id");
                            String description = mOArray.getString("description");
                            String project_id = mOArray.getString("project_id");
                            dataBaseHelper.insertForm(form_id, description, project_id);
                        }
                        dataBaseHelper.close();
                    }

                } else {
                    dismissMyDialog();
                    // Utility.showInfoDialog(LoginActivity.this, msg);
                }
                getFormDetails();
            } catch (JSONException e) {
                e.printStackTrace();
                dismissMyDialog();
                Utility.showInfoDialog(LoginActivity.this, getResources().getString(R.string.lbl_No_form_assigned_to_the_user));
            }
        }
        // Forms Details
        if (responseCode == ResponseCode.WS_FORMS_DETAILS) {
            try {
                JSONObject mLoginObj = new JSONObject(response);
                String msg = mLoginObj.getString("msg");
                if (msg.equalsIgnoreCase("Success")) {
                    if (mLoginObj.has("data")) {
                        JSONArray dataArray = new JSONArray(mLoginObj.getString("data"));
                        if (dataArray.length() > 0) {
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject jsonObject = dataArray.getJSONObject(i);
                                String formId = jsonObject.optString("form_id");
                                JSONArray formDataArr = jsonObject.optJSONArray("form_data");
                                if (formDataArr != null)
                                    dataBaseHelper.insertFormDetails(formId, formDataArr.toString(), "", "", DataBaseHelper.isSyncOffline, DataBaseHelper.FORM_TYPE.TYPE_NEW);
                            }
                        }
                    }

                } else {
                    dismissMyDialog();
                    //  Utility.showInfoDialog(LoginActivity.this, msg);
                }
                getUserRights();
            } catch (JSONException e) {
                e.printStackTrace();
                dismissMyDialog();
                Utility.showInfoDialog(LoginActivity.this, getResources().getString(R.string.lbl_No_form_details_assigned_to_the_user));
            }
        }
        // User Rights
        if (responseCode == ResponseCode.WS_USER_RIGHTS) {
            try {
                JSONObject mLoginObj = new JSONObject(response);
                if (mLoginObj.has("data")) {
                    JSONArray dataArray = new JSONArray(mLoginObj.getString("data"));
                    if (dataArray.length() > 0) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject mOArray = dataArray.getJSONObject(i);
                            if (mOArray.has("user_type")) {
                                String user_type = mOArray.getString("user_type");
                                Utility.saveData(LoginActivity.this, Utility.USER_TYPE, user_type);
                                break;
                            }
                        }
                    }
                    // getProfile();
                    // processToModules();
                }
                getUserTrackStatus();
                // processToBoundaryGoeJson();
            } catch (JSONException e) {
                e.printStackTrace();
                dismissMyDialog();
            }
        }
        // -------------- OLD ------------------------------

        // -------------- New ------------------------------

        // User Tracking Status
        if (responseCode == ResponseCode.WS_USER_TRACK_STATUS){
            //String res = AESCrypt.decryptResponse(response);
            String res = AESCrypt.decryptResponse(response);
            Log.e(TAG,"Tracking Response: \n"+res);
            if(!res.equals("")) {
                try {
                    JSONObject mLoginObj = new JSONObject(res);
                    String msg = mLoginObj.optString("status");
                    Log.e(TAG, "Tracking Status: "+ msg);
                    if (msg.equalsIgnoreCase("Success")) {
                        //Log.e(TAG,msg);
                        JSONObject mOArray = new JSONObject(mLoginObj.optString("data"));

                        if(mOArray.length() > 0){
                            String user_tracking = mOArray.optString("user_tracking");
                            String start_date = mOArray.optString("start_date");
                            String end_date = mOArray.optString("end_date");
                            String start_time = mOArray.optString("start_time");
                            String end_time = mOArray.optString("end_time");
                            String distance = mOArray.optString("distance");
                            String interval = mOArray.optString("interval");
                            Utility.saveData(LoginActivity.this, Utility.IS_USER_TRACKING, !Utility.isEmptyString(user_tracking) && user_tracking.equalsIgnoreCase("Yes"));
                            Utility.saveData(LoginActivity.this, Utility.TRACKING_START_DATE, start_date);
                            Utility.saveData(LoginActivity.this, Utility.TRACKING_END_DATE, end_date);
                            Utility.saveData(LoginActivity.this, Utility.TRACKING_START_TIME, start_time);
                            Utility.saveData(LoginActivity.this, Utility.TRACKING_END_TIME, end_time);
                            Utility.saveData(LoginActivity.this, Utility.TRACKING_DISTANCE, distance);
                            Utility.saveData(LoginActivity.this, Utility.TRACKING_INTERVAL, interval);
                        }
                        Utility.insertStandard_P_L_F_FD_ToDb(mActivity, dataBaseHelper, Utility.getSavedData(LoginActivity.this, Utility.PROFILE_FIRSTNAME));
                        Utility.saveData(LoginActivity.this, Utility.IS_USER_SUCCESSSFULLY_LOGGED_IN, true);
                        //reDirectProject();
                        //reDirectToDashBoardSplashActivity();
                        reDirectToDashBoard();
                        dismissMyDialog();

                    } else {
                        Utility.insertStandard_P_L_F_FD_ToDb(mActivity, dataBaseHelper, Utility.getSavedData(LoginActivity.this, Utility.PROFILE_FIRSTNAME));
                        Utility.saveData(LoginActivity.this, Utility.IS_USER_SUCCESSSFULLY_LOGGED_IN, true);
                        //reDirectToDashBoardSplashActivity();
                        reDirectToDashBoard();
                        dismissMyDialog();
                        //Log.e(TAG,"Tracking User Status: "+msg);
                        //Utility.showInfoDialog(LoginActivity.this, msg);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                    Utility.insertStandard_P_L_F_FD_ToDb(mActivity, dataBaseHelper, Utility.getSavedData(LoginActivity.this, Utility.PROFILE_FIRSTNAME));
                    Utility.saveData(LoginActivity.this, Utility.IS_USER_SUCCESSSFULLY_LOGGED_IN, true);
                    //reDirectToDashBoardSplashActivity();
                    reDirectToDashBoard();
                    dismissMyDialog();
                }
            }
            else{
                Utility.insertStandard_P_L_F_FD_ToDb(mActivity, dataBaseHelper, Utility.getSavedData(LoginActivity.this, Utility.PROFILE_FIRSTNAME));
                Utility.saveData(LoginActivity.this, Utility.IS_USER_SUCCESSSFULLY_LOGGED_IN, true);
                //reDirectToDashBoardSplashActivity();
                reDirectToDashBoard();
                dismissMyDialog();
            }
        }
        // Forget Password
        if(responseCode == ResponseCode.WS_FORGET_PASSWORD){

            //String res = AESCrypt.decryptResponse(response);
            String res = AESCrypt.decryptResponse(response);
            Log.e(TAG,"Forgot API Response: "+res);
            if(!res.equals("")){
                try {
                    JSONObject mBoundaryObj = new JSONObject(res);
                    String msg = mBoundaryObj.optString("status");
                    Log.e(TAG, "Forget Password Status: "+msg);
                    if (msg.equalsIgnoreCase("Success")) {
                        dismissMyDialog();
                        Utility.getOKDialogBox(mActivity, "Password", "Password Change Successfully", DialogInterface::dismiss);
                    }
                    else {
                        dismissMyDialog();
                        Utility.getOKDialogBox(mActivity, "Password", "Password Change not Successfully", DialogInterface::dismiss);
                    }
                }
                catch (JSONException e) {
                    dismissMyDialog();
                    Log.e(TAG,e.getMessage());
                    Toast.makeText(mActivity, "something is wrong", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(mActivity, "something is wrong", Toast.LENGTH_SHORT).show();
                dismissMyDialog();
            }
        }

        // -------------- New ------------------------------

    }

//---------------------------------------------------------- Server onErrorResponse ------------------------------------------------------------

    @Override
    public void onErrorResponse(ResponseCode responseCode, VolleyError error) {

        if( responseCode == ResponseCode.WS_LOGIN || responseCode == ResponseCode.WS_USER_PROJECT || responseCode == ResponseCode.WS_PROFILE_UPDATE
                || responseCode == ResponseCode.WS_GPS_TRACKING_SHOW_RECORDS || responseCode == ResponseCode.WS_GPS_TRACKING_UPLOAD
                || responseCode == ResponseCode.WS_UPLOAD_CAMERA_IMAGE       || responseCode == ResponseCode.WS_USER_TRACK_STATUS || responseCode == ResponseCode.WS_GIS_SURVEY || responseCode == ResponseCode.WS_FORGET_PASSWORD){
            String response = AESCrypt.decryptResponse(error.getMessage());
            Log.e(TAG,""+response);
            Log.e(TAG,""+error.getMessage());
            dismissMyDialog();
        }
        else{
            Log.e(TAG,error.getMessage());
            Utility.showInfoDialog(LoginActivity.this, error.getMessage());
            dismissMyDialog();
        }
    }

//---------------------------------------------------------- reDirect ------------------------------------------------------------

    private void reDirectToDashBoard() {
        // Change by Rahul Suthar
        Intent intent = new Intent(LoginActivity.this, DashBoardBNActivity.class);
        startActivity(intent);
        finish();
    }

//---------------------------------------------------------- Progress Bar ------------------------------------------------------------

    private void dismissMyDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void startMyDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(message);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }
        else {
            progressDialog.setMessage(message);
        }
    }

    private void showProgressBar(String message, boolean enable){

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            if(!enable){
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                progressDialog.setContentView(R.layout.activity_dash_board_splash);
            }
            else{
                progressDialog.setMessage(message);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            }
            progressDialog.show();
        }
        else {
            if(!enable){
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                progressDialog.setContentView(R.layout.activity_dash_board_splash);
            }
            else{
                progressDialog.setMessage(message);
            }
        }

    }

//---------------------------------------------------------- Forget Password ------------------------------------------------------------

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    private void forgetPassword(){
        try{
            // Dialog Box
            Dialog dialog = new Dialog(mActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.profile_change_password_view);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            // Edit Text
            TextView title = dialog.findViewById(R.id.headLineTitle);
            title.setText("Forget Password");
            LinearLayout llOldPassword = dialog.findViewById(R.id.oldPasswordLayout);
            llOldPassword.setVisibility(View.GONE);
            LinearLayout llEmailID   = dialog.findViewById(R.id.emailIdLayout);
            llEmailID.setVisibility(View.VISIBLE);
            EditText emailId         = dialog.findViewById(R.id.emailID);
            EditText newPassword     = dialog.findViewById(R.id.newPassword);
            EditText confirmPassword = dialog.findViewById(R.id.confirmPassword);
            // Submit Button
            Button submitButton = dialog.findViewById(R.id.profileChangeSubmitButton);
            // Close Button
            Button closeButton = dialog.findViewById(R.id.profileChangeCloseButton);
            closeButton.setOnClickListener(view -> dialog.dismiss());
            // Submit Button
            submitButton.setOnClickListener(view -> {

                if(!Utility.isEmptyString(emailId.getText().toString()) && !Utility.isEmptyString(newPassword.getText().toString()) && !Utility.isEmptyString(confirmPassword.getText().toString())) {

                    if(!newPassword.getText().toString().equals(confirmPassword.getText().toString())){
                        Toast.makeText(mActivity, "password wrong", Toast.LENGTH_SHORT).show();
                        newPassword.setError("password not match");
                        confirmPassword.setError("password not match");
                    }
                    else{
                        if (SystemUtility.isInternetConnected(mActivity)){
                            forgetPasswordSubmit(emailId,newPassword);
                        } else {
                            dismissMyDialog();
                            Utility.getOKCancelDialogBox(mActivity, "Connection Error", "Need Internet Connection to Change Password", DialogInterface::dismiss);
                        }
                    }
                }
                else{
                    Toast.makeText(mActivity, "Enter Field", Toast.LENGTH_SHORT).show();
                }

            });



            newPassword.setOnTouchListener((view, event) -> {
                final int Right = 2;
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(event.getRawX() >= newPassword.getRight() - newPassword.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = newPassword.getSelectionEnd();
                        if(isNewPasswordVisible){
                            newPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.icon_password_not_visible,0);
                            newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            isNewPasswordVisible = false;
                        }
                        else{
                            newPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.icon_password_visible,0);
                            newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isNewPasswordVisible = true;
                        }
                        newPassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            });


            confirmPassword.setOnTouchListener((view, event) -> {
                final int Right = 2;
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(event.getRawX() >= confirmPassword.getRight() - confirmPassword.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = confirmPassword.getSelectionEnd();
                        if(isConfirmPasswordVisible){
                            confirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.icon_password_not_visible,0);
                            confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            isConfirmPasswordVisible = false;
                        }
                        else{
                            confirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.icon_password_visible,0);
                            confirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isConfirmPasswordVisible = true;
                        }
                        confirmPassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            });


            // Dialog Box Show
            dialog.show();
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }

    private void forgetPasswordSubmit(EditText edtEmailID, EditText edtNewPassword){
        startMyDialog("Forget Password...");
        Map<String, String> params1 = new HashMap<>();
        String data = "";
        JSONObject params = new JSONObject();
        try {
            params.put(URL_Utility.PARAM_NEW_PASSWORD, edtNewPassword.getText().toString());
            params.put(URL_Utility.PARAM_EMAIL_ID    , edtEmailID.getText().toString());
        }
        catch (JSONException e){
            dismissMyDialog();
            e.printStackTrace();
        }
        Log.e(TAG, params.toString());
        try {
            data =  AESCrypt.encrypt(params.toString());
        } catch (Exception e) {
            dismissMyDialog();
            e.printStackTrace();
        }
        params1.put("data",data);
        if(SystemUtility.isInternetConnected(mActivity)){
            URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_FORGET_PASSWORD;
            BaseApplication.getInstance().makeHttpPostRequest(mActivity, responseCode, URL_Utility.WS_FORGET_PASSWORD, params1, false, false);
        }
        else {
            dismissMyDialog();
            Utility.getOKCancelDialogBox(mActivity, "Connection Error", "Need Internet Connection to Change Password", DialogInterface::dismiss);
        }
    }

//-----------------------------------------------------------------------------------Network Register-----------------------------------------------------------------------------------------------

    // Register Network
    protected void registerNetworkBroadcast(){
        registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
    // UnRegister Network
    protected void unregisterNetwork(){
        try{
            unregisterReceiver(broadcastReceiver);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

//----------------------------------------------------------------------------------- onDestroy -----------------------------------------------------------------------------------------------

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetwork();
    }

//---------------------------------------------------------- Old Person Api ------------------------------------------------------------

//########################################################## Old Person Api #############################################################################################
//---------------------------------------------------------- Get User Rights ------------------------------------------------------------

    private void getUserRights() {
        Map<String, String> params = new HashMap<>();
        ResponseCode responseCode = ResponseCode.WS_USER_RIGHTS;
        params.put(URL_Utility.PARAM_LOGIN_USER_ID, Utility.getSavedData(LoginActivity.this, Utility.LOGGED_USERID));
        params.put(URL_Utility.ACTION_APP_VERSION, URL_Utility.APP_VERSION);
        BaseApplication.getInstance().makeHttpPostRequest(LoginActivity.this, responseCode, URL_Utility.WS_USER_RIGHTS, params, false, false);
    }

//---------------------------------------------------------- Get Project ------------------------------------------------------------

    private void getProject() {
        Map<String, String> params = new HashMap<>();
        ResponseCode responseCode = ResponseCode.WS_PROJECT;
        params.put(URL_Utility.PARAM_LOGIN_USER_ID, Utility.getSavedData(LoginActivity.this, Utility.LOGGED_USERID));
        params.put(URL_Utility.ACTION_APP_VERSION, URL_Utility.APP_VERSION);
        BaseApplication.getInstance().makeHttpPostRequest(LoginActivity.this, responseCode, URL_Utility.WS_PROJECT, params, false, false);
    }

//---------------------------------------------------------- Get Form Details ------------------------------------------------------------

    private void getFormDetails() {
        StringBuilder form_id = getFormID();
        Map<String, String> params = new HashMap<>();
        ResponseCode responseCode = ResponseCode.WS_FORMS_DETAILS;
        params.put(URL_Utility.PARAM_FORM_ID, validateIds(form_id.toString()));
        params.put(URL_Utility.ACTION_APP_VERSION, URL_Utility.APP_VERSION);
        BaseApplication.getInstance().makeHttpPostRequest(LoginActivity.this, responseCode, URL_Utility.WS_FORMS_DETAILS, params, false, false);
    }

//---------------------------------------------------------- Get FormID ------------------------------------------------------------

    private StringBuilder getFormID() {
        StringBuilder stringBuilder = new StringBuilder();
        dataBaseHelper.open();
        Cursor curFormID = dataBaseHelper.executeCursor(DataBaseHelper.GET_FORM);
        if (curFormID != null && curFormID.getCount() > 0) {
            curFormID.moveToFirst();
            for (int i = 0; i < curFormID.getCount(); i++) {
                stringBuilder.append(curFormID.getString(curFormID.getColumnIndex("form_id")));
                stringBuilder.append(",");
                curFormID.moveToNext();
            }
        }
        dataBaseHelper.close();
        return stringBuilder;
    }

    public interface LAYER_TYPE {
        String Point = "Point";
        String Line = "LineString";
        String Polygon = "Polygon";
    }
}
