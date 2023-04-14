package com.surveybaba;

import android.app.Activity;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.surveybaba.ADAPTER.AdapterLayerList;
import com.surveybaba.Database.DataBaseHelper;
import com.surveybaba.LocationAssist.LocationAssistant;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.LayerModel;
import com.surveybaba.model.ProjectLayerModel;
import com.volly.BaseApplication;
import java.util.ArrayList;
import java.util.Objects;


public class LayersActivity extends AppCompatActivity implements LocationAssistant.Listener {

    // Activity
    private Activity mActivity;
    // Database
    private DataBaseHelper dataBaseHelper;
    // base Application
    BaseApplication baseApplication;
    // Location
    private LocationAssistant assistant;
    // Boolean
    private boolean isDataChanged;
    // Recycler View
    private RecyclerView rvLayers;
    // Adapter
    AdapterLayerList adapter;
    // Linear Layout
    LinearLayoutManager layoutManager;
    // ArrayList
    private ArrayList<ProjectLayerModel> listLayers = new ArrayList<>();

//------------------------------------------------------------ on Create ---------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layers);
        // Tool bar
        Objects.requireNonNull(getSupportActionBar()).setTitle("Layers");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // baseApplication
        baseApplication = (BaseApplication) getApplication();
        // Location
        assistant = new LocationAssistant(this, this, LocationAssistant.Accuracy.HIGH, 0, true);
        assistant.setVerbose(true);
        // Activity
        mActivity = this;
        // Database
        initDatabase();
        // init
        init();
        // bind Data
        bindData();
    }


//------------------------------------------------------------ initDatabase ---------------------------------------------------------------------------------------------------------------------

    private void initDatabase() {
        dataBaseHelper = new DataBaseHelper(LayersActivity.this);
    }

//------------------------------------------------------------ init ---------------------------------------------------------------------------------------------------------------------

    private void init() {
        rvLayers = findViewById(R.id.rvLayers);
    }

//------------------------------------------------------------ Bind Data ---------------------------------------------------------------------------------------------------------------------

    private void bindData() {
        listLayers = new ArrayList<>();
        listLayers = dataBaseHelper.getProjectLayersList(Utility.getSavedData(this, Utility.PROJECT_WORK_ID), false);
        setAdapter();
    }

//------------------------------------------------------------ Set Adapter ---------------------------------------------------------------------------------------------------------------------

    private void setAdapter() {
        layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvLayers.setLayoutManager(layoutManager);
        ArrayList<LayerModel> layerModels = new ArrayList<>();
        layerModels.add(new LayerModel(Utility.VIEW_TYPE_ASSET_LAYER_STR,listLayers));
        layerModels.add(new LayerModel(Utility.VIEW_TYPE_BASE_ONLINE_LAYER_STR,listLayers));

        adapter = new AdapterLayerList(LayersActivity.this, layerModels, () -> isDataChanged = true);
        rvLayers.setAdapter(adapter);
    }

//------------------------------------------------------------ Menu ---------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            if (isDataChanged && listLayers.size() > 0) {
                dataBaseHelper.updateProjectLayersData(listLayers);
                setResult(RESULT_OK);
            }
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


//------------------------------------------------------------ Location ---------------------------------------------------------------------------------------------------------------------

    @Override
    public void onNeedLocationPermission() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.app_name);
        dialogBuilder.setMessage("Need\nPermission");
        dialogBuilder.setPositiveButton("Ok", (dialog, which) -> {
            dialog.dismiss();
            assistant.requestLocationPermission();
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
        new AlertDialog.Builder(this).setMessage(R.string.switchOnLocationShort).setPositiveButton(R.string.lbl_Ok, (dialog, which) -> {
            dialog.dismiss();
            assistant.changeLocationSettings();
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
    }

    @Override
    public void onMockLocationsDetected(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

    }

    @Override
    public void onError(LocationAssistant.ErrorType type, String message) {

    }


//------------------------------------------------------------ On Resume ---------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onResume() {
        super.onResume();
        assistant.start();
    }

//------------------------------------------------------------ On Pause ---------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onPause() {
        super.onPause();
        assistant.stop();
    }

//------------------------------------------------------------ on Destroy ---------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//------------------------------------------------------------ On back Press ---------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        if(isDataChanged && listLayers.size()>0) {
            dataBaseHelper.updateProjectLayersData(listLayers);
            setResult(RESULT_OK);
        }
        finish();
    }

}
