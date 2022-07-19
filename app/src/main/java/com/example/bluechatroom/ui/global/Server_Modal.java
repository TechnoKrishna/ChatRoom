package com.example.bluechatroom.ui.global;

public class Server_Modal {

    String server_id;
    String server_name;

    public Server_Modal() {
    }

    public Server_Modal(String server_id, String server_name) {
        this.server_id = server_id;
        this.server_name = server_name;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public String getServer_name() {
        return server_name;
    }

    public void setServer_name(String server_name) {
        this.server_name = server_name;
    }
}

