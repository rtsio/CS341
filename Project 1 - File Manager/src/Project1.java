package proj1;

import java.io.*;

/**
 * This is the driver class for Project 1.
 * 
 * @version 03/04/12
 * @author Rostislav Tsiomenko <tsiomr1@umbc.edu>
 * Project: CMSC 341 - Spring 2012 - Project 1
 * Section: 01
 */
public class Project1 {

	/**
	 * Number of command line arguments required to run program.
	 */
	public static final int CMD_LINE_ARGS = 3;

	public static void main (String args[]) {

		// Check for command line arguments
		if (args.length != CMD_LINE_ARGS)
		{
			System.err.println("Usage: java Project1 number-of-blocks bytes-per-block path-to-command-file\n" +
					"For example: java Project1 20000 1024 commands.txt\n ");
			System.exit(-1);
		}

		int blocks = Integer.parseInt(args[0]);
		int blockSize = Integer.parseInt(args[1]);
		
		// Check that blocks and block size is at least 1
		if (blocks < 1 || blockSize < 1) {
			System.err.println("Blocks and bytes per blocks must both be greater than 0.");
			System.err.println("Usage: java Project1 number-of-blocks bytes-per-block path-to-command-file\n" +
					"For example: java Project1 20000 1024 commands.txt\n ");
			System.exit(-1);
		}

		// Create new FileManager object with the passed in parameters
		FileManager disk = new FileManager(blocks, blockSize);

		try {
			FileReader fileInput = new FileReader(args[2]);
			BufferedReader input = new BufferedReader(fileInput);
			String line;
			
			while ((line = input.readLine()) != null) {
				
				String[] parts = line.split(" ");
				if (parts[0].equals("CREATE")) {
					// Creating a file
					System.out.printf("Command: %-10s %-10s %-6s \n", parts[0], parts[1], parts[2]);
					try {
						System.out.println(disk.create(parts[1], Integer.parseInt(parts[2])));
					} catch (DuplicateFileException e){
						System.out.println(e.getMessage());
					} catch (InsufficientDiskSpaceException e) {
						System.out.println(e.getMessage());
					}

				} else if (parts[0].equals("DELETE")) {
					// Deleting a file
					System.out.printf("Command: %-10s %-10s\n", parts[0], parts[1]);
					try {
						System.out.println(disk.delete(parts[1]));
					} catch (NoSuchFileException e) {
						System.out.println(e.getMessage());
					}

				} else if (parts[0].equals("EXTEND")) {
					// Extending a file
					System.out.printf("Command: %-10s %-10s %-6s\n", parts[0], parts[1], parts[2]);
					try {
						System.out.println(disk.extend(parts[1], Integer.parseInt(parts[2])));
					} catch (NoSuchFileException e){
						System.out.println(e.getMessage());
					} catch (InsufficientDiskSpaceException e) {
						System.out.println(e.getMessage());
					}

				} else if (parts[0].equals("TRUNCATE")) {
					// Truncating a file
					System.out.printf("Command: %-10s %-10s %-6s\n", parts[0], parts[1], parts[2]);
					try {
						System.out.println(disk.truncate(parts[1], Integer.parseInt(parts[2])));
					} catch (NoSuchFileException e) {
						System.out.println(e.getMessage());
					} catch (FileUnderflowException e) {
						System.out.println(e.getMessage());
					}

				} else if (parts[0].equals("PRINT")) {
					// Printing out disk status
					System.out.printf("Command: %-10s\n", parts[0]);
					System.out.println(disk.toString());
				}
			}

			input.close();

		}catch (FileNotFoundException e){
			System.err.println("Cannot find the file specified.");
			System.err.println("Usage: java Project1 number-of-blocks bytes-per-block path-to-command-file\n" +
					"For example: java Project1 20000 1024 commands.txt\n ");
		}catch (IOException e){
			e.printStackTrace();
		}
	}
}
