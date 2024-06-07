package com.digiview.gabay.domain.entities;

public class User {
    public User() {}
    public User(String user_id, String user_fname, String user_mname, String user_lname) {
        this.user_id = user_id;
        this.user_fname = user_fname;
        this.user_mname = user_mname;
        this.user_lname = user_lname;
    }

    public String user_id;
    public String user_fname;
    public String user_mname;
    public String user_lname;
}
