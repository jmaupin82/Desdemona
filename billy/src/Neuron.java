import java.util.ArrayList;


public class Neuron {

	private ArrayList<Double> weights;
	private static FunctionTypes ACTIVATION_FUNCTION;
	
	public Neuron(){
		this.ACTIVATION_FUNCTION = FunctionTypes.SIGMOID;
	}
	
	public ArrayList<Double> getWeights(){
		return (this.weights);
	}


}

