package com.example.photos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Data implements Parcelable {
    List<Album> albums;

    public Data(){
        albums = new ArrayList<Album>();
    }

    //parcelling constructor
    public Data(Parcel in){
        albums = in.readParcelableList(albums, Album.class.getClassLoader());

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelableList(albums, flags);

    }
    public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    /*
    public static void saveData(String fileName){
        FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(this);
        os.close();
        fos.close();
    }
    */

}


