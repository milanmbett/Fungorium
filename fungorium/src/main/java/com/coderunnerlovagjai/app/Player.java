package com.coderunnerlovagjai.app;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.awt.Color; // Added import for Color

public class Player 
{
    private static final Logger PLAYER_LOGGER = LogManager.getLogger(Player.class);

    private RoleType role; 
    private int income;
    private int fungusCounter; 
    private int score;
    private int action;
    private final int id;
    private Game game;
    private String name;
    private Color color; // Added color field

    public Player() {
        this.id = 0;
        this.role = RoleType.NONE; 
        this.fungusCounter = 0;
        this.income = 200;
        this.score = 0;
        this.action = 3;
        this.color = Color.GRAY; // Default color
        PLAYER_LOGGER.log(Level.forName("INIT", 402), "Player created with default values. Income: " + income + ", Score: " + score);
    }

    public Player(int id, String name) {
        this.id = id;
        this.role = RoleType.NONE; 
        this.fungusCounter = 0;
        this.income = 200;
        this.score = 0;
        this.action = 3;
        this.name = name;
        // Assign unique colors based on ID or other logic
        if (id == 1) {
            this.color = Color.BLUE; 
        } else if (id == 2) {
            this.color = Color.RED;
        } else {
            this.color = Color.GRAY; // Default for other players
        }
        PLAYER_LOGGER.log(Level.forName("INIT", 402), "Player created with default values. Income: " + income + ", Score: " + score);
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public int getId() {
        return id;
    }

    // Módosított szerep beállító metódusok
    public void setRoleInsect() {
        this.role = RoleType.INSECT;
        this.setAction(3);
        PLAYER_LOGGER.log(Level.forName("ROLE", 401), "Player {} role set to: {}", id, role.getRoleName());
    }
    
    public void setRoleMushroom() {
        this.role = RoleType.MUSHROOM;
        this.setAction(2);
        PLAYER_LOGGER.log(Level.forName("ROLE", 401), "Player {} role set to: {}", id, role.getRoleName());
    }

    public void setRoleNull() {
        this.role = RoleType.NONE;
        PLAYER_LOGGER.log(Level.forName("ROLE", 401), "Player {} role set to: {}", id, role.getRoleName());
    }

    public RoleType getRole() {
        return role;
    }

    // Módosított kör befejezés
    public void endTurn() {
        // Do NOT reset role here so role switching in GameController works properly
        // Just reset action points or any other per-turn resources
        this.action = 0;
    }

    // Mozgás validáció közvetlenül a RoleType enum használatával
    public boolean moveInsect(Insect_Class insect, Tecton_Class target) {
        return role.validateAndMoveInsect(this, insect, target);
    }
    
    // Gomba lehelyezés validáció közvetlenül a RoleType enum használatával
    public boolean placeMushroom(Mushroom_Class mushroom, Tecton_Class target) {
        return role.validateAndPlaceMushroom(this, mushroom, target);
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
        if (this.income - amount < 0) {
            this.income = 0;
        } else {
            this.income -= amount;
        }
        PLAYER_LOGGER.log(Level.forName("INCOME", 401), "Income decreased by " + amount + ". New income: " + this.income);
    }
    
    public int getAction() {
        return action;
    }
    public void setAction(int action) {
        if (action < 0) {
            action = 0; // Action points cannot be negative
        }
        this.action = action;
        PLAYER_LOGGER.log(Level.forName("ACTION", 401), "Action points set to " + action);
    }
    public String getName() {
        return name;
    }

    public Color getColor() { // Added getColor method
        return color;
    }

    public void setColor(Color color) { // Added setColor method
        this.color = color;
    }
}
