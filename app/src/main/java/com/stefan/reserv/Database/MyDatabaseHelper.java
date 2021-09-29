package com.stefan.reserv.Database;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stefan.reserv.Model.Movie;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    String email = "vms1238@gmail.com";
    String password = "1904stef";
    private final Context context;
    private static final String DATABASE_NAME = "reserv.db";
    private static final int DATABASE_VERSION = 18;

    //TABLE NAMES
    private static final String CINEMA_TABLE_NAME = "CINEMA";
    private static final String MOVIE_TABLE_NAME = "MOVIE";
    private static final String USER_TABLE_NAME = "USER";
    private static final String GENRE_TABLE_NAME = "GENRE";
    private static final String MOVIE_GENRE_TABLE_NAME = "MOVIE_GENRE";
    private static final String RESERVATION_TABLE_NAME = "RESERVATION";
    private static final String SEAT_TABLE_NAME = "SEAT";
    private static final String SEAT_RESERVATION_TABLE_NAME = "SEAT_RESERVATION";
    private static final String HALL_TABLE_NAME = "HALL";
    private static final String MOVIE_SCHEDULE_TABLE_NAME = "MOVIE_SCHEDULE";

    //COLUMNS CINEMA TABLE
    private static final String CINEMA_COLUMN_ID = "id";
    private static final String CINEMA_COLUMN_NAME = "name";
    private static final String CINEMA_COLUMN_LOCATION = "location";
    private static final String CINEMA_COLUMN_PHOTO = "photo";

    //COLUMNS USER TABLE
    private static final String USER_COLUMN_ID = "id";
    private static final String USER_COLUMN_EMAIL = "email";
    private static final String USER_COLUMN_PASSWORD = "password";
    private static final String USER_COLUMN_ROLE = "role";

    //COLUMNS MOVIE TABLE
    private static final String MOVIE_COLUMN_ID = "id";
    private static final String MOVIE_COLUMN_TITLE = "title";
    private static final String MOVIE_COLUMN_RELEASE_DATE = "release_date";
    private static final String MOVIE_COLUMN_PHOTO = "poster";

    //COLUMNS GENRE TABLE
    private static final String GENRE_COLUMN_ID = "id";
    private static final String GENRE_COLUMN_NAME = "name";

    //COLUMNS MOVIE_GENRE TABLE
    private static final String MOVIE_GENRE_COLUMN_ID = "id";
    private static final String MOVIE_GENRE_COLUMN_ID_MOVIE = "id_movie";
    private static final String MOVIE_GENRE_COLUMN_ID_GENRE = "id_genre";

    //COLUMNS HALL TABLE
    private static final String HALL_COLUMN_ID = "id";
    private static final String HALL_COLUMN_ID_CINEMA = "id_cinema";

    //COLUMNS SEAT TABLE
    private static final String SEAT_COLUMN_ID_ROW = "id_row";
    private static final String SEAT_COLUMN_ID_COLUMN = "id_column";
    private static final String SEAT_COLUMN_ID_HALL = "id_hall";

    //COLUMNS RESERVATION TABLE
    private static final String RESERVATION_COLUMN_ID = "id";
    private static final String RESERVATION_COLUMN_ID_USER = "id_user";
    private static final String RESERVATION_COLUMN_ID_SCHEDULE = "id_schedule";

    //COLUMNS SEAT_RESERVATION TABLE
    private static final String SEAT_RESERVATION_COLUMN_ID = "id";
    private static final String SEAT_RESERVATION_COLUMN_ID_ROW = "id_row";
    private static final String SEAT_RESERVATION_COLUMN_ID_COLUMN = "id_column";
    private static final String SEAT_RESERVATION_COLUMN_ID_RESERVATION = "id_reservation";

    //COLUMNS MOVIE_SCHEDULE TABLE
    private static final String MOVIE_SCHEDULE_COLUMN_ID = "id";
    private static final String MOVIE_SCHEDULE_COLUMN_ID_MOVIE = "id_movie";
    private static final String MOVIE_SCHEDULE_COLUMN_ID_HALL = "id_hall";
    private static final String MOVIE_SCHEDULE_COLUMN_START_HOUR = "start_hour";
    private static final String MOVIE_SCHEDULE_COLUMN_DURATION = "duration";


    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_cinema = "CREATE TABLE " + CINEMA_TABLE_NAME + " ("
                + CINEMA_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CINEMA_COLUMN_NAME + " TEXT, "
                + CINEMA_COLUMN_LOCATION + " TEXT, "
                + CINEMA_COLUMN_PHOTO + " BLOB);";

        String query_movie = "CREATE TABLE " + MOVIE_TABLE_NAME + " ("
                + MOVIE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MOVIE_COLUMN_TITLE + " TEXT, "
                + MOVIE_COLUMN_RELEASE_DATE + " TEXT, "
                + MOVIE_COLUMN_PHOTO + " BLOB);";

        String query_user = "CREATE TABLE " + USER_TABLE_NAME + " ("
                + USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_COLUMN_EMAIL + " TEXT, "
                + USER_COLUMN_PASSWORD + " TEXT, "
                + USER_COLUMN_ROLE + " TEXT);";

        String query_genre = "CREATE TABLE " + GENRE_TABLE_NAME + " ("
                + GENRE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GENRE_COLUMN_NAME + " TEXT);";

        String query_genre_movie = "CREATE TABLE " + MOVIE_GENRE_TABLE_NAME + " ("
                + MOVIE_GENRE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MOVIE_GENRE_COLUMN_ID_MOVIE + " INTEGER, "
                + MOVIE_GENRE_COLUMN_ID_GENRE + " INTEGER, "
                + "FOREIGN KEY(" + MOVIE_GENRE_COLUMN_ID_MOVIE + ") REFERENCES " + MOVIE_TABLE_NAME + "(" + MOVIE_COLUMN_ID + "), "
                + "FOREIGN KEY(" + MOVIE_GENRE_COLUMN_ID_GENRE + ") REFERENCES " + GENRE_TABLE_NAME + "(" + GENRE_COLUMN_ID + "));";

        String query_hall = "CREATE TABLE " + HALL_TABLE_NAME + " ("
                + HALL_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HALL_COLUMN_ID_CINEMA + " INTEGER, "
                + "FOREIGN KEY(" + HALL_COLUMN_ID_CINEMA + ") REFERENCES " + CINEMA_TABLE_NAME + "(" + CINEMA_COLUMN_ID + "));";

        String query_seat = "CREATE TABLE " + SEAT_TABLE_NAME + " ("
                + SEAT_COLUMN_ID_ROW + " INTEGER, "
                + SEAT_COLUMN_ID_COLUMN + " INTEGER, "
                + SEAT_COLUMN_ID_HALL + " INTEGER, "
                + "PRIMARY KEY (" + SEAT_COLUMN_ID_ROW + ", " + SEAT_COLUMN_ID_COLUMN + "), "
                + "FOREIGN KEY(" + SEAT_COLUMN_ID_HALL + ") REFERENCES " + HALL_TABLE_NAME + "(" + HALL_COLUMN_ID + "));";

        String query_movie_schedule = "CREATE TABLE " + MOVIE_SCHEDULE_TABLE_NAME + " ("
                + MOVIE_SCHEDULE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MOVIE_SCHEDULE_COLUMN_ID_MOVIE + " INTEGER, "
                + MOVIE_SCHEDULE_COLUMN_ID_HALL + " INTEGER, "
                + MOVIE_SCHEDULE_COLUMN_START_HOUR + " TEXT, "
                + MOVIE_SCHEDULE_COLUMN_DURATION + " TEXT, "
                + "FOREIGN KEY(" + MOVIE_SCHEDULE_COLUMN_ID_MOVIE + ") REFERENCES " + MOVIE_TABLE_NAME + "(" + MOVIE_COLUMN_ID + "), "
                + "FOREIGN KEY(" + MOVIE_SCHEDULE_COLUMN_ID_HALL + ") REFERENCES " + HALL_TABLE_NAME + "(" + HALL_COLUMN_ID + "));";

        String query_reservation = "CREATE TABLE " + RESERVATION_TABLE_NAME + " ("
                + RESERVATION_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RESERVATION_COLUMN_ID_USER + " INTEGER, "
                + RESERVATION_COLUMN_ID_SCHEDULE + " INTEGER, "
                + "FOREIGN KEY(" + RESERVATION_COLUMN_ID_USER + ") REFERENCES " + USER_TABLE_NAME + "(" + USER_COLUMN_ID + "), "
                + "FOREIGN KEY(" + RESERVATION_COLUMN_ID_SCHEDULE + ") REFERENCES " + MOVIE_SCHEDULE_TABLE_NAME + "(" + MOVIE_SCHEDULE_COLUMN_ID + "));";

        String query_seat_reservation = "CREATE TABLE " + SEAT_RESERVATION_TABLE_NAME + " ("
                + SEAT_RESERVATION_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SEAT_RESERVATION_COLUMN_ID_ROW + " INTEGER, "
                + SEAT_RESERVATION_COLUMN_ID_COLUMN + " INTEGER, "
                + SEAT_RESERVATION_COLUMN_ID_RESERVATION + " INTEGER, "
                + "FOREIGN KEY(" + SEAT_RESERVATION_COLUMN_ID_RESERVATION + ") REFERENCES " + RESERVATION_TABLE_NAME + "(" + RESERVATION_COLUMN_ID + "), "
                + "FOREIGN KEY(" + SEAT_RESERVATION_COLUMN_ID_COLUMN + ") REFERENCES " + SEAT_TABLE_NAME + "(" + SEAT_COLUMN_ID_COLUMN + "), "
                + "FOREIGN KEY(" + SEAT_RESERVATION_COLUMN_ID_ROW + ") REFERENCES " + SEAT_TABLE_NAME + "(" + SEAT_COLUMN_ID_ROW + "));";

        db.execSQL(query_cinema);
        db.execSQL(query_movie);
        db.execSQL(query_user);
        db.execSQL(query_genre);
        db.execSQL(query_genre_movie);
        db.execSQL(query_hall);
        db.execSQL(query_seat);
        db.execSQL(query_movie_schedule);
        db.execSQL(query_reservation);
        db.execSQL(query_seat_reservation);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String user_query = "INSERT INTO " + USER_TABLE_NAME + " (" +
                USER_COLUMN_EMAIL + ", " +
                USER_COLUMN_PASSWORD + ", " +
                USER_COLUMN_ROLE + ") VALUES ('" + email + "', '" + password + "', " + "'admin');";
        db.execSQL("DROP TABLE IF EXISTS " + CINEMA_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MOVIE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GENRE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MOVIE_GENRE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + HALL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RESERVATION_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SEAT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SEAT_RESERVATION_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MOVIE_SCHEDULE_TABLE_NAME);
        onCreate(db);
        db.execSQL(user_query);
    }

    public void insertUserData(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USER_COLUMN_EMAIL, email);
        cv.put(USER_COLUMN_PASSWORD, password);
        cv.put(USER_COLUMN_ROLE, "user");
        long result = db.insert(USER_TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to add data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully to add data", Toast.LENGTH_SHORT).show();

        }
    }

    public Cursor checkUserCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + USER_TABLE_NAME + " WHERE email= '" + email + "'" +
                " AND password= '" + password + "';";
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
        String query = "SELECT * FROM " + USER_TABLE_NAME + " WHERE email= '" + email + "';";
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

    public void insertMovieGenreData(String name, ArrayList<String> genreList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        for (String genre : genreList) {
            String query1 = "SELECT * FROM " + GENRE_TABLE_NAME + " WHERE name= '" + genre + "';";
            String query2 = "SELECT * FROM " + MOVIE_TABLE_NAME + " WHERE title= '" + name + "';";
            Cursor cursor1 = null;
            Cursor cursor2 = null;
            if (db != null) {
                cursor1 = db.rawQuery(query1, null);
                cursor2 = db.rawQuery(query2, null);
            }
            while (cursor1.moveToNext()) {
                cv.put(MOVIE_GENRE_COLUMN_ID_GENRE, cursor1.getInt(0));
            }
            while (cursor2.moveToNext()) {
                cv.put(MOVIE_GENRE_COLUMN_ID_MOVIE, cursor2.getInt(0));
            }
            long result = db.insert(MOVIE_GENRE_TABLE_NAME, null, cv);
            if (result == -1) {
                Toast.makeText(context, "Failed to add data", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Successfully to add data", Toast.LENGTH_SHORT).show();
            }
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
        String query = "SELECT * FROM " + GENRE_TABLE_NAME + " WHERE name= '" + genre + "';";
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

    public void deleteSpecificMovie(@NonNull Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query_movie = "DELETE FROM " + MOVIE_TABLE_NAME + " WHERE id= '" + movie.getId() + "';";
        String query_movie_genre = "DELETE FROM " + MOVIE_GENRE_TABLE_NAME + " WHERE id_movie= '" + movie.getId() + "';";
        db.execSQL(query_movie);
        db.execSQL(query_movie_genre);
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

    public Cursor readAllMovieData(String filter_genre) {
        String query;
        if (filter_genre == null) {
            query = "SELECT * FROM " + MOVIE_TABLE_NAME;
        } else {
            query = "SELECT * FROM " + MOVIE_TABLE_NAME + " AS F INNER JOIN "
                    + MOVIE_GENRE_TABLE_NAME + " AS GN ON F.id = GN.id_movie INNER JOIN "
                    + GENRE_TABLE_NAME + " AS G ON GN.id_genre = G.id WHERE G.name = '"
                    + filter_genre + "';";
        }
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllMovieSpecificGenreData(@NonNull Movie movie) {
        String query = "SELECT * FROM " + GENRE_TABLE_NAME + " AS G INNER JOIN "
                + MOVIE_GENRE_TABLE_NAME + " AS GN ON G.id = GN.id_genre INNER JOIN "
                + MOVIE_TABLE_NAME + " AS F ON GN.id_movie = F.id WHERE F.id = '"
                + movie.getId() + "';";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

}
