package com.example.fitboyz.thirdeye;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int REQUEST_STORAGE_PERMISSION = 201;
    private Button mBtnCamera, mBtnPhoto;
    private String TAG = "tag";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mBtnCamera = (Button) findViewById(R.id.btn_camera);
        mBtnPhoto = (Button)findViewById(R.id.btn_photo);

        mBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CustomCameraActivity.class));
            }
        });

        mBtnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PhotoActivity.class));
            }
        });

        
        requestPermissionThenOpenCamera();
//        read();


//        if (!read()) {
//            int[][][] precomputedData = Daltonize.getInstance().initializePrecompute();
//            write();
//        }
//        else {
//            write();
//        }


    }

    private void requestPermissionThenOpenCamera() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                if (!read()) {
//                    write();
//                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestPermissionThenOpenCamera();
            } else {
                Toast.makeText(MainActivity.this, "CAMERA PERMISSION REQUIRED", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        if(requestCode == REQUEST_STORAGE_PERMISSION) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestPermissionThenOpenCamera();
            } else {
                Toast.makeText(MainActivity.this, "STORAGE PERMISSION REQUIRED", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void write() {

        String filename = "NUMBERS.DATA";

        try {
            File file = new File(getApplicationContext().getFilesDir(), filename);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new IOException("Unable to create file");
                }
                // else { //prompt user to confirm overwrite }
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
//            int[][] NUMBERS;  // Populate it.
            int[][][] precomputedData = Daltonize.getInstance().initializePrecompute();
            oos.writeObject(precomputedData);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean read() {
        try {
            String filename = "NUMBERS.DATA";
            File file = new File(getApplicationContext().getFilesDir(), filename);
            FileInputStream fos = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fos);
            try {
                Object temp = ois.readObject();
                int[][][] res = (int[][][])temp;
                Daltonize.getInstance().setPrecompute(res);
                return true;
            }
            catch(Exception e) {
                e.printStackTrace();
                //handle it
            }

            return false;
//            if (precompute.length == 0) {
//                int[][][] precomputedData = Daltonize.getInstance().initializePrecompute();
//
//            } else {
//                Daltonize.getInstance().setPrecompute(precompute);
//            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;

//        FileInputStream fis;
//        try {
//            fis = openFileInput("CalEvents");
//            ObjectInputStream ois = new ObjectInputStream(fis);
//
//
//            ois.readObject();
//            ois.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}
