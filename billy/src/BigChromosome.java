import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;


public class BigChromosome implements Serializable{

	private static final long serialVersionUID = 2904572892942180307L;

	/** Double matrix of position weights */
	private double[][] weights;

	/** The total number of genes above, which is 100*/
	private static final int numGenes = 100;

	/**
	 * This constructor creates a random chromosome with coefficients in [-1,1]
	 */
	public BigChromosome() {
		this.weights = new double[10][10];
		Random randGen = new Random();
		for(int i =0; i < 10; ++i){
			for(int j = 0; j < 10; ++j){
				//randomly set the weights between 0 and 100
				weights[i][j] = randGen.nextDouble() * 100;
			}
		}
	}

	/**
	 * Create a new chromosome with given coefficients.
	 * 
	 * @param stabilityCoef
	 * @param parityCoef
	 * @param mobilityCoef
	 * @param differentialCoef
	 */
	public BigChromosome(double[][] coef) {
		this.weights = coef;
	}

	/**
	 * This method returns a random individual.
	 * @return
	 */
	public static BigChromosome getRandomChromosome(){
		BigChromosome individual = new BigChromosome();

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
	 * This method returns a set with a random population of size
	 * populationSize.
	 * 
	 * @param populationSize
	 * @return
	 */
	public static Population getRandomPopulation(int populationSize){
		// Create the initial population.
		HashSet<BigChromosome> population = new HashSet<BigChromosome>();

		for(int i=0; i< populationSize; i++) {
			if(i % 10 == 0){
				population.add(BigChromosome.getRandomChromosome());
			}
			else{
				population.add( BigChromosome.getRandomChromosome() );
			}
		}

		return new Population(population);
	}

	/**
	 * Returns how many genes the chromosome has.
	 * @return
	 */
	public int getNumGenes() {
		return BigChromosome.numGenes;
	}

	
	public static BigChromosome getJoelsChromosome(){
		double[][] weights = {{50.0, 10, 20, 20, 20, 20, 20, 20, 10, 50 },
							  {10, 8, 4, 4, 4, 4, 4, 4, 8, 10}, 
							  {10, 8, 4, 4, 4, 4, 4, 4, 8, 10},
							  {10, 8, 4, 4, 4, 4, 4, 4, 8, 10},
							  {10, 8, 4, 4, 4, 4, 4, 4, 8, 10},
							  {10, 8, 4, 4, 4, 4, 4, 4, 8, 10},
							  {10, 8, 4, 4, 4, 4, 4, 4, 8, 10},
							  {10, 8, 4, 4, 4, 4, 4, 4, 8, 10},
							  {10, 8, 4, 4, 4, 4, 4, 4, 8, 10},
							  {50.0, 10, 20, 20, 20, 20, 20, 20, 10, 50 }};
		BigChromosome res = new BigChromosome(weights);
		return res;
		
	}
	/**
	 * Takes two individuals and performs crossover. We perform uniform 
	 * crossover.
	 * 
	 * @param parentA
	 * @param parentB
	 * @return
	 */
	public static BigChromosome reproduce(BigChromosome parentA, BigChromosome parentB){
		// This will be the child chromosome.
		BigChromosome child = new BigChromosome();

		Random randGen = new Random();

		// Iterate through all possible states and randomly choose from which
		// parent to inherit a certain gene.
		for(int rowIndex = 0; rowIndex < 10; rowIndex++){
			for(int colIndex = 0; colIndex < 10; ++colIndex){
				int randInt = randGen.nextInt(2);

				if(randInt == 0){
					// Take gene from parent A.
					child.setGeneByIndex(rowIndex,colIndex, parentA.getGeneByIndex(rowIndex, colIndex));
				}
				else {
					// Take gene from parent B.
					child.setGeneByIndex(rowIndex, colIndex, parentB.getGeneByIndex(rowIndex, colIndex));
				}
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
	public static BigChromosome mutate(BigChromosome individual, double mutationRatio){
		int numGenesToMutate = (int) (mutationRatio * BigChromosome.numGenes);
		Random randGen = new Random();

		for(int i = 0; i < numGenesToMutate; i++){
			// Choose a random gene.
			int randomGeneRow = randGen.nextInt(10);
			int randomGeneCol = randGen.nextInt(10);

			// Add some random gaussian noise to that gene.
			individual.setGeneByIndex(randomGeneRow,randomGeneCol, 
					individual.getGeneByIndex(randomGeneRow, randomGeneCol) + randGen.nextGaussian() * 10);
		}

		return individual;
	}

	/**
	 * Given an index, this returns the corresponding gene.
	 * 
	 * @param index
	 * @return
	 */
	public double getGeneByIndex(int row, int col) {
		return this.weights[row][col];
	}

	/**
	 * Given an index, this sets the corresponding gene.
	 * 
	 * @param index
	 * @return
	 */
	private void setGeneByIndex(int row, int col, double value) {
		//prevent the values from ever going above 100 or below 0
		if (value > 100.0){
			value = 100;
		}
		else if(value < 0){
			value = 0;
		}
		this.weights[row][col] = value;
	}
}
