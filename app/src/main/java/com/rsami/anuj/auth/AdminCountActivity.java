package com.rsami.anuj.auth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rsami.anuj.auth.model.RSA;
import com.rsami.anuj.auth.model.aceKeys;
import com.rsami.anuj.auth.model.logModel;
import com.rsami.anuj.auth.model.reciptModel;
import com.rsami.anuj.auth.model.transec;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdminCountActivity extends AppCompatActivityExt implements AdapterView.OnItemSelectedListener {

    private int totalPrice;

    private Button confirmButton;

    public boolean isMemberAttending;
    public int dep_ct = 0, dep_ct_limit = 0,guest_ct_lim=6;
    public int guest_ct = 0;
    String dcount,mcount,gcount,type;
    private static int depPrice = 75;
    private static int guestPrice = 125;
    String iniseats="";
    String a="0",b="0",c="0",d="0";
    private String post_key = null;

    private Spinner spinner1, spinner2;

    private String movie_title = null;
    private String movie_date = null;

    private EditText rsiIdnum, rsiIDalpha, mobNo;
    private int size = 1;

    private logModel l;
    int inicost=0;

    boolean ret = false;

    private String id = null;
    private String num = null;

    private reciptModel r = null;

    private LinearLayout rsi, book;

    boolean f = false, spin_flag = false;

    DatabaseReference mDatabaseReference, mDatabase, databaseSettings, databaseDep;
    boolean flagGlobal = false;

    boolean log_p;
    int log_dep_lim, log_mem, log_guest, log_dep;

    ArrayAdapter<CharSequence> adapter, adapter_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_count);

        setTitle("BOOKING DETAILS");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } else {
            // Swap without transition
        }


        databaseSettings = FirebaseDatabase.getInstance().getReference("Settings");

        databaseSettings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                flagGlobal = false;

                depPrice = Integer.parseInt(dataSnapshot.child("memPrice").getValue().toString());
                guestPrice = Integer.parseInt(dataSnapshot.child("guePrice").getValue().toString());

                flagGlobal = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        post_key = getIntent().getExtras().getString("post_key");
        movie_title = getIntent().getExtras().getString("movie_n");
        movie_date = getIntent().getExtras().getString("movie_d");

        confirmButton = findViewById(R.id.admin_confirm_button);

        rsi = findViewById(R.id.rsiInfo);
        book = findViewById(R.id.bookingInfo);

        rsiIDalpha = findViewById(R.id.admin_rsiIDalpha);
        rsiIdnum = findViewById(R.id.admin_rsiIDnum);
        mobNo = findViewById(R.id.admin_mobno);

        rsiIDalpha.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(rsiIDalpha.getText().toString().length()==size)
                {
                    rsiIdnum.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        rsiIdnum.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(rsiIdnum.getText().toString().length()==0)
                {
                    rsiIDalpha.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flagGlobal) {
                    if(f) {

                        //Toast.makeText(AdminCountActivity.this, Integer.toString(dep_ct), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(AdminCountActivity.this, Integer.toString(guest_ct), Toast.LENGTH_SHORT).show();


                        if (!ret || dep_ct >= 0 && dep_ct <= 4 && guest_ct >= 0 && guest_ct <= 6) {
                            if (isMemberAttending) {
                                if (!ret || guest_ct + dep_ct >= 0) {
                                    if (isMemberAttending) {
                                        totalPrice = depPrice + dep_ct * depPrice + guest_ct * guestPrice;
                                    } else {
                                        totalPrice = dep_ct * depPrice + guest_ct * guestPrice;
                                    }
                                    if(totalPrice > 0) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminCountActivity.this);
                                        builder.setTitle("Confirmation");
                                        builder.setMessage("Total Price: " + Long.toString(totalPrice));
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                transec t = new transec();
                                                if (isMemberAttending)
                                                    t.setSeat_count(dep_ct + guest_ct + 1);
                                                else
                                                    t.setSeat_count(dep_ct + guest_ct);
                                                totalPrice = totalPrice + inicost;
                                                Log.e("summaryCostAdmin",inicost+"");
                                                t.setPrice(totalPrice);
                                                t.setPost_key(post_key);
                                                Intent i = new Intent(AdminCountActivity.this, SeatBooking.class);
                                                Log.e("summaryNull",mcount+" "+a);
                                                if(!mcount.equals("1")){
                                                    Log.e("summaryadmincount","asdasdtuqyiuq3o3");
                                                    mcount = Integer.parseInt(mcount)+Integer.parseInt(a)+"";
                                                }
                                                dcount = Integer.parseInt(dcount)+Integer.parseInt(b)+"";
                                                gcount = Integer.parseInt(gcount)+Integer.parseInt(c)+"";
                                                i.putExtra("Object", t);
                                                i.putExtra("dcount", dcount);
                                                i.putExtra("dcount_lim", Integer.toString(dep_ct_limit));
                                                i.putExtra("mcount", mcount);
                                                i.putExtra("gcount", gcount);
                                                i.putExtra("setlist", iniseats);
                                                i.putExtra("type","Provisional Ticket");
                                                i.putExtra("rsiID", id);
                                                Log.e("summaryadmincount","dcount "+dcount+" "+"mcount "+mcount+" gcount"+gcount);
                                            /*
                                            RSA rs = new RSA();
                                            try{
                                                num = rs.Decrypt(num);
                                            }catch (Exception e){
                                                Log.e("Decryting problem",e.getMessage());
                                                e.printStackTrace();
                                            }
                                            */
                                                i.putExtra("mobno", num);
                                                startActivity(i);
                                            }
                                        });

                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });

                                        builder.show();
                                    }
                                    else {
                                        Toast.makeText(AdminCountActivity.this, "Invalid Input!", Toast.LENGTH_SHORT).show();
                                    }
                                } else
                                    Toast.makeText(AdminCountActivity.this, "Guest without Member/Dependant not permitted!", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!ret || guest_ct + dep_ct >= 1 && dep_ct > 0) {
                                    if (isMemberAttending) {
                                        totalPrice = depPrice + dep_ct * depPrice + guest_ct * guestPrice;
                                    } else {
                                        totalPrice = dep_ct * depPrice + guest_ct * guestPrice;
                                    }
                                    if(totalPrice > 0) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminCountActivity.this);
                                        builder.setTitle("Confirmation");
                                        builder.setMessage("Total Price: " + Long.toString(totalPrice));
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                transec t = new transec();
                                                if (isMemberAttending)
                                                    t.setSeat_count(dep_ct + guest_ct + 1);
                                                else
                                                    t.setSeat_count(dep_ct + guest_ct);
                                                totalPrice = totalPrice + inicost;
                                                Log.e("summaryCostAdmin",inicost+"");
                                                t.setPrice(totalPrice);
                                                t.setPost_key(post_key);
                                                Intent i = new Intent(AdminCountActivity.this, SeatBooking.class);
                                                if(!mcount.equals("1")){
                                                    mcount = Integer.parseInt(mcount)+Integer.parseInt(a)+"";
                                                }
                                                dcount = Integer.parseInt(dcount)+Integer.parseInt(b)+"";
                                                gcount = Integer.parseInt(gcount)+Integer.parseInt(c)+"";
                                                i.putExtra("Object", t);
                                                i.putExtra("dcount", dcount);
                                                i.putExtra("dcount_lim", Integer.toString(dep_ct_limit));
                                                i.putExtra("mcount", mcount);
                                                i.putExtra("gcount", gcount);
                                                i.putExtra("setlist", iniseats);
                                                i.putExtra("type","Provisional Ticket");
                                                i.putExtra("rsiID", id);
                                                Log.e("summaryadmincount","dcount "+dcount+" "+"mcount "+mcount+" gcount"+gcount);
                                            /*
                                            RSA rs = new RSA();
                                            try{
                                                num = rs.Decrypt(num);
                                            }catch (Exception e){
                                                Log.e("Decryting problem",e.getMessage());
                                            }
                                            */
                                                i.putExtra("mobno", num);
                                                startActivity(i);
                                            }
                                        });

                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });

                                        builder.show();
                                    }
                                    else {
                                        Toast.makeText(AdminCountActivity.this, "Invlid input!", Toast.LENGTH_SHORT).show();
                                    }
                                } else
                                    Toast.makeText(AdminCountActivity.this, "Guest without Member/Dependant not permitted!", Toast.LENGTH_SHORT).show();
                            }
                        } else
                            Toast.makeText(AdminCountActivity.this, "Guest without Member/Dependant not permitted!", Toast.LENGTH_SHORT).show();
                    }

                    else {

                        id = rsiIDalpha.getText().toString().toUpperCase() + "-" + rsiIdnum.getText().toString().toUpperCase();
                        num = mobNo.getText().toString();

                        if(!id.toUpperCase().equals(Global.AdminID)) {
                            if(id != null && id != "" && num != null && num != "") {

                                mDatabase = FirebaseDatabase.getInstance().getReference("UserSignIn2");
                                mDatabase.keepSynced(true);

                                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.child(id.toUpperCase()).exists() ) {

                                            String mob = dataSnapshot.child(id.toUpperCase()).child("mobno").getValue().toString();
                                            if(mob.length()>100){ // number not encrypted
                                                try{
                                                    RSA r = new RSA();
                                                    mob = r.Decrypt(mob);
                                                }catch(Exception e){
                                                    Log.e("RSAErrorInDecrypting",e.getMessage());
                                                }
                                            }
                                            //Toast.makeText(AdminCountActivity.this, mob.equals(num)+"", Toast.LENGTH_SHORT).show();
                                            if(mob.equals(num)){

                                                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Summary").child(movie_date);
                                                reference.keepSynced(true);

                                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        if (dataSnapshot.child(id).exists()) {
                                                            l = dataSnapshot.child(id).getValue(logModel.class);
                                                            Log.e("summaryCostAdmininitial",inicost+"");
                                                            iniseats = l.seats;
                                                            inicost = Integer.parseInt(l.totalCost);
                                                            mcount = ""+Integer.parseInt(l.member);
                                                            dcount = ""+Integer.parseInt(l.dependents);
                                                            gcount = ""+Integer.parseInt(l.guest);
                                                            a = mcount;
                                                            b=dcount;
                                                            c=gcount;
                                                            Log.e("summaryadminbefore","dcount "+dcount+" "+"mcount "+mcount+" gcount"+gcount);
                                                            log_p = true;
                                                            if (l.date.equals(movie_date)) {
                                                                if (l.member.equals("1") && Integer.parseInt(l.dcount_lim) == Integer.parseInt(l.dependents)  && Integer.parseInt(l.guest) == 6) {
                                                                    ret = false;
                                                                    Toast.makeText(AdminCountActivity.this, "Tickets already booked upto limit!", Toast.LENGTH_SHORT).show();
                                                                }
                                                                else {
                                                                    ret = true;
                                                                    setSpinner(id.toUpperCase());

                                                                    f = true;
                                                                    confirmButton.setText("CONFIRM");
                                                                    rsiIDalpha.setEnabled(false);
                                                                    rsiIdnum.setEnabled(false);
                                                                    mobNo.setEnabled(false);
                                                                    book.setVisibility(View.VISIBLE);
                                                                }
                                                            }
                                                        }
                                                        else {
                                                            ret = true;
                                                            setSpinner(id.toUpperCase());

                                                            f = true;
                                                            confirmButton.setText("CONFIRM");
                                                            rsiIDalpha.setEnabled(false);
                                                            rsiIdnum.setEnabled(false);
                                                            mobNo.setEnabled(false);
                                                            book.setVisibility(View.VISIBLE);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                            else {
                                               // Toast.makeText(AdminCountActivity.this, mob + " :: " + num, Toast.LENGTH_SHORT).show();
                                                Toast.makeText(AdminCountActivity.this, "Invalid ID or MOBILE NUMBER", Toast.LENGTH_SHORT).show();
                                            }


                                        } else {
                                            Toast.makeText(AdminCountActivity.this, "Invalid ID or mobile number", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                            else {
                                Toast.makeText(AdminCountActivity.this, "All fields are necessary", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(AdminCountActivity.this, "Admin cannot book tickets for itself!", Toast.LENGTH_SHORT).show();
                        }


                    }
                }
                else {
                    Toast.makeText(AdminCountActivity.this, "Connection Slow! Please wait", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    public void setSpinner(String id) {

        try {
            databaseDep = FirebaseDatabase.getInstance().getReference("DepCount").child(id);

            databaseDep.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    int d_ct_lim = 0;

                    try {
                        dep_ct_limit = Integer.parseInt(dataSnapshot.child("depCount").getValue(String.class));
                        d_ct_lim = dep_ct_limit;

                        if(ret) {
                            //Toast.makeText(AdminCountActivity.this, dep_ct_limit + " : " + l.dependents, Toast.LENGTH_SHORT).show();
                            d_ct_lim = dep_ct_limit - Integer.parseInt(l.dependents);
                            //Toast.makeText(AdminCountActivity.this, dep_ct_limit + " : " + l.dependents, Toast.LENGTH_SHORT).show();
                            guest_ct_lim -= Integer.parseInt(l.guest);
                            if(Integer.parseInt(l.member) > 0) {
                                TextView tv = findViewById(R.id.mem_text_admin);
                                tv.setTextColor(Color.GRAY);
                                findViewById(R.id.admin_check_1).setEnabled(false);
                            }
                        }

                    } catch (Exception e) {
                        dep_ct_limit = 0;
                    }
                    spin_flag = true;
                    // Toast.makeText(AdminCountActivity.this, dep_ct_limit+"", Toast.LENGTH_SHORT).show();

                    //Toast.makeText(AdminCountActivity.this, dep_ct_limit+"", Toast.LENGTH_SHORT).show();

                    switch (d_ct_lim) {
                        case 0:
                            spinner1 = findViewById(R.id.admin_spinner_dependants);
                            adapter = ArrayAdapter.createFromResource(AdminCountActivity.this, R.array
                                    .dep_no_0, android.R.layout.simple_spinner_item);
                            TextView tv = findViewById(R.id.dep_text_admin);
                            tv.setTextColor(Color.GRAY);
                            findViewById(R.id.admin_spinner_dependants).setEnabled(false);
                            break;

                        case 1:
                            spinner1 = findViewById(R.id.admin_spinner_dependants);
                            adapter = ArrayAdapter.createFromResource(AdminCountActivity.this, R.array
                                    .dep_no_1, android.R.layout.simple_spinner_item);
                            break;

                        case 2:
                            spinner1 = findViewById(R.id.admin_spinner_dependants);
                            adapter = ArrayAdapter.createFromResource(AdminCountActivity.this, R.array
                                    .dep_no_2, android.R.layout.simple_spinner_item);
                            break;

                        case 3:
                            spinner1 = findViewById(R.id.admin_spinner_dependants);
                            adapter = ArrayAdapter.createFromResource(AdminCountActivity.this, R.array
                                    .dep_no_3, android.R.layout.simple_spinner_item);
                            break;

                        case 4:
                            spinner1 = findViewById(R.id.admin_spinner_dependants);
                            adapter = ArrayAdapter.createFromResource(AdminCountActivity.this, R.array
                                    .dep_no_4, android.R.layout.simple_spinner_item);
                            break;


                        case 5:
                            spinner1 = findViewById(R.id.admin_spinner_dependants);
                            adapter = ArrayAdapter.createFromResource(AdminCountActivity.this, R.array
                                    .dep_no_5, android.R.layout.simple_spinner_item);
                            break;

                        case 6:
                            spinner1 = findViewById(R.id.admin_spinner_dependants);
                            adapter = ArrayAdapter.createFromResource(AdminCountActivity.this, R.array
                                    .dep_no_6, android.R.layout.simple_spinner_item);
                            break;

                        default:
                            //calToast.makeText(AdminCountActivity.this, "**//**", Toast.LENGTH_SHORT).show();
                            spinner1 = findViewById(R.id.admin_spinner_dependants);
                            adapter = ArrayAdapter.createFromResource(AdminCountActivity.this, R.array
                                    .dep_no_0, android.R.layout.simple_spinner_item);
                            TextView tv1 = findViewById(R.id.dep_text_admin);
                            tv1.setTextColor(Color.GRAY);
                            findViewById(R.id.admin_spinner_dependants).setEnabled(false);
                            break;
                    }

                    switch (guest_ct_lim) {
                        case 0:
                            spinner2 = findViewById(R.id.admin_spinner_guests);
                            adapter_2 = ArrayAdapter.createFromResource(AdminCountActivity.this, R.array
                                    .dep_no_0, android.R.layout.simple_spinner_item);
                            TextView tv = findViewById(R.id.guest_text_admin);
                            tv.setTextColor(Color.GRAY);
                            findViewById(R.id.admin_spinner_guests).setEnabled(false);
                            break;

                        case 1:
                            spinner2 = findViewById(R.id.admin_spinner_guests);
                            adapter_2 = ArrayAdapter.createFromResource(AdminCountActivity.this, R.array
                                    .dep_no_1, android.R.layout.simple_spinner_item);
                            break;

                        case 2:
                            spinner2 = findViewById(R.id.admin_spinner_guests);
                            adapter_2 = ArrayAdapter.createFromResource(AdminCountActivity.this, R.array
                                    .dep_no_2, android.R.layout.simple_spinner_item);
                            break;

                        case 3:
                            spinner2 = findViewById(R.id.admin_spinner_guests);
                            adapter_2 = ArrayAdapter.createFromResource(AdminCountActivity.this, R.array
                                    .dep_no_3, android.R.layout.simple_spinner_item);
                            break;

                        case 4:
                            spinner2 = findViewById(R.id.admin_spinner_guests);
                            adapter_2 = ArrayAdapter.createFromResource(AdminCountActivity.this, R.array
                                    .dep_no_4, android.R.layout.simple_spinner_item);
                            break;


                        case 5:
                            spinner2 = findViewById(R.id.admin_spinner_guests);
                            adapter_2 = ArrayAdapter.createFromResource(AdminCountActivity.this, R.array
                                    .dep_no_5, android.R.layout.simple_spinner_item);
                            break;

                        case 6:
                            spinner2 = findViewById(R.id.admin_spinner_guests);
                            adapter_2 = ArrayAdapter.createFromResource(AdminCountActivity.this, R.array
                                    .dep_no_6, android.R.layout.simple_spinner_item);
                            break;

                        default:
                            spinner1 = findViewById(R.id.admin_spinner_dependants);
                            adapter = ArrayAdapter.createFromResource(AdminCountActivity.this, R.array
                                    .dep_no_0, android.R.layout.simple_spinner_item);
                            TextView tv1 = findViewById(R.id.guest_text_admin);
                            tv1.setTextColor(Color.GRAY);
                            findViewById(R.id.admin_spinner_guests).setEnabled(false);
                            break;
                    }

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner1.setAdapter(adapter);
                    spinner1.setSelection(0);
                    spinner1.setOnItemSelectedListener(AdminCountActivity.this);

                    adapter_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adapter_2);
                    spinner2.setSelection(0);
                    spinner2.setOnItemSelectedListener(AdminCountActivity.this);
                    /////////////////

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            dep_ct_limit = 0;

            spinner1 = findViewById(R.id.admin_spinner_dependants);
            adapter = ArrayAdapter.createFromResource(AdminCountActivity.this, R.array
                    .dep_no_0, android.R.layout.simple_spinner_item);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(adapter);
            spinner1.setSelection(0);
            spinner1.setOnItemSelectedListener(AdminCountActivity.this);
            /////////////////

        }

    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (parent.getId()) {
            case R.id.admin_spinner_dependants:
                dep_ct = position;
                dcount = position+"";
                // Whatever you want to happen when the second item gets selected
                break;
            case R.id.admin_spinner_guests:
                guest_ct = position;
                gcount = position+"";
                // Whatever you want to happen when the second item gets selected
                break;

        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.admin_check_1:
                if (checked) {
                    isMemberAttending = true;
                    mcount = 1+"";
                } else {
                    isMemberAttending = false;
                    mcount = 0+"";
                    break;
                }

        }
    }
}
