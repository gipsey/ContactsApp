package com.training.contactsapp.repository.sqLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.training.contactsapp.model.User;
import com.training.contactsapp.repository.TemporaryRepositoryTasks;
import com.training.contactsapp.repository.UserDataAccess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidd on 1/14/15.
 */
class SqLiteUserDataAccess implements UserDataAccess {
    private final String[] ALL_USER_COLUMNS;
    private SqLiteUserDataAccessHelper mSqLiteUserDataAccessHelper;
    private SQLiteDatabase mSqLiteDatabase;

    public SqLiteUserDataAccess() {
        ALL_USER_COLUMNS = new String[]{mSqLiteUserDataAccessHelper.COLUMN_UID, mSqLiteUserDataAccessHelper.COLUMN_NAME,
                mSqLiteUserDataAccessHelper.COLUMN_PHONE_NUMBER, mSqLiteUserDataAccessHelper.COLUMN_EMAIL,
                mSqLiteUserDataAccessHelper.COLUMN_DOB, mSqLiteUserDataAccessHelper.COLUMN_ADDRESS,
                mSqLiteUserDataAccessHelper.COLUMN_WEBSITE, mSqLiteUserDataAccessHelper.COLUMN_AVATAR};
        mSqLiteUserDataAccessHelper = new SqLiteUserDataAccessHelper();
        mSqLiteDatabase = mSqLiteUserDataAccessHelper.getWritableDatabase();

        if (isUserTableEmpty()) {
            Log.i(getClass().getName(), "Default users will be inserted, because the user table is empty");
            TemporaryRepositoryTasks.insertDefaultUsers(this);
        }
    }

    private boolean isUserTableEmpty() {
        Cursor cursor = mSqLiteDatabase.rawQuery("SELECT COUNT(*) FROM " + mSqLiteUserDataAccessHelper.TABLE_NAME, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getInt(0) == 0) {
                return true;
            } else {
                return false;
            }
        } else {
            Log.i(getClass().getName(), "Cannot check if 'User' table is empty or not, because cursor is null");
            return true;
        }
    }

    public List<User> getUsers() {
        Cursor cursor = mSqLiteDatabase.query(mSqLiteUserDataAccessHelper.TABLE_NAME, ALL_USER_COLUMNS, null, null, null,
                null, null);
        List<User> users = new ArrayList<User>();

        while (cursor.moveToNext()) {
            User user = new User();
            user.setUid(cursor.getInt(cursor.getColumnIndex(mSqLiteUserDataAccessHelper.COLUMN_UID)));
            user.setName(cursor.getString(cursor.getColumnIndex(mSqLiteUserDataAccessHelper.COLUMN_NAME)));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(mSqLiteUserDataAccessHelper.COLUMN_PHONE_NUMBER)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(mSqLiteUserDataAccessHelper.COLUMN_EMAIL)));
            user.setDob(cursor.getString(cursor.getColumnIndex(mSqLiteUserDataAccessHelper.COLUMN_DOB)));
            user.setAddress(cursor.getString(cursor.getColumnIndex(mSqLiteUserDataAccessHelper.COLUMN_ADDRESS)));
            user.setWebsite(cursor.getString(cursor.getColumnIndex(mSqLiteUserDataAccessHelper.COLUMN_WEBSITE)));
            user.setAvatar(cursor.getBlob(cursor.getColumnIndex(mSqLiteUserDataAccessHelper.COLUMN_AVATAR)));
            users.add(user);
        }
        return users;
    }

    public User getUserByUid(long uid) {
        String[] args = {String.valueOf(uid)};

        Cursor cursor = mSqLiteDatabase.query(mSqLiteUserDataAccessHelper.TABLE_NAME, ALL_USER_COLUMNS,
                mSqLiteUserDataAccessHelper.COLUMN_UID + "=?", args, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        return new User(
                cursor.getInt(cursor.getColumnIndex(mSqLiteUserDataAccessHelper.COLUMN_UID)),
                cursor.getString(cursor.getColumnIndex(mSqLiteUserDataAccessHelper.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(mSqLiteUserDataAccessHelper.COLUMN_PHONE_NUMBER)),
                cursor.getString(cursor.getColumnIndex(mSqLiteUserDataAccessHelper.COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndex(mSqLiteUserDataAccessHelper.COLUMN_DOB)),
                cursor.getString(cursor.getColumnIndex(mSqLiteUserDataAccessHelper.COLUMN_ADDRESS)),
                cursor.getString(cursor.getColumnIndex(mSqLiteUserDataAccessHelper.COLUMN_WEBSITE)),
                cursor.getBlob(cursor.getColumnIndex(mSqLiteUserDataAccessHelper.COLUMN_AVATAR))
        );
    }

    public List<User> getUsersUidNamePhoneNumberAvatar() {
        String[] columns = {mSqLiteUserDataAccessHelper.COLUMN_UID, mSqLiteUserDataAccessHelper.COLUMN_NAME,
                mSqLiteUserDataAccessHelper.COLUMN_PHONE_NUMBER, mSqLiteUserDataAccessHelper.COLUMN_AVATAR};

        Cursor cursor = mSqLiteDatabase.query(mSqLiteUserDataAccessHelper.TABLE_NAME, columns, null, null, null,
                null, mSqLiteUserDataAccessHelper.COLUMN_NAME +
                        " COLLATE NOCASE ASC");
        List<User> users = new ArrayList<User>();

        while (cursor.moveToNext()) {
            User user = new User();
            user.setUid(cursor.getInt(cursor.getColumnIndex(mSqLiteUserDataAccessHelper.COLUMN_UID)));
            user.setName(cursor.getString(cursor.getColumnIndex(mSqLiteUserDataAccessHelper.COLUMN_NAME)));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(mSqLiteUserDataAccessHelper.COLUMN_PHONE_NUMBER)));
            user.setAvatar(cursor.getBlob(cursor.getColumnIndex(mSqLiteUserDataAccessHelper.COLUMN_AVATAR)));
            users.add(user);
        }
        return users;
    }

    public long insertUser(User user) {
        ContentValues contentValues = new ContentValues();

        if (user.getUid() != -1) {
            contentValues.put(SqLiteUserDataAccessHelper.COLUMN_UID, user.getUid());
        }
        contentValues.put(SqLiteUserDataAccessHelper.COLUMN_NAME, user.getName());
        contentValues.put(SqLiteUserDataAccessHelper.COLUMN_PHONE_NUMBER, user.getPhoneNumber());
        contentValues.put(SqLiteUserDataAccessHelper.COLUMN_EMAIL, user.getEmail());
        contentValues.put(SqLiteUserDataAccessHelper.COLUMN_DOB, user.getDob());
        contentValues.put(SqLiteUserDataAccessHelper.COLUMN_ADDRESS, user.getAddress());
        contentValues.put(SqLiteUserDataAccessHelper.COLUMN_WEBSITE, user.getWebsite());
        contentValues.put(SqLiteUserDataAccessHelper.COLUMN_AVATAR, user.getAvatar());

        return mSqLiteDatabase.insert(SqLiteUserDataAccessHelper.TABLE_NAME, null, contentValues);
    }

    public long updateUser(User user) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(SqLiteUserDataAccessHelper.COLUMN_NAME, user.getName());
        contentValues.put(SqLiteUserDataAccessHelper.COLUMN_PHONE_NUMBER, user.getPhoneNumber());
        contentValues.put(SqLiteUserDataAccessHelper.COLUMN_EMAIL, user.getEmail());
        contentValues.put(SqLiteUserDataAccessHelper.COLUMN_DOB, user.getDob());
        contentValues.put(SqLiteUserDataAccessHelper.COLUMN_ADDRESS, user.getAddress());
        contentValues.put(SqLiteUserDataAccessHelper.COLUMN_WEBSITE, user.getWebsite());
        contentValues.put(SqLiteUserDataAccessHelper.COLUMN_AVATAR, user.getAvatar());

        return mSqLiteDatabase.update(mSqLiteUserDataAccessHelper.TABLE_NAME, contentValues,
                mSqLiteUserDataAccessHelper.COLUMN_UID + "=" + user.getUid(), null);
    }

    public long deleteUsers() {
        return mSqLiteDatabase.delete(SqLiteUserDataAccessHelper.TABLE_NAME, null, null);
    }

    public long deleteUserByUid(long id) {
        return mSqLiteDatabase.delete(mSqLiteUserDataAccessHelper.TABLE_NAME, mSqLiteUserDataAccessHelper.COLUMN_UID + "=" +
                id, null);
    }

}
