package com.example.photos.activities;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.example.photos.R;
import com.example.photos.model.Album;
import com.example.photos.model.Photo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SelectedPhoto extends AppCompatActivity {

    ArrayList<Album> allAlbums;
    //Album currentAlbum;
    //Photo currentPhoto;
    int albumPosition;
    int photoPosition;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_photo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        loadData();
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        allAlbums = getIntent().getExtras().getParcelableArrayList("allAlbums");
        //currentAlbum = getIntent().getParcelableExtra("currentAlbum");
        //currentPhoto = getIntent().getParcelableExtra("chosenPhoto");
        albumPosition = getIntent().getExtras().getInt("albumPosition");
        photoPosition = getIntent().getExtras().getInt("photoPosition");

        // display selected image

        ImageView image = (ImageView) findViewById(R.id.selectedPhoto);
        image.setImageURI(Uri.parse(allAlbums.get(albumPosition).photos.get(photoPosition).photoPath));

        /*
        int imageResource = getResources().getIdentifier(
                currentPhoto.photoPath,
                null, this.getPackageName());

        image.setImageResource(imageResource);

         */

        /*

        TextView selectedPhotoTags = (TextView) findViewById(R.id.personTags);
        StringBuilder sbTags = new StringBuilder();
        for(String key: currentAlbum.photos.get(currentIndex).tags.keySet()) {
            sbTags.append(key);
            sbTags.append("=");
            sbTags.append(currentAlbum.photos.get(currentIndex).tags.get(key));
            sbTags.append("\n");
        }
        if (sbTags.length() > 0) {
            sbTags.setLength(sbTags.length() - 1);
        }
        selectedPhotoTags.setText(sbTags.toString());

         */

        TextView personTags = (TextView) findViewById(R.id.personTags);
        StringBuilder sbTags = new StringBuilder();
        sbTags.append("person: ");
        sbTags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person"));

        /*
        if (sbTags.length() > 0) {
            sbTags.setLength(sbTags.length() - 1);
        }

         */
        personTags.setText(sbTags.toString());

        TextView locationTags = (TextView) findViewById(R.id.locationTags);
        StringBuilder sb1Tags = new StringBuilder();
        sb1Tags.append("location: ");
        sb1Tags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location"));

        /*
        if (sb1Tags.length() > 0) {
            sb1Tags.setLength(sb1Tags.length() - 1);
        }

         */
        locationTags.setText(sb1Tags.toString());
        System.out.println("these are the tags");
        System.out.println(allAlbums.get(albumPosition).photos.get(photoPosition).tags);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.delete_tag_action:
                promptUserDeleteTag();
                return true;
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

    public boolean isValidTag(String tag) {
        return tag.matches("(.*)=(.*)");
    }

    private void promptUserDeleteTag() { // have to add in error messages still
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);
        input.setHint("Enter tag here ");

        builder.setView(input);
        builder.setPositiveButton("Remove Person Tag", ((dialog, which) -> deletePersonTag(input.getText().toString())));
        builder.setNegativeButton("Remove Location Tag", ((dialog, which) -> deleteLocationTag(input.getText().toString())));
        builder.show();

        /*

        // error message here for if tag is not entered in correct format

        String[] tagDeli= tag.split("=");
        String key = tagDeli[0];
        String value = tagDeli[1];

        // error message here for if tag entered is not present

        String existingValue = allAlbums.get(albumPosition).photos.get(photoPosition).tags.get(key);
        String[] existingValueDelimited = existingValue.split(",");
        StringBuilder newValue = new StringBuilder();

        for (String s : existingValueDelimited) {
            if (!s.equals(value)) {
                newValue.append(s + ",");
            }
        }

        if (newValue.length() != 0) {
            newValue.setLength(newValue.length() - 1);
            allAlbums.get(albumPosition).photos.get(photoPosition).tags.put(key, newValue.toString());
        }
        else {
            allAlbums.get(albumPosition).photos.get(photoPosition).tags.remove(key);
        }

        TextView selectedPhotoTags = (TextView) findViewById(R.id.personTags);
        StringBuilder sbTags = new StringBuilder();
        for(String k: allAlbums.get(albumPosition).photos.get(photoPosition).tags.keySet()) {
            sbTags.append(k);
            sbTags.append("=");
            sbTags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get(k));
            sbTags.append("\n");
        }
        if (sbTags.length() > 0) {
            sbTags.setLength(sbTags.length() - 1);
        }
        selectedPhotoTags.setText(sbTags.toString());

        saveData();

         */
    }

    private void deleteLocationTag(String tag) {
        String existingValue = allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location");
        String[] existingValueDelimited = existingValue.split(",");
        StringBuilder newValue = new StringBuilder();

        for (String s : existingValueDelimited) {
            if (!s.equals(tag)) {
                newValue.append(s + ",");
            }
        }

        if (newValue.length() != 0) {
            newValue.setLength(newValue.length() - 1);
            allAlbums.get(albumPosition).photos.get(photoPosition).tags.put("location", newValue.toString());
        }
        else {
            allAlbums.get(albumPosition).photos.get(photoPosition).tags.remove("location");
        }

        TextView locationTags = (TextView) findViewById(R.id.locationTags);
        StringBuilder sb1Tags = new StringBuilder();
        sb1Tags.append("location: ");
        sb1Tags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location"));

        locationTags.setText(sb1Tags.toString());

        saveData();
    }

    private void deletePersonTag(String tag) {
        String existingValue = allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person");
        String[] existingValueDelimited = existingValue.split(",");
        StringBuilder newValue = new StringBuilder();

        for (String s : existingValueDelimited) {
            if (!s.equals(tag)) {
                newValue.append(s + ",");
            }
        }

        if (newValue.length() != 0) {
            newValue.setLength(newValue.length() - 1);
            allAlbums.get(albumPosition).photos.get(photoPosition).tags.put("person", newValue.toString());
        }
        else {
            allAlbums.get(albumPosition).photos.get(photoPosition).tags.remove("person");
        }

        TextView personTags = (TextView) findViewById(R.id.personTags);
        StringBuilder sb1Tags = new StringBuilder();
        sb1Tags.append("person: ");
        sb1Tags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person"));

        personTags.setText(sb1Tags.toString());

        saveData();
    }

    private void promptUserAddNewTag() { // have to add in error messages still
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);
        input.setHint("Enter tag here ");

        builder.setView(input);
        builder.setPositiveButton("Add Person Tag", ((dialog, which) -> addPersonTag(input.getText().toString())));
        builder.setNegativeButton("Add Location Tag", ((dialog, which) -> addLocationTag(input.getText().toString())));
        builder.show();
        /*
        String tag = input.getText().toString();

        // check here to see if tag is entered in correct format

        String[] tagDelim = tag.split("=");
        String addedKey = tagDelim[0];
        String addedValue = tagDelim[1];

        if (!currentAlbum.photos.get(currentIndex).tags.containsKey(addedKey)) {
            currentAlbum.photos.get(currentIndex).tags.put(addedKey, addedValue);
        }
        else {
            String[] existingValueDelimited =
                    currentAlbum.photos.get(currentIndex).tags.get(addedKey).split(",");
            StringBuilder existingValueWithAddedValue =
                    new StringBuilder(currentAlbum.photos.get(currentIndex).tags.get(addedKey));
            existingValueWithAddedValue.append("," + addedValue);
            currentAlbum.photos.get(currentIndex).tags.put(addedKey, existingValueWithAddedValue.toString());
        }

        TextView selectedPhotoTags = (TextView) findViewById(R.id.personTags);
        StringBuilder sbTags = new StringBuilder();
        for(String key: currentAlbum.photos.get(currentIndex).tags.keySet()) {
            sbTags.append(key);
            sbTags.append("=");
            sbTags.append(currentAlbum.photos.get(currentIndex).tags.get(key));
            sbTags.append("\n");
        }
        if (sbTags.length() > 0) {
            sbTags.setLength(sbTags.length() - 1);
        }
        selectedPhotoTags.setText(sbTags.toString());

        saveData();

         */
    }

    private void addLocationTag(String tag) {
        System.out.println("the tag is " + tag);
        if (!allAlbums.get(albumPosition).photos.get(photoPosition).tags.containsKey("location")) {
            allAlbums.get(albumPosition).photos.get(photoPosition).tags.put("location", tag);
        }
        else {
            String[] existingValueDelimited =
                    allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location").split(",");
            StringBuilder existingValueWithAddedValue =
                    new StringBuilder(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location"));
            existingValueWithAddedValue.append("," + tag);
            allAlbums.get(albumPosition).photos.get(photoPosition).tags.put("location", existingValueWithAddedValue.toString());
        }
        TextView locationTags = (TextView) findViewById(R.id.locationTags);
        StringBuilder sbTags = new StringBuilder();
        sbTags.append("location: ");
        sbTags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location"));

        /*
        if (sbTags.length() > 0) {
            sbTags.setLength(sbTags.length());
        }

         */
        locationTags.setText(sbTags.toString());
        //System.out.println(sbTags.toString());

        System.out.println(allAlbums.get(albumPosition).photos.get(photoPosition).tags);

        saveData();
    }

    private void addPersonTag(String tag) {
        System.out.println("the tag is " + tag);
        if (!allAlbums.get(albumPosition).photos.get(photoPosition).tags.containsKey("person")) {
            allAlbums.get(albumPosition).photos.get(photoPosition).tags.put("person", tag);
        }
        else {
            String[] existingValueDelimited =
                    allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person").split(",");
            StringBuilder existingValueWithAddedValue =
                    new StringBuilder(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person"));
            existingValueWithAddedValue.append("," + tag);
            allAlbums.get(albumPosition).photos.get(photoPosition).tags.put("person", existingValueWithAddedValue.toString());
        }
        TextView personTags = (TextView) findViewById(R.id.personTags);
        StringBuilder sbTags = new StringBuilder();
        sbTags.append("person: ");
        sbTags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person"));

        /*
        if (sbTags.length() > 0) {
            sbTags.setLength(sbTags.length() - 1);
        }

         */
        personTags.setText(sbTags.toString());

        saveData();
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

            image.setImageURI(Uri.parse(allAlbums.get(albumPosition).photos.get(photoPosition).photoPath));


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

            // now we delete the photo and update photo position
            allAlbums.get(albumPosition).photos.remove(oldPosition);
            saveData();

        }

        // if we are removing the first photo, then we display the photo that is next
        else {
            oldPosition = 0;
            photoPosition++;
            //currentPhoto = currentAlbum.photos.get(photoPosition);

            ImageView image = (ImageView) findViewById(R.id.selectedPhoto);
            image.setImageURI(Uri.parse(allAlbums.get(albumPosition).photos.get(photoPosition).photoPath));

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

            // now we delete the photo and update photo position
            allAlbums.get(albumPosition).photos.remove(oldPosition);
            saveData();
        }

    }



    public void nextPhoto(View view) {
        if (photoPosition < allAlbums.get(albumPosition).photos.size() - 1) {
            photoPosition++;
            //currentPhoto = allAlbums.get(albumPosition).photos.get(photoPosition);

            ImageView image = (ImageView) findViewById(R.id.selectedPhoto);
            image.setImageURI(Uri.parse(allAlbums.get(albumPosition).photos.get(photoPosition).photoPath));

            /*

            int imageResource = getResources().getIdentifier(
                    currentAlbum.photos.get(photoPosition).photoPath,
                    null, this.getPackageName());

            image.setImageResource(imageResource);

             */

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
            //currentPhoto = currentAlbum.photos.get(photoPosition);
            saveData();
        }
    }

    public void previousPhoto(View view) {
        if (photoPosition > 0) {
            photoPosition--;
            //currentPhoto = currentAlbum.photos.get(photoPosition);

            ImageView image = (ImageView) findViewById(R.id.selectedPhoto);
            image.setImageURI(Uri.parse(allAlbums.get(albumPosition).photos.get(photoPosition).photoPath));

            /*

            int imageResource = getResources().getIdentifier(
                    currentAlbum.photos.get(photoPosition).photoPath,
                    null, this.getPackageName());

            image.setImageResource(imageResource);

             */

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
            //currentPhoto = currentAlbum.photos.get(photoPosition);
            saveData();

        }
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

    }
}