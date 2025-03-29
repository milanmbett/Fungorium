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
       
        MAIN_LOGGER.log(Level.forName("CREATE",401),"Program created");
        Player p1 = new Player();
        
        Tecton_Basic t1 = new Tecton_Basic();
        Tecton_Dead t2 = new Tecton_Dead();
        t1.add_TectonNeighbour(t2);
        t2.add_TectonNeighbour(t1);

        Mushroom_Shroomlet m1 = new Mushroom_Shroomlet(t1, p1);
        Insect_Buglet i1 = new Insect_Buglet(t1, p1);
        i1.move_Insect(t2);
        Thread_Class th1 = new Thread_Class(t1);
        th1.expand_Thread();
        i1.move_Insect(t2);

        
        
    }
}