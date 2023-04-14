package com.surveybaba;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.surveybaba.DashBoard.DashBoardBNActivity;
import com.surveybaba.Utilities.Utility;

public class DashBoardSplashActivity extends AppCompatActivity {


//---------------------------------------------------------- onCreate ------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_splash);
        // Hide Action Bar
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        new Handler().postDelayed(() -> {
            try {
                if(Utility.getBooleanSavedData(this, Utility.IS_USER_SUCCESSSFULLY_LOGGED_IN)) {
                    reDirectToDashBoard();
                }
                else {
                    reDirectPermission();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1500);

    }
//---------------------------------------------------------- reDirect ------------------------------------------------------------


    private void reDirectToDashBoard() {
        Intent intent = new Intent(this, DashBoardBNActivity.class);
        startActivity(intent);
        finish();
    }


    private void reDirectPermission() {
        Intent intent = new Intent(this, PermissionActivity.class);
        startActivity(intent);
        finish();
    }

}