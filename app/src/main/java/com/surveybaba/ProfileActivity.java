package com.surveybaba;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.surveybaba.Utilities.Utility;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
    }

    private EditText edtEmailID, edtFirstname, edtLastname;
    private Button btnSubmit;

    private void init() {
        edtEmailID = findViewById(R.id.edtEmailID);
        edtFirstname = findViewById(R.id.edtFirstname);
        edtLastname = findViewById(R.id.edtLastname);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(click);

        edtEmailID.setText(Utility.getSavedData(ProfileActivity.this, Utility.PROFILE_EMAILID));
        edtFirstname.setText(Utility.getSavedData(ProfileActivity.this, Utility.PROFILE_FIRSTNAME));
        edtLastname.setText(Utility.getSavedData(ProfileActivity.this, Utility.PROFILE_LASTNAME));

        isVisible(false);
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
            Utility.showInfoDialog(ProfileActivity.this, getResources().getString(R.string.Please_enter_emailid));
            return;
        }
        if(Utility.isEmptyString(edtFirstname.getText().toString().trim())) {
            Utility.showInfoDialog(ProfileActivity.this, getResources().getString(R.string.Please_enter_firstname));
            return;
        }
        if(Utility.isEmptyString(edtLastname.getText().toString().trim())) {
            Utility.showInfoDialog(ProfileActivity.this, getResources().getString(R.string.Please_enter_lastname));
            return;
        }
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!edtEmailID.getText().toString().trim().matches(emailPattern)) {
            Utility.showInfoDialog(ProfileActivity.this, getResources().getString(R.string.Please_enter_valid_emailid));
            return;
        }
    }

    private void isVisible(boolean flag) {
        edtEmailID.setEnabled(flag);
        edtFirstname.setEnabled(flag);
        edtLastname.setEnabled(flag);

        if(flag == true) {
            edtEmailID.setTextColor(getResources().getColor(R.color.black));
            edtFirstname.setTextColor(getResources().getColor(R.color.black));
            edtLastname.setTextColor(getResources().getColor(R.color.black));
        }
        if(flag == false) {
            edtEmailID.setTextColor(getResources().getColor(R.color.colorDarkGery));
            edtFirstname.setTextColor(getResources().getColor(R.color.colorDarkGery));
            edtLastname.setTextColor(getResources().getColor(R.color.colorDarkGery));
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
