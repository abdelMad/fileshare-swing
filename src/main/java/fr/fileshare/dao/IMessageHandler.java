package fr.fileshare.dao;

import fr.fileshare.model.Message;
import fr.fileshare.model.Utilisateur;

import java.util.List;

public interface IMessageHandler {
 /**
  * Ajout d un message
  * @param message
  * @return
  */
   boolean add(Message message);

 /**
  * modification d un message
  * @param message
  * @return
  */
 boolean update(Message message);

 /**
  * supression d un message
  * @param message
  * @return
  */
    boolean delete(Message message);

 /**
  * recuperation d un message
  * @param message
  * @return
  */
    Message get(Message message);

 /**
  * recuperation de la liste des utilisateurs contactes par un utilisateur
  * @param id_utilisateur
  * @return
  */
 List getUtilisateursContactes(int id_utilisateur);


 /**
  * verification si un emetteur est un nouveau contact pour l' utilisateur
  * @param idUtilisateur
  * @param idEmetteur
  * @return
  */
    boolean checkNouveauContact(int idUtilisateur, int idEmetteur);

 /**
  * recuperation des messages d'une conversation
  * @param sender
  * @param receiver
  * @param start
  * @param end
  * @return
  */
    List<Message> getMessages(int sender, int receiver, int start, int end);

 /**
  * recuperation de la liste des contactes
  * @param userId
  * @return
  */
 List<Utilisateur> getContacts(int userId);

 /**
  * recuperation de la conversation du groupe
  * @param idG
  * @param start
  * @param end
  * @return
  */
    List<Message> getGroupeMessages(int idG, int start, int end);

}
