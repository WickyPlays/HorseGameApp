package me.thienbao860.android.horsegameapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import me.thienbao860.android.horsegameapp.R;

public class ActivityLogin extends AppCompatActivity {

    private EditText txtUserName;
    private EditText txtPassword;

    private final String FIXED_ACCOUNT_NAME = "admin";
    private final String FIXED_ACCOUNT_PASSWORD = "1234";

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

        // Clear hints on focus
        txtUserName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                txtUserName.setHint("");
            }
        });
        txtPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                txtPassword.setHint("");
            }
        });

        btnLogin.setOnClickListener((e) -> {
            String enteredUsername = txtUserName.getText().toString();
            String enteredPassword = txtPassword.getText().toString();

            if (enteredUsername.equals(FIXED_ACCOUNT_NAME) && enteredPassword.equals(FIXED_ACCOUNT_PASSWORD)) {
                Intent intent = new Intent(this, ActivityGameplay.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(ActivityLogin.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
