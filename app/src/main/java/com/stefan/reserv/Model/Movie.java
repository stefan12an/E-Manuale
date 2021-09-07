package com.stefan.reserv.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    String id;
    String title;
    String release_date;
    byte[] movie_poster;

    public Movie(String id, String title, String release_date, byte[] movie_poster) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.movie_poster = movie_poster;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public byte[] getMovie_poster() {
        return movie_poster;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(release_date);
        dest.writeByteArray(movie_poster);
    }
    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in) {
        id = in.readString();
        title = in.readString();
        release_date = in.readString();
        movie_poster = in.createByteArray();
    }
}
