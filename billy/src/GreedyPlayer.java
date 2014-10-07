import java.util.ArrayList;
import java.util.Random;


public class GreedyPlayer extends Player{

	public GreedyPlayer(){
		this.name = "Greedy Player";
	}

	public GreedyPlayer(String name){
		this.name = name;
	}

	@Override
	public Move makeMove(Board board) {

		// Search all of the possible moves and pick the one that has the best reward.

		// First find all the valid moves in this board state.
		ArrayList<Move> validMoves = getValidMoves(board);

		// Throw a die and sample one valid move from a uniform 
		// distribution.
		Random randGen = new Random();	
		//System.out.println(validMoves.size());
		int randomMoveIdx = randGen.nextInt(validMoves.size());


		return validMoves.get(randomMoveIdx);
	}
	
	/**
	 * Search all of the moves in the next round only and return the one that gives greedy 
	 * the most stones. 
	 * @return		A move object that is the greediest possible move.
	 */
	public Move getBestMove(Board board){
		
		
		
		
		return null;
	}

}
