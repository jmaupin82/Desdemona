
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Random;

/**
 * This class implements a chromosome, which is a mapping between the set of 
 * all discrete states and actions.
 *
 */
public class Chromosome implements Serializable {

	private double stabilityCoef;
	private double parityCoef;
	private double mobilityCoef;
	private double differentialCoef;
	private int numGenes = 4;
	
	/**
	 * This constructor creates a random chromosome with coefficients in [-1,1]
	 */
	public Chromosome() {
		Random randGen = new Random();
		
		this.stabilityCoef    = randGen.nextDouble() * 2 -1;
		this.parityCoef       = randGen.nextDouble() * 2 -1;
		this.mobilityCoef     = randGen.nextDouble() * 2 -1;
		this.differentialCoef = randGen.nextDouble() * 2 -1;
	}
	
	/**
	 * Create a new chromosome with given coefficients.
	 * 
	 * @param stabilityCoef
	 * @param parityCoef
	 * @param mobilityCoef
	 * @param differentialCoef
	 */
	public Chromosome(double stabilityCoef, double parityCoef, 
			double mobilityCoef, double differentialCoef) {
		this.stabilityCoef    = stabilityCoef;
		this.parityCoef       = parityCoef;
		this.mobilityCoef     = mobilityCoef;
		this.differentialCoef = differentialCoef;
	}
	
	
	/**
	 * This method returns a random individual.
	 * @return
	 */
	public static Chromosome getRandomChromosome(){
		Chromosome individual = new Chromosome();
		
		return individual;
	}
	
	
	/**
	 * This method returns a set with a random population of size
	 * populationSize.
	 * 
	 * @param populationSize
	 * @return
	 *
	public static HashSet<Chromosome> getRandomPopulation(int populationSize){
		// Create the initial population.
		HashSet<Chromosome> population = new HashSet<Chromosome>();
		
		for(int i=0; i< populationSize; i++) {
			population.add( Chromosome.getRandomChromosome() );
		}
		
		return population;
	}
	*/
	
	/**
	 * This method is used to get the action associated with a certain discrete state.
	 * @return
	 */

	
	public double getStabilityCoef() {
		return stabilityCoef;
	}

	public double getParityCoef() {
		return parityCoef;
	}

	public double getMobilityCoef() {
		return mobilityCoef;
	}

	public double getDifferentialCoef() {
		return differentialCoef;
	}

	/**
	 * This method returns a set with a random population of size
	 * populationSize.
	 * 
	 * @param populationSize
	 * @return
	 */
	public static Population getRandomPopulation(int populationSize){
		// Create the initial population.
		HashSet<Chromosome> population = new HashSet<Chromosome>();
		
		for(int i=0; i< populationSize; i++) {
			population.add( Chromosome.getRandomChromosome() );
		}
		
		return new Population(population);
	}
	
	/**
	 * Returns how many genes the chromosome has.
	 * @return
	 */
	public int getNumGenes() {
		return this.numGenes;
	}
	
	
	/**
	 * Takes to individuals and performs crossover. We perform uniform 
	 * crossover.
	 * @param parentA
	 * @param parentB
	 * @return
	 */
	public static Chromosome reproduce(Chromosome parentA, Chromosome parentB){
		LinkedHashMap<ChromosomeState, String> childPolicies = 
				new LinkedHashMap<ChromosomeState, String>();
		
		// Make an arraylist out of the policies for parent A.
		ArrayList<String> actionsParentA = new ArrayList<String>(parentA.getNumGenes());
		
		for(ChromosomeState policyA: parentA.policies.keySet()){	
			actionsParentA.add(parentA.policies.get(policyA));
		}
		
		// Make an array out of the policies for parent B.
		ArrayList<String> actionsParentB = new ArrayList<String>(parentA.getNumGenes());
		
		for(ChromosomeState policyB: parentA.policies.keySet()){
			actionsParentB.add(parentB.policies.get(policyB));		
		}
		
		Random randGen = new Random();
		int nextActionIndex = 0;
		
		// Iterate through all possible states and randomly choose from which
		// parent to inherit a certain gene.
		for(ChromosomeState policyA: parentA.policies.keySet()){
			int randInt = randGen.nextInt(2);
			
			if(randInt == 0){
				// Take gene from parent A.
				childPolicies.put(policyA, actionsParentA.get(nextActionIndex));
			}
			else {
				// Take gene from parent B.
				childPolicies.put(policyA, actionsParentB.get(nextActionIndex));
			}
			nextActionIndex++;
		}
		
		return new Chromosome(childPolicies);
		
	}
	
	/**
	 * Mutates mutationRatio genes of an individual.
	 * @param individual
	 * @param mutationRatio
	 *            The fraction of the genes that are randomly mutated.
	 * @return
	 */
	public static Chromosome mutate(Chromosome individual, double mutationRatio){
		int numGenesToMutate = (int) (mutationRatio * individual.getNumGenes());
		Random randGen = new Random();
		
		// Make an arraylist out of the policies for individual.
		ArrayList<String> actionsIndividual = new ArrayList<String>(individual.getNumGenes());
		
		for(ChromosomeState policy: individual.policies.keySet()){	
			actionsIndividual.add(individual.policies.get(policy));
		}
		
		for(int i = 0; i < numGenesToMutate; i++){
			// Choose a random gene.
			int randomGeneIndex = randGen.nextInt(individual.getNumGenes());
			
			// Replace that gene by a random one.
			actionsIndividual.set(randomGeneIndex, ChromosomeState.getRandomAction());
		}
		
		// Now we assemble the map of policies.
		LinkedHashMap<ChromosomeState, String> newPolicies = 
				new LinkedHashMap<ChromosomeState, String>();
		
		int i = 0;
		for(ChromosomeState policy: individual.policies.keySet()){	
			newPolicies.put(policy, actionsIndividual.get(i));
			i++;
		}
		
		return new Chromosome(newPolicies);
	}
}
