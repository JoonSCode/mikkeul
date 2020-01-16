package com.unimaginablecode.mikkeul;

import android.content.Intent;
import android.app.Activity;
import android.os.Handler;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends LoginActivity {

    private Intent mIntent;
//    protected  void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_login);
//        Handler hd = new Handler();
//        hd.postDelayed(new splashhandler(),0);
//    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(mIntent);
        finish();
    }
}
