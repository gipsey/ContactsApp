package com.training.contactsapp.access.sqLite;

import com.training.contactsapp.access.AbstractDAOFactory;
import com.training.contactsapp.access.UserDAO;

/**
 * Created by davidd on 1/29/15.
 */
public class SqLiteDAOFactory extends AbstractDAOFactory {

    @Override
    public UserDAO getUserDAO() {
        return SqLiteUserDAO.getInstance();
    }

}
