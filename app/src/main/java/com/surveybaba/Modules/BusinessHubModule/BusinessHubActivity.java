package com.surveybaba.Modules.BusinessHubModule;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.surveybaba.ADAPTER.AdapterBusinessHub;
import com.surveybaba.LocationAssist.LocationAssistant;
import com.surveybaba.R;
import com.surveybaba.Utilities.SystemPermission;
import com.volly.BaseApplication;
import java.util.ArrayList;
import java.util.Objects;

public class BusinessHubActivity extends AppCompatActivity implements AdapterBusinessHub.AdapterBusinessHubListener,LocationAssistant.Listener{

    // TAG
    private static final String TAG = "BusinessHubActivity";
    // Recycle View
    RecyclerView businessHubRecycleView;
    // Project Activity List
    ArrayList<String> businessHubRecycleViewArrayList = new ArrayList<>();
    // Location
    private LocationAssistant assistant;

//------------------------------------------------------- onCreate --------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_hub);
        // Add Back Button on ToolBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Init
        init();
        // Location
        assistant = new LocationAssistant(this, this, LocationAssistant.Accuracy.HIGH, 0, false);
        assistant.setVerbose(true);
        // Dynamic Project Activity Work
        DynamicProjectActivityWork();
        // base Application
        // Base Application
        BaseApplication baseApplication = (BaseApplication) getApplication();

        SystemPermission systemPermission = new SystemPermission(this);
        if(systemPermission.isLocation()){
            baseApplication.startMyService();
        }
    }

//------------------------------------------------------- Init --------------------------------------------------------------------------------------------------------------------------

    private void init() { businessHubRecycleView = findViewById(R.id.businessHubRecycleView);}

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

        if (businessHubRecycleViewArrayList.size() != 0) {
            businessHubRecycleViewArrayList.clear();
        }
        businessHubRecycleViewArrayList.add(AdapterBusinessHub.SEARCH_SURVEYORS);
        businessHubRecycleViewArrayList.add(AdapterBusinessHub.SEARCH_SURVEYING_AGENCY);
        businessHubRecycleViewArrayList.add(AdapterBusinessHub.SEARCH_SURVEY_WORK);
        AdapterBusinessHub adapterBusinessHub = new AdapterBusinessHub(this,businessHubRecycleViewArrayList,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        businessHubRecycleView.setLayoutManager(gridLayoutManager);
        businessHubRecycleView.setAdapter(adapterBusinessHub);
    }


//------------------------------------------------------- On Item Adapter Business Hub Click  --------------------------------------------------------------------------------------------------------------------------

    @Override
    public void OnItemClick(int position) {
        String businessHub = businessHubRecycleViewArrayList.get(position);

        switch (businessHub){

            case AdapterBusinessHub.SEARCH_SURVEYORS:
                startActivity(new Intent(this,SearchSurveyorsActivity.class));
                break;

            case AdapterBusinessHub.SEARCH_SURVEYING_AGENCY:
                startActivity(new Intent(this,SearchSurveyingAgencyActivity.class));
                break;

            case AdapterBusinessHub.SEARCH_SURVEY_WORK:
                startActivity(new Intent(this,SearchSurveyWorkActivity.class));
                break;

        }

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