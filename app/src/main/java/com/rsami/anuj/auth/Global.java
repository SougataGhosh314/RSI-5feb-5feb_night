package com.rsami.anuj.auth;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.rsami.anuj.auth.model.aceKeys;
import com.rsami.anuj.auth.model.keyClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Global extends Application {

    public static String AdminID = "A-00700";
    private static final String TAG = Global.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        try{
            getKeys();
            final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

            // set in-app defaults
            Map<String, Object> remoteConfigDefaults = new HashMap();
            remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_REQUIRED, false);
            remoteConfigDefaults.put(ForceUpdateChecker.KEY_CURRENT_VERSION, "1.0.0");
            remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_URL, "https://play.google.com/store/apps/details?id=com.rsami.anuj.auth&hl=en");
            //remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_URL, "https://google.com");

            firebaseRemoteConfig.setDefaults(remoteConfigDefaults);
            firebaseRemoteConfig.fetch(6) // fetch every 100 minutes
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "remote config is fetched.");
                                firebaseRemoteConfig.activateFetched();
                            }
                            else {
                                Log.d(TAG, "remote config NOT is fetched." + "\n" + task.getException().toString());
                            }
                        }
                    });

            Picasso.Builder builder = new Picasso.Builder(this);
            builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
            Picasso built = builder.build();
            built.setIndicatorsEnabled(false);
            built.setLoggingEnabled(true);
            Picasso.setSingletonInstance(built);
        }catch (Exception e){
            Log.e("ErrorGolbal",e.getMessage());
        }

    }
    private void getKeys() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("Keys");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                keyClass kc = dataSnapshot.getValue(keyClass.class);
                String pb = kc.pbl;
                String pr = kc.prv;
                String sapi = kc.getSmsapi();
                aceKeys.privateKey = pr;
                aceKeys.publicKey = pb;
                aceKeys.smsapi = sapi;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
