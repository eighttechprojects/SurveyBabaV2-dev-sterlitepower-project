package com.surveybaba.setting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.surveybaba.R;
import java.util.Objects;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{


    private LinearLayout llDisplay, llMapScreen, llPanelsButtons;
    private TextView txtGuidance, txtTrackRecording, txtUnits;

//------------------------------------------------------- onCreate ----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_settings);
        init();
        setOnClickListener();
    }

//------------------------------------------------------- init ----------------------------------------------------------------------------------------------------------------------

    private void init() {
        llDisplay = findViewById(R.id.llDisplay);
        llMapScreen = findViewById(R.id.llMapScreen);
        llPanelsButtons = findViewById(R.id.llPanelsButtons);
        txtGuidance = findViewById(R.id.txtGuidance);
        txtTrackRecording = findViewById(R.id.txtTrackRecording);
        txtUnits = findViewById(R.id.txtUnits);
    }

//------------------------------------------------------- setOnClickListener ----------------------------------------------------------------------------------------------------------------------

    private void setOnClickListener(){
        llDisplay.setOnClickListener(this);
        llMapScreen.setOnClickListener(this);
        llPanelsButtons.setOnClickListener(this);
        txtGuidance.setOnClickListener(this);
        txtTrackRecording.setOnClickListener(this);
        txtUnits.setOnClickListener(this);
    }

//------------------------------------------------------- Menu ----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//------------------------------------------------------- onClick ----------------------------------------------------------------------------------------------------------------------

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.llDisplay:
                reDirectSettingsDisplayScreen();
                break;

            case R.id.llMapScreen:
                reDirectSettingsMapScreen();
                break;

            case R.id.llPanelsButtons:
                reDirectSettingsPanelScreen();
                break;

            case R.id.txtGuidance:
                redirectToGuidance();
                break;

            case R.id.txtTrackRecording:
                redirectToTrackRecordings();
                break;

            case R.id.txtUnits:
                redirectToUnitsScreen();
                break;

        }

    }

//------------------------------------------------------- ReDirect ----------------------------------------------------------------------------------------------------------------------

    private void redirectToUnitsScreen() {
        Intent intent = new Intent(SettingActivity.this, UnitsActivity.class);
        startActivity(intent);
    }

    private void redirectToTrackRecordings() {
        Intent intent = new Intent(SettingActivity.this, TrackRecordingActivity.class);
        startActivity(intent);
    }

    private void redirectToGuidance() {
        Intent intent = new Intent(SettingActivity.this, GuidanceActivity.class);
        startActivity(intent);
    }


    private void reDirectSettingsMapScreen() {
        Intent intent = new Intent(SettingActivity.this, SettingsMapScreenActivity.class);
        startActivity(intent);
    }

    private void reDirectSettingsDisplayScreen() {
        Intent intent = new Intent(SettingActivity.this, SettingsDisplayScreenActivity.class);
        startActivity(intent);
    }

    private void reDirectSettingsPanelScreen() {
        Intent intent = new Intent(SettingActivity.this, SettingsPanelScreenActivity.class);
        startActivity(intent);
    }


}
