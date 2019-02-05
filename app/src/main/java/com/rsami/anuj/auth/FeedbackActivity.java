package com.rsami.anuj.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rsami.anuj.auth.SessionManagement.SessionManager;

public class FeedbackActivity extends AppCompatActivityExt {



    Button submitButton;

    EditText myFeedbackEditText;
    TextView memNo;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        sessionManager = new SessionManager(this);
        myFeedbackEditText = findViewById(R.id.myFeedback);
        memNo = findViewById(R.id.memberNo_f);
        memNo.setText(sessionManager.getMemberShipNo());
        submitButton = findViewById(R.id.submitFeedbackButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFeedBack();
            }
        });
    }

    public void sendFeedBack(){

        String[] addresses = {"ankurdewan2000@yahoo.co.in", "suryabhai.raj@gmail.com"};

        String uriText =
                "mailto:rsipune@gmail.com" +"?cc="+"anujsingh9710@gmail.com"+
                        "&subject=" + Uri.encode("RSAMI feedback") +
                        "&body=" + Uri.encode("From: "+memNo.getText().toString()+"\n\n"+myFeedbackEditText.getText().toString());

        Uri uri = Uri.parse(uriText);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.setData(uri);
        startActivityForResult(Intent.createChooser(intent, "Send Feedback"), 1001);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(this, "Feedback Sent.", Toast.LENGTH_SHORT).show();
    }
}
