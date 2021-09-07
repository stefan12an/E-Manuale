package com.stefan.reserv.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stefan.reserv.Model.Movie;
import com.stefan.reserv.Model.User;
import com.stefan.reserv.R;

import java.io.ByteArrayInputStream;

public class MovieFragment extends Fragment {
    private Movie selected_movie;
    private TextView movie_title,movie_date,movie_description;
    private ImageView movie_poster;
    private User current_user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        movie_title = view.findViewById(R.id.movie_preview_title);
        movie_date = view.findViewById(R.id.movie_preview_date);
        movie_description = view.findViewById(R.id.movie_preview_description);
        movie_poster = view.findViewById(R.id.movie_preview_poster);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            selected_movie = bundle.getParcelable("movie");
            current_user = bundle.getParcelable("current_user");
            movie_title.setText(selected_movie.getTitle());
            movie_date.setText(selected_movie.getRelease_date());
            ByteArrayInputStream imageStream = new ByteArrayInputStream(selected_movie.getMovie_poster());
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            movie_poster.setImageBitmap(theImage);
        }
        return view;
    }
}