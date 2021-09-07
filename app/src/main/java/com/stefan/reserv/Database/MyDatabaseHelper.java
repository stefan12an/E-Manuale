package com.stefan.reserv.Database;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private final Context context;
    private static final String DATABASE_NAME = "reserv.db";
    private static final int DATABASE_VERSION = 11;

    //TABLE NAMES
    private static final String CINEMA_TABLE_NAME = "Cinema";
    private static final String MOVIE_TABLE_NAME = "Movie";
    private static final String USER_TABLE_NAME = "User";
    private static final String GENRE_TABLE_NAME = "Genre";
    private static final String MOVIE_GENRE_TABLE_NAME = "Movie_Genre";

    //COLUMNS CINEMA TABLE
    private static final String CINEMA_COLUMN_ID = "cinema_id";
    private static final String CINEMA_COLUMN_NAME = "cinema_name";
    private static final String CINEMA_COLUMN_LOCATION = "cinema_location";
    private static final String CINEMA_COLUMN_PHOTO = "cinema_photo";
    //COLUMNS USER TABLE
    private static final String USER_COLUMN_ID = "user_id";
    private static final String USER_COLUMN_EMAIL = "user_email";
    private static final String USER_COLUMN_PASSWORD = "user_password";
    private static final String USER_COLUMN_ROLE = "user_role";
    //COLUMNS MOVIE TABLE
    private static final String MOVIE_COLUMN_ID = "movie_id";
    private static final String MOVIE_COLUMN_TITLE = "movie_title";
    private static final String MOVIE_COLUMN_RELEASE_DATE = "movie_release_date";
    private static final String MOVIE_COLUMN_PHOTO = "movie_poster";

    //COLUMNS GENRE TABLE
    private static final String GENRE_COLUMN_ID = "genre_id";
    private static final String GENRE_COLUMN_NAME = "genre_name";
    //COLUMNS MOVIE_GENRE TABLE
    private static final String MOVIE_GENRE_COLUMN_ID = "_id";


    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_cinema = "CREATE TABLE " + CINEMA_TABLE_NAME +
                " (" + CINEMA_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CINEMA_COLUMN_NAME + " TEXT, " +
                CINEMA_COLUMN_LOCATION + " TEXT, " +
                CINEMA_COLUMN_PHOTO + " BLOB);";
        String query_movie = "CREATE TABLE " + MOVIE_TABLE_NAME +
                " (" + MOVIE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MOVIE_COLUMN_TITLE + " TEXT, " +
                MOVIE_COLUMN_RELEASE_DATE + " TEXT, " +
                MOVIE_COLUMN_PHOTO + " BLOB);";
        String query_user = "CREATE TABLE " + USER_TABLE_NAME +
                " (" + USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_COLUMN_EMAIL + " TEXT, " +
                USER_COLUMN_PASSWORD + " TEXT, " +
                USER_COLUMN_ROLE + " TEXT);";
        String query_genre = "CREATE TABLE " + GENRE_TABLE_NAME +
                " (" + GENRE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GENRE_COLUMN_NAME + " TEXT);";
        String query_genre_movie = "CREATE TABLE " + MOVIE_GENRE_TABLE_NAME +
                " (" + MOVIE_GENRE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MOVIE_COLUMN_ID + " INTEGER, " + "FOREIGN KEY(" + MOVIE_COLUMN_ID + ") REFERENCES " +
                MOVIE_TABLE_NAME + "(" + MOVIE_COLUMN_ID + "));";
        db.execSQL(query_cinema);
        db.execSQL(query_movie);
        db.execSQL(query_user);
        db.execSQL(query_genre);
        db.execSQL(query_genre_movie);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CINEMA_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MOVIE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GENRE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MOVIE_GENRE_TABLE_NAME);
        onCreate(db);
    }

    public void insertUserData(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USER_COLUMN_EMAIL, email);
        cv.put(USER_COLUMN_PASSWORD, password);
        cv.put(USER_COLUMN_ROLE, "admin");
        long result = db.insert(USER_TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to add data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully to add data", Toast.LENGTH_SHORT).show();

        }
    }

    public Cursor checkUserCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + USER_TABLE_NAME + " WHERE user_email= '" + email + "'" +
                " AND user_password= '" + password + "';";
        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        switch (cursor.getCount()) {
            case 0:
                Toast.makeText(context, "There is no user registered with these credentials", Toast.LENGTH_SHORT).show();
                return null;
            case 1:
                return cursor;
            default:
                Toast.makeText(context, "The e-mail and password you've entered are incorrect", Toast.LENGTH_SHORT).show();
        }
        return cursor;
    }

    public boolean checkForDuplicateAccount(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + USER_TABLE_NAME + " WHERE user_email= '" + email + "';";
        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        if (cursor.getCount() == 0) {
            return false;
        }
        return true;
    }

    public void insertMovieData(String name, byte[] photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String date = format1.format(currentTime);
        cv.put(MOVIE_COLUMN_TITLE, name);
        cv.put(MOVIE_COLUMN_RELEASE_DATE, date);
        cv.put(MOVIE_COLUMN_PHOTO, photo);
        long result = db.insert(MOVIE_TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to add data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully to add data", Toast.LENGTH_SHORT).show();

        }
    }

    public void insertCinemaData(String email, String password, byte[] photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CINEMA_COLUMN_NAME, email);
        cv.put(CINEMA_COLUMN_LOCATION, password);
        cv.put(CINEMA_COLUMN_PHOTO, photo);
        long result = db.insert(CINEMA_TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to add data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully to add data", Toast.LENGTH_SHORT).show();

        }
    }

    public boolean insertGenreData(String genre) {
        SQLiteDatabase w_db = this.getWritableDatabase();
        SQLiteDatabase r_db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        String query = "SELECT * FROM " + GENRE_TABLE_NAME + " WHERE genre_name= '" + genre + "';";
        Cursor cursor = null;
        if (r_db != null) {
            cursor = r_db.rawQuery(query, null);
        }
        if (cursor.getCount() == 0) {
            cv.put(GENRE_COLUMN_NAME, genre);
            long result = w_db.insert(GENRE_TABLE_NAME, null, cv);
            if (result == -1) {
                Toast.makeText(context, "Failed to add data", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Successfully to add data", Toast.LENGTH_SHORT).show();
                return true;
            }
        } else {
            Toast.makeText(context, "There is already a genre of this type", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void deleteCinemaData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + CINEMA_TABLE_NAME;
        db.execSQL(query);
    }

    public void deleteGenreData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + GENRE_TABLE_NAME;
        db.execSQL(query);
    }

    public Cursor readAllCinemaData() {
        String query = "SELECT * FROM " + CINEMA_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllGenreData() {
        String query = "SELECT * FROM " + GENRE_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllMovieData() {
        String query = "SELECT * FROM " + MOVIE_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

}
