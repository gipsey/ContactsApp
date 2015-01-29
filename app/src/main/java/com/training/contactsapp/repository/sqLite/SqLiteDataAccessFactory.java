package com.training.contactsapp.repository.sqLite;

import com.training.contactsapp.repository.DataAccessFactory;
import com.training.contactsapp.repository.UserDataAccess;

/**
 * Created by davidd on 1/29/15.
 */
public class SqLiteDataAccessFactory extends DataAccessFactory {
    private static volatile UserDataAccess sUserDataAccessInstance = null;

    @Override
    public UserDataAccess getUserDataAccess() {
        if (sUserDataAccessInstance == null) {
            synchronized (SqLiteDataAccessFactory.class) {
                if (sUserDataAccessInstance == null) {
                    sUserDataAccessInstance = new SqLiteUserDataAccess();
                }
            }
        }
        return sUserDataAccessInstance;
    }


}
