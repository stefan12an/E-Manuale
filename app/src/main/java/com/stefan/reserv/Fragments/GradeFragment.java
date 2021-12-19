package com.stefan.reserv.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stefan.reserv.Adapter.GradeAdapter;
import com.stefan.reserv.Database.MyDatabaseHelper;
import com.stefan.reserv.Model.Grade;
import com.stefan.reserv.R;

import java.util.ArrayList;

public class GradeFragment extends Fragment {
    RecyclerView recyclerView;
    MyDatabaseHelper myDB;
    GradeAdapter adapter;
    ArrayList<Grade> gradeList;
    Grade grade = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grade, container, false);

        recyclerView = view.findViewById(R.id.cinema_RecyclerView);
        myDB = new MyDatabaseHelper(getContext());
        gradeList = new ArrayList<>();
        adapter = new GradeAdapter(getContext(), gradeList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    void displayData() {
        Cursor cursor = myDB.readAllGradeData();
        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                grade = new Grade(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getBlob(3));
                gradeList.add(grade);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        gradeList.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        displayData();
    }
}