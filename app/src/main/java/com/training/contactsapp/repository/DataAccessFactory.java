package com.training.contactsapp.repository;

import com.training.contactsapp.repository.sqLite.SqLiteDataAccessFactory;

/**
 * Created by davidd on 1/29/15.
 */
// review: AbstractDataAccessFactory
public abstract class DataAccessFactory {
    private static volatile DataAccessFactory sInstance = null;

// review: this is not singleton
    public static DataAccessFactory getInstance() {
        if (sInstance == null) {
            synchronized (DataAccessFactory.class) {
                if (sInstance == null) {
                    sInstance = new SqLiteDataAccessFactory();
//                    sInstance = new FileDataAccessFactory();
                }
            }
        }
        return sInstance;
    }

    public abstract UserDataAccess getUserDataAccess();

}
