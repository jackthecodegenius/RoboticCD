import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * MyModel is a custom table model that extends AbstractTableModel
 * to provide data for a JTable, representing a list of CD entries.
 * The table supports dynamic row addition, sorting by column, and
 * a boolean display for the "On-Loan" status.
 */
public class MyModel extends AbstractTableModel {

    /**
     * The data for the table model, each element representing a row.
     */
    ArrayList<Object[]> al;

    // The headers
    String[] header;

    // To hold the column index for the On-Loan column
    int col;

    // Constructor
    MyModel(ArrayList<Object[]> obj, String[] header) {
        // Save the header
        this.header = header;
        // And the data
        al = obj;
        // Get the column index for the On-Loan column
        col = this.findColumn("On-Loan");
    }

    // Method that needs to be overridden: Row count is the size of the ArrayList
    @Override
    public int getRowCount() {
        return al.size();
    }

    // Method that needs to be overridden: Column count is the size of the header
    @Override
    public int getColumnCount() {
        return header.length;
    }

    // Method that needs to be overridden: Object is in the ArrayList at rowIndex
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return al.get(rowIndex)[columnIndex];
    }

    // A method to return the column name
    @Override
    public String getColumnName(int index) {
        return header[index];
    }



    // Define the class type for each column
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 8) { // Assuming "On-Loan" is the 9th column (index 8)
            return Boolean.class; // This will display a checkbox in the "On-Loan" column
        }
        return super.getColumnClass(columnIndex);
    }

    /**
     * Sorts the table model by the specified column using bubble sort.
     * Updates the table display after sorting.
     *
     * @param columnIndex The index of the column to sort by.
     */
    // Generalized method to sort by any column index
    public void sortByColumn(int columnIndex) {
        // Call the bubble sort algorithm to sort by the specified column index.
        bubbleSort(al, columnIndex);
        // Notify the table that data has changed, so it updates.
        fireTableDataChanged();
    }

    /**
     * Performs bubble sort on the ArrayList by the specified column.
     *
     * @param arr         The data to be sorted.
     * @param columnIndex The index of the column to sort by.
     */
    // Bubble sort algorithm to sort by any column
    private void bubbleSort(ArrayList<Object[]> arr, int columnIndex)
    {
        for (int j = 0; j < arr.size(); j++)
        {
            for (int i = j + 1; i < arr.size(); i++)
            {
                // Compare the values in the specified column and sort them alphabetically or numerically
                Comparable currentVal = (Comparable) arr.get(i)[columnIndex];
                Comparable previousVal = (Comparable) arr.get(j)[columnIndex];

                if (currentVal.compareTo(previousVal) < 0)
                {
                    // Swap the rows if the current row comes after the next one
                    Object[] temp = arr.get(j);
                    arr.set(j, arr.get(i));
                    arr.set(i, temp);
                }
            }
            // Optional: Debugging, prints the sorted values from the specified column.
            System.out.println(arr.get(j)[columnIndex] + " - " + arr.get(j)[1]); // Prints "SortedColumnValue - Title"
        }
    }
    /**
     * Adds a new row to the table model and refreshes the display.
     *
     * @param rowData The data for the new row to be added.
     */

    // Method to add a row dynamically to the table model
    public void addRow(Object[] rowData) {
        al.add(rowData);           // Add the new row data to the ArrayList
        fireTableDataChanged();     // Refresh the table display with the new row
        System.out.println("Row added to model: " + java.util.Arrays.toString(rowData));  // Debug output
    }


}