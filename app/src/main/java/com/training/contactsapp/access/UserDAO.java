package com.training.contactsapp.access;

import com.training.contactsapp.model.User;

import java.util.List;

public interface UserDAO {
    public List<User> getUsers();

    public User getUserByUid(long uid);

    public List<User> getUsersUidNamePhoneNumberAvatar();

    public long insertUser(User user);

    public long updateUser(User user);

    public long deleteUsers();

    public long deleteUserByUid(long id);
}
