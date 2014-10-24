

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

/** 
 * This class is a container for all individuals in the
 * GA algorithm.
 *
 */
public class Population implements Serializable {
	/** This holds the current population of chromosomes. */
	HashSet<Chromosome> currentPopulation;

	/** This holds the chromosomes that have not been evaluated */
	HashSet<Chromosome> nonEvaluatedIndividuals;

	HashMap<Chromosome, Integer> fitnessOfIndividuals;


	/**
	 * This constructor is used to set a population in which
	// no chromosome has been evaluated yet.
	 * 
	 * @param currentPopulation
	 */
	public Population(HashSet<Chromosome> currentPopulation){

		this.currentPopulation       = currentPopulation;
		this.nonEvaluatedIndividuals = new HashSet<Chromosome>();
		this.fitnessOfIndividuals    = new HashMap<Chromosome,Integer>();

		for(Chromosome chromosome : currentPopulation){
			nonEvaluatedIndividuals.add(chromosome);
			fitnessOfIndividuals.put(chromosome, 0);
		}
	}

	/**
	 * 
	 * @param currentPopulation
	 * @param nonEvaluatedChromosomes
	 * @param fitnessOfChromosomes
	 */
	public Population(HashSet<Chromosome> currentPopulation, 
			HashSet<Chromosome> nonEvaluatedChromosomes,
			HashMap<Chromosome, Integer> fitnessOfChromosomes){

		this.currentPopulation       = currentPopulation;
		this.nonEvaluatedIndividuals = nonEvaluatedChromosomes;
		this.fitnessOfIndividuals    = fitnessOfChromosomes;
	}

	public boolean areThereNonEvaluatedChromosomes(){
		return nonEvaluatedIndividuals.size() > 0;
	}

	/**
	 * This method is used to select an individual that hasn't been evaluated.
	 * 
	 * @return
	 */
	public Chromosome selectNonEvaluatedIndividual(){
		HashSet<Chromosome> nonEvaluatedIndividuals = getNonEvaluatedIndividuals();

		for(Chromosome chr : nonEvaluatedIndividuals){
			return chr;
		}
		return null;
	}

	/**
	 * This function is used to calculate the fitness of one chromosome in terms
	 * of the money earned in one game.
	 * 
	 * @param individual
	 * @param moneyValue
	 */
	public void setFitnessOf(Chromosome individual, int moneyValue ){
		this.fitnessOfIndividuals.put(individual, moneyValue);
	}

	/** Getters and setters */
	public HashSet<Chromosome> getSetOfIndividuals(){
		return currentPopulation;
	}

	public HashSet<Chromosome> getNonEvaluatedIndividuals(){
		return nonEvaluatedIndividuals;
	}

	/**
	 * This is a getter method.
	 * @return
	 */
	public HashMap<Chromosome, Integer> getFitnessOfAllIndividuals(){
		return fitnessOfIndividuals;
	}

	public int getFitnessOf(Chromosome individual){
		return getFitnessOfAllIndividuals().get(individual);
	}

	/**
	 * Find the individual with the highest fitness score in a population.
	 * 
	 * @return
	 */
	public Chromosome findMostFitIndividual() {
		double bestFitnessScore = Double.NEGATIVE_INFINITY;
		Chromosome mostFitIndividual = null;

		for(Chromosome individual: this.getSetOfIndividuals()) {
			double individualFitness = fitnessOfIndividuals.get(individual);

			if(individualFitness > bestFitnessScore) {
				bestFitnessScore = individualFitness;
				mostFitIndividual = individual;
			}
		}
		return mostFitIndividual;
	}
}
