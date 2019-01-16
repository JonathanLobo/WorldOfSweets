import java.io.Serializable;

public class Game implements Serializable {

	private static final long serialVersionUID = 1L;
	public int numberOfPlayers;
	private int currentPlayer;
	private String[] names;
	public String[] colors;
	public int[] playerTypes;
	public Space[] spaceArray;
	private Deck cardDeck;
	private Deck discardDeck;
	private final int deckSize = 70;

	/**
	 * Game - The game for a given number of players
	 *
	 * @param int
	 *            number of players 2-4
	 *
	 */
	public Game(int numPlayers, String[] name, String[] color, int[] types) {
		numberOfPlayers = numPlayers;
		currentPlayer = 0;
		names = name;
		colors = color;
		playerTypes = types;
		cardDeck = initializeDeck(); // card deck shuffled
		discardDeck = new Deck(deckSize); // discard deck initially empty
	}

	public void setSpaceArray(Space[] sa) {
		spaceArray = sa;
	}

	/**
	 * getNumberofPlayers - the number of player in the game
	 *
	 * @return current number of players
	 */
	public int getNumberofPlayers() {
		return numberOfPlayers;
	}

	/**
	 * nextTurn - updates current player to next person's turns
	 *
	 */
	public void nextTurn() {
		currentPlayer = (currentPlayer + 1) % numberOfPlayers;
	}

	/**
	 * getCurrentPlayer - gets the current player of the game
	 *
	 * @return current player
	 */
	public int getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * findPlayer - gets the current player of the game
	 *
	 * @return current player
	 */
	public int findPlayer(String na) {
		int playerNum = -1;
		for (int i = 0; i < names.length; i++) {
			if (na.equals(names[i])) {
				playerNum = i;
				break;
			}
		}
		return playerNum;
	}

	/**
	 * getCurrentPlayerName
	 *
	 * @return current player's name
	 */
	public String getCurrentPlayerName() {
		return names[currentPlayer];
	}

	/**
	 * getAllNames
	 *
	 * @return String of names
	 */
	public String getAllNames() {
		String allNames = "";
		for (int i = 0; i < numberOfPlayers - 1; i++) {
			allNames = allNames + (i + 1) + ": " + names[i] + ", ";
		}
		allNames = allNames + (numberOfPlayers) + ": " + names[numberOfPlayers - 1];
		return allNames;
	}

	/**
	 * getPlayerName - returns the name of a player at a given index
	 *
	 * @param int index
	 * @return String name
	 */
	public String getPlayerName(int index) {
		return names[index];
	}

	/**
	 * getPlayerType - returns the type of a player at a given index
	 *
	 * @param int index
	 * @return String name
	 */
	public int getPlayerType(int index) {
		return playerTypes[index];
	}

	/**
	 * getCardDeck - returns the current deck
	 *
	 * @return Deck card deck
	 */
	public Deck getCardDeck() {
		return cardDeck;
	}

	/**
	 * getDiscardDeck - returns the discard pile
	 *
	 * @return Deck discard
	 */
	public Deck getDiscardDeck() {
		return discardDeck;
	}

	/**
	 * initializeDeck - Deck should be initialized for the standard game with 5
	 * colors, 10 single/2 double, 5 "skip turn" cards, 3 "Go to X" 70 total cards
	 * and shuffled
	 */
	public Deck initializeDeck() {

		String[] colors = { "red", "yellow", "blue", "green", "orange" };
		Deck mainDeck = new Deck(deckSize);
		Card temp;
		for (String color : colors) {
			for (int i = 0; i < 10; i++) {
				temp = new Card(color, "single");
				mainDeck.add(temp);
			}
			for (int i = 0; i < 2; i++) {
				temp = new Card(color, "double");
				mainDeck.add(temp);
			}
		}

		for (int i = 0; i < 5; i++) {
			temp = new Card("wild", "skip");
			mainDeck.add(temp);
		}

		mainDeck.add(new Card("wild", "iceCream"));
		mainDeck.add(new Card("wild", "cottonCandy"));
		mainDeck.add(new Card("wild", "mint"));
		mainDeck.add(new Card("wild", "candyCane"));
		mainDeck.add(new Card("wild", "lollipop"));

		mainDeck.shuffle();
		return mainDeck;
	}

	/**
	 * draw - draw a card from the main deck and place in discard pile if card deck
	 * is empty, reshuffle the discard pile and replace the main deck
	 *
	 * @return card drawn
	 */
	public Card draw(String playerName, int currentLocation, boolean boomerang) {
		Card drawnCard;
		if (cardDeck.isEmpty()) {
			discardDeck.shuffle();
			cardDeck = discardDeck;
			discardDeck = new Deck(deckSize);
		}

		if (playerName.equals("Dad") && !boomerang) {
			drawnCard = cardDeck.drawDadCard(currentLocation, spaceArray);
		} else {
			drawnCard = cardDeck.drawCard();
		}
		discardDeck.add(drawnCard);
		return drawnCard;
	}
}
