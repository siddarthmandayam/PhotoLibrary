package com.example.photos;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Data implements Serializable {
    List<Album> albums;

    public Data(){
        albums = new ArrayList<Album>();
    }

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


