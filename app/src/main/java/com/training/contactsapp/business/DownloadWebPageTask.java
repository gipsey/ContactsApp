package com.training.contactsapp.business;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by davidd on 1/23/15.
 */
public class DownloadWebPageTask extends AsyncTask<String, Void, String> {
    private Data data;

    public DownloadWebPageTask(NetworkConnectionToGetWeather networkConnectionToGetWeather) {
        this.data = (Data) networkConnectionToGetWeather;
    }

    @Override
    protected String doInBackground(String... params) {
        if (params.length == 0) return null;

        try {
            return downloadUrl(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String downloadUrl(String param) throws IOException {
        InputStream inputStream = null;

        try {
            URL url = new URL(param);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);

            httpURLConnection.connect();
            int response = httpURLConnection.getResponseCode();
            Log.d(getClass().getName(), "The response code is " + response);
            inputStream = httpURLConnection.getInputStream();

            String result = readIt(inputStream);
            return result;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private String readIt(InputStream inputStream) throws IOException {
        Reader reader = new InputStreamReader(inputStream, "UTF-8");
        String result = "";

        int oneCharacter = reader.read();
        int i = 0;
        while (oneCharacter != -1) {
            result += (char) oneCharacter;
            oneCharacter = reader.read();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        data.onDataIsReady(s);
    }

    // review: rename
    public interface Data {
        public void onDataIsReady(String data);
    }

}
