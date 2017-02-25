package com.fimo_pitch.support;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fimo_pitch.CONSTANT;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by diep1 on 2/20/2017.
 */

public class MyAsyncTask extends AsyncTask {
    HashMap<String,String> param;
    ProgressDialog progressDialog;
    public OkHttpClient okHttpClient;
    public String httpType;
    public String url;
    public Context context;
    public MyAsyncTask(Context c,HashMap<String,String> body,String url,String type)
    {
        this.param=body;
        this.httpType = type;
        this.url = url;
        this.context =c;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        okHttpClient = new OkHttpClient();
        if(httpType == CONSTANT.POST) {
            try {
                Response response = okHttpClient.newCall(NetworkUtils.createPostRequest(url, this.param)).execute();
                if (response.isSuccessful()) {
                    String results = response.body().string();
                    Log.d("run", results);
                    return results;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
        }
        if(httpType == CONSTANT.GET) {
            try {
                Response response = okHttpClient.newCall(NetworkUtils.createGetRequest(url)).execute();
                if (response.isSuccessful()) {
                    String results = response.body().string();
                    Log.d("run", results);
                    return results;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
        }
        if(httpType == CONSTANT.PUT) {
            try {
                Response response = okHttpClient.newCall(NetworkUtils.createPutRequest(url,param)).execute();
                if (response.isSuccessful()) {
                    String results = response.body().string();
                    Log.d("run", results);
                    return results;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
        }
        return "failed";
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Đang thao tác....");
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Log.d("mytask",o.toString());
        progressDialog.dismiss();
    }

}
