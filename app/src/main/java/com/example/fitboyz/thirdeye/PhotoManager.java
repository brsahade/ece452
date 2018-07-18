package com.example.fitboyz.thirdeye;

import com.example.fitboyz.thirdeye.Photo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PhotoManager {
    private HashMap<String, Photo> photos;
    private List<Photo> photoList;

    private static PhotoManager pm;

    private PhotoManager() {

        photos = new HashMap<>();
        photoList = new ArrayList<>();
    }

    public static PhotoManager getInstance() {
        if (pm == null) {
            pm = new PhotoManager();
        }

        return pm;
    }

    public HashMap<String, Photo> getPhotos() {
        return photos;
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public Photo getPhotoById(int id) {
        return photos.get(id);
    }

    public Photo getPhotoByIndex(int index) {
        return photoList.get(index);
    }


    public void add(String id, Photo photo) {
        photos.put(id, photo);
        photoList.add(photo);
    }

    public boolean remove(String id) {
        Photo photo = photos.remove(id);
        if (photo != null) {
            return true;
        }
        return false;
    }
}
