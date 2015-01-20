package com.training.contactsapp.model;

import java.io.Serializable;

/**
 * Created by davidd on 1/14/15.
 */
public class User implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;

    private int uid;
    private String name;
    private String phone_number;
    private String email;
    private String dob;
    private String address;
    private String website;

    public User() {
    }

    public User(int uid, String name, String phone_number, String email, String dob, String address, String website) {
        this.uid = uid;
        this.name = name;
        this.phone_number = phone_number;
        this.email = email;
        this.dob = dob;
        this.address = address;
        this.website = website;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", email='" + email + '\'' +
                ", dob=" + dob +
                ", address='" + address + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}
