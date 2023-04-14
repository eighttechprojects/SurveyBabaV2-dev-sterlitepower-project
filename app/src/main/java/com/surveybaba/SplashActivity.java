package com.surveybaba;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.surveybaba.DashBoard.DashBoardBNActivity;
import com.surveybaba.Utilities.Utility;
import java.util.Objects;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Objects.requireNonNull(getSupportActionBar()).hide();

            new Handler().postDelayed(() -> {
                try {
                    if (Utility.getBooleanSavedData(SplashActivity.this, Utility.IS_USER_SUCCESSSFULLY_LOGGED_IN)) {
                        reDirectToDashBoard();
                    }
                    else {
                        reDirectPermission();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 1000);

    }


//------------------------------- Re Direct --------------------------------------------------------------------------------------

    private void reDirectScreen() {
        new Handler().postDelayed(() -> {
            try {
                if (Utility.getBooleanSavedData(SplashActivity.this, Utility.IS_USER_SUCCESSSFULLY_LOGGED_IN)) {
                    reDirectToDashBoard();
                } else {
                    reDirectLogin();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1000);
    }

    private void reDirectLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
    }

    private void reDirectPermission() {
        Intent intent = new Intent(SplashActivity.this, PermissionActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
    }


    private void reDirectToDashBoard(){
        Intent intent = new Intent(SplashActivity.this, DashBoardBNActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
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
                Toast.makeText(SplashActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
