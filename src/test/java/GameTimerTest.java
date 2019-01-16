
import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;
import java.util.*;

public class GameTimerTest {

    Game gm;
    GameFrame gf;
    GameTimer timer;

    @Before
    public void setup() {
        String names[] = {"a", "b"};
        String colors[] = {"red", "green"};
		int types[] = {0,0};
		gm = new Game(2, names, colors, types);
        Listener listener = new Listener();
        gf = new GameFrame(listener);
        timer = new GameTimer(gf);

    }

    // ensure can create a timer with a given start time
    @Test
    public void testStartTime() {
        GameTimer gt = new GameTimer(gf, 5);
        assertEquals(gt.getTime(), 5);
    }

    // ensure when a timer is made without a specific start time
    // it defaults to 0
    @Test
    public void testStartZero() {
        assertEquals(timer.getTime(), 0);
    }

    // ensure formatTime produces correct time
    @Test
    public void testFormatTime() {
        assertEquals("00:43:59", timer.formatTime(2639));
    }

    // ensure formatTime produces correct time
    @Test
    public void testFormatZero() {
        assertEquals("00:00:00", timer.formatTime(0));
    }
}
