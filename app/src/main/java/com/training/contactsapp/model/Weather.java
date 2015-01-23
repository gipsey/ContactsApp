package com.training.contactsapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by davidd on 1/23/15.
 */
public class Weather {

//{"coord":{"lon":139.72,"lat":35.8},"sys":{"message":0.2204,"country":"JP","sunrise":1421963234,"sunset":1421999915},
// "weather":[{"id":802,"main":"Clouds","description":"scattered clouds","icon":"03n"}],"base":"cmc stations",
// "main":{"temp":4.359,"temp_min":4.359,"temp_max":4.359,"pressure":1024.44,"sea_level":1029.55,"grnd_level":1024.44,"humidity":100},
// "wind":{"speed":6.61,"deg":353.503},"clouds":{"all":32},"dt":1422022080,"id":1864231,"name":"Egotamachi","cod":200}

    @SerializedName("coord")
    public Coordinates coordinates;
    public Sys sys;
    @SerializedName("weather")
    public List<WeatherInner> weatherInner;
    public Main main;
    public Wind wind;

    @Override
    public String toString() {
        return "Weather{" +
                "coordinates=" + coordinates +
                ", sys=" + sys +
                ", weatherInner=" + weatherInner +
                ", main=" + main +
                ", wind=" + wind +
                '}';
    }

    public class Coordinates {
        @SerializedName("lon")
        public Float mLongitude;
        @SerializedName("lat")
        public Float mLatitude;

        @Override
        public String toString() {
            return "Coordinates{" +
                    "mLongitude=" + mLongitude +
                    ", mLatitude=" + mLatitude +
                    '}';
        }
    }

    public class Sys {
        @SerializedName("sunrise")
        public Long mSunrise;
        @SerializedName("sunset")
        public Long mSunset;

        @Override
        public String toString() {
            return "Sys{" +
                    "mSunrise='" + mSunrise + '\'' +
                    ", mSunset='" + mSunset + '\'' +
                    '}';
        }
    }

    public class WeatherInner {
        @SerializedName("main")
        public String mWeatherMain;
        @SerializedName("description")
        public String mWeatherDescription;

        @Override
        public String toString() {
            return "WeatherInner{" +
                    "mWeatherMain='" + mWeatherMain + '\'' +
                    ", mWeatherDescription='" + mWeatherDescription + '\'' +
                    '}';
        }
    }

    public class Main {
        @SerializedName("temp")
        public Float mTemperature;
        @SerializedName("pressure")
        public Float mPressure;
        @SerializedName("humidity")
        public Float mHumidity;

        @Override
        public String toString() {
            return "Main{" +
                    "mTemperature=" + mTemperature +
                    ", mPressure=" + mPressure +
                    ", mHumidity=" + mHumidity +
                    '}';
        }
    }

    public class Wind {
        @SerializedName("speed")
        public Float mWindSpeed;

        @Override
        public String toString() {
            return "Wind{" +
                    "mWindSpeed=" + mWindSpeed +
                    '}';
        }
    }
}
