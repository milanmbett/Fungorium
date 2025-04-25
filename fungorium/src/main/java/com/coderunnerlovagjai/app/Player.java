package com.coderunnerlovagjai.app;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Player //TODO: Megírás: Pontok tárolása, Role választás
{
    private static final Logger PLAYER_LOGGER = LogManager.getLogger(Player.class);

    private Role role;
    private int income;
    private int score;  //mintha income lenne, viszont soha nem csökkenhet, csak nőhet
    private final int id;

    public Player() {
        this.id = 0;
        this.role = null; 
        this.income = 200;
        this.score = 0;
        PLAYER_LOGGER.log(Level.forName("INIT", 402), "Player created with default values. Income: " + income + ", Score: " + score);
    }

    public Player(int id) {
        this.id = id;
        this.role = null; 
        this.income = 200;
        this.score = 0;
        PLAYER_LOGGER.log(Level.forName("INIT", 402), "Player created with default values. Income: " + income + ", Score: " + score);
    }


    public int getId() {
        return id;
    }

    public void setRoleInsect() {
        this.role = new Role_Insect(this);
        PLAYER_LOGGER.log(Level.forName("ROLE", 401), "Player {} role set to: {}", id, role.getRoleName());
    }
    public void setRoleMushroom() {
        this.role = new Role_Mushroom(this);
        PLAYER_LOGGER.log(Level.forName("ROLE", 401), "Player {} role set to: {}", id, role.getRoleName());
    }

    public void setRoleNull(){
        this.role = null;
        PLAYER_LOGGER.log(Level.forName("ROLE", 401), "Player {} role set to: {}", id, "null");
    }

    public Role getRole() {
        return role;
    }

    public boolean moveInsect(Insect_Class insect, Tecton_Class target) {
        if (role instanceof Role_Insect role_insect) {
            return role_insect.validateAndMoveInsect(insect, target);
        } else {
            PLAYER_LOGGER.log(Level.forName("ERROR", 404), 
                "Player {} does not have Insect role!", id);
            return false;
        }
    }

    public void newTurn() {
        if (role != null) {
            role.on_turn();
            role = null; // Reset the role after the turn
        }
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
        this.score += amount; // SCORE is növekszik az INCOME növekedésével
        PLAYER_LOGGER.log(Level.forName("SCORE", 401), "Score increased by " + amount + ". New score: " + this.score);

    }


    public void decreaseIncome(int amount) {
        this.income -= amount;
        PLAYER_LOGGER.log(Level.forName("INCOME", 401), "Income decreased by " + amount + ". New income: " + this.income);
    }
    
    
}
