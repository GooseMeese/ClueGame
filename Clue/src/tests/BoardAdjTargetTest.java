package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTest {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout306.csv", "ClueSetup306.txt");		
		// Initialize will load config files 
		board.initialize();
	}

	// Ensure that player does not move around within room
	// These cells are LIGHT ORANGE on the planning spreadsheet
	@Test
	public void testAdjacenciesRooms()
	{
		// we want to test a couple of different rooms.
		// First, the study that only has a single door but a secret room
		Set<BoardCell> testList = board.getAdjList(19, 18);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(20, 14)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		
		// now test the ballroom (note not marked since multiple test here)
		testList = board.getAdjList(19, 8);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(18, 11)));
		
		// one more room, the kitchen
		testList = board.getAdjList(10, 1);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(13, 2)));
	}

	
	// Ensure door locations include their rooms and also additional walkways
	// These cells are LIGHT ORANGE on the planning spreadsheet
	@Test
	public void testAdjacencyDoor()
	{
		Set<BoardCell> testList = board.getAdjList(13, 2);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(10, 1)));
		assertTrue(testList.contains(board.getCell(14, 2)));
		assertTrue(testList.contains(board.getCell(13, 3)));
		

		testList = board.getAdjList(5, 0);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertTrue(testList.contains(board.getCell(5, 1)));
		assertTrue(testList.contains(board.getCell(6, 0)));
		
		testList = board.getAdjList(12, 15);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(11, 15)));
		assertTrue(testList.contains(board.getCell(13, 15)));
		assertTrue(testList.contains(board.getCell(12, 14)));
		assertTrue(testList.contains(board.getCell(12, 18)));
	}
	
	// Test a variety of walkway scenarios
	// These tests are Dark Orange on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways()
	{
		// Test on bottom edge of board, just one walkway piece
		Set<BoardCell> testList = board.getAdjList(21, 3);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(21, 4)));
		assertTrue(testList.contains(board.getCell(20, 3)));
		
		// Test near a door but not adjacent
		testList = board.getAdjList(6, 2);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(6, 3)));
		assertTrue(testList.contains(board.getCell(6, 1)));
		assertTrue(testList.contains(board.getCell(5, 2)));

		// Test adjacent to walkways
		testList = board.getAdjList(16, 13);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(16, 12)));
		assertTrue(testList.contains(board.getCell(16, 14)));
		assertTrue(testList.contains(board.getCell(15, 13)));
		assertTrue(testList.contains(board.getCell(17, 13)));

		// Test next to unused space
		testList = board.getAdjList(8,13);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(7, 13)));
		assertTrue(testList.contains(board.getCell(9, 13)));
		assertTrue(testList.contains(board.getCell(8, 14)));
	
	}
	
	
	// Tests out of room center, 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsInBathroom() {
		// test a roll of 1
		board.calcTargets(board.getCell(2, 14), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(1, 17)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(2, 14), 3);
		targets= board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(2, 18)));
		assertTrue(targets.contains(board.getCell(1, 19)));	
		assertTrue(targets.contains(board.getCell(3, 17)));
		assertTrue(targets.contains(board.getCell(0, 18)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(2, 14), 4);
		targets= board.getTargets();
		assertEquals(12, targets.size());
		assertTrue(targets.contains(board.getCell(3, 20)));
		assertTrue(targets.contains(board.getCell(4, 17)));	
		assertTrue(targets.contains(board.getCell(0, 19)));
		assertTrue(targets.contains(board.getCell(2, 19)));	
	}
	
	@Test
	public void testTargetsInBar() {
		// test a roll of 1
		board.calcTargets(board.getCell(18, 1), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(20, 5)));
		assertTrue(targets.contains(board.getCell(17, 3)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(18, 1), 3);
		targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(20, 5)));
		assertTrue(targets.contains(board.getCell(17, 3)));	
		assertTrue(targets.contains(board.getCell(5, 17)));
		assertTrue(targets.contains(board.getCell(16, 4)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(18, 1), 4);
		targets= board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCell(14, 3)));
		assertTrue(targets.contains(board.getCell(5, 16)));	
		assertTrue(targets.contains(board.getCell(5, 18)));
		assertTrue(targets.contains(board.getCell(19, 4)));	
	}

	// Tests out of room center, 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsAtDoor() {
		// test a roll of 1, at door
		board.calcTargets(board.getCell(9, 17), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(12, 18)));
		assertTrue(targets.contains(board.getCell(8, 17)));	
		assertTrue(targets.contains(board.getCell(9, 18)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(9, 17), 3);
		targets= board.getTargets();
		assertEquals(12, targets.size());
		assertTrue(targets.contains(board.getCell(12, 18)));
		assertTrue(targets.contains(board.getCell(10, 15)));
		assertTrue(targets.contains(board.getCell(6, 17)));	
		assertTrue(targets.contains(board.getCell(7, 19)));
		assertTrue(targets.contains(board.getCell(8, 15)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(9, 17), 4);
		targets= board.getTargets();
		assertEquals(15, targets.size());
		assertTrue(targets.contains(board.getCell(12, 18)));
		assertTrue(targets.contains(board.getCell(7, 15)));
		assertTrue(targets.contains(board.getCell(9, 13)));	
		assertTrue(targets.contains(board.getCell(5, 17)));
		assertTrue(targets.contains(board.getCell(6, 16)));	
	}

	@Test
	public void testTargetsInWalkway1() {
		// test a roll of 1
		board.calcTargets(board.getCell(19, 3), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(18, 3)));
		assertTrue(targets.contains(board.getCell(20, 3)));	
		assertTrue(targets.contains(board.getCell(19, 4)));	
		
		// test a roll of 2
		board.calcTargets(board.getCell(19, 3), 2);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(17, 3)));
		assertTrue(targets.contains(board.getCell(18, 4)));
		assertTrue(targets.contains(board.getCell(20, 4)));	
		assertTrue(targets.contains(board.getCell(21, 3)));
		
		// test a roll of 4
		board.calcTargets(board.getCell(19, 3), 4);
		targets= board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(15, 3)));
		assertTrue(targets.contains(board.getCell(17, 3)));
		assertTrue(targets.contains(board.getCell(16, 4)));
		assertTrue(targets.contains(board.getCell(16, 2)));
		assertTrue(targets.contains(board.getCell(18, 4)));
		assertTrue(targets.contains(board.getCell(20, 4)));
		assertTrue(targets.contains(board.getCell(21, 3)));

	}

	@Test
	public void testTargetsInWalkway2() {
		// test a roll of 1
		board.calcTargets(board.getCell(19, 14), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(19, 13)));
		assertTrue(targets.contains(board.getCell(20, 14)));	
		
		// test a roll of 2
		board.calcTargets(board.getCell(19, 14), 3);
		targets= board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCell(19, 13)));
		assertTrue(targets.contains(board.getCell(20, 12)));
		assertTrue(targets.contains(board.getCell(17, 13)));
		assertTrue(targets.contains(board.getCell(18, 12)));
		assertTrue(targets.contains(board.getCell(19, 11)));
		
		// test a roll of 4
		board.calcTargets(board.getCell(19, 14), 4);
		targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(19, 12)));
		assertTrue(targets.contains(board.getCell(18, 13)));
		assertTrue(targets.contains(board.getCell(19, 11)));
		assertTrue(targets.contains(board.getCell(16, 13)));
		assertTrue(targets.contains(board.getCell(17, 12)));
		assertTrue(targets.contains(board.getCell(18, 11)));
		
	}

	@Test
	// test to make sure occupied locations do not cause problems
	public void testTargetsOccupied() {
		// test a roll of 3 blocked 3 down
		board.getCell(8, 15).setOccupied(true);
		board.calcTargets(board.getCell(5, 15), 3);
		board.getCell(8, 15).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(12, targets.size());
		assertTrue(targets.contains(board.getCell(6, 15)));
		assertTrue(targets.contains(board.getCell(5, 18)));
		assertTrue(targets.contains(board.getCell(4, 13)));	
		assertFalse( targets.contains( board.getCell(8, 15))) ;
	
		// we want to make sure we can get into a room, even if flagged as occupied
		board.getCell(10, 1).setOccupied(true);
		board.getCell(13, 3).setOccupied(true);
		board.calcTargets(board.getCell(13, 2), 1);
		board.getCell(10, 1).setOccupied(false);
		board.getCell(13, 3).setOccupied(false);
		targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(10, 1)));	
		assertTrue(targets.contains(board.getCell(14, 2)));	

		
		// check leaving a room with a blocked doorway
		board.getCell(18, 11).setOccupied(true);
		board.calcTargets(board.getCell(19, 8), 3);
		board.getCell(18, 11).setOccupied(false);
		targets= board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCell(13, 6)));
		assertTrue(targets.contains(board.getCell(16, 3)));	
		assertTrue(targets.contains(board.getCell(16, 9)));

	}
}
