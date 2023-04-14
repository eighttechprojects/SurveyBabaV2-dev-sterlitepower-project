package com.surveybaba;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.zxing.Result;
import com.surveybaba.ADAPTER.FormRenderAdapter;
import com.surveybaba.Utilities.Utility;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {


    public static final String TAG ="BarCodeActivity";
    ZXingScannerView scannerView;

//------------------------------------------------------- onCreate ----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
    }

//------------------------------------------------------- handleResult ----------------------------------------------------------------------------------------------------------------------

    @Override
    public void handleResult(Result result) {
        Log.e(TAG, "BarCode Result: "+result.getText());
        Utility.saveData(this,Utility.BarCodeResult,result.getText());
        setResult(RESULT_OK);
        finish();
        //onBackPressed();
    }

//------------------------------------------------------- MENU ----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();
        if (itemID == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        return false;
    }

//------------------------------------------------------- onResume ----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

//------------------------------------------------------- onPause ----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }


}