package course.oop.view;

import java.io.*;
import java.util.*;

import course.oop.controller.TTTControllerImpl;
import course.oop.main.TTTDriver;
import course.oop.players.ComputerPlayer;
import course.oop.players.Player;
import course.oop.players.Position;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.effect.MotionBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class MainView {
	private BorderPane root;
	private Scene scene; 
	private Text statusNode;
	private final int windowWidth = 800;
	private final int windowHeight = 600;
	private Map<String, Records> usernameSpace = new HashMap<>(); 
	boolean isPvP = true; 
	Tile[][] array = new Tile[3][3];
	TTTControllerImpl drive = new TTTControllerImpl();
	int curPlayer = 1;
	Timer timer;
	boolean timeLimit;
	Text timeLeft;
	
	public MainView() {
		this.root = new BorderPane();
		this.scene = new Scene(root, windowWidth, windowHeight);
		//		this.scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
		timer = new Timer();
		this.statusNode = new Text("no status");
		statusNode.setFill(Color.YELLOW);
		this.root.setTop(this.buildSetupPane());
		this.root.setRight(getPlayerPane());
	}


	private GridPane selectPlayerPane() {
		GridPane gridPane = new GridPane();    

		gridPane.setMinSize((int)windowWidth/4,  windowHeight); 
		//Setting the padding  
		//		gridPane.setPadding(new Insets(10, 10, 10, 10));      
		//Setting the vertical and horizontal gaps between the columns 
		gridPane.setVgap(5); 
		gridPane.setHgap(5);       
		gridPane.setAlignment(Pos.TOP_CENTER);
		
		
		ChoiceBox<String> player = new ChoiceBox<>();
		player.getItems().addAll(usernameSpace.keySet());
		gridPane.add(new Text("choose first player"), 0, 0);
		gridPane.add(player, 0, 1);
		ChoiceBox<String> player2 = new ChoiceBox<>(); 
		if(isPvP) {
			player2.getItems().addAll(usernameSpace.keySet());
			gridPane.add(new Text("choose second player"), 0, 2);
			gridPane.add(player2, 0, 3);
		}
		
		ChoiceBox<Integer> timerChoice = new ChoiceBox<>(); 
		timerChoice.getItems().addAll(0, 5, 10, 20, 30, 40, 50, 60);
		gridPane.add(new Text("choose timer, 0 for unlimit amount of time"), 0, 4);
		gridPane.add(timerChoice, 0, 5);
		
		Button submit = new Button("confirm");
		submit.addEventHandler(MouseEvent.MOUSE_CLICKED,  new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent e) { 
				
				if(player.getValue() == null || player2.getValue() == null || timerChoice.getValue() == null ) {
					return;
				}
				
				int time = timerChoice.getValue();
				if(time != 0) {
					timeLimit = true;
				}
				if(isPvP) {
					drive.startNewGame(2, time);
				} else {
					drive.startNewGame(1, time); 
				}
				drive.createPlayer(player.getValue(), usernameSpace.get(player.getValue()).mark, 1);
				if(isPvP) {
					drive.createPlayer(player2.getValue(), usernameSpace.get(player2.getValue()).mark, 2);
				}else {
					Player computer = new ComputerPlayer("ぁ", "Computer Player");
					drive.map.put(2, computer);
				}
				root.setCenter(addMainPane());
			}
		});
		
		gridPane.add(submit, 0, 6);
		return gridPane;
	}

	public GridPane addMainPane() {
		GridPane gridPane = new GridPane();
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				array[i][j] = new Tile(i, j);
				array[i][j].setMinSize(100, 100);
				gridPane.add(array[i][j], j, i);
			}
		}
		
		if(drive.secs != 0) {
			timer.schedule(new ExpireTask(this, drive.secs), 1000, 1000);
		}
		
		timeLeft = new Text("time left");
		gridPane.add(timeLeft, 0, 3);
		
		return gridPane;
	}
	
	

	public GridPane buildSetupPane() {
		Text username = new Text("username:");  
		Text marker = new Text("marker: ");       
		TextField usernameField = new TextField();
		//TODO #1: Add a text field for a user to input a default value to init array
		TextField markerField = new TextField();

		Button button1 = new Button("Submit"); 
		button1.getStyleClass().add("orangeButton");

		Line line = new Line();

		line.setStartX(0.0f); 
		line.setStartY(0.0f);         
		line.setEndX((float) windowWidth); 
		line.setEndY(0.0f);

		//Creating the mouse event handler 
		EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent e) { 
				String username = usernameField.getText();
				String marker = markerField.getText();
				usernameSpace.put(username, new Records(0, 0, marker));
				storeInfo();
				updatePlayerPane();
			}
		};  
		//Registering the event filter 
		button1.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);   
		//Creating a Grid Pane 
		GridPane gridPane = new GridPane();    

		//Setting size for the pane 
		gridPane.setMinSize(windowWidth, (int) windowHeight/4); 
		//Setting the padding  
		gridPane.setPadding(new Insets(10, 10, 10, 10));      
		//Setting the vertical and horizontal gaps between the columns 
		gridPane.setVgap(5); 
		gridPane.setHgap(5);       

		//Setting the Grid alignment 
		gridPane.setAlignment(Pos.CENTER); 

		gridPane.add(username, 0, 0); 
		//TODO #3: Remove comment so that the label will show
		gridPane.add(marker, 1, 0); 

		gridPane.add(usernameField, 0, 1); 

		//TODO #4: Add the text field for the default value
		gridPane.add(markerField, 1, 1); 
		gridPane.add(button1, 2, 1); 
		gridPane.add(line, 0, 2, 3, 1); 
		gridPane.add(new Text("select mode"), 3, 1);
		ChoiceBox<String> mode = new ChoiceBox<>();
		mode.getItems().addAll("PvP", "PvC");
		mode.setValue("PvP");
		gridPane.add(mode, 4, 1);
		
		Button submit = new Button("confirm selection");
		
		EventHandler<MouseEvent> submitHandler = new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent e) { 
				String choice = mode.getValue();
				if(choice.compareTo("PvP") == 0) {
					isPvP = true;
				} else {
					isPvP = false;
				}
				root.setLeft(selectPlayerPane());
			}
		};  
		submit.addEventHandler(MouseEvent.MOUSE_CLICKED, submitHandler);
		gridPane.add(submit, 4, 0);
		return gridPane;
	}

	public GridPane getPlayerPane() {
		GridPane gridPane = new GridPane();   
		gridPane.setMinSize((int)windowWidth/4,  windowHeight); 
		//Setting the padding  
		//		gridPane.setPadding(new Insets(10, 10, 10, 10));      
		//Setting the vertical and horizontal gaps between the columns 
		gridPane.setVgap(5); 
		gridPane.setHgap(5);       
		gridPane.setAlignment(Pos.TOP_CENTER);
		gridPane.add(new Text("username"), 0, 0);
		gridPane.add(new Text("mark"), 1, 0);
		gridPane.add(new Text("win"), 2, 0);
		gridPane.add(new Text("loss"), 3, 0);
		//Setting the Grid alignment 
		int i = 1;
		int j = 1;
		if(usernameSpace.size() == 0) {
			getInfo();
		}
		for(String key : usernameSpace.keySet()) {
			gridPane.add(new Text(key), 0, i);
			
			Records s = usernameSpace.get(key);
			String mark = s.mark;
			gridPane.add(new Text(mark), 1, j);
			gridPane.add(new Text(Integer.toString(usernameSpace.get(key).win)), 2, j);
			gridPane.add(new Text(Integer.toString(usernameSpace.get(key).loss)), 3, j);
			i++;
			j++;
		}
		return gridPane;
	}

	private void updatePlayerPane() {
		this.root.setRight(getPlayerPane());
	} 

	private void storeInfo() {
		try {
			FileOutputStream fos = new FileOutputStream("hashmap.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(usernameSpace);
			oos.close();
			fos.close();
			System.out.printf("Serialized HashMap data is saved in hashmap.ser");
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private void getInfo() {
		try {
			FileInputStream fis = new FileInputStream("hashmap.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			usernameSpace = (Map<String, Records>) ois.readObject();
			fis.close();
			ois.close();
			System.out.printf("Serialized HashMap data is retrieved in hashmap.ser");
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Scene getMainScene() {
		return scene;
	}
	
	private void move() {
		curPlayer  = curPlayer%2 + 1;
	}
	
	private void checkWinner() {
		int winner = drive.determineWinner();
		System.out.println();
		System.out.println("this winner is: " + winner);
		if(winner != 0) {
			setBottomPane(winner);
		}
	}
	
	public void setBottomPane(int winner) {
		Stage stage = new Stage();
	
		GridPane gridPane = new GridPane();
		
		Button exit = new Button("exit");
		 
		
		EventHandler<MouseEvent> endHandler = new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent e) {
				stage.close();
				Platform.exit();
		        System.exit(0);
			}
		};  
		//Registering the event filter 
		exit.addEventHandler(MouseEvent.MOUSE_CLICKED, endHandler);  
		GridPane.setConstraints(exit, 2, 0);
		String username;
		String loser;
		if(winner != 3) {
			username = drive.map.get(drive.determineWinner()).username;
			loser = drive.map.get(drive.determineWinner() % 2 + 1).username;
			usernameSpace.get(username).win++; 
			usernameSpace.get(loser).loss++; 
			storeInfo();
			gridPane.getChildren().addAll(new Text("Game Over, the winner is: " + drive.map.get(drive.determineWinner()).username ), exit);
		} else {
			gridPane.getChildren().addAll(new Text("draw no one win"), exit);
		}
		String musicFile = "win.mp3"; 
		Media sound = new Media(new File(musicFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		gridPane.setMinSize(200, 200);
		stage.setScene(new Scene(gridPane));
		stage.sizeToScene();
		stage.show();
	}
	
	
	private void computerMove() {
		Player computer = drive.map.get(curPlayer);
		Position p = computer.nextMove(drive.array);
		boolean res = drive.setSelection(p.x, p.y, curPlayer);
		array[p.x][p.y].text.setText(drive.map.get(curPlayer).mark);
		move();
	}
	
	public void timeout() {
		drive.winner = curPlayer  = curPlayer%2 + 1;
		timer.cancel();
	}

	public MainView getMainView() {
		return this;
	}
	
	private class Tile extends StackPane{
		Text text = new Text();
		int x;
		int y;
		
		public Tile(int x, int y) {
			Rectangle border = new Rectangle(100,100);
			border.setFill(null);
			border.setStroke(Color.BLACK);
			text.setFont(Font.font(32));
			setAlignment(Pos.CENTER);
			getChildren().addAll(border, text);
			
			setOnMouseClicked(event -> {
				boolean res = drive.setSelection(x, y, curPlayer);
				if(res) {
					if(timer != null && drive.secs != 0 ) {
						timer.cancel();
						timer = new Timer();
						timer.schedule(new ExpireTask(getMainView(), drive.secs), 1000, 1000);
					}
					
					text.setText(drive.map.get(curPlayer).mark);
					checkWinner();
					move();
					if(!isPvP) {
						computerMove();
					}
				}
				
			});
		}
		public void set(String s) {
			text.setText(s);
		}
	}
	
}

class Records implements Serializable{
	int win = 0;
	int loss = 0;
	String mark = "X";
	
	public Records(int x, int y, String z) {
		win = x;
		loss = y;
		mark = z;
	}
}

class ExpireTask extends TimerTask{
	MainView callbackClass;
	int x = 10;

	public ExpireTask(MainView callbackClass, int time){
		this.callbackClass = callbackClass;
		x = time;
	}


	public void run(){
		x--;
		callbackClass.timeLeft.setText("time left: " + Integer.valueOf(x));;
		
		if(x == 0) {
			callbackClass.timeout();
		}
	}
}