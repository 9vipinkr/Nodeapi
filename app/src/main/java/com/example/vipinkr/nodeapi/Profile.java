package com.example.vipinkr.nodeapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Profile extends AppCompatActivity {
    private static final String TAG = "Profile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        String token = getIntent().getStringExtra("token");
        String url = getIntent().getStringExtra("url");
        Log.d(TAG, "onCreate: url"+url);
        Log.d(TAG, "onCreate: token"+token);
       new ProfileGet().execute(url,token);


    }
}
