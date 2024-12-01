import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AbstractDocument;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The SecondForm class represents a GUI for managing and displaying CD data,
 * connecting to a server, and receiving updates from MainForm via the ChatClientThread2.
 */
public class SecondForm extends JFrame implements ActionListener {

JButton btnProcess, btnAddItem, btnExit, btnDropDown, btnConnect;
JLabel  lblMessage, lblAutomation, lblCurrent, lblRandom, lblBarcodes, lblSections, lblArchive, lblTitle, lblAuthor, lblSection, lblX, lblY, lblBarcode, lblDescription;
JTextField txtCurrent, txtBarcode, txtSection;
JTable table;
MyModel wordModel;
JComboBox<String> txtCurrents;

private DList selectedCDList;


SpringLayout layout = new SpringLayout(); // Moved layout outside to be accessible globally

    private Socket socket;
    private DataOutputStream streamOut;
    private ChatClientThread2 clientThread;
    private String serverName = "localhost";
    private int serverPort = 4444;
    private JLabel retrievalLabel; // Declare label to display retrieval message


    /**
     * Constructor for SecondForm, initializing the window size, layout, and components.
     * Sets up buttons, labels, and the table, and configures event listeners.
     */
public SecondForm()
{
    this.setSize(700, 500);
    this.setLocationRelativeTo(null); // Center the window
    this.setLayout(layout); // Set the layout for the frame

    // Create the labels and add them to the newItemFrame instead of 'this'
    lblAutomation = UIBuilderLibrary.BuildJLabelWithNorthWestAnchor("Automation Console                                                                                                                                ", 0, 10, layout, this);
    lblAutomation.setOpaque(true);
    Color rgbColor = new Color(0, 114, 188);
    lblAutomation.setBackground(rgbColor);
    lblAutomation.setForeground(Color.WHITE);
    Font font = lblAutomation.getFont();
    lblAutomation.setFont(font.deriveFont(25f));
    lblAutomation.setHorizontalAlignment(SwingConstants.CENTER);
    add(lblAutomation); // Add label to new frame

    lblCurrent = UIBuilderLibrary.BuildJLabelInlineBelow("Current Requested Action:", 30, layout, lblAutomation);
    lblCurrent.setForeground(new Color(0, 114, 188));
    add(lblCurrent);

    txtCurrents = new JComboBox<>(new String[] {"Add", "Remove", "Retrieve", "Return", "Sort"});
    layout.putConstraint(SpringLayout.WEST, txtCurrents, 15, SpringLayout.EAST, lblCurrent);
    layout.putConstraint(SpringLayout.NORTH, txtCurrents, -18, SpringLayout.SOUTH, lblCurrent);
    add(txtCurrents);


    btnProcess = UIBuilderLibrary.BuildJButtonInlineToRight(80,18,"Process",150,this,layout,lblCurrent);
    btnProcess.setMargin(new Insets(0,0,0,0));
    add(btnProcess);
    btnConnect = UIBuilderLibrary.BuildJButtonInlineToRight(80,18,"Connect",15,this,layout,btnProcess);
    btnConnect.setMargin(new Insets(0,0,0,0));
    add(btnConnect);


    lblBarcode = UIBuilderLibrary.BuildJLabelInlineBelow("Bar Code of Selected Item:", 20, layout, lblCurrent);
    lblBarcode.setForeground(new Color(0, 114, 188));
    add(lblBarcode);
    txtBarcode = UIBuilderLibrary.BuildJTextFieldInlineToRight(11, 5, layout, lblBarcode);
    add(txtBarcode);

    lblSections = UIBuilderLibrary.BuildJLabelInlineToRight("Section:", 130, layout, lblBarcode);
    lblSections.setForeground(new Color(0, 114, 188));
    add(lblSections);
    txtSection = UIBuilderLibrary.BuildJTextFieldInlineToRight(5, 5, layout, lblSections);
    add(txtSection);
    btnAddItem = UIBuilderLibrary.BuildJButtonInlineToRight(80,18,"Add Item",15,this,layout,txtSection);
    btnAddItem.setMargin(new Insets(0,0,0,0));
    add(btnAddItem);

    lblMessage = UIBuilderLibrary.BuildJLabelInlineBelow("No active request", 400, layout, lblAutomation);
    lblMessage.setForeground(new Color(0, 114, 188));
    add(lblMessage);


    setupTable();  // Call the method to set up the table

    // Set the frame visible
    this.setVisible(true);


}


    /**
     * Establishes a connection to the server using the specified server name and port.
     * Starts a thread for listening to incoming messages from the server.
     */
    private void connectToServer()
    {
        if (socket != null && !socket.isClosed())
        {
            System.out.println("Already connected to server.");
            return;
        }

        try
        {
            socket = new Socket(serverName, serverPort);
            streamOut = new DataOutputStream(socket.getOutputStream());

            clientThread = new ChatClientThread2(this, socket);
            clientThread.start();

            btnProcess.setEnabled(true);
            System.out.println("SecondForm connected to server at " + serverName + ":" + serverPort);


            open();
        }
        catch (IOException e)
        {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }

    /**
     * Initializes the output stream for the socket connection and starts the client thread.
     */
    public void open()
    {
        try
        {
            streamOut = new DataOutputStream(socket.getOutputStream());
            clientThread = new ChatClientThread2(this, socket);
        }
        catch (IOException ioe)
        {
            System.out.println("Error opening output stream: " + ioe);
        }
    }


    /**
     * Sends a message to the server through the established socket connection.
     *
     * @param message The message to send to the server.
     */
    private void sendMessage(String message)
    {
        try
        {
            if (streamOut != null)
            {
                streamOut.writeUTF(message);
                streamOut.flush();
            }
            else
            {
                System.out.println("Stream not available for sending message: " + message);
            }
        }
        catch (IOException e)
        {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }



    /**
     * Handles incoming messages from the server, updates UI components, and processes actions.
     *
     * @param message The message received from the server.
     */

    // This method will be called by ChatClientThread2 when it receives a message
    public void handle(String message)
    {
        System.out.println("Received message: " + message);  // Debug output to verify reception

        if (message.equals(".bye"))
        {
            println("Good bye. Press EXIT button to exit ...");
            close();

        }
        else
        {
            // Display the original message exactly as received
            println(message);

            String[] Temp = message.split(": ");
            if (Temp.length > 2 && Temp[1].equals("MAIN"))
            {
                String action = Temp[2];
                System.out.println("Action received: " + action);  // Debug output to confirm action parsing

                if (action.equals("UpdateRow"))
                {

                    String id = Temp[3];
                    String title = Temp[4];
                    String author = Temp[5];
                    String section = Temp[6];
                    String x = Temp[7];
                    String y = Temp[8];
                    String barcode = Temp[9];
                    String description = Temp[10];
                    String onLoan = Temp[11];

                    Object[] rowData = { id, title, author, section, x, y, barcode, description, Boolean.parseBoolean(onLoan) };

                    // Verify parsed row data
                    System.out.println("Adding row: " + java.util.Arrays.toString(rowData));

                    MyModel model = (MyModel) table.getModel();
                    model.addRow(rowData);
                    // table update
                    model.fireTableDataChanged();
                }


                // Handle "Add To Collection" action
                else if (action.equals("Add To Collection"))
                {
                    txtCurrents.setSelectedItem("Add");
                    txtBarcode.setText(Temp[5]);
                    txtSection.setText(Temp[6]);
                }
                else
                {
                    txtCurrents.setSelectedItem(action);
                    txtBarcode.setText(Temp[5]);
                    txtSection.setText(Temp[6]);
                }

            }
        }
    }

    /**
     * Updates the message label with a given string.
     *
     * @param msg The message to display on the label.
     */
    void println(String msg)
    {
        //display.appendText(msg + "\n");
        lblMessage.setText(msg);
    }

    /**
     * Closes the server connection and interrupts the client thread.
     */
    // Close connection and stop the thread
    public void close()
    {
        try
        {
            if (streamOut != null) streamOut.close();
            if (socket != null) socket.close();
        }
        catch (IOException e)
        {
            System.out.println("Error closing connection: " + e.getMessage());
        }

        if (clientThread != null)
        {
            clientThread.interrupt();
            // Stop the thread
        }
    }

    /**
     * Sets up the JTable to display CD data with appropriate headers and initializes it with no data.
     * Configures selection properties and adds the table to a scroll pane.
     */
    // Inside SecondForm
    private void setupTable() {
        // Define the column names
        String[] columnNames = { "ID", "Title", "Author", "Section", "X", "Y", "Barcode", "Description", "On-Loan" };

        // Provide an empty ArrayList for initial data
        ArrayList<Object[]> emptyData = new ArrayList<>();

        // Initialize the table model with headers only
        wordModel = new MyModel(emptyData, columnNames);
        table = new JTable(wordModel);

        // Set selection properties
        table.setSelectionForeground(Color.white);
        table.setSelectionBackground(Color.blue);

        // Wrap the table in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(650, 200));
        layout.putConstraint(SpringLayout.NORTH, scrollPane, 20, SpringLayout.SOUTH, btnAddItem);
        layout.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, this.getContentPane());
        layout.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.EAST, this.getContentPane());
        layout.putConstraint(SpringLayout.SOUTH, scrollPane, -80, SpringLayout.SOUTH, this.getContentPane());
        this.add(scrollPane);
    }


    /**
     * Handles button click events in the SecondForm, such as connecting to the server
     * and processing actions like adding or retrieving items.
     *
     * @param e The ActionEvent triggered by a button click.
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == btnConnect)
        {
            // Connect to the server when the Connect button is clicked
            connectToServer();
            System.out.println("SecondForm connected to server.");
        }

        if (e.getSource() == btnProcess)
        {

            // Check if connection is established
            if (socket == null || socket.isClosed())
            {
                System.out.println("Error: SecondForm is not connected to the server.");
                return;
            }

            try
            {
                System.out.println(socket);
                String action = txtCurrents.getSelectedItem().toString();
                System.out.println(action);
                // Generate a log message based on the current form inputs
                String barcode = txtBarcode.getText().trim();
                String section = txtSection.getText().trim();
                String logMessage = action+" - Barcode: " + barcode + ", Section: " + section;

                // Send the log message to the MainForm via the server
                String serverMessage = "SECOND: "+action + "-" +barcode + ": " + section;
                sendMessage(serverMessage);

                // Debugging output
                System.out.println("Log generated and sent to server from SecondForm: " + logMessage);
            }
            catch (Exception ex)
            {
                System.out.println("Error processing btnProcess in SecondForm: " + ex.getMessage());
            }


        }

    }

}
