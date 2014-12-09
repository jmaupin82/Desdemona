

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/** 
 * This class is a container for all individuals in the
 * GA algorithm.
 *
 */
public class Population implements Serializable, Iterable<BigChromosome> {

	private static final long serialVersionUID = 1L;

	/** This holds the current population of BigChromosomes. */
	private HashSet<BigChromosome> currentPopulation;

	/** This map tells the fitness of each BigChromosome */
	private HashMap<BigChromosome, Integer> populationFitness;

	public int generation;

	/**
	 * This constructor is used to set a population in which
	// no BigChromosome has been evaluated yet.
	 * 
	 * @param currentPopulation
	 */
	public Population(HashSet<BigChromosome> currentPopulation){

		this.currentPopulation = currentPopulation;
		this.populationFitness = new HashMap<BigChromosome,Integer>();

		// Set the fitness to be 0 for all of them
		for(BigChromosome BigChromosome : currentPopulation){
			populationFitness.put(BigChromosome, 0);
		}
	}

	/**
	 * 
	 * @param currentPopulation
	 * @param nonEvaluatedBigChromosomes
	 * @param fitnessOfBigChromosomes
	 */
	public Population(HashSet<BigChromosome> currentPopulation, 
			HashSet<BigChromosome> nonEvaluatedBigChromosomes,
			HashMap<BigChromosome, Integer> fitnessOfBigChromosomes){

		this.currentPopulation = currentPopulation;
		this.populationFitness = fitnessOfBigChromosomes;
	}

	/**
	 * Returns how many BigChromosomes are there in the population
	 * 
	 * @return
	 */
	public int size() {
		return currentPopulation.size();
	}
	
	public int getGeneration() {
		return generation;
	}
	
	public Iterator<BigChromosome> iterator() {
		return currentPopulation.iterator();
	}

	
	public void setChromosome(HashSet<BigChromosome> next){
		this.currentPopulation = next;
	}
	/**
	 * This function is used to calculate the fitness of one BigChromosome in terms
	 * of the money earned in one game.
	 * 
	 * @param individual
	 * @param moneyValue
	 */
	public void setFitnessOf(BigChromosome individual, int moneyValue ){
		this.populationFitness.put(individual, moneyValue);
	}

	/** Getters and setters */
	public HashSet<BigChromosome> getSetOfIndividuals(){
		return currentPopulation;
	}

	/**
	 * This is a getter method.
	 * @return
	 */
	public HashMap<BigChromosome, Integer> getFitnessOfAllIndividuals(){
		return populationFitness;
	}

	/**
	 * Returns the fitness value of a BigChromosome.
	 * 
	 * @param individual
	 * @return
	 */
	public int getFitnessValue(BigChromosome individual){
		return getFitnessOfAllIndividuals().get(individual);
	}

	/**
	 * This method is used to fill the attribute with the map
	 * of fitness.
	 * 
	 * @return
	 */
	public void evaluateWholePopulation() {
		
		// This is the number of plies for the minimax
		final int numPlies = Constants.numPliesIago;
		final int populationSize = this.size();

		// We insert all the BigChromosomes into the map, with a fitness of 0.
		for(BigChromosome individual: currentPopulation) {
			populationFitness.put(individual, 0);
		}
		
		// Correct this check
		if(populationSize % 2 != 0) {
			System.err.println("[ERROR] The population size must be a multiple of 2");
		}
		
		// Convert the set into an array
		ArrayList<BigChromosome> populationArray = 
				new ArrayList<BigChromosome>(currentPopulation);
		
		int numGames = 0;
		// This loop implements the tournament
		for(int step = 1; step < populationSize; step*=2) {
			for(int i = 0; i+step < populationSize; i+= 2 * step) {				
				//System.out.println("Playing game " + numGames);
				int indexWhite = i;
				int indexBlack = i + step;
				
				// Retrieve the BigChromosomes from the population
				BigChromosome chromoWhite = populationArray.get(indexWhite);
				BigChromosome chromoBlack = populationArray.get(indexBlack);
				
				// Instantiate the minimax players from the BigChromosomes
//				Player playerWhite = 
//						new MinimaxPlayer("whiteMini", Constants.WHITE, numPlies,
//						chromoWhite.getStabilityCoef(), chromoWhite.getParityCoef(),
//						chromoWhite.getMobilityCoef(), chromoWhite.getDifferentialCoef());
				Player playerWhite = new MinimaxPlayerTrial("whiteMini", Constants.WHITE, numPlies, (BigChromosome)chromoWhite);
//				Player playerBlack =
//						new MinimaxPlayer("blackMini", Constants.BLACK, numPlies,
//								chromoBlack.getStabilityCoef(), chromoBlack.getParityCoef(),
//								chromoBlack.getMobilityCoef(), chromoBlack.getDifferentialCoef());
				Player playerBlack = new RandomPlayer("Random", Constants.BLACK);
				
				Driver driver = new Driver(playerWhite, playerBlack);
			
				// Start the game and get the final disk differential
				int diskDifferential = driver.startGame();
				
				// Update the tournament score array.
				if(diskDifferential > 0) {
					// If white wins increment its score by 1.
					int playerWhiteOldFitness = populationFitness.get(chromoWhite);
					populationFitness.put(chromoWhite, playerWhiteOldFitness+1);
					
					// Place the winner in the white index
					populationArray.add(indexWhite, chromoWhite);
				} 
				else if(diskDifferential < 0) {
					// If black wins increment its score by 1.
					int playerBlackOldFitness = populationFitness.get(chromoBlack);
					populationFitness.put(chromoWhite, playerBlackOldFitness+1);
					
					// Place the winner in the white index
					populationArray.add(indexWhite, chromoBlack);
				}
				numGames++;
			}	
		}
		
		// At this point populationScores has the scores of the BigChromosomes
		// stored in populationArray.
	}
	
	/**
	 * Find the individual with the highest fitness score in a population.
	 * 
	 * @return
	 */
	public BigChromosome findMostFitIndividual() {
		double bestFitnessScore = Double.NEGATIVE_INFINITY;
		BigChromosome mostFitIndividual = null;

		// Loop through all the BigChromosomes seaching for the one.
		for(BigChromosome individual: this.getSetOfIndividuals()) {
			double individualFitness = populationFitness.get(individual);

			if(individualFitness > bestFitnessScore) {
				bestFitnessScore = individualFitness;
				mostFitIndividual = individual;
			}
		}
		return mostFitIndividual;
	}
	public double calculateAverageFitness(){
		double average = 0.0;
		for(BigChromosome individual : this.getSetOfIndividuals()){
			average += this.populationFitness.get(individual);
		}
		average = average / this.populationFitness.size();
		return average;
	}
	/**
	 * This method finds the best individual and plays against random.
	 * 
	 * @param numPlies
	 * @return
	 */
	public int testPopulation(int numPlies) {
		// Retrieve the most fit individual
		BigChromosome chromoMostFit = this.findMostFitIndividual();
		
		// Instantiate the minimax players from the BigChromosomes
		Player playerWhite = new MinimaxPlayerTrial("whiteMini", Constants.WHITE, numPlies, (BigChromosome) chromoMostFit);

		
		Player playerBlack =
				new RandomPlayer("blackRandom", Constants.BLACK);
		
		Driver driver = new Driver(playerWhite, playerBlack);
		
		// Start the game and get the final disk differential
		int diskDifferential = driver.startGame();
		
		return diskDifferential;
	}
	
	public BigChromosome getRandomIndividual(){
		Random r = new Random();
		int element = r.nextInt(this.currentPopulation.size());
		int i = 0;
		for(BigChromosome b : this.currentPopulation){
			if(i == element){
				return b;
			}
			i++;
		}
		return this.findMostFitIndividual();
	}
}
