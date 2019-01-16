import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;
import java.util.*;

public class GameTest {
	Game gm;
	int deckSize = 70;
	String playerName = "Player A";
	int currentLocation = 2;

	@Before
	public void setup() {
		String names[] = { "a", "b" };
		String colors[] = { "red", "green" };
		int types[] = { 0, 0};
		gm = new Game(2, names, colors, types);
	}

	// ensure game created with the correct amount of players
	@Test
	public void initalConditions() {
		assertEquals(gm.getNumberofPlayers(), 2);
	}

	// ensures current player is correct
	@Test
	public void initalPlayerName() {
		assertEquals(gm.getCurrentPlayerName(), "a");
	}

	// ensures current player is correct
	@Test
	public void initalPlayerNumber() {
		assertEquals(gm.getCurrentPlayer(), 0);
	}

	// ensures that the next turn is correct
	@Test
	public void nextTurn() {
		gm.nextTurn();
		assertEquals(gm.getCurrentPlayer(), 1);
	}

	// ensures that players get reset
	@Test
	public void nextTurnOrginal() {
		gm.nextTurn();
		gm.nextTurn();
		assertEquals(gm.getCurrentPlayer(), 0);
	}

	// ensure to string works
	@Test
	public void testString() {
		assertEquals(gm.getAllNames(), "1: a, 2: b");
	}

	// ensure main deck has all cards added
	@Test
	public void testAllCards() {
		Deck mainDeck = gm.getCardDeck();
		assertEquals(mainDeck.getNumCards(), deckSize);
	}

	// ensure main deck has all cards added
	@Test
	public void testDiscardEmpty() {
		Deck discardPile = gm.getDiscardDeck();
		assertTrue(discardPile.isEmpty());
	}

	// ensure 8 wild cards
	@Test
	public void testWildCardBalance() {
		int numSpecialCards = 0;
		for (int i = 0; i < deckSize; i++) {
			if ("wild".equals(gm.draw(playerName, currentLocation, false).color()))
				numSpecialCards++;
		}
		assertEquals(numSpecialCards, 10);
	}

	// ensure 10 single/2 double
	@Test
	public void testCardBalance() {
		int numSingles = 0;
		int numDoubles = 0;
		for (int i = 0; i < deckSize; i++) {
			Card drawn = gm.draw(playerName, currentLocation, false);
			switch (drawn.type()) {
			case "single":
				numSingles++;
				break;
			case "double":
				numDoubles++;
				break;
			}
		}
		assertEquals(numSingles / numDoubles, 5);
	}

	// ensure 5 unique cards
	@Test
	public void testSpecialCardBalance() {
		int numSpecial = 0;
		for (int i = 0; i < deckSize; i++) {
			Card drawn = gm.draw(playerName, currentLocation, false);
			if (drawn.type().equals("iceCream") || drawn.type().equals("cottonCandy") || drawn.type().equals("mint")
					|| drawn.type().equals("candyCane") || drawn.type().equals("lollipop")) {
				numSpecial++;
			}
		}
		assertEquals(numSpecial, 5);
	}

	// ensure when draw from deck, the card is added to the discard pile
	@Test
	public void testDrawDiscard() {
		Card drawn = gm.draw(playerName, currentLocation, false);
		Deck discard = gm.getDiscardDeck();
		assertEquals(discard.cardAt(0), drawn);
	}

	// ensure when main deck is empty, it gets reshuffles
	// the deck discard pile should be reset
	@Test
	public void testDrawShuffle() {
		for (int i = 0; i < deckSize; i++) {
			gm.draw(playerName, currentLocation, false);
		}
		gm.draw(playerName, currentLocation, false);
		Deck discard = gm.getDiscardDeck();
		assertEquals(discard.getNumCards(), 1);
	}

}
