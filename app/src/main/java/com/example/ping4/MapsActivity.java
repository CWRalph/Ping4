package com.example.ping4;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.ping4.databinding.ActivityMapsBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    LocationManager locationManager;
    LocationListener locationListener;
    private GoogleMap mMap;
    private MarkerOptions options = new MarkerOptions();
    private ArrayList<LatLng> latlngs = new ArrayList<LatLng>();
    private ActivityMapsBinding binding;
    double latitude,longitude;
    Marker marker,person;
    boolean commence = false;
    Bundle result = new Bundle();
    ArrayList<String> array_result = new ArrayList<>();
    Button btn_ping_list;

    //Dictionary containing our pings mapping to their indexes
    Map pings = new HashMap();
    int index = 0;

    public void compass(View view) {
        //Creates an intent which is the other activity
        Intent intent = new Intent(getApplicationContext(), Compass.class);
        //Start activity activates the intent
        startActivity(intent);
    }

    public void maps(View view) {
        Toast.makeText(this, "Already on map page!", Toast.LENGTH_SHORT).show();
    }

    public void center(View view) {
        LatLng position = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 1));
    }

    public void ping(View view) {
        //Get initial ping from user
        Intent intent = new Intent(this,PING.class);
        //intent.putExtra("Boolean",commence);
        startActivityForResult(intent,1);
        //commence = true;
    }

    public void ping_list(View view){
        Intent intent = new Intent(this,ShowWaypoints.class);
        //intent.putExtra("Boolean",commence);
        startActivityForResult(intent,2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btn_ping_list = findViewById(R.id.ping_list);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationInfo(location);
            }
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                updateLocationInfo(lastKnownLocation);
            }
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            //Result from PING
            if (resultCode == Activity.RESULT_OK) {
                result = data.getExtras();
                latitude = result.getDouble("Latitude");
                longitude = result.getDouble("Longitude");
                LatLng position = new LatLng(latitude,longitude);
                String name = result.getString("pingName");
                MyApplication myApplication = (MyApplication) getApplicationContext();
                myApplication.add_value(name,position);

                marker = mMap.addMarker(new MarkerOptions().position(position).title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 1));
            }
        }

        if (requestCode == 2) {
            //Result from ping list
            if (resultCode == Activity.RESULT_OK) {
                array_result = data.getStringArrayListExtra("remove_array");
                MyApplication myApplication = (MyApplication) getApplicationContext();
                for (String x : array_result){
                    myApplication.remove_value(x);
                }
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        LatLng position = new LatLng(latitude, longitude);
        marker = mMap.addMarker(new MarkerOptions().position(position).title("Person").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 1));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    public void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    public void updateLocationInfo(Location location) {
        if (mMap != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            LatLng position = new LatLng(latitude, longitude);
            //Creates a marker at the position of the latlng object with a title
            //THe icon changes the shape and colour of the pin on the map
            if (person != null){
                person.remove();
            }
            person = mMap.addMarker(new MarkerOptions().position(position).title("Person").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            Log.i("Here","Repeating");
        }
    }
}