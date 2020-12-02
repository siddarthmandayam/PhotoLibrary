package com.example.photos;

import java.io.Serializable;
import java.util.List;

public class Album implements Serializable {
    String name;
    List<Photo> photos;
    public Album(String name, List<Photo> photos){
        this.name = name;
        this.photos = photos;
    }

    public String toString(){
        return name;
    }
}
