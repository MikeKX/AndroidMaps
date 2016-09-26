package com.pathmazing.androidmaps;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.pathmazing.androidmaps.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private String TAG = "Main Acitivty";
    private ListView listView;
    private String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=11.5718121%2C104.9173815&rankby=distance&types=gas_station&key=AIzaSyD6KsxH2-B-ZhCWOQZJpa29ELmICK86BAc";
    private ArrayList<PlaceModel> array = new ArrayList<PlaceModel>();
    private AdapterView adapterView;
    private String tag_json_array = "json_array";
    private GoogleMap mMap;
    private LatLng gasStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        listView = (ListView) navigationView.findViewById(R.id.listview);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        getDataFromServer();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng khmer = new LatLng(11.5718121, 104.9173815);
        mMap.addMarker(new MarkerOptions().position(khmer).title("Marker in Cambodia"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(khmer, 17));
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng latlng = mMap.getCameraPosition().target;
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latlng.latitude +
                        "," + latlng.longitude +
                        "&rankby=distance&types=gas_station&key=AIzaSyD6KsxH2-B-ZhCWOQZJpa29ELmICK86BAc";

                setupSendRequest(url, latlng);
            }
        });


    }


    public void setupShowJson(String json) {
        ArrayList<PlaceModel> lists = new GarasData(this).getAllGaras(json);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        for (int i = 0; i < lists.size(); i++) {
            gasStation = new LatLng(lists.get(i).getLat(), lists.get(i).getLng());
            mMap.addMarker(new MarkerOptions().position(gasStation).title(array.get(i).getName()));


        }

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(gas_station));
    }

    public void setupSendRequest(String url, final LatLng latLng) {
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                setupShowJson(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//               Toast.makeText(MapsActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    //fucntion get data from server
    private void getDataFromServer() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        PlaceModel pm = new PlaceModel();
                        JSONObject obj = (JSONObject) jsonArray.get(i);

                        pm.setIcon(obj.getString("icon"));
                        pm.setName(obj.getString("name"));
                        pm.setVicinity(obj.getString("vicinity"));
                        pm.setPlace_id(obj.getString("place_id"));

                        pm.setLat(Double.parseDouble(obj.getJSONObject("geometry").getJSONObject("location").getString("lat")));
                        pm.setLng(Double.parseDouble(obj.getJSONObject("geometry").getJSONObject("location").getString("lng")));

                        array.add(pm);
                    }
                    adapterView = new AdapterView(MainActivity.this, R.layout.row_listview, array);
                    listView.setAdapter(adapterView);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


}
