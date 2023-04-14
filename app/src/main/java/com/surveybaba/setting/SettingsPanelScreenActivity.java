package com.surveybaba.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.surveybaba.R;
import com.surveybaba.Utilities.Utility;

import static com.surveybaba.Utilities.SystemUtility.DEFAULT_DURATION_SEC;
import static com.surveybaba.Utilities.Utility.BASE_MAP_HYBRID;
import static com.surveybaba.Utilities.Utility.BASE_MAP_NORMAL;
import static com.surveybaba.Utilities.Utility.BASE_MAP_SATELLITE;
import static com.surveybaba.Utilities.Utility.BASE_MAP_TERRAIN;

public class SettingsPanelScreenActivity extends AppCompatActivity {
    SwitchCompat switchInactiveUiHide;
    LinearLayout llPanelHideDuration;
    EditText edtPanelHideTimeout;
    boolean isPanelHidden;
    Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_settings_panel_screen);
        mActivity = this;
        init();
        initListener();
        initData();
    }

    private void initListener() {
        switchInactiveUiHide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                llPanelHideDuration.setVisibility(isChecked?View.VISIBLE:View.GONE);
                Utility.saveData(mActivity, Utility.IS_PANEL_HIDE, isChecked);
                isPanelHidden = isChecked;
            }
        });
    }

    private void init() {
        isPanelHidden = Utility.getBooleanSavedData(mActivity, Utility.IS_PANEL_HIDE);
        switchInactiveUiHide = findViewById(R.id.switchInactiveUiHide);
        llPanelHideDuration = findViewById(R.id.llPanelHideDuration);
        edtPanelHideTimeout = findViewById(R.id.edtPanelHideTimeout);
        switchInactiveUiHide.setChecked(isPanelHidden);
        llPanelHideDuration.setVisibility(isPanelHidden?View.VISIBLE:View.GONE);
    }

    private void initData() {
        String duration = Utility.getSavedData(mActivity, Utility.HIDE_PANEL_DURATION);
        if(Utility.isEmptyString(duration))
            duration = DEFAULT_DURATION_SEC;
        else if(Integer.parseInt(duration)<=Integer.parseInt(DEFAULT_DURATION_SEC))
            duration = DEFAULT_DURATION_SEC;
        edtPanelHideTimeout.setText(duration);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isPanelHidden) {
            String duration = edtPanelHideTimeout.getText().toString().trim();
            if(Utility.isEmptyString(duration))
                duration = DEFAULT_DURATION_SEC;
            else if(Integer.parseInt(duration)<=Integer.parseInt(DEFAULT_DURATION_SEC))
                duration = DEFAULT_DURATION_SEC;
            Utility.saveData(mActivity, Utility.HIDE_PANEL_DURATION, duration);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
