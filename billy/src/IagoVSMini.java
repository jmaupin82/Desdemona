import java.io.FileWriter;
import java.io.IOException;


public class IagoVSMini {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final int numGames = 31;
		final int numPlies = 2;
		final String filename = "bestIago.txt";
		final String file1 = "1IagoPopulation.txt";
		try{
			FileWriter output = new FileWriter("miniVSRandom.txt");

			for(int i = 0; i < numGames; i++) {
				double averageScore = 0.0;
				for(int j = 0; j < 10; ++j){


					GeneticAlgorithm ga = new GeneticAlgorithm(0,0,0);

					Population population = ga.readPopulationFromFile(filename);
					Population badPop = ga.readPopulationFromFile(file1);
					BigChromosome chromoWhite= population.findMostFitIndividual();
					BigChromosome chromoBlack = badPop.getRandomIndividual();
					Player playerWhite = new MinimaxPlayerTrial("whitemine", Constants.WHITE, numPlies, chromoWhite);
					
					// Iago
					//			Player playerWhite = 
					//					new MinimaxPlayer("whiteMini", Constants.WHITE, numPlies,
					//					chromoWhite.getStabilityCoef(), chromoWhite.getParityCoef(),
					//					chromoWhite.getMobilityCoef(), chromoWhite.getDifferentialCoef());

					//Player randomBlack = new RandomPlayer("randomBlack", Constants.BLACK);
					Player randomBlack = new MinimaxPlayerTrial("blackmini", Constants.BLACK, numPlies, chromoBlack );

					Driver driver = new Driver(playerWhite, randomBlack);
					int score = driver.startGame();
					averageScore += score;
					System.out.println("Game " + i + " score: " + score);
					if(i %100 == 0 && i>99) {
						//System.out.println(i);
					}

				}
				averageScore = averageScore / 10.0;
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
