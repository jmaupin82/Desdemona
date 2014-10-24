

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * This class implements a genetic algorithm that performs
 * stochastic search over a space.
 * 
 *
 */
public class GeneticAlgorithm {
	// The size of a population.
	private final int populationSize;

	// An object whose class implements a fitness function.
	private final FitnessFunction fitnessFunction;

	// Ratio of genes that are mutated.
	private final double mutationRatio;


	/**
	 * 
	 * @param populationSize
	 * @param fitnessFunction
	 * @param mutationRatio
	 */
	public GeneticAlgorithm(int populationSize, FitnessFunction fitnessFunction, 
			double mutationRatio) {
		this.populationSize  = populationSize;
		this.fitnessFunction = fitnessFunction;
		this.mutationRatio   = mutationRatio;	
	}

	/**
	 * The proper genetic algorithm.
	 * 
	 * @param maxSteps
	 *           An upper limit in the number of steps.
	 * @return
	 */
	public Chromosome run(int maxGenerations, double fitnessGoal) {
		int numGenerations = 0;

		// Create the initial population.
		HashSet<Chromosome> population = new HashSet<Chromosome>();

		for(int i=0; i< populationSize; i++) {
			population.add( Chromosome.getRandomChromosome() );
		}

		// Evaluate initial population.
		Chromosome mostFitIndividual = findMostFitIndividual(population);
		double highestFitnessScore   = fitnessFunction.fitnessValue(mostFitIndividual);

		// We find successive generations until we find an individual that is fit
		// enough or until we reach the pre-specified upper bound for the number
		// of generations.
		while(highestFitnessScore >= fitnessGoal  || 
				numGenerations < maxGenerations ) {

			HashSet<Chromosome> newPopulation = new HashSet<Chromosome>();

			for(int i=0; i < populationSize; i++){

				// We select to individuals for reproduction.
				Chromosome parentA = select(population); 
				Chromosome parentB = select(population); 

				// Reproduce the individuals.
				Chromosome child = reproduce(parentA, parentB);

				// Mutate the child with a small probability.
				child = mutate(child, mutationRatio);

				// Add the child to the new population.
				newPopulation.add(child);
			}

			// Re-evaluate population
			mostFitIndividual   = findMostFitIndividual(newPopulation);
			highestFitnessScore = fitnessFunction.fitnessValue(mostFitIndividual);

			numGenerations++;
		} // End of while loop over generations

		return mostFitIndividual;
	}

	/**
	 * This is only a one step generation generation.
	 * 
	 * @param population
	 * 
	 * @return
	 *
	public HashSet<Chromosome> generateOneGeneration(HashSet<Chromosome> population) {

		// Evaluate initial population.
		Chromosome mostFitIndividual = findMostFitIndividual(population);
		double highestFitnessScore   = fitnessFunction.fitnessValue(mostFitIndividual);

		// We find successive generations until we find an individual that is fit
		// enough or until we reach the pre-specified upper bound for the number
		// of generations.

		HashSet<Chromosome> newPopulation = new HashSet<Chromosome>();

		for(int i=0; i < populationSize; i++){

			// We select to individuals for reproduction.
			Chromosome parentA = select(population); 
			Chromosome parentB = select(population); 

			// Reproduce the individuals.
			Chromosome child = reproduce(parentA, parentB);

			// Mutate the child with a small probability.
			child = mutate(child, mutationRatio);

			// Add the child to the new population.
			newPopulation.add(child);
		}

		// Re-evaluate population
		mostFitIndividual   = findMostFitIndividual(newPopulation);
		highestFitnessScore = fitnessFunction.fitnessValue(mostFitIndividual);

		return newPopulation;
	}
	 */

	/**
	 * This is only a one step generation generation.
	 * 
	 * @param population
	 * 
	 * @return
	 */
	public Population generateOneGeneration(Population population) {

		// Evaluate initial population.
		Chromosome mostFitIndividual = findMostFitIndividual(population.getSetOfIndividuals());
		double highestFitnessScore   = fitnessFunction.fitnessValue(mostFitIndividual);

		// We find successive generations until we find an individual that is fit
		// enough or until we reach the pre-specified upper bound for the number
		// of generations.

		HashSet<Chromosome> newPopulation = new HashSet<Chromosome>();

		for(int i=0; i < populationSize; i++){

			// We select to individuals for reproduction.
			Chromosome parentA = select(population.getSetOfIndividuals()); 
			Chromosome parentB = select(population.getSetOfIndividuals()); 

			// Reproduce the individuals.
			Chromosome child = reproduce(parentA, parentB);

			// Mutate the child with a small probability.
			child = mutate(child, mutationRatio);

			// Add the child to the new population.
			newPopulation.add(child);
		}

		// Re-evaluate population
		mostFitIndividual   = findMostFitIndividual(newPopulation);
		highestFitnessScore = fitnessFunction.fitnessValue(mostFitIndividual);

		return new Population(newPopulation, population.getNonEvaluatedIndividuals(),
				population.getFitnessOfAllIndividuals());
	}
	/**
	 * This function selects an individual from a population.
	 * Our selection is NOT UNIFORMLY RANDOM.
	 * 
	 * This implements proportional fitness selection.
	 * 
	 * @return The selected individual.
	 */
	private Chromosome select(HashSet<Chromosome> population){
		double sumAllFitnessValues = 0.0;

		// We convert the hashset to an array.
		Chromosome[] populationArray = new Chromosome[population.size()];
		Double[] fitnessPartialSums  = new Double[population.size()];

		int index = 0;

		// Fill the chromosome array and find the overall sum of fitness values.
		for(Chromosome individual: population) {		

			populationArray[index] = individual;

			// Update the global sum of fitness values.
			sumAllFitnessValues += fitnessFunction.fitnessValue(individual);

			index++;
		}

		// Calculate the probabilities of selecting each of the individuals.
		// (Parallel sum).
		for(int i = 0; i < populationArray.length; i++) {

			double individualFitness = fitnessFunction.fitnessValue(populationArray[i]);

			if(i == 0) {
				fitnessPartialSums[i] = individualFitness / sumAllFitnessValues;
			}
			else {
				fitnessPartialSums[i] = (fitnessPartialSums[i-1] + individualFitness)/ sumAllFitnessValues;
			}
		}

		// Get a random number.
		Random randomGen = new Random();
		Double randomNumber = randomGen.nextDouble();

		// Find whose interval does this random number correspond to.
		// This uses linear search.
		for(int i = 0; i < population.size(); i++) {
			if (randomNumber <= fitnessPartialSums[i] ){
				return populationArray[i];
			}
		}

		// This is non-reachable code. But necessary for Java.
		return populationArray[populationArray.length-1];
	}

	/**
	 * Takes to cromosomes and reproduces them.
	 * 
	 * @param parentA
	 * @param parentB
	 * @return
	 */
	private Chromosome reproduce(Chromosome parentA, Chromosome parentB){
		return Chromosome.reproduce(parentA, parentB);
	}

	private Chromosome mutate(Chromosome individual, double mutationRatio){
		return Chromosome.mutate(individual, mutationRatio);
	}

	/**
	 * Find the highest fitness score in a population.
	 * 
	 * @param population
	 * @return
	 */
	private double evaluatePopulation(HashSet<Chromosome> population) {
		double bestFitnessScore = Double.NEGATIVE_INFINITY;

		for(Chromosome individual: population) {
			double individualFitness = fitnessFunction.fitnessValue(individual);

			if(individualFitness > bestFitnessScore) {
				bestFitnessScore = individualFitness;
			}
		}
		return bestFitnessScore;
	}

	/**
	 * Find the individual with the highest fitness score in a population.
	 * 
	 * @param population
	 * @return
	 */
	public Chromosome findMostFitIndividual(HashSet<Chromosome> population) {
		double bestFitnessScore = Double.NEGATIVE_INFINITY;
		Chromosome mostFitIndividual = null;

		for(Chromosome individual: population) {
			double individualFitness = fitnessFunction.fitnessValue(individual);

			if(individualFitness > bestFitnessScore) {
				bestFitnessScore = individualFitness;
				mostFitIndividual = individual;
			}
		}
		return mostFitIndividual;
	}

	public int[] evaluateWholePopulation(HashSet<Chromosome> population) {
		// This is the number of plies for the minimax
		final int numPlies = 3;
		final int populationSize = population.size();

		// Correct this check
		if(populationSize % 2 != 0) {
			System.err.println("[ERROR] The population size must be a power of 2");
		}
		
		// Convert the set into an array
		ArrayList<Chromosome> populationArray = 
				new ArrayList<Chromosome>(population);

		// This is the array where we keep the scores for the population.
		int[] populationScores = new int[populationSize];
		
		for(int step = 1; step < populationSize; step*=2) {
			for(int i = 0; i+step < populationSize; i+= 2 * step) {				
				
				int indexWhite = i;
				int indexBlack = i + step;
				
				// Retrieve the chromosomes from the population
				Chromosome chromoWhite = populationArray.get(indexWhite);
				Chromosome chromoBlack = populationArray.get(indexBlack);
				
				// Instantiate the minimax players from the chromosomes
				Player playerWhite = 
						new MinimaxPlayer("whiteMini", Constants.WHITE, numPlies,
						chromoWhite.getStabilityCoef(), chromoWhite.getParityCoef(),
						chromoWhite.getMobilityCoef(), chromoWhite.getDifferentialCoef());
				
				Player playerBlack =
						new MinimaxPlayer("blackMini", Constants.BLACK, numPlies,
								chromoBlack.getStabilityCoef(), chromoBlack.getParityCoef(),
								chromoBlack.getMobilityCoef(), chromoBlack.getDifferentialCoef());
				
				Driver driver = new Driver(playerWhite, playerBlack);
			
				// Start the game and get the final disk differential
				int diskDifferential = driver.startGame();
				
				// Update the tournament score array.
				if(diskDifferential > 0) {
					// If white wins
					populationScores[indexWhite] += 1;
					
					// Place the winner in the white index
					populationArray.add(indexWhite, chromoWhite);
				} 
				else if(diskDifferential < 0) {
					// If black wins
					populationScores[indexBlack] +=1;
					
					// Place the winner in the white index
					populationArray.add(indexWhite, chromoBlack);
				}
			}	
		}
		
		// At this point populationScores has the scores of the chromosomes
		// stored in populationArray.
		
		return populationScores;
	}
}
