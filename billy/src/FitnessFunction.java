

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class implements the fitness function that is used in 
 * our genetic algorithm.
 *
 */
public class FitnessFunction {
	Population population;
	
	public FitnessFunction(Population population){
		this.population = population;
	}
	
	
	/**
	 * This function evaluates the fitness of a chromosome.
	 * @param individual
	 * @return
	 */
	public double fitnessValue(Chromosome individual) {
		
		//return population.getFitnessValue(individual);
		return 0.0;
	}
	
	
	
}
