package fr.fileshare.controllers;

import fr.fileshare.dao.SessionFactoryHelper;
import fr.fileshare.views.Connexion;

/**
 * Hello world!
 *
 */
public class Init
{
    public static void main( String[] args )
    {

//        SessionFactoryHelper.init();
        System.out.println( "Hello World!" );
        Connexion init = new Connexion();

    }
}
