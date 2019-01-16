import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;
import java.util.*;

public class SpaceTest{
	Space sp;

	@Before
	public void setUp() {
		sp = new Space("wild", "mint");
	}

	//ensure the first constructor works
	@Test
	public void testConstructorWorks(){
		assertNotNull(sp);
	}
	//ensure the second constructor works
	@Test
	public void testConstructor2Works(){
		Space sp2 = new Space("red");
		assertNotNull(sp);
	}

	//ensure space color
	@Test
	public void testSpaceColor() {
		assertEquals("wild", sp.getColor());
	}

	//ensure space type
	@Test
	public void testSpaceType() {
		assertEquals("mint", sp.getType());
	}

}
