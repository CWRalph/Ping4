package com.example.ping4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowWaypoints extends AppCompatActivity {

    ArrayList<String> keys = new ArrayList<>();
    ArrayList<String> removed = new ArrayList<>();

    public void done(View view){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("remove_array",removed);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    ListView lv_savedLocations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_waypoints);

        lv_savedLocations = (ListView) findViewById(R.id.lv_values);

        MyApplication myApplication = (MyApplication) getApplicationContext();
        HashMap<String,LatLng> myPings = myApplication.getMyLocations();
        for (String temp : myPings.keySet()){
            keys.add(temp);
            Log.i("Keys",temp);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,keys);
        lv_savedLocations.setAdapter(arrayAdapter);

        //The listener waits until the user clicks on a certain value in the list
        lv_savedLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //adapterView is the listview parent, view is the item within list, position is the index of the item that was clicked on
                AlertDialog.Builder adb=new AlertDialog.Builder(ShowWaypoints.this);
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete " + keys.get(position));
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        keys.remove(positionToRemove);
                        removed.add(keys.get(positionToRemove));
                        arrayAdapter.notifyDataSetChanged();
                    }});
                adb.show();
            }
        });
    }
}