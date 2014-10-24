import static org.junit.Assert.*;

import org.junit.Test;


public class NeuralNetTest2 {

	@Test
	public void test() {
		// Two inputs x,y and the third is the bias (which will be ignored).
		final int sizeInputLayer  = 3; // No bias.
		final int sizeHiddenLayer = 2; // Not Including bias
		final int sizeOutputLayer = 2;
		final double tol = 0.001; // The tolerance for the results.

		NeuralNet net = new NeuralNet(sizeInputLayer, sizeHiddenLayer, sizeOutputLayer);

		// Remember that weights[i][j] is the j\ith weight into neuron j.
		double[][] hiddenLayerWeights = new double[sizeInputLayer][sizeHiddenLayer];
		double[][] outputLayerWeights = new double[sizeHiddenLayer][sizeOutputLayer];

		hiddenLayerWeights[0][0] = 0.2;  hiddenLayerWeights[0][1] = 0.7;  // Ignore the bias.
		hiddenLayerWeights[1][0] = -0.1; hiddenLayerWeights[1][1] = -1.2;
		hiddenLayerWeights[2][0] = 0.4;  hiddenLayerWeights[2][1] = 1.2;

		outputLayerWeights[0][0] = 1.1;  outputLayerWeights[0][1] = 3.1; 
		outputLayerWeights[1][0] = 0.1;  outputLayerWeights[1][1] = 1.17;

		net.setHiddenLayerWeights(hiddenLayerWeights);
		net.setOutputLayerWeights(outputLayerWeights);

		// Initialize the arrays for the inputs.
		double[]  inputs = new double[sizeInputLayer];
		inputs[0] = 10.0; inputs[1] = 30.0; inputs[2] = 20.0; 

		// Initialize the values for the targets.
		double[] targets = new double[sizeOutputLayer];
		targets[0] = 0.750;
		targets[1] = 0.957;
		
		// Now test if it encodes the xor function.
		double[] outputs = net.evaluateInputs(inputs);
		
		System.out.println(outputs[0]);
		System.out.println(outputs[1]);
		
		assertEquals(targets[0], outputs[0], tol);
		assertEquals(targets[1], outputs[1], tol);
	}
}
