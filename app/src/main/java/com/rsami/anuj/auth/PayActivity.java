package com.rsami.anuj.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PayActivity extends AppCompatActivityExt {

    private String TAG = "MainActivity";

    private String payeeAddress = "8698670658@upi";
    private String payeeName = "RAJ KUMAR";
    private String transactionNote = "Test for Deeplinking";
    private String amount = "0";
    private String currencyUnit = "INR";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);


        Intent intent = getIntent();
        int total = (Integer) intent.getSerializableExtra("price");
        amount = total + "";
        Button payButton = findViewById(R.id.pay);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("upi://pay?pa=" + payeeAddress + "&pn=" + payeeName + "&tn=" + transactionNote +
                        "&am=" + amount + "&cu=" + currencyUnit);

                /*
                    consult this link for further elaboration on deeplinking:-
                        https://stackoverflow.com/questions/44985944/upi-app-deep-linking-using-intent-inconsistent-and-buggy-behavior
                 */

               // Log.d(TAG, "onClick: uri: " + uri);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                Intent chooser = Intent.createChooser(intent, "Pay with...");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivityForResult(chooser, 1);
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // Log.d(TAG, "onActivityResult: requestCode: " + requestCode);
        //Log.d(TAG, "onActivityResult: resultCode: " + resultCode);
        //txnId=UPI20b6226edaef4c139ed7cc38710095a3&responseCode=00&ApprovalRefNo=null&Status=SUCCESS&txnRef=undefined
        //txnId=UPI608f070ee644467aa78d1ccf5c9ce39b&responseCode=ZM&ApprovalRefNo=null&Status=FAILURE&txnRef=undefined

        if (data != null) {
           // Log.d(TAG, "onActivityResult: data: " + data.getStringExtra("response"));
            String res = data.getStringExtra("response");
            String search = "SUCCESS";
            if (res.toLowerCase().contains(search.toLowerCase())) {
                Toast.makeText(this, "Payment Successful", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Payment Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}
