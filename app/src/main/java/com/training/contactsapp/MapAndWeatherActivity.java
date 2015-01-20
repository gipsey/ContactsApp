package com.training.contactsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapAndWeatherActivity extends ActionBarActivity {

    private final LatLng periceiChurch = new LatLng(47.231595, 22.863289);
    private final LatLng fortechMeteor = new LatLng(46.754277, 23.593797);

    private MapFragment googleMapFragment;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_and_weather);

        String addressToBedisplayed = getIntent().getStringExtra(Intent.EXTRA_TEXT);

        googleMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.google_map);
        googleMap = googleMapFragment.getMap();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(periceiChurch);
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(periceiChurch, 15);
                googleMap.animateCamera(cameraUpdate);
                googleMap.addMarker(new MarkerOptions().position(periceiChurch).title("Pericei"));
            }
        }, 2000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(fortechMeteor);
                googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(fortechMeteor, 15);
                googleMap.animateCamera(cameraUpdate);
                googleMap.addMarker(new MarkerOptions().position(fortechMeteor).title("Fortech"));
            }
        }, 6000);

    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map_and_weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
