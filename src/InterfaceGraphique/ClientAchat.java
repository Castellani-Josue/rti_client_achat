package InterfaceGraphique;

import Properties.PropertiesPerso;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
        headerRenderer.setBackground(Color.PINK);
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




    }

    public static void main(String[] args) {
        ClientAchat dialog = new ClientAchat();
        dialog.setVisible(true);
    }


}
