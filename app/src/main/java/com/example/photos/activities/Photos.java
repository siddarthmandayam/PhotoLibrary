package com.example.photos.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.photos.R;
import com.example.photos.model.Album;
import com.example.photos.model.Photo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Photos extends AppCompatActivity {

    //listview element in UI
    private ListView listOfAlbums;

    //internal storage that will populate the listview
    private ArrayList<Album> userAlbums;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set home_page to be the layout
        setContentView(R.layout.home_page);
        //hook up all elements by ther id
        listOfAlbums = findViewById(R.id.album_list);

        //load data into list
        loadData();



        //connects the source data to the listview
        ArrayAdapter<Album> adapter =
                new ArrayAdapter<>(this, R.layout.album, userAlbums);
        listOfAlbums.setAdapter(adapter);


        //set listview to listen to selections

        /*
        listOfAlbums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                transitionToTheSelectedAlbum(position);
            }
        });
        */

        //it's a functional interface, so we can replace it with a lambda expression:
        listOfAlbums.setOnItemClickListener((parents, view, position, id) -> transitionToTheSelectedAlbum(position));

    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.add_album_action:
                promptUserAddNewAlbum();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void promptUserAddNewAlbum(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);
        input.setHint("New Album Name");
        builder.setView(input);
        builder.setPositiveButton("Create", ((dialog, which) -> createNewAlbum(input.getText().toString())));
        builder.setNegativeButton("Cancel", ((dialog, which) -> dialog.cancel()));
        builder.show();

    }
    private void createNewAlbum(String newAlbumName){
        userAlbums.add(new Album(newAlbumName, null));
        saveData();



    }
    private void saveData(){
        SharedPreferences sharedP = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedP.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userAlbums);
        editor.putString("data", json);
        editor.apply();
    }

    private void loadData(){
        SharedPreferences sharedP = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedP.getString("data", null);
        Type type = new TypeToken<List<Album>>(){}.getType();
        userAlbums = gson.fromJson(json, type);

        if(userAlbums == null)
            userAlbums = new ArrayList<Album>();

        System.out.println("LOADING FOLLOWING DATA: ");

        for(Album album: userAlbums){
            System.out.println(album.name + " - ");
            if(album.photos != null)
            for(Photo photo: album.photos){
                System.out.println("path: " + photo.photoPath);
                System.out.println("tags: " + photo.tags);
            }
        }

    }
    //Pass the selected album information
    private void transitionToTheSelectedAlbum(int position){
        Bundle bundle = new Bundle();

        //add mapping "selected_album" -> userAlbum[position]
        //bundle.putParcelableArrayList("allAlbums", userAlbums);
        //bundle.putInt("position", position);


        //pass in all albums, and specific chosen album
        Intent intent = new Intent(this, SelectedAlbum.class);
        intent.putParcelableArrayListExtra("allAlbums", userAlbums);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}