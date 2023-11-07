package Socket;


import javax.net.SocketFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class socket
{

    public static final String SERVER_IP = "192.168.137.129" ;
    public static final int SERVER_PORT = 50000;
    public static Socket ClientSocket()
    {


        try
        {

            Socket socket = new Socket(SERVER_IP,SERVER_PORT);
            //socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 2000);
            System.out.println("Connecté au serveur.");

            return socket;
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private socket()
    {
        //empêche l'instanciation de la classe
    }
    public static void Send(String message, DataOutputStream envoi) throws IOException
    {
        message = message + "#)";
        envoi.write(message.getBytes());
        envoi.flush();
        System.out.println("Réponse envoyée");
    }

    public static String Receive(DataInputStream reception) throws IOException
    {

        StringBuffer buffer = new StringBuffer();
        boolean EOT = false;
        while(!EOT)
        {
            byte b1 = reception.readByte();
            System.out.println("b1 : --" + (char)b1 + "--");
            if(b1 == (byte)'#')
            {
                byte b2 = reception.readByte();
                System.out.println("b2 : --" + (char)b2 + "--");
                if(b2 ==(byte)')')
                    EOT = true;
                else
                {
                    buffer.append((char)b1);
                    buffer.append((char)b2);
                }
            }
            else buffer.append((char)b1);
        }
        String requete = buffer.toString();
        System.out.println("Reçu : --" + requete + "--");
        return requete;
    }

}
