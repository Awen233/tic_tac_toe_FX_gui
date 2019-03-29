package course.oop.players;

public abstract class Player {
	public String mark;
	public String username;
	public int curRow;
	public int curCol;
	
	public Player(String mark, String name) {
		this.mark  =mark;
		this.username = name;
	}
	
	public abstract Position nextMove(String[][] array);
	
}
