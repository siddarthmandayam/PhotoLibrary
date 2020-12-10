package com.example.photos.activities;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.example.photos.model.Album;
import com.example.photos.model.Photo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.photos.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SelectedSearchResult extends AppCompatActivity {

    ArrayList<Photo> allPhotos;
    int photoPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_search_result);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        allPhotos = getIntent().getExtras().getParcelableArrayList("allPhotos");
        photoPosition = getIntent().getExtras().getInt("photoPosition");

        ImageView image = (ImageView) findViewById(R.id.selectedPhoto);
        image.setImageURI(Uri.parse(allPhotos.get(photoPosition).photoPath));

        TextView personTags = (TextView) findViewById(R.id.personTags);
        StringBuilder sbTags = new StringBuilder();
        sbTags.append("person: ");
        if (allPhotos.get(photoPosition).tags.get("person") != null) {
            sbTags.append(allPhotos.get(photoPosition).tags.get("person"));
        }
        personTags.setText(sbTags.toString());

        TextView locationTags = (TextView) findViewById(R.id.locationTags);
        StringBuilder sb1Tags = new StringBuilder();
        sb1Tags.append("location: ");
        if (allPhotos.get(photoPosition).tags.get("location") != null) {
            sb1Tags.append(allPhotos.get(photoPosition).tags.get("location"));
        }
        locationTags.setText(sb1Tags.toString());
    }

    public void nextPhoto(View view) {
        if (photoPosition < allPhotos.size() - 1) {
            photoPosition++;
            //currentPhoto = allAlbums.get(albumPosition).photos.get(photoPosition);

            ImageView image = (ImageView) findViewById(R.id.selectedPhoto);
            image.setImageURI(Uri.parse(allPhotos.get(photoPosition).photoPath));

            /*
            TextView personTags = (TextView) findViewById(R.id.personTags);
            StringBuilder sbTags = new StringBuilder();
            sbTags.append("person: ");
            sbTags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person"));

            personTags.setText(sbTags.toString());

            TextView locationTags = (TextView) findViewById(R.id.locationTags);
            StringBuilder sb1Tags = new StringBuilder();
            sb1Tags.append("location: ");
            sb1Tags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location"));

            locationTags.setText(sb1Tags.toString());

             */

            TextView personTags = (TextView) findViewById(R.id.personTags);
            StringBuilder sbTags = new StringBuilder();
            sbTags.append("person: ");
            if (allPhotos.get(photoPosition).tags.get("person") != null) {
                sbTags.append(allPhotos.get(photoPosition).tags.get("person"));
            }
            personTags.setText(sbTags.toString());

            TextView locationTags = (TextView) findViewById(R.id.locationTags);
            StringBuilder sb1Tags = new StringBuilder();
            sb1Tags.append("location: ");
            if (allPhotos.get(photoPosition).tags.get("location") != null) {
                sb1Tags.append(allPhotos.get(photoPosition).tags.get("location"));
            }
            locationTags.setText(sb1Tags.toString());
            //currentPhoto = currentAlbum.photos.get(photoPosition);
        }
    }

    public void previousPhoto(View view) {
        if (photoPosition > 0) {
            photoPosition--;
            //currentPhoto = currentAlbum.photos.get(photoPosition);

            ImageView image = (ImageView) findViewById(R.id.selectedPhoto);
            image.setImageURI(Uri.parse(allPhotos.get(photoPosition).photoPath));

            /*
            TextView personTags = (TextView) findViewById(R.id.personTags);
            StringBuilder sbTags = new StringBuilder();
            sbTags.append("person: ");
            sbTags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person"));

            personTags.setText(sbTags.toString());

            TextView locationTags = (TextView) findViewById(R.id.locationTags);
            StringBuilder sb1Tags = new StringBuilder();
            sb1Tags.append("location: ");
            sb1Tags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location"));

            locationTags.setText(sb1Tags.toString());

             */
            TextView personTags = (TextView) findViewById(R.id.personTags);
            StringBuilder sbTags = new StringBuilder();
            sbTags.append("person: ");
            if (allPhotos.get(photoPosition).tags.get("person") != null) {
                sbTags.append(allPhotos.get(photoPosition).tags.get("person"));
            }
            personTags.setText(sbTags.toString());

            TextView locationTags = (TextView) findViewById(R.id.locationTags);
            StringBuilder sb1Tags = new StringBuilder();
            sb1Tags.append("location: ");
            if (allPhotos.get(photoPosition).tags.get("location") != null) {
                sb1Tags.append(allPhotos.get(photoPosition).tags.get("location"));
            }
            locationTags.setText(sb1Tags.toString());
            //currentPhoto = currentAlbum.photos.get(photoPosition);
        }
    }
}