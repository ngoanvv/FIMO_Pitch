package com.fimo_pitch.support;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by diep1 on 1/10/2017.
 */

public class NetworkUtils {
    public static Request createPostRequest(String url, HashMap<String,String> params)
    {
        FormBody.Builder builder = new FormBody.Builder();
        for(Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            builder.add(key,value);
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        return request;
    }
    public static Request createPutRequest(String url, HashMap<String,String> params)
    {
        FormBody.Builder builder = new FormBody.Builder();
        for(Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            builder.add(key,value);
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder().url(url).put(formBody).build();
        return request;
    }
}
