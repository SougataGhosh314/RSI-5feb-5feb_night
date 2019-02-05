package com.rsami.anuj.auth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rsami.anuj.auth.SessionManagement.SessionManager;
import com.rsami.anuj.auth.model.logModel;
import com.rsami.anuj.auth.model.reciptModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class MovieDetails extends AppCompatActivityExt {

    private static TextView uiname, uidate, uitime, dur, lang, cert;
    private static ImageView imageMainPage;
    private static Button watchTrailer, bookNow;
    private static RelativeLayout layout;

    private String post_key = null;

    private DatabaseReference mDatabase;
    DatabaseReference mDatabaseTickets;

    SessionManager sessionManager;

    String movie_title;
    String movie_date;
    String movie_time;
    String movie_image;

    String movie_duration;
    String movie_language;
    String movie_certification;

    String movie_trailer;

    Bitmap bitmap;
    logModel l;

    private String id;

    private boolean flag = true, processed = false, ret, log_p = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } else {
            // Swap without transition
        }

        layout = findViewById(R.id.back);

        uiname = findViewById(R.id.movieName);
        uidate = findViewById(R.id.movieDate);
        uitime = findViewById(R.id.movie_timing);

        dur = findViewById(R.id.durationID);
        lang = findViewById(R.id.language);
        cert = findViewById(R.id.certification);

//        watchTrailer = findViewById(R.id.watchTrailer);
        bookNow = findViewById(R.id.bookNow);
//        imageMainPage = findViewById(R.id.imageMainPage);

        id = sessionManager.getMemberShipNo();

        post_key = getIntent().getExtras().getString("post_key");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Movies");
        mDatabase.keepSynced(true);

        mDatabaseTickets = FirebaseDatabase.getInstance().getReference().child("Tickets").child(id);
        mDatabaseTickets.keepSynced(true);


//        Toast.makeText(this, post_key, Toast.LENGTH_SHORT).show();

        if (post_key != null) {
            mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    movie_title = dataSnapshot.child("name").getValue().toString();
                    movie_date = dataSnapshot.child("date").getValue().toString();
                    movie_time = dataSnapshot.child("timing").getValue().toString();
                    movie_image = dataSnapshot.child("image_url").getValue().toString();

                    movie_duration = dataSnapshot.child("duration").getValue().toString();
                    movie_language = dataSnapshot.child("language").getValue().toString();
                    movie_certification = dataSnapshot.child("certification").getValue().toString();

//                    movie_trailer = dataSnapshot.child("trailer").getValue().toString();

                    uiname.setText(movie_title);
                    uidate.setText(movie_date);
                    uitime.setText(movie_time + " hr");

                    dur.setText(movie_duration + " hr");
                    lang.setText(movie_language);
                    cert.setText(movie_certification);

                    Picasso.get().load(movie_image).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bit, Picasso.LoadedFrom from) {
                            bitmap = bit;
                        }
                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        }
                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    });
                    BitmapDrawable bd = new BitmapDrawable(getResources(), bitmap);
                    layout.setBackground(bd);

                    //getAvailableSeats();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


/*
        watchTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(movie_trailer)));
            }
        });

*/

        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id.equals(Global.AdminID)) {
                    Intent i = new Intent(MovieDetails.this, AdminCountActivity.class);

                    /*i.putExtra("bool_log", Boolean.toString(log_p));
                    //Toast.makeText(MovieDetails.this, "miva" + log_p, Toast.LENGTH_SHORT).show();
                    if (log_p) {
                        i.putExtra("dcount_lim", l.dcount_lim);
                        i.putExtra("member", l.member);
                        i.putExtra("dep", l.dependents);
                        i.putExtra("guest_ct", l.guest);
                    }*/

                    i.putExtra("post_key", post_key);
                    i.putExtra("movie_n", movie_title);
                    i.putExtra("movie_d", movie_date);
                    startActivity(i);
                }
                else {
                    if (flag) {
                        if (processed) {
                            Intent i = new Intent(MovieDetails.this, CountActivity.class);

                            i.putExtra("bool_log", Boolean.toString(log_p));
                            //Toast.makeText(MovieDetails.this, "miva" + log_p, Toast.LENGTH_SHORT).show();
                            if (log_p) {
                                i.putExtra("dcount_lim", l.dcount_lim);
                                i.putExtra("member", l.member);
                                i.putExtra("dep", l.dependents);
                                i.putExtra("guest_ct", l.guest);
                            }

                            i.putExtra("log_model", l);

                            i.putExtra("post_key", post_key);
                            i.putExtra("id", id);
                            startActivity(i);
                        } else {
                            Toast.makeText(MovieDetails.this, "Connection Slow, please wait!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MovieDetails.this, "Cannot book twice for the same show!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

       // Toast.makeText(this, ret+"", Toast.LENGTH_SHORT).show();

        if(!id.equals(Global.AdminID) && !ret) {
            mDatabaseTickets.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        reciptModel r = snapshot.getValue(reciptModel.class);
                        if (r.getMovieNmae().equals(movie_title) && r.getDate().equals(movie_date)) {
                            /*
                            bookNow.setBackgroundColor(Color.GRAY);
                            bookNow.setText("Already Booked");
                            flag = false;
                            */
                            getAvailableSeats(id);
                            break;
                        }
                    }
                    processed = true;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    public void getAvailableSeats(final String user_id) {

        ret = false;

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Summary").child(movie_date);
        reference.keepSynced(true);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user_id).exists()) {
                    l = dataSnapshot.child(user_id).getValue(logModel.class);
                    log_p = true;
                    //Toast.makeText(MovieDetails.this, "possible", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MovieDetails.this, l.movieName, Toast.LENGTH_SHORT).show();
                    if (l.date.equals(movie_date)) {
                        if (l.member.equals("1") && Integer.parseInt(l.dcount_lim) == Integer.parseInt(l.dependents) && Integer.parseInt(l.guest) == 6) {
                            ret = false;
                            //Toast.makeText(MovieDetails.this, "nahi", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(MovieDetails.this, l.member+" : "+l.dependents +" : "+l.dcount_lim, Toast.LENGTH_SHORT).show();
                            bookNow.setBackgroundColor(Color.GRAY);
                            bookNow.setText("Already Booked");
                            flag = false;
                        }
                        else {
                            ret = true;
                            //Toast.makeText(MovieDetails.this, l.member+" : "+l.dependents +" : "+l.dcount_lim, Toast.LENGTH_SHORT).show();
                            //Toast.makeText(MovieDetails.this, "kar sakta!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    //Toast.makeText(MovieDetails.this, "sdf", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        flag = true;
        processed = false;
    }
}
