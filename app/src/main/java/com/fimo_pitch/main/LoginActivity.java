package com.fimo_pitch.main;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
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
import com.fimo_pitch.R;
import com.fimo_pitch.model.UserModel;
import com.fimo_pitch.support.Utils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
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

import cz.msebera.android.httpclient.impl.conn.LoggingSessionOutputBuffer;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    public static String TAG = "LoginActivity";
    private TextView tv_signUp, bt_login, tv_forgot, tv_trial;
    private EditText edt_email, edt_password;
    private SharedPreferences sharedPreferences;
    private LoginButton loginFB;
    private Dialog dialog;
    private CallbackManager callbackManager;
    private String email, password;
    private SignInButton loginGG;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 9999;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        try {
//            PackageInfo info = null;
//            try {
//                info = getPackageManager().getPackageInfo(
//                        "com.fimo_pitch",
//                        PackageManager.GET_SIGNATURES);
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
//            for (android.content.pm.Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash.", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (NoSuchAlgorithmException e) {
//
//        }

        setContentView(R.layout.activity_login);
        initGoogleAPI();
        initView();
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();



        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        if (sharedPreferences != null)
        {
            if(!sharedPreferences.getString("email","null").equals("null")
                    &&!sharedPreferences.getString("password","null").equals("null") )
            {
                flash();
            }
            else
            {

            }

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
        startActivity(new Intent(LoginActivity.this, NavigationActivity.class));
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
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
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

        if (email.equals("null") && password.equals("null")) {
            edt_email.setText("");
            edt_password.setText("");
        } else {
            edt_email.setText(email);
            edt_password.setText(password);
            Log.d("email/password", email + "/" + password);
//            dialog = new MaterialDialog.Builder(this)
//                    .content("Đang đăng nhập....")
//                    .progress(true, 0)
//                    .show();
            moveToHomeScreen();

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
        switch (id) {
            case R.id.link_signup: {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;
            }
            case R.id.loginGG: {
                loginGoogle();
                break;
            }
            case R.id.btn_login: {
                moveToHomeScreen();
                finish();
//                if (!validate(edt_email.getText().toString(),edt_password.getText().toString())) {
//                        onLoginFailed();
//                        break;
//                }
//                else {
//
//                    dialog = new MaterialDialog.Builder(this)
//                            .content("Đang đăng nhập....")
//                            .progress(true, 0)
//                            .show();
//                    break;
//                }
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

            email = acct.getEmail();
            password = acct.getId();
            saveUserData(email, password, UserModel.TYPE_TEAM);

            Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
            intent.putExtra("data", bundle);
            startActivity(intent);


        } else {

        }
    }

    @Override
    public void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();


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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
}
