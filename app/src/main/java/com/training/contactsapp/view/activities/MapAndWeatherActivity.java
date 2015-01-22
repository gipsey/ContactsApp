package com.training.contactsapp.view.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.training.contactsapp.R;

import java.io.IOException;
import java.util.List;

public class MapAndWeatherActivity extends ActionBarActivity {
    // MAP
    private static final int ADDRESS_TO_BE_FOUND = 5;
    private final int GOOGLE_MAP_ZOOM = 15;
    private MapFragment mGoogleMapFragment;
    private GoogleMap mGoogleMap;

    // WEATHER


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_and_weather);

        // Create Map
        createMap(getIntent().getStringExtra(Intent.EXTRA_TEXT));

        // Create Weather

    }

    private void createMap(String addressToBeDisplayed) {
        mGoogleMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.google_map);
        mGoogleMap = mGoogleMapFragment.getMap();

        List<Address> addresses = getAddressesFromString(addressToBeDisplayed);

        if (addresses == null || addresses.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.cannot_find_address), Toast.LENGTH_LONG).show();
        } else if (addresses.size() == 1) {
            showLocation(addresses.get(0), addressToBeDisplayed);
            Log.w(getClass().getName(), "One address found");
        } else {
            Toast.makeText(this, getResources().getString(R.string.more_than_one_address_found), Toast.LENGTH_LONG).show();
            showLocation(addresses.get(0), addressToBeDisplayed);
            Log.w(getClass().getName(), "There was found more than one address");
        }
    }

    private List<Address> getAddressesFromString(String addressToBeDisplayed) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocationName(addressToBeDisplayed, ADDRESS_TO_BE_FOUND);
            Log.w(getClass().getName(), "There wasn't found any address");
            return addresses;
        } catch (IOException e) {
            Log.e(getClass().getName(), "Error message while getting Address from string", e);
            return null;
        }

    }

    private void showLocation(Address address, String addressToBeDisplayed) {
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        LatLng latLngToShow = new LatLng(address.getLatitude(), address.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLngToShow, GOOGLE_MAP_ZOOM);
        mGoogleMap.animateCamera(cameraUpdate);
        mGoogleMap.addMarker(new MarkerOptions().position(latLngToShow).title(addressToBeDisplayed));
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
