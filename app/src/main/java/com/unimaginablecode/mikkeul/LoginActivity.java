package com.unimaginablecode.mikkeul;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private Button mLoginBtn;
    private Button mSignUpBtn;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mIntent);
                finish();
            }
        });

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(mIntent);
            }
        });

    }

    private void init(){
        mSignUpBtn = findViewById(R.id.sign_up_btn);
        mLoginBtn = findViewById(R.id.login_btn);

    }
}
