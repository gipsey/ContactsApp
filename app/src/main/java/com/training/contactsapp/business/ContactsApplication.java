package com.training.contactsapp.business;

import android.app.Application;
import android.content.Context;

/**
 * Created by davidd on 1/22/15.
 */
public class ContactsApplication extends Application {
    private static Context sContext;
    private static String sPackageName;

    public static Context getContext() {
        return sContext;
    }

    public static String getPackage() {
        return sPackageName;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sPackageName = getPackageName();
    }

}
