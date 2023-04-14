package com.duy.compass;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.duy.compass.fragments.CompassFragment;
import com.duy.compass.sensor.SensorUtil;
import com.duy.compass.view.CustomViewPager;

public class MainCompassActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_main);
        if (hasSensor()) {
            createView();
        } else {
            showDialogUnsupported();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_satellite_compass, menu);
        menu.findItem(R.id.ic_satellite).setVisible(false);
        menu.findItem(R.id.ic_compass).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        } else if (itemId == R.id.ic_satellite) {
            finish();
        }
        return true;
    }

    private void createView() {
        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.view_pager);
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new CompassFragment();
                }
                return null;
            }
        };
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
    }

    private boolean hasSensor() {
        return SensorUtil.hasAccelerometer(this) && SensorUtil.hasMagnetometer(this);
    }

    private void showDialogUnsupported() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your device unsupported Accelerometer sensor and Magnetometer");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).create().show();
    }
}
