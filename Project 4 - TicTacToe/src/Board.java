package proj4;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class models a tic tac toe game board.
 * 
 * @version 05/10/12
 * @author Rostislav Tsiomenko <tsiomr1@umbc.edu>
 * Project: CMSC 341 - Spring 2012 - Project 4
 * Section: 01
 */
public class Board {

	private ArrayList<Integer> board;
	private Random moveGen;
	private double wins;
	private double losses;
	private double draws;
	private final int BOARD_SIZE = 9;
	private final int X = 1;
	private final int O = 2;
	private final int BLANK = 0;

	/**
	 * Constructor for a Board, creates a blank Board.
	 */
	public Board () {

		board = new ArrayList<Integer>();
		moveGen = new Random();
		
		// Fill with blanks
		for (int i = 0; i < BOARD_SIZE; i++) {
			board.add(BLANK);
		}

		wins = 0;
		losses = 0;
		draws = 0;

	}
	
	/**
	 * Copy constructor for a Board, copies a Board object's internal
	 * ArrayList representing the actual game board, but resets wins, losses, 
	 * and draws, creating a new Random object as well.
	 * @param original Board object to copy
	 */
	public Board(Board original) {
		// use ArrayList copy constructor
		board = new ArrayList<Integer>(original.getBoard());
		moveGen = new Random();
		wins = 0;
		losses = 0;
		draws = 0;
	}

	/**
	 * Returns an ArrayList of all the possible moves O can go
	 * in this Board.
	 * @return ArrayList of "children" boards (possible moves)
	 */
	public ArrayList<Board> generateAllChildren(){

		ArrayList<Integer> blanks = new ArrayList<Integer>();
		ArrayList<Board> children = new ArrayList<Board>();

		// Get indices of blanks
		for (int i = 0; i < BOARD_SIZE; i++) {
			if (board.get(i) == BLANK) {
				blanks.add(i);
			}
		}
		
		// Generate boards with blanks filled
		for (int i = 0; i < blanks.size(); i++) {
			Board child = new Board(this);
			child.setO(blanks.get(i));
			children.add(child);
		}
		
		return children;
	}

	/**
	 * Returns internal ArrayList of Xs, Os, and blanks.
	 * @return internal ArrayList
	 */
	public ArrayList<Integer> getBoard() {
		return board;
	}

	/**
	 * Sets the given index in this Board to an X.
	 * @param index position to set X
	 */
	public void setX(int index){
		board.set(index, X);
	}

	/**
	 * Sets the given index in this Board to an O.
	 * @param index position to set O
	 */
	public void setO(int index){
		board.set(index, O);
	}

	/**
	 * Marks that this board has been responsible for a win.
	 */
	public void addWin() {
		wins++;
	}

	/**
	 * Marks that this board has been responsible for a loss.
	 */
	public void addLoss() {
		
		wins-=2;
	}

	/**
	 * Returns a number representing how probable this Board is to cause a win.
	 * @return win ratio
	 */
	public Double getWinRatio() {
		
		return wins;

	}

	/**
	 * Checks if this Board is a win, loss, draw, or still in progress (more moves available).
	 * @return 1 if X has on, 2 if O has won, 3 if tie, 0 if more moves are possible.
	 */
	public int isWinner() {

		// Check X rows
		if (board.get(0) == X && board.get(1) == X && board.get(2) == X) {
			return X;
		}
		if (board.get(3) == X && board.get(4) == X && board.get(5) == X) {
			return X;
		}
		if (board.get(6) == X && board.get(7) == X && board.get(8) == X) {
			return X;
		}
		// X columns
		if (board.get(0) == X && board.get(3) == X && board.get(6) == X) {
			return X;
		}
		if (board.get(1) == X && board.get(4) == X && board.get(7) == X) {
			return X;
		}
		if (board.get(2) == X && board.get(5) == X && board.get(8) == X) {
			return 1;
		}
		// X diagonals
		if (board.get(0) == X && board.get(4) == X && board.get(8) == X) {
			return X;
		}
		if (board.get(2) == X && board.get(4) == X && board.get(6) == X) {
			return 1;
		}


		// Check O rows
		if (board.get(0) == O && board.get(1) == O && board.get(2) == O) {
			return O;
		}
		if (board.get(3) == O && board.get(4) == O && board.get(5) == O) {
			return O;
		}
		if (board.get(6) == O && board.get(7) == O && board.get(8) == O) {
			return O;
		}
		// O columns
		if (board.get(0) == O && board.get(3) == O && board.get(6) == O) {
			return O;
		}
		if (board.get(1) == O && board.get(4) == O && board.get(7) == O) {
			return O;
		}
		if (board.get(2) == O && board.get(5) == O && board.get(8) == O) {
			return O;
		}
		// O diagonals
		if (board.get(0) == O && board.get(4) == O && board.get(8) == O) {
			return O;
		}
		if (board.get(2) == O && board.get(4) == O && board.get(6) == O) {
			return O;
		}


		if (board.indexOf(BLANK) == -1) {
			// Draw
			return 3; 
		} 
		
		// Not yet full
		return 0; 


	}

	/**
	 * Equals method compares game boards ONLY. Does not compare
	 * wins/losses/anything else. 
	 */
	public boolean equals(Object o){
		Board j = (Board) o;
		// Use ArrayList equals method
		return (board.equals(j.getBoard()));
	}

	/**
	 * Hashcode function for internal game board (does not take wins/losses into account).
	 */
	public int hashCode() {
		
		// Create String and use String hashCode
		StringBuilder string = new StringBuilder();
		for (int i = 0; i < 9; i++){
			int current = board.get(i);
			if (current == 1) {
				string.append("X");
			} else if (current == 2) {
				string.append("O");
			} else {
				string.append(" ");
			}
		}
		return string.toString().hashCode();
	}

	/**
	 * Returns a String representation of this object.
	 */
	public String toString() {

		StringBuilder string = new StringBuilder();
		for (int i = 0; i < BOARD_SIZE; i+=3) {
			if (board.get(i) == X) {
				string.append("X |");
			} else if (board.get(i) == O) {
				string.append("O |");
			} else {
				string.append("  |");
			}

			if (board.get(i+1) == X) {
				string.append(" X ");
			} else if (board.get(i+1) == O) {
				string.append(" O ");
			} else {
				string.append("   ");
			}

			if (board.get(i+2) == X) {
				string.append("| X\n");
			} else if (board.get(i+2) == O) {
				string.append("| O\n");
			} else {
				string.append("|  \n");
			}

			if (i != 6) {
				string.append("---------\n");
			}
		}
		return new String (string);
	}
}
