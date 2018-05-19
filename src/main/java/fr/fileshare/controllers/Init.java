package fr.fileshare.controllers;

import fr.fileshare.dao.IUtilisateurHandler;
import fr.fileshare.dao.SessionFactoryHelper;
import fr.fileshare.dao.UtilisateurHandler;
import fr.fileshare.views.Connexion;
import fr.fileshare.views.Dashboard;
import fr.fileshare.views.Debug;
import fr.fileshare.views.HTMLDocumentEditor;
import java.io.File;

/**
 * Hello world!
 *
 */
public class Init {

    public static void main(String[] args) {

        Connexion c = new Connexion();
        //Debug c = new Debug();
        //HTMLDocumentEditor c = new HTMLDocumentEditor();
        c.setVisible(true);

    }
}
