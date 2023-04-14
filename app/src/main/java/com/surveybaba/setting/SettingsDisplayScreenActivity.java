package com.surveybaba.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.surveybaba.R;
import com.surveybaba.Utilities.SystemUtility;
import com.surveybaba.Utilities.Utility;

import java.util.Objects;

public class SettingsDisplayScreenActivity extends AppCompatActivity {
    SwitchCompat switchFullScreen, switchScreenOnOff;
    ImageView imgPortrait, imgLandscape;
    Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_settings_display_screen);
        mActivity = this;
        initView();
        initListener();
    }

    private void initListener() {

        switchFullScreen.setOnCheckedChangeListener((buttonView, isChecked) -> SystemUtility.setFullscreenToggle(mActivity, isChecked));

        switchScreenOnOff.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SystemUtility.setKeepScreenAwakeAlwaysToggle(mActivity, isChecked);
            if(isChecked)
                Utility.showToast(mActivity, "Always screen mode ON", null);
            else
                Utility.showToast(mActivity, "Always screen mode OFF", null);
        });

        imgPortrait.setOnClickListener(v -> {
            Utility.saveData(mActivity, Utility.IS_SCREEN_ORIENTATION_PORTRAIT, true);
            Utility.showToast(mActivity, "Portrait mode enabled for Map screen", v);
        });

        imgLandscape.setOnClickListener(v -> {
            Utility.saveData(mActivity, Utility.IS_SCREEN_ORIENTATION_PORTRAIT, false);
            Utility.showToast(mActivity, "Landscape mode enabled for Map screen", v);
        });
    }

    private void initView() {
        switchFullScreen = findViewById(R.id.switchFullScreen);
        switchScreenOnOff = findViewById(R.id.switchScreenOnOff);
        imgPortrait = findViewById(R.id.imgPortrait);
        imgLandscape = findViewById(R.id.imgLandscape);
        switchFullScreen.setChecked(Utility.getBooleanSavedData(this, Utility.IS_FULL_SCREEN));
        switchScreenOnOff.setChecked(Utility.getBooleanSavedData(this, Utility.IS_ALWAYS_KEEP_SCREEN_AWAKE));
        SystemUtility.setFullscreenToggle(mActivity, Utility.getBooleanSavedData(this, Utility.IS_FULL_SCREEN));
        SystemUtility.setKeepScreenAwakeAlwaysToggle(mActivity, Utility.getBooleanSavedData(this, Utility.IS_ALWAYS_KEEP_SCREEN_AWAKE));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
