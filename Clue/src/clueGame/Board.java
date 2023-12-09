package clueGame;
import java.util.*;

import experiment.TestBoardCell;

import java.io.*;	
public class Board {
	private BoardCell[][] grid;
	private int numRows, numColumns;	
	private Set<BoardCell> targets, visited;
	private String layoutConfigFile, setupConfigFile;
	private Map<Character, Room> roomMap = new HashMap<Character, Room>();
	private Map<Character, String> cellMap = new HashMap<Character, String>();
	private static Board theInstance;

	// Constructor is private to ensure only one can be created
	private Board(){
		super();
		visited = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
	}

	// Sets up the board and initializes the adjacency list
	public void setupBoard() {
		grid = new BoardCell[numColumns][numRows];
		for(int i = 0; i< numColumns;i++) {
			for(int j = 0;j < numRows;j++) {
				grid[i][j] = new BoardCell(i, j);
				grid[i][j].setOccupied(false);
				grid[i][j].setRoom(false);
			}
		}
		
	}
	
	private void makeAdjList() {
		// Nested for loop to set secret passages
		for(int col = 0;col<numColumns;col++) {
			for(int row = 0;row<numRows;row++) {
				if(grid[col][row].isSecretPassage) {
					Room thisRoom = roomMap.get(grid[col][row].getInitial());
					Room adjacentRoomThroughSP = roomMap.get(grid[col][row].getSecretPassage());
					thisRoom.setAdjRoomCenterThroughSecretPassage(adjacentRoomThroughSP.getCenterCell());
				}
				
				// Check if the cell is a doorway or walkway
				if(grid[col][row].isDoorway() || grid[col][row].getInitial() == 'W') {
					
					// Check for adjacent walkways 
					if(grid[col][row].getInitial() == 'W') {
						if (col < numColumns - 1 && grid[col + 1][row].getInitial() == 'W'){
							grid[col][row].addAdjacency(grid[col + 1][row]);
						}
						if (row < numColumns - 1 && grid[col][row + 1].getInitial() == 'W'){
							grid[col][row].addAdjacency(grid[col][row + 1]);
						}
						if (col > 0 && grid[col - 1][row].getInitial() == 'W'){
							grid[col][row].addAdjacency(grid[col-1][row]);
						}
						if (row > 0 && grid[col][row - 1].getInitial() == 'W'){
							grid[col][row].addAdjacency(grid[col][row - 1]);
						}
						
					}
					
					if(grid[col][row].isDoorway()) {
						DoorDirection roomDirection = grid[col][row].getDoorDirection();
						char adjacentRoomInitial; 
						
						// Get the door direction for the room adjacent to the door
						switch(roomDirection) {
							case LEFT:
								adjacentRoomInitial = grid[col - 1][row].getInitial();
								break;
							case RIGHT:
								adjacentRoomInitial = grid[col + 1][row].getInitial();
								break;
							case UP:
								adjacentRoomInitial = grid[col][row -1].getInitial();
								break;
							case DOWN:
								adjacentRoomInitial = grid[col][row +1].getInitial();
								break;
						default:
							adjacentRoomInitial = ' ';
							break;
						}
						
						Room adjRoom = roomMap.get(adjacentRoomInitial); // Get the center cell for the room
						adjRoom.addDoorway(grid[col][row]);
						grid[col][row].addAdjacency(adjRoom.getCenterCell());
					}
				}
			}
		}
		
		// We handle the adjacency list for room centers in this loop
		for(int col = 0;col < numColumns;col++) {
			for(int row = 0;row < numRows;row++) {
				
				if(grid[col][row].isRoomCenter()) {
					char roomInitial =  grid[col][row].getInitial(); // Get the room center initial
					Room thisRoom = roomMap.get(roomInitial); 
					
					// Add the doorways in the room
					for(BoardCell door : thisRoom.getDoorways()) {
						grid[col][row].addAdjacency(door);
					}
					
					// Adds our secret passages
					if(thisRoom.getAdjRoomCenterThroughSecretPassage() != null) {
						grid[col][row].addAdjacency(thisRoom.getAdjRoomCenterThroughSecretPassage());
					}
					
				}
				
			}
		}
		
		
	}
	
	// Sets the correct config files
	public void setConfigFiles(String layout, String setup) {
		layoutConfigFile = layout;
		setupConfigFile = setup;

	}
	
	
	// This method returns the only Board
	public static Board getInstance() {
		if(theInstance == null) {
			theInstance = new Board();
		}
		return theInstance;
	}      

	
	/*
	 * initialize the board (since we are using singleton pattern)
	 */
	public void initialize() {
		try {
			this.loadSetupConfig();
			this.loadLayoutConfig();
			this.makeAdjList();
		}catch(BadConfigFormatException e){
			System.out.println("Error with initialization");
		}
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
		for(BoardCell adj : cell.adjList) {
			if(visited.contains(adj) || (adj.getOccupied() && !adj.isRoom())){
				continue;
			}
			visited.add(adj);
			if (pathlength == 1 || adj.isRoom()){
				targets.add(adj);
			}
			else {
				this.findAllTargets(adj, pathlength - 1);
			}
			visited.remove(adj);
		}
	}

	// Gets the targets last created by calcTargets()
	public Set<BoardCell> getTargets(){
		return targets;
	}

	// Returns the cell from the board at row, col
	public BoardCell getCell(int row, int col) {
		return grid[col][row];
	}

	// Returns the cell from the board at col, row
	public BoardCell getCellFromGrid(int col, int row) {
		return grid[col][row];
	}



	// Read in ClueSetup.txt
	public void loadSetupConfig() throws BadConfigFormatException{
		String finishedSetup = "./data/" + setupConfigFile;
		
		// Try to load the file
		try(Scanner setupScanner = new Scanner(new File(finishedSetup))){
			while(setupScanner.hasNext()) {
				String line = new String(setupScanner.nextLine());
				String[] parts = line.split(",");

				
				
				// Read each line
				if(line.matches("^Room.*$")) {
					if(parts.length != 3) {
						String message = "Invalid room, expecting a room name and symbol for each room, bad line: \" " + line + "\".";
						throw new BadConfigFormatException(message);
					}
					
					String roomName = parts[1].substring(1);
					Room newRoom = new Room(roomName);
					String symbol = parts[2].substring(1);
					roomMap.put(symbol.charAt(0), newRoom);

					
					
				} else if (line.matches("^Space.*$")) {
					if(parts.length != 3) {
						String message = "Invalid space, expecting a space name and symbol for each type of space, bad line: \" " + line + "\".";
						throw new BadConfigFormatException(message);
					}
					

					String spaceName = parts[1].substring(1);
					String spaceSymbol = parts[2].substring(1);

					Room newRoom = new Room(spaceName);
					roomMap.put(spaceSymbol.charAt(0), newRoom);
					cellMap.put(spaceSymbol.charAt(0), spaceName);

				} else if (line.matches("^//.*$")) {
					continue;
				} else {
					throw new BadConfigFormatException();
				}
			}


		} catch (FileNotFoundException e){
			System.out.println("Setup file not found");
		}

	}


	
	// Loads in the csv
	public void loadLayoutConfig() throws BadConfigFormatException {
		String finishedLayout = "./data/" + layoutConfigFile;
		
		// Try and open layout file
		try(Scanner test = new Scanner(new File(finishedLayout))){
			ArrayList<String[]> boardLayout = new ArrayList<String[]>();
			
			int numCols = 0;
			int numRows = 0;
			boolean firstRow = true;
			while(test.hasNext()) {
				String line = new String(test.nextLine());
				String[] spaces = line.split(",");
				
				if(firstRow) { 
					numCols = spaces.length; 
					firstRow = false;
				} else {
					if(numCols != spaces.length) {
						String message = "Column number mismatch on line " + (numRows + 1) + " of " + finishedLayout;
						throw new BadConfigFormatException(message); 
					}
				}

				numRows++;
				boardLayout.add(spaces);
			}
			
			this.numColumns = numCols;
			this.numRows = numRows;
			this.setupBoard();
			
			
			for(int i = 0; i < boardLayout.size();i++) {
				for(int j = 0; j < boardLayout.get(i).length; j++) {
					String[] arr = boardLayout.get(i);

					if(roomMap.containsKey(arr[j].charAt(0)) && !cellMap.containsKey(arr[j].charAt(0))) {
						this.getCellFromGrid(j, i).setRoom(true);
					}

					if(roomMap.containsKey(arr[j].charAt(0))) {
						this.getCellFromGrid(j, i).setInitial(arr[j].charAt(0));
						// If it is greater than 1, it is a special cell, here we determine that cell
						if(arr[j].length() != 1) {
							switch( arr[j].charAt(1) ) {

							case '<':
								this.getCellFromGrid(j, i).setDoorDirection('L');
								break;
							case '>':
								this.getCellFromGrid(j, i).setDoorDirection('R');
								break;
							case '^':
								this.getCellFromGrid(j, i).setDoorDirection('U');
								break;
							case 'v':
								this.getCellFromGrid(j, i).setDoorDirection('D');
								break;
							case '#':
								this.getCellFromGrid(j, i).setLabel();
								roomMap.get(arr[j].charAt(0)).setLabelCell(this.getCellFromGrid(j, i));
								break;
							case '*':
								this.getCellFromGrid(j, i).setCenter();
								roomMap.get(arr[j].charAt(0)).setCenterCell(this.getCellFromGrid(j, i));
								break;								
							default:
								if(roomMap.containsKey(arr[j].charAt(1))) {
									this.getCellFromGrid(j, i).setSecretPassage(arr[j].charAt(1));
								} else {
									String message = "Invalid second character, row: " + i + ", column: " + j;
									throw new BadConfigFormatException(message);
									
								}
							}
						}

					} else {
						String message = "This character is the layout but not in the setup " + arr[j] + " .";
						throw new BadConfigFormatException(message);
					}
				}
			}
		} catch (FileNotFoundException e){
			System.out.println("File not found");
		}
	}

	
	// Get room initial
	public Room getRoom(char c) {
		return roomMap.get(c);
	}

	// Get room cell
	public Room getRoom(BoardCell cell) {
		char character = cell.getInitial();
		return roomMap.get(character);
	}
	
	// Get row count
	public int getNumRows() {
		return numRows;
	}

	// Get Column count
	public int getNumCols() {
		return numColumns;
	}


	public Set<BoardCell> getAdjList(int col, int row){
		return this.getCell(col, row).getAdjList();
	}
	
	public Set<BoardCell> getAdjListFromGrid(int col, int row){
		return this.getCellFromGrid(col, row).getAdjList();
	}


}
