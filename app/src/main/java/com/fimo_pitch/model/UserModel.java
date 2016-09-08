package com.fimo_pitch.model;

import java.io.Serializable;

/**
 * Created by Diep_Chelsea on 13/07/2016.
 */
public class UserModel implements Serializable {
    public static String TYPE_TEAM="TEAM";
    public static String TYPE_OWNER="OWNER";
    public String name;
    public String id;
    public String phone;
    public String email;
    public String userType;
    public String imageURL;
    public String password;

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", userType='" + userType + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
