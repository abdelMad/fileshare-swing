package fr.fileshare.controllers;

import fr.fileshare.dao.IUtilisateurHandler;
import fr.fileshare.dao.SessionFactoryHelper;
import fr.fileshare.dao.UtilisateurHandler;
import fr.fileshare.views.Connexion;
import fr.fileshare.views.Dashboard;
import java.io.File;

/**
 * Hello world!
 *
 */
public class Init
{
    public static void main( String[] args )
    {
        //xx
        
        
         IUtilisateurHandler uHandler = new UtilisateurHandler();
         UtilisateurHandler.utilisateur = uHandler.get(1);
         Connexion c = new Connexion();
         Dashboard db = new Dashboard(c);
         c.setVisible(false);
         db.setVisible(true);
         System.out.println("here");


    }
}
