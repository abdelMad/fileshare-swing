package fr.fileshare.dao;

import fr.fileshare.models.Historique;
import fr.fileshare.models.Document;
import java.util.ArrayList;
import java.util.List;
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

    public List<Document> getDocsModifies(int idU, int start, int end) {
        List<Document> documents = new ArrayList<>();
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query = session.createSQLQuery("SELECT distinct document.* FROM fileshare.historique JOIN document ON document.document_id = historique.document \n" +
                    "where (document.status=:partage or document.status=:public or document.auteur=:idu )and historique.editeur=:idu").addEntity("document", Document.class);
            query.setParameter("partage", Document.PARTAGE);
            query.setParameter("public", Document.PUBLIC);
            query.setParameter("idu", idU);
            query.setFirstResult(start);
            query.setMaxResults(end);
            documents = (List<Document>) query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return documents;
    }

    public List<Historique> getHistorique(int idU, int doc, int start, int end) {
        List<Historique> historiques = new ArrayList<>();
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query = session.createSQLQuery("SELECT historique.* FROM fileshare.historique JOIN document on document.document_id=historique.document LEFT JOIN document_utilisateur ON document.document_id=document_utilisateur.document_id \n" +
                    "where (document.auteur=:idu or document_utilisateur.utilisateur_id=:idu or document.status=:public) AND  document = :doc").addEntity("historique", Historique.class);
            query.setParameter("doc", doc);
            query.setParameter("public", Document.PUBLIC);
            query.setParameter("idu", idU);
            query.setFirstResult(start);
            query.setMaxResults(end);
            historiques = (List<Historique>) query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return historiques;
    }
}
