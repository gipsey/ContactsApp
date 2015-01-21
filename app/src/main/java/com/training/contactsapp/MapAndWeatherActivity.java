package com.training.contactsapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.training.contactsapp.view.MyMapFragment;
import com.training.contactsapp.view.MyWeatherFragment;

public class MapAndWeatherActivity extends ActionBarActivity {
    public final String MAP_FRAGMENT_TAG = "MAP_FRAGMENT";
    public final String WEATHER_FRAGMENT_TAG = "WEATHER_FRAGMENT";

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private MyMapFragment myMapFragment;
    private MyWeatherFragment myWeatherFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_and_weather);

        String addressToBeDisplayed = getIntent().getStringExtra(Intent.EXTRA_TEXT);

        myMapFragment = new MyMapFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MyMapFragment.ADDRESS_TO_BE_DISPLAYED_TAG, addressToBeDisplayed);
        myMapFragment.setArguments(bundle);
        myWeatherFragment = new MyWeatherFragment();

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.map_frame_layout, myMapFragment, MAP_FRAGMENT_TAG);
        fragmentTransaction.add(R.id.weather_frame_layout, myWeatherFragment, WEATHER_FRAGMENT_TAG);
        fragmentTransaction.commit();
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

    private void showToast(String text) {
        Toast.makeText(this, getLocalClassName() + " " + text, Toast.LENGTH_LONG).show();
    }

}
