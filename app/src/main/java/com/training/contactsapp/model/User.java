package com.training.contactsapp.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * Created by davidd on 1/14/15.
 */
public class User implements Serializable {
    private static final long serialVersionUid = -7060210544600464481L;
    private int mUid;
    private String mName;
    private String mPhoneNumber;
    private String mEmail;
    private String mDob;
    private String mAddress;
    private String mWebsite;
    private byte[] mAvatar;

    public User() {
    }

    public User(int uid, String name, String phoneNumber, String email, String dob, String address,
                String website, byte[] avatar) {
        this.mUid = uid;
        this.mName = name;
        this.mPhoneNumber = phoneNumber;
        this.mEmail = email;
        this.mDob = dob;
        this.mAddress = address;
        this.mWebsite = website;
        this.mAvatar = avatar; // TODO: probably here is nedd to copy by bytes not the reference, so we'll see
    }

    public int getUid() {
        return mUid;
    }

    public void setUid(int mUid) {
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
}
