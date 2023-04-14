package com.surveybaba.Modules.ProjectModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.surveybaba.ADAPTER.AdapterDashboard;
import com.surveybaba.ADAPTER.AdapterProjectAssign;
import com.surveybaba.Database.DataBaseHelper;
import com.surveybaba.R;
import com.surveybaba.Utilities.SystemPermission;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.DashBoardModule;
import com.surveybaba.model.ProjectModule;
import com.volly.BaseApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class ProjectResurveyActivity extends AppCompatActivity implements AdapterDashboard.AdapterDashBoardListener{

    // TAG
    private static final String TAG = "ProjectResurveyActivity";
    // Activity
    Activity mActivity;
    // Recycle View
    private RecyclerView projectResurveyRecycleView;
    // Project Resurvey  List
    private final ArrayList<DashBoardModule> projectResurveyList = new ArrayList<>();
    // Database helper
    private DataBaseHelper dataBaseHelper;
//------------------------------------------------------- On Create --------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_resurvey);
        // Activity
        mActivity = this;
        // Add Back Button on ToolBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Init
        init();
        // InitDatabase
        initDatabase();
        // Dynamic Project Resurvey
        DynamicProjectResurvey();
        // System Permission
        SystemPermission systemPermission = new SystemPermission(this);
        BaseApplication baseApplication = (BaseApplication) getApplication();
        if(systemPermission.isLocation()){
            baseApplication.startMyService();
        }

    }

//------------------------------------------------------- Init --------------------------------------------------------------------------------------------------------------------------

    private void init() { projectResurveyRecycleView = findViewById(R.id.projectResurveyRecycleView); }

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

    private void DynamicProjectResurvey() {

        // Default Module Add!
        projectResurveyList.add(new DashBoardModule(AdapterDashboard.PROJECT_LIST   , AdapterDashboard.PROJECT_LIST));
        projectResurveyList.add(new DashBoardModule(AdapterDashboard.RESURVEY     , AdapterDashboard.RESURVEY));
        AdapterDashboard adapterDashboard = new AdapterDashboard(mActivity, projectResurveyList, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 2);
        projectResurveyRecycleView.setLayoutManager(gridLayoutManager);
        projectResurveyRecycleView.setAdapter(adapterDashboard);

    }

//------------------------------------------------------- On Item Adapter Project Assign Click  --------------------------------------------------------------------------------------------------------------------------

    @Override
    public void OnItemClick(int position) {

        String module = projectResurveyList.get(position).getModule();

        switch (module) {

            case AdapterDashboard.PROJECT_LIST:
                reDirectToProjectAssign();
                break;

            case AdapterDashboard.RESURVEY:
                reDirectToResurvey();
                break;
        }
    }

//------------------------------------------------------- reDirect  --------------------------------------------------------------------------------------------------------------------------

    private void reDirectToProjectAssign(){
        ArrayList<ProjectModule> projects = dataBaseHelper.getProjectArray();
        if(projects.size() > 0){
            startActivity(new Intent(mActivity, ProjectAssignActivity.class));
        }
        else{
            projectNotAssign();
        }
    }
//------------------------------------------------------- DialogBox  --------------------------------------------------------------------------------------------------------------------------

    private void projectNotAssign(){
        Utility.getOKDialogBox(this,  "Project is not Assigned", DialogInterface::dismiss);
    }

    private void reDirectToResurvey(){
       // startActivity(new Intent(mActivity,PResurveyActivity.class));
    }

//------------------------------------------------------- On Back Press  --------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        finish();
    }



}