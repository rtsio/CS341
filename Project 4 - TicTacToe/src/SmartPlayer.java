package proj4;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * This class models a "smart" tic tac toe player that learns.
 * 
 * @version 05/10/12
 * @author Rostislav Tsiomenko <tsiomr1@umbc.edu>
 * Project: CMSC 341 - Spring 2012 - Project 4
 * Section: 01
 */
public class SmartPlayer {

	public ArrayList<Board> movesUsed;
	public SeparateChainingHashTable<Board> dictionary;

	/**
	 * Default constructor for SmartPlayer.
	 */
	public SmartPlayer() {
		dictionary = new SeparateChainingHashTable<Board>();
		movesUsed = new ArrayList<Board>();
	}

	/**
	 * Method updates internal dictionary of moves depending on passed in variable.
	 * @param win must be 1 if SmartPlayer has lost, 2 if SmartPlayer has won.
	 */
	public void updateDictionary(int win) {

		Iterator<Board> itr = movesUsed.iterator();
		int count = 1;
		
		// Iterate through all the moves used
		while (itr.hasNext()) {

			Board board = itr.next();
			
			// If the move already exists, we simply get a reference to it, and update it's loss/win
			if (dictionary.contains(board)) {

				Board temp = dictionary.get(board);

				if (win == 2) {
					for (int j = 0; j < count; j++) {
						temp.addWin();
					}

				} else if (win == 1){
					for (int j = 0; j < count; j++) {
						temp.addLoss();
					}
				}  

			} else {
				
				// If the move doesn't exist, we update win/loss and insert it into dictionary
				if (win == 2) {
					for (int j = 0; j < count; j++) {
						board.addWin();
					}

				} else if (win == 1){
					for (int j = 0; j < count; j++) {
						board.addLoss();
					}
				}  

				dictionary.insert(board);
			}

			// Count is used to give a curve to win/loss ratios; games closer to the beginning of the round
			// are weighed less heavily than those close to the end of the round
			count++;
		}
		
		// Clear moves for next game
		movesUsed.clear();
	}


	/**
	 * Returns a Board object which is this Player's move given an input Board.
	 * @param input Board from which to make a move
	 * @return new Board with SmartPlayer move added
	 */
	public Board move(Board input) {

		// Generate all possibles moves from our input
		ArrayList<Board> children = input.generateAllChildren();
		Iterator<Board> itr = children.iterator();
		
		Board bestChoice = new Board();
		double bestRatio = 0;
		
		while (itr.hasNext()){

			Board p = itr.next();
			
			// If the move is present in the dictionary, we check if it's the best move currently possible
			if (dictionary.contains(p)) {
				Board d = dictionary.get(p);
				if (d.getWinRatio() > bestRatio) {
					bestChoice = d;
					bestRatio = d.getWinRatio();
				}
			}
		}
		
		Random moveGen = new Random();
		
		// If we didn't find a good move, or none of the moves exist in the dictionary, try a random move
		if (bestRatio == 0) {
			movesUsed.add(children.get(moveGen.nextInt(children.size())));
			return movesUsed.get(movesUsed.size()-1);
		} else {
			// Otherwise, try the max move we found earlier
			movesUsed.add(bestChoice);
			return movesUsed.get(movesUsed.size()-1);
		}
	}
}
