package com.example.photos.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Album implements Parcelable {
    public String name;
    public ArrayList<Photo> photos;
    public Album(String name, ArrayList<Photo> photos){
        this.name = name;
        this.photos = photos;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelableList(photos, flags);
    }

    public Album(Parcel in) {
        name = in.readString();
        photos = new ArrayList<Photo>();
        in.readParcelableList(photos, Photo.class.getClassLoader());
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    public String toString(){
        return name;
    }


}
