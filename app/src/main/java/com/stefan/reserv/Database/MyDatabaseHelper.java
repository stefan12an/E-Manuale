package com.stefan.reserv.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stefan.reserv.Model.Book;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String username = "admin";
    private static final String email = "vms1238@gmail.com";
    private static final String password = "1904stef";
    private final Context context;
    private static final String DATABASE_NAME = "reserv.db";
    private static final int DATABASE_VERSION = 26;

    //TABLE NAMES
    private static final String GRADE_TABLE_NAME = "GRADE";
    private static final String BOOK_TABLE_NAME = "BOOK";
    private static final String USER_TABLE_NAME = "USER";
    private static final String GENRE_TABLE_NAME = "GENRE";
    private static final String BOOK_GENRE_TABLE_NAME = "BOOK_GENRE";
        private static final String CINEMA_TABLE_NAME = "CINEMA";
    private static final String MOVIE_TABLE_NAME = "MOVIE";
    private static final String MOVIE_GENRE_TABLE_NAME = "MOVIE_GENRE";
    private static final String RESERVATION_TABLE_NAME = "RESERVATION";
    private static final String SEAT_TABLE_NAME = "SEAT";
    private static final String SEAT_RESERVATION_TABLE_NAME = "SEAT_RESERVATION";
    private static final String HALL_TABLE_NAME = "HALL";
    private static final String MOVIE_SCHEDULE_TABLE_NAME = "MOVIE_SCHEDULE";
    //COLUMNS GRADE TABLE
    private static final String GRADE_COLUMN_ID = "id";
    private static final String GRADE_COLUMN_NAME = "name";
    //COLUMNS USER TABLE
    private static final String USER_COLUMN_ID = "id";
    private static final String USER_COLUMN_USERNAME = "username";
    private static final String USER_COLUMN_EMAIL = "email";
    private static final String USER_COLUMN_PASSWORD = "password";
    private static final String USER_COLUMN_ROLE = "role";
    private static final String USER_COLUMN_PICTURE = "profile_pic";
    //COLUMNS BOOK TABLE
    private static final String BOOK_COLUMN_ID = "id";
    private static final String BOOK_COLUMN_TITLE = "title";
    private static final String BOOK_COLUMN_RELEASE_DATE = "release_date";
    private static final String BOOK_COLUMN_PHOTO = "poster";
    private static final String BOOK_COLUMN_PATH = "pdf";
    private static final String BOOK_COLUMN_AUTHOR = "author";

    //COLUMNS GENRE TABLE
    private static final String GENRE_COLUMN_ID = "id";
    private static final String GENRE_COLUMN_NAME = "name";

    //COLUMNS BOOK_GENRE TABLE
    private static final String BOOK_GENRE_COLUMN_ID = "id";
    private static final String BOOK_GENRE_COLUMN_ID_MOVIE = "id_book";
    private static final String BOOK_GENRE_COLUMN_ID_GENRE = "id_genre";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_grade = "CREATE TABLE " + GRADE_TABLE_NAME + " ("
                + GRADE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GRADE_COLUMN_NAME + " TEXT);";

        String query_book = "CREATE TABLE " + BOOK_TABLE_NAME + " ("
                + BOOK_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BOOK_COLUMN_TITLE + " TEXT, "
                + BOOK_COLUMN_RELEASE_DATE + " TEXT, "
                + BOOK_COLUMN_PHOTO + " BLOB, "
                + BOOK_COLUMN_PATH + " BLOB, "
                + BOOK_COLUMN_AUTHOR + " TEXT);";

        String query_user = "CREATE TABLE " + USER_TABLE_NAME + " ("
                + USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_COLUMN_USERNAME + " TEXT, "
                + USER_COLUMN_EMAIL + " TEXT, "
                + USER_COLUMN_PASSWORD + " TEXT, "
                + USER_COLUMN_ROLE + " TEXT, "
                + USER_COLUMN_PICTURE + " BLOB);";

        String query_genre = "CREATE TABLE " + GENRE_TABLE_NAME + " ("
                + GENRE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GENRE_COLUMN_NAME + " TEXT);";

        String query_genre_book = "CREATE TABLE " + BOOK_GENRE_TABLE_NAME + " ("
                + BOOK_GENRE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BOOK_GENRE_COLUMN_ID_MOVIE + " INTEGER, "
                + BOOK_GENRE_COLUMN_ID_GENRE + " INTEGER, "
                + "FOREIGN KEY(" + BOOK_GENRE_COLUMN_ID_MOVIE + ") REFERENCES " + BOOK_TABLE_NAME + "(" + BOOK_COLUMN_ID + "), "
                + "FOREIGN KEY(" + BOOK_GENRE_COLUMN_ID_GENRE + ") REFERENCES " + GENRE_TABLE_NAME + "(" + GENRE_COLUMN_ID + "));";
        db.execSQL(query_grade);
        db.execSQL(query_book);
        db.execSQL(query_user);
        db.execSQL(query_genre);
        db.execSQL(query_genre_book);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String user_query = "INSERT INTO " + USER_TABLE_NAME + " (" +
                USER_COLUMN_USERNAME + ", " +
                USER_COLUMN_EMAIL + ", " +
                USER_COLUMN_PASSWORD + ", " +
                USER_COLUMN_ROLE + ") VALUES ('" + username + "', '" + email + "', '" + password + "', " + "'admin');";
        db.execSQL("DROP TABLE IF EXISTS " + GRADE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BOOK_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GENRE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BOOK_GENRE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CINEMA_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MOVIE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MOVIE_GENRE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + HALL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RESERVATION_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SEAT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SEAT_RESERVATION_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MOVIE_SCHEDULE_TABLE_NAME);
        onCreate(db);
        db.execSQL(user_query);
    }

    public void insertUserData(String username, String email, String password, byte[] profile_pic) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_COLUMN_USERNAME, username);
        cv.put(USER_COLUMN_EMAIL, email);
        cv.put(USER_COLUMN_PASSWORD, password);
        cv.put(USER_COLUMN_ROLE, "user");
        cv.put(USER_COLUMN_PICTURE, profile_pic);
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

    public void insertBookData(String name, byte[] photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String date = format1.format(currentTime);
        cv.put(BOOK_COLUMN_TITLE, name);
        cv.put(BOOK_COLUMN_RELEASE_DATE, date);
        cv.put(BOOK_COLUMN_PHOTO, photo);
        cv.put(BOOK_COLUMN_PATH, (byte[]) null);
        cv.put(BOOK_COLUMN_AUTHOR, "Gigel");
        long result = db.insert(BOOK_TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to add data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully to add data", Toast.LENGTH_SHORT).show();
        }
    }

    public void insertBookGenreData(String name, ArrayList<String> genreList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        for (String genre : genreList) {
            String query1 = "SELECT * FROM " + GENRE_TABLE_NAME + " WHERE name= '" + genre + "';";
            String query2 = "SELECT * FROM " + BOOK_TABLE_NAME + " WHERE title= '" + name + "';";
            Cursor cursor1 = null;
            Cursor cursor2 = null;
            if (db != null) {
                cursor1 = db.rawQuery(query1, null);
                cursor2 = db.rawQuery(query2, null);
            }
            while (cursor1.moveToNext()) {
                cv.put(BOOK_GENRE_COLUMN_ID_GENRE, cursor1.getInt(0));
            }
            while (cursor2.moveToNext()) {
                cv.put(BOOK_GENRE_COLUMN_ID_MOVIE, cursor2.getInt(0));
            }
            long result = db.insert(BOOK_GENRE_TABLE_NAME, null, cv);
            if (result == -1) {
                Toast.makeText(context, "Failed to add data", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Successfully to add data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void insertGradeData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(GRADE_COLUMN_NAME, name);
        long result = db.insert(GRADE_TABLE_NAME, null, cv);
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

    public void deleteGradeData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + GRADE_TABLE_NAME;
        db.execSQL(query);
    }

    public void deleteGenreData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + GENRE_TABLE_NAME;
        db.execSQL(query);
    }

    public void deleteSpecificBook(@NonNull Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query_book = "DELETE FROM " + BOOK_TABLE_NAME + " WHERE id= '" + book.getId() + "';";
        String query_book_genre = "DELETE FROM " + BOOK_GENRE_TABLE_NAME + " WHERE id_movie= '" + book.getId() + "';";
        db.execSQL(query_book);
        db.execSQL(query_book_genre);
    }

    public Cursor readAllGradeData() {
        String query = "SELECT * FROM " + GRADE_TABLE_NAME;
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

    public Cursor readAllBookData(String filter_genre) {
        String query;
        if (filter_genre == null) {
            query = "SELECT * FROM " + BOOK_TABLE_NAME;
        } else {
            query = "SELECT * FROM " + BOOK_TABLE_NAME + " AS F INNER JOIN "
                    + BOOK_GENRE_TABLE_NAME + " AS GN ON F.id = GN.id_book INNER JOIN "
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

    public Cursor readAllBookSpecificGenreData(@NonNull Book book) {
        String query = "SELECT * FROM " + GENRE_TABLE_NAME + " AS G INNER JOIN "
                + BOOK_GENRE_TABLE_NAME + " AS GN ON G.id = GN.id_genre INNER JOIN "
                + BOOK_TABLE_NAME + " AS F ON GN.id_book = F.id WHERE F.id = '"
                + book.getId() + "';";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

}
