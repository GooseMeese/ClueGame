package experiment;

import java.util.*;

public class TestBoardCell {
	
	// Constructor with row and col inputs
	public TestBoardCell(int row, int col) {
		super();
	}
	
	// Adds an adjacent cell to this cells adjacency list
	public void addAdjacency(TestBoardCell cell) {
		
	}
	
	// Returns the adjacency list for the cell
	public Set<TestBoardCell> getAdjList(){
		return null;
	}
	
	// Setter for creating a room cell
	public void setRoom(boolean cell) {
		return;
	}
	
	// Getter for if a space is a room cell
	public boolean getRoom(TestBoardCell cell) {
		return true;
	}
	
	
	// Setter for indicating if a cell is occupied by another player
	public void setOccupied(boolean cell) {
		return;
	}
	
	// Getter for indicating if a cell is occupied by another player
	public boolean getOccupied(TestBoardCell cell) {
		return true;
	}

	
}
