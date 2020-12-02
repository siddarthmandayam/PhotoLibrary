package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Photos extends AppCompatActivity {

    //listview element in UI
    private ListView listOfAlbums;

    //internal storage that will populate the listview
    private List<Album> userAlbums;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set home_page to be the layout
        setContentView(R.layout.home_page);

        //hook up all elements by ther id
        listOfAlbums = findViewById(R.id.album_list);

        //manually initialize for now, will worry about serialization later
        userAlbums = new ArrayList<Album>();
        userAlbums.add(new Album("Album 1", null));
        userAlbums.add(new Album("Album 2", null));
        userAlbums.add(new Album("Album 3", null));



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


    }
    //Pass the selected album information
    private void transitionToTheSelectedAlbum(int position){
        Bundle bundle = new Bundle();

        //add mapping "selected_album" -> userAlbum[position]
        bundle.putSerializable("selected_album", userAlbums.get(position));
        Intent intent = new Intent(this, Photos.class);
        intent.putExtras(bundle);
        startActivity(intent);



    }
}