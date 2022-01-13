package com.stefan.reserv;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.barteksc.pdfviewer.PDFView;
import com.stefan.reserv.Adapter.PdfDocumentAdapter;
import com.stefan.reserv.Model.Book;

import java.io.File;

public class pdf_view extends AppCompatActivity {
    private PDFView pdfView;
    private Book selected_book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        Toolbar toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (getIntent().hasExtra("selected_book")) {
            Bundle bundle = getIntent().getExtras();
            selected_book = bundle.getParcelable("selected_book");
        }
        pdfView = findViewById(R.id.pdfView);
        pdfView.fromAsset("PDF/" + selected_book.getTitle()).load();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.print:
                PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
                try {
                    PrintDocumentAdapter printAdapter = new PdfDocumentAdapter(this, selected_book.getTitle());
                    printManager.print("Document", printAdapter, new PrintAttributes.Builder().build());
                } catch (Exception e) {
                    Log.e(TAG, "onOptionsItemSelected: " + e);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pdf_options, menu);
        return true;
    }
}