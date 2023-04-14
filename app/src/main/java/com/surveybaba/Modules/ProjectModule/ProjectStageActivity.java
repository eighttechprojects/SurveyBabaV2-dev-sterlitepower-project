package com.surveybaba.Modules.ProjectModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.surveybaba.ADAPTER.AdapterProjectStage;
import com.surveybaba.Database.DataBaseHelper;
import com.surveybaba.R;
import com.surveybaba.Utilities.SystemPermission;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.ProjectModule;
import com.volly.BaseApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Objects;

public class ProjectStageActivity extends AppCompatActivity implements AdapterProjectStage.AdapterProjectStageListener{
    // TAG
    private static final String TAG = "ProjectStageActivity";
    // Recycle View
    RecyclerView projectStageRecycleView;
    // Project Assign List
    ArrayList<ProjectModule> projectStageList = new ArrayList<>();
    // Database helper
    private DataBaseHelper dataBaseHelper;
    // Progress Dialog
    private ProgressDialog progressDialog;
//------------------------------------------------------- On Create  --------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_stage);
        // Add Back Button on ToolBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Init
        init();
        initDatabase();
        // Dynamic Project Stage Work
        DynamicProjectStageWork();


    }

//------------------------------------------------------- Init --------------------------------------------------------------------------------------------------------------------------

    private void init() {
        projectStageRecycleView = findViewById(R.id.projectStageRecycleView);
    }

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

    private void DynamicProjectStageWork() {

//        String project_id = getIntent().getStringExtra("project_id");
//        String project = dataBaseHelper.getProjectUser();
//        if (projectStageList.size() != 0) {
//            projectStageList.clear();
//        }
//        if(!project.equals("")){
//            try {
//                JSONObject mObj = new JSONObject(project);
//                JSONArray projectStageArray   = new JSONArray(mObj.getString("stages"));
//                for(int i=0; i<projectStageArray.length(); i++){
//                    String stage_project_id = projectStageArray.getJSONObject(i).getString("project_id");
//                    if(stage_project_id.equals(project_id)){
//                        String stage_id = projectStageArray.getJSONObject(i).getString("stage_id");
//                        String stage_name = projectStageArray.getJSONObject(i).getString("stage_name");
//                        projectStageList.add(new ProjectModule(stage_id,stage_name));
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        AdapterProjectStage adapterProjectStatus = new AdapterProjectStage(this,projectStageList,this);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
//        projectStageRecycleView.setLayoutManager(gridLayoutManager);
//        projectStageRecycleView.setAdapter(adapterProjectStatus);
    }

//------------------------------------------------------- On Item Adapter Project Assign Stage Click  --------------------------------------------------------------------------------------------------------------------------
    @Override
    public void OnItemClick(int position) {
//        ProjectModule projectModule = projectStageList.get(position);
//       // Toast.makeText(this, ""+projectModule.getName(), Toast.LENGTH_SHORT).show();
//        String project = dataBaseHelper.getProjectUser();
//        boolean isHas = false;
//        if(!project.equals("")){
//            try {
//                showProgressDialog();
//                JSONObject mObj = new JSONObject(project);
//                JSONArray projectActivityArray   = new JSONArray(mObj.getString("activities"));
//                if(projectActivityArray.length() > 0){
//                    for(int i=0; i<projectActivityArray.length(); i++){
//                        String activity_stage_id = projectActivityArray.getJSONObject(i).getString("stage_id");
//                        // it means we have stage for this project
//                        if(activity_stage_id.equals(projectModule.getId())){
//                            isHas = true;
//                            break;
//                        }
//                    }
//                }
//                if(isHas){
//                    dismissProgressDialog();
//                    reDirectToProjectActivity(projectModule.getId());
//                }
//                else{
//                    dismissProgressDialog();
//                    projectNotAssign();
//                }
//
//            } catch (JSONException e) {
//                dismissProgressDialog();
//                e.printStackTrace();
//            }
//        }
    }

//------------------------------------------------------- reDirect  --------------------------------------------------------------------------------------------------------------------------

    private void reDirectToProjectActivity(String stage_id){
        Intent intent = new Intent(this,ProjectActivityActivity.class);
        intent.putExtra("stage_id",stage_id);
        startActivity(intent);
    }

//------------------------------------------------------- DialogBox  --------------------------------------------------------------------------------------------------------------------------

    private void projectNotAssign(){
        Utility.getOKDialogBox(this, "Alert", "Activity is not Assign", DialogInterface::dismiss);
    }

//------------------------------------------------------- On Back Press  --------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        finish();
    }

//---------------------------------------------------------- Progress Bar ------------------------------------------------------------

    private void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Project Stage...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        } else {
            progressDialog.setMessage("Project Stage...");
        }
    }

}