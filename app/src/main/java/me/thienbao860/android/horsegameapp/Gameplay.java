package me.thienbao860.android.horsegameapp;

import android.os.Handler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import me.thienbao860.android.horsegameapp.obj.Horse;
import me.thienbao860.android.horsegameapp.obj.User;

public class Gameplay {

    private static final int FIXED_PLAYERS = 7;
    private static volatile Gameplay instance = null;
    private final List<Horse> horseList = new ArrayList<>();
    private GameplayStatus status = GameplayStatus.NOT_STARTED;
    private List<Horse> wonHorses = new ArrayList<>();
    private User currentUser = null;
    private GameplayUI gameplayUI = new GameplayUI();

    private Handler handler;
    private Runnable gameLoop;

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
            gameplayUI.callToast("Game is already in progress");
            return;
        }

        if (!checkStartingCondition()) {
            return;
        }

        status = GameplayStatus.PLAYING;

        gameplayUI.enableBackButton(false);
        gameplayUI.enableEditBetAmount(false);
        gameplayUI.enableResetButton(false);
        gameplayUI.enableStartButton(false);

        List<Horse> betHorses = getBetHorses();
        Horse yourHorse = betHorses.get(0);
        yourHorse.setName(currentUser.getHorseName());

        handler = new Handler();
        AtomicInteger currentRank = new AtomicInteger(1);

        gameLoop = new Runnable() {
            @Override
            public void run() {
                boolean allHorsesFinished = true;

                for (Horse horse : horseList) {
                    horse.setSpeed((float) (Math.random() * Utils.generateRandom(0.01, 0.05)));
                    horse.setProgress(horse.getProgress() + horse.getSpeed());

                    if (horse.getProgress() >= 1.0 && horse.getRank() == 0) {
                        horse.setRank(currentRank.getAndIncrement());
                    }


                    if (horse.getProgress() < 1.0) {
                        allHorsesFinished = false;
                    }
                }

                gameplayUI.refreshTrack();

                wonHorses = getWonHorses();
                if (!wonHorses.isEmpty()) {
                    String winners = wonHorses.stream()
                            .sorted(Comparator.comparingInt(Horse::getRank))
                            .map((p) -> p.getName() + " (#" + p.getRank() + ")")
                            .collect(Collectors.joining(", "));
                    gameplayUI.callCommentary("Winner" + (wonHorses.size() > 1 ? "s" : "") + ": " + winners);
                }

                if (allHorsesFinished) {
                    status = GameplayStatus.FINISHED;
                    Horse yourHorse = getBetHorses().get(0);
                    if (yourHorse != null && yourHorse.getRank() == 1) {
                        callWin();
                    } else {
                        callLose();
                    }
                } else {
                    handler.postDelayed(this, 50);
                }
            }
        };

        handler.post(gameLoop);
    }

    public void stopGameplay() {
        if (handler != null && gameLoop != null) {
            handler.removeCallbacks(gameLoop);
        }

        gameplayUI.enableBackButton(true);
        gameplayUI.enableEditBetAmount(true);
        gameplayUI.enableResetButton(true);
        gameplayUI.enableStartButton(false);
    }

    public void setupGameplay() {
        stopGameplay();

        status = GameplayStatus.NOT_STARTED;

        gameplayUI.callStatus("Your selected horse will be named " + currentUser.getHorseName());
        gameplayUI.callCommentary("Please select a horse to bet on");

        horseList.clear();
        for (int i = 0; i < FIXED_PLAYERS; i++) {
            Horse horse = new Horse("Horse " + (i + 1));
            horse.setProgress(0F);
            horse.setColor(Utils.randomHexColor());
            horseList.add(horse);
        }

        gameplayUI.setTrack(horseList);
        gameplayUI.refreshTrack();

        if (currentUser != null) {
            gameplayUI.setBankAmount(currentUser.getBalance());
            gameplayUI.setBetAmount(0);
        }

        gameplayUI.enableStartButton(true);
        gameplayUI.enableResetButton(true);
    }

    public boolean checkStartingCondition() {
        if (currentUser.getBalance() < 0) {
            gameplayUI.callCommentary("Negative balance, cannot bet");
            return false;
        }

        if (currentUser.getBalance() < currentUser.getBetAmount()) {
            gameplayUI.callCommentary("Not enough money to bet");
            return false;
        }

        List<Horse> betHorses = getBetHorses();
        if (betHorses.isEmpty()) {
            gameplayUI.callCommentary("You must bet on a horse before the game begins");
            return false;
        }

        if (betHorses.size() > 1) {
            gameplayUI.callCommentary(betHorses.size() + " horse(s) are selected. Only 1 is allowed");
            return false;
        }

        if (currentUser.getBetAmount() == 0) {
            gameplayUI.callStatus("Entering free play as you don't bet any money.");
        } else {
            gameplayUI.callStatus("You have bet " + currentUser.getBetAmount() + " USD to " + betHorses.get(0).getName());
        }

        gameplayUI.callCommentary("Your horse: " + currentUser.getHorseName());

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
        Horse betHorse = getBetHorses().get(0);
        gameplayUI.callWin(currentUser.getHorseName() + " has won the race (Rank: #" + betHorse.getRank() + ")");
        currentUser.setBalance(currentUser.getBalance() + currentUser.getBetAmount());
        gameplayUI.setBankAmount(currentUser.getBalance());
        stopGameplay();
    }

    public void callLose() {
        Horse betHorse = getBetHorses().get(0);
        gameplayUI.callLose(currentUser.getHorseName() + " has lost the race (Rank: #" + betHorse.getRank() + ")");
        currentUser.setBalance(currentUser.getBalance() - currentUser.getBetAmount());
        gameplayUI.setBankAmount(currentUser.getBalance());
        stopGameplay();
    }

    public GameplayUI getGameplayUI() {
        return gameplayUI; }

}
