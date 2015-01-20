package com.training.davidd.contactsapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.training.davidd.contactsapp.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidd on 1/14/15.
 */
public class UserDBImplementation {
    private static volatile UserDBImplementation instance;

    private UserDBHelper userDBHelper;
    private SQLiteDatabase sqLiteDatabase;


    private UserDBImplementation(Context context) {
        userDBHelper = new UserDBHelper(context);
        sqLiteDatabase = userDBHelper.getWritableDatabase();
    }

    public static UserDBImplementation getInstance(Context context) {
        if (instance == null) {
            synchronized (UserDBImplementation.class) {
                if (instance == null) {
                    instance = new UserDBImplementation(context);  // TODO: context can be null?
                }
            }
        }
        return instance;
    }

    public List<User> queryUsers() {
        String[] columns = {userDBHelper.COLUMN_UID, userDBHelper.COLUMN_NAME, userDBHelper.COLUMN_PHONE_NUMBER,
                userDBHelper.COLUMN_EMAIL, userDBHelper.COLUMN_DOB, userDBHelper.COLUMN_ADDRESS, userDBHelper.COLUMN_WEBSITE};

        Cursor cursor = sqLiteDatabase.query(userDBHelper.TABLE_NAME, columns, null, null, null, null, null);
        List<User> users = new ArrayList<User>();

        while (cursor.moveToNext()) {
            User user = new User();
            user.setUid(cursor.getInt(0));
            user.setName(cursor.getString(1));
            user.setPhone_number(cursor.getString(2));
            user.setEmail(cursor.getString(3));
            user.setDob(cursor.getString(4));
            user.setAddress(cursor.getString(5));
            user.setWebsite(cursor.getString(6));
            users.add(user);
        }
        return users;
    }

    public User queryUserByUID(int id) {
        String[] columns = {userDBHelper.COLUMN_UID, userDBHelper.COLUMN_NAME, userDBHelper.COLUMN_PHONE_NUMBER,
                userDBHelper.COLUMN_EMAIL, userDBHelper.COLUMN_DOB, userDBHelper.COLUMN_ADDRESS, userDBHelper.COLUMN_WEBSITE};
        String[] args = {String.valueOf(id)};

        Cursor cursor = sqLiteDatabase.query(userDBHelper.TABLE_NAME, columns, userDBHelper.COLUMN_UID + "=?", args, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        return new User(
                cursor.getInt(cursor.getColumnIndex(userDBHelper.COLUMN_UID)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6));
    }

    public ArrayList<User> queryUsersUIDAndNameAndPhoneNumber() {
        String[] columns = {userDBHelper.COLUMN_UID, userDBHelper.COLUMN_NAME, userDBHelper.COLUMN_PHONE_NUMBER};

        Cursor cursor = sqLiteDatabase.query(userDBHelper.TABLE_NAME, columns, null, null, null, null, userDBHelper.COLUMN_NAME + " COLLATE NOCASE ASC");
        ArrayList<User> users = new ArrayList<User>();

        while (cursor.moveToNext()) {
            User user = new User();
            user.setUid(cursor.getInt(0));
            user.setName(cursor.getString(1));
            user.setPhone_number(cursor.getString(2));
            users.add(user);
        }
        return users;
    }

    public long insertUser(User user) {
        ContentValues contentValues = new ContentValues();

        if (user.getUid() != -1) {
            contentValues.put(UserDBHelper.COLUMN_UID, user.getUid());
        }
        contentValues.put(UserDBHelper.COLUMN_NAME, user.getName());
        contentValues.put(UserDBHelper.COLUMN_PHONE_NUMBER, user.getPhoneNumber());
        contentValues.put(UserDBHelper.COLUMN_EMAIL, user.getEmail());
        contentValues.put(UserDBHelper.COLUMN_DOB, user.getDob());
        contentValues.put(UserDBHelper.COLUMN_ADDRESS, user.getAddress());
        contentValues.put(UserDBHelper.COLUMN_WEBSITE, user.getWebsite());

        long re = sqLiteDatabase.insert(UserDBHelper.TABLE_NAME, null, contentValues);
        return re;
    }

    public long updateUser(User user) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(UserDBHelper.COLUMN_NAME, user.getName());
        contentValues.put(UserDBHelper.COLUMN_PHONE_NUMBER, user.getPhoneNumber());
        contentValues.put(UserDBHelper.COLUMN_EMAIL, user.getEmail());
        contentValues.put(UserDBHelper.COLUMN_DOB, user.getDob());
        contentValues.put(UserDBHelper.COLUMN_ADDRESS, user.getAddress());
        contentValues.put(UserDBHelper.COLUMN_WEBSITE, user.getWebsite());

        long re = sqLiteDatabase.update(userDBHelper.TABLE_NAME, contentValues, userDBHelper.COLUMN_UID + "=" + user.getUid(), null);
        return re;
    }

    public long deleteUserByUID(int id) {
        long re = sqLiteDatabase.delete(userDBHelper.TABLE_NAME, userDBHelper.COLUMN_UID + "=" + id, null);
        return re;
    }

    public long deleteUsers() {
        return sqLiteDatabase.delete(UserDBHelper.TABLE_NAME, null, null);
    }


}
