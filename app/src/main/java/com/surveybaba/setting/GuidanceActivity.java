package com.surveybaba.setting;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.surveybaba.R;
import com.surveybaba.Utilities.SystemUtility;
import com.surveybaba.Utilities.Utility;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.IndicatorType;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;
import com.warkiz.widget.TickMarkType;

public class GuidanceActivity extends AppCompatActivity {
    SwitchCompat switchNotification;
    ImageView imgColor1, imgColor2, imgColor3, imgColor4, imgColor5, imgColor6,
            imgColor7, imgColor8;
    IndicatorSeekBar seekBarLineSize, seekBarTextSize;
    Activity mActivity;
    String TAG = GuidanceActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_settings_guidance_screen);
        mActivity = this;
        initView();
        initListener();
    }

    private void initListener() {
        seekBarLineSize.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                Log.e(TAG, "LINE_SIZE: "+seekParams.progress);
                Utility.saveData(mActivity, Utility.LINE_SIZE, ""+seekParams.progress);
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });
        seekBarTextSize.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                Log.e(TAG, "TEXT_SIZE: "+seekParams.progress);
                Utility.saveData(mActivity, Utility.TEXT_SIZE, ""+seekParams.progress);
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });
        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Utility.saveData(mActivity, Utility.IS_NOTIFICATION_DISPLAY, isChecked);
            }
        });
        imgColor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.saveData(mActivity, Utility.LINE_COLOR_CODE, Utility.COLOR_CODE.ORANGE);
            }
        });
        imgColor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.saveData(mActivity, Utility.LINE_COLOR_CODE, Utility.COLOR_CODE.PURPLE);
            }
        });
        imgColor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.saveData(mActivity, Utility.LINE_COLOR_CODE, Utility.COLOR_CODE.PINK);
            }
        });
        imgColor4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.saveData(mActivity, Utility.LINE_COLOR_CODE, Utility.COLOR_CODE.BLUE);
            }
        });
        imgColor5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.saveData(mActivity, Utility.LINE_COLOR_CODE, Utility.COLOR_CODE.RED);
            }
        });
        imgColor6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.saveData(mActivity, Utility.LINE_COLOR_CODE, Utility.COLOR_CODE.LIGHT_BLUE);
            }
        });
        imgColor7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.saveData(mActivity, Utility.LINE_COLOR_CODE, Utility.COLOR_CODE.GREEN);
            }
        });
        imgColor8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.saveData(mActivity, Utility.LINE_COLOR_CODE, Utility.COLOR_CODE.BLACK);
            }
        });
    }

    private void initView() {
        switchNotification = findViewById(R.id.switchNotification);
        seekBarLineSize = findViewById(R.id.seekBarLineSize);
        seekBarTextSize = findViewById(R.id.seekBarTextSize);
        imgColor1 = findViewById(R.id.imgColor1);
        imgColor2 = findViewById(R.id.imgColor2);
        imgColor3 = findViewById(R.id.imgColor3);
        imgColor4 = findViewById(R.id.imgColor4);
        imgColor5 = findViewById(R.id.imgColor5);
        imgColor6 = findViewById(R.id.imgColor6);
        imgColor7 = findViewById(R.id.imgColor7);
        imgColor8 = findViewById(R.id.imgColor8);
        switchNotification.setChecked(Utility.getBooleanSavedData(this, Utility.IS_NOTIFICATION_DISPLAY));
        seekBarLineSize.setProgress(SystemUtility.getLineSize(this));
        seekBarTextSize.setProgress(SystemUtility.getTextSizeValue(this));
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
