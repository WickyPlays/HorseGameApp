package me.thienbao860.android.horsegameapp;

import android.os.Handler;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import me.thienbao860.android.horsegameapp.activities.ActivityGameplay;
import me.thienbao860.android.horsegameapp.obj.Horse;

public class Gameplay {

    private final int FIXED_PLAYERS = 5;
    private static Gameplay instance = null;
    private final List<Horse> horseList = new ArrayList<>();
    private int bankAmount = 100;
    private int betAmount = 50;
    private final GameplayUIManager uiManager;
    private GameplayStatus status = GameplayStatus.NOT_STARTED;
    private Timer timer;

    public Gameplay() {

        uiManager = ActivityGameplay.getUIManager();

        setBankAmount(bankAmount);
        setBetAmount(betAmount, true);

        uiManager.callStatus("");
    }

    public void startGameplay() {
        if (status == GameplayStatus.PLAYING) {
            uiManager.callToast("The game has already begun");
            return;
        }

        status = GameplayStatus.WAITING;

        boolean isStart = checkStartingCondition();
        if (!isStart) {
            status = GameplayStatus.NOT_STARTED;
            return;
        }

        status = GameplayStatus.PLAYING;

        final Handler handler = new Handler();
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                handler.post(() -> {
                    for (Horse horse : horseList) {
                        horse.setSpeed((float) (Math.random() * Utils.generateRandom(0.01, 0.05)));
                        horse.setProgress(horse.getProgress() + horse.getSpeed());
                    }
                    uiManager.refreshTrack();

                    Horse wonHorse = getWonHorse();
                    if (wonHorse == null) return;

                    timer.cancel();
                    uiManager.callToast(wonHorse.getName() + " has won the race");

                    if (wonHorse.isBet()) {
                        callWin();
                    } else {
                        callLose();
                    }
                });
            }
        };

        timer.schedule(timerTask, 0, 100);
    }

    public void setupTracks() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        horseList.clear();
        for (int i = 0; i < FIXED_PLAYERS; i++) {
            Horse horse = new Horse("Horse " + (i + 1));
            horse.setProgress(0F);
            horse.setColor(Utils.randomHexColor());
            horseList.add(horse);
        }

        uiManager.setTrack(horseList);
        uiManager.refreshTrack();
    }

    public boolean checkStartingCondition() {

        uiManager.callStatus("");

        if (getBankAmount() < 0) {
            uiManager.callToast("Negative balance, cannot bet.");
            return false;
        }

        if (getBankAmount() < getBetAmount()) {
            uiManager.callToast("Not enough money to bet");
            return false;
        }

        List<Horse> betHorses = getBetHorses();
        if (betHorses.isEmpty()) {
            uiManager.callToast("You must bet on a horse before the game begin");
            return false;
        }

        if (betHorses.size() > 1) {
            uiManager.callToast(betHorses.size() + " horse(s) are selected. Only 1 is allowed");
            return false;
        }

        uiManager.callToast("Let the game begin!");

        return true;
    }

    public List<Horse> getHorseList() {
        return horseList;
    }

    public int getBankAmount() {
        return bankAmount;
    }

    public void setBankAmount(int bankAmount) {
        this.bankAmount = bankAmount;
        uiManager.setBankAmount(bankAmount);
    }

    public int getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(int amount) {
        setBetAmount(betAmount, false);
    }

    public void setBetAmount(int amount, boolean uiChange) {
        this.betAmount = amount;
        if (uiChange) {
            uiManager.setBetAmount(amount);
        }
    }

    public List<Horse> getBetHorses() {
        return horseList.stream().filter(Horse::isBet).collect(Collectors.toList());
    }

    public Horse getWonHorse() {
        return horseList.stream().filter(Horse::isWon).findFirst().orElse(null);
    }

    public void callWin() {
        uiManager.callWin("Your horse has won the race");
        setBankAmount(getBankAmount() + getBetAmount());
    }

    public void callLose() {
        uiManager.callLose("Your horse has lost the race");
        setBankAmount(getBankAmount() - getBetAmount());
    }

    public void reset() {
        status = GameplayStatus.NOT_STARTED;
        timer = null;
        uiManager.callStatus("");
        setupTracks();
    }

    public static Gameplay getInstance() {
        if (instance == null) {
            instance = new Gameplay();
        }

        return instance;
    }
}
