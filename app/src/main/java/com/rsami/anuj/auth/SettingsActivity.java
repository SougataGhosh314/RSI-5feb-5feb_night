package com.rsami.anuj.auth;

import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends PreferenceActivity {

    EditTextPreference upi_id, mem_price, gue_price;

    DatabaseReference mDatabase;

    private String upi, g_price, m_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        mDatabase = FirebaseDatabase.getInstance().getReference("Settings");

        upi_id = (EditTextPreference) findPreference("upi_id");
        mem_price = (EditTextPreference) findPreference("mem_price");
        gue_price = (EditTextPreference) findPreference("gue_price");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                upi = dataSnapshot.child("upiID").getValue().toString();
                m_price = dataSnapshot.child("memPrice").getValue().toString();
                g_price = dataSnapshot.child("guePrice").getValue().toString();

                upi_id.setText(upi);
                mem_price.setText(m_price);
                gue_price.setText(g_price);

                upi_id.setSummary(upi);
                mem_price.setSummary("₹"+m_price);
                gue_price.setSummary("₹"+g_price);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        upi_id.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mDatabase.child("upiID").setValue(newValue);
                return true;
            }
        });

        mem_price.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mDatabase.child("memPrice").setValue(newValue);
                return true;
            }
        });

        gue_price.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mDatabase.child("guePrice").setValue(newValue);
                return true;
            }
        });


    }
}