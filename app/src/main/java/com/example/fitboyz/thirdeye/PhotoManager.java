package com.example.fitboyz.thirdeye;

import com.example.fitboyz.thirdeye.Photo;

import java.util.HashMap;

public class PhotoManager {
    private HashMap<String, Photo> map;

    private static PhotoManager pm;

    private PhotoManager() {
        map = new HashMap<>();
    }

    public static PhotoManager getInstance() {
        if (pm == null) {
            pm = new PhotoManager();
        }

        return pm;
    }

    public void add(String id, Photo photo) {
        map.put(id, photo);
    }

    public boolean remove(String id) {
        Photo photo = map.remove(id);
        if (photo != null) {
            return true;
        }
        return false;
    }
}
