package proj3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Driver for Project 3. Reads command file and performs best-first search to find 
 * the path from the initial state to the goal state.
 * 
 * @version 04/15/12
 * @author Rostislav Tsiomenko <tsiomr1@umbc.edu>
 * Project: CMSC 341 - Spring 2012 - Project 3
 * Section: 01
 */
public class Project3 {

	private final static int CMD_LINE_ARGS = 1;
	private final static int BOARD_SIZE = 9;
	private final static int STATES_TO_PRINT = 5;

	public static void main (String args[]) {

		// Check command line arguments
		if (args.length != CMD_LINE_ARGS)
		{
			System.err.println("Usage: java Project3 file");
			System.exit(-1);
		}

		try {

			Scanner input = new Scanner(new File(args[0]));

			State goal = new State(0, -1, null);
			State initial = new State(0, -1, goal);

			// Read command file into initial and goal States
			while(input.hasNextInt()) {
				for (int i = 0; i < BOARD_SIZE; i++){
					initial.insert(input.nextInt());
				}
				for (int j = 0; j < BOARD_SIZE; j++){
					goal.insert(input.nextInt());
				}
			}
			input.close();

			boolean foundGoal = false;
			int count = 0;
			LinkedList<State> firstFive = new LinkedList<State>();

			// Create priority queue and enqueue initial State
			PriorityQueue<State> openQ = new PriorityQueue<State>();
			openQ.enqueue(initial);

			while (foundGoal == false) {

				State currentState = openQ.dequeue();

				// Save first X states to print
				if (count < STATES_TO_PRINT) {
					firstFive.add(currentState);
					count++;
				}

				if (currentState.isEqual(goal)) {

					// If solution is found, print appropriate output
					foundGoal = true;
					System.out.println("Solution found!\n");
					System.out.println("Initial state: ");
					System.out.println(initial);
					System.out.println("Cost: " + initial.calculateMerit());
					System.out.println("g value: " + initial.getPath());
					System.out.println("h value: " + (initial.calculateMerit() - initial.getPath()));
					System.out.println("\nGoal state: ");
					System.out.println(currentState);
					System.out.println("Cost: " + currentState.calculateMerit());
					System.out.println("g value: " + currentState.getPath());
					System.out.println("h value: " + (currentState.calculateMerit() - currentState.getPath()));
					System.out.println("\nSolution cost: " + currentState.getPath());
					System.out.println("Size of openQ: " + openQ.size());
					System.out.println("\nFirst five states: ");
					int stateNum = 1;

					while (!firstFive.isEmpty()) {
						State removed = firstFive.removeFirst();
						System.out.println("\nState: " + stateNum++);
						System.out.print(removed);
						System.out.println("Cost: " + removed.calculateMerit());
						System.out.println("g value: " + removed.getPath());
						System.out.println("h value: " + (removed.calculateMerit() - removed.getPath()));
					}
					
				} else {

					// Otherwise, generate and enqueue children
					LinkedList<State> children = currentState.generateStates();
					while (!children.isEmpty()) {
						openQ.enqueue(children.removeFirst());
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("Cannot find the file specified.");
			System.err.println("java Project3 file");
		}
	}
}
