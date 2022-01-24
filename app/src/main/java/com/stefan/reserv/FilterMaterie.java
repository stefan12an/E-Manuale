package com.stefan.reserv;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.stefan.reserv.Adapter.FilterMaterieAdapter;
import com.stefan.reserv.Database.MyDatabaseHelper;
import com.stefan.reserv.Model.User;

import java.util.ArrayList;

public class FilterMaterie extends AppCompatActivity implements FilterMaterieAdapter.OnFilterMaterieClickListener {
    private RecyclerView materie_Rv;
    private ArrayList<String> materieList;
    private MyDatabaseHelper myDB;
    private FilterMaterieAdapter materie_adapter;
    private User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_materie);
        Toolbar toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        materieList = new ArrayList<>();
        myDB = new MyDatabaseHelper(this);
        if (getIntent().hasExtra("current_user")) {
            Bundle bundle = getIntent().getExtras();
            current_user = bundle.getParcelable("current_user");
        }
        materie_adapter = new FilterMaterieAdapter(this, materieList, this);
        materie_Rv = findViewById(R.id.materie_list_recyclerView);
        materie_Rv.setAdapter(materie_adapter);
    }

    private void displayMaterii() {
        Cursor cursor = myDB.viewMaterii();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No genres.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                Cursor aa = myDB.ala(current_user);
                while (aa.moveToNext()) {
                    if (cursor.getString(0).equals(aa.getString(0))) {
                        materieList.add(cursor.getString(1));
                    }
                }
            }
            materie_adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        materieList.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayMaterii();
        Log.e(TAG, "onResume: " + materieList + current_user.getEmail());
    }

    @Override
    public void OnFilterMaterieClick(int position, CardView genre_cv) {
        Intent i = new Intent(this, BookList.class);
        i.putExtra("current_user", current_user);
        i.putExtra("filter_materie", materieList.get(position));
        startActivity(i);
    }
}