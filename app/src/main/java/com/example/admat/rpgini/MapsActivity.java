package com.example.admat.rpgini;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        ((Button) findViewById(R.id.button_lookForTrouble)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent battleIntent = new Intent(MapsActivity.this, GameplayActivity.class);
                startActivity(battleIntent);
            }
        });
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
        LocationManager locationManager = (LocationManager) this.getApplicationContext()
                .getSystemService(LOCATION_SERVICE);

        try {
            if(locationManager != null) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                // Add a marker in Sydney and move the camera
                LatLng sydney = new LatLng(location.getLatitude(),location.getLongitude());//(-34, 151);
                Toast.makeText(getApplicationContext(),"You are at: "+sydney.latitude+", "+sydney.longitude,Toast.LENGTH_LONG).show();
                mMap.addMarker(new MarkerOptions().position(sydney).title("Where you are"));
                mMap.addCircle(new CircleOptions().center(sydney).radius(10000));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,11));
                mMap.setLatLngBoundsForCameraTarget(new LatLngBounds(new LatLng(sydney.latitude+0.05,sydney.longitude-0.05),new LatLng(sydney.latitude-0.05,sydney.longitude-0.05)));
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }
}
