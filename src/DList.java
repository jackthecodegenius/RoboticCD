/**
 * **************************************************************************
 */
/*                                                                           */
/*                    Doubly-Linked List Manipulation                        */
/*                                                                           */
/*                     January 1998, Toshimi Minoura                         */
/*                                                                           */
// Filename: Doubly-LinkedList_ToshimiMinoura
// Source:   TBA
//package dlist;

// A Node is a node in a doubly-linked list.

/**
 * Represents a node in a doubly-linked list. Each node holds a reference to a CD entry, along with
 * pointers to the previous and next nodes in the list.
 */
class Node
{
    Node prev;
    Node next;
    CDEntry cdEntry;

    // Constructor for head Node
    Node()
    {
        prev = this;
        next = this;
        cdEntry = new CDEntry();
    }

    // Constructor for a Node with BookEntry data
    Node(CDEntry cdEntry)
    {
        prev = null;
        next = null;
        this.cdEntry = cdEntry;
    }

    public void append(Node newNode)
    {
        newNode.prev = this;
        newNode.next = next;
        if (next != null) {
            next.prev = newNode;
        }
        next = newNode;
        System.out.println("Node with data " + newNode.cdEntry.title + " appended.");
    }

    public void insert(Node newNode)
    {
        newNode.prev = prev;
        newNode.next = this;
        prev.next = newNode;
        prev = newNode;
        System.out.println("Node with data " + newNode.cdEntry.title + " inserted.");
    }

    public void remove()
    {
        next.prev = prev;
        prev.next = next;
        System.out.println("Node with data " + cdEntry.title + " removed.");
    }

    @Override
    public String toString()
    {
        return cdEntry.toString();
    }
}

class DList
{
    Node head;

    public DList()
    {
        head = new Node();
    }

    public DList(CDEntry cdEntry)
    {
        head = new Node(cdEntry);
    }

    public Node findBySection(String section)
    {
        for (Node current = head.next; current != head; current = current.next)
        {
            if (current.cdEntry.section.equals(section)) {
                System.out.println("CD found in section " + section);
                return current;
            }
        }
        System.out.println("CD not found in section " + section);
        return null;
    }

    public Node findByCoordinates(String x, String y)
    {
        for (Node current = head.next; current != head; current = current.next)
        {
            if (current.cdEntry.x.equals(x) && current.cdEntry.y.equals(y))
            {
                System.out.println("CD found at coordinates [" + x + ", " + y + "]");
                return current;
            }
        }
        System.out.println("CD not found at coordinates [" + x + ", " + y + "]");
        return null;
    }

    public Node findByBarcode(String barcode)
    {
        for (Node current = head.next; current != head; current = current.next)
        {
            if (current.cdEntry.barcode.equals(barcode))
            {
                System.out.println("CD found with barcode " + barcode);
                return current;
            }
        }
        //System.out.println("CD not found with barcode " + barcode);
        return null;
    }

    public void print()
    {
        if (head.next == head)
        {
            System.out.println("List is empty.");
            return;
        }
        System.out.print("List content = ");
        for (Node current = head.next; current != head; current = current.next)
        {
            System.out.print(" " + current.cdEntry);
        }
        System.out.println("");
    }

    public static void main(String[] args)
    {
        DList dList = new DList(); // Create an empty DList
        dList.print();

        // Add CDEntries to the list
        dList.head.append(new Node(new CDEntry("B", "1", "32", "327868723", "Cool Book", "Cockers")));
        dList.print();

        dList.head.append(new Node(new CDEntry("C", "1", "12", "327868724", "Awesome Book", "Blake")));
        dList.print();

        dList.head.append(new Node(new CDEntry("B", "2", "39", "327868725", "Boring Book", "Riley")));
        dList.print();

        dList.head.insert(new Node(new CDEntry("A", "1", "5", "327868726", "Crazy Book", "Tim")));
        dList.print();

        // Find CDs
        Node node = dList.findBySection("B");
        if (node != null) node.remove();
        dList.print();

        node = dList.findByCoordinates("1", "12");
        if (node != null) node.remove();
        dList.print();

        node = dList.findByBarcode("327868725");
        if (node != null) node.append(new Node(new CDEntry("D", "3", "44", "327868727", "New CD", "John")));
        dList.print();
    }


}
