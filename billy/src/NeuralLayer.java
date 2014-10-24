import java.util.Random;

/**
 * This class implements a single layer of a neural network.
 * 
 * @author eleal
 *
 */
public class NeuralLayer {
	// This is the matrix of weights.
	// Invariant: weights[i][j] is the weight associated with the jth entry
	//            to neuron i.
	/** The matrix of weights of the layer */
	private double[][] weights;
	/** The number of neurons that form the layer */
	private int numNeurons;
	/** The number of inputs to the layer */
	private int numInputs;

	/**
	 * 
	 * @param inputs   The number of inputs (including bias) to the layer
	 * @param neurons  The number of neurons that form the layer
	 */
	public NeuralLayer(int inputs, int neurons){
		this.numNeurons = neurons;
		this.numInputs = inputs;
		this.weights = new double[numNeurons][numInputs];
		//initialize the weights

		initWeights();
	}

	/**
	 * This function initializes the weights for the layer.
	 */
	private void initWeights(){
		Random rand = new Random();

		float rvalue = rand.nextFloat() /** (maxX - minX) + minX*/;
		for(int i = 0; i < this.weights.length; i++) {
			for(int j = 0; j < this.weights[0].length; j++) {
				weights[i][j] = rvalue;
			}
		}
	}

	public int getNumNeurons() {
		return numNeurons;
	}

	public double[][] getWeights() {
		return this.weights;
	}

	public void setWeights(double[][] weights) {
		this.weights = weights;
	}

	public void setNumNeurons(int numNeurons) {
		this.numNeurons = numNeurons;
	}

	public int getNumInputs() {
		return numInputs;
	}

	public void setNumInputs(int numInputs) {
		this.numInputs = numInputs;
	}

	/**
	 * Evaluate this layer with a given input.
	 * 
	 * @param input
	 * @return
	 */
	public double[] evaluate(double[] input){
		double[] result = new double[this.numNeurons];

		// The neurons are the rows of the weight matrix.
		for(int i =0; i < numNeurons; ++i){
			double summ = 0.0;
			// The inputs are columns of the weight matrix.
			for(int j = 0; j < input.length; ++j){
				summ += input[j] * weights[i][j];
			}
			result[i] = NeuralNet.sigmoid(summ);
		}
		return result;
	}

}
