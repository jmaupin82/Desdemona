import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;


public class DesdemonaVsRandom {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final int numGames = 100;
		final int numPlies = 2;
		final double gamma = 0.8;
		final double lambda= 0.00001;
		final double alpha = 0.1;
		final int numOutput = 1;
		final int numHidden = Constants.SIZE;
		final int numInput  = Constants.SIZE * Constants.SIZE + 2;
		double[][] hiddenLayerWeights = new double[numHidden][numInput];
		double[][] outputLayerWeights = new double[numOutput][numHidden];
		try{
			FileWriter output = new FileWriter("desVSradny.txt");

			
			for(int i = 0; i < numGames; i++) {
				int numWon = 0;
				double averageScore = 0;
				for(int j = 0; j< 20; ++j){
					GeneticAlgorithm ga = new GeneticAlgorithm(0,0,0);


					//Population badPop = ga.readPopulationFromFile("1IagoPopulation.txt");

					//BigChromosome chromoBlack = badPop.getRandomIndividual();				
					Player desdemonaWhite = new DesdemonaPlayer("desdemonaWhite", Constants.WHITE, numPlies, gamma, lambda, alpha,
							hiddenLayerWeights, outputLayerWeights, true);

					Player randomBlack = new RandomPlayer("randomBlack", Constants.BLACK);
					//Player randomBlack = new MinimaxPlayerTrial("blackmini", Constants.BLACK, numPlies, chromoBlack );
					Driver driver = new Driver(desdemonaWhite, randomBlack);


					int score = driver.startGame();
					if(score > 0){
						++numWon;
					}
					averageScore += score;

					//System.out.println("Game " + i + " score: " + score);
				}
				averageScore = numWon / 20.0 ;
				output.write(Double.toString(averageScore) + "," +  Integer.toString(i) + ", Game: " + Integer.toString(i) + "\n");

				output.flush();

			}


		} catch ( IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}

}
