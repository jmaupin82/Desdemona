

public class Main {

	/**
	 * @param args
	 */	
	public static void main(String[] args) {
		final int numGames = 10;
		final int numPlies = 2;
		final double gamma = 0.8;
		final double lambda= 0.2;
		final double alpha = 0.2;
		final int numOutput = 1;
		final int numHidden = Constants.SIZE;
		final int numInput  = Constants.SIZE * Constants.SIZE + 2;
		double[][] hiddenLayerWeights = new double[numHidden][numInput];
		double[][] outputLayerWeights = new double[numOutput][numHidden];
		

		for(int i = 0; i < numGames; i++) {
			Player randomWhite = new RandomPlayer("randomWhite", Constants.WHITE);
			Player randomBlack = new RandomPlayer("randomBlack", Constants.BLACK);
			Player mini = new MinimaxPlayer("randomBlack", Constants.BLACK, numPlies);
			
			Driver driver = new Driver(randomWhite, mini);
			
//			Player desdemona1 = new DesdemonaPlayer("randomBlack", Constants.WHITE, numPlies, gamma, lambda, alpha);
//			Player desdemona2 = new DesdemonaPlayer("randomBlack", Constants.BLACK, numPlies, gamma, lambda, alpha,
//					hiddenLayerWeights, outputLayerWeights);
//			Driver driver = new Driver(desdemona1, desdemona2);
			
			driver.startGame();
			
			if(i %100 == 0 && i>99) {
				System.out.println(i);
			}
		}
		
//		LogicBoard b = new LogicBoard();
//		b.play(1, 1, 1);
//		b.play(1, 8, 8);
//		System.out.println(b);
//		b.prev();
//		
//		System.out.println(b);
		
	}

}
