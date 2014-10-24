
/**
 * Assume that this neural net is fully connected so there is no reason to map out the connections between
 * neurons. 
 * 
 * @author joelmaupin
 *
 */
public class NeuralNet {

	private NeuralLayer hiddenLayer;
	private NeuralLayer outputLayer;
	private int numHidden;
	private int numInput;
	private int numOutput;
	private double[] hiddenLayerOutput;
	
	/**
	 * 
	 * @param numInput
	 * @param numHidden
	 * @param numOutput
	 */
	public NeuralNet(int numInput, int numHidden, int numOutput){
		this.numInput  = numInput;
		this.numHidden = numHidden;
		this.numOutput = numOutput;
		this.hiddenLayer = new NeuralLayer(this.numInput, this.numHidden);
		this.outputLayer = new NeuralLayer(this.numHidden, this.numOutput);
	}
	
	
	public int getNumHidden() {
		return numHidden;
	}


	public int getNumInput() {
		return numInput;
	}


	public int getNumOutput() {
		return numOutput;
	}


	/**
	 * This sets the weights for the hidden layer.
	 * @param weights
	 */
	public void setHiddenLayerWeights(double[][] weights) {
		this.hiddenLayer.setWeights(weights);
	}
	
	/**
	 * 
	 * @return
	 */
	public double[][] getHiddenLayerWeights() {
		return this.hiddenLayer.getWeights();
	}
	
	/**
	 * This sets the weights for the output layer.
	 * @param weights
	 */
	public void setOutputLayerWeights(double[][] weights) {
		this.outputLayer.setWeights(weights);
	}
	
	/**
	 * 
	 * @return
	 */
	public double[][] getOutputLayerWeights() {
		return this.outputLayer.getWeights();
	}
	
	/**
	 * 
	 * @param inputs	Input integers INCLUDING the bias.
	 * @return			The classified output from the neural network.
	 */
	public double[] evaluateInputs(double[] inputs){
		// Check if the input has the appropriate size.
		if(inputs.length != this.numInput){
			System.err.println("[ERROR] The number of inputs " + 
					inputs.length + " does not match with " + numInput);
		}
		
		// Pass inputs into hidden layer
		hiddenLayerOutput = this.hiddenLayer.evaluate(inputs);
		
		// Take the output from the hidden layer and pass it to the output layer.
		double[] outputLayerOut = this.outputLayer.evaluate(hiddenLayerOutput);
		
		return outputLayerOut;
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
	 * @param inputs
	 * @param correctOutput
	 * @param eta
	 * @return The mean squared error
	 */
	public double backpropagation(double[][] inputs, double[][] correctOutput, double eta) {
//		double[][] hiddenWeights = hiddenLayer.getWeights();
//		double[][] outputWeights = outputLayer.getWeights();
		// The Mean-squared error
		double meanSquaredError = 0.0;
		
		double[][] hiddenWeights = new double[hiddenLayer.getWeights().length][hiddenLayer.getWeights()[0].length];
		double[][] outputWeights = new double[outputLayer.getWeights().length][outputLayer.getWeights()[0].length];
		for(int i = 0; i < hiddenWeights.length; i++) {
			for(int j = 0; j < hiddenWeights[0].length; j++) {
				hiddenWeights[i][j] = hiddenLayer.getWeights()[i][j];
			}
		}
		for(int i = 0; i < outputWeights.length; i++) {
			for(int j = 0; j < outputWeights[0].length; j++) {
				outputWeights[i][j] = outputLayer.getWeights()[i][j];
			}
		}
		
		// This is the number of examples (that matches the number of rows
		// of the matrix inputs.
		final int numExamples = inputs.length;	
		
		// Verify that the size of the inputs/outputs matches the number of inputs
		// that the networks expects.
		if(inputs[0].length != this.numInput || correctOutput[0].length != this.numOutput) {
			System.err.println("[ERROR] Bad Number! Check your input/output sizes");
		}
		
		for(int example = 0; example < numExamples; example++) {
			double[] actual = evaluateInputs(inputs[example]);
			double[] del_k = new double[numOutput];
			
			// Add the mean squared error of the current example.
			for(int k = 0; k < this.numOutput; ++k){
				meanSquaredError += Math.pow(Math.abs((correctOutput[example][k] - actual[k])), 2);
				//System.out.println("Example " + example + " " + actual[k]);
			}
			
			// Update weights of output layer
			for(int k = 0; k < this.numOutput; ++k){
				del_k[k] = (correctOutput[example][k] - actual[k]) * (1 - actual[k]) * actual[k];
				for(int j = 0; j < numHidden; ++j){
					double delta_w_kj = eta * del_k[k] * hiddenLayerOutput[j];
					outputWeights[k][j] += delta_w_kj;
				}
			}
			
			// Update weights of hidden layer
			for(int j = 0; j < this.numHidden; ++j){
				double sum_del_k = 0.0;
				
				for(int k = 0; k < numOutput; ++k){
					sum_del_k += outputLayer.getWeights()[k][j] * del_k[k];
				}
				
				double del_j = hiddenLayerOutput[j] * (1 - hiddenLayerOutput[j]) * sum_del_k;
				
				for(int i = 0; i < this.numInput; ++i){
					double delta_w_kj = eta * del_j * inputs[example][i];
					hiddenWeights[j][i] += delta_w_kj;
				}
			}
			for(int i = 0; i < hiddenWeights.length; i++) {
				for(int j = 0; j < hiddenWeights[0].length; j++) {
					hiddenLayer.getWeights()[i][j] = hiddenWeights[i][j];
				}
			}
			for(int i = 0; i < outputWeights.length; i++) {
				for(int j = 0; j < outputWeights[0].length; j++) {
					outputLayer.getWeights()[i][j] = outputWeights[i][j];
				}
			}
		}
		return meanSquaredError/ (numExamples * numOutput);
	}

	/**
	 * 
	 * @param x		Value of input into the sigmoid function.
	 * @return		Result of the sigmoid function at the specified value of x.
	 */
	public static double sigmoid(double x)
	{
	    return 1 / (1 + Math.exp(-x));
	}
}
