package com.stefan.reserv;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;
import com.stefan.reserv.Model.Book;

import java.io.File;

public class pdf_view extends AppCompatActivity {
    private PDFView pdfView;
    private Book selected_book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        if (getIntent().hasExtra("selected_book")) {
            Bundle bundle = getIntent().getExtras();
            selected_book = bundle.getParcelable("selected_book");
        }
        pdfView = findViewById(R.id.pdfView);
        pdfView.fromAsset("PDF/" + selected_book.getTitle()).load();
    }
}