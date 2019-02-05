package com.rsami.anuj.auth;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rsami.anuj.auth.model.reciptModel;
import com.rsami.anuj.auth.model.transec;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class checkSeat extends AppCompatActivityExt implements View.OnClickListener {

    reciptModel r;
    ViewGroup layout;
    List<String> seatList;
    List<String> list;
    GenericTypeIndicator<List<String>> genericTypeIndicator;
    int flg=0;
    TextView stat;
    int chkflg=0;
    int seatStatus[] = new int[374];
    private int selectedSeats=0,totalSeats;
    final int maxSeats = 10;
    String sts="",tmp="";
    List<String> seatNum = new ArrayList<String>();
    List<String> mappedValues = new ArrayList<String>();
    List<TextView> seatViewList = new ArrayList<>();
    String seats = "AAAAAAAAAAA_AAAAAAAAAAA/"   //B         x24 x19
            + "_AAAAAAAAAA_AAAAAAAAAA_/"   //C
            + "AAAAAAAAAAA_AAAAAAAAAAA/"   //D
            + "_AAAAAAAAAA_AAAAAAAAAA_/"   //E
            + "AAAAAAAAAAA_AAAAAAAAAAA/"   //F
            + "_AAAAAAAAAA_AAAAAAAAAA_/"         //G
            + "AAAAAAAAAAA_AAAAAAAAAAA/"         //H
            + "_AAAAAAAAAA_AAAAAAAAAA_/"         //I
            + "_______________________/"       // PASSAGE
            + "AAAAAAAAAAA_AAAAAAAAAAA/"       //J
            + "_AAAAAAAAAA_AAAAAAAAAA_/"       //K
            + "AAAAAAAAAAA_AAAAAAAAAAA/"       //L
            + "_AAAAAAAAAA_AAAAAAAAAA_/"       //M
            + "AAAAAAAAAAA_AAAAAAAAAAA/"       //N
            + "_______________________/"   //PASSAGE
            + "_AAAAAAAAAA_AAAAAAAAAA_/"       //O
            + "AAAAAAAAAAA_AAAAAAAAAAA/"        //P
            + "_AAAAAAAAAA_AAAAAAAAAA_/"       //Q
            + "AAAAAAAAAAA_AAAAAAAAAAA/";      //R
    int seatSize = 100;
    int seatGaping = 10;

    int STATUS_AVAILABLE = 1;
    int STATUS_BOOKED = 2;
    int STATUS_RESERVED = 3;
    String selectedIds = "";

    transec bookingInfo = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_seat);


        r = (reciptModel) getIntent().getSerializableExtra("Seats");
        seatList = r.getSeatsList();
        stat = findViewById(R.id.chekStatus);
        layout = findViewById(R.id.checklayoutSeat);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        tmp=seats;
        seats = "/"+seats;
        mapSeat();
        readDatabase();
        //writeDatabase();
        findViewById(R.id.chkOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(checkSeat.this,QrScanActivity.class);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.chkReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(checkSeat.this);
                builder.setTitle("WARNING!!!");
                builder.setMessage("Are you sure you want to reset the seat layout!\nAll seat info will be removed.");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        writeDatabase();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();

            }
        });
    }


    void writeDatabase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("MovieAdmin").child("Hall");
        int i,j,k;
        for(i=0;i<358;i++){
            reference.child(i+"").setValue("A");

        }
    }

    void mapSeat(){
        int i,j=0,k=1,flg=0;    // 0-> 22 ****** 1-> 20
        for(i=0;i<358;i++){

            if(flg==0){

                mappedValues.add((char)('B'+j)+""+k);   k++;

            }
            if(flg==1){

                mappedValues.add((char)('B'+j)+""+k);   k++;

            }

            if(k==23 && flg==0) { j++; k=1; flg=1; }
            if(k==21 && flg==1) { j++; k=1; flg=0; }


          //  Log.e("map",mappedValues.get(i));
        }

    }

    void readDatabase(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("MovieAdmin").child("Hall");
        reference.addValueEventListener(new ValueEventListener() {
            public static final String TAG = "checkSeat";

            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //ProgressBar progressBar = new ProgressBar(this);
                genericTypeIndicator = new GenericTypeIndicator<List<String>>() {};
                list = dataSnapshot.getValue(genericTypeIndicator);
                // Toast.makeText(SeatBooking.this, "Data Change"+list.get(0), Toast.LENGTH_SHORT).show();
              //  Log.e("Seats Prev",":"+tmp);
                int i=0,j=0,k=0,l=0;
                sts="";
                for(i=0;i<tmp.length();i++){
                    if(tmp.charAt(i)=='A'){
                        sts=sts+list.get(k);    k++;
                    }
                    else
                        sts = sts+tmp.charAt(i);
                }

                List<String> mappedValues = new ArrayList<String>();
                mapSeat();
             //   Log.e("Seats Prev",":"+tmp);

             //   Log.e("Seats Noow",":"+sts);
                // Toast.makeText(checkSeat.this, "chechking seat "+list.get(0), Toast.LENGTH_SHORT).show();
                seats = "/"+sts;
              //  Log.e("Seats",seats);
                design();
                if(chkflg==0) {
                    chkflg=1;
                    chkSeats();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
             //   Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    void chkSeats(){
        // if currntly not reserved then update else dont
        int ok=1,s=0;
        for(int i=0;i<seatList.size();i++){
            if(list.get(Integer.parseInt(seatList.get(i))).equals("R")){ ok=0;  s=i; break;}
        }
        if(ok==0){

            stat.setText("Ehh seat "+mappedValues.get(Integer.parseInt(seatList.get(s)))+" already checked");
            stat.setBackgroundColor(Color.rgb(150,0,0));
        }
        else{
            // Toast.makeText(this, "Welcome Sir,", Toast.LENGTH_SHORT).show();
            stat.setText("Welcome Enjoy The Show !");
            stat.setBackgroundColor(Color.rgb(0,150,0));
            updateSeats();
        }

    }

    void updateSeats(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("MovieAdmin").child("Hall");
        for(int i=0;i<seatList.size();i++){
            reference.child(seatList.get(i)).setValue("R");
        }
    }



    void design(){

        LinearLayout layoutSeat = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutSeat.setOrientation(LinearLayout.VERTICAL);
        layoutSeat.setLayoutParams(params);
        layoutSeat.setPadding(8 * seatGaping, 8 * seatGaping, 8 * seatGaping, 8 * seatGaping);
        layout.removeAllViews();
        layout.addView(layoutSeat);

        LinearLayout layout = null;

        int count = -1;

        for (int index = 0; index < seats.length(); index++) {
            if (seats.charAt(index) == '/') {
                layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layoutSeat.addView(layout);
            } else if (seats.charAt(index) == 'U') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 28, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);
                view.setBackgroundResource(R.drawable.ic_seats_book);
                view.setTextColor(Color.WHITE);
                view.setTag(STATUS_BOOKED);
                view.setText(mappedValues.get(count));
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener(this);
            } else if (seats.charAt(index) == 'A') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 28, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);
                view.setBackgroundResource(R.drawable.ic_seats_book);
                view.setText(mappedValues.get(count));
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                view.setTextColor(Color.BLACK);
                view.setTag(STATUS_AVAILABLE);
                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener((View.OnClickListener) this);
            } else if (seats.charAt(index) == 'R') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 28, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);
                view.setBackgroundResource(R.drawable.ic_seats_reserved);
                view.setText(mappedValues.get(count));
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                view.setTextColor(Color.WHITE);
                view.setTag(STATUS_RESERVED);
                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener((View.OnClickListener) this);
            } else if (seats.charAt(index) == '_') {
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setBackgroundColor(Color.TRANSPARENT);
                view.setText("");
                layout.addView(view);
            }
        }
    }

    @Override
    public void onClick(View view) {



    }



}