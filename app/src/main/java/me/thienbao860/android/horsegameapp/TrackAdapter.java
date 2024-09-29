package me.thienbao860.android.horsegameapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.SeekBar;

import java.util.List;

import me.thienbao860.android.horsegameapp.obj.Horse;

public class TrackAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Horse> horseList;

    public TrackAdapter(List<Horse> list, Context context, int layout) {
        this.context = context;
        this.layout = layout;
        this.horseList = list;
    }

    @Override
    public int getCount() {
        return horseList.size();
    }

    @Override
    public Horse getItem(int i) {
        return horseList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null);

        SeekBar sb = view.findViewById(R.id.sbTrack);

        Horse horse = getItem(i);
        if (horse != null) {
            ColorStateList csl = ColorStateList.valueOf(Color.parseColor(horse.getColor()));

            sb.setThumbTintList(csl);
            sb.setProgressTintList(csl);

            CheckBox cb = view.findViewById(R.id.cbBet);
            cb.setChecked(horse.isBet());
            cb.setButtonTintList(csl);

            cb.setOnCheckedChangeListener((cbView, checked) -> {
                horse.setBet(checked);
            });

            sb.setProgress((int) (horse.getProgress() * 100));
        }

        return view;
    }
}
