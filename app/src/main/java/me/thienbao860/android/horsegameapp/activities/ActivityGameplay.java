package me.thienbao860.android.horsegameapp.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import me.thienbao860.android.horsegameapp.Gameplay;
import me.thienbao860.android.horsegameapp.GameplayUIManager;
import me.thienbao860.android.horsegameapp.R;

public class ActivityGameplay extends AppCompatActivity {

    private static ActivityGameplay instance;
    private static GameplayUIManager uiManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        uiManager = new GameplayUIManager();

        setContentView(R.layout.layout_ex_racing_gameplay);
        Gameplay.getInstance().setupTracks();

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
                try {
                    int amountBet = Integer.parseInt(charSequence.toString());
                    Gameplay.getInstance().setBetAmount(amountBet);
                } catch (Exception ignored) {}
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etBetAmount.setOnFocusChangeListener((view, isFocused) -> {
            if (!isFocused) {
                try {
                    Gameplay.getInstance().setBetAmount(Integer.parseInt(etBetAmount.getText().toString()));
                } catch (Exception ignored) {}
            }
        });

        Button btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(v -> {
            Gameplay.getInstance().reset();
        });
    }

    public static ActivityGameplay getContext() {
        return instance;
    }

    public static GameplayUIManager getUIManager() {
        return uiManager;
    }
}
