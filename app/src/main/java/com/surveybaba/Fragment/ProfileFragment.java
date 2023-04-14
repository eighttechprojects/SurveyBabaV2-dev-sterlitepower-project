package com.surveybaba.Fragment;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.surveybaba.ADAPTER.AdapterSurveyMarker;
import com.surveybaba.AESCrypt.AESCrypt;
import com.surveybaba.DashBoard.DashBoardBNActivity;
import com.surveybaba.FormBuilder.FormDetailData;
import com.surveybaba.LocationAssist.LocationAssistant;
import com.surveybaba.R;
import com.surveybaba.Utilities.SystemPermission;
import com.surveybaba.Utilities.SystemUtility;
import com.surveybaba.Utilities.Utility;
import com.volly.BaseApplication;
import com.volly.URL_Utility;
import com.volly.WSResponseInterface;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import uk.co.senab.photoview.PhotoViewAttacher;


public class ProfileFragment extends Fragment implements WSResponseInterface,LocationAssistant.Listener {

    // TAG
    private static final String TAG = "ProfileFragment";
    // View
    protected View view;
    // AppCompat Activity
    AppCompatActivity activity;
    // Toolbar
    protected Toolbar toolbar;
    // Context
    Context context;
    // ProgressDialog
    private ProgressDialog progressDialog;
    // EditText
    private EditText edtFirstname, edtLastname,edtEmailID ,edtMobileNumber;
    private ImageView profileImageView;
    // PhotoView Attacher
    PhotoViewAttacher photoViewAttacher;
    // Camera Permission CODE
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION = 11;
    // Intent CODE
    private static final int CAMERA_CODE = 110;
    private static final int IMAGE_PICKER_FOR_GALLERY_CODE = 12;
    // ALERT Message
    public static final String NEED_CAMERA_PERMISSION_ALERT   = "Need Camera Permission to Access Camera. \n\nGo to Setting -> App info -> SurveyBaba -> Permissions -> Allow Camera";
    public static final String NEED_STORAGE_PERMISSION_ALERT  = "Need Storage Permission to Store File. \n\nGo to Setting -> App info -> SurveyBaba -> Permissions -> Allow Storage";

    boolean isProfileEdit = false;
    // Floating Button
    private FloatingActionButton floatingActionButton;
    String profileImageBase64 = "";
    String imageStoragePath;
    File cameraPhotoFile = null;

    private LocationAssistant assistant;
    BaseApplication baseApplication;

    boolean isNewPasswordVisible = false;
    boolean isConfirmPasswordVisible = false;

//------------------------------------------------------- Constructor ------------------------------------------------------------------------------------------------------------------

    public ProfileFragment() {
        // Required empty public constructor
    }

//------------------------------------------------------- On Create ------------------------------------------------------------------------------------------------------------------

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

//------------------------------------------------------- On Attrach ------------------------------------------------------------------------------------------------------------------

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context =context;
    }

//------------------------------------------------------- On CreateView ------------------------------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=  inflater.inflate(R.layout.fragment_profile, container, false);
        // Custom Tool Bar
        toolbar = view.findViewById(R.id.profile_toolbar);
        activity = (AppCompatActivity) getActivity();
        assert activity != null;
        activity.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_arrow_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), DashBoardBNActivity.class));
            }
        });

        // Init
        init(view);
        // Bottom Navigation View
        BottomNavigationView navView = view.findViewById(R.id.nav_view);
        navView.setBackground(null);

        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_dash_board_bnactivity);
        NavigationUI.setupWithNavController(navView, navController);

        baseApplication = (BaseApplication) requireActivity().getApplication();
        // Location
        assistant = new LocationAssistant(getContext(), this, LocationAssistant.Accuracy.HIGH, 0, false);
        assistant.setVerbose(true);

        SystemPermission systemPermission = new SystemPermission(requireActivity());
        if(systemPermission.isLocation()){
            baseApplication.startMyService();
        }
        // Return View
        return view;
    }

//------------------------------------------------------- Init ------------------------------------------------------------------------------------------------------------------

    public void init(View view) {
        // TextView init
        edtFirstname = view.findViewById(R.id.profileFirstName);
        edtLastname  = view.findViewById(R.id.profileLastName);
        edtEmailID = view.findViewById(R.id.profileEmailID);
        edtMobileNumber = view.findViewById(R.id.profileMobileNumber);
        // ImageView
        profileImageView  =view.findViewById(R.id.profileImage);
        // Floating Button
        floatingActionButton = view.findViewById(R.id.mfloatingbutton);

        // Set Save Data
        edtFirstname.setText(Utility.getSavedData(requireActivity(),    Utility.PROFILE_FIRSTNAME));
        edtLastname.setText(Utility.getSavedData(requireActivity(),     Utility.PROFILE_LASTNAME));
        edtEmailID.setText(Utility.getSavedData(requireActivity(),      Utility.PROFILE_EMAILID));
        edtMobileNumber.setText(Utility.getSavedData(requireActivity(), Utility.PROFILE_MOBILE_NUMBER));
        // Profile Image
        try{
            // Set profile Image
            Glide.with(requireContext()).load(decodeBase64Image(Utility.getSavedData(requireContext(), Utility.PROFILE_IMAGE))).error(R.drawable.ic_login_user_icon).circleCrop().into(profileImageView);
        }
        catch (Exception e){
            // Set profile Image
            Glide.with(requireContext()).load(R.drawable.ic_login_user_icon).circleCrop().into(profileImageView);
            Log.e(TAG, e.getMessage());
        }

        isProfileEdit(false);

//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Toast.makeText(requireContext(), "float button", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

//------------------------------------------------------- is Profile Edit ------------------------------------------------------------------------------------------------------------------

    public void isProfileEdit(boolean enable) {
        edtFirstname.setEnabled(enable);
        edtLastname.setEnabled(enable);
        edtMobileNumber.setEnabled(enable);
        edtEmailID.setEnabled(false);
        edtEmailID.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorDarkGery));

        // profile is visible then
        if(enable){
            edtFirstname.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            edtLastname.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            //edtEmailID.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            edtMobileNumber.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            // when User Click on Profile pick
            profileImageView.setOnClickListener(view1 -> pickProfileImage());
        }
        else{
            edtFirstname.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorDarkGery));
            edtLastname.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorDarkGery));
            //edtEmailID.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorDarkGery));
            edtMobileNumber.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorDarkGery));

            // when User Click on Profile pick
            profileImageView.setOnClickListener(view1 ->{
                try{
                    Bitmap bitmap = (decodeBase64Image(Utility.getSavedData(requireActivity(),Utility.PROFILE_IMAGE)));
                    zoomImageOption(bitmap);
                }
                catch (Exception e){
                    //Bitmap bitmap = (decodeBase64Image(Utility.getSavedData(requireActivity(),Utility.PROFILE_IMAGE)));
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_login_user_icon);
                    zoomImageOption(bitmap);
                }
            });

        }


    }

//------------------------------------------------------- Profile Save ------------------------------------------------------------------------------------------------------

    private void profileSave(){
        // Internet Connected!
        if(SystemUtility.isInternetConnected(context)){
            // Progress Bar Show
            showProgressBar();

            Map<String, String> params1 = new HashMap<>();
            String data = "";

            JSONObject params = new JSONObject();
            try {
                params.put(URL_Utility.PARAM_LOGIN_USER_ID, Utility.getSavedData(requireContext(), Utility.LOGGED_USERID));
                params.put(URL_Utility.PARAM_FIRST_NAME,    edtFirstname.getText().toString());
                params.put(URL_Utility.PARAM_LAST_NAME,     edtLastname.getText().toString());
                //params.put(URL_Utility.PARAM_EMAIL_ID,    edtEmailID.getText().toString());
                // Login Token
                params.put(URL_Utility.PARAM_LOGIN_TOKEN,Utility.getSavedData(requireActivity(),Utility.LOGGED_TOKEN));
                params.put(URL_Utility.PARAM_MOBILE_NUMBER, edtMobileNumber.getText().toString());
                params.put(URL_Utility.PARAM_PROFILE_IMAGE, profileImageBase64);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(TAG, params.toString());
            try {
                data =  AESCrypt.encrypt(params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            params1.put("data",data);
            URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_PROFILE_UPDATE;
            BaseApplication.getInstance().makeHttpPostRequest(ProfileFragment.this, responseCode, URL_Utility.WS_PROFILE_UPDATE, params1, false, false);
        }
        else{
            Utility.getOKCancelDialogBox(context, "Connection Error", "Need Internet Connection to Upload Profile", DialogInterface::dismiss);
            dismissProgressBar();
        }
    }

//------------------------------------------------------- On Create Options Menu ------------------------------------------------------------------------------------------------------------------

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_edit_menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

//------------------------------------------------------- On OptionsItem Selected ------------------------------------------------------------------------------------------------------

    // Edit button on menu!
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Profile Edit
        if (item.getItemId() == R.id.profileEditButton) {

            if(!isProfileEdit){
                Objects.requireNonNull(activity.getSupportActionBar()).setTitle("Profile Edit");
                isProfileEdit = true;
                isProfileEdit(isProfileEdit);
            }
            else{
                Objects.requireNonNull(activity.getSupportActionBar()).setTitle("Profile ");
                isProfileEdit = false;
                isProfileEdit(isProfileEdit);
            }
        }

        // Profile Save
        if(item.getItemId() == R.id.profileSaveButton){
            if(isProfileEdit){
                Utility.getYesNoDialogBox(context, "Save", "Are you sure you want to save this profile?", dialog -> profileSave());
            }
        }
        // Profile Change Password
        if(item.getItemId() == R.id.profileChangePasswordButton){
//            if(SystemUtility.isInternetConnected(requireContext())){
//                profileChangePassword();
//            }
//            else{
//                Utility.getOKCancelDialogBox(context, "Connection Error", "Need Internet Connection to Change Password", DialogInterface::dismiss);
//            }
            profileChangePassword();

        }

        return false;
    }

//------------------------------------------------------- On Success Response ------------------------------------------------------------------------------------------------------

    @Override
    public void onSuccessResponse(URL_Utility.ResponseCode responseCode, String response) {
        Log.e("http", "responseCode: " + responseCode);
        Log.e("http", "response: " + response);

        if (responseCode == URL_Utility.ResponseCode.WS_PROFILE_UPDATE){
            String res = AESCrypt.decryptResponse(response);
            if(!res.equals("")){
                try {
                    JSONObject mBoundaryObj = new JSONObject(res);
                    String msg = mBoundaryObj.optString("status");
                    if (msg.equalsIgnoreCase("Success")) {
                        // Save Data
                        Utility.saveData(requireContext(),Utility.PROFILE_FIRSTNAME, edtFirstname.getText().toString());
                        Utility.saveData(requireContext(),Utility.PROFILE_LASTNAME, edtLastname.getText().toString());
                        //Utility.saveData(requireContext(),Utility.PROFILE_EMAILID, edtEmailID.getText().toString());
                        Utility.saveData(requireContext(),Utility.PROFILE_MOBILE_NUMBER, edtMobileNumber.getText().toString());
                        Utility.saveData(requireActivity(),Utility.PROFILE_IMAGE, profileImageBase64);
                        // Set Save Data
                        edtFirstname.setText(Utility.getSavedData(requireActivity(),    Utility.PROFILE_FIRSTNAME));
                        edtLastname.setText(Utility.getSavedData(requireActivity(),     Utility.PROFILE_LASTNAME));
                        //edtEmailID.setText(Utility.getSavedData(requireActivity(),      Utility.PROFILE_EMAILID));
                        edtMobileNumber.setText(Utility.getSavedData(requireActivity(), Utility.PROFILE_MOBILE_NUMBER));
                        // Set Profile Image
                        try{
                            // Set profile Image
                            Glide.with(requireContext()).load(decodeBase64Image(Utility.getSavedData(requireContext(), Utility.PROFILE_IMAGE))).error(R.drawable.ic_login_user_icon).circleCrop().into(profileImageView);
                        }
                        catch (Exception e){
                            // Set profile Image
                            Glide.with(requireContext()).load(R.drawable.ic_login_user_icon).circleCrop().into(profileImageView);
                            Log.e(TAG, e.getMessage());
                        }
                        //Glide.with(requireContext()).load((decodeBase64Image(Utility.getSavedData(requireActivity(),Utility.PROFILE_IMAGE)))).circleCrop().error(R.drawable.ic_login_user_icon).into(profileImageView);
                        // Dialog Box
                        Utility.getOKDialogBox(context, "Profile Save", "Profile Upload Successfully", dialog -> {
                            Objects.requireNonNull(activity.getSupportActionBar()).setTitle("Profile");
                            isProfileEdit = false;
                            isProfileEdit(isProfileEdit);
                        });
                        // Progress Bar Close
                        dismissProgressBar();
                        Log.e(TAG, msg);
                    }
                    else {
                        Log.e(TAG,msg);
                        dismissProgressBar();
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(requireContext(), "response blank", Toast.LENGTH_SHORT).show();
                dismissProgressBar();
            }


        }

        // Profile Change password
        if (responseCode == URL_Utility.ResponseCode.WS_PROFILE_PASSWORD_CHANGE){

            String res = AESCrypt.decryptResponse(response);
            if(!res.equals("")){
                try {
                    JSONObject mBoundaryObj = new JSONObject(res);
                    String msg = mBoundaryObj.optString("status");
                    if (msg.equalsIgnoreCase("Success")) {
                        dismissProgressBar();
                        Utility.getOKDialogBox(context, "Password", "Profile Password Change Successfully", dialog -> {
                            Objects.requireNonNull(activity.getSupportActionBar()).setTitle("Profile");
                            isProfileEdit = false;
                            isProfileEdit(isProfileEdit);
                            dialog.dismiss();
                        });
                        Log.e(TAG, msg);
                    }
                    else {
                        Log.e(TAG,msg);
                        dismissProgressBar();
                        Utility.getOKDialogBox(context, "Password", "Profile Password Change not Successfully", dialog -> {
                            Objects.requireNonNull(activity.getSupportActionBar()).setTitle("Profile");
                            isProfileEdit = false;
                            isProfileEdit(isProfileEdit);
                            dialog.dismiss();
                        });
                    }
                } catch (JSONException e) {
                    Log.e(TAG,e.getMessage());
                    e.printStackTrace();
                }
            }
            else{
                dismissProgressBar();
            }
        }
    }

//------------------------------------------------------- On Error Response ------------------------------------------------------------------------------------------------------

    @Override
    public void onErrorResponse(URL_Utility.ResponseCode responseCode, VolleyError error) {
        try{
            String res = AESCrypt.decryptResponse(error.getMessage());
            Log.e(TAG,res);
            Utility.showInfoDialog(getContext(), error.getMessage());
            dismissProgressBar();
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }


//------------------------------------------------------- Zoom Image Option ------------------------------------------------------------------------------------------------------

    public void zoomImageOption(Bitmap bitmap) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.profileimage_zoom_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView imageView = dialog.findViewById(R.id.dialogbox_image);
        photoViewAttacher = new PhotoViewAttacher(imageView);
        Glide.with(this).load(bitmap).error(R.drawable.ic_login_user_icon).into(imageView);
        photoViewAttacher.update();
        dialog.show();
    }

//------------------------------------------------------- Pick Profile Image ------------------------------------------------------------------------------------------------------

    private void pickProfileImage() {
        String[] options = {"Camera", "Gallery","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose");
        builder.setIcon(R.drawable.ic_logo_surveybaba);
        builder.setItems(options, (dialog, i) -> {
            if (options[i].equals("Camera")) {
                cameraPermission();
                dialog.dismiss();
            }
            else if (options[i].equals("Gallery")) {
                if(isStoragePermissionGranted()){
                    imagePickFormGallery();
                }
                else{
                    requestPermissionsStorage();
                }
                dialog.dismiss();
            }
            else {
                dialog.dismiss();
            }
        });
        builder.show();
    }

//------------------------------------------------------- Camera ------------------------------------------------------------------------------------------------------

    private void cameraPermission(){
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
        else
        {
            openCamera();
        }

    }
    // Camera open!
    private void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String  image_file_name = "profileImage"+ ".jpg";
        cameraPhotoFile = getPhotoFileUri(image_file_name);
        Uri imageuri = FileProvider.getUriForFile(context,"com.surveybaba.provider",cameraPhotoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
        startActivityForResult(intent,CAMERA_CODE);
    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ProfileImage");
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            mediaStorageDir.mkdirs();
            Log.d("Photo File", "failed to create directory");
        }
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

//------------------------------------------------------- Gallery ------------------------------------------------------------------------------------------------------

    private boolean isStoragePermissionGranted(){
        boolean read_external_storage_permission  = (ContextCompat.checkSelfPermission(requireContext(), READ_EXTERNAL_STORAGE)  == PackageManager.PERMISSION_GRANTED);
        boolean write_external_storage_permission = (ContextCompat.checkSelfPermission(requireContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        return read_external_storage_permission && write_external_storage_permission;
    }

    public  void requestPermissionsStorage(){
        ActivityCompat.requestPermissions(requireActivity(),new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION);
    }

    private void imagePickFormGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent,IMAGE_PICKER_FOR_GALLERY_CODE);
    }

//------------------------------------------------------- On Request Permission Result ------------------------------------------------------------------------------------------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Camera Permission
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Permission Granted
                openCamera();
            }
            else{
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.ic_logo_surveybaba)
                        .setTitle("Permission Alert")
                        .setMessage(NEED_CAMERA_PERMISSION_ALERT)
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, i) -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        })
                        .create();
                alertDialog.show();
            }
        }

        // Storage Permission
        if(requestCode == STORAGE_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                imagePickFormGallery();
            }
            else{
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.ic_logo_surveybaba)
                        .setTitle("Permission Alert")
                        .setMessage(NEED_STORAGE_PERMISSION_ALERT)
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, i) -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        })
                        .create();
                alertDialog.show();
            }
        }

    }

//------------------------------------------------------- On Activity Result ------------------------------------------------------------------------------------------------------

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Camera
        if(requestCode == CAMERA_CODE){

            if(resultCode == RESULT_OK){
                if(cameraPhotoFile != null){
                    File file = new File(cameraPhotoFile.getAbsolutePath());
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                    // initialize byte stream
                    ByteArrayOutputStream stream=new ByteArrayOutputStream();
                    // compress Bitmap
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    // Initialize byte array
                    byte[] bytes=stream.toByteArray();
                    // get base64 encoded string
                    profileImageBase64 = Base64.encodeToString(bytes,Base64.DEFAULT);
                    // Decode
                    Bitmap bitmap1 = decodeBase64Image(profileImageBase64);
                    // Set Image to Profile
                    Glide.with(requireContext()).load(bitmap1).error(R.drawable.ic_login_user_icon).circleCrop().into(profileImageView);
                }
            }
        }
        // Gallery
        if(requestCode == IMAGE_PICKER_FOR_GALLERY_CODE){

            if(resultCode == RESULT_OK){
                if(data != null){
                    Uri uriGallery = data.getData();
                    profileImageBase64 = encodeBase64Image(uriGallery);
                    // Save Image
                    //Utility.saveData(getContext(),Utility.PROFILE_IMAGE, profileImageBase64);
                    // Decode
                    Bitmap bitmap = decodeBase64Image(profileImageBase64);
                    // Set Image to Profile
                    Glide.with(requireContext()).load(bitmap).error(R.drawable.ic_login_user_icon).circleCrop().into(profileImageView);
                }
            }
        }
    }

//------------------------------------------------------- Encode/Decode 64 Base Image ------------------------------------------------------------------------------------------------------

    private String encodeBase64Image(Uri uri){
        try{
            Bitmap bitmap= MediaStore.Images.Media.getBitmap((requireActivity()).getContentResolver(),uri);
            // initialize byte stream
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            // compress Bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            // Initialize byte array
            byte[] bytes=stream.toByteArray();
            // get base64 encoded string
            return Base64.encodeToString(bytes,Base64.DEFAULT);

        }catch(IOException e){
            e.printStackTrace();
        }
        return "";
    }

    private Bitmap decodeBase64Image(String base64Image){
        // decode base64 string
        byte[] bytes=Base64.decode(base64Image,Base64.DEFAULT);
        // Initialize bitmap
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

//------------------------------------------------------- ProgressBar Show/ Dismiss ------------------------------------------------------------------------------------------------------

    private void dismissProgressBar() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void showProgressBar() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Profile Uploading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }else {
            progressDialog.setMessage("Profile Uploading...");
        }
    }

    private void showProgressBar(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(message);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }else {
            progressDialog.setMessage("Profile Uploading...");
        }
    }

//------------------------------------------------------- On Change Password ------------------------------------------------------------------------------------------------------

    @SuppressLint("ClickableViewAccessibility")
    private void profileChangePassword(){

        try{
            // Dialog Box
            Dialog dialog = new Dialog(requireContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.profile_change_password_view);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            // Edit Text
            //oldPasswordLayout
            TextView title = dialog.findViewById(R.id.headLineTitle);
            title.setText("Password Change");
            LinearLayout llOldPassword = dialog.findViewById(R.id.oldPasswordLayout);
            llOldPassword.setVisibility(View.VISIBLE);
            LinearLayout llEmailID = dialog.findViewById(R.id.emailIdLayout);
            llEmailID.setVisibility(View.GONE);
            EditText oldPassword = dialog.findViewById(R.id.oldPassword);
            EditText newPassword = dialog.findViewById(R.id.newPassword);
            EditText confirmPassword = dialog.findViewById(R.id.confirmPassword);
            // Submit Button
            Button profileChangeSubmitButton = dialog.findViewById(R.id.profileChangeSubmitButton);
            // Close Button
            Button profileChangeCloseButton = dialog.findViewById(R.id.profileChangeCloseButton);
            profileChangeCloseButton.setOnClickListener(view -> dialog.dismiss());
            // Submit Button
            profileChangeSubmitButton.setOnClickListener(view -> {

                if(!Utility.isEmptyString(oldPassword.getText().toString()) && !Utility.isEmptyString(newPassword.getText().toString()) && !Utility.isEmptyString(confirmPassword.getText().toString())) {

                    if(!newPassword.getText().toString().equals(confirmPassword.getText().toString())){
                        Toast.makeText(requireContext(), "password wrong", Toast.LENGTH_SHORT).show();
                        newPassword.setError("password not match");
                        confirmPassword.setError("password not match");
                    }
                    else{
                        if (SystemUtility.isInternetConnected(requireContext())){
                            showProgressBar("Changing Password...");
                            profilePasswordChangeSubmit(oldPassword,newPassword);
                        } else {
                            Utility.getOKCancelDialogBox(context, "Connection Error", "Need Internet Connection to Change Password", DialogInterface::dismiss);
                        }
                    }
                }
                else{
                    Toast.makeText(requireContext(), "Enter Field", Toast.LENGTH_SHORT).show();
                }

            });



            newPassword.setOnTouchListener((view, event) -> {
                final int Right = 2;
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(event.getRawX() >= newPassword.getRight() - newPassword.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = newPassword.getSelectionEnd();
                        if(isNewPasswordVisible){
                            newPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.icon_password_not_visible,0);
                            newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            isNewPasswordVisible = false;
                        }
                        else{
                            newPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.icon_password_visible,0);
                            newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isNewPasswordVisible = true;
                        }
                        newPassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            });

            confirmPassword.setOnTouchListener((view, event) -> {
                final int Right = 2;
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(event.getRawX() >= confirmPassword.getRight() - confirmPassword.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = confirmPassword.getSelectionEnd();
                        if(isConfirmPasswordVisible){
                            confirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.icon_password_not_visible,0);
                            confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            isConfirmPasswordVisible = false;
                        }
                        else{
                            confirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.icon_password_visible,0);
                            confirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isConfirmPasswordVisible = true;
                        }
                        confirmPassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            });


            // Dialog Box Show
            dialog.show();
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

    }


    private void profilePasswordChangeSubmit(EditText edtOldPassword, EditText edtNewPassword){
        //showProgressBar();

        Map<String, String> params1 = new HashMap<>();
        String data = "";

        JSONObject params = new JSONObject();
        try {
            params.put(URL_Utility.PARAM_LOGIN_USER_ID, Utility.getSavedData(requireContext(), Utility.LOGGED_USERID));
            params.put("current_password",  edtOldPassword.getText().toString());
            params.put("new_password",  edtNewPassword.getText().toString());
            //user_id
            //current_password
            //new_password
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, params.toString());
        try {
            data =  AESCrypt.encrypt(params.toString());
        } catch (Exception e) {
            dismissProgressBar();
            e.printStackTrace();
        }
        params1.put("data",data);
        URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_PROFILE_PASSWORD_CHANGE;
        BaseApplication.getInstance().makeHttpPostRequest(ProfileFragment.this, responseCode, URL_Utility.WS_PROFILE_PASSWORD_CHANGE, params1, false, false);
    }


    @Override
    public void onNeedLocationPermission() {

    }

    @Override
    public void onExplainLocationPermission() {

    }

    @Override
    public void onLocationPermissionPermanentlyDeclined(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

    }

    @Override
    public void onNeedLocationSettingsChange() {

    }

    @Override
    public void onFallBackToSystemSettings(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

    }

    @Override
    public void onNewLocationAvailable(Location location) {

    }

    @Override
    public void onMockLocationsDetected(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

    }

    @Override
    public void onError(LocationAssistant.ErrorType type, String message) {

    }


    //------------------------------------------------------- on Resume ---------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onResume() {
        super.onResume();
        assistant.start();

    }

//------------------------------------------------------- on pause ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onPause() {
        assistant.stop();
        super.onPause();
    }

}

