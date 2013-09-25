package proj1;

/**
 * This class models a disk block.
 * 
 * @version 03/04/12
 * @author Rostislav Tsiomenko <tsiomr1@umbc.edu>
 * Project: CMSC 341 - Spring 2012 - Project 1
 * Section: 01
 */
public class Block implements Comparable<Block> {

	private int number;

	/**
	 * Constructor for <code>Block</code>.
	 * @param number the number of this <code>Block</code>
	 */
	public Block (int number) {
		this.number = number;
	}
	
	/**
	 * Returns <code>Block</code> number.
	 * @return <code>Block</code> number
	 */
	public int getNum () {
		return number;
	}

	/**
	 * Compares <code>Blocks</code> based on number.
     * @see java.lang.Comparable
     */
	public int compareTo(Block block) {
		if (number < block.getNum()) {
			return -1;
		} else if (number > block.getNum()) {
			return 1;
		} else {
			return 0;
		}
	}
}
