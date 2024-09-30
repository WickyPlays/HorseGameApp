package me.thienbao860.android.horsegameapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import me.thienbao860.android.horsegameapp.Gameplay;
import me.thienbao860.android.horsegameapp.GameplayUI;
import me.thienbao860.android.horsegameapp.R;
import me.thienbao860.android.horsegameapp.obj.User;

public class ActivityGameplay extends AppCompatActivity {

    private static ActivityGameplay instance;
    private static GameplayUI gameplayUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        gameplayUI = new GameplayUI();

        setContentView(R.layout.layout_ex_racing_gameplay);

        Button btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(v -> {
            Gameplay.getInstance().startGameplay();
        });

        EditText etBetAmount = findViewById(R.id.etBetAmount);

        etBetAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                User user = Gameplay.getInstance().getUser();
                if (user == null) return;

                try {
                    int amountBet = Integer.parseInt(charSequence.toString());
                    user.setBetAmount(amountBet);
                } catch (Exception ignored) {}
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etBetAmount.setOnFocusChangeListener((view, isFocused) -> {
            if (!isFocused) {
                try {
                   User user = Gameplay.getInstance().getUser();
                   if (user == null) return;

                   int amountBet = Integer.parseInt(etBetAmount.getText().toString());
                   user.setBetAmount(amountBet);
                } catch (Exception ignored) {}
            }
        });

        Button btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(v -> {
            Gameplay.getInstance().setupGameplay();
        });

        Gameplay.getInstance().setupGameplay();

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener((e) -> {
            Intent intent = new Intent(this, ActivityLogin.class);
            startActivity(intent);
        });

    }

    public static ActivityGameplay getContext() {
        return instance;
    }

    public static GameplayUI getUIManager() {
        return gameplayUI;
    }
}
