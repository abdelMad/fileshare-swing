package fr.fileshare.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "message")
public class Message {
    public static final String MSG_LU = "vu";
    public static final String MSG_NON_LU = "-1";
    @Id
    @GeneratedValue
    private int id;
    @Column(columnDefinition = "TEXT")
    private String text;
    @ManyToOne
    @JoinColumn(name = "emetteur")
    private Utilisateur emetteur;
    @ManyToOne
    @JoinColumn(name = "recepteur")
    private Utilisateur recepteur;
    private Date date;
    private String status;
    @ManyToOne
    @JoinColumn(name = "messagesGroupe")
    private Document groupe;
    public Message() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Utilisateur getEmetteur() {
        return emetteur;
    }

    public void setEmetteur(Utilisateur emetteur) {
        this.emetteur = emetteur;
    }

    public Utilisateur getRecepteur() {
        return recepteur;
    }

    public void setRecepteur(Utilisateur recepteur) {
        this.recepteur = recepteur;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Document getGroupe() {
        return groupe;
    }

    public void setGroupe(Document groupe) {
        this.groupe = groupe;
    }
}
