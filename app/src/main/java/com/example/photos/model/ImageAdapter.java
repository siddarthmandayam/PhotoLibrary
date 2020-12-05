package com.example.photos.model;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.photos.model.Photo;

import java.util.ArrayList;


public class ImageAdapter extends BaseAdapter {
    ArrayList<Photo> selectedAlbum;
    Context mContext;

    public ImageAdapter(ArrayList<Photo> selectedAlbum, Context mContext){
        this.selectedAlbum = selectedAlbum;
        this.mContext = mContext;

    }

    @Override
    public int getCount() {
        return selectedAlbum.size();
    }

    @Override
    public Object getItem(int position) {
        return selectedAlbum.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(5, 10, 5, 10);
        } else {
            imageView = (ImageView) convertView;}
        imageView.setImageURI(Uri.parse(selectedAlbum.get(position).photoPath));
       // thumbnail.setImageBitmap(BitmapFactory.decodeFile(selectedAlbum.get(position).photoPath));
        return imageView;
    }
}
