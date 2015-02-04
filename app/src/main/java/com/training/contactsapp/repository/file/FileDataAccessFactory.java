package com.training.contactsapp.repository.file;

import com.training.contactsapp.repository.DataAccessFactory;
import com.training.contactsapp.repository.UserDataAccess;

/**
 * Created by davidd on 1/29/15.
 */
public class FileDataAccessFactory extends DataAccessFactory {
    private static volatile UserDataAccess sUserDataAccessInstance = null;

    @Override
    public UserDataAccess getUserDataAccess() {
        if (sUserDataAccessInstance == null) {
            synchronized (FileDataAccessFactory.class) {
                if (sUserDataAccessInstance == null) {
                    sUserDataAccessInstance = new FileUserDataAccess();
                }
            }
        }
        return sUserDataAccessInstance;
    }

}
