package com.pathmazing.androidmaps;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.pathmazing.androidmaps.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Bundle bundle;
    private Double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        bundle = getIntent().getExtras();
        lat = bundle.getDouble("key_lat");
        lng = bundle.getDouble("key_lng");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng khmer = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(khmer).title("Marker in Cambodia"));
       // CameraUpdate update = CameraUpdateFactory.newLatLngZoom(khmer, 17);
        mMap.animateCamera(CameraUpdateFactory.zoomBy(15));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(khmer));


    }
}
