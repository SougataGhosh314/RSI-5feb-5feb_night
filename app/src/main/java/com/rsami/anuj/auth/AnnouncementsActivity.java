package com.rsami.anuj.auth;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rsami.anuj.auth.SessionManagement.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AnnouncementsActivity extends AppCompatActivityExt {

    SessionManager sessionManager;

    private EditText announcement_admin;
    private TextView announcement_user;
    private Button announceBtn;

    DatabaseReference mDatabase;

    private ProgressDialog mProgress;

    private String ann;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);

        sessionManager = new SessionManager(this);

        announcement_user = findViewById(R.id.announcements_text_user);
        announcement_admin = findViewById(R.id.announcements_text_admin);
        announceBtn = findViewById(R.id.announce_btn);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Announcements");

        mProgress = new ProgressDialog(AnnouncementsActivity.this);

        mProgress.setMessage("Loading Announcements");
        mProgress.show();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ann = dataSnapshot.getValue().toString();

                if(ann != null) {
                    announcement_user.setText(ann);
                    announcement_admin.setText(ann);
                }
                else
                    announcement_user.setText("No announcements to show!");

                mProgress.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AnnouncementsActivity.this, "Failed to load! Please try again", Toast.LENGTH_SHORT).show();
                mProgress.dismiss();
            }
        });

        if(sessionManager.getMemberShipNo().equals(Global.AdminID)) {

            announcement_admin.setVisibility(View.VISIBLE);
            announceBtn.setVisibility(View.VISIBLE);
            announcement_user.setBackgroundColor(Color.GRAY);

            announceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mProgress.setMessage("Making Announcement");
                    mProgress.show();

                    final String announce_val = announcement_admin.getText().toString();

                    if(announce_val != "" && announce_val != null) {

                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                mDatabase.setValue(announce_val);
                                Toast.makeText(AnnouncementsActivity.this, "Announcement made", Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();
//                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(AnnouncementsActivity.this, "Failed! Please try again", Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();
                            }
                        });

                    }
                    else {
                        Toast.makeText(AnnouncementsActivity.this, "Invlid Input!", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

    }



}
