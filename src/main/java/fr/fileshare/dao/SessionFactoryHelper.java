package fr.fileshare.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionFactoryHelper {

    private static SessionFactory sessionFactory;

    private SessionFactoryHelper() {
        // load config
        try {
            Configuration config = new Configuration();
            
            sessionFactory = config.configure().buildSessionFactory();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null)
            new SessionFactoryHelper();
        return sessionFactory;
    }

    public static void init(){
       Session s = getSessionFactory().openSession();
       s.close();
    }

}
