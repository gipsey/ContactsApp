package com.training.contactsapp.repository;

import com.training.contactsapp.repository.file.FileDataAccesFactory;

/**
 * Created by davidd on 1/29/15.
 */
public abstract class DataAccessFactory {
    private static volatile DataAccessFactory sDataAccessFactoryInstance = null;

    public static DataAccessFactory getInstance() {
        if (sDataAccessFactoryInstance == null) {
            synchronized (DataAccessFactory.class) {
                if (sDataAccessFactoryInstance == null) {
//                    sDataAccessFactoryInstance = new SqLiteDataAccessFactory();
                    sDataAccessFactoryInstance = new FileDataAccesFactory();
                }
            }
        }
        return sDataAccessFactoryInstance;
    }

    public abstract UserDataAccess getUserDataAccess();

}
