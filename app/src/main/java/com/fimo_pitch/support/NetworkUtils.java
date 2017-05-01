package com.fimo_pitch.support;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;

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
    public static Request createGetRequest(String url)
    {
        Request request = new Request.Builder().url(url).build();
        return request;
    }
    public static Request createDeleteRequest(String url)
    {
        Request request = new Request.Builder().delete().url(url).build();
        return request;
    }
}
