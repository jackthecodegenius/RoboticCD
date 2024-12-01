//Source:
//  Creating a simple Chat Client1/Server Solution 
//  http://pirate.shu.edu/~wachsmut/Teaching/CSAS2214/Virtual/Lectures/chat-client-server.html
import java.net.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;


/**
 * The ChatClientThread1 class represents a client-side thread that listens for messages from the server.
 * This thread connects with the server and communicates with the MainForm to handle received messages.
 *
 */

public class ChatClientThread1 extends Thread
{

    private Socket socket;
    private MainForm mainForm;  // Changed from ChatClient1 to MainForm
    private DataInputStream streamIn = null;

    /**
     * Constructs a ChatClientThread1 with the specified main form and socket.
     *
     * @param mainForm the main form that handles client actions
     * @param socket   the socket connection to the server
     */
    public ChatClientThread1(MainForm mainForm, Socket socket)
    {
        this.mainForm = mainForm;
        this.socket = socket;
        try
        {
            streamIn = new DataInputStream(socket.getInputStream());
        }
        catch (IOException e)
        {
            System.out.println("Error initializing ChatClientThread1: " + e.getMessage());
        }
    }

    public void open()
    {
        try
        {
            streamIn = new DataInputStream(socket.getInputStream());
        }
        catch (IOException ioe)
        {
            System.out.println("Error getting input stream: " + ioe);
            mainForm.close();  // Assuming close() method exists in MainForm, to handle errors
        }
    }

    public void close()
    {
        try
        {
            if (streamIn != null) streamIn.close();
        }
        catch (IOException ioe)
        {
            System.out.println("Error closing input stream: " + ioe);
        }
    }

    @Override
    public void run()
    {
        try
        {
            while (true)
            {
                String message = streamIn.readUTF();
                System.out.println("Received from server in CLient 1: " + message);

                // Check if message indicates SecondForm is connected
                if (message.equals("SecondForm connected"))
                {
                    mainForm.setSecondFormConnected(true); // Use the setter to update the status
                }
                else
                {
                    mainForm.handle(message);
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Connection error in ChatClientThread1: " + e.getMessage());
        }


    }

    public void sendRetrieveMessage()
    {
        try
        {
            socket.getOutputStream().write("RETRIEVE_CD".getBytes("UTF-8"));
            socket.getOutputStream().flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
