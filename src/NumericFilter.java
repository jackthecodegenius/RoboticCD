import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * A custom DocumentFilter that restricts input to numeric characters only.
 * This filter can be applied to a text field to prevent the user from entering
 * non-numeric characters.
 */
public class NumericFilter extends DocumentFilter {

    /**
     * Handles the insertion of new strings into the document.
     * If the string is numeric, it is allowed to be inserted, otherwise it is blocked.
     *
     * @param fb       The FilterBypass that allows the insertion.
     * @param offset   The position in the document where the string should be inserted.
     * @param string   The string to be inserted.
     * @param attr     The AttributeSet for the inserted string.
     *
     * @throws BadLocationException If the insertion fails.
     */
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException
    {
        if (string == null)
        {
            return;  // If the string is null, do nothing.
        }

        // If the string contains only numeric characters, insert it.
        if (isNumeric(string)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException
    {
        // Replace text if the new text contains only numeric characters.
        if (isNumeric(text))
        {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException
    {
        // Always allow text to be removed.
        super.remove(fb, offset, length);
    }

    private boolean isNumeric(String text)
    {
        // Regular expression to match only digits (0-9).
        return text.matches("\\d*");
    }
}