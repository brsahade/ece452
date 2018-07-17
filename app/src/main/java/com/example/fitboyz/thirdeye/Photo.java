package com.example.fitboyz.thirdeye;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.ByteArrayOutputStream;

public class Photo {
    private String id;
    private Uri uri;
    private int width;
    private int height;
    private Bitmap photoOriginal;
    private Bitmap photoDaltonized;

    Photo(String id, Uri uri, int width, int height) {
        this.id = id;
        this.uri = uri;
        this.width = width;
        this.height = height;
    }

    public Uri getUri() {
        return this.uri;
    }

    public String getId() {
        return this.id;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setPhotoOriginal(Bitmap photoOriginal) {
        this.photoOriginal = photoOriginal;
    }

    public Bitmap getPhotoOriginal() {
        return this.photoOriginal;
    }

    public void setPhotoDaltonized(Bitmap photoDaltonized) {
        this.photoDaltonized = photoDaltonized;
    }

    public Bitmap getPhotoDaltonized() {
        return this.photoDaltonized;
    }

    public void computeDalonization(int typeId) {
        Daltonize d = new Daltonize();
        this.photoDaltonized = d.daltonizeImage(getPhotoOriginal(), typeId);
    }
}
