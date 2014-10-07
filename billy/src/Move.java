
public class Move {
	Board board;
	Square square;
	
	public Move(Square square) {
		this.square = square;
	}

	public Board getBoard() {
		return board;
	}

	public Square getSquare() {
		return square;
	}

	public String toString(){
		return square.toString();
	}
	
}
