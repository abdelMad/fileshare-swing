package fr.fileshare.dao;

import fr.fileshare.models.Message;

import java.util.List;

public interface IMessageHandler {
    boolean add(Message message);

    boolean update(Message message);

    boolean delete(Message message);

    Message get(Message message);

    List getUtilisateursContactes(int id_utilisateur);

    List getConversation(int emetteur, int recepteur);

    String getJsonConversation(int emetteur, int recepteur);

    String getJsonUtilisateursContactes(int idUtilisateur, int emeteur);

    boolean checkNouveauContact(int idUtilisateur, int idEmetteur);

    boolean changerStatusMessage(int utilisateur, int emetteur);

}
