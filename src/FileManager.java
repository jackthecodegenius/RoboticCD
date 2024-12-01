import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * File management class for writing our application data to csv files and reading it back again.
 */
public class FileManager {
    String fileName = "RoboticCD.csv";
    String hashMap = "Hash.csv";

    /**
     * Use the values created in a array to write data to file in a semicolon format.
     * @param cdData The array we want to write to file.
     */
    public void WriteToFile(CDs[] cdData)
    {
        try
        {
            // Will create a BufferedWriter to write to the file
            BufferedWriter output = new BufferedWriter(new FileWriter(fileName));
            for (int i = 0; i < cdData.length; i++) {
                if (cdData[i] == null)
                {
                    break;
                }
                // Write each CD entry to the file in semicolon-separated format
                output.write(cdData[i].Id + ";" + cdData[i].Title + ";" + cdData[i].Author + ";" +
                        cdData[i].Section + ";" + cdData[i].X + ";" + cdData[i].Y + ";" +
                        cdData[i].Barcode + ";" + cdData[i].Description + ";" +
                        cdData[i].OnLoan);
                output.newLine();
            }
            // Closes the BufferedWriter
            output.close();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Reads data from a CSV file and populates a FileData object with the CD information.
     * Each line in the file is split using a semicolon (;) delimiter and converted into a CDs object.
     *
     * @return A FileData object containing an array of CDs and a count of the entries read.
     *         Returns an empty FileData object if an error occurs during file reading.
     */
    public FileData ReadFromFile()
    {
        FileData data = new FileData();
        data.cdData = new CDs[100];
        data.count = 0;

        try
        {
            // Read from the file
            BufferedReader input = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = input.readLine()) != null)
            {
                // Split the line by semicolon (;) to get individual fields
                String[] temp = line.split(";");
                data.cdData[data.count] = new CDs(Integer.parseInt(temp[0]), temp[1], temp[2], temp[3],
                        temp[4], temp[5], temp[6], temp[7], temp[8]);
                data.count++;
            }
            input.close();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            data.cdData = null;
            data.count = 0;
        }
        return data;

    }

    /**
     * Writes the provided HashMap data to a CSV file.
     * The HashMap is serialized into a string format and saved to the file.
     *
     * @param Map The HashMap to write to the file.
     */
    public void WriteToHashmap(HashMap Map)
    {
        try
        {
            // Will create a BufferedWriter to write to the file
            BufferedWriter output = new BufferedWriter(new FileWriter(hashMap));

            output.write(Map.toString());

            // Closes the BufferedWriter
            output.close();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

}
