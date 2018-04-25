package fr.fileshare.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "verificationtoken")
public class VerificationToken {
    // 2 days until the token expire
    public static final int EXPIRATION = 2;
    public static final int VALIDATION_MAIL_TOKEN = 0;
    public static final int RECOVERY_PWD_TOKEN = 1;
    @Id
    @GeneratedValue
    private int id;
    private String token;
    @OneToOne(targetEntity = Utilisateur.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "utilisateur_id")
    private Utilisateur utilisateur;
    private int type;

    private Date expirationDate;

    public VerificationToken() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
