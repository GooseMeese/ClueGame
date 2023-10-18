package clueGame;

import java.util.HashSet;
import java.util.Set;

import experiment.TestBoardCell;

public class BoardCell {
	private int row, col;
	private Boolean isRoom, isOccupied, doorway, roomCenter, label;
	char type, roomInitial;
	Set<TestBoardCell> adjList;
	DoorDirection direction;
	char secretPassage;
	// Constructor with row and col inputs
	public BoardCell(int row, int col) {
	    super();
	    this.row = row;
	    this.col = col;
	    adjList = new HashSet<TestBoardCell>();
	}

	
	// Adds an adjacent cell to this cells adjacency list
	public void addAdjacency(TestBoardCell cell) {
	    this.adjList.add(cell);
	}

	// Returns the adjacency list for the cell
	public Set<TestBoardCell> getAdjList(){
		return adjList;
	}
	
	// Setter for creating a room cell
	public void setRoom(boolean cell) {
		this.isRoom = cell;;
	}
	
	// Getter for if a space is a room cell
	public boolean isRoom() {
		return isRoom;
	}
	
	// Setter for indicating if a cell is occupied by another player
	public void setOccupied(boolean cell) {
		this.isOccupied = cell;;
	}
	
	// Getter for indicating if a cell is occupied by another player
	public boolean getOccupied() {
		return isOccupied;
	}
	
	public void setCenter(boolean x) {
		this.roomCenter = x;
	}
	
	public void setLabel(boolean x) {
		this.label = x;
	}
	
	public void setDoorway(boolean x) {
		this.doorway = x;
	}
	
	public boolean isRoomCenter() {
		// TODO Auto-generated method stub
		return roomCenter;
	}
	
	public boolean isDoorway() {
		return doorway;
	}
	
	public DoorDirection getDoorDirection() {
		return this.direction;
	}
	
	public boolean isLabel() {
		return label;
	}
	
	public char getSecretPassage() {
		return secretPassage;
	}
	
	public char getInitial() {
		return this.roomInitial;
	}
	
	public void setInitial(char character) {
		this.roomInitial = character;
	}
}
