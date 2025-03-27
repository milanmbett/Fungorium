package com.coderunnerlovagjai.app;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//TODO: Van olyan tekton-fajta, amelyik életben tartja azokat a fonalakat, amelyek nincsenek közvetve vagy közvetlenül gombatesthez kötve.
//TODO: Az elrágott fonalak nem pusztulnak el azonnal, hanem csak egy kis idő elteltével (ez fonaltípustól függő idő).  
public class Main
{
    public static void main(String[] args)
    {
        Logger logger = LogManager.getLogger(Main.class);
        logger.info("Hello, World!");
        System.out.println("Hello faszos!");
    }
}