
public class TrainIagoMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final int populationSize     = 100;
		final int maxGenerations     = 100;
		final int gensBetweenBackups = 1;
		final double mutationRatio   = 0.05;
		final double crossoverRatio = 0.8;
		final String populationInputFilename  = "";
		final String populationOutputFilename = "IagoPopulation.txt";
		
		// Instantiate the GA
		GeneticAlgorithm ga = new GeneticAlgorithm(populationSize, mutationRatio, crossoverRatio);
		
		// Run the GA
		ga.run(populationInputFilename, maxGenerations, gensBetweenBackups);
	}

}
