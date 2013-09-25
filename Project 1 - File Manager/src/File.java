package proj1;

import java.util.Iterator;

/**
 * This class models a file. A <code>File</code> consists of a name, 
 * number of bytes, and a <code>MyLinkedList</code> of <code>Block</code> objects
 * (to simulate blocks assigned to the file). <code>Files</code> can be
 * compared to other <code>Files</code> using their filenames.
 * 
 * @version 03/04/12
 * @author Rostislav Tsiomenko <tsiomr1@umbc.edu>
 * Project: CMSC 341 - Spring 2012 - Project 1
 * Section: 01
 */
public class File implements Comparable<File> {

	private int bytes;
	private int blockSize;
	private String name;
	private MyLinkedList<Block> blocks;
	
	/**
	 * Constructor for a <code>File</code> object.
	 * Pre-condition: <code>size</code> and <code>blockSize</code> must be greater than 0
	 * 
	 * @param bytes size of the file in bytes
	 * @param blockSize number of bytes per block in the <code>FileManager</code> using this File
	 * 		  (needed to calculate allocated disk size)
	 * @param name name of the file
	 */
	public File (int bytes, int blockSize, String name) {
		// Initialize variables and create list of Blocks
		this.bytes = bytes;
		this.name = name;
		this.blockSize = blockSize;
		blocks = new MyLinkedList<Block>();
	}

	/**
	 * Adds passed in <code>Block</code> to the list of <code>Blocks</code> in
	 * this <code>File</code> object.
	 * @param block <code>Block</code> object to add to File
	 */
	public void add (Block block) {
		blocks.add(block);
	}
	
	/**
	 * Removes and returns a <code>Block</code> object from the end of the internal block list.
	 * @return Block from the end of this File's list of blocks
	 */
	public Block remove () {
		// Remove block from the end of list
		return blocks.remove(blocks.size() - 1);
	}
	
	/**
	 * Return amount of bytes in <code>File</code>.
	 * @return the bytes
	 */
	public int getBytes() {
		return bytes;
	}

	/**
	 * Add bytes to <code>File</code>.
	 * @param bytes the bytes to add
	 */
	public void addBytes(int bytes) {
		this.bytes += bytes;
	}

	/**
	 * Delete bytes from <code>File</code>.
	 * @param bytes the bytes to delete
	 */
	public void delBytes(int bytes){
		this.bytes -= bytes;
	}

	/**
	 * Return name of the <code>File</code>.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns a <code>String</code> representation of this object.
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		
		// Create StringBuilder Object to output file details
		StringBuilder sb = new StringBuilder("File: ");
		
		// Appending easy details
		sb.append(name + "\n");
		sb.append("\tActual size (bytes): " + bytes + "\n");
		sb.append("\tAllocated size:      " + (blockSize * blocks.size()) + "\n");
		sb.append("\tBlocks allocated:    " + blocks.size() + "\n");
		
		// Appending disk block ranges
		sb.append("\tDisk blocks: ");
		Iterator<Block> blockItr = blocks.iterator();
		int prev = blocks.get(0).getNum();
		while (blockItr.hasNext()) {
			// Algorithm to append only integer ranges
			Block block = blockItr.next();
			if (block.getNum() >= (prev+2)) {
				sb.append(prev + "]");
				sb.append("[" + block.getNum() + " , ");
			} else if (block.getNum() == prev) {
				sb.append("[" + block.getNum() + " , ");
			}
			prev = block.getNum();
		}
		sb.append(prev + "]\n");
		
		return new String( sb );
	}

	/**
	 * Compares <code>Files</code> based on filename.
     * @see java.lang.Comparable
     */
	public int compareTo(File file) {
		// Compare filenames using String's compareTo method
		return name.compareTo(file.getName());
	}
}
