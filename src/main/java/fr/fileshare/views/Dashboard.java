/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.fileshare.views;

import fr.fileshare.controllers.DashboardController;
import fr.fileshare.dao.DocumentHandler;
import fr.fileshare.dao.HistoriqueHandler;
import fr.fileshare.dao.IDocumentHandler;
import fr.fileshare.dao.IHistoriqueHandler;
import fr.fileshare.dao.IMessageHandler;
import fr.fileshare.dao.IUtilisateurHandler;
import fr.fileshare.dao.MessageHandler;
import fr.fileshare.utilities.Util;
import fr.fileshare.dao.UtilisateurHandler;
import fr.fileshare.model.Document;
import fr.fileshare.model.Historique;
import fr.fileshare.model.Message;
import fr.fileshare.model.Utilisateur;
import fr.fileshare.utilities.JsonHelper;
import fr.fileshare.websocket.ClientEndPointX;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import org.json.JSONArray;

/**
 *
 * @author abdelmoghitmadih
 */
public class Dashboard extends javax.swing.JFrame {

    private JFrame connexion;
    DashboardController dashboardController = new DashboardController();
    int idSender = -1;
    int idU;
    ClientEndPointX chatWS;
    ClientEndPointX chatGrpWS;
    ClientEndPointX docWS;
    int idSelectedDoc = -1;

    protected UndoableEditListener undoHandler = new UndoHandler();
    protected UndoManager undo = new UndoManager();
    private UndoAction undoAction = new UndoAction();
    private RedoAction redoAction = new RedoAction();
    private Action boldAction = new StyledEditorKit.BoldAction();
    private Action underlineAction = new StyledEditorKit.UnderlineAction();
    private Action italicAction = new StyledEditorKit.ItalicAction();
    private Color currentColor;
    String[] fontTypes = {"", "Arial", "Courier", "Comic Sans MS", "Helvetica", "Open Sans", "Tahoma", "Verdana"};
    Integer[] tailles = {-1, 1, 3, 5};
    JButton declancheAction = new JButton();
    int keyPressed = -1;
    private HTMLDocument document;

    /**
     * Creates new form Dashboard
     */
    public Dashboard() {
        initComponents();
    }

    public Dashboard(JFrame connexion) {
        this.connexion = connexion;
        this.connexion.setVisible(false);
        initComponents();
        this.setVisible(true);
        this.icone1.setIcon(new ImageIcon(getClass().getResource("/images/mesdocuments.png")));
        this.icone2.setIcon(new ImageIcon(getClass().getResource("/images/favoris.png")));
        this.icone3.setIcon(new ImageIcon(getClass().getResource("/images/historique.png")));
        this.icone4.setIcon(new ImageIcon(getClass().getResource("/images/nouveau.png")));
        this.icone5.setIcon(new ImageIcon(getClass().getResource("/images/message.png")));
        this.icone6.setIcon(new ImageIcon(getClass().getResource("/images/deconnexion.png")));
        this.lblNomUtilisateur.setText(UtilisateurHandler.utilisateur.getNom().toUpperCase());
        System.out.println(UtilisateurHandler.utilisateur.getImage());
        if (UtilisateurHandler.utilisateur.getImage() == null || UtilisateurHandler.utilisateur.getImage().length() == 0) {
            this.lblUtilisateurImage.setIcon(new ImageIcon(getClass().getResource("/images/people2.png")));
        } else {
            try {

                URL url = new URL(Util.getProperty("host") + UtilisateurHandler.utilisateur.getImage());
                Image image = ImageIO.read(url);
                BufferedImage resizedImg = new BufferedImage(90, 90, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = resizedImg.createGraphics();

                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(image, 0, 0, 90, 90, null);
                g2.dispose();
                this.lblUtilisateurImage.setIcon(new ImageIcon(resizedImg));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        loadMesDocuments();
        pnlPartage.setVisible(false);
        final boolean showTabsHeader = false;

        tabPaneMain.setUI(new javax.swing.plaf.metal.MetalTabbedPaneUI() {
            @Override
            protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount, int maxTabHeight) {
                if (showTabsHeader) {
                    return super.calculateTabAreaHeight(tabPlacement, horizRunCount, maxTabHeight);
                } else {
                    return 0;
                }
            }

            protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
            }
        });

        declancheAction.setAction(new StyledEditorKit.FontFamilyAction(fontTypes[0], fontTypes[0]));
        declancheAction.doClick();
        jlblColorViewer.setIcon(jlabelIcon(Color.BLACK));
        HTMLEditorKit editorKit = new HTMLEditorKit();
        document = (HTMLDocument) editorKit.createDefaultDocument();
        docTxt.setDocument(document);
        document.addUndoableEditListener(undoHandler);

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
        icone1 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        icone2 = new javax.swing.JLabel();
        lblMesDocIcone = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        icone3 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        icone4 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        jPanel6 = new javax.swing.JPanel();
        icone5 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JSeparator();
        jPanel7 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        icone6 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        icone8 = new javax.swing.JLabel();
        lblAccueilIcone3 = new javax.swing.JLabel();
        icone_message1 = new javax.swing.JLabel();
        icone9 = new javax.swing.JLabel();
        main = new javax.swing.JPanel();
        tabPaneMain = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        MesDocuments = new javax.swing.JTable();
        btnVoirmesDocs = new javax.swing.JButton();
        btnTelechargerDoc = new javax.swing.JButton();
        btnModifierMesDoc = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        btnVoirmesDocs1 = new javax.swing.JButton();
        btnVoirUtilisateurs = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        Historique = new javax.swing.JTable();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        HistoriqueDoc = new javax.swing.JTable();
        jButton24 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        scrollPcontactes = new java.awt.ScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        msgTxt = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        conversationMsg = new javax.swing.JTextArea();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        convGrp = new javax.swing.JTextArea();
        jScrollPane8 = new javax.swing.JScrollPane();
        msgGrpTxt = new javax.swing.JTextArea();
        jButton8 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lblTitreDoc = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        docTxt = new javax.swing.JEditorPane();
        jScrollPane11 = new javax.swing.JScrollPane();
        jPanel17 = new javax.swing.JPanel();
        jLblInfoNbEditeurs = new javax.swing.JLabel();
        jLblEdtiteurs = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        btnGras = new javax.swing.JButton();
        btnItalic = new javax.swing.JButton();
        btnSouligne = new javax.swing.JButton();
        cmbFonts = new javax.swing.JComboBox<>();
        jButton12 = new javax.swing.JButton();
        jlblColorViewer = new javax.swing.JLabel();
        btnAlignGauche = new javax.swing.JButton();
        btnAlignSroit = new javax.swing.JButton();
        btnAlignCentre = new javax.swing.JButton();
        btnBarre = new javax.swing.JButton();
        btnRedo = new javax.swing.JButton();
        btnUndo = new javax.swing.JButton();
        cmbTaille = new javax.swing.JComboBox<>();
        jButton9 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        txtIntitule = new javax.swing.JTextField();
        jScrollPane10 = new javax.swing.JScrollPane();
        txtDesc = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtTags = new javax.swing.JTextField();
        pnlPartage = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtEmails = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        MesFavoris = new javax.swing.JTable();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jbtnSupprimerDocModifierDoc = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        btnVoirUtilisateurs1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
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
        lblNomUtilisateur.setText("nom utilisateur");
        lblNomUtilisateur.setToolTipText("");
        jPanel2.add(lblNomUtilisateur, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, -1, 20));
        jPanel2.add(lblUtilisateurImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 90, 90));

        jLabel10.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Bienvenue");
        jLabel10.setToolTipText("");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 70, 40));

        nav.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 0, 390, 90));

        jLabel9.setFont(new java.awt.Font("SansSerif", 1, 15)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("File Share");
        nav.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        container.add(nav, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 90));

        sideBar.setBackground(new java.awt.Color(242, 242, 242));
        sideBar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(229, 229, 229));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(88, 88, 88));
        jButton1.setText("Mes Documents");
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 40));
        jPanel1.add(icone1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 30, 40));

        sideBar.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 250, 40));

        jSeparator3.setBackground(new java.awt.Color(229, 229, 229));
        sideBar.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 45, 250, 10));

        jPanel3.setBackground(new java.awt.Color(229, 229, 229));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel3.add(icone2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 30, 40));
        jPanel3.add(lblMesDocIcone, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 30, 40));

        jButton3.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jButton3.setForeground(new java.awt.Color(88, 88, 88));
        jButton3.setText("Favoris");
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 40));

        sideBar.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 53, 250, 40));

        jSeparator4.setBackground(new java.awt.Color(229, 229, 229));
        sideBar.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 88, 240, 10));

        jPanel4.setBackground(new java.awt.Color(229, 229, 229));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel4.add(icone3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 30, 40));

        jButton4.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jButton4.setForeground(new java.awt.Color(88, 88, 88));
        jButton4.setText("Historique");
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 40));

        sideBar.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 96, 230, 40));

        jSeparator5.setBackground(new java.awt.Color(229, 229, 229));
        sideBar.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 130, 250, 10));

        jPanel5.setBackground(new java.awt.Color(229, 229, 229));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel5.add(icone4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 30, 40));

        jButton5.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jButton5.setForeground(new java.awt.Color(88, 88, 88));
        jButton5.setText("Nouveau Document");
        jButton5.setBorderPainted(false);
        jButton5.setContentAreaFilled(false);
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 40));

        sideBar.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 138, -1, 40));

        jSeparator6.setBackground(new java.awt.Color(229, 229, 229));
        sideBar.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 173, 240, 10));

        jPanel6.setBackground(new java.awt.Color(229, 229, 229));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel6.add(icone5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 30, 40));

        jButton6.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jButton6.setForeground(new java.awt.Color(88, 88, 88));
        jButton6.setText("Messages");
        jButton6.setBorderPainted(false);
        jButton6.setContentAreaFilled(false);
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 40));

        sideBar.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 181, 230, 40));

        jSeparator7.setBackground(new java.awt.Color(229, 229, 229));
        jSeparator7.setForeground(new java.awt.Color(229, 229, 229));
        sideBar.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 216, 240, 10));

        jPanel7.setBackground(new java.awt.Color(229, 229, 229));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton7.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jButton7.setForeground(new java.awt.Color(88, 88, 88));
        jButton7.setText("Deconnexion");
        jButton7.setToolTipText("");
        jButton7.setBorderPainted(false);
        jButton7.setContentAreaFilled(false);
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 40));
        jPanel7.add(icone6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 30, 40));

        sideBar.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 224, 230, 40));

        jSeparator8.setBackground(new java.awt.Color(229, 229, 229));
        sideBar.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 258, 240, 10));
        sideBar.add(icone8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 30, 40));
        sideBar.add(lblAccueilIcone3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 30, 40));
        sideBar.add(icone_message1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 30, 40));
        sideBar.add(icone9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 30, 40));

        container.add(sideBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 230, 660));

        main.setBackground(new java.awt.Color(255, 255, 255));
        main.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabPaneMain.setEnabled(false);
        tabPaneMain.setFocusTraversalKeysEnabled(false);
        tabPaneMain.setFocusable(false);
        tabPaneMain.setRequestFocusEnabled(false);
        tabPaneMain.setVerifyInputWhenFocusTarget(false);

        MesDocuments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id_auteur", "Intitulé", "Date de publication", "Dernière modification", "Tags", "Dernier éditeur", "Statut", "Id_doc","id_utilisateurs"
            }
        )
        {public boolean isCellEditable(int row, int column){return false;}}
    );
    MesDocuments.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            MesDocumentsMouseReleased(evt);
        }
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            MesDocumentsMouseClicked(evt);
        }
    });
    jScrollPane3.setViewportView(MesDocuments);

    btnVoirmesDocs.setText("Voir Document");
    btnVoirmesDocs.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnVoirmesDocsActionPerformed(evt);
        }
    });

    btnTelechargerDoc.setText("Télécharger");
    btnTelechargerDoc.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnTelechargerDocActionPerformed(evt);
        }
    });

    btnModifierMesDoc.setText("Modifier");
    btnModifierMesDoc.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnModifierMesDocActionPerformed(evt);
        }
    });

    jButton14.setText("Supprimer");
    jButton14.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton14ActionPerformed(evt);
        }
    });

    btnVoirmesDocs1.setText("Supprimer");
    btnVoirmesDocs1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jbtnSupprimerDocModifierDocActionPerformed(evt);
        }
    });

    btnVoirUtilisateurs.setText("Voir les utilisateurs");
    btnVoirUtilisateurs.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnVoirUtilisateursActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
    jPanel8.setLayout(jPanel8Layout);
    jPanel8Layout.setHorizontalGroup(
        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(236, 236, 236))
        .addGroup(jPanel8Layout.createSequentialGroup()
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 716, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel1))
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(67, 67, 67)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnVoirmesDocs, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnTelechargerDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(58, 58, 58)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnModifierMesDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnVoirmesDocs1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(240, 240, 240)
                    .addComponent(btnVoirUtilisateurs)))
            .addContainerGap(21, Short.MAX_VALUE))
    );
    jPanel8Layout.setVerticalGroup(
        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel8Layout.createSequentialGroup()
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(238, 238, 238)
                    .addComponent(jLabel1))
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(27, 27, 27)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnVoirmesDocs, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnModifierMesDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnTelechargerDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnVoirmesDocs1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnVoirUtilisateurs, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(58, 58, 58)
            .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    tabPaneMain.addTab("tab1", jPanel8);

    Historique.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {
            "Id_auteur", "Intitulé", "Date de publication", "Dernière modification", "Tags", "Dernier éditeur", "Statut", "Id_doc"
        }
    )
    {public boolean isCellEditable(int row, int column){return false;}}
    );
    Historique.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            HistoriqueMouseClicked(evt);
        }
    });
    jScrollPane5.setViewportView(Historique);

    jButton20.setText("Voir Document");
    jButton20.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton20ActionPerformed(evt);
        }
    });

    jButton21.setText("Modifier");
    jButton21.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton21ActionPerformed(evt);
        }
    });

    jButton22.setText("Voir Historique");
    jButton22.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton22ActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
    jPanel10.setLayout(jPanel10Layout);
    jPanel10Layout.setHorizontalGroup(
        jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel10Layout.createSequentialGroup()
            .addComponent(jScrollPane5)
            .addContainerGap())
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
            .addContainerGap(282, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(237, 237, 237))
    );
    jPanel10Layout.setVerticalGroup(
        jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel10Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(20, 20, 20)
            .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    tabPaneMain.addTab("tab3", jPanel10);

    HistoriqueDoc.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {
            "Id_auteur", "Intitulé", "Date de modification", "Editeur", "Id_historique", "Id_doc"
        }
    )
    {public boolean isCellEditable(int row, int column){return false;}}
    );
    HistoriqueDoc.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            HistoriqueDocMouseClicked(evt);
        }
    });
    jScrollPane6.setViewportView(HistoriqueDoc);

    jButton24.setText("Revenir à cette Version");
    jButton24.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton24ActionPerformed(evt);
        }
    });

    jButton25.setText("Voir Version");
    jButton25.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton25ActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
    jPanel11.setLayout(jPanel11Layout);
    jPanel11Layout.setHorizontalGroup(
        jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
            .addContainerGap(27, Short.MAX_VALUE)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 716, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
        .addGroup(jPanel11Layout.createSequentialGroup()
            .addGap(251, 251, 251)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(236, 236, 236))
    );
    jPanel11Layout.setVerticalGroup(
        jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel11Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
            .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(38, 38, 38))
    );

    tabPaneMain.addTab("tab4", jPanel11);

    msgTxt.setColumns(20);
    msgTxt.setRows(3);
    jScrollPane1.setViewportView(msgTxt);

    jButton2.setText("Envoyer");
    jButton2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton2ActionPerformed(evt);
        }
    });

    jScrollPane2.setEnabled(false);

    conversationMsg.setEditable(false);
    conversationMsg.setColumns(20);
    conversationMsg.setRows(5);
    conversationMsg.setEnabled(false);
    conversationMsg.setFocusable(false);
    jScrollPane2.setViewportView(conversationMsg);

    javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
    jPanel14.setLayout(jPanel14Layout);
    jPanel14Layout.setHorizontalGroup(
        jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton2))
                .addComponent(jScrollPane2))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scrollPcontactes, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18))
    );
    jPanel14Layout.setVerticalGroup(
        jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel14Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(scrollPcontactes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    tabPaneMain.addTab("messages", jPanel14);

    jPanel15.setBackground(new java.awt.Color(255, 255, 255));
    jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    convGrp.setEditable(false);
    convGrp.setColumns(20);
    convGrp.setRows(5);
    convGrp.setEnabled(false);
    convGrp.setFocusable(false);
    jScrollPane7.setViewportView(convGrp);

    jPanel15.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 20, 281, 440));

    msgGrpTxt.setColumns(15);
    msgGrpTxt.setRows(4);
    jScrollPane8.setViewportView(msgGrpTxt);

    jPanel15.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 480, 190, 70));

    jButton8.setText("Envoyer");
    jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jButton8.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton8ActionPerformed(evt);
        }
    });
    jPanel15.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 480, 90, 70));

    jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    jLabel2.setText("Document:");
    jPanel15.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

    lblTitreDoc.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    lblTitreDoc.setText("Blala:");
    jPanel15.add(lblTitreDoc, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, -1, -1));

    jPanel16.setBackground(new java.awt.Color(255, 255, 255));
    jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    docTxt.setContentType("text/html"); // NOI18N
    docTxt.setText("");
    docTxt.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            docTxtKeyPressed(evt);
        }
        public void keyReleased(java.awt.event.KeyEvent evt) {
            docTxtKeyReleased(evt);
        }
    });
    jScrollPane9.setViewportView(docTxt);

    jPanel16.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 218, 430, 250));

    jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
    jPanel17.add(jLblInfoNbEditeurs, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

    jLblEdtiteurs.setForeground(new java.awt.Color(51, 102, 0));
    jPanel17.add(jLblEdtiteurs, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

    jScrollPane11.setViewportView(jPanel17);

    jPanel16.add(jScrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 330, 90));

    jPanel13.setBackground(new java.awt.Color(255, 255, 255));

    btnGras.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
    btnGras.setText("G");
    btnGras.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnGrasActionPerformed(evt);
        }
    });

    btnItalic.setFont(new java.awt.Font("Lucida Grande", 3, 13)); // NOI18N
    btnItalic.setText("I");
    btnItalic.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnItalicActionPerformed(evt);
        }
    });

    btnSouligne.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
    btnSouligne.setText("S");
    btnSouligne.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnSouligneActionPerformed(evt);
        }
    });

    cmbFonts.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Police", "Arial", "Courier", "Comic Sans MS", "Helvetica", "Open Sans", "Tahoma", "Verdana" }));
    cmbFonts.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmbFontsActionPerformed(evt);
        }
    });

    jButton12.setText("Couleur");
    jButton12.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton12ActionPerformed(evt);
        }
    });

    jlblColorViewer.setText("jLabel1");

    btnAlignGauche.setText("gauche");
    btnAlignGauche.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnAlignGaucheActionPerformed(evt);
        }
    });

    btnAlignSroit.setText("droit");
    btnAlignSroit.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnAlignSroitActionPerformed(evt);
        }
    });

    btnAlignCentre.setText("center");
    btnAlignCentre.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnAlignCentreActionPerformed(evt);
        }
    });

    btnBarre.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
    btnBarre.setText("ST");
    btnBarre.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnBarreActionPerformed(evt);
        }
    });

    btnRedo.setText("->");
    btnRedo.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnRedoActionPerformed(evt);
        }
    });

    btnUndo.setText("<-");
    btnUndo.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnUndoActionPerformed(evt);
        }
    });

    cmbTaille.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Taille", "Petit", "Normal", "Grande" }));
    cmbTaille.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmbTailleActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
    jPanel13.setLayout(jPanel13Layout);
    jPanel13Layout.setHorizontalGroup(
        jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel13Layout.createSequentialGroup()
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel13Layout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addComponent(jlblColorViewer, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0)
                    .addComponent(btnAlignGauche)
                    .addGap(1, 1, 1)
                    .addComponent(btnAlignSroit)
                    .addGap(5, 5, 5)
                    .addComponent(btnAlignCentre))
                .addGroup(jPanel13Layout.createSequentialGroup()
                    .addGap(60, 60, 60)
                    .addComponent(btnUndo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(10, 10, 10)
                    .addComponent(btnRedo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addGroup(jPanel13Layout.createSequentialGroup()
            .addComponent(btnGras, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(2, 2, 2)
            .addComponent(btnItalic, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(2, 2, 2)
            .addComponent(btnSouligne, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(2, 2, 2)
            .addComponent(btnBarre, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(cmbFonts, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(cmbTaille, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel13Layout.setVerticalGroup(
        jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel13Layout.createSequentialGroup()
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnGras, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnItalic, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnSouligne, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBarre, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbFonts, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTaille, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(13, 13, 13)
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jlblColorViewer, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton12)
                .addComponent(btnAlignGauche)
                .addComponent(btnAlignSroit)
                .addComponent(btnAlignCentre))
            .addGap(1, 1, 1)
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnUndo)
                .addComponent(btnRedo)))
    );

    jPanel16.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 430, 110));

    jPanel15.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

    jButton9.setText("Modifier");
    jButton9.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton9ActionPerformed(evt);
        }
    });
    jPanel15.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, -1, -1));

    jButton11.setText("Ajouter au favoris");
    jButton11.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton11ActionPerformed(evt);
        }
    });
    jPanel15.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 40, -1, -1));

    tabPaneMain.addTab("modif doc", jPanel15);

    jLabel7.setText("Titre du document");

    cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Public", "Just moi", "Des utilisateurs" }));
    cmbStatus.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmbStatusActionPerformed(evt);
        }
    });

    txtDesc.setColumns(20);
    txtDesc.setRows(5);
    jScrollPane10.setViewportView(txtDesc);

    jLabel8.setText("Description");

    jLabel11.setText("Tags");

    jLabel12.setText("Qui peut voir ce fichier");

    pnlPartage.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    jLabel13.setText("Veuillez entrer les adresses emails des utilisateurs");
    pnlPartage.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, -1, -1));
    pnlPartage.add(txtEmails, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 30, 260, 50));

    jButton10.setText("Valider");
    jButton10.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton10ActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
    jPanel12.setLayout(jPanel12Layout);
    jPanel12Layout.setHorizontalGroup(
        jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel12Layout.createSequentialGroup()
            .addGap(10, 10, 10)
            .addComponent(pnlPartage, javax.swing.GroupLayout.PREFERRED_SIZE, 730, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGroup(jPanel12Layout.createSequentialGroup()
            .addGap(360, 360, 360)
            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGroup(jPanel12Layout.createSequentialGroup()
            .addGap(156, 156, 156)
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jLabel7)
                .addComponent(jLabel8)
                .addComponent(jLabel12)
                .addComponent(jLabel11))
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addGap(60, 60, 60)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                        .addComponent(txtTags)))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                    .addGap(56, 56, 56)
                    .addComponent(txtIntitule, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))))
    );
    jPanel12Layout.setVerticalGroup(
        jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel12Layout.createSequentialGroup()
            .addGap(56, 56, 56)
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtIntitule, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel7))
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addGap(37, 37, 37)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addGap(49, 49, 49)
                    .addComponent(jLabel8)))
            .addGap(20, 20, 20)
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addComponent(jLabel11))
                .addComponent(txtTags, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(40, 40, 40)
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel12)
                .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(22, 22, 22)
            .addComponent(pnlPartage, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(20, 20, 20)
            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    tabPaneMain.addTab("tab5", jPanel12);

    MesFavoris.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {
            "Id_auteur", "Intitulé", "Date de publication", "Dernière modification", "Tags", "Dernier éditeur", "Statut", "Id_doc","id_utilisateurs"
        }
    )
    {public boolean isCellEditable(int row, int column){return false;}}
    );
    MesFavoris.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            MesFavorisMouseReleased(evt);
        }
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            MesFavorisMouseClicked(evt);
        }
    });
    jScrollPane4.setViewportView(MesFavoris);

    jButton15.setText("Voir Document");
    jButton15.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton15ActionPerformed(evt);
        }
    });

    jButton16.setText("Télécharger");
    jButton16.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton16ActionPerformed(evt);
        }
    });

    jButton17.setText("Modifier");
    jButton17.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton17ActionPerformed(evt);
        }
    });

    jbtnSupprimerDocModifierDoc.setText("Retirer des favoris");
    jbtnSupprimerDocModifierDoc.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jbtnSupprimerDocModifierDocActionPerformed(evt);
        }
    });

    jButton19.setText("Retirer des favoris");
    jButton19.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton19ActionPerformed(evt);
        }
    });

    btnVoirUtilisateurs1.setText("Voir les utilisateurs");
    btnVoirUtilisateurs1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnVoirUtilisateurs1ActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
    jPanel9.setLayout(jPanel9Layout);
    jPanel9Layout.setHorizontalGroup(
        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel9Layout.createSequentialGroup()
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(7, 7, 7)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 716, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(90, 90, 90)
                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(40, 40, 40)
                    .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(90, 90, 90)
                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(40, 40, 40)
                    .addComponent(jbtnSupprimerDocModifierDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(276, 276, 276)
                    .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(257, 257, 257)
                    .addComponent(btnVoirUtilisateurs1)))
            .addGap(26, 26, 26))
    );
    jPanel9Layout.setVerticalGroup(
        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel9Layout.createSequentialGroup()
            .addGap(6, 6, 6)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(15, 15, 15)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jbtnSupprimerDocModifierDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(btnVoirUtilisateurs1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(46, 46, 46)
            .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    tabPaneMain.addTab("tab2", jPanel9);

    main.add(tabPaneMain, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 770, 600));

    container.add(main, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 60, 800, 620));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(container, javax.swing.GroupLayout.PREFERRED_SIZE, 1000, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(container, javax.swing.GroupLayout.PREFERRED_SIZE, 683, javax.swing.GroupLayout.PREFERRED_SIZE)
    );

    pack();
    setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        UtilisateurHandler.utilisateur = null;
        if (docWS != null) {
            docWS.close();
        }
        if (chatGrpWS != null) {
            chatGrpWS.close();
        }
        if (chatWS != null) {
            chatWS.close();
        }
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
        /* Historique */
        if (docWS != null) {
            docWS.close();
        }
        if (chatGrpWS != null) {
            chatGrpWS.close();
        }
        if (chatWS != null) {
            chatWS.close();
        }
        tabPaneMain.setSelectedIndex(1);
        IDocumentHandler documentHandler = new DocumentHandler();
        List<Document> documents = documentHandler.getMesDocuments(UtilisateurHandler.utilisateur.getId());

        DefaultTableModel model = (DefaultTableModel) Historique.getModel();
        /* Supprimer les données du JTable avant chaque remplissage */
        model.setRowCount(0);

        Object rowData[] = new Object[8];

        /* Cacher les colonne id_documents et id_auteur */
        Historique.getColumnModel().getColumn(0).setMinWidth(0);
        Historique.getColumnModel().getColumn(0).setMaxWidth(0);
        Historique.getColumnModel().getColumn(0).setWidth(0);
        Historique.getColumnModel().getColumn(7).setMinWidth(0);
        Historique.getColumnModel().getColumn(7).setMaxWidth(0);
        Historique.getColumnModel().getColumn(7).setWidth(0);

        for (int i = 0; i < documents.size(); i++) {
            rowData[0] = documents.get(i).getAuteur().getId();
            rowData[1] = documents.get(i).getIntitule();
            rowData[2] = documents.get(i).getDatePublixation();
            rowData[3] = documents.get(i).getDateDerniereModif();
            rowData[4] = documents.get(i).getTag();
            rowData[5] = documents.get(i).getDernierEditeur().getPrenom() + " " + documents.get(i).getDernierEditeur().getNom();
            switch (documents.get(i).getStatus()) {
                case Document.PUBLIC:
                    rowData[6] = "Public";
                    break;
                case Document.PRIVE:
                    rowData[6] = "Privé";
                    break;
                case Document.PARTAGE:
                    Set<Utilisateur> user = documents.get(i).getUtilisateursAvecDroit();
                    String partager_avec = "Partagé avec ";
                    for (Utilisateur utilisateur : user) {
                        if (utilisateur.getId() == UtilisateurHandler.utilisateur.getId()) {
                            partager_avec += "moi ";
                        } else {
                            partager_avec += utilisateur.getNom() + " ";
                        }
                    }
                    rowData[6] = partager_avec;
                    break;
                default:
                    break;
            }
            rowData[7] = documents.get(i).getId();
            model.addRow(rowData);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        /* Mes favoris */

        if (docWS != null) {
            docWS.close();
        }
        if (chatGrpWS != null) {
            chatGrpWS.close();
        }
        if (chatWS != null) {
            chatWS.close();
        }
        tabPaneMain.setSelectedIndex(6);
        IDocumentHandler documentHandler = new DocumentHandler();
        List<Document> documents = documentHandler.getDocumentsFavoris(UtilisateurHandler.utilisateur, 0, 400);

        DefaultTableModel model = (DefaultTableModel) MesFavoris.getModel();
        /* Supprimer les données du JTable avant chaque remplissage */
        model.setRowCount(0);

        Object rowData[] = new Object[9];

        /* Cacher les colonne id_documents et id_auteur */
        MesFavoris.getColumnModel().getColumn(0).setMinWidth(0);
        MesFavoris.getColumnModel().getColumn(0).setMaxWidth(0);
        MesFavoris.getColumnModel().getColumn(0).setWidth(0);
        MesFavoris.getColumnModel().getColumn(7).setMinWidth(0);
        MesFavoris.getColumnModel().getColumn(7).setMaxWidth(0);
        MesFavoris.getColumnModel().getColumn(7).setWidth(0);
        MesFavoris.getColumnModel().getColumn(8).setMinWidth(0);
        MesFavoris.getColumnModel().getColumn(8).setMaxWidth(0);
        MesFavoris.getColumnModel().getColumn(8).setWidth(0);

        for (int i = 0; i < documents.size(); i++) {
            rowData[0] = documents.get(i).getAuteur().getId();
            rowData[1] = documents.get(i).getIntitule();
            rowData[2] = documents.get(i).getDatePublixation();
            rowData[3] = documents.get(i).getDateDerniereModif();
            rowData[4] = documents.get(i).getTag();
            rowData[5] = documents.get(i).getDernierEditeur().getNom();
            switch (documents.get(i).getStatus()) {
                case Document.PUBLIC:
                    rowData[6] = "Public";
                    rowData[8] = "";
                    break;
                case Document.PRIVE:
                    rowData[6] = "Privé";
                    rowData[8] = "";
                    break;
                case Document.PARTAGE:
                    Set<Utilisateur> user = documents.get(i).getUtilisateursAvecDroit();
                    String partager_avec = "Partagé avec ";
                    String idUtilisateurs = "";
                    int cpt = 0;
                    for (Utilisateur utilisateur : user) {
                        if (utilisateur.getId() == UtilisateurHandler.utilisateur.getId()) {
                            partager_avec += "moi ";
                        } else {
                            partager_avec += utilisateur.getNom() + " ";
                            idUtilisateurs += (cpt > 0 ? "|" : "") + utilisateur.getId();

                        }
                        cpt++;
                    }
                    rowData[6] = partager_avec;
                    rowData[8] = idUtilisateurs;
                    break;
                default:
                    break;
            }
            rowData[7] = documents.get(i).getId();
            model.addRow(rowData);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        /* Mes documents */

        if (docWS != null) {
            docWS.close();
        }
        if (chatGrpWS != null) {
            chatGrpWS.close();
        }
        if (chatWS != null) {
            chatWS.close();
        }
        loadMesDocuments();

    }//GEN-LAST:event_jButton1ActionPerformed
    private void loadMesDocuments() {
        tabPaneMain.setSelectedIndex(0);
        IDocumentHandler documentHandler = new DocumentHandler();
        List<Document> documents = documentHandler.getMesDocuments(UtilisateurHandler.utilisateur.getId());

        DefaultTableModel model = (DefaultTableModel) MesDocuments.getModel();
        /* Supprimer les données du JTable avant chaque remplissage */
        model.setRowCount(0);

        Object rowData[] = new Object[9];

        /* Cacher les colonne id_documents et id_auteur  et id_utilisateurs*/
        MesDocuments.getColumnModel().getColumn(0).setMinWidth(0);
        MesDocuments.getColumnModel().getColumn(0).setMaxWidth(0);
        MesDocuments.getColumnModel().getColumn(0).setWidth(0);
        MesDocuments.getColumnModel().getColumn(7).setMinWidth(0);
        MesDocuments.getColumnModel().getColumn(7).setMaxWidth(0);
        MesDocuments.getColumnModel().getColumn(7).setWidth(0);
        MesDocuments.getColumnModel().getColumn(8).setMinWidth(0);
        MesDocuments.getColumnModel().getColumn(8).setMaxWidth(0);
        MesDocuments.getColumnModel().getColumn(8).setWidth(0);

        for (int i = 0; i < documents.size(); i++) {
            rowData[0] = documents.get(i).getAuteur().getId();
            rowData[1] = documents.get(i).getIntitule();
            rowData[2] = documents.get(i).getDatePublixation();
            rowData[3] = documents.get(i).getDateDerniereModif();
            rowData[4] = documents.get(i).getTag();
            rowData[5] = documents.get(i).getDernierEditeur().getNom();
            switch (documents.get(i).getStatus()) {
                case 0:
                    rowData[6] = "Public";
                    rowData[8] = "";
                    break;
                case 1:
                    rowData[6] = "Privé";
                    rowData[8] = "";
                    break;
                case 2:
                    Set<Utilisateur> user = documents.get(i).getUtilisateursAvecDroit();
                    String partager_avec = "Partagé avec ";
                    String idUtilisateurs = "";
                    int cpt = 0;
                    for (Utilisateur utilisateur : user) {
                        if (utilisateur.getId() == UtilisateurHandler.utilisateur.getId()) {
                            partager_avec += "moi ";
                        } else {
                            partager_avec += utilisateur.getNom() + " ";
                            idUtilisateurs += (cpt > 0 ? "|" : "") + utilisateur.getId();
                        }
                        cpt++;
                    }
                    rowData[6] = partager_avec;
                    rowData[8] = idUtilisateurs;
                    break;
                default:
                    break;
            }
            rowData[7] = documents.get(i).getId();
            model.addRow(rowData);
        }
    }
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        URI uri = null;
        try {
            uri = new URI("ws://localhost:8085/chat/" + UtilisateurHandler.utilisateur.getId() + "/-1");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            if (docWS != null) {
                docWS.close();
            }
            if (chatGrpWS != null) {
                chatGrpWS.close();
            }
            chatWS = new ClientEndPointX(uri);
            chatWS.addMessageHandler(responseString -> {
                try {
                    JsonHelper helper = new JsonHelper();
                    Map<String, String> msg = helper.decodeMessage(responseString);
                    String conversation = Dashboard.this.conversationMsg.getText();
                    conversation += "\n" + msg.get("sender") + " : " + msg.get("message") + "\n";
                    Dashboard.this.conversationMsg.setText(conversation);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        tabPaneMain.setSelectedIndex(3);
        IMessageHandler messageHandler = new MessageHandler();
        List<Utilisateur> utilisateurs = messageHandler.getContacts(UtilisateurHandler.utilisateur.getId());
        GridBagConstraints c = new GridBagConstraints();
        JPanel jpContactes = new JPanel();
        c.gridx = 0;
        for (int i = 0; i < utilisateurs.size(); i++) {
            c.gridy = i + 1;
            System.out.println(utilisateurs.get(i).getNom());
            JButton jbtn = new JButton(utilisateurs.get(i).getNom());
            jbtn.setActionCommand(Integer.toString(utilisateurs.get(i).getId()));
            jbtn.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    IMessageHandler messageHandler = new MessageHandler();
                    List<Message> messages = messageHandler.getMessages(UtilisateurHandler.utilisateur.getId(), Integer.parseInt(((JButton) e.getComponent()).getActionCommand()), 0, 300);
                    Dashboard.this.idSender = Integer.parseInt(((JButton) e.getComponent()).getActionCommand());
                    String conversation = "";
                    for (int j = messages.size() - 1; j >= 0; j--) {
                        String sender = "moi";
                        Message msg = messages.get(j);
                        if (!msg.getEmetteur().equals(UtilisateurHandler.utilisateur)) {
                            sender = msg.getEmetteur().getNom();
                        }
                        conversation += sender + " : " + msg.getText() + "\n";
                    }
                    Dashboard.this.conversationMsg.setText(conversation);
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
        }
        scrollPcontactes.add(jpContactes);

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        String txt = msgTxt.getText();
        if (txt.length() > 0) {
            JsonHelper helper = new JsonHelper();
            HashMap<String, String> msg = new HashMap<>();
            msg.put("message", txt.trim().replace("\n", "<br>"));
            msg.put("type", "solo");
            msg.put("sender", "moi");
            msg.put("receiver", Integer.toString(idSender));
            msg.put("sentDate", new Date().toString());
            msg.put("senderImg", (UtilisateurHandler.utilisateur.getImage() == null) ? "/assets/images/people.png" : UtilisateurHandler.utilisateur.getImage());
            msg.put("senderId", Integer.toString(UtilisateurHandler.utilisateur.getId()));
            String msgEncoded = helper.encodeMessage(msg);
            chatWS.sendMessage(msgEncoded);
            conversationMsg.setText(conversationMsg.getText() + "moi : " + txt);
            msgTxt.setText("");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        // TODO add your handling code here:
        /* Voir Version Historique */

        DefaultTableModel model = (DefaultTableModel) HistoriqueDoc.getModel();
        int i = HistoriqueDoc.getSelectedRow();

        if (i >= 0) {
            IHistoriqueHandler historiqueHandler = new HistoriqueHandler();
            Historique historique = historiqueHandler.get(Integer.parseInt(model.getValueAt(i, 4).toString()));
            Document doc = new Document();
            doc.setIntitule(historique.getDocument().getIntitule());
            doc.setDernierContenu(historique.getContenu());
            VoirDocument voirDocument = new VoirDocument(doc);
            System.out.println("ID de la version du document à consulter : " + Integer.parseInt(model.getValueAt(i, 4).toString()));
        } else {
            MessageBox.show("Aucun Document n'a été selectionné !", MessageBox.WARNING, this);
        }
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        // TODO add your handling code here:
        /* Revenir à une version depuis historique */

        DefaultTableModel model = (DefaultTableModel) HistoriqueDoc.getModel();
        int i = HistoriqueDoc.getSelectedRow();

        if (i >= 0) {
            /* Verifier si l'utilisateur connecté est l'auteur du document avant suppression */
            if (Integer.parseInt(model.getValueAt(i, 0).toString()) == UtilisateurHandler.utilisateur.getId()) {
                int confirmation = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment Revenir à cette version ?", "Confirmation retour version précédente", JOptionPane.YES_NO_OPTION);
                if (confirmation == 0) {

                    /* Récuperer l'historique */
                    HistoriqueHandler historiqueHandler = new HistoriqueHandler();
                    Historique historique = historiqueHandler.get(Integer.parseInt(model.getValueAt(i, 4).toString()));
                    IDocumentHandler documentHandler = new DocumentHandler();
                    Date derniereModif = new Date();
                    // modification document
                    Document doc = historique.getDocument();
                    doc.setDernierContenu(historique.getContenu());
                    doc.setDateDerniereModif(derniereModif);
                    doc.setVersion(historique.getVersion());
                    if (documentHandler.update(doc)) {
                        System.out.println("Roll back reussi!");
                    } else {
                        System.out.println("Roll back Echoué!");
                    }
                }
            }
        } else {
            MessageBox.show("Aucun Document n'a été selectionné !", MessageBox.WARNING, this);
        }
    }//GEN-LAST:event_jButton24ActionPerformed

    private void HistoriqueDocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HistoriqueDocMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_HistoriqueDocMouseClicked

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        // TODO add your handling code here:
        /* Voir Historique Document */

        DefaultTableModel model = (DefaultTableModel) Historique.getModel();
        int i = Historique.getSelectedRow();

        int id_doc = Integer.parseInt(model.getValueAt(i, 7).toString());
        HistoriqueHandler historiqueHandler = new HistoriqueHandler();
        List<Historique> historique = historiqueHandler.getHistorique(UtilisateurHandler.utilisateur.getId(), id_doc, 0, 400);

        if (i >= 0) {
            tabPaneMain.setSelectedIndex(2);

            DefaultTableModel model1 = (DefaultTableModel) HistoriqueDoc.getModel();
            /* Supprimer les données du JTable avant chaque remplissage */
            model1.setRowCount(0);

            Object rowData[] = new Object[8];

            /* Cacher les colonne id_documents et id_auteur */
            HistoriqueDoc.getColumnModel().getColumn(0).setMinWidth(0);
            HistoriqueDoc.getColumnModel().getColumn(0).setMaxWidth(0);
            HistoriqueDoc.getColumnModel().getColumn(0).setWidth(0);
            HistoriqueDoc.getColumnModel().getColumn(4).setMinWidth(0);
            HistoriqueDoc.getColumnModel().getColumn(4).setMaxWidth(0);
            HistoriqueDoc.getColumnModel().getColumn(4).setWidth(0);

            for (int l = 0; l < historique.size(); l++) {
                rowData[0] = historique.get(l).getDocument().getAuteur().getId();
                rowData[1] = historique.get(l).getDocument().getIntitule();
                rowData[2] = historique.get(l).getDateModif();
                rowData[3] = historique.get(l).getEditeur().getNom();
                rowData[4] = historique.get(l).getId();
                rowData[5] = historique.get(l).getDocument().getId();
                model1.addRow(rowData);
            }
        } else {
            MessageBox.show("Aucun Document n'a été selectionné !", MessageBox.WARNING, this);
        }

    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        // TODO add your handling code here:
        /* Modifier Document Historique */

        DefaultTableModel model = (DefaultTableModel) Historique.getModel();
        int i = Historique.getSelectedRow();

        if (i >= 0) {
            //modifier
            idSelectedDoc = Integer.parseInt(model.getValueAt(i, 7).toString());
            __modifierDocument(idSelectedDoc);
            System.out.println("ID du document à modifier : " + Integer.parseInt(model.getValueAt(i, 7).toString()));
        } else {
            MessageBox.show("Aucun Document n'a été selectionné !", MessageBox.WARNING, this);
        }
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // TODO add your handling code here:

        /* Voir Document Historique */
        DefaultTableModel model = (DefaultTableModel) Historique.getModel();
        int i = Historique.getSelectedRow();

        if (i >= 0) {
            //voir document historique
            IDocumentHandler documentHandler = new DocumentHandler();
            Document doc = documentHandler.get(Integer.parseInt(model.getValueAt(i, 7).toString()));
            VoirDocument voirDocument = new VoirDocument(doc);
        } else {
            MessageBox.show("Aucun Document n'a été selectionné !", MessageBox.WARNING, this);
        }
    }//GEN-LAST:event_jButton20ActionPerformed

    private void HistoriqueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HistoriqueMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_HistoriqueMouseClicked

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // TODO add your handling code here:
        /* Retirer Des Favoris */

        DefaultTableModel model = (DefaultTableModel) MesFavoris.getModel();
        int i = MesFavoris.getSelectedRow();

        if (i >= 0) {
            /* Verifier si l'utilisateur connecté est l'auteur du document avant suppression */
            if (Integer.parseInt(model.getValueAt(i, 0).toString()) == UtilisateurHandler.utilisateur.getId()) {
                int confirmation = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment retirer ce document de vos favoris ?", "Confirmation retrait favoris", JOptionPane.YES_NO_OPTION);
                if (confirmation == 0) {
                    model.removeRow(i);
                    DocumentHandler documentHandler = new DocumentHandler();
                    documentHandler.supprimerFavoris(UtilisateurHandler.utilisateur.getId(), Integer.parseInt(model.getValueAt(i, 7).toString()));
                }
            }
        } else {
            MessageBox.show("Aucun Document n'a été selectionné !", MessageBox.WARNING, this);
        }
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jbtnSupprimerDocModifierDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSupprimerDocModifierDocActionPerformed
        // TODO add your handling code here:
        /* Suppression Document Favoris */

        DefaultTableModel model = (DefaultTableModel) MesFavoris.getModel();
        int i = MesFavoris.getSelectedRow();

        if (i >= 0) {
            int confirmation = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment retirerr ce document des favoris?", "Confirmation suppression", JOptionPane.YES_NO_OPTION);
            if (confirmation == 0) {
                IDocumentHandler documentHandler = new DocumentHandler();
                if (documentHandler.supprimerFavoris(UtilisateurHandler.utilisateur.getId(), Integer.parseInt(model.getValueAt(i, 7).toString()))) {
                    model.removeRow(i);
                }
            }
        } else {
            MessageBox.show("Aucun Document n'a été selectionné !", MessageBox.WARNING, this);
        }
    }//GEN-LAST:event_jbtnSupprimerDocModifierDocActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
        /* Modifier Document Favoris */

        DefaultTableModel model = (DefaultTableModel) MesFavoris.getModel();
        int i = MesFavoris.getSelectedRow();

        if (i >= 0) {
            idSelectedDoc = Integer.parseInt(model.getValueAt(i, 7).toString());
            __modifierDocument(idSelectedDoc);
        } else {
            MessageBox.show("Aucun Document n'a été selectionné !", MessageBox.WARNING, this);
        }
    }//GEN-LAST:event_jButton17ActionPerformed
    private Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
    private Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        /* Téléchargement Document Favoris */

        int i = MesFavoris.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) MesDocuments.getModel();
        if (i >= 0) {

            idSelectedDoc = Integer.parseInt(model.getValueAt(i, 7).toString());
            __telechargerDoc(idSelectedDoc);
            this.setCursor(defaultCursor);

        } else {
            MessageBox.show("Aucun Document n'a été selectionné !", MessageBox.WARNING, this);
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
        /* Voir Document Favoris */

        DefaultTableModel model = (DefaultTableModel) MesFavoris.getModel();
        int i = MesFavoris.getSelectedRow();

        if (i >= 0) {
            IDocumentHandler documentHandler = new DocumentHandler();
            Document doc = documentHandler.get(Integer.parseInt(model.getValueAt(i, 7).toString()));
            VoirDocument voirDocument = new VoirDocument(doc);
        } else {
            MessageBox.show("Aucun Document n'a été selectionné !", MessageBox.WARNING, this);
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    private void MesFavorisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MesFavorisMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_MesFavorisMouseClicked

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
        /* Suppression */

        DefaultTableModel model = (DefaultTableModel) MesDocuments.getModel();
        int i = MesDocuments.getSelectedRow();

        if (i >= 0) {
            /* Verifier si l'utilisateur connecté est l'auteur du document avant suppression */
            if (Integer.parseInt(model.getValueAt(i, 0).toString()) == UtilisateurHandler.utilisateur.getId()) {
                int confirmation = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment supprimer ce document ?", "Confirmation suppression", JOptionPane.YES_NO_OPTION);
                if (confirmation == 0) {
                    model.removeRow(i);
                    DocumentHandler documentHandler = new DocumentHandler();
                    Document document = documentHandler.get(Integer.parseInt(model.getValueAt(i, 7).toString()));
                    documentHandler.delete(document);
                }
            }
        } else {
            MessageBox.show("Aucun Document n'a été selectionné !", MessageBox.WARNING, this);
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void btnModifierMesDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifierMesDocActionPerformed
        // TODO add your handling code here:
        /* Modifier Document */

        DefaultTableModel model = (DefaultTableModel) MesDocuments.getModel();
        int i = MesDocuments.getSelectedRow();

        if (i >= 0) {
            idSelectedDoc = Integer.parseInt(model.getValueAt(i, 7).toString());
            __modifierDocument(idSelectedDoc);

        } else {
            MessageBox.show("Aucun Document n'a été selectionné !", MessageBox.WARNING, this);
        }
    }//GEN-LAST:event_btnModifierMesDocActionPerformed

    public void __modifierDocument(int idSelectedDoc) {
        HTMLEditorKit editorKit = new HTMLEditorKit();
        document = (HTMLDocument) editorKit.createDefaultDocument();
        docTxt.setDocument(document);
        URI uri = null;
        URI uriModifDoc = null;
        try {
            uriModifDoc = new URI("ws://localhost:8085/document-modif/" + idSelectedDoc + "/" + UtilisateurHandler.utilisateur.getId());
            uri = new URI("ws://localhost:8085/chat/" + UtilisateurHandler.utilisateur.getId() + "/" + idSelectedDoc);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {

            chatGrpWS = new ClientEndPointX(uri);
            chatGrpWS.addMessageHandler(responseString -> {
                try {
                    System.out.println("Messare received");
                    JsonHelper helper = new JsonHelper();
                    Map<String, String> msg = helper.decodeMessage(responseString);
                    String conversation = convGrp.getText();
                    conversation += "\n" + msg.get("sender") + " : " + msg.get("message") + "\n";
                    convGrp.setForeground(Color.black);
                    convGrp.setText(conversation);
                } catch (Exception e) {

                }
            });

            // realtime modification
            docWS = new ClientEndPointX(uriModifDoc);
            docWS.addMessageHandler(responseString -> {
                JsonHelper helper = new JsonHelper();

                try {
                    if (responseString.indexOf("[") == 0) {
                        JSONArray jSONArray = new JSONArray(responseString);
                        int nbEditeurs = jSONArray.length() - 1;
                        jLblInfoNbEditeurs.setText("Actuellement " + nbEditeurs + (nbEditeurs <= 1 ? " éditeur" : " éditeurs") + " en ligne");
                        String editeurs = "";

                        for (int i = 1; i < jSONArray.length(); i++) {

                            Utilisateur u = helper.decodeUtilisateur(jSONArray.getString(i));
                            if (u.getId() == UtilisateurHandler.utilisateur.getId()) {
                                editeurs += " | Moi";
                            } else {
                                editeurs += " | " + u.getPrenom() + " " + u.getNom();
                            }

                        }
                        jLblEdtiteurs.setText(editeurs);
                    } else {
                        System.out.println("Messare received");
                        Map<String, String> doc = helper.decodeDoc(responseString);
                        //docTxt.setText(doc.get("txt"));
                        docTxt.setDocument(document);
                        Element[] roots = document.getRootElements();
                        Element body = null;
                        for (int i = 0; i < roots[0].getElementCount(); i++) {
                            Element element = roots[0].getElement(i);
                            if (element.getAttributes().getAttribute(StyleConstants.NameAttribute) == HTML.Tag.BODY) {
                                body = element;
                                break;
                            }
                        }
                        System.out.println(body);
                        document.setInnerHTML(body, doc.get("txt"));
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        IMessageHandler messageHandler = new MessageHandler();
        List<Message> messages = messageHandler.getGroupeMessages(idSelectedDoc, 0, 400);
        String conversation = "";
        for (int j = messages.size() - 1; j >= 0; j--) {
            String sender = "moi";
            Message msg = messages.get(j);
            if (!msg.getEmetteur().equals(UtilisateurHandler.utilisateur)) {
                sender = msg.getEmetteur().getNom();
            }
            conversation += sender + " : " + msg.getText() + "\n";
        }
        convGrp.setText(conversation);
        IDocumentHandler documentHandler = new DocumentHandler();
        if (documentHandler.estFavoris(idSelectedDoc, UtilisateurHandler.utilisateur.getId())) {
            jButton11.setText("Retirer des favoris");
        }
        Document document = documentHandler.get(idSelectedDoc);
        System.out.println("Dernier contenu: "+document.getDernierContenu());
        Element[] roots = this.document.getRootElements();
        Element body = null;
        for (int i = 0; i < roots[0].getElementCount(); i++) {
            Element element = roots[0].getElement(i);
            System.out.println(element.getName());
            if (element.getAttributes().getAttribute(StyleConstants.NameAttribute) == HTML.Tag.BODY) {
                body = element;
                break;
            }
        }
        System.out.println(body);
        
        try {
            this.document.setInnerHTML(body, document.getDernierContenu());
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        lblTitreDoc.setText(document.getIntitule());
        tabPaneMain.setSelectedIndex(4);
    }

    private void btnTelechargerDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTelechargerDocActionPerformed
        // TODO add your handling code here:
        /* Téléchargement */
        DefaultTableModel model = (DefaultTableModel) MesDocuments.getModel();
        int i = MesDocuments.getSelectedRow();

        if (i >= 0) {
            idSelectedDoc = Integer.parseInt(model.getValueAt(i, 7).toString());
            __telechargerDoc(idSelectedDoc);
        } else {
            System.out.println("Aucun Document n'a été selectionné !");
        }
    }//GEN-LAST:event_btnTelechargerDocActionPerformed
    public void __telechargerDoc(int idSelectedDoc) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Téléchargement Document");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fc.showOpenDialog(btnTelechargerDoc) == JFileChooser.APPROVE_OPTION) {
            this.setCursor(waitCursor);
            btnTelechargerDoc.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            System.out.println("Emplacement choisi : " + fc.getSelectedFile().getAbsolutePath());
            IDocumentHandler documentHandler = new DocumentHandler();
            Document document = documentHandler.get(idSelectedDoc);
            documentHandler.telechargerDoc(document, fc.getSelectedFile().getAbsolutePath());
        }
    }
    private void btnVoirmesDocsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoirmesDocsActionPerformed
        /* Voir Document */

        DefaultTableModel model = (DefaultTableModel) MesDocuments.getModel();
        int i = MesDocuments.getSelectedRow();

        if (i >= 0) {
            IDocumentHandler documentHandler = new DocumentHandler();
            Document doc = documentHandler.get(Integer.parseInt(model.getValueAt(i, 7).toString()));
            VoirDocument voirDocument = new VoirDocument(doc);
        } else {
            MessageBox.show("Aucun Document n'a été selectionné !", MessageBox.WARNING, this);
        }
    }//GEN-LAST:event_btnVoirmesDocsActionPerformed

    private void MesDocumentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MesDocumentsMouseClicked
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) MesDocuments.getModel();
        int i = MesDocuments.getSelectedRow();
        //jButton11.setActionCommand(Integer.toString(model.getValueAt(i, 1)));
    }//GEN-LAST:event_MesDocumentsMouseClicked

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed

        String txt = msgGrpTxt.getText();
        if (txt.length() > 0) {
            JsonHelper helper = new JsonHelper();
            HashMap<String, String> msg = new HashMap<>();
            msg.put("message", txt.trim().replace("\n", "<br>"));
            msg.put("type", "grp");
            msg.put("sender", "moi");
            msg.put("receiver", Integer.toString(idSelectedDoc));
            msg.put("sentDate", new Date().toString());
            msg.put("senderImg", (UtilisateurHandler.utilisateur.getImage() == null) ? "/assets/images/people.png" : UtilisateurHandler.utilisateur.getImage());
            msg.put("senderId", Integer.toString(UtilisateurHandler.utilisateur.getId()));
            String msgEncoded = helper.encodeMessage(msg);
            chatGrpWS.sendMessage(msgEncoded);
            convGrp.setForeground(Color.red);
            convGrp.setText(convGrp.getText() + "moi : " + txt);
            msgGrpTxt.setText("");
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void cmbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStatusActionPerformed
        if (cmbStatus.getSelectedIndex() == 2) {
            pnlPartage.setVisible(true);
        } else {
            pnlPartage.setVisible(false);
        }
        pnlPartage.repaint();
    }//GEN-LAST:event_cmbStatusActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        idSelectedDoc = -1;
        txtDesc.setText("");
        txtIntitule.setText("");
        txtTags.setText("");
        txtEmails.setText("");
        tabPaneMain.setSelectedIndex(5);
        if (docWS != null) {
            docWS.close();
        }
        if (chatGrpWS != null) {
            chatGrpWS.close();
        }
        if (chatWS != null) {
            chatWS.close();
        }

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        if (docWS != null) {
            docWS.close();
        }
        if (chatGrpWS != null) {
            chatGrpWS.close();
        }
        if (chatWS != null) {
            chatWS.close();
        }
        IDocumentHandler documentHandler = new DocumentHandler();
        Document doc = documentHandler.get(idSelectedDoc);
        txtIntitule.setText(doc.getIntitule());
        txtDesc.setText(doc.getDescription());
        txtTags.setText(doc.getTag());
        String emails = "";

        for (Iterator<Utilisateur> it = doc.getUtilisateursAvecDroit().iterator(); it.hasNext();) {
            Utilisateur u = it.next();
            emails += " " + u.getEmail();
        }
        txtEmails.setText(emails);
        cmbStatus.setSelectedIndex(doc.getStatus());
        tabPaneMain.setSelectedIndex(5);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        IDocumentHandler documentHandler = new DocumentHandler();
        String intitule = txtIntitule.getText();
        String description = txtDesc.getText();
        String tags = txtTags.getText();
        String emails = "";
        int status = cmbStatus.getSelectedIndex();

        if (intitule.length() != 0) {
            if (status == Document.PARTAGE) {
                emails = txtEmails.getText();
                if (emails.length() == 0) {
                    System.out.println("erreur msg");
                    return;
                }
            }
            Document doc;
            if (idSelectedDoc == -1) {
                doc = new Document();
            } else {
                doc = documentHandler.get(idSelectedDoc);
            }
            Date currentDate = new Date();
            doc.setAuteur(UtilisateurHandler.utilisateur);
            doc.setDateDerniereModif(currentDate);
            doc.setDatePublixation(currentDate);
            doc.setDernierEditeur(UtilisateurHandler.utilisateur);
            doc.setDescription(description);
            doc.setIntitule(intitule);
            doc.setTag(tags);
            doc.setStatus(status);
            if (cmbStatus.getSelectedIndex() == Document.PARTAGE) {
                IUtilisateurHandler utilisateurHandler = new UtilisateurHandler();
                Set<Utilisateur> utilisateurs_autorises = new HashSet<>();
                String[] utilisateurs = txtEmails.getText().split(" ");
                for (int i = 0; i < utilisateurs.length; i++) {
                    Utilisateur utilisateur = utilisateurHandler.get(utilisateurs[i]);
                    if (utilisateur != null) {
                        utilisateurs_autorises.add(utilisateur);
                    }
                }
                doc.setUtilisateursAvecDroit(utilisateurs_autorises);
            } else {
                doc.setUtilisateursAvecDroit(new HashSet<Utilisateur>());
            }
            if (idSelectedDoc == -1) {
                if (documentHandler.add(doc)) {
                    idSelectedDoc = doc.getId();
                    __modifierDocument(idSelectedDoc);
                }
            } else {
                if (documentHandler.update(doc)) {
                    idSelectedDoc = doc.getId();
                    __modifierDocument(idSelectedDoc);
                }
            }

        }


    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed

        IDocumentHandler documentHandler = new DocumentHandler();
        Document doc = documentHandler.get(idSelectedDoc);
        Set<Document> favs = UtilisateurHandler.utilisateur.getFavoris();
        String status;
        if (documentHandler.estFavoris(idSelectedDoc, UtilisateurHandler.utilisateur.getId())) {
            documentHandler.supprimerFavoris(UtilisateurHandler.utilisateur.getId(), idSelectedDoc);
            status = "Ajouter au favoris";
        } else {
            documentHandler.ajouterFavoris(UtilisateurHandler.utilisateur.getId(), idSelectedDoc);
            status = "Retirer des favoris";
        }
        UtilisateurHandler.utilisateur.setFavoris(favs);
        if (new UtilisateurHandler().update(UtilisateurHandler.utilisateur)) {
            jButton11.setText(status);
            System.out.println("reussi");
        } else {
            System.out.println("Erreur");
        }

    }//GEN-LAST:event_jButton11ActionPerformed

    private void btnVoirUtilisateursActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoirUtilisateursActionPerformed
        DefaultTableModel model = (DefaultTableModel) MesDocuments.getModel();
        int i = MesDocuments.getSelectedRow();

        if (i >= 0) {
            IDocumentHandler documentHandler = new DocumentHandler();
            String partageAvec = model.getValueAt(i, 8).toString();
            System.out.println("partage avec " + partageAvec);
            if (partageAvec.length() > 0) {
                VoirUtilisateurs voirUtilisateurs = new VoirUtilisateurs(partageAvec, this);
                voirUtilisateurs.setVisible(true);
            }
        }
    }//GEN-LAST:event_btnVoirUtilisateursActionPerformed

    private void MesDocumentsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MesDocumentsMouseReleased
        DefaultTableModel model = (DefaultTableModel) MesDocuments.getModel();
        int i = MesDocuments.getSelectedRow();
        System.out.println("Im clicked");
        if (i >= 0) {
            IDocumentHandler documentHandler = new DocumentHandler();
            String partageAvec = model.getValueAt(i, 8).toString();
            if (partageAvec.length() > 0) {
                btnVoirUtilisateurs.setEnabled(true);
                System.out.println("enqbled true");
            } else {
                btnVoirUtilisateurs.setEnabled(false);
                System.out.println("enqbled true");

            }
            System.out.println(btnVoirUtilisateurs.isEnabled());
            btnVoirUtilisateurs.repaint();
        }    }//GEN-LAST:event_MesDocumentsMouseReleased

    private void btnVoirUtilisateurs1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoirUtilisateurs1ActionPerformed
        DefaultTableModel model = (DefaultTableModel) MesFavoris.getModel();
        int i = MesFavoris.getSelectedRow();

        if (i >= 0) {
            IDocumentHandler documentHandler = new DocumentHandler();
            String partageAvec = model.getValueAt(i, 8).toString();
            System.out.println("partage avec " + partageAvec);
            if (partageAvec.length() > 0) {
                VoirUtilisateurs voirUtilisateurs = new VoirUtilisateurs(partageAvec, this);
                voirUtilisateurs.setVisible(true);
            }
        }
    }//GEN-LAST:event_btnVoirUtilisateurs1ActionPerformed

    private void MesFavorisMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MesFavorisMouseReleased
        DefaultTableModel model = (DefaultTableModel) MesFavoris.getModel();
        int i = MesFavoris.getSelectedRow();
        System.out.println("Im clicked");
        if (i >= 0) {
            IDocumentHandler documentHandler = new DocumentHandler();
            String partageAvec = model.getValueAt(i, 8).toString();
            if (partageAvec.length() > 0) {
                btnVoirUtilisateurs1.setEnabled(true);
                System.out.println("enqbled true");
            } else {
                btnVoirUtilisateurs1.setEnabled(false);
                System.out.println("enqbled true");

            }
            System.out.println(btnVoirUtilisateurs1.isEnabled());
            btnVoirUtilisateurs1.repaint();
        }    }//GEN-LAST:event_MesFavorisMouseReleased

    private void docTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_docTxtKeyReleased

        //webscoket modification
        if (evt.getKeyCode() != 157 && evt.getKeyCode() != 17) {
            JsonHelper helper = new JsonHelper();
            HashMap<String, String> doc = new HashMap<>();
            doc.put("idDoc", Integer.toString(idSelectedDoc));
            doc.put("idU", Integer.toString(UtilisateurHandler.utilisateur.getId()));
            String documentText = docTxt.getText().substring(docTxt.getText().indexOf("<body>") + "<body>".length(), docTxt.getText().indexOf("</body>"));
            System.out.println(documentText);
            doc.put("txt", documentText);
            String stringifiedDoc = helper.encodeDoc(doc);
            docWS.sendMessage(stringifiedDoc);

            System.out.println("justpressedKey: " + evt.getKeyCode());
            System.out.println("keyPressed: " + keyPressed);
            System.out.println("====");
            if ((keyPressed == 157 || keyPressed == 17) && evt.getKeyCode() == 85) {
                btnRedo.doClick();
            } else if ((keyPressed == 157 || keyPressed == 17) && evt.getKeyCode() == 90) {
                System.out.println("keyPressed: " + keyPressed);
                btnUndo.doClick();
            }

        } else {
            keyPressed = -1;
        }
    }//GEN-LAST:event_docTxtKeyReleased

    private void btnGrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrasActionPerformed
        declancheAction = new JButton(boldAction);
        declancheAction.doClick();
        docTxt.requestFocus();
    }//GEN-LAST:event_btnGrasActionPerformed

    private void btnItalicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItalicActionPerformed
        declancheAction = new JButton(italicAction);
        declancheAction.doClick();
        docTxt.requestFocus();

    }//GEN-LAST:event_btnItalicActionPerformed

    private void btnSouligneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSouligneActionPerformed
        declancheAction = new JButton(underlineAction);
        declancheAction.doClick();
        docTxt.requestFocus();

    }//GEN-LAST:event_btnSouligneActionPerformed

    private void cmbFontsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFontsActionPerformed
        int index = cmbFonts.getSelectedIndex();
        if (fontTypes[index] != "") {
            System.out.println(fontTypes[index]);
            declancheAction.setAction(new StyledEditorKit.FontFamilyAction(fontTypes[index], fontTypes[index]));
            declancheAction.doClick();
            docTxt.requestFocus();
        }
    }//GEN-LAST:event_cmbFontsActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        Color newColor = JColorChooser.showDialog(null, "Choose a color", currentColor);
        currentColor = newColor;
        declancheAction = new JButton(new StyledEditorKit.ForegroundAction("currentColor", currentColor));
        declancheAction.doClick();

        jlblColorViewer.setIcon(jlabelIcon(currentColor));
        docTxt.requestFocus();
        repaint();
    }//GEN-LAST:event_jButton12ActionPerformed
    private ImageIcon jlabelIcon(Color color) {
        BufferedImage image = new BufferedImage(20, 20, java.awt.image.BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(color);
        graphics.fillRect(0, 0, 20, 20);
        graphics.setXORMode(color);
        graphics.drawRect(0, 0, 20 - 1, 20 - 1);
        image.flush();
        ImageIcon icon = new ImageIcon(image);
        return icon;
    }
    private void btnAlignGaucheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlignGaucheActionPerformed
        declancheAction.setAction(new StyledEditorKit.AlignmentAction("Left Align", StyleConstants.ALIGN_LEFT));
        declancheAction.doClick();
        docTxt.requestFocus();
    }//GEN-LAST:event_btnAlignGaucheActionPerformed

    private void btnAlignSroitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlignSroitActionPerformed
        declancheAction = new JButton(new StyledEditorKit.AlignmentAction("Right Align", StyleConstants.ALIGN_RIGHT));
        declancheAction.doClick();
        docTxt.requestFocus();
    }//GEN-LAST:event_btnAlignSroitActionPerformed

    private void btnAlignCentreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlignCentreActionPerformed
        declancheAction = new JButton(new StyledEditorKit.AlignmentAction("Center", StyleConstants.ALIGN_CENTER));
        declancheAction.doClick();
        docTxt.requestFocus();
    }//GEN-LAST:event_btnAlignCentreActionPerformed

    private void btnBarreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBarreActionPerformed
        declancheAction = new JButton(new StrikeThroughAction());
        declancheAction.doClick();
        docTxt.requestFocus();

    }//GEN-LAST:event_btnBarreActionPerformed

    private void btnRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRedoActionPerformed
        declancheAction = new JButton(redoAction);
        declancheAction.doClick();
        docTxt.requestFocus();
    }//GEN-LAST:event_btnRedoActionPerformed

    private void btnUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUndoActionPerformed

        declancheAction = new JButton(undoAction);
        declancheAction.doClick();
        docTxt.requestFocus();
    }//GEN-LAST:event_btnUndoActionPerformed

    private void cmbTailleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTailleActionPerformed
        int index = cmbTaille.getSelectedIndex();
        if (tailles[index] != -1) {
            declancheAction = new JButton(new StyledEditorKit.FontSizeAction(String.valueOf(tailles[index]), tailles[index]));
            declancheAction.doClick();
            docTxt.requestFocus();
        }
    }//GEN-LAST:event_cmbTailleActionPerformed

    private void docTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_docTxtKeyPressed

        if (evt.getKeyCode() == 157 || evt.getKeyCode() == 17) {
            keyPressed = evt.getKeyCode();
        }
    }//GEN-LAST:event_docTxtKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Historique;
    private javax.swing.JTable HistoriqueDoc;
    private javax.swing.JTable MesDocuments;
    private javax.swing.JTable MesFavoris;
    private javax.swing.JButton btnAlignCentre;
    private javax.swing.JButton btnAlignGauche;
    private javax.swing.JButton btnAlignSroit;
    private javax.swing.JButton btnBarre;
    private javax.swing.JButton btnGras;
    private javax.swing.JButton btnItalic;
    private javax.swing.JButton btnModifierMesDoc;
    private javax.swing.JButton btnRedo;
    private javax.swing.JButton btnSouligne;
    private javax.swing.JButton btnTelechargerDoc;
    private javax.swing.JButton btnUndo;
    private javax.swing.JButton btnVoirUtilisateurs;
    private javax.swing.JButton btnVoirUtilisateurs1;
    private javax.swing.JButton btnVoirmesDocs;
    private javax.swing.JButton btnVoirmesDocs1;
    private javax.swing.JComboBox<String> cmbFonts;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JComboBox<String> cmbTaille;
    private javax.swing.JPanel container;
    private javax.swing.JTextArea convGrp;
    public javax.swing.JTextArea conversationMsg;
    private javax.swing.JEditorPane docTxt;
    private javax.swing.JLabel icone1;
    private javax.swing.JLabel icone2;
    private javax.swing.JLabel icone3;
    private javax.swing.JLabel icone4;
    private javax.swing.JLabel icone5;
    private javax.swing.JLabel icone6;
    private javax.swing.JLabel icone8;
    private javax.swing.JLabel icone9;
    private javax.swing.JLabel icone_message1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLblEdtiteurs;
    private javax.swing.JLabel jLblInfoNbEditeurs;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JButton jbtnSupprimerDocModifierDoc;
    private javax.swing.JLabel jlblColorViewer;
    private javax.swing.JLabel lblAccueilIcone3;
    private javax.swing.JLabel lblMesDocIcone;
    private javax.swing.JLabel lblNomUtilisateur;
    private javax.swing.JLabel lblTitreDoc;
    private javax.swing.JLabel lblUtilisateurImage;
    private javax.swing.JPanel main;
    private javax.swing.JTextArea msgGrpTxt;
    private javax.swing.JTextArea msgTxt;
    private javax.swing.JPanel nav;
    private javax.swing.JPanel pnlPartage;
    public java.awt.ScrollPane scrollPcontactes;
    private javax.swing.JPanel sideBar;
    public javax.swing.JTabbedPane tabPaneMain;
    private javax.swing.JTextArea txtDesc;
    private javax.swing.JTextField txtEmails;
    private javax.swing.JTextField txtIntitule;
    private javax.swing.JTextField txtTags;
    // End of variables declaration//GEN-END:variables
class UndoHandler implements UndoableEditListener {

        /**
         * Messaged when the Document has created an edit, the edit is added to
         * <code>undo</code>, an instance of UndoManager.
         */
        public void undoableEditHappened(UndoableEditEvent e) {
            undo.addEdit(e.getEdit());
            undoAction.update();
            redoAction.update();
        }
    }

    class UndoAction extends AbstractAction {

        public UndoAction() {
            super("Undo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undo.undo();
            } catch (CannotUndoException ex) {
                System.out.println("Unable to undo: " + ex);
                ex.printStackTrace();
            }
            update();
            redoAction.update();
        }

        protected void update() {
            if (undo.canUndo()) {
                setEnabled(true);
                putValue(Action.NAME, undo.getUndoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Undo");
            }
        }
    }

    class RedoAction extends AbstractAction {

        public RedoAction() {
            super("Redo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undo.redo();
            } catch (CannotRedoException ex) {
                System.err.println("Unable to redo: " + ex);
                ex.printStackTrace();
            }
            update();
            undoAction.update();
        }

        protected void update() {
            if (undo.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, undo.getRedoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Redo");
            }
        }
    }

}
