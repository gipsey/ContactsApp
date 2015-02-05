package com.training.contactsapp.presentation.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.training.contactsapp.R;
import com.training.contactsapp.business.GetWeatherRequest;
import com.training.contactsapp.model.Weather;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class MapAndWeatherActivity extends Activity {
    // MAP
    private static final int ADDRESS_TO_BE_FOUND = 5;
    private final int GOOGLE_MAP_ZOOM = 15;
    private final String NO_ADDRESS_NAME_FOUND = "NO_ADDRESS_NAME_FOUND";
    private MapFragment mGoogleMapFragment;
    private GoogleMap mGoogleMap;
    private String mLocationAddressSuggestedByGoogleMaps;
    private LatLng mLatLngSuggestedByGoogleMaps;
    // WEATHER
    private LinearLayout mWeatherLinearLayout;
    private ProgressBar mWeatherLayoutProgressBar;

    private TextView mWeatherStatusOrAddressTextView;

    private GetWeatherRequest mGetWeatherRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_and_weather);

        // Create Map
        createMap(getIntent().getStringExtra(Intent.EXTRA_TEXT));

        // Create Weather
        setupWeatherLayout();
        if (mLocationAddressSuggestedByGoogleMaps == null || mLocationAddressSuggestedByGoogleMaps.isEmpty()) {
            mWeatherStatusOrAddressTextView.setText(getResources().getString(R.string.no_weather_data));
            crossFade();
        } else {
            getWeatherDataBasedOnLatLng();
        }

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

        mLocationAddressSuggestedByGoogleMaps = getAddressStringFromAddress(address);

        mLatLngSuggestedByGoogleMaps = new LatLng(address.getLatitude(), address.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mLatLngSuggestedByGoogleMaps, GOOGLE_MAP_ZOOM);
        mGoogleMap.animateCamera(cameraUpdate);
        mGoogleMap.addMarker(new MarkerOptions().position(mLatLngSuggestedByGoogleMaps).title(addressToBeDisplayed));
    }

    private String getAddressStringFromAddress(Address address) {
        String addressString = null;

        if (address.getLocality() != null) addressString = address.getLocality();

        if (address.getAdminArea() != null) {
            if (addressString == null || addressString.isEmpty()) {
                addressString = address.getAdminArea();
            } else {
                addressString = addressString + ", " + address.getAdminArea();
            }
        }

        if (address.getCountryCode() != null) {
            if (addressString == null || addressString.isEmpty()) {
                addressString = address.getCountryCode();
            } else {
                addressString = addressString + ", " + address.getCountryCode();
            }
        }

        if (addressString != null) {
            return addressString;
        } else {
            if (address.getMaxAddressLineIndex() != 0) {
                addressString = address.getAddressLine(1);
                return addressString;
            }
            return NO_ADDRESS_NAME_FOUND;
        }
    }

    private void setupWeatherLayout() {
        mWeatherLinearLayout = (LinearLayout) findViewById(R.id.weather_linear_layout);
        mWeatherLayoutProgressBar = (ProgressBar) findViewById(R.id.weather_layout_progress_bar);
        mWeatherLinearLayout.setVisibility(View.GONE);
        mWeatherStatusOrAddressTextView = (TextView) findViewById(R.id.status_or_address_text_view);
    }

    private void crossFade() {
        mWeatherLinearLayout.setAlpha(0f);
        mWeatherLinearLayout.setVisibility(View.VISIBLE);

        int shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        mWeatherLinearLayout.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        mWeatherLayoutProgressBar.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mWeatherLayoutProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void getWeatherDataBasedOnLatLng() {
        mGetWeatherRequest = new GetWeatherRequest(this);
        mGetWeatherRequest.getWeatherData(mLatLngSuggestedByGoogleMaps);
    }

    public void setWeatherData(Weather weather) {
        String latitude = weather.coordinates.mLatitude.toString();
        String longitude = weather.coordinates.mLongitude.toString();

        String weatherMain = weather.weatherInner.get(0).mWeatherMain;
        String weatherDescription = weather.weatherInner.get(0).mWeatherDescription;
        String windSpeed = getResources().getString(R.string.wind_speed) + ": " + weather.wind.mWindSpeed.toString() + " km/h";

        String temperature = getResources().getString(R.string.temperature) + ": " + weather.main.mTemperature.toString() + " °C";
        String pressure = getResources().getString(R.string.pressure) + ": " + weather.main.mPressure.toString() + " hPa";
        String humidity = getResources().getString(R.string.humidity) + ": " + weather.main.mHumidity.toString() + "%";

        // Time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());

        calendar.setTimeInMillis(weather.sys.mSunrise * 1000);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String sunRise = getResources().getString(R.string.sunrise) + ": " + hour + ":" + minute + " ";

        calendar.setTimeInMillis(weather.sys.mSunset * 1000);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        String sunSet = getResources().getString(R.string.sunset) + ": " + hour + ":" + minute + " ";

        mWeatherStatusOrAddressTextView.setText(mLocationAddressSuggestedByGoogleMaps + " (" + latitude + ", " + longitude + ")");

        TextView weatherCenterNameDescriptionWind = (TextView) findViewById(R.id.weather_center_name_description_wind);
        weatherCenterNameDescriptionWind.setText(weatherMain + "\n" + weatherDescription + "\n" + windSpeed);

        TextView weatherCenterTemperaturePressureHumidity = (TextView) findViewById(R.id.weather_center_temperature_pressure_humidity);
        weatherCenterTemperaturePressureHumidity.setText(temperature + "\n" + pressure + "\n" + humidity);

        TextView sunriseTextView = (TextView) findViewById(R.id.sunrise_text_view);
        sunriseTextView.setText(sunRise);

        TextView sunsetTextView = (TextView) findViewById(R.id.sunset_text_view);
        sunsetTextView.setText(sunSet);

        crossFade();
    }

}
