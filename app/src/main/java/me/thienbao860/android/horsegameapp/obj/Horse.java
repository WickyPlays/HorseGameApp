package me.thienbao860.android.horsegameapp.obj;

import me.thienbao860.android.horsegameapp.Utils;

public class Horse {

    private String color;
    private String name;
    private float progress;
    private float speed;
    private boolean isBet;

    public Horse(String name) {
        this.name = name;
        this.progress = 0;
        this.speed = 0;
        this.isBet = false;
        this.color = Utils.randomHexColor();
    }
    
    public void generateRandomSpeed() {
        setSpeed((float) Math.random());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public boolean isBet() {
        return isBet;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void move() {
        setProgress(getProgress() + getSpeed());
    }

    public void reset() {
        setProgress(0);
    }

    public void setBet(boolean b) {
        isBet = b;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isWon() {
        return getProgress() >= 1;
    }
}
