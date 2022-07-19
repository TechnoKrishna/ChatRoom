package com.example.bluechatroom.ui.search;

public class userData {

    public String name;
    public String username;
    public String email;
    public String phone;
    public String password;
    public String gender;
    public String downloadUrl;
    public String anonymous_status;
    public String online_status;

    public userData(String name, String username, String email, String phone, String password, String gender) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
    }

    public userData() {
    }

    public userData(String name, String username, String email, String phone, String password, String gender, String downloadUrl, String anonymous_status, String online_status) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.downloadUrl = downloadUrl;
        this.anonymous_status = anonymous_status;
        this.online_status = online_status;
    }

    public userData(String name, String username, String email, String phone, String password, String gender, String downloadUrl) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.downloadUrl = downloadUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getAnonymous_status() {
        return anonymous_status;
    }

    public void setAnonymous_status(String anonymous_status) {
        this.anonymous_status = anonymous_status;
    }

    public String getOnline_status() {
        return online_status;
    }

    public void setOnline_status(String online_status) {
        this.online_status = online_status;
    }
}

