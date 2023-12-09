package clueGame;

import java.util.HashSet;
import java.util.Set;

import experiment.TestBoardCell;

public class BoardCell {
	private int row, col;
	private Boolean isRoom, isOccupied, roomCenter, roomLabel;
	public Boolean isSecretPassage = false;
	private char roomInitial;
	Set<BoardCell> adjList;
	DoorDirection doorDirection;
	char secretPassage;
	
	// Constructor with row and col inputs
	public BoardCell(int row, int col) {
	    super();
	    this.row = row;
	    this.col = col;
	    adjList = new HashSet<BoardCell>();
		this.doorDirection = DoorDirection.NONE;
		this.isRoom = false;
	    this.isOccupied = false;
	    this.roomCenter = false;
	    this.roomLabel = false;
	}
	
	// Sets the direction of a door based off the character given
	public void setDoorDirection(char c) {
		switch(c) {
			case 'U':
				this.doorDirection = DoorDirection.UP;
				break;
			case 'D':
				this.doorDirection = DoorDirection.DOWN;
				break;
			case 'L':
				this.doorDirection = DoorDirection.LEFT;
				break;
			case 'R':
				this.doorDirection = DoorDirection.RIGHT;
				break;
		}
		return;
	}
	
	// Adds an adjacent cell to this cells adjacency list
	public void addAdjacency(BoardCell grid) {
	    this.adjList.add(grid);
	}
	
	// Returns the adjacency list for the cell
	public Set<BoardCell> getAdjList(){
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
	
	public void setCenter() {
		roomCenter = true;
		return;
	}
	
	public void setLabel() {
		roomLabel = true;
	}
	
	
	public boolean isRoomCenter() {
		return roomCenter;
	}
	
	public boolean isDoorway() {
		if(doorDirection != DoorDirection.NONE) {
			return true;
		}
		return false;
	}
	
	public DoorDirection getDoorDirection() {
		return this.doorDirection;
	}
	
	public boolean isLabel() {
		return roomLabel;
	}
	
	public char getSecretPassage() {
		return secretPassage;
	}
	
	public void setSecretPassage(char room) {
		this.isSecretPassage = true;
		this.secretPassage = room;
	}
	
	public char getInitial() {
		return this.roomInitial;
	}
	
	public void setInitial(char character) {
		this.roomInitial = character;
	}
	
}
