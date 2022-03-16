package com.markss.rfidtemplate.modules.login.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.markss.rfidtemplate.R;
import com.markss.rfidtemplate.databinding.ActivityLoginBinding;
import com.markss.rfidtemplate.home.MainActivity;
import com.markss.rfidtemplate.modules.login.model.UserModel;
import com.markss.rfidtemplate.network.ApiClient;
import com.markss.rfidtemplate.utils.PreferenceUtil;
import com.markss.rfidtemplate.utils.URLs;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityLoginBinding binding;
    Context mCon;
    ProgressDialog pdLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        mCon = this;

        binding.btnLogin.setOnClickListener(this);
        binding.btnipconfig.setOnClickListener(this);

        binding.edtusername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0){
                    binding.tilusername.setError(null);
                }
            }
        });

        binding.edtpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0){
                    binding.tilpassword.setError(null);
                }
            }
        });

        binding.edtusername.setText("admin");
        binding.edtpassword.setText("admin");
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnLogin:
                validateLogin();
                break;
            case R.id.btnipconfig:
                displayIPDialog();
                break;

        }
    }

    public void validateLogin(){
        boolean isComplete = true;
        if(TextUtils.isEmpty(binding.edtusername.getText())){
            isComplete = false;
            binding.tilusername.setError(getString(R.string.username_empty));
        }

        if(TextUtils.isEmpty(binding.edtpassword.getText())){
            isComplete = false;
            binding.tilpassword.setError(getString(R.string.password_empty));
        }

        if(isComplete){
            pdLogin = new ProgressDialog(LoginActivity.this);
            pdLogin.setMessage("Please Wait.!");
            pdLogin.setCancelable(false);
            pdLogin.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//            pdLogin.show();
            loginUser();
        }
    }

    public void loginUser(){

        String userName = binding.edtusername.getText().toString().trim();
        String password = binding.edtpassword.getText().toString().trim();

//        HashMap<String,String> requestBody = new HashMap<>();
//        requestBody.put("UserName",userName);
//        requestBody.put("Password",password);

        UserModel userModel = new UserModel();
        userModel.setUserName(userName);
        userModel.setPassword(password);

        Call<UserModel> call = ApiClient.getApiInterface().loginUser(userModel);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                Log.e("resp", ""+call.request().url());
                if(response.isSuccessful()){
                    if (response.body() != null && response.body().getCode().equals(1)){
                        PreferenceUtil.setUserLoggedIn(true);
                        UserModel userLogin = response.body();
                        PreferenceUtil.setUser(userLogin);
                        proceedtoMain();
                        pdLogin.dismiss();
                    }else {

                        Toast.makeText(LoginActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        pdLogin.dismiss();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Server issue. Please Try again", Toast.LENGTH_SHORT).show();
                    pdLogin.dismiss();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Server issue. Please Try again", Toast.LENGTH_SHORT).show();
                pdLogin.dismiss();
            }
        });
    }

    public void proceedtoMain(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void displayIPDialog(){
        AlertDialog.Builder sayWindows = new AlertDialog.Builder(mCon);
        final EditText saySomething = new EditText(mCon);

        sayWindows.setPositiveButton("ok", null);
        sayWindows.setNegativeButton("cancel", null);

        LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.config_ip, null);
        sayWindows.setView(dialogView);

        final EditText configEdit = (EditText) dialogView.findViewById(R.id.configip_IpAddressEdt);
        configEdit.setText("");
        configEdit.setEnabled(true);
        configEdit.append(PreferenceUtil.getIPAddress());

        final AlertDialog mAlertDialog = sayWindows.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (!TextUtils.isEmpty(configEdit.getText())) {
                            PreferenceUtil.setIPAddress(configEdit.getText().toString().trim());
//                            sharedPreferences.edit().putString("IPAddress", configEdit.getText().toString().trim()).apply();
//                            URLs.changeIP(LoginActivity.this);
                            mAlertDialog.cancel();
                            Toast.makeText(LoginActivity.this, "Saved successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Enter the IP", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
        mAlertDialog.show();
    }
}