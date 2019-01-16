import java.io.Serializable;
import java.util.*;

/**
 * Deck class
 */
public class Deck implements Serializable {

	private int size;
	private int numCards = 0;
	private Card[] cards;

	/**
	 * Initializes a deck.
	 *
	 * @param size
	 *            the size of the deck
	 *
	 * @throws IllegalArgumentException
	 *             if size is negative
	 */
	public Deck(int size) {
		if (size < 0)
			throw new IllegalArgumentException("Size of deck is invalid");

		this.size = size;
		this.cards = new Card[size];
	}

	/**
	 * Returns the size of this deck.
	 *
	 * @return the size of this deck
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Returns the number of cards in this deck.
	 *
	 * @return int cards
	 */
	public int getNumCards() {
		return numCards;
	}

	/**
	 * Adds a card to the deck
	 */
	public boolean add(Card card) {
		int i = 0;
		boolean returnVal = false;
		while (i < cards.length) {
			if (cards[i] == null) {
				cards[i] = card;
				returnVal = true;
				numCards++;
				break;
			}
			i++;
		}
		return returnVal;
	}

	/**
	 * Removes a card from the deck
	 *
	 * @return the card removed
	 */
	public Card remove(Card card) {
		Card removeCard = card;
		int cardLocation = -1;

		// find card
		for (int i = 0; i < cards.length; i++) {
			if (cards[i] == card) {
				removeCard = cards[i];
				cardLocation = i;
			}
		}

		if (cardLocation == -1) {
			throw new IllegalArgumentException("Card is not in deck");
		} else {
			// delete card from deck
			if (cardLocation == cards.length - 1) { // last card
				cards[cardLocation] = null;
			} else {
				for (int i = cardLocation; i < cards.length - 1; i++) { // shift cards
					cards[i] = cards[i + 1];
				}
				cards[cards.length - 1] = null;
			}
			numCards--;
		}
		return removeCard;
	}

	/**
	 * Shuffles the deck (Fisher-Yates/Durstenfeld)
	 */
	public void shuffle() {
		int index;
		Card temp;
		Random rand = new Random();
		for (int i = cards.length - 1; i > 0; i--) {
			index = rand.nextInt(i + 1);
			temp = cards[index];
			cards[index] = cards[i];
			cards[i] = temp;
		}
	}

	/**
	 * Returns the "top" card
	 *
	 * @return the card at the 0th index of the deck
	 */
	public Card drawCard() {
		if (isEmpty()) {
			throw new NullPointerException("Deck is empty");
		}
		return remove(cardAt(0));
	}

	/**
	 * Returns the "dad" card
	 *
	 * @return the "worst" card in the deck
	 */
	public Card drawDadCard(int playerLocation, Space[] spaceArray) {
		if (isEmpty()) {
			throw new NullPointerException("Deck is empty");
		}
		int worstIndex = getWorstIndexInDeck(playerLocation, spaceArray);
		return remove(cardAt(worstIndex));
	}

	/**
	 * Returns worst index in the deck
	 *
	 * @return "worst" index
	 */
	public int getWorstIndexInDeck(int playerLocation, Space[] spaceArray) {
		int worstIndex = 0;
		int tempIndex;
		int tempCardResult;
		int worstCardResult;

		Card worstCard = cardAt(worstIndex);
		Card tempCard;

		for (int i = 1; i < this.getNumCards(); i++) {
			tempIndex = i;
			tempCard = cardAt(tempIndex);

			tempCardResult = getCardLocation(tempCard, playerLocation, spaceArray);
			worstCardResult = getCardLocation(worstCard, playerLocation, spaceArray);

			if (tempCardResult < worstCardResult) { // new card is worse
				worstIndex = tempIndex;
				worstCard = cards[tempIndex];
			}
		}

		return worstIndex;
	}

	/**
	 * Returns the location that a card will send a player to
	 *
	 * @return the intended location
	 */
	public int getCardLocation(Card c, int currentLocation, Space[] spaceArray) {
		int newLocation = 0;
		String cardType = c.type();
		String cardColor = c.color();
		if (cardType.equals("mint")) {
			newLocation = 10;
		} else if (cardType.equals("iceCream")) {
			newLocation = 19;
		} else if (cardType.equals("cottonCandy")) {
			newLocation = 28;
		} else if (cardType.equals("candyCane")) {
			newLocation = 38;
		} else if (cardType.equals("lollipop")) {
			newLocation = 47;
		} else if (cardType.equals("single")) {
			for (int i = currentLocation + 1; i <= currentLocation + 6 && i < spaceArray.length; i++) {
				if (spaceArray[i].getColor().equals(cardColor) || spaceArray[i].getColor().equals("finish")) {
					newLocation = i;
					break;
				}
			}
		} else if (cardType.equals("double")) {
			if ((currentLocation + 5) >= spaceArray.length) {
				newLocation = spaceArray.length - 1;
			} else {
				for (int i = currentLocation + 6; i <= currentLocation + 11 && i < spaceArray.length; i++) {
					if (spaceArray[i].getColor().equals(cardColor) || spaceArray[i].getColor().equals("finish")) {
						newLocation = i;
						break;
					}
				}
			}
		}

		return newLocation;
	}

	/**
	 * Returns the card at an index
	 *
	 * @return the card at the index
	 */
	public Card cardAt(int index) {
		if (index > cards.length - 1) {
			throw new IllegalArgumentException("Invalid index in deck");
		}
		return cards[index];
	}

	/**
	 * Returns the if the deck is empty
	 *
	 * @return true if empty, false otherwise
	 */
	public boolean isEmpty() {
		if (cardAt(0) == null) {
			return true;
		}
		return false;
	}

	/**
	 * Returns a string representation of this deck.
	 *
	 * @return a string representation of this card
	 */
	public String toString() {
		return "Deck of size: " + size;
	}
}
