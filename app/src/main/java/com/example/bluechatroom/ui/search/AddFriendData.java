package com.example.bluechatroom.ui.search;

public class AddFriendData {

    public String username, name, email, downloadUrl, room_id, phone;

    public AddFriendData() {
    }

    public AddFriendData(String username, String name, String email, String downloadUrl, String room_id, String phone) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.downloadUrl = downloadUrl;
        this.room_id = room_id;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
