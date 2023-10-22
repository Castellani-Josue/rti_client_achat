package InterfaceGraphique;

import Properties.PropertiesPerso;

import javax.swing.*;
import java.awt.*;

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
    private JList list1;
    private JButton confimerAchatButton;
    private JButton supprimerArticleButton;
    private JButton viderLePanierButton;
    private JLabel TotalLabel;
    private JTextField TotTxt;

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

        ImageIcon logoIcon = new ImageIcon(PropertiesPerso.PropertiesTest());
        setIconImage(logoIcon.getImage());




    }

    public static void main(String[] args) {
        ClientAchat dialog = new ClientAchat();
        dialog.setVisible(true);
    }


}
