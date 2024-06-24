package com.example.demo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.Models.cartModel;
import com.example.demo.R;

import java.util.ArrayList;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.viewholder>{

    Context context;
    ArrayList<cartModel> list;

    public cartAdapter(Context context, ArrayList<cartModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recommendation_sample_view,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        cartModel story=list.get(position);
        holder.currentImg.setImageResource(story.getImage());
        holder.currentPrice.setText(story.getRecommendPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{


        ImageView currentImg;
        TextView currentPrice;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            currentImg=itemView.findViewById(R.id.imageView3);
            currentPrice=itemView.findViewById(R.id.recommendationPrice);
        }
    }
}
