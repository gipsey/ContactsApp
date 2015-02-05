package com.training.contactsapp.repository.file;

import com.training.contactsapp.repository.DataAccessFactory;
import com.training.contactsapp.repository.UserDataAccess;

/**
 * Created by davidd on 1/29/15.
 */
public class FileDataAccessFactory extends DataAccessFactory {
    //review: sInstance
    private static volatile UserDataAccess sUserDataAccessInstance = null;

    @Override
    //review: getUserDAO
    public UserDataAccess getUserDataAccess() {
        if (sUserDataAccessInstance == null) {
            sUserDataAccessInstance = new FileUserDataAccess();
        }
        return sUserDataAccessInstance;
    }

}
