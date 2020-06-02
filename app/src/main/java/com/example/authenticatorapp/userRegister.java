package com.example.authenticatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class userRegister extends AppCompatActivity {
    EditText mUsername, mEmail, mPassword;
    Button mRegisterbtn;
    TextView mLogin;
    private FirebaseAuth mAuth;
    FirebaseFirestore mfStore;

    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        //setting varrieables
        mUsername = findViewById(R.id.userName);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);

        mRegisterbtn = findViewById(R.id.signUpSubmitbtn);
        mLogin = findViewById(R.id.loginPage);
        mAuth = FirebaseAuth.getInstance();

        mfStore = FirebaseFirestore.getInstance();

        //check if user is logged in
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        //redirect to loggin page
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), logIn.class));
                finish();
            }
        });
        //register the user
        mRegisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName="";
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                userName = mUsername.getText().toString().trim();

                //validation of the fields
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required");
                    return;
                }
                if (TextUtils.isEmpty(userName)) {
                    mUsername.setError("Username is required");
                    return;
                }
                if (TextUtils.isEmpty(userName)) {
                    mPassword.setError("Password must be longer than 6 charactors");
                    return;
                }

                final String finalUserName = userName;
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(userRegister.this, "User Created", Toast.LENGTH_SHORT).show();
                             userID = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference =mfStore .collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("username", finalUserName);
                            user.put("email", email);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(userRegister.this, "User Created", Toast.LENGTH_SHORT).show();

                                }
                            });
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        } else {
                            Toast.makeText(userRegister.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
