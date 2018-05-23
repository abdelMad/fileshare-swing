package fr.fileshare.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
/**
 * Table document
 */
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
    @Column(columnDefinition = "TEXT")
    private String description;
    private Date datePublixation;
    private Date dateDerniereModif;
    @ManyToOne
    @JoinColumn(name = "dernier_editeur")
    private Utilisateur dernierEditeur;
    private String tag;
    private boolean readOnly;
    private int status;
    @ManyToOne
    @JoinColumn(name = "auteur")
    private Utilisateur auteur;
    @OneToMany(mappedBy = "document", cascade = CascadeType.REMOVE)
    private Set<Historique> historique;

    @OneToMany(mappedBy = "groupe")
    private Set<Message> messagesGroupe;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="document_utilisateur", joinColumns=@JoinColumn(name="document_id"), inverseJoinColumns=@JoinColumn(name="utilisateur_id"))
    private Set<Utilisateur> utilisateursAvecDroit;
    @Column(columnDefinition = "TEXT")
    private String dernierContenu;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "favoris", joinColumns = @JoinColumn(name = "document_id"), inverseJoinColumns = @JoinColumn(name = "utilisateur_id"))
    private Set<Utilisateur> utilisateursFavoris;

    @Column(unique = true)
    private String version;

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

    public Set<Utilisateur> getUtilisateursFavoris() {
        return utilisateursFavoris;
    }

    public void setUtilisateursFavoris(Set<Utilisateur> utilisateursFavoris) {
        this.utilisateursFavoris = utilisateursFavoris;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Set<Message> getMessagesGroupe() {
        return messagesGroupe;
    }

    public void setMessagesGroupe(Set<Message> messagesGroupe) {
        this.messagesGroupe = messagesGroupe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return id == document.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
