package com.training.contactsapp.repository;

import com.training.contactsapp.model.User;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidd on 1/29/15.
 */
public interface UserDataAccess {
    public List<User> getUsers();

    public User getUserByUid(long uid);

    public List<User> getUsersUidNamePhoneNumberAvatar();

    public long insertUser(User user);

    public long updateUser(User user);

    public long deleteUsers();

    public long deleteUserByUid(long id);

    public void insertDefaultUsers();
}
