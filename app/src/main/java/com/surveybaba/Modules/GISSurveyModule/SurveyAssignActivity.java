package com.surveybaba.Modules.GISSurveyModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.surveybaba.ADAPTER.AdapterProjectAssign;
import com.surveybaba.Database.DataBaseHelper;
import com.surveybaba.LocationAssist.LocationAssistant;
import com.surveybaba.R;
import com.surveybaba.Utilities.SystemPermission;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.ProjectModule;
import com.volly.BaseApplication;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class SurveyAssignActivity extends AppCompatActivity implements AdapterProjectAssign.AdapterProjectAssignListener,LocationAssistant.Listener {

    // TAG
    private static final String TAG = "SurveyAssignActivity";
    // Recycle View
    private RecyclerView surveyAssignRecycleView;
    // Project Assign List
    private final ArrayList<ProjectModule> surveyAssignList = new ArrayList<>();
    // Database helper
    private DataBaseHelper dataBaseHelper;
    // Location
    private LocationAssistant assistant;

//------------------------------------------------------- On Create --------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_assign);
        // Add Back Button on ToolBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Location
        assistant = new LocationAssistant(this, this, LocationAssistant.Accuracy.HIGH, 0, false);
        assistant.setVerbose(true);
        // Init
        init();
        initDatabase();
        // Dynamic Project Assign Work
        DynamicSurveyAssignWork();
        // Permission
        SystemPermission systemPermission = new SystemPermission(this);
        // base Application
        BaseApplication baseApplication = (BaseApplication) getApplication();
        if(systemPermission.isLocation()){
            baseApplication.startMyService();
        }
    }

    //------------------------------------------------------- Init --------------------------------------------------------------------------------------------------------------------------

    private void init() {
        surveyAssignRecycleView = findViewById(R.id.surveyAssignRecycleView);
    }

//---------------------------------------------------------- Init Database --------------------------------------------------------------------------------------------------------------------------

    private void initDatabase() {
        dataBaseHelper = new DataBaseHelper(SurveyAssignActivity.this);
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

    private void DynamicSurveyAssignWork() {

        ArrayList<ProjectModule> project = dataBaseHelper.getGISSurveyArray();
        Log.e(TAG, ""+project.size());
        if(surveyAssignList.size() != 0) {
            surveyAssignList.clear();
        }
        if(project.size() > 0){
            try {
                 surveyAssignList.addAll(project);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        surveyAssignList.sort(Comparator.comparing(ProjectModule::getName));

        AdapterProjectAssign adapterProjectAssign = new AdapterProjectAssign(this, surveyAssignList, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        surveyAssignRecycleView.setLayoutManager(gridLayoutManager);
        surveyAssignRecycleView.setAdapter(adapterProjectAssign);
    }

//------------------------------------------------------- On Item Adapter Project Assign Click  --------------------------------------------------------------------------------------------------------------------------

    @Override
    public void OnItemClick(int position) {
        ProjectModule projectModule = surveyAssignList.get(position);
        ArrayList<ProjectModule> project = dataBaseHelper.getGISSurveyWorkArray(projectModule.getId());
        Log.e(TAG, "Survey Id : " + projectModule.getId());
        if(project.size() > 0){
            reDirectToSurveyWork(projectModule.getId(),projectModule.getName());
        }
        else{
            projectNotAssign();
        }
    }

//------------------------------------------------------- reDirect  --------------------------------------------------------------------------------------------------------------------------

    private void reDirectToSurveyWork(String project_id,String project_name){
        Intent intent = new Intent(this, SurveyWorkActivity.class);
        intent.putExtra("survey_id",project_id);
        intent.putExtra("survey_name",project_name);
        startActivity(intent);
    }

//------------------------------------------------------- DialogBox  --------------------------------------------------------------------------------------------------------------------------

    private void projectNotAssign(){
        Utility.getOKDialogBox(this, "Work is not Assigned", DialogInterface::dismiss);
    }

//------------------------------------------------------- On Back Press  --------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        finish();
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



}