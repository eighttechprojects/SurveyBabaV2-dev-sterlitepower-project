package com.surveybaba;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.android.volley.VolleyError;
import com.fileupload.AndroidMultiPartEntity;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikelau.croperino.Croperino;
import com.mikelau.croperino.CroperinoConfig;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.surveybaba.DTO.ImagSyncVo;
import com.surveybaba.DTO.PathVO;
import com.surveybaba.Database.DataBaseHelper;
import com.surveybaba.LocationAssist.LocationAssistant;
import com.surveybaba.Utilities.ImageFileUtils;
import com.surveybaba.Utilities.SystemPermission;
import com.surveybaba.Utilities.SystemUtility;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.BinTimeline;
import com.volly.BaseApplication;
import com.volly.URL_Utility;
import com.volly.URL_Utility.ResponseCode;
import com.volly.WSResponseInterface;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.surveybaba.MapsActivity.key_intent_timeline;

public class TimeLineActivity extends AppCompatActivity implements WSResponseInterface, LocationAssistant.Listener {

    private ProgressDialog progressDialog;
    long totalSize = 0;
    SystemPermission systemPermission;
    Activity mContext;
    ImageFileUtils imageFileUtils;
    private int REQUEST_TAKE_PHOTO = 222;
    private DataBaseHelper dataBaseHelper;
    private LocationAssistant assistant;
    private Location mCurrentLocation;
    private ArrayList<ImagSyncVo> imageUploadPath;
    private EditText edtDescription;
    private ImageView imgTimeline;
    private LinearLayout llTimeline;
    private Map<String, String> imageData;
    private ArrayList<PathVO> imagePathAll;
    private File destFile, destFileTemp;
    BinTimeline binTimeline;

//------------------------------------------------------- On Create ------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Add Back Button on ToolBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mContext = this;
        assistant = new LocationAssistant(this, this, LocationAssistant.Accuracy.HIGH, 0, false);
        assistant.setVerbose(true);
        dataBaseHelper = new DataBaseHelper(this);
        systemPermission = new SystemPermission(this);
        imageFileUtils = new ImageFileUtils();
        init();
    }


//------------------------------------------------------- Init ------------------------------------------------------------

    private void init() {
        imageData = new HashMap<String, String>();
        imagePathAll = new ArrayList<PathVO>();
        imageUploadPath = new ArrayList<ImagSyncVo>();
        //llTimeline = findViewById(R.id.llTimeline);
        imgTimeline = findViewById(R.id.imgTimeline);
        edtDescription = findViewById(R.id.edtDescription);

        imgTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String imageStoragePath = "Timeline"+ "_" + getDateDDMMYYYY();
//                showPictureDialog(v.getTag(), imageStoragePath);
                if (systemPermission.isExternalStorage()) {
                    if (systemPermission.isCamera()) {
                        takePhoto();
                    }
                }
            }
        });
    }

//------------------------------------------------------- MENU ------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timeline_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;



//            case R.id.ic_submit:
//                if(destFile == null) {
//                    Utility.showInfoDialog(TimeLineActivity.this, getResources().getString(R.string.select_timeline_image));
//                    return false;
//                }
//                if(Utility.isEmptyString(edtDescription.getText().toString().trim())) {
//                    Utility.showInfoDialog(TimeLineActivity.this, getResources().getString(R.string.type_your_description));
//                    return false;
//                }
//                imageUpload();
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }


//------------------------------------------------------- take photo ------------------------------------------------------------

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        destFileTemp = imageFileUtils.getDestinationFile(imageFileUtils.getRootDirFile(mContext));
        Uri photoURI = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", destFileTemp);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
    }

//    private String LAST_SAVED_TAG = "";
//    private void showPictureDialog(final Object getTag, String fileName) {
//        LAST_SAVED_TAG = (String) getTag;
//        captureImage(fileName);
//    }

    /*private void requestCameraPermission(final int type) {
        Dexter.withActivity(this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    captureImage(fileName);
                } else if (report.isAnyPermissionPermanentlyDenied()) {
                    showPermissionsAlert();
                }
            }
            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }*/
//------------------------------------------------------- Show Permission Alert ------------------------------------------------------------

    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions required!");
        builder.setMessage("Camera needs few permissions to work properly. Grant them in settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

//    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
//    private static String imageStoragePath;
//    private void captureImage(String fileName) {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (fileName != null) {
//            imageStoragePath = fileName;
//        }
//        Uri fileUri = Uri.fromFile(new File(fileName));
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
//    }
//
//    private void previewCapturedImage(String from) {
//        try {
//            imageData.put(LAST_SAVED_TAG, imageStoragePath);
//            imagePathAll.add(new PathVO(imageStoragePath, imageStoragePath));
//            Picasso.with(TimeLineActivity.this).load(new File(imageStoragePath)).networkPolicy(NetworkPolicy.NO_CACHE).networkPolicy(NetworkPolicy.OFFLINE).into(imgTimeline);
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//    }

//------------------------------------------------------- on Activity Result ------------------------------------------------------------

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                Croperino.runCropImage(destFileTemp, mContext, true, 1, 1, R.color.gray, R.color.gray_variant);
            }
        }
        else if(requestCode == CroperinoConfig.REQUEST_CROP_PHOTO) {
            try {
//                destFile = imageFileUtils.getDestinationFile(imageFileUtils.getRootDirFile(mContext));
//                Bitmap bitmap = imageFileUtils.getBitmapAspectRatio(destFileTemp, destFile);
                destFile = destFileTemp;
                imgTimeline.setImageBitmap(imageFileUtils.getBitmapFromFilePath(destFile.getAbsolutePath()));
//                Picasso.with(TimeLineActivity.this).load(destFile).networkPolicy(NetworkPolicy.NO_CACHE).networkPolicy(NetworkPolicy.OFFLINE).into(imgTimeline);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

//------------------------------------------------------- Image Upload ------------------------------------------------------------

    private void imageUpload() {
        progressDialog = new ProgressDialog(TimeLineActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.lbl_File_upload));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();
        new FileUploadToServe().execute();
    }

//------------------------------------------------------- process To Time Line Data ------------------------------------------------------------

    private void processToTimeLineData() {
        String dtTime = getDateDDMMYYYY();
        Map<String, String> params = new HashMap<String, String>();
        ResponseCode responseCode = ResponseCode.WS_UPLOAD_TIMELINE;
        params.put(URL_Utility.PARAM_LOGIN_USER_ID, Utility.getSavedData(TimeLineActivity.this, Utility.LOGGED_USERID));
        params.put(URL_Utility.ACTION_APP_VERSION, URL_Utility.APP_VERSION);
        params.put(URL_Utility.PARAM_IMAGE_NAME, destFile.getName());
        params.put(URL_Utility.PARAM_UPLOAD_PATH, destFile.getAbsolutePath());
        params.put(URL_Utility.PARAM_CREATED_DATE, dtTime);
        params.put(URL_Utility.PARAM_DESCRIPTION, edtDescription.getText().toString().trim());
        binTimeline = new BinTimeline();
        binTimeline.setUserId(Utility.getSavedData(TimeLineActivity.this, Utility.LOGGED_USERID));
        binTimeline.setAppVersion(URL_Utility.APP_VERSION);
        binTimeline.setImageFilePath(destFile.getAbsolutePath());
        binTimeline.setDescription(edtDescription.getText().toString().trim());
        binTimeline.setLat(mCurrentLocation.getLatitude());
        binTimeline.setLongi(mCurrentLocation.getLongitude());
        binTimeline.setRecordDate(dtTime);
        BaseApplication.getInstance().makeHttpPostRequest(TimeLineActivity.this, responseCode, URL_Utility.WS_UPLOAD_TIMELINE, params, false, false);
    }

//------------------------------------------------------- Date Time ------------------------------------------------------------

    private String getDateDDMMYYYY() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
    }

    private String getDateDDMMYYYYHHMMSS() {
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US).format(new Date());
    }


//------------------------------------------------------- On Error Response ------------------------------------------------------------

    @Override
    public void onSuccessResponse(ResponseCode responseCode, String response) {
        if (responseCode == ResponseCode.WS_UPLOAD_TIMELINE) {
            confimationMessage();
        }
    }

//------------------------------------------------------- On Error Response ------------------------------------------------------------

    @Override
    public void onErrorResponse(ResponseCode responseCode, VolleyError error) {
        Utility.showInfoDialog(TimeLineActivity.this, error.toString());
    }


//------------------------------------------------------- Redirect ------------------------------------------------------------

    private void reDirectTimeLine() {
        Intent intent = new Intent();
        if(binTimeline!=null)
            intent.putExtra(key_intent_timeline, new Gson().toJson(binTimeline));
        setResult(RESULT_OK, intent);
        finish();
    }

//-------------------------------------------------------  ------------------------------------------------------------

    private void confimationMessage() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TimeLineActivity.this);
        alertDialog.setMessage(getResources().getString(R.string.lbl_Timeline_Add_Successfully));
        alertDialog.setPositiveButton(getResources().getString(R.string.lbl_Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onClickPointSave();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void onClickPointSave() {
        LatLng mSavepoint = new LatLng(binTimeline.getLat(), binTimeline.getLongi());
        dataBaseHelper.open();
        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("latitude", mSavepoint.latitude);
            jsonObject.put("longitude", mSavepoint.longitude);
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dataBaseHelper.insertGeomDetails(getRandomNoFromDate(), binTimeline.getUserId(), binTimeline.getImageFilePath(), "", jsonArray.toString(), ""+binTimeline.getLat(),
                ""+binTimeline.getLongi(), "0", binTimeline.getRecordDate(), binTimeline.getDescription(), "", 0);

        dataBaseHelper.close();
        reDirectTimeLine();
    }

    private String getRandomNoFromDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss", Locale.US);
        String s = formatter.format(date);
        return s;
    }

    private String getRecordDate()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(c.getTime());
    }


//------------------------------------------------------- Location ------------------------------------------------------------

    @Override
    public void onNeedLocationPermission() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.app_name);
        dialogBuilder.setMessage("Need\nPermission");
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                assistant.requestLocationPermission();
            }
        });
        dialogBuilder.setCancelable(true);
        dialogBuilder.show();
        assistant.requestAndPossiblyExplainLocationPermission();
    }

    @Override
    public void onExplainLocationPermission() {
        assistant.requestLocationPermission();
    }

    @Override
    public void onLocationPermissionPermanentlyDeclined(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {
        new AlertDialog.Builder(this).setMessage(R.string.permissionPermanentlyDeclined).setPositiveButton(R.string.lbl_Ok, fromDialog).show();
    }

    @Override
    public void onNeedLocationSettingsChange() {
        new AlertDialog.Builder(this).setMessage(R.string.switchOnLocationShort).setPositiveButton(R.string.lbl_Ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                assistant.changeLocationSettings();
            }
        }).show();
    }

    @Override
    public void onFallBackToSystemSettings(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.switchOnLocationLong)
                .setPositiveButton(R.string.lbl_Ok, fromDialog)
                .show();
    }

    @Override
    public void onNewLocationAvailable(Location location) {
        if (location == null) return;
        mCurrentLocation = location;
//        mCurrentLocation = new Location("");
//        mCurrentLocation.setLatitude(23.038300);
//        mCurrentLocation.setLongitude(72.512396);
    }

    @Override
    public void onMockLocationsDetected(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

    }

    @Override
    public void onError(LocationAssistant.ErrorType type, String message) {}

//------------------------------------------------------- On Resume ------------------------------------------------------------

    @Override
    protected void onResume() {
        super.onResume();
        SystemUtility.setFullscreenToggle(this, Utility.getBooleanSavedData(this, Utility.IS_FULL_SCREEN));
        SystemUtility.setKeepScreenAwakeAlwaysToggle(this, Utility.getBooleanSavedData(this, Utility.IS_ALWAYS_KEEP_SCREEN_AWAKE));
        assistant.start();
    }

    //------------------------------------------------------- On Pause ------------------------------------------------------------
    @Override
    protected void onPause() {
        assistant.stop();
        super.onPause();
    }

//------------------------------------------------------- On back Pressed ------------------------------------------------------------

    @Override
    public void onBackPressed() {
        reDirectTimeLine();
    }

//------------------------------------------------------- File Upload to Server ------------------------------------------------------------

    private class FileUploadToServe extends AsyncTask<Void, Integer, String> {

        public FileUploadToServe() { }

        @Override
        protected void onPreExecute() {
            progressDialog.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressDialog.setProgress(progress[0]);
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL_Utility.BASE_IMAGE_URL);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                });
                File sourceFile = new File(destFile.getPath());
                entity.addPart(URL_Utility.PARAM_IMAGE, new FileBody(sourceFile));
                entity.addPart(URL_Utility.PARAM_UNIQ_ID, new StringBody(getDateDDMMYYYY()));
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: " + statusCode;
                }
            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            processToTimeLineData();
            super.onPostExecute(result);
        }
    }


}
