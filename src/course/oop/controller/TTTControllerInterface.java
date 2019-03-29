/**
 * You can and should have other members (properties or functions) 
 * in your controller, but the functions provided in 
 * TTTControllerInterface.java are required.
 * 
 * Create a class called TTTControllerImpl.java and implement the 
 * methods in this interface.
 * 
 */

package course.oop.controller;

public interface TTTControllerInterface {
	
	/**
	 * Initialize or reset game board. Set each entry back to a default value.
	 * 
	 * @param numPlayers Must be valid. 2 = two human players, 1 = human plays against computer
	 * @param timeoutInSecs Allow for a user's turn to time out. Any
	 * 						int <=0 means no timeout.  Any int > 0 means to time out
	 * 						in the given number of seconds.
	 */
	void startNewGame(int numPlayers, int timeoutInSecs);

	/**
	 * Create a player with specified username, marker, 
	 * and player number (either 1 or 2) 
	 * 
	 * @param username
	 * @param marker
	 * @param playerNum
	 */
	void createPlayer(String username, String marker, int playerNum);

	/**
	 * Allow user to specify location for marker.  
	 * Return true if the location is valid and available.
	 * 
	 * @param row Must be valid. 0,1,2
	 * @param col Must be valid. 0,1,2
	 * @param currentPlayer Must be valid. 1 = player1; 2 = player2
	 * @return
	 */
	boolean setSelection(int row, int col, int currentPlayer);
	
	/**
	 * Determines if there is a winner and returns the following:
	 * 
	 * 0=no winner / game in progress / not all spaces have been selected; 
	 * 1=player1; 
	 * 2=player2; 
	 * 3=tie/no more available locations
	 * 
	 * @return
	 */
	int determineWinner();

	/**
	 * Return a printable display of current selections.
	 * Shows 3x3 (or nxn) board with each players marker.
	 * 
	 * @return
	 */
	String getGameDisplay();
}
