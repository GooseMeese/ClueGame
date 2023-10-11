package clueGame;
import java.util.*;

public class Board {
	private BoardCell[][] grid;
	private int numRows;
	private int numColumns;
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character, Room> roomMap;

	/*
	 * variable and methods used for singleton pattern
	 */
	private static Board theInstance = new Board();

	// constructor is private to ensure only one can be created
	private Board() {
		super() ;
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
	    return roomMap.get(c);
	}

	
	public void getNumRows() {
		// TODO Auto-generated method stub
		return;
	}

	
	
	public void getNumColumns() {
		// TODO Auto-generated method stub
		return;
	}

	
	
	public BoardCell getCell(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setConfigFiles(String string, String string2) {
		// TODO Auto-generated method stub
		
	}


}
