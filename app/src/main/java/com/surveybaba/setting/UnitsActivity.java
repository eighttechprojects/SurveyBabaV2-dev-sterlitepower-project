package com.surveybaba.setting;

import android.app.Activity;
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
import com.surveybaba.Utilities.SystemUtility;
import com.surveybaba.Utilities.Utility;

public class UnitsActivity extends AppCompatActivity {
    Activity mActivity;
    String unitAreaIndex, unitArea;
    String[] unitAreaArr;
    LinearLayout llUnitDistance, llUnitArea, llUnitAltitude, llUnitSpeed, llUnitAngle, llUnitTemperature;
    TextView txtUnitDistance, txtUnitArea, txtUnitAltitude, txtUnitSpeed, txtUnitAngle, txtUnitTemperature;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_settings_units);
        mActivity = this;
        initView();
        initListener();
        setDefaultValues();
    }

    private void setDefaultValues() {
        unitAreaArr = Utility.getUNIT_AREA(mActivity);
        unitAreaIndex = Utility.getSavedData(mActivity, Utility.UNIT_AREA_DATA);
        if(Utility.isEmptyString(unitAreaIndex))
            unitAreaIndex = ""+Utility.UNIT_AREA.YD_2;
        txtUnitArea.setText(unitAreaArr[Integer.parseInt(unitAreaIndex)-1]);
    }

    private void initListener() {
        llUnitDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] arr = Utility.getUNIT_DISTANCE(mActivity);
                Utility.openSelectDialog(mActivity, arr, new Utility.onItemClick() {
                    @Override
                    public void itemSelected(int position) {
                        txtUnitDistance.setText(arr[position]);
                        Utility.saveData(mActivity, Utility.UNIT_DISTANCE_DATA, ""+(position+1));
                    }
                });
            }
        });
        llUnitArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.openSelectDialog(mActivity, unitAreaArr, new Utility.onItemClick() {
                    @Override
                    public void itemSelected(int position) {
                        txtUnitArea.setText(unitAreaArr[position]);
                        Utility.saveData(mActivity, Utility.UNIT_AREA_DATA, ""+(position+1));
                    }
                });
            }
        });
        llUnitAltitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] arr = Utility.getUNIT_ALTITUDE(mActivity);
                Utility.openSelectDialog(mActivity, arr, new Utility.onItemClick() {
                    @Override
                    public void itemSelected(int position) {
                        Utility.saveData(mActivity, Utility.UNIT_GPS_ALTITUDE_DATA, ""+(position+1));
                        txtUnitAltitude.setText(arr[position]);
                    }
                });
            }
        });
        llUnitSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] arr = Utility.getUNIT_SPEED(mActivity);
                Utility.openSelectDialog(mActivity, arr, new Utility.onItemClick() {
                    @Override
                    public void itemSelected(int position) {
                        txtUnitSpeed.setText(arr[position]);
                    }
                });
            }
        });
        llUnitAngle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] arr = Utility.getUNIT_ANGLE(mActivity);
                Utility.openSelectDialog(mActivity, arr, new Utility.onItemClick() {
                    @Override
                    public void itemSelected(int position) {
                        txtUnitAngle.setText(arr[position]);
                    }
                });
            }
        });
        llUnitTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] arr = Utility.getUNIT_TEMPERATURE(mActivity);
                Utility.openSelectDialog(mActivity, arr, new Utility.onItemClick() {
                    @Override
                    public void itemSelected(int position) {
                        txtUnitTemperature.setText(arr[position]);
                    }
                });
            }
        });
    }

    private void initView() {
        llUnitDistance = findViewById(R.id.llUnitDistance);
        llUnitArea = findViewById(R.id.llUnitArea);
        llUnitAltitude = findViewById(R.id.llUnitAltitude);
        llUnitSpeed = findViewById(R.id.llUnitSpeed);
        llUnitAngle = findViewById(R.id.llUnitAngle);
        llUnitTemperature = findViewById(R.id.llUnitTemperature);
        txtUnitDistance = findViewById(R.id.txtUnitDistance);
        txtUnitArea = findViewById(R.id.txtUnitArea);
        txtUnitAltitude = findViewById(R.id.txtUnitAltitude);
        txtUnitSpeed = findViewById(R.id.txtUnitSpeed);
        txtUnitAngle = findViewById(R.id.txtUnitAngle);
        txtUnitTemperature = findViewById(R.id.txtUnitTemperature);
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
