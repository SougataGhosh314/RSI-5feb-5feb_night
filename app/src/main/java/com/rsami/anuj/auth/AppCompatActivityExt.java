package com.rsami.anuj.auth;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import com.rsami.anuj.auth.SessionManagement.ServiceManager;

public class AppCompatActivityExt extends AppCompatActivity {

    private BroadcastReceiver mNetworkReceiver;

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }


    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    protected void onStop() {
        super.onStop();
        unregisterNetworkChanges();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mNetworkReceiver = new ServiceManager();
        registerNetworkBroadcastForNougat();

    }
}
