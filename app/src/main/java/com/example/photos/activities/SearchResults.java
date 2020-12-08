package com.example.photos.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.photos.model.ImageAdapter;
import com.example.photos.model.Photo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.GridView;

import com.example.photos.R;

import java.util.ArrayList;

public class SearchResults extends AppCompatActivity {

    ArrayList<Photo> searchResults;
    ImageAdapter adapter;
    GridView resultsGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        resultsGrid = findViewById(R.id.grid_results);

        searchResults = getIntent().getExtras().getParcelableArrayList("searchResults");

        adapter = new ImageAdapter(searchResults, this);
        //allAlbums.get(position).photos.add(new Photo("content://com.android.externalstorage.documents/document/primary%3ADownload%2FStockPhoto1.jpeg", null));
        resultsGrid.setAdapter(adapter);
        resultsGrid.setOnItemClickListener((parent, view, position1, id) -> transitionToSelectedPhoto(position1));

    }

    private void transitionToSelectedPhoto(int photoPosition){
        Intent intent = new Intent(this, SelectedSearchResult.class);
        intent.putParcelableArrayListExtra("allPhotos", searchResults);
        startActivity(intent);


    }
}