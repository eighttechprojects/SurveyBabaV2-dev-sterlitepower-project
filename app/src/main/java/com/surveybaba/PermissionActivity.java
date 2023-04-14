package com.surveybaba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.surveybaba.DashBoard.DashBoardBNActivity;
import com.surveybaba.Utilities.Utility;

public class PermissionActivity extends AppCompatActivity {

    Button permissionApproveButton;
    private static final int RequestPermissionCode = 1;

//------------------------------------------------------- On Create ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        // Button
        permissionApproveButton = findViewById(R.id.permissionApproveButton);

        if(isAllPermissionGranted()){
            reDirectScreen();
        }

        permissionApproveButton.setOnClickListener(view -> {
            // permission
            ActivityCompat.requestPermissions(this, new String[]{
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_NETWORK_STATE}, RequestPermissionCode);
        });

    }

    private boolean isAllPermissionGranted(){

        boolean isStorage  = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean isCamera   = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean isLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        return isLocation && isStorage && isCamera;
    }

//------------------------------------------------------- Re Direct ---------------------------------------------------------------------------------------------------------------------------

    private void reDirectScreen() {
                if (Utility.getBooleanSavedData(this, Utility.IS_USER_SUCCESSSFULLY_LOGGED_IN)) {
                    reDirectToDashBoard();
                } else {
                    reDirectLogin();
                }
    }

    private void reDirectLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void reDirectToDashBoard(){
        Intent intent = new Intent(this, DashBoardBNActivity.class);
        startActivity(intent);
        finish();
    }

    private void redirectToPermissionSettings(Activity mContext) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        mContext.startActivity(intent);
    }

//------------------------------- On Request permission Result --------------------------------------------------------------------------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED
                    && grantResults[4] == PackageManager.PERMISSION_GRANTED
                    && grantResults[5] == PackageManager.PERMISSION_GRANTED) {
                reDirectScreen();
            }
            else
            {
                redirectToPermissionSettings(this);
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


}