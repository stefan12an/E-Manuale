package com.stefan.reserv.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    String id;
    String title;
    String release_date;
    byte[] book_poster;

    public Book(String id, String title, String release_date, byte[] movie_poster) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.book_poster = movie_poster;
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
        return book_poster;
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
        dest.writeByteArray(book_poster);
    }
    public static final Parcelable.Creator<Book> CREATOR
            = new Parcelable.Creator<Book>() {
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    private Book(Parcel in) {
        id = in.readString();
        title = in.readString();
        release_date = in.readString();
        book_poster = in.createByteArray();
    }
}
