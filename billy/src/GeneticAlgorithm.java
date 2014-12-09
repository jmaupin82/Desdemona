

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

/**
 * This class implements a genetic algorithm that performs
 * stochastic search over a space.
 * 
 *
 */
public final class GeneticAlgorithm {
	// The size of a population.
	private final int populationSize;

	// Ratio of genes that are mutated.
	private final double mutationRatio;

	private final double crossoverRatio;

	/**
	 * 
	 * @param populationSize
	 * @param mutationRatio
	 */
	public GeneticAlgorithm(int populationSize,
			double mutationRatio, double crossoverRatio) {
		this.populationSize  = populationSize;
		this.mutationRatio   = mutationRatio;	
		this.crossoverRatio = crossoverRatio;
	}

	/**
	 * The proper genetic algorithm.
	 * 
	 * @param populationFilename
	 *           The name of the file from which to read the initial population
	 * @param maxGenerations
	 *           An upper limit in the number of generations.
	 * @param gensBetweenBackups
	 *           The number of generations between backups.
	 * @return
	 */
	public BigChromosome run(String populationInputFilename, int maxGenerations,
			int gensBetweenBackups) {
		// The number of generations between printing the number
		// of generations.
		final int gensBetweenPrints = 1;

		// The name of the file to which we write the population
		final String populationOutputFilename = "IagoPopulation.txt";

		int numGenerations = 0;

		Population population;

		// If no input filename is given then create a random population.
		if(populationInputFilename.equals("")) {
			// Create the initial population.
			HashSet<BigChromosome> populationSet = new HashSet<BigChromosome>();

			for(int i=0; i < populationSize; i++) {
				populationSet.add( BigChromosome.getRandomChromosome() );
			}

			population = new Population(populationSet);

			// Evaluate initial population.
			population.evaluateWholePopulation();
		}
		else {
			// Else read it from the file.
			population = readPopulationFromFile(populationInputFilename);
			numGenerations = population.getGeneration();
			
			System.out.println("[INFO] REad population from file of size" +population.size());
		}

		// We find successive generations until we find an individual that is fit
		// enough or until we reach the pre-specified upper bound for the number
		// of generations.
		while(numGenerations < maxGenerations) {
			// If it is time to print the number of generations
			if(numGenerations % gensBetweenPrints == 0) {
				System.out.println("[INFO] Generation number " + numGenerations + ", " + new Date());
			}

			// If it is time to backup the population to disk
			if(numGenerations % gensBetweenBackups == 0 && numGenerations>0) {

				//writePopulationToFile(numGenerations + populationOutputFilename, population);
				
				//System.out.println("Population written now starting the test");
				// Test the best individual
				double diff = population.calculateAverageFitness();
				//System.out.println("Test done now writing performance");
				writePopulationPerformanceToFile("iagoPerformance10.txt", diff, numGenerations);
				System.out.println("Performance written");
			}

			System.out.println("[INFO] Starting population " + numGenerations);
			HashSet<BigChromosome> newPopulation = new HashSet<BigChromosome>();

			for(int i=0; i < populationSize - 1; i++){
				
				// We select two individuals for reproduction.
				BigChromosome parentA = select(population); 
				BigChromosome parentB = select(population); 

				// Reproduce the individuals.
				BigChromosome child = reproduce(parentA, parentB);

				// Mutate the child with a small probability.
				child = mutate(child, mutationRatio);

				// Add the child to the new population.
				newPopulation.add(child);
			}
			System.out.println("New population obtained");
			//add the best individual to the next generation
			newPopulation.add(population.findMostFitIndividual());
			population.setChromosome(newPopulation);
			// Evaluate the population for the next round.
			population.evaluateWholePopulation();
			
			numGenerations++;
		} // End of while loop over generations

		// Evaluate the population one more time, to return the most fit.
		population.evaluateWholePopulation();

		// Find the best individual
		BigChromosome mostFitIndividual = population.findMostFitIndividual();

		return mostFitIndividual;
	}

	/**
	 * This function selects an individual from a population.
	 * Our selection is NOT UNIFORMLY RANDOM.
	 * 
	 * This implements proportional fitness selection.
	 * 
	 * @return The selected individual.
	 */
	private BigChromosome select(Population population){
		double sumAllFitnessValues = 0.0;

		// We convert the hashset to an array.
		BigChromosome[] populationArray = new BigChromosome[population.size()];
		Double[] fitnessPartialSums  = new Double[population.size()];

		int index = 0;

		// Fill the chromosome array and find the overall sum of fitness values.
		for(BigChromosome individual: population) {		

			populationArray[index] = individual;

			// Update the global sum of fitness values.
			sumAllFitnessValues += population.getFitnessValue(individual);

			index++;
		}

		// Calculate the probabilities of selecting each of the individuals.
		// (Parallel sum).
		for(int i = 0; i < populationArray.length; i++) {

			double individualFitness = population.getFitnessValue(populationArray[i]);

			if(i == 0) {
				fitnessPartialSums[i] = individualFitness / sumAllFitnessValues;
			}
			else {
				
				// THIS HAD A PARENTHESIS THAT LOOKED LIKE A BUG!!!!!!!!!!!!!!!!
				fitnessPartialSums[i] = 
						fitnessPartialSums[i-1] + (individualFitness / sumAllFitnessValues);
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
	 * Takes two chromosomes and reproduces them.
	 * 
	 * @param parentA
	 * @param parentB
	 * @return
	 */
	private BigChromosome reproduce(BigChromosome parentA, BigChromosome parentB){
		Random rand = new Random();
		if(rand.nextDouble() > this.crossoverRatio){
			return parentA;
		}
		else{
			return BigChromosome.reproduce(parentA, parentB);
		}
	}

	private BigChromosome mutate(BigChromosome individual, double mutationRatio){
		return BigChromosome.mutate(individual, mutationRatio);
	}

	/**
	 * This method read the population from a file.
	 * 
	 * @param filename
	 * @return
	 */
	public Population readPopulationFromFile(String filename){
		try{
			FileInputStream fileInputStream = 
					new FileInputStream(filename);

			ObjectInputStream objectInputStream = 
					new ObjectInputStream(fileInputStream);

			Population population = (Population) objectInputStream.readObject();

			objectInputStream.close();
			return population;
		}
		catch(Exception exc) {
			System.out.println("returning null");
			return null;
		}
	}

	/**
	 * This method writes to the file named 'filename' a 
	 * serialized population.
	 * 
	 * @param filename
	 * @return
	 */
	private void writePopulationToFile(String filename, Population population) {
		try{
			System.out.println("[INFO] Writing population to file");
			FileOutputStream fileOutputStream = 
					new FileOutputStream(filename);

			ObjectOutputStream objectOutputStream = 
					new ObjectOutputStream(fileOutputStream);

			objectOutputStream.writeObject(population);

			objectOutputStream.close();
			System.out.println("[INFO] Population file successfully updated");
		}
		catch(Exception exc) {
			exc.printStackTrace();
		}
	}

	private void writePopulationPerformanceToFile(String filename, double diskDifferential, int generation) {
		try{
			File file = new File(filename);

			BufferedWriter output = new BufferedWriter(new FileWriter(file,true));
			output.write(diskDifferential + ", Generation: " +  generation + " time: " + new Date() + "\n");			

			output.close();
			System.out.println("[INFO] Population performance of " + diskDifferential);
		}
		catch(Exception exc) {
			exc.printStackTrace();
		}
	}
}
