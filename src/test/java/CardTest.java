import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;
import java.util.*;

public class CardTest{
	Card cd;
	Card special;


	@Before
	public void setUp() {
		cd = new Card("red", "single");
		special = new Card("wild", "skip");
	}

	//ensure card exist
	@Test
	public void testCardExist() {
		assertNotNull(cd);
	}

  //ensure card color work correctly
	@Test
	public void testCardColor() {
		assertEquals("red", cd.color());
	}

	//ensure card type works correctly
	@Test
	public void testCardType() {
		assertEquals("single", cd.type());
	}

	// ensure toString works correctly
	@Test
	public void testCardToString() {
		assertEquals("red single", cd.toString());
	}

	//ensure that special card color works
	@Test
	public void testSpecialCardColor(){
		assertEquals("wild", special.color());
	}

  //ensure that special card get get correct type
	@Test
	public void testSpecialCardType(){
		assertEquals("skip", special.type());
	}

}
