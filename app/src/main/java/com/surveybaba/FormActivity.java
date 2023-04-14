package com.surveybaba;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.VolleyError;
import com.fileupload.ImageFileUploadToServer;
import com.fileupload.VideoFileUploadToServe;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.surveybaba.ADAPTER.FormAdapter;
import com.surveybaba.ADAPTER.FormRenderAdapter;
import com.surveybaba.DTO.FormDTO;
import com.surveybaba.Database.DataBaseHelper;
import com.surveybaba.FormBuilder.FormDetailData;
import com.surveybaba.FormBuilder.FormDetailModel;
import com.surveybaba.Utilities.SystemUtility;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.backup.BackupManagerActivity;
import com.surveybaba.setting.MySubscription;
import com.surveybaba.setting.SettingActivity;
import com.volly.BaseApplication;
import com.volly.URL_Utility;
import com.volly.URL_Utility.ResponseCode;
import com.volly.WSResponseInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import static com.surveybaba.Database.DataBaseHelper.keyParam_form_uid;
import static com.surveybaba.Utilities.Utility.validateIds;
import static com.volly.URL_Utility.IMAGE_PATH;
import static com.volly.URL_Utility.VIDEO_PATH;

public class FormActivity extends AppCompatActivity implements WSResponseInterface {

    BaseApplication baseApplication;
    private GridView rvForm;
    Activity mActivity;

    private ProgressDialog progressDialog;
    ArrayList<FormDTO> listForms = new ArrayList<>();
    ArrayList<String> listVideoPaths = new ArrayList<>();
    ArrayList<String> listImagePaths = new ArrayList<>();
    ArrayList<Map<String, String>> listFormsDetails = new ArrayList<>();
    private DataBaseHelper dataBaseHelper;
    private String project_id = "";
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvView;
    private ActionBarDrawerToggle drawerToggle;
    private ArrayList<FormDTO> formDTOS;
    private LinearLayout llBackupManager, llMySubscription, llTimeline, llSetting,llLogout,llBackupManagerView, llSettingView;
    private TextView tvUsername;
    private BottomNavigationView bottomNavigation;
    private FormAdapter formAdapter;

    Map<String, String> binMapUploadParams = new HashMap<>();
//------------------------------------------------------- on Create ----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        baseApplication = (BaseApplication) getApplication();
        mActivity = this;
        initDatabase();
        initExtra();
        initSlidePanel();
        init();
        bindData();
        initFooter();
    }

//------------------------------------------------------- init Database ----------------------------------------------------------------------------------------------------------------------

    private void initDatabase() {
        dataBaseHelper = new DataBaseHelper(FormActivity.this);
    }

//-------------------------------------------------------  init Extra ----------------------------------------------------------------------------------------------------------------------

    private void initExtra() {
        Intent intent = getIntent();
        project_id = intent.getStringExtra(Utility.PASS_PROJECT_ID);
    }

//------------------------------------------------------- init Slider Panel ----------------------------------------------------------------------------------------------------------------------

    private void initSlidePanel() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nvView = (NavigationView) findViewById(R.id.nvView);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        initSliebar();
    }

//------------------------------------------------------- init  ----------------------------------------------------------------------------------------------------------------------

    private void init() {
        rvForm = findViewById(R.id.rvForm);
    }

//------------------------------------------------------- init Slider bar ----------------------------------------------------------------------------------------------------------------------

    private void initSliebar() {
        View hView =  nvView.getHeaderView(0);
        llBackupManager = hView.findViewById(R.id.llBackupManager);
        llSetting = hView.findViewById(R.id.llSetting);
        llLogout = hView.findViewById(R.id.llLogout);
        llBackupManagerView = hView.findViewById(R.id.llBackupManagerView);
        llSettingView = hView.findViewById(R.id.llSettingView);
        llTimeline = hView.findViewById(R.id.llTimeline);
        llMySubscription = hView.findViewById(R.id.llMySubscription);

        tvUsername = hView.findViewById(R.id.tvUsername);
        tvUsername.setText(Utility.getSavedData(FormActivity.this, Utility.LOGGED_FIRSTNAME));

        llSettingView.setOnClickListener(clickSlidebar);
        llBackupManagerView.setOnClickListener(clickSlidebar);
        llSetting.setOnClickListener(clickSlidebar);
        llBackupManager.setOnClickListener(clickSlidebar);
        llLogout.setOnClickListener(clickSlidebar);
        llTimeline.setOnClickListener(clickSlidebar);
        llMySubscription.setOnClickListener(clickSlidebar);
    }

//------------------------------------------------------- init Data ----------------------------------------------------------------------------------------------------------------------

    private void bindData() {
        formDTOS = new ArrayList<FormDTO>();
        dataBaseHelper.open();
        Cursor curForm = dataBaseHelper.executeCursor(DataBaseHelper.GET_FORM);
        if(curForm != null && curForm.getCount() > 0) {
            curForm.moveToFirst();
            for(int i=0;i<curForm.getCount();i++) {
                String form_id = curForm.getString(curForm.getColumnIndex("form_id"));
                String description = curForm.getString(curForm.getColumnIndex("description"));
                String project_id = curForm.getString(curForm.getColumnIndex("project_id"));
                formDTOS.add(new FormDTO(form_id, description, project_id));
                curForm.moveToNext();
            }
        }
        dataBaseHelper.close();
        setAdapter();
    }

//------------------------------------------------------- init Footer ----------------------------------------------------------------------------------------------------------------------

    private void initFooter() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.getMenu().getItem(0).setChecked(false);
        bottomNavigation.getMenu().getItem(0).setCheckable(false);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_project:
                        reDirectProject();
                        return true;
//                    case R.id.navigation_notification:
//                        return true;
                    case R.id.navigation_profile:
                        reDirectProfile();
                        return true;
                }
                return false;
            }
        });
    }

//------------------------------------------------------- on Slider Click ----------------------------------------------------------------------------------------------------------------------

    private View.OnClickListener clickSlidebar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int getid = v.getId();
            switch (getid) {
                case R.id.llBackupManager:
                    if(llBackupManagerView.getVisibility() == View.GONE) {
                        llBackupManagerView.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(llBackupManagerView.getVisibility() == View.VISIBLE) {
                        llBackupManagerView.setVisibility(View.GONE);
                        return;
                    }
                    break;
                case R.id.llBackupManagerView:
                    mDrawer.closeDrawer(GravityCompat.START);
                    reDirectBackupManger();
                    break;
                case R.id.llSetting:
                    if(llSettingView.getVisibility() == View.GONE) {
                        llSettingView.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(llSettingView.getVisibility() == View.VISIBLE) {
                        llSettingView.setVisibility(View.GONE);
                        return;
                    }
                    break;
                case R.id.llSettingView:
                    mDrawer.closeDrawer(GravityCompat.START);
                    reDirectSetting();
                    break;
                case R.id.llLogout:
                    mDrawer.closeDrawer(GravityCompat.START);
                    onClickLogout();
                    break;
                case R.id.llTimeline:
                    mDrawer.closeDrawer(GravityCompat.START);
                    redirectToTimeLine();
                    break;
                case R.id.llMySubscription:
                    mDrawer.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(FormActivity.this, MySubscription.class));
                    break;
            }
        }
    };

//------------------------------------------------------- on sET Adapter ----------------------------------------------------------------------------------------------------------------------

    private void setAdapter() {
        formAdapter = new FormAdapter(FormActivity.this, formDTOS);
        rvForm.setAdapter(formAdapter);
        rvForm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FormDTO dto = formDTOS.get(position);
                reDirectMap(dto);
                /*if(Utility.getSavedData(FormActivity.this, Utility.USER_TYPE).equalsIgnoreCase(Utility.USER_TYPE_GIS)) {
                    reDirectMap(dto);
                } else {
                    reDirectFormDetails(dto);
                }*/
            }
        });
    }

//------------------------------------------------------- on Create Options Menu ----------------------------------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard, menu);

        /*MenuItem ic_download = menu.findItem(R.id.ic_download);
        SpannableString s = new SpannableString(ic_download.getTitle().toString());
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
        ic_download.setTitle(s);
        ic_download.setVisible(false);*/

        MenuItem ic_sync = menu.findItem(R.id.ic_sync);
        SpannableString sic_sync = new SpannableString(ic_sync.getTitle().toString());
        sic_sync.setSpan(new ForegroundColorSpan(Color.BLACK), 0, sic_sync.length(), 0);
        ic_sync.setTitle(sic_sync);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();
        switch( itemID ){
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.ic_sync:
                progressDialog = new ProgressDialog(FormActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Synchronizing for data...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                listForms.clear();
                listForms = dataBaseHelper.getAllForms();
                listFormsDetails.clear();
                listVideoPaths.clear();
                listImagePaths.clear();
                for (FormDTO binForm : listForms) {
                    FormDetailModel binFormDataModel = dataBaseHelper.getFormData(binForm.getForm_id(), DataBaseHelper.isSyncOffline, DataBaseHelper.FORM_TYPE.TYPE_COMPLETE);
                    if (binFormDataModel != null && binFormDataModel.getData() != null && binFormDataModel.getData().size() > 0) {
                        Map<String, String> map = new HashMap<>();
                        Map<String, String> mapData = new HashMap<>();
                        map.put(DataBaseHelper.keyParam_UserId, Utility.getSavedData(FormActivity.this, Utility.LOGGED_USERID));
                        map.put(DataBaseHelper.keyParam_record_date, dataBaseHelper.getRecordDate());
                        map.put(DataBaseHelper.keyParam_formid, binForm.getForm_id());
                        map.put(keyParam_form_uid, "" + binFormDataModel.getUniqueFormId());
                        map.put(DataBaseHelper.keyParam_project_id, binForm.getProject_id());
                        map.put(DataBaseHelper.keyParam_appversion, BuildConfig.VERSION_NAME);
                        if (!Utility.isEmptyString(binFormDataModel.getLatitude()) &&
                                !Utility.isEmptyString(binFormDataModel.getLongitude())) {
                            map.put(DataBaseHelper.keyParam_latitude, binFormDataModel.getLatitude());
                            map.put(DataBaseHelper.keyParam_longitude, binFormDataModel.getLongitude());
                        }
                        for (int j = 0; j < binFormDataModel.getData().size(); j++) {
                            FormDetailData binFormDetailsData = binFormDataModel.getData().get(j);

                            if (binFormDetailsData.getType().equalsIgnoreCase(FormRenderAdapter.VIEW_TYPE_RADIO_GROUP_STR) ||
                                binFormDetailsData.getType().equalsIgnoreCase(FormRenderAdapter.VIEW_TYPE_SELECT_STR)) {

                                List<FormDetailData.FormDetailOption> listOptions = binFormDetailsData.getOptionsList();

                                for (int k = 0; k < listOptions.size(); k++) {
                                    FormDetailData.FormDetailOption binOption = listOptions.get(k);
                                    if (binOption.isSelected()) {
                                        String key = binFormDetailsData.getInput_id();
                                        String value = binOption.getValue();
                                        mapData.put(key, value);
                                        break;
                                    }
                                }
                            }
                            else if (binFormDetailsData.getType().equalsIgnoreCase(FormRenderAdapter.VIEW_TYPE_CHECKBOX_GROUP_STR)) {
                                List<FormDetailData.FormDetailOption> listOptions = binFormDetailsData.getOptionsList();
                                StringBuilder stringBuilder = new StringBuilder();
                                String key = "";
                                for (int k = 0; k < listOptions.size(); k++) {
                                    FormDetailData.FormDetailOption binOption = listOptions.get(k);
                                    if (binOption.isSelected()) {
                                        key = binFormDetailsData.getInput_id();
                                        String value = binOption.getValue();
                                        stringBuilder.append(value);
                                        stringBuilder.append(",");
                                    }
                                }
                                String values = stringBuilder.toString();
                                if (!Utility.isEmptyString(values) && !Utility.isEmptyString(key)) {
                                    String finalValues = validateIds(values);
                                    mapData.put(key, finalValues);
                                }
                            } else if (binFormDetailsData.getType().equalsIgnoreCase(FormRenderAdapter.VIEW_TYPE_VIDEO_STR)) {
                                String fileName = "";
                                if (!Utility.isEmptyString(binFormDetailsData.getValue()) && !binFormDetailsData.getValue().startsWith(VIDEO_PATH)) {
                                    listVideoPaths.add(binFormDetailsData.getValue());
                                    fileName = VIDEO_PATH + new File(binFormDetailsData.getValue()).getName();
                                }
                                mapData.put(binFormDetailsData.getInput_id(), fileName);
                            } else if (
                                    binFormDetailsData.getType().equalsIgnoreCase(FormRenderAdapter.VIEW_TYPE_FILE_STR) ||
                                    binFormDetailsData.getType().equalsIgnoreCase(FormRenderAdapter.VIEW_TYPE_AUDIO_STR)) {
                                String fileName = "";
                                if (!Utility.isEmptyString(binFormDetailsData.getValue()) && !binFormDetailsData.getValue().startsWith(IMAGE_PATH)) {
                                    listImagePaths.add(binFormDetailsData.getValue());
                                    fileName = IMAGE_PATH + new File(binFormDetailsData.getValue()).getName();
                                }
                                mapData.put(binFormDetailsData.getInput_id(), fileName);
                            } else {
                                mapData.put(binFormDetailsData.getInput_id(), binFormDetailsData.getValue());
                            }
                        }
                        map.put(DataBaseHelper.keyParam_data, new Gson().toJson(mapData));
                        listFormsDetails.add(map);
                    }
                }
                if (listFormsDetails.size() > 0) {
                    syncFormDetails();
                } else {
                    dismissMyDialog();
                    Utility.showInfoDialog(FormActivity.this, "Form details are upto date.");
                }
                return true;
        }
        return false;
    }

//------------------------------------------------------- Dismiss progressDialog Bar ----------------------------------------------------------------------------------------------------------------------

    private void dismissMyDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

//------------------------------------------------------- on Sync ----------------------------------------------------------------------------------------------------------------------


    private void syncFormDetails() {
        if (listFormsDetails != null && listFormsDetails.size() > 0) {
            binMapUploadParams = listFormsDetails.get(0);
            listFormsDetails.remove(0);
            uploadFormDetails(binMapUploadParams);
        } else {
            dismissMyDialog();
            prepareImageUploads();
        }
    }


//------------------------------------------------------- Upload ----------------------------------------------------------------------------------------------------------------------

    private void prepareImageUploads() {
        if (listImagePaths != null && listImagePaths.size() > 0) {
            String imgFile = listImagePaths.get(0);
            listImagePaths.remove(0);
            uploadImageFile(imgFile);
        } else {
            dismissMyDialog();
            prepareVideoFiles();
        }
    }

    private void prepareVideoFiles() {
        if (listVideoPaths != null && listVideoPaths.size() > 0) {
            String vidFile = listVideoPaths.get(0);
            listVideoPaths.remove(0);
            uploadVideoFiles(vidFile);
        } else {
            dismissMyDialog();
        }
    }

    private void uploadImageFile(String imgFilePath) {
        new ImageFileUploadToServer(mActivity, imgFilePath, new ImageFileUploadToServer.onImageUpload() {
            @Override
            public void getResult(boolean isSuccess) {
                prepareImageUploads();
            }
        }).execute();
    }

    private void uploadVideoFiles(String vidFile) {
        new VideoFileUploadToServe(mActivity, vidFile, new VideoFileUploadToServe.onVideoUpload() {
            @Override
            public void getResult(boolean isSuccess) {
                prepareVideoFiles();
            }
        }).execute();
    }

    private void uploadFormDetails(Map<String, String> params) {
        ResponseCode responseCode = ResponseCode.WS_SAVE;
        BaseApplication.getInstance().makeHttpPostRequest(mActivity, responseCode, URL_Utility.WS_SAVE, params, false, false);
    }

    private void downloadForm() {
        Map<String, String> params = new HashMap<String, String>();
        ResponseCode responseCode = ResponseCode.WS_FORMS;
        params.put(URL_Utility.PARAM_LOGIN_USER_ID, Utility.getSavedData(FormActivity.this, Utility.LOGGED_USERID));
        params.put(URL_Utility.PARAM_PROJECT_ID, project_id);
        params.put(URL_Utility.ACTION_APP_VERSION, URL_Utility.APP_VERSION);
        BaseApplication.getInstance().makeHttpPostRequest(FormActivity.this, responseCode, URL_Utility.WS_FORMS, params, false, false);
    }

//------------------------------------------------------- on Success Response ----------------------------------------------------------------------------------------------------------------------

    @Override
    public void onSuccessResponse(ResponseCode responseCode, String response) {
        if (responseCode == ResponseCode.WS_FORMS) {
            try {
                JSONObject mLoginObj = new JSONObject(response);
                String msg = mLoginObj.getString("msg");
                if(msg.equalsIgnoreCase("Success")) {
                    JSONArray dataArray = new JSONArray(mLoginObj.getString("data"));
                    if(dataArray.length() > 0) {
                        dataBaseHelper.open();
                        dataBaseHelper.executeQuery(DataBaseHelper.DELETE_TABLE_FORM);
                        for(int i=0;i<dataArray.length();i++) {
                            JSONObject mOArray = dataArray.getJSONObject(i);
                            String form_id = mOArray.getString("form_id");
                            String description = mOArray.getString("description");
                            String project_id = mOArray.getString("project_id");
                            dataBaseHelper.insertForm(form_id, description, project_id);
                        }
                        dataBaseHelper.close();
                    }
                    bindData();
                } else {
                    Utility.showInfoDialog(FormActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else if (responseCode == ResponseCode.WS_SAVE) {
            try {
                if (!Utility.isEmptyString(response))
                    Log.e("SAVE", response);
                JSONObject jsonObject = new JSONObject(response);
                String msg = jsonObject.optString("msg");
                if (msg.equalsIgnoreCase("Success")) {
                    String uniq_id = jsonObject.optString(keyParam_form_uid);
                    dataBaseHelper.deleteFormDetails(uniq_id);
                    Utility.showToast(mActivity, msg, null);
                    syncFormDetails();
                } else {
                    syncFormDetails();
                    Utility.showInfoDialog(mActivity, msg);
                }
            } catch (JSONException e) {
                dismissMyDialog();
            }
        }
    }

//------------------------------------------------------- on Error Response ----------------------------------------------------------------------------------------------------------------------

    @Override
    public void onErrorResponse(ResponseCode responseCode, VolleyError error) {
        Utility.showInfoDialog(FormActivity.this, error.getMessage());
    }

//------------------------------------------------------- on Activity Result ----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1003 && resultCode ==RESULT_OK)
        {}
    }


    private String getDateDDMMYYYY() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

//------------------------------------------------------- on Logout ----------------------------------------------------------------------------------------------------------------------

    private void onClickLogout() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FormActivity.this);
        alertDialog.setMessage(getResources().getString(R.string.lblAre_you_sure));
        alertDialog.setPositiveButton(getResources().getString(R.string.lblLogout), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                processLogout();
            }
        });
        alertDialog.setNegativeButton(getResources().getString(R.string.lblCancel), null);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void processLogout() {
        Utility.clearData(FormActivity.this);
        dataBaseHelper.open();
        dataBaseHelper.logout();
        dataBaseHelper.close();
        baseApplication.stopMyService();
        FormActivity.this.finish();
        finishAffinity();
        startActivity(new Intent(this, SplashActivity.class));
    }

//------------------------------------------------------- ReDirect ----------------------------------------------------------------------------------------------------------------------

    private void reDirectProject() {
        Intent intent = new Intent(FormActivity.this, ProjectActivity.class);
        startActivity(intent);
        finish();
    }

    private void reDirectFormDetails(FormDTO dto) {
        Intent intent = new Intent(FormActivity.this, FormDetailsActivity.class);
        intent.putExtra(Utility.PASS_FORM_ID, dto.getForm_id());
        intent.putExtra(Utility.PASS_PROJECT_ID, project_id);
        intent.putExtra(Utility.PASS_FORM_NM, dto.getDescription());
        final int random = new Random().nextInt(61) + 20;
        intent.putExtra(Utility.PASS_GEOM_ID, ""+random);
        startActivityForResult(intent, 1003);
    }

    private void reDirectMap(FormDTO dto) {
        Intent intent = new Intent(FormActivity.this, MapsActivity.class);
        intent.putExtra(Utility.PASS_FORM_ID, dto.getForm_id());
        intent.putExtra(Utility.PASS_PROJECT_ID, project_id);
        intent.putExtra(Utility.PASS_FORM_NM, dto.getDescription());
        startActivity(intent);
    }

    private void redirectToTimeLine() {
        Intent intent = new Intent(FormActivity.this, TimeLineMapsActivity.class);
        startActivity(intent);
    }

    private void reDirectBackupManger() {
        Intent intent = new Intent(FormActivity.this, BackupManagerActivity.class);
        startActivity(intent);
    }

    private void reDirectSetting() {
        Intent intent = new Intent(FormActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    private void reDirectProfile() {
        Intent intent = new Intent(FormActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

//------------------------------------------------------- on Resume ----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onResume() {
        super.onResume();
        SystemUtility.setFullscreenToggle(this, Utility.getBooleanSavedData(this, Utility.IS_FULL_SCREEN));
        SystemUtility.setKeepScreenAwakeAlwaysToggle(this, Utility.getBooleanSavedData(this, Utility.IS_ALWAYS_KEEP_SCREEN_AWAKE));
    }

//------------------------------------------------------- on Back Press ----------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        reDirectProject();
    }


}
