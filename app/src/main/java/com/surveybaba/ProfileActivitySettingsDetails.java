package com.surveybaba;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.gson.Gson;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.setting.model.BinRecordProfile;

import static com.surveybaba.Utilities.Utility.key_profile_is_add;

public class ProfileActivitySettingsDetails extends AppCompatActivity {
    LinearLayout llCategoryName, llDistanceInterval, llTimeInterval,
            llVertexRecordingCondition, llGpsAccuracy, llTrackPointQty;
    SwitchCompat switchMovement, switchGPS, switchTrack;
    TextView txtCategoryName, txtRemoveProfile;
    Activity mActivity;
    String TAG = ProfileActivity.class.getSimpleName();
    String unitDistance, unitGpsAltitude;
    Bundle bundle;
    int indexDistUnit;
    int indexGpsAltitudeUnit;
    BinRecordProfile binRecordProfile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_profile_setting_details_screen);
        mActivity = this;
        initView();
        getExtras();
        initListener();
    }

    private void getExtras() {
        bundle = getIntent().getExtras();
        if(bundle!=null && bundle.containsKey(Utility.key_profile_activity_data))
        {
            binRecordProfile = new Gson().fromJson(bundle.getString(Utility.key_profile_activity_data), BinRecordProfile.class);
            txtCategoryName.setText(binRecordProfile.getName());
            switchMovement.setChecked(binRecordProfile.isRecordOnlyWhenMoving());
            switchGPS.setChecked(binRecordProfile.isRecordWhenGpsOff());
            switchTrack.setChecked(binRecordProfile.getNoOfTracksToDisplay()>0);
            llTrackPointQty.setVisibility(switchTrack.isChecked() ? View.VISIBLE : View.INVISIBLE);
        }
        if(bundle!=null && bundle.containsKey(key_profile_is_add))
        {
            txtRemoveProfile.setEnabled(!bundle.getBoolean(key_profile_is_add));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        indexDistUnit = Integer.parseInt(Utility.getSavedData(mActivity, Utility.UNIT_DISTANCE_DATA));
        unitDistance = Utility.getUNIT_DISTANCE(mActivity)[indexDistUnit-1];

        indexGpsAltitudeUnit = Integer.parseInt(Utility.getSavedData(mActivity, Utility.UNIT_GPS_ALTITUDE_DATA));
        unitGpsAltitude = Utility.getUNIT_ALTITUDE(mActivity)[indexGpsAltitudeUnit-1];
    }

    private void initListener() {
        llCategoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showEditTextDialog(mActivity, "Name", null, binRecordProfile.getName(), Utility.INPUT_TYPE.STRING, new Utility.onDialogClickListener() {
                    @Override
                    public void onPositiveAction(String value) {
                        Log.e(TAG, value);
                        binRecordProfile.setName(value);
                    }

                    @Override
                    public void onCancelAction() {
                        Log.e(TAG, "onCancelAction");
                    }
                });
            }
        });
        txtRemoveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showTextDialog(mActivity, "Alert", "Remove profile \"" + txtCategoryName.getText().toString().trim() + "\" ?", new Utility.onDialogClickListener() {
                    @Override
                    public void onPositiveAction(String ignore) {
                        Log.e(TAG, "Action Removed");
                        saveData(true);
                    }

                    @Override
                    public void onCancelAction() {
                        Log.e(TAG, "onCancelAction");
                    }
                });
            }
        });
        llDistanceInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] dataValue = Utility.convertDistanceFromMeter(mActivity, binRecordProfile.getDistanceInterval()).split(" ");
                Utility.showEditTextDialog(mActivity, getString(R.string.distance_interval), unitDistance, dataValue[0], Utility.INPUT_TYPE.NUMBER_DECIMAL, new Utility.onDialogClickListener() {
                    @Override
                    public void onPositiveAction(String value) {
                        Log.e(TAG, ""+value);
                        binRecordProfile.setDistanceInterval(Utility.convertDistanceToMeter(Double.parseDouble(value), indexDistUnit));
                    }

                    @Override
                    public void onCancelAction() {
                        Log.e(TAG, "onCancelAction");
                    }
                });
            }
        });
        llTimeInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showEditTextDialog(mActivity, getString(R.string.time_interval), Utility.UNIT_TIME_SEC, ""+binRecordProfile.getTimeInterval(), Utility.INPUT_TYPE.NUMBER_ONLY, new Utility.onDialogClickListener() {
                    @Override
                    public void onPositiveAction(String value) {
                        Log.e(TAG, ""+value);
                        binRecordProfile.setTimeInterval(Double.parseDouble(value));
                    }

                    @Override
                    public void onCancelAction() {
                        Log.e(TAG, "onCancelAction");
                    }
                });
            }
        });
        llVertexRecordingCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] arr = {getString(R.string.distance_and_time_both), getString(R.string.distance_or_time_one)};
                Utility.showLocationDialog(mActivity, getString(R.string.vertex_recording_condition), arr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        Log.e(TAG, "VertexRecordingCondition = " + arr[position]);
                        binRecordProfile.setVertexRecordingIndex(position+1);
                    }
                });
            }
        });
        llGpsAccuracy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataValue = ""+Utility.convertGpsAltitudeFromMeter(binRecordProfile.getGpsAccuracy(), indexGpsAltitudeUnit);
                Utility.showEditTextDialog(mActivity, getString(R.string.required_gps_accuracy), unitGpsAltitude, dataValue, Utility.INPUT_TYPE.NUMBER_DECIMAL, new Utility.onDialogClickListener() {
                    @Override
                    public void onPositiveAction(String value) {
                        Log.e(TAG, ""+value);
                        binRecordProfile.setGpsAccuracy(Utility.convertGpsAltitudeToMeter(Double.parseDouble(value), indexGpsAltitudeUnit));
                    }

                    @Override
                    public void onCancelAction() {
                        Log.e(TAG, "onCancelAction");
                    }
                });
            }
        });
        switchMovement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e(TAG, "switchMovement " + isChecked);
                Utility.saveData(mActivity, Utility.IS_RECORD_ONLY_WHEN_MOVING, isChecked);
                binRecordProfile.setRecordOnlyWhenMoving(isChecked);
            }
        });
        switchGPS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e(TAG, "switchGPS " + isChecked);
                Utility.saveData(mActivity, Utility.IS_RECORD_WHEN_GPS_OFF, isChecked);
                binRecordProfile.setRecordWhenGpsOff(isChecked);
            }
        });
        switchTrack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e(TAG, "switchTrack " + isChecked);
                llTrackPointQty.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                Utility.saveData(mActivity, Utility.IS_DISPLAY_PART_OF_TRACK, isChecked);
            }
        });
        llTrackPointQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showEditTextDialog(mActivity, getString(R.string.required_gps_accuracy), null, ""+binRecordProfile.getNoOfTracksToDisplay(), Utility.INPUT_TYPE.NUMBER_ONLY, new Utility.onDialogClickListener() {
                    @Override
                    public void onPositiveAction(String value) {
                        Log.e(TAG, ""+value);
                        Utility.saveData(mActivity, Utility.NO_OF_TRACKS_TO_RECORD, value);
                        binRecordProfile.setNoOfTracksToDisplay(Integer.parseInt(value));
                        if(Integer.parseInt(value) < 1)
                        {
                            switchTrack.setChecked(false);
                        }
                    }

                    @Override
                    public void onCancelAction() {
                        Log.e(TAG, "onCancelAction");
                    }
                });
            }
        });
    }

    private void initView() {
        llCategoryName = findViewById(R.id.llCategoryName);
        llDistanceInterval = findViewById(R.id.llDistanceInterval);
        llTimeInterval = findViewById(R.id.llTimeInterval);
        llVertexRecordingCondition = findViewById(R.id.llVertexRecordingCondition);
        llGpsAccuracy = findViewById(R.id.llGpsAccuracy);
        llTrackPointQty = findViewById(R.id.llTrackPointQty);
        txtCategoryName = findViewById(R.id.txtCategoryName);
        txtRemoveProfile = findViewById(R.id.txtRemoveProfile);
        switchMovement = findViewById(R.id.switchMovement);
        switchGPS = findViewById(R.id.switchGPS);
        switchTrack = findViewById(R.id.switchTrack);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveData(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveData(false);
    }

    private void saveData(boolean isRemovedProfile)
    {
        Intent intent = new Intent();
        intent.putExtra(Utility.key_profile_activity_data, new Gson().toJson(binRecordProfile));
        intent.putExtra(Utility.key_is_profile_remove, isRemovedProfile);
        setResult(RESULT_OK, intent);
        finish();
    }
}
