package com.volly;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDexApplication;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.surveybaba.ADAPTER.BluetoothDeviceAdapter;
import com.surveybaba.service.AppLifecycleObserver;
import com.surveybaba.service.BluetoothDeviceService;
import com.surveybaba.service.ForgroundLocationService;
import com.surveybaba.service.SyncService;
import com.volly.URL_Utility.ResponseCode;
import java.util.HashMap;
import java.util.Map;

import static com.surveybaba.service.BluetoothDeviceService.START_FOREGROUND_BLUETOOTH_DEVICE;
import static com.surveybaba.service.BluetoothDeviceService.STOP_FOREGROUND_BLUETOOTH_DEVICE;
import static com.surveybaba.service.ForgroundLocationService.START_FOREGROUND_ACTION;
import static com.surveybaba.service.ForgroundLocationService.STOP_FOREGROUND_ACTION;
import static com.surveybaba.service.SyncService.START_FOREGROUND_SYNC;
import static com.surveybaba.service.SyncService.STOP_FOREGROUND_SYNC;

public class BaseApplication extends MultiDexApplication {

	public AppLifecycleObserver appLifecycleObserver;
	private static BaseApplication sInstance;
	private static RequestQueue mVolleyQueue;
	private static final int SOCKET_TIME_OUT = 600000; // 10 minutes
//	private static final int SOCKET_TIME_OUT = 0; // 10 minutes

	private static Map<String, WSResponseInterface> postInterfaceMap = new HashMap<String, WSResponseInterface>();
	private static Map<String, ResponseCode> postCodeMap = new HashMap<String, ResponseCode>();
	private static Map<String, Map<String, String>> postParamMap = new HashMap<String, Map<String, String>>();

//------------------------------------------------- onCreate ------------------------------------------------------------------------------------------------------------------------

	@Override
	public void onCreate() {
		super.onCreate();
		initVolley(getApplicationContext());
		appLifecycleObserver = new AppLifecycleObserver();
		ProcessLifecycleOwner.get().getLifecycle().addObserver(appLifecycleObserver);
	}

//------------------------------------------------- initVolley ------------------------------------------------------------------------------------------------------------------------

	private final void initVolley(Context context) {
		sInstance = this;
		mVolleyQueue = Volley.newRequestQueue(context);
	}

//------------------------------------------------- getInstance ------------------------------------------------------------------------------------------------------------------------

    public static synchronized BaseApplication getInstance() {
        return sInstance;
    }

//------------------------------------------------- makeHttpPostRequest ------------------------------------------------------------------------------------------------------------------------

	public void makeHttpPostRequest(Context activity, ResponseCode responseCode, String url, Map<String, String> params, boolean showInternetError, boolean shouldCache) {
		final WSResponseInterface responseInterface;
		if (activity instanceof WSResponseInterface) {
			responseInterface = (WSResponseInterface) activity;
		} else {
			throw new ClassCastException(activity.getClass().getSimpleName() + " must implement the WSResponseInterface");
		}
		Log.e("http", "responseCode: "+responseCode);
		Log.e("http", "url: "+url);
		//Log.e("http", "params: "+new Gson().toJson(params));
		makePostRequest(responseInterface, responseCode, url, params, showInternetError, shouldCache);
	}

//------------------------------------------------- makeHttpPostRequest ------------------------------------------------------------------------------------------------------------------------

	public void makeHttpPostRequest(Activity activity, ResponseCode responseCode, String url, Map<String, String> params, boolean showInternetError, boolean shouldCache) {
		final WSResponseInterface responseInterface;
		if (activity instanceof WSResponseInterface) {
			responseInterface = (WSResponseInterface) activity;
		} else {
			throw new ClassCastException(activity.getLocalClassName() + " must implement the WSResponseInterface");
		}
		Log.e("http", "responseCode: "+responseCode);
		Log.e("http", "url: "+url);
		//Log.e("http", "params: "+new Gson().toJson(params));
		makePostRequest(responseInterface, responseCode, url, params, showInternetError, shouldCache);
	}

//------------------------------------------------- makeHttpPostRequest ------------------------------------------------------------------------------------------------------------------------

	public void makeHttpPostRequest(Fragment fragment, ResponseCode responseCode, String url, Map<String, String> params, boolean showInternetError, boolean shouldCache) {
		final WSResponseInterface responseInterface;
		if (fragment instanceof WSResponseInterface) {
			responseInterface = (WSResponseInterface) fragment;
		} else {
			throw new ClassCastException(fragment.getClass().getName() + " must implement the WSResponseInterface");
		}
		makePostRequest(responseInterface, responseCode, url, params, showInternetError, shouldCache);
	}

//------------------------------------------------- makePostRequest ------------------------------------------------------------------------------------------------------------------------

	// post Code to server
	public void makePostRequest(final WSResponseInterface responseInterface, final ResponseCode responseCode, final String url, final Map<String, String> params, final boolean showInternetError, final boolean shouldCache) {
		
		postInterfaceMap.put(url, responseInterface);
		postCodeMap.put(url, responseCode);
		postParamMap.put(url, params);

		StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				responseInterface.onSuccessResponse(responseCode, response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				responseInterface.onErrorResponse(responseCode, error);
			}
		}){
			@Override
			protected Map<String, String> getParams(){
				return params;
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("Content-Type","application/x-www-form-urlencoded");
				return params;
			}
		};

		RetryPolicy policy = new DefaultRetryPolicy(SOCKET_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		stringRequest.setRetryPolicy(policy);
		stringRequest.setShouldCache(shouldCache);
		mVolleyQueue.add(stringRequest);
	}


//------------------------------------------------- makeHttpGetRequest ------------------------------------------------------------------------------------------------------------------------

	public void makeHttpGetRequest(Activity activity, ResponseCode responseCode, String url, Map<String, String> params, boolean showInternetError, boolean shouldCache) {
		final WSResponseInterface responseInterface;
		if (activity instanceof WSResponseInterface) {
			responseInterface = (WSResponseInterface) activity;
		} else {
			throw new ClassCastException(activity.getLocalClassName() + " must implement the WSResponseInterface");
		}
		makeGetRequest(responseInterface, responseCode, url, params, showInternetError, shouldCache);
	}

//------------------------------------------------- makeHttpGetRequest ------------------------------------------------------------------------------------------------------------------------

	public void makeHttpGetRequest(Fragment fragment, ResponseCode responseCode, String url, Map<String, String> params, boolean showInternetError, boolean shouldCache) {
		final WSResponseInterface responseInterface;
		if (fragment instanceof WSResponseInterface) {
			responseInterface = (WSResponseInterface) fragment;
		} else {
			throw new ClassCastException(fragment.getClass().getName() + " must implement the WSResponseInterface");
		}
		makeGetRequest(responseInterface, responseCode, url, params, showInternetError, shouldCache);
	}

//------------------------------------------------- makeGetRequest ------------------------------------------------------------------------------------------------------------------------

	// Get Code to Server
	public void makeGetRequest(final WSResponseInterface responseInterface, final ResponseCode responseCode, final String url, final Map<String, String> params, final boolean showInternetError, final boolean shouldCache) {

		postInterfaceMap.put(url, responseInterface);
		postCodeMap.put(url, responseCode);
		postParamMap.put(url, params);

		StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				responseInterface.onSuccessResponse(responseCode, response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				responseInterface.onErrorResponse(responseCode, error);
			}
		}){
			@Override
			protected Map<String, String> getParams(){
				return params;
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("Content-Type","application/x-www-form-urlencoded");
				return params;
			}
		};

		RetryPolicy policy = new DefaultRetryPolicy(SOCKET_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		stringRequest.setRetryPolicy(policy);
		stringRequest.setShouldCache(shouldCache);
		mVolleyQueue.add(stringRequest);
	}


//------------------------------------------------------- isMyServiceRunning ----------------------------------------------------------------------------------------------------------------------

	public boolean isMyServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer
				.MAX_VALUE)) {
			if (service != null && serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
//------------------------------------------------------- Sync Service  ----------------------------------------------------------------------------------------------------------------------

	public void startSyncService() {
		if(!isMyServiceRunning(SyncService.class)) {
			Intent intent = new Intent(this, SyncService.class);
			intent.setAction(START_FOREGROUND_SYNC);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				startForegroundService(intent);
			} else {
				startService(intent);
			}
		}
	}
	public void stopSyncService() {
		if (isMyServiceRunning(SyncService.class)) {
			Intent intent = new Intent(this, SyncService.class);
			intent.setAction(STOP_FOREGROUND_SYNC);
			stopService(intent);
		}
	}

//------------------------------------------------------- Location Service ----------------------------------------------------------------------------------------------------------------------

	public void startMyService() {
		if(!isMyServiceRunning(ForgroundLocationService.class)) {
			Intent intent = new Intent(this, ForgroundLocationService.class);
			intent.setAction(START_FOREGROUND_ACTION);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				startForegroundService(intent);
			} else {
				startService(intent);
			}
		}
	}

	public void stopMyService() {
		if (isMyServiceRunning(ForgroundLocationService.class)) {
			Intent intent = new Intent(this, ForgroundLocationService.class);
			intent.setAction(STOP_FOREGROUND_ACTION);
			stopService(intent);
		}
	}
//
////------------------------------------------------------- Bluetooth Device Service ----------------------------------------------------------------------------------------------------------------------
//
//	public void startBluetoothDeviceService(){
//		if(!isMyServiceRunning(BluetoothDeviceService.class)) {
//			Intent intent = new Intent(this, BluetoothDeviceService.class);
//			intent.setAction(START_FOREGROUND_BLUETOOTH_DEVICE);
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//				startForegroundService(intent);
//			} else {
//				startService(intent);
//			}
//		}
//	}
//
//	public void stopBluetoothDeviceService() {
//		if (isMyServiceRunning(BluetoothDeviceService.class)) {
//			Intent intent = new Intent(this, BluetoothDeviceService.class);
//			intent.setAction(STOP_FOREGROUND_BLUETOOTH_DEVICE);
//			stopService(intent);
//		}
//	}


}
