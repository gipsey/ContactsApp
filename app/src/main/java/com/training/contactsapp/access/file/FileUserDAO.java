package com.training.contactsapp.access.file;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.training.contactsapp.access.MockDataManager;
import com.training.contactsapp.access.UserDAO;
import com.training.contactsapp.model.User;
import com.training.contactsapp.utils.ContactsApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileUserDAO implements UserDAO {
    public static final int ERROR_RETURN_CODE = -1;
    public static final int SUCCESS_RETURN_CODE = 0;
    private static final String DIR_NAME = "users";
    private static final String FILE_NAME = "users.txt";
    private static final String IMAGE_FILE_PREFIX = "image_";
    private static final String IMAGE_FILE_EXTENSION = ".png";
    private static final String NEW_LINE = "\n";
    private static final int USER_BEAN_COLUMNS_COUNT = 8;
    private static volatile FileUserDAO sInstance;
    private static File sDirectory;
    private static File sFile;

    /**
     * Constructor of the class adjusted to fit the Singleton pattern. Checks
     * whether the external storage is writable or not. If the file used
     * to store data is empty inserts some {@code User} instances.
     */
    private FileUserDAO() {
        if (isExternalStorageWritableAndWritable()) {
            createDirectoryAndFileObjects();
            if (!sFile.exists()) {
                Log.i(getClass().getName(), "Default users will be inserted, because the file doesn't exist.");
                MockDataManager.insertDefaultUsers(this);
            }
        } else {
            Log.e(getClass().getName(), "External storage is not writable");
            // TODO: Handle that situation when external storage is not available
        }
    }

    /**
     * Creates the instance of the class.
     *
     * @return The existing or the created instance of the class.
     */
    synchronized public static UserDAO getInstance() {
        if (sInstance == null) {
            sInstance = new FileUserDAO();
        }
        return sInstance;
    }

    /**
     * Checks whether the external storage is writable.
     *
     * @return {@code true} if external storage is writable, {@code false} otherwise.
     */
    public boolean isExternalStorageWritableAndWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * Checks whether directory specified by {@code sDirectory} exists,
     * if not exists creates the specified directory. Creates the file
     * specified by {@code sFile}.
     */
    private void createDirectoryAndFileObjects() {
        sDirectory = new File(Environment.getExternalStoragePublicDirectory(ContactsApplication.getPackage()), DIR_NAME);
        if (!sDirectory.exists()) {
            Log.i(getClass().getName(), sDirectory + " does not exist, creating it");
            if (sDirectory.mkdirs()) {
                Log.i(getClass().getName(), sDirectory + "creation is successful");
            } else {
                Log.e(getClass().getName(), sDirectory + " creation returned false, in this case an error occurred");
                // TODO: throw new exception
            }
        }
        sFile = new File(sDirectory, FILE_NAME);
    }

    /**
     * Saves the given {@code Bitmap} in the storage.
     *
     * @param bitmapToSave The {@code Bitmap} objects to be saved.
     * @return The absolute path of the saved {@code Bitmap} objects, {@code null} otherwise.
     */
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

    /**
     * Reads in the entire file, specified by {@code sFile} and returns
     * {@code User} instances in a {@code List}.
     *
     * @param all Tells that the method have to read and return all column
     *            of each row or call just the {@code setUid}, {@code setName},
     *            {@code setPhoneNumber} and {@code setAvatar} methods.
     * @return The {@code List} containing all the {@code User} instances read from {@code sFile}
     */
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

    /**
     * Reads file and returns the {@code User} instance that has the {@code uid} given as parameter.
     *
     * @param uid The uid that has to match.
     * @return A new {@code User} instance that has the {@code uid} given as parameter.
     */
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

    /**
     * Returns all {@code User} instances all columns.
     *
     * @return The {@code List} containing {@code User} instances.
     */
    @Override
    public List<User> getUsers() {
        return getUsers(true);
    }

    /**
     * Returns all {@code User} instances, but not all the columns, sorted by {@code Name}.
     *
     * @return The {@code List} containing {@code User} instances.
     */
    @Override
    public List<User> getUsersUidNamePhoneNumberAvatar() {
        List<User> users = getUsers(false);
        Collections.sort(users);
        return users;
    }

    /**
     * Inserts the given user into the file, specified by {@code sFile}.
     *
     * @param user The {@code User} instance to be inserted.
     * @return Returns {@code SUCCESS_RETURN_CODE} on success, {@code ERROR_RETURN_CODE} otherwise.
     */
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

    /**
     * Updates the given {@code User} instance in the file, specified by {@code sFile}. First of
     * all retrieves all the {@code User} instances from file, then deletes it. After these inserts
     * the retrieved {@code User} instances from {@code List} into file, and of course the specified
     * {@code User} instance will be inserted instead of the old one.
     *
     * @param user The new {@code User} instance to be inserted instead of the old one.
     * @return Returns {@code SUCCESS_RETURN_CODE} on success, {@code ERROR_RETURN_CODE} otherwise.
     */
    @Override
    public long updateUser(User user) {
        List<User> users = getUsers(true);

        if (!sFile.delete()) {
            Log.e(getClass().getName(), "Cannot remove the '" + sFile.getName() + "' file");
        }

        for (User u : users) {
            if (u.getUid() == user.getUid()) {
                File avatarFile = new File(u.getAvatarPath());
                if (!avatarFile.delete()) {
                    Log.w(getClass().getName(), "Cannot remove the '" + avatarFile.getName() + "' file");
                }
                insertUser(user);
            } else {
                u.setAvatar(null);
                insertUser(u);
            }
        }

        return SUCCESS_RETURN_CODE;
    }

    /**
     * Delete the file what contains all the information about users. It deletes the entire file.
     *
     * @return Returns {@code SUCCESS_RETURN_CODE} on success, {@code ERROR_RETURN_CODE} otherwise.
     */
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

    /**
     * Deletes one {@code User} instance from the file. The {@code User} instance is identified by
     * the {@code uid} parameter.
     *
     * @param uid Identifier for the {@code User} instance that has to be deleted.
     * @return Returns {@code SUCCESS_RETURN_CODE} on success, {@code ERROR_RETURN_CODE} otherwise.
     */
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
}
