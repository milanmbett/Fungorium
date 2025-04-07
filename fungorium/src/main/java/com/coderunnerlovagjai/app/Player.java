package com.coderunnerlovagjai.app;
import org.apache.logging.log4j.Level;
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
        this.income = 200;  //TODO pontos érték megbeszélendő
        this.score = 0;
        PLAYER_LOGGER.log(Level.forName("INIT", 402), "Player created with default values. Income: " + income + ", Score: " + score);
    }

    /*public Player(Role role) {
        this.role = role;
        this.income = 0;
        this.score = 0;
    }*/

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
        PLAYER_LOGGER.log(Level.forName("SCORE", 401), "Score set to " + score);
    }

    public void setIncome(int income) {
        this.income = income;
        PLAYER_LOGGER.log(Level.forName("INCOME", 401), "Income set to " + income);
    }

    public void increaseIncome(int amount) {
        this.income += amount;
        PLAYER_LOGGER.log(Level.forName("INCOME", 401), "Income increased by " + amount + ". New income: " + this.income);
        this.score += amount;
        PLAYER_LOGGER.log(Level.forName("SCORE", 401), "Score increased by " + amount + ". New score: " + this.score);

    }

    public void decreaseIncome(int amount) {
        this.income -= amount;
        PLAYER_LOGGER.log(Level.forName("INCOME", 401), "Income decreased by " + amount + ". New income: " + this.income);
    }
    
    
}
