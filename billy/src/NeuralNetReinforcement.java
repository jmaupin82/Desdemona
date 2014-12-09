/**
 * 
 * @author eleal
 *
 */
public class NeuralNetReinforcement extends NeuralNet {

	/** The vector of eligibility traces contains an entry for every
	 * possible hidden weight.  */
	private double[][] eligibilityTracesHiddenWeights;

	/** The vector of eligibility traces contains an entry for every
	 * possible output weight. */
	private double[][] eligibilityTracesOutputWeights;

	/** This is the discount rate of the rewards in TD(lambda) */
	private final double gamma;

	/** This is the lambda parameter of TD(lambda) */
	private final double lambda;

	/** This is the learning rate of the reinforcement learning */
	private final double alpha;

	/**
	 * 
	 * @param numInput
	 * @param numHidden
	 * @param numOutput
	 * @param gamma
	 * @param lambda
	 * @param alpha
	 */
	public NeuralNetReinforcement(int numInput, int numHidden, int numOutput,
			double gamma, double lambda, double alpha) {
		super(numInput, numHidden, numOutput);

		this.gamma  = gamma;
		this.lambda = lambda;
		this.alpha  = alpha;
		this.eligibilityTracesHiddenWeights = new double[numHidden][numInput];
		this.eligibilityTracesOutputWeights = new double[numOutput][numHidden];

		// To give the initial 0 values to the eligibility traces.
		initializeEligibilityTraces();
	}


	public void onlineTrain(double V_old, double V_new, double reward,
			double[] oldBoardNetInput) {

		double[][] outputLayerWeights = getOutputLayerWeights();
		double[][] hiddenLayerWeights = getHiddenLayerWeights();

		// Step 1. Update the output layer weights
		for(int i = 0; i < getNumOutput(); i++) {
			for(int j = 0; j < getNumHidden(); j++) {

				outputLayerWeights[i][j] += alpha * 
						(reward + gamma * V_new - V_old) *
						eligibilityTracesOutputWeights[i][j];
			}
		}

		// Step 2. Update the hidden layer weights
		for(int i = 0; i < getNumHidden(); i++) {
			for(int j = 0; j < getNumInput(); j++) {
				hiddenLayerWeights[i][j] += alpha * 
						(reward + gamma * V_new - V_old) *
						eligibilityTracesHiddenWeights[i][j];
			}
		}

		// Calculate the grad_{weights}V(s).
		// These 2 arrays will hold the value of the gradient evaluated
		// at the old board, with respect to the weights.
		double[][] hiddenWeightsGrad = new double[numHidden][numInput];
		double[][] outputWeightsGrad = new double[numOutput][numHidden];
		
		calculateGradient(oldBoardNetInput, hiddenWeightsGrad, outputWeightsGrad);

		// Update the eligibility traces according to the formula
		// e(t) = gamma * lambda * e_{t-1} + grad_{weights} (V_t(s_t))
		// for the output weights.
		for(int i = 0; i < getNumOutput(); i++) {
			for(int j = 0; j < getNumHidden(); j++) {
				eligibilityTracesOutputWeights[i][j] = 
						gamma * lambda * eligibilityTracesOutputWeights[i][j] +
						outputWeightsGrad[i][j];
			}
		}
		// Update the eligibility traces according to the formula
		// e(t) = gamma * lambda * e_{t-1} + grad_{weights} (V_t(s_t))
		// for the hidden weights.
		for(int i = 0; i < getNumHidden(); i++) {
			for(int j = 0; j < getNumInput(); j++) {
				eligibilityTracesHiddenWeights[i][j] = 
						gamma * lambda * eligibilityTracesHiddenWeights[i][j] +
						hiddenWeightsGrad[i][j];
			}
		}
	}

	/**
	 * This function is in charge of initializing the eligibility traces to 0.0
	 * 
	 */
	private void initializeEligibilityTraces() {
		// Step 1. Initialize the eligibility traces of the output layer weights
		for(int i = 0; i < getNumOutput(); i++) {
			for(int j = 0; j < getNumHidden(); j++) {
				eligibilityTracesOutputWeights[i][j] = 0.0;
			}
		}

		// Step 2. Initialize the eligibility traces of the hidden layer weights
		for(int i = 0; i < getNumHidden(); i++) {
			for(int j = 0; j < getNumInput(); j++) {
				eligibilityTracesHiddenWeights[i][j] = 0.0;
			}
		}
	}

	/**
	 * This function implements the backpropagation algorithm.
	 * 
	 * Receives an array of inputs such that inputs[i][j] contains the jth
	 * entry of the ith example.
	 * 
	 * Receives an array correctOuput such that correctOutput[i][j] contains
	 * the jth correct output for the ith example.
	 * 
	 * @param input
	 * @param hiddenWeightsDeriv
	 *             This is the matrix with the derivative of the output of the net
	 *             with respect to the hidden weights
	 * @param outputWeightsDeriv
	 * 	          This is the matrix with the derivative of the output of the net
	 *             with respect to the output weights
	 */
	public void calculateGradient(double[] input, double[][] hiddenWeightsDeriv, 
			double[][] outputWeightsDeriv) {

		// Verify that the size of the inputs/outputs matches the number of inputs
		// that the networks expects.
		if(input.length != this.numInput) {
			System.err.println("[ERROR] Bad Number! Check your input/output sizes");
		}

		double[] actual    = evaluateInputs(input);
		double[] del_k     = new double[numOutput];
		// This array will hold the value (1-o_j) ^2, where o_j is the output of
		// neuron j of the output layer.
		double[] tanhPrime = new double[numOutput];
		
		// Find the gradient with respect to the output layer weights
		// For tanh the \derv{E}{w_{ji}} = (1-o_j)^2 * x_i
		for(int k = 0; k < this.numOutput; ++k){
			tanhPrime[k] = (1 - actual[k]) * (1 - actual[k]);
			for(int j = 0; j < numHidden; ++j){
				outputWeightsDeriv[k][j] = tanhPrime[k] * hiddenLayerOutput[j];
			}
		}

		// Find the gradient with respect to the of hidden layer weights
		for(int j = 0; j < this.numHidden; ++j){
			double sum_del_k = 0.0;

			for(int k = 0; k < numOutput; ++k){
				sum_del_k += outputLayer.getWeights()[k][j] * tanhPrime[k];
			}

			double del_j = (1 - hiddenLayerOutput[j]) * 
					(1 - hiddenLayerOutput[j]) * sum_del_k;

			for(int i = 0; i < this.numInput; ++i){
				hiddenWeightsDeriv[j][i] = del_j * input[i];
			}
		}
	}

}