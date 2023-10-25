package experiment;
import java.util.*;

public class TestBoard {
	private TestBoardCell[][] grid;
	private Set<TestBoardCell> targets;
	private Set<TestBoardCell> visited;
	final static int COLS = 4;
	final static int ROWS = 4;
	
	public TestBoard() {
        super();
        grid = new TestBoardCell[ROWS][COLS];
        
        // Initialize cells
        for(int i = 0;i < COLS ;i++) {
			for(int j = 0;j < ROWS;j++) {
				grid[i][j] = new TestBoardCell(i, j);
				grid[i][j].setOccupied(false);
				grid[i][j].setRoom(false);
			}
		}
        
        // Initialize adjacent cells
		for(int i = 0;i < COLS;i++) {
			for(int j = 0;j<ROWS;j++) {
				if (i > 0){
					grid[i][j].addAdjacency(grid[i-1][j]);
				}
				if (j > 0){
					grid[i][j].addAdjacency(grid[i][j-1]);
				}
				if (i < COLS - 1){
					grid[i][j].addAdjacency(grid[i+1][j]);
				}
				if (j < ROWS - 1){
					grid[i][j].addAdjacency(grid[i][j+1]);
				}
			}
		}
        
        targets = new HashSet<TestBoardCell>();
        visited = new HashSet<TestBoardCell>();
    }
	
	// Calculates targets for a move from startCell of length pathlength.
	public void calcTargets(TestBoardCell startCell, int pathlength) {
		targets.clear();
		visited.clear();
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
			} else {
				this.findAllTargets(adj, pathlength - 1);
			}
			
			visited.remove(adj);
		}
	} 

	
	// Returns the cell from the board at row, col
	public TestBoardCell getCell(int row, int col) {
	    return grid[row][col];
	}

	
	// Gets the targets last created by calcTargets()
	public Set<TestBoardCell> getTargets(){
		return targets;
	}
	
	
	
}
