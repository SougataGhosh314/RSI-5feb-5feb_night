package com.rsami.anuj.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.rsami.anuj.auth.SessionManagement.SessionManager;
import com.rsami.anuj.auth.model.aceKeys;
import com.rsami.anuj.auth.model.keyClass;
import com.rsami.anuj.auth.model.userModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivityExt {

    private FirebaseAuth mAuth;

    private EditText mMobno;
    private EditText mPass;
    String em, pa;
    private Button signInBtn;
    ProgressDialog mProgress;
    String id;
    private String pb,pr,sapi;
    static userModel user;
    SessionManager session;

    TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try{
            session = new SessionManager(this);
            if (session.isLoggedIn()) {
                Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            user = (userModel) getIntent().getSerializableExtra("userModel");
            pb = getIntent().getStringExtra("publicKey");
            pr = getIntent().getStringExtra("privateKey");
            sapi = getIntent().getStringExtra("smsapi");
            mAuth = FirebaseAuth.getInstance();

            mMobno = findViewById(R.id.signin_mobno_field);
            mPass = findViewById(R.id.singin_pass_field);
            signInBtn = findViewById(R.id.signin_button);

            signUp = findViewById(R.id.sign_up);
            signUp.setText(Html.fromHtml("<u>Sign Up/Forgot Password</u>"));

            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                    intent.putExtra("publicKey",pb);
                    intent.putExtra("privateKey",pr);
                    intent.putExtra("smsapi",sapi);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            });

            mProgress = new ProgressDialog(this);

            signInBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        mProgress.setMessage("Verifying...");
                        mProgress.show();

                        if (TextUtils.isDigitsOnly(mMobno.getText().toString()) && user.mobno.equals(mMobno.getText().toString()) && user.pass.equals(mPass.getText() + "")) {

                            Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                            Toast.makeText(LoginActivity.this, user.rsiID, Toast.LENGTH_SHORT).show();

                            session.createLoginSession(user.mobno, null, user.rsiID, null, null, null, null, null, null);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mProgress.dismiss();
                            startActivity(intent);
                        } else
                            Toast.makeText(LoginActivity.this, "Wrong Number or Password", Toast.LENGTH_SHORT).show();

                        mProgress.dismiss();
                    }
                    catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "Please Sign Up if here for the first time!", Toast.LENGTH_SHORT).show();
                        mProgress.dismiss();
                    }


                }
            });
        }catch (Exception e){
            Log.e("ErrorLogin",e.getMessage());
        }

    }

}