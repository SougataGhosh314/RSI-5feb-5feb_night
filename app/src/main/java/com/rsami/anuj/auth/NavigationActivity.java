package com.rsami.anuj.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rsami.anuj.auth.SessionManagement.SessionManager;
import com.rsami.anuj.auth.model.aceKeys;
import com.rsami.anuj.auth.model.keyClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NavigationActivity extends AppCompatActivityExt
        implements NavigationView.OnNavigationItemSelectedListener  {

    SessionManager sessionManager;
    TextView rsmiText;
    View v;
    String bcrajpublic,bcrajprivate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
        setContentView(R.layout.activity_navigation);

        try{
            getKeys();
        }catch (Exception e){
            Log.e("ErrorNavigation",e.getMessage());
        }
    }

    private void getKeys() {


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("Keys");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                keyClass kc = dataSnapshot.getValue(keyClass.class);
                String pb = kc.pbl;
                String pr = kc.prv;
                String sapi = kc.getSmsapi();
                aceKeys.privateKey = pr;
                aceKeys.publicKey = pb;
                aceKeys.smsapi = sapi;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setClickListeners() {
        findViewById(R.id.sports_activity_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NavigationActivity.this,Sports.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.sanjog_hall_activity_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NavigationActivity.this,SanjogHall.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.bar_activity_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NavigationActivity.this,Bar.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.swimming_activity_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NavigationActivity.this,pool.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.party_activity_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NavigationActivity.this,PartyHall.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.trinco_activity_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NavigationActivity.this,FoodCourt  .class);
                startActivity(intent);
            }
        });
        findViewById(R.id.guest_activity_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NavigationActivity.this,Sports.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    finish();
                }
            }, 1000);

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_history) {
            Intent intent = new Intent(NavigationActivity.this, HistoryActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_facilities) {
            Intent intent = new Intent(NavigationActivity.this, FacilitiesActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_contact_us) {
            Intent intent = new Intent(NavigationActivity.this, ContactUsActivity.class);
            startActivity(intent);

        }  else if (id == R.id.nav_about_app) {
            Intent intent = new Intent(NavigationActivity.this, AboutAppActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_feedback) {
            Intent intent = new Intent(NavigationActivity.this, FeedbackActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_announcements) {
            Intent intent = new Intent(NavigationActivity.this, AnnouncementsActivity.class);
            startActivity(intent);
        }






        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nevmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
/*
        if (item.getItemId() == R.id.feedback_icon) {
            startActivity(new Intent(NavigationActivity.this, FeedbackActivity.class));
        }
*/
        ProgressDialog mProgress;
        if (item.getItemId() == R.id.logout_icon) {
            mProgress = new ProgressDialog(this);
            mProgress.setMessage("Signing Out");
            mProgress.show();

            sessionManager.logoutUser();
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        try{

            getKeys();

            NavigationView navigationView = findViewById(R.id.nav_view);
            if (navigationView != null) {
                String str = sessionManager.getMemberShipNo();
                Menu menu = navigationView.getMenu();
                v = navigationView.getHeaderView(0);
                rsmiText = v.findViewById(R.id.rsmIDNev);
                rsmiText.setText("RSAMI ID: "+str+"");
                navigationView.setNavigationItemSelectedListener(this);
            }

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            findViewById(R.id.movie_activity_image).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NavigationActivity.this, MovieActivity.class);
                    startActivity(intent);
                }
            });

            setClickListeners();
        }catch (Exception e){
            Log.e("ErrorNavigationOnstart",e.getMessage());
        }

    }
}