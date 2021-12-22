package com.example.ping4;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.ping4.databinding.ActivityMapsBinding;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    int latitude = 0;
    int longitude = 0;
    Random rand = new Random();
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    public void random(View view){
        latitude = rand.nextInt(100);
        longitude = rand.nextInt(100);
        Log.i("Info", "Button Pressed");
        LatLng position = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(position).title("Random Position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,10));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap = googleMap;
        //Change the type of map displayed
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


        // Add a marker in Sydney and move the camera
        //Creates a LatLng coordinate at specific latitude longitude
        LatLng position = new LatLng(latitude, longitude);
        //Creates a marker at the position of the latlng object with a title
        //THe icon changes the shape and colour of the pin on the map
        mMap.addMarker(new MarkerOptions().position(position).title("Random Position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        //Moves the camera to the coordinates of everest
        // This creates a zoomed out camera view:  mMap.moveCamera(CameraUpdateFactory.newLatLng(everest));
        //This creates a more zoomed in camera view
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,10));
    }
}