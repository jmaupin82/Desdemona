import java.util.ArrayList;
import java.util.List;


public abstract class Player {
	/** 
	 * 
	 */
	String name;
	
	public abstract Move makeMove(Board board);
	
	protected ArrayList<Move> getValidMoves(Board board) {
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
