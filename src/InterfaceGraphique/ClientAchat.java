package InterfaceGraphique;

import Properties.PropertiesPerso;
import com.formdev.flatlaf.FlatDarculaLaf;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.DecimalFormat;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;


import static Socket.socket.Receive;
import static Socket.socket.Send;
import static Socket.socket.ClientSocket;
//import javax.swing.table.TableCellRenderer;



public class ClientAchat extends JFrame
{
    private static ClientAchat dialog;
    private JPanel MainPanel;
    private JPanel AchatPanel;
    private JPanel PubPanel;
    private JPanel PanierPanel;
    private JPanel ConnexionPanel;
    private JLabel NomLabel;
    private JLabel MdpLabel;
    private JButton LoginButton;
    private JButton LogoutButton;
    private JCheckBox nouveauCliChecbox;
    private JTextField NomTxt;
    private JTextField MDPtxt;
    private JLabel MagasinLabel;
    private JLabel ArticleLabel;
    private JLabel PrixLabel;
    private JLabel StockLabel;
    private JLabel QuantiteLabel;
    private JTextField ArticleTxt;
    private JTextField PrixTxt;
    private JTextField StockTxt;
    private JSpinner QttSpinner;
    private JButton PrecedentBouton;
    private JPanel PrecendentPanel;
    private JButton SuivantBouton;
    private JButton acheterButton;
    private JLabel ImgLabel;
    private JLabel PubLabel;
    private JLabel PanierLabel;
    private JButton confimerAchatButton;
    private JButton supprimerArticleButton;
    private JButton viderLePanierButton;
    private JLabel TotalLabel;
    private JTextField TotTxt;
    private JPanel ImgPanel;
    private JTable PanierTable;
    private JScrollPane PanierScroll;

    private JPanel ImagePanel;


    private boolean logged = false;
    private Socket sClient;
    private int idArticleEnCours;
    private int nbArticlePanier;
    private float prixTotal = 0;


    private String REPERTOIRE_IMAGES = "C:\\Users\\josue\\Java_Project_2023_2024\\rti_client_achat\\resources\\";


    public void loginOK()
    {
        LoginButton.setEnabled(false);
        LogoutButton.setEnabled(true);
        NomTxt.setEditable(false);
        MDPtxt.setEditable(false);
        nouveauCliChecbox.setEnabled(false);

        QttSpinner.setEnabled(true);
        PrecedentBouton.setEnabled(true);
        SuivantBouton.setEnabled(true);
        acheterButton.setEnabled(true);
        supprimerArticleButton.setEnabled(true);
        StockTxt.setEnabled(true);
        viderLePanierButton.setEnabled(true);
        confimerAchatButton.setEnabled(true);

    }

    public void logoutOK()
    {

        LoginButton.setEnabled(true);
        LogoutButton.setEnabled(false);


        NomTxt.setEditable(true);
        MDPtxt.setEditable(true);

        //nouveauCliChecbox.setEnabled(false);


        QttSpinner.setVisible(true);
        PrecedentBouton.setVisible(true);
        SuivantBouton.setVisible(true);
        acheterButton.setVisible(true);
        supprimerArticleButton.setVisible(true);
        viderLePanierButton.setVisible(true);
        confimerAchatButton.setVisible(true);

        // Réinitialiser des valeurs

        setNom("");
        setMotDePasse("");
        setArticle("", (float) -1.0,-1,"");

        nouveauCliChecbox.setSelected(false);
    }

    public void ajoutArticleTablePanier(String intiule, int quantite, float prix)
    {
        DefaultTableModel model = (DefaultTableModel) PanierTable.getModel();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String Prix = decimalFormat.format(prix);
        model.addRow(new Object[] { intiule, "" + quantite, Prix });

        PanierTable.setModel(model);
    }
    public void videTablePanier()
    {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Intitulé");
        model.addColumn("Prix à l'unité");
        model.addColumn("Quantité");


        PanierTable.setModel(model);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Supposons que "columnIndex" est l'index de la colonne pour laquelle vous souhaitez définir l'alignement.
        PanierTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        PanierTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        PanierTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        PanierTable.getTableHeader().setReorderingAllowed(false);
    }


    public int isNouveauClientChecked()
    {
        if (nouveauCliChecbox.isSelected())
        {
            return 1;
        }
        return 0;
    }

    public boolean OVESP_Login(String user, String password, int NouveauClient)
    {
        String requete, responce;


        // Construction de la requête
        requete = "LOGIN#" + user + "#" + password + "#" + NouveauClient;

        /************ENVOIE************************/


        try
        {
            DataOutputStream fluxSortie = new DataOutputStream(new BufferedOutputStream(sClient.getOutputStream()));
            Send(requete,fluxSortie);
        }
        catch (IOException e1)
        {
            System.err.println("Erreur de SEND !" + e1.getMessage());
            try {
                sClient.close();
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            return false;
        }


        /*************RECEPTION*****************************/

        try
        {
            DataInputStream fluxEntree = new DataInputStream(new BufferedInputStream(sClient.getInputStream()));
            responce = Receive(fluxEntree);
            String[] data = responce.split("#");
            if(data[1].equals("ok"))
            {
                //envoie d'une requete consult dans la suite du code mdr
            }
            else if (data[1].equals("ko"))
            {
                //affichage de l'erreur
                JOptionPane.showMessageDialog(null, data[2], "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (IOException e2)
        {
            System.err.println("Erreur de RECEIVE " + e2.getMessage());
            return false;
        }

        return true;
    }

    public boolean Logout()
    {
        String requete = "LOGOUT";

        //---------------------ENVOIE---------------------------//

        try
        {
            DataOutputStream fluxSortie = new DataOutputStream(new BufferedOutputStream(sClient.getOutputStream()));
            Send(requete,fluxSortie);
        }
        catch (IOException e1)
        {
            System.err.println("Erreur de SEND !" + e1.getMessage());
            try {
                sClient.close();
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            return false;
        }

        JOptionPane.showMessageDialog(null, "Logout réussi.", "LOGOUT", JOptionPane.INFORMATION_MESSAGE);
        logoutOK();
        nbArticlePanier = 0;
        logged = false;
        return true;
    }

    public void gestionCaddie()
    {
        //----------caddie

        String requete = "CADDIE";
        String responce = "";

        int quantite;
        float prix;
        String intitule;


        //---------------------ENVOIE---------------------------//

        try
        {
            DataOutputStream fluxSortieCaddie = new DataOutputStream(new BufferedOutputStream(sClient.getOutputStream()));
            Send(requete,fluxSortieCaddie);
        }
        catch (IOException e1)
        {
            System.err.println("Erreur de  : " + e1.getMessage());
            System.exit(1);
        }

        //---------------------RECEPTION-----------------------//
        try
        {
            DataInputStream fluxEntreeCaddie = new DataInputStream(new BufferedInputStream(sClient.getInputStream()));
            responce = Receive(fluxEntreeCaddie);

            String[] testOK = responce.split("#");
            if(testOK[1].equals("ko"))
            {
                JOptionPane.showMessageDialog(null, testOK[2], "Erreur", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            //gestion de la reponse du caddie

            videTablePanier();
            prixTotal = 0;
            //int nboccurence = nombreOccurrences(responce);


            //--------------------Problème------------------------------//
            /*Vous obtenez probablement l'erreur "Index 1 out of bounds for length 1" parce que ElemCaddie a une longueur de 1,
            mais vous essayez d'accéder à ElemCaddie[i] où i est supérieur à 0. Assurez-vous que i est dans les limites de ElemCaddie.
            De plus, dans votre boucle for, vous utilisez nboccurence comme condition,
            mais il est possible que ElemCaddie ait moins d'éléments que nboccurence,
            ce qui pourrait également entraîner une exception.*/
            //-------------------------------------------------------------//


            //--------------------Résolution------------------------------//
            /*Lorsque vous utilisez String.split("$"), le caractère $ est interprété
            comme une expression régulière, ce qui signifie qu'il est utilisé
            comme un délimiteur spécial. Pour l'utiliser comme un caractère ordinaire, vous devez échapper le $ en utilisant \\$
            for (String elem : ElemCaddie) {: Vous itérez à travers toutes les sous-chaînes obtenues
            à partir de la division précédente,
            en les stockant dans la variable elem.*/
            //-------------------------------------------------------------//


            String[] ElemCaddie = responce.split("\\$");
            for (String elem : ElemCaddie)
            {
                elem = elem.replace("$", "");
                String[] UnElemCaddie = elem.split(",");

                if (UnElemCaddie.length >= 4)
                {
                    intitule = UnElemCaddie[1];
                    quantite = Integer.parseInt(UnElemCaddie[2]);
                    prix = Float.parseFloat(UnElemCaddie[3]);

                    prixTotal += prix;

                    // Ajoutez ici l'article au panier avec ajoutArticleTablePanier(intitule, quantite, prix)
                    ajoutArticleTablePanier(intitule, quantite, prix);
                }
            }

            setTotTxt("" + prixTotal);

        } catch (Exception e4)
        {
            System.err.println("Erreur de  : " + e4.getMessage());
            System.exit(1);
        }

    }

    public boolean EnvoieConsult(String requete)
    {

        String responce;

        //---------------------ENVOIE---------------------------//

        try
        {
            DataOutputStream fluxSortie = new DataOutputStream(new BufferedOutputStream(sClient.getOutputStream()));
            Send(requete,fluxSortie);
        }
        catch (IOException e1)
        {
            System.err.println("Erreur de SEND !" + e1.getMessage());
            try
            {
                sClient.close();
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            return false;
        }

        //---------------------RECEPTION-----------------------//

        try
        {
            DataInputStream fluxEntree = new DataInputStream(new BufferedInputStream(sClient.getInputStream()));
            responce = Receive(fluxEntree);
            String[] data = responce.split("#");
            if(data[1].equals("ok"))
            {
                //affichage des éléments
                int id, stock;
                String intitule, image;
                float prix;

                //sprintf(reponse,"CONSULT#ok#%d#%s#%f#%d#%s", atoi(Tuple[0]), Tuple[1], atof(Tuple[2]), atoi(Tuple[3]), Tuple[4]);

                id = Integer.parseInt(data[2]);
                intitule = data[3];

                //ici il y a peut-être une erreur avec un point qui devrait être une virgule
                prix = Float.parseFloat(data[4]);

                stock = Integer.parseInt(data[5]);
                image = data[6];


                System.out.printf("id = %d\n", id);
                System.out.printf("intitule = %s\n", intitule);
                System.out.printf("prix = %f\n", prix);
                System.out.printf("stock = %d\n", stock);
                System.out.printf("image = %s\n", image);

                setArticle(intitule, prix, stock, image);
            }
            else if (data[1].equals("ko"))
            {
                //affichage de l'erreur
                JOptionPane.showMessageDialog(null, "Article non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (IOException e2)
        {
            System.err.println("Erreur de RECEIVE " + e2.getMessage());
            return false;
        }

        return true;
    }

    private void setArticle(String intitule, float prix, int stock, String image)
    {
        ArticleTxt.setText(intitule);

        if(prix == -1.0)
        {
            PrixTxt.setText("");
        }
        else
        {
            PrixTxt.setText("" + prix);
        }
        if(stock == -1)
        {
            StockTxt.setText("");
        }
        else
        {
            StockTxt.setText("" + stock);
        }

        ImageIcon imageIcon = new ImageIcon(REPERTOIRE_IMAGES + image);
        ImgLabel.setIcon(imageIcon);
        ImgPanel.add(ImgLabel, BorderLayout.CENTER);
        ImgPanel.revalidate(); //revalidé le composant enfant d'un conteneur, donc du panel (un peu comme une maj)
        ImgPanel.repaint();

    }

    private int getQuantite()
    {
        return (int) QttSpinner.getValue();
    }

    public ClientAchat()
    {

        setTitle("Client Achat");

        Dimension nouvelleTaille = new Dimension(120, 200);
        PrecedentBouton.setPreferredSize(nouvelleTaille);
        SuivantBouton.setPreferredSize(nouvelleTaille);
        Dimension loginButtonSize = new Dimension(140, 20);
        LoginButton.setPreferredSize(loginButtonSize);
        Dimension logoutButtonSize = new Dimension(140, 20);
        LogoutButton.setPreferredSize(logoutButtonSize);
        Dimension Tot = new Dimension(160,20);
        TotTxt.setPreferredSize(Tot);
        Dimension supp = new Dimension(250,20);
        supprimerArticleButton.setPreferredSize(supp);
        viderLePanierButton.setPreferredSize(supp);
        Dimension Img = new Dimension(200,200);
        ImgLabel.setPreferredSize(Img);
        int width = PanierTable.getWidth();
        int newHeight = 150;
        Dimension panier = new Dimension(width,newHeight);

        SpinnerNumberModel numberModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1); // Limite minimale à 0
        QttSpinner.setModel(numberModel);

        ((JSpinner.DefaultEditor) QttSpinner.getEditor()).getTextField().setEnabled(false); //il faut faire cette ligne après les 2 précédante

        PanierScroll.setPreferredSize(panier);

        setContentPane(MainPanel);
        setLocationRelativeTo(null);
        pack();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1000,700);
        setLocation((screen.width - this.getSize().width)/2,(screen.height - this.getSize().height)/2);
        setResizable(false);


        ImgPanel.setLayout(new BorderLayout());
        ImgPanel.setPreferredSize(Img);

        Border border = new LineBorder(Color.BLACK, 1); // Couleur et largeur de la bordure
        AchatPanel.setBorder(border);
        PanierPanel.setBorder(border);
        ImgLabel.setBorder(border);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Intitulé");
        model.addColumn("Prix à l'unité");
        model.addColumn("Quantité");

        PanierTable.setModel(model);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Supposons que "columnIndex" est l'index de la colonne pour laquelle vous souhaitez définir l'alignement.
        PanierTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        PanierTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        PanierTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        PanierTable.getTableHeader().setReorderingAllowed(false);

        logoutOK();
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(Color.BLACK);
        // Appliquez la couleur aux header
        for (int i = 0; i < model.getColumnCount(); i++)
        {
            PanierTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }


        ImageIcon logoIcon = new ImageIcon(PropertiesPerso.PropertiesTest());
        setIconImage(logoIcon.getImage());


        LoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int nouveauClient = isNouveauClientChecked();

                String user = getNom();
                String password = getMotDePasse();
                if(user.equals("") || user.equals(" ") || password.equals("") || password.equals(" "))
                {
                    if(user.equals("") || user.equals(" "))
                    {
                        JOptionPane.showMessageDialog(null, "User invalide !", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                    if(password.equals("") || password.equals(" "))
                    {
                        JOptionPane.showMessageDialog(null, "Password invalide !", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else
                {
                    try
                    {
                        sClient = ClientSocket();

                        System.out.println("Connecté au serveur.");

                        if (!OVESP_Login(user, password, nouveauClient))
                        {
                            System.err.println("Erreur dans OVESP_Login");
                            System.exit(1);
                        }
                        else
                        {
                            //la consult

                            String requete = "CONSULT#1";
                            idArticleEnCours = 1;
                            if(!EnvoieConsult(requete))
                            {
                                System.err.println("Erreur dans EnvoieConsult");
                                System.exit(1);
                            }

                        }
                        logged = true;
                        loginOK();
                        JOptionPane.showMessageDialog(null, "Login réussi.", "LOGIN", JOptionPane.INFORMATION_MESSAGE);


                    } catch (Exception e2)
                    {
                        System.err.println("Erreur de ClientSocket : " + e2.getMessage());
                        System.exit(1);
                    }


                    logged = true;
                }
            }
        });

        PrecedentBouton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //test si l'id de l'article est a 0
                if(idArticleEnCours == 1)
                {
                    JOptionPane.showMessageDialog(null, "Pas d'article précédent.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    idArticleEnCours--;

                    String requete = "CONSULT#" + idArticleEnCours;
                    if(!EnvoieConsult(requete))
                    {
                        System.err.println("Erreur dans EnvoieConsult");

                        //remettre à l'etat initial
                        idArticleEnCours++;

                        System.exit(1);
                    }
                }
            }
        });

        SuivantBouton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //test si l'id de l'article est a 0
                if(idArticleEnCours == 21)
                {
                    JOptionPane.showMessageDialog(null, "Pas d'article suivant.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    idArticleEnCours++;

                    String requete = "CONSULT#" + idArticleEnCours;
                    if(!EnvoieConsult(requete))
                    {
                        System.err.println("Erreur dans EnvoieConsult");

                        //remettre à l'etat initial
                        idArticleEnCours--;

                        System.exit(1);
                    }
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(logged == true)
                {
                    if(!Logout())
                    {
                        System.err.println("Erreur dans le logout");
                        System.exit(1);
                    }
                }

                nbArticlePanier = 0;

                //fermeture de la fenêtre
                dialog.dispose();
                System.exit(0);
            }
        });

        LogoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(logged == true)
                {
                    if(!Logout())
                    {
                        System.err.println("Erreur dans le logout");
                        System.exit(1);
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Impossible de se logout (pas log)", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        acheterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String requete = "PRESENCECADDIE#" + idArticleEnCours;
                String responce;

                //---------------------ENVOIE---------------------------//

                try
                {
                    DataOutputStream fluxSortie = new DataOutputStream(new BufferedOutputStream(sClient.getOutputStream()));
                    Send(requete,fluxSortie);
                }
                catch (IOException e1)
                {
                    System.err.println("Erreur de  : " + e1.getMessage());
                    System.exit(1);
                }

                //---------------------RECEPTION-----------------------//

                try
                {
                    DataInputStream fluxEntree = new DataInputStream(new BufferedInputStream(sClient.getInputStream()));
                    responce = Receive(fluxEntree);

                    String[] data = responce.split("#");
                    boolean estPresent = false;

                    if(data[1].equals("ok"))
                        estPresent = true;

                    if((nbArticlePanier < 5 || estPresent) && getQuantite() != 0)
                    {
                        //ici c'est la gesiton de l'achat
                        requete = "ACHAT#" + idArticleEnCours + "#" + getQuantite();

                        //---------------------ENVOIE---------------------------//

                        try
                        {
                            DataOutputStream fluxSortie = new DataOutputStream(new BufferedOutputStream(sClient.getOutputStream()));
                            Send(requete,fluxSortie);
                        }
                        catch (IOException e1)
                        {
                            System.err.println("Erreur de  : " + e1.getMessage());
                            System.exit(1);
                        }

                        //---------------------RECEPTION-----------------------//
                        try
                        {
                            DataInputStream fluxEntreeAchat = new DataInputStream(new BufferedInputStream(sClient.getInputStream()));
                            responce = Receive(fluxEntreeAchat);

                            //vérification de si l'achat a échoué

                            String[] dataAchat = responce.split("#");

                            if(dataAchat[1].equals("ko"))
                            {
                                System.err.println("Erreur de dans la requete achat !");
                                JOptionPane.showMessageDialog(null, "L'achat a echoue !", "Erreur", JOptionPane.ERROR_MESSAGE);
                            }

                            gestionCaddie();

                            if(!estPresent)
                            {
                                nbArticlePanier++;
                            }

                        } catch (Exception e3)
                        {
                            System.err.println("Erreur de  : " + e3.getMessage());
                            System.exit(1);
                        }
                    }
                    else
                    {
                        if(nbArticlePanier == 5)
                        {
                            JOptionPane.showMessageDialog(null, "Nombre maximum d'achat atteint !", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                        if(getQuantite() == 0)
                        {
                            JOptionPane.showMessageDialog(null, "Veuillez choisir une quantité valide !", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (Exception e2)
                {
                    System.err.println("Erreur de  : " + e2.getMessage());
                    System.exit(1);
                }
            }
        });

        supprimerArticleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectRow = PanierTable.getSelectedRow();
                if(selectRow == -1)
                {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner une ligne", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    String requete = "CANCEL#" + selectRow;
                    String responce = "";

                    //---------------------ENVOIE---------------------------//

                    try
                    {
                        DataOutputStream fluxSortie = new DataOutputStream(new BufferedOutputStream(sClient.getOutputStream()));
                        Send(requete,fluxSortie);
                    }
                    catch (IOException e1)
                    {
                        System.err.println("Erreur de  : " + e1.getMessage());
                        System.exit(1);
                    }

                    //---------------------RECEPTION-----------------------//

                    try
                    {
                        DataInputStream fluxEntreeCaddie = new DataInputStream(new BufferedInputStream(sClient.getInputStream()));
                        responce = Receive(fluxEntreeCaddie);
                    } catch (Exception e1)
                    {
                        System.err.println("Erreur de  : " + e1.getMessage());
                        System.exit(1);
                    }

                    String[] data = responce.split("#");

                    if(data[1].equals("ok"))
                    {
                        JOptionPane.showMessageDialog(null, "Suppression reussie !", "CANCEL", JOptionPane.INFORMATION_MESSAGE);
                        nbArticlePanier--;

                        videTablePanier();

                        setTotTxt("");

                        //--------------------gestion du caddie

                        gestionCaddie();

                        JOptionPane.showMessageDialog(null, "Suppression reussie !", "CANCELALL", JOptionPane.INFORMATION_MESSAGE);

                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, data[2], "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        viderLePanierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String requete = "CANCELALL", responce;

                //---------------------ENVOIE---------------------------//

                try
                {
                    DataOutputStream fluxSortie = new DataOutputStream(new BufferedOutputStream(sClient.getOutputStream()));
                    Send(requete,fluxSortie);
                }
                catch (IOException e1)
                {
                    System.err.println("Erreur de  : " + e1.getMessage());
                    System.exit(1);
                }

                //---------------------RECEPTION-----------------------//

                try
                {
                    DataInputStream fluxEntreeCaddie = new DataInputStream(new BufferedInputStream(sClient.getInputStream()));
                    responce = Receive(fluxEntreeCaddie);
                } catch (Exception e1)
                {
                    System.err.println("Erreur de  : " + e1.getMessage());
                    System.exit(1);
                }


                videTablePanier();
                prixTotal = 0.0F;

                setTotTxt("");
                nbArticlePanier = 0;
                JOptionPane.showMessageDialog(null, "Vidage du Caddie reussie !", "CANCELALL", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        confimerAchatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String requete = "CONFIRMER", responce = "";

                //---------------------ENVOIE---------------------------//

                try
                {
                    DataOutputStream fluxSortie = new DataOutputStream(new BufferedOutputStream(sClient.getOutputStream()));
                    Send(requete,fluxSortie);
                }
                catch (IOException e1)
                {
                    System.err.println("Erreur de  : " + e1.getMessage());
                    System.exit(1);
                }

                //---------------------RECEPTION-----------------------//

                try
                {
                    DataInputStream fluxEntreeCaddie = new DataInputStream(new BufferedInputStream(sClient.getInputStream()));
                    responce = Receive(fluxEntreeCaddie);
                } catch (Exception e1)
                {
                    System.err.println("Erreur de  : " + e1.getMessage());
                    System.exit(1);
                }

                String[] data = responce.split("#");
                if(data[1].equals("ok"))
                {
                    videTablePanier();
                    prixTotal = 0.0F;
                    setTotTxt("");
                    nbArticlePanier = 0;

                    JOptionPane.showMessageDialog(null, data[2], "CONFIMER", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    JOptionPane.showMessageDialog(null, data[2], "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



    }
    public static int nombreOccurrences(String lachaine)
    {
        int compter = 0;
        for (int i = 0; i < lachaine.length(); i++)
        {
            if('$' == lachaine.charAt(i))
            {
                compter++;
            }
        }
        return compter;
    }

    public static void main(String[] args)
    {
        FlatDarculaLaf.install(new FlatDarculaLaf());
        dialog = new ClientAchat();
        dialog.setVisible(true);
    }


    public void setNom(JTextField NomTxt)
    {
        this.NomTxt = NomTxt;
    }

    public void setTextFieldMotDePasse(JTextField MDPtxt)
    {
        this.MDPtxt = MDPtxt;
    }

    public void setTotTxt(String Totaltxt) {
        this.TotTxt.setText(Totaltxt);
    }

    public void setNom(String text)
    {
        if (text.isEmpty())
        {
            NomTxt.setText("");
        }
        else
        {
            NomTxt.setText(text);
        }
    }

    public String getNom()
    {
        return NomTxt.getText();
    }


    public void setMotDePasse(String text)
    {
        if (text.isEmpty())
        {
            MDPtxt.setText("");
        } else
        {
            MDPtxt.setText(text);
        }
    }

    public String getMotDePasse()
    {
        return MDPtxt.getText();
    }
}



