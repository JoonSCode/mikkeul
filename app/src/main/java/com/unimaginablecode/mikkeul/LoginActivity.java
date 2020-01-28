package com.unimaginablecode.mikkeul;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.Handler;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private Button mLoginBtn;
    private Button mSignUpBtn;
    private Intent mIntent;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    private FirebaseAuth firebaseAuth;

    private EditText editTextEmail;
    private EditText editTextPassword;

    private String email = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        editTextEmail = findViewById(R.id.activity_login_et_id);
        editTextPassword = findViewById(R.id.activity_login_et_pw);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(mIntent);
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();


    }
    private boolean isValidEmail() {
        if (email.isEmpty()) {
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return false;
        } else {
            return true;
        }
    }
    public void signIn() {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        if(isValidEmail() && isValidPasswd()) {
            loginUser();
        }
    }
    private void init(){
        mSignUpBtn = findViewById(R.id.sign_up_btn);
        mLoginBtn = findViewById(R.id.login_btn);

    }
    private void loginUser()
    {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            String USER_KEY =  currentUser.getUid();
                            mIntent = new Intent(getApplicationContext(), MainActivity.class);
                            mIntent.putExtra("USER_KEY", USER_KEY);
                            startActivity(mIntent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this,"로그인 실패...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
