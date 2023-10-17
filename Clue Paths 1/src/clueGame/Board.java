package clueGame;
import java.util.*;
import java.util.Scanner;
import java.io.*;	
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
		this.loadSetupConfig();
	}

	
	public void loadSetupConfig(){
		try (Scanner sc = new Scanner(new File(setupConfigFile))){
			while (sc.hasNextLine()) {
				String[] temp = sc.nextLine().split(",");
				roomMap.put(temp[2].charAt(0), new Room(temp[1]));
			}
		} catch(FileNotFoundException e) {
			System.out.println("Error: File not found");
		}
		System.out.print(setupConfigFile);
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
		this.layoutConfigFile = string;
		this.setupConfigFile = string2;
		
	}




}
