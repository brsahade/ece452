package com.example.fitboyz.thirdeye;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

public class PhotoActivity extends AppCompatActivity {

    // this is the action code we use in our intent,
    // this way we know we're looking at the response from our own action
    private static final int SELECT_PICTURE = 1;

    private Uri selectedImagePath;
    private ImageView mMainImage;
    private LinearLayout mTypeOne, mTypeTwo, mTypeThree;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        mMainImage = (ImageView)findViewById(R.id.photo_view);

        mTypeOne = (LinearLayout)findViewById(R.id.image_type_one);
        mTypeTwo = (LinearLayout)findViewById(R.id.image_type_two);
        mTypeThree = (LinearLayout)findViewById(R.id.image_type_three);

        mTypeOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daltonization(1);
            }
        });

        mTypeTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daltonization(2);
            }
        });

        mTypeThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daltonization(3);
            }
        });

        // in onCreate or any event where your want the user to
        // select a file
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);

    }

    public void daltonization(int typeId) {

        Bitmap bitmap = loadBitmap(selectedImagePath);
//        Bitmap bitmapConverted = someting(bitmap, typeId);
        updateImage(bitmap);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

//                    File file = new File(new URI(selectedImageUri.toString()));
//                    ImageView imageView = (ImageView) findViewById(R.id.photo_view);
//
                    selectedImagePath = selectedImageUri;
//                    Glide.with(this).load(selectedImageUri).into(mMainImage);
                Glide.with(this).load(R.drawable.test).into(mMainImage);

            }
        }
    }

    private byte[] bitmapToByte(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void updateImage(Bitmap b) {
        Glide.with(this).asBitmap().load(bitmapToByte(b)).into(mMainImage); //>>not tested

//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        b.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        Glide.with(this)
//                .asBitmap()
//                .load(stream.toByteArray())
//                .into(mMainImage);

    }

    public Bitmap loadBitmap(Uri url)
    {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), url);
            return bitmap;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return bitmap;
    }

}
