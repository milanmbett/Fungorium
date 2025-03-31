package com.coderunnerlovagjai.app;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//TODO: Van olyan tekton-fajta, amelyik életben tartja azokat a fonalakat, amelyek nincsenek közvetve vagy közvetlenül gombatesthez kötve.
//TODO: Az elrágott fonalak nem pusztulnak el azonnal, hanem csak egy kis idő elteltével (ez fonaltípustól függő idő).  
public class Main
{

    private static final Logger MAIN_LOGGER = LogManager.getLogger(Main.class);
    public static void main(String[] args)
    {
        _Tests.test13(); 
    }
}