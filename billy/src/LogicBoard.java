import java.awt.Point;
import java.util.ArrayList;
import java.util.List;


public class LogicBoard implements Constants {

	private int position[][];

	public History history;

	public LogicBoard() {
		history = new History();
		
		position = new int[SIZE][SIZE];

		initialPosition();
	}
	
	/**
	 * This is Eleazar's and Joel's method not Billy's
	 * 
	 * This method receives a board, and simply performs
	 * a deep copy of it.
	 * 
	 * Remember that position stores the trasposed of the
	 * cartesian representation so rows go in the y direction,
	 * columns in the x.
	 * 
	 * @param b
	 */
	public LogicBoard(LogicBoard b) {
		history = new History(b.history);
		//setLayout(new GridLayout(SIZE,SIZE,1,1));
		//setBackground(Color.BLACK);
		position = new int[SIZE][SIZE];
		for (int i=0;i<SIZE;i++) {
			for (int j=0;j<SIZE;j++) {
				
				//System.out.println(b.position[i][j]);
				setState(i,j, b.getState(i, j));
				//add(sq,i*SIZE+j);
			}
		}
	}

	private void initialPosition() {
		for (int i=0;i<SIZE;i++)
			for (int j=0;j<SIZE;j++) {
				if (i==4 && j==4 || i==5 && j==5){
					setState(i,j,WHITE);
				}
				else if (i==4 && j==5 || i==5 && j==4){
					setState(i,j,BLACK);
				}
				else {
					setState(i,j,EMPTY);
				}
			}
	}

	public int getSquare(int x,int y) {
		return position[y][x];
	}

	public int getState(int x,int y) {
		return position[y][x];
	}

	public void setState(int x,int y,int state) {
		position[y][x] = state;
	}


	public void first() {
		while(history.hasPrev()) prev();
	}

	public void prev() {
		Point m = history.getLastMove();
		position[(int)m.getY()][(int)m.getX()] = EMPTY;
		List<Point> changed =  history.takeBack();
		for(Point p:changed) { 
			int pos = position[(int)p.getY()][(int)p.getX()];
			position[(int)p.getY()][(int)p.getX()] =  flip(pos);
		}
	}

	public void next() {
		int c = history.getNextColor();
		List<Point> ch = impact(c,history.getNextMove());
		Point m = history.getNextMove();
		history.forward(ch);
		position[(int)m.getY()][(int)m.getX()] = (c);
		for(Point p:ch) { 
			int pos = position[(int)p.getY()][(int)p.getX()];
			position[(int)p.getY()][(int)p.getX()] =  flip(pos);
		}
	}

	public void last() {
		while (history.hasNext()) { 
			next();
		}
	}

	/*
	 * The inputs are cartesian coordinates (x horizontal)
	 */
	public void play(int color,int x,int y) {
		
		Point m = new Point(x,y);
		List<Point> ch = impact(color,m);
		history.play(color,m,ch);
		setState(x,y,color);
		for(Point p:ch) {
			int pos = position[(int)p.getY()][(int)p.getX()];
			//System.out.print("flipped " + p + " from " + pos + " to " );
			position[(int)p.getY()][(int)p.getX()] = flip(pos);
			//System.out.println(flip(pos));
		}
		//System.out.println("Making play to x="+x + " y="+y + "board="+ position[y][x]);
	}

	public boolean findPossible(int color) {
		boolean found = false;

		List<Point> list;

		for(int x=0;x<SIZE;x++)
			for(int y=0;y<SIZE;y++) {
				if (getState(x,y)==EMPTY) {
					list = impact(color,new Point(x,y));

					if (list.size()>0) {
						found = true;
						//getSquare(x,y).setClickable(true);
						continue;
					}
				}
				//getSquare(x,y).setClickable(false);
			}

		return found;
	}

	public int count(int color) {
		int rst = 0;
		for(int x=0;x<SIZE;x++)
			for(int y=0;y<SIZE;y++)
				if (getState(x,y) == color){
					rst++;
				}
		return rst;
	}

	private List<Point> impact(int color,Point move) {
		List<Point> rst = new ArrayList<Point>();
		for (int i:increment)
			for(int j:increment) {
				if (i==0 && j==0) continue;
				List<Point> line = lineImpact(color,(int)move.getX(),(int)move.getY(),i,j);
				if (line != null) rst.addAll(line);
			}
		return rst;
	}

	private List<Point> lineImpact(int color,int x,int y,int dx,int dy) {
		List<Point> rst = new ArrayList<Point>();
		x+=dx; y+=dy;
		while (x>=0 && x<SIZE && y>=0 && y<SIZE) {
			if (getState(x,y)==-color) rst.add(new Point(x,y));
			else if (getState(x,y)==EMPTY) return null;
			else if (getState(x,y)==color) return rst;
			x+=dx; y+=dy;
		}
		return null;
	}

	/**
	 * 
	 * @param player
	 * @return
	 */
	public ArrayList<Move> findPossibleMoves(int playerColor) {
		//final int playerColor = player.getColor();
		ArrayList<Move> possibleMoves = new ArrayList<Move>();

		List<Point> list;

		for(int x=0;x<SIZE;x++) {
			for(int y=0;y<SIZE;y++) {
				if (getState(x,y)==EMPTY) {
					list = impact(playerColor,new Point(x,y));
		
					if (list.size()>0) {
						possibleMoves.add(new Move(x,y));
						continue;
					}
				}
			}
		}
		return possibleMoves;
	}
	
	/**
	 * This returns the disk differential
	 * @return
	 */
	public int getDiskDifferential() {
		int numberWhiteStones = this.count(Constants.WHITE);
		int numberBlackStones = this.count(Constants.BLACK);
		int diskDifferential = numberWhiteStones - numberBlackStones;
		
		return diskDifferential;
	}
	
	/**
	 * This function returns true iff the game has ended
	 * 
	 * @param board
	 * @return
	 */
	public boolean EndOfGame() {
		// 1. The game ends either when the number of white stones plus the 
		// number of black stones equals the number of cells in the board.

		boolean noMovesWhite = this.findPossibleMoves(Constants.WHITE).isEmpty();
		boolean noMovesBlack = this.findPossibleMoves(Constants.BLACK).isEmpty();
			
		return noMovesWhite || noMovesBlack;
		//return (noMovesWhite && noMovesBlack);
	}
	
	/**
	 * This function counts the number of squares in the board that are empty.
	 * @return
	 */
	public int countEmptySquares() {
		int count = 0;
		for(int x=0; x<SIZE; x++)
			for(int y=0; y<SIZE; y++)
				if (getState(x,y) == Constants.EMPTY){
					count++;
				}
		return count;
	}
	
	public int countStableStones(Player player) {
		int count = 0;
		for(int x=0; x<SIZE; x++)
			for(int y=0; y<SIZE; y++)
				if (getState(x,y) == Constants.EMPTY){
					count++;
				}
		return count;
	}
	
	public int countPlayedCorners(int playerColor) {
		int count = 0;

		if (getState(0,0) == playerColor){
			count++;
		}
		if (getState(9,9) == playerColor){
			count++;
		}
		if (getState(0,9) == playerColor){
			count++;
		}
		if (getState(9,0) == playerColor){
			count++;
		}
		
		return count;
	}
	
	 public int flip(int state) { 
		 assert(state!=EMPTY); 
		 return (state==BLACK?WHITE:BLACK);
	}
	 
	 public String toString() {
		 String str = "";
		 for(int y=0; y<SIZE; y++) {
				for(int x=0; x<SIZE; x++) {
					if (getState(x,y) == Constants.EMPTY){
						str += "--";
					}
					else if (getState(x,y) == Constants.WHITE){
						str += " 1";
					}
					else {
						str += "-1";
					}
					str += "  ";
				}
				str +="\n";
		 }
		 return str;
	 }
}
