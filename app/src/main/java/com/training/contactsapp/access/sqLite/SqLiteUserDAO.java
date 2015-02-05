package com.training.contactsapp.access.sqLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.training.contactsapp.access.MockDataManager;
import com.training.contactsapp.access.UserDAO;
import com.training.contactsapp.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidd on 1/14/15.
 */
class SqLiteUserDAO implements UserDAO {
    private static volatile SqLiteUserDAO sInstance;
    private static String[] sUserColumns;
    private SqLiteUserDAOHelper mSqLiteUserDAOHelper;
    private SQLiteDatabase mSqLiteDatabase;

    private SqLiteUserDAO() {
        sUserColumns = new String[]{mSqLiteUserDAOHelper.COLUMN_UID, mSqLiteUserDAOHelper.COLUMN_NAME,
                mSqLiteUserDAOHelper.COLUMN_PHONE_NUMBER, mSqLiteUserDAOHelper.COLUMN_EMAIL,
                mSqLiteUserDAOHelper.COLUMN_DOB, mSqLiteUserDAOHelper.COLUMN_ADDRESS,
                mSqLiteUserDAOHelper.COLUMN_WEBSITE, mSqLiteUserDAOHelper.COLUMN_AVATAR};
        mSqLiteUserDAOHelper = new SqLiteUserDAOHelper();
        mSqLiteDatabase = mSqLiteUserDAOHelper.getWritableDatabase();

        if (isUserTableEmpty()) {
            Log.i(getClass().getName(), "Default users will be inserted, because the user table is empty");
            MockDataManager.insertDefaultUsers(this);
        }
    }

    synchronized public static UserDAO getInstance() {
        if (sInstance == null) {
            sInstance = new SqLiteUserDAO();
        }
        return sInstance;
    }

    private boolean isUserTableEmpty() {
        Cursor cursor = mSqLiteDatabase.rawQuery("SELECT COUNT(*) FROM " + mSqLiteUserDAOHelper.TABLE_NAME, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor.getInt(0) == 0;
        } else {
            Log.i(getClass().getName(), "Cannot check if 'User' table is empty or not, because cursor is null");
            return true;
        }
    }

    public List<User> getUsers() {
        Cursor cursor = mSqLiteDatabase.query(mSqLiteUserDAOHelper.TABLE_NAME, sUserColumns, null, null, null,
                null, null);
        List<User> users = new ArrayList<User>();

        while (cursor.moveToNext()) {
            User user = new User();
            user.setUid(cursor.getInt(cursor.getColumnIndex(mSqLiteUserDAOHelper.COLUMN_UID)));
            user.setName(cursor.getString(cursor.getColumnIndex(mSqLiteUserDAOHelper.COLUMN_NAME)));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(mSqLiteUserDAOHelper.COLUMN_PHONE_NUMBER)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(mSqLiteUserDAOHelper.COLUMN_EMAIL)));
            user.setDob(cursor.getString(cursor.getColumnIndex(mSqLiteUserDAOHelper.COLUMN_DOB)));
            user.setAddress(cursor.getString(cursor.getColumnIndex(mSqLiteUserDAOHelper.COLUMN_ADDRESS)));
            user.setWebsite(cursor.getString(cursor.getColumnIndex(mSqLiteUserDAOHelper.COLUMN_WEBSITE)));
            user.setAvatar(cursor.getBlob(cursor.getColumnIndex(mSqLiteUserDAOHelper.COLUMN_AVATAR)));
            users.add(user);
        }
        return users;
    }

    public User getUserByUid(long uid) {
        String[] args = {String.valueOf(uid)};

        Cursor cursor = mSqLiteDatabase.query(mSqLiteUserDAOHelper.TABLE_NAME, sUserColumns,
                mSqLiteUserDAOHelper.COLUMN_UID + "=?", args, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        return new User(
                cursor.getInt(cursor.getColumnIndex(mSqLiteUserDAOHelper.COLUMN_UID)),
                cursor.getString(cursor.getColumnIndex(mSqLiteUserDAOHelper.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(mSqLiteUserDAOHelper.COLUMN_PHONE_NUMBER)),
                cursor.getString(cursor.getColumnIndex(mSqLiteUserDAOHelper.COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndex(mSqLiteUserDAOHelper.COLUMN_DOB)),
                cursor.getString(cursor.getColumnIndex(mSqLiteUserDAOHelper.COLUMN_ADDRESS)),
                cursor.getString(cursor.getColumnIndex(mSqLiteUserDAOHelper.COLUMN_WEBSITE)),
                cursor.getBlob(cursor.getColumnIndex(mSqLiteUserDAOHelper.COLUMN_AVATAR))
        );
    }

    public List<User> getUsersUidNamePhoneNumberAvatar() {
        String[] columns = {mSqLiteUserDAOHelper.COLUMN_UID, mSqLiteUserDAOHelper.COLUMN_NAME,
                mSqLiteUserDAOHelper.COLUMN_PHONE_NUMBER, mSqLiteUserDAOHelper.COLUMN_AVATAR};

        Cursor cursor = mSqLiteDatabase.query(mSqLiteUserDAOHelper.TABLE_NAME, columns, null, null, null,
                null, mSqLiteUserDAOHelper.COLUMN_NAME +
                        " COLLATE NOCASE ASC");
        List<User> users = new ArrayList<User>();

        while (cursor.moveToNext()) {
            User user = new User();
            user.setUid(cursor.getInt(cursor.getColumnIndex(mSqLiteUserDAOHelper.COLUMN_UID)));
            user.setName(cursor.getString(cursor.getColumnIndex(mSqLiteUserDAOHelper.COLUMN_NAME)));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(mSqLiteUserDAOHelper.COLUMN_PHONE_NUMBER)));
            user.setAvatar(cursor.getBlob(cursor.getColumnIndex(mSqLiteUserDAOHelper.COLUMN_AVATAR)));
            users.add(user);
        }
        return users;
    }

    public long insertUser(User user) {
        ContentValues contentValues = new ContentValues();

        if (user.getUid() != -1) {
            contentValues.put(SqLiteUserDAOHelper.COLUMN_UID, user.getUid());
        }
        contentValues.put(SqLiteUserDAOHelper.COLUMN_NAME, user.getName());
        contentValues.put(SqLiteUserDAOHelper.COLUMN_PHONE_NUMBER, user.getPhoneNumber());
        contentValues.put(SqLiteUserDAOHelper.COLUMN_EMAIL, user.getEmail());
        contentValues.put(SqLiteUserDAOHelper.COLUMN_DOB, user.getDob());
        contentValues.put(SqLiteUserDAOHelper.COLUMN_ADDRESS, user.getAddress());
        contentValues.put(SqLiteUserDAOHelper.COLUMN_WEBSITE, user.getWebsite());
        contentValues.put(SqLiteUserDAOHelper.COLUMN_AVATAR, user.getAvatar());

        return mSqLiteDatabase.insert(SqLiteUserDAOHelper.TABLE_NAME, null, contentValues);
    }

    public long updateUser(User user) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(SqLiteUserDAOHelper.COLUMN_NAME, user.getName());
        contentValues.put(SqLiteUserDAOHelper.COLUMN_PHONE_NUMBER, user.getPhoneNumber());
        contentValues.put(SqLiteUserDAOHelper.COLUMN_EMAIL, user.getEmail());
        contentValues.put(SqLiteUserDAOHelper.COLUMN_DOB, user.getDob());
        contentValues.put(SqLiteUserDAOHelper.COLUMN_ADDRESS, user.getAddress());
        contentValues.put(SqLiteUserDAOHelper.COLUMN_WEBSITE, user.getWebsite());
        contentValues.put(SqLiteUserDAOHelper.COLUMN_AVATAR, user.getAvatar());

        return mSqLiteDatabase.update(mSqLiteUserDAOHelper.TABLE_NAME, contentValues,
                mSqLiteUserDAOHelper.COLUMN_UID + "=" + user.getUid(), null);
    }

    public long deleteUsers() {
        return mSqLiteDatabase.delete(SqLiteUserDAOHelper.TABLE_NAME, null, null);
    }

    public long deleteUserByUid(long id) {
        return mSqLiteDatabase.delete(
                mSqLiteUserDAOHelper.TABLE_NAME,
                mSqLiteUserDAOHelper.COLUMN_UID + "=" + id,
                null);
    }

}
