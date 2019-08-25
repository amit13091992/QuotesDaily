package com.amit.quotesdaily.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Window;
import android.view.WindowManager;

import com.amit.quotesdaily.R;


public class SplashScreen extends AppCompatActivity {
    public static final String TAG = SplashScreen.class.getSimpleName();

    //ActivitySplashScreenBinding splashScreenBinding;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //splashScreenBinding = DataBindingUtil.setContentView(this,
        setContentView(R.layout.activity_splash_screen);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }

        setupWindowAnimations();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                createFirstTimeOnly();
            }
        }, 2000);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(2000);
        getWindow().setReturnTransition(slide);
    }

    private void createFirstTimeOnly() {
        SharedPreferences settings = getSharedPreferences("prefs", 0);
        boolean firstRun = settings.getBoolean("firstRun", false);
        if (!firstRun)//if running for first time
        //Splash will load for first time
        {
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstRun", true);
            editor.apply();
            Intent i = new Intent(SplashScreen.this, IntroActivity.class);
            startActivity(i);
            finish();
        } else {
            Intent a = new Intent(SplashScreen.this, CategoryActivity.class);
            startActivity(a);
            finish();
        }
    }


}
