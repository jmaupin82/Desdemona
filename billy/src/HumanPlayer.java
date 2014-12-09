import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;


public class HumanPlayer extends Player{

	private FileOutputStream reader;

	public HumanPlayer(String name, int color){
		super(name, color);
	}
	
	
	@Override
	public Move makeMove(LogicBoard board) {

		//Ask the user to select a move by clicking on a square. 
		ArrayList<Move> validMoves = board.findPossibleMoves(getColor());
		System.out.println("Please select a move from the following list. ");
		int count = 0;
		for(Move m : validMoves){
			System.out.println(count++ + ". " + m.toString());
		}
		Scanner scan = new Scanner(System.in);
		while(true){
			try{
				int user = scan.nextInt();
				return validMoves.get(user);
			}catch(Exception e){
				System.out.println("You appear to have entered a wrong number.");
				System.out.println(e.getMessage());
			}
		}

	}

	@Override
	public void endOfGame(int diskDifferential) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void postMoveProcessing(LogicBoard oldBoard, LogicBoard newBoard,
			Player whoPlayed) {
		// TODO Auto-generated method stub
		
	}

}
