package clueGame;
import java.util.*;

import experiment.TestBoardCell;

import java.io.*;	
public class Board {
	private BoardCell[][] grid;
	private int numRows, numColumns;
	private String layoutConfigFile, setupConfigFile;
	private Map<Character, Room> roomMap;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;

	/*
	 * variable and methods used for singleton pattern
	 */
	private static Board theInstance = new Board();

	// Constructor is private to ensure only one can be created
	private Board() {
		super() ;
		roomMap = new HashMap<Character, Room>();
	}

	// This method returns the only Board
	public static Board getInstance() {
		if(theInstance == null) {
			theInstance = new Board();
		}
		return theInstance;
	}

	// Calculates targets for a move from startCell of length pathlength.
	public void calcTargets(BoardCell startCell, int pathlength) {
		targets.clear();
		visited.clear();
		visited.add(startCell);
		this.findAllTargets(startCell, pathlength);
	}

	// Calls calcTargets recursively
	public void findAllTargets(BoardCell cell, int pathlength){
		for(BoardCell adj:cell.adjList) {
			if(visited.contains(adj) || adj.getOccupied()){
				continue;
			}

			visited.add(adj);
			if (pathlength == 1||adj.isRoom()){
				targets.add(adj);
			} else {
				this.findAllTargets(adj, pathlength - 1);
			}

			visited.remove(adj);
		}
	} 
	

	// Returns the cell from the board at row, col
	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}


	// Gets the targets last created by calcTargets()
	public Set<BoardCell> getTargets(){
		return targets;
	}


	// Sets up the board and initializes the adjacency list
	public void setupBoard() {
		grid = new BoardCell[numColumns][numRows];
		for(int i = 0;i<numColumns;i++) {
			for(int j = 0;j<numRows;j++) {
				grid[i][j] = new BoardCell(i, j);
				grid[i][j].setOccupied(false);
				grid[i][j].setRoom(false);
			}
		}
		for(int i = 0;i<numColumns;i++) {
			for(int j = 0;j<numRows;j++) {
				if (i > 0){
					grid[i][j].addAdjacency(grid[i-1][j]);
				}
				if (j > 0){
					grid[i][j].addAdjacency(grid[i][j-1]);
				}
				if (i < numColumns - 1){
					grid[i][j].addAdjacency(grid[i+1][j]);
				}
				if (j < numColumns - 1){
					grid[i][j].addAdjacency(grid[i][j+1]);
				}
			}
		}
		visited = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
	}


	/*
	 * initialize the board (since we are using singleton pattern)
	 */
	public void initialize(){
		this.loadSetupConfig();
		this.loadLayoutConfig();
	}


	public void loadSetupConfig(){
		File file = new File(setupConfigFile);
		try (Scanner sc = new Scanner(file)){
			int x = 0;
			while (sc.hasNextLine()) {
				String[] temp = sc.nextLine().split(",");
				if( temp[0].isEmpty()) {
					break;
				}
				for (int i = 0; i < temp.length; i++) {
					BoardCell tempCell = new BoardCell(x, i);
					if (roomMap.containsKey(temp[i].charAt(0))) {
						tempCell.setRoom(true);
						if (temp[i].length() > 1) {
							if (temp[i].charAt(1) == '*') {
								tempCell.setCenter(true);
							}
							if (temp[i].charAt(1) == '#') {
								tempCell.setLabel(true);
							}
							if (temp[i].charAt(1) == '<') {
								tempCell.setDoorway(true);
								tempCell.setDirection(DoorDirection.LEFT);
							} else if ( temp[i].charAt(1) ==  '>') {
								tempCell.setDoorway(true);
								tempCell.setDirection(DoorDirection.RIGHT);
							}  else if ( temp[i].charAt(1) ==  '^') {
								tempCell.setDoorway(true);
								tempCell.setDirection(DoorDirection.UP);
							}  else if (temp[i].charAt(1) ==  'v') {
								tempCell.setDoorway(true);
								tempCell.setDirection(DoorDirection.DOWN);
							}
							if (temp[i].charAt(1) == 'S') {
								tempCell.setPassage();
							}
						}
					}
					grid[x][i] = tempCell;
					System.out.println(grid[x][i].getInitial());
				}
				x++;
				System.out.println();
				roomMap.put(temp[2].charAt(0), new Room(temp[1]));
			}
		} catch(FileNotFoundException e) {
			System.out.println("Error: File not found");
		}
		System.out.print(roomMap.isEmpty());
	}


	public void loadLayoutConfig(){
		File file = new File(layoutConfigFile);
		try (Scanner sc = new Scanner(file)){
			while (sc.hasNextLine()) {
				String[] temp = sc.nextLine().split(",");
				if( temp[0].isEmpty()) {
					break;
				}
				for (int i = 0; i < temp.length; i++) {
					System.out.print(temp[i]);
				}
				System.out.println();
			}
		} catch(FileNotFoundException e) {
			System.out.println("Error: File not found");
		}
	}



	public Room getRoom(char c) {
		return roomMap.get(c);
	}


	public Room getRoom(BoardCell cell) {
		char character = cell.getInitial();
		return roomMap.get(character);
	}

	public int getNumRows() {
		return numColumns;
	}


	public int getNumColumns() {
		return numColumns;
	}


	public void setConfigFiles(String layout, String setup) {
		this.layoutConfigFile = layout;
		this.setupConfigFile = setup;

	}




}
