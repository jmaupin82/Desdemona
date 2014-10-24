
import java.util.ArrayList;


public abstract class Player {
	/** 
	 * 
	 */
	protected String name;
	
	protected int color;
	
	public Player() {
		this.name = "Default Player";
		this.color = 0;
	}
	public Player(String name, int color) {
		this.name  = name;
		this.color = color;
	}
	
	public String getName() {
		return name;
	}

	public int getColor() {
		return color;
	}

	public abstract Move makeMove(Board board);
	
	public abstract void postMoveProcessing(Board oldBoard, Board newBoard, Player whoPlayed);
	
	public abstract void endOfGame(int diskDifferential);
	
	protected static ArrayList<Move> getValidMoves(Board board) {
		ArrayList<Move> validMoves = new ArrayList<Move>();
		
		// Iterate over all the squares in the board, and check if they
		// are valid movements. If they are, save the valid ones in a list.
		for(int x = 0; x < Constants.SIZE; x++) {
			for(int y = 0; y < Constants.SIZE; y++){
				Square square = board.getSquare(x, y);
				
				// Right now this is a synonym of being a valid move.
				// Maybe we need to change the name of this.
				if(square.isClickable()) {
					validMoves.add(new Move(square));
				}
			}
		}
		return validMoves;
	}
}
