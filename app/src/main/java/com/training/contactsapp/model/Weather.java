package com.training.contactsapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by davidd on 1/23/15.
 */
public class Weather {
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
