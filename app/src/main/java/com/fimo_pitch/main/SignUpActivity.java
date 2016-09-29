package com.fimo_pitch.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.custom.view.RoundedImageView;
import com.fimo_pitch.model.UserModel;
import com.fimo_pitch.model.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    public   static String TAG = "SignUpActivity";
    private TextView bt_signUp,bt_login,tv_temp;
    private ArrayList<String> list_userType,list_userAdd;
    private EditText edt_userName,edt_password,edt_rePassword,edt_userEmail;
    private EditText edt_phone;
    private RadioGroup groupUsertype;
    private String userType= UserModel.TYPE_TEAM;
    private UserModel userModel;
    private Dialog dialog;
    private SharedPreferences sharedPreferences;
    private RoundedImageView img_avatar;
    private static int PICK_IMAGE=1111;
    private Uri avatarUri;
    private String downloadURL="";
    private boolean pickedImage=false;

    // Progress Dialog Object
    ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        setListener();

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Chờ trong giây lát...");
        // set mCancelable = false
        prgDialog.setCancelable(false);

    }

    public void initView()
    {
        groupUsertype  = (RadioGroup) findViewById(R.id.radio_userType);
        edt_userName   = (EditText) findViewById(R.id.input_name);
        edt_password   = (EditText) findViewById(R.id.input_password);
        edt_rePassword = (EditText) findViewById(R.id.input_re_password);
        edt_userEmail  = (EditText) findViewById(R.id.input_email);
        edt_phone      = (EditText) findViewById(R.id.input_phone);
        bt_signUp      = (TextView) findViewById(R.id.btn_signUp);
        // textview dung de luu lai imageURL
        tv_temp        = (TextView) findViewById(R.id.txt_temp);
        bt_login       = (TextView) findViewById(R.id.link_login);
        img_avatar     = (RoundedImageView) findViewById(R.id.img_avatar);
        img_avatar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.btn_signUp :
            {

            }
            case R.id.btn_login :
            {
                onBackPressed();
                break;
            }
            case R.id.img_avatar :
            {
                chooseImage();
                break;
            }
        }
    }

    public void chooseImage()
    {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    // set ảnh cho imageivew khi chọn ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"intent data: "+data.toString());
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            if (data == null) {
                Log.d(TAG, "data null");
            } else {
             Picasso.with(SignUpActivity.this).load(data.getData()).fit().into(img_avatar);
                try {
                        pickedImage=true;
                        avatarUri = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), avatarUri);
                        img_avatar.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
        }

    }

    public void setListener()
    {
        bt_login.setOnClickListener(this);
        bt_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(v);
            }
        });
        groupUsertype.check(R.id.radio_team);
        groupUsertype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId==R.id.radio_owner)
                    {
                        userType = UserModel.TYPE_OWNER;
                        Log.d("owner","click");
                    }
                    if(checkedId==R.id.radio_team)
                    {
                        userType = UserModel.TYPE_TEAM;
                        Log.d("team","click");
                    }
            }
        });


    }

    public void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }
        bt_signUp.setEnabled(false);
        String name = edt_userName.getText().toString();
        String email = edt_userEmail.getText().toString();
        String password = edt_password.getText().toString();
        String phone = edt_phone.getText().toString();
        createAccount(email,password,name,phone,userType,tv_temp.getText().toString());

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onSignupSuccess();
                    }
                }, 3000);
    }

    public void onSignupSuccess() {
        bt_signUp.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        bt_signUp.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();



    }

    public boolean validate() {
        boolean valid = true;

        String name = edt_userName.getText().toString();
        String email = edt_userEmail.getText().toString();
        String password = edt_password.getText().toString();
        String re_password = edt_rePassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            edt_userName.setError(getString(R.string.name_length));
            valid = false;
        } else {
            edt_userName.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_userEmail.setError(getString(R.string.invalid_email));
            valid = false;
        } else {
            edt_userEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            edt_password.setError(getString(R.string.pass_length));
            valid = false;
        } else {
            edt_password.setError(null);
        }
        if (re_password.isEmpty() || re_password.length() < 4 || re_password.length() > 10) {
            edt_rePassword.setError(getString(R.string.pass_length));
            valid = false;
        } else {
            edt_rePassword.setError(null);
        }
        if(!re_password.equals(password))
        {
            valid=false;
            edt_rePassword.setError(getString(R.string.invalid_repassword));
            edt_password.setError(getString(R.string.invalid_repassword));
            edt_rePassword.setText("");
            edt_password.setText("");

        }
        else
        {
            edt_rePassword.setError(null);
            edt_password.setError(null);
        }
        if(edt_phone.length()<8)
        {
            valid=false;
            edt_phone.setError(getString(R.string.invalid_repassword));
            edt_phone.setText("");

        }
        else
        {
            edt_phone.setError(null);
            edt_phone.setError(null);
        }
        return valid;
    }
    private void createAccount(final String email, final String password,final  String name,
                               final String phone, final String userType,final String imgURL)
    {

    }
    private void showProgressDialog() {
    }
    private void onLoginSuccess(UserModel userModel,String password)
    {
        dialog.dismiss();
        saveUserData(userModel.getEmail(),password,userModel.getUserType());
        Intent intent= new Intent(SignUpActivity.this,MainActivity.class);
        intent.putExtra(CONSTANT.USER_TYPE,userModel.getUserType());
        intent.putExtra(CONSTANT.KEY_USER,userModel);
        startActivity(intent);
        finish();
    }
    public void saveUserData(String email,String password,String userType)
    {
        sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email",email);
        editor.putString("password",password);
        editor.putString("userType",userType);
        Log.d("info",email+"/"+password+"/"+userType);
        editor.commit();

    }
    private void  hideProgressDialog()
    {
        dialog.dismiss();
    }

    /**
     * Method gets triggered when Register button is clicked
     *
     * @param view
     */
    public void registerUser(View view){
        //get value in xml

        String name = edt_userName.getText().toString();
        String email = edt_userEmail.getText().toString();
        String password = edt_password.getText().toString();
        String phone = edt_phone.getText().toString();
        String usertype = "";

        if (userType == UserModel.TYPE_OWNER) // chu san
        {
            usertype = "1";
        }
        else {
            usertype = "0";
        }


        // Instantiate Http Request Param Object
        RequestParams params = new RequestParams();
        // check value name, email, password
        if(Utility.isNotNull(name) && Utility.isNotNull(email) && Utility.isNotNull(password)){
            // When Email entered is Valid
            if(Utility.validate(email)){

                // put value to params
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("phone", phone);
                params.put("usertype", usertype);

                invokeWS(params);
            }
            else{
                Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Method RESTful webservice
     *
     */
    public void invokeWS(RequestParams params){

        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.1.54:8083/WebService/register/doregister",params ,new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(new String(responseBody));

                    if(obj.getBoolean("status")){

                        setDefaultValues();
                        Toast.makeText(getApplicationContext(), "You are successfully registered!", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            prgDialog.hide();

                            if(statusCode == 404){
                                Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                            }
                            else if(statusCode == 500){
                                Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                            }
                    }
       });
    }

    /**
     * Method which navigates from Register Activity to Login Activity
     */
    public void navigatetoLoginActivity(View view){
        Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
        // Clears History of Activity
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    /**
     * Set degault values for Edit View controls
     */
    public void setDefaultValues(){
        edt_userName.setText("");
        edt_userEmail.setText("");
        edt_password.setText("");
        edt_phone.setText("");
    }

}

