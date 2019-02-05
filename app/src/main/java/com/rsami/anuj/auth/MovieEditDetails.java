package com.rsami.anuj.auth;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class MovieEditDetails extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 1001;

    private ImageButton addImg;
    private EditText titleTxt;
    private EditText dateTxt;
    private EditText timeTxt;
    private EditText duration, language, certificate, trailerUrl;

    private Button addBtn;

    private Uri uri = null;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private ProgressDialog mProgress;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private int _year, _month, _day;
    private int booking_limit = 5;

    TimePickerDialog timePickerDialog;

    private String post_key;

    String movie_title;
    String movie_date;
    String movie_time;
    String movie_image;

    String movie_duration;
    String movie_language;
    String movie_certification;

    Bitmap bitmap;

    boolean flagGlobal = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_edit_details);

        setTitle("EDIT BOOKING DETAILS");


        post_key = getIntent().getExtras().getString("post_key");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Movies");

        addImg = findViewById(R.id.add_image_btn1);
        titleTxt = findViewById(R.id.title_txt1);
        dateTxt = findViewById(R.id.date_txt1);
        timeTxt = findViewById(R.id.time_txt1);

        duration = findViewById(R.id.durationID1);
        language = findViewById(R.id.langID1);
        certificate = findViewById(R.id.catID1);

        if (post_key != null) {
            mDatabase.child(post_key).addListenerForSingleValueEvent(new ValueEventListener() {
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

                    titleTxt.setText(movie_title);
                    dateTxt.setText(movie_date);
                    timeTxt.setText(movie_time);

                    duration.setText(movie_duration);
                    language.setText(movie_language);
                    certificate.setText(movie_certification);

                    Picasso.get().load(movie_image).into(addImg, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(movie_image).into(addImg);
                        }
                    });

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

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                    uri = Uri.parse(path);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

//        trailerUrl = findViewById(R.id.trailerUrlID);

        addBtn = findViewById(R.id.add_btn1);

        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        //mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid().toString());

        mProgress = new ProgressDialog(MovieEditDetails.this);

        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });


        dateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                _year = calendar.get(Calendar.YEAR);
                _month = calendar.get(Calendar.MONTH);
                _day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MovieEditDetails.this,
                        mDateSetListener,
                        _year, _month, _day);

                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis() + booking_limit*86400000);

                dialog.show();
            }
        });

        timeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(MovieEditDetails.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String h = "";
                        String m = "";
                        if(hourOfDay/10 == 0)
                            h = "0"+hourOfDay;
                        else
                            h = hourOfDay+"";
                        if(minute/10 == 0)
                            m = "0"+minute;
                        else
                            m = minute+"";

                        timeTxt.setText(h + ":" + m);
                    }
                }, 0, 0, false);

                timePickerDialog.show();
            }
        });

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

//                String date = dayOfMonth + "-" + month + "-" + year;
                /*if (_year <= year && _month <= month && dayOfMonth - _day < booking_limit && dayOfMonth - _day >= 0)
                    dateTxt.setText(date);
                else {
                    dateTxt.setText("");
                    Toast.makeText(MovieEditDetails.this, "Invalid date", Toast.LENGTH_SHORT).show();
                }*/
                try {
                    if (System.currentTimeMillis() + booking_limit*86400000 >= formatDate(date, "00:00:00")) {
                        dateTxt.setText(date);
                        flagGlobal = true;
                    }
                    else {
                        dateTxt.setText("");
                        Toast.makeText(MovieEditDetails.this, "Invalid date", Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        };


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateImage()) {
                    final String title_val = titleTxt.getText().toString();
                    final String date_val = dateTxt.getText().toString();
                    final String time_val = timeTxt.getText().toString();

                    final String dur = duration.getText().toString();
                    final String lang = language.getText().toString();
                    final String cert = certificate.getText().toString();
//                final String trailer = trailerUrl.getText().toString();

                    String _date[] = date_val.split("-");

                    Calendar calendar = Calendar.getInstance();
                    _year = calendar.get(Calendar.YEAR);
                    _month = calendar.get(Calendar.MONTH);
                    _day = calendar.get(Calendar.DAY_OF_MONTH);

                    try {
                        if (System.currentTimeMillis() + booking_limit*86400000 >= formatDate(date_val, time_val+":00")) {
                            flagGlobal = true;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (flagGlobal) {
                        if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(date_val) && !TextUtils.isEmpty(time_val) && uri != null) {

                            String rand_name = UUID.randomUUID().toString();

                            final StorageReference path = mStorage.child("movie_images").child(rand_name);

                            mProgress.setMessage("Adding Movie");
                            mProgress.show();

                            path.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    return path.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        final Uri imageUrl = task.getResult();

                                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                DatabaseReference newMovie = mDatabase.child(post_key);

                                                newMovie.child("name").setValue(title_val);
                                                newMovie.child("date").setValue(date_val);
                                                newMovie.child("timing").setValue(time_val);
                                                newMovie.child("image_url").setValue(imageUrl.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        mProgress.dismiss();
                                                        Toast.makeText(MovieEditDetails.this, "Movie Added", Toast.LENGTH_SHORT).show();

                                                        finish();

                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                            overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
                                                        } else {
                                                            // Swap without transition
                                                        }

                                                    }
                                                });
/*
                                                DatabaseReference ref = newMovie.child("hall").child("status");
                                                int i, j, k;
                                                for (i = 0; i < 358; i++) {
                                                    ref.child(i + "").setValue("A");
                                                }
*/
                                                newMovie.child("duration").setValue(dur);
                                                newMovie.child("language").setValue(lang);
                                                newMovie.child("certification").setValue(cert);
//                                        newMovie.child("trailer").setValue(trailer);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    } else {
                                        Toast.makeText(MovieEditDetails.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(MovieEditDetails.this, "Must add something to each field!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(MovieEditDetails.this, "Invalid date!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(MovieEditDetails.this, "Make sure that the moview poster is in portrait form.", Toast.LENGTH_SHORT).show();

            }
        });

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

    public boolean validateImage() {
        double a = addImg.getDrawable().getBounds().height();
        double b = addImg.getDrawable().getBounds().width();
        b=b*1.3;
        if(a<b){
            AlertDialog.Builder Alert ;
            Alert = new AlertDialog.Builder(MovieEditDetails.this);
            Alert.setCancelable(false)
                    .setTitle("ALERT!!")
                    .setMessage("Insert a Portrait image please.");
            Alert.setNegativeButton("Select Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    addImg.destroyDrawingCache();
                    dialogInterface.dismiss();
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
                }
            });
            Alert.show();
            return false;
        }
        else
            return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
        } else {
            // Swap without transition
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            uri = data.getData();
            addImg.setImageURI(uri);
            addImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

            validateImage();

        }

    }

}
