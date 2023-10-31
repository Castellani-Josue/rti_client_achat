package InterfaceGraphique;

import Properties.PropertiesPerso;
import Socket.socket;
import com.formdev.flatlaf.FlatDarculaLaf;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.TableCellRenderer;



public class ClientAchat extends JFrame
{
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


    public static final String SERVER_IP = "192.168.146.128";
    public static final int SERVER_PORT = 50000;


    private boolean logged = false;
    //int sClient;
    Socket sClient;

    public String getNom()
    {
        String nom = NomTxt.getText();
        return nom;
    }

    public String getMotDePasse()
    {
        String mdp = MDPtxt.getText();
        return mdp;
    }

    public int isNouveauClientChecked()
    {
        if (nouveauCliChecbox.isSelected())
        {
            return 1;
        }
        return 0;
    }

    public static Socket ClientSocket()
    {
        Socket socket = new Socket();

        try
        {
            socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
            System.out.println("Connecté au serveur.");

            return socket;
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }


    public boolean OVESP_Login(String user, String password, int NouveauClient, Socket sClient)
    {
        int off = 0;
        boolean onContinue = true;
        String requete, reponse;

        try {
            // Construction de la requête
            requete = "LOGIN#" + user + "#" + password + "#" + NouveauClient;

            // Envoi de la requête + réception de la réponse
            PrintWriter out = new PrintWriter(sClient.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(sClient.getInputStream()));

            // Envoi de la requête
            out.println(requete);

            // Attente de la réponse
            reponse = in.readLine();

            if (reponse == null) {
                System.out.println("Serveur arrêté, aucune réponse reçue...");
                //sClient.close();
                System.exit(1);
            }

            // Parsing de la réponse
            String[] parts = reponse.split("#");
            String statut = parts[1];

            if ("ok".equals(statut)) {
                String resultat = parts[2];
                System.out.println("Résultat = " + resultat);
                // Vous pouvez effectuer des opérations d'interface utilisateur ici

                // Envoi d'une nouvelle requête CONSULT#1
                requete = "CONSULT#1";
                out.println(requete);
                reponse = in.readLine();

                if (reponse == null) {
                    System.out.println("Serveur arrêté, aucune réponse reçue...");
                    //sClient.close();
                    System.exit(1);
                }

                String action = reponse.split("#")[1];
                if ("ok".equals(action)) {
                    int id, stock;
                    String intitule, image;
                    float prix;

                    id = Integer.parseInt(reponse.split("#")[2]);
                    intitule = reponse.split("#")[3];
                    prix = Float.parseFloat(reponse.split("#")[4].replace(',', '.'));
                    stock = Integer.parseInt(reponse.split("#")[5]);
                    image = reponse.split("#")[6];

                    System.out.println("id = " + id);
                    System.out.println("intitule = " + intitule);
                    System.out.println("prix = " + prix);
                    System.out.println("stock = " + stock);
                    System.out.println("image = " + image);

                    // Vous pouvez effectuer des opérations d'interface utilisateur ici
                } else {
                    String erreur = reponse.split("#")[2];
                    System.out.println("Erreur: " + erreur);
                    onContinue = false;
                }
            } else {
                String erreur = parts[2];
                System.out.println("Erreur: " + erreur);
                if (erreur.equals("Mot de passe incorrecte !")) {
                    off++;
                    // Vous pouvez effectuer des opérations d'interface utilisateur ici
                    if (off == 3) {
                        onContinue = false;
                    }
                } else {
                    // Vous pouvez effectuer des opérations d'interface utilisateur ici
                    onContinue = false;
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur de communication avec le serveur : " + e.getMessage());
            onContinue = false;
        }

        return onContinue;
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
        Dimension Img = new Dimension(220,220);
        ImgLabel.setPreferredSize(Img);
        int width = PanierTable.getWidth();
        int newHeight = 150;
        Dimension panier = new Dimension(width,newHeight);
        //PanierTable.setPreferredSize(panier);
        PanierScroll.setPreferredSize(panier);
        /*Dimension labDim = new Dimension(300,200);
        ImgLabel.setPreferredSize(labDim);*/
        setContentPane(MainPanel);
        setLocationRelativeTo(null);
        pack();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1000,700);
        setLocation((screen.width - this.getSize().width)/2,(screen.height - this.getSize().height)/2);
        setResizable(false);

        Border border = new LineBorder(Color.BLACK, 1); // Couleur et largeur de la bordure
        AchatPanel.setBorder(border);
        PanierPanel.setBorder(border);
        ImgLabel.setBorder(border);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Intitulé");
        model.addColumn("Prix à l'unité");
        model.addColumn("Quantité");

        PanierTable.setModel(model);
        PanierTable.getTableHeader().setReorderingAllowed(false);


        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(Color.BLACK);
        // Appliquez la couleur aux header
        for (int i = 0; i < model.getColumnCount(); i++)
        {
            PanierTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }



        //ImageIcon imageIcon = new ImageIcon(ClientAchat.class.getResource("resources/"));
        //ImgLabel.setIcon(imageIcon);
        //ImgLabel = new JLabel(new ImageIcon(imagepath));

        String directoryPath = "C:\\Users\\josue\\Java_Project_2023_2024\\rti_client_achat\\resources";

        // Liste des fichiers dans le répertoire
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files != null && files.length > 0)
        {
            for (File file : files) {
                if (file.isFile())
                {
                    try
                    {
                        // Chargez l'image depuis le fichier
                        BufferedImage img = ImageIO.read(file);

                        // Créez une ImageIcon à partir de l'image
                        ImageIcon imageIcon = new ImageIcon(img);
                        ImgLabel.setIcon(imageIcon);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }


        ImageIcon logoIcon = new ImageIcon(PropertiesPerso.PropertiesTest());
        setIconImage(logoIcon.getImage());


        LoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                char[] login = new char[50];
                char[] mdp = new char[50];
                int nouveauClient = isNouveauClientChecked();

                String user = getNom();
                String password = getMotDePasse();




                try
                {
                    sClient = ClientSocket();

                    System.out.println("Connecté au serveur.");

                    if (!OVESP_Login(user, password, nouveauClient, sClient)) {
                        System.err.println("Erreur dans OVESP_Login");
                        System.exit(1);
                    }
                } catch (Exception e2)
                {
                    System.err.println("Erreur de ClientSocket : " + e2.getMessage());
                    System.exit(1);
                }


                logged = true;
            }
        });


    }

    public static void main(String[] args)
    {
        FlatDarculaLaf.install(new FlatDarculaLaf());
        ClientAchat dialog = new ClientAchat();
        dialog.setVisible(true);
    }


}
