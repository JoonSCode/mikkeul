package com.unimaginablecode.mikkeul;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.regex.Pattern;

public class SignUpNextActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
    final FirebaseFirestore db = FirebaseFirestore.getInstance();


    private FirebaseAuth firebaseAuth;
    private Intent mIntent;
    private EditText et_name;
    private EditText et_contact;
    private EditText et_relation;

    private String USER_KEY;

    private String contact = "";
    private String relation = "";
    private String name;

    private Button confirm;
    private Button skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_next);
        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseFirestore db = FirebaseFirestore.getInstance();


        USER_KEY = getIntent().getStringExtra("USER_KEY");

        et_contact = findViewById(R.id.activity_sign_up_next_et_contact);
        et_relation = findViewById(R.id.activity_sign_up_next_et_relation);
        et_name = findViewById(R.id.activity_sign_up_next_et_name);

        confirm = findViewById(R.id.activity_sign_up_next_btn_confirm);
        skip = findViewById(R.id.activity_sign_up_next_btn_skip);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_info();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
    public void add_info() {
        name = et_name.getText().toString();
        contact = et_contact.getText().toString();
        relation = et_relation.getText().toString();

        if(!name.isEmpty() && !contact.isEmpty() && !relation.isEmpty()) {
            upload();
        }
    }


    private void upload() {
        DocumentReference docRefUser = db.collection("User").document(USER_KEY);
        HashMap<String, Object> updates = new HashMap<>();


        updates.put("parentalContact", contact);
        updates.put("parentalName", name);
        updates.put("parentalRelationship", relation);

        docRefUser.update(updates);
        finish();
    }
}
