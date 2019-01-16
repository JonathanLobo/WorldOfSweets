import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

import java.util.*;

/**
 * Subclass of JFrame that generates a playable World of Sweets board with all
 * the associated buttons and status indicators
 */
public class GameFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private GameBoard gb;
	public PlayerTracker pt;
	private GridBagConstraints gbc;
	public Listener listener;
	public ResizableImagePanel drawnCard;
	public ResizableImagePanel deck;
	public JButton saveButton;
	public JButton loadButton;
	public JButton boomerangButton;
	public JLabel timerText;
	public JLabel p1b;
	public JLabel p2b;
	public JLabel p3b;
	public JLabel p4b;

	/**
	 * Constructor for a GameFrame object
	 *
	 * @param l
	 *            Listener to attach to the GameFrame object
	 */
	public GameFrame(Listener l) {
		listener = l;
		mainPanel = new JPanel(new GridBagLayout());
		setTitle("World of Sweets");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Starts a new World of Sweets game
	 *
	 * @param numberOfPlayers
	 *            The number of players in the game
	 * @param game
	 *            The Game object containing the game's logic
	 * @throws Catch
	 *             exceptions from loading images
	 */
	public void startNewGame(int numberOfPlayers, Game game, String[] colors, int mode) throws IOException {
		// Generate the main JPanel to add to the JFrame
		mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBackground(Color.WHITE);

		// Create various GUI elements
		gb = new GameBoard(55, numberOfPlayers);
		pt = new PlayerTracker(game);
		gbc = new GridBagConstraints();

		// Split up the GameBoard GUI element into its various components
		JComponent board = gb.getGUI();
		Component[] components = board.getComponents();
		BoardPanel brd = (BoardPanel) components[0];
		JPanel ui = (JPanel) components[1];
		Component[] uiComponents = ui.getComponents();
		ResizableImagePanel title = (ResizableImagePanel) uiComponents[0];
		drawnCard = (ResizableImagePanel) uiComponents[1];
		deck = (ResizableImagePanel) uiComponents[2];

		// Create a bevel for the deck button
		BevelBorder raised = new BevelBorder(BevelBorder.RAISED);
		BevelBorder lowered = new BevelBorder(BevelBorder.LOWERED);
		deck.setBorder(raised);

		// Create save and load JButtons
		saveButton = new JButton("Save Game");
		loadButton = new JButton("Load Game");
		boomerangButton = new JButton("Use a Boomerang");
		JPanel gamePanel = new JPanel(new GridBagLayout());
		gamePanel.setBackground(Color.WHITE);

		// GridBagConstraints for the board GUI element
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.75;
		gbc.weighty = 1;
		gamePanel.add(brd, gbc);

		// JPanel holding interactive button elements
		JPanel uiElements = new JPanel(new GridBagLayout());
		uiElements.setBackground(Color.WHITE);

		// GridBagConstraints for the title
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.insets = new Insets(10, 0, 10, 0);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0.28;
		uiElements.add(title, gbc);

		// GridBagConstraints for the last drawn card element
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 0.3;
		uiElements.add(drawnCard, gbc);

		// GridBagConstraints for the deck button
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 1;
		gbc.weighty = 0.3;
		uiElements.add(deck, gbc);

		if (mode == 1) {
			JPanel boomerangPanel = new JPanel();
			boomerangPanel.setBackground(Color.WHITE);
			boomerangPanel.add(boomerangButton);
			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.weighty = 0.0;
			gbc.weightx = 0.5;
			uiElements.add(boomerangPanel, gbc);
		}

		// Create a container for the save/load buttons
		JPanel saveLoadPanel = new JPanel();
		saveLoadPanel.setBackground(Color.WHITE);
		saveLoadPanel.add(saveButton);
		saveLoadPanel.add(loadButton);
		gbc.gridx = 0;
		if (mode == 1) {
			gbc.gridy = 4;
		} else {
			gbc.gridy = 3;
		}
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.weighty = 0.0;
		gbc.weightx = 0.5;
		uiElements.add(saveLoadPanel, gbc);

		// GridBagConstraints for the UI buttons
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.25;
		gbc.weighty = 1;
		gamePanel.add(uiElements, gbc);

		// JPanel containing the status bar for the game's current conditions
		JPanel statusBar = new JPanel(new GridBagLayout());
		statusBar.setBackground(Color.WHITE);

		// Set the player names for the status bar
		JLabel p1Text = new JLabel(game.getPlayerName(0) + ":");
		p1Text.setHorizontalAlignment(SwingConstants.RIGHT);
		p1Text.setFont(new Font("Arial", Font.PLAIN, 18));
		JLabel p2Text = new JLabel(game.getPlayerName(1) + ":");
		p2Text.setHorizontalAlignment(SwingConstants.RIGHT);
		p2Text.setFont(new Font("Arial", Font.PLAIN, 18));
		JLabel p3Text;
		JLabel p4Text;

		BufferedImage p1Image = ImageIO.read(new File("assets/tokens/" + colors[0] + ".png"));
		BufferedImage p2Image = ImageIO.read(new File("assets/tokens/" + colors[0] + ".png"));
		BufferedImage p3Image = ImageIO.read(new File("assets/tokens/" + colors[0] + ".png"));
		BufferedImage p4Image = ImageIO.read(new File("assets/tokens/" + colors[0] + ".png"));

		// Load the player token images
		if (numberOfPlayers == 2) {
			p1Image = ImageIO.read(new File("assets/tokens/" + colors[0] + ".png"));
			p2Image = ImageIO.read(new File("assets/tokens/" + colors[1] + ".png"));
		} else if (numberOfPlayers == 3) {
			p1Image = ImageIO.read(new File("assets/tokens/" + colors[0] + ".png"));
			p2Image = ImageIO.read(new File("assets/tokens/" + colors[1] + ".png"));
			p3Image = ImageIO.read(new File("assets/tokens/" + colors[2] + ".png"));
		} else {
			p1Image = ImageIO.read(new File("assets/tokens/" + colors[0] + ".png"));
			p2Image = ImageIO.read(new File("assets/tokens/" + colors[1] + ".png"));
			p3Image = ImageIO.read(new File("assets/tokens/" + colors[2] + ".png"));
			p4Image = ImageIO.read(new File("assets/tokens/" + colors[3] + ".png"));
		}

		// Create JLabels from the player token images
		JLabel p1 = new JLabel(new ImageIcon(
				p1Image.getScaledInstance(p1Image.getWidth() / 10, p1Image.getHeight() / 10, Image.SCALE_SMOOTH)));
		p1.setHorizontalAlignment(SwingConstants.LEFT);
		JLabel p2 = new JLabel(new ImageIcon(
				p2Image.getScaledInstance(p2Image.getWidth() / 10, p2Image.getHeight() / 10, Image.SCALE_SMOOTH)));
		p2.setHorizontalAlignment(SwingConstants.LEFT);
		JLabel p3 = new JLabel(new ImageIcon(
				p3Image.getScaledInstance(p3Image.getWidth() / 10, p3Image.getHeight() / 10, Image.SCALE_SMOOTH)));
		p3.setHorizontalAlignment(SwingConstants.LEFT);
		JLabel p4 = new JLabel(new ImageIcon(
				p4Image.getScaledInstance(p3Image.getWidth() / 10, p3Image.getHeight() / 10, Image.SCALE_SMOOTH)));
		p4.setHorizontalAlignment(SwingConstants.LEFT);

		// Add the player names and tokens to the status bar
		// gbc.fill = GridBagConstraints.HORIZONTAL;
		
		//Run in classic mode
		if (mode == 0) {
			gbc.fill = GridBagConstraints.NONE;
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.insets = new Insets(0, 0, 0, 0);
			statusBar.add(p1Text, gbc);
			gbc.gridx = 1;
			statusBar.add(p1, gbc);
			gbc.gridx = 2;
			statusBar.add(p2Text, gbc);
			gbc.gridx = 3;
			statusBar.add(p2, gbc);

			// Add player 3 if applicable
			if (game.getNumberofPlayers() > 2) {
				p3Text = new JLabel(game.getPlayerName(2) + ":");
				p3Text.setHorizontalAlignment(SwingConstants.RIGHT);
				p3Text.setFont(new Font("Arial", Font.PLAIN, 18));
				gbc.gridx = 4;
				statusBar.add(p3Text, gbc);
				gbc.gridx = 5;
				statusBar.add(p3, gbc);

			}

			// Add player 4 if applicable
			if (game.getNumberofPlayers() > 3) {
				p4Text = new JLabel(game.getPlayerName(3) + ":");
				p4Text.setHorizontalAlignment(SwingConstants.RIGHT);
				p4Text.setFont(new Font("Arial", Font.PLAIN, 18));
				gbc.gridx = 6;
				statusBar.add(p4Text, gbc);
				gbc.gridx = 7;
				statusBar.add(p4, gbc);
			}

			// Add the player tracker to the status bar
			gbc.gridx = 8;
			gbc.gridy = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0;
			gbc.insets = new Insets(0, 0, 0, 0);
			pt.setFont(new Font("Arial", Font.PLAIN, 18));
			statusBar.add(pt, gbc);

			// Create timer JLabel
			timerText = new JLabel();
			timerText.setFont(new Font("Arial", Font.PLAIN, 18));

			// Create a container for timer
			JPanel timerPanel = new JPanel();
			timerPanel.setBackground(Color.WHITE);
			timerPanel.add(timerText);

			// GridBagConstraints for timer
			gbc.gridx = 9;
			gbc.gridy = 0;
			gbc.weightx = 0.05;
			gbc.weighty = 0;
			statusBar.add(timerPanel, gbc);
		}//run in strategic mode 
		else {
			BufferedImage p1ImageB = ImageIO.read(new File("assets/boomerangs/boom-3.png"));
			p1b = new JLabel();
			p1b.setIcon(new ImageIcon(
					p1ImageB.getScaledInstance(p1ImageB.getWidth() / 5, p1ImageB.getHeight() / 5, Image.SCALE_SMOOTH)));
			p2b = new JLabel();
			p2b.setIcon(new ImageIcon(
					p1ImageB.getScaledInstance(p1ImageB.getWidth() / 5, p1ImageB.getHeight() / 5, Image.SCALE_SMOOTH)));
			gbc.fill = GridBagConstraints.NONE;
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0.5;
			gbc.weighty = 0;
			gbc.insets = new Insets(0, 10, 5, 5);
			statusBar.add(p1Text, gbc);
			gbc.gridx = 1;
			statusBar.add(p1, gbc);
			gbc.gridx = 0;
			gbc.gridy = 1;
			statusBar.add(p1b, gbc);
			gbc.gridx = 2;
			gbc.gridy = 0;
			statusBar.add(p2Text, gbc);
			gbc.gridx = 3;
			statusBar.add(p2, gbc);
			gbc.gridx = 2;
			gbc.gridy = 1;
			statusBar.add(p2b, gbc);

			// Add player 3 if applicable
			if (game.getNumberofPlayers() > 2) {
				p3b = new JLabel();
				p3b.setIcon(new ImageIcon(p1ImageB.getScaledInstance(p1ImageB.getWidth() / 5, p1ImageB.getHeight() / 5,
						Image.SCALE_SMOOTH)));
				p3b.setHorizontalAlignment(SwingConstants.LEFT);
				p3Text = new JLabel(game.getPlayerName(2) + ":");
				p3Text.setHorizontalAlignment(SwingConstants.RIGHT);
				p3Text.setFont(new Font("Arial", Font.PLAIN, 18));
				gbc.gridx = 4;
				gbc.gridy = 0;
				statusBar.add(p3Text, gbc);
				gbc.gridx = 5;
				statusBar.add(p3, gbc);
				gbc.gridx = 4;
				gbc.gridy = 1;
				statusBar.add(p3b, gbc);

			}

			// Add player 4 if applicable
			if (game.getNumberofPlayers() > 3) {
				p4b = new JLabel();
				p4b.setIcon(new ImageIcon(p1ImageB.getScaledInstance(p1ImageB.getWidth() / 5, p1ImageB.getHeight() / 5,
						Image.SCALE_SMOOTH)));
				p4b.setHorizontalAlignment(SwingConstants.LEFT);
				p4Text = new JLabel(game.getPlayerName(3) + ":");
				p4Text.setHorizontalAlignment(SwingConstants.RIGHT);
				p4Text.setFont(new Font("Arial", Font.PLAIN, 18));
				gbc.gridx = 6;
				gbc.gridy = 0;
				statusBar.add(p4Text, gbc);
				gbc.gridx = 7;
				statusBar.add(p4, gbc);
				gbc.gridx = 6;
				gbc.gridy = 1;
				statusBar.add(p4b, gbc);
			}

			// Add the player tracker to the status bar
			gbc.gridx = 12;
			gbc.gridy = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0;
			gbc.insets = new Insets(0, 5, 5, 10);
			pt.setFont(new Font("Arial", Font.PLAIN, 18));
			statusBar.add(pt, gbc);

			// Create timer JLabel
			timerText = new JLabel();
			timerText.setFont(new Font("Arial", Font.PLAIN, 18));

			// Create a container for timer
			JPanel timerPanel = new JPanel();
			timerPanel.setBackground(Color.WHITE);
			timerPanel.add(timerText);

			// GridBagConstraints for timer
			gbc.gridx = 13;
			gbc.gridy = 0;
			gbc.weightx = 0.05;
			gbc.weighty = 0;
			statusBar.add(timerPanel, gbc);

		}
		// Add the game panel to the main JPanel
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.75;
		gbc.weighty = 1;
		mainPanel.add(gamePanel, gbc);

		// Add the status bar to the main JPanel
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		mainPanel.add(statusBar, gbc);

		// Set the game's application icon
		ImageIcon gameIcon = new ImageIcon("assets/icons/game_app_icon.png");
		setIconImage(gameIcon.getImage());

		// Add all the GUI elements to the JFrame
		add(mainPanel);
		setLocationByPlatform(true);
		setMinimumSize(getPreferredSize());
		setLocation(50, 50);
		pack();
		setExtendedState(MAXIMIZED_BOTH);

	}

	/**
	 * Get the GameBoard object of the GameFrame
	 *
	 * @return The GameBoard attached to this GameFrame
	 */
	public GameBoard getBoard() {
		return gb;
	}

	/**
	 * Get the deck from the GameFrame
	 *
	 * @return The deck attached to this GameFrame
	 */
	public ResizableImagePanel getDeck() {
		return deck;
	}

	/**
	 * Set the image of the drawn card on the board
	 *
	 * @param path
	 * @param drawnCard
	 *            The JPanel of the drawn card image
	 */
	public void displayNewCard(String path, ResizableImagePanel drawnCard) {
		try {
			drawnCard.setImage(path);
		} catch (Exception exc) {
			System.out.println("could not set image");
		}
	}

	/**
	 * Set the time label
	 *
	 * @param int
	 *            time
	 */
	public void displayTime(String time) {
		timerText.setText("Time: " + time);
	}

	/**
	 * Get the ResizableImagePanel containing the drawn card of the board
	 *
	 * @return The GUI element of the drawn card
	 */
	public ResizableImagePanel getDrawnCard() {
		return drawnCard;
	}

	/**
	 * Set the image of the drawn card on the board
	 *
	 * @param boom Jlabel for current player
	 * @param i int for current number of boomerangs
	 *           
	 */
	public void displayNewBoomerang(JLabel boom, int i) {
		BufferedImage p1ImageB = null;
		try {
			if (i == 3)
				p1ImageB = ImageIO.read(new File("assets/boomerangs/boom-3.png"));
			else if (i == 2)
				p1ImageB = ImageIO.read(new File("assets/boomerangs/boom-2.png"));
			else if (i == 1)
				p1ImageB = ImageIO.read(new File("assets/boomerangs/boom-1.png"));
			else
				p1ImageB = ImageIO.read(new File("assets/boomerangs/boom-0.png"));

			boom.setIcon(new ImageIcon(
					p1ImageB.getScaledInstance(p1ImageB.getWidth() / 5, p1ImageB.getHeight() / 5, Image.SCALE_SMOOTH)));
		} catch (Exception exc) {
			System.out.println("could not set image");
		}
	}

	/**
	 * Get the GameBoard containing space information of the board
	 *
	 * @return The GameBoard object attached to the GameFrame
	 */
	public GameBoard getGameBoard() {
		return gb;
	}

	/**
	 * Get the path of a card image
	 *
	 * @return A string of the image's relative path
	 */
	public String getPath(String cardColor, String cardType) {
		String path = "assets/cards/" + cardColor + "_card_" + cardType + ".png";
		return path;
	}

	/**
	 * getJLabel - return the jlabel for the current player
	 *
	 * @return A Jlabel for boomerang icon
	 */
	public JLabel getJLabel(int i) {
		if (i == 0)
			return p1b;
		else if (i == 1)
			return p2b;
		else if (i == 2)
			return p3b;
		else
			return p4b;
	}

}
