import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;


/**
 * Class Name: Driver
 * 
 * This class is in charge of coordinating the overall execution of the game.
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
		this.board         = billy.getBoard();
		this.numberOfMoves = 0;
	}

	/**
	 * This function starts and drives a game and returns the disk
	 * differential.
	 */
	public int startGame() {
		Player currentPlayer = playerBlack;		
		Player otherPlayer   = playerWhite;

		while(board.notEndOfGame()) {
			try {
				//Thread.sleep(5000);
			}
			catch(Exception e) {}
			
			// Ask the white player to provide a move.
			Move move = currentPlayer.makeMove(board);

			// Modify board with the white player's move.			
			board = updateBoard(board, move);

			// Give feedback to the player
			//TO DO: UPDATE THE BOARD
			currentPlayer.postMoveProcessing(board, board, currentPlayer);
			otherPlayer.postMoveProcessing(board, board, currentPlayer);
			
			// Switch the players.
			currentPlayer = switchPlayer(currentPlayer);
			otherPlayer   = switchPlayer(otherPlayer);
			
			// Increment the number of moves
			numberOfMoves++;		
		}	
		
		// In this function we tell the players who won, and they can do
		// whatever else they need to do after the game ended.
		postGameProcessing();
		
		return board.getDiskDifferential();
	}



	private Board updateBoard(Board oldBoard, Move move) {
		billy.makeMove(move.getSquare());
		Board newBoard = billy.getBoard();

		return newBoard;
	}

	private void postGameProcessing() {
		int numberWhiteStones = board.count(Constants.WHITE);
		int numberBlackStones = board.count(Constants.BLACK);
		int diskDifferential = numberWhiteStones - numberBlackStones;
		int winLose = 0;

		try {
			File file = new File("avgNumMoves2.txt");
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
