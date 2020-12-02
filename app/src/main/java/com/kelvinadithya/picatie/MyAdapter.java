package com.kelvinadithya.picatie;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.kc.unsplash.models.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;



public class MyAdapter extends RecyclerView.Adapter<com.kelvinadithya.picatie.MyAdapter.ViewHolder> implements View.OnClickListener {
    private final List<Photo> photoList;
    private Context mContext;

    public MyAdapter(List<Photo> photos, Context context) {
        photoList = photos;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Photo photo = photoList.get(position);
        Picasso.with(mContext).load(photo.getUrls().getRegular()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, fullPhotoActivity.class);
                intent.putExtra("URLS", photo);
                mContext.startActivity(intent);
            }
        });
    }

}

