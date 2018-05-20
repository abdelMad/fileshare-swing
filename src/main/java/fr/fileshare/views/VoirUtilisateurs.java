/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.fileshare.views;

import fr.fileshare.dao.IMessageHandler;
import fr.fileshare.dao.IUtilisateurHandler;
import fr.fileshare.dao.MessageHandler;
import fr.fileshare.dao.UtilisateurHandler;
import fr.fileshare.model.Message;
import fr.fileshare.model.Utilisateur;
import fr.fileshare.utilities.JsonHelper;
import fr.fileshare.websocket.ClientEndPointX;
import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * Interface de visualisation des utilisateurs
 */
public class VoirUtilisateurs extends javax.swing.JFrame {

    /**
     * Creates new form VoirUtilisateurs
     */
    //private javax.swing.JButton btnEnvoyerMsg;
    //private javax.swing.JComboBox<String> cmbNomUtilisateurs;
    private javax.swing.JComboBox<Utilisateur> cmbUtilisateurs;

    private javax.swing.JLabel lblVoirUtilisateur;
    Dashboard d;

    public VoirUtilisateurs(String partageAvec, Dashboard d) {
        initComponents();
        this.d = d;
        cmbUtilisateurs = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
       
        DefaultListModel  defaultListModel = new DefaultListModel<>();
        
        
        String[] idUtilisateurs = partageAvec.split("|");
        IUtilisateurHandler utilisateurHandler = new UtilisateurHandler();

        for (int i = 0; i < idUtilisateurs.length; i++) {
            int idUtilisateur = Integer.parseInt(idUtilisateurs[i]);
            Utilisateur u = utilisateurHandler.get(idUtilisateur);
            this.cmbUtilisateurs.addItem(u);
            defaultListModel.add(i,u.getPrenom() + " " + u.getNom());
        }
        this.cmbNomUtilisateurs.setModel(defaultListModel);
        btnEnvoyerMsg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnvoyerMsgActionPerformed(evt);
            }
        });
    }

    private void btnEnvoyerMsgActionPerformed(java.awt.event.ActionEvent evt) {
        URI uri = null;
        try {
            uri = new URI("ws://localhost:8085/chat/" + UtilisateurHandler.utilisateur.getId() + "/-1");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            if (d.docWS != null) {
                d.docWS.close();
            }
            if (d.chatGrpWS != null) {
                d.chatGrpWS.close();
            }
            d.chatWS = new ClientEndPointX(uri);
            d.chatWS.addMessageHandler(responseString -> {
                try {
                    JsonHelper helper = new JsonHelper();
                    Map<String, String> msg = helper.decodeMessage(responseString);
                    String conversation = VoirUtilisateurs.this.d.conversationMsg.getText();
                    conversation += "\n" + msg.get("sender") + " : " + msg.get("message") + "\n";
                    VoirUtilisateurs.this.d.conversationMsg.setText(conversation);
                } catch (Exception e) {

                }

                //System.out.println(responseString);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        int index = cmbNomUtilisateurs.getSelectedIndex();
        Utilisateur utilisateur = (Utilisateur) cmbUtilisateurs.getItemAt(index);
        IMessageHandler messageHandler = new MessageHandler();
        if (messageHandler.checkNouveauContact(UtilisateurHandler.utilisateur.getId(), utilisateur.getId())) {
            JButton jbtn = new JButton(utilisateur.getNom());
            JPanel jpContactes = new JPanel();
            GridBagConstraints c = new GridBagConstraints();

            jbtn.setActionCommand(Integer.toString(utilisateur.getId()));
            jbtn.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    __loadMessages(Integer.parseInt(((JButton) e.getComponent()).getActionCommand()));
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });
            jpContactes.add(jbtn, c);
            d.scrollPcontactes.add(jpContactes);
        }
        __loadMessages(utilisateur.getId());
        d.tabPaneMain.setSelectedIndex(3);
        this.dispose();
    }

    void __loadMessages(int idSender) {
        IMessageHandler messageHandler = new MessageHandler();
        List<Message> messages = messageHandler.getMessages(UtilisateurHandler.utilisateur.getId(), idSender, 0, 300);
        VoirUtilisateurs.this.d.idSender = idSender;
        String conversation = "";
        for (int j = messages.size() - 1; j >= 0; j--) {
            String sender = "moi";
            Message msg = messages.get(j);
            if (!msg.getEmetteur().equals(UtilisateurHandler.utilisateur)) {
                sender = msg.getEmetteur().getNom();
            }
            conversation += sender + " : " + msg.getText() + "\n";
        }
        VoirUtilisateurs.this.d.conversationMsg.setText(conversation);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnEnvoyerMsg = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        cmbNomUtilisateurs = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnEnvoyerMsg.setText("Envoyer un message");
        getContentPane().add(btnEnvoyerMsg, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 250, 150, 40));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel1.setText("Liste des utilisateurs");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, -1, -1));

        cmbNomUtilisateurs.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(cmbNomUtilisateurs);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 250, 170));

        setSize(new java.awt.Dimension(400, 322));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEnvoyerMsg;
    private javax.swing.JList<String> cmbNomUtilisateurs;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
