package fr.fileshare.dao;

import fr.fileshare.model.Document;
import fr.fileshare.model.Historique;
import java.util.List;

public interface IHistoriqueHandler {
    /**
     * Ajout d'un historique
     * @param historique
     * @return
     */
    boolean add(Historique historique);

    /**
     * modification d'un historique
     * @param historique
     * @return
     */
    boolean update(Historique historique);

    /**
     * supression d'un historique
     * @param historique
     * @return
     */
    boolean delete(Historique historique);

    /**
     * recuperation d'un historique par id
     * @param id
     * @return
     */
   Historique get(int id);

    /**
     * recuperation du dernier historique
     * @param documentId
     * @return
     */
    Historique getDernierHistoique(int documentId);

    /**
     * recuperation des documents modifies par un utilisateur
     * @param idU
     * @param start
     * @param end
     * @return
     */
    List<Document> getDocsModifies(int idU, int start, int end);

    /**
     * list de l'historique
     * @param idU
     * @param doc
     * @param start
     * @param end
     * @return
     */
    List<Historique> getHistorique(int idU, int doc, int start, int end);

}
