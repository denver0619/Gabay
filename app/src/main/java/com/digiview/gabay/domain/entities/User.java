package com.digiview.gabay.domain.entities;

public class User {
    public User() {}
    public User(String user_id, String user_email, String user_password) {
        this.user_id = user_id;
        this.user_email = user_email;
        this.user_password = user_password;
    }

    public String user_id;
    public String user_email;
    public String user_password;
}
