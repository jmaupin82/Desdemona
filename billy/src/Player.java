


public abstract class Player {
	/** 
	 * 
	 */
	protected String name;
	
	protected int color;
	
	public Player(String name, int color) {
		this.name  = name;
		this.color = color;
	}
	
	public String getName() {
		return name;
	}

	public int getColor() {
		return color;
	}

	public abstract Move makeMove(LogicBoard board);
	
	public abstract void postMoveProcessing(LogicBoard oldBoard, LogicBoard newBoard, Player whoPlayed);
	
	public abstract void endOfGame(int diskDifferential);
	
}
