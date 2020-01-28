package com.unimaginablecode.mikkeul;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
    final FirebaseFirestore db = FirebaseFirestore.getInstance();


    private FirebaseAuth firebaseAuth;
    private Intent mIntent;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText et_name;

    private String email = "";
    private String password = "";
    private String name;

    private Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.activity_sign_up_et_id);
        editTextPassword = findViewById(R.id.activity_sign_up_et_pw);
        et_name = findViewById(R.id.activity_sign_up_et_name);
        confirm = findViewById(R.id.activity_sign_up_btn_confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singUp();
            }
        });

        ImageView back_btn = findViewById(R.id.backbtn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void singUp() {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        if(isValidEmail() && isValidPasswd()) {
            createUser(email, password);
        }
    }
    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 공백
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            // 비밀번호 공백
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            return false;
        } else {
            return true;
        }
    }


    private void createUser(String email, String password) {
        Log.d("로그인", "이메일: "+ email + "  비번: "+ password);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override

                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            String USER_KEY =  currentUser.getUid();
                            User user =new User();
                            name = et_name.getText().toString();
                            user.setName(name);
                            db.collection("User").document(USER_KEY).set(user);
                            Toast.makeText(SignUpActivity.this,"회원가입 성공적!!!", Toast.LENGTH_SHORT).show();
                            mIntent = new Intent(getApplicationContext(), SignUpNextActivity.class);
                            mIntent.putExtra("USER_KEY", USER_KEY);
                            startActivity(mIntent);
                            finish();

                        } else {
                            // 회원가입 실패
                            Toast.makeText(SignUpActivity.this, "회원가입 실패적...id: ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
