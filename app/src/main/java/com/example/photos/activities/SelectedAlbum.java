package com.example.photos.activities;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.GridView;

import com.example.photos.R;
import com.example.photos.model.Album;
import com.example.photos.model.ImageAdapter;
import com.example.photos.model.Photo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectedAlbum extends AppCompatActivity {

    ArrayList<Album> allAlbums;
    int position;
    ImageAdapter adapter;
    GridView thumbnailGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_album_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        allAlbums = getIntent().getExtras().getParcelableArrayList("allAlbums");
        position = getIntent().getExtras().getInt("position");
        thumbnailGrid = findViewById(R.id.grid_view);

        //allAlbums.get(position).photos.add(new Photo())



        System.out.println(allAlbums.get(position));
        adapter = new ImageAdapter(allAlbums.get(position).photos, this);
        //allAlbums.get(position).photos.add(new Photo("content://com.android.externalstorage.documents/document/primary%3ADownload%2FStockPhoto1.jpeg", null));
        thumbnailGrid.setAdapter(adapter);
        thumbnailGrid.setOnItemClickListener((parent, view, position1, id) -> transitionToSelectedPhoto(position1));


    }

    public static final int PICK_IMAGE = 1;

    public static final int BACK = 2;


    private void transitionToSelectedPhoto(int photoPosition){
        Intent intent = new Intent(this, SelectedPhoto.class);
        intent.putParcelableArrayListExtra("allAlbums", allAlbums);
        intent.putExtra("albumPosition", position);
        intent.putExtra("photoPosition", photoPosition);
        startActivityForResult(intent, BACK);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {



            Uri selectedImage = data.getData();
            System.out.println(selectedImage);

            int flags = data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;;
            ContentResolver resolver = this.getContentResolver();
            resolver.takePersistableUriPermission(selectedImage, flags);

            /*
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            //Cursor cursor = getContentResolver().query(selectedImage, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            System.out.println(cursor);
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

             */

            System.out.println(selectedImage);
            allAlbums.get(position).photos.add(new Photo(selectedImage.toString(), new HashMap<String,String>()));
            adapter.notifyDataSetChanged();
            thumbnailGrid.invalidate();


            System.out.println("SAVING DATA...");
            saveData();


        }
        else if (requestCode == BACK){
            if (resultCode == RESULT_OK) {
                allAlbums = data.getParcelableArrayListExtra("allAlbums");
                adapter = new ImageAdapter(allAlbums.get(position).photos, this);
                //allAlbums.get(position).photos.add(new Photo("content://com.android.externalstorage.documents/document/primary%3ADownload%2FStockPhoto1.jpeg", null));
                thumbnailGrid.setAdapter(adapter);
            }

        }
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void promptUserAddNewPhoto(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");

        //Intent intent = new Intent();
        //intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);

        //Intent intent = new Intent(Intent.ACTION_PICK,
                //MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        //Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);


    }

    private void saveData(){
        SharedPreferences sharedP = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedP.edit();
        Gson gson = new Gson();
        String json = gson.toJson(allAlbums);
        editor.putString("data", json);
        editor.apply();
    }

    private void loadData(){
        SharedPreferences sharedP = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedP.getString("data", null);
        Type type = new TypeToken<List<Album>>(){}.getType();
        allAlbums = gson.fromJson(json, type);

        if(allAlbums == null)
            allAlbums = new ArrayList<Album>();

        System.out.println("LOADING FOLLOWING DATA: ");

        for(Album album: allAlbums){
            System.out.println(album.name + " - ");
            if(album.photos != null)
                for(Photo photo: album.photos){
                    System.out.println("path: " + photo.photoPath);
                    System.out.println("tags: " + photo.tags);
                }
        }

    }


}