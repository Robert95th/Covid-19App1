package com.example.authenticatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView t1, t2, t3, t4, t5, t6;
    RequestQueue mQueue;
    ArrayList<Object> covidCountries;
    String urlLink = "https://corona.lmao.ninja/v2/countries/namibia";
    Button b1;
    ProgressBar p1,p2,p3,p4,p5,p6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);
        t1 = findViewById(R.id.dashTotalCase);
        t2 = findViewById(R.id.dashActiveCases);
        t5 = findViewById(R.id.dashActiveCasesToday);
        t3 = findViewById(R.id.dashDeaths);
        t4 = findViewById(R.id.dashRecoverd);
        t6 = findViewById(R.id.dashtest);


        //Progress bar

        p1 = findViewById(R.id.progAct);
        p2 = findViewById(R.id.progRec);
        p5 = findViewById(R.id.progToday);
        p3 = findViewById(R.id.testedPro);
        p4 = findViewById(R.id.totalPro);
        p6 = findViewById(R.id.testedDeath);

        b1 = findViewById(R.id.loadbtn);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGetRequest();
            }
        });

        sendGetRequest();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.dashboard:
                        return true;
                    case R.id.info:


                        startActivity(new Intent(getApplicationContext()
                                , info.class));
                        overridePendingTransition(0, 0);
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

    private void sendGetRequest() {
        //get working now
        //let's try post and send some data to server
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://corona.lmao.ninja/v2/countries/namibia";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                t1.setText("Data : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    t1.setText(jsonObject.getString("cases"));
                    t3.setText(jsonObject.getString("deaths") + "\n");
                    t4.setText(jsonObject.getString("recovered"));
                    t2.setText(jsonObject.getString("active"));
                    t5.setText(jsonObject.getString("todayCases"));
                    t6.setText(jsonObject.getString("tests"));
                    // removing progress bar
                    p1.setVisibility(View.GONE);
                    p2.setVisibility(View.GONE);
                    p3.setVisibility(View.GONE);
                    p4.setVisibility(View.GONE);
                    p5.setVisibility(View.GONE);
                    p6.setVisibility(View.GONE);
                    //putting the view
                    t1.setVisibility(View.VISIBLE);
                    t2.setVisibility(View.VISIBLE);
                    t3.setVisibility(View.VISIBLE);
                    t4.setVisibility(View.VISIBLE);
                    t5.setVisibility(View.VISIBLE);
                    t6.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    e.printStackTrace();
                    t1.setText("Failed to Parse Json");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                t1.setText("Data : Response Failed");
            }
        });

        queue.add(stringRequest);
    }

    public void getDAta() {

        String data = "";
        URL url = null;
        try {
            url = new URL("https://corona.lmao.ninja/v2/countries/");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = " ";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }
            t1.setText(data);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //log out user
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), logIn.class));
        finish();
    }


}
