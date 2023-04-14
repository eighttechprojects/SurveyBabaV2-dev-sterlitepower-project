package com.surveybaba.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.surveybaba.ADAPTER.AdapterRecordings;
import com.surveybaba.ProfileActivitySettingsDetails;
import com.surveybaba.R;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.setting.model.BinRecordProfile;

import java.util.ArrayList;
import java.util.List;

import static com.surveybaba.Utilities.Utility.REQUEST_CODE_PROFILE_ADD;
import static com.surveybaba.Utilities.Utility.REQUEST_CODE_PROFILE_UPDATE;
import static com.surveybaba.Utilities.Utility.convertStringToRecordProfileArray;

public class TrackRecordingActivity extends AppCompatActivity {
    Activity mActivity;
    String TAG = TrackRecordingActivity.class.getSimpleName();
    LinearLayout llAddNewProfile;
    RecyclerView rcvTrackProfiles;
    private LinearLayoutManager layoutManager;
    private AdapterRecordings adapter;
    private List<BinRecordProfile> listProfiles = new ArrayList<>();
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_settings_track_recordings_screen);
        mActivity = this;
        initView();
        initListener();
        setData();
    }


//----------------------------------------- Init View --------------------------------------------------------------------------------------------------------------------------------
    private void initView() {
        // RecycleView
        rcvTrackProfiles = findViewById(R.id.rcvTrackProfiles);
        // Linear Layout
        llAddNewProfile = findViewById(R.id.llAddNewProfile);

        layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvTrackProfiles.setLayoutManager(layoutManager);

        // Recording Adapter
        adapter = new AdapterRecordings(mActivity, listProfiles, new AdapterRecordings.onCustomRowListener() {
            @Override
            public void onSetProfile(int position) {
                Utility.showToast(mActivity, listProfiles.get(position).getName() + " - profile set", null);
            }
            @Override
            public void onRedirectToSetting(int position) {
                Intent intent = new Intent(mActivity, ProfileActivitySettingsDetails.class);
                intent.putExtra(Utility.key_profile_activity_data, gson.toJson(listProfiles.get(position)));
                intent.putExtra(Utility.key_profile_is_add, false);
                startActivityForResult(intent, REQUEST_CODE_PROFILE_UPDATE);
            }
        });
        rcvTrackProfiles.setAdapter(adapter);
    }

//----------------------------------------- Init Listener --------------------------------------------------------------------------------------------------------------------------------

    private void initListener() {
        llAddNewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showProfileActivityDialog(mActivity);
            }
        });
    }

//----------------------------------------- Set Data --------------------------------------------------------------------------------------------------------------------------------

    private void setData() {
        String listStr = Utility.getSavedData(mActivity, Utility.KEY_RECORD_PROFILES_LIST);
        if (Utility.isEmptyString(listStr)) {
            BinRecordProfile bin1 = new BinRecordProfile();
            bin1.setName("Walking");
            bin1.setDistanceInterval(100);
            bin1.setTimeInterval(5 * 60);
            bin1.setGpsAccuracy(10);
            listProfiles.add(bin1);

            BinRecordProfile bin2 = new BinRecordProfile();
            bin2.setName("Driving");
            bin2.setDistanceInterval(100);
            bin2.setTimeInterval(5 * 60);
            bin2.setGpsAccuracy(10);
            listProfiles.add(bin2);
        } else {
            listProfiles.addAll(convertStringToRecordProfileArray(listStr));
        }
        adapter.notifyDataSetChanged();
    }

//----------------------------------------- onActivity Result --------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PROFILE_ADD && resultCode == RESULT_OK && data != null) {
            if (data.getExtras().containsKey(Utility.key_profile_activity_data)) {
                BinRecordProfile bin = gson.fromJson(data.getExtras().getString(Utility.key_profile_activity_data), BinRecordProfile.class);
                listProfiles.add(bin);
                adapter.notifyDataSetChanged();
            }
        }
        else if (requestCode == REQUEST_CODE_PROFILE_UPDATE && resultCode == RESULT_OK && data != null) {
            if (data.getExtras().containsKey(Utility.key_is_profile_remove)) {
                if(data.getExtras().getBoolean(Utility.key_is_profile_remove))
                {
                    BinRecordProfile bin = gson.fromJson(data.getExtras().getString(Utility.key_profile_activity_data), BinRecordProfile.class);
                    int index = -1;
                    for (int i = 0; i < listProfiles.size(); i++) {
                        BinRecordProfile binTemp = listProfiles.get(i);
                        if (binTemp.getName().equalsIgnoreCase(bin.getName())) {
                            index = i;
                            break;
                        }
                    }
                    if (index >= 0) {
                        listProfiles.remove(index);
                    }
                    adapter.notifyDataSetChanged();
                }
                else if (data.getExtras().containsKey(Utility.key_profile_activity_data)) {
                    BinRecordProfile bin = gson.fromJson(data.getExtras().getString(Utility.key_profile_activity_data), BinRecordProfile.class);
                    int index = -1;
                    for (int i = 0; i < listProfiles.size(); i++) {
                        BinRecordProfile binTemp = listProfiles.get(i);
                        if (binTemp.getName().equalsIgnoreCase(bin.getName())) {
                            index = i;
                            break;
                        }
                    }
                    if (index >= 0) {
                        listProfiles.set(index, bin);
                    } else {
                        listProfiles.add(bin);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

//----------------------------------------- On Option Item Selected --------------------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
//----------------------------------------- On Pause --------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onPause() {
        super.onPause();
        Utility.saveData(mActivity, Utility.KEY_RECORD_PROFILES_LIST, gson.toJson(listProfiles));
    }
}
