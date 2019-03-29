package course.oop.players;

import java.util.Scanner;

public class HumanPlayer extends Player {

	public HumanPlayer(String mark, String username) {
		super(mark, username);
	}

	@Override
	public Position nextMove(String[][] array) {
		// TODO Auto-generated method stub
		Scanner sr = new Scanner(System.in);
		Position p = new Position();
		do {
			System.out.println("please enter the x , y location you want to choose");
			p.x = sr.nextInt();
			p.y = sr.nextInt();
			if(p.x >= array.length || p.y >= array[0].length || array[p.x][p.y] != null) {
				System.out.println("illegal input, please enter again");
			}
		} while(p.x >= array.length || p.y >= array[0].length || array[p.x][p.y] != null);
		
		return p;
	}
	
}
