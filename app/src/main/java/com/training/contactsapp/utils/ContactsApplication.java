package com.training.contactsapp.utils;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;

public class ContactsApplication extends Application {
    private static Context sContext;
    private static String sPackageName;
    private static ContentResolver sContentResolver;

    public static Context getContext() {
        return sContext;
    }

    public static String getPackage() {
        return sPackageName;
    }

    public static ContentResolver getContentResolverForContacts() {
        return sContentResolver;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sPackageName = getPackageName();
        sContentResolver = getContentResolver();
    }
}
