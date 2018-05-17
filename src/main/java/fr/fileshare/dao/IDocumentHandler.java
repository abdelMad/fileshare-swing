package fr.fileshare.dao;

import fr.fileshare.model.Document;
import fr.fileshare.model.Utilisateur;

import java.util.List;

public interface IDocumentHandler {
    boolean add(Document document);

    boolean update(Document document);

    boolean delete(Document document);

    Document get(int id);

    List<Document> getDocumentsAVoir(Utilisateur utilisateurCourant, int debut, int fin, String intitule, String tags);

    List<Document> getDocumentsFavoris(Utilisateur utilisateurCourant, int debut, int fin);

    List<Document> getMesDocuments(int id_utilisateur);

    boolean estFavoris(int idDoc, int idU);

    boolean supprimerFavoris(int idU, int idDoc);

    boolean ajouterFavoris(int idU, int idDoc);
    boolean telechargerDoc(Document document,String path);

}

