package com.example.bluechatroom.ui.search;

public class userroom {

    String roomId, you, friend, name, email, phone, downloadUrl;

    public userroom() {
    }

    public userroom(String roomId, String you, String friend, String name, String email, String phone, String downloadUrl) {
        this.roomId = roomId;
        this.you = you;
        this.friend = friend;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.downloadUrl = downloadUrl;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getYou() {
        return you;
    }

    public void setYou(String you) {
        this.you = you;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}

