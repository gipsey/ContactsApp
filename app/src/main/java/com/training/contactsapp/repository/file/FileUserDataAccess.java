package com.training.contactsapp.repository.file;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.training.contactsapp.R;
import com.training.contactsapp.business.ContactsApplication;
import com.training.contactsapp.model.User;
import com.training.contactsapp.repository.UserDataAccess;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidd on 1/29/15.
 */
public class FileUserDataAccess implements UserDataAccess {
    public static final int ERROR_RETURN_CODE = -1;
    public static final int SUCCESS_RETURN_CODE = 0;
    private static final String DIR_NAME = "users";
    private static final String FILE_NAME = "users.txt";
    private static final String TEMPORARY_FILE_NAME = "temp_users.txt";
    private static final String IMAGE_FILE_PREFIX = "image_";
    private static final String IMAGE_FILE_EXTENSION = ".png";
    private static final String NEW_LINE = "\n";
    private static final int USER_BEAN_COLUMNS_COUNT = 8;
    private static File sDirectory;
    private static File sFile;

    public FileUserDataAccess() {
        if (isExternalStorageWritableAndWritable()) {
            createDirectoryAndFileObjects();
            insertDefaultUsers(); // TODO: It's temporary
        } else {
            Log.e(getClass().getName(), "External storage is not writable");
            // TODO: Handle that situation when external storage is not available
        }
    }

    public boolean isExternalStorageWritableAndWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private void createDirectoryAndFileObjects() {
        sDirectory = new File(Environment.getExternalStoragePublicDirectory(ContactsApplication.getPackage()), DIR_NAME);
        if (!sDirectory.exists()) {
            Log.i(getClass().getName(), sDirectory + " does not exist, creating it");
            if (sDirectory.mkdirs()) {
                Log.i(getClass().getName(), sDirectory + "creation is successful");
            } else {
                Log.e(getClass().getName(), sDirectory + " creation returned false, in this case an error occured");
                // TODO: throw new exception
            }
        }
        sFile = new File(sDirectory, FILE_NAME);
    }

    private boolean isFileEmpty(File file) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            if (bufferedReader.readLine() == null) {
                return true;
            }
            return false;
        } catch (IOException e) {
            Log.e(getClass().getName(), "Cannot decide that " + file.getName() + " is empty or not");
            e.printStackTrace();
            return true;
        }
    }

    private String saveImageInStorage(Bitmap bitmapToSave) {
        String fileName = IMAGE_FILE_PREFIX + System.currentTimeMillis() + IMAGE_FILE_EXTENSION;
        File file = new File(sDirectory, fileName);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmapToSave.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            Log.e(getClass().getName(), "Saving bitmap in storage is not succeeded");
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileOutputStream != null) fileOutputStream.close();
            } catch (IOException e) {
                Log.e(getClass().getName(), "Cannot close fileOutputStream");
                e.printStackTrace();
            }
        }
        return file.getAbsolutePath();
    }

    private List<User> getUsers(boolean all) {
        List<User> users = new ArrayList<User>();

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(sFile));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                User user = new User();
                user.setUid(Long.parseLong(line));
                if (!(line = bufferedReader.readLine()).isEmpty()) user.setName(line);
                if (!(line = bufferedReader.readLine()).isEmpty()) user.setPhoneNumber(line);
                if (all) {
                    if (!(line = bufferedReader.readLine()).isEmpty()) user.setEmail(line);
                    if (!(line = bufferedReader.readLine()).isEmpty()) user.setDob(line);
                    if (!(line = bufferedReader.readLine()).isEmpty()) user.setAddress(line);
                    if (!(line = bufferedReader.readLine()).isEmpty()) user.setWebsite(line);
                } else {
                    bufferedReader.readLine();
                    bufferedReader.readLine();
                    bufferedReader.readLine();
                    bufferedReader.readLine();
                }
                if (!(line = bufferedReader.readLine()).isEmpty()) {
                    user.setAvatarAsBitmap(BitmapFactory.decodeFile(line));
                    user.setAvatarPath(line);
                }
                bufferedReader.readLine();
                users.add(user);
            }
        } catch (IOException e) {
            Log.e(getClass().getName(), "Exception while trying to open and read from file");
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
            } catch (IOException e) {
                Log.e(getClass().getName(), "Cannot close reader");
                e.printStackTrace();
            }
        }
        return users;
    }

    @Override
    public User getUserByUid(long uid) { // I want to use this instead of reading the whole file, because if the uid is found the method returns the specified user
        User user = null;
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(sFile));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (Long.parseLong(line) == uid) {
                    user = new User();
                    user.setUid(Long.parseLong(line));
                    if (!(line = bufferedReader.readLine()).isEmpty()) user.setName(line);
                    if (!(line = bufferedReader.readLine()).isEmpty()) user.setPhoneNumber(line);
                    if (!(line = bufferedReader.readLine()).isEmpty()) user.setEmail(line);
                    if (!(line = bufferedReader.readLine()).isEmpty()) user.setDob(line);
                    if (!(line = bufferedReader.readLine()).isEmpty()) user.setAddress(line);
                    if (!(line = bufferedReader.readLine()).isEmpty()) user.setWebsite(line);
                    if (!(line = bufferedReader.readLine()).isEmpty()) {
                        user.setAvatarAsBitmap(BitmapFactory.decodeFile(line));
                    }
                    return user;
                } else {
                    for (int i = 0; i < (USER_BEAN_COLUMNS_COUNT - 1 + 1); i++) {
                        bufferedReader.readLine();
                    }
                }
            }
        } catch (IOException e) {
            Log.e(getClass().getName(), "Exception while trying to open and read from file");
            e.printStackTrace();
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.e(getClass().getName(), "Cannot close reader");
                    e.printStackTrace();
                }
            }
        }
        return user;
    }

    @Override
    public List<User> getUsers() {
        return getUsers(true);
    }

    @Override
    public List<User> getUsersUidNamePhoneNumberAvatar() {
        return getUsers(false);
    }

    @Override
    public long insertUser(User user) {
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new FileWriter(sFile, true));
        } catch (IOException e) {
            Log.e(getClass().getName(), "Cannot create PrintWriter");
            e.printStackTrace();
            return ERROR_RETURN_CODE;
        }

        long id = user.getUid();
        if (id == -1 || id == 0) id = System.currentTimeMillis();
        String path = null;

        if (user.getAvatar() != null) {
            path = saveImageInStorage(user.getAvatarAsBitmap());
        } else {
            path = user.getAvatarPath();
        }
        printWriter.append(id + NEW_LINE +
                user.getName() + NEW_LINE +
                user.getPhoneNumber() + NEW_LINE +
                user.getEmail() + NEW_LINE +
                user.getDob() + NEW_LINE +
                user.getAddress() + NEW_LINE +
                user.getWebsite() + NEW_LINE +
                path + NEW_LINE +
                NEW_LINE);
        printWriter.flush();
        printWriter.close();

        return SUCCESS_RETURN_CODE;
    }

    @Override
    public long updateUser(User user) { // TODO: Will be implemented


        return SUCCESS_RETURN_CODE;
    }

    @Override
    public long deleteUsers() {
        boolean allFilesDeletedSuccessfully = true;
        for (File file : sDirectory.listFiles()) {
            if (!file.delete()) {
                allFilesDeletedSuccessfully = false;
            }
        }
        if (allFilesDeletedSuccessfully) {
            return SUCCESS_RETURN_CODE;
        } else {
            return ERROR_RETURN_CODE;
        }
    }

    @Override
    public long deleteUserByUid(long uid) {
        List<User> users = getUsers(true);

        if (!sFile.delete()) {
            Log.e(getClass().getName(), "Cannot remove the '" + sFile.getName() + "' file");
        }

        for (User user : users) {
            if (user.getUid() == uid) {
                File avatarFile = new File(user.getAvatarPath());
                if (!avatarFile.delete()) {
                    Log.w(getClass().getName(), "Cannot remove the '" + avatarFile.getName() + "' file");
                }
            } else {
                user.setAvatar(null);
                insertUser(user);
            }
        }

        return SUCCESS_RETURN_CODE;
    }

    @Override
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

    private byte[] getByteArrayFromBitmap(Bitmap bitmapAvatar) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapAvatar.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}