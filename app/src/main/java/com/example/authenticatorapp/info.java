package com.example.authenticatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class info extends AppCompatActivity {
    FloatingActionButton faqbtn, carebtn;
    TextView textheader;
    ScrollView s1, s2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.info);

        textheader = findViewById(R.id.InformationHeader);
        faqbtn = findViewById(R.id.faqInfo);
        carebtn = findViewById(R.id.careInfo);
        s1 = findViewById(R.id.viewS1);
        s2 = findViewById(R.id.viewS2);
        textheader.setText("Care Information");
        faqbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textheader.setText("FAQ's");
                s1.setVisibility(View.GONE);
                s2.setVisibility(View.VISIBLE);

            }
        });
        carebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textheader.setText("Care Information");
                s1.setVisibility(View.VISIBLE);
                s2.setVisibility(View.GONE);

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext()
                                , MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.info:
                        return true;
                    case R.id.travel:


                        startActivity(new Intent(getApplicationContext()
                                , travel.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.logOut:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), logIn.class));
                        finish();
                        return true;

                }
                return false;
            }
        });
    }
}
