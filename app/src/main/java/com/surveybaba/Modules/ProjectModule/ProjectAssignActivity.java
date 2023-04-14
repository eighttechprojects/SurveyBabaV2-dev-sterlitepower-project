package com.surveybaba.Modules.ProjectModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.surveybaba.ADAPTER.AdapterProjectAssign;
import com.surveybaba.Database.DataBaseHelper;
import com.surveybaba.R;
import com.surveybaba.Utilities.SystemPermission;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.ProjectModule;
import com.volly.BaseApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class ProjectAssignActivity extends AppCompatActivity implements AdapterProjectAssign.AdapterProjectAssignListener {

    // TAG
    private static final String TAG = "ProjectAssignActivity";
    // Recycle View
    private RecyclerView projectAssignRecycleView;
    // Project Assign List
    private final ArrayList<ProjectModule> projectAssignList = new ArrayList<>();
    // Database helper
    private DataBaseHelper dataBaseHelper;

//------------------------------------------------------- On Create --------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_assign);
        // Add Back Button on ToolBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Init
        init();
        initDatabase();
        // Dynamic Project Assign Work
        DynamicProjectAssignWork();

        // System Permission
        SystemPermission systemPermission = new SystemPermission(this);
        BaseApplication baseApplication = (BaseApplication) getApplication();
        if(systemPermission.isLocation()){
            baseApplication.startMyService();
        }

    }

//------------------------------------------------------- Init --------------------------------------------------------------------------------------------------------------------------

    private void init() {
        projectAssignRecycleView = findViewById(R.id.projectAssignRecycleView);
    }

//---------------------------------------------------------- Init Database --------------------------------------------------------------------------------------------------------------------------

    private void initDatabase() {
        dataBaseHelper = new DataBaseHelper(ProjectAssignActivity.this);
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

    private void DynamicProjectAssignWork() {

        ArrayList<ProjectModule> projects = dataBaseHelper.getProjectArray();

        if(projectAssignList.size() != 0) {
            projectAssignList.clear();
        }
        if(projects.size() > 0){
            projectAssignList.addAll(projects);
        }

        projectAssignList.sort(Comparator.comparing(ProjectModule::getName));

        AdapterProjectAssign adapterProjectAssign = new AdapterProjectAssign(this, projectAssignList, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        projectAssignRecycleView.setLayoutManager(gridLayoutManager);
        projectAssignRecycleView.setAdapter(adapterProjectAssign);
    }

//------------------------------------------------------- On Item Adapter Project Assign Click  --------------------------------------------------------------------------------------------------------------------------

    @Override
    public void OnItemClick(int position) {
        ProjectModule projectModule = projectAssignList.get(position);
        ArrayList<ProjectModule> project = dataBaseHelper.getProjectWorkArray(projectModule.getId());
        Log.e(TAG, "project id: "+ projectModule.getId());
        if(project.size() > 0){
            reDirectToProjectWork(projectModule.getId(),projectModule.getName());
        }
        else{
            projectNotAssign();
        }
    }

//------------------------------------------------------- reDirect  --------------------------------------------------------------------------------------------------------------------------

    private void reDirectToProjectWork(String project_id,String project_name){
        Intent intent = new Intent(this,ProjectWorkActivity.class);
        intent.putExtra("project_id",project_id);
        intent.putExtra("project_name",project_name);
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


}