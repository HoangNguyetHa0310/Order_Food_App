package com.example.order_food_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.order_food_app.Domain.Category;
import com.example.order_food_app.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewhlder> {

    ArrayList<Category> items;
    Context context;

    public CategoryAdapter(ArrayList<Category> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryAdapter.viewhlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category,parent,false);
        return new viewhlder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.viewhlder holder, int position) {
        holder.titleTxt.setText(items.get(position).getName());
        int drawableResourceId = context.getResources().getIdentifier(items.get(position).getImagePath(), "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(context)
                .load(drawableResourceId)
                .into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class viewhlder extends RecyclerView.ViewHolder {
        TextView titleTxt;
        ImageView pic;

        public viewhlder(@NonNull View itemView) {
            super(itemView);
            titleTxt=itemView.findViewById(R.id.catNameTxt);
            pic=itemView.findViewById(R.id.imgCat);
        }
    }
}
