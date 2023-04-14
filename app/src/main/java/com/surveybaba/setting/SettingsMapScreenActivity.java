package com.surveybaba.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.surveybaba.R;
import com.surveybaba.Utilities.Utility;

import static com.surveybaba.Utilities.Utility.BASE_MAP_HYBRID;
import static com.surveybaba.Utilities.Utility.BASE_MAP_NORMAL;
import static com.surveybaba.Utilities.Utility.BASE_MAP_SATELLITE;
import static com.surveybaba.Utilities.Utility.BASE_MAP_TERRAIN;

public class SettingsMapScreenActivity extends AppCompatActivity {
    String[] mapStylesArr = {BASE_MAP_NORMAL, BASE_MAP_SATELLITE, BASE_MAP_TERRAIN, BASE_MAP_HYBRID};
    LinearLayout llMapStyle, llMapCursor;
    TextView txtMapStyle;
    ImageView imgTargetLoc1, imgTargetLoc2, imgTargetLoc3, imgTargetLoc4,
            imgTargetLoc5, imgTargetLoc6, imgTargetLoc7, imgTargetLoc8;
    SwitchCompat switchReAlignMapCursor;
    Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_settings_map_screen);
        mActivity = this;
        init();
        initListener();
        setData();
    }

    private void setData() {

    }

    private void initListener() {
        llMapStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.openSelectDialog(mActivity, mapStylesArr, new Utility.onItemClick() {
                    @Override
                    public void itemSelected(int position) {
                        txtMapStyle.setText(mapStylesArr[position]);
                        Utility.saveData(mActivity, Utility.BASE_MAP, mapStylesArr[position]);
                    }
                });
            }
        });
        switchReAlignMapCursor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Utility.saveData(mActivity, Utility.IS_REALIGN_MAP_CURSOR, isChecked);
            }
        });
        imgTargetLoc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.saveData(mActivity, Utility.CURSOR_CENTER, "0");
            }
        });
        imgTargetLoc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.saveData(mActivity, Utility.CURSOR_CENTER, "1");
            }
        });
        imgTargetLoc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.saveData(mActivity, Utility.CURSOR_CENTER, "2");
            }
        });
        imgTargetLoc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.saveData(mActivity, Utility.CURSOR_CENTER, "3");
            }
        });
        imgTargetLoc5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.saveData(mActivity, Utility.CURSOR_CENTER, "4");
            }
        });
        imgTargetLoc6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.saveData(mActivity, Utility.CURSOR_CENTER, "5");
            }
        });
        imgTargetLoc7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.saveData(mActivity, Utility.CURSOR_CENTER, "6");
            }
        });
        imgTargetLoc8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.saveData(mActivity, Utility.CURSOR_CENTER, "7");
            }
        });

    }

    private void init() {
        llMapStyle = findViewById(R.id.llMapStyle);
        txtMapStyle = findViewById(R.id.txtMapStyle);
        llMapCursor = findViewById(R.id.llMapCursor);
        imgTargetLoc1 = findViewById(R.id.imgTargetLoc1);
        imgTargetLoc2 = findViewById(R.id.imgTargetLoc2);
        imgTargetLoc3 = findViewById(R.id.imgTargetLoc3);
        imgTargetLoc4 = findViewById(R.id.imgTargetLoc4);
        imgTargetLoc5 = findViewById(R.id.imgTargetLoc5);
        imgTargetLoc6 = findViewById(R.id.imgTargetLoc6);
        imgTargetLoc7 = findViewById(R.id.imgTargetLoc7);
        imgTargetLoc8 = findViewById(R.id.imgTargetLoc8);
        switchReAlignMapCursor = findViewById(R.id.switchReAlignMapCursor);
        switchReAlignMapCursor.setChecked(Utility.getBooleanSavedData(mActivity, Utility.IS_REALIGN_MAP_CURSOR));
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
