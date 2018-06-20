package com.example.fitboyz.thirdeye;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;

public class PhotoActivity extends AppCompatActivity {

    // this is the action code we use in our intent,
    // this way we know we're looking at the response from our own action
    private static final int SELECT_PICTURE = 1;

    private Uri selectedImagePath;
    private ImageView mMainImage;
    private LinearLayout mTypeOne, mTypeTwo, mTypeThree;
    private Button mButton;

    public void onCreate(Bundle savedInstanceState) {
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

        // in onCreate or any event where your want the user to
        // select a file
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);

    }

    public void daltonization(int typeId) {

        Daltonize d = new Daltonize();
        Bitmap bitmap = loadBitmap(selectedImagePath);
        Bitmap newBItmap = d.daltonizeImage(bitmap, typeId);
        updateImage(newBItmap);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = selectedImageUri;
                Glide.with(this).load(selectedImagePath).into(mMainImage);

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
