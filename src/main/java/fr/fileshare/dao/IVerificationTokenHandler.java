package fr.fileshare.dao;

import fr.fileshare.model.Utilisateur;


public interface IVerificationTokenHandler {
    /**
     * send a verification email to the utilisateur so he can validate his email or recover his maile
     *
     * @param utilisateur the recipient
     * @param verificationType verification type  VALIDATION_MAIL_TOKEN  or RECOVERY_PWD_TOKEN
     * @param showAlert
     * @return return true if the message is sent, if not false
     */
    boolean sendVerificationMail(Utilisateur utilisateur, int verificationType, boolean showAlert);

    /**
     * after the click on the validation link this function validate the email
     *
     * @param token
     * @return 1:if the validation process is done, -1:if not  0:if the user is already verified
     */
    int validateMail(String token);

    /**
     * after the click on the recover password link this function give the access to recover password
     *
     * @param token the token
     * @return true if the recover process is done, if not false
     */
    boolean recoverPassword(String token);

    Utilisateur getUtilisateur(String token);

}