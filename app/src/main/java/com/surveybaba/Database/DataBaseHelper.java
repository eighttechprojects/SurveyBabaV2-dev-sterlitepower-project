package com.surveybaba.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.surveybaba.DTO.FormDTO;
import com.surveybaba.FormBuilder.FormDataModel;
import com.surveybaba.FormBuilder.FormDetailModel;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.BinLayerProject;
import com.surveybaba.model.CameraModule;
import com.surveybaba.model.GISSurveyModel;
import com.surveybaba.model.GeoFenceModel.GeoFenceModel;
import com.surveybaba.model.GpsTrackingModule;
import com.surveybaba.model.OnlineLayerModel;
import com.surveybaba.model.ProjectLayerModel;
import com.surveybaba.model.ProjectModule;
import com.surveybaba.model.ProjectWorkModel;
import com.surveybaba.model.TrackingData;
import com.surveybaba.model.TrackingStatusData;
import com.surveybaba.model.WalkingModule;

import org.json.JSONArray;

import java.sql.Array;
import java.sql.DataTruncation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DataBaseHelper extends SQLiteOpenHelper {

	public static final String TAG = "DataBaseHelper";
	public static final String DATABASE_NAME = "SurveyBaba4.db";
	public static final int DATABASE_VERSION = 17;
	public static int isSyncOffline = 0;
	public static int isSyncOnline = 1;
	public static String keyParam_UserId = "user_id";
	public static String keyParam_record_date = "record_date";
	public static String keyParam_formid = "formid";
	public static String keyParam_project_id = "project_id";
	public static String keyParam_form_uid = "uniq_id";
	public static String keyParam_data = "data";
	public static String keyParam_appversion = "version";
	public static String keyParam_latitude = "latitude";
	public static String keyParam_longitude = "longitude";

	// Tracking
	public static String keyParam_tracking_user_id = "id";
	public static String keyParam_tracking_user_latitude = "latitude";
	public static String keyParam_tracking_user_longitude = "longitude";
	public static String keyParam_tracking_status_user_id = "user_id";
	public static String keyParam_tracking_user_status_latitude = "latitude";
	public static String keyParam_tracking_user_status_longitude = "longitude";
	public static String keyParam_tracking_user_status_created_date = "created_date";
	public static String keyParam_tracking_user_status_version = "version";
	public static String keyParam_tracking_isInsideCoverageArea = "isInsideCoverageArea";

	// Common Field
	public static final String keyParamID         = "id";
	public static final String keyParamUserID     = "user_id";
	public static final String keyParamProjectID  = "project_id";
	public static final String keyParamSurveyID   = "survey_id";
	public static final String keyParamLayerID    = "layer_id";
	public static final String keyParamWorkID     = "work_id";
	public static final String keyParamFormID     = "form_id";
	public static final String keyParamStageID    = "stage_id";
	public static final String keyParamActivityID = "activity_id";
	public static final String keyParamImage      = "image";
	public static final String keyParamFile       = "file";
	public static final String keyParamCamera     = "camera";
	public static final String keyParamAudio      = "audio";
	public static final String keyParamVideo      = "video";
	public static final String keyParamMode       = "mode";
	public static final String keyParamIcon       = "icon";
	public static final String keyParamToken      = "token";
	public static final String keyParamLat        = "latitude";
	public static final String keyParamLon        = "longitude";
	public static final String keyParamDateTime   = "datetime";
	public static final String keyParamVersion    = "version";
	public static final String keyParamLatLong    = "latlong";
	public static final String keyParamUniqueNumber ="unique_number";


	// Camera Image
	public static final String keyParamCameraImageName   = "name";
	public static final String keyParamCameraImage  = "image";
	public static final String keyParamCameraDesc   = "description";
	// GPS Tracking
	public static final String keyParamType = "mode";

	SQLiteDatabase db;
	Context ctx;

	public interface FORM_TYPE
	{
		String TYPE_NEW = "NEW";
		String TYPE_COMPLETE = "COMPLETE";
		String TYPE_RESURVEY_COMPLETE = "RESURVEY_COMPLETE";
	}
//---------------------- Change by Rahul Suthar

	// Camera Image Save
	public static final String CREATE_TABLE_CAMERA = "CREATE TABLE Camera(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id VARCHAR(100),latitude VARCHAR(100), longitude VARCHAR(100),image TEXT, name VARCHAR(200), description TEXT, datetime VARCHAR(100),version VARCHAR(10))";
	public static final String DROP_TABLE_CAMERA   = "DROP TABLE Camera";
	public static final String DELETE_TABLE_CAMERA = "DELETE FROM Camera";
	public static final String GET_CAMERA          = "SELECT * FROM Camera";

	// TimeLine Image Save
	public static final String CREATE_TABLE_TIMELINE_IMAGE = "CREATE TABLE TimeLineImage(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id VARCHAR(100),latitude VARCHAR(100), longitude VARCHAR(100),image TEXT, name VARCHAR(200), description TEXT, datetime VARCHAR(100),version VARCHAR(10))";
	public static final String DROP_TABLE_TIMELINE_IMAGE   = "DROP TABLE TimeLineImage";
	public static final String DELETE_TABLE_TIMELINE_IMAGE = "DELETE FROM TimeLineImage";
	public static final String GET_TIMELINE_IMAGE          = "SELECT * FROM TimeLineImage";

	// TimeLine Image Local Save
	public static final String CREATE_TABLE_TIMELINE_IMAGE_LOCAL = "CREATE TABLE TimeLineImageLocal(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id VARCHAR(100),latitude VARCHAR(100), longitude VARCHAR(100),image TEXT, name VARCHAR(200), description TEXT, datetime VARCHAR(100),version VARCHAR(10))";
	public static final String DROP_TABLE_TIMELINE_IMAGE_LOCAL   = "DROP TABLE TimeLineImageLocal";
	public static final String DELETE_TABLE_TIMELINE_IMAGE_LOCAL = "DELETE FROM TimeLineImageLocal";
	public static final String GET_TIMELINE_IMAGE_LOCAL          = "SELECT * FROM TimeLineImageLocal";

	// TimeLine S Image Local Save
	public static final String CREATE_TABLE_TIMELINE_S_IMAGE_LOCAL = "CREATE TABLE TimeLineSImageLocal(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id VARCHAR(100),latitude VARCHAR(100), longitude VARCHAR(100),image TEXT, description TEXT, datetime VARCHAR(100))";
	public static final String DROP_TABLE_TIMELINE_S_IMAGE_LOCAL   = "DROP TABLE TimeLineSImageLocal";
	public static final String DELETE_TABLE_TIMELINE_S_IMAGE_LOCAL = "DELETE FROM TimeLineSImageLocal";
	public static final String GET_TIMELINE_S_IMAGE_LOCAL          = "SELECT * FROM TimeLineSImageLocal";


	// Map Camera Image Save
	public static final String CREATE_TABLE_MAP_CAMERA_IMAGE = "CREATE TABLE MapCameraImage(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id VARCHAR(100),project_id VARCHAR(100),work_id VARCHAR(100),latitude VARCHAR(100), longitude VARCHAR(100),image TEXT, name VARCHAR(200), description TEXT, datetime VARCHAR(100),version VARCHAR(10))";
	public static final String DROP_TABLE_MAP_CAMERA_IMAGE   = "DROP TABLE MapCameraImage";
	public static final String DELETE_TABLE_MAP_CAMERA_IMAGE = "DELETE FROM MapCameraImage";
	public static final String GET_MAP_CAMERA_IMAGE          = "SELECT * FROM MapCameraImage";

	// Map Camera Image Local Save
	public static final String CREATE_TABLE_MAP_CAMERA_IMAGE_LOCAL = "CREATE TABLE MapCameraImageLocal(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id VARCHAR(100),project_id VARCHAR(100),work_id VARCHAR(100),latitude VARCHAR(100), longitude VARCHAR(100),image TEXT, name VARCHAR(200), description TEXT, datetime VARCHAR(100),version VARCHAR(10))";
	public static final String DROP_TABLE_MAP_CAMERA_IMAGE_LOCAL   = "DROP TABLE MapCameraImageLocal";
	public static final String DELETE_TABLE_MAP_CAMERA_IMAGE_LOCAL = "DELETE FROM MapCameraImageLocal";
	public static final String GET_MAP_CAMERA_IMAGE_LOCAL          = "SELECT * FROM MapCameraImageLocal";

	// Map Camera Image S Local Save
	public static final String CREATE_TABLE_MAP_CAMERA_IMAGE_S_LOCAL = "CREATE TABLE MapCameraImageSLocal(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id VARCHAR(100),project_id VARCHAR(100),work_id VARCHAR(100),latitude VARCHAR(100), longitude VARCHAR(100),image TEXT, description TEXT, datetime VARCHAR(100))";
	public static final String DROP_TABLE_MAP_CAMERA_IMAGE_S_LOCAL   = "DROP TABLE MapCameraImageSLocal";
	public static final String DELETE_TABLE_MAP_CAMERA_IMAGE_S_LOCAL = "DELETE FROM MapCameraImageSLocal";
	public static final String GET_MAP_CAMERA_IMAGE_S_LOCAL          = "SELECT * FROM MapCameraImageSLocal";


	// GIS Map Camera Image Save
	public static final String CREATE_TABLE_GIS_MAP_CAMERA_IMAGE = "CREATE TABLE GISMapCameraImage(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id VARCHAR(100),survey_id VARCHAR(100),work_id VARCHAR(100),latitude VARCHAR(100), longitude VARCHAR(100),image TEXT, name VARCHAR(200), description TEXT, datetime VARCHAR(100),version VARCHAR(10))";
	public static final String DROP_TABLE_GIS_MAP_CAMERA_IMAGE   = "DROP TABLE GISMapCameraImage";
	public static final String DELETE_TABLE_GIS_MAP_CAMERA_IMAGE = "DELETE FROM GISMapCameraImage";
	public static final String GET_GIS_MAP_CAMERA_IMAGE          = "SELECT * FROM GISMapCameraImage";

	// GIS Map Camera Image Local Save
	public static final String CREATE_TABLE_GIS_MAP_CAMERA_IMAGE_LOCAL = "CREATE TABLE GISMapCameraImageLocal(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id VARCHAR(100),survey_id VARCHAR(100),work_id VARCHAR(100),latitude VARCHAR(100), longitude VARCHAR(100),image TEXT, name VARCHAR(200), description TEXT, datetime VARCHAR(100),version VARCHAR(10))";
	public static final String DROP_TABLE_GIS_MAP_CAMERA_IMAGE_LOCAL   = "DROP TABLE GISMapCameraImageLocal";
	public static final String DELETE_TABLE_GIS_MAP_CAMERA_IMAGE_LOCAL = "DELETE FROM GISMapCameraImageLocal";
	public static final String GET_GIS_MAP_CAMERA_IMAGE_LOCAL          = "SELECT * FROM GISMapCameraImageLocal";

	// GIS Map Camera Image S Local Save
	public static final String CREATE_TABLE_GIS_MAP_CAMERA_IMAGE_S_LOCAL = "CREATE TABLE GISMapCameraImageSLocal(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id VARCHAR(100),survey_id VARCHAR(100),work_id VARCHAR(100),latitude VARCHAR(100), longitude VARCHAR(100),image TEXT, description TEXT, datetime VARCHAR(100))";
	public static final String DROP_TABLE_GIS_MAP_CAMERA_IMAGE_S_LOCAL   = "DROP TABLE GISMapCameraImageSLocal";
	public static final String DELETE_TABLE_GIS_MAP_CAMERA_IMAGE_S_LOCAL = "DELETE FROM GISMapCameraImageSLocal";
	public static final String GET_GIS_MAP_CAMERA_IMAGE_S_LOCAL          = "SELECT * FROM GISMapCameraImageSLocal";


	// Walking Tracking  Details
	public static final String CREATE_TABLE_WALKING_TRACKING = "CREATE TABLE WalkTracking(id INTEGER PRIMARY KEY AUTOINCREMENT, latitude VARCHAR(100), longitude VARCHAR(100))";
	public static final String DROP_TABLE_WALKING_TRACKING = "DROP TABLE WalkTracking";
	public static final String DELETE_TABLE_WALKING_TRACKING = "DELETE FROM WalkTracking";
	public static final String GET_WALKING_TRACKING = "SELECT * FROM WalkTracking";

	// Walking/ Tap Tracking  Details
	public static final String CREATE_TABLE_GPS_TRACKING = "CREATE TABLE GpsTracking(id INTEGER PRIMARY KEY AUTOINCREMENT,user_id VARCHAR(100), mode VARCHAR(100), latlong TEXT, datetime VARCHAR(100), version VARCHAR(10), token VARCHAR(100), unique_number VARCHAR(100))";
	public static final String DROP_TABLE_GPS_TRACKING = "DROP TABLE GpsTracking";
	public static final String DELETE_TABLE_GPS_TRACKING = "DELETE FROM GpsTracking";
	public static final String GET_GPS_TRACKING = "SELECT * FROM GpsTracking";

	// Walking/ Tracking  Details
	public static final String CREATE_TABLE_TRACKING = "CREATE TABLE Tracking(id VARCHAR(50), latitude VARCHAR(100), longitude VARCHAR(100), isInsideCoverageArea VARCHAR(100) )";
	public static final String DROP_TABLE_TRACKING = "DROP TABLE Tracking";
	public static final String DELETE_TABLE_TRACKING = "DELETE FROM Tracking";
	public static final String GET_TRACKING = "SELECT * FROM Tracking";

	// User Tracking  Details
	public static final String CREATE_TABLE_TRACKING_STATUS = "CREATE TABLE TrackingStatus(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id VARCHAR(50), latitude VARCHAR(100), longitude VARCHAR(100), created_date VARCHAR(100), version VARCHAR(100), unique_number VARCHAR(100) )";
	public static final String DROP_TABLE_TRACKING_STATUS = "DROP TABLE TrackingStatus";
	public static final String DELETE_TABLE_TRACKING_STATUS = "DELETE FROM TrackingStatus";
	public static final String GET_TRACKING_STATUS = "SELECT * FROM TrackingStatus";


	// GIS Survey
	public static final String CREATE_TABLE_GIS_SURVEY = "CREATE TABLE GISSurvey(data TEXT)";
	public static final String DROP_TABLE_GIS_SURVEY = "DROP TABLE GISSurvey";
	public static final String DELETE_TABLE_GIS_SURVEY = "DELETE FROM GISSurvey";
	public static final String GET_GIS_SURVEY = "SELECT * FROM GISSurvey";


	// GIS Survey Array
	public static final String CREATE_TABLE_GIS_SURVEY_Array = "CREATE TABLE GISSurveyArray(survey_id VARCHAR(500), survey_name VARCHAR(500))";
	public static final String DROP_TABLE_GIS_SURVEY_Array = "DROP TABLE GISSurveyArray";
	public static final String DELETE_TABLE_GIS_SURVEY_Array = "DELETE FROM GISSurveyArray";
	public static final String GET_GIS_SURVEY_Array = "SELECT * FROM GISSurveyArray";

	// GIS Survey Work Array
	public static final String CREATE_TABLE_GIS_SURVEY_WORK_Array = "CREATE TABLE GISSurveyWorkArray(survey_id VARCHAR(500), stage_id VARCHAR(500), activity_id VARCHAR(500), work_id VARCHAR(500), work_name VARCHAR(500))";
	public static final String DROP_TABLE_GIS_SURVEY_WORK_Array = "DROP TABLE GISSurveyWorkArray";
	public static final String DELETE_TABLE_GIS_SURVEY_WORK_Array = "DELETE FROM GISSurveyWorkArray";
	public static final String GET_GIS_SURVEY_WORK_Array = "SELECT * FROM GISSurveyWorkArray";

	// User Project
	public static final String CREATE_TABLE_PROJECT_USER = "CREATE TABLE ProjectUser(data TEXT)";
	public static final String DROP_TABLE_PROJECT_USER = "DROP TABLE ProjectUser";
	public static final String DELETE_TABLE_PROJECT_USER = "DELETE FROM ProjectUser";
	public static final String GET_PROJECT_USER = "SELECT * FROM ProjectUser";

	// Project Array
	public static final String CREATE_TABLE_PROJECT_Array = "CREATE TABLE ProjectArray(project_id VARCHAR(500), project_name VARCHAR(500))";
	public static final String DROP_TABLE_PROJECT_Array = "DROP TABLE ProjectArray";
	public static final String DELETE_TABLE_PROJECT_Array = "DELETE FROM ProjectArray";
	public static final String GET_PROJECT_Array = "SELECT * FROM ProjectArray";

	// Project Work Array
	public static final String CREATE_TABLE_PROJECT_WORK_Array = "CREATE TABLE ProjectWorkArray(project_id VARCHAR(500), stage_id VARCHAR(500), activity_id VARCHAR(500), work_id VARCHAR(500), work_name VARCHAR(500))";
	public static final String DROP_TABLE_PROJECT_WORK_Array = "DROP TABLE ProjectWorkArray";
	public static final String DELETE_TABLE_PROJECT_WORK_Array = "DELETE FROM ProjectWorkArray";
	public static final String GET_PROJECT_WORK_Array = "SELECT * FROM ProjectWorkArray";

//	String work_project_id = workArray.getJSONObject(i).getString("project_id");
//	String work_stage_id = workArray.getJSONObject(i).getString("stage_id");
//	String work_activity_id = workArray.getJSONObject(i).getString("activity_id");
//	String work_id = workArray.getJSONObject(i).getString("work_id");
//	String work_name = workArray.getJSONObject(i).getString("work_name");


	public static final String CREATE_TABLE_USER_PROJECT = "CREATE TABLE UserProject(user_id VARCHAR(100), project_id VARCHAR(500), project_name VARCHAR(500))";
	public static final String DROP_TABLE_USER_PROJECT = "DROP TABLE UserProject";
	public static final String DELETE_TABLE_USER_PROJECT = "DELETE FROM UserProject";
	public static final String GET_USER_PROJECT = "SELECT * FROM UserProject";

	// Project Layer
	public static final String CREATE_TABLE_PROJECT_LAYERS = "CREATE TABLE ProjectLayers(id INTEGER PRIMARY KEY AUTOINCREMENT, layer_id VARCHAR(100), project_id VARCHAR(100),work_id VARCHAR(100),form_id TEXT, layer_name VARCHAR(100), layer_type VARCHAR(100), layer_icon VARCHAR(100), layer_icon_height VARCHAR(100), layer_icon_width, layer_line_color VARCHAR(50), layer_line_type VARCHAR(100), latlong TEXT, form_data TEXT, filledForms TEXT, formbg_color VARCHAR(100), form_logo TEXT, form_sno VARCHAR(100), only_view VARCHAR(100),is_enable INTEGER, is_active INTEGER, is_custom_made INTEGER)";
	public static final String DROP_TABLE_PROJECT_LAYERS   = "DROP TABLE ProjectLayers";
	public static final String DELETE_TABLE_PROJECT_LAYERS = "DELETE FROM ProjectLayers";
	public static final String GET_PROJECT_LAYERS          = "SELECT * FROM ProjectLayers";

	// GIS Survey Layers
	public static final String CREATE_TABLE_GIS_SURVEY_LAYERS = "CREATE TABLE GISSurveyLayers(id INTEGER PRIMARY KEY AUTOINCREMENT, layer_id VARCHAR(100), survey_id VARCHAR(100),work_id VARCHAR(100),form_id TEXT, layer_name VARCHAR(100), layer_type VARCHAR(100), layer_icon VARCHAR(100), layer_icon_height VARCHAR(100), layer_icon_width, layer_line_color VARCHAR(50), layer_line_type VARCHAR(100), latlong TEXT, form_data TEXT, filledForms TEXT, formbg_color VARCHAR(100), form_logo TEXT, form_sno VARCHAR(100), only_view VARCHAR(100),is_enable INTEGER, is_active INTEGER, is_custom_made INTEGER)";
	public static final String DROP_TABLE_GIS_SURVEY_LAYERS   = "DROP TABLE GISSurveyLayers";
	public static final String DELETE_TABLE_GIS_SURVEY_LAYERS = "DELETE FROM GISSurveyLayers";
	public static final String GET_GIS_SURVEY_LAYERS          = "SELECT * FROM GISSurveyLayers";

	// GIS Survey Layers Online
	public static final String CREATE_TABLE_GIS_SURVEY_ONLINE_LAYERS = "CREATE TABLE GISSurveyOnlineLayers(id INTEGER PRIMARY KEY AUTOINCREMENT, layer_id VARCHAR(100), survey_id VARCHAR(100),work_id VARCHAR(100),form_id TEXT, layer_name VARCHAR(100), layer_type VARCHAR(100), layer_icon VARCHAR(100), layer_icon_height VARCHAR(100), layer_icon_width, layer_line_color VARCHAR(50), layer_line_type VARCHAR(100), latlong TEXT, form_data TEXT, filledForms TEXT, formbg_color VARCHAR(100), form_logo TEXT, form_sno VARCHAR(100), only_view VARCHAR(100),is_enable INTEGER, is_active INTEGER, is_custom_made INTEGER, is_layer_change TEXT)";
	public static final String DROP_TABLE_GIS_SURVEY_ONLINE_LAYERS   = "DROP TABLE GISSurveyOnlineLayers";
	public static final String DELETE_TABLE_GIS_SURVEY_ONLINE_LAYERS = "DELETE FROM GISSurveyOnlineLayers";
	public static final String GET_GIS_SURVEY_ONLINE_LAYERS          = "SELECT * FROM GISSurveyOnlineLayers";



	public static final String CREATE_TABLE_ONLINE_LAYERS = "CREATE TABLE OnlineLayer(id INTEGER PRIMARY KEY AUTOINCREMENT, latlong TEXT, change TEXT)";
	public static final String DROP_TABLE_ONLINE_LAYERS   = "DROP TABLE OnlineLayer";
	public static final String DELETE_TABLE_ONLINE_LAYERS = "DELETE FROM OnlineLayer";
	public static final String GET_ONLINE_LAYERS          = "SELECT * FROM OnlineLayer";

	// Project Form
	public static final String CREATE_TABLE_PROJECT_FORM = "CREATE TABLE ProjectForm(id INTEGER PRIMARY KEY , layer_id VARCHAR(100),work_id VARCHAR(100),form_id VARCHAR(100),form_data TEXT, unique_number VARCHAR(100), file  TEXT, camera TEXT, audio TEXT, video TEXT, file_col_number VARCHAR(100) , camera_col_number VARCHAR(100), video_col_number VARCHAR(100), audio_col_number VARCHAR(100), icon TEXT, form TEXT)";
	//public static final String CREATE_TABLE_PROJECT_FORM = "CREATE TABLE ProjectForm(id INTEGER PRIMARY KEY , work_id VARCHAR(100),form_id VARCHAR(100), form_data TEXT, unique_number VARCHAR(100))";
	public static final String DROP_TABLE_PROJECT_FORM   = "DROP TABLE ProjectForm";
	public static final String DELETE_TABLE_PROJECT_FORM = "DELETE FROM ProjectForm";
	public static final String GET_PROJECT_FORM          = "SELECT * FROM ProjectForm";

	// GIS Survey Form
	public static final String CREATE_TABLE_GIS_SURVEY_FORM = "CREATE TABLE GISSurveyForm(id INTEGER PRIMARY KEY , layer_id VARCHAR(100),work_id VARCHAR(100),form_id VARCHAR(100),form_data TEXT, unique_number VARCHAR(100), file  TEXT, camera TEXT, audio TEXT, video TEXT, file_col_number VARCHAR(100) , camera_col_number VARCHAR(100), video_col_number VARCHAR(100), audio_col_number VARCHAR(100), icon TEXT, form TEXT)";
	public static final String DROP_TABLE_GIS_SURVEY_FORM   = "DROP TABLE GISSurveyForm";
	public static final String DELETE_TABLE_GIS_SURVEY_FORM = "DELETE FROM GISSurveyForm";
	public static final String GET_GIS_SURVEY_FORM          = "SELECT * FROM GISSurveyForm";

	// Project Survey Form
	public static final String CREATE_TABLE_PROJECT_SURVEY_FORM = "CREATE TABLE ProjectSurveyForm(id INTEGER PRIMARY KEY , layer_id VARCHAR(100),work_id VARCHAR(100),form_id VARCHAR(100),form_data TEXT, unique_number VARCHAR(100), file  TEXT, camera TEXT, audio TEXT, video TEXT, file_col_number VARCHAR(100) , camera_col_number VARCHAR(100), video_col_number VARCHAR(100), audio_col_number VARCHAR(100), icon TEXT)";
	//public static final String CREATE_TABLE_PROJECT_FORM = "CREATE TABLE ProjectForm(id INTEGER PRIMARY KEY , work_id VARCHAR(100),form_id VARCHAR(100), form_data TEXT, unique_number VARCHAR(100))";
	public static final String DROP_TABLE_PROJECT_SURVEY_FORM   = "DROP TABLE ProjectSurveyForm";
	public static final String DELETE_TABLE_PROJECT_SURVEY_FORM = "DELETE FROM ProjectSurveyForm";
	public static final String GET_PROJECT_SURVEY_FORM          = "SELECT * FROM ProjectSurveyForm";

	// GIS Survey Survey Form
	public static final String CREATE_TABLE_GIS_SURVEY_SURVEY_FORM = "CREATE TABLE GISSurveySurveyForm(id INTEGER PRIMARY KEY , layer_id VARCHAR(100),work_id VARCHAR(100),form_id VARCHAR(100),form_data TEXT, unique_number VARCHAR(100), file  TEXT, camera TEXT, audio TEXT, video TEXT, file_col_number VARCHAR(100) , camera_col_number VARCHAR(100), video_col_number VARCHAR(100), audio_col_number VARCHAR(100), icon TEXT)";
	public static final String DROP_TABLE_GIS_SURVEY_SURVEY_FORM   = "DROP TABLE GISSurveySurveyForm";
	public static final String DELETE_TABLE_GIS_SURVEY_SURVEY_FORM = "DELETE FROM GISSurveySurveyForm";
	public static final String GET_PROJECT_GIS_SURVEY_FORM          = "SELECT * FROM GISSurveySurveyForm";


	// Project GeoFence
	public static final String CREATE_TABLE_PROJECT_GEOFENCE = "CREATE TABLE ProjectGeoFence(id INTEGER PRIMARY KEY AUTOINCREMENT, project_id VARCHAR(100), stage_id VARCHAR(100),activity_id VARCHAR(100), work_id VARCHAR(100), work_name VARCHAR(100), geofence TEXT)";
	public static final String DROP_TABLE_PROJECT_GEOFENCE   = "DROP TABLE ProjectGeoFence";
	public static final String DELETE_TABLE_PROJECT_GEOFENCE = "DELETE FROM ProjectGeoFence";
	public static final String GET_PROJECT_GEOFENCE          = "SELECT * FROM ProjectGeoFence";

	// GIS Survey GeoFence
	public static final String CREATE_TABLE_GIS_SURVEY_GEOFENCE = "CREATE TABLE SurveyGeoFence(id INTEGER PRIMARY KEY AUTOINCREMENT, survey_id VARCHAR(100), work_id VARCHAR(100), work_name VARCHAR(100), geofence TEXT)";
	public static final String DROP_TABLE_GIS_SURVEY_GEOFENCE   = "DROP TABLE SurveyGeoFence";
	public static final String DELETE_TABLE_GIS_SURVEY_GEOFENCE = "DELETE FROM SurveyGeoFence";
	public static final String GET_GIS_SURVEY_GEOFENCE          = "SELECT * FROM SurveyGeoFence";





	//#####################################################################################
	//----------------------------------------------------------------------------------------

	// Project
	public static final String CREATE_TABLE_PROJECT = "CREATE TABLE Project(id VARCHAR(50), project VARCHAR(100), latitude VARCHAR(100), longitude VARCHAR(100), layers VARCHAR(100))";
	public static final String DROP_TABLE_PROJECT = "DROP TABLE Project";
	public static final String DELETE_TABLE_PROJECT = "DELETE FROM Project";
	public static final String GET_PROJECT = "SELECT * FROM Project ORDER BY project DESC";
	// Form
	public static final String CREATE_TABLE_FORM = "CREATE TABLE Form(form_id VARCHAR(50), description VARCHAR(100), project_id VARCHAR(50))";
	public static final String DROP_TABLE_FORM = "DROP TABLE Form";
	public static final String DELETE_TABLE_FORM = "DELETE FROM Form";
	public static final String GET_FORM = "SELECT * FROM Form ORDER BY description DESC";
	// Form Details
	public static final String CREATE_TABLE_FORM_DETAILS = "CREATE TABLE FormDetails(id INTEGER PRIMARY KEY, form_id VARCHAR(50), data TEXT, IS_SYNC INTEGER, type VARCHAR(50), latitude VARCHAR(50), longitude VARCHAR(50), unique_number VARCHAR(100))";
	public static final String DROP_TABLE_FORM_DETAILS = "DROP TABLE FormDetails";
	public static final String DELETE_TABLE_FORM_DETAILS = "DELETE FROM FormDetails";
	// Geom Details
	public static final String CREATE_TABLE_GEOM_DETAILS = "CREATE TABLE GeomDetails(gid VARCHAR, user_id VARCHAR(50), file_path VARCHAR(50), geom_type VARCHAR(50), geom_array TEXT, latitude VARCHAR(50), longitude VARCHAR(50), accuracy VARCHAR(10), record_date VARCHAR(50), viewData TEXT, syncData TEXT, IS_SYNC INTEGER)";
	public static final String DROP_TABLE_GEOM_DETAILS = "DROP TABLE GeomDetails";
	public static final String DELETE_TABLE_GEOM_DETAILS = "DELETE FROM GeomDetails";
	// Image Details
	public static final String CREATE_TABLE_IMAGE_DETAILS = "CREATE TABLE ImageDetails(gid VARCHAR(50), imgpath TEXT)";
	public static final String DROP_TABLE_IMAGE_DETAILS = "DROP TABLE ImageDetails";
	public static final String DELETE_TABLE_IMAGE_DETAILS = "DELETE FROM ImageDetails";
	// Layers
	public static final String CREATE_TABLE_LAYERS = "CREATE TABLE Layers(id INTEGER PRIMARY KEY, project_id VARCHAR(50), layer_name VARCHAR(100), type VARCHAR(50), is_enable INTEGER, is_active INTEGER, is_custom_made INTEGER, color VARCHAR(50), order_index INTEGER, array_latlong TEXT, form_id VARCHAR(50), form_name VARCHAR(50))";
	public static final String DROP_TABLE_LAYERS = "DROP TABLE Layers";
	public static final String DELETE_TABLE_LAYERS = "DELETE FROM Layers";


//---------------------------------------------------------- Constructor ------------------------------------------------------------

	public DataBaseHelper(Context context, String name, CursorFactory factory,int version) {
		super(context, DATABASE_NAME, factory, version);
	}

	public DataBaseHelper(Context c) {
		super(c, DATABASE_NAME, null, DATABASE_VERSION);
		ctx = c;
	}

//---------------------------------------------------------- On Create ------------------------------------------------------------

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_PROJECT);
		db.execSQL(CREATE_TABLE_FORM);
		db.execSQL(CREATE_TABLE_FORM_DETAILS);
		db.execSQL(CREATE_TABLE_LAYERS);
		db.execSQL(CREATE_TABLE_GEOM_DETAILS);
		db.execSQL(CREATE_TABLE_IMAGE_DETAILS);
		// Change by Rahul Suthar
		db.execSQL(CREATE_TABLE_TRACKING);
		db.execSQL(CREATE_TABLE_TRACKING_STATUS);
		db.execSQL(CREATE_TABLE_CAMERA);
		db.execSQL(CREATE_TABLE_WALKING_TRACKING);
		db.execSQL(CREATE_TABLE_GPS_TRACKING);
		// User project
		db.execSQL(CREATE_TABLE_USER_PROJECT);
		db.execSQL(CREATE_TABLE_PROJECT_USER);
		db.execSQL(CREATE_TABLE_PROJECT_LAYERS);
		db.execSQL(CREATE_TABLE_PROJECT_FORM);
		db.execSQL(CREATE_TABLE_PROJECT_SURVEY_FORM);
		db.execSQL(CREATE_TABLE_PROJECT_GEOFENCE);
		// Time Line
		db.execSQL(CREATE_TABLE_TIMELINE_IMAGE);
		db.execSQL(CREATE_TABLE_TIMELINE_IMAGE_LOCAL);
		db.execSQL(CREATE_TABLE_TIMELINE_S_IMAGE_LOCAL);
		// Map Camera
		db.execSQL(CREATE_TABLE_MAP_CAMERA_IMAGE);
		db.execSQL(CREATE_TABLE_MAP_CAMERA_IMAGE_LOCAL);
		db.execSQL(CREATE_TABLE_MAP_CAMERA_IMAGE_S_LOCAL);

		db.execSQL(CREATE_TABLE_GIS_MAP_CAMERA_IMAGE);
		db.execSQL(CREATE_TABLE_GIS_MAP_CAMERA_IMAGE_LOCAL);
		db.execSQL(CREATE_TABLE_GIS_MAP_CAMERA_IMAGE_S_LOCAL);

		// GIS Survey
		db.execSQL(CREATE_TABLE_GIS_SURVEY);
		db.execSQL(CREATE_TABLE_GIS_SURVEY_GEOFENCE);
		db.execSQL(CREATE_TABLE_GIS_SURVEY_LAYERS);
		db.execSQL(CREATE_TABLE_GIS_SURVEY_ONLINE_LAYERS);
		db.execSQL(CREATE_TABLE_GIS_SURVEY_FORM);
		db.execSQL(CREATE_TABLE_GIS_SURVEY_SURVEY_FORM);


		db.execSQL(CREATE_TABLE_ONLINE_LAYERS);

		// Project Array
		db.execSQL(CREATE_TABLE_PROJECT_Array);
		db.execSQL(CREATE_TABLE_PROJECT_WORK_Array);
		// GIS Survey Array
		db.execSQL(CREATE_TABLE_GIS_SURVEY_Array);
		db.execSQL(CREATE_TABLE_GIS_SURVEY_WORK_Array);

	}
//---------------------------------------------------------- On Upgrade ------------------------------------------------------------

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// DROP
		db.execSQL(DROP_TABLE_PROJECT);
		db.execSQL(DROP_TABLE_FORM);
		db.execSQL(DROP_TABLE_FORM_DETAILS);
		db.execSQL(DROP_TABLE_LAYERS);
		db.execSQL(DROP_TABLE_GEOM_DETAILS);
		db.execSQL(DROP_TABLE_IMAGE_DETAILS);
		// Change By Rahul Suthar
		db.execSQL(DROP_TABLE_TRACKING);
		db.execSQL(DROP_TABLE_TRACKING_STATUS);
		db.execSQL(DROP_TABLE_CAMERA);
		db.execSQL(DROP_TABLE_WALKING_TRACKING);
		db.execSQL(DROP_TABLE_GPS_TRACKING);
		// user project
		db.execSQL(DROP_TABLE_USER_PROJECT);
		db.execSQL(DROP_TABLE_PROJECT_USER);
		db.execSQL(DROP_TABLE_PROJECT_LAYERS);
		db.execSQL(DROP_TABLE_PROJECT_FORM);
		db.execSQL(DROP_TABLE_PROJECT_SURVEY_FORM);
		db.execSQL(DROP_TABLE_PROJECT_GEOFENCE);
		// Time  Line
		db.execSQL(DROP_TABLE_TIMELINE_IMAGE);
		db.execSQL(DROP_TABLE_TIMELINE_IMAGE_LOCAL);
		db.execSQL(DROP_TABLE_TIMELINE_S_IMAGE_LOCAL);
		// Map Camera
		db.execSQL(DROP_TABLE_MAP_CAMERA_IMAGE);
		db.execSQL(DROP_TABLE_MAP_CAMERA_IMAGE_LOCAL);
		db.execSQL(DROP_TABLE_MAP_CAMERA_IMAGE_S_LOCAL);
		db.execSQL(DROP_TABLE_GIS_MAP_CAMERA_IMAGE);
		db.execSQL(DROP_TABLE_GIS_MAP_CAMERA_IMAGE_LOCAL);
		db.execSQL(DROP_TABLE_GIS_MAP_CAMERA_IMAGE_S_LOCAL);

		// GIS Survey
		db.execSQL(DROP_TABLE_GIS_SURVEY);
		db.execSQL(DROP_TABLE_GIS_SURVEY_GEOFENCE);
		db.execSQL(DROP_TABLE_GIS_SURVEY_LAYERS);
		db.execSQL(DROP_TABLE_GIS_SURVEY_ONLINE_LAYERS);
		db.execSQL(DROP_TABLE_GIS_SURVEY_FORM);
		db.execSQL(DROP_TABLE_GIS_SURVEY_SURVEY_FORM);


		db.execSQL(DROP_TABLE_ONLINE_LAYERS);

		// Project Array
		db.execSQL(DROP_TABLE_PROJECT_Array);
		db.execSQL(DROP_TABLE_PROJECT_WORK_Array);
		// GIS Survey Array
		db.execSQL(DROP_TABLE_GIS_SURVEY_Array);
		db.execSQL(DROP_TABLE_GIS_SURVEY_WORK_Array);


		// Insert
		db.execSQL(CREATE_TABLE_PROJECT);
		db.execSQL(CREATE_TABLE_FORM);
		db.execSQL(CREATE_TABLE_FORM_DETAILS);
		db.execSQL(CREATE_TABLE_LAYERS);
		db.execSQL(CREATE_TABLE_GEOM_DETAILS);
		db.execSQL(CREATE_TABLE_IMAGE_DETAILS);
		// Change By Rahul Suthar
		db.execSQL(CREATE_TABLE_TRACKING);
		db.execSQL(CREATE_TABLE_TRACKING_STATUS);
		db.execSQL(CREATE_TABLE_CAMERA);
		db.execSQL(CREATE_TABLE_GPS_TRACKING);
		db.execSQL(CREATE_TABLE_WALKING_TRACKING);
		// user project
		db.execSQL(CREATE_TABLE_USER_PROJECT);
		db.execSQL(CREATE_TABLE_PROJECT_USER);
		db.execSQL(CREATE_TABLE_PROJECT_LAYERS);
		db.execSQL(CREATE_TABLE_PROJECT_FORM);
		db.execSQL(CREATE_TABLE_PROJECT_SURVEY_FORM);
		db.execSQL(CREATE_TABLE_PROJECT_GEOFENCE);
		// Time Line
		db.execSQL(CREATE_TABLE_TIMELINE_IMAGE);
		db.execSQL(CREATE_TABLE_TIMELINE_IMAGE_LOCAL);
		db.execSQL(CREATE_TABLE_TIMELINE_S_IMAGE_LOCAL);
		// Map Camera
		db.execSQL(CREATE_TABLE_MAP_CAMERA_IMAGE);
		db.execSQL(CREATE_TABLE_MAP_CAMERA_IMAGE_LOCAL);
		db.execSQL(CREATE_TABLE_MAP_CAMERA_IMAGE_S_LOCAL);

		db.execSQL(CREATE_TABLE_GIS_MAP_CAMERA_IMAGE);
		db.execSQL(CREATE_TABLE_GIS_MAP_CAMERA_IMAGE_LOCAL);
		db.execSQL(CREATE_TABLE_GIS_MAP_CAMERA_IMAGE_S_LOCAL);

		// GIS Survey
		db.execSQL(CREATE_TABLE_GIS_SURVEY);
		db.execSQL(CREATE_TABLE_GIS_SURVEY_GEOFENCE);
		db.execSQL(CREATE_TABLE_GIS_SURVEY_LAYERS);
		db.execSQL(CREATE_TABLE_GIS_SURVEY_ONLINE_LAYERS);
		db.execSQL(CREATE_TABLE_GIS_SURVEY_FORM);
		db.execSQL(CREATE_TABLE_GIS_SURVEY_SURVEY_FORM);

		db.execSQL(CREATE_TABLE_ONLINE_LAYERS);

		// Project Array
		db.execSQL(CREATE_TABLE_PROJECT_Array);
		db.execSQL(CREATE_TABLE_PROJECT_WORK_Array);
		// Gis Survey Array
		db.execSQL(CREATE_TABLE_GIS_SURVEY_Array);
		db.execSQL(CREATE_TABLE_GIS_SURVEY_WORK_Array);

	}

//---------------------------------------------------------- Open Database ------------------------------------------------------------

	public void open() {
		db = this.getWritableDatabase();
	}
//---------------------------------------------------------- Close Database ------------------------------------------------------------

	@Override
	public void close() {
		db.close();
	}
//---------------------------------------------------------- Execute Query ------------------------------------------------------------

	public void executeQuery(String query) {
		db.execSQL(query);
	}

//---------------------------------------------------------- Execute Cursor ------------------------------------------------------------

	public Cursor executeCursor(String selectQuery) {
		Cursor cursor = db.rawQuery(selectQuery, null);
		return cursor;
	}

//######################################################### Insert Query ####################################################################################################
	//##########################################


	//---------------------------------------------------------- Insert Map Camera S Image Local------------------------------------------------------------
	public void insertGISMapImageSLocal(String user_id,String survey_id, String work_id,String description,String latitude, String longitude, String date_time, String image) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("user_id", user_id);
		cv.put("survey_id",survey_id);
		cv.put("work_id",work_id);
		cv.put("latitude", latitude);
		cv.put("longitude", longitude);
		cv.put("datetime",date_time);
		cv.put("image",image);
		cv.put("description",description);
		db.insert("GISMapCameraImageSLocal", null, cv);
		close();
	}

	//---------------------------------------------------------- Insert TimeLine Image Local------------------------------------------------------------

	public void insertGISMapImageLocal(String user_id,String survey_id, String work_id,String description,String latitude, String longitude, String date_time, String version, String image,String imageName) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("user_id", user_id);
		cv.put("survey_id",survey_id);
		cv.put("work_id",work_id);
		cv.put("latitude", latitude);
		cv.put("longitude", longitude);
		cv.put("datetime",date_time);
		cv.put("version",version);
		cv.put("image",image);
		cv.put("description",description);
		cv.put("name",imageName);
		db.insert("GISMapCameraImageLocal", null, cv);
		Log.e(TAG,"insert Data GIS Camera");
		close();
	}

	//---------------------------------------------------------- Insert TimeLine Image------------------------------------------------------------
	public void insertGISMapImage(String user_id,String survey_id, String work_id,String description,String latitude, String longitude, String date_time, String version, String image,String imageName) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("user_id", user_id);
		cv.put("survey_id",survey_id);
		cv.put("work_id",work_id);
		cv.put("latitude", latitude);
		cv.put("longitude", longitude);
		cv.put("datetime",date_time);
		cv.put("version",version);
		cv.put("image",image);
		cv.put("description",description);
		cv.put("name",imageName);
		db.insert("MapCameraImage", null, cv);
		close();
	}



	//---------------------------------------------------------- Insert Map Camera S Image Local------------------------------------------------------------
	public void insertMapImageSLocal(String user_id,String project_id, String work_id,String description,String latitude, String longitude, String date_time, String image) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("user_id", user_id);
		cv.put("project_id",project_id);
		cv.put("work_id",work_id);
		cv.put("latitude", latitude);
		cv.put("longitude", longitude);
		cv.put("datetime",date_time);
		cv.put("image",image);
		cv.put("description",description);
		db.insert("MapCameraImageSLocal", null, cv);
		close();
	}

	//---------------------------------------------------------- Insert TimeLine Image Local------------------------------------------------------------

	public void insertMapImageLocal(String user_id,String project_id, String work_id,String description,String latitude, String longitude, String date_time, String version, String image,String imageName) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("user_id", user_id);
		cv.put("latitude", latitude);
		cv.put("longitude", longitude);
		cv.put("project_id",project_id);
		cv.put("work_id",work_id);
		cv.put("datetime",date_time);
		cv.put("version",version);
		cv.put("image",image);
		cv.put("description",description);
		cv.put("name",imageName);
		db.insert("MapCameraImageLocal", null, cv);
		close();
	}

	//---------------------------------------------------------- Insert TimeLine Image------------------------------------------------------------
	public void insertMapImage(String user_id,String project_id, String work_id,String description,String latitude, String longitude, String date_time, String version, String image,String imageName) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("user_id", user_id);
		cv.put("project_id",project_id);
		cv.put("work_id",work_id);
		cv.put("latitude", latitude);
		cv.put("longitude", longitude);
		cv.put("datetime",date_time);
		cv.put("version",version);
		cv.put("image",image);
		cv.put("description",description);
		cv.put("name",imageName);
		db.insert("MapCameraImage", null, cv);
		close();
	}

	//---------------------------------------------------------- Insert TimeLine S Image Local------------------------------------------------------------
	public void insertTimeLineSImageLocal(String user_id,String description,String latitude, String longitude, String date_time, String image) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("user_id", user_id);
		cv.put("latitude", latitude);
		cv.put("longitude", longitude);
		cv.put("datetime",date_time);
		cv.put("image",image);
		cv.put("description",description);
		db.insert("TimeLineSImageLocal", null, cv);
		close();
	}

	//---------------------------------------------------------- Insert TimeLine Image Local------------------------------------------------------------

	public void insertTimeLineImageLocal(String user_id,String description,String latitude, String longitude, String date_time, String version, String image,String imageName) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("user_id", user_id);
		cv.put("latitude", latitude);
		cv.put("longitude", longitude);
		cv.put("datetime",date_time);
		cv.put("version",version);
		cv.put("image",image);
		cv.put("description",description);
		cv.put("name",imageName);
		db.insert("TimeLineImageLocal", null, cv);
		close();
	}

	//---------------------------------------------------------- Insert TimeLine Image------------------------------------------------------------
	public void insertTimeLineImage(String user_id,String description,String latitude, String longitude, String date_time, String version, String image,String imageName) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("user_id", user_id);
		cv.put("latitude", latitude);
		cv.put("longitude", longitude);
		cv.put("datetime",date_time);
		cv.put("version",version);
		cv.put("image",image);
		cv.put("description",description);
		cv.put("name",imageName);
		db.insert("TimeLineImage", null, cv);
		close();
	}

	//---------------------------------------------------------- Insert GIS Survey GeoFence ------------------------------------------------------------


	public void insertGISSurveyGeoFence(String survey_id, String work_id, String work_name, String geofence){
		open();
		ContentValues cv = new ContentValues();
		cv.put("survey_id"  , survey_id);
		cv.put("work_id"     , work_id);
		cv.put("work_name"   , work_name);
		cv.put("geofence"    , geofence);
		long _id = db.insert("SurveyGeoFence", null, cv);
		close();
	}

//---------------------------------------------------------- Insert Project Form ------------------------------------------------------------

	//ProjectSurveyForm
	public void insertGISSurveySurveyForm(String layer_id,String work_id,String form_id,String unique_number, String file, String camera, String video ,String audio, String colNumberFile , String colNumberCamera,String colNumberAudio,String colNumberVideo, String formData,String icon) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("form_data", formData);
		cv.put("layer_id",layer_id);
		cv.put("work_id"  , work_id);
		cv.put("form_id"  , form_id);
		cv.put("file"   , file);
		cv.put("camera" , camera);
		cv.put("audio"  , audio);
		cv.put("video"  , video);
		cv.put("file_col_number"   , colNumberFile);
		cv.put("camera_col_number" , colNumberCamera);
		cv.put("video_col_number"  , colNumberVideo);
		cv.put("audio_col_number"  , colNumberAudio);
		cv.put("unique_number"     , unique_number);
		cv.put("icon", icon);
		long _id = db.insert("GISSurveySurveyForm", null, cv);
		close();
	}


	public void insertGISSurveyForm(String layer_id,String work_id,String form_id,String unique_number, String file, String camera, String video ,String audio, String colNumberFile , String colNumberCamera,String colNumberAudio,String colNumberVideo, String formData,String icon) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("form_data", formData);
		cv.put("layer_id",layer_id);
		cv.put("work_id"  , work_id);
		cv.put("form_id"  , form_id);
		cv.put("file"   , file);
		cv.put("camera" , camera);
		cv.put("audio"  , audio);
		cv.put("video"  , video);
		cv.put("file_col_number"   , colNumberFile);
		cv.put("camera_col_number" , colNumberCamera);
		cv.put("video_col_number"  , colNumberVideo);
		cv.put("audio_col_number"  , colNumberAudio);
		cv.put("unique_number"     , unique_number);
		cv.put("icon", icon);
		long _id = db.insert("GISSurveyForm", null, cv);
		close();
	}


	//---------------------------------------------------------- Insert Project Layers ------------------------------------------------------------

	public void insertGISSurveyLayers(String layer_id, String survey_id, String work_id,String form_id,String layer_name, String layer_type,String layer_icon, String layer_icon_height, String layer_icon_width,String layer_line_color, String layer_line_type, String latlong, String form_data,String filledForms,String formbg_color, String form_logo,String form_sno,String only_view,boolean isEnable, boolean isActive,boolean is_custom_made) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("layer_id", layer_id);
		cv.put("survey_id", survey_id);
		cv.put("work_id", work_id);
		cv.put("form_id",form_id);
		cv.put("layer_name", layer_name);
		cv.put("layer_type", layer_type);
		cv.put("layer_icon", layer_icon);
		cv.put("layer_icon_height", layer_icon_height);
		cv.put("layer_icon_width",layer_icon_width);
		cv.put("layer_line_color", layer_line_color);
		cv.put("layer_line_type", layer_line_type);
		cv.put("latlong", latlong);
		cv.put("form_data",form_data);
		cv.put("is_enable", isEnable?1:0);
		cv.put("is_active", isActive?1:0);
		cv.put("is_custom_made", is_custom_made?1:0);
		cv.put("filledForms",filledForms);
		cv.put("formbg_color",formbg_color);
		cv.put("form_logo",form_logo);
		cv.put("form_sno",form_sno);
		cv.put("only_view",only_view);
		long _id = db.insert("GISSurveyLayers", null, cv);
		Log.e("INSERT", "survey_id: "+ survey_id +" " + "layerName: " + layer_name);
		close();
	}

	public void insertGISSurveyOnlineLayers(String layer_id, String survey_id, String work_id,String form_id,String layer_name, String layer_type,String layer_icon, String layer_icon_height, String layer_icon_width,String layer_line_color, String layer_line_type, String latlong, String form_data,String filledForms,String formbg_color, String form_logo,String form_sno,String only_view,boolean isEnable, boolean isActive,boolean is_custom_made, String isLayerChange) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("layer_id", layer_id);
		cv.put("survey_id", survey_id);
		cv.put("work_id", work_id);
		cv.put("form_id",form_id);
		cv.put("layer_name", layer_name);
		cv.put("layer_type", layer_type);
		cv.put("layer_icon", layer_icon);
		cv.put("layer_icon_height", layer_icon_height);
		cv.put("layer_icon_width",layer_icon_width);
		cv.put("layer_line_color", layer_line_color);
		cv.put("layer_line_type", layer_line_type);
		cv.put("latlong", latlong);
		cv.put("form_data",form_data);
		cv.put("is_enable", isEnable?1:0);
		cv.put("is_active", isActive?1:0);
		cv.put("is_custom_made", is_custom_made?1:0);
		cv.put("filledForms",filledForms);
		cv.put("formbg_color",formbg_color);
		cv.put("form_logo",form_logo);
		cv.put("form_sno",form_sno);
		cv.put("only_view",only_view);
		cv.put("is_layer_change",isLayerChange);
		long _id = db.insert("GISSurveyOnlineLayers", null, cv);
		Log.e("INSERT", "survey_id: "+ survey_id +" " + "is_layer_change: " + isLayerChange);
		close();
	}

	public void insertOnlineLayer(String latlong, String isLayerChange){
		open();
		ContentValues cv = new ContentValues();
		cv.put("latlong", latlong);
		cv.put("change",isLayerChange);
		long _id = db.insert("OnlineLayer", null, cv);
		close();
	}


//---------------------------------------------------------- Insert Project GeoFence ------------------------------------------------------------

	// "CREATE TABLE ProjectGeoFence(id INTEGER PRIMARY KEY AUTOINCREMENT, project_id VARCHAR(100), stage_id VARCHAR(100),activity_id VARCHAR(100), work_id VARCHAR(100), work_name VARCHAR(100), geofence TEXT)";

	public void insertProjectGeoFence(String project_id, String stage_id, String activity_id, String work_id, String work_name, String geofence){
		open();
		ContentValues cv = new ContentValues();
		cv.put("project_id"  , project_id);
		cv.put("stage_id"    , stage_id);
		cv.put("activity_id" , activity_id);
		cv.put("work_id"     , work_id);
		cv.put("work_name"   , work_name);
		cv.put("geofence"    , geofence);
		long _id = db.insert("ProjectGeoFence", null, cv);
		close();
	}

//---------------------------------------------------------- Insert Project Form ------------------------------------------------------------

//ProjectSurveyForm
	public void insertProjectSurveyForm(String layer_id,String work_id,String form_id,String unique_number, String file, String camera, String video ,String audio, String colNumberFile , String colNumberCamera,String colNumberAudio,String colNumberVideo, String formData,String icon) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("form_data", formData);
		cv.put("layer_id",layer_id);
		cv.put("work_id"  , work_id);
		cv.put("form_id"  , form_id);
		cv.put("file"   , file);
		cv.put("camera" , camera);
		cv.put("audio"  , audio);
		cv.put("video"  , video);
		cv.put("file_col_number"   , colNumberFile);
		cv.put("camera_col_number" , colNumberCamera);
		cv.put("video_col_number"  , colNumberVideo);
		cv.put("audio_col_number"  , colNumberAudio);
		cv.put("unique_number"     , unique_number);
		cv.put("icon", icon);
		long _id = db.insert("ProjectSurveyForm", null, cv);
		close();
	}


	public void insertProjectForm(String layer_id,String work_id,String form_id,String unique_number, String file, String camera, String video ,String audio, String colNumberFile , String colNumberCamera,String colNumberAudio,String colNumberVideo, String formData,String icon) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("form_data", formData);
		cv.put("layer_id",layer_id);
		cv.put("work_id"  , work_id);
		cv.put("form_id"  , form_id);
		cv.put("file"   , file);
		cv.put("camera" , camera);
		cv.put("audio"  , audio);
		cv.put("video"  , video);
		cv.put("file_col_number"   , colNumberFile);
		cv.put("camera_col_number" , colNumberCamera);
		cv.put("video_col_number"  , colNumberVideo);
		cv.put("audio_col_number"  , colNumberAudio);
		cv.put("unique_number"     , unique_number);
		cv.put("icon", icon);
		long _id = db.insert("ProjectForm", null, cv);
		close();
	}


	//---------------------------------------------------------- Insert Project Layers ------------------------------------------------------------

	public void insertProjectLayers(String layer_id, String project_id, String work_id,String form_id,String layer_name, String layer_type,String layer_icon, String layer_icon_height, String layer_icon_width,String layer_line_color, String layer_line_type, String latlong, String form_data,String filledForms,String formbg_color, String form_logo,String form_sno,String only_view,boolean isEnable, boolean isActive,boolean is_custom_made) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("layer_id", layer_id);
		cv.put("project_id", project_id);
		cv.put("work_id", work_id);
		cv.put("form_id",form_id);
		cv.put("layer_name", layer_name);
		cv.put("layer_type", layer_type);
		cv.put("layer_icon", layer_icon);
		cv.put("layer_icon_height", layer_icon_height);
		cv.put("layer_icon_width",layer_icon_width);
		cv.put("layer_line_color", layer_line_color);
		cv.put("layer_line_type", layer_line_type);
		cv.put("latlong", latlong);
		cv.put("form_data",form_data);
		cv.put("is_enable", isEnable?1:0);
		cv.put("is_active", isActive?1:0);
		cv.put("is_custom_made", is_custom_made?1:0);
		cv.put("filledForms",filledForms);
		cv.put("formbg_color",formbg_color);
		cv.put("form_logo",form_logo);
		cv.put("form_sno",form_sno);
		cv.put("only_view",only_view);
		long _id = db.insert("ProjectLayers", null, cv);
		close();
	}

	//---------------------------------------------------------- Insert Project User ------------------------------------------------------------
	public void insertProjectUser(String project){
		open();
		ContentValues cv = new ContentValues();
		cv.put("data", project);
		db.insert("ProjectUser", null, cv);
		close();
	}

	public void insertProjectArray(String projectId, String projectName){
		open();
		ContentValues cv = new ContentValues();
		cv.put("project_id",projectId);
		cv.put("project_name",projectName);
		db.insert("ProjectArray", null, cv);
		close();
	}


	public void insertProjectWorkArray(String project_id, String stage_id, String activity_id, String work_id, String work_name){
		open();
		ContentValues cv = new ContentValues();
		cv.put("project_id"  , project_id);
		cv.put("stage_id"    , stage_id);
		cv.put("activity_id" , activity_id);
		cv.put("work_id"     , work_id);
		cv.put("work_name"   , work_name);
		long _id = db.insert("ProjectWorkArray", null, cv);
		close();
	}

	//---------------------------------------------------------- Insert Project User ------------------------------------------------------------
	public void insertGISSurvey(String data){
		open();
		ContentValues cv = new ContentValues();
		cv.put("data", data);
		db.insert("GISSurvey", null, cv);
		close();
	}

	public void insertGISSurveyArray(String survey_id, String survey_name){
		open();
		ContentValues cv = new ContentValues();
		cv.put("survey_id", survey_id);
		cv.put("survey_name", survey_name);
		db.insert("GISSurveyArray", null, cv);
		close();
	}

	public void insertGISSurveyWorkArray(String survey_id, String stage_id, String activity_id, String work_id, String work_name){
		open();
		ContentValues cv = new ContentValues();
		cv.put("survey_id"  , survey_id);
		cv.put("stage_id"    , stage_id);
		cv.put("activity_id" , activity_id);
		cv.put("work_id"     , work_id);
		cv.put("work_name"   , work_name);
		long _id = db.insert("GISSurveyWorkArray", null, cv);
		close();
	}


	//---------------------------------------------------------- Insert Walking Tracking ------------------------------------------------------------
	public void insertWalkingTracking(String latitude, String longitude) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("latitude", latitude);
		cv.put("longitude", longitude);
		db.insert("WalkTracking", null, cv);
		close();
	}

	//---------------------------------------------------------- Insert GPS Tracking ------------------------------------------------------------
	public void insertGPSTracking(String user_id, String type,String latlong, String datetime,String version, String token) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("user_id",user_id);
		cv.put("mode",type);
		cv.put("latlong", latlong);
		cv.put("datetime",datetime);
		cv.put("version",version);
		cv.put("token",token);
		cv.put("unique_number",String.valueOf(Utility.getToken()));
		db.insert("GpsTracking", null, cv);
		close();
	}

	//---------------------------------------------------------- Insert Camera Image ------------------------------------------------------------
	public void insertCameraImage(String user_id,String description,String latitude, String longitude, String date_time, String version, String image,String imageName) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("user_id", user_id);
		cv.put("latitude", latitude);
		cv.put("longitude", longitude);
		cv.put("datetime",date_time);
		cv.put("version",version);
		cv.put("image",image);
		cv.put("description",description);
		cv.put("name",imageName);
		db.insert("Camera", null, cv);
		close();
	}

	//---------------------------------------------------------- Insert Tracking Status ------------------------------------------------------------
	public void insertTrackingStatus(String id, String latitude, String longitude, String created_date, String version) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("user_id", id);
		cv.put("latitude", latitude);
		cv.put("longitude", longitude);
		cv.put("created_date",created_date);
		cv.put("version",version);
		cv.put("unique_number",String.valueOf(Utility.getToken()));
		db.insert("TrackingStatus", null, cv);
		close();
//		long result = db.insert("Tracking", null, cv);
//		if(result == -1){
//			return false;
//		}
//		else{
//			return true;
//		}
	}

	//---------------------------------------------------------- Insert Tracking/ walking Data ------------------------------------------------------------
	public void insertTracking(String id, String latitude, String longitude, String isInsideCoverageArea) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("id", id);
		cv.put("latitude", latitude);
		cv.put("longitude", longitude);
		cv.put("isInsideCoverageArea",isInsideCoverageArea);
		db.insert("Tracking", null, cv);
		close();
//		long result = db.insert("Tracking", null, cv);
//		if(result == -1){
//			return false;
//		}
//		else{
//			return true;
//		}
	}

	//#######################################

	//---------------------------------------------------------- Insert Project ------------------------------------------------------------

	public void insertProject(String id, String project, String latitude, String longitude, String layers) {
		ContentValues cv = new ContentValues();
		cv.put("id", id);
		cv.put("project", project);
		cv.put("latitude", latitude);
		cv.put("longitude", longitude);
		cv.put("layers", layers);
		db.insert("Project", null, cv);
	}

	//---------------------------------------------------------- Insert Form ------------------------------------------------------------

	public void insertForm(String form_id, String description, String project_id) {
		ContentValues cv = new ContentValues();
		cv.put("form_id", form_id);
		cv.put("description", description);
		cv.put("project_id", project_id);
		db.insert("Form", null, cv);
	}

	//---------------------------------------------------------- Insert Form Details ------------------------------------------------------------

	public void insertFormDetails(String form_id, String data, String latitude, String longitude, int isSync, String type) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("form_id", form_id);
		cv.put("latitude", latitude);
		cv.put("longitude", longitude);
		cv.put("data", data);
		cv.put("IS_SYNC", isSync);
		//cv.put("unique_number",Utility.getToken());
		cv.put("type", type);
		long id = db.insert("FormDetails", null, cv);
	//	Log.e("FORM DETAILS", "INSERT ID - "+id);
		close();
	}

	//---------------------------------------------------------- Insert Geom Details ------------------------------------------------------------
	public void insertGeomDetails(String gid, String user_id, String file_path, String geom_type, String geom_array, String latitude, String longitude, String accuracy, String record_date, String viewData, String syncData, int IS_SYNC) {
		ContentValues cv = new ContentValues();
		cv.put("gid", gid);
		cv.put("user_id", user_id);
		cv.put("file_path", file_path);
		cv.put("geom_type", geom_type);
		cv.put("geom_array", geom_array);
		cv.put("latitude", latitude);
		cv.put("longitude", longitude);
		cv.put("accuracy", accuracy);
		cv.put("record_date", record_date);
		cv.put("viewData", viewData);
		cv.put("syncData", syncData);
		cv.put("IS_SYNC", IS_SYNC);
		long _id = db.insert("GeomDetails", null, cv);
		//Log.e("INSERT", "rowId: "+_id);
	}

	//---------------------------------------------------------- Insert Layers ------------------------------------------------------------
	public void insertLayers(String project_id, String layer_name, String type, boolean is_enable, boolean is_active, String color, int index, boolean is_custom_made, String array_latlong, String form_id, String form_name) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("project_id", project_id);
		cv.put("form_id", form_id);
		cv.put("form_name", form_name);
		cv.put("layer_name", layer_name);
		cv.put("type", type);
		cv.put("is_enable", is_enable?1:0);
		cv.put("is_active", is_active?1:0);
		cv.put("is_custom_made", is_custom_made?1:0);
		cv.put("color", color);
		cv.put("order_index", index);
		cv.put("array_latlong", array_latlong);
		long _id = db.insert("Layers", null, cv);
		//Log.e("INSERT", "rowId: "+_id);
		close();
	}

	//---------------------------------------------------------- Insert Image Details ------------------------------------------------------------
	public void insertImageDetails(String gid, String imgpath) {
		ContentValues cv = new ContentValues();
		cv.put("gid", gid);
		cv.put("imgpath", imgpath);
		db.insert("ImageDetails", null, cv);
	}


//######################################################### Update Query ####################################################################################################

	//---------------------------------------------------------- update GIS Survey Layers Data ------------------------------------------------------------
	public void updateGISSurveyLayersData(ArrayList<ProjectLayerModel> listLayers) {
		open();
		for(ProjectLayerModel bin : listLayers)
		{
			ContentValues cv = new ContentValues();
			cv.put("is_enable", bin.isEnable()?1:0);
			cv.put("is_active", bin.isActive()?1:0);
			cv.put("is_custom_made", bin.isCustomMade()?1:0);
			String whereClause = "id = ?";
			String[] whereArgs = { String.valueOf(bin.getID())};
			long _id = db.update("GISSurveyLayers", cv, whereClause, whereArgs);
		}
		close();
	}

	public void updateGISSurveyLayersDataViewOnly(ProjectLayerModel bin) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("is_enable", bin.isEnable()?1:0);
		cv.put("is_active", bin.isActive()?1:0);
		cv.put("is_custom_made", bin.isCustomMade()?1:0);
		cv.put("latlong",bin.getLatLong());
		String whereClause = "id = ?";
		String[] whereArgs = { String.valueOf(bin.getID())};
		long _id = db.update("GISSurveyLayers", cv, whereClause, whereArgs);
		close();
	}
//	public void updateGISSurveyLayersDataViewOnly(int id,boolean isCustomMade) {
//		open();
//		ContentValues cv = new ContentValues();
//		cv.put("is_custom_made", isCustomMade ? 1 : 0);
//		String whereClause = "id = ?";
//		String[] whereArgs = { String.valueOf(id)};
//		long _id = db.update("GISSurveyLayers", cv, whereClause, whereArgs);
//		close();
//	}

//
//	public void updateGISSurveyOnlineLayersDataViewOnly(ProjectLayerModel bin) {
//		open();
//		ContentValues cv = new ContentValues();
//		cv.put("is_enable", bin.isEnable()?1:0);
//		cv.put("is_active", bin.isActive()?1:0);
//		cv.put("is_custom_made", bin.isCustomMade()?1:0);
//		cv.put("latlong",bin.getLatLong());
//		cv.put("is_layer_change", bin.getIsLayerChange());
//		String whereClause = "id = ?";
//		String[] whereArgs = { String.valueOf(bin.getID())};
//		Log.e(TAG,"Update online Layer -> " + bin.getIsLayerChange());
//		long _id = db.update("GISSurveyOnlineLayers", cv, whereClause, whereArgs);
//		close();
//	}
//
//	//OnlineLayer
//	public void updateOnlineLayer(OnlineLayerModel bin){
//		open();
//		ContentValues cv = new ContentValues();
//		cv.put("latlong",bin.getLatLong());
//		cv.put("change", bin.getChange());
//		String whereClause = "id = ?";
//		String[] whereArgs = { String.valueOf(bin.getId())};
//		Log.e(TAG,"Update online Layer -> " + bin.getChange());
//		long _id = db.update("OnlineLayer", cv, whereClause, whereArgs);
//		close();
//	}


	//---------------------------------------------------------- update One GIS Survey Layer Data ------------------------------------------------------------
	public void updateOneGISSurveyLayerData(ProjectLayerModel bin) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("is_enable", bin.isEnable()?1:0);
		cv.put("is_active", bin.isActive()?1:0);
		cv.put("is_custom_made", bin.isCustomMade()?1:0);
		String whereClause = "id = ?";
		String[] whereArgs = { String.valueOf(bin.getID())};
		long _id = db.update("GISSurveyLayers", cv, whereClause, whereArgs);
		Log.e(TAG, "Update online layer custom made -> " + bin.isCustomMade() );
		close();
	}

	//---------------------------------------------------------- update Project Layers Data ------------------------------------------------------------
	public void updateProjectLayersData(ArrayList<ProjectLayerModel> listLayers) {
		open();
		for(ProjectLayerModel bin : listLayers)
		{
			ContentValues cv = new ContentValues();
			cv.put("is_enable", bin.isEnable()?1:0);
			cv.put("is_active", bin.isActive()?1:0);
			cv.put("is_custom_made", bin.isCustomMade()?1:0);
			String whereClause = "id = ?";
			String[] whereArgs = { String.valueOf(bin.getID())};
			long _id = db.update("ProjectLayers", cv, whereClause, whereArgs);
		}
		close();
	}

	//---------------------------------------------------------- update One Project Layer Data ------------------------------------------------------------

	public void updateOneProjectLayerData(ProjectLayerModel bin) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("is_enable", bin.isEnable()?1:0);
		cv.put("is_active", bin.isActive()?1:0);
		cv.put("is_custom_made", bin.isCustomMade()?1:0);
		String whereClause = "id = ?";
		String[] whereArgs = { String.valueOf(bin.getID())};
		long _id = db.update("ProjectLayers", cv, whereClause, whereArgs);
		close();
	}


	//---------------------------------------------------------- update Layers Data ------------------------------------------------------------
	public void updateLayersData(ArrayList<BinLayerProject> listLayers) {
		open();
		for(BinLayerProject bin : listLayers)
		{
			ContentValues cv = new ContentValues();
			cv.put("is_enable", bin.isEnable()?1:0);
			cv.put("is_active", bin.isActive()?1:0);
			cv.put("is_custom_made", bin.isCustomMade()?1:0);
			cv.put("order_index", bin.getIndex());
			String whereClause = "id = ?";
			String[] whereArgs = { String.valueOf(bin.getId())};
			long _id = db.update("Layers", cv, whereClause, whereArgs);
		//	Log.e("UPDATE", "rowId: "+_id);
		}
		close();
	}

	//---------------------------------------------------------- update One Layer Data ------------------------------------------------------------
	public void updateOneLayerData(BinLayerProject bin) {
		open();
		ContentValues cv = new ContentValues();
		cv.put("is_enable", bin.isEnable()?1:0);
		cv.put("is_active", bin.isActive()?1:0);
		cv.put("is_custom_made", bin.isCustomMade()?1:0);
		cv.put("order_index", bin.getIndex());
		String whereClause = "id = ?";
		String[] whereArgs = { String.valueOf(bin.getId())};
		long _id = db.update("Layers", cv, whereClause, whereArgs);
		//Log.e("UPDATE", "rowId: "+_id);
		close();
	}

	//---------------------------------------------------------- update Form Details ------------------------------------------------------------
	/*public void updateFormDetails(String form_id, String data, int isSync) {
        open();
        ContentValues cv = new ContentValues();
        String where = "form_id=?";
        String[] whereArg = {form_id};
        if(!Utility.isEmptyString(data))
            cv.put("data", data);
        cv.put("IS_SYNC", isSync);
        int id = db.update("FormDetails", cv, where, whereArg);
        Log.e("UPDATE", "id"+id);
        close();
    }*/

	//---------------------------------------------------------- update Image Details ------------------------------------------------------------
	public String updateImageDetails(String gid, String imgpath) {
		return "UPDATE ImageDetails SET imgpath = '"+imgpath+"' WHERE gid = " + gid;
	}

	//---------------------------------------------------------- update Geom Details ------------------------------------------------------------
	public String updateGeomDetails(String record_date, String viewData, String syncData, String gid) {
		return "UPDATE GeomDetails SET record_date = '"+record_date+"', viewData = '"+viewData+"', syncData = '"+syncData+"' WHERE gid = " + gid;
	}




//######################################################### Delete Query ####################################################################################################

	//---------------------------------------------------------- Delete Tracking ------------------------------------------------------------
	public void deleteTrackingStatusData(String id)
	{
		open();
		String whereClause = "id = ?";
		String[] whereArgs = { id };
		int noOfRowAffected = db.delete("TrackingStatus", whereClause, whereArgs);
		//Log.e("DELETE", ""+noOfRowAffected);
		close();
	}

	//---------------------------------------------------------- Delete Walking ------------------------------------------------------------
	public void deleteWalking(String id)
	{
		open();
		String whereClause = "id = ?";
		String[] whereArgs = { id };
		int noOfRowAffected = db.delete("WalkTracking", whereClause, whereArgs);
		//Log.e("DELETE", ""+noOfRowAffected);
		close();
	}

	//---------------------------------------------------------- Delete GPS Tracking ------------------------------------------------------------
	public void deleteGPSTracking(String id)
	{
		open();
		String whereClause = "id = ?";
		String[] whereArgs = { id };
		int noOfRowAffected = db.delete("GpsTracking", whereClause, whereArgs);
		//Log.e("DELETE", ""+noOfRowAffected);
		close();
	}

	//---------------------------------------------------------- Delete Map Camera Image S Local ------------------------------------------------------------
	public void deleteMapCameraImageSLocal(String id)
	{
		open();
		String whereClause = "id = ?";
		String[] whereArgs = { id };
		int noOfRowAffected = db.delete("MapCameraImageSLocal", whereClause, whereArgs);
		close();
	}
	//---------------------------------------------------------- Delete Map Camera Image Local ------------------------------------------------------------

	public void deleteMapCameraImageLocal(String id)
	{
		open();
		String whereClause  = "id = ?";
		String[] whereArgs  = { id };
		int noOfRowAffected = db.delete("MapCameraImageLocal", whereClause, whereArgs);
		close();
	}

	//---------------------------------------------------------- Delete Map Camera Image ------------------------------------------------------------
	public void deleteGISMapCameraImage(String id)
	{
		open();
		String whereClause = "id = ?";
		String[] whereArgs = { id };
		int noOfRowAffected = db.delete("GISMapCameraImage", whereClause, whereArgs);
		close();
	}

	//---------------------------------------------------------- Delete Map Camera Image ------------------------------------------------------------
	public void deleteMapCameraImage(String id)
	{
		open();
		String whereClause = "id = ?";
		String[] whereArgs = { id };
		int noOfRowAffected = db.delete("MapCameraImage", whereClause, whereArgs);
		close();
	}

	//---------------------------------------------------------- Delete TimeLine S Image Local ------------------------------------------------------------
	public void deleteTimeLineSImageLocal(String id)
	{
		open();
		String whereClause = "id = ?";
		String[] whereArgs = { id };
		int noOfRowAffected = db.delete("TimeLineSImageLocal", whereClause, whereArgs);
		//Log.e("DELETE", ""+noOfRowAffected);
		close();
	}
	//---------------------------------------------------------- Delete TimeLine Image Local ------------------------------------------------------------
	public void deleteTimeLineImageLocal(String id)
	{
		open();
		String whereClause = "id = ?";
		String[] whereArgs = { id };
		int noOfRowAffected = db.delete("TimeLineImageLocal", whereClause, whereArgs);
		//Log.e("DELETE", ""+noOfRowAffected);
		close();
	}
	//---------------------------------------------------------- Delete TimeLine Image ------------------------------------------------------------
	public void deleteTimeLineImage(String id)
	{
		open();
		String whereClause = "id = ?";
		String[] whereArgs = { id };
		int noOfRowAffected = db.delete("TimeLineImage", whereClause, whereArgs);
		//Log.e("DELETE", ""+noOfRowAffected);
		close();
	}

	public void deleteGISSurveyOnlineLayers(String id){
		// GISSurveyOnlineLayers
		open();
		String whereClause = "id = ?";
		String[] whereArgs = { id };
		int noOfRowAffected = db.delete("GISSurveyOnlineLayers", whereClause, whereArgs);
		close();
	}

	//---------------------------------------------------------- Delete Camera Image ------------------------------------------------------------
	public void deleteCameraImage(String id)
	{
		open();
		String whereClause = "id = ?";
		String[] whereArgs = { id };
		int noOfRowAffected = db.delete("Camera", whereClause, whereArgs);
		//Log.e("DELETE", ""+noOfRowAffected);
		close();
	}
	//TimeLineImage


	//---------------------------------------------------------- Delete Tracking Status Details ------------------------------------------------------------
	public void deleteTrackingStatusDetails(String UID)
	{
		open();
		String whereClause = "user_id = ?";
		String[] whereArgs = { UID };
		int noOfRowAffected = db.delete("TrackingStatus", whereClause, whereArgs);
		//Log.e("DELETE", ""+noOfRowAffected);
		close();
	}

	//TrackingStatus
	//---------------------------------------------------------- Delete Tracking Details ------------------------------------------------------------
	public void deleteTrackingDetails(String UID)
	{
		open();
		String whereClause = "id = ?";
		String[] whereArgs = { UID };
		int noOfRowAffected = db.delete("Tracking", whereClause, whereArgs);
		//Log.e("DELETE", ""+noOfRowAffected);
		close();
	}

	//---------------------------------------------------------- Delete Form Details ------------------------------------------------------------
	public void deleteFormDetails(String UID)
	{
		open();
		String whereClause = "id = ?";
		String[] whereArgs = { UID };
		int noOfRowAffected = db.delete("FormDetails", whereClause, whereArgs);
		//Log.e("DELETE", ""+noOfRowAffected);
		close();
	}
	public void deleteProjectFormDetails(String UID)
	{
		open();
		String whereClause = "id = ?";
		String[] whereArgs = { UID };
		int noOfRowAffected = db.delete("ProjectForm", whereClause, whereArgs);
		//Log.e("DELETE", ""+noOfRowAffected);
		close();
	}

	public void deleteGISSurveyFormDetails(String UID)
	{
		open();
		String whereClause = "id = ?";
		String[] whereArgs = { UID };
		int noOfRowAffected = db.delete("GISSurveyForm", whereClause, whereArgs);
		//Log.e("DELETE", ""+noOfRowAffected);
		close();
	}

	//---------------------------------------------------------- Delete Image Details ------------------------------------------------------------
	public String deleteImageDetails(String gid) {
		return "DELETE FROM ImageDetails WHERE gid = " + gid;
	}



//######################################################### Select Query ####################################################################################################

	public ArrayList<BinLayerProject> getLayersList(String projectId, boolean isCustomMade){
		ArrayList<BinLayerProject> list = new ArrayList<BinLayerProject>();
		open();
		Cursor curForm = executeCursor(getLayersListQry(projectId, isCustomMade));
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0; i<curForm.getCount(); i++) {
				BinLayerProject bin = new BinLayerProject();
				bin.setId(curForm.getInt(curForm.getColumnIndex("id")));
				bin.setProjectId(curForm.getString(curForm.getColumnIndex("project_id")));
				bin.setFormId(curForm.getString(curForm.getColumnIndex("form_id")));
				bin.setFormName(curForm.getString(curForm.getColumnIndex("form_name")));
				bin.setName(curForm.getString(curForm.getColumnIndex("layer_name")));
				bin.setType(curForm.getString(curForm.getColumnIndex("type")));
				bin.setEnable(curForm.getInt(curForm.getColumnIndex("is_enable"))==1);
				bin.setActive(curForm.getInt(curForm.getColumnIndex("is_active"))==1);
				bin.setCustomMade(curForm.getInt(curForm.getColumnIndex("is_custom_made"))==1);
				bin.setDisplay(curForm.getString(curForm.getColumnIndex("color")));
				bin.setIndex(curForm.getInt(curForm.getColumnIndex("order_index")));
				bin.setArrLatLong(curForm.getString(curForm.getColumnIndex("array_latlong")));
				list.add(bin);
				curForm.moveToNext();
			}
		}
		close();
		return list;
	}

	public String getExportData() {
		return "SELECT * FROM FormDetails";
	}

	public String getProjectWiseExportData() {
		return "SELECT * FROM Project";
	}

	private String getLayersListQry(String proj_id, boolean isCustomMade) {
		return "SELECT * FROM Layers WHERE project_id = '"+proj_id+"' AND is_custom_made = '"+(isCustomMade?1:0)+"' ORDER BY order_index DESC";
	}

	public String getProjectDetails(String proj_id) {
		return "SELECT * FROM Project WHERE id = '"+proj_id+"'";
	}

	public String getFormDetailsBlank(String form_id, String type) {
		return "SELECT * FROM FormDetails WHERE form_id = '"+form_id+"' AND type = '" + type + "'";
	}

	public String getFormDetails(String form_id, int isSync, String type) {
		return "SELECT * FROM FormDetails WHERE form_id = '"+form_id+"' AND IS_SYNC = '" + isSync + "' AND type = '" + type + "'";
	}
	public String getForm(String form_id) {
		return "SELECT * FROM ProjectForm WHERE form_id = "+form_id;
	}

	public String getGeomDetails() {
//		return "SELECT * FROM GeomDetails WHERE form_id = '"+form_id+"' AND IS_SYNC = " + IS_SYNC;
		return "SELECT * FROM GeomDetails";
	}

	public String checkGeomDetails(String gid) {
		return "SELECT * FROM GeomDetails WHERE gid = " + gid;
	}

	public static final String getSync(int is_sync) {
		return "SELECT * FROM GeomDetails WHERE IS_SYNC = "+is_sync+" LIMIT 1";
	}

	public static final String updateSync(int is_sync, String gid) {
		return "UPDATE GeomDetails SET IS_SYNC = "+is_sync+" WHERE gid = " + gid;
	}

	public String getImageDetails(String gid) {
		return "SELECT * FROM ImageDetails WHERE gid = " + gid;
	}
	@SuppressLint("Range")
	public String getFormName(String form_id) {
		String returnName = "";
		String qty = "SELECT description FROM Form WHERE form_id = " + form_id;
		Cursor curName = executeCursor(qty);
		if(curName != null && curName.getCount() > 0) {
			curName.moveToFirst();
			String nm = curName.getString(curName.getColumnIndex("description"));
			returnName = nm;
		}
		return returnName;
	}
	@SuppressLint("Range")
    public ArrayList<FormDTO> getAllForms() {
        ArrayList<FormDTO> formDTOS = new ArrayList<FormDTO>();
        open();
        Cursor curForm = executeCursor(DataBaseHelper.GET_FORM);
        if(curForm != null && curForm.getCount() > 0) {
            curForm.moveToFirst();
            for(int i=0;i<curForm.getCount();i++) {
                String form_id = curForm.getString(curForm.getColumnIndex("form_id"));
                String description = curForm.getString(curForm.getColumnIndex("description"));
                String project_id = curForm.getString(curForm.getColumnIndex("project_id"));
                formDTOS.add(new FormDTO(form_id, description, project_id));
                curForm.moveToNext();
            }
        }
        close();
        return formDTOS;
    }
	@SuppressLint("Range")
	public FormDetailModel getFormData(String form_id, int isSync, String type)
	{
		String formDetails = "";
		open();
		FormDetailModel bin = new FormDetailModel();
		Cursor curDetails = executeCursor(getFormDetails(form_id, isSync, type));
		if(curDetails != null && curDetails.getCount() > 0) {
			curDetails.moveToFirst();
			formDetails = curDetails.getString(curDetails.getColumnIndex("data"));
			bin.setData(Utility.convertJsonArrayToCustomList(formDetails));
			bin.setUniqueFormId(curDetails.getInt(curDetails.getColumnIndex("id")));
			bin.setLatitude(curDetails.getString(curDetails.getColumnIndex("latitude")));
			bin.setLongitude(curDetails.getString(curDetails.getColumnIndex("longitude")));
			//Log.e("TAG", "formDetails - "+formDetails);
		}
		close();
		return bin;
	}

	public String getRecordDate()
	{
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		return df.format(c.getTime());
	}

	@SuppressLint("Range")
	// Change by Rahul Suthar
	public ArrayList<TrackingData> getTrackingDetails(){
		ArrayList<TrackingData> tracking_list = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(DataBaseHelper.GET_TRACKING);
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0;i<curForm.getCount();i++) {
				String user_id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParam_tracking_user_id));
				String lat = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParam_tracking_user_latitude));
				String lon = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParam_tracking_user_longitude));
				String inside_cover = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParam_tracking_isInsideCoverageArea));
				tracking_list.add(new TrackingData(user_id,lat,lon,inside_cover));
				curForm.moveToNext();
			}
		}
		close();
		return tracking_list;
	}

	@SuppressLint("Range")
	// Camera
	public ArrayList<CameraModule> getCameraImage(){
		ArrayList<CameraModule> imageList = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(DataBaseHelper.GET_CAMERA);
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0;i<curForm.getCount();i++){
				String id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamID));
				String user_id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamUserID));
				String lat     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLat));
				String lon     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLon));
				String version = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamVersion));
				String desc    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraDesc));
				String dt      = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamDateTime));
				String image   = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraImage));
				String name    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraImageName));
				imageList.add(new CameraModule(id,user_id,version,lat,lon,dt,desc,image,name));
				curForm.moveToNext();
			}
		}
		close();
		return imageList;
	}

	// Time Line Camera
	@SuppressLint("Range")
	public ArrayList<CameraModule> getTimeLineImage(){
		ArrayList<CameraModule> imageList = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(DataBaseHelper.GET_TIMELINE_IMAGE);
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0;i<curForm.getCount();i++){
				String id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamID));
				String user_id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamUserID));
				String lat     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLat));
				String lon     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLon));
				String version = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamVersion));
				String desc    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraDesc));
				String dt      = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamDateTime));
				String image   = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraImage));
				String name    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraImageName));
				imageList.add(new CameraModule(id,user_id,version,lat,lon,dt,desc,image,name));
				curForm.moveToNext();
			}
		}
		close();
		return imageList;
	}
	// Time Line Local Camera
	@SuppressLint("Range")
	public ArrayList<CameraModule> getTimeLineImageLocal(){
		ArrayList<CameraModule> imageList = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(DataBaseHelper.GET_TIMELINE_IMAGE_LOCAL);
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0;i<curForm.getCount();i++){
				String id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamID));
				String user_id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamUserID));
				String lat     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLat));
				String lon     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLon));
				String version = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamVersion));
				String desc    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraDesc));
				String dt      = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamDateTime));
				String image   = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraImage));
				String name    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraImageName));
				imageList.add(new CameraModule(id,user_id,version,lat,lon,dt,desc,image,name));
				curForm.moveToNext();
			}
		}
		close();
		return imageList;
	}
	// Time Line S Camera
	@SuppressLint("Range")
	public ArrayList<CameraModule> getTimeLineSImageLocal(){
		ArrayList<CameraModule> imageList = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(DataBaseHelper.GET_TIMELINE_S_IMAGE_LOCAL);
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0;i<curForm.getCount();i++){
				String id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamID));
				String user_id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamUserID));
				String lat     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLat));
				String lon     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLon));
				String desc    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraDesc));
				String dt      = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamDateTime));
				String image   = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraImage));
				imageList.add(new CameraModule(id,user_id,"",lat,lon,dt,desc,image,""));
				curForm.moveToNext();
			}
		}
		close();
		return imageList;
	}

	// Map Camera Image
	@SuppressLint("Range")
	public ArrayList<CameraModule> getMapCameraImage(){
		ArrayList<CameraModule> imageList = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(DataBaseHelper.GET_MAP_CAMERA_IMAGE);
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0;i<curForm.getCount();i++){
				String id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamID));
				String user_id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamUserID));
				String project_id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamProjectID));
				String work_id    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamWorkID));
				String lat     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLat));
				String lon     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLon));
				String version = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamVersion));
				String desc    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraDesc));
				String dt      = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamDateTime));
				String image   = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraImage));
				String name    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraImageName));
				CameraModule cameraModule = new CameraModule(id,user_id,version,lat,lon,dt,desc,image,name);
				cameraModule.setProjectId(project_id);
				cameraModule.setWorkId(work_id);
				imageList.add(cameraModule);
				//imageList.add(new CameraModule(id,user_id,version,lat,lon,dt,desc,image,name));
				curForm.moveToNext();
			}
		}
		close();
		return imageList;
	}

	public String getMapCameraImageLocalListQry(String word_id) {
		return "SELECT * FROM MapCameraImageLocal WHERE work_id ="+word_id;
	}

	// Map Camera Image Local
	@SuppressLint("Range")
	public ArrayList<CameraModule> getMapCameraImageLocal(String workID){
		ArrayList<CameraModule> imageList = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(getMapCameraImageLocalListQry(workID));
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0;i<curForm.getCount();i++){
				String id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamID));
				String user_id    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamUserID));
				String project_id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamProjectID));
				String work_id    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamWorkID));
				String lat        = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLat));
				String lon        = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLon));
				String version    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamVersion));
				String desc       = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraDesc));
				String dt         = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamDateTime));
				String image      = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraImage));
				String name       = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraImageName));
				CameraModule cameraModule = new CameraModule(id,user_id,version,lat,lon,dt,desc,image,name);
				cameraModule.setProjectId(project_id);
				cameraModule.setWorkId(work_id);
				imageList.add(cameraModule);
				curForm.moveToNext();
			}
		}
		close();
		return imageList;
	}


	// Map Camera Image Local
	@SuppressLint("Range")
	public ArrayList<CameraModule> getMapCameraImageLocal(){
		ArrayList<CameraModule> imageList = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(DataBaseHelper.GET_MAP_CAMERA_IMAGE_LOCAL);
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0;i<curForm.getCount();i++){
				String id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamID));
				String user_id    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamUserID));
				String project_id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamProjectID));
				String work_id    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamWorkID));
				String lat        = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLat));
				String lon        = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLon));
				String version    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamVersion));
				String desc       = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraDesc));
				String dt         = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamDateTime));
				String image      = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraImage));
				String name       = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraImageName));
				CameraModule cameraModule = new CameraModule(id,user_id,version,lat,lon,dt,desc,image,name);
				cameraModule.setProjectId(project_id);
				cameraModule.setWorkId(work_id);
				imageList.add(cameraModule);
				curForm.moveToNext();
			}
		}
		close();
		return imageList;
	}

	public String getMapCameraImageSLocalListQry(String word_id) {
		return "SELECT * FROM MapCameraImageSLocal WHERE work_id ="+word_id;
	}

	// Map Camera Image S
	@SuppressLint("Range")
	public ArrayList<CameraModule> getMapCameraImageSLocal(String workID){
		ArrayList<CameraModule> imageList = new ArrayList<>();
		open();
		//MapCameraImageSLocal
		Cursor curForm = executeCursor(getMapCameraImageSLocalListQry(workID));
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0;i<curForm.getCount();i++){
				String id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamID));
				String user_id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamUserID));
				String project_id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamProjectID));
				String work_id    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamWorkID));
				String lat     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLat));
				String lon     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLon));
				String desc    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraDesc));
				String dt      = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamDateTime));
				String image   = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraImage));
				CameraModule cameraModule = new CameraModule(id,user_id,"",lat,lon,dt,desc,image,"");
				cameraModule.setProjectId(project_id);
				cameraModule.setWorkId(work_id);
				imageList.add(cameraModule);
				//imageList.add(new CameraModule(id,user_id,"",lat,lon,dt,desc,image,""));
				curForm.moveToNext();
			}
		}
		close();
		return imageList;
	}

	// Map Camera Image S
	@SuppressLint("Range")
	public ArrayList<CameraModule> getMapCameraImageSLocal(){
		ArrayList<CameraModule> imageList = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(DataBaseHelper.GET_MAP_CAMERA_IMAGE_S_LOCAL);
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0;i<curForm.getCount();i++){
				String id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamID));
				String user_id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamUserID));
				String project_id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamProjectID));
				String work_id    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamWorkID));
				String lat     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLat));
				String lon     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLon));
				String desc    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraDesc));
				String dt      = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamDateTime));
				String image   = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraImage));
				CameraModule cameraModule = new CameraModule(id,user_id,"",lat,lon,dt,desc,image,"");
				cameraModule.setProjectId(project_id);
				cameraModule.setWorkId(work_id);
				imageList.add(cameraModule);
				//imageList.add(new CameraModule(id,user_id,"",lat,lon,dt,desc,image,""));
				curForm.moveToNext();
			}
		}
		close();
		return imageList;
	}


	// GIS Map Camera Image
	@SuppressLint("Range")
	public ArrayList<CameraModule> getGISMapCameraImage(){
		ArrayList<CameraModule> imageList = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(DataBaseHelper.GET_GIS_MAP_CAMERA_IMAGE);
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0;i<curForm.getCount();i++){
				String id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamID));
				String user_id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamUserID));
				String survey_id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamSurveyID));
				String work_id    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamWorkID));
				String lat     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLat));
				String lon     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLon));
				String version = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamVersion));
				String desc    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraDesc));
				String dt      = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamDateTime));
				String image   = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraImage));
				String name    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraImageName));
				CameraModule cameraModule = new CameraModule(id,user_id,version,lat,lon,dt,desc,image,name);
				cameraModule.setSurveyId(survey_id);
				cameraModule.setWorkId(work_id);
				imageList.add(cameraModule);
//				imageList.add(new CameraModule(id,user_id,version,lat,lon,dt,desc,image,name));
				curForm.moveToNext();
			}
		}
		close();
		return imageList;
	}

	public String getGISMapCameraImageLocalListQry(String word_id) {
		return "SELECT * FROM GISMapCameraImageLocal WHERE work_id ="+word_id;
	}

	// GIS Map Camera Image Local
	@SuppressLint("Range")
	public ArrayList<CameraModule> getGISMapCameraImageLocal(String workID){
		ArrayList<CameraModule> imageList = new ArrayList<>();
		open();
		// GISMapCameraImageLocal
		Cursor curForm = executeCursor(getGISMapCameraImageLocalListQry(workID));
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0;i<curForm.getCount();i++){
				String id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamID));
				String user_id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamUserID));
				String survey_id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamSurveyID));
				String work_id    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamWorkID));
				String lat     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLat));
				String lon     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLon));
				String version = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamVersion));
				String desc    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraDesc));
				String dt      = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamDateTime));
				String image   = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraImage));
				String name    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraImageName));
				CameraModule cameraModule = new CameraModule(id,user_id,version,lat,lon,dt,desc,image,name);
				cameraModule.setSurveyId(survey_id);
				cameraModule.setWorkId(work_id);
				imageList.add(cameraModule);
				//imageList.add(new CameraModule(id,user_id,version,lat,lon,dt,desc,image,name));
				curForm.moveToNext();
			}
		}
		close();
		return imageList;
	}

	// GIS Map Camera Image Local
	@SuppressLint("Range")
	public ArrayList<CameraModule> getGISMapCameraImageLocal(){
		ArrayList<CameraModule> imageList = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(DataBaseHelper.GET_GIS_MAP_CAMERA_IMAGE_LOCAL);
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0;i<curForm.getCount();i++){
				String id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamID));
				String user_id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamUserID));
				String survey_id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamSurveyID));
				String work_id    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamWorkID));
				String lat     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLat));
				String lon     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLon));
				String version = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamVersion));
				String desc    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraDesc));
				String dt      = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamDateTime));
				String image   = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraImage));
				String name    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraImageName));
				CameraModule cameraModule = new CameraModule(id,user_id,version,lat,lon,dt,desc,image,name);
				cameraModule.setSurveyId(survey_id);
				cameraModule.setWorkId(work_id);
				imageList.add(cameraModule);
				//imageList.add(new CameraModule(id,user_id,version,lat,lon,dt,desc,image,name));
				curForm.moveToNext();
			}
		}
		close();
		return imageList;
	}

	public String getGISMapCameraImageSLocalListQry(String word_id) {
		return "SELECT * FROM GISMapCameraImageSLocal WHERE work_id ="+word_id;
	}
	// GIS Map Camera Image S
	@SuppressLint("Range")
	public ArrayList<CameraModule> getGISMapCameraImageSLocal(String workID){
		ArrayList<CameraModule> imageList = new ArrayList<>();
		open();
		//GISMapCameraImageSLocal
		Cursor curForm = executeCursor(getGISMapCameraImageSLocalListQry(workID));
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0;i<curForm.getCount();i++){
				String id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamID));
				String user_id    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamUserID));
				String survey_id  = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamSurveyID));
				String work_id    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamWorkID));
				String lat     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLat));
				String lon     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLon));
				String desc    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraDesc));
				String dt      = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamDateTime));
				String image   = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraImage));
				CameraModule cameraModule = new CameraModule(id,user_id,"",lat,lon,dt,desc,image,"");
				cameraModule.setSurveyId(survey_id);
				cameraModule.setWorkId(work_id);
				imageList.add(cameraModule);
				//	imageList.add(new CameraModule(id,user_id,"",lat,lon,dt,desc,image,""));
				curForm.moveToNext();
			}
		}
		close();
		return imageList;
	}

	// GIS Map Camera Image S
	@SuppressLint("Range")
	public ArrayList<CameraModule> getGISMapCameraImageSLocal(){
		ArrayList<CameraModule> imageList = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(DataBaseHelper.GET_GIS_MAP_CAMERA_IMAGE_S_LOCAL);
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0;i<curForm.getCount();i++){
				String id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamID));
				String user_id    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamUserID));
				String survey_id  = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamSurveyID));
				String work_id    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamWorkID));
				String lat     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLat));
				String lon     = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLon));
				String desc    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraDesc));
				String dt      = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamDateTime));
				String image   = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamCameraImage));
				CameraModule cameraModule = new CameraModule(id,user_id,"",lat,lon,dt,desc,image,"");
				cameraModule.setSurveyId(survey_id);
				cameraModule.setWorkId(work_id);
				imageList.add(cameraModule);
			//	imageList.add(new CameraModule(id,user_id,"",lat,lon,dt,desc,image,""));
				curForm.moveToNext();
			}
		}
		close();
		return imageList;
	}



	// Walking Tracking
	@SuppressLint("Range")
	public ArrayList<WalkingModule> getWalkingTracking(){
		ArrayList<WalkingModule> List = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(DataBaseHelper.GET_WALKING_TRACKING);
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0;i<curForm.getCount();i++){
				String id   = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamID));
				String lat  = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLat));
				String lon  = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLon));
				List.add(new WalkingModule(id,lat,lon));
				curForm.moveToNext();
			}
		}
		close();
		return List;
	}
	// GPS Tracking
	@SuppressLint("Range")
	public ArrayList<GpsTrackingModule> getGpsTracking(){
		ArrayList<GpsTrackingModule> List = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(DataBaseHelper.GET_GPS_TRACKING);
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0;i<curForm.getCount();i++){
				String id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamID));
				String user_id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamUserID));
				String latlng  = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamLatLong));
				String version = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamVersion));
				String dt      = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamDateTime));
				String type    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamType));
				String token    = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamToken));
				String unique_number = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamUniqueNumber));
				List.add(new GpsTrackingModule(id,user_id,type,latlng,dt,version,token,unique_number));
				curForm.moveToNext();
			}
		}
		close();
		return List;
	}
	//TrackingStatus
	@SuppressLint("Range")
	public ArrayList<TrackingStatusData> getTrackingStatusDetails(){

		ArrayList<TrackingStatusData> tracking_list = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(DataBaseHelper.GET_TRACKING_STATUS);
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0;i<curForm.getCount();i++) {
				String id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParamID));
				String user_id = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParam_tracking_status_user_id));
				String lat = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParam_tracking_user_status_latitude));
				String lon = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParam_tracking_user_status_longitude));
				String date_time = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParam_tracking_user_status_created_date));
				String version = curForm.getString(curForm.getColumnIndex(DataBaseHelper.keyParam_tracking_user_status_version));
				tracking_list.add(new TrackingStatusData(id,user_id,lat,lon,date_time,version));
				curForm.moveToNext();
			}
		}
		close();
		return tracking_list;
	}

	// user Project
	@SuppressLint("Range")
	public String getProjectUser(){
		open();
		String data = "";
		Cursor curForm = executeCursor(DataBaseHelper.GET_PROJECT_USER);
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			data  = curForm.getString(curForm.getColumnIndex("data"));
		}
		close();
		return data;
	}

	// Project Array
	//ProjectArray
	@SuppressLint("Range")
	public ArrayList<ProjectModule> getProjectArray(){
		ArrayList<ProjectModule> list = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(DataBaseHelper.GET_PROJECT_Array);
		Log.e("ProjectArray", ""+curForm.getCount());
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0; i<curForm.getCount(); i++) {
				String project_id = curForm.getString(curForm.getColumnIndex("project_id"));
				String project_name = curForm.getString(curForm.getColumnIndex("project_name"));
				ProjectModule bin = new ProjectModule(project_id,project_name);
				list.add(bin);
				curForm.moveToNext();
			}
		}
		close();
		return list;
	}

	private String getProjectWorkArrayListQry(String project_id) {
		return "SELECT * FROM ProjectWorkArray WHERE project_id ="+project_id;
	}

	//Project Work Array
	@SuppressLint("Range")
	public ArrayList<ProjectModule> getProjectWorkArray(String project_id){
		ArrayList<ProjectModule> list = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(getProjectWorkArrayListQry(project_id));
		Log.e("ProjectWorkArray", ""+curForm.getCount());
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0; i<curForm.getCount(); i++) {
				String work_id   = curForm.getString(curForm.getColumnIndex("work_id"));
				String work_name = curForm.getString(curForm.getColumnIndex("work_name"));
				ProjectModule bin = new ProjectModule(work_id,work_name);
				list.add(bin);
				curForm.moveToNext();
			}
		}
		close();
		return list;
	}

	private String getGISSurveyWorkArrayListQry(String survey_id) {
		return "SELECT * FROM GISSurveyWorkArray WHERE survey_id ="+survey_id;
	}

	//GIS Survey Array
	@SuppressLint("Range")
	public ArrayList<ProjectModule> getGISSurveyWorkArray(String survey_id){
		ArrayList<ProjectModule> list = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(getGISSurveyWorkArrayListQry(survey_id));
		Log.e("GISSurveyWorkArray", ""+curForm.getCount());
		if(curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0; i<curForm.getCount(); i++) {
				String work_id = curForm.getString(curForm.getColumnIndex("work_id"));
				String work_name = curForm.getString(curForm.getColumnIndex("work_name"));
				ProjectModule bin = new ProjectModule(work_id,work_name);
				list.add(bin);
				curForm.moveToNext();
			}
		}
		close();
		return list;
	}


	//GIS Survey Array
	@SuppressLint("Range")
	public ArrayList<ProjectModule> getGISSurveyArray(){
		ArrayList<ProjectModule> list = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(DataBaseHelper.GET_GIS_SURVEY_Array);
		Log.e("ProjectArray", ""+curForm.getCount());
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0; i<curForm.getCount(); i++) {
				String project_id = curForm.getString(curForm.getColumnIndex("survey_id"));
				String project_name = curForm.getString(curForm.getColumnIndex("survey_name"));
				ProjectModule bin = new ProjectModule(project_id,project_name);
				list.add(bin);
				curForm.moveToNext();
			}
		}
		close();
		return list;
	}
	// GIS Survey
	@SuppressLint("Range")
	public String getGISSurvey(){
		open();
		String data = "";
		Cursor curForm = executeCursor(DataBaseHelper.GET_GIS_SURVEY);
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			data  = curForm.getString(curForm.getColumnIndex("data"));
		}
		close();
		return data;
	}


	// Project Layers
	@SuppressLint("Range")
	public ArrayList<ProjectLayerModel> getProjectLayersList(String work_id, boolean isCustomMade) {
		ArrayList<ProjectLayerModel> list = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(getProjectLayersListQry(work_id));
		Log.e("ProjectLayer", ""+curForm.getCount());
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0; i<curForm.getCount(); i++) {
				ProjectLayerModel bin = new ProjectLayerModel();
				bin.setID(curForm.getString(curForm.getColumnIndex("id")));
				bin.setLayerID(curForm.getString(curForm.getColumnIndex("layer_id")));
				bin.setProjectID(curForm.getString(curForm.getColumnIndex("project_id")));
				bin.setWorkID(curForm.getString(curForm.getColumnIndex("work_id")));
				bin.setFormID(curForm.getString(curForm.getColumnIndex("form_id")));
				bin.setLayerName(curForm.getString(curForm.getColumnIndex("layer_name")));
				bin.setLayerType(curForm.getString(curForm.getColumnIndex("layer_type")));
				bin.setLayerIcon(curForm.getString(curForm.getColumnIndex("layer_icon")));
				bin.setLayerIconHeight(curForm.getString(curForm.getColumnIndex("layer_icon_height")));
				bin.setLayerIconWidth(curForm.getString(curForm.getColumnIndex("layer_icon_width")));
				bin.setLayerLineColor(curForm.getString(curForm.getColumnIndex("layer_line_color")));
				bin.setLayerLineType(curForm.getString(curForm.getColumnIndex("layer_line_type")));
				bin.setEnable(curForm.getInt(curForm.getColumnIndex("is_enable"))==1);
				bin.setActive(curForm.getInt(curForm.getColumnIndex("is_active"))==1);
				bin.setCustomMade(curForm.getInt(curForm.getColumnIndex("is_custom_made"))==1);
				bin.setLatLong(curForm.getString(curForm.getColumnIndex("latlong")));
				bin.setFormData(curForm.getString(curForm.getColumnIndex("form_data")));
				bin.setFilledForms(curForm.getString(curForm.getColumnIndex("filledForms")));
				bin.setFormbg_color(curForm.getString(curForm.getColumnIndex("formbg_color")));
				bin.setForm_logo(curForm.getString(curForm.getColumnIndex("form_logo")));
				bin.setForm_sno(curForm.getString(curForm.getColumnIndex("form_sno")));
				bin.setOnly_view(curForm.getString(curForm.getColumnIndex("only_view")));
				list.add(bin);
			//	Log.e("ProjectLayer", "Layer Name: "+bin.getLayerName() +"  WorkID: "+ bin.getWorkID()+ " Enable: "+ bin.isEnable()+ " OnlyView: "+ bin.getOnly_view());
				curForm.moveToNext();
			}
		}
		close();
		return list;
	}


	// GIS Layers
	@SuppressLint("Range")
	public ArrayList<ProjectLayerModel> getGISSurveyLayersList(String work_id, boolean isCustomMade) {
		ArrayList<ProjectLayerModel> list = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(getGISSurveyLayersListQry(work_id));
		Log.e("GISSurveyLayers", ""+curForm.getCount());
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0; i<curForm.getCount(); i++) {
				ProjectLayerModel bin = new ProjectLayerModel();
				bin.setID(curForm.getString(curForm.getColumnIndex("id")));
				bin.setLayerID(curForm.getString(curForm.getColumnIndex("layer_id")));
				bin.setSurveyID(curForm.getString(curForm.getColumnIndex("survey_id")));
				bin.setWorkID(curForm.getString(curForm.getColumnIndex("work_id")));
				bin.setFormID(curForm.getString(curForm.getColumnIndex("form_id")));
				bin.setLayerName(curForm.getString(curForm.getColumnIndex("layer_name")));
				bin.setLayerType(curForm.getString(curForm.getColumnIndex("layer_type")));
				bin.setLayerIcon(curForm.getString(curForm.getColumnIndex("layer_icon")));
				bin.setLayerIconHeight(curForm.getString(curForm.getColumnIndex("layer_icon_height")));
				bin.setLayerIconWidth(curForm.getString(curForm.getColumnIndex("layer_icon_width")));
				bin.setLayerLineColor(curForm.getString(curForm.getColumnIndex("layer_line_color")));
				bin.setLayerLineType(curForm.getString(curForm.getColumnIndex("layer_line_type")));
				bin.setEnable(curForm.getInt(curForm.getColumnIndex("is_enable"))==1);
				bin.setActive(curForm.getInt(curForm.getColumnIndex("is_active"))==1);
				bin.setCustomMade(curForm.getInt(curForm.getColumnIndex("is_custom_made"))==1);
				bin.setLatLong(curForm.getString(curForm.getColumnIndex("latlong")));
				bin.setFormData(curForm.getString(curForm.getColumnIndex("form_data")));
				bin.setFilledForms(curForm.getString(curForm.getColumnIndex("filledForms")));
				bin.setFormbg_color(curForm.getString(curForm.getColumnIndex("formbg_color")));
				bin.setForm_logo(curForm.getString(curForm.getColumnIndex("form_logo")));
				bin.setForm_sno(curForm.getString(curForm.getColumnIndex("form_sno")));
				bin.setOnly_view(curForm.getString(curForm.getColumnIndex("only_view")));
				list.add(bin);
				Log.e("GISSurveyLayer ->", "Layer Name: "+bin.getLayerName() +"  WorkID: "+ bin.getWorkID()+ " Enable: "+ bin.isEnable()+ " OnlyView: "+ bin.getOnly_view() + " customMade: " + bin.isCustomMade());
				curForm.moveToNext();
			}
		}
		close();
		return list;
	}

	@SuppressLint("Range")
	public ArrayList<ProjectLayerModel> getGISSurveyLayersList() {
		ArrayList<ProjectLayerModel> list = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(GET_GIS_SURVEY_LAYERS);
		Log.e("GISSurveyLayers", ""+curForm.getCount());
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0; i<curForm.getCount(); i++) {
				ProjectLayerModel bin = new ProjectLayerModel();
				bin.setID(curForm.getString(curForm.getColumnIndex("id")));
				bin.setLayerID(curForm.getString(curForm.getColumnIndex("layer_id")));
				bin.setSurveyID(curForm.getString(curForm.getColumnIndex("survey_id")));
				bin.setWorkID(curForm.getString(curForm.getColumnIndex("work_id")));
				bin.setFormID(curForm.getString(curForm.getColumnIndex("form_id")));
				bin.setLayerName(curForm.getString(curForm.getColumnIndex("layer_name")));
				bin.setLayerType(curForm.getString(curForm.getColumnIndex("layer_type")));
				bin.setLayerIcon(curForm.getString(curForm.getColumnIndex("layer_icon")));
				bin.setLayerIconHeight(curForm.getString(curForm.getColumnIndex("layer_icon_height")));
				bin.setLayerIconWidth(curForm.getString(curForm.getColumnIndex("layer_icon_width")));
				bin.setLayerLineColor(curForm.getString(curForm.getColumnIndex("layer_line_color")));
				bin.setLayerLineType(curForm.getString(curForm.getColumnIndex("layer_line_type")));
				bin.setEnable(curForm.getInt(curForm.getColumnIndex("is_enable"))==1);
				bin.setActive(curForm.getInt(curForm.getColumnIndex("is_active"))==1);
				bin.setCustomMade(curForm.getInt(curForm.getColumnIndex("is_custom_made"))==1);
				bin.setLatLong(curForm.getString(curForm.getColumnIndex("latlong")));
				bin.setFormData(curForm.getString(curForm.getColumnIndex("form_data")));
				bin.setFilledForms(curForm.getString(curForm.getColumnIndex("filledForms")));
				bin.setFormbg_color(curForm.getString(curForm.getColumnIndex("formbg_color")));
				bin.setForm_logo(curForm.getString(curForm.getColumnIndex("form_logo")));
				bin.setForm_sno(curForm.getString(curForm.getColumnIndex("form_sno")));
				bin.setOnly_view(curForm.getString(curForm.getColumnIndex("only_view")));
				list.add(bin);
				Log.e("GISSurveyLayer", "Layer Name: "+bin.getLayerName() +"  WorkID: "+ bin.getWorkID()+ " Enable: "+ bin.isEnable()+ " OnlyView: "+ bin.getOnly_view() + " Custom: " + bin.isCustomMade());
				curForm.moveToNext();
			}
		}
		close();
		return list;
	}


//
//	public ArrayList<OnlineLayerModel> getAllOnlineLayer(){
//		ArrayList<OnlineLayerModel> list = new ArrayList<>();
//		open();
//		Cursor curForm = executeCursor(GET_ONLINE_LAYERS);
//		Log.e("OnlineLayers", ""+curForm.getCount());
//		if(curForm != null && curForm.getCount() > 0) {
//			curForm.moveToFirst();
//			for(int i=0; i<curForm.getCount(); i++) {
//				OnlineLayerModel bin = new OnlineLayerModel();
//				bin.setChange(curForm.getString(curForm.getColumnIndex("id")));
//				bin.setLatLong(curForm.getString(curForm.getColumnIndex("latlong")));
//				bin.setChange(curForm.getString(curForm.getColumnIndex("change")));
//				list.add(bin);
//				curForm.moveToNext();
//			}
//		}
//		close();
//		return list;
//	}

//	// GIS Online Layers
//	@SuppressLint("Range")
//	public ArrayList<ProjectLayerModel> getGISSurveyOnlineLayersList() {
//		ArrayList<ProjectLayerModel> list = new ArrayList<>();
//		open();
//		Cursor curForm = executeCursor(GET_GIS_SURVEY_ONLINE_LAYERS);
//		Log.e("OnlineLayers", ""+curForm.getCount());
//		if(curForm != null && curForm.getCount() > 0) {
//			curForm.moveToFirst();
//			for(int i=0; i<curForm.getCount(); i++) {
//				ProjectLayerModel bin = new ProjectLayerModel();
//						bin.setID(curForm.getString(curForm.getColumnIndex("id")));
//						bin.setLayerID(curForm.getString(curForm.getColumnIndex("layer_id")));
//						bin.setSurveyID(curForm.getString(curForm.getColumnIndex("survey_id")));
//						bin.setWorkID(curForm.getString(curForm.getColumnIndex("work_id")));
//						bin.setFormID(curForm.getString(curForm.getColumnIndex("form_id")));
//						bin.setLayerName(curForm.getString(curForm.getColumnIndex("layer_name")));
//						bin.setLayerType(curForm.getString(curForm.getColumnIndex("layer_type")));
//						bin.setLayerIcon(curForm.getString(curForm.getColumnIndex("layer_icon")));
//						bin.setLayerIconHeight(curForm.getString(curForm.getColumnIndex("layer_icon_height")));
//						bin.setLayerIconWidth(curForm.getString(curForm.getColumnIndex("layer_icon_width")));
//						bin.setLayerLineColor(curForm.getString(curForm.getColumnIndex("layer_line_color")));
//						bin.setLayerLineType(curForm.getString(curForm.getColumnIndex("layer_line_type")));
//						bin.setEnable(curForm.getInt(curForm.getColumnIndex("is_enable"))==1);
//						bin.setActive(curForm.getInt(curForm.getColumnIndex("is_active"))==1);
//						bin.setCustomMade(curForm.getInt(curForm.getColumnIndex("is_custom_made"))==1);
//						bin.setLatLong(curForm.getString(curForm.getColumnIndex("latlong")));
//						bin.setFormData(curForm.getString(curForm.getColumnIndex("form_data")));
//						bin.setFilledForms(curForm.getString(curForm.getColumnIndex("filledForms")));
//						bin.setFormbg_color(curForm.getString(curForm.getColumnIndex("formbg_color")));
//						bin.setForm_logo(curForm.getString(curForm.getColumnIndex("form_logo")));
//						bin.setForm_sno(curForm.getString(curForm.getColumnIndex("form_sno")));
//						bin.setOnly_view(curForm.getString(curForm.getColumnIndex("only_view")));
//						bin.setIsLayerChange(curForm.getString(curForm.getColumnIndex("is_layer_change")));
//						list.add(bin);
//				curForm.moveToNext();
//			}
//		}
//		close();
//		return list;
//	}

//	@SuppressLint("Range")
//	public String getAllGISSurveyOnlineLayersList() {
//		//ArrayList<ProjectLayerModel> list = new ArrayList<>();
//		JSONArray jsonArray = new JSONArray();
//		ArrayList<String> list = new ArrayList<>();
//		ArrayList<String> latlonList = new ArrayList<>();
//
//		open();
//		StringBuilder data = new StringBuilder();
//		//	SELECT * FROM GISSurveyLayers
//		Cursor curForm = executeCursor(DataBaseHelper.GET_GIS_SURVEY_ONLINE_LAYERS);
//		Log.e(TAG, "Online Layer:- "+curForm.getCount());
//		if(curForm != null && curForm.getCount() > 0) {
//			curForm.moveToFirst();
//			for(int i=0; i<curForm.getCount(); i++) {
//			//	String isLayerChange = curForm.getString(curForm.getColumnIndex("is_layer_change"));
//			//	Log.e(TAG, "IsLayerChange-> "+ isLayerChange);
////				list.add(isLayerChange);
//				//				if(!Utility.isEmptyString(isLayerChange)){
////					// When Layer Change then
//					if(curForm.getString(curForm.getColumnIndex("is_layer_change")).equals("t")){
//						Log.e(TAG,"isLayerChange-> true");
//						//String latlon = curForm.getString(curForm.getColumnIndex("latlong"));
//						//latlonList.add(latlon);
////						ProjectLayerModel bin = new ProjectLayerModel();
////						bin.setLatLong(latlon);
////						jsonArray.put(new Gson().toJson(bin));
////						//bin.setLatLong(curForm.getString(curForm.getColumnIndex("latlong")));
//					}
//					else{
//						Log.e(TAG,"isLayerChange-> false");
//					}
////				}
////				else{
////					Log.e(TAG,"isLayerChange -> Null");
////				}
//				curForm.moveToNext();
//			}
//			Log.e(TAG, "List Size -> " + latlonList.size());
//		//	data.append(jsonArray.toString());
//			//Log.e(TAG,"Database Online Layer Format -> " + jsonArray.toString());
//		}
//		close();
//
//
//		return data.toString();
//	}
//
//	@SuppressLint("Range")
//	public boolean getGISSurveyOnlineLayersList(String work_id,String layer_id) {
//		//ArrayList<ProjectLayerModel> list = new ArrayList<>();
//
//		ProjectLayerModel bin = null;
//		boolean is = false;
//		open();
//		String query = "SELECT * FROM GISSurveyLayers WHERE layer_id ="+layer_id + " and " + "work_id =" + work_id;
//		Cursor curForm = executeCursor(query);
//		//Cursor curForm = executeCursor(getGISSurveyLayersListQry(work_id));
//		Log.e("GISSurveyOnlineLayers", ""+curForm.getCount());
//		if(curForm.getCount() > 0) {
//			curForm.moveToFirst();
//			is = true;
//			//return true;
//			//for(int i=0; i<curForm.getCount(); i++) {
////				bin = new ProjectLayerModel();
////				bin.setID(curForm.getString(curForm.getColumnIndex("id")));
////				bin.setLayerID(curForm.getString(curForm.getColumnIndex("layer_id")));
////				bin.setSurveyID(curForm.getString(curForm.getColumnIndex("survey_id")));
////				bin.setWorkID(curForm.getString(curForm.getColumnIndex("work_id")));
////				bin.setFormID(curForm.getString(curForm.getColumnIndex("form_id")));
////				bin.setLayerName(curForm.getString(curForm.getColumnIndex("layer_name")));
////				bin.setLayerType(curForm.getString(curForm.getColumnIndex("layer_type")));
////				bin.setLayerIcon(curForm.getString(curForm.getColumnIndex("layer_icon")));
////				bin.setLayerIconHeight(curForm.getString(curForm.getColumnIndex("layer_icon_height")));
////				bin.setLayerIconWidth(curForm.getString(curForm.getColumnIndex("layer_icon_width")));
////				bin.setLayerLineColor(curForm.getString(curForm.getColumnIndex("layer_line_color")));
////				bin.setLayerLineType(curForm.getString(curForm.getColumnIndex("layer_line_type")));
////				bin.setEnable(curForm.getInt(curForm.getColumnIndex("is_enable"))==1);
////				bin.setActive(curForm.getInt(curForm.getColumnIndex("is_active"))==1);
////				bin.setCustomMade(curForm.getInt(curForm.getColumnIndex("is_custom_made"))==1);
////				bin.setLatLong(curForm.getString(curForm.getColumnIndex("latlong")));
////				bin.setFormData(curForm.getString(curForm.getColumnIndex("form_data")));
////				bin.setFilledForms(curForm.getString(curForm.getColumnIndex("filledForms")));
////				bin.setFormbg_color(curForm.getString(curForm.getColumnIndex("formbg_color")));
////				bin.setForm_logo(curForm.getString(curForm.getColumnIndex("form_logo")));
////				bin.setForm_sno(curForm.getString(curForm.getColumnIndex("form_sno")));
////				bin.setOnly_view(curForm.getString(curForm.getColumnIndex("only_view")));
//			//	list.add(bin);
//			//	Log.e("GISSurveyLayer", "Layer Name: "+bin.getLayerName() +"  WorkID: "+ bin.getWorkID()+ " Enable: "+ bin.isEnable()+ " OnlyView: "+ bin.getOnly_view());
//			//	curForm.moveToNext();
//		//	}
//		}
//		close();
//		return is;
//	}
//


	public String getProjectLayersListQry(String word_id) {
		return "SELECT * FROM ProjectLayers WHERE work_id ="+word_id;
	}

	public String getGISSurveyLayersListQry(String word_id) {
		return "SELECT * FROM GISSurveyLayers WHERE work_id ="+word_id;
	}
	public String getGISSurveyLayersListQry(String layer_id, String word_id ) {
		return "SELECT * FROM GISSurveyLayers WHERE layer_id ="+layer_id + " and " + "work_id =" + word_id;
//		return "SELECT * FROM GISSurveyLayers WHERE layer_id ="+layer_id;

	}

	public String getProjectLayersListQry(String layer_id, String word_id ) {
		return "SELECT * FROM ProjectLayers WHERE layer_id ="+layer_id + " and " + "work_id =" + word_id;
		//		return "SELECT * FROM ProjectLayers WHERE layer_id ="+layer_id;
	}
	@SuppressLint("Range")
	public ArrayList<ProjectLayerModel> getSurveyFormList() {
		ArrayList<ProjectLayerModel> list = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(DataBaseHelper.GET_PROJECT_LAYERS);
		// Log.e("ProjectLayer", ""+curForm.getCount());
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0; i<curForm.getCount(); i++) {
				ProjectLayerModel bin = new ProjectLayerModel();
				bin.setID(curForm.getString(curForm.getColumnIndex("id")));
				bin.setLayerID(curForm.getString(curForm.getColumnIndex("layer_id")));
				bin.setProjectID(curForm.getString(curForm.getColumnIndex("project_id")));
				bin.setWorkID(curForm.getString(curForm.getColumnIndex("work_id")));
				bin.setFormID(curForm.getString(curForm.getColumnIndex("form_id")));
				bin.setLayerName(curForm.getString(curForm.getColumnIndex("layer_name")));
				bin.setLayerType(curForm.getString(curForm.getColumnIndex("layer_type")));
				bin.setLayerIcon(curForm.getString(curForm.getColumnIndex("layer_icon")));
				bin.setLayerIconHeight(curForm.getString(curForm.getColumnIndex("layer_icon_height")));
				bin.setLayerIconWidth(curForm.getString(curForm.getColumnIndex("layer_icon_width")));
				bin.setLayerLineColor(curForm.getString(curForm.getColumnIndex("layer_line_color")));
				bin.setLayerLineType(curForm.getString(curForm.getColumnIndex("layer_line_type")));
				bin.setEnable(curForm.getInt(curForm.getColumnIndex("is_enable"))==1);
				bin.setActive(curForm.getInt(curForm.getColumnIndex("is_active"))==1);
				bin.setCustomMade(curForm.getInt(curForm.getColumnIndex("is_custom_made"))==1);
				bin.setLatLong(curForm.getString(curForm.getColumnIndex("latlong")));
				bin.setFormData(curForm.getString(curForm.getColumnIndex("form_data")));
				bin.setFilledForms(curForm.getString(curForm.getColumnIndex("filledForms")));
				bin.setFormbg_color(curForm.getString(curForm.getColumnIndex("formbg_color")));
				bin.setForm_logo(curForm.getString(curForm.getColumnIndex("form_logo")));
				bin.setForm_sno(curForm.getString(curForm.getColumnIndex("form_sno")));

				list.add(bin);
				//Log.e("ProjectLayer", "Layer Name: "+bin.getLayerName() +"  WorkID: "+ bin.getWorkID()+ " Enable: "+ bin.isEnable());
				curForm.moveToNext();
			}
		}
		close();
		return list;
	}

	@SuppressLint("Range")
	public ArrayList<ProjectLayerModel> getSurveyFormList(String work_id) {
		ArrayList<ProjectLayerModel> list = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(getSurveyFormListQry(work_id));
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0; i<curForm.getCount(); i++) {
				ProjectLayerModel bin = new ProjectLayerModel();
				bin.setID(curForm.getString(curForm.getColumnIndex("id")));
				bin.setLayerID(curForm.getString(curForm.getColumnIndex("layer_id")));
				bin.setProjectID(curForm.getString(curForm.getColumnIndex("project_id")));
				bin.setWorkID(curForm.getString(curForm.getColumnIndex("work_id")));
				bin.setFormID(curForm.getString(curForm.getColumnIndex("form_id")));
				bin.setLayerName(curForm.getString(curForm.getColumnIndex("layer_name")));
				bin.setLayerType(curForm.getString(curForm.getColumnIndex("layer_type")));
				bin.setLayerIcon(curForm.getString(curForm.getColumnIndex("layer_icon")));
				bin.setLayerIconHeight(curForm.getString(curForm.getColumnIndex("layer_icon_height")));
				bin.setLayerIconWidth(curForm.getString(curForm.getColumnIndex("layer_icon_width")));
				bin.setLayerLineColor(curForm.getString(curForm.getColumnIndex("layer_line_color")));
				bin.setLayerLineType(curForm.getString(curForm.getColumnIndex("layer_line_type")));
				bin.setEnable(curForm.getInt(curForm.getColumnIndex("is_enable"))==1);
				bin.setActive(curForm.getInt(curForm.getColumnIndex("is_active"))==1);
				bin.setCustomMade(curForm.getInt(curForm.getColumnIndex("is_custom_made"))==1);
				bin.setLatLong(curForm.getString(curForm.getColumnIndex("latlong")));
				bin.setFormData(curForm.getString(curForm.getColumnIndex("form_data")));
				bin.setFilledForms(curForm.getString(curForm.getColumnIndex("filledForms")));
				bin.setFormbg_color(curForm.getString(curForm.getColumnIndex("formbg_color")));
				bin.setForm_logo(curForm.getString(curForm.getColumnIndex("form_logo")));
				bin.setForm_sno(curForm.getString(curForm.getColumnIndex("form_sno")));
				list.add(bin);
				curForm.moveToNext();
			}
		}
		close();
		return list;
	}

	private String getSurveyFormListQry(String word_id) {
		return "SELECT * FROM ProjectLayers WHERE work_id ="+word_id;
	}

	private String getGISSurveySurveyFormListQry(String word_id) {
		return "SELECT * FROM GISSurveyLayers WHERE work_id ="+word_id;
	}

	@SuppressLint("Range")
	public ArrayList<ProjectLayerModel> getGISSurveySurveyFormList(String work_id) {
		ArrayList<ProjectLayerModel> list = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(getGISSurveySurveyFormListQry(work_id));
		if(curForm != null && curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0; i<curForm.getCount(); i++) {
				ProjectLayerModel bin = new ProjectLayerModel();
				bin.setID(curForm.getString(curForm.getColumnIndex("id")));
				bin.setLayerID(curForm.getString(curForm.getColumnIndex("layer_id")));
				bin.setSurveyID(curForm.getString(curForm.getColumnIndex("survey_id")));
				bin.setWorkID(curForm.getString(curForm.getColumnIndex("work_id")));
				bin.setFormID(curForm.getString(curForm.getColumnIndex("form_id")));
				bin.setLayerName(curForm.getString(curForm.getColumnIndex("layer_name")));
				bin.setLayerType(curForm.getString(curForm.getColumnIndex("layer_type")));
				bin.setLayerIcon(curForm.getString(curForm.getColumnIndex("layer_icon")));
				bin.setLayerIconHeight(curForm.getString(curForm.getColumnIndex("layer_icon_height")));
				bin.setLayerIconWidth(curForm.getString(curForm.getColumnIndex("layer_icon_width")));
				bin.setLayerLineColor(curForm.getString(curForm.getColumnIndex("layer_line_color")));
				bin.setLayerLineType(curForm.getString(curForm.getColumnIndex("layer_line_type")));
				bin.setEnable(curForm.getInt(curForm.getColumnIndex("is_enable"))==1);
				bin.setActive(curForm.getInt(curForm.getColumnIndex("is_active"))==1);
				bin.setCustomMade(curForm.getInt(curForm.getColumnIndex("is_custom_made"))==1);
				bin.setLatLong(curForm.getString(curForm.getColumnIndex("latlong")));
				bin.setFormData(curForm.getString(curForm.getColumnIndex("form_data")));
				bin.setFilledForms(curForm.getString(curForm.getColumnIndex("filledForms")));
				bin.setFormbg_color(curForm.getString(curForm.getColumnIndex("formbg_color")));
				bin.setForm_logo(curForm.getString(curForm.getColumnIndex("form_logo")));
				bin.setForm_sno(curForm.getString(curForm.getColumnIndex("form_sno")));
				list.add(bin);
				curForm.moveToNext();
			}
		}
		close();
		return list;
	}


	@SuppressLint("Range")
	public ArrayList<FormDataModel> getProjectFormList(String work_id) {
		ArrayList<FormDataModel> list = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(getProjectFormListQry(work_id));
		//Log.e("ProjectForm", ""+curForm.getCount());
		if(curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0; i<curForm.getCount(); i++) {
				FormDataModel bin = new FormDataModel();
				bin.setID(curForm.getString(curForm.getColumnIndex("id")));
				bin.setWorkID(curForm.getString(curForm.getColumnIndex("work_id")));
				bin.setFormID(curForm.getString(curForm.getColumnIndex("form_id")));
				bin.setFormData(curForm.getString(curForm.getColumnIndex("form_data")));
				list.add(bin);
				//Log.e("ProjectForm", " ID: "+list.get(i).getID() +" WorkID: " + list.get(i).getWorkID()+ " FormID: "+ list.get(i).getFormID() +" FormData: "+ list.get(i).getFormData());
				curForm.moveToNext();
			}
		}
		close();
		return list;
	}

	@SuppressLint("Range")
	public ArrayList<FormDataModel> getProjectFormList() {
		ArrayList<FormDataModel> list = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(DataBaseHelper.GET_PROJECT_FORM);
		//Log.e("ProjectFormSync", ""+curForm.getCount());
		if(curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0; i<curForm.getCount(); i++) {
				FormDataModel bin = new FormDataModel();
				bin.setID(curForm.getString(curForm.getColumnIndex("id")));
				bin.setWorkID(curForm.getString(curForm.getColumnIndex("work_id")));
				bin.setFormID(curForm.getString(curForm.getColumnIndex("form_id")));
				bin.setLayerID(curForm.getString(curForm.getColumnIndex("layer_id")));
				bin.setFormData(curForm.getString(curForm.getColumnIndex("form_data")));
				bin.setUniqueNumber(curForm.getString(curForm.getColumnIndex("unique_number")));
				bin.setFileContain(curForm.getString(curForm.getColumnIndex("file")));
				bin.setCameraContain(curForm.getString(curForm.getColumnIndex("camera")));
				bin.setVideoContain(curForm.getString(curForm.getColumnIndex("video")));
				bin.setAudioContain(curForm.getString(curForm.getColumnIndex("audio")));
				bin.setFileColNumber(curForm.getString(curForm.getColumnIndex("file_col_number")));
				bin.setCameraColNumber(curForm.getString(curForm.getColumnIndex("camera_col_number")));
				bin.setVideoColNumber(curForm.getString(curForm.getColumnIndex("video_col_number")));
				bin.setAudioColNumber(curForm.getString(curForm.getColumnIndex("audio_col_number")));
				bin.setIcon(curForm.getString(curForm.getColumnIndex("icon")));
				list.add(bin);
				//Log.e("ProjectFormSync", " ID: "+bin.getID() +" WorkID: " +bin.getWorkID()+ " FormID: "+ bin.getFormID() +" FormData: "+ bin.getFormData() + " file: "+ bin.getFileContain() +" image: "+ bin.getCameraContain() + " video: "+ bin.getVideoContain() + " audio: "+ bin.getAudioContain());
				//Log.e("ProjectFormSync", " ID: "+bin.getID());
				curForm.moveToNext();
			}
		}
		close();
		return list;
	}


	@SuppressLint("Range")
	public ArrayList<FormDataModel> getGISSurveyFormList() {
		ArrayList<FormDataModel> list = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(DataBaseHelper.GET_GIS_SURVEY_FORM);
		//Log.e("ProjectFormSync", ""+curForm.getCount());
		if(curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0; i<curForm.getCount(); i++) {
				FormDataModel bin = new FormDataModel();
				bin.setID(curForm.getString(curForm.getColumnIndex("id")));
				bin.setWorkID(curForm.getString(curForm.getColumnIndex("work_id")));
				bin.setFormID(curForm.getString(curForm.getColumnIndex("form_id")));
				bin.setLayerID(curForm.getString(curForm.getColumnIndex("layer_id")));
				bin.setFormData(curForm.getString(curForm.getColumnIndex("form_data")));
				bin.setUniqueNumber(curForm.getString(curForm.getColumnIndex("unique_number")));
				bin.setFileContain(curForm.getString(curForm.getColumnIndex("file")));
				bin.setCameraContain(curForm.getString(curForm.getColumnIndex("camera")));
				bin.setVideoContain(curForm.getString(curForm.getColumnIndex("video")));
				bin.setAudioContain(curForm.getString(curForm.getColumnIndex("audio")));
				bin.setFileColNumber(curForm.getString(curForm.getColumnIndex("file_col_number")));
				bin.setCameraColNumber(curForm.getString(curForm.getColumnIndex("camera_col_number")));
				bin.setVideoColNumber(curForm.getString(curForm.getColumnIndex("video_col_number")));
				bin.setAudioColNumber(curForm.getString(curForm.getColumnIndex("audio_col_number")));
				bin.setIcon(curForm.getString(curForm.getColumnIndex("icon")));
				list.add(bin);
				//Log.e("ProjectFormSync", " ID: "+bin.getID() +" WorkID: " +bin.getWorkID()+ " FormID: "+ bin.getFormID() +" FormData: "+ bin.getFormData() + " file: "+ bin.getFileContain() +" image: "+ bin.getCameraContain() + " video: "+ bin.getVideoContain() + " audio: "+ bin.getAudioContain());
				//Log.e("ProjectFormSync", " ID: "+bin.getID());
				curForm.moveToNext();
			}
		}
		close();
		return list;
	}

	@SuppressLint("Range")
	public ArrayList<FormDataModel> getProjectFormListLocal(String work_id) {
		ArrayList<FormDataModel> list = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(getProjectFormListLocalQry(work_id));
		//Log.e("ProjectFormSync", ""+curForm.getCount());
		if(curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0; i<curForm.getCount(); i++) {
				FormDataModel bin = new FormDataModel();
				bin.setID(curForm.getString(curForm.getColumnIndex("id")));
				bin.setWorkID(curForm.getString(curForm.getColumnIndex("work_id")));
				bin.setFormID(curForm.getString(curForm.getColumnIndex("form_id")));
				bin.setLayerID(curForm.getString(curForm.getColumnIndex("layer_id")));
				bin.setFormData(curForm.getString(curForm.getColumnIndex("form_data")));
				bin.setUniqueNumber(curForm.getString(curForm.getColumnIndex("unique_number")));
				bin.setFileContain(curForm.getString(curForm.getColumnIndex("file")));
				bin.setCameraContain(curForm.getString(curForm.getColumnIndex("camera")));
				bin.setVideoContain(curForm.getString(curForm.getColumnIndex("video")));
				bin.setAudioContain(curForm.getString(curForm.getColumnIndex("audio")));
				bin.setFileColNumber(curForm.getString(curForm.getColumnIndex("file_col_number")));
				bin.setCameraColNumber(curForm.getString(curForm.getColumnIndex("camera_col_number")));
				bin.setVideoColNumber(curForm.getString(curForm.getColumnIndex("video_col_number")));
				bin.setAudioColNumber(curForm.getString(curForm.getColumnIndex("audio_col_number")));
				bin.setIcon(curForm.getString(curForm.getColumnIndex("icon")));
				list.add(bin);
				//Log.e("ProjectFormSync", " ID: "+bin.getID() +" WorkID: " +bin.getWorkID()+ " FormID: "+ bin.getFormID() +" FormData: "+ bin.getFormData() + " file: "+ bin.getFileContain() +" image: "+ bin.getCameraContain() + " video: "+ bin.getVideoContain() + " audio: "+ bin.getAudioContain());
				//Log.e("ProjectFormSync", " ID: "+bin.getID());
				curForm.moveToNext();
			}
		}
		close();
		return list;
	}

	@SuppressLint("Range")
	public ArrayList<FormDataModel> getGISSurveyFormListLocal(String work_id) {
		ArrayList<FormDataModel> list = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(getGISSurveyFormListLocalQry(work_id));
		//Log.e("ProjectFormSync", ""+curForm.getCount());
		if(curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0; i<curForm.getCount(); i++) {
				FormDataModel bin = new FormDataModel();
				bin.setID(curForm.getString(curForm.getColumnIndex("id")));
				bin.setWorkID(curForm.getString(curForm.getColumnIndex("work_id")));
				bin.setFormID(curForm.getString(curForm.getColumnIndex("form_id")));
				bin.setLayerID(curForm.getString(curForm.getColumnIndex("layer_id")));
				bin.setFormData(curForm.getString(curForm.getColumnIndex("form_data")));
				bin.setUniqueNumber(curForm.getString(curForm.getColumnIndex("unique_number")));
				bin.setFileContain(curForm.getString(curForm.getColumnIndex("file")));
				bin.setCameraContain(curForm.getString(curForm.getColumnIndex("camera")));
				bin.setVideoContain(curForm.getString(curForm.getColumnIndex("video")));
				bin.setAudioContain(curForm.getString(curForm.getColumnIndex("audio")));
				bin.setFileColNumber(curForm.getString(curForm.getColumnIndex("file_col_number")));
				bin.setCameraColNumber(curForm.getString(curForm.getColumnIndex("camera_col_number")));
				bin.setVideoColNumber(curForm.getString(curForm.getColumnIndex("video_col_number")));
				bin.setAudioColNumber(curForm.getString(curForm.getColumnIndex("audio_col_number")));
				bin.setIcon(curForm.getString(curForm.getColumnIndex("icon")));
				list.add(bin);
				//Log.e("ProjectFormSync", " ID: "+bin.getID() +" WorkID: " +bin.getWorkID()+ " FormID: "+ bin.getFormID() +" FormData: "+ bin.getFormData() + " file: "+ bin.getFileContain() +" image: "+ bin.getCameraContain() + " video: "+ bin.getVideoContain() + " audio: "+ bin.getAudioContain());
				//Log.e("ProjectFormSync", " ID: "+bin.getID());
				curForm.moveToNext();
			}
		}
		close();
		return list;
	}


	private String getProjectFormListLocalQry(String word_id) {
		return "SELECT * FROM ProjectForm WHERE work_id ="+word_id;
	}

	private String getGISSurveyFormListLocalQry(String word_id) {
		return "SELECT * FROM GISSurveyForm WHERE work_id ="+word_id;
	}

	private String getProjectFormListQry(String word_id) {
		return "SELECT * FROM ProjectForm WHERE work_id ="+word_id;
	}

	private String getProjectSurveyFormListLocalQry(String work_id){
		return "SELECT * FROM ProjectSurveyForm WHERE work_id ="+work_id;
	}

	private String getGISSurveySurveyFormListLocalQry(String work_id){
		return "SELECT * FROM GISSurveySurveyForm WHERE work_id ="+work_id;
	}


	@SuppressLint("Range")
	public ArrayList<FormDataModel> getProjectSurveyFormListLocal(String work_id) {
		ArrayList<FormDataModel> list = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(getProjectSurveyFormListLocalQry(work_id));
		//Log.e("ProjectFormSync", ""+curForm.getCount());
		if(curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0; i<curForm.getCount(); i++) {
				FormDataModel bin = new FormDataModel();
				bin.setID(curForm.getString(curForm.getColumnIndex("id")));
				bin.setWorkID(curForm.getString(curForm.getColumnIndex("work_id")));
				bin.setFormID(curForm.getString(curForm.getColumnIndex("form_id")));
				bin.setLayerID(curForm.getString(curForm.getColumnIndex("layer_id")));
				bin.setFormData(curForm.getString(curForm.getColumnIndex("form_data")));
				bin.setUniqueNumber(curForm.getString(curForm.getColumnIndex("unique_number")));
				bin.setFileContain(curForm.getString(curForm.getColumnIndex("file")));
				bin.setCameraContain(curForm.getString(curForm.getColumnIndex("camera")));
				bin.setVideoContain(curForm.getString(curForm.getColumnIndex("video")));
				bin.setAudioContain(curForm.getString(curForm.getColumnIndex("audio")));
				bin.setFileColNumber(curForm.getString(curForm.getColumnIndex("file_col_number")));
				bin.setCameraColNumber(curForm.getString(curForm.getColumnIndex("camera_col_number")));
				bin.setVideoColNumber(curForm.getString(curForm.getColumnIndex("video_col_number")));
				bin.setAudioColNumber(curForm.getString(curForm.getColumnIndex("audio_col_number")));
				bin.setIcon(curForm.getString(curForm.getColumnIndex("icon")));
				list.add(bin);
				curForm.moveToNext();
			}
		}
		close();
		return list;
	}


	@SuppressLint("Range")
	public ArrayList<FormDataModel> getGISSurveySurveyFormListLocal(String work_id) {
		ArrayList<FormDataModel> list = new ArrayList<>();
		open();
		Cursor curForm = executeCursor(getGISSurveySurveyFormListLocalQry(work_id));
		//Log.e("ProjectFormSync", ""+curForm.getCount());
		if(curForm.getCount() > 0) {
			curForm.moveToFirst();
			for(int i=0; i<curForm.getCount(); i++) {
				FormDataModel bin = new FormDataModel();
				bin.setID(curForm.getString(curForm.getColumnIndex("id")));
				bin.setWorkID(curForm.getString(curForm.getColumnIndex("work_id")));
				bin.setFormID(curForm.getString(curForm.getColumnIndex("form_id")));
				bin.setLayerID(curForm.getString(curForm.getColumnIndex("layer_id")));
				bin.setFormData(curForm.getString(curForm.getColumnIndex("form_data")));
				bin.setUniqueNumber(curForm.getString(curForm.getColumnIndex("unique_number")));
				bin.setFileContain(curForm.getString(curForm.getColumnIndex("file")));
				bin.setCameraContain(curForm.getString(curForm.getColumnIndex("camera")));
				bin.setVideoContain(curForm.getString(curForm.getColumnIndex("video")));
				bin.setAudioContain(curForm.getString(curForm.getColumnIndex("audio")));
				bin.setFileColNumber(curForm.getString(curForm.getColumnIndex("file_col_number")));
				bin.setCameraColNumber(curForm.getString(curForm.getColumnIndex("camera_col_number")));
				bin.setVideoColNumber(curForm.getString(curForm.getColumnIndex("video_col_number")));
				bin.setAudioColNumber(curForm.getString(curForm.getColumnIndex("audio_col_number")));
				bin.setIcon(curForm.getString(curForm.getColumnIndex("icon")));
				//bin.setLine_type(curForm.getString(curForm.getColumnIndex("line_type")));

				//	bin.setFormbg_color(curForm.getString(curForm.getColumnIndex("formbg_color")));
				//	bin.setForm_logo(curForm.getString(curForm.getColumnIndex("form_logo")));
				//	bin.setForm_sno(curForm.getString(curForm.getColumnIndex("form_sno")));
				list.add(bin);
				//Log.e("ProjectFormSync", " ID: "+bin.getID() +" WorkID: " +bin.getWorkID()+ " FormID: "+ bin.getFormID() +" FormData: "+ bin.getFormData() + " file: "+ bin.getFileContain() +" image: "+ bin.getCameraContain() + " video: "+ bin.getVideoContain() + " audio: "+ bin.getAudioContain());
				//	Log.e("ProjectFormSync", " ID: "+bin.getID());
				curForm.moveToNext();
			}
		}
		close();
		return list;
	}

	private String getProjectGeoFenceListLocalQry(String work_id){
		return "SELECT * FROM ProjectGeoFence WHERE work_id ="+work_id;
	}
	private String getGISSurveyGeoFenceListLocalQry(String work_id){
		return "SELECT * FROM SurveyGeoFence WHERE work_id ="+work_id;
	}


	// Geo Fence
	@SuppressLint("Range")
	public GeoFenceModel getGeoFenceListLocal(String work_id){
		GeoFenceModel bin = new GeoFenceModel();
		open();
		Cursor curForm = executeCursor(getProjectGeoFenceListLocalQry(work_id));
		if(curForm.getCount() > 0) {
			curForm.moveToFirst();
				bin.setProjectID(    curForm.getString(curForm.getColumnIndex("project_id")));
				bin.setStageID(      curForm.getString(curForm.getColumnIndex("stage_id")));
				bin.setActivityID(   curForm.getString(curForm.getColumnIndex("activity_id")));
				bin.setWorkID(       curForm.getString(curForm.getColumnIndex("work_id")));
				bin.setWorkName(     curForm.getString(curForm.getColumnIndex("work_name")));
				bin.setGeoFence(     curForm.getString(curForm.getColumnIndex("geofence")));
		}
		close();
		return bin;
	}

	@SuppressLint("Range")
	public GeoFenceModel getGISSurveyGeoFenceListLocal(String work_id){
		GeoFenceModel bin = new GeoFenceModel();
		open();
		Cursor curForm = executeCursor(getGISSurveyGeoFenceListLocalQry(work_id));
		if(curForm.getCount() > 0) {
			curForm.moveToFirst();
			bin.setSurveyID(    curForm.getString(curForm.getColumnIndex("survey_id")));
			bin.setWorkID(       curForm.getString(curForm.getColumnIndex("work_id")));
			bin.setWorkName(     curForm.getString(curForm.getColumnIndex("work_name")));
			bin.setGeoFence(     curForm.getString(curForm.getColumnIndex("geofence")));
		}
		close();
		return bin;
	}

//---------------------------------------------------------- LogOut ------------------------------------------------------------

	public void logout() {
		executeQuery(DELETE_TABLE_PROJECT);
		executeQuery(DELETE_TABLE_FORM);
		executeQuery(DELETE_TABLE_FORM_DETAILS);
		executeQuery(DELETE_TABLE_GEOM_DETAILS);
		executeQuery(DELETE_TABLE_IMAGE_DETAILS);
		executeQuery(DELETE_TABLE_LAYERS);
		executeQuery(DELETE_TABLE_TRACKING);
		// Change By Rahul Suthar
		// Tracking
		executeQuery(DELETE_TABLE_TRACKING_STATUS);
		// Camera
		executeQuery(DELETE_TABLE_CAMERA);
		executeQuery(DELETE_TABLE_WALKING_TRACKING);
		executeQuery(DELETE_TABLE_GPS_TRACKING);
		// Project
		executeQuery(DELETE_TABLE_USER_PROJECT);
		executeQuery(DELETE_TABLE_PROJECT_USER);
		executeQuery(DELETE_TABLE_PROJECT_LAYERS);
		executeQuery(DELETE_TABLE_PROJECT_FORM);
		executeQuery(DELETE_TABLE_PROJECT_SURVEY_FORM);
		executeQuery(DELETE_TABLE_PROJECT_GEOFENCE);
		// Time Line
		executeQuery(DELETE_TABLE_TIMELINE_IMAGE);
		executeQuery(DELETE_TABLE_TIMELINE_IMAGE_LOCAL);
		executeQuery(DELETE_TABLE_TIMELINE_S_IMAGE_LOCAL);
		// Map Camera
		executeQuery(DELETE_TABLE_MAP_CAMERA_IMAGE);
		executeQuery(DELETE_TABLE_MAP_CAMERA_IMAGE_LOCAL);
		executeQuery(DELETE_TABLE_MAP_CAMERA_IMAGE_S_LOCAL);

		executeQuery(DELETE_TABLE_GIS_MAP_CAMERA_IMAGE);
		executeQuery(DELETE_TABLE_GIS_MAP_CAMERA_IMAGE_LOCAL);
		executeQuery(DELETE_TABLE_GIS_MAP_CAMERA_IMAGE_S_LOCAL);


		// GIS Survey
		executeQuery(DELETE_TABLE_GIS_SURVEY);
		executeQuery(DELETE_TABLE_GIS_SURVEY_GEOFENCE);
		executeQuery(DELETE_TABLE_GIS_SURVEY_LAYERS);
		executeQuery(DELETE_TABLE_GIS_SURVEY_ONLINE_LAYERS);
		executeQuery(DELETE_TABLE_GIS_SURVEY_FORM);
		executeQuery(DELETE_TABLE_GIS_SURVEY_SURVEY_FORM);

		executeQuery(DELETE_TABLE_ONLINE_LAYERS);

		// Project Array
		executeQuery(DELETE_TABLE_PROJECT_Array);
		executeQuery(DELETE_TABLE_PROJECT_WORK_Array);
		// Gis Survey Array
		executeQuery(DELETE_TABLE_GIS_SURVEY_Array);
		executeQuery(DELETE_TABLE_GIS_SURVEY_WORK_Array);

	}
}