package clueGame;
import java.util.*;

public class Board {
	private BoardCell[][] grid;
	private int numRows, numColumns;
	private String layoutConfigFile, setupConfigFile;
	private Map<Character, Room> roomMap;

	/*
	 * variable and methods used for singleton pattern
	 */
	private static Board theInstance = new Board();

	// constructor is private to ensure only one can be created
	private Board() {
		super() ;
		roomMap = new HashMap<Character, Room>();
	}

	// this method returns the only Board
	public static Board getInstance() {
		return theInstance;
	}


	/*
	 * initialize the board (since we are using singleton pattern)
	 */
	public void initialize() {
		// Empty
	}

	
	public void loadSetupConfig(){
		// Empty
	}

	
	public void loadLayoutConfig(){
		// Empty
	}

	
	public Room getRoom(char c) {
	    return new Room("Blank");
	}

	
	public Room getRoom(BoardCell cell) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int getNumRows() {
		// TODO Auto-generated method stub
		return numColumns;
	}

	
	
	public int getNumColumns() {
		// TODO Auto-generated method stub
		return numColumns;
	}

	
	
	public BoardCell getCell(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setConfigFiles(String string, String string2) {
		// TODO Auto-generated method stub
		
	}




}
