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


	/*
	 * variable and methods used for singleton pattern
	 */
	private static Board theInstance;

	// Constructor is private to ensure only one can be created
	private Board(){
		super();
		visited = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
	}

	

	// Sets up the board and initializes the adjacency list
	public void setupBoard() {
		grid = new BoardCell[numRows][numColumns];
		for(int i = 0;i<numRows;i++) {
			for(int j = 0;j<numColumns;j++) {
				grid[i][j] = new BoardCell(i, j);
				grid[i][j].setOccupied(false);
				grid[i][j].setRoom(false);
			}
		}
	}
	
	
	private void makeAdjList() {
		//build adjacency lists 
		/* TODO
		 * 
		 * Done in first nested for loop
		 * 
		 * DONE Walkways connect to adjacent walkways.
		 * DONE Walkways with doors will also connect to the room center the door points to.
		 * 
		 * Done in second nested for loop
		 * 
		 * DONE The cell that represents the Room (i.e. connects to walkway) is the cell with a second character of ‘*’ (no other cells in a room should have adjacencies).
		 * DONE Room center cells ONLY connect to 1) door walkways that enter the room and 2) another room center cell if there is a secret passage connecting.
		 */
		
		//first nested for loop builds walkway and doorway adjacentcy's, also sets room secret passages
		for(int col = 0;col<numColumns;col++) {
			for(int row = 0;row<numRows;row++) {
				//set room's secret passages
				//For each board cell that is a secret passage
				//	get its room, and the room connected via secret passage, then set its room's adjRoomCenterThroughSecretPassage to the center of the other room
				if(grid[col][row].isSecretPassage) {
					Room thisRoom = roomMap.get(grid[col][row].getInitial()); //this room by initial
					Room adjacentRoomThroughSP = roomMap.get(grid[col][row].getSecretPassage()); //adjacent room through SP from roomMap
					thisRoom.setAdjRoomCenterThroughSecretPassage(adjacentRoomThroughSP.getCenterCell()); //give this room the center cell of it's adjacent room's (via SP)
				}
				
				//we only process a cell if it is a walkway or doorway here
				if(grid[col][row].getInitial() == 'W' || grid[col][row].isDoorway()) {
					
					//if walkway connect to adjacent walkways 
					if(grid[col][row].getInitial() == 'W') {
						if (col > 0 && grid[col-1][row].getInitial() == 'W'){
							grid[col][row].addAdjacency(grid[col-1][row]);
						}
						if (row > 0 && grid[col][row-1].getInitial() == 'W'){
							grid[col][row].addAdjacency(grid[col][row-1]);
						}
						if (col < numColumns - 1 && grid[col+1][row].getInitial() == 'W'){
							grid[col][row].addAdjacency(grid[col+1][row]);
						}
						if (row < numColumns - 1 && grid[col][row+1].getInitial() == 'W'){
							grid[col][row].addAdjacency(grid[col][row+1]);
						}
					}
					
					//if doorway connect with room center in direction it points too
					//also add it to doorwayList of its room
					if(grid[col][row].isDoorway()) {
						DoorDirection roomDirection = grid[col][row].getDoorDirection(); //direction of room
						char adjacentRoomInitial; //char which is key value in roomMap
						
						//switch on roomDirection, to get the room adjacent to a door in the appropriate door direction
						switch(roomDirection) {
							case UP:
								adjacentRoomInitial = grid[col][row-1].getInitial(); //set room initial
								break;
							case DOWN:
								adjacentRoomInitial = grid[col][row+1].getInitial();
								break;
							case LEFT:
								adjacentRoomInitial = grid[col-1][row].getInitial();
								break;
							case RIGHT:
								adjacentRoomInitial = grid[col+1][row].getInitial();
								break;
						default:
							adjacentRoomInitial = ' ';
							System.out.println("ERROR, invalid door direction"); // we should never get here 
							break;
						}
						
						Room adjRoom = roomMap.get(adjacentRoomInitial); // Room adjacent to the door, we need to get its center cell
						adjRoom.addDoorway(grid[col][row]);	//add this doorway to room's doorways set
						grid[col][row].addAdjacency(adjRoom.getCenterCell()); // Finally add the center cell of the room adjacent to the doorway to the adjacency list of the doorway
					}
				}
			}
		}
		
		//Second for loop handles room centers
		//Room centers are handled after walkways and doorways, this is because rooms may have multiple doorways, and they may be added after room centers
		//	so we store the doorways for each room in the first pass over the board then handle room center adjacency's after
		for(int col = 0;col<numColumns;col++) {
			for(int row = 0;row<numRows;row++) {
				//if room center cell, the cell should be adjacent to the doorway of the room and to any secret passage rooms
				if(grid[col][row].isRoomCenter()) {
					char roomInitial =  grid[col][row].getInitial(); //if its a room center it has a room initial, get room initial
					Room thisRoom = roomMap.get(roomInitial); //get the room that corresponds with room initial from roomMap
					
					//for each doorway in the doorways of this room add them to the adjacency list of the room center
					for(BoardCell door : thisRoom.getDoorways()) {
						grid[col][row].addAdjacency(door);
					}
					
					//add secret passages
					if(thisRoom.getAdjRoomCenterThroughSecretPassage() != null) {
						grid[col][row].addAdjacency(thisRoom.getAdjRoomCenterThroughSecretPassage());
					}
					
				}
			}
		}
		
	}

	//singleton setup
	public static Board getInstance() {
		if(theInstance == null) {
			theInstance = new Board();
		}
		return theInstance;
	}

	//setup the board from the files
	public void initialize() {
		try {
			this.loadSetupConfig();
			this.loadLayoutConfig();
			this.makeAdjList();
		}catch(BadConfigFormatException e){
			System.out.println("Errored");
		}
	}

	//finds the targets from a cell
	public void calcTargets(BoardCell startCell, int pathlength) {
		visited.clear();
		targets.clear();
		visited.add(startCell);
		this.findAllTargets(startCell, pathlength);
	}

	//returns a cell from a position, with parameters COLUMN THEN ROW
	public BoardCell getCellFromGrid(int col, int row) {
		return grid[col][row];
	}

	//returns a cell from a position, with parameters ROW THEN COLUMN
	public BoardCell getCell(int row, int col) {
		return grid[col][row];
	}

	//recursive call for calctargets
	public void findAllTargets(BoardCell cell, int pathlength){
		for(BoardCell adj:cell.adjList) {
			if(visited.contains(adj) || (adj.getOccupied() && !adj.isRoom())){
				continue;
			}
			visited.add(adj);
			if (pathlength == 1||adj.isRoom()){
				targets.add(adj);
			}
			else {
				this.findAllTargets(adj, pathlength - 1);
			}
			visited.remove(adj);
		}
	}

	public Set<BoardCell> getTargets() {
		return targets;
	}

	public void setConfigFiles(String layout, String setup) {
		layoutConfigFile = layout;
		setupConfigFile = setup;

	}


	//Read in ClueSetup.txt
	public void loadSetupConfig() throws BadConfigFormatException{
		
		String finishedSetup = "./data/" + setupConfigFile; //Read from data folder 
		
		/*
		 * Try and open file, catch file not found, throws BadConfigFormatException
		 */
		try(Scanner setupScanner = new Scanner(new File(finishedSetup))){
			//TODO read data from setupConfigFile
			while(setupScanner.hasNext()) {
				String line = new String(setupScanner.nextLine());
				String[] parts = line.split(",");

				//Regex for each line, to tell if its a room, a space, or a comment 
				if(line.matches("^Room.*$")) {
					if(parts.length != 3) {
						String message = "Invalid Setup format, expecting a room name and symbol for each room, bad line: \" " + line + "\".";
						throw new BadConfigFormatException(message);
					}
					String roomName = parts[1].substring(1);
					Room newRoom = new Room(roomName);
					String symbol = parts[2].substring(1);
					roomMap.put(symbol.charAt(0), newRoom);

				}else if(line.matches("^Space.*$")) {
					if(parts.length != 3) {
						String message = "Invalid Setup format, expecting a space name and symbol for each type of space, bad line: \" " + line + "\".";
						throw new BadConfigFormatException(message);
					}

					String spaceName = parts[1].substring(1);
					String spaceSymbol = parts[2].substring(1);

					Room newRoom = new Room(spaceName);
					roomMap.put(spaceSymbol.charAt(0), newRoom);
					cellMap.put(spaceSymbol.charAt(0), spaceName);

				}else if(line.matches("^//.*$")) {
					continue;
				}else {
					throw new BadConfigFormatException();
				}
			}


		}catch(FileNotFoundException e){
			System.out.println("File not found, please try again.");
		}

	}



	//Read in ClueLayout.csv
	//loads the csv file and helps setup board labels
	public void loadLayoutConfig() throws BadConfigFormatException {
		String finishedLayout = "./data/" + layoutConfigFile; //Read from data folder 
		
		/*
		 * Try and open file, catch file not found, throws BadConfigFormatException
		 */
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
				}else {
					if(numCols != spaces.length) {
						//Throw exception if the board layout file does not have the same number of columns in every row.
						String message = "Number of colums in this row does not match the number of columns in another row, bad layout format! Error on line " + (numRows+1) + " of " + finishedLayout;
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
						this.getCellFromGrid(j, i).setRoom(true); //if in roomMap its a room
					}

					if(roomMap.containsKey(arr[j].charAt(0))) {
						this.getCellFromGrid(j, i).setInitial(arr[j].charAt(0));
						//If of length 2, then the second char is a special deliminator of center, label, door direction, or secret passage
						//	we use a switch statement to efficiently handle all of these mutually exclusive cell modifiers
						if(arr[j].length() != 1) {
							switch( arr[j].charAt(1) ) {
							case '#':
								this.getCellFromGrid(j, i).setLabel();
								roomMap.get(arr[j].charAt(0)).setLabelCell(this.getCellFromGrid(j, i));
								break;
							case '*':
								this.getCellFromGrid(j, i).setCenter();
								roomMap.get(arr[j].charAt(0)).setCenterCell(this.getCellFromGrid(j, i));
								break;
							case '>':
								this.getCellFromGrid(j, i).setDoorDirection('R');
								break;
							case '<':
								this.getCellFromGrid(j, i).setDoorDirection('L');
								break;
							case '^':
								this.getCellFromGrid(j, i).setDoorDirection('U');
								break;
							case 'v':
								this.getCellFromGrid(j, i).setDoorDirection('D');
								break;
								//TODO SECRET PASSAGE CASE 
							default:
								if(roomMap.containsKey(arr[j].charAt(1))) {
									this.getCellFromGrid(j, i).setSecretPassage(arr[j].charAt(1));
								}
								else {
									String message = "Invalid second character in boardLayout on cell row: " + i + " column: " + j + "Bad character: " + (arr[j].charAt(1)) + " .";
									throw new BadConfigFormatException(message);
								}
							}
						}

					}else {
						//Throw exception if character in csv layout not in text setup
						String message = "This character is not in the Setup but is in the Layout " + arr[j] + " .";
						throw new BadConfigFormatException(message);
					}
				}
			}
		}
		catch (FileNotFoundException e){
			System.out.println("File not found, please try again.");
		}
	}

	//Get room by initial from roomMap
	public Room getRoom(char c) {
		return roomMap.get(c);
	}


	//TODO make work
	public Room getRoom(BoardCell cell) {
		char character = cell.getInitial();
		return roomMap.get(character);
	}

	public int getNumRows() {
		return numRows;
	}
	
	public Set<BoardCell> getAdjList(int col, int row){
		return this.getCell(col, row).getAdjList();
	}
	
	public Set<BoardCell> getAdjListFromGrid(int col, int row){
		return this.getCellFromGrid(col, row).getAdjList();
	}

	public int getNumColumns() {
		return numColumns;
	}


}

