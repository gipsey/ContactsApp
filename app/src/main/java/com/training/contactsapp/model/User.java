package com.training.contactsapp.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * Created by davidd on 1/14/15.
 */
public class User implements Serializable, Comparable<User> {
    private static final long serialVersionUid = -7060210544600464481L;
    private long mUid;
    private String mName;
    private String mPhoneNumber;
    private String mEmail;
    private String mDob;
    private String mAddress;
    private String mWebsite;
    private byte[] mAvatar;
    private String mAvatarPath;

    public User() {
    }

    public User(long uid, String name, String phoneNumber, String email, String dob, String address,
                String website, byte[] avatar) {
        this.mUid = uid;
        this.mName = name;
        this.mPhoneNumber = phoneNumber;
        this.mEmail = email;
        this.mDob = dob;
        this.mAddress = address;
        this.mWebsite = website;
        this.mAvatar = avatar;
    }

    public long getUid() {
        return mUid;
    }

    public void setUid(long mUid) {
        this.mUid = mUid;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getDob() {
        return mDob;
    }

    public void setDob(String dob) {
        this.mDob = dob;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public void setWebsite(String website) {
        this.mWebsite = website;
    }

    public byte[] getAvatar() {
        return mAvatar;
    }

    public void setAvatar(byte[] avatar) {
        this.mAvatar = avatar;
    }

    public Bitmap getAvatarAsBitmap() {
        return BitmapFactory.decodeByteArray(mAvatar, 0, mAvatar.length);
    }

    public void setAvatarAsBitmap(Bitmap bitmapAvatar) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapAvatar.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        mAvatar = byteArrayOutputStream.toByteArray();
    }

    public String getAvatarPath() {
        return mAvatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.mAvatarPath = avatarPath;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + mUid +
                ", name='" + mName + '\'' +
                ", phoneNumber='" + mPhoneNumber + '\'' +
                ", email='" + mEmail + '\'' +
                ", dob='" + mDob + '\'' +
                ", address='" + mAddress + '\'' +
                ", website='" + mWebsite + '\'' +
                ", avatar='" + mAvatar + "\'" +
                '}';
    }

    @Override
    public int compareTo(User another) {
        try {
            return this.mName.compareToIgnoreCase(another.mName);
        } catch (NullPointerException e) {
            Log.w(getClass().getName() + ".compareTo", "NullPointerException");
            return -1;
        }
    }
}
