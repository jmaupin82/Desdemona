import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Class Name: Driver
 * 
 * This class is in charge of coordinating the overall structure of the game.
 * @author eleal
 *
 */
public class Driver {
	Player playerWhite;
	Player playerBlack;
	Billy billy;
	Board board;
	int numberOfMoves;
	
	public Driver(Player playerWhite, Player playerBlack) {
		this.playerWhite = playerWhite;
		this.playerBlack = playerBlack;
		this.billy       = new Billy();
		
		billy.go(new String[0]);
		this.board       = billy.getBoard();
		this.numberOfMoves = 0;
		// give the players the board and billy objects
		this.playerBlack.setBilly(this.billy);
		this.playerBlack.setColor(Constants.BLACK);
		this.playerWhite.setBilly(this.billy);
		this.playerWhite.setColor(Constants.WHITE);
	}
	
	/**
	 * This function starts and drives a game.
	 */
	public void startGame() {
		Player currentPlayer = playerBlack;
		
		while(notEndOfGame(board, currentPlayer)) {
			// Ask the white player to provide a move.
			Move currentMove = currentPlayer.makeMove(board);
			
			// Modify board with the white player's move.			
			billy.makeMove(currentMove.getSquare());
			board = billy.getBoard();
			
			// Swith the players.
			currentPlayer = switchPlayer(currentPlayer);
			
			numberOfMoves++;
		}	
		
		postGameProcessing();
	}
	
	private boolean notEndOfGame(Board board, Player currentPlayer) {
		// 1. The game ends either when the number of white stones plus the 
		// number of black stones equals the number of cells in the board.
		/*int numberWhiteStones = board.count(Constants.WHITE);
		int numberBlackStones = board.count(Constants.BLACK);
		int totalStones = numberWhiteStones + numberBlackStones;
		
		boolean noEmptyCells = totalStones == Constants.SIZE * Constants.SIZE;
		
		// 2. Or if one of the players ends up without stones.
		boolean runOutOfStones = (numberWhiteStones == 0) || (numberBlackStones==0);
		
		return !noEmptyCells && ! runOutOfStones; */
		ArrayList<Move> validMoves = currentPlayer.getValidMoves(board);
		
		return !validMoves.isEmpty();
	}
	
	private void postGameProcessing() {
		int numberWhiteStones = board.count(Constants.WHITE);
		int numberBlackStones = board.count(Constants.BLACK);
		int diskDifferential = numberWhiteStones - numberBlackStones;
		int winLose = 0;
		
		try {
		File file = new File("avgNumMoves.txt");
		if(diskDifferential >0) {
			winLose = 1;
		}
		else if(diskDifferential <0) {
			winLose = -1;
		}
		else {
			winLose = 0;
		}
		//String content = winLose + "," +diskDifferential + "\n";
		String content = numberOfMoves + "\n";
		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file, true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		billy.dispose();
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
