
public class NeuralLayer {

	private double[][] weights;
	private int numNeurons;
	private int numInputs;
	
	/**
	 * 
	 * @param inputs		The number of inputs to the layer.
	 * @param neurons		The number of neurons in layer.
	 */
	public NeuralLayer(int inputs, int neurons){
		this.numNeurons = neurons;
		this.numInputs = inputs;
		this.weights = new double[numInputs][numNeurons];
		//initialize the weights
	}

	private void initWeights(){
		
	}
	public int getNumNeurons() {
		return numNeurons;
	}

	public double[][] getWeights() {
		return this.weights;
	}
	
	public void setWeights(double[][] newWeights){
		this.weights = newWeights;
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
	
	//Evaluate this layer.
	public double[] evaluate(double[] input){
		double[] result = new double[this.numNeurons];
		
		for(int j =0; j < numNeurons; ++j){
			double summ = 0.0;
			for(int i = 0; i < input.length; ++i){
				summ += input[i] * weights[i][j];
			}
			result[j] = NeuralNet.sigmoid(summ);
		}
		return result;
	}

}
