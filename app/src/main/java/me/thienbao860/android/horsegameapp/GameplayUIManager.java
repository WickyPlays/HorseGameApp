package me.thienbao860.android.horsegameapp;

import android.graphics.Color;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import me.thienbao860.android.horsegameapp.activities.ActivityGameplay;
import me.thienbao860.android.horsegameapp.obj.Horse;

public class GameplayUIManager {

    public GameplayUIManager() {

    }

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
}
