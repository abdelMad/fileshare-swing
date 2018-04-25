package fr.fileshare.dao;

import fr.fileshare.models.Historique;

public interface IHistoriqueHandler {
    boolean add(Historique historique);

    boolean update(Historique historique);

    boolean delete(Historique historique);

    Historique get(int id);

    Historique getDernierHistoique(int documentId);

}
