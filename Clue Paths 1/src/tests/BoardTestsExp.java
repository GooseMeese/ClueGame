package tests;
import java.util.*;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import experiment.*;

class BoardTestsExp {
	TestBoard board;
	
	@BeforeEach
	public void setUp() {
		board = new TestBoard();
	}
	
	
	
	/*
	 * Test adjacencies for different player locations
	 * Test centers and edges
	 */
	// Tests for adjacencies in the 0,0 corner cell
	@Test
	public void testAdjacency1() {
		TestBoardCell cell = board.getCell(0, 0);
		if (cell == null) {
			Assert.assertEquals(0, 1);
			return;
		}
		cell.addAdjacency(board.getCell(1, 0));
	    cell.addAdjacency(board.getCell(0, 1));
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(1, 0)));
		Assert.assertTrue(testList.contains(board.getCell(0, 1)));
		Assert.assertEquals(2, testList.size());
	}
	
	
	// Tests for adjacencies in the (3,3) corner cell
	@Test
	public void testAdjacency2() {
		TestBoardCell cell = board.getCell(3, 3);
		if (cell == null) {
			Assert.assertEquals(0, 1);
			return;
		}
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(3, 2)));
		Assert.assertTrue(testList.contains(board.getCell(2, 3)));
		Assert.assertEquals(2, testList.size());
	}
	
	
	// Tests for adjacencies in the (1,1) middle cell
	@Test
	public void testAdjacency3() {
		TestBoardCell cell = board.getCell(1, 1);
		if (cell == null) {
			Assert.assertEquals(0, 1);
			return;
		}
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(0, 1)));
		Assert.assertTrue(testList.contains(board.getCell(1, 0)));
		Assert.assertTrue(testList.contains(board.getCell(1, 2)));
		Assert.assertTrue(testList.contains(board.getCell(2, 1)));
		Assert.assertEquals(4, testList.size());
	}
	
	
	// Tests for adjacencies in the (1,3) edge cell
	@Test
	public void testAdjacency4() {
		TestBoardCell cell = board.getCell(1, 3);
		if (cell == null) {
			Assert.assertEquals(0, 1);
			return;
		}
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(0, 3)));
		Assert.assertTrue(testList.contains(board.getCell(1, 2)));
		Assert.assertTrue(testList.contains(board.getCell(2, 3)));
		Assert.assertEquals(3, testList.size());
	}

	
	// Tests for adjacencies in the (2,0) edge cell
	@Test
	public void testAdjacency5() {
		TestBoardCell cell = board.getCell(2, 0);
		if (cell == null) {
			Assert.assertEquals(0, 1);
			return;
		}
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(board.getCell(1, 0)));
		Assert.assertTrue(testList.contains(board.getCell(2, 1)));
		Assert.assertTrue(testList.contains(board.getCell(3, 0)));
		Assert.assertEquals(3, testList.size());
	}
	
	
	
	
	
	/*
	 * Test targets with several rolls and start locations
	 */
	@Test
	//tests targets for a roll of 1
	public void testTargetsNormal1() {
		TestBoardCell cell = board.getCell(0, 0);
		if (cell == null) {
			Assert.assertEquals(0, 1);
			return;
		}
		board.calcTargets(cell, 1);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertTrue(targets.contains(board.getCell(0, 1)));
		Assert.assertTrue(targets.contains(board.getCell(1, 0)));
		Assert.assertEquals(2, targets.size());
	}
	
	@Test
	public void testTargetsNormal2() {
		TestBoardCell cell = board.getCell(0, 0);
		if (cell == null) {
			Assert.assertEquals(0, 1);
			return;
		}
		Set<TestBoardCell> targets = board.getTargets();
		board.calcTargets(cell, 2);
		Assert.assertTrue(targets.contains(board.getCell(0, 2)));
		Assert.assertTrue(targets.contains(board.getCell(2, 0)));
		Assert.assertTrue(targets.contains(board.getCell(1, 1)));
		Assert.assertEquals(3, targets.size());
	}
	
	@Test
	public void testTargetsNormal3() {
		TestBoardCell cell = board.getCell(0, 0);
		
		if (cell == null) {
			Assert.assertEquals(0, 1);
			return;
		}
		Set<TestBoardCell> targets = board.getTargets();
		board.calcTargets(cell, 3);
		Assert.assertTrue(targets.contains(board.getCell(0, 3)));
		Assert.assertTrue(targets.contains(board.getCell(3, 0)));
		Assert.assertTrue(targets.contains(board.getCell(2, 1)));
		Assert.assertTrue(targets.contains(board.getCell(1, 2)));
		Assert.assertTrue(targets.contains(board.getCell(0, 1)));
		Assert.assertTrue(targets.contains(board.getCell(1, 0)));
		Assert.assertEquals(6, targets.size());
	}
	

	@Test
	public void testTargetsNormal4() {
		TestBoardCell cell = board.getCell(0, 0);
		
		if (cell == null) {
			Assert.assertEquals(0, 1);
			return;
		}
		Set<TestBoardCell> targets = board.getTargets();
		board.calcTargets(cell, 4);
		Assert.assertTrue(targets.contains(board.getCell(0, 2)));
		Assert.assertTrue(targets.contains(board.getCell(1, 1)));
		Assert.assertTrue(targets.contains(board.getCell(1, 3)));
		Assert.assertTrue(targets.contains(board.getCell(2, 0)));
		Assert.assertTrue(targets.contains(board.getCell(2, 2)));
		Assert.assertTrue(targets.contains(board.getCell(3, 1)));
		Assert.assertEquals(6, targets.size());
	}
	
	@Test
	public void testTargetsNormal5() {
		TestBoardCell cell = board.getCell(2, 2);
		if (cell == null) {
			Assert.assertEquals(0, 1);
			return;
		}
		Set<TestBoardCell> targets = board.getTargets();
		board.calcTargets(cell, 5);
		Assert.assertTrue(targets.contains(board.getCell(0, 1)));
		Assert.assertTrue(targets.contains(board.getCell(0, 3)));
		Assert.assertTrue(targets.contains(board.getCell(1, 0)));
		Assert.assertTrue(targets.contains(board.getCell(1, 2)));
		Assert.assertTrue(targets.contains(board.getCell(2, 1)));
		Assert.assertTrue(targets.contains(board.getCell(2, 3)));	
		Assert.assertTrue(targets.contains(board.getCell(3, 0)));
		Assert.assertTrue(targets.contains(board.getCell(3, 2)));
		Assert.assertEquals(8, targets.size());
	}
	
	@Test
	public void testTargetsNormal6() {
		TestBoardCell cell = board.getCell(3, 3);
		if (cell == null) {
			Assert.assertEquals(0, 1);
			return;
		}
		Set<TestBoardCell> targets = board.getTargets();
		board.calcTargets(cell, 6);
		Assert.assertTrue(targets.contains(board.getCell(0, 0)));
		Assert.assertTrue(targets.contains(board.getCell(0, 2)));
		Assert.assertTrue(targets.contains(board.getCell(1, 1)));
		Assert.assertTrue(targets.contains(board.getCell(1, 3)));
		Assert.assertTrue(targets.contains(board.getCell(2, 0)));
		Assert.assertTrue(targets.contains(board.getCell(2, 2)));
		Assert.assertTrue(targets.contains(board.getCell(3, 1)));
		Assert.assertEquals(7, targets.size());
	}
	
	@Test
	public void testTargetsNormal7() {
		TestBoardCell cell = board.getCell(2, 1);
		
		if (cell == null) {
			Assert.assertEquals(0, 1);
			return;
		}
		Set<TestBoardCell> targets = board.getTargets();
		board.calcTargets(cell, 2);
		Assert.assertTrue(targets.contains(board.getCell(0, 1)));
		Assert.assertTrue(targets.contains(board.getCell(1, 0)));
		Assert.assertTrue(targets.contains(board.getCell(1, 2)));
		Assert.assertTrue(targets.contains(board.getCell(2, 3)));
		Assert.assertTrue(targets.contains(board.getCell(3, 0)));
		Assert.assertTrue(targets.contains(board.getCell(3, 2)));
		Assert.assertEquals(6, targets.size());
	}
	
	
	
	/*
	 * Test for targets when a room is involved
	 */
	@Test
	// Tests for targets with a room at (0,2), a roll of 2, and a starting position at (0,0)
	public void testTargetsRoom1() {
		TestBoardCell cell = board.getCell(0, 0);
		if (cell == null) {
			Assert.assertEquals(0, 1);
			return;
		}
		board.getCell(0, 2).setRoom(true);
		board.calcTargets(cell, 2);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertTrue(targets.contains(board.getCell(0, 2)));
		Assert.assertTrue(targets.contains(board.getCell(1, 1)));
		Assert.assertTrue(targets.contains(board.getCell(2, 0)));
		Assert.assertEquals(3, targets.size());
	}
	
	@Test
	// Tests for targets with a room at (1,0), a roll of 3, and a starting position at (2,2)
	public void testTargetsRoom2() {
		TestBoardCell cell = board.getCell(2, 2);
		if (cell == null) {
			Assert.assertEquals(0, 1);
			return;
		}
		board.getCell(1, 0).setRoom(true);
		board.calcTargets(cell, 3);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertTrue(targets.contains(board.getCell(0, 1)));
		Assert.assertTrue(targets.contains(board.getCell(0, 3)));
		Assert.assertTrue(targets.contains(board.getCell(1, 0)));
		Assert.assertTrue(targets.contains(board.getCell(1, 2)));
		Assert.assertTrue(targets.contains(board.getCell(2, 1)));
		Assert.assertTrue(targets.contains(board.getCell(2, 3)));
		Assert.assertTrue(targets.contains(board.getCell(3, 0)));
		Assert.assertTrue(targets.contains(board.getCell(3, 2)));
		Assert.assertEquals(8, targets.size());
	}
	
	
	
	
	
	/*
	 * Test for targets when an interfering occupied space is involved
	 */
	@Test
	// Tests for targets when the cell at (1,2) is occupied and the player rolls a 6 on the cell (1,0)
	public void testTargetsOccupied1() {
		TestBoardCell cell = board.getCell(1, 0);
		if (cell == null) {
			Assert.assertEquals(0, 1);
			return;
		}
		board.getCell(1, 2).setOccupied(true);
		board.calcTargets(cell, 6);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertTrue(targets.contains(board.getCell(0, 1)));
		Assert.assertTrue(targets.contains(board.getCell(0, 3)));
		Assert.assertTrue(targets.contains(board.getCell(2, 1)));
		Assert.assertTrue(targets.contains(board.getCell(2, 3)));
		Assert.assertTrue(targets.contains(board.getCell(3, 0)));
		Assert.assertTrue(targets.contains(board.getCell(3, 2)));
		Assert.assertEquals(6, targets.size());
	}
	
	@Test
	// Tests for targets when the cell at (3,3) is occupied and the player rolls a 5 on the cell (2,1)
	public void testTargetsOccupied2() {
		TestBoardCell cell = board.getCell(2, 1);
		if (cell == null) {
			Assert.assertEquals(0, 1);
			return;
		}
		board.getCell(3, 3).setOccupied(true);
		board.calcTargets(cell, 5);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertTrue(targets.contains(board.getCell(0, 0)));
		Assert.assertTrue(targets.contains(board.getCell(0, 2)));
		Assert.assertTrue(targets.contains(board.getCell(1, 1)));
		Assert.assertTrue(targets.contains(board.getCell(1, 3)));
		Assert.assertTrue(targets.contains(board.getCell(2, 0)));
		Assert.assertTrue(targets.contains(board.getCell(2, 2)));
		Assert.assertTrue(targets.contains(board.getCell(3, 1)));
		Assert.assertEquals(7, targets.size());
	}

	
	
	
	
	/*
	 * Test a situation where there may be a cell occupied by a room and another that is occupied by an opponent
	 */
	@Test
	// Tests for targets when there is a room at (2,0), another player at (0,2), a dice roll of 2, and a starting location of (0,0)
	public void testTargetsMixed1() {
		TestBoardCell cell = board.getCell(0, 0);
		if (cell == null) {
			Assert.assertEquals(0, 1);
			return;
		}
		board.getCell(0, 2).setOccupied(true);
		board.getCell(2, 0).setRoom(true);
		board.calcTargets(cell, 2);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertTrue(targets.contains(board.getCell(1, 1)));
		Assert.assertTrue(targets.contains(board.getCell(2, 0)));
		Assert.assertEquals(2, targets.size());
	}
	
	
	@Test
	// Tests for targets when there is a room at (2,1), another player at (1,2), a dice roll of 1, and a starting location of (2,2)
	public void testTargetsMixed2() {
		TestBoardCell cell = board.getCell(2, 2);
		if (cell == null) {
			Assert.assertEquals(0, 1);
			return;
		}
		board.getCell(1, 2).setOccupied(true);
		board.getCell(2, 1).setRoom(true);
		board.calcTargets(cell, 1);
		Set<TestBoardCell> targets = board.getTargets();
		Assert.assertTrue(targets.contains(board.getCell(2, 1)));
		Assert.assertTrue(targets.contains(board.getCell(2, 3)));
		Assert.assertTrue(targets.contains(board.getCell(3, 2)));
		Assert.assertEquals(3, targets.size());
	}

	

}

