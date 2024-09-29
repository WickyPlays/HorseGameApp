package me.thienbao860.android.horsegameapp;

import android.os.Handler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
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

        List<Horse> betHorses = getBetHorses();
        Horse yourHorse = betHorses.get(0);
        yourHorse.setName(currentUser.getHorseName());

        final Handler handler = new Handler();
        timer = new Timer();
        AtomicInteger currentRank = new AtomicInteger(1);
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                handler.post(() -> {
                    for (Horse horse : horseList) {
                        horse.setSpeed((float) (Math.random() * Utils.generateRandom(0.01, 0.05)));
                        horse.setProgress(horse.getProgress() + horse.getSpeed());

                        if (horse.getProgress() >= 1.0 && horse.getRank() == 0) {
                            horse.setRank(currentRank.getAndIncrement());
                        }
                    }

                    getGameplayUI().refreshTrack();

                    wonHorses = getWonHorses();
                    if (wonHorses.isEmpty()) return;

                    long finishedCount = horseList.stream().filter(h -> h.getProgress() >= 1.0).count();
                    if (finishedCount >= horseList.size()) {
                        timer.cancel();
                        status = GameplayStatus.FINISHED;

                        Horse yourHorse = getBetHorses().get(0);
                        if (yourHorse != null && yourHorse.getRank() == 1) {
                            callWin();
                        } else {
                            callLose();
                        }
                    }

                    if (!wonHorses.isEmpty()) {
                        String winners = wonHorses.stream()
                                .sorted(Comparator.comparingInt(Horse::getRank))
                                .map((p) -> p.getName() + " (#" + p.getRank() + ")")
                                .collect(Collectors.joining(", "));
                        getGameplayUI().callCommentary("Winner" + (wonHorses.size() > 1 ? "s" : "") + ": " + winners);
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

        getGameplayUI().callStatus("Your selected horse will be named " + currentUser.getHorseName());
        getGameplayUI().callCommentary("Please select a horse to bet on");

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

        getGameplayUI().enableStartButton(true);
        getGameplayUI().enableResetButton(true);
    }

    public void stopGameplay() {
        getGameplayUI().enableResetButton(true);
        getGameplayUI().enableStartButton(false);
    }

    public boolean checkStartingCondition() {
        if (currentUser.getBalance() < 0) {
            getGameplayUI().callCommentary("Negative balance, cannot bet");
            return false;
        }

        if (currentUser.getBalance() < currentUser.getBetAmount()) {
            getGameplayUI().callCommentary("Not enough money to bet");
            return false;
        }

        List<Horse> betHorses = getBetHorses();
        if (betHorses.isEmpty()) {
            getGameplayUI().callCommentary("You must bet on a horse before the game begins");
            return false;
        }

        if (betHorses.size() > 1) {
            getGameplayUI().callCommentary(betHorses.size() + " horse(s) are selected. Only 1 is allowed");
            return false;
        }

        if (currentUser.getBetAmount() == 0) {
            getGameplayUI().callStatus("Entering free play as you don't bet any money.");
        } else {
            getGameplayUI().callStatus("You have bet " + currentUser.getBetAmount() + " USD to " + betHorses.get(0).getName());
        }

        getGameplayUI().callCommentary("Your horse: " + currentUser.getHorseName());

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

    public GameplayStatus getStatus() {
        return status;
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
