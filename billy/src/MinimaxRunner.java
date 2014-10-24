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
	protected static Move alphaBetaSearch(Board board, int playerColor, 
			int currentDepth, int maxDepth, double alpha, double beta,
			BoardEvaluator boardEvaluator) {

		// Get all the possible actions from the current state.
		ArrayList<Move> validMoves = board.findPossibleMoves(playerColor);

		Move bestMove = null;
		
		// Find the minimax values of those moves
		if(playerColor == Constants.WHITE) {
			// Its the turn of WHITE so the other player is BLACK
			final int otherPlayerColor = Constants.BLACK;
			
			for(Move move : validMoves) {
				// Copy board into nextBoard
				Board nextBoard = new Board(board);
				nextBoard.play(playerColor, 
						move.square.getXCoord(), move.square.getYCoord());
				
				// WHITE seeks to maximize, so it will find the max of the
				// mins of the next moves.
				double currentValue = minValue(nextBoard, otherPlayerColor, 
						currentDepth+1, maxDepth,
						alpha, beta, boardEvaluator);
				double bestValue = - Double.MAX_VALUE;
				
				if(bestValue < currentValue) {
					bestValue = currentValue;
					bestMove  = move;
				}
			}
		}
		else {
			// Its the turn of BLACK so the other player is WHITE
			final int otherPlayerColor = Constants.WHITE;
			
			for(Move move : validMoves) {
				// Copy board into nextBoard
				Board nextBoard = new Board(board);
				nextBoard.play(playerColor, 
						move.square.getXCoord(), move.square.getYCoord());
				
				// BLACK seeks to minimize, so it will find the min of the
				// maxs of the next moves.
				double currentValue = maxValue(nextBoard, otherPlayerColor, 
						currentDepth+1, maxDepth,
						alpha, beta, boardEvaluator);
				double bestValue = Double.MAX_VALUE;
				
				if(bestValue > currentValue) {
					bestValue = currentValue;
					bestMove  = move;
				}
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
	private static double maxValue(Board board, int playerColor,
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
				Board nextBoard = new Board(board);
				nextBoard.play(playerColor, 
						move.square.getXCoord(), move.square.getYCoord());

				v = Math.max(v, minValue(nextBoard, getOtherPlayerColor(playerColor), 
						currentDepth+1, maxDepth, alpha, beta,
						boardEvaluator));
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
	private static double minValue(Board board, int playerColor,
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
				Board nextBoard = new Board(board);
				nextBoard.play(playerColor, 
						move.square.getXCoord(), move.square.getYCoord());

				// Find the min value between the current best minimum
				// and the next value.
				v = Math.min(v, maxValue(nextBoard, getOtherPlayerColor(playerColor), 
						currentDepth+1, maxDepth, alpha, beta,
						boardEvaluator));
			}
			return v;
		}
	}
	
	/*protected static ArrayList<Move> getValidMoves(Board board) {
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
	} */
	
	/**
	 * 
	 * @param board
	 * @param player
	 * @return
	 */
	private static boolean isTerminal(Board board) {
		boolean noMovesWhite = board.findPossibleMoves(Constants.WHITE).isEmpty();		
		boolean noMovesBlack = board.findPossibleMoves(Constants.BLACK).isEmpty();
		
		return noMovesWhite && noMovesBlack;
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
