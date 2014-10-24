import java.util.ArrayList;


public class GreedyMinMaxPlayer extends Player{
	
	public GreedyMinMaxPlayer(){
		this.name = "Greedy Min Max";
	}
	public GreedyMinMaxPlayer(String name){
		this.name = name;
	}

	@Override
	public Move makeMove(Board board) {

		// Search all of the possible moves and pick the one that has the best reward.

		// First find all the valid moves in this board state.
		ArrayList<Move> validMoves = getValidMoves(board);


		return getBestMove(board, validMoves);
	}
	
	/**
	 * 
	 * @param board 	Current game board
	 * @param m			The move to be evaluated
	 * @return			A double representing the value of that move. Here just counts number of own pieces. 
	 */
	public double simpleEvaluate(Board board, Move m){
//		this.billy.makeMove(m.getSquare());
//		board = billy.getBoard();
		int temp = board.count(this.color);
		//board.prev();
		return temp;
	}
	
	/**
	 * Search all of the moves in the next round only and return the one that gives greedy 
	 * the most stones. 
	 * @return		A move object that is the greediest possible move.
	 */
	public Move getBestMove(Board board, ArrayList<Move> moves){
		Move result = moves.get(0);
		double best = Double.MIN_VALUE;
		for(Move m : moves){
			double temp = simpleEvaluate(board, m);
			//System.out.println(temp);
			if(temp > best){
				best = temp;
				result = m;
			}
		}
		System.out.println("best outcome: " + best);
		return result;
	}
	@Override
	public void postMoveProcessing(Board oldBoard, Board newBoard,
			Player whoPlayed) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void endOfGame(int diskDifferential) {
		// TODO Auto-generated method stub
		
	}
}
