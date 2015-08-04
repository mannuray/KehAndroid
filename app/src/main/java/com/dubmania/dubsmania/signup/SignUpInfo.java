package com.dubmania.dubsmania.signup;

import io.realm.RealmObject;

/**
 * Created by hardik.parekh on 8/2/2015.
 */
public class SignUpInfo extends RealmObject {
    private String userName;

    private String password;

    private String email;

    private String dob;

    private String id;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
