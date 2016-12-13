package com.fimo_pitch;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by diep1 on 12/5/2016.
 */

public class HttpRequest {
    private static RequestQueue requestQueue;
    private static int MY_SOCKET_TIMEOUT_MS = 15000;

    public static void HttpGETrequest(Context context,String url, Response.Listener response, Response.ErrorListener errorListener)
    {
        if(requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
            StringRequest request = new StringRequest(StringRequest.Method.GET,url,response,errorListener);
            request.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(request);
        }
    }
    public static void HttpPostRequest(Context context, final String url, final String token, final Map<String,String> params, final Response.Listener response, final Response.ErrorListener errorListener)
    {
        if(requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    url,
                    response,
                    errorListener) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
//                    param.put("Accept", "application/json");
//                    param.put("Authorization", "bearer " + token);
//                    param.put("Content-Type", "application/json");
                    return param;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return params;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(request);
        }
    }
}
