package com.example.bluechatroom;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class isOnlineVerify {

    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            
            if ((Runtime.getRuntime().exec(command).waitFor() == 0))
            {
                Log.e("metaverse","Connected");
            }
            else {
                Log.e("metaverse","Dis-Connected");
            }
            return true;
        } catch (Exception e) {
            Log.e("metaverse","Error");
            return false;
        }
    }

}
