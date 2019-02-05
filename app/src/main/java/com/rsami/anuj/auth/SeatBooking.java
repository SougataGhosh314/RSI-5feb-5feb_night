package com.rsami.anuj.auth;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rsami.anuj.auth.SessionManagement.SessionManager;
import com.rsami.anuj.auth.model.reciptModel;
import com.rsami.anuj.auth.model.transec;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SeatBooking extends AppCompatActivityExt implements View.OnClickListener {
    ViewGroup layout;

    private static final String TAG = "SeatBooking";
    Button b[][] = new Button[17][23];
    List<String> list;
    GenericTypeIndicator<List<String>> genericTypeIndicator;
    int flg = 0;
    String dcount,mcount,gcount,type, dcount_lim;
    int seatStatus[] = new int[374];
    private int selectedSeats = 0, totalSeats;
    final int maxSeats = 10;
    String sts = "", tmp = "";
    List<String> seatNum = new ArrayList<String>();
    List<String> mappedValues = new ArrayList<String>();
    private ProgressDialog mProgress;

    SessionManager sessionManager;

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


    List<TextView> seatViewList = new ArrayList<>();
    int seatSize = 100;
    int seatGaping = 10;
    String iniseats="";
    int STATUS_AVAILABLE = 1;
    int STATUS_BOOKED = 2;
    int STATUS_RESERVED = 3;
    String selectedIds = "";

    transec bookingInfo = null;
    private DatabaseReference mDatabase;
    private String post_key = null;

    private String movie_title;
    private String movie_date;
    private String movie_time;

    private String id, mob = null;

    private Button print;

    StorageReference sref;

    private String profileImageUrl;

    private static final int REQUEST = 112;
    private Context mContext= SeatBooking.this;

    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_booking);

        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            } else {
                // Swap without transition
            }


            sessionManager = new SessionManager(this);
            id = sessionManager.getMemberShipNo();
            dcount =  getIntent().getExtras().getString("dcount");
            dcount_lim =  getIntent().getExtras().getString("dcount_lim");
            mcount =  getIntent().getExtras().getString("mcount");
            gcount =  getIntent().getExtras().getString("gcount");
            iniseats =  getIntent().getExtras().getString("setlist");
            type =  getIntent().getExtras().getString("type");

            if(id.equals(Global.AdminID)) {
                id = getIntent().getExtras().getString("rsiID").toUpperCase();
                mob = getIntent().getExtras().getString("mobno");
                sref = FirebaseStorage.getInstance().getReference();
                //Toast.makeText(SeatBooking.this, mob, Toast.LENGTH_SHORT).show();
            }

            layout = findViewById(R.id.layoutSeat);

            tmp = seats;
            seats = "/" + seats;     // add a back slash in seats *************************

            bookingInfo = (transec) getIntent().getSerializableExtra("Object");
            post_key = bookingInfo.getPost_key();
            totalSeats = bookingInfo.getSeat_count();

            readDatabase(post_key);
            //writeDatabase();

//        Toast.makeText(this, post_key, Toast.LENGTH_SHORT).show();

            mDatabase = FirebaseDatabase.getInstance().getReference().child("Movies");
            mDatabase.keepSynced(true);

            if (post_key != null) {
                mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        movie_title = dataSnapshot.child("name").getValue().toString();
                        movie_date = dataSnapshot.child("date").getValue().toString();
                        movie_time = dataSnapshot.child("timing").getValue().toString();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }


            findViewById(R.id.seatBook).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedSeats == totalSeats) {
                        Intent intent = new Intent(SeatBooking.this, Ticket.class);
                        reciptModel r = new reciptModel();
                        r.setCost(bookingInfo.getPrice());
                        java.util.Collections.sort(seatNum);
                        r.setSeatsList(seatNum);
                        r.setTimestamp(new Timestamp(System.currentTimeMillis()) + "");
                        r.setMovieNmae(movie_title);
                        r.setDate(movie_date);
                        r.setMovietime(movie_time);
                        r.setUserID(id);
                        intent.putExtra("Recipt", r);
                        intent.putExtra("post_key", post_key);
                        intent.putExtra("dcount", dcount);
                        intent.putExtra("dcount_lim", dcount_lim);
                        intent.putExtra("setlist", iniseats);
                        intent.putExtra("mcount", mcount);
                        intent.putExtra("gcount", gcount);
                        intent.putExtra("type",type);
                        Log.e("summarySeatbooking","dcount "+dcount+" "+"mcount "+mcount+" gcount"+gcount+" type "+type);
                        if(sessionManager.getMemberShipNo().equals(Global.AdminID)) {
                            intent.putExtra("rsiID", id);
                            intent.putExtra("mobno", mob);
                        }
                        startActivity(intent);
                        //Toast.makeText(SeatBooking.this, "sfasdf", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(SeatBooking.this, "Please Select " + totalSeats + " Seats", Toast.LENGTH_SHORT).show();
                    }
                }
            });




            if(sessionManager.getMemberShipNo().equals(Global.AdminID)) {

                print = findViewById(R.id.getLayout);

                print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        validatePermission();

                        if(flag) {

                            new asyn().execute();
                        }

                    }
                });
            }
        }catch (Exception e){
            Log.e("ErrorSeatBooking",e.getMessage());
        }


    }

    class  asyn extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress = new ProgressDialog(SeatBooking.this);
            mProgress.setCancelable(false);
            mProgress.setMessage("Please Wait...");
            mProgress.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            View u = findViewById(R.id.layoutSeat);

            HorizontalScrollView z = u.findViewById(R.id.layoutSeat);
            int totalHeight = z.getChildAt(0).getHeight();
            int totalWidth = z.getChildAt(0).getWidth();
            Bitmap b = getBitmapFromView(u, totalHeight, totalWidth);

            final Bitmap icon = b;


            try {
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);
                File sdcard = Environment.getExternalStorageDirectory();
                File myDir = new File(sdcard, "/RSAMI/");
                myDir.mkdirs();
                File outputFile = new File(myDir, formattedDate + ".jpg");
                FileOutputStream fos = new FileOutputStream(outputFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                icon.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
            }
            catch (Exception ex){
                Toast.makeText(SeatBooking.this,"Do again",Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgress.dismiss();
            Toast.makeText(SeatBooking.this,"Check file manager /RSAMI/",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do here
                } else {
                    Toast.makeText(mContext, "The app was not allowed to write in your storage", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void validatePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!hasPermissions(mContext, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST );
            } else {
                flag = true;
            }
        } else {
            flag = true;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(sessionManager.getMemberShipNo().equals(Global.AdminID)) {

            print.setVisibility(View.VISIBLE);

        }

    }


    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {

        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth,totalHeight , Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    private void uploadImageToFirebaseStorage(Bitmap b) {

        Bitmap icon = b;

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), icon, "Title", null);
        Uri uriProfileImage =  Uri.parse(path);


        final StorageReference storageReferenceSeats = FirebaseStorage.getInstance().getReference("seatlayoutinfo/"+System.currentTimeMillis()+".jpg");

        if (uriProfileImage != null){
            UploadTask uploadTask;
            uploadTask = storageReferenceSeats.putFile(uriProfileImage);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return storageReferenceSeats.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        Uri downloadUri = task.getResult();
                        profileImageUrl = downloadUri.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance()
                                .getReference("seatlayouturls");

                        String dateTime = DateFormat.getDateTimeInstance().format(new Date());
                        ref.child(dateTime).setValue(profileImageUrl);

                        try {
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                            URL url = new URL(profileImageUrl);
                            HttpURLConnection c = (HttpURLConnection) url.openConnection();
                            c.setRequestMethod("GET");
                            c.setDoOutput(true);
                            c.connect();

                            File sdcard = Environment.getExternalStorageDirectory();
                            File myDir = new File(sdcard, "/RSAMI/");
                            myDir.mkdirs();
                            File outputFile = new File(myDir, timeStamp+".png");
                            FileOutputStream fos = new FileOutputStream(outputFile);

                            InputStream is = new BufferedInputStream(url.openStream(), 8192);
                            byte[] buffer = new byte[1024];
                            int len1 = 0;
                            while ((len1 = is.read(buffer)) != -1) {
                                fos.write(buffer, 0, len1);
                            }
                            fos.flush();
                            fos.close();
                            is.close();
                        }
                        catch (Exception e){
                            Toast.makeText(SeatBooking.this, "Do again", Toast.LENGTH_SHORT).show();
                        }

//                        String url = profileImageUrl;
//                        Intent i = new Intent(Intent.ACTION_VIEW);
//                        i.setData(Uri.parse(url));
//                        startActivity(i);

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
    }

    void addMyseat(final int seatid){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("Movies").child(post_key).child("hall").child("Users");

        //Toast.makeText(this, ""+seatid, Toast.LENGTH_SHORT).show();
      //  Log.e("seats",seatid+"");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int s = seatid;
                String st = dataSnapshot.child(s+"").child("user").getValue(String.class);
              //  Log.e("remove",s+" "+st+" "+id);
                if(!dataSnapshot.child(s+"").exists()) {
                    database.getReference().child("Movies").child(post_key).child("hall").child("Users").child(seatid+"").child("user").setValue(id);
                    database.getReference().child("Movies").child(post_key).child("hall").child("Users").child(seatid+"").child("seat").setValue(seatid+"");
                    database.getReference().child("Movies").child(post_key).child("hall").child("Users").child(seatid+"").child("time").setValue(ServerValue.TIMESTAMP);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

      /*  reference.child(seatid+"").child("user").setValue(id);
        reference.child(seatid+"").child("time").setValue(ServerValue.TIMESTAMP);*/

    }

    void removeMyseat(final int seatid){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference().child("Movies").child(post_key).child("hall").child("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int s = seatid;
                String st = dataSnapshot.child(s+"").child("user").getValue(String.class);
              //  Log.e("remove",s+" "+st+" "+id);
                if(dataSnapshot.child(s+"").exists()){
                    if(dataSnapshot.child(s+"").child("user").getValue(String.class).equals(id)){
                       // Log.e("remove",s+" "+st);
                        FirebaseDatabase.getInstance().getReference().child("Movies").child(post_key).child("hall").child("Users").child(s+"").child("user").removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Movies").child(post_key).child("hall").child("Users").child(s+"").child("seat").removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Movies").child(post_key).child("hall").child("Users").child(s+"").child("time").removeValue();
                        // DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Movies").child(post_key).child("hall").child("status");
                        //ref.setValue("A");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void writeDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("hallB").child("status");
        int i;
        for (i = 0; i < 358; i++) {
            reference.child(i + "").setValue("A");

        }
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


          //  Log.e("map", mappedValues.get(i));
        }

    }


    void readDatabase(String post_key) {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Movies");
        mDatabase.keepSynced(true);

        DatabaseReference reference = mDatabase.child(post_key).child("hall").child("status");

        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference reference = database.getReference().child("hallB").child("status");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //ProgressBar progressBar = new ProgressBar(this);
                genericTypeIndicator = new GenericTypeIndicator<List<String>>() {
                };
                list = dataSnapshot.getValue(genericTypeIndicator);
                // Toast.makeText(SeatBooking.this, "Data Change"+list.get(0), Toast.LENGTH_SHORT).show();
                //Log.e("Seats Prev", ":" + tmp);
                int i = 0, j = 0, k = 0, l = 0;
                sts = "";
                try {
                    for (i = 0; i < tmp.length(); i++) {
                        if (tmp.charAt(i) == 'A') {
                            sts = sts + list.get(k);
                            k++;
                        } else
                            sts = sts + tmp.charAt(i);
                    }
                }
                catch (NullPointerException e) {
                  //  Log.e("SeatBooking", e.toString());
                }

                List<String> mappedValues = new ArrayList<String>();
                mapSeat();
//                Log.e("Seats Prev", ":" + tmp);

//                Log.e("Seats Noow", ":" + sts);

                seats = "/" + sts;
                design();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
             //   Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }


    void design() {

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
                view.setOnClickListener(this);
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
                view.setOnClickListener(this);
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
        if ((int) view.getTag() == STATUS_AVAILABLE && selectedSeats < totalSeats) {
            addMyseat(view.getId());
            view.setTag(STATUS_BOOKED);
            selectedSeats++;
            if (!seatNum.contains(mappedValues.get(view.getId()))) {
                seatNum.add(view.getId() + "");
            }
            view.setBackgroundResource(R.drawable.ic_seats_selected);
          //  Toast.makeText(this, "Seat " + mappedValues.get(view.getId()) + " is Booked", Toast.LENGTH_SHORT).show();
            //   Log.e("selected",seatNum.get(seatNum.size()-1));
        } else if ((int) view.getTag() == STATUS_BOOKED) {
            removeMyseat(view.getId());
            view.setTag(STATUS_AVAILABLE);
            selectedSeats--;
            if (seatNum.contains(view.getId() + "")) {
                seatNum.remove(view.getId() + "");
            }
            view.setBackgroundResource(R.drawable.ic_seats_book);
            //Log.e("selected",seatNum.get(seatNum.size()-1));
        } else if ((int) view.getTag() == STATUS_RESERVED) {

            Toast.makeText(this, "Seat " + mappedValues.get(view.getId()) + " is Reserved", Toast.LENGTH_SHORT).show();
        }
    }
}
