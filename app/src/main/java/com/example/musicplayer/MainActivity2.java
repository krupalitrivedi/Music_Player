 package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

 public class MainActivity2 extends AppCompatActivity {

     private static final int REQUEST_PERMISSION = 99;

    ArrayList<Song> songArrayList;
    ListView songList;
    SongsAdapter songsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        songList = findViewById(R.id.listSong);
        songArrayList = new ArrayList<>();

        songsAdapter = new SongsAdapter(this, songArrayList);
        songList.setAdapter(songsAdapter);


//        for (int i = 0; i <=  10; i++)
//            songArrayList.add(new Song("Song "+i, "Artist "+i, "Path "+i));
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            return;

        }else{
            //permission is already granted
            getSongs();
        }
// lvSongs is songList

        songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Song song = songArrayList.get(position);
                Intent openMusicPlayer = new Intent(MainActivity2.this, MainActivity.class);
                openMusicPlayer.putExtra("song", song);
//                openMusicPlayer.putExtra("artist", song.getArtist());
//                openMusicPlayer.putExtra("path", song.getPath());
                startActivity(openMusicPlayer);
            }
        });


//        songsAdapter = new SongsAdapter(this, songArrayList);
//
//        songList.setAdapter(songsAdapter);
    }

     @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         if (requestCode == REQUEST_PERMISSION) {
             if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 getSongs();
             }

         }
     }

     private void getSongs(){
        //read songs from phone
         ContentResolver contentResolver = getContentResolver();
         Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

         Cursor songCursor = contentResolver.query(songUri, null,null, null,null);
         if(songCursor != null && songCursor.moveToFirst()){

             int indexTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
             int indexArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
             int indexPath = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

             do{
                 String title = songCursor.getString(indexTitle);
                 String artist = songCursor.getString(indexArtist);
                 String path = songCursor.getString(indexPath);
                 Song song = new Song(title, artist, path);
                 songArrayList.add(new Song(title,artist,path));

             } while(songCursor.moveToNext());

         }

         songsAdapter.notifyDataSetChanged();




    }







}