package com.example.photos.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;


import com.example.photos.R;
import com.example.photos.model.Album;
import com.example.photos.model.Photo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Photos extends AppCompatActivity {

    //listview element in UI
    private ListView listOfAlbums;
    ArrayAdapter<Album> adapter;

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
        adapter = new ArrayAdapter<>(this, R.layout.album, userAlbums);
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
        listOfAlbums.setOnItemClickListener((parents, view, position, id) -> selectedAlbumActionOptions(position, view));



    }

    private void selectedAlbumActionOptions(int position, View v){
        PopupMenu popUpActions = new PopupMenu(this, v);
        popUpActions.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.openAction:
                        transitionToTheSelectedAlbum(position);
                        return true;
                    case R.id.renameAction:
                        promptRenameAlbum(position);
                        return true;
                    case R.id.deleteAction:
                        deleteAlbum(position);
                        return true;
                    case R.id.cancelAction:
                        popUpActions.dismiss();
                        return true;
                    default:
                        return false;

                }
            }
        });
        popUpActions.inflate(R.menu.home_page_selected_album_actions);
        popUpActions.show();

    }
    private void promptRenameAlbum(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);
        input.setHint("Rename album ");
        builder.setView(input);
        builder.setPositiveButton("Rename", ((dialog, which) -> renameAlbum(input.getText().toString(), position)));
        builder.setNegativeButton("Cancel", ((dialog, which) -> dialog.cancel()));
        builder.show();


    }
    private void renameAlbum(String newName, int position){
        userAlbums.get(position).name = newName;
        adapter.notifyDataSetChanged();
        saveData();
    }
    private void deleteAlbum(int position){
        userAlbums.remove(position);
        adapter.notifyDataSetChanged();
        saveData();

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
            case R.id.search_photos_action:
                promptUserSearchQuery();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void promptUserSearchQuery(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);
        builder.setMessage("Examples of valid queries: \n location=sweden \n location=sweden AND person=joe \n location=sweden OR location=hungary");
        input.setHint("Enter query here ");
        builder.setView(input);
        builder.setPositiveButton("Search", (dialog, which) -> {
            String searchQuery = input.getText().toString();
            if(searchQuery.equals("")){
                System.out.println("query is empty");
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("Query is empty");
                alert.setNeutralButton("OK", null);
                alert.show();

            }
            String[] tagFilterDelim = searchQuery.split(" ");
            if(!isValidTag(tagFilterDelim)){
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("Query format is incorrect");
                alert.setNeutralButton("OK", null);
                alert.show();
            }
            else {
                ArrayList<Photo> searchResults = new ArrayList<Photo>();
                HashSet<Photo> seen = new HashSet<Photo>();

                for(Album album : userAlbums){
                    for(Photo photo: album.photos){
                        if(seen.contains(photo))
                            continue;
                        seen.add(photo);
                        if(tagFilterDelim.length == 1){
                            String[] tagValue = tagFilterDelim[0].split("=");
                            if(photo.tags.containsKey(tagValue[0]) && containsValue(photo.tags.get(tagValue[0]),tagValue[1])){
                                searchResults.add(photo);
                            }
                        }
                        else{
                            String firstKeyValue = tagFilterDelim[0];
                            String secondKeyValue = tagFilterDelim[2];

                            String[] firstDelimited = firstKeyValue.split("=");
                            String[] secondDelimited = secondKeyValue.split("=");

                            String firstKey = firstDelimited[0];
                            String firstValue = firstDelimited[1];
                            String secondKey = secondDelimited[0];
                            String secondValue = secondDelimited[1];

                            if(tagFilterDelim[1].equals("OR")) {
                                if((photo.tags.containsKey(firstKey) && containsValue(photo.tags.get(firstKey), firstValue) || (photo.tags.containsKey(secondKey) && containsValue(photo.tags.get(secondKey),secondValue)))) {
                                    searchResults.add(photo);
                                }
                            }
                            else {
                                if((photo.tags.containsKey(firstKey) && containsValue(photo.tags.get(firstKey), firstValue) && (photo.tags.containsKey(secondKey) && containsValue(photo.tags.get(secondKey),secondValue)))) {
                                    searchResults.add(photo);
                                }

                            }

                        }

                    }
                }

                Intent intent = new Intent(this, SearchResults.class);
                intent.putParcelableArrayListExtra("searchResults", searchResults);
                startActivity(intent);

            }

        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();

    }
    private boolean containsValue(String values, String targetValue){
        String[] delimitedValues = values.split(",");
        for(String currentValue: delimitedValues) {
            if(currentValue.equals(targetValue) || currentValue.startsWith(targetValue)) return true;
        }
        return false;

    }

    /*
    private void transitionToSearchResults(String searchQuery){
        System.out.println("checkign query");
        if(searchQuery.equals("")){
            System.out.println("query is empty");
            Toast.makeText(Photos.this, "Query cannot be empty",
                    Toast.LENGTH_LONG).show();

        }
        String[] tagFilterDelim = searchQuery.split(" ");
        if(!isValidTag(tagFilterDelim)){
            Toast.makeText(this, "Query format is incorrect",
                    Toast.LENGTH_LONG).show();
        }
        else {

            ArrayList<Photo> searchResults = new ArrayList<Photo>();

            //add manually for now
            searchResults.add(userAlbums.get(0).photos.get(0));
            searchResults.add(userAlbums.get(0).photos.get(1));

            Intent intent = new Intent(this, SearchResults.class);
            intent.putParcelableArrayListExtra("searchResults", searchResults);
            startActivity(intent);

        }

    }

     */
    private boolean isValidTag(String[] tagDelim){
        if(tagDelim.length != 1 && tagDelim.length != 3){

            return false;
        }
        if(tagDelim.length == 1){
            if(!tagDelim[0].matches("((location|person).*)=(.*)")){

                return false;

            }
        }
        if(tagDelim.length == 3){
            if(!(tagDelim[0].matches("((location|person).*)=(.*)") && tagDelim[2].matches("((location|person).*)=(.*)") && tagDelim[1].matches("OR|AND"))){
                if(!tagDelim[0].matches("((location|person).*)=(.*)")){
                    return false;
                }

            }
        }
        return true;

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
        adapter.notifyDataSetChanged();
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