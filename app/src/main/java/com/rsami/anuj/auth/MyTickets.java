package com.rsami.anuj.auth;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rsami.anuj.auth.SessionManagement.SessionManager;
import com.rsami.anuj.auth.model.TicketContent;
import com.rsami.anuj.auth.model.reciptModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class MyTickets extends AppCompatActivityExt {

    SessionManager sessionManager;

    private RecyclerView ticket_list_view;

    private String id = "";

    private Query query;
    private FirebaseRecyclerAdapter<TicketContent, TicketViewHolder> adapter;
    private DatabaseReference mDatabaseTickets;
    static List<String> mappedValues = new ArrayList<String>();

    Gson gson = null;

    TextView empty;

    private FirebaseRecyclerOptions<TicketContent> options;

    private Button delTickets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);
        try{

            setTitle("TICKETS");

            sessionManager = new SessionManager(this);

            id = sessionManager.getMemberShipNo();

            gson = new Gson();

            empty = findViewById(R.id.empty_ticket_view);

            mapSeat();

/*        if(sessionManager.getMemberShipNo().equals(Global.AdminID)) {
            query = FirebaseDatabase.getInstance().getReference().child("Tickets").child(sessionManager.getMemberShipNo()).orderByChild("userID");
        }
        else {
            query = FirebaseDatabase.getInstance().getReference().child("Tickets").child(sessionManager.getMemberShipNo()).orderByChild("date");
        }
*/
            Calendar calendar = Calendar.getInstance();

            int _year = calendar.get(Calendar.YEAR);
            int _month = calendar.get(Calendar.MONTH)+1;
            int _day = calendar.get(Calendar.DAY_OF_MONTH);

            String day, mon, yea;

            if(_day/10 == 0)
                day = "0" + _day;
            else
                day = _day + "";

            if(_month/10 == 0)
                mon = "0" + _month;
            else
                mon = _month + "";

            yea = _year + "";

            String t_date = day + "-" + mon + "-" + yea;

            Toast.makeText(this, t_date , Toast.LENGTH_SHORT).show();

            query = FirebaseDatabase.getInstance().getReference().child("Tickets").child(sessionManager.getMemberShipNo()).orderByChild("date").startAt(t_date);
            query.keepSynced(true);

            mDatabaseTickets = FirebaseDatabase.getInstance().getReference().child("Tickets").child(id);
            mDatabaseTickets.keepSynced(true);

            ticket_list_view = findViewById(R.id.ticket_view_list);
            ticket_list_view.setLayoutManager(new LinearLayoutManager(this));

            options = new FirebaseRecyclerOptions.Builder<TicketContent>()
                    .setQuery(query, TicketContent.class)
                    .build();

            delTickets = findViewById(R.id.delete_tickets);

            if(id.equals(Global.AdminID)) {
                delTickets.setVisibility(View.VISIBLE);

                delTickets.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MyTickets.this, DeleteTickets.class);
                        startActivityForResult(i, 1009);
                    }
                });

            }
        }catch (Exception e){
            Log.e("ErrorMyTicket",e.getMessage());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();

        adapter = new FirebaseRecyclerAdapter<TicketContent, TicketViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final TicketViewHolder holder, int position, @NonNull TicketContent model) {
                final String post_key = getRef(position).getKey();

                reciptModel r = new reciptModel();

                r.setCost(model.getCost());
                r.setDate(model.getDate());
                r.setMovieNmae(model.getMovieNmae());
                r.setMovietime(model.getMovietime());
                r.setSeatsList(model.getSeatsList());
                r.setTimestamp(model.getTimestamp());
                r.setUserID(model.getUserID());
                r.setProvisional(model.isProvisional());
                r.setCost(model.getCost());

                String text = gson.toJson(r);

                holder.setImage(MyTickets.this, text);
                holder.setTitle(model.getMovieNmae());
                holder.setDate(model.getDate());
                holder.setTime(model.getMovietime());
                holder.setSeats(model.getSeatsList());
                holder.setCost(String.valueOf(model.getCost()));
                holder.setColor(model.isProvisional());
                holder.setId(model.getUserID());

            }

            @NonNull
            @Override
            public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ticket_view, parent, false);

                return new TicketViewHolder(view);
            }



            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if(getItemCount() == 0) {
                    empty.setVisibility(View.VISIBLE);
                }
                else {
                    empty.setVisibility(View.GONE);
                }

            }
        };

        //ticket_list_view.scrollTo(30, 0);
        ticket_list_view.setAdapter(adapter);

        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
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


        }

    }

    public class TicketViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView qr_image;

        public TicketViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            qr_image = mView.findViewById(R.id.ticket_movie_qr_image);

        }

        public void setTitle(String mTitle) {
            TextView movie_title = mView.findViewById(R.id.ticket_movie_title);
            movie_title.setText(mTitle);
        }

        public void setDate(String mDate) {
            TextView movie_date = mView.findViewById(R.id.ticket_movie_date);
            movie_date.setText(mDate);
        }

        public void setTime(String mTime) {
            TextView movie_time = mView.findViewById(R.id.ticket_movie_time);
            movie_time.setText(mTime + " hr");
        }

        public void setSeats(List<String> seatsList) {
            TextView seatList = mView.findViewById(R.id.ticket_movie_seats);

            Iterator it = seatsList.iterator();
            int c = 0;
            String str = "";
            seatList.setText("");
            while (it.hasNext() && c <= 11) {
                str += mappedValues.get(Integer.parseInt(it.next().toString())) + ", ";
                c++;
            }
            str = str.substring(0, str.length() - 2);
            seatList.setText(str);

        }

        public void setCost(String c){
            TextView cost = mView.findViewById(R.id.ticket_price_cost);
            cost.setText("₹"+c);
        }

        public void setColor(boolean p) {
            TextView pro = mView.findViewById(R.id.provision_bool);
            LinearLayout ticketLayout = mView.findViewById(R.id.ticket_view);
            if(p) {
                pro.setText("Provisional");
                ticketLayout.setBackgroundColor(getResources().getColor(R.color.colourRed));
            }
            else {
                pro.setText("NOT Provisional");
                ticketLayout.setBackgroundColor(getResources().getColor(R.color.colourWhite));
            }
        }

        public void setId(String id) {
            TextView rsi_id_text = mView.findViewById(R.id.ticket_rsi_id);
            rsi_id_text.setText(id);
        }

        public void setImage(final Context context, final String text) {

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = null;
            try {
                bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 500, 500);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = null;
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qr_image.setImageBitmap(bitmap);

            qr_image.setScaleType(ImageView.ScaleType.FIT_CENTER);

        }

    }
}
