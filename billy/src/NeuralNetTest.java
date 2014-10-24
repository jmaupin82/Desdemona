import static org.junit.Assert.*;

import org.junit.Test;


public class NeuralNetTest {

	@Test
	public void xorNetworkTest() {
		// Two inputs x,y and the third is the bias (which will be ignored).
		final int sizeInputLayer  = 3; // Including bias.
		final int sizeHiddenLayer = 2; // Not Including bias
		final int sizeOutputLayer = 1;
		final double tol = 0.001; // The tolerance for the results.
		
		NeuralNet net = new NeuralNet(sizeInputLayer, sizeHiddenLayer, sizeOutputLayer);
		
		// Remember that weights[i][j] is the j\ith weight into neuron j.
		double[][] hiddenLayerWeights = new double[sizeInputLayer][sizeHiddenLayer];
		double[][] outputLayerWeights = new double[sizeHiddenLayer][sizeOutputLayer];
		
		hiddenLayerWeights[0][0] = 0; hiddenLayerWeights[0][1] = 0;  // Ignore the bias.
		hiddenLayerWeights[1][0] = 1; hiddenLayerWeights[1][1] = 1;
		hiddenLayerWeights[2][0] = 1; hiddenLayerWeights[2][1] = 1;
		
		outputLayerWeights[0][0] = 1; 
		outputLayerWeights[1][0] = -1; 
		
		net.setHiddenLayerWeights(hiddenLayerWeights);
		net.setOutputLayerWeights(outputLayerWeights);
		
		// Initialize the arrays for the inputs.
		double[]  inputs00 = new double[sizeInputLayer];
		inputs00[0] = 0.0; inputs00[1] = 0.0; inputs00[2] = 0.0; 
		
		double[]  inputs01 = new double[sizeInputLayer];
		inputs01[0] = 0.0; inputs01[1] = 0.0; inputs01[2] = 1.0;
		
		double[]  inputs10 = new double[sizeInputLayer];
		inputs10[0] = 1.0;  inputs10[1] = 1.0; inputs10[2] = 0.0;
		
		double[]  inputs11 = new double[sizeInputLayer];
		inputs11[0] = 1.0; inputs11[1] = 1.0; inputs11[2] = 1.0;
		
		// Initialize the values for the targets.
		double target00 = 0.5;
		double target01 = 0.5;
		
		double target10 = 0.5;		
		double target11 = 0.5;
		
		// Now test if it encodes the xor function.
		double[] output00 = net.evaluateInputs(inputs00);
		double[] output01 = net.evaluateInputs(inputs01);
		double[] output10 = net.evaluateInputs(inputs10);
		double[] output11 = net.evaluateInputs(inputs11);
		
		System.out.println(output00[0]);
		System.out.println(output01[0]);
		System.out.println(output10[0]);
		System.out.println(output11[0]);
		
		assertEquals(target00, output00[0], tol);
		assertEquals(target01, output01[0], tol);
		assertEquals(target10, output10[0], tol);
		assertEquals(target11, output11[0], tol);
	}

}
