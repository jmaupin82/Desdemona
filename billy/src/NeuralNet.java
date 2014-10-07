import java.io.Serializable;
import java.util.ArrayList;

/**
 * Assume that this neural net is fully connected so there is no reason to map out the connections between
 * neurons. 
 * 
 * @author joelmaupin
 *
 */
public class NeuralNet implements Serializable{

	/**
	 * Version id for our Serialization
	 */
	private static final long serialVersionUID = 5607742528650919974L;
	
	private NeuralLayer hidden;
	private NeuralLayer output;
	private static int numHidden = 200;
	private static int numInput = 201;
	private static int numOutput = 100;
	private double[] hiddenOut;
	
	/*
	 * //add in the bias input on only the input layer
		int[] biased = new int[numInput];
		for(int i = 0; i < numInput; ++i){
			if(i == inputs.length){
				biased[i] = -1;
			}
			biased[i] = inputs[i];
		}
	 */
	public NeuralNet(){
		this.hidden = new NeuralLayer(this.numInput, this.numHidden);
		this.output = new NeuralLayer(this.numHidden, this.numOutput);
		
	}
	
	/**
	 * 
	 * @param inputs	Input integers INCLUDING the bias.
	 * @return			The classified output from the neural network.
	 */
	public double[] classifyInputs(double[] inputs){
		int[] output = new int[numOutput];
		if(inputs.length != this.numInput){
			System.out.println("The number of inputs does not match up. Please check your code.");
		}
		
		//pass inputs into hidden layer
		hiddenOut = this.hidden.evaluate(inputs);
		double[] outputOut = this.output.evaluate(hiddenOut);
		
		//convert the sigmoid doubles to 1s and 0s.
//		for(int i = 0; i < outputOut.length; ++i){
//			//logic to convert
//			if(outputOut[i] > 0.5){
//				output[i] = 1;
//			}
//			else{
//				output[i] = 0;
//			}
//		}
		
		return outputOut;
	}
	
	public void backpropagation(double[][] inputs, double[][] correctOutput) {
		int numExamples = inputs.length;
		double[][] hiddenWeights = hidden.getWeights();
		double[][] outputWeights = output.getWeights();
		double eta = 0.8;
		if(inputs[0].length != this.numInput || correctOutput[0].length != this.numOutput) {
			System.out.println("Bad Number! Check your input size");
		}
		for(int example = 0; example < inputs.length; example++) {
			double[] actual = classifyInputs(inputs[example]);
			
			// We find the absolute error between the correct output and the
			// networks output.
			double[] absError = new double[numOutput];
			for(int i = 0; i < absError.length; i++) {
				absError[i] = Math.abs(actual[i] - correctOutput[example][i]);
			}
			double[] del_k = new double[numOutput];
			// Update weights of output layer
			for(int k = 0; k < this.numOutput; ++k){
				del_k[k] = -(correctOutput[example][k] - actual[k]) * (1 - actual[k]) * actual[k];
				for(int j = 0; j < numHidden; ++j){
					double delta_w_kj = eta * del_k[k] * hiddenOut[j];
					outputWeights[j][k] = outputWeights[j][k] + delta_w_kj;
				}
			}
			
			// Update weights of hidden layer
			for(int j = 0; j < this.numHidden; ++j){
				for(int i = 0; i < numInput; ++i){
					double sum_del_k = 0.0;
					for(int k = 0; k < numOutput; ++k){
						sum_del_k += outputWeights[j][k] * del_k[k];
					}
					double del_j = hiddenOut[i] * (1 - hiddenOut[i]) * sum_del_k;
					double delta_w_kj = eta * del_j * hiddenOut[i];
					hiddenWeights[i][j] = hiddenWeights[i][j] + delta_w_kj;
				}
			}
	
		}

	}
//
//	public double[] findDeltaWeights(double[] expected, double[] actual, double[] input, double eta ) {
//		// Find the delta_wji
//		for(int j = 0; j < this.numNeurons; ++j){
//			for(int i = 0; i < input.length; ++i){
//				double del_j = -(expected[j] - actual[j]) * (1 - actual[j]) * actual[j];
//				double delta_w_ji = eta * del_j * input[i];
//				this.weights[i][j] = this.weights[i][j] + delta_w_ji;
//			}
//		}
//		
//		
//		// Update the weight_ji with our delta value
//		
//		//return 
//	}
	
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
