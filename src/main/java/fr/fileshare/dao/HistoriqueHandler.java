package fr.fileshare.dao;

import fr.fileshare.models.Historique;
import org.hibernate.Query;
import org.hibernate.Session;

public class HistoriqueHandler implements IHistoriqueHandler {
    public boolean add(Historique historique) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        boolean check = false;
        try {
            session.beginTransaction();
            session.save(historique);
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

    public boolean update(Historique historique) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        boolean check = false;
        try {
            session.beginTransaction();
            session.update(historique);
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

    public boolean delete(Historique historique) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        boolean check = false;
        try {
            session.beginTransaction();
            Historique historiqueASupprimer = session.get(historique.getClass(), historique.getId());
            session.delete(historiqueASupprimer);
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

    public Historique get(int id) {
        Historique historique = null;
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            historique = session.get(Historique.class, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return historique;
    }

    public Historique getDernierHistoique(int documentId){
        Historique historique = null;
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            Query query = session.createQuery("from Historique WHERE document_id=:document_id ORDER BY id DESC");
            query.setParameter("document_id",documentId);
            historique = (Historique) query.setMaxResults(1).uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return historique;
    }
}
