package com.surveybaba.Modules.ProjectModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.surveybaba.ADAPTER.AdapterProjectActivity;
import com.surveybaba.Database.DataBaseHelper;
import com.surveybaba.LocationAssist.LocationAssistant;
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

public class ProjectActivityActivity extends AppCompatActivity implements AdapterProjectActivity.AdapterProjectActivityListener,LocationAssistant.Listener{

    // TAG
    private static final String TAG = "ProjectActivityActivity";
    // Recycle View
    RecyclerView projectActivityRecycleView;
    // Project Activity List
    ArrayList<ProjectModule> projectActivityList = new ArrayList<>();
    // Database helper
    private DataBaseHelper dataBaseHelper;
    // Progress Dialog
    private ProgressDialog progressDialog;
    // Base Application
    private BaseApplication baseApplication;
    // Location
    private LocationAssistant assistant;

//------------------------------------------------------- onCreate --------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_activity);
        // Add Back Button on ToolBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Init
        init();
        initDatabase();
        // Location
        assistant = new LocationAssistant(this, this, LocationAssistant.Accuracy.HIGH, 0, false);
        assistant.setVerbose(true);

        // Dynamic Project Activity Work
        DynamicProjectActivityWork();
        // base Application
        baseApplication = (BaseApplication) getApplication();

        SystemPermission systemPermission = new SystemPermission(this);
        if(systemPermission.isLocation()){
            baseApplication.startMyService();
        }
    }

//------------------------------------------------------- Init --------------------------------------------------------------------------------------------------------------------------

    private void init() { projectActivityRecycleView = findViewById(R.id.projectActivityRecycleView);}

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

    private void DynamicProjectActivityWork() {
//
//        String stage_id = getIntent().getStringExtra("stage_id");
//        String project = dataBaseHelper.getProjectUser();
//        if (projectActivityList.size() != 0) {
//            projectActivityList.clear();
//        }
//        if(!project.equals("")){
//            try {
//                JSONObject mObj = new JSONObject(project);
//                JSONArray projectActivityArray   = new JSONArray(mObj.getString("activities"));
//                for(int i=0; i<projectActivityArray.length(); i++){
//                    String activity_stage_id = projectActivityArray.getJSONObject(i).getString("stage_id");
//                    if(activity_stage_id.equals(stage_id)){
//                        String activity_id = projectActivityArray.getJSONObject(i).getString("activity_id");
//                        String activity_name = projectActivityArray.getJSONObject(i).getString("activity_name");
//                        projectActivityList.add(new ProjectModule(activity_id,activity_name));
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        AdapterProjectActivity adapterProjectActivity = new AdapterProjectActivity(this,projectActivityList,this);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
//        projectActivityRecycleView.setLayoutManager(gridLayoutManager);
//        projectActivityRecycleView.setAdapter(adapterProjectActivity);
    }

//------------------------------------------------------- On Item Adapter Project Activity Click  --------------------------------------------------------------------------------------------------------------------------

    @Override
    public void OnItemClick(int position) {
//        ProjectModule projectModule = projectActivityList.get(position);
//        //Toast.makeText(this, ""+projectModule.getName(), Toast.LENGTH_SHORT).show();
//        String project = dataBaseHelper.getProjectUser();
//        boolean isHas = false;
//        if(!project.equals("")){
//            try {
//                showProgressDialog();
//                JSONObject mObj = new JSONObject(project);
//                JSONArray projectWorkArray   = new JSONArray(mObj.getString("works"));
//                if(projectWorkArray.length() > 0){
//                    for(int i=0; i<projectWorkArray.length(); i++){
//                        String work_activity_id = projectWorkArray.getJSONObject(i).getString("activity_id");
//                        // it means we have stage for this project
//                        if(work_activity_id.equals(projectModule.getId())){
//                            isHas = true;
//                            break;
//                        }
//                    }
//                }
//                if(isHas){
//                    dismissProgressDialog();
//                    reDirectToWorkActivity(projectModule.getId());
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

    private void reDirectToWorkActivity(String activity_id){
        Intent intent = new Intent(this,ProjectWorkActivity.class);
        intent.putExtra("activity_id",activity_id);
        startActivity(intent);
    }

//------------------------------------------------------- DialogBox  --------------------------------------------------------------------------------------------------------------------------

    private void projectNotAssign(){
        Utility.getOKDialogBox(this, "Alert", "Work is not Assign", DialogInterface::dismiss);
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
            progressDialog.setMessage("Project Activity...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        } else {
            progressDialog.setMessage("Project Activity...");
        }
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