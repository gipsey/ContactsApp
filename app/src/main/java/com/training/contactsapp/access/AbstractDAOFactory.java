package com.training.contactsapp.access;

import com.training.contactsapp.access.sqLite.SqLiteDAOFactory;

public abstract class AbstractDAOFactory {

    public static AbstractDAOFactory getInstance() {
        return new SqLiteDAOFactory();
//        return new FileDataAccessFactory();
    }

    public abstract UserDAO getUserDataAccess();

}
