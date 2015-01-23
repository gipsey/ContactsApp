package com.training.contactsapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.training.contactsapp.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidd on 1/14/15.
 */
public class UserDBImplementation {
    private static volatile UserDBImplementation sInstance;

    private UserDBHelper mUserDBHelper;
    private SQLiteDatabase mSQLiteDatabase;

    private UserDBImplementation() {
        mUserDBHelper = new UserDBHelper();
        mSQLiteDatabase = mUserDBHelper.getWritableDatabase();
        insertDefaultUsers();
    }

    public static UserDBImplementation getInstance() {
        if (sInstance == null) {
            synchronized (UserDBImplementation.class) {
                if (sInstance == null) {
                    sInstance = new UserDBImplementation();
                }
            }
        }
        return sInstance;
    }

    public List<User> queryUsers() {
        String[] columns = {mUserDBHelper.COLUMN_UID, mUserDBHelper.COLUMN_NAME,
                mUserDBHelper.COLUMN_PHONE_NUMBER, mUserDBHelper.COLUMN_EMAIL,
                mUserDBHelper.COLUMN_DOB, mUserDBHelper.COLUMN_ADDRESS,
                mUserDBHelper.COLUMN_WEBSITE};

        Cursor cursor = mSQLiteDatabase.query(mUserDBHelper.TABLE_NAME, columns, null, null, null,
                null, null);
        List<User> users = new ArrayList<User>();

        while (cursor.moveToNext()) {
            User user = new User();
            user.setUid(cursor.getInt(cursor.getColumnIndex(mUserDBHelper.COLUMN_UID)));
            user.setName(cursor.getString(cursor.getColumnIndex(mUserDBHelper.COLUMN_NAME)));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(mUserDBHelper.COLUMN_PHONE_NUMBER)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(mUserDBHelper.COLUMN_EMAIL)));
            user.setDob(cursor.getString(cursor.getColumnIndex(mUserDBHelper.COLUMN_DOB)));
            user.setAddress(cursor.getString(cursor.getColumnIndex(mUserDBHelper.COLUMN_ADDRESS)));
            user.setWebsite(cursor.getString(cursor.getColumnIndex(mUserDBHelper.COLUMN_WEBSITE)));
            users.add(user);
        }
        return users;
    }

    public User queryUserByUID(int id) {
        String[] columns = {mUserDBHelper.COLUMN_UID, mUserDBHelper.COLUMN_NAME,
                mUserDBHelper.COLUMN_PHONE_NUMBER, mUserDBHelper.COLUMN_EMAIL,
                mUserDBHelper.COLUMN_DOB, mUserDBHelper.COLUMN_ADDRESS,
                mUserDBHelper.COLUMN_WEBSITE};
        String[] args = {String.valueOf(id)};

        Cursor cursor = mSQLiteDatabase.query(mUserDBHelper.TABLE_NAME, columns,
                mUserDBHelper.COLUMN_UID + "=?", args, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        return new User(
                cursor.getInt(cursor.getColumnIndex(mUserDBHelper.COLUMN_UID)),
                cursor.getString(cursor.getColumnIndex(mUserDBHelper.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(mUserDBHelper.COLUMN_PHONE_NUMBER)),
                cursor.getString(cursor.getColumnIndex(mUserDBHelper.COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndex(mUserDBHelper.COLUMN_DOB)),
                cursor.getString(cursor.getColumnIndex(mUserDBHelper.COLUMN_ADDRESS)),
                cursor.getString(cursor.getColumnIndex(mUserDBHelper.COLUMN_WEBSITE)));
    }

    public ArrayList<User> queryUsersUIDAndNameAndPhoneNumber() {
        String[] columns = {mUserDBHelper.COLUMN_UID, mUserDBHelper.COLUMN_NAME,
                mUserDBHelper.COLUMN_PHONE_NUMBER};

        Cursor cursor = mSQLiteDatabase.query(mUserDBHelper.TABLE_NAME, columns, null, null, null,
                null, mUserDBHelper.COLUMN_NAME +
                        " COLLATE NOCASE ASC");
        ArrayList<User> users = new ArrayList<User>();

        while (cursor.moveToNext()) {
            User user = new User();
            user.setUid(cursor.getInt(cursor.getColumnIndex(mUserDBHelper.COLUMN_UID)));
            user.setName(cursor.getString(cursor.getColumnIndex(mUserDBHelper.COLUMN_NAME)));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(mUserDBHelper.COLUMN_PHONE_NUMBER)));
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

        return mSQLiteDatabase.insert(UserDBHelper.TABLE_NAME, null, contentValues);
    }

    public long updateUser(User user) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(UserDBHelper.COLUMN_NAME, user.getName());
        contentValues.put(UserDBHelper.COLUMN_PHONE_NUMBER, user.getPhoneNumber());
        contentValues.put(UserDBHelper.COLUMN_EMAIL, user.getEmail());
        contentValues.put(UserDBHelper.COLUMN_DOB, user.getDob());
        contentValues.put(UserDBHelper.COLUMN_ADDRESS, user.getAddress());
        contentValues.put(UserDBHelper.COLUMN_WEBSITE, user.getWebsite());

        return mSQLiteDatabase.update(mUserDBHelper.TABLE_NAME, contentValues,
                mUserDBHelper.COLUMN_UID + "=" + user.getUid(), null);
    }

    public long deleteUserByUID(int id) {
        return mSQLiteDatabase.delete(mUserDBHelper.TABLE_NAME, mUserDBHelper.COLUMN_UID + "=" +
                id, null);
    }

    public long deleteUsers() {
        return mSQLiteDatabase.delete(UserDBHelper.TABLE_NAME, null, null);
    }

    private void insertDefaultUsers() {
        long re = deleteUsers();
        insertUser(new User(-1, "Mihu Cosmin", "0754919860", "cosmin.mihu@gmail.com", "1992-10-27", "Nr. 45, Str. B.P.Hasdeu, 400371, Cluj-Napoca, Jud. Cluj, Romania", "http://www.cosminmihu.info/"));
        insertUser(new User(-1, "Debre Elizabeth", "0735507173", "eliz_debre@yahoo.com", "1999-01-02", "address2", "https://www.facebook.com/debre.elizabeth"));
        insertUser(new User(-1, "gipsey", "0735502246", "debredavid@yahoo.com", "1992-08-15", "Perecsenyi Magyar Baptista Imaház", "http://www.gipsey.tk"));
        insertUser(new User(-1, "Fortech", "+40 264 438217", "office@fortech.ro", "2003-12-01", "Str. Frunzisului nr.106 400664 Cluj-Napoca", "http://www.fortech.ro/"));
        insertUser(new User(-1, "fortech1", "+40 264 453303", "office@fortech.ro", "1999-01-02", "Fortech 15-17, Strada Meteor, Cluj-Napoca 400000", "http://www.fortech.ro/"));
        insertUser(new User(-1, "Jakab Hunor", "+40747253683", "jakabh@cs.ubbcluj.ro", "1985-04-19", "str. Rozelor nr. 62 547535 Sangeorgiu de Padure Mures, Romania", "http://www.cs.ubbcluj.ro/~jakabh/"));
        insertUser(new User(-1, "Name4", "004", "name4@yahoo.com", "1999-01-04", "address4", "http://www.gipsey.tk/4"));
        insertUser(new User(-1, "Name5", "915673282", "name5@yahoo.com", "1999-01-05", "address5", "http://www.gipsey.tk/5"));
        insertUser(new User(-1, "Name6", "006", "name6@yahoo.com", "1999-01-06", "address6", "http://www.gipsey.tk/6"));
        insertUser(new User(-1, "Name7", "007", "name7@yahoo.com", "1999-01-07", "address7", "http://www.gipsey.tk/7"));
        insertUser(new User(-1, "Name8", "008", "name8@yahoo.com", "1999-01-08", "address8", "http://www.gipsey.tk/8"));
        insertUser(new User(-1, "Name9", "009", "name9@yahoo.com", "1999-9-9", "address9", "http://www.gipsey.tk/9"));
        insertUser(new User(-1, "Name10", "0010", "name10@yahoo.com", "1999-10-10", "address10", "http://www.gipsey.tk/10"));
    }

}
