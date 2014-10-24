import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;


public class NeuralNetTestBackprop {

	@Test
	public void test() {
		// Two inputs x,y and the third is the bias (which will be ignored).
		final int sizeInputLayer  = 3; // Including bias.
		final int sizeHiddenLayer = 2; // Not Including bias
		final int sizeOutputLayer = 1;
		final int numExamples = 620000;  
		final double tol = 0.001; // The tolerance for the results.
		final double eta = 0.05;

		NeuralNet net = new NeuralNet(sizeInputLayer, sizeHiddenLayer, sizeOutputLayer);

		// Initialize the arrays for the inputs.
		double[][]  inputs = new double[numExamples][sizeInputLayer];


		inputs[0][0] = 1.0; inputs[0][1] = 0.0; inputs[0][2] = 0.0; 
		inputs[1][0] = 1.0; inputs[1][1] = 0.0; inputs[1][2] = 1.0;
		inputs[2][0] = 1.0; inputs[2][1] = 1.0; inputs[2][2] = 0.0;
		inputs[3][0] = 1.0; inputs[3][1] = 1.0; inputs[3][2] = 1.0;

//		inputs[0][0] = 0.0; inputs[0][1] = 0.0; 
//		inputs[1][0] = 0.0; inputs[1][1] = 1.0; 
//		inputs[2][0] = 1.0; inputs[2][1] = 0.0; 
//		inputs[3][0] = 1.0; inputs[3][1] = 1.0; 

		// Initialize the values for the targets.
		double[][] targets = new double[numExamples][sizeOutputLayer];
		targets[0][0] = 0.0;
		targets[1][0] = 1.0;
		targets[2][0] = 1.0;
		targets[3][0] = 0.0;

		// Throw a die and sample one valid move from a uniform 
		// distribution.
		Random randGen = new Random();	


		for(int i = 4; i < numExamples; i++) {
			int randomIdx = randGen.nextInt(4);
			//System.out.println(inputs[randomIdx][0]);
			inputs[i][0] = inputs[randomIdx][0];  
			inputs[i][1] = inputs[randomIdx][1];
			inputs[i][2] = inputs[randomIdx][2];
			targets[i][0] = targets[randomIdx][0];
			//System.out.println("input " + i + " column " + inputs[i][1] + " xor " + inputs[i][2]+ " target " + targets[i][0]);
		}

		double meanSquaredError = net.backpropagation(inputs, targets, eta);
		System.out.println("the mean squared error = " + meanSquaredError);
//
//		double[] input0 = { 0.0, 0.0};
//		double[] input1 = {0.0, 1.0};
//		double[] input2 = { 1.0, 0.0};
//		double[] input3 = { 1.0, 1.0};
//		
		double[] input0 = {1.0,  0.0, 0.0};
		double[] input1 = {1.0, 0.0, 1.0};
		double[] input2 = {1.0,  1.0, 0.0};
		double[] input3 = {1.0,  1.0, 1.0};

		// Now test if it encodes the xor function.
		double[] output00 = net.evaluateInputs(input0);
		double[] output01 = net.evaluateInputs(input1);
		double[] output10 = net.evaluateInputs(input2);
		double[] output11 = net.evaluateInputs(input3);

		System.out.println(output00[0]);
		System.out.println(output01[0]);
		System.out.println(output10[0]);
		System.out.println(output11[0]);

	}

}
