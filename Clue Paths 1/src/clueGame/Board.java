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
	public Set<BoardCell> getAdjList(int row, int col){
		BoardCell cell = this.getCell(row, col);
		
		return cell.getAdjList();
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
		grid = new BoardCell[numRows][numColumns];
		for(int i = 0;i<numRows;i++) {
			for(int j = 0;j<numColumns;j++) {
				grid[i][j] = new BoardCell(i, j);
				grid[i][j].setOccupied(false);
				grid[i][j].setRoom(false);
			}
		}
		visited = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
	}


	/*
	 * initialize the board (since we are using singleton pattern)
	 */
	public void initialize(){
		Board board = Board.getInstance();
		try {
			board.loadSetupConfig();
		} catch (BadConfigFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			board.loadLayoutConfig();
		} catch (BadConfigFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.makeAdjList();
	}

	
	public void loadSetupConfig() throws BadConfigFormatException{
		String finishedSetup = "./data/" + setupConfigFile;
		try(Scanner setupScanner = new Scanner(new File(finishedSetup))){
			
			// Read from setupConfigFile
			while(setupScanner.hasNext()) {
				String line = new String(setupScanner.nextLine());
				String[] parts = line.split(", ");
				
				// Check each line for rooms and spaces
				if(line.matches("^Room.*$")) {
					if(parts.length != 3) {
						String message = "Invalidformat, expecting a name and symbol for each room, bad line: \" " + line + "\".";
						throw new BadConfigFormatException(message);
					}
					
					String roomName = parts[1].substring(0);
					Room newRoom = new Room(roomName);				
					String symbol = parts[2].substring(0);
					roomMap.put(symbol.charAt(0), newRoom);
					
				} else if(line.matches("^Space.*$")) {
					if(parts.length != 3) {
						String message = "Invalid format, expecting aname and symbol for each type of space, bad line: \" " + line + "\".";
						throw new BadConfigFormatException(message);
					}
					
					String spaceName = parts[1].substring(0);
					String spaceSymbol = parts[2].substring(0);
					Room newRoom = new Room(spaceName);
					roomMap.put(spaceSymbol.charAt(0), newRoom);
					cellMap.put(spaceSymbol.charAt(0), spaceName);
					
				} else {
					continue;
				}
			}
			
			
		} catch(FileNotFoundException e){
			System.out.println("File not found, please try again.");
		}
		
	}
	
	
	
	public void makeAdjList() {
		for(int i = 0;i<numRows;i++) {
			for(int j = 0;j<numColumns;j++) {
				if (grid[i][j].isRoom() == false) {
					if (i > 0){
						if (grid[i - 1][j].getInitial() == 'W' || grid[i - 1][j].isDoorway() == true) {
							grid[i][j].addAdjacency(grid[i-1][j]);
						}					
					}
					if (j > 0){
						if (grid[i][j-1].getInitial() == 'W' || grid[i][j-1].isDoorway() == true) {
							grid[i][j].addAdjacency(grid[i][j-1]);
						}
					}
					if (i < numColumns - 1){
						if (grid[i + 1][j].getInitial() == 'W' || grid[i + 1][j].isDoorway() == true) {
							grid[i][j].addAdjacency(grid[i+1][j]);
						}		
					}
					if (j < numColumns - 1){
						if (grid[i][j+1].getInitial() == 'W' || grid[i][j+1].isDoorway() == true) {
							grid[i][j].addAdjacency(grid[i][j+1]);
						}		
					}
				}  //from here we need to figure out how to identify doorways to the the cell if it is a room
				//as well as account for secret passageways
			}
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
				// Read the next line from the layout file
				String line = new String(test.nextLine());
				// Split the line into parts using a comma as a delimiter
				String[] spaces = line.split(",");
				if(firstRow) { 
					numCols = spaces.length; 
					firstRow = false;
				} else {
					if(numCols != spaces.length) {
						
						// Check if the number of columns in each row is consistent
	                    String message = "Column mismatch. column numbers aren't correctly counted between rows. Error is on line " + (numRows + 1) + " of " + finishedLayout;
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
						this.getCell(i, j).setRoom(true);
						
					}
					
					if(roomMap.containsKey(arr[j].charAt(0))) {
						this.getCell(i, j).setInitial(arr[j].charAt(0));
						if(arr[j].length() != 1) {
							switch(arr[j].charAt(1)) {
							
							case '<':
								this.getCell(i, j).setDirection('L');
								this.getCell(i, j).setDoorway(true);
								break;
								
							case '^':
								this.getCell(i, j).setDirection('U');
								this.getCell(i, j).setDoorway(true);
								break;
								
							case '>':
								this.getCell(i, j).setDirection('R');
								this.getCell(i, j).setDoorway(true);
								break;
								
							case 'v':
								this.getCell(i, j).setDirection('D');
								this.getCell(i, j).setDoorway(true);
								break;
								
							case '*':
								this.getCell(i, j).setCenter(true);
								roomMap.get(arr[j].charAt(0)).setCenterCell(this.getCell(i, j));
								break;
								
							case '#':
								this.getCell(i, j).setLabel(true);
								roomMap.get(arr[j].charAt(0)).setLabelCell(this.getCell(i, j));
								break;
								
							default:
								if (roomMap.containsKey(arr[j].charAt(1))) {
									this.getCell(i, j).setSecretPassage(arr[j].charAt(1));
									
									break;
								}
								// Handle invalid second character in the layout
	                            String message = "Invalid second character on cell,  Row: " + i + " Column: " + j + " Character: " + (arr[j].charAt(1)) + ".";
	                            throw new BadConfigFormatException(message);
							}
						}

					} else {
						// Handle cases where the character in the layout is not in the setup
	                    String message = "Character is in our Layout and is not in our Setup  " + arr[j] + " .";
	                    throw new BadConfigFormatException(message);
					}
				}
			}
		}
		
		catch (FileNotFoundException e){
			System.out.println("File not found.");
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
		return numRows;
	}


	public int getNumColumns() {
		return numColumns;
	}


	public void setConfigFiles(String layout, String setup) {
		this.layoutConfigFile = layout;
		this.setupConfigFile = setup;

	}




}
