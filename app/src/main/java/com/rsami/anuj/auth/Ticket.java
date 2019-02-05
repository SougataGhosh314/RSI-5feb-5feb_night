package com.rsami.anuj.auth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rsami.anuj.auth.SessionManagement.SessionManager;
import com.rsami.anuj.auth.model.logModel;
import com.rsami.anuj.auth.model.reciptModel;
import com.rsami.anuj.auth.smsService.sms;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Ticket extends AppCompatActivityExt {

    TextView movieName, date, time, userID, cost, seatList;
    ImageView qrImage;
    String text = null;
    reciptModel r = null;

    private String pb,pr,sapi;

    private static final String TAG = "MainActivity";
    String iniseats="";
    private String payeeAddress = "8698670658@upi";
    private String payeeName = "RSAMI PUNE";
    private String transactionNote = "Movie Seat Booking";
    private String amount = "0";
    private String currencyUnit = "INR";
    int selectedSeats = 0, totalSeats = 7;
    final int maxSeats = 10;
    List<String> mappedValues = new ArrayList<String>();
    List<String> seatNum = new ArrayList<String>();

    private DatabaseReference mDatabase, databaseSettings;

    private boolean flagGlobal = false;

    Gson gson = null;

    private String post_key = null;

    private String id, mob = null;

    private String seatsAvailable;

    static SessionManager sessionManager;
    String dcount,mcount,gcount,type,dcount_lim;
    Button confirm;
    boolean flag = false;
    boolean test_flag = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_recipt);

        try{
            sessionManager = new SessionManager(this);
//        getKeys();

            try{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                } else {
                    // Swap without transition
                }

                //Log.e(TAG, "API KEY ***: "+alpha);
                // Toast.makeText(this, alpha, Toast.LENGTH_SHORT).show();

                databaseSettings = FirebaseDatabase.getInstance().getReference("Settings");

                databaseSettings.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        flagGlobal = false;

                        payeeAddress = dataSnapshot.child("upiID").getValue().toString();

                        flagGlobal = true;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                post_key = getIntent().getExtras().getString("post_key");


                dcount =  getIntent().getExtras().getString("dcount");
                dcount_lim =  getIntent().getExtras().getString("dcount_lim");
                mcount =  getIntent().getExtras().getString("mcount");
                gcount =  getIntent().getExtras().getString("gcount");
                iniseats =  getIntent().getExtras().getString("setlist");
                if(iniseats==null) iniseats="";
                type =  getIntent().getExtras().getString("type");
                gson = new Gson();

                r = (reciptModel) getIntent().getSerializableExtra("Recipt");
                amount = Integer.toString(r.getCost());

                checkSeatList();

                confirm = findViewById(R.id.payMoney);

                try {
                    //15 minutes
                    if (System.currentTimeMillis() + 1800000 >= formatDate(r.getDate(), r.getMovietime()) && !sessionManager.getMemberShipNo().equals(Global.AdminID)) {
                        Toast.makeText(this, "Cannot book Tickets 30 min before the show!", Toast.LENGTH_SHORT).show();
                        confirm.setVisibility(View.GONE);
                        test_flag = false;
                    }

                }
                catch (Exception e) {

                }

                if(sessionManager.getMemberShipNo().equals(Global.AdminID)) {

                    id = getIntent().getExtras().getString("rsiID");
                    mob = getIntent().getExtras().getString("mobno");
                    // mob = SessionManager.getPno();
                    r.setUserID(id);
                    r.setProvisional(true);

                    // Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

                    //confirm.setText("Confirm Booking");
                    flag = true;

                }
                else {
                    r.setProvisional(false);
                }

                mapSeat();

                getInstances();

                seatNum = r.getSeatsList();

                checkValidity(seatNum);

                setValues();

                if(sessionManager.getMemberShipNo().equals(Global.AdminID)) {
                    try {
                        //GenrateQR();
                    } catch (Exception e) {
                        Toast.makeText(Ticket.this, "error generating qr !", Toast.LENGTH_LONG);
                    }
                }

//        addTicket();

                if(test_flag) {
                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            try {
                                //4x15 minutes
                                if (System.currentTimeMillis() + 4*900000 + 900000>= formatDate(r.getDate(), r.getMovietime())) {
                                    Toast.makeText(Ticket.this, "Cannot book Tickets 60 min before the show!", Toast.LENGTH_SHORT).show();
                                    confirm.setVisibility(View.GONE);
                                    test_flag = false;
                                    flag = true;
                                }

                            }
                            catch (Exception e) {

                            }

                            if(!flag) {
                                if(flagGlobal) {
                                    confirmBook();
/*                                Uri uri = Uri.parse("upi://pay?pa=" + payeeAddress + "&pn=" + payeeName + "&tn=" + transactionNote +
                                        "&am=" + amount + "&cu=" + currencyUnit);


                                //  Log.d(TAG, "onClick: uri: " + uri);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                Intent chooser = Intent.createChooser(intent, "Pay with...");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    startActivityForResult(chooser, 1);
                                }*/
                                }
                                else
                                    Toast.makeText(Ticket.this, "Connection Slow! Please wait", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if(sessionManager.getMemberShipNo().equals(Global.AdminID)) {
                                    confirmBook();
                                }
                            }

                        }
                    });
                }
            }catch (Exception e){
                Toast.makeText(this, "Something Went Wrong Please Bear With Us.", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Log.e("ErrorTicket",e.getMessage());
        }

        //sendSms();
    }

    void checkValidity(final List<String> seats){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Movies").child(post_key).child("hall").child("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> s = seats;
                for(int i = 0;i<s.size();i++){
                    String a = dataSnapshot.child(seats.get(i)+"").child("user").getValue(String.class);
                    if(sessionManager.getMemberShipNo().equals(Global.AdminID)) {
                        if(!r.getUserID().equals(a)){
                            //Toast.makeText(Ticket.this, "Please Select Seats Again !", Toast.LENGTH_SHORT).show();

                            // show alert and go back
                            AlertDialog.Builder Alert = new AlertDialog.Builder(Ticket.this);
                            Alert.setCancelable(false)
                                    .setTitle("ALERT!!")
                                    .setMessage("Your all seats are not available please select again and don't select "+mappedValues.get(Integer.parseInt(seats.get(i))));
                            Alert.setNegativeButton("Select Again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    finish();
                                }
                            });
                            Alert.create();
                            Alert.show();

                        }
                    }
                    else {
                        if(!sessionManager.getMemberShipNo().equals(a)){
                            //Toast.makeText(Ticket.this, "Please Select Seats Again !", Toast.LENGTH_SHORT).show();

                            // show alert and go back
                            AlertDialog.Builder Alert = new AlertDialog.Builder(Ticket.this);
                            Alert.setCancelable(false)
                                    .setTitle("ALERT!!")
                                    .setMessage("Your all seats are not available please select again and don't select "+mappedValues.get(Integer.parseInt(seats.get(i))));
                            Alert.setNegativeButton("Select Again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    finish();
                                }
                            });
                            Alert.create();
                            Alert.show();

                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    void mapSeat() {
        int i, j = 0, k = 1, flg = 0;    // 0-> 22 ****** 1-> 20
        for (i = 0; i < 358; i++) {

            if (flg == 0) {

                mappedValues.add((char) ('B' + j) + "" + k);
                k++;

            }
            if (flg == 1) {

                mappedValues.add((char) ('B' + j) + "" + k);
                k++;

            }

            if (k == 23 && flg == 0) {
                j++;
                k = 1;
                flg = 1;
            }
            if (k == 21 && flg == 1) {
                j++;
                k = 1;
                flg = 0;
            }


           // Log.e("map", mappedValues.get(i));
        }

    }

    void confirmBook() {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Movies");
        mDatabase.keepSynced(true);
        DatabaseReference reference = mDatabase.child(post_key).child("hall").child("status");
        for (int i = 0; i < seatNum.size(); i++) {
            reference.child(seatNum.get(i)).setValue("R");
        }

        DatabaseReference referenceSeats = mDatabase.child(post_key).child("available_seats");
        referenceSeats.setValue(Integer.toString(Integer.parseInt(MovieActivity.seatsAvailable) - seatNum.size()));

        logData();
        addTicket();


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
//                SessionManager.AddRecipt(r);
                //addTicket();

                try {
                 //   GenrateQR();
                } catch (Exception e) {
                    Toast.makeText(Ticket.this, "Error generating qr !", Toast.LENGTH_LONG);
                }

                confirmBook();
            } else {
                Toast.makeText(this, "Payment Failed!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean addTicket() {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Tickets").child(r.getUserID());

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                DatabaseReference newTicket = mDatabase.push();
                newTicket.setValue(r);

                sendSms();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference mDatabaseAdmin = FirebaseDatabase.getInstance().getReference().child("Tickets").child(Global.AdminID);

        mDatabaseAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference newTicket = mDatabaseAdmin.push();
                newTicket.setValue(r);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Intent i = new Intent(Ticket.this, MyTickets.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Staring Login Activity
        startActivity(i);
        finish();

        return true;
    }

    public static Long formatDate (String date, String time) throws ParseException {

        try {
            DateFormat formatter;
            formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date _date = formatter.parse(date + " " + time + ":00");
            java.sql.Timestamp timeStampDate = new Timestamp(_date.getTime());

//            Toast.makeText(this, timeStampDate.toString(), Toast.LENGTH_SHORT).show();
            return timeStampDate.getTime();
        } catch (ParseException e) {
            System.out.println("Exception :" + e);
        }

        return Long.parseLong("0");
    }

    private void sendSms() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Keys");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sapi = dataSnapshot.child("smsapi").getValue(String.class);
                String yourSeat = "";
                List<String> l = r.getSeatsList();
                for(int i=0;i<l.size()-1;i++){
                    yourSeat+=mappedValues.get(Integer.parseInt(l.get(i)))+", ";
                }
                yourSeat+=mappedValues.get(Integer.parseInt(l.get(l.size()-1)));
                String msg = "Dear "+r.getUserID()+", Seats : "+yourSeat+" confirmed for "+r.getMovieNmae().toUpperCase()+". Show starts at "+r.getMovietime()+" hr on "+r.getDate()+".";
                // String msg = "ticket booked "+r.getMovieNmae();
                if(sessionManager.getMemberShipNo().equals(Global.AdminID)) {
                    //msg = "Hi "+r.getUserID()+", Your Tickets for the movie "+r.getMovieNmae()+" have been booked. Your seats are: "+yourSeat+". Show starts at "+r.getMovietime()+", on "+r.getDate()+" Provisional Ticket, " + amount + " ₹ to be paid on arrival, regards RSAMI.";
                    msg = "Dear "+r.getUserID()+", Seats : "+yourSeat+" confirmed for "+r.getMovieNmae().toUpperCase()+". Show starts at "+r.getMovietime()+" hr on "+r.getDate()+"."+" Amount will be deducted from your RSAMI account.";
                    new Thread(new sms(msg,mob, sapi)).start();
                }
                else {
                    new Thread(new sms(msg,sessionManager.getPno(), sapi)).start();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    private void checkSeatList() {

        List<String> l = r.getSeatsList();
        int i,j,k;
        for(i=0;i<l.size();i++){
            for(j=i+1;j<l.size();j++){
                if(l.get(i).equals(l.get(j))){
                    AlertDialog.Builder Alert = new AlertDialog.Builder(Ticket.this);
                    Alert.setCancelable(false)
                            .setTitle("ALERT!!")
                            .setMessage("Something seems wrong with seat list please select again.");
                    Alert.setNegativeButton("Select Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            finish();
                        }
                    });
                    Alert.create();
                    Alert.show();
                }
            }
        }

    }



    private void GenrateQR() throws WriterException {
        qrImage.setVisibility(View.VISIBLE);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
//        text = r.getMovieNmae() + r.getTimestamp() + r.getUserID();
        String text = gson.toJson(r);
        BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,
                500, 500);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = null;
        bitmap = barcodeEncoder.createBitmap(bitMatrix);
        qrImage.setImageBitmap(bitmap);
    }

    private void setValues() {
        movieName.setText(r.getMovieNmae());
        date.setText(r.getDate());
        time.setText(r.getMovietime() + " hr");
        userID.setText(r.getUserID());
        cost.setText("₹" + String.valueOf(r.getCost()));
        String str = null;
        str = "";
        List<String> list = r.getSeatsList();
        Iterator it = list.iterator();
        int c = 0;
        seatList.setText("");
        while (it.hasNext() && c < 11) {
            str += mappedValues.get(Integer.parseInt(it.next().toString())) + ", ";
            c++;
        }
        str = str.substring(0, str.length() - 2);
        seatList.setText(str);
    }

    private void getInstances() {
        movieName = findViewById(R.id.movieName);
        date = findViewById(R.id.movieDate);
        time = findViewById(R.id.movieTime);
        userID = findViewById(R.id.userID);
        cost = findViewById(R.id.cost);
        seatList = findViewById(R.id.seatsList);
        qrImage = findViewById(R.id.qrImage);
    }

    private void logData() {

        logModel l = new logModel();
        Log.e("summarySeatTicket","dcount "+dcount+" "+"mcount "+mcount+" gcount"+gcount+" type "+type);
        l.date = date.getText()+"";
        l.time = time.getText()+"";
        l.seats = seatList.getText()+", "+iniseats;
        l.totalCost = String.valueOf(r.getCost());
        l.movieName = movieName.getText()+"";
        l.rsiID = userID.getText()+"";
        l.dependents = dcount;
        l.member = mcount;
        l.guest = gcount;
        l.typeOfTicket = type;
        l.dcount_lim = dcount_lim;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Summary").child(l.date).child(l.rsiID);
        reference.setValue(l);


    }

}