package com.example.authenticatorapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class travel extends AppCompatActivity {
    TextView mEmail, mUser, tstatus, tv1, tv2;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    String userId, userNameT, emailT;
    EditText t1, t2, t3, t4, t5, t6, t7;
    Button travelBtn;
    FloatingActionButton fb;
    ProgressBar p1;
    LinearLayout l1,l2;
    RelativeLayout r1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);
        mEmail = findViewById(R.id.emailFetch);
        mUser = findViewById(R.id.userNameFetch);
        tstatus = findViewById(R.id.tripstatus);
        t1 = findViewById(R.id.fname);
        t2 = findViewById(R.id.sname);
        t3 = findViewById(R.id.idNumber);
        t4 = findViewById(R.id.desFrom);
        t5 = findViewById(R.id.desTo);
        t6 = findViewById(R.id.desMode);
        t7 = findViewById(R.id.desReason);
        travelBtn = findViewById(R.id.travelsubmitBtn);
        fb = findViewById(R.id.addTrip);
        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        p1 = findViewById(R.id.TravelPro);
        l1 = findViewById(R.id.travelName);
        l2 = findViewById(R.id.mainTripView);
        r1 = findViewById(R.id.relaView1);
        tv1 = findViewById(R.id.tripStatusfecth);
        tv2 = findViewById(R.id.TripName);
        //view full details
        l1.setVisibility(View.VISIBLE);
        l2.setVisibility(View.GONE);
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l1.setVisibility(View.GONE);
                l2.setVisibility(View.VISIBLE);
            }
        });
        //get the user id

        userId = fauth.getCurrentUser().getUid();
        DocumentReference documentReference = fstore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                emailT = documentSnapshot.getString("email");
                userNameT = documentSnapshot.getString("username");
                mEmail.setText(emailT);
                mUser.setText(userNameT);
            }
        });
        //get the user  trip
        refreshData();
        documentReference = fstore.collection("travel").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                emailT = documentSnapshot.getString("email");
                userNameT = documentSnapshot.getString("username");
                t1.setText(documentSnapshot.getString("firstName"));
                t2.setText(documentSnapshot.getString("surname"));
                t3.setText(documentSnapshot.getString("idNumber"));
                t4.setText(documentSnapshot.getString("desFrom"));
                t5.setText(documentSnapshot.getString("desTo"));
                t6.setText(documentSnapshot.getString("transMod"));
                t7.setText(documentSnapshot.getString("travRes"));
                tstatus.setText(documentSnapshot.getString("travStatus"));

                mEmail.setText(emailT);
                mUser.setText(userNameT);
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //upload new data
                final String email = mEmail.getText().toString().trim();
                String userName = mUser.getText().toString().trim();
                String Fname = t1.getText().toString().trim();
                String Sname = t2.getText().toString().trim();
                String idnum = t3.getText().toString().trim();
                String destiFrom = t4.getText().toString().trim();
                String destiTo = t5.getText().toString().trim();
                String destiMod = t6.getText().toString().trim();
                String destiRes = t7.getText().toString().trim();

                //sending the data to teh db for approval for travel permit
                DocumentReference documentReference = fstore.collection("travel").document(userId);
                Map<String, Object> user = new HashMap<>();
                user.put("firstName", Fname);
                user.put("surname", Sname);
                user.put("email", email);
                user.put("idNumber", idnum);
                user.put("desFrom", destiFrom);
                user.put("desTo", destiTo);
                user.put("transMod", destiMod);
                user.put("travRes", destiRes);
                user.put("travStatus", "pending");
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(travel.this, "permit was sent ", Toast.LENGTH_SHORT).show();
                        t1.getText().clear();
                        t2.getText().clear();
                        t3.getText().clear();
                        t4.getText().clear();
                        t5.getText().clear();
                        t6.getText().clear();
                        t7.getText().clear();
                        refreshData();
                    }
                });
//make Form invisible again
                l1.setVisibility(View.VISIBLE);
                l2.setVisibility(View.GONE);

            }
        });
        travelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refreshData();
            }
        });
        //get stored data


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.travel);

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
                        startActivity(new Intent(getApplicationContext()
                                , info.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.travel:

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

//refresh data

    public void refreshData() {
        //get the user  trip
        DocumentReference documentReference = fstore.collection("travel").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                String tripNAme = "";
                tripNAme = documentSnapshot.getString("desFrom") + " to " + documentSnapshot.getString("desTo");
                emailT = documentSnapshot.getString("email");
                userNameT = documentSnapshot.getString("username");

                t1.setText(documentSnapshot.getString("firstName"));
                t2.setText(documentSnapshot.getString("surname"));
                t3.setText(documentSnapshot.getString("idNumber"));
                t4.setText(documentSnapshot.getString("desFrom"));
                t5.setText(documentSnapshot.getString("desTo"));
                t6.setText(documentSnapshot.getString("transMod"));
                t7.setText(documentSnapshot.getString("travRes"));
                tstatus.setText(documentSnapshot.getString("travStatus"));
                tv1.setText(documentSnapshot.getString("travStatus"));
                tv2.setText(tripNAme);
                mEmail.setText(emailT);
                mUser.setText(userNameT);
                p1.setVisibility(View.GONE);
                r1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                l1.setVisibility(View.VISIBLE);
                l2.setVisibility(View.GONE);
            }
        });
    }

    //log out user
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), logIn.class));
        finish();
    }
}
