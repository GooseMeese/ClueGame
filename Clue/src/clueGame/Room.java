package clueGame;

import java.util.*;

public class Room {
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	private BoardCell adjRoomCenterThroughSecretPassage;
	private Set<BoardCell> doorways;

	//Default constructor
	public Room() {
		super();
		doorways = new HashSet<BoardCell>();
	}

	//Overloaded constructor takes name and sets it
	public Room(String name) {
		super();
		this.name = name;
		doorways = new HashSet<BoardCell>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BoardCell getLabelCell() {
		return labelCell;
	}

	public BoardCell getCenterCell() {
		return centerCell;
	}
	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}
	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}

	public BoardCell getAdjRoomCenterThroughSecretPassage() {
		return adjRoomCenterThroughSecretPassage;
	}

	public void setAdjRoomCenterThroughSecretPassage(BoardCell adjRoomCenterThroughSecretPassage) {
		this.adjRoomCenterThroughSecretPassage = adjRoomCenterThroughSecretPassage;
	}

	public void addDoorway(BoardCell door) {
		doorways.add(door);
	}

	public Set<BoardCell> getDoorways() {
		return doorways;
	}


}
