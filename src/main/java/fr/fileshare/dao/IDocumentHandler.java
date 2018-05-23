package fr.fileshare.dao;

import fr.fileshare.model.Document;
import fr.fileshare.model.Utilisateur;

import java.util.List;

public interface IDocumentHandler {
    /**
     *  Ajout d'un document
     * @param document
     * @return
     */
    boolean add(Document document);

    /**
     *  modification d'un document
     * @param document
     * @return
     */
    boolean update(Document document);

    /**
     * supression d'un document
     * @param document
     * @return
     */
    boolean delete(Document document);

    /**
     *  recuperation d'un document par un id
     * @param id
     * @return
     */
    Document get(int id);

    /**
     *  recuperation des document qu'un utilisateur peut voir
     * @param utilisateurCourant
     * @param debut
     * @param fin
     * @param intitule
     * @param tags
     * @return
     */
    List<Document> getDocumentsAVoir(Utilisateur utilisateurCourant, int debut, int fin, String intitule, String tags);

    /**
     *  recuperation des documents favoris
     * @param utilisateurCourant
     * @param debut
     * @param fin
     * @return
     */
    List<Document> getDocumentsFavoris(Utilisateur utilisateurCourant, int debut, int fin);

    /**
     * recuperation des documents de l'utilisateurs
     * @param id_utilisateur
     * @return
     */
    List<Document> getMesDocuments(int id_utilisateur);

    /**
     * verification du document par id si il est favoris ou pas
     * @param idDoc
     * @param idU
     * @return
     */
    boolean estFavoris(int idDoc, int idU);

    /**
     * retirer un document des favoris
     * @param idU
     * @param idDoc
     * @return
     */
    boolean supprimerFavoris(int idU, int idDoc);

    /**
     * ajout d'un document au favoris
     * @param idU
     * @param idDoc
     * @return
     */
    boolean ajouterFavoris(int idU, int idDoc);

    /**
     * permet le telechargement d'un document
     * @param document
     * @param path
     * @return
     */
    boolean telechargerDoc(Document document,String path);

}

