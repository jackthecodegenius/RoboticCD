/**
 * Interface representing a basic application with start and shutdown functionality.
 */
public interface IApp {

    /**
     * Starts the application. This method should contain all necessary initialization
     * and setup required to run the application.
     */
    void start();

    /**
     * Shuts down the application. This method should handle any necessary cleanup
     * operations, such as releasing resources or saving data before exit.
     */
    void shutDown();
}
