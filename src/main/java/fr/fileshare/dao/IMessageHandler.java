package fr.fileshare.dao;

import fr.fileshare.model.Message;
import fr.fileshare.model.Utilisateur;

import java.util.List;

public interface IMessageHandler {
   boolean add(Message message);

    boolean update(Message message);

    boolean delete(Message message);

    Message get(Message message);

    List getUtilisateursContactes(int id_utilisateur);


    boolean checkNouveauContact(int idUtilisateur, int idEmetteur);

    List<Message> getMessages(int sender, int receiver, int start, int end);

    List<Utilisateur> getContacts(int userId);

    List<Message> getGroupeMessages(int idG, int start, int end);

}
