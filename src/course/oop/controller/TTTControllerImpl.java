package course.oop.controller;

import java.util.*;

import course.oop.players.ComputerPlayer;
import course.oop.players.HumanPlayer;
import course.oop.players.Player;

public class TTTControllerImpl implements TTTControllerInterface{

	public String[][] array;
	public Map<Integer, Player> map = new HashMap<>();
	public int secs;
	int size;
	public int winner;
	
	@Override
	public void startNewGame(int numPlayers, int timeoutInSecs) {
		// TODO Auto-generated method stub
		array = new String[3][3];
		secs = timeoutInSecs;
		size = 0;
		winner = 0;
	}

	@Override
	public void createPlayer(String username, String marker, int playerNum) {
		// TODO Auto-generated method stub
		if(playerNum != 2 && playerNum != 1) {
			return;
		}
		map.put(playerNum, new HumanPlayer(marker, username));
	}

	@Override
	public boolean setSelection(int row, int col, int currentPlayer) {
		if(row >= array.length || col >= array.length) return false; 
		if(currentPlayer != 1 && currentPlayer != 2) return false;
		
		
		if(array[row][col] != null ) {
			return false;
		} 
		if(!map.containsKey(currentPlayer)) {
			if(currentPlayer == 1) {
				map.put(currentPlayer, new ComputerPlayer("X", "player1"));
			} else {
				map.put(currentPlayer, new ComputerPlayer("O", "player2"));
			}
		}
		
		array[row][col] = map.get(currentPlayer).mark;
		size++;

		boolean res = check(row, col, array);
		if(res) winner = currentPlayer;
		
		if(size == 9 && winner == 0) winner = 3;
		return true;
	}

	@Override
	public int determineWinner() {
		return winner;
	}

	@Override
	public String getGameDisplay() {
		StringBuilder sb = new StringBuilder();
		sb.append("+---+---+---+");
		sb.append("\n");
		for(int i = 0; i < array.length; i++) {
			for(int j = 0; j < array[0].length; j++) {
				sb.append("| ");
				if(array[i][j] != null) {
					sb.append(array[i][j]);
				} else {
					sb.append(" ");
				}
				sb.append(" ");
			}
			sb.append("|");
			sb.append("\n");
			sb.append("+---+---+---+");
			sb.append("\n");
		}
		return sb.toString();
	}
	
	private boolean check(int curRow, int curCol, String[][] array) {
		String cur = array[curRow][curCol];
		boolean res = true;
		for(int i = 0; i < array.length; i++) {
			if(array[i][curCol] != cur) {
				res = false;
			}
		}
		if(res) return res;
		res = true;
		for(int i = 0; i < array[0].length; i++) {
			if(array[curRow][i] != cur ) {
				res = false;
			}
		}
		if(res) return res;
		res = true;
		for(int i = 0; i < array.length; i++) {
			if(array[i][i] != cur) res = false;
		}
		if(res) return res;
		res = true;
		for(int i = array.length - 1; i >= 0; i--) {
			if(array[array.length - 1 - i][i] != cur) res = false;
		}
		return res;
	}

}
