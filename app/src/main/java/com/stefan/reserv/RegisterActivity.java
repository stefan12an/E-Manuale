package com.stefan.reserv;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.stefan.reserv.Database.MyDatabaseHelper;
import com.stefan.reserv.Model.User;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private TextView goto_login;
    private TextInputEditText register_password, register_email, register_username;
    private Button register_send;
    private ImageView profile_pic;
    private Spinner grade_spinner;
    private byte[] photo;
    private MyDatabaseHelper db;
    private ArrayList<String> arraySpinner;
    public Uri postImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        arraySpinner = new ArrayList<>();
        profile_pic = findViewById(R.id.profile_pic);
        goto_login = findViewById(R.id.jmp_login);
        register_username = findViewById(R.id.register_username);
        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);
        register_send = findViewById(R.id.register_button);
        grade_spinner = findViewById(R.id.grade_spinner);
        db = new MyDatabaseHelper(RegisterActivity.this);
        Cursor cursor = db.viewClase();
        Log.e(TAG, "onCreate: " + cursor.getCount());
        while (cursor.moveToNext()) {
            arraySpinner.add("Clasa a " + cursor.getString(1) + "-a");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, arraySpinner);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        grade_spinner.setAdapter(adapter);
        register_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = Objects.requireNonNull(register_username.getText()).toString().trim();
                String email = Objects.requireNonNull(register_email.getText()).toString().trim();
                String pass = Objects.requireNonNull(register_password.getText()).toString().trim();
                String s = grade_spinner.getSelectedItem().toString().substring(grade_spinner.getSelectedItem().toString().indexOf("a") + 6);
                s = s.substring(0, s.indexOf("-"));
                int selected_grade = Integer.parseInt(s) + 1;
                if (!email.isEmpty() && !pass.isEmpty()) {
                    if (isValidEmail(email)) {
                        db.copyDatabase();
                        if (!db.checkForDuplicateAccount(email, pass)) {
                            db.insertUserData(username, email, pass, photo, selected_grade);
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Deja există un cont inregistrat cu această adresă de e-mail", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Vă rugăm să introduceți o adresă de e-mail validă", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Vă rugăm să introduceți un e-mail și o parolă", Toast.LENGTH_SHORT).show();
                }
            }
        });
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        goto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    public static boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void selectImage() {
        Intent intent = CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropResultSize(320, 566)
                .setMinCropWindowSize(320, 566)
                .getIntent(RegisterActivity.this);
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = RegisterActivity.this.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                postImageUri = result.getUri();
                try {
                    bitmap = getBitmapFromUri(postImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                photo = baos.toByteArray();
                profile_pic.setImageURI(postImageUri);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(RegisterActivity.this, result.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


}