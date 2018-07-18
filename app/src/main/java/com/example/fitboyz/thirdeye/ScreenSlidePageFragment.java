package com.example.fitboyz.thirdeye;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class ScreenSlidePageFragment extends Fragment {


    private ImageView mImageView;
    private int mNum;

    public static ScreenSlidePageFragment newInstance(int num) {
        ScreenSlidePageFragment f = new ScreenSlidePageFragment();

        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);

        Photo photo = PhotoManager.getInstance().getPhotoByIndex(mNum);
//        List<Photo> photos = PhotoManager.getInstance().getPhotoList();

        mImageView = rootView.findViewById(R.id.image);
        Glide.with(this).load(photo.getUri()).into(mImageView);

        return rootView;
    }
}