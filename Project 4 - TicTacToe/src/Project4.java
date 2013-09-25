package proj4;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Driver class for Project 4.
 * 
 * @version 05/10/12
 * @author Rostislav Tsiomenko <tsiomr1@umbc.edu>
 * Project: CMSC 341 - Spring 2012 - Project 4
 * Section: 01
 */
public class Project4 {

	public static void main (String args[]) {

		boolean history = false;
		double games = 1;
		
		// Don't support more than 2 arguments
		if (args.length > 2)
		{
			System.err.println("Usage: java Project4 <-h> <-# of games>");
			System.exit(-1);
		}

		// Check and assign command variables
		for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-h")) {
            	history = true;
            	System.out.println("Console output enabled.");
            } else {
            	
            	// NOTE TO TA - THIS WILL NOT WORK UNDER ANT
            	// As it will expect -Dargs="" to actually make args.length equal to 0
            	// Otherwise it will, for some reason, assume that there are some args, and not run
            	// without any command line arguments. Don't ask me, Ant sucks.
            	
            	 try {
            	        games = Integer.parseInt(args[i].substring(1));
            	        System.out.printf("Playing %.0f game(s)..\n", games);
            	    } catch (NumberFormatException e) {
            	        System.err.println("Number of games must be an integer");
            	        System.exit(1);
            	    }
            }
    	}
		
		
			
		
		double wins = 0;
		double losses = 0;
		double draws = 0;

		// Players are outside the "games" loop
		RandomPlayer p1_random = new RandomPlayer();
		SmartPlayer p2_smart = new SmartPlayer();
		
		for (int i = 0; i < games; i++) {

			// Create new board every game
			Board currentBoard = new Board();
			boolean finished = false;

			while (!finished) {
				
				// Random player moves first
				if (currentBoard.isWinner() == 0) {
					currentBoard = p1_random.move(currentBoard);
					if (history) {
						System.out.println("Random played moved: ");
						System.out.println(currentBoard);
					}
				}

				// Check if someone has won
				if (currentBoard.isWinner() != 0) {
					
					int win = currentBoard.isWinner();
					
					// Increment appropriate variables
					if (win == 1) {
						if (history) {
							System.out.println("X (random player) has won.");
						}
						losses++;
					} else if (win == 2) {
						if (history) {
							System.out.println("O (smart player) has won.");
						}
						wins++;
						
					} else {
						if (history) {
							System.out.println("The game is a draw.");
						}
						draws++;
					}

					finished = true;
					
					// And update the smart player dictionary
					p2_smart.updateDictionary(win);

				} else {
					
					// If no one has won yet, the smart player moves
					currentBoard = p2_smart.move(currentBoard);
					if (history) {
						System.out.println("Smart played moved: ");
						System.out.println(currentBoard);
					}
				}
			}
		}
		
		// Print out endgame summary
		double percent = (wins / games) * 100;
		System.out.printf("\nGames played: %.0f\n", games);
		System.out.printf("Games won by X (random): %.0f\n", losses);
		System.out.printf("Games won by O (smart): %.0f\n", wins);
		System.out.printf("Games that tied: %.0f\n", draws);
		System.out.printf("Win percentage was %.2f%%\n", percent);
		System.out.printf("Number of slots is %d\n", p2_smart.dictionary.getSlots());
		System.out.printf("Number of entries is %d\n", p2_smart.dictionary.getEntries());
		System.out.printf("Load factor is %.2f\n", ((double)p2_smart.dictionary.getEntries()/(double)p2_smart.dictionary.getSlots()) * 100);
	}
}

