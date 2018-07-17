package com.example.fitboyz.thirdeye;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.annotation.Target;
import java.util.UUID;

import com.example.fitboyz.thirdeye.Photo;
import com.example.fitboyz.thirdeye.PhotoManager;

public class PhotoActivity extends AppCompatActivity {

    // this is the action code we use in our intent,
    // this way we know we're looking at the response from our own action
    private static final int SELECT_PICTURE = 1;

    private Uri selectedImagePath;
    private ImageView mMainImage;
    private LinearLayout mTypeOne, mTypeTwo, mTypeThree;
    private Button mButton;
    private ProgressBar mProgressBar;
    private Photo photo;
    private PhotoManager pm;

    public void onCreate(Bundle savedInstanceState) {
        pm = PhotoManager.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        mMainImage = (ImageView)findViewById(R.id.photo_view);

        mTypeOne = (LinearLayout)findViewById(R.id.image_type_one);
        mTypeTwo = (LinearLayout)findViewById(R.id.image_type_two);
        mTypeThree = (LinearLayout)findViewById(R.id.image_type_three);
        mButton = (Button)findViewById(R.id.photo_more);

        mTypeOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(Color.parseColor("#3F51B5"));

                View view2 = (View) findViewById(R.id.image_type_two);
                View view3 = (View) findViewById(R.id.image_type_three);
                view2.setBackgroundColor(Color.parseColor("#303F9F"));
                view3.setBackgroundColor(Color.parseColor("#303F9F"));

                daltonization(1);
            }
        });

        mTypeTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(Color.parseColor("#3F51B5"));

                View view2 = (View) findViewById(R.id.image_type_one);
                View view3 = (View) findViewById(R.id.image_type_three);
                view2.setBackgroundColor(Color.parseColor("#303F9F"));
                view3.setBackgroundColor(Color.parseColor("#303F9F"));

                daltonization(2);
            }
        });

        mTypeThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(Color.parseColor("#3F51B5"));

                View view2 = (View) findViewById(R.id.image_type_one);
                View view3 = (View) findViewById(R.id.image_type_two);
                view2.setBackgroundColor(Color.parseColor("#303F9F"));
                view3.setBackgroundColor(Color.parseColor("#303F9F"));

                daltonization(3);
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });

        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);
        // in onCreate or any event where your want the user to
        // select a file

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);

    }

    public void daltonization(final int typeId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });
//        mProgressBar.setVisibility(View.VISIBLE);

//        byte[] bArray = bitmapToByte(bitmap);
//        Bitmap b = BitmapFactory.decodeByteArray(bArray, 0, bArray.length);
//        Bitmap newBItmap = d.daltonizeImage(Bitmap.createScaledBitmap(bitmap, 500, 500, false), typeId);

        Runnable myRunnable = new Runnable(){

            public void run(){
                photo.computeDalonization(typeId);
                pm.add(photo.getId(), photo);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        Glide.with(PhotoActivity.this).asBitmap().load(bitmapToByte(photo.getPhotoDaltonized()))
                                .into(mMainImage);
                    }
                });

            }
        };

        Thread thread = new Thread(myRunnable);
        thread.start();

//        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImagePath = data.getData();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(new File(selectedImagePath.getPath()).getAbsolutePath(), options);
                int height = options.outHeight;
                int width = options.outWidth;
                String id = UUID.randomUUID().toString();

                photo = new Photo(id, selectedImagePath, width, height);
                photo.setPhotoOriginal(loadBitmap(selectedImagePath));

                Glide.with(this).load(photo.getUri()).into(mMainImage);

            }
        }
    }

    private byte[] bitmapToByte(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
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
