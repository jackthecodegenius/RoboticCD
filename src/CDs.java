/**
 * The CDs class represents a CD item with various attributes such as ID, title, author, section,
 * location coordinates, barcode, description, and loan status. This class allows developers
 * to interact with CD data within the application.
 */
public class CDs implements Comparable
{
    public int Id;
    public String Title;
    public String Author;
    public String Section;
    public String X;
    public String Y;
    public String Barcode;
    public String Description;
    public String OnLoan;




    public CDs (Integer id, String title, String author, String section, String x, String y, String barcode, String description, String onloan) {
        Id = id;
        Title = title;
        Author = author;
        Section = section;
        X = x;
        Y = y;
        Barcode = barcode;
        Description = description;
        OnLoan = onloan;
    }

    @Override
    public String toString()
    {
        return Id + ";" + Title + ";" + Author + ";" + Section + ";" + X + ";" + Y + ";" + Barcode + ";" + Description + ";" + OnLoan ;
    }

    @Override
    public int compareTo(Object other)
    {
        if (other instanceof String)
        {
            String searchTerm = (String)other;
            return this.Title.toLowerCase().compareTo(searchTerm.toLowerCase());
        }
        CDs otherTitles = (CDs)other;
        return this.Title.compareTo(otherTitles.Title);
    }
}
