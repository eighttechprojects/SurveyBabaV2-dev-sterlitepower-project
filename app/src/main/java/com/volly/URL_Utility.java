package com.volly;

import com.surveybaba.Utilities.Utility;

public class URL_Utility {

	public static final String STATUS_DUPLICATE = "Duplicate";
	public static final String STATUS_SUCCESS = "Success";
	public static final String STATUS_FAIL = "fail";

	public static final String APP_VERSION = "1.0";
	public static final String ACTION_APP_VERSION = "version";
	public static final String COMMEON_API = "https://surveybaba.com/web_api/";
	// NEW API
	// https://surveybaba.com/survey-demo/api/login;
	// public static final String NEW_API = "https://surveybaba.com/survey-demo/api/";
    // public static final String NEW_API = "https://surveybaba.com/survey-dev/api/";


//	public static final String NEW_API = "https://surveybaba.com/survey-staging/api/";
	public static final String NEW_API = "http://173.249.24.149/sterlitepower/api/";
//	public static final String NEW_API = "https://surveybaba.com/sterlite/api/";



	// Common PARAM
	public static final String PARAM_TEST_FILE     = "file";
	public static final String PARAM_USED_ID       = "user_id";
	public static final String PARAM_STATE_ID      = "state_id";
	public static final String PARAM_PROJECT_ID    = "project_id";
	public static final String PARAM_WORK_ID       = "work_id";
	public static final String PARAM_SURVEY_ID     = "survey_id";
	public static final String PARAM_VERSION       = "version";
	public static final String PARAM_USERNAME 	   = "username";
	public static final String PARAM_PASSWORD      = "password";
	public static final String PARAM_NEW_PASSWORD  = "new_password";
	public static final String PARAM_CURRENT_PASSWORD = "current_password";
	public static final String PARAM_FIRST_NAME    = "first_name";
	public static final String PARAM_LAST_NAME     = "last_name";
	public static final String PARAM_EMAIL_ID      = "email_id";
	public static final String PARAM_MOBILE_NUMBER = "mobile_number";
	public static final String PARAM_DATETIME      = "datetime";
	public static final String PARAM_LATITUDE      = "latitude";
	public static final String PARAM_LONGITUDE     = "longitude";
	public static final String PARAM_TYPE          = "type";
	public static final String PARAM_LATLON        = "latlong";
	public static final String PARAM_LOGIN_TOKEN   = "login_token";
	public static final String PARAM_DESCRIPTION   = "description";
	public static final String PARAM_UNIQUE_NUMBER = "unique_number";

	// Change by Rahul Suthar New API
// ########################################## NEW API #################################################################################################################################

	// Login API
	public static final String WS_LOGIN                     = NEW_API + "login";
	// Profile Upload API
	public static final String WS_PROFILE_UPDATE            = NEW_API + "update-profile";
	// Profile password Change
	public static final String WS_PROFILE_PASSWORD_CHANGE   = NEW_API + "change-password";
	// Forget Password
	public static final String WS_FORGET_PASSWORD           = NEW_API + "forgot-password";
	// Project File
	public static final String WS_USER_PROJECT              = NEW_API + "get-user-projects";
	// Camera API
	public static final String WS_UPLOAD_CAMERA_IMAGE       = NEW_API +"upload-camera-image";
	// Time Line API
	public static final String WS_UPLOAD_TIMELINE_IMAGE       = NEW_API +"upload-timeline-image";
	// Map Camera API
	public static final String WS_UPLOAD_MAP_CAMERA_IMAGE       = NEW_API +"upload-work-image";
	// GIS Camera API
	public static final String WS_UPLOAD_GIS_CAMERA_IMAGE       = NEW_API +"upload-work-image";
	// GPS TRACKING SHOW
	public static final String WS_GPS_TRACKING_SHOW      = NEW_API + "upload-tracking-data";

	// GPS TRACKING UPLOAD API
	public static final String WS_GPS_TRACKING_UPLOAD       = NEW_API + "upload-tracking-data";
	// GPS TRACKING SHOW ALL RECORD API
	public static final String WS_GPS_TRACKING_SHOW_RECORDS = NEW_API + "get-tracking-data";
	// TRACKING STATUS
	public static final String WS_USER_TRACKING_STATUS      = NEW_API + "get-user-tracking-status";
	public static final String WS_USER_TRACKING             = NEW_API + "upload-user-tracking";
	// FORM UPLOAD
	public static final String WS_LAYER_FORM_UPLOAD         = NEW_API + "upload-project-form";
	public static final String WS_FORM_FILE_UPLOAD          = NEW_API + "upload-form-image";
	// Survey Form Data Records
	public static final String WS_SURVEY_FORM_SHOW_RECORD    = NEW_API + "";
	// State
	public static final String WS_STATE = NEW_API + "states";
	// City
	public static final String WS_CITY = NEW_API + "cities";
	// Search Surveyors
	public static final String WS_SEARCH_SURVEYORS = NEW_API +"search-surveyors";
	// Search Survey Agencies
	public static final String WS_SEARCH_SURVEY_AGENCIES = NEW_API +"search-survey-agencies";
	// Search Survey Work
	public static final String WS_SEARCH_SURVEY_WORK = NEW_API +"search-work";

	// GIS Survey
	public static final String WS_GIS_SURVEY = NEW_API + "get-user-survey";

	public static final String WS_ONLINE_LAYER = NEW_API + "update-online-map";



	// PARAMS
	// LOGIN
	public static final String PARAM_LOGIN_USER_ID  = "user_id";
	public static final String PARAM_LOGIN_USERNAME = "username";
	public static final String PARAM_LOGIN_PASSWORD = "password";
	// Camera
	public static final String PARAM_IMAGE_DATA     = "imageData";
	public static final String PARAM_IMAGE_DESC     = "image_desc";
	// Tracking status
	public static final String PARAM_TRACKING_CREATED_DATE = "created_date";
	// GPS TRACKING
	public static final String PARAM_WT_TOKEN       = "wt_token";
	// Profile
	public static final String PARAM_PROFILE_IMAGE = "profile_image";
	// State
	public static final String PARAM_ACTION = "action";
	// Search
	public static final String PARAM_STATE = "state";
	public static final String PARAM_CITY  = "city";
	public static final String PARAM_ZIP_CODER = "zipcode";


// ########################################## OLD API #######################################################################################################################

	// OLd API ####
	// Boundary API
	public static final String WS_BOUNDARY_GEO = COMMEON_API + "geojson.php";
	// Project API
	public static final String WS_PROJECT = COMMEON_API + "get_projects.php";
	//public static final String WS_LOGIN = COMMEON_API + "login.php";
	// Form API
	public static final String WS_FORMS = COMMEON_API + "get_forms.php";
	// Form Details API
	public static final String WS_FORMS_DETAILS = COMMEON_API + "form_detail.php";
	// USER Rights API
	public static final String WS_USER_RIGHTS = COMMEON_API + "user_rights.php";


	public static final String WS_USER_REGISTRATION = COMMEON_API + "user_registration.php";
	public static final String WS_USER_PROFILE = COMMEON_API + "user_profile.php";
	public static final String WS_UPLOAD_TIMELINE = COMMEON_API + "upload_timeline.php";
	public static final String WS_SAVE = COMMEON_API + "save.php";
	public static String BASE_IMAGE_URL = COMMEON_API + "upload.php";
	public static String VIDEO_PATH = COMMEON_API + "video_upload/";
	public static String BASE_VIDEO_URL = COMMEON_API + "upload_video.php";
	public static String IMAGE_PATH = COMMEON_API + "uploads/";

	// PARAM
	public static final String PARAM_FORM_ID = "formid";
	public static final String PARAM_IS_INSIDE_COVERAGE_AREA = "isInsideCoverageArea";
	public static final String PARAM_ACCURACY = "accuracy";
	public static final String PARAM_RECORD_DATE = "record_date";
	public static final String PARAM_UNIQ_ID = "uniq_id";
	public static final String PARAM_DATA = "data";
	public static final String PARAM_IMAGE = "image";
	public static final String PARAM_VIDEO = "video";
	public static final String PARAM_IMAGE_NAME = "image_name";
	public static final String PARAM_UPLOAD_PATH = "upload_path";
	public static final String PARAM_CREATED_DATE = "created_date";


// ########################################## Response Code ####################################################################################################################

	public enum ResponseCode {
		WS_LOGIN,
		WS_PROJECT,
		WS_FORMS,
		WS_FORMS_DETAILS,
		WS_SAVE,
		WS_USER_RIGHTS,
		WS_USER_REGISTRATION,
		WS_USER_PROFILE,
		WS_UPLOAD_TIMELINE,
		WS_USER_TRACK,
		WS_USER_TRACK_LOCAL,
		// Change By Rahul Suthar-----------------------------------------
		WS_USER_TRACK_STATUS,
		WS_BOUNDARY_GEO,
		WS_PROFILE_UPDATE,
		WS_PROFILE_PASSWORD_CHANGE,
		WS_FORGET_PASSWORD,
		WS_UPLOAD_CAMERA_IMAGE,
		WS_GPS_TRACKING_UPLOAD,
		WS_GPS_TRACKING_SHOW_RECORDS,
		WS_USER_PROJECT,
		WS_LAYER_FORM_UPLOAD,
		WS_SURVEY_FORM_SHOW_RECORD,
		WS_STATE,
		WS_CITY,
		WS_SEARCH_SURVEYORS,
		WS_SEARCH_SURVEY_AGENCIES,
		WS_SEARCH_SURVEY_WORK,
		WS_GIS_SURVEY,
		WS_GPS_TRACKING_SHOW,
		WS_ONLINE_LAYER

	}

//	public static String NETWORK_ERROR 		=  "The seems to be unavailable. Please check your connection and try again.";
//	public static String CLIENT_ERROR  		=  "Server is busy. Please try in sometime.";
//	public static String SERVER_ERROR  		=  "Server is busy. Please try in sometime.";
//	public static String AUTHFAILURE_ERROR 	=  "Wrong username or password.";
//	public static String PARSE_ERROR 		=  "There is issue in data. Please contact your manager.";
//	public static String NOCONNECTION_ERROR =  "The Internet seems to be unavailable. Please check your connection and try again.";
//	public static String TIMEOUT_ERROR 		=  "The Internet seems to be unavailable or slow or Server is busy, Please try in sometime.";

}