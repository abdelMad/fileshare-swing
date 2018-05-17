package fr.fileshare.utilities;


import fr.fileshare.model.Message;
import fr.fileshare.model.Utilisateur;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Queue;

public class JsonHelper {

    public HashMap<String, String> decodeMessage(String textMessage) {
        HashMap<String, String> chatMessage = new HashMap<>();
        JSONObject jsonObject = new JSONObject(textMessage);
        chatMessage.put("message", jsonObject.getString("message").trim().replace("\n", "<br>"));
        chatMessage.put("type", jsonObject.getString("type"));
        chatMessage.put("sender", jsonObject.getString("sender"));
        chatMessage.put("receiver", jsonObject.getString("receiver"));
        chatMessage.put("sentDate", jsonObject.getString("sentDate"));
        chatMessage.put("senderImg", jsonObject.getString("senderImg"));
        chatMessage.put("senderId", jsonObject.getString("senderId"));
        return chatMessage;
    }

    public String encodeMessage(final HashMap<String, String> chatMessage) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", chatMessage.get("type"));
        jsonObject.put("message", chatMessage.get("message"));
        jsonObject.put("sender", chatMessage.get("sender"));
        jsonObject.put("receiver", chatMessage.get("receiver"));
        jsonObject.put("sentDate", chatMessage.get("sentDate"));
        jsonObject.put("senderImg", chatMessage.get("senderImg"));
        jsonObject.put("senderId", chatMessage.get("senderId"));

        return jsonObject.toString();
    }

    public String encodeMessage(final Message message) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message.getText());
        jsonObject.put("senderId", Integer.toString(message.getEmetteur().getId()));
        jsonObject.put("sender", message.getEmetteur().getNom());
        jsonObject.put("senderImg", (message.getEmetteur().getImage() == null) ? "/assets/images/people.png" : message.getEmetteur().getImage());
        jsonObject.put("sentDate", formatter.format(message.getDate()));
        return jsonObject.toString();
    }

    public String encodeUtilisateur(final Utilisateur utilisateur) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("senderId", utilisateur.getId());
        jsonObject.put("sender", utilisateur.getPrenom() + " " + utilisateur.getNom());
        jsonObject.put("senderImg", (utilisateur.getImage() == null) ? "/assets/images/people.png" : utilisateur.getImage());
        return jsonObject.toString();
    }

    public String encodeDoc(final HashMap<String, String> doc) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("idDoc", doc.get("idDoc"));
        jsonObject.put("idU", doc.get("idU"));
        jsonObject.put("txt", doc.get("txt"));

        return jsonObject.toString();
    }

    public HashMap<String, String> decodeDoc(final String docText) {
        HashMap<String, String> doc = new HashMap<>();
        JSONObject jsonObject = new JSONObject(docText);
        doc.put("idDoc", jsonObject.getString("idDoc"));
        doc.put("idU", jsonObject.getString("idU"));
        doc.put("txt", jsonObject.getString("txt"));
        return doc;
    }

}