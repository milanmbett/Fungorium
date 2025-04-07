package com.coderunnerlovagjai.app;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Player //TODO: Megírás
{
    private static final Logger PLAYER_LOGGER = LogManager.getLogger(Player.class);

    private Role role;
    private int income;
    private int score;  //mintha income lenne, viszont soha nem csökkenhet, csak nőhet

    public Player() {
        this.role = null;
        this.income = 0;
        this.score = 0;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    public Role getRole() {
        return role;
    }

    public int getIncome() {
        return income;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public void increaseIncome(int amount) {
        this.income += amount;
        this.score += amount;
    }

    public void decreaseIncome(int amount) {
        this.income -= amount;
    }

    public Player(Role role) {
        this.role = role;
        this.income = 0;
        this.score = 0;
    }
}
