package com.surveybaba.Modules.GISSurveyModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.surveybaba.ADAPTER.AdapterDashboard;
import com.surveybaba.Database.DataBaseHelper;
import com.surveybaba.LocationAssist.LocationAssistant;
import com.surveybaba.Modules.ProjectModule.ProjectAssignActivity;
import com.surveybaba.R;
import com.surveybaba.Utilities.SystemPermission;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.DashBoardModule;
import com.surveybaba.model.ProjectModule;
import com.volly.BaseApplication;

import java.util.ArrayList;
import java.util.Objects;

public class SurveyResurveyActivity extends AppCompatActivity implements AdapterDashboard.AdapterDashBoardListener, LocationAssistant.Listener{

    // TAG
    private static final String TAG = "SurveyResurveyActivity";
    // Activity
    Activity mActivity;
    // Recycle View
    private RecyclerView surveyResurveyRecycleView;
    // Survey Resurvey  List
    private final ArrayList<DashBoardModule> surveyResurveyList = new ArrayList<>();
    // Location
    private LocationAssistant assistant;
    // Database helper
    private DataBaseHelper dataBaseHelper;
//------------------------------------------------------- On Create --------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_resurvey);

        // Activity
        mActivity = this;
        // Add Back Button on ToolBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Location
        assistant = new LocationAssistant(this, this, LocationAssistant.Accuracy.HIGH, 0, false);
        assistant.setVerbose(true);
        // Init
        init();
        // initDatabase
        initDatabase();
        // Dynamic Project Resurvey
        DynamicSurveyResurvey();

        // base Application
        BaseApplication baseApplication = (BaseApplication) getApplication();
        SystemPermission systemPermission = new SystemPermission(this);
        if(systemPermission.isLocation()){
            baseApplication.startMyService();
        }

    }

//------------------------------------------------------- Init --------------------------------------------------------------------------------------------------------------------------

    private void init() { surveyResurveyRecycleView = findViewById(R.id.surveyResurveyRecycleView); }

//---------------------------------------------------------- Init Database --------------------------------------------------------------------------------------------------------------------------

    private void initDatabase() {
        dataBaseHelper = new DataBaseHelper(this);
    }

//------------------------------------------------------- Menu --------------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }


//------------------------------------------------------- RecycleView --------------------------------------------------------------------------------------------------------------------------

    private void DynamicSurveyResurvey() {

        // Default Module Add!
        surveyResurveyList.add(new DashBoardModule(AdapterDashboard.GIS_SURVEY_LIST   , AdapterDashboard.GIS_SURVEY_LIST));
        surveyResurveyList.add(new DashBoardModule(AdapterDashboard.RESURVEY     , AdapterDashboard.RESURVEY));
        AdapterDashboard adapterDashboard = new AdapterDashboard(mActivity, surveyResurveyList, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 2);
        surveyResurveyRecycleView.setLayoutManager(gridLayoutManager);
        surveyResurveyRecycleView.setAdapter(adapterDashboard);

    }

//------------------------------------------------------- On Item Adapter Project Assign Click  --------------------------------------------------------------------------------------------------------------------------

    @Override
    public void OnItemClick(int position) {

        String module = surveyResurveyList.get(position).getModule();

        switch (module) {

            case AdapterDashboard.GIS_SURVEY_LIST:
                reDirectToGISSurveyAssign();
                break;

            case AdapterDashboard.RESURVEY:
                reDirectToGISResurvey();
                break;
        }
    }

//------------------------------------------------------- reDirect  --------------------------------------------------------------------------------------------------------------------------

    private void reDirectToGISSurveyAssign(){
        ArrayList<ProjectModule> project = dataBaseHelper.getGISSurveyArray();

        if(project.size() > 0){
            startActivity(new Intent(mActivity, SurveyAssignActivity.class));
        }
        else{
            surveyNotAssign();
        }
    }

//------------------------------------------------------- DialogBox  --------------------------------------------------------------------------------------------------------------------------

    private void surveyNotAssign(){
        Utility.getOKDialogBox(this,  "Survey is not Assigned", DialogInterface::dismiss);
    }

    private void reDirectToGISResurvey(){
       // startActivity(new Intent(mActivity,GISResurveyActivity.class));
    }


//------------------------------------------------------- Location ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onNeedLocationPermission() {

    }

    @Override
    public void onExplainLocationPermission() {

    }

    @Override
    public void onLocationPermissionPermanentlyDeclined(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

    }

    @Override
    public void onNeedLocationSettingsChange() {

    }

    @Override
    public void onFallBackToSystemSettings(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

    }

    @Override
    public void onNewLocationAvailable(Location location) {

    }

    @Override
    public void onMockLocationsDetected(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

    }

    @Override
    public void onError(LocationAssistant.ErrorType type, String message) {

    }

//------------------------------------------------------- on Resume ---------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onResume() {
        super.onResume();
        assistant.start();

    }

//------------------------------------------------------- on pause ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onPause() {
        assistant.stop();
        super.onPause();
    }

//------------------------------------------------------- On Back Press  --------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        finish();
    }


}