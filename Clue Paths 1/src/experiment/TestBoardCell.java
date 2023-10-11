package experiment;
import java.util.*;

public class TestBoardCell {
	private int row, col;
	private Boolean isRoom, isOccupied;
	Set<TestBoardCell> adjList;
	
	
	// Constructor with row and col inputs
	public TestBoardCell(int row, int col) {
	    super();
	    this.row = row;
	    this.col = col;
	    adjList = new HashSet<TestBoardCell>();
	}

	
	// Adds an adjacent cell to this cells adjacency list
	public void addAdjacency(TestBoardCell cell) {
	    this.adjList.add(cell);
	}

	// Returns the adjacency list for the cell
	public Set<TestBoardCell> getAdjList(){
		return adjList;
	}
	
	// Setter for creating a room cell
	public void setRoom(boolean cell) {
		this.isRoom = cell;;
	}
	
	// Getter for if a space is a room cell
	public boolean getRoom() {
		return isRoom;
	}
	
	
	// Setter for indicating if a cell is occupied by another player
	public void setOccupied(boolean cell) {
		this.isOccupied = cell;;
	}
	
	// Getter for indicating if a cell is occupied by another player
	public boolean getOccupied() {
		return isOccupied;
	}



	
}
