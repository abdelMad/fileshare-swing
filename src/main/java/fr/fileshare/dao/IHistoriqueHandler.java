package fr.fileshare.dao;

import fr.fileshare.model.Document;
import fr.fileshare.model.Historique;
import java.util.List;

public interface IHistoriqueHandler {
    boolean add(Historique historique);

    boolean update(Historique historique);

    boolean delete(Historique historique);

   Historique get(int id);

    Historique getDernierHistoique(int documentId);

    List<Document> getDocsModifies(int idU, int start, int end);

    List<Historique> getHistorique(int idU, int doc, int start, int end);

}
