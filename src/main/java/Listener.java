import java.util.*;

/**
 *  Listener Class
 */
public class Listener {
    public boolean cardDrawn;
    public boolean tokenMoved;
	public boolean inPlay;

    /**
     * Initializes a Listener.
     */
    public Listener() {
        cardDrawn = false;
        tokenMoved = false;
		inPlay = true;
    }
}
