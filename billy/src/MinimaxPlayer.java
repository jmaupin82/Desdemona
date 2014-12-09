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
	public Move makeMove(LogicBoard board) {
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
	public void postMoveProcessing(LogicBoard oldBoard, LogicBoard newBoard, 
			Player whoPlayed) {}
	
	@Override
	public void endOfGame(int diskDifferential) {} 
	
	/**
	 * 
	 * @param board
	 * @param currentPlayer
	 * @return
	 */
	 public double evaluateBoard(LogicBoard board) {	
		int numberWhiteStones = board.count(Constants.WHITE);
		int numberBlackStones = board.count(Constants.BLACK);
		int diskDifferential  = numberWhiteStones - numberBlackStones;
		
		int diskDifferentialRatio = diskDifferential / 
				(numberWhiteStones + numberBlackStones);
		
		// Stability is the number of possible moves for WHITE minus
		// the number of possible moves for BLACK.
		final int numMovesBlack = board.findPossibleMoves(Constants.BLACK).size();
		final int numMovesWhite = board.findPossibleMoves(Constants.WHITE).size();
		int mobility;
		
		if(numMovesBlack + numMovesWhite != 0) {
			mobility =  (numMovesBlack - numMovesWhite) / 
				(numMovesBlack + numMovesWhite);
		}
		else {
			mobility = 0;
		}
		
		// This is the number of empty squares in the board.
		int emptySquares = board.countEmptySquares();
		double parity    = Math.pow(-1, emptySquares % 2 + 1);
		
		//
		final int numBlackCorners = board.countPlayedCorners(Constants.BLACK);
		final int numWhiteCorners = board.countPlayedCorners(Constants.WHITE);
		final int stability       = numBlackCorners - numWhiteCorners;
		
		// The value of a position is a convex combination of stability,
		// parity and mobility.
		double positionValue = 
				stabilityCoef * stability +
				parityCoef * parity + 
				mobilityCoef * mobility +
				differentialCoef * (-diskDifferentialRatio);
		
		return positionValue;
	}
}
