package com.rsami.anuj.auth.SessionManagement;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ServiceManager extends BroadcastReceiver {
    static Context showOnMain;
    static AlertDialog.Builder  Alert=null;
    static AlertDialog alert = null;
    static int c=0;
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (!isOnline(context)) {
                showOnMain = context;
                if(c==0)
                    ShowAlert(true);
            } else {
                if(Alert !=null) {
                    c--;
                    alert.dismiss();
                    Alert = null;
                    Toast.makeText(showOnMain, "Online Now!", Toast.LENGTH_LONG).show();
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    void ShowAlert(boolean bool) {
        if (bool) {
            c++;
            Alert = new AlertDialog.Builder(showOnMain);
            Alert.setCancelable(false)
                    .setTitle("ALERT!!")
                    .setMessage("INTERNET Connection NOT Available !!");
            Alert.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    c--;
                    alert.dismiss();
                    Alert = null;
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    showOnMain.startActivity(a);

                }
            });

//            Alert.create();
            alert = Alert.create();
            alert.show();

        }
    }

    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}
