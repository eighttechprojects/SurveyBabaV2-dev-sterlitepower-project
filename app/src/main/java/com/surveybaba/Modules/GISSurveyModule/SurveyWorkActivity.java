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
import com.surveybaba.ADAPTER.AdapterProjectWork;
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

public class SurveyWorkActivity extends AppCompatActivity implements AdapterProjectWork.AdapterProjectWorkListener,LocationAssistant.Listener{

    // TAG
    private static final String TAG = "SurveyWorkActivity";
    // Recycle View
    RecyclerView surveyWorkRecycleView;
    // Project Work List
    ArrayList<ProjectModule> surveyWorkList = new ArrayList<>();
    // Database helper
    private DataBaseHelper dataBaseHelper;
    // Location
    private LocationAssistant assistant;


//------------------------------------------------------- onCreate --------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_work);


        // Add Back Button on ToolBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Init
        init();
        initDatabase();
        // Location
        assistant = new LocationAssistant(this, this, LocationAssistant.Accuracy.HIGH, 0, false);
        assistant.setVerbose(true);
        // Base Application
        BaseApplication baseApplication = (BaseApplication) getApplication();

        SystemPermission systemPermission = new SystemPermission(this);
        if(systemPermission.isLocation()){
            baseApplication.startMyService();
        }
        // Dynamic Project Work
        DynamicSurveyWork();
        Utility.saveData(this,Utility.UNIT_AREA_DATA, "");
        Utility.saveData(this,Utility.UNIT_DISTANCE_DATA, "");
    }


//------------------------------------------------------- Init --------------------------------------------------------------------------------------------------------------------------

    private void init() { surveyWorkRecycleView = findViewById(R.id.surveyWorkRecycleView);}

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

    private void DynamicSurveyWork() {

        String survey_id = getIntent().getStringExtra("survey_id");
        String survey_name = getIntent().getStringExtra("survey_name");
        Utility.saveData(this,Utility.GIS_SURVEY_ID,survey_id);
        Utility.saveData(this,Utility.GIS_SURVEY_NAME,survey_name);
        ArrayList<ProjectModule> project = dataBaseHelper.getGISSurveyWorkArray(survey_id);
        if (surveyWorkList.size() != 0) {
            surveyWorkList.clear();
        }
        if(project.size() > 0){
            try {
                surveyWorkList.addAll(project);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        surveyWorkList.sort(Comparator.comparing(ProjectModule::getName));

        AdapterProjectWork adapterProjectWork = new AdapterProjectWork(this,surveyWorkList,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        surveyWorkRecycleView.setLayoutManager(gridLayoutManager);
        surveyWorkRecycleView.setAdapter(adapterProjectWork);
    }

//------------------------------------------------------- On Item Adapter Project Activity Click  --------------------------------------------------------------------------------------------------------------------------

    @Override
    public void OnItemClick(int position) {
        ProjectModule projectModule = surveyWorkList.get(position);
        Log.e(TAG, "work_id: "+ projectModule.getId());
        Utility.saveData(this,Utility.GIS_SURVEY_WORK_NAME,projectModule.getName());
        reDirectToMapActivity(projectModule.getId());
    }

//------------------------------------------------------- reDirect  --------------------------------------------------------------------------------------------------------------------------

    private void reDirectToMapActivity(String work_id){
        Intent intent = new Intent(this, GISSurveyActivity.class);
        intent.putExtra("work_id",work_id);
        Utility.saveData(this,Utility.GIS_SURVEY_WORK_ID,work_id);
        startActivity(intent);
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