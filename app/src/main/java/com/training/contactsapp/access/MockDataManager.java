package com.training.contactsapp.access;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.training.contactsapp.R;
import com.training.contactsapp.model.User;
import com.training.contactsapp.utils.ContactsApplication;

import java.io.ByteArrayOutputStream;

public class MockDataManager {

    /**
     * Inserts mock data using the {@code UserDAO} parameter.
     *
     * @param userDAO It makes possible to insert mock data.
     */
    public static void insertDefaultUsers(UserDAO userDAO) {
        long re = userDAO.deleteUsers();

        byte[] avatarEliz = getByteArrayFromBitmap(BitmapFactory.decodeResource(ContactsApplication.getContext().getResources(), R.drawable.avatar_eliz));
        byte[] avatarFortech = getByteArrayFromBitmap(BitmapFactory.decodeResource(ContactsApplication.getContext().getResources(), R.drawable.avatar_fortech));
        byte[] avatarGipsey = getByteArrayFromBitmap(BitmapFactory.decodeResource(ContactsApplication.getContext().getResources(), R.drawable.avatar_gipsey));
        byte[] avatarHunor = getByteArrayFromBitmap(BitmapFactory.decodeResource(ContactsApplication.getContext().getResources(), R.drawable.avatar_hunor));
        byte[] avatarMdc = getByteArrayFromBitmap(BitmapFactory.decodeResource(ContactsApplication.getContext().getResources(), R.drawable.avatar_mdc));
        byte[] avatarMirjam = getByteArrayFromBitmap(BitmapFactory.decodeResource(ContactsApplication.getContext().getResources(), R.drawable.avatar_mirjam));
        byte[] avatarTamas = getByteArrayFromBitmap(BitmapFactory.decodeResource(ContactsApplication.getContext().getResources(), R.drawable.avatar_tamas));
        byte[] avatarUnknown = getByteArrayFromBitmap(BitmapFactory.decodeResource(ContactsApplication.getContext().getResources(), R.drawable.avatar_unknown));

        userDAO.insertUser(new User(-1, "Mihu Cosmin", "0754919860", "cosmin.mihu@gmail.com", "1992-10-27", "Nr. 45, Str. B.P.Hasdeu, 400371, Cluj-Napoca, Jud. Cluj, Romania", "http://www.cosminmihu.info/", avatarMdc));
        userDAO.insertUser(new User(-1, "Debre Elizabeth", "0735507173", "eliz_debre@yahoo.com", "1999-01-02", "address2", "https://www.facebook.com/debre.elizabeth", avatarEliz));
        userDAO.insertUser(new User(-1, "Gipsey", "0735502246", "debredavid@yahoo.com", "1992-08-15", "Strada Scorțarilor 13, Cluj-Napoca", "http://www.avatar_gipsey.tk", avatarGipsey));
        userDAO.insertUser(new User(-1, "Fortech", "+40 264 438217", "office@fortech.ro", "2003-12-01", "Str. Frunzisului nr.106 400664 Cluj-Napoca", "http://www.avatar_fortech.ro/", avatarFortech));
        userDAO.insertUser(new User(-1, "Fortech", "+40 264 453303", "office@fortech.ro", "1999-01-02", "Fortech 15-17, Strada Meteor, Cluj-Napoca 400000", "http://www.avatar_fortech.ro/", avatarFortech));
        userDAO.insertUser(new User(-1, "Jakab Hunor", "+40747253683", "jakabh@cs.ubbcluj.ro", "1985-04-19", "str. Rozelor nr. 62 547535 Sangeorgiu de Padure Mures, Romania", "http://www.cs.ubbcluj.ro/~jakabh/", avatarHunor));
        userDAO.insertUser(new User(-1, "Borzási Mírjám", "0735 500 171", "mirjam.borzasi@yahoo.com", "1992-12-14", "Perecsenyi Magyar Baptista Imaház", "https://www.facebook.com/mirjam.borzasy", avatarMirjam));
        userDAO.insertUser(new User(-1, "Balla Tamás", "0735 502 238", "balla_tomi@yahoo.com", "1994-01-19", "Strada Membrilor, Pericei 457265", "https://www.facebook.com/balla.btmy", avatarTamas));
        userDAO.insertUser(new User(-1, "Name6", "006", "name6@yahoo.com", "1999-01-06", "address6", "http://www.gipsey.tk/6", avatarUnknown));
        userDAO.insertUser(new User(-1, "Name7", "007", "name7@yahoo.com", "1999-01-07", "address7", "http://www.gipsey.tk/7", avatarUnknown));
        userDAO.insertUser(new User(-1, "Name8", "008", "name8@yahoo.com", "1999-01-08", "address8", "http://www.gipsey.tk/8", avatarUnknown));
        userDAO.insertUser(new User(-1, "Name9", "009", "name9@yahoo.com", "1999-9-9", "address9", "http://www.gipsey.tk/9", avatarUnknown));
        userDAO.insertUser(new User(-1, "", "", "", "", "", "", avatarUnknown));
    }

    /**
     * Converts {@code Bitmap} object to {@code byte} array.
     *
     * @param bitmapAvatar The {@code Bitmap} object to be converted.
     * @return {@code byte} array that contains the given {@code Bitmap} object.
     */
    private static byte[] getByteArrayFromBitmap(Bitmap bitmapAvatar) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapAvatar.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
