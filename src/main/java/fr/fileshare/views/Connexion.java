/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.fileshare.views;

import fr.fileshare.dao.IUtilisateurHandler;
import fr.fileshare.dao.UtilisateurHandler;
import fr.fileshare.utilities.Util;

/**
 *
 * @author abdelmoghitmadih
 */
public class Connexion extends javax.swing.JFrame {

    /**
     * Creates new form Connexion
     */
    public Connexion() {
        initComponents();
        String utilisateur = Util.getProperty("uilisateur");
        String mdp = Util.getProperty("mdp");
        if(!mdp.equals("x") && !utilisateur.equals("x")){
            emailTxt.setText(utilisateur);
            mdpTxt.setText(mdp);
            chbxRememberMe.setSelected(true);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        emailTxt = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        mdpTxt = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnConnexion = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        chbxRememberMe = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        emailTxtInscription = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        mdpInscriptionTxt = new javax.swing.JPasswordField();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        nomTxt = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        PrenomTxt = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();
        verificationMdpTxt = new javax.swing.JPasswordField();
        jLabel10 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        btnInscription = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jPanel1.setPreferredSize(new java.awt.Dimension(700, 410));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(67, 142, 185));
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Mot de passe oublié?");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 360, -1, -1));

        emailTxt.setBackground(new java.awt.Color(67, 142, 185));
        emailTxt.setForeground(new java.awt.Color(255, 255, 255));
        emailTxt.setBorder(null);
        emailTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTxtActionPerformed(evt);
            }
        });
        jPanel3.add(emailTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, 240, 20));
        jPanel3.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 140, 250, 10));
        jPanel3.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 270, 250, 10));

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Email");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, -1, -1));

        mdpTxt.setBackground(new java.awt.Color(67, 142, 185));
        mdpTxt.setForeground(new java.awt.Color(255, 255, 255));
        mdpTxt.setBorder(null);
        jPanel3.add(mdpTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 250, 240, 20));

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Mot de passe");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 210, -1, -1));

        jPanel2.setBackground(new java.awt.Color(111, 179, 224));

        btnConnexion.setBackground(new java.awt.Color(111, 179, 224));
        btnConnexion.setForeground(new java.awt.Color(255, 255, 255));
        btnConnexion.setText("Connexion");
        btnConnexion.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnConnexion.setBorderPainted(false);
        btnConnexion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConnexion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConnexionMouseClicked(evt);
            }
        });
        btnConnexion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnexionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(btnConnexion, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(btnConnexion, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 420, 270, 40));

        jLabel4.setBackground(new java.awt.Color(67, 142, 185));
        jLabel4.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Dèja Membre ?");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 190, -1));

        chbxRememberMe.setForeground(new java.awt.Color(255, 255, 255));
        chbxRememberMe.setText("Se rappeler de moi");
        chbxRememberMe.setContentAreaFilled(false);
        jPanel3.add(chbxRememberMe, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 310, -1, -1));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 0, 420, 560));

        jLabel5.setBackground(new java.awt.Color(67, 142, 185));
        jLabel5.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(67, 142, 185));
        jLabel5.setText("Nouveau Membre?");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 197, -1));

        emailTxtInscription.setForeground(new java.awt.Color(67, 142, 185));
        emailTxtInscription.setBorder(null);
        jPanel1.add(emailTxtInscription, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, 240, 20));
        jPanel1.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, 250, 10));

        jLabel6.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(67, 142, 185));
        jLabel6.setText("Email");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, -1, 20));

        jLabel7.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(67, 142, 185));
        jLabel7.setText("Mot de passe");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 290, -1, -1));

        mdpInscriptionTxt.setForeground(new java.awt.Color(67, 142, 185));
        mdpInscriptionTxt.setBorder(null);
        jPanel1.add(mdpInscriptionTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 320, 240, 20));
        jPanel1.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, 250, 10));

        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(67, 142, 185));
        jLabel8.setText("Nom");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, -1, 20));

        nomTxt.setForeground(new java.awt.Color(67, 142, 185));
        nomTxt.setBorder(null);
        jPanel1.add(nomTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 170, 240, 20));
        jPanel1.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 190, 250, 10));

        jLabel9.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(67, 142, 185));
        jLabel9.setText("Prenom");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 210, -1, 20));

        PrenomTxt.setForeground(new java.awt.Color(67, 142, 185));
        PrenomTxt.setBorder(null);
        jPanel1.add(PrenomTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, 240, 20));
        jPanel1.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 260, 250, 10));

        verificationMdpTxt.setForeground(new java.awt.Color(67, 142, 185));
        verificationMdpTxt.setBorder(null);
        jPanel1.add(verificationMdpTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 400, 240, 20));

        jLabel10.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(67, 142, 185));
        jLabel10.setText("Verification mot de passe");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 370, -1, -1));
        jPanel1.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 420, 250, 10));

        jPanel4.setBackground(new java.awt.Color(67, 142, 185));

        btnInscription.setBackground(new java.awt.Color(111, 179, 224));
        btnInscription.setForeground(new java.awt.Color(255, 255, 255));
        btnInscription.setText("Inscription");
        btnInscription.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnInscription.setBorderPainted(false);
        btnInscription.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnInscription.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnInscriptionMouseClicked(evt);
            }
        });
        btnInscription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInscriptionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(btnInscription, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(btnInscription, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 470, 270, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setSize(new java.awt.Dimension(782, 582));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnConnexionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConnexionMouseClicked
            
    }//GEN-LAST:event_btnConnexionMouseClicked

    private void btnInscriptionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnInscriptionMouseClicked
        String email = emailTxtInscription.getText().trim();
        String nom = nomTxt.getText().trim();
        String Prenom = PrenomTxt.getText().trim();
        String mdp = mdpInscriptionTxt.getText().trim();
        String verifMdp = verificationMdpTxt.getText().trim();
        if(email.length()!=0 && mdp.length()!=0 && Prenom.length()!=0 && nom.length()!=0 && verifMdp.length()!=0 ){
            IUtilisateurHandler utilisateurHandler = new UtilisateurHandler();
            if(utilisateurHandler.register(nom, Prenom, email, mdp, verifMdp))
                 new Dashboard(this);
            
        }
    }//GEN-LAST:event_btnInscriptionMouseClicked

    private void btnInscriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInscriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnInscriptionActionPerformed

    private void emailTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailTxtActionPerformed

    private void btnConnexionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnexionActionPerformed
        String email = emailTxt.getText().trim();
        String mdp = mdpTxt.getText().trim();
        if(email.length()!=0 && mdp.length()!=0 ){
            IUtilisateurHandler utilisateurHandler = new UtilisateurHandler();
            if(utilisateurHandler.authenticate(email, mdp)){
                System.out.println("Im logging in ...");
            emailTxt.setText("");
            mdpTxt.setText("");
             new Dashboard(this);
            }
        }
    }//GEN-LAST:event_btnConnexionActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField PrenomTxt;
    private javax.swing.JButton btnConnexion;
    private javax.swing.JButton btnInscription;
    private javax.swing.JCheckBox chbxRememberMe;
    private javax.swing.JTextField emailTxt;
    private javax.swing.JTextField emailTxtInscription;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JPasswordField mdpInscriptionTxt;
    private javax.swing.JPasswordField mdpTxt;
    private javax.swing.JTextField nomTxt;
    private javax.swing.JPasswordField verificationMdpTxt;
    // End of variables declaration//GEN-END:variables
}
