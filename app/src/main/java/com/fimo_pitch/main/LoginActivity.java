package com.fimo_pitch.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fimo_pitch.API;
import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.adapter.SystemPitchAdapter;
import com.fimo_pitch.model.SystemPitch;
import com.fimo_pitch.model.UserModel;
import com.fimo_pitch.support.NetworkUtils;
import com.fimo_pitch.support.ShowToast;
import com.fimo_pitch.support.Utils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    public static String TAG = "LoginActivity";
    private TextView tv_signUp, bt_login, tv_forgot, tv_trial;
    private EditText edt_email, edt_password;
    private SharedPreferences sharedPreferences;
    private LoginButton loginFB;
    private String firebaseToken;
    private Dialog dialog;
    private CallbackManager callbackManager;
    private String email, password;
    private SignInButton loginGG;
    private UserModel userModel;
    private OkHttpClient okHttpClient;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 9999;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        okHttpClient = new OkHttpClient();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        userModel = new UserModel();
        initGoogleAPI();
        initView();
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        if (sharedPreferences != null)
        {
         flash();
        }
        else
        {
            Log.d(TAG,"shared null");
        }
//        moveToHomeScreen();
    }

    private void login() {
    }

    public void initGoogleAPI() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void moveToHomeScreen() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        this.finish();
    }

    public void initView() {
        tv_signUp = (TextView) findViewById(R.id.link_signup);
        tv_forgot = (TextView) findViewById(R.id.link_forgot);
        tv_trial = (TextView) findViewById(R.id.link_trial);

        bt_login = (TextView) findViewById(R.id.btn_login);
        edt_password = (EditText) findViewById(R.id.input_password);
        edt_email = (EditText) findViewById(R.id.input_email);
        loginFB = (LoginButton) findViewById(R.id.loginFB);
        loginGG = (SignInButton) findViewById(R.id.loginGG);
        loginGG.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        tv_signUp.setOnClickListener(this);
        tv_forgot.setOnClickListener(this);
        tv_trial.setOnClickListener(this);

        edt_email.setText("owner@gmail.com");
        edt_password.setText("vanduong");


        Log.d("FCM",FirebaseInstanceId.getInstance().getToken()+" ");
        loginFB.setReadPermissions(Arrays.asList("public_profile,email,user_birthday"));
        loginFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                String accessToken = loginResult.getAccessToken().getToken();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d(TAG, "onCOmplete :" + object.toString());
                        getFacebookData(object);
                        saveUserData(email, password, UserModel.TYPE_TEAM);
                        moveToHomeScreen();

                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            Utils.openDialog(LoginActivity.this,getString(R.string.login_failed));

            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private Bundle getFacebookData(JSONObject object) {
        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");
            bundle.putString("idFacebook", id);
            if (object.has("first_name")) {
                bundle.putString("first_name", object.getString("first_name"));
                Log.d("facebook:", "first_name"+object.getString("first_name"));
            }
            if (object.has("last_name")) {
                bundle.putString("last_name", object.getString("last_name"));
                Log.d("facebook:", "last_name"+object.getString("last_name"));

            }
                if (object.has("email")) {
                bundle.putString("email", object.getString("email"));
                email = object.getString("email");
                password = object.getString("id");
                Log.d("facebook:","Email:"+email);

            }
            if (object.has("gender")) {
                bundle.putString("gender", object.getString("gender"));
                Log.d("facebook:", "gender "+object.getString("gender"));

            }
            if (object.has("birthday")) {
                Log.d("facebook:", "birthday " + object.getString("birthday"));
                bundle.putString("birthday", object.getString("birthday"));
            }
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));
            return bundle;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void flash() {

        String email = sharedPreferences.getString("email", "null");
        String password = sharedPreferences.getString("password", "null");
        Log.d(TAG,email+" - "+password);
        if (email.equals("null") || password.equals("null")) {
            edt_email.setText("owner@gmail.com");
            edt_password.setText("vanduong");
        }
        else {
            edt_email.setText(email);
            edt_password.setText(password);
            HashMap<String,String> body = new HashMap<>();
            body.put("email",email);
            body.put("password",password);
            new Login(body).execute();

        }


    }

    public void saveUserData(String email, String password, String userType) {
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("userType", userType);
        Log.d("info", email + "/" + password + "/" + userType);
        editor.commit();

    }

    public boolean validate(String email, String password) {
        boolean valid = true;

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_email.setError(getString(R.string.invalid_email));
            valid = false;
        } else {
            edt_email.setError(null);
        }

        if (password.isEmpty() || password.length() < 1 || password.length() > 20) {
            edt_password.setError(getString(R.string.pass_length));
            valid = false;
        } else {
            edt_password.setError(null);
        }

        return valid;
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.link_signup: {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;
            }
            case R.id.loginGG: {
//                loginGoogle();
                break;
            }
            case R.id.btn_login: {

                if (!validate(edt_email.getText().toString(),edt_password.getText().toString())) {
                        Utils.openDialog(LoginActivity.this,"Điền đầy đủ vào thông tin đăng nhập");
                        break;
                }
                else
                {

                    HashMap<String, String> body = new HashMap<>();
                    body.put("email", edt_email.getText().toString());
                    body.put("password", edt_password.getText().toString());
                    new Login(body).execute();
                    break;
                }
            }
            case R.id.link_forgot: {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
            }
            case R.id.link_trial: {
                moveToHomeScreen();
            }
        }
    }

    class Login extends AsyncTask<String,String,String>
    {


        HashMap<String,String> param;
        ProgressDialog progressDialog;

        public Login(HashMap<String,String> body)
        {
            this.param=body;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Đang thao tác");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                Response response =
                        okHttpClient.newCall(NetworkUtils.createPostRequest(API.Login, this.param)).execute();
                String results = response.body().string();
                Log.d(TAG,results);
                if(results.contains("success"))
                    return results;
                if(results.contains("fail"))
                    return "fail";
            }
            catch (Exception e)
            {
                Log.d(TAG,"exception here");
                return "failed";
            }
            return "failed";
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG,s);
            progressDialog.dismiss();
            if(s.contains("success")) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray data = jsonObject.getJSONArray("data");
                    JSONObject result = data.getJSONObject(0);
                    userModel.setPhone(result.getString("phone"));
                    userModel.setId(result.getString("id"));
                    userModel.setName(result.getString("name"));
                    userModel.setPassword(result.getString("password"));
                    userModel.setEmail(result.getString("email"));
                    if(result.getString("type").contains("1"))
                    userModel.setUserType(UserModel.TYPE_TEAM);
                    else  userModel.setUserType(UserModel.TYPE_OWNER);
                    userModel.setImageURL("img");
                    userModel.setToken(jsonObject.getString("token"));
                    // lưu dữ liệu đăng nhập vào máy
                    saveUserData(userModel.getEmail(),userModel.getPassword(),userModel.getUserType());
                    //gửi dữ liệu user sang main activity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(CONSTANT.KEY_USER, userModel);
                    intent.putExtra(CONSTANT.KEY_USER,userModel);
                    startActivity(intent);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Utils.openDialog(LoginActivity.this,getString(R.string.login_failed));
                }
            }
            else
            {
                Utils.openDialog(LoginActivity.this,getString(R.string.login_failed));
            }

        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d(TAG, "email:" + acct.getEmail());
            Log.d(TAG, "id " + acct.getId());
            Log.d(TAG, "token " + acct.getIdToken());
            Log.d(TAG, "photo " + acct.getPhotoUrl().toString());
            Bundle bundle = new Bundle();
            bundle.putString("email", acct.getEmail());
            bundle.putString("id", acct.getId());
            bundle.putString("token", acct.getIdToken());
            bundle.putString("photo", acct.getPhotoUrl().toString());
            bundle.putString("name", acct.getGivenName().toString());

            userModel.setPhone("");
            userModel.setId("1");
            userModel.setName(acct.getGivenName().toString());
            userModel.setEmail(acct.getEmail());
            userModel.setImageURL("img");
            email = acct.getEmail();
            password = acct.getId();
            saveUserData(email, password, UserModel.TYPE_TEAM);

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("data", bundle);
            intent.putExtra(CONSTANT.KEY_USER,userModel);
            startActivity(intent);


        }
        else {

        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
