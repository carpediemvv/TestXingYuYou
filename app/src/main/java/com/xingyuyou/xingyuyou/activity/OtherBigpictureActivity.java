package com.xingyuyou.xingyuyou.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.xingyuyou.xingyuyou.R;

import static android.R.attr.width;

public class OtherBigpictureActivity extends AppCompatActivity {

    private PhotoView photoView_bigPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_bigpicture);
        initView();
        initData();


    }

    private void initData() {
        String image = getIntent().getStringExtra("image");
        Glide.with(OtherBigpictureActivity.this)
                .load(image)
                .override(width, Target.SIZE_ORIGINAL)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(photoView_bigPicture);
    }

    private void initView() {
        photoView_bigPicture = (PhotoView) findViewById(R.id.photoView_bigPicture);
        photoView_bigPicture.enable();

        photoView_bigPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
