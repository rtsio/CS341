package proj3;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Models a "state" of an 8-puzzle, which is essentially 
 * the layout of the 8 numbers + the blank space on the game board.
 * 
 * @version 04/15/12
 * @author Rostislav Tsiomenko <tsiomr1@umbc.edu>
 * Project: CMSC 341 - Spring 2012 - Project 3
 * Section: 01
 */
public class State implements Comparable<State> {

	private ArrayList<Integer> board;
	private int parentIndex;
	private State goalState;
	private int path;
	private final static int BOARD_SIZE = 9;
	private final static int UPPER_LEFT = 0;
	private final static int CENTER_LEFT = 3;
	private final static int LOWER_LEFT = 6;
	private final static int UPPER_CENTER = 1;
	private final static int CENTER = 4;
	private final static int LOWER_CENTER = 7;
	private final static int UPPER_RIGHT = 2;
	private final static int CENTER_RIGHT = 5;
	private final static int LOWER_RIGHT = 8;

	/**
	 * Constructor for a board state.
	 * @param path the number of moves it has taken to get to this state
	 * @param parentBlankIndex the position of the "blank" spot on the board of this State's "parent" State
	 * @param goalState reference to the State which we are trying to get to
	 */
	public State (int path, int parentBlankIndex, State goalState) {
		this.path = path;
		this.goalState = goalState;
		this.parentIndex = parentBlankIndex;
		board = new ArrayList<Integer>();
	}

	/**
	 * Adds a single integer to the end of this State.
	 * @param i integer to add to state
	 */
	public void insert(Integer i) {
		board.add(i);
	}
	
	/**
	 * Adds 3 integers to this State.
	 * @param i first integer
	 * @param j second integer
	 * @param k third integer
	 */
	public void insert(Integer i, Integer j, Integer k) {
		board.add(i);
		board.add(j);
		board.add(k);
	}

	/**
	 * Returns the number of moves it took to generate this State.
	 * @return number of moves
	 */
	public int getPath(){
		return path;
	}

	/** 
	 * Returns a reference to this State's internal game "board" of numbers.
	 * @return internal ArrayList of integers that represents this State
	 */
	public ArrayList<Integer> getBoard (){
		return board;
	}

	/** 
	 * Calculates the merit value of this State based on g (number of moves
	 * to get to this State) + h (heuristic to determine distance from goal State).
	 * @return merit value of this State (h + g values)
	 */
	public Integer calculateMerit() {
		
		int count = 0;
		// Get the goal State's layout
		ArrayList<Integer> goal = goalState.getBoard();
		
		// Add 1 for every integer out of place
		for (int i = 0; i < BOARD_SIZE; i++) {
			if (board.get(i) != 0 && board.get(i) != goal.get(i)) {
				count++;
			}
		}
		return Integer.valueOf(path + count);
	}

	/**
	 * Method of the Comparable interface, compares two State objects
	 * using their merit value (g + h values).
	 */
	public int compareTo(State o) {
		return this.calculateMerit().compareTo(o.calculateMerit());
	}

	/**
	 * Compares two State objects based on their layout (merit, 
	 * g, and/or h values are NOT compared). Overrides Object's
	 * isEqual().
	 * @param o State to compare with
	 * @return false if State layouts are different, true if same
	 */
	public boolean isEqual(State o){

		for (int i = 0; i < BOARD_SIZE; i++) {
			if (board.get(i) != o.getBoard().get(i)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns a String representation of this Object, 
	 * aka the layout of the State board. Overrides 
	 * Object's toString().
	 */
	public String toString(){

		StringBuilder string = new StringBuilder();
		for (int i = 0; i < BOARD_SIZE; i+=3) {
			string.append(board.get(i) + " ");
			string.append(board.get(i+1) + " ");
			string.append(board.get(i+2) + "\n");
		}
		return new String (string);
	}

	/**
	 * Returns a LinkedList of States that can be 
	 * generated from the current State. State that is
	 * parent of current State is ignored.
	 * @return LinkedList of State children objects
	 */
	public LinkedList<State> generateStates() {

		LinkedList<State> children = new LinkedList<State>();
		
		// Get current location of the "blank" space
		int blank = board.indexOf(0);

		if (blank == UPPER_LEFT) {
			
			// Generate possible children States in up/left/down/right order; the path/amount of moves/g value of new States is incremented
			State shiftDown = new State(path + 1, blank, goalState);
			shiftDown.insert(board.get(CENTER_LEFT), board.get(UPPER_CENTER), board.get(UPPER_RIGHT));
			shiftDown.insert(board.get(UPPER_LEFT), board.get(CENTER), board.get(CENTER_RIGHT));
			shiftDown.insert(board.get(LOWER_LEFT), board.get(LOWER_CENTER), board.get(LOWER_RIGHT));
			/* If the newly generated State's "blank" position is the same as in the parent of this State, 
			 * don't return it; newly generated States keep track of their parent's blank position */
			if (shiftDown.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftDown);
			}

			// If the "blank" spot is in the upper left spot, we can only shift it down or right, etc.
			State shiftRight = new State(path + 1, blank, goalState);
			shiftRight.insert(board.get(UPPER_CENTER), board.get(UPPER_LEFT), board.get(UPPER_RIGHT));
			shiftRight.insert(board.get(CENTER_LEFT), board.get(CENTER), board.get(CENTER_RIGHT));
			shiftRight.insert(board.get(LOWER_LEFT), board.get(LOWER_CENTER), board.get(LOWER_RIGHT));
			if (shiftRight.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftRight);
			}
		// Rest of code is identical generation of the different possible children generations, so comments are left out
		} else if (blank == UPPER_CENTER) {

			State shiftLeft = new State(path + 1, blank, goalState);
			shiftLeft.insert(board.get(UPPER_CENTER), board.get(UPPER_LEFT), board.get(UPPER_RIGHT));
			shiftLeft.insert(board.get(CENTER_LEFT), board.get(CENTER), board.get(CENTER_RIGHT));
			shiftLeft.insert(board.get(LOWER_LEFT), board.get(LOWER_CENTER), board.get(LOWER_RIGHT));
			if (shiftLeft.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftLeft);
			}

			State shiftDown = new State(path + 1, blank, goalState);
			shiftDown.insert(board.get(UPPER_LEFT), board.get(CENTER), board.get(UPPER_RIGHT));
			shiftDown.insert(board.get(CENTER_LEFT), board.get(UPPER_CENTER), board.get(CENTER_RIGHT));
			shiftDown.insert(board.get(LOWER_LEFT), board.get(LOWER_CENTER), board.get(LOWER_RIGHT));
			if (shiftDown.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftDown);
			}

			State shiftRight = new State(path + 1, blank, goalState);
			shiftRight.insert(board.get(UPPER_LEFT), board.get(UPPER_RIGHT), board.get(UPPER_CENTER));
			shiftRight.insert(board.get(CENTER_LEFT), board.get(CENTER), board.get(CENTER_RIGHT));
			shiftRight.insert(board.get(LOWER_LEFT), board.get(LOWER_CENTER), board.get(LOWER_RIGHT));
			if (shiftRight.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftRight);
			}

		} else if (blank == UPPER_RIGHT) {

			State shiftLeft = new State(path + 1, blank, goalState);
			shiftLeft.insert(board.get(UPPER_LEFT), board.get(UPPER_RIGHT), board.get(UPPER_CENTER));
			shiftLeft.insert(board.get(CENTER_LEFT), board.get(CENTER), board.get(CENTER_RIGHT));
			shiftLeft.insert(board.get(LOWER_LEFT), board.get(LOWER_CENTER), board.get(LOWER_RIGHT));
			if (shiftLeft.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftLeft);
			}

			State shiftDown = new State(path + 1, blank, goalState);
			shiftDown.insert(board.get(UPPER_LEFT), board.get(UPPER_CENTER), board.get(CENTER_RIGHT));
			shiftDown.insert(board.get(CENTER_LEFT), board.get(CENTER), board.get(UPPER_RIGHT));
			shiftDown.insert(board.get(LOWER_LEFT), board.get(LOWER_CENTER), board.get(LOWER_RIGHT));
			if (shiftDown.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftDown);
			}

		} else if (blank == CENTER_LEFT) {

			State shiftUp = new State(path + 1, blank, goalState);
			shiftUp.insert(board.get(CENTER_LEFT), board.get(UPPER_CENTER), board.get(UPPER_RIGHT));
			shiftUp.insert(board.get(UPPER_LEFT), board.get(CENTER), board.get(CENTER_RIGHT));
			shiftUp.insert(board.get(LOWER_LEFT), board.get(LOWER_CENTER), board.get(LOWER_RIGHT));
			if (shiftUp.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftUp);
			}

			State shiftDown = new State(path + 1, blank, goalState);
			shiftDown.insert(board.get(UPPER_LEFT), board.get(UPPER_CENTER), board.get(UPPER_RIGHT));
			shiftDown.insert(board.get(LOWER_LEFT), board.get(CENTER), board.get(CENTER_RIGHT));
			shiftDown.insert(board.get(CENTER_LEFT), board.get(LOWER_CENTER), board.get(LOWER_RIGHT));
			if (shiftDown.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftDown);
			}

			State shiftRight = new State(path + 1, blank, goalState);
			shiftRight.insert(board.get(UPPER_LEFT), board.get(UPPER_CENTER), board.get(UPPER_RIGHT));
			shiftRight.insert(board.get(CENTER), board.get(CENTER_LEFT), board.get(CENTER_RIGHT));
			shiftRight.insert(board.get(LOWER_LEFT), board.get(LOWER_CENTER), board.get(LOWER_RIGHT));
			if (shiftRight.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftRight);
			}

		} else if (blank == CENTER) {

			State shiftUp = new State(path + 1, blank, goalState);
			shiftUp.insert(board.get(UPPER_LEFT), board.get(CENTER), board.get(UPPER_RIGHT));
			shiftUp.insert(board.get(CENTER_LEFT), board.get(UPPER_CENTER), board.get(CENTER_RIGHT));
			shiftUp.insert(board.get(LOWER_LEFT), board.get(LOWER_CENTER), board.get(LOWER_RIGHT));
			if (shiftUp.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftUp);
			}

			State shiftLeft = new State(path + 1, blank, goalState);
			shiftLeft.insert(board.get(UPPER_LEFT), board.get(UPPER_CENTER), board.get(UPPER_RIGHT));
			shiftLeft.insert(board.get(CENTER), board.get(CENTER_LEFT), board.get(CENTER_RIGHT));
			shiftLeft.insert(board.get(LOWER_LEFT), board.get(LOWER_CENTER), board.get(LOWER_RIGHT));
			if (shiftLeft.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftLeft);
			}

			State shiftDown = new State(path + 1, blank, goalState);
			shiftDown.insert(board.get(UPPER_LEFT), board.get(UPPER_CENTER), board.get(UPPER_RIGHT));
			shiftDown.insert(board.get(CENTER_LEFT), board.get(LOWER_CENTER), board.get(CENTER_RIGHT));
			shiftDown.insert(board.get(LOWER_LEFT), board.get(CENTER), board.get(LOWER_RIGHT));
			if (shiftDown.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftDown);
			}

			State shiftRight = new State(path + 1, blank, goalState);
			shiftRight.insert(board.get(UPPER_LEFT), board.get(UPPER_CENTER), board.get(UPPER_RIGHT));
			shiftRight.insert(board.get(CENTER_LEFT), board.get(CENTER_RIGHT), board.get(CENTER));
			shiftRight.insert(board.get(LOWER_LEFT), board.get(LOWER_CENTER), board.get(LOWER_RIGHT));
			if (shiftRight.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftRight);
			}

		} else if (blank == CENTER_RIGHT) {

			State shiftUp = new State(path + 1, blank, goalState);
			shiftUp.insert(board.get(UPPER_LEFT), board.get(UPPER_CENTER), board.get(CENTER_RIGHT));
			shiftUp.insert(board.get(CENTER_LEFT), board.get(CENTER), board.get(UPPER_RIGHT));
			shiftUp.insert(board.get(LOWER_LEFT), board.get(LOWER_CENTER), board.get(LOWER_RIGHT));
			if (shiftUp.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftUp);
			}

			State shiftLeft = new State(path + 1, blank, goalState);
			shiftLeft.insert(board.get(UPPER_LEFT), board.get(UPPER_CENTER), board.get(UPPER_RIGHT));
			shiftLeft.insert(board.get(CENTER_LEFT), board.get(CENTER_RIGHT), board.get(CENTER));
			shiftLeft.insert(board.get(LOWER_LEFT), board.get(LOWER_CENTER), board.get(LOWER_RIGHT));
			if (shiftLeft.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftLeft);
			}

			State shiftDown = new State(path + 1, blank, goalState);
			shiftDown.insert(board.get(UPPER_LEFT), board.get(UPPER_CENTER), board.get(UPPER_RIGHT));
			shiftDown.insert(board.get(CENTER_LEFT), board.get(CENTER), board.get(LOWER_RIGHT));
			shiftDown.insert(board.get(LOWER_LEFT), board.get(LOWER_CENTER), board.get(CENTER_RIGHT));
			if (shiftDown.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftDown);
			}

		} else if (blank == LOWER_LEFT) {

			State shiftUp = new State(path + 1, blank, goalState);
			shiftUp.insert(board.get(UPPER_LEFT), board.get(UPPER_CENTER), board.get(UPPER_RIGHT));
			shiftUp.insert(board.get(LOWER_LEFT), board.get(CENTER), board.get(CENTER_RIGHT));
			shiftUp.insert(board.get(CENTER_LEFT), board.get(LOWER_CENTER), board.get(LOWER_RIGHT));
			if (shiftUp.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftUp);
			}

			State shiftRight = new State(path + 1, blank, goalState);
			shiftRight.insert(board.get(UPPER_LEFT), board.get(UPPER_CENTER), board.get(UPPER_RIGHT));
			shiftRight.insert(board.get(CENTER_LEFT), board.get(CENTER_RIGHT), board.get(CENTER));
			shiftRight.insert(board.get(LOWER_CENTER), board.get(LOWER_LEFT), board.get(LOWER_RIGHT));
			if (shiftRight.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftRight);
			}

		} else if (blank == LOWER_CENTER) {

			State shiftUp = new State(path + 1, blank, goalState);
			shiftUp.insert(board.get(UPPER_LEFT), board.get(UPPER_CENTER), board.get(UPPER_RIGHT));
			shiftUp.insert(board.get(CENTER_LEFT), board.get(LOWER_CENTER), board.get(CENTER_RIGHT));
			shiftUp.insert(board.get(LOWER_LEFT), board.get(CENTER), board.get(LOWER_RIGHT));
			if (shiftUp.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftUp);
			}

			State shiftLeft = new State(path + 1, blank, goalState);
			shiftLeft.insert(board.get(UPPER_LEFT), board.get(UPPER_CENTER), board.get(UPPER_RIGHT));
			shiftLeft.insert(board.get(CENTER_LEFT), board.get(CENTER), board.get(CENTER_RIGHT));
			shiftLeft.insert(board.get(LOWER_CENTER), board.get(LOWER_LEFT), board.get(LOWER_RIGHT));
			if (shiftLeft.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftLeft);
			}

			State shiftRight = new State(path + 1, blank, goalState);
			shiftRight.insert(board.get(UPPER_LEFT), board.get(UPPER_CENTER), board.get(UPPER_RIGHT));
			shiftRight.insert(board.get(CENTER_LEFT), board.get(CENTER), board.get(CENTER_RIGHT));
			shiftRight.insert(board.get(LOWER_LEFT), board.get(LOWER_RIGHT), board.get(LOWER_CENTER));
			if (shiftRight.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftRight);
			}

		} else if (blank == LOWER_RIGHT) {

			State shiftUp = new State(path + 1, blank, goalState);
			shiftUp.insert(board.get(UPPER_LEFT), board.get(UPPER_CENTER), board.get(UPPER_RIGHT));
			shiftUp.insert(board.get(CENTER_LEFT), board.get(CENTER), board.get(LOWER_RIGHT));
			shiftUp.insert(board.get(LOWER_LEFT), board.get(LOWER_CENTER), board.get(CENTER_RIGHT));
			if (shiftUp.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftUp);
			}

			State shiftLeft = new State(path + 1, blank, goalState);
			shiftLeft.insert(board.get(UPPER_LEFT), board.get(UPPER_CENTER), board.get(UPPER_RIGHT));
			shiftLeft.insert(board.get(CENTER_LEFT), board.get(CENTER), board.get(CENTER_RIGHT));
			shiftLeft.insert(board.get(LOWER_LEFT), board.get(LOWER_RIGHT), board.get(LOWER_CENTER));
			if (shiftLeft.getBoard().indexOf(0) != parentIndex) {
				children.add(shiftLeft);
			}
		}
		return children;
	}
}
