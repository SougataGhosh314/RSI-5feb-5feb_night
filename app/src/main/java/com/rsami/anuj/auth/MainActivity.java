package com.rsami.anuj.auth;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.rsami.anuj.auth.SessionManagement.SessionManager;
import com.rsami.anuj.auth.model.RSA;
import com.rsami.anuj.auth.model.aceKeys;
import com.rsami.anuj.auth.model.keyClass;
import com.rsami.anuj.auth.model.userModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivityExt implements ForceUpdateChecker.OnUpdateNeededListener {

    private String validID = "123";
    private String verifyID = null;
    private EditText etAlpha, etNum;
    private Button submitButton;
    private String pb,pr,sapi;
    private String et = null;
    private int size = 1;
    String pbl,prv,smsspi;
    SessionManager sessionManager;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    protected void onStart() {
        super.onStart();
        try{
            sessionManager = new SessionManager(this);
            String bcrajpublic = getIntent().getStringExtra("splashpublic");
            String bcrajprivate = getIntent().getStringExtra("splashprivate");
            if (sessionManager.isLoggedIn()) {

                Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("pb",bcrajpublic);
                intent.putExtra("pr",bcrajprivate);
                startActivity(intent);
                finish();
            }
            ForceUpdateChecker.with(this).onUpdateNeeded(this).check();



            etAlpha = findViewById(R.id.rsiIDalpha);
            etNum = findViewById(R.id.rsiIDnum);
            submitButton = findViewById(R.id.submitID);

            etAlpha.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start,int before, int count)
                {
                    // TODO Auto-generated method stub
                    if(etAlpha.getText().toString().length()==size)
                    {
                        etNum.requestFocus();
                    }
                }
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                    // TODO Auto-generated method stub

                }

                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub
                }

            });

            etNum.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start,int before, int count)
                {
                    // TODO Auto-generated method stub
                    if(etNum.getText().toString().length()==0)
                    {
                        etAlpha.requestFocus();
                    }
                }
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                    // TODO Auto-generated method stub

                }

                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub
                }

            });

            mProgress = new ProgressDialog(MainActivity.this);

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    et = etAlpha.getText().toString() + "-" + etNum.getText().toString();

                    verifyID = et;

                    mProgress.setMessage("Verifying...");
                    mProgress.show();

                //    validate();
                    getKeys();

                }
            });
        }catch (Exception e){
            Log.e("ErrorMain",e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ForceUpdateChecker.with(this).onUpdateNeeded(this).check();
    }

    @Override
    public void onUpdateNeeded(final String updateUrl) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("New version available")
                .setMessage("Please, update app to new version to continue.")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                redirectStore(updateUrl);
                            }
                        })
                .create();
        dialog.show();
    }

    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    public void onBackPressed() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }
        }, 10);

    }

    void validate() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("UserSignIn2");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            private static final String TAG = "MainActivity";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                et = etAlpha.getText().toString() + "-" + etNum.getText().toString();

                userModel u = dataSnapshot.child(et.toUpperCase()).getValue(userModel.class);
                Log.e("OTPERROR","YAYi2");

                if(u != null && (u.mobno != null || u.mobno != "") && (u.pass != null || u.pass != "")) {
                    RSA r = new RSA();

                    if(u.mobno.length()<=10){ // decrypted strings
                        try{
                            u.pass = r.Encrypt(u.pass);
                            u.mobno = r.Encrypt(u.mobno);
                            myRef.child(u.rsiID).setValue(u);
                        }catch(Exception e){
                        }
                    }

                    if(u.mobno.length()>50){ // encrypted strings
                        try{
                            //          Log.e("hhh",""+u.pass+"\n"+u.mobno+"\n"+"PrivatKey "+r.kPrivate + "\n" + "public" + r.kPublic);
                            u.pass = r.Decrypt(u.pass);
                            u.mobno = r.Decrypt(u.mobno);
                            //        Log.e("hhh",""+u.pass+"\n"+u.mobno);
                        }catch(Exception e){
                            Log.e("hhh",e.getMessage());
                        }
                    }



                    if (dataSnapshot.child(et.toUpperCase()).exists()) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.putExtra("userModel", u);
                        //     Log.e("errorMainActivity","hi"+aceKeys.privateKey);
                        intent.putExtra("publicKey",pbl);
                        intent.putExtra("privateKey",prv);
                        intent.putExtra("smsapi",smsspi);
                        mProgress.dismiss();
                        startActivity(intent);

                    } else {

                        Toast.makeText(MainActivity.this, "Invalid ID", Toast.LENGTH_SHORT).show();
                        mProgress.dismiss();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Cannot login, kindly contact RSAMI admin to update your data", Toast.LENGTH_LONG).show();
                    mProgress.dismiss();
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //  Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    private void getKeys() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("Keys");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                keyClass kc = dataSnapshot.getValue(keyClass.class);
                pbl = kc.pbl;
                prv = kc.prv;
                smsspi = kc.smsapi;
                aceKeys.privateKey = kc.pbl;
                aceKeys.publicKey = kc.prv;
                aceKeys.smsapi = kc.smsapi;
                Log.e("OTPERROR","YAYi1");
                validate();
//                sessionManager.rsa=new RSA(kc.pbl,kc.prv);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}