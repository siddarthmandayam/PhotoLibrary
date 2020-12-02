package com.example.photos;

import java.io.Serializable;
import java.util.HashMap;

public class Photo implements Serializable {
    String photoPath;
    HashMap<String, String> tags;
    public Photo(String photoPath, HashMap<String,String> tags){
        this.photoPath = photoPath;
        this.tags = tags;
    }
}
