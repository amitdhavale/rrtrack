package com.markss.rfidtemplate.modules.splashscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.markss.rfidtemplate.R;

import com.markss.rfidtemplate.databinding.ActivitySplashScreenBinding;
import com.markss.rfidtemplate.home.MainActivity;
import com.markss.rfidtemplate.modules.login.activity.LoginActivity;
import com.markss.rfidtemplate.utils.PreferenceUtil;

public class  SplashScreenActivity extends AppCompatActivity {

    ActivitySplashScreenBinding binding;
    Context mCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);

        mCon = this;

        proceedToLoginCheck();
    }

    public void proceedToLoginCheck(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                check if logged in and processed to Main Activity else Login
                if (!PreferenceUtil.isUserLoggedIn()) {
                    startActivity(new Intent(mCon, LoginActivity.class));
                    finish();
                } else {
                    Intent intent = new Intent(mCon, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                Log.d("checkLogin", ""+ PreferenceUtil.isUserLoggedIn());
            }
        }, 3000);

    }
}