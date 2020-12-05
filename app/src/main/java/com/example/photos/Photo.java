package com.example.photos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;

public class Photo implements Parcelable {
    String photoPath;
    HashMap<String, String> tags;
    public Photo(String photoPath, HashMap<String,String> tags){
        this.photoPath = photoPath;
        this.tags = tags;
    }

    protected Photo(Parcel in) {
        photoPath = in.readString();
        tags = in.readHashMap(String.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(photoPath);
        dest.writeMap(tags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(photoPath);
        sb.append("/n");
        sb.append(tags);
        return sb.toString();
    }
}
