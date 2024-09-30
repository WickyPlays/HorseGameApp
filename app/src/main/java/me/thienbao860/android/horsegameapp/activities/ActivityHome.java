package me.thienbao860.android.horsegameapp.activities;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import me.thienbao860.android.horsegameapp.R;

public class ActivityHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ex_racing_home);

        Button btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener((e) -> {
            Intent intent = new Intent(this, ActivityLogin.class);
            startActivity(intent);
            finish();
        });

        ImageButton btnIntro = findViewById(R.id.btnIntro);
        btnIntro.setOnClickListener((e) -> {
            Intent intent = new Intent(this, ActivityIntroduction.class);
            startActivity(intent);
        });
        String url = "https://download.samplelib.com/mp3/sample-15s.mp3";
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setLooping(true);
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
