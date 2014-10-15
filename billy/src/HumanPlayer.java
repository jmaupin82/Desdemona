import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;


public class HumanPlayer extends Player{

	private FileOutputStream reader;
	
	public HumanPlayer(){
		this.name = "Human Player";
		String filename = (new Date()).toString() + "neuralOuput.txt";
		try {
			this.reader = new FileOutputStream(new File(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public HumanPlayer(String name){
		this.name = name;
	}
	
	
	@Override
	public Move makeMove(Board board) {

		//Ask the user to select a move by clicking on a square. 
		ArrayList<Move> allMoves = getValidMoves(board);
		System.out.println("Please select a move from the following list. ");
		int count = 0;
		for(Move m : allMoves){
			System.out.println(count++ + ". " + m.toString());
		}
		Scanner scan = new Scanner(System.in);
		while(true){
			try{
				int user = scan.nextInt();
				return allMoves.get(user);
			}catch(Exception e){
				System.out.println("You appear to have entered a wrong number.");
				e.printStackTrace();
			}
		}

	}

}
