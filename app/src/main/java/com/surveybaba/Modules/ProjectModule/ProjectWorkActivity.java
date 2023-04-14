package com.surveybaba.Modules.ProjectModule;

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
import com.surveybaba.MapsActivity;
import com.surveybaba.R;
import com.surveybaba.Utilities.SystemPermission;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.ProjectModule;
import com.volly.BaseApplication;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class ProjectWorkActivity extends AppCompatActivity implements AdapterProjectWork.AdapterProjectWorkListener,LocationAssistant.Listener {

    // TAG
    private static final String TAG = "ProjectWorkActivity";
    // Recycle View
    RecyclerView projectWorkRecycleView;
    // Project Work List
    ArrayList<ProjectModule> projectWorkList = new ArrayList<>();
    // Database helper
    private DataBaseHelper dataBaseHelper;
    // Location
    private LocationAssistant assistant;

//------------------------------------------------------- onCreate --------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_work);

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
        DynamicProjectWork();

        Utility.saveData(this,Utility.UNIT_AREA_DATA,"");
        Utility.saveData(this,Utility.UNIT_DISTANCE_DATA, "");
    }

//------------------------------------------------------- Init --------------------------------------------------------------------------------------------------------------------------

    private void init() { projectWorkRecycleView = findViewById(R.id.projectWorkRecycleView);}

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

    private void DynamicProjectWork() {

        String project_id = getIntent().getStringExtra("project_id");
        String project_name = getIntent().getStringExtra("project_name");
        Utility.saveData(this,Utility.PROJECT_PROJECT_ID,project_id);
        Utility.saveData(this,Utility.PROJECT_PROJECT_NAME,project_name);
        ArrayList<ProjectModule> project = dataBaseHelper.getProjectWorkArray(project_id);
        if (projectWorkList.size() != 0) {
            projectWorkList.clear();
        }
        if(project.size() > 0){
            try {
                    projectWorkList.addAll(project);
                }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        projectWorkList.sort(Comparator.comparing(ProjectModule::getName));

        AdapterProjectWork adapterProjectWork = new AdapterProjectWork(this,projectWorkList,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        projectWorkRecycleView.setLayoutManager(gridLayoutManager);
        projectWorkRecycleView.setAdapter(adapterProjectWork);
    }

//------------------------------------------------------- On Item Adapter Project Activity Click  --------------------------------------------------------------------------------------------------------------------------

    @Override
    public void OnItemClick(int position) {
        ProjectModule projectModule = projectWorkList.get(position);
        Log.e(TAG,"work_id: "+ projectModule.getId());
        Utility.saveData(this,Utility.PROJECT_WORK_NAME,projectModule.getName());
        reDirectToMapActivity(projectModule.getId());
    }

//------------------------------------------------------- reDirect  --------------------------------------------------------------------------------------------------------------------------

    private void reDirectToMapActivity(String work_id){
        Intent intent = new Intent(this,MapsActivity.class);
        intent.putExtra("work_id",work_id);
        Utility.saveData(this,Utility.PROJECT_WORK_ID,work_id);
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
        Log.e(TAG,"Project Work Activity Resume");
    }

//------------------------------------------------------- on pause ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onPause() {
        assistant.stop();
        super.onPause();
        Log.e(TAG,"Project Work Activity Pause");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"Project Work Activity Destroy");
    }
}