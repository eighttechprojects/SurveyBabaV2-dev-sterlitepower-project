package com.surveybaba.Utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLngBounds;
import com.surveybaba.BuildConfig;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.surveybaba.ADAPTER.AdapterMultiCheckDialog;
import com.surveybaba.ADAPTER.AdapterRecordingsDialog;
import com.surveybaba.BarCodeActivity;
import com.surveybaba.Database.DataBaseHelper;
import com.surveybaba.FormBuilder.FormDetailData;
import com.surveybaba.MapsActivity;
import com.surveybaba.R;
import com.surveybaba.model.BinLayerProject;
import com.surveybaba.model.BinTopPanel;
import com.surveybaba.model.FormLatLon;
import com.surveybaba.model.LabelTextModel;
import com.surveybaba.model.PointViewLayerModel.PointViewLayerModel;
import com.surveybaba.model.PolygonViewLayerModel.Geometry;
import com.surveybaba.model.PolygonViewLayerModel.PolygonViewLayerModel;
import com.surveybaba.model.PolylineViewLayerModel.PolylineViewLayerModel;
import com.surveybaba.model.SurveyFormLocalModel;
import com.surveybaba.setting.model.BinRecordProfile;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class Utility {

    public static final String BASE_ONLINE_LAYER  = "Base/Online Layer";
    public static final String ASSET_LAYER = "Asset Layer";

    public static final int VIEW_TYPE_BASE_ONLINE_LAYER = 1;
    public static final int VIEW_TYPE_ASSET_LAYER       = 2;

    public static final String VIEW_TYPE_BASE_ONLINE_LAYER_STR = "base-online-layer";
    public static final String VIEW_TYPE_ASSET_LAYER_STR       = "asset-layer";


    public static final int BARCODE_SCAN_CODE = 1001;
    // Internet Connection Constants
    public static final String NO_NETWORK_CONNECTED = "No Network Connected";
    public static final String NETWORK_CONNECTED = "Network Connected";
    // Bluetooth Connection
    public static final String BLUETOOTH_ON                        = "Bluetooth ON";
    public static final String BLUETOOTH_OFF                       = "Bluetooth OFF";
    public static final String BLUETOOTH_CONNECTING                = "Bluetooth Connecting...";
    public static final String BLUETOOTH_CONNECTED                 = "Bluetooth Connected";
    public static final String BLUETOOTH_DISCONNECTING             = "Bluetooth Disconnecting...";
    public static final String BLUETOOTH_DISCONNECTED              = "Bluetooth Disconnected";
    public static final String BLUETOOTH_DEVICE_FOUND              = "Bluetooth Device Found";
    public static final String BLUETOOTH_DEVICE_CONNECTED          = "Bluetooth Device Connected";
    public static final String BLUETOOTH_DEVICE_DISCONNECT_REQUEST = "Bluetooth Device Disconnecting...";
    public static final String BLUETOOTH_DEVICE_DISCONNECT         = "Bluetooth Device Disconnected";

    // Min Distance
    public static final String MIN_WALKING_DISTANCE = "min distance";

    public static final String STATE = "states";
    public static final String CITY  = "cities";
    public static final String isAdmin = "admin";

    public static final String BarCodeResult = "barcodeResult";

    // Change by Rahul Suthar
    public static final String TRACKING_DATE_FORMAT = "yyyy-MM-dd";
    public static final String TRACKING_TIME_FORMAT = "HH:mm";
    public static final String TRACKING_CREATED_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TRACKING_START_DATE  = "start_date";
    public static final String TRACKING_END_DATE    = "end_date";
    public static final String TRACKING_START_TIME  = "start_time";
    public static final String TRACKING_END_TIME    = "end_time";
    public static final String TRACKING_DISTANCE    = "distance";
    public static final String TRACKING_INTERVAL    = "interval";

    public static final String INSIDE_ZONE          = "zone";

    public static final String OLD_DATE = "old_date";

    public static final String PROJECT_WORK_ID = "project_work_id";
    public static final String PROJECT_PROJECT_ID = "project_project_id";
    public static final String PROJECT_PROJECT_NAME = "project_project_name";
    public static final String PROJECT_WORK_NAME = "project_work_name";
    // GIS Survey
    public static final String GIS_SURVEY_WORK_ID = "survey_work_id";
    public static final String GIS_SURVEY_ID = "survey_survey_id";
    public static final String GIS_SURVEY_NAME = "survey_survey_name";
    public static final String GIS_SURVEY_WORK_NAME = "survey_work_name";

    // Layer
    public static final String LAYER_ONLY_VIEW = "only_view";
    public static final String LAYER_POINT_ICON = "point_icon";
    public static final String LAYER_POINT_ICON_HEIGHT = "point_icon_height";
    public static final String LAYER_POINT_ICON_WIDTH = "point_icon_width";
    public static final String LAYER_LINE_COLOR = "layer_line_color";
    public static final String LAYER_LINE_TYPE = "layer_line_type";

    public static final String VIEW_LAYER_LINE_TYPE = "view_layer_line_type";

    public static final String TYPE_FILE   = "file";
    public static final String TYPE_CAMERA = "cameraUploader";
    public static final String TYPE_VIDEO  = "videoUploader";
    public static final String TYPE_AUDIO  = "audioUploader";


    public static final String BOUNDARY_GEO_JSON = "boundary_geo_json";
    public static final String MODULES = "modules";
    public static final String WALKING = "walking";
    public static final String TYPE_WALK = "Walk";
    public static final String TYPE_TAP = "Tap";
    public static final String TEST_FILE = "1";




    public static final String IMAGE_CAMERA = "Camera";
    public static final String IMAGE_GALLERY = "Gallery";
    public static final String TYPE_GEOM = "GEOM";
    public static final String CURSOR_CENTER = "cursor_center";
    public static final int REQUEST_CODE_PROFILE_ADD = 2050;
    public static final int REQUEST_CODE_PROFILE_UPDATE = 2039;
    public static final String PROJECT_ID_DEFAULT = "0";
    public static final String KEY_PROJECT_ID = "PROJECT_ID";
    public static final String KEY_PROJECT_NAME = "PROJECT_NAME";
    public static final String KEY_PROJECT_LAT = "PROJECT_LAT";
    public static final String KEY_PROJECT_LONG = "PROJECT_LONG";
    public static final String KEY_PROJECT_LOCATION = "PROJECT_LOCATION";

    private final static String PREF_KEY = "SurveyBaBa";
    public static final String IS_USER_SUCCESSSFULLY_LOGGED_IN = "IS_USER_SUCCESSSFULLY_LOGGED_IN";
    public static final String LOGGED_USERID = "UserID";
    public static final String LOGGED_USERNAME = "username";
    public static final String LOGGED_FIRSTNAME = "first_name";
    public static final String LOGGED_TOKEN = "token";
    public static final String IS_USER_TRACKING = "user_tracking";

    public static final String IS_FULL_SCREEN = "is_full_screen";
    public static final String IS_SCREEN_ORIENTATION_PORTRAIT = "is_portrait_screen";
    public static final String IS_ALWAYS_KEEP_SCREEN_AWAKE = "is_always_screen_awake";
    public static final String IS_REALIGN_MAP_CURSOR = "is_realign_map_cursor";
    public static final String IS_PANEL_HIDE = "is_panel_hide";
    public static final String IS_RECORD_ONLY_WHEN_MOVING = "IS_RECORD_ONLY_WHEN_MOVING";
    public static final String IS_RECORD_WHEN_GPS_OFF = "IS_RECORD_WHEN_GPS_OFF";
    public static final String IS_DISPLAY_PART_OF_TRACK = "IS_DISPLAY_PART_OF_TRACK";
    public static final String NO_OF_TRACKS_TO_RECORD = "NO_OF_TRACKS_TO_RECORD";
    public static final String HIDE_PANEL_DURATION = "duration_panel_hide";
    public static final String IS_NOTIFICATION_DISPLAY = "is_notification_display";
    public static final String LINE_COLOR_CODE = "line_color_code";
    public static final String LINE_SIZE = "line_size";
    public static final String TEXT_SIZE = "text_size";
    public static final String UNIT_DISTANCE_DATA = "UNIT_DISTANCE";
    public static final String UNIT_AREA_DATA = "UNIT_AREA";
    public static final String UNIT_GPS_ALTITUDE_DATA = "UNIT_GPS_ALTITUDE_DATA";
    public static final String UNIT_TIME_SEC = "sec";
    public static final String KEY_RECORD_PROFILES_LIST = "RECORD_PROFILES_LIST";

    public static final String LOGGED_PASS = "password";
    public static final String LOGGED_USER_VERSION = "login_version";
    public static final String FORM_BG_COLOR = "form_bg_color";
    public static final String FORM_LOGO = "form_logo";
    public static final String FORM_SNO = "form_sno";
    public static final String SURVEY_FORM_SNO = "marker_sno";


    public static final String PASS_PROJECT_ID = "project_id";
    public static final String PASS_SURVEY_ID = "survey_id";
    public static final String PASS_FORM_ID = "formid";
    public static final String PASS_FORM_NM = "form_name";
    public static final String PASS_GEOM_ID = "geomid";
    public static final String PASS_GEOM_ARRAY = "geom_array";
    public static final String PASS_GEOM_TYPE = "geom_type";
    public static final String PASS_LAYER_ID ="pass_layer_id";
    public static final String PASS_WORK_ID = "pass_work_id";
    public static final String PASS_LAYER_TYPE = "pass_layer_type";
    public static final String PASS_LINE_COLOR = "pass_line_color";
    public static final String PASS_LINE_TYPE = "pass_line_type";
    public static final String PASS_ICON = "pass_icon";
    public static final String PASS_ICON_WIDTH = "pass_icon_width";
    public static final String PASS_ICON_HEIGHT = "pass_icon_height";
    public static final String PASS_FORM_BG_COLOR = "pass_form_bg_color";
    public static final String PASS_FORM_LOGO = "pass_form_logo";
    public static final String PASS_FORM_SNO = "pass_form_sno";
    public static final String PASS_LAT = "lat";
    public static final String PASS_LONG = "lon";




    public static final String SELECTED_FEATURE = "selectedFeature";

    public static final String USER_TYPE = "userType";
    public static final String USER_TYPE_GIS = "GIS";

    public static final String BASE_MAP = "BASE_MAP";
    public static final String BASE_MAP_HYBRID = "HYBRID";
    public static final String BASE_MAP_TERRAIN = "TERRAIN";
    public static final String BASE_MAP_SATELLITE = "SATELLITE";
    public static final String BASE_MAP_NORMAL = "NORMAL";
    public static final String BASE_MAP_NONE = "NONE";

    public static final String YARDS   = "yards";
    public static final String SqMeter = "square Meter";
    public static final String HECTARE = "hectare";
    public static final String ACRES   = "acres";


    public static final String PROFILE_FIRSTNAME = "first_name";
    public static final String PROFILE_LASTNAME = "last_name";
    public static final String PROFILE_EMAILID = "email_id";
    public static final String PROFILE_IMAGE = "profile_image";
    public static final String PROFILE_MOBILE_NUMBER = "mobile_number";

    public static final int PICK_IMAGE_REQUEST = 1001;
    public static final int REQUEST_TAKE_PHOTO = 1002;
    public static final int REQUEST_VIDEO_CAPTURE = 1003;
    public static final int PICK_FILE_RESULT_CODE = 1004;
    public static final int PICK_AUDIO_FILE_RESULT_CODE = 1005;
    public static final int PICK_VIDEO_FILE_RESULT_CODE = 1006;
    public static final int PICK_BAR_CODE_RESULT_CODE = 1007;

    public static String key_intent_videoPath = "key_video_path_uri";
    public static String key_profile_activity_data = "key_profile_activity_data";
    public static String key_is_profile_remove = "is_profile_remove";
    public static String key_profile_is_add = "profile_is_add";

    public final String permission_ext_storage = "permission_ext_storage";
    public final String permission_camera = "permission_camera";
    public final String permission_fine_location = "permission_fine_location";
    public final String permission_coarse_location = "permission_coarse_location";

    public static final String BOUNDARY_ERROR_MESSAGE = "You can't work outside of boundary";
    public static final String BOUNDARY_FORM_ERROR_MESSAGE = "Sorry, you can't open the form because your work is outside the defined boundary";

    // Activtiy
    Activity context;



    public Utility(Activity context) {
        this.context = context;
    }

    // Change by Rahul Suthar
    public static Date convertIntoTrackingDateFormat(String date) throws ParseException {
        // Set Date Format
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat(Utility.TRACKING_DATE_FORMAT,Locale.getDefault());
        // Set Time Zone
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.parse(date);
    }
    // Change by Rahul Suthar
    public static Date convertIntoTrackingTimeFormat(String time) throws ParseException {
        // Set Date Format
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat timeFormat = new SimpleDateFormat(Utility.TRACKING_TIME_FORMAT);
        return timeFormat.parse(time);
    }
    // Change by Rahul Suthar
    public static Date getCurrentDate() throws ParseException {
        // Set Date Format
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat(Utility.TRACKING_DATE_FORMAT, Locale.getDefault());
        // Set Time Zone
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        // Current Date
        String cur_date = dateFormat.format(new Date());
        return dateFormat.parse(cur_date);
    }

    // Change by Rahul Suthar
    public static Date getCurrentTime() throws ParseException{
        // Set Time Format
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat timeFormat = new SimpleDateFormat(Utility.TRACKING_TIME_FORMAT);
        // Calendar
        Calendar calendar = Calendar.getInstance();
        // Current Time
        String cur_time = timeFormat.format(calendar.getTime());
        return timeFormat.parse(cur_time);
    }

    public static void saveData(Context ctx, String TAG, String data) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREF_KEY, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TAG, data);
        editor.commit();
    }

    public static void saveData(Context ctx, String TAG, boolean value) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREF_KEY, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(TAG, value);
        editor.commit();
    }

    public static String getSavedData(Context ctx, String TAG) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREF_KEY, 0);
        String data = prefs.getString(TAG, "");
        return data;
    }

    public static boolean getBooleanSavedData(Context ctx, String TAG) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREF_KEY, 0);
        boolean value = prefs.getBoolean(TAG, false);
        return value;
    }

    public static void clearData(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREF_KEY, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

    public static boolean isEmptyString(String str) {
        if (str == null) {
            return true;
        } else if (str.isEmpty()) {
            return true;
        } else return str.equalsIgnoreCase("null");
    }

    public static void showInfoDialog(Context context, String errorMessage) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
    }

    public static String validateIds(String projectIds) {
        if (projectIds == null) {
            projectIds = "";
        } else if (projectIds.endsWith(",") && projectIds.length() >= 2) {
            projectIds = projectIds.substring(0, projectIds.length() - 1);//2,
        }
        return projectIds;
    }

    private static Toast mToast;

    public static void showToast(Context mContext, String message, View view) {
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        } else {
            if (mToast != null)
                mToast.cancel();
            mToast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
            mToast.show();
        }
    }

    public static String getTempProjectApiResponse() {
        return "{\n" +
                "\t\"data\": [{\n" +
                "\t\t\"id\": \"3\",\n" +
                "\t\t\"project\": \"Water Sanitation\",\n" +
                "\t\t\"latitude\": 23.0225,\n" +
                "\t\t\"longitude\": 72.5714,\n" +
                "\t\t\"layers\": [{\n" +
                "\t\t\t\t\"name\": \"Ahmedabad Boundry\",\n" +
                "\t\t\t\t\"layer_url\": \"http:\\/\\/surveybaba.com\\/geojson\\/ahmedabad_boundry.geojson\",\n" +
                "\t\t\t\t\"layer_type\": \"Polygon\",\n" +
                "\t\t\t\t\"layer_display\": \"#ADD8E6\",\n" +
                "\t\t\t\t\"form\": {\n" +
                "\t\t\t\t\t\"form_id\": \"4\",\n" +
                "\t\t\t\t\t\"description\": \"Personal Form\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"name\": \"Sewerage Installation\",\n" +
                "\t\t\t\t\"layer_url\": \"http:\\/\\/surveybaba.com\\/geojson\\/Sewerage.geojson\",\n" +
                "\t\t\t\t\"layer_type\": \"Point\",\n" +
                "\t\t\t\t\"layer_display\": \"http://surveybaba.com/surveybaba/uploads/icon_files/blue_point.png\",\n" +
                "\t\t\t\t\"form\": {\n" +
                "\t\t\t\t\t\"form_id\": \"5\",\n" +
                "\t\t\t\t\t\"description\": \"Survey\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"name\": \"Sewerage Network\",\n" +
                "\t\t\t\t\"layer_url\": \"http:\\/\\/surveybaba.com\\/geojson\\/line.geojson\",\n" +
                "\t\t\t\t\"layer_type\": \"Line\",\n" +
                "\t\t\t\t\"layer_display\": \"#0000FF\",\n" +
                "\t\t\t\t\"form\": {\n" +
                "\t\t\t\t\t\"form_id\": \"6\",\n" +
                "\t\t\t\t\t\"description\": \"all_form\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t}],\n" +
                "\t\"msg\": \"success\"\n" +
                "}\n";
    }

    public interface INPUT_TYPE {
        int NUMBER_ONLY = 1;
        int NUMBER_DECIMAL = 2;
        int STRING = 3;
    }

    public interface UNIT_DISTANCE {
        int METER = 1;
        int METER_KM = 2;
        int FT = 3;
        int FT_MI = 4;
        int YD = 5;
        //        int YD_MI = 6;
        int M_NMI = 6;
    }

    public static String[] getUNIT_DISTANCE(Context context) {
        return new String[]{context.getString(R.string.metric_meter),
                context.getString(R.string.metric_m_km),
                context.getString(R.string.imperial_ft),
                context.getString(R.string.imperial_ft_mi),
                context.getString(R.string.imperial_yd),
//                context.getString(R.string.imperial_yd_mi),
                context.getString(R.string.nautical_m_nmi)
        };
    }

    public interface UNIT_AREA {
        int SQ_METER = 0;
        int METER_2 = 1;
        int HA = 2;
        int KM_2 = 3;
        int FT_2 = 4;
        int YD_2 = 5;
        int ACRE = 6;
        int MI_2 = 7;
        int NMI_2 = 8;
    }

    public static String[] getUNIT_AREA(Context context) {
        return new String[]{context.getString(R.string.meter_square),
                context.getString(R.string.ha),
                context.getString(R.string.km_square),
                context.getString(R.string.ft_square),
                context.getString(R.string.yd_square),
                context.getString(R.string.acre),
                context.getString(R.string.mi_square),
                context.getString(R.string.nmi_square)
        };
    }

    public interface UNIT_ALTITUDE {
        int METERS = 1;
        int FT = 2;
    }

    public static String[] getUNIT_ALTITUDE(Context context) {
        return new String[]{context.getString(R.string.meters),
                context.getString(R.string.feet)
        };
    }

    public interface UNIT_SPEED {
        int KMPH = 1;
        int MPH = 2;
        int NAUTICAL_MILE_PH = 3;
        int KNOTS = 4;
    }

    public static String[] getUNIT_SPEED(Context context) {
        return new String[]{context.getString(R.string.kmph),
                context.getString(R.string.mph),
                context.getString(R.string.nautical_mh),
                context.getString(R.string.knots)
        };
    }

    public interface ANGLE {
        int DEGREE = 1;
        int ANGULAR_MIL = 2;
        int RUSSIAN_MIL = 3;
        int US_ARTILLERY_MIL = 4;
    }

    public static String[] getUNIT_ANGLE(Context context) {
        return new String[]{context.getString(R.string.degree),
                context.getString(R.string.angular_mil),
                context.getString(R.string.russian_mil),
                context.getString(R.string.us_artillery_mil)
        };
    }

    public interface TEMPERATURE {
        int CELSIUS = 1;
        int FAHRENHEIT = 2;
    }

    public static String[] getUNIT_TEMPERATURE(Context context) {
        return new String[]{context.getString(R.string.celsius),
                context.getString(R.string.fahrenheit)
        };
    }

    public interface COLOR_CODE {
        String ORANGE = "#E78B13";
        String PURPLE = "#800080";
        String BLUE = "#0000FF";
        String RED = "#FF0000";
        String LIGHT_BLUE = "#1976D2";
        String GREEN = "#00FF00";
        String BLACK = "#000000";
        String PINK = "#FF10F0";
        String YELLOW = "#FFEA00";
    }

    public interface onDialogClickListener {
        void onPositiveAction(String value);
        void onCancelAction();
    }

    public static void showEditTextDialog(Activity mContext, String title, String unit, String value, int inputType, onDialogClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = mContext.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edittext, null);
        builder.setView(dialogView);

        TextView txtDialogTitle = dialogView.findViewById(R.id.txtDialogTitle);
        TextView txtUnit = dialogView.findViewById(R.id.txtUnit);
        TextView txtCancel = dialogView.findViewById(R.id.txtCancel);
        TextView txtSet = dialogView.findViewById(R.id.txtSet);
        EditText edtInput = dialogView.findViewById(R.id.edtInput);
        ImageView imgClose = dialogView.findViewById(R.id.imgClose);

        if (!Utility.isEmptyString(title))
            txtDialogTitle.setText(title);
        else
            txtDialogTitle.setText("");

        if (!Utility.isEmptyString(unit)) {
            txtUnit.setText(unit);
            txtUnit.setVisibility(View.VISIBLE);
        } else {
            txtUnit.setVisibility(View.GONE);
        }

        if(!Utility.isEmptyString(value))
        {
            edtInput.setText(String.format(Locale.US, "%s", String.valueOf(value)));
        }

        switch (inputType) {
            case INPUT_TYPE.NUMBER_ONLY:
                edtInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case INPUT_TYPE.NUMBER_DECIMAL:
                edtInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                break;
            case INPUT_TYPE.STRING:
                edtInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                break;
        }

        AlertDialog alert = builder.create();
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                onClickListener.onCancelAction();
            }
        });
        txtSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = edtInput.getText().toString().trim();
                if (!Utility.isEmptyString(str)) {
                    onClickListener.onPositiveAction(str);
                    alert.dismiss();
                }
            }
        });
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                onClickListener.onCancelAction();
            }
        });
        alert.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openKeyboard(mContext, edtInput);
            }
        },400);
    }

    public static void showTextDialog(Activity mContext, String title, String message, onDialogClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = mContext.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_textview, null);
        builder.setView(dialogView);

        TextView txtDialogTitle = dialogView.findViewById(R.id.txtDialogTitle);
        ImageView imgClose = dialogView.findViewById(R.id.imgClose);
        TextView txtMessage = dialogView.findViewById(R.id.txtMessage);
        TextView txtCancel = dialogView.findViewById(R.id.txtCancel);
        TextView txtOK = dialogView.findViewById(R.id.txtOK);
        txtOK.setText("Remove");
        txtMessage.setText(message);

        if (!Utility.isEmptyString(title))
            txtDialogTitle.setText(title);
        else
            txtDialogTitle.setText("");

        AlertDialog alert = builder.create();
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                onClickListener.onCancelAction();
            }
        });
        txtOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                onClickListener.onPositiveAction("");
            }
        });
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                onClickListener.onCancelAction();
            }
        });
        alert.show();
    }

    public interface onUpdatedSelection{
        void onResult();
    }

    public static void showMapTypeDialog(Activity mContext, OnClickMap onClickMap){

        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_map_type);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog.setCancelable(false);
        // Button
        Button btSatellite = dialog.findViewById(R.id.bt_satellite);
        Button btHybrid = dialog.findViewById(R.id.bt_hybrid);
        Button btTerrain = dialog.findViewById(R.id.bt_terrain);
        Button btNormal = dialog.findViewById(R.id.bt_normal);
        ImageView btClose = dialog.findViewById(R.id.bt_close);

        btSatellite.setOnClickListener(view -> {
            onClickMap.onClickMap(Utility.BASE_MAP_SATELLITE);
            dialog.dismiss();
        });
        btHybrid.setOnClickListener(view -> {
            onClickMap.onClickMap(Utility.BASE_MAP_HYBRID);
            dialog.dismiss();
        });
        btTerrain.setOnClickListener(view -> {
            onClickMap.onClickMap(Utility.BASE_MAP_TERRAIN);
            dialog.dismiss();
        });
        btNormal.setOnClickListener(view -> {
            onClickMap.onClickMap(Utility.BASE_MAP_NORMAL);
            dialog.dismiss();
        });
        btClose.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();

    }

    public static void showConverterMeasuremetDialog(Activity mContext, OnClickMap onClickMap){

        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_converter_type);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        // Button
        Button btYards    = dialog.findViewById(R.id.bt_yards);
        Button btSqMeter  = dialog.findViewById(R.id.bt_square_meter);
        Button btHectare  = dialog.findViewById(R.id.bt_hectare);
        Button btAcres    = dialog.findViewById(R.id.bt_acres);
        ImageView btClose = dialog.findViewById(R.id.bt_close);

//        public interface UNIT_AREA {
//            int METER_2 = 1;
//            int HA = 2;
//            int KM_2 = 3;
//            int FT_2 = 4;
//            int YD_2 = 5;
//            int ACRE = 6;
//            int MI_2 = 7;
//            int NMI_2 = 8;
//        }

        btYards.setOnClickListener(view -> {
            onClickMap.onClickMap(""+ UNIT_AREA.YD_2);
            dialog.dismiss();
        });
        btSqMeter.setOnClickListener(view -> {
            onClickMap.onClickMap(""+ UNIT_AREA.SQ_METER);
            dialog.dismiss();
        });
        btHectare.setOnClickListener(view -> {
            onClickMap.onClickMap(""+ UNIT_AREA.HA);
            dialog.dismiss();
        });
        btAcres.setOnClickListener(view -> {
            onClickMap.onClickMap(""+UNIT_AREA.ACRE);
            dialog.dismiss();
        });
        btClose.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();

    }


    public static void showConverterMeasuremetPolyLineDialog(Activity mContext, OnClickMap onClickMap){

        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_converter_line_type);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        // Button
        Button btMeter    = dialog.findViewById(R.id.bt_meter);
        Button btKM       = dialog.findViewById(R.id.bt_km);
        Button btFeet     = dialog.findViewById(R.id.bt_feet);
        ImageView btClose = dialog.findViewById(R.id.bt_close);


        btMeter.setOnClickListener(view -> {
            onClickMap.onClickMap(""+ UNIT_DISTANCE.METER);
            dialog.dismiss();
        });
        btKM.setOnClickListener(view -> {
            onClickMap.onClickMap(""+ UNIT_DISTANCE.METER_KM);
            dialog.dismiss();
        });
        btFeet.setOnClickListener(view -> {
            onClickMap.onClickMap(""+ UNIT_DISTANCE.FT);
            dialog.dismiss();
        });

        btClose.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();

    }





    public static void showMultiCheckDialog(Activity mContext, onUpdatedSelection onUpdatedSelection) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = mContext.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_top_panel, null);
        builder.setView(dialogView);

        AlertDialog alert = builder.create();
        AdapterMultiCheckDialog adapter;
        List<BinTopPanel> listData = new ArrayList<>();
        listData.addAll(getListTopPanel(mContext));
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView rcvProfileActivities = dialogView.findViewById(R.id.rcvTopPanel);
        TextView txtDialogTitle = dialogView.findViewById(R.id.txtDialogTitle);
        ImageView imgClose = dialogView.findViewById(R.id.imgClose);
        txtDialogTitle.setText(R.string.set_top_panel);
        rcvProfileActivities.setLayoutManager(layoutManager);
        adapter = new AdapterMultiCheckDialog(mContext, listData, alert);
        rcvProfileActivities.setAdapter(adapter);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdatedSelection.onResult();
                alert.dismiss();
            }
        });
        alert.show();
    }

    public interface TOP_PANEL
    {
        String LAYERS = "LAYERS";
        String MEASURE = "MEASURE";
        String GPS_COMPASS = "GPS_COMPASS";
    }

    public static List<BinTopPanel> getListTopPanel(Activity context)
    {
        List<BinTopPanel> list = new ArrayList<>();
        BinTopPanel bin1 = new BinTopPanel();
        bin1.setName(context.getString(R.string.layers));
        bin1.setChecked(Utility.getBooleanSavedData(context, Utility.TOP_PANEL.LAYERS));
        list.add(bin1);
        BinTopPanel bin3 = new BinTopPanel();
        bin3.setName(context.getString(R.string.gps_compass));
        bin3.setChecked(Utility.getBooleanSavedData(context, Utility.TOP_PANEL.GPS_COMPASS));
        list.add(bin3);
        BinTopPanel bin2 = new BinTopPanel();
        bin2.setName(context.getString(R.string.measurement));
        bin2.setChecked(Utility.getBooleanSavedData(context, Utility.TOP_PANEL.MEASURE));
        list.add(bin2);
        BinTopPanel bin4 = new BinTopPanel();
        bin4.setName(context.getString(R.string.set_top_panel));
        bin4.setChecked(true);
        list.add(bin4);
        return list;
    }

    public static void showProfileActivityDialog(Activity mContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = mContext.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_profile, null);
        builder.setView(dialogView);
        AlertDialog alert = builder.create();
        AdapterRecordingsDialog adapter;
        List<BinRecordProfile> listProfiles = new ArrayList<>();
        listProfiles.addAll(getAllProfileCategories());
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView rcvProfileActivities = dialogView.findViewById(R.id.rcvProfileActivities);
        TextView txtDialogTitle = dialogView.findViewById(R.id.txtDialogTitle);
        ImageView imgClose = dialogView.findViewById(R.id.imgClose);
        txtDialogTitle.setText(R.string.choose_activity);
        rcvProfileActivities.setLayoutManager(layoutManager);
        adapter = new AdapterRecordingsDialog(mContext, listProfiles, alert);
        rcvProfileActivities.setAdapter(adapter);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    private static Collection<? extends BinRecordProfile> getAllProfileCategories() {
        List<BinRecordProfile> list = new ArrayList<>();
        BinRecordProfile bin1 = new BinRecordProfile();
        bin1.setCategory("Land Activities");
        bin1.setName("Walking");
        bin1.setHeader(true);
        list.add(bin1);
        BinRecordProfile bin2 = new BinRecordProfile();
        bin2.setCategory("Land Activities");
        bin2.setName("Scooter");
        bin2.setHeader(false);
        list.add(bin2);
        BinRecordProfile bin3 = new BinRecordProfile();
        bin3.setCategory("Land Activities");
        bin3.setName("Hiking");
        bin3.setHeader(false);
        list.add(bin3);
        BinRecordProfile bin4 = new BinRecordProfile();
        bin4.setCategory("Land Activities");
        bin4.setName("Running");
        bin4.setHeader(false);
        list.add(bin4);
        BinRecordProfile bin5 = new BinRecordProfile();
        bin5.setCategory("Land Activities");
        bin5.setName("Cycling");
        bin5.setHeader(false);
        list.add(bin5);

        BinRecordProfile bin6 = new BinRecordProfile();
        bin6.setCategory("Water Activities");
        bin6.setName("Swimming");
        bin6.setHeader(true);
        list.add(bin6);
        BinRecordProfile bin7 = new BinRecordProfile();
        bin7.setCategory("Water Activities");
        bin7.setName("Sailing");
        bin7.setHeader(false);
        list.add(bin7);

        BinRecordProfile bin8 = new BinRecordProfile();
        bin8.setCategory("Winter Activities");
        bin8.setName("Snowshoeing");
        bin8.setHeader(true);
        list.add(bin8);
        BinRecordProfile bin9 = new BinRecordProfile();
        bin9.setCategory("Winter Activities");
        bin9.setName("Downhill skiing");
        bin9.setHeader(false);
        list.add(bin9);

        BinRecordProfile bin10 = new BinRecordProfile();
        bin10.setCategory("Other Activities");
        bin10.setName("Gliding");
        bin10.setHeader(true);
        list.add(bin10);
        BinRecordProfile bin11 = new BinRecordProfile();
        bin11.setCategory("Other Activities");
        bin11.setName("Horse Riding");
        bin11.setHeader(false);
        list.add(bin11);
        BinRecordProfile bin12 = new BinRecordProfile();
        bin12.setCategory("Other Activities");
        bin12.setName("Wheelchair");
        bin12.setHeader(false);
        list.add(bin12);

        BinRecordProfile bin13 = new BinRecordProfile();
        bin13.setCategory("Transportation");
        bin13.setName("Car");
        bin13.setHeader(true);
        list.add(bin13);
        BinRecordProfile bin14 = new BinRecordProfile();
        bin14.setCategory("Transportation");
        bin14.setName("Motorcycle");
        bin14.setHeader(false);
        list.add(bin14);
        BinRecordProfile bin15 = new BinRecordProfile();
        bin15.setCategory("Transportation");
        bin15.setName("Truck");
        bin15.setHeader(false);
        list.add(bin15);
        BinRecordProfile bin16 = new BinRecordProfile();
        bin16.setCategory("Transportation");
        bin16.setName("Public Transport");
        bin16.setHeader(false);
        list.add(bin16);
        BinRecordProfile bin17 = new BinRecordProfile();
        bin17.setCategory("Transportation");
        bin17.setName("Boat");
        bin17.setHeader(false);
        list.add(bin17);
        BinRecordProfile bin18 = new BinRecordProfile();
        bin18.setCategory("Transportation");
        bin18.setName("Airplane");
        bin18.setHeader(false);
        list.add(bin18);
        return list;
    }

    /*public static ArrayList<BinLayer> getTempListLayer() {
        ArrayList<BinLayer> list = new ArrayList<>();
        BinLayer binLayer1 = new BinLayer();
        binLayer1.setType(LayersAdapter.LAYER_TYPE.POINT);
        binLayer1.setNameLayer("Line");
        binLayer1.setChecked(true);

        BinLayer binLayer2 = new BinLayer();
        binLayer2.setType(LayersAdapter.LAYER_TYPE.POLYGON);
        binLayer2.setNameLayer("Polygon");
        binLayer2.setChecked(true);

        BinLayer binLayer3 = new BinLayer();
        binLayer3.setType(LayersAdapter.LAYER_TYPE.POLYGON);
        binLayer3.setNameLayer("Power Line");
        binLayer3.setChecked(true);

        BinLayer binLayer4 = new BinLayer();
        binLayer4.setType(LayersAdapter.LAYER_TYPE.POLYGON);
        binLayer4.setNameLayer("Power Polygon");
        binLayer4.setChecked(true);

        BinLayer binLayer5 = new BinLayer();
        binLayer5.setType(LayersAdapter.LAYER_TYPE.POLYGON);
        binLayer5.setNameLayer("Polygon Long");
        binLayer5.setChecked(true);

        BinLayer binLayer6 = new BinLayer();
        binLayer6.setType(LayersAdapter.LAYER_TYPE.POLYGON);
        binLayer6.setNameLayer("Line Polygon");
        binLayer6.setChecked(true);

        BinLayer binLayer7 = new BinLayer();
        binLayer7.setType(LayersAdapter.LAYER_TYPE.POLYGON);
        binLayer7.setNameLayer("Polygon Point");
        binLayer7.setChecked(true);

        list.add(binLayer1);
        list.add(binLayer2);
        list.add(binLayer3);
        list.add(binLayer4);
        list.add(binLayer5);
        list.add(binLayer6);
        list.add(binLayer7);

        return list;
    }*/

    public interface onItemClick {
        void itemSelected(int position);
    }

    public static void openSelectDialog(Context context, String[] arr, onItemClick onItemClick) {
        android.app.AlertDialog.Builder pictureDialog = new AlertDialog.Builder(context);
        pictureDialog.setTitle(context.getString(R.string.select));
        pictureDialog.setItems(arr, (dialog, position) -> {
            onItemClick.itemSelected(position);
            dialog.dismiss();
        });
        pictureDialog.show();
    }

    public interface onDateSelection {
        void onDateSelected(String date);
    }

    public interface onTimeSelection {
        void onTimeSelected(String time);
    }

    public static String validateDoubleDigit(int digit) {
        if (String.valueOf(digit).length() < 2) {
            return "0" + digit;
        } else {
            return "" + digit;
        }
    }

    public static void openDatePickerDialog(Context context, onDateSelection onDateSelection) {
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        onDateSelection.onDateSelected(validateDoubleDigit(dayOfMonth) + "-" + validateDoubleDigit((monthOfYear + 1)) + "-" + validateDoubleDigit(year));
                        onDateSelection.onDateSelected( validateDoubleDigit(year)+"-"+validateDoubleDigit((monthOfYear + 1)) + "-"+ validateDoubleDigit(dayOfMonth));
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public static void openTimePickerDialog(Context context, onTimeSelection onTimeSelection) {
        int mHour, mMinute, mSec;
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mSec = c.get(Calendar.SECOND);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    //    onTimeSelection.onTimeSelected(validateDoubleDigit(hourOfDay) + ":" + validateDoubleDigit(minute) + ":" + validateDoubleDigit(mSec));
                        onTimeSelection.onTimeSelected(validateDoubleDigit(hourOfDay) + ":" + validateDoubleDigit(minute) + ":" + validateDoubleDigit(mSec));
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    public void setPermissionDenied(Context ctx, String keyPermission, boolean isDenied) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREF_KEY, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(keyPermission, isDenied);
        editor.apply();
    }

    public boolean isPermissionDenied(Context ctx, String keyPermission) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREF_KEY, 0);
        return prefs.getBoolean(keyPermission, false);
    }

    public static String[] getPhotoSelectionOptions() {
        return new String[]{PHOTO_SELECTION.TAKE_PHOTO,
                //PHOTO_SELECTION.CHOOSE_FROM_GAL,
                PHOTO_SELECTION.CANCEL};
    }

    public interface PHOTO_SELECTION {
        String TAKE_PHOTO = "Take Photo";
        String CHOOSE_FROM_GAL = "Choose from Gallery";
        String CANCEL = "Cancel";
    }

    public static void showLocationDialog(Context context, String title,
                                          String[] items,
                                          DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(items, onClickListener);
        builder.setCancelable(false);
        builder.create().show();
    }

    public static void showSingleBtnDialog(Context context, String title,
                                          String message,
                                          String OkBtnText,
                                          DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(!Utility.isEmptyString(title))
            builder.setTitle(title);
        if(!Utility.isEmptyString(message))
            builder.setMessage(message);
        builder.setPositiveButton(OkBtnText, onClickListener);
        builder.setCancelable(false);
        builder.create().show();
    }

    public static void showDoubleBtnDialog(Context context, String title,
                                           String message,
                                           String OkBtnText,
                                           String CancelBtnText,
                                           DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(!Utility.isEmptyString(title))
            builder.setTitle(title);
        if(!Utility.isEmptyString(message))
            builder.setMessage(message);
        builder.setPositiveButton(OkBtnText, onClickListener);
        builder.setNegativeButton(CancelBtnText, onClickListener);
        builder.setCancelable(false);
        builder.create().show();
    }
    public static void showDoubleBtnDialog1(Context context, String title, String message, String OkBtnText, String CancelBtnText, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(!Utility.isEmptyString(title))
            builder.setTitle(title);
        if(!Utility.isEmptyString(message))
            builder.setMessage(message);
        builder.setPositiveButton(OkBtnText, onClickListener);
        builder.setNegativeButton(CancelBtnText, onClickListener);
        builder.setCancelable(true);
        builder.create().show();
    }
    public void openFilePicker(Activity context, SystemPermission systemPermission, int position, onPhotoCaptured onPhotoCaptured) {
        if (systemPermission.isExternalStorage()) {
            Intent chooseFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
            chooseFile.setType("*/*");
           // chooseFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            chooseFile = Intent.createChooser(chooseFile, "Choose a file");
            onPhotoCaptured.getPath(null, position);
            context.startActivityForResult(chooseFile, PICK_FILE_RESULT_CODE);
        }
    }

    public void openFilePickerMultiple(Activity context, SystemPermission systemPermission, int position, onPhotoCaptured onPhotoCaptured) {
        if (systemPermission.isExternalStorage()) {
            Intent chooseFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
            chooseFile.setType("*/*");
            chooseFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            chooseFile = Intent.createChooser(chooseFile, "Choose a file");
            onPhotoCaptured.getPath(null, position);
            context.startActivityForResult(chooseFile, PICK_FILE_RESULT_CODE);
        }
    }

    public void openAudioFilePicker(Activity context, SystemPermission systemPermission, int position, onPhotoCaptured onPhotoCaptured) {
        if (systemPermission.isExternalStorage()) {
            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.setType("audio/*");
            chooseFile = Intent.createChooser(chooseFile, "Choose a file");
            onPhotoCaptured.getPath(null, position);
            context.startActivityForResult(chooseFile, PICK_AUDIO_FILE_RESULT_CODE);
        }
    }

    public void openVideoFilePicker(Activity context, SystemPermission systemPermission, int position, onPhotoCaptured onPhotoCaptured) {
        if (systemPermission.isExternalStorage()) {
            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.setType("video/*");
            chooseFile = Intent.createChooser(chooseFile, "Choose a file");
            onPhotoCaptured.getPath(null, position);
            context.startActivityForResult(chooseFile, PICK_VIDEO_FILE_RESULT_CODE);
        }
    }


    String selectedPicOption = "";

    public void openImageSelection(Activity context, SystemPermission systemPermission, ImageFileUtils imageFileUtils, int position, onPhotoCaptured onPhotoCaptured) {
        String[] items = getPhotoSelectionOptions();
        showLocationDialog(context,
                context.getString(R.string.select),
                items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int item) {
                        selectedPicOption = items[item];
                        if (items[item].equals(PHOTO_SELECTION.TAKE_PHOTO)) {
                            if (systemPermission.isExternalStorage()) {
                                if (systemPermission.isCamera()) {
                                    takePhoto(context, imageFileUtils, position, onPhotoCaptured);
                                }
                            }
                        }
//                        else if (items[item].equals(PHOTO_SELECTION.CHOOSE_FROM_GAL)) {
//                            if (systemPermission.isExternalStorage()) {
//                                pickFromGallery(context, position, onPhotoCaptured);
//                            }
//                        }
                        else if (items[item].equals(PHOTO_SELECTION.CANCEL)) {
                            dialogInterface.dismiss();
                        }
                    }
                });
    }

    private void pickFromGallery(Activity context, int position, onPhotoCaptured onPhotoCaptured) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        onPhotoCaptured.getPath(null, position);
        context.startActivityForResult(Intent.createChooser(intent, context.getString(R.string.select_image)), PICK_IMAGE_REQUEST);
    }

    public interface onPhotoCaptured {
        void getPath(String path, int position);
    }

    private void takePhoto(Activity context, ImageFileUtils imageFileUtils, int position, onPhotoCaptured onPhotoCaptured) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File destFileTemp = imageFileUtils.getDestinationFile(imageFileUtils.getRootDirFile(context));
        Uri photoURI = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", destFileTemp);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        onPhotoCaptured.getPath(destFileTemp.getAbsolutePath(), position);
        context.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
    }

    public void openBarCode(Activity context,int position,onPhotoCaptured onPhotoCaptured) {
        onPhotoCaptured.getPath(null, position);
        Intent  intent = new Intent(context,BarCodeActivity.class);
        context.startActivityForResult(intent,Utility.PICK_BAR_CODE_RESULT_CODE);
    }


    public static ArrayList<ArrayList<LatLng>> convertStringToListOfPolygonLine(String responseString)
    {
        Type userListType = new TypeToken<ArrayList<ArrayList<LatLng>>>() {
        }.getType();
        return new Gson().fromJson(responseString, userListType);
    }


    public static ArrayList<LabelTextModel> convertStringToPropertiesList(String responseString){
        ArrayList<LabelTextModel> list = new ArrayList<>();

        return list;
    }

    public static ArrayList<ArrayList<LatLng>> convertStringToPolygonLatLon(String responseString){
        ArrayList<ArrayList<LatLng>> list = new ArrayList<>();
        try{
            PolygonViewLayerModel bin = convertStringToPolygonViewLayerModel(responseString);
            for(int i=0; i<bin.getFeatures().size(); i++){
                Geometry geometry = bin.getFeatures().get(i).getGeometry();
                if(geometry != null){

                    if(geometry.getCoordinates() != null){

                        int geometrySize = geometry.getCoordinates().size();
                        if(geometrySize > 0){

                            for(int j=0; j<geometrySize; j++){

                                int coordinatesSize = geometry.getCoordinates().get(j).size();

                                if(coordinatesSize > 0){
                                    ArrayList<LatLng> latLngs = new ArrayList<>();

                                    for(int k =0; k<coordinatesSize; k++){
                                        ArrayList<Double> latlongs = geometry.getCoordinates().get(j).get(k);
                                        latLngs.add(new LatLng(latlongs.get(1),latlongs.get(0)));
                                    }
                                    if(latLngs.size() > 0){
                                        list.add(latLngs);
                                    }
                                }
                            }
                        }
                        else{
                            ArrayList<LatLng> latLngs = new ArrayList<>();
                            list.add(latLngs);
                        }
                    }
                    else{
                        ArrayList<LatLng> latLngs = new ArrayList<>();
                        list.add(latLngs);
                    }
                }
                else{
                    ArrayList<LatLng> latLngs = new ArrayList<>();
                    list.add(latLngs);
                }

            }
        }
        catch (Exception e){
            Log.e("OnlineViewLayer",e.getMessage());
        }
        return list;
    }

    public static ArrayList<ArrayList<LatLng>> convertStringToPolylineLatLon(String responseString){
        ArrayList<ArrayList<LatLng>> list = new ArrayList<>();
        try{
            PolylineViewLayerModel bin = convertStringToPolylineViewLayerModel(responseString);
            for(int i=0; i<bin.getFeatures().size(); i++){
                com.surveybaba.model.PolylineViewLayerModel.Geometry geometry = bin.getFeatures().get(i).getGeometry();
                if(geometry != null){
                    if(geometry.getCoordinates() != null){
                        int geometrySize = geometry.getCoordinates().size();
                        Log.e("OnlineViewLayer", "Polyline Size: "+geometrySize);
                        if(geometrySize > 0){
                            ArrayList<LatLng> latLngs = new ArrayList<>();
                            for(int j=0; j<geometrySize; j++){
                                ArrayList<Double> latlongs = geometry.getCoordinates().get(j);
                                latLngs.add(new LatLng(latlongs.get(1),latlongs.get(0)));
                            }
                            if(latLngs.size() > 0){
                                list.add(latLngs);
                            }
                        }
                        else{
                            ArrayList<LatLng> latLngs = new ArrayList<>();
                            list.add(latLngs);
                        }
                    }
                    else{
                        ArrayList<LatLng> latLngs = new ArrayList<>();
                        list.add(latLngs);
                    }
                }
                else{
                    ArrayList<LatLng> latLngs = new ArrayList<>();
                    list.add(latLngs);
                }

            }
        }
        catch (Exception e){
            Log.e("OnlineViewLayer",e.getMessage());
        }
        return list;
    }

    public static ArrayList<ArrayList<LatLng>> convertStringToPointLatLon(String responseString){
        ArrayList<ArrayList<LatLng>> list = new ArrayList<>();
        try{
            PointViewLayerModel bin = convertStringToPointViewLayerModel(responseString);

            for(int i=0; i<bin.getFeatures().size(); i++){
                com.surveybaba.model.PointViewLayerModel.Geometry geometry = bin.getFeatures().get(i).getGeometry();

                if(geometry != null){
                    if(geometry.getCoordinates() != null){
                        int geometrySize = geometry.getCoordinates().size();
                        if(geometrySize > 0){
                            ArrayList<LatLng> latLngs = new ArrayList<>();
                            ArrayList<Double> latlongs = geometry.getCoordinates();
                            latLngs.add(new LatLng(latlongs.get(1),latlongs.get(0)));
                            if(latLngs.size() > 0){
                                list.add(latLngs);
                            }
                        }
                        else{
                            ArrayList<LatLng> latLngs = new ArrayList<>();
                            list.add(latLngs);

                        }
                    }
                    else{
                        ArrayList<LatLng> latLngs = new ArrayList<>();
                        list.add(latLngs);
                    }
                }
                else{
                    ArrayList<LatLng> latLngs = new ArrayList<>();
                    list.add(latLngs);
                }

            }
        }
        catch (Exception e){
            Log.e("OnlineViewLayer",e.getMessage());
        }
        return list;
    }


    public static PolygonViewLayerModel convertStringToPolygonViewLayerModel(String responseString)
    {
        Type userListType = new TypeToken<PolygonViewLayerModel>() {}.getType();
        return   new Gson().fromJson(responseString, userListType);
    }

    public static PointViewLayerModel convertStringToPointViewLayerModel(String responseString)
    {
        Type userListType = new TypeToken<PointViewLayerModel>() {}.getType();
        return  new Gson().fromJson(responseString, userListType);
    }

    public static PolylineViewLayerModel convertStringToPolylineViewLayerModel(String responseString)
    {
        Type userListType = new TypeToken<PolylineViewLayerModel>() {}.getType();
        return   new Gson().fromJson(responseString, userListType);
    }


    public static JsonArray convertPolygonListToJsonArray(ArrayList<ArrayList<LatLng>> listLatLng) {
        return new Gson().toJsonTree(listLatLng).getAsJsonArray();
    }

    public static ArrayList<LatLng> convertStringToListOfPoints(String responseString)
    {
        Type userListType = new TypeToken<ArrayList<LatLng>>() {
        }.getType();
        return new Gson().fromJson(responseString, userListType);
    }

    public static JsonArray convertCustomListToJsonArray(ArrayList<LatLng> listLatLng) {
        return new Gson().toJsonTree(listLatLng).getAsJsonArray();
    }

    public static ArrayList<FormDetailData> convertJsonArrayToCustomList(String responseString) {
        Type userListType = new TypeToken<ArrayList<FormDetailData>>() {
        }.getType();
        return new Gson().fromJson(responseString, userListType);
    }

    public static ArrayList<BinLayerProject> convertStringToGsonArray(String responseString) {
        Type userListType = new TypeToken<ArrayList<BinLayerProject>>() {
        }.getType();
        return new Gson().fromJson(responseString, userListType);
    }


    public static ArrayList<BinRecordProfile> convertStringToRecordProfileArray(String responseString) {
        Type profileListType = new TypeToken<ArrayList<BinRecordProfile>>() {
        }.getType();
        return new Gson().fromJson(responseString, profileListType);
    }

    public static double convertDistanceToMeter(double value, int INDEX_UNIT_DISTANCE) {
        double result = value;
        if (INDEX_UNIT_DISTANCE != 0) {
            switch (INDEX_UNIT_DISTANCE) {
                case UNIT_DISTANCE.METER:
                    result = value;
                    break;
                case UNIT_DISTANCE.METER_KM:
                    result = (value * 1000d);
                    break;
                case UNIT_DISTANCE.FT:
                    result = (value / 3.281d);
                    break;
                case UNIT_DISTANCE.FT_MI:
                    result = (value * 1609);
                    break;
                case UNIT_DISTANCE.YD:
                    result = (value / 1.094d);
                    break;
                case UNIT_DISTANCE.M_NMI:
                    result = (value * 1852);
                    break;
            }
        }
        return result;
    }

    public static double convertGpsAltitudeToMeter(double value, int INDEX_UNIT_GPS) {
        double result = value;
        if (INDEX_UNIT_GPS != 0) {
            switch (INDEX_UNIT_GPS) {
                case UNIT_GPS.METER:
                    result = value;
                    break;
                case UNIT_GPS.FT:
                    result = (value / 3.281d);
                    break;
            }
        }
        return result;
    }

    public interface UNIT_GPS {
        int METER = 1;
        int FT = 2;
    }

    public static double convertGpsAltitudeFromMeter(double value, int INDEX_UNIT_GPS) {
        double result = value;
        if (INDEX_UNIT_GPS != 0) {
            switch (INDEX_UNIT_GPS) {
                case UNIT_GPS.METER:
                    result = value;
                    break;
                case UNIT_GPS.FT:
                    result = (value * 3.281d);
                    break;
            }
        }
        return result;
    }

    public static String convertDistanceFromMeter(Activity mActivity, double meter) {
        String index_distance_Unit = getSavedData(mActivity, UNIT_DISTANCE_DATA);
        String result = convert2decimalPoints(meter / 1000d) + " km";
        if (isEmptyString(index_distance_Unit))
            return result;
        else {
            switch (Integer.parseInt(index_distance_Unit)) {
                case UNIT_DISTANCE.METER:
                    result = convert2decimalPoints(meter) + " meter";
                    break;
                case UNIT_DISTANCE.METER_KM:
                    result = convert2decimalPoints(meter / 1000d) + " km";
                    break;
                case UNIT_DISTANCE.FT:
                    result = convert2decimalPoints(meter * 3.281d) + " ft";
                    break;
                case UNIT_DISTANCE.FT_MI:
                    result = convert2decimalPoints(meter / 1609d) + " mi";
                    break;
                case UNIT_DISTANCE.YD:
                    result = convert2decimalPoints(meter * 1.094d) + " yd";
                    break;
                case UNIT_DISTANCE.M_NMI:
                    result = convert2decimalPoints(meter / 1852d) + " nmi";
                    break;
            }
        }
        return result;
    }

    public static String convertAreaFromSqMeter(Activity mActivity, double sqMtr) {
        String index_area_Unit = getSavedData(mActivity, UNIT_AREA_DATA);
        String result = convert2decimalPoints(sqMtr * 1.196) + " yd2";
        if (isEmptyString(index_area_Unit))
            return result;
        else {
            switch (Integer.parseInt(index_area_Unit)) {

                case UNIT_AREA.SQ_METER:
                    result = convert2decimalPoints(sqMtr) + " sqMtr";
                    break;
                case UNIT_AREA.METER_2:
                    result = convert2decimalPoints(sqMtr) + " m2";
                    break;
                case UNIT_AREA.HA:
                    result = convert2decimalPoints(sqMtr / 10000) + " ha";
                    break;
                case UNIT_AREA.KM_2:
                    result = convert2decimalPoints(sqMtr / 1e+6) + " km2";
                    break;
                case UNIT_AREA.FT_2:
                    result = convert2decimalPoints(sqMtr * 10.764) + " ft2";
                    break;
                case UNIT_AREA.YD_2:
                    result = convert2decimalPoints(sqMtr * 1.196) + " yd2";
                    break;
                case UNIT_AREA.ACRE:
                    result = convert2decimalPoints(sqMtr / 4047) + " acre";
                    break;
                case UNIT_AREA.MI_2:
                    result = convert2decimalPoints(sqMtr / 1.59e+6) + " mi2";
                    break;
                case UNIT_AREA.NMI_2:
                    result = convert2decimalPoints(sqMtr / 3.43e+6) + " nmi2";
                    break;
            }
        }
        return result;
    }

    public static String getDistanceDoubleDigit(double meterDist) {
        if (meterDist < 1000d) {
            return convert2decimalPoints(meterDist) + " Mtr";
        } else {
            return convert2decimalPoints(meterDist * 0.001d) + " Km";
        }
    }

    public static String convert2decimalPoints(double value) {
        return String.format(Locale.US, "%.2f", value);
    }

    public static ArrayList<ArrayList<LatLng>> getAmcLatLongBoundryLine(Context context) {
        ArrayList<ArrayList<LatLng>> listPolyLineLatLongs = new ArrayList<>();
        try {
            JSONArray jsonCoordinatesArray = new JSONArray(readJsonFileCoordinates(context, R.raw.amc_coordinates));
            for (int j = 0; j < jsonCoordinatesArray.length(); j++) {
                ArrayList<LatLng> listBoundryAmc = new ArrayList<>();
                JSONArray jsonCoordinatesArrayChild = jsonCoordinatesArray.optJSONArray(j);
                for (int i = 0; i < jsonCoordinatesArrayChild.length(); i++) {
                    JSONArray coordinate = jsonCoordinatesArrayChild.optJSONArray(i);
                    Log.e("COORDINATE", "" + coordinate);
                    if (coordinate.length() > 1) {
                        listBoundryAmc.add(new LatLng(Double.parseDouble(coordinate.optString(1)), Double.parseDouble(coordinate.optString(0))));
                    }
                }
                listPolyLineLatLongs.add(listBoundryAmc);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listPolyLineLatLongs;
    }

    public static ArrayList<LatLng> getLatLongLayerPoint(Context context, String geoJsonResponse) {
        JSONObject jsonObjectMain;
        ArrayList<LatLng> listPointLatLongs = new ArrayList<>();
        try {
            if (Utility.isEmptyString(geoJsonResponse))
                jsonObjectMain = new JSONObject(readJsonFileCoordinates(context, R.raw.sewerage));
            else
                jsonObjectMain = new JSONObject(geoJsonResponse);

            JSONArray jsonArrayFeatures = jsonObjectMain.optJSONArray("features");
            for (int j = 0; j < jsonArrayFeatures.length(); j++) {
                JSONObject jsonArrayFeatureObject = jsonArrayFeatures.optJSONObject(j);
                JSONObject jsonObjectGeometry = jsonArrayFeatureObject.optJSONObject("geometry");
                String geoMetriType = jsonObjectGeometry.optString("type");
                JSONArray jsonArrayCoordinates = jsonObjectGeometry.optJSONArray("coordinates");
                if (geoMetriType.equalsIgnoreCase("Point")) {
                    // Here type = Polygon
                    LatLng latLng = new LatLng(jsonArrayCoordinates.optDouble(1), jsonArrayCoordinates.optDouble(0));
                    listPointLatLongs.add(latLng);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listPointLatLongs;
    }

    //    ==============================================================
    public static ArrayList<ArrayList<LatLng>> getLatLongLayerLine(Context context, String geoJsonResponse) {
        LatLng latLng;
        ArrayList<ArrayList<LatLng>> listPolyLines = new ArrayList<>();
        JSONObject jsonObjectMain;
        try {
            if (Utility.isEmptyString(geoJsonResponse))
                jsonObjectMain = new JSONObject(readJsonFileCoordinates(context, R.raw.line));
            else
                jsonObjectMain = new JSONObject(geoJsonResponse);
            JSONArray jsonArrayFeatures = jsonObjectMain.optJSONArray("features");
            for (int j = 0; j < jsonArrayFeatures.length(); j++) {
                ArrayList<LatLng> listLatlongLine1 = new ArrayList<>();
                JSONObject jsonArrayFeatureObject = jsonArrayFeatures.optJSONObject(j);
                JSONObject jsonObjectGeometry = jsonArrayFeatureObject.optJSONObject("geometry");
                String geoMetriType = jsonObjectGeometry.optString("type");
                JSONArray jsonArrayCoordinates = jsonObjectGeometry.optJSONArray("coordinates");
                for (int k = 0; k < jsonArrayCoordinates.length(); k++)// only 1 child obj for polygon but can be multi for MultiPolygon type
                {
                    // 1st child coordinates loop => [0] index
                    // OR It will have JsonArray [0], [1], ...[n]
                    JSONArray jsonArrayCoordinateChild = jsonArrayCoordinates.getJSONArray(k);
                    if (geoMetriType.equalsIgnoreCase("LineString")) {
                        latLng = new LatLng(jsonArrayCoordinateChild.optDouble(1), jsonArrayCoordinateChild.optDouble(0));
                        listLatlongLine1.add(latLng);
                    } else if (geoMetriType.equalsIgnoreCase("MultiLineString")) {
                        ArrayList<LatLng> listLatlongLine2 = new ArrayList<>();
                        for (int p = 0; p < jsonArrayCoordinateChild.length(); p++) {
                            // this contains 100s of latlongs
                            // 3rd child
                            JSONArray latlongList = jsonArrayCoordinateChild.getJSONArray(p);// can take 0
                            if (latlongList.length() > 1) {
                                latLng = new LatLng(latlongList.optDouble(1), latlongList.optDouble(0));
                                listLatlongLine2.add(latLng);
                            }
                        }
                        listPolyLines.add(listLatlongLine2);
                    }
                }
                if (listLatlongLine1.size() > 0) {
                    listPolyLines.add(listLatlongLine1);
                }
                /*if(listPolyLines.size()>1050)
                {
                    break;
                }*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listPolyLines;
    }

    //    ==============================================================
    public static ArrayList<ArrayList<LatLng>> getLatLongBoundryPolygon(Context context, String geoJsonResponse) {
        LatLng latLng;
        ArrayList<LatLng> listLatlongPoly1 = new ArrayList<>();
        ArrayList<ArrayList<LatLng>> listPolyGons = new ArrayList<>();
        JSONObject jsonObjectMain;
        try {
            if (Utility.isEmptyString(geoJsonResponse))
                jsonObjectMain = new JSONObject(readJsonFileCoordinates(context, R.raw.amc_ward_boundry));
            else
                jsonObjectMain = new JSONObject(geoJsonResponse);
            JSONArray jsonArrayFeatures = jsonObjectMain.optJSONArray("features");
            listPolyGons.clear();
            for (int j = 0; j < jsonArrayFeatures.length(); j++) {
                JSONObject jsonArrayFeatureObject = jsonArrayFeatures.optJSONObject(j);
                JSONObject jsonObjectGeometry = jsonArrayFeatureObject.optJSONObject("geometry");
                String geoMetriType = jsonObjectGeometry.optString("type");
                JSONArray jsonArrayCoordinates = jsonObjectGeometry.optJSONArray("coordinates");
                for (int k = 0; k < jsonArrayCoordinates.length(); k++)// only 1 child obj for polygon but can be multi for MultiPolygon type
                {
                    // 1st child coordinates loop => [0] index
                    // OR It will have JsonArray [0], [1], ...[n]
                    JSONArray jsonArrayCoordinateChild = jsonArrayCoordinates.getJSONArray(k);
                    listLatlongPoly1 = new ArrayList<>();
                    for (int h = 0; h < jsonArrayCoordinateChild.length(); h++) {
                        // 2nd layer coordinates child => It will have 200 latlongs for type = polygon
                        // 2nd layer coordinates child => It will have JsonArray [0]
                        JSONArray latlongArr = jsonArrayCoordinateChild.getJSONArray(h);// can take Zero
                        // Here JsonArray with => [0] index for type = multipolygon
                        // Here we are checking Coordinate's child's first child type
                        if (latlongArr.length() > 0 && latlongArr.opt(0) instanceof Double) {
                            // Here type = Polygon
                            if (latlongArr.length() > 1) {
                                latLng = new LatLng(latlongArr.optDouble(1), latlongArr.optDouble(0));
                                listLatlongPoly1.add(latLng);
                            }
                        } else if (latlongArr.length() > 0 && latlongArr.opt(0) instanceof JSONArray) {
                            // Here type = MultiPolygon
                            ArrayList<LatLng> listLatlongPoly2 = new ArrayList<>();
                            for (int p = 0; p < latlongArr.length(); p++) {
                                // this contains 100s of latlongs
                                // 3rd child
                                JSONArray latlongList = latlongArr.getJSONArray(p);// can take 0
                                if (latlongArr.length() > 1) {
                                    latLng = new LatLng(latlongList.optDouble(1), latlongList.optDouble(0));
                                    listLatlongPoly2.add(latLng);
                                }
                            }
                            listPolyGons.add(listLatlongPoly2);
                        }
                    }
                    if (listLatlongPoly1.size() > 0) {
                        listPolyGons.add(listLatlongPoly1);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listPolyGons;
    }

    public static String readJsonFileCoordinates(Context context, int rawRes) {
        String jsonString = "";
        InputStream is = context.getResources().openRawResource(rawRes);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        jsonString = writer.toString();
        return jsonString;
    }
//    ==============================================================

    public static void setStyleMap(Context context, GoogleMap mMap, int mSelectedStyleId) {
        MapStyleOptions style;
        switch (mSelectedStyleId) {
            case R.string.style_label_retro:
                // Sets the retro style via raw resource JSON.
                style = MapStyleOptions.loadRawResourceStyle(context, R.raw.mapstyle_retro);
                break;
            case R.string.style_label_night:
                // Sets the night style via raw resource JSON.
                style = MapStyleOptions.loadRawResourceStyle(context, R.raw.mapstyle_night);
                break;
            case R.string.style_label_grayscale:
                // Sets the grayscale style via raw resource JSON.
                style = MapStyleOptions.loadRawResourceStyle(context, R.raw.mapstyle_grayscale);
                break;
            case R.string.style_label_no_pois_no_transit:
                // Sets the no POIs or transit style via JSON string.
                style = new MapStyleOptions("[" +
                        "  {" +
                        "    \"featureType\":\"poi.business\"," +
                        "    \"elementType\":\"all\"," +
                        "    \"stylers\":[" +
                        "      {" +
                        "        \"visibility\":\"off\"" +
                        "      }" +
                        "    ]" +
                        "  }," +
                        "  {" +
                        "    \"featureType\":\"transit\"," +
                        "    \"elementType\":\"all\"," +
                        "    \"stylers\":[" +
                        "      {" +
                        "        \"visibility\":\"off\"" +
                        "      }" +
                        "    ]" +
                        "  }" +
                        "]");
                break;
            case R.string.style_label_default:
                // Removes previously set style, by setting it to null.
                style = null;
                break;
            default:
                return;
        }
        mMap.setMapStyle(style);
    }

    public static Calendar getEveningStartTime() {
        Calendar calendar8pm = Calendar.getInstance(Locale.US);

        calendar8pm.set(Calendar.HOUR_OF_DAY, 18);
        calendar8pm.set(Calendar.MINUTE, 0);
        calendar8pm.set(Calendar.SECOND, 0);
        return calendar8pm;
    }

    public static Calendar getNoonStartTime() {
        Calendar calendar12pm = Calendar.getInstance(Locale.US);

        calendar12pm.set(Calendar.HOUR_OF_DAY, 12);
        calendar12pm.set(Calendar.MINUTE, 0);
        calendar12pm.set(Calendar.SECOND, 0);
        return calendar12pm;
    }

    public static void openKeyboard(Activity context, EditText editText)
    {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static String getStandardFormDetails()
    {
        String result = "";
        try {
            JSONArray jsonArray = new JSONArray("[{\n" +
                    "\t\t\"type\": \"textarea\",\n" +
                    "\t\t\"label\": \"Description\",\n" +
                    "\t\t\"input_id\": \"textarea-1\",\n" +
                    "\t\t\"is_history\": \"No\"\n" +
                    "\t},\n" +
                    "\t{\n" +
                    "\t\t\"type\": \"image\",\n" +
                    "\t\t\"label\": \"Photo\",\n" +
                    "\t\t\"input_id\": \"photo-1\",\n" +
                    "\t\t\"is_history\": \"No\"\n" +
                    "\t}\n" +
                    "]");
            result = jsonArray.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void insertStandard_P_L_F_FD_ToDb(Activity context, DataBaseHelper dataBaseHelper, String firstName)
    {
        String projectId = PROJECT_ID_DEFAULT;

        String formNameLine = "Standard Line";
        String formIdLine = "10000";
        int layerIdLine = 10000;

        String formNamePolygon = "Standard Polygon";
        String formIdPolygon = "10001";
        int layerIdPolygon = 10001;

        String formNamePoint = "Standard Point";
        String formIdPoint = "10002";
        int layerIdPoint = 10002;

        dataBaseHelper.open();
        dataBaseHelper.insertForm(formIdLine, formNameLine, projectId);
        dataBaseHelper.open();
        dataBaseHelper.insertFormDetails(formIdLine, getStandardFormDetails(),"","", DataBaseHelper.isSyncOffline, DataBaseHelper.FORM_TYPE.TYPE_NEW);

        ArrayList<BinLayerProject> listLayers = new ArrayList<>();
        BinLayerProject bin1 = new BinLayerProject();
        bin1.setId(layerIdLine);
        bin1.setProjectId(projectId);
        bin1.setName(firstName+" - "+MapsActivity.LAYER_TYPE.Line);
        bin1.setType(MapsActivity.LAYER_TYPE.Line);
        bin1.setEnable(true);
        bin1.setActive(false);
        bin1.setCustomMade(true);
        bin1.setDisplay(SystemUtility.getColorHexCode(context));
        bin1.setIndex(1);
        bin1.setArrLatLong("");
        bin1.setFormId(formIdLine);
        bin1.setFormName(formNameLine);
        listLayers.add(bin1);

        dataBaseHelper.open();
        dataBaseHelper.insertLayers(bin1.getProjectId(), bin1.getName(), bin1.getType(), true, false, bin1.getDisplay(), 0, false, "", bin1.getFormId(), bin1.getFormName());

        dataBaseHelper.open();
        dataBaseHelper.insertForm(formIdPolygon, formNamePolygon, projectId);
        dataBaseHelper.open();
        dataBaseHelper.insertFormDetails(formIdPolygon, getStandardFormDetails(),"","", DataBaseHelper.isSyncOffline, DataBaseHelper.FORM_TYPE.TYPE_NEW);

        BinLayerProject bin2 = new BinLayerProject();
        bin2.setId(layerIdPolygon);
        bin2.setProjectId(projectId);
        bin2.setName(firstName+" - "+MapsActivity.LAYER_TYPE.Polygon);
        bin2.setType(MapsActivity.LAYER_TYPE.Polygon);
        bin2.setEnable(true);
        bin2.setActive(false);
        bin2.setCustomMade(true);
        bin2.setDisplay(SystemUtility.getColorHexCode(context));
        bin2.setIndex(2);
        bin2.setArrLatLong("");
        bin2.setFormId(formIdPolygon);
        bin2.setFormName(formNamePolygon);
        listLayers.add(bin2);

        dataBaseHelper.open();
        dataBaseHelper.insertLayers(bin2.getProjectId(), bin2.getName(), bin2.getType(), true, false, bin2.getDisplay(), 0, false, "", bin2.getFormId(), bin2.getFormName());

        dataBaseHelper.open();
        dataBaseHelper.insertForm(formIdPoint, formNamePoint, projectId);
        dataBaseHelper.open();
        dataBaseHelper.insertFormDetails(formIdPoint, getStandardFormDetails(),"","", DataBaseHelper.isSyncOffline, DataBaseHelper.FORM_TYPE.TYPE_NEW);

        BinLayerProject bin3 = new BinLayerProject();
        bin3.setId(layerIdPoint);
        bin3.setProjectId(projectId);
        bin3.setName(firstName+" - "+MapsActivity.LAYER_TYPE.Point);
        bin3.setType(MapsActivity.LAYER_TYPE.Point);
        bin3.setEnable(true);
        bin3.setActive(false);
        bin3.setCustomMade(true);
        bin3.setDisplay(SystemUtility.getColorHexCode(context));
        bin3.setIndex(3);
        bin3.setArrLatLong("");
        bin3.setFormId(formIdPoint);
        bin3.setFormName(formNamePoint);
        listLayers.add(bin3);

        dataBaseHelper.open();
        dataBaseHelper.insertLayers(bin3.getProjectId(), bin3.getName(), bin3.getType(), true, false, bin3.getDisplay(), 0, false, "", bin3.getFormId(), bin3.getFormName());

        dataBaseHelper.open();
        dataBaseHelper.insertProject(projectId, "Standard", "", "", new Gson().toJson(listLayers));

        dataBaseHelper.close();
    }

    public static ArrayList<BinLayerProject> getNormalUserLayers(String firstName, Activity mActivity)
    {
        ArrayList<BinLayerProject> list = new ArrayList<>();
        BinLayerProject bin1 = new BinLayerProject();
        bin1.setId(10000);
        bin1.setProjectId("0");
        bin1.setName(firstName+" - "+MapsActivity.LAYER_TYPE.Line);
        bin1.setType(MapsActivity.LAYER_TYPE.Line);
        bin1.setEnable(true);
        bin1.setActive(false);
        bin1.setCustomMade(true);
        bin1.setDisplay(SystemUtility.getColorHexCode(mActivity));
        bin1.setIndex(1);
        bin1.setArrLatLong("");
        list.add(bin1);

        BinLayerProject bin2 = new BinLayerProject();
        bin2.setId(10001);
        bin2.setProjectId("0");
        bin2.setName(firstName+" - "+MapsActivity.LAYER_TYPE.Polygon);
        bin2.setType(MapsActivity.LAYER_TYPE.Polygon);
        bin2.setEnable(true);
        bin2.setActive(false);
        bin2.setCustomMade(true);
        bin2.setDisplay(SystemUtility.getColorHexCode(mActivity));
        bin2.setIndex(2);
        bin2.setArrLatLong("");
        list.add(bin2);

        BinLayerProject bin3 = new BinLayerProject();
        bin3.setId(10002);
        bin3.setProjectId("0");
        bin3.setName(firstName+" - "+MapsActivity.LAYER_TYPE.Point);
        bin3.setType(MapsActivity.LAYER_TYPE.Point);
        bin3.setEnable(true);
        bin3.setActive(false);
        bin3.setCustomMade(true);
        bin3.setDisplay(SystemUtility.getColorHexCode(mActivity));
        bin3.setIndex(3);
        bin3.setArrLatLong("");
        list.add(bin3);
        return list;
    }



    // Change by Rahul Suthar
    public static void getUserInsideAreaAlertDialogBox(Context context,DialogBoxOKClick dialogBoxOKClick){
        AlertDialog alertDialog = new AlertDialog.Builder(context)
               // .setIcon(R.drawable.ic_logo_surveybaba)
                .setTitle("Alert")
                .setMessage("You Are OutSide Area/Zone You Cannot Access This Option")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, i) -> {
                    dialogBoxOKClick.OkClick(dialog);
                })
                .create();
        alertDialog.show();
    }

    public static void getOKCancelDialogBox(Context context, String title,String  msg, DialogBoxOKClick dialogBoxOKClick){
        AlertDialog alertDialog = new AlertDialog.Builder(context)
               // .setIcon(R.drawable.ic_logo_surveybaba)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, i) -> {
                    dialogBoxOKClick.OkClick(dialog);
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .create();
        alertDialog.show();
    }


    public static void getSyncCancelDialogBox(Context context, String title,String  msg, DialogBoxOKClick dialogBoxOKClick){
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_logo_surveybaba)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Sync", (dialog, i) -> {
                    dialogBoxOKClick.OkClick(dialog);
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .create();
        alertDialog.show();
    }

    public static void getYesNoDialogBox(Context context, String title,String  msg, DialogBoxOKClick dialogBoxOKClick){
        AlertDialog alertDialog = new AlertDialog.Builder(context)
              //  .setIcon(R.drawable.ic_logo_surveybaba)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, i) -> {
                    dialogBoxOKClick.OkClick(dialog);
                })
                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss())
                .create();
        alertDialog.show();
    }


    public static void getYesNoDialogBox(Context context,String title,String str, DialogBoxOKClick dialogBoxOKClick, DialogBoxCancelClick dialogBoxCancelClick){
        AlertDialog alertDialog = new AlertDialog.Builder(context)
               // .setIcon(R.drawable.ic_logo_surveybaba)
                .setTitle(title)
                .setMessage(str)
                .setCancelable(false)
                .setPositiveButton("Yes", (dialogOk, i) -> dialogBoxOKClick.OkClick(dialogOk))
                .setNegativeButton("No", (dialogCancel, i) -> dialogBoxCancelClick.CancelClick(dialogCancel))
                .create();
        alertDialog.show();
    }
    public static void getOKDialogBox(Context context, String title,String  msg, DialogBoxOKClick dialogBoxOKClick){
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, i) -> {
                    dialogBoxOKClick.OkClick(dialog);
                })
                .create();
        alertDialog.show();
    }
    public static void getOKDialogBox(Context context, String  msg, DialogBoxOKClick dialogBoxOKClick){
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, i) -> {
                    dialogBoxOKClick.OkClick(dialog);
                })
                .create();
        alertDialog.show();
    }

    public static void getSelectedItemDialogBox(Context context,String title, String[] itemsLists, DialogBoxSelectedItem dialogBoxSelectedItem){

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(itemsLists, (dialog, which) -> {
                dialogBoxSelectedItem.onSelectedItem(itemsLists[which]);
                dialog.dismiss();
        });
        builder.show();
    }


    public static void getCallDialogBox(Context context, DialogBoxOKClick dialogBoxOKClick){
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage("You Want to Call this Person?")
                .setCancelable(false)
                .setPositiveButton("Call", (dialog, i) -> {
                    dialogBoxOKClick.OkClick(dialog);
                })
                .setNegativeButton("Cancel", (dialog, i) -> {
                    dialog.dismiss();
                })
                .create();
        alertDialog.show();
    }

    public static void getMailDialogBox(Context context, DialogBoxOKClick dialogBoxOKClick){
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage("You Want to Mail this Person?")
                .setCancelable(false)
                .setPositiveButton("Mail", (dialog, i) -> {
                    dialogBoxOKClick.OkClick(dialog);
                })
                .setNegativeButton("Cancel", (dialog, i) -> {
                    dialog.dismiss();
                })
                .create();
        alertDialog.show();
    }

    public static void openCallDial(Context context , String number){
        if(!isEmptyString(number)){
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
            context.startActivity(callIntent);
        }
        else{
            Log.e("Error","Number is Empty/Null");
        }
    }

    public static void openEmail(Context context , String emailID){
        if(!isEmptyString(emailID)){
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + emailID));
            context.startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
        }
        else{
            Log.e("Error","Email ID is Empty/Null");
        }
    }

    public static long getToken() {
        Random rand = new Random();
        //long x = (long)(rand.nextDouble()*100000000000000L);
        long x = (long)(rand.nextDouble()*100000000L);
        long y = (long)(rand.nextDouble()*100000000L);
        @SuppressLint("DefaultLocale") String s =  String.format("%08d", x)+ String.format("%08d", y);
        return Long.parseLong(s);
    }
    public static String getRecordDate()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return df.format(c.getTime());
    }


    public static String getDateTime()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return df.format(c.getTime());
    }

    public static Bitmap decodeBase64Image(String base64Image){
        // decode base64 string
        byte[] bytes= Base64.decode(base64Image,Base64.DEFAULT);
        // Initialize bitmap
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

    public static ArrayList<FormLatLon> convertStringToFormLatLon(String GeomArray){
        java.lang.reflect.Type listType1 = new TypeToken<ArrayList<FormLatLon>>() {}.getType();
        return new Gson().fromJson(GeomArray, listType1);
    }

    public static ArrayList<LatLng> convertStringToGeoFenceLatLon(String geoFenceArray){
        java.lang.reflect.Type Type = new TypeToken<ArrayList<LatLng>>() {}.getType();
        return new Gson().fromJson(geoFenceArray, Type);
    }

    public static SurveyFormLocalModel convertStringToSurveyFormLocalModel(String formData){
        java.lang.reflect.Type listType = new TypeToken<SurveyFormLocalModel>() {}.getType();
        return new Gson().fromJson(formData, listType);
    }

    public static void SyncYourDataAlert(Context context){
        Utility.getOKDialogBox(context, "Sync Alert", "Please Sync your Data for your Safety else your data may be loss.", dialog -> dialog.dismiss());
    }

    public static void reDirectToBluetoothSetting(Context context){
        Intent intentOpenBluetoothSettings = new Intent();
        intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        context.startActivity(intentOpenBluetoothSettings);
    }

    public static String getUploadFilePath(ArrayList<FormDetailData> listFormDetailsData, int pos){

        if(listFormDetailsData.get(pos).getType().equals(TYPE_CAMERA)){
            //Log.e("FormDetailsActivity", listFormDetailsData.get(pos).getFill_value());
            // sb_CameraPathLocal.append("local").append("#").append(destFile.getAbsolutePath());
           // Log.e("FormDetailsActivity",listFormDetailsData.get(pos).getFill_value().split("#")[1]);
            return listFormDetailsData.get(pos).getFill_value().split("#")[1];
        }
        else{
            // sb_AudioPathLocal.append("local").append("%").append(destFile.getName()).append("#").append(destFile.getPath());
            String[] filesList = listFormDetailsData.get(pos).getFill_value().split(",");
            StringBuilder sb_FilePath   = new StringBuilder();
            for(int i=0; i<filesList.length; i++){
                String filePath = filesList[i].split("%")[1].split("#")[1];
                sb_FilePath.append(filePath);
                if(i < filesList.length - 1){
                    sb_FilePath.append(",");
                }
            }
            return sb_FilePath.toString();
        }

    }


    public static void someThingIsWrongToaster(Context context){
        Toast.makeText(context, "Something is Wrong", Toast.LENGTH_SHORT).show();
    }

//------------------------------------------------------- Interface -------------------------------------------------------------------------------------------------------------------------------------------------

    public interface OnClickMap {
        void onClickMap(String mapType);
    }
    public interface DialogBoxOKClick{
        void OkClick(DialogInterface dialog);
    }
    public interface DialogBoxCancelClick{
        void CancelClick(DialogInterface dialog);
    }

    public interface DialogBoxSelectedItem{
        void onSelectedItem(String selectedItem);
    }

    public interface SELECTED_ITEMS {
        String DGPS = "DGPS";
        String PHONEGPS = "Phone GPS";
    }

    public interface MIN_DISTANCE{
        String METER_5  =  "5 Meter";
        String METER_10 = "10 Meter";
        String METER_15 = "15 Meter";
        String METER_20 = "20 Meter";
        String METER_25 = "25 Meter";
        String METER_30 = "30 Meter";
        String METER_35 = "35 Meter";
        String METER_40 = "40 Meter";
        String METER_45 = "45 Meter";
        String METER_50 = "50 Meter";
    }

    public interface MIN_WALKING{
        String METER_5  =  "5";
        String METER_10 = "10";
        String METER_15 = "15";
        String METER_20 = "20";
        String METER_25 = "25";
        String METER_30 = "30";
        String METER_35 = "35";
        String METER_40 = "40";
        String METER_45 = "45";
        String METER_50 = "50";
    }

    public static void setToVerticalRecycleView(Context mActivity, RecyclerView recyclerView,RecyclerView.Adapter<?> adapter ){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public static LatLng getPolygonCenterPoint(ArrayList<LatLng> polygonPointsList){
        LatLng centerLatLng;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(int i = 0 ; i < polygonPointsList.size() ; i++) { builder.include(polygonPointsList.get(i)); }
        LatLngBounds bounds = builder.build();
        centerLatLng =  bounds.getCenter();
        return centerLatLng;
    }


}