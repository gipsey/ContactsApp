package com.training.contactsapp.business;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.training.contactsapp.model.User;
import com.training.contactsapp.utils.ContactsApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Debre David on 2/6/15.
 */
public class ContactsManager {
    private ContentResolver mContentResolver;

    public ContactsManager() {
        mContentResolver = ContactsApplication.getContentResolverForContacts();
    }

    public List<User> getUsersFromContacts() {
        String[] projection = new String[]{ContactsContract.Contacts._ID,
                ContactsContract.Contacts.PHOTO_URI,
                ContactsContract.Contacts.DISPLAY_NAME};

        Cursor cursor = mContentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                projection,
                null,
                null,
                null);

        if (cursor == null) {
            Log.e(getClass().getName() + " getUsersFromContacts", "Cursor object is null. Returning.");
            return null;
        }

        List<User> users = new ArrayList<User>();
        while (cursor.moveToNext()) {
            User user = new User();
            int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            user.setUid(id);
            user.setAvatarPath(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)));
            user.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
            user.setPhoneNumber(getPhoneNumberByContactId(id));

            users.add(user);
        }
        cursor.close();

        return users;
    }

    public User getUserFromContactsById(long id) {
        String[] projection = new String[]{ContactsContract.Contacts._ID,
                ContactsContract.Contacts.PHOTO_URI,
                ContactsContract.Contacts.DISPLAY_NAME};

        Cursor cursor = mContentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                projection,
                String.format(ContactsContract.Contacts._ID + " = %d", id),
                null,
                null);

        if (cursor == null) {
            Log.e(getClass().getName() + " getUserByIdFromContacts()", "Cursor object is null. Returning.");
            return null;
        }

        User user = new User();
        while (cursor.moveToNext()) {
//            int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            user.setUid(id);
            user.setAvatarPath(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)));
            user.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
            user.setPhoneNumber(getPhoneNumberByContactId(id));
            user.setEmail(getEmailByContactId(id));
            user.setDob(getDobByContactId(id));
            user.setAddress(getAddressByContactId(id));
            user.setWebsite(getWebsiteByContactId(id));
        }

        return user;
    }

    private String getPhoneNumberByContactId(long contactId) {
        Cursor cursor = mContentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE}, // ContactsContract.CommonDataKinds.Phone.MIMETYPE
                String.format(ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = %d", contactId),
                null,
                null);

        String data = null;
        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)) == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                data = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
        }
        cursor.close();

        return data;
    }

    private String getEmailByContactId(long contactId) {
        Cursor cursor = mContentResolver.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS, ContactsContract.CommonDataKinds.Email.TYPE},
                String.format(ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = %d", contactId),
                null,
                null);

        String data = null;
        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE)) == ContactsContract.CommonDataKinds.Email.TYPE_HOME) {
                data = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
            }
        }
        cursor.close();

        return data;
    }

    private String getDobByContactId(long contactId) {
        Cursor cursor = mContentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Event.START_DATE, ContactsContract.CommonDataKinds.Event.TYPE, ContactsContract.CommonDataKinds.Event.MIMETYPE},
                String.format(ContactsContract.Data.CONTACT_ID + " = %d AND " +
                                ContactsContract.CommonDataKinds.Event.TYPE + " = " + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY + " AND " +
                                ContactsContract.CommonDataKinds.Event.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "'",
                        contactId),
                null,
                null);

        String data = null;
        while (cursor.moveToNext()) {
            data = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
        }
        cursor.close();

        return data;
    }

    private String getAddressByContactId(long contactId) {
        Cursor cursor = mContentResolver.query(
                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, ContactsContract.CommonDataKinds.StructuredPostal.TYPE},
                String.format(ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = %d AND " +
                                ContactsContract.CommonDataKinds.StructuredPostal.TYPE + " = " + ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME,
                        contactId),
                null,
                null);

        String data = null;
        while (cursor.moveToNext()) {
            data = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
        }
        cursor.close();

        return data;
    }

    private String getWebsiteByContactId(long contactId) {
        Cursor cursor = mContentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Website.URL, ContactsContract.CommonDataKinds.Event.TYPE},
                String.format(ContactsContract.Data.CONTACT_ID + " = %d AND " +
                                ContactsContract.CommonDataKinds.Website.TYPE + " = " + ContactsContract.CommonDataKinds.Website.TYPE_HOMEPAGE,
                        contactId),
                null,
                null);

        String data = null;
        while (cursor.moveToNext()) {
            data = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL));
        }
        cursor.close();

        return data;
    }

}