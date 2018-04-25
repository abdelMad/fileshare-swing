package fr.fileshare.dao;

import fr.fileshare.models.Utilisateur;
import fr.fileshare.models.VerificationToken;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import java.util.Date;


public class UtilisateurHandler implements IUtilisateurHandler {
    public static Utilisateur utilisateur = null;
    public int add(Utilisateur utilisateur) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        int check;
        try {
            session.beginTransaction();
            session.save(utilisateur);
            session.getTransaction().commit();
            check = 1;
        } catch (ConstraintViolationException e) {
            check = -1;
            session.getTransaction().rollback();
            e.printStackTrace();
        } catch (Exception e){
            check = 0;
            session.getTransaction().rollback();
            e.printStackTrace();
        }
        finally {
            session.close();
        }
        return check;

    }

    public boolean update(Utilisateur utilisateur) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        boolean check = false;
        try {
            session.beginTransaction();
            session.update(utilisateur);
            session.getTransaction().commit();
            check = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return check;
    }

    public boolean delete(Utilisateur utilisateur) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        boolean check = false;
        try {
            session.beginTransaction();
            Utilisateur utilisateurToDelete = session.get(utilisateur.getClass(), utilisateur.getId());
            session.delete(utilisateurToDelete);
            session.getTransaction().commit();
            check = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return check;
    }

    public boolean authenticate(String email,String mdp) {
        boolean check = false;

            if (email.length() != 0 && mdp.length() != 0) {
                Session session = SessionFactoryHelper.getSessionFactory().openSession();
                try {
                    session.beginTransaction();
                    Query query = session.createQuery("from Utilisateur where email=:email AND mdp=:mdp");
                    query.setString("email", email);
                    query.setString("mdp", Util.hashString(mdp));
                    Object object = query.uniqueResult();
                    session.getTransaction().commit();

                    if (object != null) {
                        utilisateur = (Utilisateur) object;

                        check = true;
                    } else
                        System.out.println("Email ou mot de passe incorrecte");

                } catch (Exception e) {
                    session.getTransaction().rollback();
                    e.printStackTrace();
                } finally {
                    session.close();
                }
            } else
                System.out.println("Veuillez fournire votre mail et mot de passe");

        return check;
    }

    public boolean register(String nom, String prenom, String email, String mdp, String confirmMdp, String description) {

            if (nom.trim().length() > 0 && prenom.trim().length() > 0 && email.trim().length() > 0 && mdp.trim().length() > 0 && confirmMdp.trim().length() > 0) {
                if (mdp.equals(confirmMdp)) {
                    if(Util.isValidEmail(email)){
                        Utilisateur newUtilisateur = new Utilisateur();
                        newUtilisateur.setEmail(email);
                        newUtilisateur.setNom(nom);
                        newUtilisateur.setPrenom(prenom);
                        newUtilisateur.setEmailChecked(false);
                        newUtilisateur.setRegisterDate(new Date());
                        newUtilisateur.setMdp(Util.hashString(mdp));
                        if (description.trim().length() > 0) newUtilisateur.setDescription(description);
                        int check = add(newUtilisateur);
                        if( check == 1) {
                            utilisateur = newUtilisateur;
                            IVerificationTokenHandler verificationTokenHandler = new VerificationTokenHandler();
                            verificationTokenHandler.sendVerificationMail(newUtilisateur, VerificationToken.VALIDATION_MAIL_TOKEN,true);
                        }else if (check == -1) {
                            System.out.println("L' email que vous venez d'entrer est dèja associé a un compte");
                            return false;
                        }else{
                            System.out.println("Une erreur est survenu! Veuillez resseyer plustard");
                            return false;
                        }
                        return true;
                    }
                    else{
                        System.out.println("Veuillez entrer un email valide");
                    }

                }else
                    System.out.println("Les mots de passe entrée ne sont pas identiques!");

        }
        return false;
    }

    public Utilisateur get(int id) {
        Utilisateur utilisateur = null;
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            utilisateur = session.get(Utilisateur.class, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return utilisateur;
    }

    public Utilisateur get(String mail) {
        Utilisateur utilisateur = null;
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Utilisateur  where email='" + mail + "'");
            utilisateur = (Utilisateur) query.uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return utilisateur;
    }

    /**
     *
     * @return true if the user is logged in, if not false.
     */
    public static boolean estConnecte() {
        return utilisateur!=null;
    }



    public static void refresh() {
        IUtilisateurHandler utilisateurHandler = new UtilisateurHandler();
        utilisateur = utilisateurHandler.get(utilisateur.getId());

    }
}
