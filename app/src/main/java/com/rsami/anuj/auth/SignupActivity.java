package com.rsami.anuj.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rsami.anuj.auth.SessionManagement.SessionManager;
import com.rsami.anuj.auth.model.RSA;
import com.rsami.anuj.auth.model.userModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SignupActivity extends AppCompatActivityExt {

    private EditText name, rsiID, dob, contact, email, signupotp;
    private String verificationId;
    private EditText pass, passConfirm;
    String msg;
    private FirebaseAuth mAuth;
    String s_mob;
    private LinearLayout passwordLayout;
    int otpnum;
    Button sendotp,verifyotp,confirm;
    String num;
    DatabaseReference mDatabase;
    String pb,pr,sapi;
    boolean flag = false;

    SessionManager sessionManager;
    private userModel id = null;

    private String TAG = "SignupActivity";

//    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        try{
            mAuth = FirebaseAuth.getInstance();

//        Log.e(TAG, "API KEY ***: "+alpha);
//        Toast.makeText(this, alpha, Toast.LENGTH_SHORT).show();

            sessionManager = new SessionManager(this);

            id = (userModel) getIntent().getExtras().getSerializable("user");
            pb = getIntent().getStringExtra("publicKey");
            pr = getIntent().getStringExtra("privateKey");
            sapi = getIntent().getStringExtra("smsapi");


            rsiID = findViewById(R.id.signID);
            contact = findViewById(R.id.signContact);

            passwordLayout = findViewById(R.id.passwordLayout);

            pass = findViewById(R.id.signupPass);
            passConfirm = findViewById(R.id.signupRePass);
            sendotp = findViewById(R.id.signupSendOtp);
            verifyotp = findViewById(R.id.signupVerifyOtp);
            signupotp = findViewById(R.id.signupOTP);
            confirm = findViewById(R.id.signupConfirm);


            try{
                if(id.mobno.length()>10 || id.pass.length()>50)
                    Toast.makeText(this, "Unexpected Error Occured Please Try Again!", Toast.LENGTH_SHORT).show();
                else
                    contact.setText(id.mobno.charAt(0)+"XXXXXXX"+id.mobno.charAt(8)+id.mobno.charAt(9));
            }
            catch (Exception e){
                Toast.makeText(this, "Sorry Mobile No Is Invalid Please Contact RSI", Toast.LENGTH_SHORT).show();
            }
            contact.setEnabled(false);

            mDatabase = FirebaseDatabase.getInstance().getReference().child("UserSignIn2");

            rsiID.setText(id.rsiID);
            rsiID.setEnabled(false);

            char[] OTP = getOTP();
            otpnum = Integer.parseInt(String.valueOf(OTP));

            sendotp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Log.e("otp",otpnum+"");
                    //Toast.makeText(SignupActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                    //     msg = "Your OTP to set password is: "+otpnum+", regards RSAMI!";
                    num = id.mobno;

                    //Toast.makeText(SignupActivity.this, ""+msg+" "+num, Toast.LENGTH_SHORT).show();
                    //new Thread(new sms(msg, num, alpha)).start();

                    if(num.length()==10){

                        sendVerificationCode(num);

                        signupotp.setVisibility(View.VISIBLE);
                        sendotp.setVisibility(View.INVISIBLE);
                        verifyotp.setVisibility(View.VISIBLE);
                        sendotp.setVisibility(View.INVISIBLE);
                    }
                    else
                        Toast.makeText(SignupActivity.this, "Unexpected Error Occured Please Try Again!", Toast.LENGTH_SHORT).show();

                }
            });

            verifyotp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                try{

                    verifyotp.setVisibility(View.GONE);
                    verifyCode(signupotp.getText()+"");


                }catch (Exception e){
                    Log.e("OTPError",e.getMessage());
                }


                }
            });

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if((passConfirm.getText()+"").equals(pass.getText()+"")){
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference().child("UserSignIn2").child(id.rsiID);
                        userModel u = new userModel();
                        u.pass = pass.getText()+"";
                        u.rsiID = id.rsiID;
                        u.mobno = num;

                        if(u.pass.length()<100 && u.mobno.length()<100){
                            RSA r = new RSA();
                            try{
                                u.pass = r.Encrypt(u.pass);
                                u.mobno = r.Encrypt(u.mobno);
                                reference.setValue(u);
                            }
                            catch (Exception e){
                                Log.e("RSAErrorEncryption",e.getMessage());
                            }

                        }

                        u.pass = pass.getText()+"";
                        u.rsiID = id.rsiID;
                        u.mobno = num;


                        Toast.makeText(SignupActivity.this, "Successful.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                        intent.putExtra("userModel",u);
                        intent.putExtra("publicKey",pb);
                        intent.putExtra("privateKey",pr);
                        intent.putExtra("smsapi",sapi);
                        startActivity(intent);
                        finish();

                    }
                    else
                        Toast.makeText(SignupActivity.this, "Password Do Not Match", Toast.LENGTH_SHORT).show();

                    signupotp.setText("");
                    pass.setText("");
                    passConfirm.setText("");

                }
            });

        }catch (Exception e){
            Log.e("ErrorSignup",e.getMessage());
        }




    }
    private void sendVerificationCode(String number){
//        progressBar.setVisibility(View.VISIBLE);
        number = "+91"+number;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            Log.e("ErrorSendingSMS","OTP SENT");

        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            signupotp.setText(code);
            if (code != null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
         //   Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            Log.e("ErrorSendingSMS",e.getMessage());
            Toast.makeText(SignupActivity.this, "Some Problem Occured While Sending OTP please Retry!", Toast.LENGTH_SHORT).show();
        }
    };
    private void verifyCode(String code){
        try{
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
        }catch (Exception e){
            Log.e("OTPErrorVerCode",e.getMessage());
        }

    }

    private void signInWithCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            pass.setVisibility(View.VISIBLE);
                            passConfirm.setVisibility(View.VISIBLE);
                            confirm.setVisibility(View.VISIBLE);
                            verifyotp.setVisibility(View.GONE);


                        }else {
                            Toast.makeText(getApplicationContext(), "Wrong OTP"
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private char[] getOTP(){
        // Using numeric values
        String numbers = "0123456789";

        // Using random method
        Random rndm_method = new Random();

        char[] otp = new char[6];

        for (int i = 0; i < 6; i++)
        {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            otp[i] =
                    numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        return otp;
    }


}
