package com.example.photos;

import android.app.AlertDialog;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SelectedPhoto extends AppCompatActivity {

    ArrayList<Album> allAlbums;
    int albumPosition;
    int photoPosition;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_photo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        allAlbums = getIntent().getExtras().getParcelableArrayList("allAlbums");
        albumPosition = getIntent().getExtras().getInt( "albumPosition");
        photoPosition = getIntent().getExtras().getInt("position");

        // display selected image

        ImageView image = (ImageView) findViewById(R.id.selectedPhoto);

        int imageResource = getResources().getIdentifier(
                allAlbums.get(albumPosition).photos.get(photoPosition).photoPath,
                null, this.getPackageName());

        image.setImageResource(imageResource);

        TextView selectedPhotoTags = (TextView) findViewById(R.id.selectedPhotoTags);
        StringBuilder sbTags = new StringBuilder();
        for(String key: allAlbums.get(albumPosition).photos.get(photoPosition).tags.keySet()) {
            sbTags.append(key);
            sbTags.append("=");
            sbTags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get(key));
            sbTags.append("\n");
        }
        if (sbTags.length() > 0) {
            sbTags.setLength(sbTags.length() - 1);
        }
        selectedPhotoTags.setText(sbTags.toString());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.add_tag_action:
                promptUserAddNewTag();
                return true;
            case R.id.delete_photo_action:
                promptUserDeletePhoto();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void promptUserAddNewTag() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);
        input.setHint("Enter tag here ");

        // still have to finish this
    }

    private void promptUserDeletePhoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView question = new TextView(this);
        question.setText("Are you sure you want to delete this photo?");

        builder.setView(question);
        builder.setPositiveButton("Yes", ((dialog, which) -> deletePhoto()));
        builder.setNegativeButton("No", ((dialog, which) -> dialog.cancel()));
        builder.show();
    }

    private void deletePhoto() {
        int oldPosition = 0;
        // if not the first photo then we display the previous photo before we delete it
        if (photoPosition > 0) {
            oldPosition = photoPosition;
            photoPosition--;

            ImageView image = (ImageView) findViewById(R.id.selectedPhoto);

            int imageResource = getResources().getIdentifier(
                    allAlbums.get(albumPosition).photos.get(photoPosition).photoPath,
                    null, this.getPackageName());

            image.setImageResource(imageResource);

            TextView selectedPhotoTags = (TextView) findViewById(R.id.selectedPhotoTags);
            StringBuilder sbTags = new StringBuilder();
            for(String key: allAlbums.get(albumPosition).photos.get(photoPosition).tags.keySet()) {
                sbTags.append(key);
                sbTags.append("=");
                sbTags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get(key));
                sbTags.append("\n");
            }
            if (sbTags.length() > 0) {
                sbTags.setLength(sbTags.length() - 1);
            }
            selectedPhotoTags.setText(sbTags.toString());

            // now we delete the photo and update photo position
            allAlbums.get(albumPosition).photos.remove(oldPosition);

        }

        // if we are removing the first photo, then we display the photo that is next
        else {
            oldPosition = 0;
            photoPosition++;

            ImageView image = (ImageView) findViewById(R.id.selectedPhoto);

            int imageResource = getResources().getIdentifier(
                    allAlbums.get(albumPosition).photos.get(photoPosition).photoPath,
                    null, this.getPackageName());

            image.setImageResource(imageResource);

            TextView selectedPhotoTags = (TextView) findViewById(R.id.selectedPhotoTags);
            StringBuilder sbTags = new StringBuilder();
            for(String key: allAlbums.get(albumPosition).photos.get(photoPosition).tags.keySet()) {
                sbTags.append(key);
                sbTags.append("=");
                sbTags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get(key));
                sbTags.append("\n");
            }
            if (sbTags.length() > 0) {
                sbTags.setLength(sbTags.length() - 1);
            }
            selectedPhotoTags.setText(sbTags.toString());

            // now we delete the photo
            allAlbums.get(albumPosition).photos.remove(oldPosition);
            photoPosition = 0;
        }

    }

    public void nextPhoto(View view) {
        if (photoPosition < allAlbums.get(albumPosition).photos.size() - 1) {
            photoPosition++;

            ImageView image = (ImageView) findViewById(R.id.selectedPhoto);

            int imageResource = getResources().getIdentifier(
                    allAlbums.get(albumPosition).photos.get(photoPosition).photoPath,
                    null, this.getPackageName());

            image.setImageResource(imageResource);

            TextView selectedPhotoTags = (TextView) findViewById(R.id.selectedPhotoTags);
            StringBuilder sbTags = new StringBuilder();
            for(String key: allAlbums.get(albumPosition).photos.get(photoPosition).tags.keySet()) {
                sbTags.append(key);
                sbTags.append("=");
                sbTags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get(key));
                sbTags.append("\n");
            }
            if (sbTags.length() > 0) {
                sbTags.setLength(sbTags.length() - 1);
            }
            selectedPhotoTags.setText(sbTags.toString());
        }
    }

    public void previousPhoto(View view) {
        if (photoPosition > 0) {
            photoPosition--;

            ImageView image = (ImageView) findViewById(R.id.selectedPhoto);

            int imageResource = getResources().getIdentifier(
                    allAlbums.get(albumPosition).photos.get(photoPosition).photoPath,
                    null, this.getPackageName());

            image.setImageResource(imageResource);

            TextView selectedPhotoTags = (TextView) findViewById(R.id.selectedPhotoTags);
            StringBuilder sbTags = new StringBuilder();
            for(String key: allAlbums.get(albumPosition).photos.get(photoPosition).tags.keySet()) {
                sbTags.append(key);
                sbTags.append("=");
                sbTags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get(key));
                sbTags.append("\n");
            }
            if (sbTags.length() > 0) {
                sbTags.setLength(sbTags.length() - 1);
            }
            selectedPhotoTags.setText(sbTags.toString());

        }
    }
}