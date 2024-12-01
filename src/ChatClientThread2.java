//Source:
//  Creating a simple Chat Client/Server Solution 
//  http://pirate.shu.edu/~wachsmut/Teaching/CSAS2214/Virtual/Lectures/chat-client-server.html


import java.net.*;
import java.io.*;

/**
 * The ChatClientThread2 class represents a client-side thread that listens for messages from the server.
 * This thread connects with the server and communicates with the SecondForm to handle received messages.
 *
 */
public class ChatClientThread2 extends Thread
{

    private Socket socket = null;
    private SecondForm secondForm = null;  // Changed from ChatClient2 to SecondForm
    private DataInputStream streamIn = null;

    /**
     * Constructs a ChatClientThread2 with the specified SecondForm and socket.
     *
     * @param secondForm the form that handles client actions
     * @param socket     the socket connection to the server
     */
    public ChatClientThread2(SecondForm secondForm, Socket socket)
    {
        // Constructor with SecondForm and Socket
        this.secondForm = secondForm;
        this.socket = socket;
        try
        {
            streamIn = new DataInputStream(socket.getInputStream());
        }
        catch (IOException e)
        {
            System.out.println("Error initializing ChatClientThread2: " + e.getMessage());
            secondForm.close();  // Close connection on error
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
            secondForm.close();  // Assuming close() method exists in SecondForm to handle errors
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
                System.out.println("Received from server: " + message);
                secondForm.handle(message);  // Pass the message to SecondForm’s handle method
            }
        }
        catch (IOException ioe)
        {
            System.out.println("Listening error in ChatClientThread2: " + ioe.getMessage());
            secondForm.close();  // Handle disconnection in SecondForm
        }
    }
}
