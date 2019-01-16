import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;
import java.util.*;
import java.io.IOException;

public class GameBoardTest {

	//ensure game created with the correct amount of spaces
	@Test
	public void testCorrectNumSquares() throws IOException{
		GameBoard gm = new GameBoard(55, 2);
		assertEquals(gm.getNumSpaces(), 57);
	}

	//ensure game throws an error
	@Test(expected = IllegalArgumentException.class)
	public void testIncorrectNumSquares()throws IOException{
		GameBoard gm = new GameBoard(0, 2);
	}

	//ensure game created with the correct amount of players
	@Test
	public void testCorrectNumPlayers()throws IOException{
		GameBoard gm = new GameBoard(55, 2);
		assertEquals(gm.getNumPlayers(), 2);
	}

	//ensure game throws an error
	@Test(expected = IllegalArgumentException.class)
	public void testIncorrectNumPlayers()throws IOException{
		GameBoard gm = new GameBoard(55, 8);
	}

	//ensure game has a cottonCandy square
	@Test
	public void testCottonCandySquare()throws IOException{
		GameBoard gm = new GameBoard(55, 3);
		assertEquals("cottonCandy", gm.spaces[28].getType());
	}
	
	//ensure game has a mint square
	@Test
	public void testMintSquare()throws IOException{
		GameBoard gm = new GameBoard(55, 3);
		assertEquals("mint", gm.spaces[10].getType());
	}
}
