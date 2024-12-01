
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import javax.swing.JFrame;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AbstractDocument;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * The MainForm class represents the main interface for managing CD entries, sorting, filtering, and interacting with
 * a second form. It includes options for adding, updating, displaying, and sorting data within a JTable.
 */
public class MainForm extends JFrame implements ActionListener
{

    /**
     * Constructor for MainForm. Initializes the layout, creates GUI components,
     * sets up listeners, and configures the row sorter for filtering table data.
     */

    JButton btnConnect, btnTitle, btnAuthor, btnBarcode, btnNewitem, btnUpdate, btnProcess, btnPreorder, btnInorder, btnPostorder, btnSave, btnDisplay, btnRetrieve, btnRemove, btnReturn, btnCollection, btnRandom, btnMostly, btnReverse, btnExit, btnProcessSecond, btnAddItem, btnExitSecond, btnDropDown;
    JLabel lblID, lblOnloans, lblString, lblArchive, lblTitle, lblAuthor, lblSection, lblX, lblY, lblBarcode, lblDescription, lblOnloan, lblTitles, lblAuthors, lblSections, lblXs, lblYs, lblBarcodes, lblDescriptions, lblProcesslog, lblDisplaybinary, lblHashmap, lblAutomation, lblSort, lblArchiveconsole, lblWhite, lblFind, lblSortSection, lblAutomationConsole, lblCurrent, lblBarcodeSelected, lblRandom, lblSectionSecond;
    JTextField txtID, txtSearch, txtTitle, txtAuthor, txtSection, txtX, txtY, txtBarcode, txtFind, txtSort, txtBarcodeSelected, txtCurrent, txtSectionSecond;
    JTextArea txtDescription, txtProcesslog;
    JComboBox<String> txtOnloans;
    SpringLayout layout = new SpringLayout();
    JTable table;
    MyModel wordModel;
    DList cdList;
    private TableRowSorter<MyModel> rowSorter;
    BinaryTree binaryTree = new BinaryTree();
    private Socket socket;
    private DataOutputStream streamOut;
    private ChatClientThread1 clientThread; // To listen to server messages
    private String serverName = "localhost";
    private int serverPort = 4444;
    private boolean isSecondFormConnected = false;  // Add this field to track SecondForm connection status
    private SecondForm secondForm;
    private List<String> processLogMessages = new ArrayList<>(); // Store specific action logs
    private boolean showingProcessLogOnly = false;
    private HashMap<Integer, String> treeMap = new HashMap<>();

    /**
     * Constructs the MainForm, the primary graphical user interface for managing CD entries.
     * Initializes the JFrame with a specific size and layout, sets up GUI components such as buttons,
     * labels, text fields, a table, and other interactive elements.
     * Configures event listeners for user actions and prepares data structures for processing CD information.
     * This form also supports server communication and interaction with a secondary form.
     */
    public MainForm()
    {

        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(layout);
        cdList = new DList();

        System.out.println("MainForm initialized.");


        WordAssociationTable(layout);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        lblArchiveconsole = UIBuilderLibrary.BuildJLabelWithNorthWestAnchor("Archive Console                                                                                                                     ", 0, 10, layout, this);

        lblArchiveconsole.setOpaque(true);
        Color rgbColor = new Color(0, 114, 188);
        lblArchiveconsole.setBackground(rgbColor);
        lblArchiveconsole.setForeground(Color.WHITE);
        Font font = lblArchiveconsole.getFont();
        lblArchiveconsole.setFont(font.deriveFont(25f));
        lblArchiveconsole.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblArchiveconsole);

        lblString = UIBuilderLibrary.BuildJLabelInlineBelow("Search String:", 5, layout, lblArchiveconsole);
        lblString.setForeground(new Color(0, 114, 188));
        add(lblString);
        txtSearch = UIBuilderLibrary.BuildJTextFieldInlineToRight(10, 5, layout, lblString);
        add(txtSearch);
        lblWhite = UIBuilderLibrary.BuildJLabelInlineBelow("Title", 15, layout, lblString);
        lblWhite.setForeground(new Color(238, 238, 238));
        add(lblWhite);
        lblArchive = UIBuilderLibrary.BuildJLabelInlineToRight("Archive CDs", 200, layout, lblWhite);
        lblArchive.setForeground(new Color(0, 114, 188));
        add(lblArchive);


        // Top left table
        lblTitle = UIBuilderLibrary.BuildJLabelInlineBelow("Title", 50, layout, lblString);
        lblTitle.setForeground(new Color(0, 114, 188));
        add(lblTitle);
        lblAuthor = UIBuilderLibrary.BuildJLabelInlineToRight("Author", 60, layout, lblTitle);
        lblAuthor.setForeground(new Color(0, 114, 188));
        add(lblAuthor);
        lblSection = UIBuilderLibrary.BuildJLabelInlineToRight("Section", 50, layout, lblAuthor);
        lblSection.setForeground(new Color(0, 114, 188));
        add(lblSection);
        lblX = UIBuilderLibrary.BuildJLabelInlineToRight("X", 25, layout, lblSection);
        lblX.setForeground(new Color(0, 114, 188));
        add(lblX);
        lblY = UIBuilderLibrary.BuildJLabelInlineToRight("Y", 25, layout, lblX);
        lblY.setForeground(new Color(0, 114, 188));
        add(lblY);
        lblBarcode = UIBuilderLibrary.BuildJLabelInlineToRight("Barcode", 40, layout, lblY);
        lblBarcode.setForeground(new Color(0, 114, 188));
        add(lblBarcode);
        lblDescription = UIBuilderLibrary.BuildJLabelInlineToRight("Description", 30, layout, lblBarcode);
        lblDescription.setForeground(new Color(0, 114, 188));
        add(lblDescription);
        lblOnloan = UIBuilderLibrary.BuildJLabelInlineToRight("On-Loan", 50, layout, lblDescription);
        lblOnloan.setForeground(new Color(0, 114, 188));
        add(lblOnloan);
        lblSort = UIBuilderLibrary.BuildJLabelInlineBelow("Sort:", 200, layout, lblTitle);
        lblSort.setForeground(new Color(0, 114, 188));
        add(lblSort);

        btnTitle = UIBuilderLibrary.BuildJButtonInlineToRight(80, 20, "By Title", 20, this, layout, lblSort);
        btnTitle.setMargin(new Insets(0, 0, 0, 0));
        add(btnTitle);
        btnAuthor = UIBuilderLibrary.BuildJButtonInlineToRight(80, 20, "By Author", 2, this, layout, btnTitle);
        btnAuthor.setMargin(new Insets(0, 0, 0, 0));
        add(btnAuthor);
        btnBarcode = UIBuilderLibrary.BuildJButtonInlineToRight(80, 20, "By Barcode", 200, this, layout, btnAuthor);
        btnBarcode.setMargin(new Insets(0, 0, 0, 0));
        add(btnBarcode);

        // top right table
        lblID = UIBuilderLibrary.BuildJLabelInlineToRight("ID:", 482, layout, txtSearch);
        lblID.setForeground(new Color(0, 114, 188));
        add(lblID);
        lblTitles = UIBuilderLibrary.BuildJLabelInlineToRight("Title:", 375, layout, lblArchive);
        lblTitles.setForeground(new Color(0, 114, 188));
        add(lblTitles);
        lblAuthors = UIBuilderLibrary.BuildJLabelInlineBelow("Author:", 15, layout, lblTitles);
        lblAuthors.setForeground(new Color(0, 114, 188));
        add(lblAuthors);
        lblSections = UIBuilderLibrary.BuildJLabelInlineBelow("Section:", 15, layout, lblAuthors);
        lblSections.setForeground(new Color(0, 114, 188));
        add(lblSections);
        lblXs = UIBuilderLibrary.BuildJLabelInlineBelow("X:", 15, layout, lblSections);
        lblXs.setForeground(new Color(0, 114, 188));
        add(lblXs);
        lblYs = UIBuilderLibrary.BuildJLabelInlineBelow("Y:", 15, layout, lblXs);
        lblYs.setForeground(new Color(0, 114, 188));
        add(lblYs);
        lblBarcodes = UIBuilderLibrary.BuildJLabelInlineBelow("Barcodes:", 15, layout, lblYs);
        lblBarcodes.setForeground(new Color(0, 114, 188));
        add(lblBarcodes);
        lblDescriptions = UIBuilderLibrary.BuildJLabelInlineBelow("Description:", 15, layout, lblBarcodes);
        lblDescriptions.setForeground(new Color(0, 114, 188));
        add(lblDescriptions);
        lblOnloans = UIBuilderLibrary.BuildJLabelInlineBelow("On-Loan:", 25, layout, lblDescriptions);
        lblOnloans.setForeground(new Color(0, 114, 188));
        add(lblOnloans);

        txtID = UIBuilderLibrary.BuildJTextFieldInlineToRight(10, 58, layout, lblID);
        add(txtID);
        txtTitle = UIBuilderLibrary.BuildJTextFieldInlineToRight(10, 45, layout, lblTitles);
        add(txtTitle);
        txtAuthor = UIBuilderLibrary.BuildJTextFieldInlineBelow(10, 11, layout, txtTitle);
        add(txtAuthor);
        txtSection = UIBuilderLibrary.BuildJTextFieldInlineBelow(10, 11, layout, txtAuthor);
        add(txtSection);
        txtX = UIBuilderLibrary.BuildJTextFieldInlineBelow(10, 11, layout, txtSection);
        add(txtX);
        txtY = UIBuilderLibrary.BuildJTextFieldInlineBelow(10, 11, layout, txtX);
        add(txtY);
        txtBarcode = UIBuilderLibrary.BuildJTextFieldInlineBelow(10, 11, layout, txtY);
        add(txtBarcode);
        //txtTitle, txtAuthor, txtSection, txtX, txtY, txtBarcode, txtDescription
        txtDescription = new JTextArea();
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);

        JScrollPane constructionPanel = new JScrollPane(txtDescription);
        constructionPanel.setPreferredSize(new Dimension(160, 40));
        layout.putConstraint(SpringLayout.NORTH, constructionPanel, 162, SpringLayout.SOUTH, txtTitle);
        layout.putConstraint(SpringLayout.WEST, constructionPanel, 747, SpringLayout.WEST, this);
        add(constructionPanel);

        txtOnloans = new JComboBox<>(new String[]{"true", "false"});
        layout.putConstraint(SpringLayout.WEST, txtOnloans, 25, SpringLayout.EAST, lblOnloans);
        layout.putConstraint(SpringLayout.NORTH, txtOnloans, 290, SpringLayout.SOUTH, txtDescription);
        add(txtOnloans);

        ((AbstractDocument) txtID.getDocument()).setDocumentFilter(new NumericFilter());
        ((AbstractDocument) txtX.getDocument()).setDocumentFilter(new NumericFilter());
        ((AbstractDocument) txtY.getDocument()).setDocumentFilter(new NumericFilter());
        ((AbstractDocument) txtBarcode.getDocument()).setDocumentFilter(new NumericFilter());

        btnNewitem = UIBuilderLibrary.BuildJButtonInlineBelow(80, 20, "New Item", 55, this, layout, lblDescriptions);
        btnNewitem.setMargin(new Insets(0, 0, 0, 0));
        add(btnNewitem);
        btnUpdate = UIBuilderLibrary.BuildJButtonInlineToRight(80, 20, "Save/Update", 70, this, layout, btnNewitem);
        btnUpdate.setMargin(new Insets(0, 0, 0, 0));
        add(btnUpdate);


        // Bottom left area
        lblProcesslog = UIBuilderLibrary.BuildJLabelInlineBelow("Process Log:", 30, layout, lblSort);
        lblProcesslog.setForeground(new Color(0, 114, 188));
        add(lblProcesslog);

        txtProcesslog = new JTextArea();
        txtProcesslog.setLineWrap(true);
        txtProcesslog.setWrapStyleWord(true);

        JScrollPane processingPanel = new JScrollPane(txtProcesslog);
        processingPanel.setPreferredSize(new Dimension(620, 120));
        layout.putConstraint(SpringLayout.NORTH, processingPanel, 301, SpringLayout.SOUTH, txtTitle);
        layout.putConstraint(SpringLayout.WEST, processingPanel, 4, SpringLayout.WEST, this);
        add(processingPanel);

        btnProcess = UIBuilderLibrary.BuildJButtonInlineToRight(80, 20, "Process Log", 469, this, layout, lblProcesslog);
        btnProcess.setMargin(new Insets(0, 0, 0, 0));
        add(btnProcess);

        //Buttons under the process log
        lblDisplaybinary = UIBuilderLibrary.BuildJLabelInlineBelow("Display Binary Tree:", 140, layout, lblProcesslog);
        lblDisplaybinary.setForeground(new Color(0, 114, 188));
        add(lblDisplaybinary);
        btnPreorder = UIBuilderLibrary.BuildJButtonInlineToRight(80, 20, "Pre-Order", 10, this, layout, lblDisplaybinary);
        btnPreorder.setMargin(new Insets(0, 0, 0, 0));
        add(btnPreorder);
        btnInorder = UIBuilderLibrary.BuildJButtonInlineToRight(80, 20, "In-Order", 10, this, layout, btnPreorder);
        btnInorder.setMargin(new Insets(0, 0, 0, 0));
        add(btnInorder);
        btnPostorder = UIBuilderLibrary.BuildJButtonInlineToRight(80, 20, "Post-Order", 10, this, layout, btnInorder);
        btnPostorder.setMargin(new Insets(0, 0, 0, 0));
        add(btnPostorder);

        lblHashmap = UIBuilderLibrary.BuildJLabelInlineBelow("HashMap / Set:", 20, layout, lblDisplaybinary);
        lblHashmap.setForeground(new Color(0, 114, 188));
        add(lblHashmap);
        btnSave = UIBuilderLibrary.BuildJButtonInlineToRight(80, 20, "Save", 39, this, layout, lblHashmap);
        btnSave.setMargin(new Insets(0, 0, 0, 0));
        add(btnSave);
        btnDisplay = UIBuilderLibrary.BuildJButtonInlineToRight(80, 20, "Display", 10, this, layout, btnSave);
        btnDisplay.setMargin(new Insets(0, 0, 0, 0));
        add(btnDisplay);
        lblFind = UIBuilderLibrary.BuildJLabelInlineToRight("Find Barcode:", 130, layout, btnDisplay);
        lblFind.setForeground(new Color(0, 114, 188));
        add(lblFind);
        txtFind = UIBuilderLibrary.BuildJTextFieldInlineToRight(10, 10, layout, lblFind);
        add(txtFind);

        //Buttom Right Section of the application
        lblAutomation = UIBuilderLibrary.BuildJLabelInlineToRight("Automation Action Request For The Item Above:", 35, layout, btnProcess);
        lblAutomation.setForeground(new Color(0, 114, 188));
        add(lblAutomation);
        btnRetrieve = UIBuilderLibrary.BuildJButtonInlineBelow(120, 20, "Retrieve", 20, this, layout, lblAutomation);
        btnRetrieve.setMargin(new Insets(0, 0, 0, 0));
        add(btnRetrieve);

        btnRemove = UIBuilderLibrary.BuildJButtonInlineToRight(120, 20, "Remove", 20, this, layout, btnRetrieve);
        btnRemove.setMargin(new Insets(0, 0, 0, 0));
        add(btnRemove);
        btnReturn = UIBuilderLibrary.BuildJButtonInlineBelow(120, 20, "Return", 20, this, layout, btnRetrieve);
        btnReturn.setMargin(new Insets(0, 0, 0, 0));
        add(btnReturn);
        btnCollection = UIBuilderLibrary.BuildJButtonInlineToRight(120, 20, "Add To Collection", 20, this, layout, btnReturn);
        btnCollection.setMargin(new Insets(0, 0, 0, 0));
        add(btnCollection);
        lblSortSection = UIBuilderLibrary.BuildJLabelInlineBelow("Sort Section:", 20, layout, btnReturn);
        lblSortSection.setForeground(new Color(0, 114, 188));
        add(lblSortSection);
        txtSort = UIBuilderLibrary.BuildJTextFieldInlineToRight(10, 10, layout, lblSortSection);
        add(txtSort);
        btnRandom = UIBuilderLibrary.BuildJButtonInlineBelow(170, 20, "Random Collection Sort", 10, this, layout, txtSort);
        btnRandom.setMargin(new Insets(0, 0, 0, 0));
        add(btnRandom);
        btnMostly = UIBuilderLibrary.BuildJButtonInlineBelow(170, 20, "Mostly Sorted Sort", 10, this, layout, btnRandom);
        btnMostly.setMargin(new Insets(0, 0, 0, 0));
        add(btnMostly);
        btnReverse = UIBuilderLibrary.BuildJButtonInlineBelow(170, 20, "Reverse Order Sort", 10, this, layout, btnMostly);
        btnReverse.setMargin(new Insets(0, 0, 0, 0));
        add(btnReverse);
        btnConnect = UIBuilderLibrary.BuildJButtonInlineBelow(270, 20, "Connect", 215, this, layout, lblAutomation);
        btnConnect.setMargin(new Insets(0, 0, 0, 0));
        add(btnConnect);
        btnExit = UIBuilderLibrary.BuildJButtonInlineBelow(270, 20, "Exit", 240, this, layout, lblAutomation);
        btnExit.setMargin(new Insets(0, 0, 0, 0));
        add(btnExit);

        btnConnect.addActionListener(this);
        btnRetrieve.addActionListener(this);

        secondForm = new SecondForm();


        setupRowSorter();

        setVisible(true);

        setupListeners();
    }

    /**
     * Sets up listeners for various buttons and input fields to trigger appropriate actions,
     * such as filtering logs or sending data to the second form.
     */

    private void setupListeners()
    {
        btnRetrieve.addActionListener(e -> sendActionToSecondForm("Retrieve"));
        btnRetrieve.addActionListener(e -> sendSelectedRowToSecondForm());

        btnReturn.addActionListener(e -> sendActionToSecondForm("Return"));
        btnReturn.addActionListener(e -> sendSelectedRowToSecondForm());

        btnRemove.addActionListener(e -> sendActionToSecondForm("Remove"));
        btnRemove.addActionListener(e -> sendSelectedRowToSecondForm());


        btnCollection.addActionListener(e -> sendActionToSecondForm("Add To Collection"));
        btnCollection.addActionListener(e -> sendSelectedRowToSecondForm());

        btnDisplay.addActionListener(e -> displayHashMap());

        btnProcess.addActionListener(e ->
        {
            System.out.println("btnProcess clicked. Current process log size: " + processLogMessages.size());

            // Check contents of processLogMessages
            for (int i = 0; i < processLogMessages.size(); i++) {
                System.out.println("Log Entry [" + i + "]: " + processLogMessages.get(i));
            }

            // Call displayProcessLogMessages to refresh
            displayProcessLogMessages();

            // Confirm the update happened
            System.out.println("txtProcesslog updated. Displaying current content:");
            System.out.println(txtProcesslog.getText());
        });

        txtFind.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                filterProcessLogByBarcode();
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                filterProcessLogByBarcode();
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                filterProcessLogByBarcode();
            }
        });


        btnProcess.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayProcessLogMessages();
            }
        });

        btnProcess.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayProcessLogMessages();
            }
        });

        btnNewitem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Clear all the text fields
                txtID.setText("");
                txtTitle.setText("");
                txtAuthor.setText("");
                txtSection.setText("");
                txtX.setText("");
                txtY.setText("");
                txtBarcode.setText("");
                txtDescription.setText("");
                // Reset the dropdown to the first item (true/false)
                txtOnloans.setSelectedIndex(0);
            }
        });
    }


    /**
     * Adds a message to the process log list and prints it to the console for debugging purposes.
     *
     * @param message The message to add to the process log.
     */

    // Adds a message to the process log list
    private void addProcessLogMessage(String message)

    {
        processLogMessages.add(message);  // Add the message to the log list
        System.out.println("Added to process log: " + message);  // Debug log for verification
    }

    /**
     * Generates a formatted log message for sorting actions, including a timestamp.
     *
     * @param action The sorting action being logged.
     * @return The formatted log message.
     */
    // Method to generate log message for sort actions without barcode
    private String generateSortLogMessage(String action)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy - hh:mma");
        String dateTime = dateFormat.format(new Date());

        StringBuilder logMessage = new StringBuilder();
        logMessage.append(dateTime)
                .append(" - SENT - ")
                .append(action)
                .append(" sort action requested");

        return logMessage.toString();
    }

    /* This is before I used optimisation

    private void displayProcessLogMessages()
    {
        // Set flag to only show action-specific logs
        showingProcessLogOnly = true;
        // Clear existing text
        txtProcesslog.setText("");

        for (String logMessage : processLogMessages)
        {
            txtProcesslog.append(logMessage + "\n"); // Display each log message
        }
    }
    */

    /**
     * Displays all stored process log messages in the process log text area.
     * Clears existing content before updating.
     */
    private void displayProcessLogMessages()
    {
        // Set flag to only show action-specific logs
        showingProcessLogOnly = true;
        // Clear existing text
        txtProcesslog.setText("");

        // Store the size of processLogMessages to avoid repeated calls
        // Count the number of tasks once at the beginning and use that
        // Count to know how many times to read through the list without checking again.
        int logSize = processLogMessages.size();

        // Loop through each log message in processLogMessages
        for (int i = 0; i < logSize; i++)
        {
            txtProcesslog.append(processLogMessages.get(i) + "\n");
        }
    }

    /* This is my generateLogMessage Before I optimised it

    // Helper method to generate log messages with timestamp
    private String generateLogMessage(String action)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy - hh:mma");
        String dateTime = dateFormat.format(new Date());
        String barcode = txtBarcode.getText().trim();

        return dateTime + " - " + action + " action requested for Item - Barcode: " + barcode;
    }
    */
    /**
     * Generates a log message with a timestamp based on a specified action.
     *
     * @param action The action being logged (e.g., "Add Item")
     * @return A formatted log message including the action and current date/time
     */

    //This approach avoids creating multiple intermediate String objects, which is what happens when using
    // + in a loop or a method like this.

    // StringBuilder is more memory-efficient and faster for building strings, especially when dealing
    // with multiple parts as it doesn't need to build a string object everytime it appends
    private String generateLogMessage(String action)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy - hh:mma");
        String dateTime = dateFormat.format(new Date());
        String barcode = txtBarcode.getText().trim();

        // Use StringBuilder for efficient concatenation
        StringBuilder logMessage = new StringBuilder();
        logMessage.append(dateTime)
                .append(" - ")
                .append(action)
                .append(" - ")
                .append(barcode);

        return logMessage.toString();
    }

    /**
     * Initializes the JTable for displaying CD data and sets up its sorting and filtering.
     *
     * @param myPanelLayout The layout manager for component placement.
     */
    public void WordAssociationTable(SpringLayout myPanelLayout) {
        // Create a panel to hold all other components
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        add(topPanel);

        // Create column names
        String columnNames[] =
                {"ID", "Title", "Author", "Section", "X", "Y", "Barcode", "Description", "On-Loan"};


        FileManager fileManager = new FileManager();
        FileData fileData = fileManager.ReadFromFile(); // Reads from RoboticCD.csv

        ArrayList<Object[]> dataValues = new ArrayList<>();

        // Loop through the cdData array from FileData and populate the ArrayList for the JTable
        for (int i = 0; i < fileData.count; i++) {
            CDs cd = fileData.cdData[i];
            if (cd != null) {
                dataValues.add(new Object[]
                        {
                                cd.Id, cd.Title, cd.Author, cd.Section, cd.X, cd.Y, cd.Barcode, cd.Description, cd.OnLoan.equals("true")
                        });
            }
        }
        // constructor of JTable model
        wordModel = new MyModel(dataValues, columnNames);

        // Create a new table instance
        table = new JTable(wordModel);

        rowSorter = new TableRowSorter<>(wordModel);
        table.setRowSorter(rowSorter);

        table.getSelectionModel().addListSelectionListener(e ->
        {
            if (!e.getValueIsAdjusting()) {
                updateTextFieldsFromTable();
            }
        });

        // Configure some of JTable's paramters
        table.isForegroundSet();
        table.setShowHorizontalLines(false);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(true);
        add(table);

        // Change the text and background colours
        table.setSelectionForeground(Color.white);
        table.setSelectionBackground(Color.blue);

        // Add the table to a scrolling pane, size and locate
        JScrollPane scrollPane = table.createScrollPaneForTable(table);
        topPanel.add(scrollPane, BorderLayout.CENTER);
        topPanel.setPreferredSize(new Dimension(625, 215));
        myPanelLayout.putConstraint(SpringLayout.WEST, topPanel, 1, SpringLayout.WEST, this);
        myPanelLayout.putConstraint(SpringLayout.NORTH, topPanel, 105, SpringLayout.NORTH, this);
    }
    /**
     * Updates the text fields with data from the selected row in the JTable.
     */
    private void updateTextFieldsFromTable()
    {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1)
        {
            // Get the values from the selected row and set them in the respective text fields
            txtID.setText(table.getValueAt(selectedRow, 0).toString());  // Convert Integer to String for ID
            txtTitle.setText((String) table.getValueAt(selectedRow, 1));  // Set the Title field
            txtAuthor.setText((String) table.getValueAt(selectedRow, 2));  // Set the Author field
            txtSection.setText((String) table.getValueAt(selectedRow, 3));  // Set the Section field
            txtX.setText(table.getValueAt(selectedRow, 4).toString());  // Convert Integer to String for X
            txtY.setText(table.getValueAt(selectedRow, 5).toString());  // Convert Integer to String for Y
            txtBarcode.setText(table.getValueAt(selectedRow, 6).toString());  // Convert Integer to String for Barcode
            txtDescription.setText((String) table.getValueAt(selectedRow, 7));  // Set the Description field

            // For On-Loan, set the selected item in the JComboBox
            txtOnloans.setSelectedItem(table.getValueAt(selectedRow, 8).toString());
        }
    }
    /**
     * Establishes a socket connection to the server and initializes the client thread.
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
            System.out.println("In main form: "+socket);
            // Instantiate ChatClientThread1 for MainForm
            clientThread = new ChatClientThread1(this, socket);
            clientThread.start();

            btnProcess.setEnabled(true);
            System.out.println("MainForm connected to server at " + serverName + ":" + serverPort);

            // Notify SecondForm (or server) that MainForm is connected
            sendMessage("MainForm connected");

        }
        catch (IOException e)
        {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }

    /**
     * Sends a message to the server via the output stream.
     *
     * @param message The message to be sent.
     */
    // Send a message to the server or SecondForm
    public void sendMessage(String message)
    {
        try
        {
            streamOut.writeUTF(message);
            streamOut.flush();
        }
        catch (IOException e)
        {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }


    /**
     * Sends a specified action message to the SecondForm via the server.
     *
     * @param action The action being performed, such as "Retrieve" or "Return."
     */
    // Formats the message for an action, sends it via ChatServer, and logs it
    private void sendActionToSecondForm(String action)
    {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) { // Ensure a row is selected
            // Get coordinates and other data for the selected CD
            String xValue = table.getValueAt(selectedRow, 4).toString();
            String yValue = table.getValueAt(selectedRow, 5).toString();
            String barcode = txtBarcode.getText().trim();
            String section = txtSection.getText().trim();

            // Format message for SecondForm's handle method
            String message = "MAIN: " + action + ": " + xValue + ": " + yValue + ": " + barcode + ": " + section;

            // Send message to ChatServer using sendMessage
            sendMessage(message);

            // Log the action with timestamp
            String logMessage = generateLogMessage(action);
            addProcessLogMessage(logMessage);
        }
    }

    /**
     * Sends data from the selected row in the JTable to the SecondForm via the server.
     */
    // Method to send the selected row data to SecondForm using the ChatServer
    private void sendSelectedRowToSecondForm()
    {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String id = table.getValueAt(selectedRow, 0).toString();
            String title = table.getValueAt(selectedRow, 1).toString();
            String author = table.getValueAt(selectedRow, 2).toString();
            String section = table.getValueAt(selectedRow, 3).toString();
            String x = table.getValueAt(selectedRow, 4).toString();
            String y = table.getValueAt(selectedRow, 5).toString();
            String barcode = table.getValueAt(selectedRow, 6).toString();
            String description = table.getValueAt(selectedRow, 7).toString();
            String onLoan = table.getValueAt(selectedRow, 8).toString();

            String message = "MAIN: UpdateRow: " + id + ": " + title + ": " + author + ": " + section + ": " +
                    x + ": " + y + ": " + barcode + ": " + description + ": " + onLoan;

            // Debug output to verify message
            System.out.println("Sending message to SecondForm: " + message);

            sendMessage(message);
        }
    }

    /**
     * Processes incoming messages from the server, parses actions, and updates the log.
     *
     * @param message The received message from the server.
     */
    public void handle(String message)
    {
        System.out.println("Received message: " + message);

        if (message.contains("SECOND: "))
        {

            String[] parts = message.split(": ");

            if (parts.length >= 3)
            {

                String[] actionParts=parts[2].split("-");
                String action=actionParts[0];

                String barcode = actionParts[1];
                String section = parts.length > 3 ? parts[3].trim() : "";
                System.out.println(section);
                String logMessage = formatLogMessage(action, barcode, section);

                addProcessLogMessage(logMessage);
                displayProcessLogMessages();
            }
        }
    }

    /**
     * Formats a log message for actions involving items, including timestamps and barcodes.
     *
     * @param action  The action being performed (e.g., "Retrieve").
     * @param barcode The barcode of the item.
     * @param section The section of the item (if applicable).
     * @return The formatted log message.
     */
    private String formatLogMessage(String action, String barcode, String section)
    {
        String timestamp = new SimpleDateFormat("MM/dd/yyyy - hh:mma").format(new Date());

        StringBuilder logMessage = new StringBuilder();
        logMessage.append(timestamp)
                .append(" - ")
                .append("RCVD")
                .append(" - Item ")
                .append(action)
                .append("- ").append(barcode);


        if (!section.isEmpty()) {
          //  logMessage.append("- ").append(section);
        }

        return logMessage.toString();
    }


    /**
     * Closes the server connection and stops the client thread.
     */
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
            clientThread.interrupt();  // Stop the thread
        }
    }
    /**
     * Updates the connection status of the SecondForm and enables the Retrieve button if connected.
     *
     * @param connected True if SecondForm is connected, false otherwise.
     */
    public void setSecondFormConnected(boolean connected)
    {
        this.isSecondFormConnected = connected;
        // Enable btnRetrieve if both MainForm and SecondForm are connected
        if (this.isSecondFormConnected)
        {
            btnRetrieve.setEnabled(true);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == btnTitle)
        {
            // Sort by Title (which is column index 1)
            wordModel.sortByColumn(1);

            // Clear the existing binary tree
            binaryTree = new BinaryTree();

            // Populate the binary tree with CDs from data values
            for (int i = 0; i < wordModel.al.size(); i++)
            {
                // column 6 is the Barcode and column 1 is the Title
                binaryTree.addBTNode
                        (
                                // Barcode as integer
                                Integer.parseInt(wordModel.al.get(i)[6].toString()),
                                // Title as string
                                wordModel.al.get(i)[1].toString()
                        );
            }

            txtProcesslog.append("Binary Tree populated based on CD titles.\n");
        }
        else if (e.getSource() == btnAuthor)
        {
            // Sort the data by Author (column index 2) using Insertion Sort
            insertionSortByAuthor(wordModel.al, 2);
            // Refresh the table display after sorting
            wordModel.fireTableDataChanged();
        }
        else if (e.getSource() == btnBarcode)
        {
            // Sort the array by Barcode using the selection sort algorithm
            selectionSortByBarcode(wordModel.al, 6);  // Assuming column 6 is the Barcode column
            // Refresh the JTable to show the sorted data
            wordModel.fireTableDataChanged();  // Notify the table model that data has changed
        }
        if (e.getSource() == btnSave)
        {
            saveBinaryTreeToHashMap();

        }
        else if (e.getSource() == btnRandom)
        {
            if (txtSort.getText().trim().isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Please enter a section in the Sort field to sort.", "Input Required", JOptionPane.WARNING_MESSAGE);
            }
            else
            {
                quickSortBySection();
                sendMessage("MAIN: Sort");
                addProcessLogMessage(generateSortLogMessage("Random Collection"));
            }
        }
        else if (e.getSource() == btnMostly)
        {
            if (txtSort.getText().trim().isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Please enter a section in the Sort field to sort.", "Input Required", JOptionPane.WARNING_MESSAGE);
            }
            else
            {
                mergeSortBySection();
                sendMessage("MAIN: Sort");
                addProcessLogMessage(generateSortLogMessage("Mostly Sorted"));
            }
        }
        else if (e.getSource() == btnReverse)
        {
            if (txtSort.getText().trim().isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Please enter a section in the Sort field to sort.", "Input Required", JOptionPane.WARNING_MESSAGE);
            }
            else
            {
                heapSortBySection();
                sendMessage("MAIN: Sort");
                addProcessLogMessage(generateSortLogMessage("Reverse Order"));
            }
        }
        else if (e.getSource() == btnPreorder)
        {
            // Clear the log
            txtProcesslog.setText("");
            // Pre-order traversal
            binaryTree.preorderTraverseTree(binaryTree.root, txtProcesslog);
        }
        else if (e.getSource() == btnInorder)
        {
            // Clear the log
            txtProcesslog.setText("");
            // In-order traversal
            binaryTree.inOrderTraverseTree(binaryTree.root, txtProcesslog);
        }
        else if (e.getSource() == btnPostorder)
        {
            // Clear the log
            txtProcesslog.setText("");
            // Post-order traversal
            binaryTree.postOrderTraverseTree(binaryTree.root, txtProcesslog);
        }
        else if (e.getSource() == btnConnect)
        {
            connectToServer();  // Establish connection when btnConnect is clicked
        }
        else if (e.getSource() == btnRetrieve)
        {

            // Only proceed if both MainForm and SecondForm are connected
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1)
            {
                String xValue = table.getValueAt(selectedRow, 4).toString();
                String yValue = table.getValueAt(selectedRow, 5).toString();


                System.out.println("Sending retrieve message to server.");
                String str = "MAIN: Retrieve: " + xValue + ": " + yValue + ": " + txtBarcode.getText() + ": " + txtSection.getText();

                sendMessage(str);


            }
        }
        btnExit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Close the program when btnExit is clicked
                System.exit(0);
            }
        });
        // Make sure only one listener is attached to btnUpdate
        for (ActionListener al : btnUpdate.getActionListeners())
        {
            btnUpdate.removeActionListener(al);
        }

        btnUpdate.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    // Gather all the text field data to create a new CD object
                    CDs newCD = new CDs(
                            Integer.parseInt(txtID.getText()),
                            txtTitle.getText(),
                            txtAuthor.getText(),
                            txtSection.getText(),
                            txtX.getText(),
                            txtY.getText(),
                            txtBarcode.getText(),
                            txtDescription.getText(),
                            txtOnloans.getSelectedItem().toString()
                    );

                    // Read the current data from the file
                    FileManager fileManager = new FileManager();
                    FileData fileData = fileManager.ReadFromFile();

                    // Add the new CD to the data array
                    for (int i = 0; i < fileData.cdData.length; i++)
                    {
                        if (fileData.cdData[i] == null)
                        {
                            fileData.cdData[i] = newCD;
                            fileData.count++;
                            break;
                        }
                    }

                    // Write the updated data back to the file
                    fileManager.WriteToFile(fileData.cdData);

                    // Refresh the table to reflect the new data
                    refreshTable();  // Call this method to reload the JTable with updated data

                    JOptionPane.showMessageDialog(null, "CD entry saved successfully.");
                }
                catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(null, "Error saving CD entry: " + ex.getMessage());
                }
            }
        });
    }

    /**
     * Refreshes the JTable with the latest data from the file.
     */
    private void refreshTable()
    {
        // Read the updated data from the file
        FileManager fileManager = new FileManager();
        FileData fileData = fileManager.ReadFromFile();

        // Clear the current data values in the table model
        ArrayList<Object[]> dataValues = new ArrayList<>();

        // Loop through the cdData array and add it to the ArrayList
        for (int i = 0; i < fileData.count; i++)
        {
            CDs cd = fileData.cdData[i];
            if (cd != null)
            {
                dataValues.add(new Object[]
                        {
                                cd.Id, cd.Title, cd.Author, cd.Section, cd.X, cd.Y, cd.Barcode, cd.Description, Boolean.parseBoolean(cd.OnLoan)
                        });
            }
        }

        // Update the table model with the new data
        wordModel.al = dataValues;
        // Update the internal data in MyModel
        wordModel.fireTableDataChanged();
        // Notify JTable that the data has changed
    }

    /**
     * Sets up a row sorter for the JTable and listeners for search and section filtering.
     */
    private void setupRowSorter()
    {
        // Initialize the TableRowSorter with your table model
        rowSorter = new TableRowSorter<>(wordModel);
        // Set the row sorter to the JTable
        table.setRowSorter(rowSorter);

        // Add a DocumentListener to txtSearch to filter rows based on the search input
        txtSearch.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                filterTable();
            }
        });

        // Add a DocumentListener to txtSort to filter rows based on the section input
        txtSort.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                filterTable();
            }
        });
    }

    /* Before I used the third party string utilis

        // If both search and section text are empty, show all rows
        if (searchText.length() == 0 && sectionText.length() == 0)
        {
            rowSorter.setRowFilter(null);
        }
     */

    /**
     * Filters the JTable rows based on the search and section text fields.
     */
    // Combined filter method to filter the table based on both txtSearch and txtSort
    private void filterTable()
    {
        // Get the text from txtSearch and txtSort, trimming any extra spaces
        String searchText = txtSearch.getText().trim();
        String sectionText = txtSort.getText().trim();

        // replaced searchText.length() == 0 with StringUtils.isEmpty(searchText)
        if (StringUtils.isEmpty(searchText) && StringUtils.isEmpty(sectionText))
        {
            rowSorter.setRowFilter(null);
        }
        else
        {
            // Combine filters for both txtSearch (any column) and txtSort (specific column)
            ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();

            // Apply a case-insensitive filter on any column for searchText, if not empty
            if (searchText.length() > 0)
            {
                filters.add(RowFilter.regexFilter("(?i)" + searchText));
            }

            // Apply a filter on the "Section" column (index 3) for sectionText, if not empty
            if (sectionText.length() > 0)
            {
                filters.add(RowFilter.regexFilter("(?i)^" + sectionText, 3));
            }

            // Combine all filters with RowFilter.andFilter so all conditions must be met
            RowFilter<Object, Object> combinedFilter = RowFilter.andFilter(filters);
            rowSorter.setRowFilter(combinedFilter);
        }
    }

    /**
     * Sorts the JTable rows by the Author column using the insertion sort algorithm.
     *
     * @param arr         The data rows to be sorted.
     * @param columnIndex The index of the Author column.
     */
    //One at a time will keep repeating by taking a element and placing it in correct position will go one at a time swapping
    private void insertionSortByAuthor(ArrayList<Object[]> arr, int columnIndex)
    {
        // Start iterating from the second element (index 1) because the first element is trivially sorted
        for (int i = 1; i < arr.size(); i++)
        {
            // Store the current element in 'key' to insert it into the correct sorted position
            Object[] key = arr.get(i);
            // Extract the value from the 'author' column (columnIndex) to compare with previous elements
            Comparable keyAuthor = (Comparable) key[columnIndex];
            // Initialize 'j' as the index of the element just before 'i'
            int j = i - 1;
            // Compare the key element with all previous elements (arr[j]) to find its correct sorted position
            // Keep shifting elements one position ahead if they are greater than the keyAuthor
            while (j >= 0 && ((Comparable) arr.get(j)[columnIndex]).compareTo(keyAuthor) > 0)
            {
                // Move the current element one position forward (to the right)
                arr.set(j + 1, arr.get(j));
                // Move the pointer 'j' one step back to continue the comparison
                j = j - 1;
            }
            // Insert the key element at its correct position once all larger elements are shifted
            arr.set(j + 1, key);
        }
    }

    /**
     * Sorts the JTable rows by the Barcode column using the selection sort algorithm.
     *
     * @param arr         The data rows to be sorted.
     * @param columnIndex The index of the Barcode column.
     */
    //Will keep repeating finding the smallest number and moving it to the front until sorted. So first index will be
    // swapped to smallest and then the 2nd index swapped to 2nd smallest
    private void selectionSortByBarcode(ArrayList<Object[]> arr, int columnIndex)
    {
        // Loop through the entire array except the last element (as it'll already be sorted)
        for (int i = 0; i < arr.size() - 1; i++)
        {
            // Assume the current index is the minimum (smallest value)
            int minIndex = i;
            // Loop through the remaining unsorted part of the array
            for (int j = i + 1; j < arr.size(); j++)
            {
                // Compare the value at minIndex with the current index j
                // If the current value is smaller, update minIndex to j
                if (((Comparable) arr.get(j)[columnIndex]).compareTo(arr.get(minIndex)[columnIndex]) < 0)
                {
                    minIndex = j; // Update minIndex to the smallest value found
                }
            }

            // If the minimum value is not the current i-th value, swap them
            if (minIndex != i)
            {
                Object[] temp = arr.get(i);
                // Store the i-th element in a temporary variable
                arr.set(i, arr.get(minIndex));
                // Replace the i-th element with the smallest found
                arr.set(minIndex, temp);
                // Place the original i-th element where the smallest was
            }
        }
    }

    //FileManager fileManager = new FileManager();
    //fileManager.WriteToFile(FileData.map);

    /**
     * Saves the binary tree data to a HashMap and logs the action.
     */
    // Method to save BinaryTree to a HashMap and log it in the txtProcesslog
    private void saveBinaryTreeToHashMap()
    {
        // Clear any existing data in treeMap
        treeMap.clear();
        // Assuming binaryTree has a root of type BTNode
        traverseBinaryTree(binaryTree.root, treeMap);

        // Log the saved HashMap in txtProcesslog
        txtProcesslog.append("Binary Tree saved to HashMap:\n");

        FileManager fileManager = new FileManager();
        fileManager.WriteToHashmap(treeMap);

    }

    /**
     * Displays the saved binary tree HashMap in the process log.
     */
    // Method to display the saved HashMap in txtProcesslog when btnDisplay is clicked
    private void displayHashMap()
    {
        // Check if the treeMap is not null and has data in it
        if (treeMap != null && !treeMap.isEmpty())
        {
            // Add a title line to the process log to show we're displaying the HashMap
            txtProcesslog.append("Displaying Binary Tree HashMap:\n");
            // Loop through each key-value pair in the HashMap
            for (Map.Entry<Integer, String> entry : treeMap.entrySet())
            {
                // Display each key and value in the process log, separated by a dash
                txtProcesslog.append(entry.getKey() + " - " + entry.getValue() + "\n");
            }
        }
        else
        {
            // If the HashMap is empty or null, display a message saying no data was found
            txtProcesslog.append("No Binary Tree data found in HashMap.\n");
        }
    }

    /**
     * Traverses the binary tree and populates the provided HashMap with key-value pairs.
     *
     * @param node The current binary tree node.
     * @param map  The HashMap to populate.
     */
    // Helper method to traverse the BinaryTree and populate the HashMap
    private void traverseBinaryTree(BTNode node, HashMap<Integer, String> map)
    {
        // Base case for recursion
        if (node == null) return;
        // 'key' and 'name' fields in BTNode
        map.put(node.key, node.name);
        // Traverse the left subtree
        traverseBinaryTree(node.leftChild, map);
        // Traverse the right subtree
        traverseBinaryTree(node.rightChild, map);
    }

    /**
     * Filters the process log messages based on a specified barcode.
     */
    // Method to filter txtProcesslog based on the barcode in txtFind
    private void filterProcessLogByBarcode()
    {
        // Get the text entered by the user in txtFind and remove any extra spaces
        String searchText = txtFind.getText().trim();

        // Check if the search text is empty
        if (searchText.isEmpty())
        {
            // If no search text, reload the full content of txtProcesslog
            reloadProcessLog();
        }
        else
        {
            // Create a new StringBuilder to hold filtered results
            StringBuilder filteredLog = new StringBuilder("Filtered Results:\n");

            // Loop through each line in txtProcesslog
            for (String line : txtProcesslog.getText().split("\n"))
            {
                // If the line contains the search text, add it to the filtered results
                if (line.contains(searchText))
                {
                    filteredLog.append(line).append("\n");
                }
            }

            // Display the filtered results in txtProcesslog
            txtProcesslog.setText(filteredLog.toString());
        }
    }

    /**
     * Reloads the full content of the process log into the text area.
     */
    // Method to reload all content back to txtProcesslog
    // Implement this by re-triggering the content generation or refreshing logic for txtProcesslog
    private void reloadProcessLog()
    {
        // Clear and re-add all entries or regenerate based on your data source
        txtProcesslog.setText("");
        // For example, you might re-run a traversal method to repopulate
        binaryTree.inOrderTraverseTree(binaryTree.root, txtProcesslog); // Or any method that loads your full log
    }


    /**
     * Sorts JTable rows by Title within a specified Section using Quick Sort.
     */
    //Will have a pivot, smaller number on one side of pivot and one larger repeat until done

    // This method filters and sorts the table data by the Section specified in txtSort
    // Sort rows by the txtSort and takes all the Title enteries.
    private void quickSortBySection()
    {
        // Get the section value entered by the user in txtSort and remove extra spaces
        String section = txtSort.getText().trim();
        // Find the column index for "Section" and "Title"
        int sectionColumnIndex = findColumnIndexByName("Section");
        int titleColumnIndex = findColumnIndexByName("Title");
        // Check if both column indexes are valid
        if (sectionColumnIndex != -1 && titleColumnIndex != -1)
        {
            // Only sort rows in wordModel.al that match the specified section
            quickSort(wordModel.al, 0, wordModel.al.size() - 1, section, sectionColumnIndex, titleColumnIndex);
            // Notify the table that data has changed to refresh the display
            wordModel.fireTableDataChanged();
        }
        else
        {
            // Print an error if the column name is not found
            System.out.println("Column index not found for Section or Title.");
        }
    }

    // QuickSort function to sort rows in the list by the Title column within the specified Section
    // Will start organising items by title in the section
    private void quickSort(ArrayList<Object[]> list, int low, int high, String section, int sectionColumnIndex, int titleColumnIndex)
    {
        if (low < high)
        {
            // Partition the list based on the Title column within the specified Section
            int pivotIndex = partition(list, low, high, section, sectionColumnIndex, titleColumnIndex);
            // Recursively apply QuickSort to elements before the pivot within the same section
            quickSort(list, low, pivotIndex - 1, section, sectionColumnIndex, titleColumnIndex);
            // Recursively apply QuickSort to elements after the pivot within the same section
            quickSort(list, pivotIndex + 1, high, section, sectionColumnIndex, titleColumnIndex);
        }
    }

    // Partition function to arrange elements around a pivot for the specified Section and Title column
    // Will be the actual algorithm using the pivot with the title values
    private int partition(ArrayList<Object[]> list, int low, int high, String section, int sectionColumnIndex, int titleColumnIndex)
    {
        // Choose the last element in the range as the pivot
        Object[] pivot = list.get(high);
        // Initialize the index for smaller elements
        int i = low - 1;
        // Loop through elements from low to high - 1
        for (int j = low; j < high; j++) {
            // Check if the row matches the specified section
            if (list.get(j)[sectionColumnIndex].toString().equalsIgnoreCase(section))
            {
                // Get the values in the Title column to compare
                Comparable currentVal = (Comparable) list.get(j)[titleColumnIndex];
                Comparable pivotVal = (Comparable) pivot[titleColumnIndex];

                // If the current element's Title is less than or equal to the pivot Title, it goes on the left
                if (currentVal.compareTo(pivotVal) <= 0) {
                    i++;
                    // Swap the current element with the element at index i
                    Object[] temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        }

        // Place the pivot in its correct position by swapping
        Object[] temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);

        // Return the pivot index for further partitioning
        return i + 1;
    }

    // Helper method to find the index of a column by its name
    private int findColumnIndexByName(String columnName)
    {
        // Loop through the headers in the table model
        for (int i = 0; i < wordModel.header.length; i++)
        {
            // Check if the header matches the specified column name
            if (wordModel.header[i].equalsIgnoreCase(columnName))
            {
                return i; // Return the index if found
            }
        }
        return -1; // Return -1 if the column name is not found
    }

    /**
     * Sorts JTable rows by Title within a specified Section using Merge Sort.
     */
    //Will keep spliting in half and will merge back together in order

    //Will filter so only the rows with the section will exist in the JTable
    // Method to perform Merge Sort by Title within a specified Section
    private void mergeSortBySection()
    {
        // Get the section value entered by the user in txtSort, removing any extra spaces
        String section = txtSort.getText().trim();
        // Find the column index for "Section" and "Title" in the table
        int sectionColumnIndex = findColumnIndexByName("Section");
        int titleColumnIndex = findColumnIndexByName("Title");
        // Check if both section and title columns are found in the table
        if (sectionColumnIndex != -1 && titleColumnIndex != -1)
        {
            // Create a list to hold only the rows that match the section from txtSort
            ArrayList<Object[]> filteredList = new ArrayList<>();
            for (Object[] row : wordModel.al) {
                // If the row's section matches the section from txtSort, add it to filteredList
                if (row[sectionColumnIndex].toString().equalsIgnoreCase(section))
                {
                    filteredList.add(row);
                }
            }
            // Sort the filtered rows by title using merge sort
            mergeSort(filteredList, titleColumnIndex);

            // Replace the section-specific rows in the original list with the sorted ones
            int index = 0;
            for (int i = 0; i < wordModel.al.size(); i++)
            {
                // If the row in wordModel.al matches the section, replace it with sorted rows
                if (wordModel.al.get(i)[sectionColumnIndex].toString().equalsIgnoreCase(section))
                {
                    wordModel.al.set(i, filteredList.get(index++));
                }
            }

            // Refresh the table display to show the sorted data
            wordModel.fireTableDataChanged();
        }
        else
        {
            // If section or title columns are not found, print an error message
            System.out.println("Column index not found for Section or Title.");
        }
    }

    // Will split them into smaller parts
    // Recursive Merge Sort function to sort rows in the list the section
    private void mergeSort(ArrayList<Object[]> list, int columnIndex)
    {
        // Base case: if the list has 0 or 1 item, it is already sorted
        if (list.size() < 2)
        {
            return;
        }
        // Split the list into two halves
        int mid = list.size() / 2;
        ArrayList<Object[]> left = new ArrayList<>(list.subList(0, mid));
        ArrayList<Object[]> right = new ArrayList<>(list.subList(mid, list.size()));
        // Recursively sort each half
        mergeSort(left, columnIndex);
        mergeSort(right, columnIndex);
        // Merge the sorted halves back together into the original list
        merge(list, left, right, columnIndex);
    }

    //Will merge all the smaller sorted parts together in correct order.
    // Merge function to combine two sorted lists (left and right) into one sorted list
    private void merge(ArrayList<Object[]> list, ArrayList<Object[]> left, ArrayList<Object[]> right, int columnIndex)
    {
        int i = 0; // Pointer for the left list
        int j = 0; // Pointer for the right list
        int k = 0; // Pointer for the merged list

        // Compare elements from left and right lists and add the smaller one to the merged list
        while (i < left.size() && j < right.size())
        {
            // Value from left list
            Comparable leftValue = (Comparable) left.get(i)[columnIndex];
            Comparable rightValue = (Comparable) right.get(j)[columnIndex];

            // If the left value is smaller or equal, add it to the main list
            if (leftValue.compareTo(rightValue) <= 0)
            {
                list.set(k++, left.get(i++));
            }
            else
            {
                // Otherwise, add the right value to the main list
                list.set(k++, right.get(j++));
            }
        }
        // Copy any remaining elements from the left list to the main list
        while (i < left.size())
        {
            list.set(k++, left.get(i++));
        }
        // Copy any remaining elements from the right list to the main list
        while (j < right.size())
        {
            list.set(k++, right.get(j++));
        }
    }

    /**
     * Sorts JTable rows by Title within a specified Section using Heap Sort.
     */
    //Heap sort will always swap with bigger child in a tree format

    //Will filter the table by whatever section
    // Method to perform Heap Sort by Title within a specified Section
    private void heapSortBySection() {
        // Get the section value entered by the user in txtSort, removing extra spaces
        String section = txtSort.getText().trim();
        // Find the column index for "Section" and "Title" in the table
        int sectionColumnIndex = findColumnIndexByName("Section");
        int titleColumnIndex = findColumnIndexByName("Title");
        // Check if both section and title columns are found in the table
        if (sectionColumnIndex != -1 && titleColumnIndex != -1)
        {
            // Create a list to hold only the rows that match the section from txtSort
            ArrayList<Object[]> filteredList = new ArrayList<>();
            for (Object[] row : wordModel.al)
            {
                // If the row's section matches the section from txtSort, add it to filteredList
                if (row[sectionColumnIndex].toString().equalsIgnoreCase(section))
                {
                    filteredList.add(row);
                }
            }
            // Sort the filtered rows by title using heap sort
            heapSort(filteredList, titleColumnIndex);
            // Replace the section-specific rows in the original list with the sorted ones
            int index = 0;
            for (int i = 0; i < wordModel.al.size(); i++)
            {
                // If the row in wordModel.al matches the section, replace it with sorted rows
                if (wordModel.al.get(i)[sectionColumnIndex].toString().equalsIgnoreCase(section))
                {
                    wordModel.al.set(i, filteredList.get(index++));
                }
            }

            // Refresh the table display to show the sorted data
            wordModel.fireTableDataChanged();
        }
        else
        {
            // If section or title columns are not found, print an error message
            System.out.println("Column index not found for Section or Title.");
        }
    }

    //Will arrange the list into the heap and will keep repeating
    // Heap Sort function to sort rows in the list by a specific column
    private void heapSort(ArrayList<Object[]> list, int columnIndex)
    {
        // Get the size of the list
        int n = list.size();
        // Build the max heap by rearranging the array
        for (int i = n / 2 - 1; i >= 0; i--)
        {
            heapify(list, n, i, columnIndex);
        }
        // Extract elements from the heap one by one
        for (int i = n - 1; i > 0; i--)
        {
            // Move the current root to the end of the list
            Object[] temp = list.get(0);
            list.set(0, list.get(i));
            list.set(i, temp);
            // Heapify the reduced heap
            heapify(list, i, 0, columnIndex);
        }
    }

    //Will maintain the structure, ensuring it keeps the correct order
    // Heapify function to maintain the max heap property
    private void heapify(ArrayList<Object[]> list, int n, int i, int columnIndex)
    {
        int largest = i; // Assume the largest element is the root
        int left = 2 * i + 1; // Left child index
        int right = 2 * i + 2; // Right child index

        // If the left child is larger than the root, update the largest
        if (left < n && ((Comparable) list.get(left)[columnIndex]).compareTo(list.get(largest)[columnIndex]) > 0)
        {
            largest = left;
        }
        // If the right child is larger than the largest so far, update the largest
        if (right < n && ((Comparable) list.get(right)[columnIndex]).compareTo(list.get(largest)[columnIndex]) > 0)
        {
            largest = right;
        }
        // If the largest is not the root, swap and continue heapifying
        if (largest != i)
        {
            Object[] swap = list.get(i);
            list.set(i, list.get(largest));
            list.set(largest, swap);
            // Recursively heapify the affected sub-tree
            heapify(list, n, largest, columnIndex);
        }
    }


}
