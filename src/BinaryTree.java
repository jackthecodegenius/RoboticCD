//Source:  http://www.newthinktank.com/2013/03/binary-tree-in-java/
// New Think Tank
import javax.swing.JTextArea;
import java.util.HashMap;

/**
 * The BinaryTree class represents a binary search tree with methods for adding nodes,
 * searching for a node, and traversing the tree in different orders.
 */

public class BinaryTree {

	BTNode root;

	/**
	 * Adds a new node to the binary tree.
	 *
	 * @param key  the integer key used for positioning the node in the tree
	 * @param name the name or label associated with the node
	 */
	public void addBTNode(int key, String name) {

		// Create a new BTNode and initialize it

		BTNode newBTNode = new BTNode(key, name);

		// If there is no root this becomes root

		if (root == null) {

			root = newBTNode;

		} else {

			// Set root as the BTNode we will start
			// with as we traverse the tree

			BTNode focusBTNode = root;

			// Future parent for our new BTNode

			BTNode parent;

			while (true) {

				// root is the top parent so we start
				// there

				parent = focusBTNode;

				// Check if the new BTNode should go on
				// the left side of the parent BTNode

				if (key < focusBTNode.key) {

					// Switch focus to the left child

					focusBTNode = focusBTNode.leftChild;

					// If the left child has no children

					if (focusBTNode == null) {

						// then place the new BTNode on the left of it

						parent.leftChild = newBTNode;
						return; // All Done

					}

				} else { // If we get here put the BTNode on the right

					focusBTNode = focusBTNode.rightChild;

					// If the right child has no children

					if (focusBTNode == null) {

						// then place the new BTNode on the right of it

						parent.rightChild = newBTNode;
						return; // All Done

					}

				}

			}
		}

	}

	// All BTNodes are visited in ascending order
	// Recursion is used to go to one BTNode and
	// then go to its child BTNodes and so forth

	//Left - Root - Right
	public void inOrderTraverseTree(BTNode focusBTNode, JTextArea txtProcesslog)
	{
		// Check if the current node (focusBTNode) is not null
		if (focusBTNode != null) {
			// Recursively traverse the left subtree
			inOrderTraverseTree(focusBTNode.leftChild, txtProcesslog);

			// Append the current node's data to the JTextArea (process log)
			txtProcesslog.append(focusBTNode.toString() + "\n");

			// Recursively traverse the right subtree
			inOrderTraverseTree(focusBTNode.rightChild, txtProcesslog);
		}
	}
	// Root - Left - Right.
	public void preorderTraverseTree(BTNode focusBTNode, JTextArea txtProcesslog)
	{
		// Check if the current node (focusBTNode) is not null
		if (focusBTNode != null) {
			// Append the current node's data to the JTextArea (process log)
			txtProcesslog.append(focusBTNode.toString() + "\n");

			// Recursively traverse the left subtree
			preorderTraverseTree(focusBTNode.leftChild, txtProcesslog);

			// Recursively traverse the right subtree
			preorderTraverseTree(focusBTNode.rightChild, txtProcesslog);
		}
	}
	// Left - Right - Root.
	public void postOrderTraverseTree(BTNode focusBTNode, JTextArea txtProcesslog)
	{
		// Check if the current node (focusBTNode) is not null
		if (focusBTNode != null) {
			// Recursively traverse the left subtree
			postOrderTraverseTree(focusBTNode.leftChild, txtProcesslog);

			// Recursively traverse the right subtree
			postOrderTraverseTree(focusBTNode.rightChild, txtProcesslog);

			// Append the current node's data to the JTextArea (process log)
			txtProcesslog.append(focusBTNode.toString() + "\n");
		}
	}



	/**
	 * Finds a node in the binary tree by its key.
	 *
	 * @param key the key to search for
	 * @return the node with the specified key, or null if not found
	 */

	public BTNode findBTNode(int key) {

		// Start at the top of the tree

		BTNode focusBTNode = root;

		// While we haven't found the BTNode
		// keep looking

		while (focusBTNode.key != key) {

			// If we should search to the left

			if (key < focusBTNode.key) {

				// Shift the focus BTNode to the left child

				focusBTNode = focusBTNode.leftChild;

			} else {

				// Shift the focus BTNode to the right child

				focusBTNode = focusBTNode.rightChild;

			}

			// The BTNode wasn't found

			if (focusBTNode == null)
				return null;

		}

		return focusBTNode;

	}



public static void main(String[] args) {

		BinaryTree theTree = new BinaryTree();

		theTree.addBTNode(50, "Boss");

		theTree.addBTNode(25, "Vice President");

		theTree.addBTNode(15, "Office Manager");

		theTree.addBTNode(30, "Secretary");

		theTree.addBTNode(75, "Sales Manager");

		theTree.addBTNode(85, "Salesman 1");

		// Different ways to traverse binary trees

		// theTree.inOrderTraverseTree(theTree.root);

		// theTree.preorderTraverseTree(theTree.root);

		// theTree.postOrderTraverseTree(theTree.root);

		// Find the BTNode with key 75

		System.out.println("\nBTNode with the key 75");

		System.out.println(theTree.findBTNode(75));

}
}

/**
 * The BTNode class represents a node in the binary tree, holding a key, name, and references to left and right children.
 */
class BTNode {

	int key;
	String name;

	BTNode leftChild;
	BTNode rightChild;

	/**
	 * Constructs a new BTNode with the specified key and name.
	 *
	 * @param key  the integer key for positioning the node
	 * @param name the name or label associated with the node
	 */

	BTNode(int key, String name) {

		this.key = key;
		this.name = name;

	}


	@Override
	public String toString() {

		// Return the barcode (key) followed by the title (name)
		return key + " - " + name;


	}






}


