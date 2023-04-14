package com.surveybaba;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.BuildConfig;
import com.android.volley.VolleyError;
import com.fileupload.AndroidMultiPartEntity;
import com.fileupload.ImageFileUploadToServer;
import com.fileupload.VideoFileUploadToServe;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.surveybaba.ADAPTER.FormRenderAdapter;
import com.surveybaba.ADAPTER.ProjectAdapter;
import com.surveybaba.DTO.FormDTO;
import com.surveybaba.DTO.Geom;
import com.surveybaba.DTO.ImagSyncVo;
import com.surveybaba.DTO.ProjectDTO;
import com.surveybaba.Database.DataBaseHelper;
import com.surveybaba.FormBuilder.FormDetailData;
import com.surveybaba.FormBuilder.FormDetailModel;
import com.surveybaba.LocationAssist.LocationAssistant;
import com.surveybaba.Utilities.MyBroadcastReceiver;
import com.surveybaba.Utilities.SystemUtility;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.backup.BackupManagerActivity;
import com.surveybaba.model.BinLayerProject;
import com.surveybaba.service.ForgroundLocationService;
import com.surveybaba.setting.MySubscription;
import com.surveybaba.setting.SettingActivity;
import com.volly.BaseApplication;
import com.volly.URL_Utility;
import com.volly.URL_Utility.ResponseCode;
import com.volly.WSResponseInterface;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.surveybaba.Database.DataBaseHelper.keyParam_data;
import static com.surveybaba.Database.DataBaseHelper.keyParam_form_uid;
import static com.surveybaba.Utilities.Utility.PROJECT_ID_DEFAULT;
import static com.surveybaba.Utilities.Utility.validateIds;
import static com.volly.URL_Utility.IMAGE_PATH;
import static com.volly.URL_Utility.VIDEO_PATH;

public class ProjectActivity extends AppCompatActivity implements WSResponseInterface, LocationAssistant.Listener {

    private ProgressDialog progressDialog;
    private long totalSize = 0;
    private GridView rvProject;
    private DataBaseHelper dataBaseHelper;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvView;
    private ActionBarDrawerToggle drawerToggle;
    private LinearLayout llTimeline, llBackupManager, llMySubscription, llSetting, llLogout, llBackupManagerView, llSettingView;
    private TextView tvUsername;
    MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();
    BaseApplication baseApplication;
    private LocationAssistant assistant;
    private Location mCurrentLocation;
    private boolean isServiceStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        baseApplication = (BaseApplication) getApplication();
        // Location--------------------------------------------------
        assistant = new LocationAssistant(this, this, LocationAssistant.Accuracy.HIGH, 0, true);
        assistant.setVerbose(true);
        initDatabase();
        initSlidePanel();
        init();
        bindData();
        initFooter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Utility.getBooleanSavedData(this, Utility.IS_USER_TRACKING)) {
            try {
                unregisterReceiver(myBroadcastReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initDatabase() {
        dataBaseHelper = new DataBaseHelper(ProjectActivity.this);
    }

    private void init() {
        rvProject = findViewById(R.id.rvProject);
    }

    private void initSlidePanel() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nvView = (NavigationView) findViewById(R.id.nvView);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        initSliebar();
    }

    private void initSliebar() {
        View hView = nvView.getHeaderView(0);
        llTimeline = hView.findViewById(R.id.llTimeline);
        llBackupManager = hView.findViewById(R.id.llBackupManager);
        llMySubscription = hView.findViewById(R.id.llMySubscription);
        llSetting = hView.findViewById(R.id.llSetting);
        tvUsername = hView.findViewById(R.id.tvUsername);
        llLogout = hView.findViewById(R.id.llLogout);
        llBackupManagerView = hView.findViewById(R.id.llBackupManagerView);
        llSettingView = hView.findViewById(R.id.llSettingView);
        tvUsername.setText(Utility.getSavedData(ProjectActivity.this, Utility.LOGGED_FIRSTNAME));

        llSettingView.setOnClickListener(clickSlidebar);
        llBackupManagerView.setOnClickListener(clickSlidebar);
        llSetting.setOnClickListener(clickSlidebar);
        llBackupManager.setOnClickListener(clickSlidebar);
        llMySubscription.setOnClickListener(clickSlidebar);
        llLogout.setOnClickListener(clickSlidebar);
        llTimeline.setOnClickListener(clickSlidebar);
    }

    private View.OnClickListener clickSlidebar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int getid = v.getId();
            switch (getid) {
                case R.id.llBackupManager:
                    if (llBackupManagerView.getVisibility() == View.GONE) {
                        llBackupManagerView.setVisibility(View.VISIBLE);
                        return;
                    }
                    if (llBackupManagerView.getVisibility() == View.VISIBLE) {
                        llBackupManagerView.setVisibility(View.GONE);
                        return;
                    }
                    break;
                case R.id.llBackupManagerView:
                    mDrawer.closeDrawer(GravityCompat.START);
                    reDirectBackupManger();
                    break;
                case R.id.llSetting:
                    if (llSettingView.getVisibility() == View.GONE) {
                        llSettingView.setVisibility(View.VISIBLE);
                        return;
                    }
                    if (llSettingView.getVisibility() == View.VISIBLE) {
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
                    startActivity(new Intent(ProjectActivity.this, MySubscription.class));
                    break;
            }
        }
    };

    private void redirectToTimeLine() {
        Intent intent = new Intent(ProjectActivity.this, TimeLineMapsActivity.class);
        startActivity(intent);
    }

    private void reDirectBackupManger() {
        Intent intent = new Intent(ProjectActivity.this, BackupManagerActivity.class);
        startActivity(intent);
    }

    private void reDirectSetting() {
        Intent intent = new Intent(ProjectActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    private ArrayList<ProjectDTO> projectDTOS;

    private void bindData() {
        projectDTOS = new ArrayList<ProjectDTO>();
        dataBaseHelper.open();
        Cursor curProject = dataBaseHelper.executeCursor(DataBaseHelper.GET_PROJECT);
        if (curProject != null && curProject.getCount() > 0) {
            curProject.moveToFirst();
            for (int i = 0; i < curProject.getCount(); i++) {
                String id = curProject.getString(curProject.getColumnIndex("id"));
                String project = curProject.getString(curProject.getColumnIndex("project"));
                String latitude = curProject.getString(curProject.getColumnIndex("latitude"));
                String longitude = curProject.getString(curProject.getColumnIndex("longitude"));
                if(!Utility.isEmptyString(id) && !id.equalsIgnoreCase(PROJECT_ID_DEFAULT))
                    projectDTOS.add(new ProjectDTO(id, project, latitude, longitude));
                curProject.moveToNext();
            }
        }
        dataBaseHelper.close();
        setAdapter();
    }

    private ProjectAdapter projectAdapter;

    private void setAdapter() {
        projectAdapter = new ProjectAdapter(ProjectActivity.this, projectDTOS);
        rvProject.setAdapter(projectAdapter);
        rvProject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProjectDTO dto = projectDTOS.get(position);
                Intent intent = new Intent();
                intent.putExtra(keyParam_data, new Gson().toJson(dto));
                setResult(RESULT_OK, intent);
                finish();
//                onClickForm(dto);
            }
        });
    }

    private void onClickForm(ProjectDTO dto) {
        Intent intent = new Intent(ProjectActivity.this, FormActivity.class);
        intent.putExtra(Utility.PASS_PROJECT_ID, dto.getId());
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard, menu);

        /*MenuItem ic_download = menu.findItem(R.id.ic_download);
        SpannableString s = new SpannableString(ic_download.getTitle().toString());
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
        ic_download.setTitle(s);*/

        MenuItem ic_sync = menu.findItem(R.id.ic_sync);
        SpannableString sic_sync = new SpannableString(ic_sync.getTitle().toString());
        sic_sync.setSpan(new ForegroundColorSpan(Color.BLACK), 0, sic_sync.length(), 0);
        ic_sync.setTitle(sic_sync);

        return true;
    }

    ArrayList<FormDTO> listForms = new ArrayList<>();
    ArrayList<String> listVideoPaths = new ArrayList<>();
    ArrayList<String> listImagePaths = new ArrayList<>();
    ArrayList<Map<String, String>> listFormsDetails = new ArrayList<>();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            /*case R.id.ic_download:
                downloadProject();
                return true;*/
            case R.id.ic_sync:
                progressDialog = new ProgressDialog(ProjectActivity.this);
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
                    if (binFormDataModel != null &&
                            binFormDataModel.getData() != null &&
                            binFormDataModel.getData().size() > 0) {
                        Map<String, String> map = new HashMap<>();
                        Map<String, String> mapData = new HashMap<>();
                        map.put(DataBaseHelper.keyParam_UserId, Utility.getSavedData(ProjectActivity.this, Utility.LOGGED_USERID));
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
                            if (binFormDetailsData.getType().equalsIgnoreCase(FormRenderAdapter.VIEW_TYPE_RADIO_GROUP_STR) || binFormDetailsData.getType().equalsIgnoreCase(FormRenderAdapter.VIEW_TYPE_SELECT_STR)) {
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
                            } else if (binFormDetailsData.getType().equalsIgnoreCase(FormRenderAdapter.VIEW_TYPE_CHECKBOX_GROUP_STR)) {
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
                    Utility.showInfoDialog(ProjectActivity.this, "Form details are upto date.");
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    Map<String, String> binMapUploadParams = new HashMap<>();

    private void syncFormDetails() {
        if (listFormsDetails != null && listFormsDetails.size() > 0) {
            binMapUploadParams = listFormsDetails.get(0);
            listFormsDetails.remove(0);
            uploadFormDetails(binMapUploadParams);
        }
        else {
            dismissMyDialog();
            prepareImageUploads();
        }
    }

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
        new ImageFileUploadToServer(ProjectActivity.this, imgFilePath, new ImageFileUploadToServer.onImageUpload() {
            @Override
            public void getResult(boolean isSuccess) {
                prepareImageUploads();
            }
        }).execute();
    }

    private void uploadVideoFiles(String vidFile) {
        new VideoFileUploadToServe(ProjectActivity.this, vidFile, new VideoFileUploadToServe.onVideoUpload() {
            @Override
            public void getResult(boolean isSuccess) {
                prepareVideoFiles();
            }
        }).execute();
    }

    private void uploadFormDetails(Map<String, String> params) {
        ResponseCode responseCode = ResponseCode.WS_SAVE;
        BaseApplication.getInstance().makeHttpPostRequest(ProjectActivity.this, responseCode, URL_Utility.WS_SAVE, params, false, false);
    }

    private void onClickLogout() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProjectActivity.this);
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
        Utility.clearData(ProjectActivity.this);
        dataBaseHelper.open();
        dataBaseHelper.logout();
        dataBaseHelper.close();
        baseApplication.stopMyService();
        ProjectActivity.this.finish();
        finishAffinity();
        startActivity(new Intent(this, SplashActivity.class));
    }

    private BottomNavigationView bottomNavigation;

    private void initFooter() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.getMenu().getItem(0).setChecked(false);
        bottomNavigation.getMenu().getItem(0).setCheckable(false);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_project:
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

    private void downloadProject() {
        Map<String, String> params = new HashMap<String, String>();
        ResponseCode responseCode = ResponseCode.WS_PROJECT;
        params.put(URL_Utility.PARAM_LOGIN_USER_ID, Utility.getSavedData(ProjectActivity.this, Utility.LOGGED_USERID));
        params.put(URL_Utility.ACTION_APP_VERSION, URL_Utility.APP_VERSION);
        BaseApplication.getInstance().makeHttpPostRequest(ProjectActivity.this, responseCode, URL_Utility.WS_PROJECT, params, false, false);
    }

    @Override
    public void onSuccessResponse(ResponseCode responseCode, String response) {
        if (responseCode == ResponseCode.WS_PROJECT) {
            try {
                JSONObject mLoginObj = new JSONObject(response);
                Log.e("Download", response);
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
                            ArrayList<BinLayerProject> listLayers = new ArrayList<>();
                            if(arrLayers!=null)
                            {
                                for(int j=0; j<arrLayers.length(); j++) {
                                    JSONObject jObjLayer = arrLayers.optJSONObject(j);
                                    if (jObjLayer != null) {
                                        String layerName = jObjLayer.optString("name");
                                        String layerUrl = jObjLayer.optString("layer_url");
                                        String layer_type = jObjLayer.optString("layer_type");
                                        String layer_display = jObjLayer.optString("layer_display");
                                        BinLayerProject binLayerProject = new BinLayerProject();
                                        binLayerProject.setName(layerName);
                                        try {
                                            binLayerProject.setUrl(URLDecoder.decode(layerUrl, "utf-8"));
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        binLayerProject.setType(layer_type);
                                        binLayerProject.setDisplay(layer_display);
                                        listLayers.add(binLayerProject);
                                    }

                                }
                            }
                            dataBaseHelper.insertProject(id, project, latitude, longitude, new Gson().toJson(listLayers));
                        }
                        dataBaseHelper.close();
                    }
                    bindData();
                } else {
                    Utility.showInfoDialog(ProjectActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (responseCode == ResponseCode.WS_SAVE) {
            try {
                if (!Utility.isEmptyString(response))
                    Log.e("SAVE", response);
                JSONObject jsonObject = new JSONObject(response);
                String msg = jsonObject.optString("msg");
                if (msg.equalsIgnoreCase("Success")) {
                    String uniq_id = jsonObject.optString(keyParam_form_uid);
                    dataBaseHelper.deleteFormDetails(uniq_id);
                    Utility.showToast(ProjectActivity.this, msg, null);
                    syncFormDetails();
                } else {
                    syncFormDetails();
                    Utility.showInfoDialog(ProjectActivity.this, msg);
                }
            } catch (JSONException e) {
                dismissMyDialog();
            }
        }
    }

    private void dismissMyDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void onErrorResponse(ResponseCode responseCode, VolleyError error) {
        dismissMyDialog();
        Utility.showInfoDialog(ProjectActivity.this, error.getMessage());
    }

    private ArrayList<Geom> arraySave;
    private ArrayList<ImagSyncVo> imageUploadPath;

    /*private void processToSynch() {
        arraySave = new ArrayList<Geom>();
        imageUploadPath = new ArrayList<ImagSyncVo>();
        dataBaseHelper.open();
        Cursor curData = dataBaseHelper.executeCursor(dataBaseHelper.getSync(0));
        if (curData != null && curData.getCount() > 0) {
            curData.moveToFirst();
            for (int i = 0; i < curData.getCount(); i++) {
                String gid = curData.getString(curData.getColumnIndex("gid"));
                String project_id = curData.getString(curData.getColumnIndex("project_id"));
                String form_id = curData.getString(curData.getColumnIndex("form_id"));
                String geom_type = curData.getString(curData.getColumnIndex("geom_type"));
                String geom_array = curData.getString(curData.getColumnIndex("geom_array"));
                String latitude = curData.getString(curData.getColumnIndex("latitude"));
                String longitude = curData.getString(curData.getColumnIndex("longitude"));
                String accuracy = curData.getString(curData.getColumnIndex("accuracy"));
                String record_date = curData.getString(curData.getColumnIndex("record_date"));
                String viewData = curData.getString(curData.getColumnIndex("viewData"));
                String syncData = curData.getString(curData.getColumnIndex("syncData"));
                String IS_SYNC = curData.getString(curData.getColumnIndex("IS_SYNC"));
                Geom vo = new Geom(gid, project_id, form_id, geom_type, geom_array, latitude, longitude, accuracy, record_date, viewData, syncData, IS_SYNC);
                arraySave.add(vo);

                imageUploadPath = new ArrayList<ImagSyncVo>();
                Cursor curImag = dataBaseHelper.executeCursor(dataBaseHelper.getImageDetails(gid));
                if (curImag != null && curImag.getCount() > 0) {
                    curImag.moveToFirst();
                    for (int j = 0; j < curImag.getCount(); j++) {
                        String imgpath = curImag.getString(curImag.getColumnIndex("imgpath"));
                        imageUploadPath.add(new ImagSyncVo(gid, imgpath));
                        curImag.moveToNext();
                    }
                }

                curData.moveToNext();
            }
        }
        curData.close();
        dataBaseHelper.close();
        if (arraySave != null && arraySave.size() == 0) {
            showSuccessfullDialog();
            return;
        }
        if (imageUploadPath.size() > 0) {
            imageUpload();
        } else {
            synchData();
        }
    }*/

    private void showSuccessfullDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(ProjectActivity.this);
        dialog.setMessage(getResources().getString(R.string.lbl_Data_Send_Successfully));
        dialog.setPositiveButton(getResources().getString(R.string.lbl_Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface subdialog, int which) {
                subdialog.cancel();
                // Intent i = new Intent(ProjectActivity.this, FormActivity.class);
                // finish();
                // overridePendingTransition(0, 0);
                // startActivity(i);
                // overridePendingTransition(0, 0);
            }
        });
        dialog.setCancelable(false);
        dialog.show();
        return;
    }

    private ArrayList<String> arrayListTestingSych;

    /*private void imageUpload() {
        arrayListTestingSych = new ArrayList<String>();
        if (imageUploadPath.size() > 0) {
            progressDialog = new ProgressDialog(ProjectActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getResources().getString(R.string.lbl_File_upload));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.show();
            for (int m = 0; m < imageUploadPath.size(); m++) {
                ImagSyncVo vo = imageUploadPath.get(m);
                new FileUploadToServe(vo).execute();
            }
        }
    }*/

    @Override
    public void onNeedLocationPermission() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.app_name);
        dialogBuilder.setMessage("Need\nPermission");
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                assistant.requestLocationPermission();
            }
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
        }).setCancelable(true).show();
    }

    @Override
    public void onFallBackToSystemSettings(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.switchOnLocationLong)
                .setPositiveButton(R.string.lbl_Ok, fromDialog)
                .setCancelable(true)
                .show();
    }

    @Override
    public void onNewLocationAvailable(Location location) {
        if (location == null) return;
        mCurrentLocation = location;
        if (!isServiceStarted) {
            isServiceStarted = true;
            if (Utility.getBooleanSavedData(this, Utility.IS_USER_TRACKING)) {
                registerReceiver(myBroadcastReceiver, ForgroundLocationService.getLocationActions());
                baseApplication.startMyService();
            }
        }
    }

    @Override
    public void onMockLocationsDetected(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

    }

    @Override
    public void onError(LocationAssistant.ErrorType type, String message) {

    }

    /*private class FileUploadToServe extends AsyncTask<Void, Integer, String> {

        ImagSyncVo imgVO;

        public FileUploadToServe(ImagSyncVo imgVO) {
            this.imgVO = imgVO;
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
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL_Utility.BASE_IMAGE_URL);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                });
                File sourceFile = new File(imgVO.getImagepath());
                entity.addPart(URL_Utility.PARAM_IMAGE, new FileBody(sourceFile));
                entity.addPart(URL_Utility.PARAM_UNIQ_ID, new StringBody(imgVO.getG_id()));
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: " + statusCode;
                }
            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            arrayListTestingSych.add(result);
            if (imageUploadPath.size() == arrayListTestingSych.size()) {
                progressDialog.dismiss();
                synchData();
            }
            super.onPostExecute(result);
        }
    }*/

    /*private void synchData() {
        if (arraySave != null && arraySave.size() > 0) {
            Geom vo = arraySave.get(0);
            processToSyncFormData(vo.getProject_id(), vo.getForm_id(), vo.getGid(), vo.getSyncData(), vo.getLatitude(), vo.getLongitude(), vo.getAccuracy(), vo.getRecord_date());
        }
    }*/

    /*private void processToSyncFormData(String project_id, String form_id, String uniq_id, String jsonRequest, String latitude, String longitude, String accuracy, String record_date) {
        Map<String, String> params = new HashMap<String, String>();
        ResponseCode responseCode = ResponseCode.WS_SAVE;
        params.put(URL_Utility.PARAM_UNIQ_ID, uniq_id);
//        params.put(URL_Utility.PARAM_DATA, "["+jsonRequest+"]");
        params.put(URL_Utility.PARAM_DATA, jsonRequest);
        params.put(URL_Utility.PARAM_LOGIN_USER_ID, Utility.getSavedData(ProjectActivity.this, Utility.LOGGED_USERID));
        params.put(URL_Utility.PARAM_PROJECT_ID, project_id);
        params.put(URL_Utility.PARAM_FORM_ID, form_id);
//        params.put(URL_Utility.PARAM_LATITUDE, latitude);
//        params.put(URL_Utility.PARAM_LONGITUDE, longitude);
//        params.put(URL_Utility.PARAM_ACCURACY, accuracy);
        params.put(URL_Utility.PARAM_RECORD_DATE, record_date);
        params.put(URL_Utility.ACTION_APP_VERSION, URL_Utility.APP_VERSION);
        BaseApplication.getInstance().makeHttpPostRequest(ProjectActivity.this, responseCode, URL_Utility.WS_SAVE, params, false, false);
    }*/

    /*private void updateSyncAfter(String unique_id) {
        dataBaseHelper.open();
        dataBaseHelper.executeQuery(dataBaseHelper.updateSync(1, unique_id));
        dataBaseHelper.executeQuery(dataBaseHelper.deleteImageDetails(unique_id));
        dataBaseHelper.close();
    }*/

    private void reDirectProfile() {
        Intent intent = new Intent(ProjectActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemUtility.setFullscreenToggle(this, Utility.getBooleanSavedData(this, Utility.IS_FULL_SCREEN));
        SystemUtility.setKeepScreenAwakeAlwaysToggle(this, Utility.getBooleanSavedData(this, Utility.IS_ALWAYS_KEEP_SCREEN_AWAKE));
        assistant.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        assistant.stop();
        dismissRevokeDialog();
    }

    AlertDialog alertDialog;

    public void revokeLocation() {
        Log.e("ProjectActivity", "Received revokeLocation");
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

}
