package proj4;
import java.util.Random;

/**
 * This class models a tic tac toe player that chooses random moves.
 * 
 * @version 05/10/12
 * @author Rostislav Tsiomenko <tsiomr1@umbc.edu>
 * Project: CMSC 341 - Spring 2012 - Project 4
 * Section: 01
 */
public class RandomPlayer {

	Random moveGen;

	/**
	 * Default constructor for RandomPlayer.
	 */
	public RandomPlayer() {
		moveGen = new Random();
	}
	

	/**
	 * Private method for determining random move.
	 * @param input Board from which to decide move
	 * @return new Board with move added
	 */
	private Board randomMove(Board input){
		
		// Choose a random blank space and put an X in it
		Board output = new Board(input);
		boolean filled = false;
		while (!filled) {
			int random = moveGen.nextInt(9);
			if (output.getBoard().get(random) == 0) {
				output.getBoard().set(random, 1);
				filled = true;
			}
		}
		
		return output;
		
	}
	
	/**
	 * Returns a Board object which is this Player's move given an input Board.
	 * @param input Board from which to make a move
	 * @return new Board with RandomPlayer move added
	 */
	public Board move(Board input) {
		
		return randomMove(input);
		
	}
	
}
	
