package com.example.photos;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.GridView;

import java.util.ArrayList;

public class SelectedAlbumView extends AppCompatActivity {

    ArrayList<Album> allAlbums;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_album_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        allAlbums = getIntent().getExtras().getParcelableArrayList("allAlbums");
        position = getIntent().getExtras().getInt("position");

        System.out.println(allAlbums.get(position));




        GridView albumThumbnails = findViewById(R.id.grid_view);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.add_photo_action:
                promptUserAddNewPhoto();
                return true;
            case R.id.search_photos_action:
                promptUserSearchQuery();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void promptUserAddNewPhoto(){


    }
    private void promptUserSearchQuery(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);
        input.setHint("Enter query here ");
        builder.setView(input);
        builder.setPositiveButton("Search by Location", ((dialog, which) -> transitionToSearchResults(0, input.getText().toString())));
        builder.setNegativeButton("Search by Person", ((dialog, which) -> transitionToSearchResults(1, input.getText().toString())));
        builder.setNeutralButton("Cancel", ((dialog, which) -> dialog.cancel()));
        builder.show();

    }
    private void transitionToSearchResults(int mode, String searchQuery){

    }

}