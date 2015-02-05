package com.training.contactsapp.business;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.training.contactsapp.model.Weather;
import com.training.contactsapp.view.activities.MapAndWeatherActivity;

/**
 * Created by davidd on 1/23/15.
 */
// review: get weather request 
public class NetworkConnectionToGetWeather implements DownloadWebPageTask.Data {
    private static final String URL_ADDRESS = "http://api.openweathermap.org/data/2.5/weather";
    private static final String URL_PARAMETER_LAT = "lat";
    private static final String URL_PARAMETER_LONG = "lon";
    private static final String URL_UNITS_METRIC = "units=metric";
    private static Context sContext;
    private MapAndWeatherActivity activity;
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo mNetworkInfo;

    public NetworkConnectionToGetWeather(MapAndWeatherActivity activity) {
        this.activity = activity;
        sContext = ContactsApplication.getContext();
        mConnectivityManager = (ConnectivityManager) sContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public void getWeatherData(LatLng latLngToBeSearched) {
        String url = null;
//review: create a constant + use String.format 
        url = URL_ADDRESS + "?" +
                URL_PARAMETER_LAT + "=" + latLngToBeSearched.latitude + "&" +
                URL_PARAMETER_LONG + "=" + latLngToBeSearched.longitude + "&" +
                URL_UNITS_METRIC;

        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null && mNetworkInfo.isConnected()) {
            DownloadWebPageTask downloadWebPageTask = new DownloadWebPageTask(this);
            downloadWebPageTask.execute(url);
        }
    }

    @Override
    // review: onRequestCompleted
    public void onDataIsReady(String jsonData) {
        Log.i(getClass().getName() + " DATA ARRIVED ", jsonData);
        parseJson(jsonData);

    }

    private void parseJson(String jsonData) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Weather weather = gson.fromJson(jsonData, Weather.class);
        activity.setWeatherData(weather);
        Log.i(getClass().getName() + " DATA PARSED ", weather.toString());
    }
}
