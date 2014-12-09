import java.io.FileWriter;
import java.io.IOException;


public class IagoVsDesdemona {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final int numGames = 31;
		final int numPlies = 2;
		final double gamma = 0.8;
		final double lambda= 0.2;
		final double alpha = 0.2;
		final int numOutput = 1;
		final int numHidden = Constants.SIZE;
		final int numInput  = Constants.SIZE * Constants.SIZE + 2;
		double[][] hiddenLayerWeights = new double[numHidden][numInput];
		double[][] outputLayerWeights = new double[numOutput][numHidden];
		final String filename = "bestIago.txt";
		try{
			FileWriter output = new FileWriter("iagoVSHuman.txt");

			for(int i = 0; i < numGames; i++) {
				double averageScore = 0.0;
				for(int j =0; j< 1; ++j){

					GeneticAlgorithm ga = new GeneticAlgorithm(0,
							0, 0);

					Population population = ga.readPopulationFromFile(filename);
					BigChromosome chromoWhite= population.findMostFitIndividual();

					// Iago
					Player playerWhite = 
							new MinimaxPlayerTrial("whiteMini", Constants.WHITE, numPlies,
									chromoWhite);

//					Player desdemonaBlack = new DesdemonaPlayer("randomBlack", Constants.BLACK, numPlies, gamma, lambda, alpha,
//							hiddenLayerWeights, outputLayerWeights);
					Player desdemonaBlack = new HumanPlayer("human", Constants.BLACK);

					Driver driver = new Driver(playerWhite, desdemonaBlack);
					int score = driver.startGame();
					averageScore += score;
					System.out.println("Game " + i + " score: " + score);
					if(i %100 == 0 && i>99) {
						System.out.println(i);
					}
				}
				
				//averageScore = averageScore / 20.0;
				System.out.println("Iago average Score " + Double.toString(averageScore));
				output.write(Double.toString(averageScore) + "," +  Integer.toString(i) + ", Game: " + Integer.toString(i) + "\n");
				output.flush();
			}
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		//		Billy b = new Billy();
		//		b.go(args);

	}

}
