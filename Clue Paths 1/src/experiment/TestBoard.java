package experiment;

import java.util.*;

public class TestBoard {
	private TestBoardCell[][] grid;
	private Set<TestBoardCell> targets;
	private Set<TestBoardCell> visited;
	final static int COLS = 4;
	final static int ROWS = 4;
	
	// Empty constructor
	public TestBoard() {
        super();
        grid = new TestBoardCell[ROWS][COLS];
        targets = new HashSet<>();
        visited = new HashSet<>();
    }
	
	// Calculates targets for a move from startCell of length pathlength.
	public void calcTargets(TestBoardCell startCell, int pathlength) {
		visited.clear();
		targets.clear();
		visited.add(startCell);
		this.findAllTargets(startCell, pathlength);
	}
	
	
	public void findAllTargets(TestBoardCell cell, int pathlength){
		for(TestBoardCell adj:cell.adjList) {
			if(visited.contains(adj) || adj.getOccupied()){
				continue;
			}
			visited.add(adj);
			if (pathlength == 1||adj.getRoom()){
				targets.add(adj);
			}
			else {
				this.findAllTargets(adj, pathlength - 1);
			}
			visited.remove(adj);
		}
	} 

	
	// Returns the cell from the board at row, col
	public TestBoardCell getCell(int row, int col) {
	    if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
	        return grid[row][col];
	    }
	    return null;
	}

	
	// Gets the targets last created by calcTargets()
	public Set<TestBoardCell> getTargets(){
		return null;
	}
	
	
	
}
