

import java.util.ArrayList;
import java.util.Random;


/**
 * This class implements a random Othello player that simply
 * samples, usign a uniform distribution, the valid moves.
 * 
 * @author eleal
 *
 */
public class RandomPlayer extends Player {

	
	public RandomPlayer(String name, int color) {
		super(name, color);
	}

	@Override
	public Move makeMove(LogicBoard board) {
		// First find all the valid moves in this board state.
		//ArrayList<Move> validMoves = findValidMoves(board);
		ArrayList<Move> validMoves = board.findPossibleMoves(getColor());
		
		//System.out.println("number of valid moves " + validMoves.size());
		if(validMoves.size()>=0) {
			//throw new RuntimeException();
		}
		// Throw a die and sample one valid move from a uniform 
		// distribution.
		Random randGen = new Random();	
		//System.out.println(validMoves.size());
		int randomMoveIdx = randGen.nextInt(validMoves.size());
		
		
		return validMoves.get(randomMoveIdx);
	}
	
	@Override
	public void postMoveProcessing(LogicBoard oldBoard, LogicBoard newBoard, 
			Player whoPlayed) {}
	
	@Override
	public void endOfGame(int diskDifferential) {} 
}
