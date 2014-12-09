import java.util.ArrayList;


public class MinimaxRunner {

	public MinimaxRunner() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 * @param board
	 * @param currentPlayer
	 * @param currentDepth
	 * @param maxDepth
	 * @param alpha
	 * @param beta
	 * @return
	 */
	protected static Move alphaBetaSearch(LogicBoard board, int playerColor, 
			int currentDepth, int maxDepth, double alpha, double beta,
			BoardEvaluator boardEvaluator) {

		// Get all the possible actions from the current state.
		ArrayList<Move> validMoves = board.findPossibleMoves(playerColor);

		Move bestMove = null;
		double bestValue = - Double.MAX_VALUE;
		// Find the minimax values of those moves
		if(playerColor == Constants.WHITE) {
			// Its the turn of WHITE so the other player is BLACK
			final int otherPlayerColor = Constants.BLACK;
			
			//System.out.println("entering absearchloop");
			for(Move move : validMoves) {
				
				// Copy board into nextBoard
				//LogicBoard nextBoard = new LogicBoard(board);
				LogicBoard nextBoard = board;
				//System.out.println("insideg absearchloop befor");
				//System.out.println(nextBoard);
				nextBoard.play(playerColor, move.getX(), move.getY());
				//System.out.println("insideg absearchloop after:");
				//System.out.println(nextBoard);
				
				
				// WHITE seeks to maximize, so it will find the max of the
				// mins of the next moves.
				double currentValue = minValue(nextBoard, otherPlayerColor, 
						currentDepth+1, maxDepth,
						alpha, beta, boardEvaluator);
				
				
				if(bestValue < currentValue) {
					bestValue = currentValue;
					bestMove  = move;
				}
				
				// Undo the play
				nextBoard.prev();
			}
		}
		else {
			// Its the turn of BLACK so the other player is WHITE
			final int otherPlayerColor = Constants.WHITE;
			bestValue = Double.MAX_VALUE;
			//System.out.println("entering absearchloop");
			for(Move move : validMoves) {
				// Copy board into nextBoard
				//LogicBoard nextBoard = new LogicBoard(board);
				LogicBoard nextBoard = board;
				//System.out.println("insideg absearchloop befor");
				//System.out.println(nextBoard);
				nextBoard.play(playerColor, move.getX(), move.getY());
				//System.out.println("insideg absearchloop after:");
				//System.out.println(nextBoard);
				
				// BLACK seeks to minimize, so it will find the min of the
				// maxs of the next moves.
				double currentValue = maxValue(nextBoard, otherPlayerColor, 
						currentDepth+1, maxDepth,
						alpha, beta, boardEvaluator);
				
				
				if(bestValue > currentValue) {
					bestValue = currentValue;
					bestMove  = move;
				}
				
				// Undo the play
				nextBoard.prev();
			}
		}
		//System.out.println("I play " + bestMove.getSquare());
		return bestMove;
	}

	/**
	 * 
	 * @param board
	 * @param currentPlayer
	 * @param currentDepth
	 * @param maxDepth
	 * @param alpha
	 * @param beta
	 * @return
	 */
	private static double maxValue(LogicBoard board, int playerColor,
			int currentDepth, int maxDepth, double alpha, double beta,
			BoardEvaluator boardEvaluator) {

		//System.out.println("MaxcurrentDepth " + currentDepth);

		// If we have descended the maximum number of plies or the state is
		// terminal, simply return the value of that state.
		if(currentDepth == maxDepth || isTerminal(board)) {
			return boardEvaluator.evaluateBoard(board);
		}
		else {
			// v = - infty
			double v = -Double.MAX_VALUE;

			// Get all the possible actions from the current state.
			ArrayList<Move> validMoves = board.findPossibleMoves(playerColor);

			for(Move move: validMoves) {
				// Find result(state,action) for the actions just found.
				// Copy board into nextBoard
				//LogicBoard nextBoard = new LogicBoard(board);
				LogicBoard nextBoard = board;
				//System.out.println("MAX befor" + currentDepth);
				//System.out.println(nextBoard);
				//System.out.println("MAX after:" + currentDepth);
				nextBoard.play(playerColor, move.getX(), move.getY());
				//System.out.println(nextBoard);

				v = Math.max(v, minValue(nextBoard, getOtherPlayerColor(playerColor), 
						currentDepth+1, maxDepth, alpha, beta,
						boardEvaluator));
				
				// Undo the play
				nextBoard.prev();
				
				// Alfa-beta pruning
				if(v >= beta) {
					return v;
				}
				alpha = Math.max(alpha, v);
			}
			return v;
		}
	}

	/**
	 * 
	 * @param board
	 * @param currentPlayer
	 * @param currentDepth
	 * @param maxDepth
	 * @param alpha
	 * @param beta
	 * @return
	 */
	private static double minValue(LogicBoard board, int playerColor,
			int currentDepth, int maxDepth, double alpha, double beta,
			BoardEvaluator boardEvaluator) {

		//System.out.println("MincurrentDepth " + currentDepth);

		// If we have descended the maximum number of plies or the state is
		// terminal, simply return the value of that state.
		if(currentDepth == maxDepth || isTerminal(board)) {
			return boardEvaluator.evaluateBoard(board);
		}
		else {
			// v = +infty
			double v = Double.MAX_VALUE;

			// Get all the possible actions from the current state.
			ArrayList<Move> validMoves = board.findPossibleMoves(playerColor);

			for(Move move : validMoves) {
				// Find result(state,action) for the actions just found.
				// Copy board into nextBoard
				//LogicBoard nextBoard = new LogicBoard(board);
				LogicBoard nextBoard = board;
				//System.out.println("Min befor + dept" + currentDepth);
				//System.out.println(nextBoard);
				nextBoard.play(playerColor, move.getX(), move.getY());
				//System.out.println("Min after:" + currentDepth);
				//System.out.println(nextBoard);
				

				// Find the min value between the current best minimum
				// and the next value.
				v = Math.min(v, maxValue(nextBoard, getOtherPlayerColor(playerColor), 
						currentDepth+1, maxDepth, alpha, beta,
						boardEvaluator));
				
				// Undo the play
				nextBoard.prev();
				
				// Alfa-beta pruning
				if(v <= alpha) {
					return v;
				}
				beta = Math.min(beta, v);
			}
			return v;
		}
	}
	
	
	/**
	 * 
	 * @param board
	 * @param player
	 * @return
	 */
	private static boolean isTerminal(LogicBoard board) {
		return board.EndOfGame();
	}
	
	/**
	 * Given a color it returns the opponent's color
	 * 
	 * @param playerColor
	 * @return
	 */
	private static int getOtherPlayerColor(int playerColor) {
		switch(playerColor) {
		case Constants.BLACK:
			return Constants.WHITE;
		case Constants.WHITE:
			return Constants.BLACK;
		}
		return Constants.WHITE;
	}

}
