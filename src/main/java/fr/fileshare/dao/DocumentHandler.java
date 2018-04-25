package fr.fileshare.dao;

import fr.fileshare.models.Document;
import fr.fileshare.models.Utilisateur;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class DocumentHandler implements IDocumentHandler {
    public boolean add(Document document) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        boolean check = false;
        try {
            session.beginTransaction();
            session.save(document);
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

    public boolean update(Document document) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        boolean check = false;
        try {
            session.beginTransaction();
            session.update(document);
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

    public boolean delete(Document document) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        boolean check = false;
        try {
            session.beginTransaction();
            Document documentASupprimer = session.get(document.getClass(), document.getId());
            session.delete(documentASupprimer);
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

    public Document get(int id) {
        Document document = null;
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            document = session.get(Document.class, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return document;
    }

    public List getDocumentsAVoir(Utilisateur utilisateur, String intitule, String tags, int maxResultat) {
        List documents = new ArrayList();
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query;
            if (utilisateur != null) {
                int id_utilisateur = utilisateur.getId();
                query = session.createSQLQuery("SELECT * FROM document as doc WHERE (doc.status = 0 OR(doc.status=2 AND EXISTS(SELECT * FROM document_utilisateur WHERE document_utilisateur.utilisateur_id=:id_utilisateur AND doc.document_id = document_utilisateur.document_id )  ) OR (doc.auteur=:id_utilisateur)) AND (intitule LIKE :intitule OR tag LIKE :tags )")
                        .addEntity("document", Document.class);
                query.setParameter("id_utilisateur", "'%" + id_utilisateur + "%'");
                query.setParameter("intitule", "'%" + intitule + "%'");
                query.setParameter("tags", tags);
            } else {
                query = session.createQuery(" FROM Document  WHERE status = 0");
            }

            query.setFirstResult(0);

            query.setMaxResults(maxResultat);
            documents = query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return documents;
    }
}
