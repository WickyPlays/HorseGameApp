package me.thienbao860.android.horsegameapp.obj;

public class User {

    private final String username;
    private final String password;
    private int balance;
    private int betAmount;
    private String horseName;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.horseName = "";
        this.balance = 100;
        this.betAmount = 50;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHorseName() {
        return horseName;
    }

    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(int betAmount) {
        this.betAmount = betAmount;
    }
}
