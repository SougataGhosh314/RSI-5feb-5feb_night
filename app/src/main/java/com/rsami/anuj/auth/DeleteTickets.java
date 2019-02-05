package com.rsami.anuj.auth;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.rsami.anuj.auth.model.reciptModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class DeleteTickets extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    int _year,_month,_day;
    EditText dt;
    private int booking_limit = 5;
    Button btn;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_tickets);

        dt = findViewById(R.id.date_delete);
        btn = findViewById(R.id.delete_btn);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                String day, mon, yea;

                if(dayOfMonth/10 == 0)
                    day = "0" + dayOfMonth;
                else
                    day = dayOfMonth + "";

                if(month/10 == 0)
                    mon = "0" + month;
                else
                    mon = month + "";

                yea = year + "";

                date = day + "-" + mon + "-" + yea;

                dt.setText(date);

            }
        };
        dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                _year = calendar.get(Calendar.YEAR);
                _month = calendar.get(Calendar.MONTH);
                _day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(DeleteTickets.this,
                        mDateSetListener,
                        _year, _month, _day);
                dialog.show();

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dt.getText().equals("") || dt.getText() ==null){
                    Toast.makeText(DeleteTickets.this,"Enter the date first",Toast.LENGTH_LONG).show();
                }
                else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(DeleteTickets.this);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Confirm delete tickets of date: " + (date));
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteTickets();
                            finish();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.show();

                }
            }
        });

    }

    public void deleteTickets() {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Tickets");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for(DataSnapshot snapshotChild : snapshot.getChildren()) {
                        reciptModel r = snapshotChild.getValue(reciptModel.class);
                        if(r.getDate().equals(date)) {
//                            Log.e("asd", "tickets: " + r.getSeatsList().toString());
                            ref.child(snapshot.getKey()).child(snapshotChild.getKey()).removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}

