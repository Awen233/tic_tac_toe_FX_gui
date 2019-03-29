package course.oop.main;

import course.oop.controller.*;
import course.oop.players.ComputerPlayer;
import course.oop.players.Player;
import course.oop.players.Position;

import java.util.*;

public class TTTDriver {

	TTTControllerImpl drive;
	int curPlayer;
	TTTDriver cur;
	Timer timer;

	public static void main(String[] args) {

		TTTDriver cur = new TTTDriver();
		cur.gameStart(cur);
	}

	private void gameStart(TTTDriver cur) {
		drive = new TTTControllerImpl();
		this.cur = cur;
		System.out.println("Welcome to game");
		Scanner sr = new Scanner(System.in);
		while(true) {
			System.out.println("select choice: start  quit");
			timer = new Timer();
			String choice = sr.next();
			if(choice.compareTo("quit") == 0) { 
				break;
			} else if(choice.compareTo("start") != 0) {
				System.out.println("wrong input, please enter again");
				continue;
			}
			System.out.println("enter time in secs for each player's turn, enter 0 for default 20 seconds");
			int secs = sr.nextInt();
			if(secs == 0) secs = 20;

			System.out.println("select game mode, 1 for player vs player, 2 for player vs computer");
			while(true) {
				choice = sr.next();
				if(choice.compareTo("1") == 0) {
					drive.startNewGame(2, secs);
					cur.pvp(false);
					break;
				} else if(choice.compareTo("2") == 0) {
					drive.startNewGame(1, secs);
					cur.pvp(true);
					break;
				} else {
					System.out.println("wrong input, enter again");
				}
			}	
		}
		System.out.println("game end, thanks for playing this game");
	}

	private void pvp(boolean computer) {
		Scanner sr  = new Scanner(System.in);

		System.out.println("enter player information: ");
		System.out.print("player1 name: ");
		String name1 = sr.next();
		System.out.print("player1 maker: ");
		String marker1 = sr.next();
		drive.createPlayer(name1, marker1, 1);

		if(computer) {
			String marker = "X";
			if(marker.compareTo(marker1) == 0) {
				marker = "Y";
			}
			drive.map.put(2, new ComputerPlayer(marker, "computer player"));
		} else {
			System.out.print("player2 name: ");
			String name2 = sr.next();
			System.out.print("player2 maker: ");
			String marker2 = sr.next();
			drive.createPlayer(name2, marker2, 2);
		}
		curPlayer = 1;
		while(drive.determineWinner() == 0) {
			timer = new Timer();

			Player p = drive.map.get(curPlayer);
			timer.schedule(new ExpireTask(this, drive.secs), 1000, 1000);
			Position pos = p.nextMove(drive.array);
			timer.cancel();
			drive.setSelection(pos.x, pos.y, curPlayer);
			if(curPlayer == 1) {
				curPlayer++;
			} else {
				curPlayer--;
			}
		}
		System.out.println(drive.getGameDisplay());
		int res = drive.determineWinner();
		if(res == 3) {
			System.out.println("draw");
			return;
		}
		System.out.println("winner is: " + drive.map.get(res).username);
	}

	public void timeout() {
		System.out.println("time out " + drive.map.get(curPlayer).username + " lose");
		System.out.println("game end, thanks for playing this game");
		timer.cancel();
		System.exit(11);
	}	
}


class ExpireTask extends TimerTask{
	TTTDriver callbackClass;
	int x = 10;

	ExpireTask(TTTDriver callbackClass, int time){
		this.callbackClass = callbackClass;
		x = time;
	}

	public void run()
	{
		x--;
		if(x % 5 == 0) {
			System.out.println();
			System.out.println( x + " secs left");
		}
		
		if(x == 0) {
			callbackClass.timeout();
		}

	}
}
