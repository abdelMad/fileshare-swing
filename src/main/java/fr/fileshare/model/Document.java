package fr.fileshare.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "document")
public class Document {

    public static final int PUBLIC = 0;
    public static final int PRIVE = 1;
    public static final int PARTAGE = 2;

    @Id
    @GeneratedValue
    @Column(name = "document_id")
    private int id;
    private String intitule;
    private String description;
    private Date datePublixation;
    private Date dateDerniereModif;
    @ManyToOne
    @JoinColumn(name = "dernier_editeur")
    private Utilisateur dernierEditeur;
    private String tag;
    private String mdp;
    private int status;
    @ManyToOne
    @JoinColumn(name = "auteur")
    private Utilisateur auteur;
    @OneToMany(mappedBy = "document", fetch = FetchType.EAGER)
    private Set<Historique> historique;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="document_utilisateur", joinColumns=@JoinColumn(name="document_id"), inverseJoinColumns=@JoinColumn(name="utilisateur_id"))
    private Set<Utilisateur> utilisateursAvecDroit;
    @Column(columnDefinition = "TEXT")
    private String dernierContenu;

    public Document() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDatePublixation() {
        return datePublixation;
    }

    public void setDatePublixation(Date datePublixation) {
        this.datePublixation = datePublixation;
    }


    public Utilisateur getAuteur() {
        return auteur;
    }

    public void setAuteur(Utilisateur auteur) {
        this.auteur = auteur;
    }

    public Set<Historique> getHistorique() {
        return historique;
    }

    public void setHistorique(Set<Historique> historique) {
        this.historique = historique;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public Set<Utilisateur> getUtilisateursAvecDroit() {
        return utilisateursAvecDroit;
    }

    public void setUtilisateursAvecDroit(Set<Utilisateur> utilisateursAvecDroit) {
        this.utilisateursAvecDroit = utilisateursAvecDroit;
    }

    public Date getDateDerniereModif() {
        return dateDerniereModif;
    }

    public void setDateDerniereModif(Date dateDerniereModif) {
        this.dateDerniereModif = dateDerniereModif;
    }

    public Utilisateur getDernierEditeur() {
        return dernierEditeur;
    }

    public void setDernierEditeur(Utilisateur dernierEditeur) {
        this.dernierEditeur = dernierEditeur;
    }

    public String getDernierContenu() {
        return dernierContenu;
    }

    public void setDernierContenu(String dernierContenu) {
        this.dernierContenu = dernierContenu;
    }
}
