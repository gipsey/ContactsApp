package com.training.contactsapp.business;

import android.app.Application;
import android.content.Context;

/**
 * Created by davidd on 1/22/15.
 */
public class ContactsApplication extends Application {
    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

}
