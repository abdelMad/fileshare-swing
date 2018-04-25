package fr.fileshare.dao;

import fr.fileshare.models.Message;
import fr.fileshare.models.Utilisateur;
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
            return ((Number)result).intValue() == 0;
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return false;
    }

    public List getConversation(int emetteur, int recepteur) {
        List utilisateurs = new ArrayList();
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            Query query = session.createSQLQuery("SELECT * FROM message WHERE (emetteur=:emetteur AND recepteur = :recepteur) OR (emetteur=:recepteur AND recepteur = :emetteur) ORDER BY id DESC")
                    .addEntity("message", Message.class);
            query.setParameter("emetteur", emetteur);
            query.setParameter("recepteur", recepteur);
            query.setMaxResults(10);
            utilisateurs = query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        System.out.println(utilisateurs.size());
        return utilisateurs;
    }

    public String getJsonConversation(int emetteur, int recepteur) {
        List messages = getConversation(emetteur, recepteur);
        String jsonResponse = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        Utilisateur recep;
        for (int i = messages.size() - 1; i >= 0; i--) {
            Message message = (Message) messages.get(i);
            jsonResponse += "{\"emetteur\":\"" + (message.getEmetteur().getId() == emetteur ? "moi" : message.getEmetteur().getNom()) + "\","
                    + "\"date\":\"" + simpleDateFormat.format(message.getDate()) + "\","
                    + "\"txt\":\"" + message.getText() + "\"}" + (i > 0 ? "," : "");
        }
        IUtilisateurHandler utilisateurHandler = new UtilisateurHandler();
        recep = utilisateurHandler.get(recepteur);

        return "\"" + recep.getPrenom() + " " + recep.getNom() + "\",\"" + (recep.getImage() == null ? "/assets/images/people.png" : recep.getImage()) + "\"," + jsonResponse;
    }

    public String getJsonUtilisateursContactes(int idUtilisateur, int emeteur) {

        IUtilisateurHandler utilisateurHandler = new UtilisateurHandler();
        Utilisateur utilisateur = utilisateurHandler.get(emeteur);
        List<Utilisateur> utilisateurs = (List<Utilisateur>) getUtilisateursContactes(idUtilisateur);
        if(checkNouveauContact(idUtilisateur,emeteur)) {
            Collections.reverse(utilisateurs);
            utilisateurs.add(utilisateur);
            Collections.reverse(utilisateurs);
        }

        String jsonResponse = "";
        for (int i = 0; i < utilisateurs.size(); i++) {
            Utilisateur u = utilisateurs.get(i);
            jsonResponse += "{" +
                    "\t\"id\": \"" + u.getId() + "\"," +
                    "\t\"nomComplet\": \"" + u.getPrenom() + " " + u.getNom() + "\"," +
                    "\t\"image\": \"" + (u.getImage() == null ? "/assets/images/people.png" : u.getImage()) + "\",\n" +
                    "\t\"aEnvoyeMsg\": \"" + (u.getId() == emeteur || !messageEstVu(idUtilisateur,u.getId()) ? "true" : "false") + "\"" +
                    "}" + (i < utilisateurs.size() - 1 ? "," : "");
        }
        return "[" + jsonResponse + "]";
    }
    public boolean changerStatusMessage(int utilisateur,int emetteur){
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE message set status='vu' WHERE emetteur="+emetteur+" AND recepteur="+utilisateur);
            query.executeUpdate();
            session.getTransaction().commit();
            return true;
        }catch (Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return false;
    }
    public boolean messageEstVu(int utilisateur,int emetteur){
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query = session.createSQLQuery("SELECT COUNT(*) FROM message WHERE status='-1' AND emetteur=:emetteur AND recepteur=:utilisateur");
            query.setParameter("emetteur",emetteur);
            query.setParameter("utilisateur",utilisateur);
            Object result = query.uniqueResult();
            session.getTransaction().commit();
            return ((Number)result).intValue() == 0;
        }catch (Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return false;
    }
}
