package com.rsami.anuj.auth;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rsami.anuj.auth.model.Movie;

import java.util.List;

class AdapterClass extends ArrayAdapter<Movie> {

    AdapterClass(Context context, List<Movie> userList) {
        super(context, 0, userList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.user_list_items, parent, false);
        }
        Movie currentMovie = getItem(position);

        TextView username = listItemView.findViewById(R.id.MovieTime);
        username.setText(currentMovie.getMovieTime());

        TextView name = listItemView.findViewById(R.id.MovieName);
        name.setText(currentMovie.getMovieName());


        TextView regNo = listItemView.findViewById(R.id.MovieDate);
        regNo.setText(String.valueOf(currentMovie.getMovieDate()));

        return listItemView;
    }

}
