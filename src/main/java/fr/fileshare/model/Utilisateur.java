package fr.fileshare.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "utilisateur")
public class Utilisateur {
    @Id
    @GeneratedValue
    @Column(name = "utilisateur_id")
    private int id;
    @Column(unique = true)
    private String email;
    private String mdp;
    private String nom;
    private String prenom;
    private String loginStatus;
    private boolean emailChecked;
    private Date registerDate;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String image;
    @OneToMany(mappedBy = "auteur", fetch = FetchType.EAGER)
    private Set<Document> documents;

    @OneToMany(mappedBy = "editeur")
    private Set<Historique> historiques;
    @OneToMany(mappedBy = "emetteur")
    private Set<Message> messagesSent;
    @OneToMany(mappedBy = "recepteur")
    private Set<Message> messagesReceived;

    @ManyToMany(mappedBy = "utilisateursAvecDroit")
    private Set<Document> documentsAutorises;

    @ManyToMany(mappedBy = "utilisateursFavoris")
    private Set<Document> favoris;

    public Utilisateur() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public boolean isEmailChecked() {
        return emailChecked;
    }

    public void setEmailChecked(boolean emailChecked) {
        this.emailChecked = emailChecked;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    public Set<Historique> getHistoriques() {
        return historiques;
    }

    public void setHistoriques(Set<Historique> historiques) {
        this.historiques = historiques;
    }

    public Set<Message> getMessagesSent() {
        return messagesSent;
    }

    public void setMessagesSent(Set<Message> messagesSent) {
        this.messagesSent = messagesSent;
    }

    public Set<Message> getMessagesReceived() {
        return messagesReceived;
    }

    public void setMessagesReceived(Set<Message> messagesReceived) {
        this.messagesReceived = messagesReceived;
    }

    public Set<Document> getDocumentsAutorises() {
        return documentsAutorises;
    }

    public void setDocumentsAutorises(Set<Document> documentsAutorises) {
        this.documentsAutorises = documentsAutorises;
    }

    public Set<Document> getFavoris() {
        return favoris;
    }

    public void setFavoris(Set<Document> favoris) {
        this.favoris = favoris;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Utilisateur that = (Utilisateur) o;

        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
