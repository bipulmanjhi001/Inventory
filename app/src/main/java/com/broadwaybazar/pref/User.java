package com.broadwaybazar.pref;

public class User {
    private String token,username,role, sales_id;

    public User(String token, String username, String role, String sales_id) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.sales_id = sales_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSales_id() {
        return sales_id;
    }

    public void setSales_id(String sales_id) {
        this.sales_id = sales_id;
    }
}
