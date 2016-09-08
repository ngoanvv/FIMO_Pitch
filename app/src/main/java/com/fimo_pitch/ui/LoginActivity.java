package com.fimo_pitch.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.R;
import com.fimo_pitch.TabHostActivivty;
import com.fimo_pitch.model.UserModel;
import com.fimo_pitch.support.ShowToask;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static String TAG="LoginActivity";
    private TextView tv_signUp,bt_login,tv_forgot;
    private  EditText edt_email,edt_password;
    private  SharedPreferences sharedPreferences;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        if(sharedPreferences!=null)   flash();
    }
    public void initView()
    {
        tv_signUp = (TextView) findViewById(R.id.link_signup);
        tv_forgot = (TextView) findViewById(R.id.link_forgot);
        bt_login= (TextView) findViewById(R.id.btn_login);
        edt_password = (EditText) findViewById(R.id.input_password);
        edt_email = (EditText) findViewById(R.id.input_email);
        bt_login.setOnClickListener(this);
        tv_signUp.setOnClickListener(this);
        tv_forgot.setOnClickListener(this);


        edt_email.setText("team@gmail.com");
        edt_password.setText("123456");
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
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
            dialog = new MaterialDialog.Builder(this)
                    .content("Đang đăng nhập....")
                    .progress(true, 0)
                    .show();
            edt_email.setText(email);
            edt_password.setText(password);
            Log.d("email/password",email+"/"+password);
        }


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

    public void onLoginFailed() {

        ShowToask.showToaskLong(LoginActivity.this,getString(R.string.login)+" "+getString(R.string.failed));

    }


    private void onLoginSuccess(UserModel userModel, String password)
    {
        dialog.dismiss();
        saveUserData(userModel.getEmail(),password,userModel.getUserType());
        Intent intent= new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra(CONSTANT.USER_TYPE,userModel.getUserType());
        intent.putExtra(CONSTANT.KEY_USER,userModel);
        startActivity(intent);
        finish();
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
            case R.id.btn_login :
            {
                startActivity(new Intent(LoginActivity.this, TabHostActivivty.class));

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
            case R.id.link_forgot :
            {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
                break;
            }
        }
    }
}
