
import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;

/**
 * This class implements a chromosome, which is a collection of coefficients
 * of a linear combination of Othello heuristics.
 *
 */
public class Chromosome implements Serializable {

	private static final long serialVersionUID = 2904572892942180307L;

	/** The coefficient multiplying stability */
	private double stabilityCoef;
	/** The coefficient multiplying parity */
	private double parityCoef;
	/** The coefficient multiplying mobility */
	private double mobilityCoef;
	/** The coefficient multiplying the difference between the number
	 * of white and black stones */
	private double differentialCoef;
	
	/** The total number of genes above, which is 4*/
	private static final int numGenes = 4;
	
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

	public void setStabilityCoef(double stabilityCoef) {
		this.stabilityCoef = stabilityCoef;
	}

	public void setParityCoef(double parityCoef) {
		this.parityCoef = parityCoef;
	}

	public void setMobilityCoef(double mobilityCoef) {
		this.mobilityCoef = mobilityCoef;
	}

	public void setDifferentialCoef(double differentialCoef) {
		this.differentialCoef = differentialCoef;
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
		return null;
		
//		return new Population(population);
	}
	
	/**
	 * Returns how many genes the chromosome has.
	 * @return
	 */
	public int getNumGenes() {
		return Chromosome.numGenes;
	}
	
	/**
	 * Takes two individuals and performs crossover. We perform uniform 
	 * crossover.
	 * 
	 * @param parentA
	 * @param parentB
	 * @return
	 */
	public static Chromosome reproduce(Chromosome parentA, Chromosome parentB){
		// This will be the child chromosome.
		Chromosome child = new Chromosome();
		
		Random randGen = new Random();
		
		// Iterate through all possible states and randomly choose from which
		// parent to inherit a certain gene.
		for(int geneIndex = 0; geneIndex < Chromosome.numGenes; geneIndex++){
			int randInt = randGen.nextInt(2);
			
			if(randInt == 0){
				// Take gene from parent A.
				child.setGeneByIndex(geneIndex, parentA.getGeneByIndex(geneIndex));
			}
			else {
				// Take gene from parent B.
				child.setGeneByIndex(geneIndex, parentB.getGeneByIndex(geneIndex));
			}
		}
		
		return child;
	}
	
	/**
	 * Mutates mutationRatio genes of an individual.
	 * @param individual
	 * @param mutationRatio
	 *            The fraction of the genes that are randomly mutated.
	 * @return
	 */
	public static Chromosome mutate(Chromosome individual, double mutationRatio){
		int numGenesToMutate = (int) (mutationRatio * Chromosome.numGenes);
		Random randGen = new Random();
		
		for(int i = 0; i < numGenesToMutate; i++){
			// Choose a random gene.
			int randomGeneIndex = randGen.nextInt(Chromosome.numGenes);
			
			// Add some random gaussian noise to that gene.
			individual.setGeneByIndex(randomGeneIndex, 
					individual.getGeneByIndex(randomGeneIndex) + randGen.nextGaussian());
		}

		return individual;
	}
	
	/**
	 * Given an index, this returns the corresponding gene.
	 * 
	 * @param index
	 * @return
	 */
	private double getGeneByIndex(int index) {
		switch(index) {
		case 0:
			return this.getStabilityCoef();	
		case 1:
			return this.getParityCoef();
		case 2:
			return this.getMobilityCoef();
		case 3:
			return this.getDifferentialCoef();
		default:
			// This should be unreachable.
			System.err.println("[ERROR] No gene get by index");
			return this.getStabilityCoef();
		}
	}
	
	/**
	 * Given an index, this sets the corresponding gene.
	 * 
	 * @param index
	 * @return
	 */
	private void setGeneByIndex(int index, double value) {
		switch(index) {
		case 0:
			this.setStabilityCoef(value);	
			break;
		case 1:
			this.setParityCoef(value);
			break;
		case 2:
			this.setMobilityCoef(value);
			break;
		case 3:
			this.setDifferentialCoef(value);
			break;
		default:
			// This should be unreachable.
			System.err.println("[ERROR] No gene set by index");
			return;
		}
	}
}
