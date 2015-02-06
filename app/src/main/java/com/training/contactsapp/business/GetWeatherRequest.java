package com.training.contactsapp.business;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.training.contactsapp.model.Weather;
import com.training.contactsapp.presentation.activity.MapAndWeatherActivity;
import com.training.contactsapp.utils.ContactsApplication;

public class GetWeatherRequest implements DownloadWebPageTask.ResponseListener {
    private final static String REQUEST_URL = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";
    private MapAndWeatherActivity activity;
    private ConnectivityManager mConnectivityManager;

    /**
     * Constructor that gets the {@code Activity} from where it was invoked.
     *
     * @param activity The {@code Activity} from where it was invoked this method.
     */
    public GetWeatherRequest(MapAndWeatherActivity activity) {
        this.activity = activity;
        Context sContext = ContactsApplication.getContext();
        mConnectivityManager = (ConnectivityManager) sContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * Sends a request for the created {@code DownloadWebPageTask} instance.
     *
     * @param latLngToBeSearched Information about the location that is needed for the
     *                           {@code DownloadWebPageTask}.
     */
    public void getWeatherData(LatLng latLngToBeSearched) {
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            DownloadWebPageTask downloadWebPageTask = new DownloadWebPageTask(this);
            downloadWebPageTask.execute(String.format(REQUEST_URL, latLngToBeSearched.latitude, latLngToBeSearched.longitude));
        }
    }

    /**
     * When the response is arrived, this method invoked and parses the data.
     *
     * @param response The response {@code String} that has format {@code JSON} and has o be parsed.
     */
    @Override
    public void onResponseIsReady(String response) {
        Log.i(getClass().getName(), "Weather data arrived. The response is: " + response);
        parseJson(response);
    }

    /**
     * Parses the given data as {@code String} (but assumes that has JSON format), and invokes one
     * of the {@code activity}'s method to respond.
     *
     * @param jsonData The {@code String} object that has to be parsed.
     */
    private void parseJson(String jsonData) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Weather weather = gson.fromJson(jsonData, Weather.class);
        activity.setWeatherData(weather);
        Log.i(getClass().getName(), "Weather data parsed. The parsed is: " + weather.toString());
    }

}
