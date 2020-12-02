package com.kelvinadithya.picatie;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.kc.unsplash.models.Photo;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class fullPhotoActivity extends AppCompatActivity {
    @BindView(R.id.Photo)
    ImageView img;
    @BindView(R.id.Like)
    Button btn;
    Photo photo;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_photo);
        ButterKnife.bind(this);
        photo = getIntent().getParcelableExtra("URLS");
        setTitle(photo.getUser().getName());
        btn.setText(photo.getLikes().toString());

        Picasso.with(context)
                .load(photo.getUrls().getRegular())
                .fit()
                .centerCrop()
                .into(img);
    }

    @OnClick(R.id.Like)
    public void Like() {
        if (photo.getLikedByUser() == false) {
            photo.setLikes(photo.getLikes() + 1);
            photo.setLikedByUser(true);
            btn.setText(photo.getLikes().toString());
        } else {
            photo.setLikedByUser(false);
            photo.setLikes(photo.getLikes() - 1);
            btn.setText(photo.getLikes().toString());
        }
    }
}

