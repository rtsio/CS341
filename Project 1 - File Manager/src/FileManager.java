package proj1;

import java.util.Iterator;

/**
 * This class models a file manager that can create, extend, 
 * truncate, and delete <code>Files</code>, and keeps track of <code>Blocks</code> and
 * bytes for each <code>File</code>.
 * 
 * @version 03/04/12
 * @author Rostislav Tsiomenko <tsiomr1@umbc.edu>
 * Project: CMSC 341 - Spring 2012 - Project 1
 * Section: 01
 */
public class FileManager {

	private int totalBlocks;
	private int blockSize;
	private MyLinkedList<Block> freeBlocks;
	private MyLinkedList<File> FAT32;
	
	/**
	 * Constructor for <code>FileManager</code>.
	 * Pre-condition: blocks and size must be greater than 0.
	 *
	 * @param blocks the number of <code>Blocks</code> in our "disk"
	 * @param size number of bytes per <code>Block</code>
	 */
	public FileManager (int blocks, int size) {
		
		// Initialize variables
		totalBlocks = blocks;
		blockSize = size;
		freeBlocks = new MyLinkedList<Block>();
		FAT32 = new MyLinkedList<File>();
		// Populate list of available blocks
		for (int i = 0; i < totalBlocks; i++) {
			freeBlocks.add(new Block(i));
		}
	}
	
	/**
	 * Creates a <code>File</code> and places it into FAT32, the <code>File</code> list.
	 * Pre-condition: size must be greater than 0
	 * Post-condition: creates a <code>File</code> with appropriate number of blocks 
	 * 				   and places it into internal <code>File</code> list (FAT32)	
	 * @param filename the name of the <code>File</code>
	 * @param size bytes in the <code>File</code>
	 * @return successful creation message
	 * @throws InSufficientDiskSpaceException if there are not enough blocks to create <code>File</code>
	 * @throws DuplicateFileException if a <code>File</code> with given filename already exists
	 */
	public String create(String filename, int size) throws InsufficientDiskSpaceException, DuplicateFileException {
		
		Iterator<File> findDuplicates = FAT32.iterator();
		// Go through file list to find duplicates first
		while (findDuplicates.hasNext()){
			if (findDuplicates.next().getName().equalsIgnoreCase(filename)) {
				throw new DuplicateFileException();
			}
		}
		// Check for available space
		if (size > (freeBlocks.size() * blockSize)) {
			throw new InsufficientDiskSpaceException("Not enough disk space to create new file.\n");
		} else {
			File file = new File(size, blockSize, filename);
			Iterator<Block> itr = freeBlocks.iterator();
			// Iterate through list of available blocks, adding them to the new file as needed
			for (int i = 0; i < bytesBlocks(size); i++) {
				if (itr.hasNext()) {
					file.add(itr.next());
					// After block is added to file, it's removed from available blocks
					itr.remove();
				} 
			}
			
			// Add file to file table
			FAT32.add(file);
			return filename + " created successfully!\n";
		}
	}
	
	/**
	 * Deletes a <code>File</code> with the given name from the internal <code>File</code> list
	 * Post-condition: <code>File</code> with the passed in filename will be deleted and 
	 * 				   its <code>Blocks</code> added back to available <code>Blocks</code>	
	 * @param filename the name of the <code>File</code>
	 * @return successful deletion message
	 * @throws NoSuchFileException if the filename does not match any existing <code>File</code>
	 */
	public String delete(String filename) throws NoSuchFileException {
		
		Iterator<File> itr = FAT32.iterator();
		boolean fileFound = false;
		
		// Iterate through list of files to look for filename
		while (itr.hasNext()) {
			File file = itr.next();
			if (file.getName().equals(filename)) {
				fileFound = true;
				// Iterate through file's blocks, adding them back to available blocks
				for (int i = 0; i < bytesBlocks(file.getBytes()); i++){
					freeBlocks.add(file.remove());
				}
				// Remove file
				itr.remove();
			}
		}
		
		// Throw an exception or return appropriate message
		if (fileFound == false) {
			throw new NoSuchFileException(filename + " does not exist!");
		} else {
			return filename + " deleted successfully!\n";
		}

	}

	/**
	 * Extends a <code>File</code> by the given size in bytes.
	 * Pre-condition: size must be greater than 0
	 * Post-condition: <code>File</code> with the given filename will be extended
	 * 				   by passed in amount of bytes, and additional <code>Blocks</code> will be
	 * 				   allocated to it as needed	
	 * @param filename the name of the <code>File</code>
	 * @param size number of bytes to increase the <code>File</code> by
	 * @return successful extension message
	 * @throws InSufficientDiskSpaceException if there are not enough blocks to extend <code>File</code>
	 * @throws NoSuchFileException if the filename does not match any existing <code>File</code>
	 */
	public String extend(String filename, int size) throws InsufficientDiskSpaceException, NoSuchFileException {
		
		Iterator<File> findFile = FAT32.iterator();
		boolean fileFound = false;
		
		// Check if file exists
		while (findFile.hasNext()){
			if (findFile.next().getName().equalsIgnoreCase(filename)) {
				fileFound = true;
			}
		}

		// Throw exception if it doesn't
		if (fileFound == false) {
			throw new NoSuchFileException(filename + " does not exist!");
		}

		// Check there is enough space to extend file
		if (size > (freeBlocks.size() * blockSize)) {
			throw new InsufficientDiskSpaceException("Not enough disk space to extend " + filename + " \n");
		} else {
			
			Iterator<File> itr = FAT32.iterator();
			// Iterate through list of files to find filename
			while (itr.hasNext()) {
				File file = itr.next();  
				if (file.getName().equals(filename)) {
					// Calculate the extended size and how many blocks need to be added
					int newSize = file.getBytes() + size;
					int blocksToAdd = bytesBlocks(newSize) - bytesBlocks(file.getBytes());
					// Take blocks from available blocks and add them to the file
					for (int i = 0; i < blocksToAdd; i++){
						file.add(freeBlocks.remove(0));
					}
					file.addBytes(size);
				}
			}
			
			// Throw exception or return appropriate message
			if (fileFound == false) {
				throw new NoSuchFileException(filename + " does not exist!");
			} else {
				return filename + " extended successfully!\n";
			}
		}
	}
	
	/**
	 * Truncates a <code>File</code> by the given size in bytes.
	 * Pre-condition: size must be greater than 0
	 * Post-condition: <code>File</code> with the given filename will be truncated
	 * 				   by passed in amount of bytes, and <code>Blocks</code> will be
	 * 				   de-allocated to it as needed	
	 * @param filename the name of the <code>File</code>
	 * @param size number of bytes to truncate the <code>File</code> by
	 * @return successful truncation message
	 * @throws NoSuchFileException if the filename does not match any existing <code>File</code>
	 * @throws FileUnderflowException if number of truncating bytes is greater than <code>File</code> size
	 */
	public String truncate(String filename, int size) throws NoSuchFileException, FileUnderflowException {
		
		Iterator<File> itr = FAT32.iterator();
		boolean fileFound = false;
		// Iterate through list of files to find filename
		while (itr.hasNext()) {
			File file = itr.next();  
			if (file.getName().equals(filename)) {
				fileFound = true;
				// If truncating by total file size, delete the file instead
				if (file.getBytes() == size) {
					delete(filename);
				} else if (file.getBytes() < size) {
					throw new FileUnderflowException("Tried to truncate " + filename + " by " + size + 
							" bytes, but it is only " + file.getBytes() + " bytes!\n");
				} else {
					// Calculate the truncated size and how many blocks need to be removed
					int newSize = file.getBytes() - size;
					int blocksToRemove = bytesBlocks(file.getBytes()) - bytesBlocks(newSize);
					// Take blocks from file and add them to available blocks
					for (int i = 0; i < blocksToRemove; i++){
						freeBlocks.add(file.remove());
					}	
					file.delBytes(size);
				}
			}
		}
		
		// Throw exception or return appropriate message
		if (fileFound == false) {
			throw new NoSuchFileException(filename + " does not exist!");
		} else {
			return filename + " truncated successfully!\n";
		}

	}
	
	/**
	 * Returns a <code>String</code> representation of this object.
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		// Create StringBuilder Object to output FileManager status
		StringBuilder sb = new StringBuilder( "\nFile Manager Status:\n--------------------\n" );
		
		// Appending easy details
		sb.append(String.format("Disk block size:  %-5d \n", blockSize));
		sb.append(String.format("Number of blocks: %-5d \n", totalBlocks));
		sb.append(String.format("Allocated blocks: %-5d \n", (totalBlocks - freeBlocks.size())));
		sb.append(String.format("Free blocks:      %-5d \n", freeBlocks.size()));
		
		// Appending disk block ranges
		sb.append("\nFree disk blocks:\n-----------------\n");
		Iterator<Block> blockItr = freeBlocks.iterator();
		if (freeBlocks.isEmpty()) {
			sb.append("There are no free disk blocks.\n");
		} else {
			// Algorithm to append only integer ranges
			int prev = freeBlocks.get(0).getNum();
			while (blockItr.hasNext()) {
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
		}
		
		// Appending files and their details
		sb.append("\nFiles:\n------\n");
		if (FAT32.size() == 0) {
			sb.append("There are no files.\n");
		} else {
			Iterator<File> fileItr = FAT32.iterator();
			while (fileItr.hasNext()) {
				sb.append(fileItr.next().toString());
			}
		}
		
		return new String( sb );
	}
	
	/**
	 * Returns the amount of <code>Blocks</code> needed for passed in byte size.
	 * @param size number of bytes to convert to <code>Blocks</code>
	 * @return number of <code>Blocks</code> needed for given bytes
	 */
	private int bytesBlocks (int size) {
		// Round up the result of bytes divided by bytes-per-block
		return (int)(Math.ceil(size / (float)blockSize));
	}

}
