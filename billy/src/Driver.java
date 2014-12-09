import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;


/**
 * Class Name: Driver
 * 
 * This class is in charge of coordinating the overall execution of the game.
 * @author eleal
 *
 */
public class Driver {
	private Player playerWhite;
	private Player playerBlack;
	private LogicBoard logicBoard;
	
	/** This implements the GUI */
	private Billy billy;
	private BoardGUI guiBoard;
	
	/** The number of moves that have been made  */
	private int numberOfMoves;
	
	/** True iff the GUI is enabled */
	private boolean enabledGUI;

	public Driver(Player playerWhite, Player playerBlack) {
		this.playerWhite = playerWhite;
		this.playerBlack = playerBlack;		
		this.logicBoard  = new LogicBoard();
		
		// No moves have been made
		this.numberOfMoves = 0;
		
		// The GUI was not enabled.
		this.enabledGUI = false;
	}
	
	public Driver(Player playerWhite, Player playerBlack, boolean enableGUI) {
		this.playerWhite = playerWhite;
		this.playerBlack = playerBlack;

		// Set the logic board.
		this.logicBoard = new LogicBoard();
		
		if(enableGUI) {
			// Set the GUI with billy
			this.billy      = new Billy();
			this.guiBoard   = billy.getBoard();
			this.billy.go(new String[0]);		
			// The GUI was not enabled.
			this.enabledGUI = true;
		}
		
		// No moves have been made
		this.numberOfMoves = 0;
	}

	/**
	 * This function starts and drives a game and returns the disk
	 * differential.
	 */
	public int startGame() {
		Player currentPlayer = playerBlack;		
		Player otherPlayer   = playerWhite;

		//System.out.println(logicBoard);
		
		while(!logicBoard.EndOfGame()) {
//			try {
//				Thread.sleep(200);
//			}
//			catch(Exception e) {}
			int currentColor  = currentPlayer.getColor();
			
			
			// Ask the white player to provide a move.
			Move move = currentPlayer.makeMove(logicBoard);
			//System.out.println(logicBoard);
			//System.out.println("im " + currentColor + " i play " + move + " num moves " + numberOfMoves );
			
			if(move == null) {
				return logicBoard.getDiskDifferential();
			}
			
			// Modify board with the white player's move.
			
			logicBoard.play(currentColor,move.getX(),move.getY());
			
			if(this.enabledGUI) {
				guiBoard = updateBoard(guiBoard, move);
			}
			
			//System.out.println(logicBoard);
			// Give feedback to the player
			//TO DO: UPDATE THE BOARD
			currentPlayer.postMoveProcessing(logicBoard, logicBoard, currentPlayer);
			otherPlayer.postMoveProcessing(logicBoard, logicBoard, currentPlayer);
			
			// Switch the players.
			currentPlayer = switchPlayer(currentPlayer);
			otherPlayer   = switchPlayer(otherPlayer);
			
			// Increment the number of moves
			numberOfMoves++;		
		}	
		
		// In this function we tell the players who won, and they can do
		// whatever else they need to do after the game ended.
		//postGameProcessing();
		
		// The final number of white stones minus that of black stones.
		int diskDifferential = logicBoard.getDiskDifferential();
		
		// Shutdown billy
		//billy.dispose();
		
		return diskDifferential;
	}

	private BoardGUI updateBoard(BoardGUI oldBoard, Move move) {
		if(move == null) {System.err.println("move is null");}
		billy.makeMove(oldBoard.getSquare(move.getX(), move.getY()));
		BoardGUI newBoard = billy.getBoard();
		//System.out.println("billys: " + newBoard);
		return newBoard;
	}

	private void postGameProcessing() {
		int numberWhiteStones = logicBoard.count(Constants.WHITE);
		int numberBlackStones = logicBoard.count(Constants.BLACK);
		int diskDifferential = numberWhiteStones - numberBlackStones;
		int winLose = 0;

		try {
			File file = new File("/home/eleal/Desktop/avgNumMoves2.txt");
			if(diskDifferential >0) {
				winLose = 1;
			}
			else if(diskDifferential <0) {
				winLose = -1;
			}
			else {
				winLose = 0;
			}
			String content = winLose + "," +diskDifferential + "\n";
			//String content = numberOfMoves + "\n";
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			//billy.dispose();
		}
		catch(Exception e){e.printStackTrace();}

	}

	private Player switchPlayer(Player currentPlayer){
		if(currentPlayer.equals(playerWhite)){
			return playerBlack;
		}
		else {
			return playerWhite;
		}
	}

}
