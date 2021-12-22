package com.example.ping4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Compass extends AppCompatActivity {

    public void goToNext(View view){
        //Creates an intent which is the other activity
        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
        //Start activity activates the intent
        startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
    }
}