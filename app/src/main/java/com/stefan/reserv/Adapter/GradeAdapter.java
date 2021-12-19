package com.stefan.reserv.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stefan.reserv.Model.Grade;
import com.stefan.reserv.R;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.MyViewHolder> {

    Context context;
    ArrayList<Grade> gradeList;
    public GradeAdapter(Context context, ArrayList<Grade> gradeList) {
        this.context = context;
        this.gradeList = gradeList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.each_grade, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.cinema_id_txt.setText("#" + gradeList.get(position).getId());
        holder.cinema_name_txt.setText(String.valueOf(gradeList.get(position).getCinema_name()));
        holder.cinema_location_txt.setText(String.valueOf(gradeList.get(position).getCinema_location()));

        ByteArrayInputStream imageStream = new ByteArrayInputStream(gradeList.get(position).getCinema_photo());
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);

        holder.cinema_photo_imv.setImageBitmap(theImage);
    }

    @Override
    public int getItemCount() {
        return gradeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cinema_id_txt, cinema_name_txt, cinema_location_txt;
        ImageView cinema_photo_imv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cinema_id_txt = itemView.findViewById(R.id.id);
            cinema_name_txt = itemView.findViewById(R.id.nume);
            cinema_location_txt = itemView.findViewById(R.id.locatie);
            cinema_photo_imv = itemView.findViewById(R.id.poza);
        }
    }
}
