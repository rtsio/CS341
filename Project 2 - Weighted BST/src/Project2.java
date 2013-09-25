package proj2;

import java.io.*;
import java.util.*;

/**
 * This is the driver for Project 2.
 * @version 03/27/12
 * @author Rostislav Tsiomenko <tsiomr1@umbc.edu>
 * CMSC 341 - Spring 2012 - Project 2
 * Section 01
 */
public class Project2 {

	public static void main (String args[]) {
		
		// Check args
		if (args.length != 2) {
			System.err.println("Usage: Project2 file level");
			System.exit(-1);
		}
		
		// Check for negative level
		if (Integer.parseInt(args[1]) < 0) {
			System.err.println("Level cannot be negative.");
			System.exit(-1);
		}
		
		BinarySearchTree<Integer> p = new BinarySearchTree<Integer>();
		// HashSets are magical and allow for constant time operations
		HashSet<Integer> set = new HashSet<Integer>();
		Scanner scanner;
		int count = 0;
		
		try {
			scanner = new Scanner(new File(args[0]));
			while(scanner.hasNextInt()){
				count ++;
				int num = scanner.nextInt();
				// Ignore duplicates or bad things will happen
				if (!(set.contains(num))) {
					set.add(num);
					p.insert(num);
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("\nFile cannot be found, usage: Project2 file level");
		}
		
		System.out.println("Read " + count + " integers from file.\n");
		System.out.println("Before balancing, the tree stats are: ");
		System.out.println("Nodes: " + p.getNodes());
		System.out.println("Height: " + p.getHeight());
		System.out.println("Median: " + p.getMedian());
		System.out.println("Tree contents up to level " + Integer.parseInt(args[1]) + " are displayed below.");
		p.printLevelOrder(Integer.parseInt(args[1]));
		p = p.balance(p);
		System.out.println("\n*Balancing tree..*\n");
		System.out.println("After balancing, the tree stats are: ");
		System.out.println("Nodes: " + p.getNodes());
		System.out.println("Height: " + p.getHeight());
		System.out.println("Median: " + p.getMedian());
		System.out.println("Tree contents up to level " + Integer.parseInt(args[1]) + " are displayed below.");
		p.printLevelOrder(Integer.parseInt(args[1]));
		System.out.println("\n\nLive long and prosper.\n");
	}
}
