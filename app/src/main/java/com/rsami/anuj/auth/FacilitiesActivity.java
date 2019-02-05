package com.rsami.anuj.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

public class FacilitiesActivity extends AppCompatActivityExt {

    CardView card1, card2, card3, card4, card5, card6, card7, card8, card9, card10, card11, card12, card13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilities);

        setViews();
        setOnClickListeners();

    }

    private void setOnClickListeners() {
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FacilitiesActivity.this, MovieActivity.class);
                startActivity(intent);
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FacilitiesActivity.this, "Coming Soon", Toast.LENGTH_LONG).show();
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FacilitiesActivity.this, "Coming Soon", Toast.LENGTH_LONG).show();
            }
        });

        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FacilitiesActivity.this, "Coming Soon", Toast.LENGTH_LONG).show();
            }
        });

        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FacilitiesActivity.this, "Coming Soon", Toast.LENGTH_LONG).show();
            }
        });

        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FacilitiesActivity.this, "Coming Soon", Toast.LENGTH_LONG).show();
            }
        });

        card7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FacilitiesActivity.this, "Coming Soon", Toast.LENGTH_LONG).show();
            }
        });

        card8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FacilitiesActivity.this, "Coming Soon", Toast.LENGTH_LONG).show();
            }
        });

        card9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FacilitiesActivity.this, "Coming Soon", Toast.LENGTH_LONG).show();
            }
        });

        card10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FacilitiesActivity.this, "Coming Soon", Toast.LENGTH_LONG).show();
            }
        });

        card11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FacilitiesActivity.this, "Coming Soon", Toast.LENGTH_LONG).show();
            }
        });

        card12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FacilitiesActivity.this, "Coming Soon", Toast.LENGTH_LONG).show();
            }
        });

        card13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FacilitiesActivity.this, "Coming Soon", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void setViews() {
        card1 = findViewById(R.id.card_1);
        card2 = findViewById(R.id.card_2);
        card3 = findViewById(R.id.card_3);
        card4 = findViewById(R.id.card_4);
        card5 = findViewById(R.id.card_5);
        card6 = findViewById(R.id.card_6);
        card7 = findViewById(R.id.card_7);
        card8 = findViewById(R.id.card_8);
        card9 = findViewById(R.id.card_9);
        card10 = findViewById(R.id.card_10);
        card11 = findViewById(R.id.card_11);
        card12 = findViewById(R.id.card_12);
        card13 = findViewById(R.id.card_13);

    }

}
