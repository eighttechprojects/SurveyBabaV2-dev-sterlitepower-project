package com.volly;

import com.android.volley.VolleyError;
import com.volly.URL_Utility.ResponseCode;

public interface WSResponseInterface {

	public void onSuccessResponse(ResponseCode responseCode, String response);

	public void onErrorResponse(ResponseCode responseCode, VolleyError error);

}
