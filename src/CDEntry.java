/**
 * The CDEntry class represents a CD entry with various attributes such as section, coordinates,
 * barcode, title, and author. It includes constructors for creating a default entry or an entry
 * with specified values, and a method for generating a string representation of the entry.
 */

public class CDEntry
{
    String section;
    String x;
    String y;
    String barcode;
    String title;
    String author;

    public CDEntry()
    {
        section = "A";
        x = "1";
        y = "1";
        barcode = "000000000";
        title = "Untitled";
        author = "Unknown";
    }

    public CDEntry(String section, String x, String y, String barcode, String title, String author)
    {
        this.section = section;
        this.x = x;
        this.y = y;
        this.barcode = barcode;
        this.title = title;
        this.author = author;
    }

    /**
     * Returns a string representation of the CD entry, including the title, author, section, coordinates, and barcode.
     *
     * @return a string containing the details of the CD entry in a readable format
     */
    @Override
    public String toString()
    {
        return title + " by " + author + " in section " + section + " [" + x + ", " + y + "], Barcode: " + barcode;
    }
}
