package com.training.contactsapp.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.training.contactsapp.utils.ContactsApplication;

/**
 * Created by davidd on 1/14/15.
 */
public class UserDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "user.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "User";
    public static final String DROP_TABLE_USER = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String COLUMN_UID = "uid";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_DOB = "dob";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_WEBSITE = "website";
    public static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_NAME + " ( " +
            COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_PHONE_NUMBER + " TEXT NOT NULL, " +
            COLUMN_EMAIL + " TEXT, " +
            COLUMN_DOB + " TEXT, " +
            COLUMN_ADDRESS + " TEXT, " +
            COLUMN_WEBSITE + " TEXT" +
            ")";

    public UserDBHelper() {
        super(ContactsApplication.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_USER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TABLE_USER);
            onCreate(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
