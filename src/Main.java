
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * The Main class contains the entry point of the application.
 * It creates an instance of the MainForm class to start the application.
 * It also starts a ChatServer and sets up a graceful shutdown process.
 */

public class Main implements IApp
{
    /**
     * The main method serves as the entry point of the application.
     * It starts the ChatServer in a separate daemon thread, initializes the MainForm,
     * and registers a shutdown hook for graceful application termination.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args)
    {
        // Start the ChatServer in a separate thread
        Thread serverThread = new Thread(() ->
        {
            try
            {
                // I used the port the one in the chatserver
                ChatServer chatServer = new ChatServer(4444);
                chatServer.start();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();

        // Initialize MainForm and SecondForm
        SwingUtilities.invokeLater(() ->
        {
            try
            {
                System.out.println("Initializing MainForm");
                new MainForm();    // Load MainForm

                IApp app = new Main();
                ShutdownInterceptor shutdownInterceptor = new ShutdownInterceptor(app);
                Runtime.getRuntime().addShutdownHook(shutdownInterceptor);
                app.start();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    public void start() {

        try {
            System.out.println("Starting Graceful Shutdown");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void shutDown() {
        // Do a graceful shutdown here

        try {
            System.out.println("Shutdown is called");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}