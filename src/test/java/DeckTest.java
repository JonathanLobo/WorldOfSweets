import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;
import java.util.*;
/**
 *  Test class to show how to use cards and deck
 */
public class DeckTest {

	Deck dk;

	@Before
	public void setup() {
		dk = new Deck(2);
	}

	// ensure correct deck size is returned
	@Test
	public void testSize() {
		assertEquals(2, dk.getSize());
	}

	// ensure can add card to an empty deck
	@Test
	public void testAddEmptyDeck() {
		Card mockCard = Mockito.mock(Card.class);
		assertTrue(dk.add(mockCard));
	}

	// ensure can number of cards is update when card added
	@Test
	public void testNumCardsAdd() {
		Card mockCard = Mockito.mock(Card.class);
		dk.add(mockCard);
		assertEquals(dk.getNumCards(), 1);
	}

	// ensure can number of cards is update when card removed
	@Test
	public void testNumCardsRemove() {
		Card mockCard = Mockito.mock(Card.class);
		dk.add(mockCard);
		dk.remove(mockCard);
		assertEquals(dk.getNumCards(), 0);
	}

	// ensure cannot add a card to a full deck
	@Test
	public void testAddFullDeck() {
		Card mockCard = Mockito.mock(Card.class);
		dk.add(mockCard);
		dk.add(mockCard);
		assertFalse(dk.add(mockCard));
	}

	// ensure can remove a card on top
	// top card should now be null
	@Test
	public void testRemoveTopCard() {
		Card c1 = Mockito.mock(Card.class);
		Card c2 = Mockito.mock(Card.class);
		dk.add(c1);
		dk.add(c2);
		dk.remove(c2);
		assertNull(dk.cardAt(1));
	}

	// ensure can remove bottom card
	// the card above should now be at the bottom
	@Test
	public void testRemoveBottomCard() {
		Card c1 = Mockito.mock(Card.class);
		Card c2 = Mockito.mock(Card.class);
		dk.add(c1);
		dk.add(c2);
		dk.remove(c1);
		assertEquals(dk.cardAt(0), c2);
		assertNull(dk.cardAt(1));
	}

	// ensure cannot remove card that is not in deck
	@Test
	public void testRemoveInvalidCard() {
		boolean thrown = false;
		Card c1 = Mockito.mock(Card.class);
		Card c2 = Mockito.mock(Card.class);
		dk.add(c1);
		dk.add(c1);

		try {
			dk.remove(c2);
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assertTrue(thrown);
	}

	// ensure cardAt throws exception when trying to retrieve
	// an index that is out of the bounds of the deck
	@Test
	public void testCardAtInvalidIndex() {
		boolean thrown = false;
		try {
			dk.cardAt(2);
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assertTrue(thrown);
	}

	// ensure cardAt returns correct card at given index
	// if no card was added at the index, should return null
	@Test
	public void testCardAtNull() {
		assertNull(dk.cardAt(0));
	}

	// ensure cardAt returns correct card at given index
	@Test
	public void testCardAtValidIndex() {
		Card c1 = Mockito.mock(Card.class);
		dk.add(c1);
		dk.add(c1);
		assertEquals(c1, dk.cardAt(1));
	}

	// ensure cannot draw card from an empty deck
	@Test
	public void testDrawFromEmptyDeck() {
		boolean thrown = false;
		try {
			dk.drawCard();
		} catch (NullPointerException e) {
			thrown = true;
		}
		assertTrue(thrown);
	}

	// ensure can draw card from deck
	// should return card at beginning of deck
	@Test
	public void testDrawCard() {
		Card c1 = Mockito.mock(Card.class);
		Card c2 = Mockito.mock(Card.class);
		dk.add(c1);
		dk.add(c2);
		assertEquals(c1, dk.drawCard());
	}

	// ensure isEmpty returns false when deck has cards
	@Test
	public void testNotEmptyDeck() {
		Card c1 = Mockito.mock(Card.class);
		dk.add(c1);
		assertFalse(dk.isEmpty());
	}

	// ensure empty deck returns true
	@Test
	public void testEmptyDeck() {
		Card c1 = Mockito.mock(Card.class);
		dk.add(c1);
		dk.add(c1);
		dk.drawCard();
		dk.drawCard();
		assertTrue(dk.isEmpty());
	}

	public static void main(String[] args) {
     // This is how a deck should be initialized for the standard game
     //   5 colors, 10 single/2 double, 60 total cards
     //   Should shuffle too
     String[] colors = {"red", "yellow", "blue", "green", "orange"};
     Deck deck = new Deck(60);
     for (String color : colors) {
        for (int i = 0; i < 10; i++) {
           Card newCard = new Card(color, "single");
           deck.add(newCard);
        }
        for (int i = 0; i < 2; i++) {
           Card newCard = new Card(color, "double");
           deck.add(newCard);
        }
     }
     deck.shuffle();																					// Shuffle
     Card drawnCard = deck.drawCard();												// Draw
		 System.out.println(drawnCard.toString());								// Print Card
     Deck discard = new Deck(60);															// Discard
     discard.add(drawnCard);

		 // Once deck is empty
		 if(deck.isEmpty()){
			 discard.shuffle();
			 deck = discard;
		 }
	}

}
