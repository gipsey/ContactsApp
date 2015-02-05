package com.training.contactsapp.access.file;

import com.training.contactsapp.access.AbstractDAOFactory;
import com.training.contactsapp.access.UserDAO;

public class FileDAOFactory extends AbstractDAOFactory {

    @Override
    public UserDAO getUserDataAccess() {
        return FileUserDAO.getInstance();
    }

}
