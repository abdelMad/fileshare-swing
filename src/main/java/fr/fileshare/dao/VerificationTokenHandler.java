package fr.fileshare.dao;

import fr.fileshare.model.Utilisateur;
import fr.fileshare.model.VerificationToken;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class VerificationTokenHandler implements IVerificationTokenHandler {

    boolean add(VerificationToken verifToken) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        boolean check = false;
        try {
            session.beginTransaction();
            session.save(verifToken);
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

    boolean delete(VerificationToken verificationToken) {
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        boolean check = false;
        try {
            session.beginTransaction();
            VerificationToken verificationTokenToDelete = session.get(verificationToken.getClass(), verificationToken.getId());
            session.delete(verificationTokenToDelete);
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

    public VerificationToken get(String token) {
        VerificationToken verificationToken = null;
        Session session = SessionFactoryHelper.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from VerificationToken where token= :token");
            query.setString("token", token);
            verificationToken = (VerificationToken) query.uniqueResult();
            session.getTransaction().commit();

        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return verificationToken;
    }

    public VerificationToken generateUniqueVerificationToken(Utilisateur utilisateur, int verificationType) {
        UUID uuid = UUID.randomUUID();
        String hashedUUID = Util.hashString(uuid.toString());
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(hashedUUID);
        verificationToken.setType(verificationType);
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, VerificationToken.EXPIRATION);
        currentDate = c.getTime();
        verificationToken.setExpirationDate(currentDate);
        verificationToken.setUtilisateur(utilisateur);
        return verificationToken;
    }

    public boolean sendVerificationMail(Utilisateur utilisateur, int verificationType, boolean showAlert) {
        boolean check = false;
        VerificationToken verificationToken = generateUniqueVerificationToken(utilisateur, verificationType);
        if (verificationToken != null) {
            boolean checkAdd = add(verificationToken);
            if (checkAdd) {
                String subject = "", messageHeader = "", verificationUrl = "", highlitedText = "", normalText = "", linkText = "";
                String toEmail = utilisateur.getEmail();
                String host = Util.getProperty("host");
                if (verificationType == VerificationToken.VALIDATION_MAIL_TOKEN) {
                    messageHeader = "Merci pour votre inscription!";
                    verificationUrl = host + "/verification-email?token=" + verificationToken.getToken();
                    highlitedText = "On est heureux de vous avoir comme membre de File Share!";
                    normalText = "Merci de bien vouloir confirmer votre email";
                    linkText = "Confirmer";
                    subject = "Validation mail";
                } else if (verificationType == VerificationToken.RECOVERY_PWD_TOKEN) {
                    messageHeader = "Recuperation de mot de passe";
                    verificationUrl = host  + "/recuperation-mot-de-passe?token=" + verificationToken.getToken();
                    highlitedText = "Si vous n'avez pas demander une recuperation de mot de passe ignorer ce message.";
                    normalText = "Pour recuperer votre mot de passe veuillez cliquer sur le bouton ci-dessous.";
                    linkText = "Recuperer mon mot de passe";
                    subject = "Recuperation de mot de passe";
                }
                String body = "<!doctype html>\n" +
                        " <html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                        " <head>\n" +
                        "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                        "  <meta name=\"viewport\" content=\"initial-scale=1.0\" />\n" +
                        "  <meta name=\"format-detection\" content=\"telephone=no\" />\n" +
                        "  <title></title>\n" +
                        "  <style type=\"text/css\">\n" +
                        " \tbody {\n" +
                        "\t\twidth: 100%;\n" +
                        "\t\tmargin: 0;\n" +
                        "\t\tpadding: 0;\n" +
                        "\t\t-webkit-font-smoothing: antialiased;\n" +
                        "\t}\n" +
                        "\t@media only screen and (max-width: 600px) {\n" +
                        "\t\ttable[class=\"table-row\"] {\n" +
                        "\t\t\tfloat: none !important;\n" +
                        "\t\t\twidth: 98% !important;\n" +
                        "\t\t\tpadding-left: 20px !important;\n" +
                        "\t\t\tpadding-right: 20px !important;\n" +
                        "\t\t}\n" +
                        "\t\ttable[class=\"table-row-fixed\"] {\n" +
                        "\t\t\tfloat: none !important;\n" +
                        "\t\t\twidth: 98% !important;\n" +
                        "\t\t}\n" +
                        "\t\ttable[class=\"table-col\"], table[class=\"table-col-border\"] {\n" +
                        "\t\t\tfloat: none !important;\n" +
                        "\t\t\twidth: 100% !important;\n" +
                        "\t\t\tpadding-left: 0 !important;\n" +
                        "\t\t\tpadding-right: 0 !important;\n" +
                        "\t\t\ttable-layout: fixed;\n" +
                        "\t\t}\n" +
                        "\t\ttd[class=\"table-col-td\"] {\n" +
                        "\t\t\twidth: 100% !important;\n" +
                        "\t\t}\n" +
                        "\t\ttable[class=\"table-col-border\"] + table[class=\"table-col-border\"] {\n" +
                        "\t\t\tpadding-top: 12px;\n" +
                        "\t\t\tmargin-top: 12px;\n" +
                        "\t\t\tborder-top: 1px solid #E8E8E8;\n" +
                        "\t\t}\n" +
                        "\t\ttable[class=\"table-col\"] + table[class=\"table-col\"] {\n" +
                        "\t\t\tmargin-top: 15px;\n" +
                        "\t\t}\n" +
                        "\t\ttd[class=\"table-row-td\"] {\n" +
                        "\t\t\tpadding-left: 0 !important;\n" +
                        "\t\t\tpadding-right: 0 !important;\n" +
                        "\t\t}\n" +
                        "\t\ttable[class=\"navbar-row\"] , td[class=\"navbar-row-td\"] {\n" +
                        "\t\t\twidth: 100% !important;\n" +
                        "\t\t}\n" +
                        "\t\timg {\n" +
                        "\t\t\tmax-width: 100% !important;\n" +
                        "\t\t\tdisplay: inline !important;\n" +
                        "\t\t}\n" +
                        "\t\timg[class=\"pull-right\"] {\n" +
                        "\t\t\tfloat: right;\n" +
                        "\t\t\tmargin-left: 11px;\n" +
                        "            max-width: 125px !important;\n" +
                        "\t\t\tpadding-bottom: 0 !important;\n" +
                        "\t\t}\n" +
                        "\t\timg[class=\"pull-left\"] {\n" +
                        "\t\t\tfloat: left;\n" +
                        "\t\t\tmargin-right: 11px;\n" +
                        "\t\t\tmax-width: 125px !important;\n" +
                        "\t\t\tpadding-bottom: 0 !important;\n" +
                        "\t\t}\n" +
                        "\t\ttable[class=\"table-space\"], table[class=\"header-row\"] {\n" +
                        "\t\t\tfloat: none !important;\n" +
                        "\t\t\twidth: 98% !important;\n" +
                        "\t\t}\n" +
                        "\t\ttd[class=\"header-row-td\"] {\n" +
                        "\t\t\twidth: 100% !important;\n" +
                        "\t\t}\n" +
                        "\t}\n" +
                        "\t@media only screen and (max-width: 480px) {\n" +
                        "\t\ttable[class=\"table-row\"] {\n" +
                        "\t\t\tpadding-left: 16px !important;\n" +
                        "\t\t\tpadding-right: 16px !important;\n" +
                        "\t\t}\n" +
                        "\t}\n" +
                        "\t@media only screen and (max-width: 320px) {\n" +
                        "\t\ttable[class=\"table-row\"] {\n" +
                        "\t\t\tpadding-left: 12px !important;\n" +
                        "\t\t\tpadding-right: 12px !important;\n" +
                        "\t\t}\n" +
                        "\t}\n" +
                        "\t@media only screen and (max-width: 458px) {\n" +
                        "\t\ttd[class=\"table-td-wrap\"] {\n" +
                        "\t\t\twidth: 100% !important;\n" +
                        "\t\t}\n" +
                        "\t}\n" +
                        "  </style>\n" +
                        " </head>\n" +
                        " <body style=\"font-family: Arial, sans-serif; font-size:13px; color: #444444; min-height: 200px;\" bgcolor=\"#E4E6E9\" leftmargin=\"0\" topmargin=\"0\" marginheight=\"0\" marginwidth=\"0\">\n" +
                        " <table width=\"100%\" height=\"100%\" bgcolor=\"#E4E6E9\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\n" +
                        " <tr><td width=\"100%\" align=\"center\" valign=\"top\" bgcolor=\"#E4E6E9\" style=\"background-color:#E4E6E9; min-height: 200px;\">\n" +
                        "<table><tr><td class=\"table-td-wrap\" align=\"center\" width=\"458\"><table class=\"table-space\" height=\"18\" style=\"height: 18px; font-size: 0px; line-height: 0; width: 450px; background-color: #e4e6e9;\" width=\"450\" bgcolor=\"#E4E6E9\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-space-td\" valign=\"middle\" height=\"18\" style=\"height: 18px; width: 450px; background-color: #e4e6e9;\" width=\"450\" bgcolor=\"#E4E6E9\" align=\"left\">&nbsp;</td></tr></tbody></table>\n" +
                        "<table class=\"table-space\" height=\"8\" style=\"height: 8px; font-size: 0px; line-height: 0; width: 450px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-space-td\" valign=\"middle\" height=\"8\" style=\"height: 8px; width: 450px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" align=\"left\">&nbsp;</td></tr></tbody></table>\n" +
                        "\n" +
                        "<table class=\"table-row\" width=\"450\" bgcolor=\"#FFFFFF\" style=\"table-layout: fixed; background-color: #ffffff;\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-row-td\" style=\"font-family: Arial, sans-serif; line-height: 19px; color: #444444; font-size: 13px; font-weight: normal; padding-left: 36px; padding-right: 36px;\" valign=\"top\" align=\"left\">\n" +
                        "  <table class=\"table-col\" align=\"left\" width=\"378\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"table-layout: fixed;\"><tbody><tr><td class=\"table-col-td\" width=\"378\" style=\"font-family: Arial, sans-serif; line-height: 19px; color: #444444; font-size: 13px; font-weight: normal; width: 378px;\" valign=\"top\" align=\"left\">\n" +
                        "    <table class=\"header-row\" width=\"378\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"table-layout: fixed;\"><tbody><tr><td class=\"header-row-td\" width=\"378\" style=\"font-family: Arial, sans-serif; font-weight: normal; line-height: 19px; color: #478fca; margin: 0px; font-size: 18px; padding-bottom: 10px; padding-top: 15px;\" valign=\"top\" align=\"left\">" + messageHeader + "</td></tr></tbody></table>\n" +
                        "    <div style=\"font-family: Arial, sans-serif; line-height: 20px; color: #444444; font-size: 13px;\">\n" +
                        "      <b style=\"color: #777777;\">" + highlitedText + "</b>\n" +
                        "      <br>\n" +
                        "      " + normalText + "\n" +
                        "    </div>\n" +
                        "  </td></tr></tbody></table>\n" +
                        "</td></tr></tbody></table>\n" +
                        "    \n" +
                        "<table class=\"table-space\" height=\"12\" style=\"height: 12px; font-size: 0px; line-height: 0; width: 450px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-space-td\" valign=\"middle\" height=\"12\" style=\"height: 12px; width: 450px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" align=\"left\">&nbsp;</td></tr></tbody></table>\n" +
                        "<table class=\"table-space\" height=\"12\" style=\"height: 12px; font-size: 0px; line-height: 0; width: 450px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-space-td\" valign=\"middle\" height=\"12\" style=\"height: 12px; width: 450px; padding-left: 16px; padding-right: 16px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" align=\"center\">&nbsp;<table bgcolor=\"#E8E8E8\" height=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td bgcolor=\"#E8E8E8\" height=\"1\" width=\"100%\" style=\"height: 1px; font-size:0;\" valign=\"top\" align=\"left\">&nbsp;</td></tr></tbody></table></td></tr></tbody></table>\n" +
                        "<table class=\"table-space\" height=\"16\" style=\"height: 16px; font-size: 0px; line-height: 0; width: 450px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-space-td\" valign=\"middle\" height=\"16\" style=\"height: 16px; width: 450px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" align=\"left\">&nbsp;</td></tr></tbody></table>\n" +
                        "\n" +
                        "<table class=\"table-row\" width=\"450\" bgcolor=\"#FFFFFF\" style=\"table-layout: fixed; background-color: #ffffff;\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-row-td\" style=\"font-family: Arial, sans-serif; line-height: 19px; color: #444444; font-size: 13px; font-weight: normal; padding-left: 36px; padding-right: 36px;\" valign=\"top\" align=\"left\">\n" +
                        "  <table class=\"table-col\" align=\"left\" width=\"378\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"table-layout: fixed;\"><tbody><tr><td class=\"table-col-td\" width=\"378\" style=\"font-family: Arial, sans-serif; line-height: 19px; color: #444444; font-size: 13px; font-weight: normal; width: 378px;\" valign=\"top\" align=\"left\">\n" +
                        "    <div style=\"font-family: Arial, sans-serif; line-height: 19px; color: #444444; font-size: 13px; text-align: center;\">\n" +
                        "      <a href='" + verificationUrl + "' style=\"color: #ffffff; text-decoration: none; margin: 0px; text-align: center; vertical-align: baseline; border: 4px solid #6fb3e0; padding: 4px 9px; font-size: 15px; line-height: 21px; background-color: #6fb3e0;\">&nbsp; " + linkText + " &nbsp;</a>\n" +
                        "    </div>\n" +
                        "    <table class=\"table-space\" height=\"16\" style=\"height: 16px; font-size: 0px; line-height: 0; width: 378px; background-color: #ffffff;\" width=\"378\" bgcolor=\"#FFFFFF\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-space-td\" valign=\"middle\" height=\"16\" style=\"height: 16px; width: 378px; background-color: #ffffff;\" width=\"378\" bgcolor=\"#FFFFFF\" align=\"left\">&nbsp;</td></tr></tbody></table>\n" +
                        "  </td></tr></tbody></table>\n" +
                        "</td></tr></tbody></table>\n" +
                        "\n" +
                        "<table class=\"table-space\" height=\"6\" style=\"height: 6px; font-size: 0px; line-height: 0; width: 450px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-space-td\" valign=\"middle\" height=\"6\" style=\"height: 6px; width: 450px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" align=\"left\">&nbsp;</td></tr></tbody></table>\n" +
                        "\n" +
                        "<table class=\"table-row-fixed\" width=\"450\" bgcolor=\"#FFFFFF\" style=\"table-layout: fixed; background-color: #ffffff;\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-row-fixed-td\" style=\"font-family: Arial, sans-serif; line-height: 19px; color: #444444; font-size: 13px; font-weight: normal; padding-left: 1px; padding-right: 1px;\" valign=\"top\" align=\"left\">\n" +
                        "  <table class=\"table-col\" align=\"left\" width=\"448\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"table-layout: fixed;\"><tbody><tr><td class=\"table-col-td\" width=\"448\" style=\"font-family: Arial, sans-serif; line-height: 19px; color: #444444; font-size: 13px; font-weight: normal;\" valign=\"top\" align=\"left\">\n" +
                        "    <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"table-layout: fixed;\"><tbody><tr><td width=\"100%\" align=\"center\" bgcolor=\"#f5f5f5\" style=\"font-family: Arial, sans-serif; line-height: 24px; color: #bbbbbb; font-size: 13px; font-weight: normal; text-align: center; padding: 9px; border-width: 1px 0px 0px; border-style: solid; border-color: #e3e3e3; background-color: #f5f5f5;\" valign=\"top\">\n" +
                        "      <a href=\"#\" style=\"color: #428bca; text-decoration: none; background-color: transparent;\">File Share &copy; 2018</a>\n" +
                        "    </td></tr></tbody></table>\n" +
                        "  </td></tr></tbody></table>\n" +
                        "</td></tr></tbody></table>\n" +
                        "<table class=\"table-space\" height=\"1\" style=\"height: 1px; font-size: 0px; line-height: 0; width: 450px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-space-td\" valign=\"middle\" height=\"1\" style=\"height: 1px; width: 450px; background-color: #ffffff;\" width=\"450\" bgcolor=\"#FFFFFF\" align=\"left\">&nbsp;</td></tr></tbody></table>\n" +
                        "<table class=\"table-space\" height=\"36\" style=\"height: 36px; font-size: 0px; line-height: 0; width: 450px; background-color: #e4e6e9;\" width=\"450\" bgcolor=\"#E4E6E9\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody><tr><td class=\"table-space-td\" valign=\"middle\" height=\"36\" style=\"height: 36px; width: 450px; background-color: #e4e6e9;\" width=\"450\" bgcolor=\"#E4E6E9\" align=\"left\">&nbsp;</td></tr></tbody></table></td></tr></table>\n" +
                        "</td></tr>\n" +
                        " </table>\n" +
                        " </body>\n" +
                        " </html>";
                check = Util.sendEmail(toEmail, subject, body);
                if (check) {
                    if(showAlert) {
                        if (verificationType == VerificationToken.VALIDATION_MAIL_TOKEN)
                            System.out.println("Un email de validation vient d'être envoyé");
                        else if (verificationType == VerificationToken.RECOVERY_PWD_TOKEN)
                            System.out.println( "Un email de recuperation de mot de passe vient d'être envoyé");
                    }
                }

            }
        }
        return check;
    }

    public int validateMail(String token) {
        int check = -1;
        Utilisateur utilisateur = UtilisateurHandler.utilisateur;

        if(!utilisateur.isEmailChecked()) {
            VerificationToken verificationToken;
                if (token.length() != 0) {
                    verificationToken = get(token);
                    if (verificationToken != null) {
                        Date currentDate = new Date();
                        if (currentDate.compareTo(verificationToken.getExpirationDate()) < 0) {
                            IUtilisateurHandler userHandler = new UtilisateurHandler();
                            utilisateur = verificationToken.getUtilisateur();
                            utilisateur.setEmailChecked(true);
                            boolean check1 = userHandler.update(utilisateur);
                            UtilisateurHandler.utilisateur = utilisateur;
                            boolean check2 = delete(verificationToken);
                            if(check1 && check2)
                            check = 1;
                        }
                    }
                }
        }else
            check = 0;
        return check;
    }

    public boolean recoverPassword(String token) {
        if (token.length() == 0) return false;
        VerificationToken verificationToken = get(token);
        if (verificationToken != null) {
            Date currentDate = new Date();
            if (currentDate.compareTo(verificationToken.getExpirationDate()) < 0) {
                return true;
            }
        }
        return false;
    }

    public Utilisateur getUtilisateur(String token){
        if (token.length() == 0) return null;
        VerificationToken verificationToken = get(token);
        if (verificationToken != null) {
            Date currentDate = new Date();
            if (currentDate.compareTo(verificationToken.getExpirationDate()) < 0) {
                Utilisateur utilisateur = verificationToken.getUtilisateur();
                delete(verificationToken);
                return utilisateur;
            }
        }
        return null;
    }
}
