package com.stefan.reserv.Database;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stefan.reserv.Model.Book;
import com.stefan.reserv.Model.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String username = "admin";
    private static final String email = "vms1238@gmail.com";
    private static final String password = "1904stef";
    private final Context context;
    private static final String DATABASE_NAME = "reserv.db";
    private static String DATABASE_PATH = "";
    private static final int DATABASE_VERSION = 56;

    //TABLE NAMES
    private static final String CLASA_TABLE_NAME = "CLASA";
    private static final String BOOK_TABLE_NAME = "BOOK";
    private static final String USER_TABLE_NAME = "USER";
    private static final String MATERIE_TABLE_NAME = "MATERIE";

    //COLUMNS CLASA TABLE
    private static final String CLASA_COLUMN_ID = "id";
    private static final String CLASA_COLUMN_NAME = "name";
    //COLUMNS USER TABLE
    private static final String USER_COLUMN_ID = "id";
    private static final String USER_COLUMN_USERNAME = "username";
    private static final String USER_COLUMN_EMAIL = "email";
    private static final String USER_COLUMN_PASSWORD = "password";
    private static final String USER_COLUMN_ROLE = "role";
    private static final String USER_COLUMN_PICTURE = "profile_pic";
    private static final String USER_COLUMN_ID_GRADE = "id_grade";

    //COLUMNS BOOK TABLE
    private static final String BOOK_COLUMN_ID = "id";
    private static final String BOOK_COLUMN_TITLE = "titlu";
    private static final String BOOK_COLUMN_RELEASE_DATE = "release_date";
    private static final String BOOK_COLUMN_PHOTO = "preview";
    private static final String BOOK_COLUMN_AUTOR = "autor";
    private static final String BOOK_COLUMN_FAVORIT = "favorit";
    private static final String BOOK_COLUMN_ID_CLASA = "id_clasa";
    private static final String BOOK_COLUMN_ID_MATERIE = "id_materie";

    //COLUMNS MATERIE TABLE
    private static final String MATERIE_COLUMN_ID = "id";
    private static final String MATERIE_COLUMN_NAME = "name";


    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        assert context != null;
        DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).getAbsolutePath();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_clasa = "CREATE TABLE IF NOT EXISTS  " + CLASA_TABLE_NAME + " ("
                + CLASA_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CLASA_COLUMN_NAME + " TEXT);";

        String query_book = "CREATE TABLE IF NOT EXISTS " + BOOK_TABLE_NAME + " ("
                + BOOK_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BOOK_COLUMN_TITLE + " TEXT, "
                + BOOK_COLUMN_RELEASE_DATE + " TEXT, "
                + BOOK_COLUMN_PHOTO + " BLOB, "
                + BOOK_COLUMN_AUTOR + " TEXT, "
                + BOOK_COLUMN_FAVORIT + " INTEGER, "
                + BOOK_COLUMN_ID_CLASA + " INTEGER, "
                + BOOK_COLUMN_ID_MATERIE + " INTEGER, "
                + "FOREIGN KEY(" + BOOK_COLUMN_ID_CLASA + ") REFERENCES " + CLASA_TABLE_NAME + "(" + CLASA_COLUMN_ID + "), "
                + "FOREIGN KEY(" + BOOK_COLUMN_ID_MATERIE + ") REFERENCES " + MATERIE_TABLE_NAME + "(" + MATERIE_COLUMN_ID + "));";

        String query_user = "CREATE TABLE IF NOT EXISTS  " + USER_TABLE_NAME + " ("
                + USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_COLUMN_USERNAME + " TEXT, "
                + USER_COLUMN_EMAIL + " TEXT, "
                + USER_COLUMN_PASSWORD + " TEXT, "
                + USER_COLUMN_ROLE + " TEXT, "
                + USER_COLUMN_PICTURE + " BLOB, "
                + USER_COLUMN_ID_GRADE + " INTEGER, "
                + "FOREIGN KEY(" + USER_COLUMN_ID_GRADE + ") REFERENCES " + CLASA_TABLE_NAME + "(" + CLASA_COLUMN_ID + "));";

        String query_materie = "CREATE TABLE IF NOT EXISTS  " + MATERIE_TABLE_NAME + " ("
                + MATERIE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MATERIE_COLUMN_NAME + " TEXT);";

        db.execSQL(query_clasa);
        db.execSQL(query_book);
        db.execSQL(query_user);
        db.execSQL(query_materie);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String user_query = "INSERT INTO " + USER_TABLE_NAME + " (" +
                USER_COLUMN_USERNAME + ", " +
                USER_COLUMN_EMAIL + ", " +
                USER_COLUMN_PASSWORD + ", " +
                USER_COLUMN_ROLE + ") VALUES ('" + username + "', '" + email + "', '" + password + "', " + "'admin');";
        db.execSQL("DROP TABLE IF EXISTS " + CLASA_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BOOK_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MATERIE_TABLE_NAME);
        onCreate(db);
        db.execSQL(user_query);
    }

    //##########################CHECK########################################

    public Cursor checkUserCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.e(TAG, "checkUserCredentials: " + this.getReadableDatabase().isOpen());
        Log.e(TAG, "checkUserCredentials: " + this.getReadableDatabase().isDatabaseIntegrityOk());
        String query = "SELECT * FROM " + USER_TABLE_NAME + " WHERE email= '" + email + "'" +
                " AND password= '" + password + "';";
        Cursor cursor = null;
        if(db != null) {
            cursor = db.rawQuery(query, null);
        }
        switch (Objects.requireNonNull(cursor).getCount()) {
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

    //##########################INSERT########################################

    public void insertUserData(String username, String email, String password, byte[] profile_pic, int grade_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_COLUMN_USERNAME, username);
        cv.put(USER_COLUMN_EMAIL, email);
        cv.put(USER_COLUMN_PASSWORD, password);
        cv.put(USER_COLUMN_ROLE, "user");
        cv.put(USER_COLUMN_PICTURE, profile_pic);
        cv.put(USER_COLUMN_ID_GRADE, grade_id);
        long result = db.insert(USER_TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to add data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully to add data", Toast.LENGTH_SHORT).show();
        }
    }

    public void insertBookData(String name, byte[] photo, String author) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String date = format1.format(currentTime);
        cv.put(BOOK_COLUMN_TITLE, name);
        cv.put(BOOK_COLUMN_RELEASE_DATE, date);
        long result = db.insert(BOOK_TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to add data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully to add data", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean insertMaterie(String materie) {
        SQLiteDatabase w_db = this.getWritableDatabase();
        SQLiteDatabase r_db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        String query = "SELECT * FROM " + MATERIE_TABLE_NAME + " WHERE name= '" + materie + "';";
        Cursor cursor = null;
        if (r_db != null) {
            cursor = r_db.rawQuery(query, null);
        }
        if (cursor.getCount() == 0) {
            cv.put(MATERIE_COLUMN_NAME, materie);
            long result = w_db.insert(MATERIE_TABLE_NAME, null, cv);
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

    public void insertClase() {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        List<String> grades = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8");
        for (int i = 0; i < grades.size(); i++) {
//            String grades_query = "INSERT INTO " + CLASA_TABLE_NAME + " ("
//                    + CLASA_COLUMN_NAME + ") VALUES ('" + grades.get(i) + "');";
//            Log.e(TAG, "onUpgrade: " + grades.get(i));
            cv.put(CLASA_COLUMN_NAME, grades.get(i));
            long result = db.insert(CLASA_TABLE_NAME, null, cv);
            if (result == -1) {
                Toast.makeText(context, "Failed to add data", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Successfully to add data", Toast.LENGTH_SHORT).show();
            }
//            db.execSQL(grades_query);
        }
    }

    //##########################DELETE########################################

    public void deleteClase() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + CLASA_TABLE_NAME;
        db.execSQL(query);
    }

    public void deleteMaterii() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + MATERIE_TABLE_NAME;
        db.execSQL(query);
    }

    public void deleteSpecificBook(@NonNull Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query_book = "DELETE FROM " + BOOK_TABLE_NAME + " WHERE id= '" + book.getId() + "';";
        db.execSQL(query_book);
    }

    //##########################READ########################################

    public Cursor viewClase() {
        String query = "SELECT * FROM " + CLASA_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor viewMaterii() {
        String query = "SELECT * FROM " + MATERIE_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllBookData(String filter_genre, User user) {
        String query;
        if (filter_genre == null) {
            query = "SELECT * FROM " + BOOK_TABLE_NAME + " WHERE id_clasa= '" + user.getId_clasa() + "';";
        } else {
            query = "SELECT * FROM " + BOOK_TABLE_NAME + " AS B INNER JOIN "
                    + MATERIE_TABLE_NAME + " AS M ON M.id = B.id_materie WHERE M.name = '"
                    + filter_genre + "' AND B.id_clasa= '" + user.getId_clasa() + "';";
        }
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllBookSpecificGenreData(@NonNull Book book) {
        String query1 = null;
        String query = "SELECT id_materie, id_clasa FROM " + BOOK_TABLE_NAME + " WHERE id= '" + book.getId() + "';";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        Cursor cursor2 = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        while (cursor.moveToNext()) {
            query1 = "SELECT name FROM " + MATERIE_TABLE_NAME + " WHERE id= '" + cursor.getString(0) + "';";
        }
        if (db != null) {
            cursor2 = db.rawQuery(query1, null);
        }
        return cursor2;
    }

    public Cursor viewMaterie(int id){
        String query = "SELECT * FROM " + MATERIE_TABLE_NAME + " WHERE id = '" + id + "';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void insertBooks(String[] name, String materie, String clasa) {
        String materie_aleasa = null;
        String clasa_aleasa = null;
        SQLiteDatabase db_r = this.getReadableDatabase();
        SQLiteDatabase db_w = this.getWritableDatabase();
        String query_materie = "SELECT * FROM " + MATERIE_TABLE_NAME + " WHERE name= '" + materie + "';";
        String query_clasa = "SELECT * FROM " + CLASA_TABLE_NAME + " WHERE name= '" + clasa + "';";
        Cursor cursor_materie = null;
        Cursor cursor_clasa = null;
        if (db_r != null) {
            cursor_materie = db_r.rawQuery(query_materie, null);
            cursor_clasa = db_r.rawQuery(query_clasa, null);
        }
        while (true) {
            assert cursor_materie != null;
            if (!cursor_materie.moveToNext()) break;
            materie_aleasa = cursor_materie.getString(0);
        }
        while (true) {
            assert cursor_clasa != null;
            if (!cursor_clasa.moveToNext()) break;
            clasa_aleasa = cursor_clasa.getString(0);
        }
        for (String s : name) {
            ContentValues cv = new ContentValues();
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            String date = format1.format(currentTime);
            cv.put(BOOK_COLUMN_TITLE, s);
            cv.put(BOOK_COLUMN_RELEASE_DATE, date);
            cv.put(BOOK_COLUMN_FAVORIT, 0);
            cv.put(BOOK_COLUMN_ID_MATERIE, materie_aleasa);
            cv.put(BOOK_COLUMN_ID_CLASA, clasa_aleasa);
            long result = db_w.insert(BOOK_TABLE_NAME, null, cv);
            if (result == -1) {
                Toast.makeText(context, "Failed to add data", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Successfully to add data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //##########################UPDATE########################################

    public void updateFavorite(String bookId, Boolean status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        if(status) {
            cv.put("favorit", 1);
        }else
            cv.put("favorit", 0);
        long result = db.update(BOOK_TABLE_NAME, cv, "id=?", new String[]{bookId});
        if (result == -1) {
            Toast.makeText(context, "Failed to update data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully updated data", Toast.LENGTH_SHORT).show();
        }
    }

    public void copyDatabase() {
        this.getReadableDatabase();
        Log.i("Database",
                "New database is being copied to device!");
        byte[] buffer = new byte[1024];
        OutputStream myOutput = null;
        int length;
        // Open your local db as the input stream
        InputStream myInput = null;
        try {
            myInput = context.getAssets().open("Database/" + DATABASE_NAME);
            // transfer bytes from the inputfile to the
            // outputfile
            myOutput = new FileOutputStream(DATABASE_PATH);
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myInput.close();
            myOutput.flush();
            myOutput.close();
            Log.i("Database",
                    "New database has been copied to device!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
