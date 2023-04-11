package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    TextView startTime, endTime, songTitle, songArtist;
    SeekBar volumeBar, timeBar;
    Button playButton;
    MediaPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        getSupportActionBar().hide();


        Song song = (Song)getIntent().getSerializableExtra("song");


        //tvTime
        startTime = findViewById(R.id.startTime);
        //tvDuration
        endTime = findViewById(R.id.endTime);
        //seekBarVolume
        volumeBar = findViewById(R.id.volumeBar);
        //seekBarTime
        timeBar = findViewById(R.id.timeBar);
        //btnPlay
        playButton = findViewById(R.id.playButton);
        //tvArtist
        songArtist = findViewById(R.id.songArtist);
        //tvTitle
        songTitle = findViewById(R.id.songTitle);

        songTitle.setText(song.getTitle());
        songArtist.setText(song.getArtist());
//        musicPlayer = MediaPlayer.create(this, R.raw.music_track1);
        musicPlayer = new MediaPlayer();
        try {
            musicPlayer.setDataSource(song.getPath());
            musicPlayer.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        musicPlayer.setLooping(true);
        musicPlayer.seekTo(0);
        musicPlayer.setVolume(0.5f,0.5f);

        String duration = millisecondsToString(musicPlayer.getDuration());
        endTime.setText(duration);





        volumeBar.setProgress(50);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                float volume = progress /100f;
                musicPlayer.setVolume(volume,volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        timeBar.setMax(musicPlayer.getDuration());
        timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
                if(isFromUser){
                    musicPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.playButton){
                    if(musicPlayer.isPlaying()){
                        musicPlayer.pause();
                        playButton.setBackgroundResource(R.drawable.play_btn);
                    }
                    else{
                        musicPlayer.start();
                        playButton.setBackgroundResource(R.drawable.pause_btn);
                    }
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (musicPlayer != null){
                    if(musicPlayer.isPlaying()){
                        try{
                            final double current = musicPlayer.getCurrentPosition();
                            double duration = musicPlayer.getDuration();
                            final double position = (100.0/duration)* current;
                            final String elapsedTime = millisecondsToString((int)current);


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startTime.setText(elapsedTime);
                                    timeBar.setProgress((int)current);
                                }
                            });

                            Thread.sleep(1000);

                        }catch(InterruptedException e){

                        }
                    }
                }
            }
        }).start();




    }

    public String millisecondsToString(int time) {
        String elapsedTime = "";
        int minutes = time/1000 /60;
        int seconds = time/1000 % 60;
        elapsedTime = minutes+":";
        if(seconds < 10){
            elapsedTime += "0";
        }
        elapsedTime += seconds;

        return elapsedTime;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            if(musicPlayer.isPlaying()){
                musicPlayer.pause();
                playButton.setBackgroundResource(R.drawable.play_btn);

            }
        }
        return super.onOptionsItemSelected(item);
    }
}
