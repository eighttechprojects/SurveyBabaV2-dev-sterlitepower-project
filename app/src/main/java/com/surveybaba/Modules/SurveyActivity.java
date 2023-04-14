package com.surveybaba.Modules;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.surveybaba.R;
import java.util.Objects;

public class SurveyActivity extends AppCompatActivity{

    // TAG
    private static final String TAG = "SurveyActivity";

//------------------------------------------------------- On Create ------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        // Add Back Button on ToolBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

//------------------------------------------------------- On Create Options Menu ------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.survey_menu, menu);
        return true;
    }

//------------------------------------------------------- On OptionsItem Selected ------------------------------------------------------------------------------------------------------

    // Edit button on menu!
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Back Button Press
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

//------------------------------------------------------- on back Pressed ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        finish();
    }


}