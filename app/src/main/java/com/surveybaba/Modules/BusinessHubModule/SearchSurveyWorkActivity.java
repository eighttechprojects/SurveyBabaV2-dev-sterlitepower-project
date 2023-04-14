package com.surveybaba.Modules.BusinessHubModule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.surveybaba.ADAPTER.AdapterSearchSurveyWorkD;
import com.surveybaba.AESCrypt.AESCrypt;
import com.surveybaba.FormBuilder.FormDetailData;
import com.surveybaba.R;
import com.surveybaba.Utilities.ImageFileUtils;
import com.surveybaba.Utilities.SystemPermission;
import com.surveybaba.Utilities.SystemUtility;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.StateModel;
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

public class SearchSurveyWorkActivity extends AppCompatActivity implements View.OnClickListener, WSResponseInterface {

    // TAG
    private static final String TAG = "SurveyWorkActivity";
    // Activity
    Activity mActivity;
    // Progress Dialog
    private ProgressDialog progressDialog;
    // Button
    Button searchButton;
    // EditText
    private EditText edZipCode;
    // Spinner
    private com.toptoche.searchablespinnerlibrary.SearchableSpinner spState;
    private com.toptoche.searchablespinnerlibrary.SearchableSpinner spCity;
    // RecycleView
    RecyclerView recyclerViewSurveyWork;
    // String
    private String stateSelected = "";
    private String citySelected  = "";
    private String stateID = "";
    private String cityID  = "";
    // ArrayList
    private ArrayList<StateModel> stateModelArrayList = new ArrayList<>();
    private ArrayList<StateModel> cityModelArrayList = new ArrayList<>();
    // Utility
    private Utility utility;
    // Image File
    private ImageFileUtils imageFileUtils;
    // Permission
    private SystemPermission systemPermission;
    // Adapter
    private AdapterSearchSurveyWorkD formRenderAdapter;
    private ArrayList<FormDetailData> listFormDetailsData = new ArrayList<>();

    //------------------------------------------------------- onCreate --------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_survey_work);
        // Add Back Button on ToolBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mActivity = this;
        utility = new Utility(this);
        imageFileUtils = new ImageFileUtils();
        systemPermission = new SystemPermission(this);

        // init
        init();
        // Internet Connected or Not
        if(!SystemUtility.isInternetConnected(this)){
            Utility.getOKDialogBox(mActivity, "Internet Issue", "Need Internet Connection", DialogInterface::dismiss);
        }

        if(SystemUtility.isInternetConnected(mActivity)){
            processToState();
        }

        spinnerState();
        spinnerCity();

        //getOpenForm(getDemoData());

    }

    private String getDemoData(){
        String res = "[{\"column_name\":\"date-26508274-1117\",\"type\":\"date\",\"required\":\"f\",\"label\":\"Inservice Date\",\"name\":\"Inservice Date\",\"multiple\":\"f\",\"selvalues\":null,\"subtype\":null},{\"column_name\":\"date-75996977-8301\",\"type\":\"date\",\"required\":\"f\",\"label\":\"Install Date\",\"name\":\"Install Date\",\"multiple\":\"f\",\"selvalues\":null,\"subtype\":null},{\"column_name\":\"radio-group-93205714-7534\",\"type\":\"radio-group\",\"required\":\"f\",\"label\":\"Life Cycle Status\",\"name\":\"Life Cycle Status\",\"multiple\":\"f\",\"selvalues\":[{\"label\":\"Unknown\",\"value\":\"Unknown\",\"selected\":true},{\"label\":\"Proposed\",\"value\":\"Proposed\",\"selected\":false},{\"label\":\"Proposed Retired\",\"value\":\"Proposed Retired\",\"selected\":false},{\"label\":\"Location Description\",\"value\":\"Location Description\",\"selected\":false},{\"label\":\"In Service\",\"value\":\"In Service\",\"selected\":false},{\"label\":\"Out of Service\",\"value\":\"Out of Service\",\"selected\":false},{\"label\":\"Retired\",\"value\":\"Retired\",\"selected\":false},{\"label\":\"Removed\",\"value\":\"Removed\",\"selected\":false},{\"label\":\"Abandoned\",\"value\":\"Abandoned\",\"selected\":false},{\"label\":\"Under Construction\",\"value\":\"Under Construction\",\"selected\":false}],\"subtype\":null},{\"column_name\":\"radio-group-24314039-7456\",\"type\":\"radio-group\",\"required\":\"f\",\"label\":\"Maintained By\",\"name\":\"Maintained By\",\"multiple\":\"f\",\"selvalues\":[{\"label\":\"Our Agency\",\"value\":\"Our Agency\",\"selected\":true},{\"label\":\"Private\",\"value\":\"Private\",\"selected\":false},{\"label\":\"Other\",\"value\":\"Other\",\"selected\":false}],\"subtype\":null},{\"column_name\":\"text-20652743-8074\",\"type\":\"text\",\"required\":\"f\",\"label\":\"Station Name\",\"name\":\"Station Name\",\"multiple\":\"f\",\"selvalues\":null,\"subtype\":null},{\"column_name\":\"radio-group-82016273-8739\",\"type\":\"radio-group\",\"required\":\"f\",\"label\":\"Owned By\",\"name\":\"Owned By\",\"multiple\":\"f\",\"selvalues\":[{\"label\":\"Our Agency\",\"value\":\"Our Agency\",\"selected\":false},{\"label\":\"Private\",\"value\":\"Private\",\"selected\":false},{\"label\":\"Other\",\"value\":\"Other\",\"selected\":false}],\"subtype\":null},{\"column_name\":\"text-11285497-4987\",\"type\":\"text\",\"required\":\"f\",\"label\":\"Pipeline Operator Number\",\"name\":\"Pipeline Operator Number\",\"multiple\":\"f\",\"selvalues\":null,\"subtype\":null},{\"column_name\":\"radio-group-62342936-8015\",\"type\":\"radio-group\",\"required\":\"f\",\"label\":\"Regulatory Type\",\"name\":\"Regulatory Type\",\"multiple\":\"f\",\"selvalues\":[{\"label\":\"Unknown\",\"value\":\"Unknown\",\"selected\":false},{\"label\":\"Distribution\",\"value\":\"Distribution\",\"selected\":false},{\"label\":\"Gathering\",\"value\":\"Gathering\",\"selected\":false},{\"label\":\"Transmission\",\"value\":\"Transmission\",\"selected\":false},{\"label\":\"Offshore Gathering\",\"value\":\"Offshore Gathering\",\"selected\":false},{\"label\":\"Offshore Transmission\",\"value\":\"Offshore Transmission\",\"selected\":false}],\"subtype\":null},{\"column_name\":\"text-10825085-4422\",\"type\":\"text\",\"required\":\"f\",\"label\":\"Supplier\",\"name\":\"Supplier\",\"multiple\":\"f\",\"selvalues\":null,\"subtype\":null}]";

        return res;
    }

    private void getOpenForm(String dataResponce) {
        if (Utility.isEmptyString(dataResponce)) {
            Utility.showInfoDialog(mActivity, getResources().getString(R.string.lbl_No_Data_Found));
            return;
        }
        try {
            JSONArray jsonResponse = new JSONArray(dataResponce);
            try {
                Gson gson = new Gson();
                java.lang.reflect.Type listType = new TypeToken<ArrayList<FormDetailData>>() {}.getType();
                listFormDetailsData = gson.fromJson(jsonResponse.toString(), listType);
                Log.e(TAG, ""+listFormDetailsData.size());
                Log.e(TAG, "Form "+new Gson().toJson(listFormDetailsData));
                formRenderAdapter = new AdapterSearchSurveyWorkD(mActivity, listFormDetailsData, (path, position) -> {
                    if(path != null){

                    }
                });

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                recyclerViewSurveyWork.setLayoutManager(linearLayoutManager);
                recyclerViewSurveyWork.setAdapter(formRenderAdapter);

            } catch (Exception e) {
                Utility.showInfoDialog(mActivity, getResources().getString(R.string.lbl_Something_wrong_please_try_again));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//------------------------------------------------------- init --------------------------------------------------------------------------------------------------------------------------

    private void init(){
        searchButton           = findViewById(R.id.searchButton);
        spState                = findViewById(R.id.spState);
        spCity                 = findViewById(R.id.spCity);
        edZipCode              = findViewById(R.id.zipCode);
        recyclerViewSurveyWork = findViewById(R.id.recyclerViewSurveyWork);
        searchButton.setOnClickListener(this);
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

//------------------------------------------------------- onClick --------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.searchButton){
            if(!SystemUtility.isInternetConnected(this)){
                Utility.getOKDialogBox(mActivity, "Internet Issue", "Need Internet Connection", DialogInterface::dismiss);
            }
            else{

                if(!Utility.isEmptyString(stateSelected) && !Utility.isEmptyString(citySelected)){
                    searchButton();
                }
                else{
                    if(Utility.isEmptyString(stateSelected)){
                        Toast.makeText(mActivity, "please select state", Toast.LENGTH_SHORT).show();
                    }
                    if(Utility.isEmptyString(citySelected)){
                        Toast.makeText(mActivity, "please select city", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

    }

//------------------------------------------------------- Search Button --------------------------------------------------------------------------------------------------------------------------

    private void searchButton(){
        if(!Utility.isEmptyString(stateID) && !Utility.isEmptyString(cityID)){
            Intent intent = new Intent(this,SearchSurveyWorkRVActivity.class);
            intent.putExtra("stateID",stateID);
            intent.putExtra("cityID",cityID);
            intent.putExtra("zipCode","" + edZipCode.getText().toString());
            startActivity(intent);
        }
    }

//------------------------------------------------------- State Spinner --------------------------------------------------------------------------------------------------------------------------

    private void spinnerState(){

        spState.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if(parent.getSelectedItemPosition() > 0){
                            stateSelected = parent.getItemAtPosition(position).toString();

                            if(stateModelArrayList.size() > 0){
                                for(int i=0; i<stateModelArrayList.size(); i++){
                                    if(stateSelected.equalsIgnoreCase(stateModelArrayList.get(i).getStateName())){
                                        stateID = stateModelArrayList.get(i).getStateID();
                                        if(SystemUtility.isInternetConnected(mActivity)){
                                            processToCity(stateID);
                                        }
                                        else{
                                            Utility.getOKDialogBox(mActivity, "Internet Issue", "Need Internet Connection", DialogInterface::dismiss);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                }
        );

    }

    private void setStateAdapter(ArrayList<String> stateList){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item, stateList);
        spState.setAdapter(adapter);
        spState.setTitle("State");
    }

//------------------------------------------------------- City Spinner --------------------------------------------------------------------------------------------------------------------------

    private void setCityAdapter(ArrayList<String> cityList){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item, cityList);
        spCity.setAdapter(adapter);
        spCity.setTitle("City");
    }

    private void spinnerCity(){

        spCity.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(parent.getSelectedItemPosition() > 0){
                            citySelected = parent.getItemAtPosition(position).toString();
                            if(cityModelArrayList.size() > 0){
                                for(int i=0; i<cityModelArrayList.size(); i++){
                                    if(citySelected.equalsIgnoreCase(cityModelArrayList.get(i).getStateName())){
                                        cityID = cityModelArrayList.get(i).getStateID();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                }
        );
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

//------------------------------------------------------- Process To Server  --------------------------------------------------------------------------------------------------------------------------

    private void processToState(){
        showProgressDialog("States Loading...");
        Map<String, String> params = new HashMap<>();
        String data = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(URL_Utility.PARAM_ACTION, Utility.STATE);
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
        URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_STATE;
        BaseApplication.getInstance().makeHttpPostRequest(mActivity, responseCode, URL_Utility.WS_STATE, params, false, false);
    }

    private void processToCity(String state_id){
        showProgressDialog("City Loading...");
        if(!Utility.isEmptyString(state_id)){
            Map<String, String> params = new HashMap<>();
            String data = "";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(URL_Utility.PARAM_ACTION, Utility.CITY);
                jsonObject.put(URL_Utility.PARAM_STATE_ID, state_id);
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
            URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_CITY;
            BaseApplication.getInstance().makeHttpPostRequest(mActivity, responseCode, URL_Utility.WS_CITY, params, false, false);
        }
    }

//------------------------------------------------------- onSuccessResponse  --------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onSuccessResponse(URL_Utility.ResponseCode responseCode, String response) {

        // State ------------------
        if (responseCode == URL_Utility.ResponseCode.WS_STATE) {
            String res = AESCrypt.decryptResponse(response);
            if(!res.equals("")) {
                try {
                    JSONObject mObj = new JSONObject(res);
                    String status = mObj.optString("status");
                    Log.e(TAG,"Response: "+ status);
                    if (status.equalsIgnoreCase("Success")) {

                        JSONArray stateArray = new JSONArray(mObj.getString("data"));
                        if(stateArray.length() > 0){
                            ArrayList<String> stateList = new ArrayList<>();
                            stateList.add("Select State");
                            for(int i=0; i<stateArray.length(); i++){
                                String state_id  = stateArray.getJSONObject(i).getString("state_id");
                                String stateName = stateArray.getJSONObject(i).getString("state");
                                stateList.add(stateName);
                                stateModelArrayList.add(new StateModel(state_id,stateName));
                            }
                            setStateAdapter(stateList);
                        }
                        dismissProgressDialog();
                    }
                    else{
                        stateSelected = "";
                        stateID = "";
                        dismissProgressDialog();
                    }
                }
                catch (JSONException e) {
                    stateSelected = "";
                    stateID ="";
                    dismissProgressDialog();
                    Log.e(TAG,e.getMessage());
                }
            }
            else{
                stateSelected = "";
                stateID = "";
                dismissProgressDialog();
                Log.e(TAG,res);
            }
        }

        // City ------------------------
        if (responseCode == URL_Utility.ResponseCode.WS_CITY) {
            String res = AESCrypt.decryptResponse(response);
            if(!res.equals("")) {
                try {
                    JSONObject mObj = new JSONObject(res);
                    String status = mObj.optString("status");
                    Log.e(TAG,"Response: "+ status);
                    if (status.equalsIgnoreCase("Success")) {
                        JSONArray cityArray = new JSONArray(mObj.getString("data"));
                        if(cityArray.length() > 0){
                            ArrayList<String> cityList = new ArrayList<>();
                            cityList.add("Select City");
                            for(int i=0; i<cityArray.length(); i++){
                                String city_id  = cityArray.getJSONObject(i).getString("city_id");
                                String cityName = cityArray.getJSONObject(i).getString("city");
                                cityList.add(cityName);
                                cityModelArrayList.add(new StateModel(city_id,cityName));
                            }
                            setCityAdapter(cityList);
                        }
                        dismissProgressDialog();
                    }
                    else{
                        citySelected = "";
                        cityID = "";
                        dismissProgressDialog();
                    }
                }
                catch (JSONException e) {
                    citySelected ="";
                    cityID = "";
                    dismissProgressDialog();
                    Log.e(TAG,e.getMessage());
                }
            }
            else{
                citySelected ="";
                cityID = "";
                dismissProgressDialog();
                Log.e(TAG,res);
            }
        }
    }

//------------------------------------------------------- onErrorResponse  --------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onErrorResponse(URL_Utility.ResponseCode responseCode, VolleyError error) {
        dismissProgressDialog();
    }


//------------------------------------------------------- On Back Press  --------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        finish();
    }



    public Utility getUtility() {
        return utility;
    }

    public void setUtility(Utility utility) {
        this.utility = utility;
    }

    public ImageFileUtils getImageFileUtils() {
        return imageFileUtils;
    }

    public void setImageFileUtils(ImageFileUtils imageFileUtils) {
        this.imageFileUtils = imageFileUtils;
    }

    public SystemPermission getSystemPermission() {
        return systemPermission;
    }

    public void setSystemPermission(SystemPermission systemPermission) {
        this.systemPermission = systemPermission;
    }
}