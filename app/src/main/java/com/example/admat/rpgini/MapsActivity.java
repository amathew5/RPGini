package com.example.admat.rpgini;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
        final String table = intent.getStringExtra("table");

        ((Button) findViewById(R.id.button_lookForTrouble)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent battleIntent = new Intent(MapsActivity.this, GameplayActivity.class);
                if(currentLocation != null) {
                    battleIntent.putExtra("seed",(currentLocation.latitude % 1337)+(currentLocation.longitude % 455));
                }
                battleIntent.putExtra("username",username);
                battleIntent.putExtra("table",table);
                startActivity(battleIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        ((TextView) findViewById(R.id.textview_playerStatus)).setText("Name: "+CurrentPlayerData.getInstance().getName()+
                "\nLevel: "+CurrentPlayerData.getInstance().getLevel()+"\tXP: "+CurrentPlayerData.getInstance().getXp());
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
            if (locationManager != null) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                // Add a marker in Sydney and move the camera
                currentLocation = new LatLng(location.getLatitude(), location.getLongitude());//(-34, 151);
                Toast.makeText(getApplicationContext(), "You are at: " + currentLocation.latitude + ", " + currentLocation.longitude, Toast.LENGTH_LONG).show();
                mMap.addMarker(new MarkerOptions().position(currentLocation).title("Where you are"));
                mMap.addCircle(new CircleOptions().center(currentLocation).radius(1000));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
                mMap.setLatLngBoundsForCameraTarget(new LatLngBounds(currentLocation, currentLocation));

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        //&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Toast.makeText(getApplicationContext(), "Location Changed\n" +
                                        "Lat: " + Math.abs(location.getLatitude() - currentLocation.latitude)
                                        + "\nLong: " + Math.abs(location.getLongitude() - currentLocation.longitude)
                                , Toast.LENGTH_SHORT).show();
                        currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                        mMap.clear();
                        //mMap.addCircle(new CircleOptions().center(currentLocation).radius(1000));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
                        mMap.setLatLngBoundsForCameraTarget(new LatLngBounds(currentLocation, currentLocation));

                        double roundedLat = Math.round(currentLocation.latitude*100)*0.01;
                        double roundedLng = Math.round(currentLocation.longitude*100)*0.01;
                        mMap.addCircle(new CircleOptions().center(new LatLng(roundedLat,roundedLng)).radius(1000));
                        mMap.addMarker(new MarkerOptions().position(currentLocation).title("["+roundedLat+", "+roundedLng+"]"));
//                        mMap.addPolygon(new PolygonOptions().add(
//                                new LatLng(roundedLat,roundedLng), new LatLng(roundedLat+0.01,roundedLng),
//                                new LatLng(roundedLat+0.01,roundedLng+0.01), new LatLng(roundedLat,roundedLng+0.01),
//                                new LatLng(roundedLat,roundedLng)
//                        )).setFillColor(Color.argb(128,0,0,255));
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        //
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        //
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        //
                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }
}
