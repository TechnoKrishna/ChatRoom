package com.example.bluechatroom.ui.global;

public class Send_Message_Modal {

    String message, unique_id, username, Time, downloadUrl;

    public Send_Message_Modal() {
    }

    public Send_Message_Modal(String message, String unique_id, String username, String time, String downloadUrl) {
        this.message = message;
        this.unique_id = unique_id;
        this.username = username;
        Time = time;
        this.downloadUrl = downloadUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
