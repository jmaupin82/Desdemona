

import java.util.ArrayList;
import java.util.List;
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
	public Move makeMove(Board board) {
		// First find all the valid moves in this board state.
		//ArrayList<Move> validMoves = findValidMoves(board);
		ArrayList<Move> validMoves = board.findPossibleMoves(getColor());
		
		// Throw a die and sample one valid move from a uniform 
		// distribution.
		Random randGen = new Random();	
		//System.out.println(validMoves.size());
		int randomMoveIdx = randGen.nextInt(validMoves.size());
		
		
		return validMoves.get(randomMoveIdx);
	}
	
	@Override
	public void postMoveProcessing(Board oldBoard, Board newBoard, 
			Player whoPlayed) {}
	
	@Override
	public void endOfGame(int diskDifferential) {} 
}
