

import java.util.LinkedHashMap;

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
	 *
	
	public double fitnessValue(Chromosome individual) {
		double score = 0.0;
		final double constant_1 = 0.7;
		final double constant_2 = 0.3;
		
		LinkedHashMap<ChromosomeState,String> policies = individual.getPolicies();
		
		
		for(ChromosomeState chromosomeState: policies.keySet()){
			String action = policies.get(chromosomeState);
			
			if(action.equals("goToBase")){
				if(chromosomeState.getFeatureValue("moneyOnShip").equals("<50")){
					score += constant_1 * Math.pow(40,2);
				}
				else if(chromosomeState.getFeatureValue("moneyOnShip").equals("50-99")){
					score += constant_1 * Math.pow(70,2);
				}
				else if(chromosomeState.getFeatureValue("moneyOnShip").equals("100-500")){
					score += constant_1 * Math.pow(125,2);
				}
				else{
					score += constant_1 * Math.pow(600,2);
				}
			}
			else if(action.equals("goToBeacon")){
				if(chromosomeState.getFeatureValue("distanceToNearestBeacon").equals("<50")){
					score += constant_2 * Math.pow(40,2);
				}
				else if(chromosomeState.getFeatureValue("distanceToNearestBeacon").equals("50-99")){
					score += constant_2 * Math.pow(70,2);
				}
				else if(chromosomeState.getFeatureValue("distanceToNearestBeacon").equals("100-500")){
					score += constant_2 * Math.pow(125,2);
				}
				else{
					score += constant_2 * Math.pow(600,2);
				}
			}
			else if(action.equals("attackEnemyShip")){
				if(chromosomeState.getFeatureValue("moneyOnShip").equals("<50")){
					score += constant_1 * Math.pow(40,2);
				}
				else if(chromosomeState.getFeatureValue("moneyOnShip").equals("50-99")){
					score += constant_1 * Math.pow(70,2);
				}
				else if(chromosomeState.getFeatureValue("moneyOnShip").equals("100-500")){
					score += constant_1 * Math.pow(125,2);
				}
				else{
					score += constant_1 * Math.pow(600,2);
				}	
			}
			else if (action.equals("attackEnemyBase")){
				
				
			}
			else if (action.equals("goToAsteroid")){
				if(chromosomeState.getFeatureValue("moneyNearestAsteroid").equals("<50")){
					score += constant_1 * Math.pow(20,2);
				}
				else if(chromosomeState.getFeatureValue("moneyNearestAsteroid").equals("50-99")){
					score += constant_1 * Math.pow(35,2);
				}
				else if(chromosomeState.getFeatureValue("moneyNearestAsteroid").equals("100-500")){
					score += constant_1 * Math.pow(65,2);
				}
				else{
					score += constant_1 * Math.pow(300,2);
				}				
			}
		} // end of for loop
		
		return score;
	}
	*/
	
	/**
	 * This function evaluates the fitness of a chromosome.
	 * @param individual
	 * @return
	 */
	public double fitnessValue(Chromosome individual) {
		double score = 0.0;
		final double constant_1 = 0.7;
		final double constant_2 = 0.3;
		
		return population.getFitnessOf(individual);	
	}
}
