
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int numGames = 2000;
		// TODO Auto-generated method stub
		for(int i = 0; i < numGames; i++) {
			Player randomWhite = new RandomPlayer();
			Player randomBlack = new RandomPlayer();
			Driver driver = new Driver(randomWhite, randomBlack);
		
			driver.startGame();
			
			if(i %100 == 0 && i>99) {
				System.out.println(i);
			}
		}
	}

}
