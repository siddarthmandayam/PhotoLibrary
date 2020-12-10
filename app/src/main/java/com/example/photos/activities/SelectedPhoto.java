package com.example.photos.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SelectedPhoto extends AppCompatActivity {

    ArrayList<Album> allAlbums;
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
        getSupportActionBar().setHomeButtonEnabled(true);


        allAlbums = getIntent().getExtras().getParcelableArrayList("allAlbums");
        albumPosition = getIntent().getExtras().getInt("albumPosition");
        photoPosition = getIntent().getExtras().getInt("photoPosition");

        // display selected image

        ImageView image = (ImageView) findViewById(R.id.selectedPhoto);
        image.setImageURI(Uri.parse(allAlbums.get(albumPosition).photos.get(photoPosition).photoPath));

        TextView personTags = (TextView) findViewById(R.id.personTags);
        StringBuilder sbTags = new StringBuilder();
        sbTags.append("person: ");
        if (allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person") != null) {
            sbTags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person"));
        }
        personTags.setText(sbTags.toString());

        TextView locationTags = (TextView) findViewById(R.id.locationTags);
        StringBuilder sb1Tags = new StringBuilder();
        sb1Tags.append("location: ");
        if (allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location") != null) {
            sb1Tags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location"));
        }
        locationTags.setText(sb1Tags.toString());

        //System.out.println("these are the tags");
        //System.out.println(allAlbums.get(albumPosition).photos.get(photoPosition).tags);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.move_photo_action:
                promptUserMovePhoto();
                return true;
            case R.id.delete_tag_action:
                promptUserDeleteTag();
                return true;
            case R.id.add_tag_action:
                promptUserAddNewTag();
                return true;
            case R.id.delete_photo_action:
                promptUserDeletePhoto();
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void promptUserMovePhoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);
        input.setHint("Enter album name here");

        builder.setView(input);
        builder.setPositiveButton("Move Photo to Album", ((dialog, which) -> movePhotoToAlbum(input.getText().toString())));
        builder.setNegativeButton("Cancel", (((dialog, which) -> dialog.cancel())));
        builder.show();

    }

    private void movePhotoToAlbum(String albumName) {
        // if album name does not exist, display error message
        boolean validName = false;
        for (Album album : allAlbums) {
            if (album.name.equals(albumName)) {
                validName = true;
                break;
            }
        }
        if (!validName) {
            Context context = getApplicationContext();
            CharSequence text = "Error: Album Does Not Exist";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        // if you try to move to the album you are already in
        if (albumName.equals(allAlbums.get(albumPosition).name)) {
            Context context = getApplicationContext();
            CharSequence text = "Error: Current Album Name Entered";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        // if there is only 1 photo in the album, then we remove it and go back to selected album view
        if (allAlbums.get(albumPosition).photos.size() == 1) {
            for (Album album : allAlbums) {
                if (album.name.equals(albumName)) {
                    // add photo to new album
                    Photo photoToRemove = allAlbums.get(albumPosition).photos.get(0);
                    album.photos.add(photoToRemove);
                    // remove photo from current album
                    allAlbums.get(albumPosition).photos.remove(0);
                    saveData();
                    Intent resultIntent = new Intent();
                    resultIntent.putParcelableArrayListExtra("allAlbums", allAlbums);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                    return;
                }
            }
        }
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
            if (allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person") != null) {
                sbTags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person"));
            }
            personTags.setText(sbTags.toString());

            TextView locationTags = (TextView) findViewById(R.id.locationTags);
            StringBuilder sb1Tags = new StringBuilder();
            sb1Tags.append("location: ");
            if (allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location") != null) {
                sb1Tags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location"));
            }
            locationTags.setText(sb1Tags.toString());

            // now we move the photo and update photo position

            for (Album album : allAlbums) {
                if (album.name.equals(albumName)) {
                    // add photo to new album
                    Photo photoToRemove = allAlbums.get(albumPosition).photos.get(oldPosition);
                    album.photos.add(photoToRemove);
                    // remove photo from current album
                    allAlbums.get(albumPosition).photos.remove(oldPosition);
                    saveData();
                    Intent resultIntent = new Intent();
                    resultIntent.putParcelableArrayListExtra("allAlbums", allAlbums);
                    setResult(RESULT_OK, resultIntent);
                    return;
                }
            }

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
            if (allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person") != null) {
                sbTags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person"));
            }
            personTags.setText(sbTags.toString());

            TextView locationTags = (TextView) findViewById(R.id.locationTags);
            StringBuilder sb1Tags = new StringBuilder();
            sb1Tags.append("location: ");
            if (allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location") != null) {
                sb1Tags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location"));
            }
            locationTags.setText(sb1Tags.toString());

            // now we move the photo and update photo position

            for (Album album : allAlbums) {
                if (album.name.equals(albumName)) {
                    // add photo to new album
                    Photo photoToRemove = allAlbums.get(albumPosition).photos.get(oldPosition);
                    album.photos.add(photoToRemove);
                    // remove photo from current album
                    allAlbums.get(albumPosition).photos.remove(oldPosition);
                    saveData();
                    Intent resultIntent = new Intent();
                    resultIntent.putParcelableArrayListExtra("allAlbums", allAlbums);
                    setResult(RESULT_OK, resultIntent);
                    return;
                }
            }
        }
    }

    private void promptUserDeleteTag() { // have to add in error messages still
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);
        input.setHint("Enter tag here ");

        builder.setView(input);
        builder.setPositiveButton("Remove Person Tag", ((dialog, which) -> deletePersonTag(input.getText().toString())));
        builder.setNegativeButton("Remove Location Tag", ((dialog, which) -> deleteLocationTag(input.getText().toString())));
        builder.setNeutralButton("Cancel", (((dialog, which) -> dialog.cancel())));
        builder.show();
    }

    private void deleteLocationTag(String tag) {
        // if no tag was entered
        if (tag.equals("")) {
            Context context = getApplicationContext();
            CharSequence text = "Error: No Tag Was Entered";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        // if there are no tags at all
        if (allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location") == null) {
            Context context = getApplicationContext();
            CharSequence text = "Error: Tag You Are Trying To Delete Does Not Exist";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        String existingValue = allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location");
        String[] existingValueDelimited = existingValue.split(",");
        StringBuilder newValue = new StringBuilder();

        // if we are trying to delete a tag that does not exist
        boolean tagPresent = false;
        for (String s : existingValueDelimited) {
            if (s.equals(tag)) {
                tagPresent = true;
                break;
            }
        }
        if (!tagPresent) {
            Context context = getApplicationContext();
            CharSequence text = "Error: Tag You Are Trying To Delete Does Not Exist";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }

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
        if (allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location") != null) {
            sb1Tags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location"));
        }
        locationTags.setText(sb1Tags.toString());

        saveData();
    }

    private void deletePersonTag(String tag) {
        // if no tag was entered
        if (tag.equals("")) {
            Context context = getApplicationContext();
            CharSequence text = "Error: No Tag Was Entered";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        // if there are no tags at all
        if (allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person") == null) {
            Context context = getApplicationContext();
            CharSequence text = "Error: Tag You Are Trying To Delete Does Not Exist";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        String existingValue = allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person");
        String[] existingValueDelimited = existingValue.split(",");
        StringBuilder newValue = new StringBuilder();

        // if we are trying to delete a tag that does not exist
        boolean tagPresent = false;
        for (String s : existingValueDelimited) {
            if (s.equals(tag)) {
                tagPresent = true;
                break;
            }
        }
        if (!tagPresent) {
            Context context = getApplicationContext();
            CharSequence text = "Error: Tag You Are Trying To Delete Does Not Exist";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }

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
        StringBuilder sbTags = new StringBuilder();
        sbTags.append("person: ");
        if (allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person") != null) {
            sbTags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person"));
        }
        personTags.setText(sbTags.toString());

        saveData();
    }

    private void promptUserAddNewTag() { // have to add in error messages still
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);
        input.setHint("Enter tag here ");

        builder.setView(input);
        builder.setPositiveButton("Add Person Tag", ((dialog, which) -> addPersonTag(input.getText().toString())));
        builder.setNegativeButton("Add Location Tag", ((dialog, which) -> addLocationTag(input.getText().toString())));
        builder.setNeutralButton("Cancel", (((dialog, which) -> dialog.cancel())));
        builder.show();
    }

    private void addLocationTag(String tag) {
        // if no tag was entered
        if (tag.equals("")) {
            Context context = getApplicationContext();
            CharSequence text = "Error: No Tag Was Entered";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }

        // if tag already exists
        if (allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location") != null) {
            String existingValue = allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location");
            String[] existing_Value_Delimited = existingValue.split(",");

            boolean tagPresent = false;
            for (String s : existing_Value_Delimited) {
                if (s.equals(tag)) {
                    tagPresent = true;
                    break;
                }
            }
            if (tagPresent) {
                Context context = getApplicationContext();
                CharSequence text = "Error: Tag You Are Trying To Add Already Exists";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                return;
            }
        }

        if (!allAlbums.get(albumPosition).photos.get(photoPosition).tags.containsKey("location")) {
            allAlbums.get(albumPosition).photos.get(photoPosition).tags.put("location", tag);
        }
        else {
            //String[] existingValueDelimited =
                    //allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location").split(",");
            StringBuilder existingValueWithAddedValue =
                    new StringBuilder(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location"));
            existingValueWithAddedValue.append("," + tag);
            allAlbums.get(albumPosition).photos.get(photoPosition).tags.put("location", existingValueWithAddedValue.toString());
        }
        TextView locationTags = (TextView) findViewById(R.id.locationTags);
        StringBuilder sbTags = new StringBuilder();
        sbTags.append("location: ");
        sbTags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location"));
        locationTags.setText(sbTags.toString());
        //System.out.println(sbTags.toString());

        System.out.println(allAlbums.get(albumPosition).photos.get(photoPosition).tags);

        saveData();
    }

    private void addPersonTag(String tag) {
        // if no tag was entered
        if (tag.equals("")) {
            Context context = getApplicationContext();
            CharSequence text = "Error: No Tag Was Entered";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        // if tag already exists
        if (allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person") != null) {
            String existingValue = allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person");
            String[] existing_Value_Delimited = existingValue.split(",");

            boolean tagPresent = false;
            for (String s : existing_Value_Delimited) {
                if (s.equals(tag)) {
                    tagPresent = true;
                    break;
                }
            }
            if (tagPresent) {
                Context context = getApplicationContext();
                CharSequence text = "Error: Tag You Are Trying To Add Already Exists";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                return;
            }
        }

        if (!allAlbums.get(albumPosition).photos.get(photoPosition).tags.containsKey("person")) {
            allAlbums.get(albumPosition).photos.get(photoPosition).tags.put("person", tag);
        }
        else {
            // String[] existingValueDelimited =
                    //allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person").split(",");
            StringBuilder existingValueWithAddedValue =
                    new StringBuilder(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person"));
            existingValueWithAddedValue.append("," + tag);
            allAlbums.get(albumPosition).photos.get(photoPosition).tags.put("person", existingValueWithAddedValue.toString());
        }
        TextView personTags = (TextView) findViewById(R.id.personTags);
        StringBuilder sbTags = new StringBuilder();
        sbTags.append("person: ");
        sbTags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person"));
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
        // if there is only 1 photo in the album, then we delete it and go back to selected album view
        if (allAlbums.get(albumPosition).photos.size() == 1) {
            allAlbums.get(albumPosition).photos.remove(0);
            saveData();
            Intent resultIntent = new Intent();
            resultIntent.putParcelableArrayListExtra("allAlbums", allAlbums);
            setResult(RESULT_OK, resultIntent);
            finish();
            return;
        }
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
            if (allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person") != null) {
                sbTags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person"));
            }
            personTags.setText(sbTags.toString());

            TextView locationTags = (TextView) findViewById(R.id.locationTags);
            StringBuilder sb1Tags = new StringBuilder();
            sb1Tags.append("location: ");
            if (allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location") != null) {
                sb1Tags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location"));
            }
            locationTags.setText(sb1Tags.toString());

            // now we delete the photo and update photo position
            allAlbums.get(albumPosition).photos.remove(oldPosition);
            saveData();
            Intent resultIntent = new Intent();
            resultIntent.putParcelableArrayListExtra("allAlbums", allAlbums);
            setResult(RESULT_OK, resultIntent);
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
            if (allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person") != null) {
                sbTags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person"));
            }
            personTags.setText(sbTags.toString());

            TextView locationTags = (TextView) findViewById(R.id.locationTags);
            StringBuilder sb1Tags = new StringBuilder();
            sb1Tags.append("location: ");
            if (allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location") != null) {
                sb1Tags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location"));
            }
            locationTags.setText(sb1Tags.toString());

            // now we delete the photo and update photo position
            allAlbums.get(albumPosition).photos.remove(oldPosition);
            saveData();
            Intent resultIntent = new Intent();
            resultIntent.putParcelableArrayListExtra("allAlbums", allAlbums);
            setResult(RESULT_OK, resultIntent);
        }

    }



    public void nextPhoto(View view) {
        if (photoPosition < allAlbums.get(albumPosition).photos.size() - 1) {
            photoPosition++;
            //currentPhoto = allAlbums.get(albumPosition).photos.get(photoPosition);

            ImageView image = (ImageView) findViewById(R.id.selectedPhoto);
            image.setImageURI(Uri.parse(allAlbums.get(albumPosition).photos.get(photoPosition).photoPath));

            TextView personTags = (TextView) findViewById(R.id.personTags);
            StringBuilder sbTags = new StringBuilder();
            sbTags.append("person: ");
            if (allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person") != null) {
                sbTags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person"));
            }
            personTags.setText(sbTags.toString());

            TextView locationTags = (TextView) findViewById(R.id.locationTags);
            StringBuilder sb1Tags = new StringBuilder();
            sb1Tags.append("location: ");
            if (allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location") != null) {
                sb1Tags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location"));
            }
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

            TextView personTags = (TextView) findViewById(R.id.personTags);
            StringBuilder sbTags = new StringBuilder();
            sbTags.append("person: ");
            if (allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person") != null) {
                sbTags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("person"));
            }
            personTags.setText(sbTags.toString());

            TextView locationTags = (TextView) findViewById(R.id.locationTags);
            StringBuilder sb1Tags = new StringBuilder();
            sb1Tags.append("location: ");
            if (allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location") != null) {
                sb1Tags.append(allAlbums.get(albumPosition).photos.get(photoPosition).tags.get("location"));
            }
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