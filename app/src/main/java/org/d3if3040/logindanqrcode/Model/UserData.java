package org.d3if3040.logindanqrcode.Model;

public class UserData {
    private int id;
    private String username;
    private String password;

    public UserData(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

