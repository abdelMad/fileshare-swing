package fr.fileshare.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "historique")
public class Historique {
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    @JoinColumn(name = "document")
    private Document document;
    private Date dateModif;
    @Column(columnDefinition = "TEXT")
    private String contenu;
    @ManyToOne
    @JoinColumn(name = "editeur")
    private Utilisateur editeur;

    public Historique() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Date getDateModif() {
        return dateModif;
    }

    public void setDateModif(Date dateModif) {
        this.dateModif = dateModif;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Utilisateur getEditeur() {
        return editeur;
    }

    public void setEditeur(Utilisateur editeur) {
        this.editeur = editeur;
    }
}
