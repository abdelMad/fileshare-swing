/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.fileshare.views;

import fr.fileshare.dao.UtilisateurHandler;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author abdelmoghitmadih
 */
public class Dashboard extends javax.swing.JFrame {
    
    private JFrame connexion;
    /**
     * Creates new form Dashboard
     */
    public Dashboard() {
        initComponents();
    }
    public Dashboard(JFrame connexion){
        this.connexion = connexion;
        this.connexion.setVisible(false);
        initComponents();
        this.setVisible(true);
        System.out.println("Im here");
        this.lblNomUtilisateur.setText(UtilisateurHandler.utilisateur.getNom().toUpperCase());
        this.lblUtilisateurImage.setIcon(new ImageIcon(getClass().getResource("/images/people.png")));
        this.lblAccueilIcone.setIcon(new ImageIcon(getClass().getResource("/images/home.png")));

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        container = new javax.swing.JPanel();
        nav = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lblNomUtilisateur = new javax.swing.JLabel();
        lblUtilisateurImage = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        sideBar = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        lblAccueilIcone = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        lblMesDocIcone = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        lblDocsPartIcone = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        nouveauDocIcone = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jPanel6 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        lblMessagesIcone = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jPanel7 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        lblDecoIcone = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        main = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        container.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        nav.setBackground(new java.awt.Color(67, 142, 185));
        nav.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(98, 168, 209));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblNomUtilisateur.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        lblNomUtilisateur.setForeground(new java.awt.Color(255, 255, 255));
        lblNomUtilisateur.setToolTipText("");
        jPanel2.add(lblNomUtilisateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, -1, 20));
        jPanel2.add(lblUtilisateurImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 10, 60, 50));

        jLabel10.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Bienvenu,");
        jLabel10.setToolTipText("");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 20, -1, 20));

        nav.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 0, 210, 70));

        jLabel9.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("File Share");
        nav.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        container.add(nav, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 970, 70));

        sideBar.setBackground(new java.awt.Color(242, 242, 242));
        sideBar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(229, 229, 229));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(88, 88, 88));
        jButton1.setText("Accueil");
        jButton1.setBorderPainted(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 40));
        jPanel1.add(lblAccueilIcone, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 30, 40));

        sideBar.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 250, 40));

        jSeparator3.setBackground(new java.awt.Color(229, 229, 229));
        sideBar.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 45, 250, 10));

        jPanel3.setBackground(new java.awt.Color(229, 229, 229));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton3.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jButton3.setForeground(new java.awt.Color(88, 88, 88));
        jButton3.setText("   Mes Documents");
        jButton3.setBorderPainted(false);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel3.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 40));
        jPanel3.add(lblMesDocIcone, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 30, 40));

        sideBar.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 53, 250, 40));

        jSeparator4.setBackground(new java.awt.Color(229, 229, 229));
        sideBar.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 88, 240, 10));

        jPanel4.setBackground(new java.awt.Color(229, 229, 229));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton4.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jButton4.setForeground(new java.awt.Color(88, 88, 88));
        jButton4.setText("         Documents Partagés");
        jButton4.setBorderPainted(false);
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 40));
        jPanel4.add(lblDocsPartIcone, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 30, 40));

        sideBar.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 96, 230, 40));

        jSeparator5.setBackground(new java.awt.Color(229, 229, 229));
        sideBar.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 130, 250, 10));

        jPanel5.setBackground(new java.awt.Color(229, 229, 229));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton5.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jButton5.setForeground(new java.awt.Color(88, 88, 88));
        jButton5.setText("              Nouveau Document");
        jButton5.setBorderPainted(false);
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel5.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 40));
        jPanel5.add(nouveauDocIcone, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 30, 40));

        sideBar.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 138, -1, 40));

        jSeparator6.setBackground(new java.awt.Color(229, 229, 229));
        sideBar.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 173, 240, 10));

        jPanel6.setBackground(new java.awt.Color(229, 229, 229));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton6.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jButton6.setForeground(new java.awt.Color(88, 88, 88));
        jButton6.setText("Messages");
        jButton6.setBorderPainted(false);
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel6.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 40));
        jPanel6.add(lblMessagesIcone, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 30, 40));

        sideBar.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 181, 230, 40));

        jSeparator7.setBackground(new java.awt.Color(229, 229, 229));
        jSeparator7.setForeground(new java.awt.Color(229, 229, 229));
        sideBar.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 216, 240, 10));

        jPanel7.setBackground(new java.awt.Color(229, 229, 229));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton7.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jButton7.setForeground(new java.awt.Color(88, 88, 88));
        jButton7.setText("    Deconnexion");
        jButton7.setToolTipText("");
        jButton7.setBorderPainted(false);
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 40));
        jPanel7.add(lblDecoIcone, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 30, 40));

        sideBar.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 224, 230, 40));

        jSeparator8.setBackground(new java.awt.Color(229, 229, 229));
        sideBar.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 258, 240, 10));

        container.add(sideBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 230, 670));

        main.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout mainLayout = new javax.swing.GroupLayout(main);
        main.setLayout(mainLayout);
        mainLayout.setHorizontalGroup(
            mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 770, Short.MAX_VALUE)
        );
        mainLayout.setVerticalGroup(
            mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        container.add(main, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 70, 770, 670));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(container, javax.swing.GroupLayout.DEFAULT_SIZE, 973, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        UtilisateurHandler.utilisateur = null;
        this.setVisible(false);
        connexion.setVisible(true);
        this.dispose();
            
    }//GEN-LAST:event_jButton7ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        UtilisateurHandler.utilisateur = null;
        connexion.setVisible(true);
    }//GEN-LAST:event_formWindowClosed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel container;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JLabel lblAccueilIcone;
    private javax.swing.JLabel lblDecoIcone;
    private javax.swing.JLabel lblDocsPartIcone;
    private javax.swing.JLabel lblMesDocIcone;
    private javax.swing.JLabel lblMessagesIcone;
    private javax.swing.JLabel lblNomUtilisateur;
    private javax.swing.JLabel lblUtilisateurImage;
    private javax.swing.JPanel main;
    private javax.swing.JPanel nav;
    private javax.swing.JLabel nouveauDocIcone;
    private javax.swing.JPanel sideBar;
    // End of variables declaration//GEN-END:variables
}