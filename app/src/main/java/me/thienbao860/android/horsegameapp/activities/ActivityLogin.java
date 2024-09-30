package me.thienbao860.android.horsegameapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import me.thienbao860.android.horsegameapp.Gameplay;
import me.thienbao860.android.horsegameapp.R;
import me.thienbao860.android.horsegameapp.UserManager;
import me.thienbao860.android.horsegameapp.obj.User;

public class ActivityLogin extends AppCompatActivity {

    private EditText txtUserName;
    private EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ex_racing_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvError = findViewById(R.id.tvError);
        EditText etHorseName = findViewById(R.id.etHorseName);

        btnLogin.setOnClickListener((e) -> {
            String enteredUsername = txtUserName.getText().toString();
            String enteredPassword = txtPassword.getText().toString();

            User user = UserManager.login(enteredUsername, enteredPassword);
            if (user == null) {
                Toast.makeText(ActivityLogin.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                tvError.setText("Invalid username or password");
                return;
            }

            if (etHorseName.getText().toString().isEmpty()) {
                Toast.makeText(ActivityLogin.this, "Please enter horse name", Toast.LENGTH_SHORT).show();
                tvError.setText("Please enter horse name");
                return;
            }

            user.setHorseName(etHorseName.getText().toString());
            Gameplay.getInstance().setUser(user);

            Intent intent = new Intent(this, ActivityGameplay.class);
            startActivity(intent);
        });

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener((e) -> {
            Intent intent = new Intent(this, ActivityHome.class);
            startActivity(intent);
        });
    }
}
