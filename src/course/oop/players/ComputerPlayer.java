package course.oop.players;

import java.util.*;

public class ComputerPlayer extends Player{

	public ComputerPlayer(String mark, String username) {
		super(mark, username);
	}

	@Override
	public Position nextMove(String[][] array) {
		Position p  = new Position();
		List<Position> list = new ArrayList<>();
		for(int i = 0; i < array.length; i++) {
			for(int j = 0; j < array[0].length; j++) {
				if(array[i][j] == null) {
					list.add(new Position(i,j)); 
				}
			}
		}
		Random r = new Random();
		int choice = r.nextInt(list.size());
		return list.get(choice);	
	}

}
