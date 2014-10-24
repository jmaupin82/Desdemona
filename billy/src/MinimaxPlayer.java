import java.util.ArrayList;

/**
 * This class implements a minimax player (with alpha-beta pruning) that
 * plays our Othello 10x10.
 * 
 * @author eleal
 *
 */
public class MinimaxPlayer extends Player implements BoardEvaluator{

	/** The 2x number of plies that the player explores */
	private final int maxDepth;
	
	private final double stabilityCoef;
	private final double parityCoef;
	private final double mobilityCoef;
	private final double differentialCoef;

	/**
	 * 
	 * @param name
	 * @param color
	 * @param numPlies
	 */
	public MinimaxPlayer(String name, int color, int numPlies) {
		super(name, color);
		this.maxDepth = 2 * numPlies;
		this.stabilityCoef    = 0.2;
		this.parityCoef       = 0.5;
		this.mobilityCoef     = 0.3;
		this.differentialCoef = 0.7;
	}
	
	/**
	 * 
	 * This constructor allows to specify the parameters to the
	 * heuristic.
	 * 
	 * @param name
	 * @param color
	 * @param numPlies
	 * @param stabilityCoef
	 * @param parityCoef
	 * @param mobilityCoef
	 * @param differentialCoef
	 */
	public MinimaxPlayer(String name, int color, int numPlies,
			double stabilityCoef, double parityCoef, double mobilityCoef,
			double differentialCoef) {
		super(name, color);
		this.maxDepth = 2 * numPlies;
		this.stabilityCoef    = stabilityCoef;
		this.parityCoef       = parityCoef;
		this.mobilityCoef     = mobilityCoef;
		this.differentialCoef = differentialCoef;
	}

	@Override
	public Move makeMove(Board board) {
		final int currentDepth = 0;
		// alpha = -infty
		final double alpha = - Double.MAX_VALUE;
		// beta = infty
		final double beta  = Double.MAX_VALUE;
		// Get the color of the current player
		final int playerColor = this.getColor();
		
		BoardEvaluator boardEvaluator = this;
		
		return MinimaxRunner.alphaBetaSearch(board, playerColor,
				currentDepth, this.maxDepth,
				alpha, beta, boardEvaluator);
	}

	@Override
	public void postMoveProcessing(Board oldBoard, Board newBoard, 
			Player whoPlayed) {}
	
	@Override
	public void endOfGame(int diskDifferential) {} 
	
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
	protected  Move alphaBetaSearch(Board board, Player currentPlayer, 
			int currentDepth, int maxDepth, double alpha, double beta) {

		// Get the color of the current player
		final int playerColor = currentPlayer.getColor();

		// Get all the possible actions from the current state.
		//ArrayList<Move> validMoves = getValidMoves(board);
		ArrayList<Move> validMoves = board.findPossibleMoves(currentPlayer.getColor());
		
		Move bestMove = null;
		
		// Find the minimax values of those moves
		if(playerColor == Constants.WHITE) {
			for(Move move : validMoves) {
				// Copy board into nextBoard
				Board nextBoard = new Board(board);
				nextBoard.play(playerColor, 
						move.square.getXCoord(), move.square.getYCoord());
				
				// WHITE seeks to maximize, so it will find the max of the
				// mins of the next moves.
				double currentValue = minValue(nextBoard, currentPlayer, 
						currentDepth+1, maxDepth,
						alpha, beta);
				double bestValue = - Double.MAX_VALUE;
				
				if(bestValue < currentValue) {
					bestValue = currentValue;
					bestMove  = move;
				}
			}
		}
		else {
			// Its the turn of BLACK
			for(Move move : validMoves) {
				// Copy board into nextBoard
				Board nextBoard = new Board(board);
				nextBoard.play(playerColor, 
						move.square.getXCoord(), move.square.getYCoord());
				
				// BLACK seeks to minimize, so it will find the min of the
				// maxs of the next moves.
				double currentValue = maxValue(nextBoard, currentPlayer, 
						currentDepth+1, maxDepth,
						alpha, beta);
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
	private double maxValue(Board board, Player currentPlayer,
			int currentDepth, int maxDepth, double alpha, double beta) {

		// Get the color of the current player
		final int playerColor = currentPlayer.getColor();

		// If we have descended the maximum number of plies or the state is
		// terminal, simply return the value of that state.
		if(currentDepth == maxDepth || isTerminal(board, currentPlayer)) {
			return evaluateBoard(board);
		}
		else {
			// v = - infty
			double v = -Double.MAX_VALUE;

			// Get all the possible actions from the current state.
			ArrayList<Move> validMoves = getValidMoves(board);

			for(Move move: validMoves) {
				// Find result(state,action) for the actions just found.
				// Copy board into nextBoard
				Board nextBoard = new Board(board);
				nextBoard.play(playerColor, 
						move.square.getXCoord(), move.square.getYCoord());

				v = Math.max(v, minValue(nextBoard, currentPlayer, 
						currentDepth+1, maxDepth, alpha, beta));
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
	private  double minValue(Board board, Player currentPlayer,
			int currentDepth, int maxDepth, double alpha, double beta) {

		// Get the color of the current player
		final int playerColor = currentPlayer.getColor();

		// If we have descended the maximum number of plies or the state is
		// terminal, simply return the value of that state.
		if(currentDepth == maxDepth || isTerminal(board, currentPlayer)) {
			return evaluateBoard(board);
		}
		else {
			// v = +infty
			double v = Double.MAX_VALUE;

			// Get all the possible actions from the current state.
			ArrayList<Move> validMoves = getValidMoves(board);

			for(Move move : validMoves) {
				// Find result(state,action) for the actions just found.
				// Copy board into nextBoard
				Board nextBoard = new Board(board);
				nextBoard.play(playerColor, 
						move.square.getXCoord(), move.square.getYCoord());

				// Find the min value between the current best minimum
				// and the next value.
				v = Math.min(v, maxValue(nextBoard, currentPlayer, 
						currentDepth+1, maxDepth, alpha, beta));
			}
			return v;
		}
	}

	/**
	 * 
	 * @param board
	 * @param currentPlayer
	 * @return
	 */
	 public double evaluateBoard(Board board) {
		
		
		int numberWhiteStones = board.count(Constants.WHITE);
		int numberBlackStones = board.count(Constants.BLACK);
		int diskDifferential  = numberWhiteStones - numberBlackStones;
		
		// Stability is the number of possible moves for WHITE minus
		// the number of possible moves for BLACK.
		final int mobility =  board.findPossibleMoves(Constants.WHITE).size() -
				board.findPossibleMoves(Constants.BLACK).size();

		// This is the number of empty squares in the board.
		int emptySquares = board.countEmptySquares();
		double parity    = Math.pow(-1, emptySquares % 2 + 1);
		int stability    = 0;
		
		// The value of a position is a convex combination of stability,
		// parity and mobility.
		double positionValue = 
				stabilityCoef * stability +
				parityCoef * parity + 
				mobilityCoef * mobility +
				differentialCoef * diskDifferential;
		
		return positionValue;
	}

	 
	/**
	 * 
	 * @param board
	 * @param player
	 * @return
	 */
	private  static boolean isTerminal(Board board, Player player) {
		boolean noMovesWhite = board.findPossibleMoves(Constants.WHITE).isEmpty();		
		boolean noMovesBlack = board.findPossibleMoves(Constants.BLACK).isEmpty();
		
		return noMovesWhite && noMovesBlack;
	}
}
