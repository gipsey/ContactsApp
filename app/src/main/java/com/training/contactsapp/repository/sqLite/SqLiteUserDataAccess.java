package com.training.contactsapp.repository.sqLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.training.contactsapp.R;
import com.training.contactsapp.business.ContactsApplication;
import com.training.contactsapp.model.User;
import com.training.contactsapp.repository.UserDataAccess;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidd on 1/14/15.
 */
class SqLiteUserDataAccess implements UserDataAccess {
    private SqLiteUserDataAccessHelper mSqLiteUserDataAccessHelper;
    private SQLiteDatabase mSQLiteDatabase;
    private final String[] ALL_USER_COLUMNS = {mSqLiteUserDataAccessHelper.COLUMN_UID, mSqLiteUserDataAccessHelper.COLUMN_NAME,
            mSqLiteUserDataAccessHelper.COLUMN_PHONE_NUMBER, mSqLiteUserDataAccessHelper.COLUMN_EMAIL,
            mSqLiteUserDataAccessHelper.COLUMN_DOB, mSqLiteUserDataAccessHelper.COLUMN_ADDRESS,
            mSqLiteUserDataAccessHelper.COLUMN_WEBSITE, mSqLiteUserDataAccessHelper.COLUMN_AVATAR};

    public SqLiteUserDataAccess() {
        mSqLiteUserDataAccessHelper = new SqLiteUserDataAccessHelper();
        mSQLiteDatabase = mSqLiteUserDataAccessHelper.getWritableDatabase();

        insertDefaultUsers();
    }

    public List<User> getUsers() {
        Cursor cursor = mSQLiteDatabase.query(mSqLiteUserDataAccessHelper.TABLE_NAME, ALL_USER_COLUMNS, null, null, null,
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

    public User getUserByUid(int uid) {
        String[] args = {String.valueOf(uid)};

        Cursor cursor = mSQLiteDatabase.query(mSqLiteUserDataAccessHelper.TABLE_NAME, ALL_USER_COLUMNS,
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

    public ArrayList<User> getUsersUidNamePhoneNumberAvatar() {
        String[] columns = {mSqLiteUserDataAccessHelper.COLUMN_UID, mSqLiteUserDataAccessHelper.COLUMN_NAME,
                mSqLiteUserDataAccessHelper.COLUMN_PHONE_NUMBER, mSqLiteUserDataAccessHelper.COLUMN_AVATAR};

        Cursor cursor = mSQLiteDatabase.query(mSqLiteUserDataAccessHelper.TABLE_NAME, columns, null, null, null,
                null, mSqLiteUserDataAccessHelper.COLUMN_NAME +
                        " COLLATE NOCASE ASC");
        ArrayList<User> users = new ArrayList<User>();

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

        return mSQLiteDatabase.insert(SqLiteUserDataAccessHelper.TABLE_NAME, null, contentValues);
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

        return mSQLiteDatabase.update(mSqLiteUserDataAccessHelper.TABLE_NAME, contentValues,
                mSqLiteUserDataAccessHelper.COLUMN_UID + "=" + user.getUid(), null);
    }

    public long deleteUsers() {
        return mSQLiteDatabase.delete(SqLiteUserDataAccessHelper.TABLE_NAME, null, null);
    }

    public long deleteUserByUid(int id) {
        return mSQLiteDatabase.delete(mSqLiteUserDataAccessHelper.TABLE_NAME, mSqLiteUserDataAccessHelper.COLUMN_UID + "=" +
                id, null);
    }

    public void insertDefaultUsers() {
        long re = deleteUsers();

        byte[] avatarEliz = getByteArrayFromBitmap(BitmapFactory.decodeResource(ContactsApplication.getContext().getResources(), R.drawable.avatar_eliz));
        byte[] avatarFortech = getByteArrayFromBitmap(BitmapFactory.decodeResource(ContactsApplication.getContext().getResources(), R.drawable.avatar_fortech));
        byte[] avatarGipsey = getByteArrayFromBitmap(BitmapFactory.decodeResource(ContactsApplication.getContext().getResources(), R.drawable.avatar_gipsey));
        byte[] avatarHunor = getByteArrayFromBitmap(BitmapFactory.decodeResource(ContactsApplication.getContext().getResources(), R.drawable.avatar_hunor));
        byte[] avatarMdc = getByteArrayFromBitmap(BitmapFactory.decodeResource(ContactsApplication.getContext().getResources(), R.drawable.avatar_mdc));
        byte[] avatarMirjam = getByteArrayFromBitmap(BitmapFactory.decodeResource(ContactsApplication.getContext().getResources(), R.drawable.avatar_mirjam));
        byte[] avatarTamas = getByteArrayFromBitmap(BitmapFactory.decodeResource(ContactsApplication.getContext().getResources(), R.drawable.avatar_tamas));
        byte[] avatarUnknown = getByteArrayFromBitmap(BitmapFactory.decodeResource(ContactsApplication.getContext().getResources(), R.drawable.avatar_unknown));

        insertUser(new User(-1, "Mihu Cosmin", "0754919860", "cosmin.mihu@gmail.com", "1992-10-27", "Nr. 45, Str. B.P.Hasdeu, 400371, Cluj-Napoca, Jud. Cluj, Romania", "http://www.cosminmihu.info/", avatarMdc));
        insertUser(new User(-1, "Debre Elizabeth", "0735507173", "eliz_debre@yahoo.com", "1999-01-02", "address2", "https://www.facebook.com/debre.elizabeth", avatarEliz));
        insertUser(new User(-1, "Gipsey", "0735502246", "debredavid@yahoo.com", "1992-08-15", "Strada Scorțarilor 13, Cluj-Napoca", "http://www.avatar_gipsey.tk", avatarGipsey));
        insertUser(new User(-1, "Fortech", "+40 264 438217", "office@fortech.ro", "2003-12-01", "Str. Frunzisului nr.106 400664 Cluj-Napoca", "http://www.avatar_fortech.ro/", avatarFortech));
        insertUser(new User(-1, "Fortech", "+40 264 453303", "office@fortech.ro", "1999-01-02", "Fortech 15-17, Strada Meteor, Cluj-Napoca 400000", "http://www.avatar_fortech.ro/", avatarFortech));
        insertUser(new User(-1, "Jakab Hunor", "+40747253683", "jakabh@cs.ubbcluj.ro", "1985-04-19", "str. Rozelor nr. 62 547535 Sangeorgiu de Padure Mures, Romania", "http://www.cs.ubbcluj.ro/~jakabh/", avatarHunor));
        insertUser(new User(-1, "Borzási Mírjám", "0735 500 171", "mirjam.borzasi@yahoo.com", "1992-12-14", "Perecsenyi Magyar Baptista Imaház", "https://www.facebook.com/mirjam.borzasy", avatarMirjam));
        insertUser(new User(-1, "Balla Tamás", "0735 502 238", "balla_tomi@yahoo.com", "1994-01-19", "Strada Membrilor, Pericei 457265", "https://www.facebook.com/balla.btmy", avatarTamas));
        insertUser(new User(-1, "Name6", "006", "name6@yahoo.com", "1999-01-06", "address6", "http://www.gipsey.tk/6", avatarUnknown));
        insertUser(new User(-1, "Name7", "007", "name7@yahoo.com", "1999-01-07", "address7", "http://www.gipsey.tk/7", avatarUnknown));
        insertUser(new User(-1, "Name8", "008", "name8@yahoo.com", "1999-01-08", "address8", "http://www.gipsey.tk/8", avatarUnknown));
        insertUser(new User(-1, "Name9", "009", "name9@yahoo.com", "1999-9-9", "address9", "http://www.gipsey.tk/9", avatarUnknown));
        insertUser(new User(-1, "Name10", "0010", "name10@yahoo.com", "1999-10-10", "address10", "http://www.gipsey.tk/10", avatarUnknown));
    }

    public byte[] getByteArrayFromBitmap(Bitmap bitmapAvatar) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapAvatar.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
