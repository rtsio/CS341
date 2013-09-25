package proj2;

import java.util.*;

/**
 * This class models a Binary Search Tree, including an inner
 * class for binary nodes. It was adapted from code written by 
 * Mark Allen Weiss - original code can be found at http://users.cis.fiu.edu/~weiss/dsaajava2/code/
 * 
 * @version 03/27/12
 * @author Rostislav Tsiomenko <tsiomr1@umbc.edu>
 * CMSC 341 - Spring 2012 - Project 2
 * Section 01
 */
public class BinarySearchTree<AnyType extends Comparable<? super AnyType>>
{

	private BinaryNode<AnyType> root;
	public static final int NODES_PER_LINE = 4;

	/**
	 * Default constructor for empty tree.
	 */
	public BinarySearchTree() {
		root = null;	
	}

	/**
	 * Constructs a new tree using the passed in node as the root.
	 * @param t BinaryNode that will root tree
	 */
	public BinarySearchTree (BinaryNode<AnyType> t) {
		root = t;
	}

	/**
	 * Wrapper method for recursive insert function, calls private recursive method
	 * to insert passed in AnyType into the binary search tree.
	 * @param x Object to insert into tree
	 */
	public void insert(AnyType x) {
		// Always start insertion at root, with level 0
		root = insert(root, x, root, 0);
	}

	/**
	 * Recursive method to insert an Object into the Binary Search Tree. parent, x, t should
	 * be non-null.
	 * @param parent Parent node, used to assign a parent reference to a node when insertion point is found
	 * @param x Object to insert into tree
	 * @param t Node we are currently traversing to find insertion point
	 * @param level Level of the tree the traversion is currently at, used to assign level to node
	 * @return current node (never used)
	 */
	private BinaryNode<AnyType> insert(BinaryNode<AnyType> parent, AnyType x, BinaryNode<AnyType> t, int level) {
		
		// If we've reached a null node, this must be where we insert
		if( t == null ) {
			return new BinaryNode<AnyType>(parent, x, null, null, level);
		}

		// Compare to see which subtree to follow
		int compareResult = x.compareTo( t.element );

		if( compareResult < 0 ) {
			// Go left, incrementing the weight of the node we're leaving behind
			t.weight++;
			// Attempt to insert into the left node, passing in the current node as the parent
			t.left = insert(t, x, t.left, level+1 );
		} else if( compareResult > 0 ) {
			// Same idea for right side
			t.weight++;
			t.right = insert(t, x, t.right, level+1);
		} else {
			// If duplicate, do nothing
			; 
		}
		return t;
	}

	/**
	 * Internal recursive method to find height of passed in node.
	 * @param t node from which height must be calculated
	 * @return height of node
	 */
	private int height(BinaryNode<AnyType> t) {
		// If we're at root, the height is -1 by convention
		if( t == null ) {
			return -1;
		} else {
			// Recursively traverse tree, returning the height of the deeper subtree
			return 1 + Math.max( height( t.left ), height( t.right ) );
		}
	}

	/**
	 * Public wrapper method for recursively finding height, 
	 * always returns height of root node. Root must not be null.
	 * @return height of root node.
	 */
	public int getHeight () {
		return height(root);
	}

	/**
	 * Returns the amount of nodes in the tree (using the weight of the root). 
	 * Root must not be null.
	 * @return number of nodes in tree
	 */
	public int getNodes () {
		return root.weight;
	}

	/**
	 * Returns the median object in the tree. Root must not be null.
	 * @return AnyType median in tree
	 */
	public AnyType getMedian () {

		BinaryNode<AnyType> node = root;

		// Calculate location of median
		int count = (int)Math.ceil(root.weight/2.0);

		int leftTree = 0;

		while(node != null)	{
			
			if (node.left == null) {
				leftTree = 0;
			} else {
				leftTree = node.left.weight;
			}

			if (leftTree + 1 == count) {
				// We've found median
				return node.element;
			} else if (leftTree < count) {
				// Median must be in right subtree
				node = node.right;
				count -= leftTree+1;
			}
			else {
				// Median must be left subtree
				node = node.left;
			}
			
		}
		
		// Need a return type
		return node.element;
	}

	/**
	 * Traverses (using pre-order traversal) the tree rooted at the passed in node, 
	 * and stores values in passed in LinkedList instead of printing them. t must not be 
	 * null, and queue must be a valid LinkedList.
	 * @param t Node at which to start traversal
	 * @param queue LinkedList to store traversal output
	 */
	private void preorderTraverse( BinaryNode<AnyType> t, LinkedList<AnyType> queue) {
		if( t != null )	{
			queue.add(t.element);
			preorderTraverse( t.left, queue);
			preorderTraverse( t.right, queue);
		}
	}

	/**
	 * Traverses (using in-order traversal) the tree rooted at the passed in node, 
	 * and stores values in passed in ArrayList instead of printing them. t must not be
	 * null, and stack must be a valid ArrayList.
	 * @param t Node at which to start traversal
	 * @param queue LinkedList to store traversal output
	 */
	private void inorderTraverse( BinaryNode<AnyType> t, ArrayList<AnyType> stack ) {
		if( t != null ) {
			inorderTraverse( t.left, stack );
			stack.add(t.element);
			inorderTraverse( t.right, stack);
		}
	}

	/**
	 * Traverses (using level-order traversal, down to the level passed in) the tree from the root,
	 * and stores values in a LinkedList of printerTuple objects, which is then returned.
	 * @param t Node at which to start traversal
	 * @param queue LinkedList to store traversal output
	 */
	private LinkedList<printerTuple> levelorderTraverse(int limit) {

		int level = -1;
		LinkedList<BinaryNode<AnyType>> queue = new LinkedList<BinaryNode<AnyType>>();
		LinkedList<printerTuple> output = new LinkedList<printerTuple>();
		
		// Add root to queue
		queue.addFirst(root);

		// Traverse until the required level limit (passed in from command line)
		while (level <= limit) {
			BinaryNode<AnyType> t;
			
			if (!queue.isEmpty()) {
				// If the stack isn't empty, dequeue a node
				t = queue.removeLast();
			} else {
				// Otherwise, we're done
				return output;
			}
			// Look at dequeued node
			if (t != null) {
				level = t.level;
				if (level <= limit) {
					// If we're at the right level, add node to output, enqueue it's children
					output.add(new printerTuple(t.toString(), t.level));
					queue.addFirst(t.left);
					queue.addFirst(t.right);
				}
			}
		}
		
		// Return the stack of printerTuples
		return output;
	}
	
	/**
	 * Prints the level-order traversal of the tree from the root, in the form of
	 * (parent value, node value, weight of node). Limit must be => 0.
	 * @param limit Level until which to print the tree
	 */
	public void printLevelOrder (int limit) {

		// Get stack of printerTuples from level-order traversal
		LinkedList<printerTuple> output = levelorderTraverse(limit);
		StringBuilder sb = new StringBuilder();
		
		// Check if limit required exceeds the height of the tree
		if (limit > getHeight()) {
			limit = getHeight();
		}
		
		for (int i = 0; i <= limit; i++) {

			ListIterator<printerTuple> itr = output.listIterator();

			sb.append("\nLevel " + i + ": \n\t");

			int count = 0;
			while (itr.hasNext()) {
				// Go through stack and find tuples that are at the current level
				printerTuple current = itr.next();
				if (current.getLevel() == i) {
					// Append the found tuple to our output
					sb.append(current.getOutput() + " ");
					count++;
					if (count == NODES_PER_LINE) {
						// If we're past 4 nodes, start a new line
						sb.append("\n\t");
						count = 0;
					}
				}
			}
		}
		
		// Print the completed output
		System.out.println(sb);
	}

	/**
	 * This class balances the BST passed in and returns the balanced tree.
	 * @param t BinarySearchTree to balance
	 * @return the balanced BinarySearchTree
	 */
	public BinarySearchTree<AnyType> balance (BinarySearchTree<AnyType> t) {

		// Wrapper for recursive balancing method
		BinarySearchTree<AnyType> p = recursiveBalance(t);

		/* Since balancing destroys node parent references, weights, and levels, 
		 * it is necessary to insert all the nodes from the balanced tree into a new tree, 
		 * as the insert function corrects parents, weights, and levels.
		 */
		
		BinarySearchTree<AnyType> newTree = new BinarySearchTree<AnyType>();
		newTree.insert(p.root.element);

		// Pre-order traversal will conserve the balanced order.
		LinkedList<AnyType> stack = new LinkedList<AnyType>();
		preorderTraverse(t.root, stack);
		Iterator<AnyType> itr = stack.iterator();
		while (itr.hasNext()) {
			AnyType temp = itr.next();
			if (temp != p.root.element) { 
				newTree.insert(temp);
			}
		}
		
		// Return the balanced and corrected tree
		return newTree;
	}

	/**
	 * Internal method that handles the recursion for balancing a BinarySearchTree.
	 * @param t tree to balance
	 * @return balanced tree
	 */
	private BinarySearchTree<AnyType> recursiveBalance (BinarySearchTree<AnyType> t) {

		// Balance the tree
		t = balanceTree(t);
		
		// If the left node isn't null, balance the tree that is rooted in the left node
		if (t.root.left != null) {

			/* Notice every node will eventually become the root node of a tree, so all
			parent references are reset to -1 and all levels are reset to 0 */
			t.root.left = recursiveBalance(new BinarySearchTree<AnyType>(t.root.left)).root;
		}
		
		// If the right node isn't null, balance the tree that is rooted in the right node
		if (t.root.right != null) {
			t.root.right = recursiveBalance(new BinarySearchTree<AnyType>(t.root.right)).root;
		}
		
		// Return balanced tree
		return t;
	}

	/**
	 * Internal helper method that completes one iteration of the balancing algorithm
	 * from the project description 
	 * @param t tree to balance
	 * @return balanced tree
	 */
	private BinarySearchTree<AnyType> balanceTree (BinarySearchTree<AnyType> t) {

		// Get median and insert into root of a new tree
		AnyType median = t.getMedian();
		BinarySearchTree<AnyType> newTree = new BinarySearchTree<AnyType>();
		newTree.insert(median);

		// Insert all values except median into new tree
		ArrayList<AnyType> stack = new ArrayList<AnyType>();
		inorderTraverse(t.root, stack);
		Iterator<AnyType> itr = stack.iterator();
		while (itr.hasNext()) {
			AnyType temp = itr.next();
			if (temp != median) { 
				newTree.insert(temp);
			}
		}
		
		// Return the new tree
		t.root = newTree.root;
		return t;
	}

	/**
	 * Inner helper class to store a String representation of a
	 * Binary Node and it's level (in an int) in one object to assist in printing
	 * nodes.
	 * 
	 * @version 03/27/12
	 * @author Rostislav Tsiomenko <tsiomr1@umbc.edu>
	 * CMSC 341 - Spring 2012 - Project 2
	 * Section 01
	 */
	private static class printerTuple {
		
		String output;
		int level;
		
		/**
		 * Constructor for a printerTuple
		 * @param output Node toString() output to store
		 * @param level Level of the node
		 */
		printerTuple(String output, int level) {
			this.output = output;
			this.level = level;
		}
		
		/**
		 * Accessor for print output.
		 * @return output string
		 */
		String getOutput () {
			return output;
		}

		/**
		 * Accessor for node level
		 * @return node level
		 */
		int getLevel () {
			return level;
		}
	}

	/**
	 * This inner class models a Binary Node in a BST. A node
	 * consists of references to parent, left, and right nodes, 
	 * the Object the node is holding, and the weight and level
	 * of the node.
	 * 
	 * @version 03/27/12
	 * @author Rostislav Tsiomenko <tsiomr1@umbc.edu>
	 * CMSC 341 - Spring 2012 - Project 2
	 * Section 01
	 */
	private static class BinaryNode<AnyType> {

		BinaryNode<AnyType> parent;
		AnyType element;            
		BinaryNode<AnyType> left;   
		BinaryNode<AnyType> right;  
		int weight;
		int level;

		/**
		 * Constructor for a node.
		 * @param parent reference to parent node
		 * @param theElement Object to hold in node
		 * @param lt reference to left node
		 * @param rt reference to right node
		 * @param level level of the node
		 */
		BinaryNode(BinaryNode<AnyType> parent, AnyType theElement, BinaryNode<AnyType> lt, BinaryNode<AnyType> rt, int level){
			this.level = level;
			this.parent = parent;
			element = theElement;
			left = lt;
			right = rt;
			// All nodes start with weight 1
			weight = 1;
		}
		
		/**
		 * Returns the String representation of the node, which for this project is
		 * (value of parent node, value of node, weight of node).
		 * @return String representation of this node
		 */
		public String toString(){
			String s = "(" + (parent == null ? -1 : parent.element) + "," + element + "," + weight + ")";
			return String.format("%-13s", s);
		}
	}
}