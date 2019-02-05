package com.rsami.anuj.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rsami.anuj.auth.model.RSA;
import com.rsami.anuj.auth.model.aceKeys;

public class ChangeNumber extends AppCompatActivity {

    DatabaseReference usersRef, usersRefToUse, depRef, depRefToUse;
    private EditText editTextNewPhone, editTextNewDep;
    private EditText editTextUserId;
    private TextView textViewCurrentPhone, textViewCurrentDep;
    private Button buttonGetPhoneAndDep, buttonChangePhone, buttonChangeDep;

    private String currentUser;
    private String newPhone, newDep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_number);

        try{
            findViews();
            setOnClickListeners();
        }catch (Exception e){
            Log.e("ErrorChangeNo",e.getMessage());
        }

    }

    private void findViews(){
        editTextNewPhone = findViewById(R.id.editTextNewPhone);
        editTextUserId = findViewById(R.id.editTextUserId);
        textViewCurrentPhone = findViewById(R.id.textViewCurrentPhone);
        editTextNewDep = findViewById(R.id.editTextNewDep);
        textViewCurrentDep = findViewById(R.id.textViewCurrentDep);
        buttonChangePhone = findViewById(R.id.buttonChangePhone);
        buttonGetPhoneAndDep = findViewById(R.id.buttonGetPhone);
        buttonChangeDep = findViewById(R.id.buttonChangeDep);
        usersRef = FirebaseDatabase.getInstance().getReference("UserSignIn");
        usersRefToUse = FirebaseDatabase.getInstance().getReference("UserSignIn");
        depRef = FirebaseDatabase.getInstance().getReference("DepCount");
        depRefToUse = FirebaseDatabase.getInstance().getReference("DepCount");
    }


    private void setOnClickListeners(){
        buttonGetPhoneAndDep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser = editTextUserId.getText().toString().trim().toUpperCase();
                if (TextUtils.isEmpty(currentUser)){
                    editTextUserId.setError("Enter valid User");
                    editTextUserId.requestFocus();
                    return;
                }
                setCurrentPhone();
            }
        });

        buttonChangePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser = editTextUserId.getText().toString().trim().toUpperCase();
                if (TextUtils.isEmpty(currentUser)){
                    editTextUserId.setError("Enter valid User");
                    editTextUserId.requestFocus();
                    return;
                }

                final String u = currentUser;
                newPhone = editTextNewPhone.getText().toString().trim();

                if(newPhone.length()!=10){
                    editTextNewPhone.setError("Enter Valid Phone");
                    editTextNewPhone.requestFocus();
                    return;
                }

                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.hasChild(u)){
                            RSA r= new RSA();
                            try{
                                newPhone = r.Encrypt(newPhone);
                            }catch (Exception e){
                                Log.e("changeerror",e.getMessage());
                            }
                            usersRefToUse.child(u)
                                    .child("mobno")
                                    .setValue(newPhone);

                            usersRefToUse.child(u)
                                    .child("pass")
                                    .setValue("1234");

                            editTextNewPhone.setText("");
                            setCurrentPhone();
                            Toast.makeText(getApplicationContext(),
                                    "New Phone number set sucessfully",
                                    Toast.LENGTH_LONG).show();

                        } else{
                            Toast.makeText(getApplicationContext(),
                                    "User ID doesn't exist!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Database error",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        buttonChangeDep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser = editTextUserId.getText().toString().trim().toUpperCase();
                if (TextUtils.isEmpty(currentUser)){
                    editTextUserId.setError("Enter valid User");
                    editTextUserId.requestFocus();
                    return;
                }

                final String u = currentUser;
                newDep = editTextNewDep.getText().toString().trim();

                if(newDep.isEmpty()){
                    editTextNewDep.setError("Enter Valid Dep. count");
                    editTextNewDep.requestFocus();
                    return;
                }

                depRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.hasChild(u)){
//                            RSA r= new RSA(aceKeys.publicKey,aceKeys.privateKey );
//                            try{
//                                newPhone = r.Encrypt(newPhone);
//                            }catch (Exception e){
//                                Log.e("changeerror",e.getMessage());
//                            }
                            depRefToUse.child(u)
                                    .child("depCount")
                                    .setValue(newDep);

                            editTextNewDep.setText("");
                            setCurrentPhone();
                            Toast.makeText(getApplicationContext(),
                                    "New Dependent(s) number set successfully",
                                    Toast.LENGTH_LONG).show();

                        } else{
                            Toast.makeText(getApplicationContext(),
                                    "User ID doesn't exist!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Database error",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void setCurrentPhone(){
        currentUser = editTextUserId.getText().toString().trim().toUpperCase();
        if (TextUtils.isEmpty(currentUser)){
            editTextUserId.setError("Enter valid User");
            editTextUserId.requestFocus();
            return;
        }

        final String u = currentUser;

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(u)){
//                    Log.e("DEBUG", snapshot.child(u).toString());
//                    Log.e("DEBUG", snapshot.child(u).child("mobno").getValue().toString() );
                    String mobno = snapshot.child(u).child("mobno").getValue().toString();
                    if(mobno.length()>100){
                        RSA r= new RSA();
                        try{
                            mobno = r.Decrypt(mobno);
                        }catch (Exception e){
                            Log.e("changeerror",e.getMessage());
                        }
                    }
                    textViewCurrentPhone.setText(mobno);
                } else{
                    Toast.makeText(getApplicationContext(),
                            "User ID doesn't exist or Error in fetching current details!",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database error",
                        Toast.LENGTH_LONG).show();
            }
        });

        depRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(u)){
//                    Log.e("DEBUG", snapshot.child(u).toString());
//                    Log.e("DEBUG", snapshot.child(u).child("mobno").getValue().toString() );
                    String dep = snapshot.child(u).child("depCount").getValue().toString();
//                    if(mobno.length()>100){
//                        RSA r= new RSA(aceKeys.publicKey,aceKeys.privateKey );
//                        try{
//                            mobno = r.Decrypt(mobno);
//                        }catch (Exception e){
//                            Log.e("changeerror",e.getMessage());
//                        }
//                    }
                    textViewCurrentDep.setText(dep);
                } else{
                    Toast.makeText(getApplicationContext(),
                            "User ID doesn't exist or Error in fetching current details!",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database error",
                        Toast.LENGTH_LONG).show();
            }
        });
    }



}
