package com.stefan.reserv;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.stefan.reserv.Add.QuickAddMovie;
import com.stefan.reserv.Database.MyDatabaseHelper;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private TextView goto_login;
    private TextInputEditText register_password, register_email, register_username;
    private Button register_send;
    private ImageView profile_pic;
    private byte[] photo;
    public Uri postImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        profile_pic = findViewById(R.id.profile_pic);
        goto_login = findViewById(R.id.jmp_login);
        register_username = findViewById(R.id.register_username);
        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);
        register_send = findViewById(R.id.register_button);
        register_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = Objects.requireNonNull(register_username.getText()).toString().trim();
                String email = Objects.requireNonNull(register_email.getText()).toString().trim();
                String pass = Objects.requireNonNull(register_password.getText()).toString().trim();
                if (!email.isEmpty() && !pass.isEmpty()) {
                    if (isValidEmail(email)) {
                        MyDatabaseHelper userDB = new MyDatabaseHelper(RegisterActivity.this);
                        if (!userDB.checkForDuplicateAccount(email, pass)) {
                            userDB.insertUserData(username, email, pass, photo);
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "There is already an account registered to this email address", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Please enter a valid e-mail address", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Please enter an e-mail and a password", Toast.LENGTH_SHORT).show();
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