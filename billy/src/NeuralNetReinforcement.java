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


	public void onlineTrain(double V_old, double V_new, double reward) {

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
		double gradientV = 0.5;

		// Update the eligibility traces according to the formula
		// e(t) = gamma * lambda * e_{t-1} + grad_{weights} (V_t(s_t))
		// for the output weights.
		for(int i = 0; i < getNumOutput(); i++) {
			for(int j = 0; j < getNumHidden(); j++) {
				eligibilityTracesOutputWeights[i][j] = 
						gamma * lambda * eligibilityTracesOutputWeights[i][j] +
						gradientV;
			}
		}
		// Update the eligibility traces according to the formula
		// e(t) = gamma * lambda * e_{t-1} + grad_{weights} (V_t(s_t))
		// for the hidden weights.
		for(int i = 0; i < getNumHidden(); i++) {
			for(int j = 0; j < getNumInput(); j++) {
				eligibilityTracesHiddenWeights[i][j] = 
						gamma * lambda * eligibilityTracesHiddenWeights[i][j] +
						gradientV;
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
}