package com.coderunnerlovagjai.app;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;

//TODO: Van olyan tekton-fajta, amelyik életben tartja azokat a fonalakat, amelyek nincsenek közvetve vagy közvetlenül gombatesthez kötve.
//TODO: Az elrágott fonalak nem pusztulnak el azonnal, hanem csak egy kis idő elteltével (ez fonaltípustól függő idő).  
public class Main
{

    public static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args)
    {
       
        logger.log(Level.forName("CREATE",401),"Program created!!!");
        
    }
}