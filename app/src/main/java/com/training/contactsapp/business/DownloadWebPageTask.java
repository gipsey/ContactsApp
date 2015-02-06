package com.training.contactsapp.business;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadWebPageTask extends AsyncTask<String, Void, String> {
    private ResponseListener mResponseListener;

    /**
     * Constructor of the class. Initializes the {@code ResponseListener} interface.
     *
     * @param getWeatherRequest The {@code GetWeatherRequest} as a listener.
     */
    public DownloadWebPageTask(GetWeatherRequest getWeatherRequest) {
        this.mResponseListener = getWeatherRequest;
    }

    /**
     * Overrides the {@code doInBackground} method of the {@code AsyncTask} class. initializing the
     * download progress.
     *
     * @param params The {@code String} array for describing the download.
     * @return The result of the request that will be used by the {@code onPostExecute}.
     */
    @Override
    protected String doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        try {
            return downloadUrl(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates a {@code HttpURLConnection} object, and send the request.
     *
     * @param param Contains the url of the target.
     * @return The result of the request that will be used by the {@code onPostExecute}.
     * @throws IOException if an error occurs while opening the connection.
     */
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
            return readIt(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * Read in a {@code String} the content from the given {@code InputStream} object.
     *
     * @param inputStream Contains the content, that has to be read.
     * @return The read content.
     * @throws IOException if cannot read from {@code InputStream} object.
     */
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

    /**
     * Invoked when the {@code doInBackground} method finishes the task, and returns.
     *
     * @param s The result of the request.
     */
    @Override
    protected void onPostExecute(String s) {
        mResponseListener.onResponseIsReady(s);
    }

    /**
     * With the help of this listener the {@code Class} that started the request will be notified.
     */
    public interface ResponseListener {
        /**
         * Invoked when response is ready.
         *
         * @param response The response {@code String} for the request.
         */
        public void onResponseIsReady(String response);
    }

}
