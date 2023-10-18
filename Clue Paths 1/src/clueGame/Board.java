package clueGame;
import java.util.*;

import experiment.TestBoardCell;

import java.io.*;	
public class Board {
	private BoardCell[][] grid;
	private int numRows, numColumns;
	private String layoutConfigFile, setupConfigFile;
	private Map<Character, Room> roomMap;
	private Map<Character, String> cellMap = new HashMap<Character, String>();
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
		try {
			this.loadSetupConfig();
		} catch (BadConfigFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.loadLayoutConfig();
		} catch (BadConfigFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void loadSetupConfig() throws BadConfigFormatException{
		String finishedSetup = "./data/" + setupConfigFile;
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
					
				}else {
					continue;
				}
			}
			
			
		}catch(FileNotFoundException e){
			System.out.println("File not found, please try again.");
		}
		
	}


	public void loadLayoutConfig() throws BadConfigFormatException {
		String finishedLayout = "./data/" + layoutConfigFile;
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
			for(int i = 0; i < boardLayout.size() - 1;i++) {
				for(int j = 0; j < boardLayout.get(i).length - 1; j++) {
					String[] arr = boardLayout.get(i);
					
					if(roomMap.containsKey(arr[j].charAt(0)) && !cellMap.containsKey(arr[j].charAt(0))) {
						this.getCell(j, i).setRoom(true); //if in roomMap its a room
					}
					
					if(roomMap.containsKey(arr[j].charAt(0))) {
						this.getCell(j, i).setInitial(arr[j].charAt(0));
						if(arr[j].length() != 1) {
							switch( arr[j].charAt(1) ) {
							case '#':
								this.getCell(j, i).isLabel();
								break;
							case '*':
								this.getCell(j, i).isRoomCenter();
								break;
							case '>':
								this.getCell(j, i).setDirection('R');
								break;
							case '<':
								this.getCell(j, i).setDirection('L');
								break;
							case '^':
								this.getCell(j, i).setDirection('U');
								break;
							case 'v':
								this.getCell(j, i).setDirection('D');
								break;
							//TODO SECRET PASSAGE CASE 
							default:
								String message = "Invalid second character in boardLayout on cell row: " + i + " column: " + j + "Bad character: " + (arr[j].charAt(1)) + " .";
								throw new BadConfigFormatException(message);
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
