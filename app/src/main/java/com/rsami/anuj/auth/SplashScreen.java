package com.rsami.anuj.auth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.rsami.anuj.auth.SessionManagement.SessionManager;
import com.rsami.anuj.auth.model.aceKeys;
import com.rsami.anuj.auth.model.keyClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scottyab.rootbeer.RootBeer;

public class SplashScreen extends AppCompatActivity{

    FirebaseAuth mAuth;
    private static int SPLASH_TIME_OUT = 2500;
    String plb=null,prv=null,smsapi;
    private RootBeer rootBeer;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        try{
            sessionManager = new SessionManager(SplashScreen.this);
            rootBeer = new RootBeer(this);

            if (rootBeer.isRootedWithoutBusyBoxCheck()) {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle("Root Detected!!!")
                        .setMessage("This application will not run on a rooted phone.")
                        .setPositiveButton("Exit",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent a = new Intent(Intent.ACTION_MAIN);
                                        a.addCategory(Intent.CATEGORY_HOME);
                                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(a);
                                    }
                                })
                        .create();
                dialog.show();
            } else {
                 // getKeys();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference().child("Keys");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        keyClass kc = dataSnapshot.getValue(keyClass.class);
                        plb= kc.pbl;
                        prv = kc.prv;
                        smsapi = kc.getSmsapi();
                        aceKeys.privateKey = prv;
                        aceKeys.publicKey = plb;
                        aceKeys.smsapi = smsapi;
                        mAuth = FirebaseAuth.getInstance();

                        if(plb!=null && prv!=null){
                            if (mAuth.getCurrentUser() == null) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent homeIntent = new Intent(SplashScreen.this, MainActivity.class);
                                        homeIntent.putExtra("splashpublic",plb);
                                        homeIntent.putExtra("splashprivate",prv);
                                        startActivity(homeIntent);
                                        finish();
                                    }
                                }, SPLASH_TIME_OUT);
                            } else {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent homeIntent = new Intent(SplashScreen.this, MainActivity.class);
                                        homeIntent.putExtra("splashpublic",plb);
                                        homeIntent.putExtra("splashprivate",prv);
                                        startActivity(homeIntent);
                                        finish();
                                    }
                                }, SPLASH_TIME_OUT);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        }catch (Exception e){
            Log.e("ErrorSplash",e.getMessage());
        }

    }




}