package com.surveybaba;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.VolleyError;
import com.surveybaba.Utilities.Utility;
import com.volly.BaseApplication;
import com.volly.URL_Utility;
import com.volly.URL_Utility.ResponseCode;
import com.volly.WSResponseInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity implements WSResponseInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
    }

    private EditText edtEmailID, edtPassword, edtFirstname, edtLastname;
    private Button btnSubmit;
    private void init() {
        edtEmailID = findViewById(R.id.edtEmailID);
        edtPassword = findViewById(R.id.edtPassword);
        edtFirstname = findViewById(R.id.edtFirstname);
        edtLastname = findViewById(R.id.edtLastname);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(click);
    }

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int getid = v.getId();
            switch (getid) {
                case R.id.btnSubmit:
                    onClickRegistration();
                    break;
            }
        }
    };

    private void onClickRegistration() {
        if(Utility.isEmptyString(edtEmailID.getText().toString().trim())) {
            Utility.showInfoDialog(RegistrationActivity.this, getResources().getString(R.string.Please_enter_emailid));
            return;
        }
        if(Utility.isEmptyString(edtPassword.getText().toString().trim())) {
            Utility.showInfoDialog(RegistrationActivity.this, getResources().getString(R.string.Please_enter_password));
            return;
        }
        if(Utility.isEmptyString(edtFirstname.getText().toString().trim())) {
            Utility.showInfoDialog(RegistrationActivity.this, getResources().getString(R.string.Please_enter_firstname));
            return;
        }
        if(Utility.isEmptyString(edtLastname.getText().toString().trim())) {
            Utility.showInfoDialog(RegistrationActivity.this, getResources().getString(R.string.Please_enter_lastname));
            return;
        }
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!edtEmailID.getText().toString().trim().matches(emailPattern)) {
            Utility.showInfoDialog(RegistrationActivity.this, getResources().getString(R.string.Please_enter_valid_emailid));
            return;
        }
        processToUserRegistration();
    }

    private void processToUserRegistration() {
        Map<String, String> params = new HashMap<String, String>();
        ResponseCode responseCode = ResponseCode.WS_USER_REGISTRATION;
        params.put(URL_Utility.PARAM_EMAIL_ID, edtEmailID.getText().toString().trim());
        params.put(URL_Utility.PARAM_PASSWORD, edtPassword.getText().toString().trim());
        params.put(URL_Utility.PARAM_FIRST_NAME, edtFirstname.getText().toString().trim());
        params.put(URL_Utility.PARAM_LAST_NAME, edtLastname.getText().toString().trim());
        params.put(URL_Utility.ACTION_APP_VERSION, URL_Utility.APP_VERSION);
        BaseApplication.getInstance().makeHttpPostRequest(RegistrationActivity.this, responseCode, URL_Utility.WS_USER_REGISTRATION, params, false, false);
    }

    @Override
    public void onSuccessResponse(ResponseCode responseCode, String response) {
        if (responseCode == ResponseCode.WS_USER_REGISTRATION) {
            try {
                Log.e("Registration", ""+response);
                JSONObject mLoginObj = new JSONObject(response);
                JSONObject objData = new JSONObject(mLoginObj.getString("data"));
                String msg = objData.getString("msg");
                if(msg.equalsIgnoreCase("Success")) {
                    showInfoDialog();
                } else {
                    Utility.showInfoDialog(RegistrationActivity.this, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onErrorResponse(ResponseCode responseCode, VolleyError error) {
        Utility.showInfoDialog(RegistrationActivity.this, error.getMessage());
    }

    @Override
    public void onBackPressed() {
        reDirectLogin();
    }

    private void reDirectLogin() {
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showInfoDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegistrationActivity.this);
        alertDialog.setMessage(getResources().getString(R.string.user_registration_successfully));
        alertDialog.setPositiveButton(getResources().getString(R.string.lbl_Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                reDirectLogin();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

}
