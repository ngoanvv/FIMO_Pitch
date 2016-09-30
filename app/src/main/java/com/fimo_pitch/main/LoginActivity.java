package com.fimo_pitch.main;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.model.UserModel;
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

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    public static String TAG="LoginActivity";
    private TextView tv_signUp,bt_login,tv_forgot;
    private EditText edt_email,edt_password;
    private SharedPreferences sharedPreferences;
    private LoginButton loginFB;
    private Dialog dialog;
    private CallbackManager callbackManager;
    private String email,password;
    private SignInButton loginGG;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN=9999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);
        initGoogleAPI();
        initView();
        getLocalData();
//        if(sharedPreferences!=null)   flash();
//        moveToHomeScreen();
    }
    public void initGoogleAPI()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
    public void moveToHomeScreen()
    {
        startActivity(new Intent(LoginActivity.this, NavigationActivity.class));
        this.finish();
    }

    public void getLocalData()
    {
        sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        if(sharedPreferences != null)
        {
            Log.d(TAG,sharedPreferences.getString("email","null"));
            Log.d(TAG,sharedPreferences.getString("password","null"));
            edt_email.setText(sharedPreferences.getString("email",""));
            edt_password.setText(sharedPreferences.getString("password",""));

        }
    }
    public void initView()
    {
        tv_signUp = (TextView) findViewById(R.id.link_signup);
        tv_forgot = (TextView) findViewById(R.id.link_forgot);
        bt_login= (TextView) findViewById(R.id.btn_login);
        edt_password = (EditText) findViewById(R.id.input_password);
        edt_email = (EditText) findViewById(R.id.input_email);
        loginFB = (LoginButton) findViewById(R.id.loginFB);
        loginGG = (SignInButton) findViewById(R.id.loginGG);
        loginGG.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        tv_signUp.setOnClickListener(this);
        tv_forgot.setOnClickListener(this);
        loginFB.setReadPermissions(Arrays.asList("public_profile,email,user_birthday"));
        loginFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        ShowToast.showToastLong(LoginActivity.this,"Login success");

                        String accessToken = loginResult.getAccessToken().getToken();
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.d(TAG,"onCOmplete :"+object.toString());
                                getFacebookData(object);
                                saveUserData(email,password,UserModel.TYPE_TEAM);
                                moveToHomeScreen();
                                Log.d(TAG,"login fb");

                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        ShowToast.showToastLong(LoginActivity.this,"Login Canceled");

                    }

                    @Override
                    public void onError(FacebookException error) {
                        ShowToast.showToastLong(LoginActivity.this,"Login error");
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
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email")) {
                bundle.putString("email", object.getString("email"));
                email = object.getString("email");
                password = object.getString("id");
            }
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));
            return bundle;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void flash()
    {

        String email = sharedPreferences.getString("email","null");
        String password = sharedPreferences.getString("password", "null");

        if(email.equals("null")&& password.equals("null"))
        {
            edt_email.setText("");
            edt_password.setText("");
        }
        else
        {
            edt_email.setText(email);
            edt_password.setText(password);
            Log.d("email/password",email+"/"+password);
            dialog = new MaterialDialog.Builder(this)
                    .content("Đang đăng nhập....")
                    .progress(true, 0)
                    .show();
//            moveToHomeScreen();

        }


    }
    public void saveUserData(String email,String password,String userType)
    {
        sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CONSTANT.USER_EMAIL,email);
        editor.putString(CONSTANT.USER_PASSWORD,password);
//        editor.putString("userType",userType);
//        Log.d("info",email+"/"+password+"/"+userType);
        editor.commit();

    }

    public boolean validate(String email,String password) {
        boolean valid = true;

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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


    private void loginGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.link_signup :
            {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                break;
            }
            case R.id.loginGG:
            {
                loginGoogle();
                break;
            }
            case R.id.btn_login :
            {

//                moveToHomeScreen();
                if (!validate(edt_email.getText().toString(),edt_password.getText().toString())) {
                        onLoginFailed();
                        break;
                }
                else {

                    if(sharedPreferences !=null)
                    {
                        if(edt_email.getText().toString().equals(sharedPreferences.getString("email","owner@gmail.com")) &&
                                edt_password.getText().toString().equals(sharedPreferences.getString("password","owner@gmail.com"))
                                )
                            startActivity(new Intent(LoginActivity.this,NavigationActivity.class));
                    }

                    dialog = new MaterialDialog.Builder(this)
                            .content("Đang đăng nhập....")
                            .progress(true, 0)
                            .show();
                    break;
                }
            }
            case R.id.link_forgot :
            {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
                break;
            }
        }
    }

    private void onLoginFailed() {
        Utils.openDialog(LoginActivity.this,"Sai thông tin đăng nhập");
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d(TAG,"email:"+acct.getEmail());
            Log.d(TAG,"id "+acct.getId());
            Log.d(TAG,"token "+acct.getIdToken());
            Log.d(TAG,"photo "+acct.getPhotoUrl().toString());
            Bundle bundle = new Bundle();
            bundle.putString(CONSTANT.USER_EMAIL,acct.getEmail());
            bundle.putString("id",acct.getId());
            bundle.putString("token",acct.getIdToken());
            bundle.putString("photo",acct.getPhotoUrl().toString());
            bundle.putString(CONSTANT.USER_NAME,acct.getGivenName().toString());

            email = acct.getEmail();
            password = acct.getId();
            saveUserData(email,password,UserModel.TYPE_TEAM);

            Intent intent = new Intent(LoginActivity.this,NavigationActivity.class);
            intent.putExtra("data",bundle);
            startActivity(intent);
            Log.d(TAG,"login fb");


        } else {

        }
    }
    @Override
    public void onStart() {
        super.onStart();


        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {

            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data); if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
