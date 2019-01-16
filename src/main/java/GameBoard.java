
import java.awt.Color;

import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class GameBoard {

	private JPanel container;
	private BoardPanel board;
	private JPanel ui;
	public Space[] spaces;
	private int numPlayers = 0;

	public GameBoard(int numSpaces, int numPlayers) throws IOException {

		if (numPlayers < 2 || numPlayers > 4)
			throw new IllegalArgumentException("Game supports only 2-4 players.");
		else
			this.numPlayers = numPlayers;
		if (numSpaces < 1)
			throw new IllegalArgumentException("Invalid number of spaces");

		spaces = new Space[numSpaces + 2];

		spaces[0] = new Space("start");

		int j = 0;
		for (int i = 0; i < numSpaces; i++) {
			if (i == 9) {
				spaces[i + 1] = new Space("wild", "mint");
			} else if (i == 18) {
				spaces[i + 1] = new Space("wild", "iceCream");
			} else if (i == 27) {
				spaces[i + 1] = new Space("wild", "cottonCandy");
			} else if (i == 37) {
				spaces[i + 1] = new Space("wild", "candyCane");
			} else if (i == 46) {
				spaces[i + 1] = new Space("wild", "lollipop");
			} else {
				if (j % 5 == 0) {
					spaces[i + 1] = new Space("red");
				} else if (j % 5 == 1) {
					spaces[i + 1] = new Space("yellow");
				} else if (j % 5 == 2) {
					spaces[i + 1] = new Space("blue");
				} else if (j % 5 == 3) {
					spaces[i + 1] = new Space("green");
				} else if (j % 5 == 4) {
					spaces[i + 1] = new Space("orange");
				}
				j++;
			}
		}

		spaces[spaces.length - 1] = new Space("finish");

		board = new BoardPanel("assets/board/candyland_short.png", this.numPlayers);
		board.setBackground(Color.WHITE);

		ui = new JPanel();
		ui.setBackground(Color.WHITE);

		ResizableImagePanel title = new ResizableImagePanel("assets/icons/game_title.png");
		ui.add(title);

		ResizableImagePanel drawnCard = new ResizableImagePanel("assets/cards/discard_pile.png");
		ui.add(drawnCard);

		ResizableImagePanel deck = new ResizableImagePanel("assets/cards/card_back.png");
		ui.add(deck);

		container = new JPanel();
		container.add(board);
		container.add(ui);

	}

	public GameBoard(int numSpaces) throws IOException {
		this(numSpaces, 2);
	}

	public GameBoard() throws IOException {
		this(50, 2);
	}

	public JComponent getGUI() {
		return container;
	}

	public BoardPanel getBoardPanel() {
		return board;
	}

	public int getNumSpaces() {
		return spaces.length;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

}
