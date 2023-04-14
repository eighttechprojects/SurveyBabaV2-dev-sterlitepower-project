package com.surveybaba.Modules.BusinessHubModule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.surveybaba.ADAPTER.AdapterSearchSurveyWork;
import com.surveybaba.AESCrypt.AESCrypt;
import com.surveybaba.R;
import com.surveybaba.Utilities.SystemUtility;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.UserSearchWorkModel;
import com.volly.BaseApplication;
import com.volly.URL_Utility;
import com.volly.WSResponseInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SearchSurveyWorkRVActivity extends AppCompatActivity implements WSResponseInterface {

    // TAG
    private static final String TAG = "SurveyWorkRVActivity";
    // Activity
    Activity mActivity;
    // Progress Dialog
    private ProgressDialog progressDialog;
    // RecyclerView
    RecyclerView rvSurveyWork;
    // Text View
    TextView txtNoDataFound;

//------------------------------------------------------- onCreate --------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_survey_work_rvactivity);
        // Add Back Button on ToolBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mActivity = this;

        rvSurveyWork = findViewById(R.id.rvSurveyWork);
        txtNoDataFound = findViewById(R.id.txtNoDataFound);

        // Internet Connected or Not
        if(!SystemUtility.isInternetConnected(this)){
            Utility.getOKDialogBox(mActivity, "Internet Issue", "Need Internet Connection", DialogInterface::dismiss);
        }
        Intent intent = getIntent();
        String stateID = intent.getStringExtra("stateID");
        String cityID  = intent.getStringExtra("cityID");
        String zipCode = intent.getStringExtra("zipCode");
        if(SystemUtility.isInternetConnected(mActivity)){
            FetchDataToServer(stateID,cityID,zipCode);
        }
    }

//------------------------------------------------------- RecycleView --------------------------------------------------------------------------------------------------------------------------

    private void addToRecycleView(ArrayList<UserSearchWorkModel> list){
        AdapterSearchSurveyWork adapterDashboard = new AdapterSearchSurveyWork(mActivity, list);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        rvSurveyWork.setLayoutManager(linearLayout);
        rvSurveyWork.setAdapter(adapterDashboard);
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

//------------------------------------------------------- Progress Bar --------------------------------------------------------------------------------------------------------------------------

    private void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(msg);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        } else {
            progressDialog.setMessage(msg);
        }
    }


//------------------------------------------------------- Fetch Data Form Server  --------------------------------------------------------------------------------------------------------------------------

    private void FetchDataToServer(String stateID, String cityID,String zipCode){
        showProgressDialog("Loading...");
        Map<String, String> params = new HashMap<>();
        String data = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(URL_Utility.PARAM_STATE,     stateID);
            jsonObject.put(URL_Utility.PARAM_CITY,      cityID);
            jsonObject.put(URL_Utility.PARAM_ZIP_CODER, zipCode);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            data =  AESCrypt.encrypt(jsonObject.toString());
            Log.e(TAG,data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.put("data",data);
        URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_SEARCH_SURVEY_WORK;
        BaseApplication.getInstance().makeHttpPostRequest(mActivity, responseCode, URL_Utility.WS_SEARCH_SURVEY_WORK, params, false, false);
    }

//------------------------------------------------------- onSuccessResponse  --------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onSuccessResponse(URL_Utility.ResponseCode responseCode, String response) {

        // State ------------------
        if (responseCode == URL_Utility.ResponseCode.WS_SEARCH_SURVEY_WORK) {
            String res = AESCrypt.decryptResponse(response);
            if(!res.equals("")) {
                try {
                    JSONObject mObj = new JSONObject(res);
                    String status = mObj.optString("status");
                    Log.e(TAG,"Response: "+ status);
                    Log.e(TAG, res);
                    if (status.equalsIgnoreCase("Success")) {

                        JSONArray jsonArray = new JSONArray(mObj.getString("data"));
                        if(jsonArray.length() > 0){
                            ArrayList<UserSearchWorkModel> list = new ArrayList<>();
                            for(int i=0; i<jsonArray.length(); i++){
                                String workName        = jsonArray.getJSONObject(i).getString("work_name");
                                String userDescription = jsonArray.getJSONObject(i).getString("work_desc");
                                String userLocation    = jsonArray.getJSONObject(i).getString("location");
                                String userPostedBy    = jsonArray.getJSONObject(i).getString("fullname");
                                String userDate        = jsonArray.getJSONObject(i).getString("datetime");
                                list.add(new UserSearchWorkModel(workName,userLocation,userDescription,userPostedBy,userDate));
                            }
                            addToRecycleView(list);
                            showNoDataFound(false);
                        }
                        else{
                            showNoDataFound(true);
                        }
                        dismissProgressDialog();
                    }
                    else{
                        JSONArray jsonArray = new JSONArray(mObj.getString("data"));
                        showNoDataFound(jsonArray.length() <= 0);
                        dismissProgressDialog();
                        dismissProgressDialog();
                    }
                }
                catch (JSONException e) {
                    showNoDataFound(true);
                    dismissProgressDialog();
                    Log.e(TAG,e.getMessage());
                }
            }
            else{
                showNoDataFound(true);
                dismissProgressDialog();
                Log.e(TAG,res);
            }
        }

    }

//------------------------------------------------------- onErrorResponse  --------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onErrorResponse(URL_Utility.ResponseCode responseCode, VolleyError error) {
        showNoDataFound(true);
        dismissProgressDialog();
    }

//------------------------------------------------------- On Back Press  --------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        finish();
    }

    private void showNoDataFound(boolean isVisible){
        txtNoDataFound.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
}