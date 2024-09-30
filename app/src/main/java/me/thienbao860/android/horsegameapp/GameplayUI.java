package me.thienbao860.android.horsegameapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import me.thienbao860.android.horsegameapp.activities.ActivityGameplay;
import me.thienbao860.android.horsegameapp.obj.Horse;

public class GameplayUI {

    public void setBankAmount(int amount) {
        TextView bankAmountTextView = ActivityGameplay.getContext().findViewById(R.id.tvBankAmount);
        bankAmountTextView.setText("Bank: " + amount + " USD");
    }

    public void setBetAmount(int amount) {
        TextView bankAmountTextView = ActivityGameplay.getContext().findViewById(R.id.etBetAmount);
        bankAmountTextView.setText(Integer.toString(amount));
    }

    public void setTrack(List<Horse> horseList) {
        ListView view = ActivityGameplay.getContext().findViewById(R.id.lvTrack);
        TrackAdapter adapter = new TrackAdapter(horseList, ActivityGameplay.getContext(), R.layout.layout_ex_racing_track);
        view.setAdapter(adapter);
    }

    public void refreshTrack() {
        ListView view = ActivityGameplay.getContext().findViewById(R.id.lvTrack);
        TrackAdapter adapter = (TrackAdapter) view.getAdapter();
        adapter.notifyDataSetChanged();
    }

    public void callCommentary(String msg) {
        TextView view = ActivityGameplay.getContext().findViewById(R.id.tvCommentary);
        view.setText(msg);
    }

    public void callStatus(String msg) {
        TextView view = ActivityGameplay.getContext().findViewById(R.id.tvStatus);
        view.setTextColor(Color.parseColor("#000000"));
        view.setText(msg);
    }

    public void callWin(String msg) {
        TextView view = ActivityGameplay.getContext().findViewById(R.id.tvStatus);
        view.setTextColor(Color.parseColor("#007219"));
        view.setText(msg);
    }

    public void callLose(String msg) {
        TextView view = ActivityGameplay.getContext().findViewById(R.id.tvStatus);
        view.setTextColor(Color.parseColor("#FF0000"));
        view.setText(msg);
    }

    public void callToast(String msg) {
        Toast.makeText(ActivityGameplay.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void enableEditBetAmount(boolean b) {
        EditText etBetAmount = ActivityGameplay.getContext().findViewById(R.id.etBetAmount);
        etBetAmount.setEnabled(b);
    }

    public void enableStartButton(boolean enable) {
        Button view = ActivityGameplay.getContext().findViewById(R.id.btnStart);
        view.setEnabled(enable);
        int originalColor = Color.parseColor("#0D8700");
        int disabledColor = Color.GRAY;

        if (enable) {
            view.setBackgroundTintList(ColorStateList.valueOf(originalColor));
        } else {
            view.setBackgroundTintList(ColorStateList.valueOf(disabledColor));
        }
    }

    public void enableResetButton(boolean enable) {
        Button view = ActivityGameplay.getContext().findViewById(R.id.btnReset);
        view.setEnabled(enable);
    }
}
