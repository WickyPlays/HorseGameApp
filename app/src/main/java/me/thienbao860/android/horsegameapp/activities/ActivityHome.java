package me.thienbao860.android.horsegameapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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

    }
}
