/**
 * ShutdownInterceptor is a thread that triggers a graceful shutdown process for an application
 * implementing the IApp interface. It is typically used as a shutdown hook to ensure
 * any necessary cleanup or final actions are performed when the JVM is shutting down.
 */
public class ShutdownInterceptor extends Thread
{

    private IApp app;

    /**
     * Constructs a ShutdownInterceptor with the specified IApp instance.
     *
     * @param app An instance of an application that implements the IApp interface.
     */
    public ShutdownInterceptor(IApp app) {
        this.app = app;
    }

    public void run() {
        System.out.println("Call the shutdown routine");
        app.shutDown();
    }
}
