package com.group9.project.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group9.project.R;
import com.group9.project.activities.ListingDetailsActivity;
import com.group9.project.models.Listing;

import java.util.List;

public class MyListingAdapter extends RecyclerView.Adapter<MyListingAdapter.ViewHolder>{

    private Context context;
    List<Listing> listings;

    public MyListingAdapter(List<Listing> listings) {
        this.listings = listings;
    }

    @NonNull
    @Override
    public MyListingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_listings, parent, false);
        return new MyListingAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyListingAdapter.ViewHolder holder, int position) {
        holder.title.setText(listings.get(position).getTitle());
        holder.desc.setText("Description: " + listings.get(position).getDesc());
        holder.address.setText("Address: " + listings.get(position).getAddress());
        holder.price.setText("Price: $" + listings.get(position).getPrice());
        Glide.with(holder.image).load(listings.get(position).getImage()).into(holder.image);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ListingDetailsActivity.class);
            intent.putExtra("listing", listings.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        TextView desc;
        TextView address;
        TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.itemImage);
            context = itemView.getContext();
            title = itemView.findViewById(R.id.itemTitle);
            desc = itemView.findViewById(R.id.itemDesc);
            address = itemView.findViewById(R.id.itemAddress);
            price = itemView.findViewById(R.id.itemPrice);
        }
    }
}