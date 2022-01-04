package com.stefan.reserv.Model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class Book implements Parcelable {
    String id;
    String title;
    String release_date;
    byte[] book_poster;
    String author;
    Boolean favorit;
    String id_clasa;
    String id_materie;

    public Book(String id, String title, String release_date, byte[] movie_poster, String author, Boolean favorit, String id_clasa, String id_materie) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.book_poster = movie_poster;
        this.author = author;
        this.favorit = favorit;
        this.id_clasa = id_clasa;
        this.id_materie = id_materie;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Boolean getFavorit() {
        return favorit;
    }

    public void setFavorit(Boolean favorit) {
        this.favorit = favorit;
    }

    public String getId_clasa() {
        return id_clasa;
    }

    public void setId_clasa(String id_clasa) {
        this.id_clasa = id_clasa;
    }

    public String getId_materie() {
        return id_materie;
    }

    public void setId_materie(String id_materie) {
        this.id_materie = id_materie;
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
        dest.writeString(author);
        dest.writeInt(favorit ? 1 : 0);
        dest.writeString(id_clasa);
        dest.writeString(id_materie);
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
        author = in.readString();
        favorit = in.readInt() == 1;
        id_clasa = in.readString();
        id_materie = in.readString();
    }
}
