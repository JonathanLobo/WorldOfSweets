import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;
import java.util.*;

public class PlayerTest{
	Player pl;

	@Before
	public void setUp() {
		pl = new Player("Brand");
		pl.setX(0);
		pl.setY(0);
		pl.setType(1);
	}

	//ensure Player exist
	@Test
	public void testPlayerExist() {
		assertNotNull(pl);
	}

	//ensure players name is correct
	@Test
	public void testPlayerName() {
		assertEquals("Brand", pl.getName());
	}

	//ensure X coordinate is correct
	@Test
	public void testPlayerX() {
		assertEquals(0, pl.getX());
	}

	//ensure Y coordinate is correct
	@Test
	public void testPlayerY() {
		assertEquals(0, pl.getY());
	}
	
	/**
	 * testGetboomerang - ensure boomerang count is correct
	 */
	@Test
	public void testGetboomerang() {
		assertEquals(3, pl.getboomerang());
	}

	/**
	 * testSetBoomerang - ensure boomerang count is correct
	 */
	@Test
	public void testSetBoomerang() {
		pl.setBoomerang(8);
		assertEquals(8, pl.getboomerang());
	}	

	/**
	 * testDecrementboomerang - ensure boomerang count is correct
	 */
	@Test
	public void testDecrementboomerang() {
		pl.decrementboomerang();
		assertEquals(2, pl.getboomerang());
	}
	
	// ensure can get and set player types 
	@Test 
	public void testGetType() {
		assertEquals(1, pl.getType());
	}
	
	// ensure can get and set player types 
	@Test
	public void testSetType() {
		pl.setType(0);
		assertEquals(0, pl.getType()); 
	}
}
