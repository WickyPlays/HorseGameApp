package me.thienbao860.android.horsegameapp;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import me.thienbao860.android.horsegameapp.obj.Horse;
import me.thienbao860.android.horsegameapp.obj.User;

public class Gameplay {

    private static final int FIXED_PLAYERS = 8;
    private static volatile Gameplay instance = null;
    private final List<Horse> horseList = new ArrayList<>();
    private GameplayStatus status = GameplayStatus.NOT_STARTED;
    private List<Horse> wonHorses = new ArrayList<>();

    private User currentUser = null;
    private Timer timer;

    public static Gameplay getInstance() {
        if (instance == null) {
            instance = new Gameplay();
        }
        return instance;
    }

    // Method to set the current user
    public void setUser(User user) {
        this.currentUser = user;
    }

    public User getUser() {
        return currentUser;
    }

    public void startGameplay() {
        if (status == GameplayStatus.PLAYING) {
            getGameplayUI().callToast("Game is already in progress");
            return;
        }

        if (!checkStartingCondition()) {
            return;
        }

        status = GameplayStatus.PLAYING;

        getGameplayUI().enableResetButton(false);
        getGameplayUI().enableStartButton(false);

        //Assign your bet horse's name as your horse name
        List<Horse> betHorses = getBetHorses();
        Horse yourHorse = betHorses.get(0);
        yourHorse.setName(currentUser.getHorseName());

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
                    getGameplayUI().refreshTrack();

                    wonHorses = getWonHorses();
                    if (wonHorses.isEmpty()) return;

                    timer.cancel();

                    if (wonHorses.size() == 1) {
                        getGameplayUI().callCommentary(wonHorses.get(0).getName() + " has won the race");
                    } else {
                        String winners = wonHorses.stream()
                                .map(Horse::getName)
                                .collect(Collectors.joining(", "));
                        getGameplayUI().callCommentary(winners + " won the race");
                    }

                    if (wonHorses.stream().anyMatch(Horse::isBet)) {
                        callWin();
                    } else {
                        callLose();
                    }
                });
            }
        };

        timer.schedule(timerTask, 0, 100);
    }

    public void setupGameplay() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        status = GameplayStatus.NOT_STARTED;

        getGameplayUI().callStatus("");

        horseList.clear();
        for (int i = 0; i < FIXED_PLAYERS; i++) {
            Horse horse = new Horse("Horse " + (i + 1));
            horse.setProgress(0F);
            horse.setColor(Utils.randomHexColor());
            horseList.add(horse);
        }

        getGameplayUI().setTrack(horseList);
        getGameplayUI().refreshTrack();

        if (currentUser != null) {
            getGameplayUI().setBankAmount(currentUser.getBalance());
            getGameplayUI().setBetAmount(0);
        }
    }

    public void stopGameplay() {
        getGameplayUI().enableResetButton(true);
        getGameplayUI().enableStartButton(true);
    }

    public boolean checkStartingCondition() {
        if (currentUser.getBalance() < 0) {
            getGameplayUI().callCommentary("Negative balance, cannot bet");
            getGameplayUI().callToast("Negative balance, cannot bet.");
            return false;
        }

        if (currentUser.getBalance() < currentUser.getBetAmount()) {
            getGameplayUI().callCommentary("Not enough money to bet");
            getGameplayUI().callToast("Not enough money to bet");
            return false;
        }

        List<Horse> betHorses = getBetHorses();
        if (betHorses.isEmpty()) {
            getGameplayUI().callCommentary("You must bet on a horse before the game begins");
            getGameplayUI().callToast("You must bet on a horse before the game begins");
            return false;
        }

        if (betHorses.size() > 1) {
            getGameplayUI().callCommentary(betHorses.size() + " horse(s) are selected. Only 1 is allowed");
            getGameplayUI().callToast(betHorses.size() + " horse(s) are selected. Only 1 is allowed");
            return false;
        }

        if (currentUser.getBetAmount() == 0) {
            getGameplayUI().callCommentary("Entering free play as you don't bet any money");
        }

        getGameplayUI().callToast("Let the game begin!");
        getGameplayUI().callCommentary("");
        status = GameplayStatus.WAITING;
        return true;
    }

    public List<Horse> getHorseList() {
        return horseList;
    }

    public List<Horse> getBetHorses() {
        return horseList.stream().filter(Horse::isBet).collect(Collectors.toList());
    }

    public List<Horse> getWonHorses() {
        return horseList.stream().filter(Horse::isWon).collect(Collectors.toList());
    }

    public void callWin() {
        getGameplayUI().callWin("Your horse (" + currentUser.getHorseName() + ") has won the race");
        currentUser.setBalance(currentUser.getBalance() + currentUser.getBetAmount());
        getGameplayUI().setBankAmount(currentUser.getBalance());
        stopGameplay();
    }

    public void callLose() {
        getGameplayUI().callLose("Your horse (" + currentUser.getHorseName() + ") has lost the race");
        currentUser.setBalance(currentUser.getBalance() - currentUser.getBetAmount());
        getGameplayUI().setBankAmount(currentUser.getBalance());
        stopGameplay();
    }

    public GameplayUI getGameplayUI() {
        return new GameplayUI();
    }

}
