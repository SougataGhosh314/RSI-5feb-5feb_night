package com.rsami.anuj.auth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rsami.anuj.auth.model.concurrencyModel;
import com.rsami.anuj.auth.model.logModel;
import com.rsami.anuj.auth.model.reciptModel;
import com.rsami.anuj.auth.model.transec;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

public class CountActivity extends AppCompatActivityExt implements AdapterView.OnItemSelectedListener {

    // private EditText dependentCount;
    // private EditText guestCount;

    private int totalPrice;

    private Button confirmButton;

    public boolean isMemberAttending;
    public int dep_ct = 0, dep_ct_limit=0, guest_ct_lim=6;
    public int guest_ct = 0;
    String dcount,mcount,gcount,type;

    private static int depPrice = 75;
    private static int guestPrice = 125;

    private String post_key = null;

    private Spinner spinner1, spinner2;

    private String movie_title = null;
    private String movie_date = null;

    private String id = null;

    logModel l;

    private reciptModel r = null;

    private TextView mem_p, gue_p;
    String iniseats;
    int inicost;
    String a="0",b="0",c="0",d="0";
    Long currentTimeStamp = Long.parseLong("0");

    boolean log_p;
    int log_dep_lim, log_mem, log_guest, log_dep;

    private ArrayAdapter<CharSequence> adapter, adapter_2;

    DatabaseReference mDatabaseReference, mDatabase, databaseSettings, databaseDep;
    boolean flagGlobal = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);

        setTitle("BOOKING DETAILS");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } else {
            // Swap without transition
        }

        log_p = Boolean.parseBoolean(getIntent().getExtras().getString("bool_log"));

        if(log_p) {

            l = (logModel) getIntent().getSerializableExtra("log_model");
            Log.e("summaryCostAdmininitial",inicost+"");
            iniseats = l.seats;
            inicost = Integer.parseInt(l.totalCost);
            mcount = ""+Integer.parseInt(l.member);
            dcount = ""+Integer.parseInt(l.dependents);
            gcount = ""+Integer.parseInt(l.guest);
            a = mcount;
            b=dcount;
            c=gcount;

            log_dep_lim = Integer.parseInt(getIntent().getExtras().getString("dcount_lim"));
            log_mem = Integer.parseInt(getIntent().getExtras().getString("member"));
            log_dep = Integer.parseInt(getIntent().getExtras().getString("dep"));
            log_guest = Integer.parseInt(getIntent().getExtras().getString("guest_ct"));
            guest_ct_lim -= log_guest;
            if(log_mem > 0) {
                TextView tv = findViewById(R.id.mem_text);
                tv.setTextColor(Color.GRAY);
                findViewById(R.id.check_1).setEnabled(false);
            }
        }

        //Toast.makeText(this, log_p+"", Toast.LENGTH_SHORT).show();

        databaseSettings = FirebaseDatabase.getInstance().getReference("Settings");

        mem_p = findViewById(R.id.pricing_mem);
        gue_p = findViewById(R.id.pricing_gue);

        databaseSettings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                flagGlobal = false;

                depPrice = Integer.parseInt(dataSnapshot.child("memPrice").getValue().toString());
                guestPrice = Integer.parseInt(dataSnapshot.child("guePrice").getValue().toString());

                mem_p.setText("Member/Dependant @ ₹" + depPrice);
                gue_p.setText("Guest @ ₹" + guestPrice);

                flagGlobal = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        post_key = getIntent().getExtras().getString("post_key");
        id = getIntent().getStringExtra("id");

        confirmButton = findViewById(R.id.confirm_button);

        setSpinner(id);

        //////////////

        /////////////////


        // dependentCount = findViewById(R.id.dep_count);
        // guestCount = findViewById(R.id.guest_count);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flagGlobal) {
                    if (log_p || dep_ct >= 0 && dep_ct <= 4 && guest_ct >= 0 && guest_ct <= 6) {
                        if (isMemberAttending) {
                            if (log_p || guest_ct + dep_ct >= 0) {
                                if (isMemberAttending) {
                                    totalPrice = depPrice + dep_ct * depPrice + guest_ct * guestPrice;
                                } else {
                                    totalPrice = dep_ct * depPrice + guest_ct * guestPrice;
                                }
                                if (totalPrice > 0) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CountActivity.this);
                                    builder.setTitle("Confirmation");
                                    builder.setMessage("Total Price: ₹" + Long.toString(totalPrice));
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
                                            Intent i = new Intent(CountActivity.this, SeatBooking.class);
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
                                            i.putExtra("setlist", iniseats);
                                            i.putExtra("gcount", gcount);
                                            i.putExtra("type","Paid Ticket");

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
                                    Toast.makeText(CountActivity.this, "Invalid Input!", Toast.LENGTH_SHORT).show();
                                }
                            } else
                                Toast.makeText(CountActivity.this, "Guest without Member/Dependant not permitted!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (log_p || guest_ct + dep_ct >= 1 && dep_ct > 0) {
                                if (isMemberAttending) {
                                    totalPrice = depPrice + dep_ct * depPrice + guest_ct * guestPrice;
                                } else {
                                    totalPrice = dep_ct * depPrice + guest_ct * guestPrice;
                                }
                                if (totalPrice > 0) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CountActivity.this);
                                    builder.setTitle("Confirmation");
                                    builder.setMessage("Total Price: ₹" + Long.toString(totalPrice));
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
                                            Intent i = new Intent(CountActivity.this, SeatBooking.class);
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
                                            i.putExtra("setlist", iniseats);
                                            i.putExtra("gcount", gcount);
                                            i.putExtra("type","Paid Ticket");

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
                                    Toast.makeText(CountActivity.this, "Invalid Input!", Toast.LENGTH_SHORT).show();
                                }
                            } else
                                Toast.makeText(CountActivity.this, "Guest without Member/Dependant not permitted!", Toast.LENGTH_SHORT).show();
                        }
                    } else
                        Toast.makeText(CountActivity.this, "Guest without Member/Dependant not permitted!", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(CountActivity.this, "Connection Slow! Please wait", Toast.LENGTH_SHORT).show();


            }
        });

        cleanSeats();

    }

    public void setSpinner(String id) {

        try {
            databaseDep = FirebaseDatabase.getInstance().getReference("DepCount").child(id);

            databaseDep.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    int dep_ct_temp = 0;

                    try {
                        dep_ct_limit = Integer.parseInt(dataSnapshot.child("depCount").getValue(String.class));
                        dep_ct_temp = dep_ct_limit;

                        if(log_p)
                            dep_ct_temp = dep_ct_limit - log_dep;

                    } catch (Exception e) {
                        dep_ct_limit = 0;
                    }
                    //Toast.makeText(CountActivity.this, dep_ct_limit+"", Toast.LENGTH_SHORT).show();

                    //Toast.makeText(CountActivity.this, dep_ct_limit+"", Toast.LENGTH_SHORT).show();

                    switch (dep_ct_temp) {
                        case 0:
                            spinner1 = findViewById(R.id.spinner_dependants);
                            adapter = ArrayAdapter.createFromResource(CountActivity.this, R.array
                                    .dep_no_0, android.R.layout.simple_spinner_item);
                            TextView tv = findViewById(R.id.dep_text);
                            tv.setTextColor(Color.GRAY);
                            findViewById(R.id.spinner_dependants).setEnabled(false);
                            break;

                        case 1:
                            spinner1 = findViewById(R.id.spinner_dependants);
                            adapter = ArrayAdapter.createFromResource(CountActivity.this, R.array
                                    .dep_no_1, android.R.layout.simple_spinner_item);
                            break;

                        case 2:
                            spinner1 = findViewById(R.id.spinner_dependants);
                            adapter = ArrayAdapter.createFromResource(CountActivity.this, R.array
                                    .dep_no_2, android.R.layout.simple_spinner_item);
                            break;

                        case 3:
                            spinner1 = findViewById(R.id.spinner_dependants);
                            adapter = ArrayAdapter.createFromResource(CountActivity.this, R.array
                                    .dep_no_3, android.R.layout.simple_spinner_item);
                            break;

                        case 4:
                            spinner1 = findViewById(R.id.spinner_dependants);
                            adapter = ArrayAdapter.createFromResource(CountActivity.this, R.array
                                    .dep_no_4, android.R.layout.simple_spinner_item);
                            break;


                        case 5:
                            spinner1 = findViewById(R.id.spinner_dependants);
                            adapter = ArrayAdapter.createFromResource(CountActivity.this, R.array
                                    .dep_no_5, android.R.layout.simple_spinner_item);
                            break;

                        case 6:
                            spinner1 = findViewById(R.id.spinner_dependants);
                            adapter = ArrayAdapter.createFromResource(CountActivity.this, R.array
                                    .dep_no_6, android.R.layout.simple_spinner_item);
                            break;

                        default:
                            spinner1 = findViewById(R.id.spinner_dependants);
                            adapter = ArrayAdapter.createFromResource(CountActivity.this, R.array
                                    .dep_no_0, android.R.layout.simple_spinner_item);
                            TextView tv1 = findViewById(R.id.dep_text);
                            tv1.setTextColor(Color.GRAY);
                            findViewById(R.id.spinner_dependants).setEnabled(false);
                            break;
                    }

                    switch (guest_ct_lim) {
                        case 0:
                            spinner2 = findViewById(R.id.spinner_guests);
                            adapter_2 = ArrayAdapter.createFromResource(CountActivity.this, R.array
                                    .dep_no_0, android.R.layout.simple_spinner_item);
                            TextView tv = findViewById(R.id.guest_text);
                            tv.setTextColor(Color.GRAY);
                            findViewById(R.id.spinner_guests).setEnabled(false);
                            break;

                        case 1:
                            spinner2 = findViewById(R.id.spinner_guests);
                            adapter_2 = ArrayAdapter.createFromResource(CountActivity.this, R.array
                                    .dep_no_1, android.R.layout.simple_spinner_item);
                            break;

                        case 2:
                            spinner2 = findViewById(R.id.spinner_guests);
                            adapter_2 = ArrayAdapter.createFromResource(CountActivity.this, R.array
                                    .dep_no_2, android.R.layout.simple_spinner_item);
                            break;

                        case 3:
                            spinner2 = findViewById(R.id.spinner_guests);
                            adapter_2 = ArrayAdapter.createFromResource(CountActivity.this, R.array
                                    .dep_no_3, android.R.layout.simple_spinner_item);
                            break;

                        case 4:
                            spinner2 = findViewById(R.id.spinner_guests);
                            adapter_2 = ArrayAdapter.createFromResource(CountActivity.this, R.array
                                    .dep_no_4, android.R.layout.simple_spinner_item);
                            break;


                        case 5:
                            spinner2 = findViewById(R.id.spinner_guests);
                            adapter_2 = ArrayAdapter.createFromResource(CountActivity.this, R.array
                                    .dep_no_5, android.R.layout.simple_spinner_item);
                            break;

                        case 6:
                            spinner2 = findViewById(R.id.spinner_guests);
                            adapter_2 = ArrayAdapter.createFromResource(CountActivity.this, R.array
                                    .dep_no_6, android.R.layout.simple_spinner_item);
                            break;

                        default:
                            spinner1 = findViewById(R.id.spinner_dependants);
                            adapter = ArrayAdapter.createFromResource(CountActivity.this, R.array
                                    .dep_no_0, android.R.layout.simple_spinner_item);
                            TextView tv1 = findViewById(R.id.guest_text);
                            tv1.setTextColor(Color.GRAY);
                            findViewById(R.id.spinner_guests).setEnabled(false);
                            break;
                    }

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner1.setAdapter(adapter);
                    spinner1.setSelection(0);
                    spinner1.setOnItemSelectedListener(CountActivity.this);

                    adapter_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adapter_2);
                    spinner2.setSelection(0);
                    spinner2.setOnItemSelectedListener(CountActivity.this);
                    //Toast.makeText(CountActivity.this, dep_ct_limit+"", Toast.LENGTH_SHORT).show();
                    /////////////////

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            dep_ct_limit = 0;

            spinner1 = findViewById(R.id.admin_spinner_dependants);
            adapter = ArrayAdapter.createFromResource(CountActivity.this, R.array
                    .dep_no_0, android.R.layout.simple_spinner_item);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(adapter);
            spinner1.setSelection(0);
            spinner1.setOnItemSelectedListener(CountActivity.this);
            /////////////////

        }

    }

    private void cleanSeats() {



        // Log.e("curretTime",currentTimeStamp+"");

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Movies");
        mDatabase.keepSynced(true);

        final DatabaseReference reference = mDatabase.child(post_key).child("hall").child("Users");

        final DatabaseReference refr = FirebaseDatabase.getInstance().getReference().child("Movies").child(post_key).child("hall").child("Users");
        refr.child(-1+"").child("time").setValue(ServerValue.TIMESTAMP);


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int x = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    concurrencyModel r = snapshot.getValue(concurrencyModel.class);
                    //  Log.e("times",r.getTime()+" "+r.getUser()+" "+r.getSeat());
                    if(x==0){
                        currentTimeStamp = r.getTime();
                        x++;
                    }
                    else {
                        if(currentTimeStamp - r.getTime()>300000){
                            //   Log.e("timeDiff",""+(r.getTime()-currentTimeStamp));
                            FirebaseDatabase.getInstance().getReference().child("Movies").child(post_key).child("hall").child("Users").child(r.getSeat()).removeValue();
                        }
                    }
                    //   Log.e("timeDiff",""+(r.getTime()-currentTimeStamp));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (parent.getId()) {
            case R.id.spinner_dependants:
                dep_ct = position;
                dcount = position+"";
                // Whatever you want to happen when the second item gets selected
                break;
            case R.id.spinner_guests:
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
            case R.id.check_1:
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