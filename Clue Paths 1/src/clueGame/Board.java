package clueGame;
import java.util.*;

public class Board {
	private BoardCell[][] grid;
	private int numRows;
	private int numColumns;
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character, Room> roomMap;
	private static Board theInstance;
	
	// Default Constructor
	public Board() {
		super();
	}
	
	
	public void initialize() {
		// Empty
	}
	
	public void loadSetupConfig(){
		// Empty
	}
	
	public void loadLayoutConfig(){
		// Empty
	}
	

}
