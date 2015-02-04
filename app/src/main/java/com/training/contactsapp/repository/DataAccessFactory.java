package com.training.contactsapp.repository;

import com.training.contactsapp.repository.file.FileDataAccessFactory;
import com.training.contactsapp.repository.sqLite.SqLiteDataAccessFactory;

/**
 * Created by davidd on 1/29/15.
 */
public abstract class DataAccessFactory {
    private static volatile DataAccessFactory sDataAccessFactoryInstance = null;

    public static DataAccessFactory getInstance() {
        if (sDataAccessFactoryInstance == null) {
            synchronized (DataAccessFactory.class) {
                if (sDataAccessFactoryInstance == null) {
                    sDataAccessFactoryInstance = new SqLiteDataAccessFactory();
//                    sDataAccessFactoryInstance = new FileDataAccessFactory();
                }
            }
        }
        return sDataAccessFactoryInstance;
    }

    public abstract UserDataAccess getUserDataAccess();

}
