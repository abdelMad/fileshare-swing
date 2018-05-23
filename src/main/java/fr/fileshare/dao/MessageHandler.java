package fr.fileshare.dao;

import fr.fileshare.model.Message;
import fr.fileshare.model.Utilisateur;
import org.hibernate.Query;
import org.hibernate.Session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageHandler implements IMessageHandler {

    public boolean add(Message message) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        boolean check = false;
        try {
            session.beginTransaction();
            message.setStatus(Message.MSG_NON_LU);
            session.save(message);
            session.getTransaction().commit();
            check = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return check;

    }

    public boolean update(Message message) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        boolean check = false;
        try {
            session.beginTransaction();
            session.update(message);
            session.getTransaction().commit();
            check = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return check;

    }

    public boolean delete(Message message) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        boolean check = false;
        try {
            session.beginTransaction();
            Message messageToDelete = session.get(message.getClass(), message.getId());
            session.delete(messageToDelete);
            session.getTransaction().commit();
            check = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return check;

    }

    public Message get(Message message) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            // TODO get Message by mail and pwd
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public List getUtilisateursContactes(int idUtilisateur) {
        List utilisateurs = new ArrayList();
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query = session.createSQLQuery("SELECT DISTINCT * from utilisateur where utilisateur.utilisateur_id = Any(SELECT DISTINCT message.recepteur FROM message WHERE message.emetteur = :id_utilisateur) OR utilisateur.utilisateur_id = Any (SELECT DISTINCT message.emetteur FROM message WHERE message.recepteur = :id_utilisateur)")
                    .addEntity("utilisateur", Utilisateur.class);
            query.setParameter("id_utilisateur", idUtilisateur);
            query.setFirstResult(0);
            query.setMaxResults(15);
            utilisateurs = query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return utilisateurs;
    }

    public boolean checkNouveauContact(int idUtilisateur, int idEmetteur) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query = session.createSQLQuery("SELECT COUNT(*) FROM message WHERE (message.emetteur=:id_utilisateur AND message.recepteur=:id_emetteur) OR (message.emetteur=:id_emetteur AND message.recepteur=:id_utilisateur)");
            query.setParameter("id_utilisateur", idUtilisateur);
            query.setParameter("id_emetteur", idEmetteur);
            Object result = query.uniqueResult();
            session.getTransaction().commit();
            return ((Number) result).intValue() == 0;
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return false;
    }

    public List<Message> getMessages(int sender, int receiver, int start, int end) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        List<Message> messages = new ArrayList<>();
        try {
            session.beginTransaction();
            Query query = session.createSQLQuery("SELECT DISTINCT * FROM message WHERE (emetteur=:sender AND recepteur=:receiver) OR (emetteur=:receiver AND recepteur=:sender) ORDER BY date DESC").addEntity("message", Message.class);
            query.setParameter("sender", sender);
            query.setParameter("receiver", receiver);
            query.setFirstResult(start);
            query.setMaxResults(end);
            messages = (List<Message>) query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return messages;
    }

    public List<Utilisateur> getContacts(int userId) {

        List<Utilisateur> utilisateurs = new ArrayList<>();
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query = session.createSQLQuery("SELECT DISTINCT * from utilisateur where (utilisateur.utilisateur_id = Any(SELECT DISTINCT message.recepteur FROM message WHERE message.emetteur = :userId) OR utilisateur.utilisateur_id = Any (SELECT DISTINCT message.emetteur FROM message WHERE message.recepteur = :userId))")
                    .addEntity("user", Utilisateur.class);
            query.setParameter("userId", userId);
            utilisateurs = (List<Utilisateur>) query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return utilisateurs;
    }

    public List<Message> getGroupeMessages(int idG, int start, int end) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        List<Message> messages = new ArrayList<>();
        try {
            session.beginTransaction();
            Query query = session.createSQLQuery("SELECT message.* FROM message JOIN document on message.messagesGroupe=document.document_id WHERE document.document_id=:idG ORDER BY date DESC").addEntity("message", Message.class);
            query.setParameter("idG", idG);
            query.setFirstResult(start);
            query.setMaxResults(end);
            messages = (List<Message>) query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        Collections.reverse(messages);
        return messages;
    }

}
