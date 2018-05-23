package fr.fileshare.dao;

import fr.fileshare.model.Document;
import fr.fileshare.model.Utilisateur;
import java.math.BigInteger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.contenttype.ContentType;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.AlternativeFormatInputPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.CTAltChunk;
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

    public List<Document> getDocumentsAVoir(Utilisateur utilisateurCourant, int debut, int fin, String intitule, String tags) {
        List<Document> documents = new ArrayList<>();
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query;
            if (utilisateurCourant != null) {
                int id_utilisateur = utilisateurCourant.getId();
                query = session.createSQLQuery("SELECT * from(SELECT * FROM fileshare.document WHERE status =0 OR auteur = :id_utilisateur\n" +
                        "UNION\n" +
                        "select document.* from fileshare.document join document_utilisateur on document_utilisateur.document_id=document_utilisateur.document_id where document.status=:status  AND document_utilisateur.utilisateur_id=:id_utilisateur\n" +
                        ") fichiers_autorise\n" +
                        "WHERE fichiers_autorise.intitule LIKE '%" + intitule + "%' AND fichiers_autorise.tag LIKE '%" + tags + "%' " +
                        "order by document_id Desc")
                        .addEntity("document", Document.class);
                query.setParameter("id_utilisateur", id_utilisateur);
                query.setParameter("status", Document.PARTAGE);
            } else {
                query = session.createQuery(" FROM Document  WHERE status = 0");
            }
            if (debut != -1 && fin != -1) {
                query.setFirstResult(debut);
                query.setMaxResults(fin);
            }
            documents = (List<Document>) query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return documents;
    }

    public List<Document> getDocumentsFavoris(Utilisateur utilisateurCourant, int debut, int fin) {
        List<Document> documents = new ArrayList<>();
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query;
            int id_utilisateur = utilisateurCourant.getId();
            query = session.createSQLQuery("SELECT distinct document.* FROM fileshare.document JOIN fileshare.favoris ON favoris.document_id = document.document_id WHERE\n" +
                    "favoris.utilisateur_id = :id_utilisateur ORDER BY document.document_id")
                    .addEntity("document", Document.class);
            query.setParameter("id_utilisateur", id_utilisateur);
            if (debut != -1 && fin != -1) {
                query.setFirstResult(debut);
                query.setMaxResults(fin);
            }
            documents = (List<Document>) query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return documents;
    }

    public boolean estFavoris(int idDoc, int idU) {
        boolean estFavoris = false;
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query;
            query = session.createSQLQuery("SELECT count(*) FROM fileshare.favoris where document_id=:idDoc and utilisateur_id =:idU ;");
            query.setParameter("idU", idU);
            query.setParameter("idDoc", idDoc);
            int count = ((BigInteger) query.uniqueResult()).intValue();
            estFavoris = (count != 0);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return estFavoris;
    }

    public boolean supprimerFavoris(int idU, int idDoc) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        boolean check = false;
        try {
            session.beginTransaction();
            Query query = session.createSQLQuery("DELETE FROM favoris where document_id=:idDoc AND utilisateur_id=:idU ");
            query.setParameter("idDoc", idDoc);
            query.setParameter("idU", idU);
            query.executeUpdate();
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

    public boolean ajouterFavoris(int idU, int idDoc) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        boolean check = false;
        try {
            session.beginTransaction();
            Query query = session.createSQLQuery("insert into favoris (document_id,utilisateur_id) values(:idDoc,:idU)");
            query.setParameter("idDoc", idDoc);
            query.setParameter("idU", idU);
            query.executeUpdate();
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


    public List<Document> getMesDocuments(int id_utilisateur) {
        List<Document> documents = new ArrayList<>();
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query;
            query = session.createSQLQuery("SELECT * from(SELECT * FROM fileshare.document WHERE auteur = :id_utilisateur\n" +
                    "UNION\n" +
                    "select document.* from fileshare.document join document_utilisateur on document_utilisateur.document_id=document_utilisateur.document_id where document.status=:status  AND document_utilisateur.utilisateur_id=:id_utilisateur\n" +
                    ") fichiers_autorise\n" +
                    "order by document_id Desc")
                    .addEntity("document", Document.class);
            query.setParameter("id_utilisateur", id_utilisateur);
            query.setParameter("status", Document.PARTAGE);

            documents = (List<Document>) query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return documents;
    }
        public boolean telechargerDoc(Document document,String path) {
        try {
            String html = "<html><head><title>Import me</title></head><body>" + document.getDernierContenu() + "</body></html>";
            System.out.println(document.getDernierContenu());
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
            AlternativeFormatInputPart afiPart = new AlternativeFormatInputPart(new PartName("/hw.html"));
            afiPart.setBinaryData(html.getBytes());
            afiPart.setContentType(new ContentType("text/html"));
            Relationship altChunkRel = wordMLPackage.getMainDocumentPart().addTargetPart(afiPart);

// .. the bit in document body
            CTAltChunk ac = Context.getWmlObjectFactory().createCTAltChunk();
            ac.setId(altChunkRel.getId());
            wordMLPackage.getMainDocumentPart().addObject(ac);

// .. content type
            wordMLPackage.getContentTypeManager().addDefaultContentType("html", "text/html");
            wordMLPackage.save(new java.io.File(path+ "/" + document.getIntitule() + ".docx"));
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
        return false;
    }

}
