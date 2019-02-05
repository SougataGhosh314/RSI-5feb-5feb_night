package com.rsami.anuj.auth;

import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.rsami.anuj.auth.model.logModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

public class SummaryAct extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    int _year,_month,_day;
    EditText dt;
    private int booking_limit = 5;
    Button btn;
    HSSFWorkbook workbook = new HSSFWorkbook();
    HSSFSheet sheet = workbook.createSheet("FirstSheet");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        dt = findViewById(R.id.date_summary);
        btn = findViewById(R.id.showSummary);

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

                String date = day + "-" + mon + "-" + yea;

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

                DatePickerDialog dialog = new DatePickerDialog(SummaryAct.this,
                        mDateSetListener,
                        _year, _month, _day);
                dialog.show();

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genSummary();
            }
        });
    }

    private void genSummary() {



        HSSFRow rowhead = sheet.createRow((short)0);
        rowhead.createCell(0).setCellValue("Date");
        rowhead.createCell(1).setCellValue("Movie Name");
        rowhead.createCell(2).setCellValue("RSI ID");
        rowhead.createCell(3).setCellValue("Seats");
        rowhead.createCell(4).setCellValue("Time");
        rowhead.createCell(5).setCellValue("Dependant(s)");
        rowhead.createCell(6).setCellValue("Guest(s)");
        rowhead.createCell(7).setCellValue("Member(s)");
        rowhead.createCell(8).setCellValue("Total Cost");
        rowhead.createCell(9).setCellValue("Type of Ticket");

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Summary").child(dt.getText()+"");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int x=1;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    logModel l = snapshot.getValue(logModel.class);

                    HSSFRow rowhead = sheet.createRow(x);
                    rowhead.createCell(0).setCellValue(l.date);
                    //  Log.e("Data",l.date);
                    rowhead.createCell(1).setCellValue(l.movieName);
                    //Log.e("Data",l.movieName);
                    rowhead.createCell(2).setCellValue(l.rsiID);
                    //Log.e("Data",l.rsiID);
                    rowhead.createCell(3).setCellValue(l.seats);
                    //Log.e("Data",l.seats);
                    rowhead.createCell(4).setCellValue(l.time);
                    //Log.e("Data",l.time);
                    rowhead.createCell(5).setCellValue(l.dependents);
                    //Log.e("Data",l.dependents);
                    rowhead.createCell(6).setCellValue(l.guest);
                    //Log.e("Data",l.guest);
                    rowhead.createCell(7).setCellValue(l.member);
                    //Log.e("Data",l.member);
                    rowhead.createCell(8).setCellValue(l.totalCost);
                    //Log.e("Data",l.totalCost);
                    rowhead.createCell(9).setCellValue(l.typeOfTicket);
                    //Log.e("Data",l.typeOfTicket);
                    x++;

                }

                if (ContextCompat.checkSelfPermission(SummaryAct.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // Do the file write
                } else {
                    // Request permission from the user
                    ActivityCompat.requestPermissions(SummaryAct.this,
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

                }


                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Summary");
                storageReference.child(dt.getText()+".xls").putBytes(workbook.getBytes());

                try {
                    File sdcard = Environment.getExternalStorageDirectory();
                    File myDir = new File(sdcard, "/RSAMI/");
                    myDir.mkdirs();
                    File outputFile = new File(myDir, dt.getText()+".xls");
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    workbook.write(fos);
                    fos.flush();
                    fos.close();
                    Toast.makeText(SummaryAct.this,dt.getText()+".xls is Stored in the FileManager/RSAMI  folder",Toast.LENGTH_LONG).show();
                    finish();
                }
                catch (Exception exc){
                    Toast.makeText(SummaryAct.this,"Unable to add to the Downloads :( ",Toast.LENGTH_LONG).show();
                }

                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}