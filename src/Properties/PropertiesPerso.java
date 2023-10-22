package Properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesPerso
{
    private static  final String CONFIG_FILE = "Image.properties";


    public static String PropertiesTest()
    {

        try {

            Properties prop = new Properties();
            FileInputStream fis =  new FileInputStream(CONFIG_FILE);
            prop.load(fis);
            //prop.list(System.out);



            String imagePath2 = prop.getProperty("image2.path");

            return imagePath2;




        }
        catch(FileNotFoundException e)
        {
            System.out.println("Erreur ! Fichier non trouve...");
        }
        catch(IOException e)
        {
            System.out.println("Erreur IO !");
        }

        return null;
    }

    public static String PropertiesTest2()
    {
        try {

            Properties prop = new Properties();
            FileInputStream fis =  new FileInputStream(CONFIG_FILE);
            prop.load(fis);
            //prop.list(System.out);



            String imagePath1 = prop.getProperty("image1.path");

            return imagePath1;




        }
        catch(FileNotFoundException e)
        {
            System.out.println("Erreur ! Fichier non trouve...");
        }
        catch(IOException e)
        {
            System.out.println("Erreur IO !");
        }

        return null;
    }



}
