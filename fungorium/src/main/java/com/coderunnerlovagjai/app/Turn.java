package com.coderunnerlovagjai.app;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Turn //TODO: Megírás: Körök számának tárolása, körök közötti váltás pontok számolása
{
    private static final Logger TURN_LOGGER = LogManager.getLogger(Turn.class);

    // EZ AZ OSZTÁLY LEHET NEM KELL 

    private int turnNumber;

    public void endTurn() //TODO: Megírás
    {
        TURN_LOGGER.info("Turn ended. Turn number: " + turnNumber);
    }
    public void startTurn() //TODO: Megírás
    {
        TURN_LOGGER.info("Turn started. Turn number: " + turnNumber);
    }

}
