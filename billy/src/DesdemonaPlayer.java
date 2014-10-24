import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 * This class implements Desdemona, which is our Othello 10x10 player
 * that uses a neural net, trained with reinforcement learning like
 * TD-Gammon, to evaluate positions.
 * 
 * @author eleal
 *
 */
public class DesdemonaPlayer extends Player implements BoardEvaluator {
	/** With this feed-forward sigmoid neural net, Desdemona
	 * evaluates positions
	 */
	private NeuralNetReinforcement net;
	/** The number of inputs to the net plus the bias and the turn */
	private final int numInput  = Constants.SIZE * Constants.SIZE + 2;
	/** The number of hidden neurons in the neural net*/
	private final int numHidden = Constants.SIZE;
	/** The number of output of the net */
	private final int numOutput = 1;

	/** True iff Desdemona is learning while playing */
	private boolean learn;

	/** The 2x number of plies that the player explores */
	private final int maxDepth;

	/** The name of the file where it reads/writes the output weights */
	private String outputWeightsFileName;

	/** The name of the file where it reads/writes the hidden weights */
	private String hiddenWeightsFileName;


	public DesdemonaPlayer(String name, int color, int numPlies,
			double gamma, double lambda, double alpha) {
		super(name, color);

		this.learn    = false;
		this.maxDepth = 2 * numPlies;
		this.net      = new NeuralNetReinforcement(numInput, numHidden, numOutput,
				gamma, lambda, alpha);
	}

	public DesdemonaPlayer(String name, int color, int numPlies, boolean learn,
			double gamma, double lambda, double alpha){
		super(name, color);

		this.learn    = learn;
		this.maxDepth = 2 * numPlies;
		this.net      = new NeuralNetReinforcement(numInput, numHidden, numOutput,
				gamma, lambda, alpha);
	}
	
	public DesdemonaPlayer(String name, int color, int numPlies,
			double gamma, double lambda, double alpha, double[][] hiddenLayerWeights,
			double[][] outputLayerWeights){
		super(name, color);

		this.learn    = learn;
		this.maxDepth = 2 * numPlies;
		this.net      = new NeuralNetReinforcement(numInput, numHidden, numOutput,
				gamma, lambda, alpha);
		
		// Set the weights to be the ones received as inputs.
		this.net.setHiddenLayerWeights(hiddenLayerWeights);
		this.net.setOutputLayerWeights(outputLayerWeights);
	}

	@Override
	public Move makeMove(Board board) {

		final int currentDepth = 0;
		// alpha = -infty
		final double alpha = - Double.MAX_VALUE;
		// beta = infty
		final double beta  = Double.MAX_VALUE;

		// Take the board representation and create the input that
		// will be provided to the neural net.
		double[] netInput = makeNetInputFromBoard(board, this.getColor());

		// This implements the board evaluation function.
		BoardEvaluator boardEvaluator = this;

		Move bestMove = MinimaxRunner.alphaBetaSearch(board, this.getColor(), 
				currentDepth, this.maxDepth, alpha, beta, boardEvaluator);

		
		return bestMove;
	}

	@Override
	public void postMoveProcessing(Board oldBoard, Board newBoard, Player whoPlayed) {
		double reward;

		// Take the board representation and create the input that
		// will be provided to the neural net.
		double[] netInput = makeNetInputFromBoard(oldBoard, this.getColor());
		double[] oldVArray = net.evaluateInputs(netInput);
		double V_old = oldVArray[0];

		// This is to find V_new
		netInput = makeNetInputFromBoard(oldBoard, this.getColor());
		double[] newVArray = net.evaluateInputs(netInput);
		double V_new = newVArray[0];

		// Check if the game has ended
		boolean gameEnded = newBoard.notEndOfGame();
		int diskDifferential = newBoard.getDiskDifferential();

		// Determine the reward in terms of the disk differential.
		if(gameEnded) {
			if(diskDifferential > 0) {
				if(this.color == Constants.WHITE) {
					// If white wins Desdemona is white.
					reward = 1.0;
				}
				else {
					// White wins but Desdemona is Black
					reward = -1.0;
				}
			}
			else if(diskDifferential < 0 ) {
				if(this.color == Constants.WHITE) {
					// Desdemona is white but lost
					reward = -1.0;
				}
				else {
					// Desdemona is black and won
					reward = 1.0;
				}
			} // if (diskDifferential < 0)
			else {
				reward = 0.0;
			}
		}
		else {
			// The game hasn't ended
			reward = 0.0;
		}
		// Update the weights in the network with the observed reward and the
		// difference between V_old and V_new.
		net.onlineTrain(V_old, V_new, reward);
	}

	@Override
	public void endOfGame(int diskDifferential) {} 

	/**
	 * 
	 * @param hiddenWeightsFilename
	 * @param outputWeightsFilename
	 */
	private void readWeightsFromFile(String hiddenWeightsFilename,
			String outputWeightsFilename) {

		double[][] hiddenWeights = new double[net.getNumHidden()][net.getNumInput()];
		double[][] outputWeights = new double[net.getNumOutput()][net.getNumHidden()];

		// First read the hidden weights file.
		try {
			BufferedReader br = new BufferedReader(new FileReader(hiddenWeightsFilename));

			for(int i = 0; i < net.getNumHidden(); i ++) {
				String line = br.readLine();

				if(line == null) {
					throw new IOException("Hidden Weights Matrix of the wrong size");
				}

				String[] hiddenWeightsNeuronJ = line.split(",");

				for(int j = 0; j < net.getNumInput(); j++) {
					hiddenWeights[i][j] = Double.parseDouble(hiddenWeightsNeuronJ[j]);
				}
			}
			br.close();	

			// Now read the output weights file
			br = new BufferedReader(new FileReader(outputWeightsFilename));

			for(int i = 0; i < net.getNumOutput(); i ++) {
				String line = br.readLine();

				if(line == null) {
					throw new IOException("Output Weights Matrix of the wrong size");
				}

				String[] outputWeightsNeuronJ = line.split(",");

				for(int j = 0; j < net.getNumHidden(); j++) {
					outputWeights[i][j] = Double.parseDouble(outputWeightsNeuronJ[j]);
				}
			}
			br.close();	

			// Now set the weights of the neural net to be the ones just read
			net.setHiddenLayerWeights(hiddenWeights);
			net.setOutputLayerWeights(outputWeights);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}


	/*private void writeWeightsToFile(String hiddenWeightsFilename,
			String outputWeightsFilename) {

		double[][] hiddenWeights = new double[net.getNumHidden()][net.getNumInput()];
		double[][] outputWeights = new double[net.getNumOutput()][net.getNumHidden()];


		// First read the hidden weights file.
		try {
			File hiddenWeightsFile = new File(hiddenWeightsFilename);

			// Create the file with hidden weights only if it doesn't exist
			if(!hiddenWeightsFile.exists()) {
			    hiddenWeightsFile.createNewFile();
			} 

			FileOutputStream hiddenWeightsOS = new FileOutputStream(hiddenWeightsFile, false); 

			for(int i = 0; i < net.getNumHidden(); i ++) {
				for(int j = 0; j < net.getNumInput(); j++) {
					hiddenWeightsOS.writeDouble.parseDouble(hiddenWeightsNeuronJ[j]);
				}
			}
			br.close();	

			// Now read the output weights file
			br = new BufferedReader(new FileReader(outputWeightsFilename));

			for(int i = 0; i < net.getNumOutput(); i ++) {
				String line = br.readLine();

				if(line == null) {
					throw new IOException("Output Weights Matrix of the wrong size");
				}

				String[] outputWeightsNeuronJ = line.split(",");

				for(int j = 0; j < net.getNumHidden(); j++) {
					outputWeights[i][j] = Double.parseDouble(outputWeightsNeuronJ[j]);
				}
			}
			br.close();	

			// Now set the weights of the neural net to be the ones just read
			net.setHiddenLayerWeights(hiddenWeights);
			net.setOutputLayerWeights(outputWeights);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * This function takes a board with stones, and then makes the input 
	 * that will be provided to the neural net.
	 * 
	 * @param board
	 * @param player
	 * @return
	 */
	private double[] makeNetInputFromBoard(Board board, int playerColor) {
		// The +2 is to encode the turn and the bias
		double [] netInput = new double[Board.SIZE * Board.SIZE + 2];

		netInput[0] = 1; // the bias
		netInput[1] = playerColor; // the turn

		// For every position of the board (in row-major order) we get its
		// state: either -1, 0, 1 and put it in the array of input.
		for(int i = 0; i < Board.SIZE; i++) {
			for(int j = 0; j < Board.SIZE; j++) {
				netInput[i*Board.SIZE + j+2] = board.getSquare(i, j).getState();
			}
		}

		return netInput;
	}

	/**
	 * This function should evaluate a position of the board.
	 * @param board
	 * @param player
	 * @return
	 */
	public double evaluateBoard(Board board) {
		double[] input = makeNetInputFromBoard(board, this.getColor());

		double boardValue = this.net.evaluateInputs(input)[0];

		return boardValue;
	}

}
