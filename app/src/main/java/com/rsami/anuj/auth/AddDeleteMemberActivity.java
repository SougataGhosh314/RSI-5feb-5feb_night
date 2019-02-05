package com.rsami.anuj.auth;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AddDeleteMemberActivity extends AppCompatActivity {

    private LinearLayout linearLayoutAddMember, linearLayoutDeleteMember;
    private EditText editTextID, editTextAddID, editTextAddPhone, editTextAddDep;

    private Button buttonDelete, buttonAdd, buttonAddMember, buttonDeleteMember;

    DatabaseReference usersRef, usersRefToUse, depRef, depRefToUse;
    int flag1 = 1, flag2 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delete_member);

        findViews();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        buttonDeleteMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag1 == 1) {
                    linearLayoutDeleteMember.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutDeleteMember.setVisibility(View.GONE);
                }
                flag1 *= -1;
            }
        });
        buttonAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag2 == 1) {
                    linearLayoutAddMember.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutAddMember.setVisibility(View.GONE);
                }
                flag2 *= -1;
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(AddDeleteMemberActivity.this);
                builder1.setTitle("Deleting Member...");
                builder1.setMessage("Are you sure you want to delete this Member? " +
                        "Member once deleted can't be recovered unless you " +
                        "add it again providing all the details.");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                final String u = editTextID.getText().toString().trim();

                                if (TextUtils.isEmpty(u)){
                                    editTextID.setError("Enter valid User");
                                    editTextID.requestFocus();
                                    return;
                                }

                                usersRef = FirebaseDatabase.getInstance().getReference("UserSignIn");

                                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        if (snapshot.hasChild(u)){
//                            RSA r= new RSA(aceKeys.publicKey,aceKeys.privateKey );
//                            try{
//                                newPhone = r.Encrypt(newPhone);
//                            }catch (Exception e){
//                                Log.e("changeerror",e.getMessage());
//                            }
                                            usersRefToUse.child(u)
                                                    .removeValue();

                                            Toast.makeText(getApplicationContext(),
                                                    "Member deleted sucessfully",
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

                                depRef = FirebaseDatabase.getInstance().getReference("DepCount");

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
                                                    .removeValue();

//                            Toast.makeText(getApplicationContext(),
//                                    "Member deleted sucessfully",
//                                    Toast.LENGTH_LONG).show();

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

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                return;
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String u = editTextAddID.getText().toString().trim();
                String phone = editTextAddPhone.getText().toString().trim(),
                        dep = editTextAddDep.getText().toString().trim();

                if (TextUtils.isEmpty(u) || TextUtils.isEmpty(dep) || phone.length() != 10){
                    editTextAddID.setError("Enter valid Details");
                    editTextAddID.requestFocus();
                    return;
                }

                usersRef = FirebaseDatabase.getInstance().getReference("UserSignIn").child(u);
                Map<String, Object> updates = new HashMap<String,Object>();
                updates.put("mobno", phone);
                updates.put("rsiID", u);
                updates.put("pass", "1234");
                usersRef.updateChildren(updates);

                depRef = FirebaseDatabase.getInstance().getReference("DepCount").child(u);
                Map<String, Object> updates2 = new HashMap<String,Object>();
                updates2.put("depCount", dep);
                updates2.put("rsiID", u);
                depRef.updateChildren(updates2);

                Toast.makeText(getApplicationContext(), "Member Added Successfully",
                        Toast.LENGTH_LONG).show();

            }
        });
    }

    private void findViews() {
        linearLayoutDeleteMember = findViewById(R.id.linearLayoutDelete);
        linearLayoutAddMember = findViewById(R.id.linearLayoutAdd);
        editTextID = findViewById(R.id.editTextID);
        editTextAddID = findViewById(R.id.editTextAddID);
        editTextAddPhone = findViewById(R.id.editTextAddPhone);
        editTextAddDep = findViewById(R.id.editTextAddDep);

        buttonAddMember = findViewById(R.id.buttonAddMember);
        buttonDeleteMember = findViewById(R.id.buttonDeleteMember);

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonDelete = findViewById(R.id.buttonDelete);


        usersRefToUse = FirebaseDatabase.getInstance().getReference("UserSignIn");

        depRefToUse = FirebaseDatabase.getInstance().getReference("DepCount");
    }
}
