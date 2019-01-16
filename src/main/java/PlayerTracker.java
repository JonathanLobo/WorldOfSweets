import java.awt.*;
import javax.swing.*;

/**
 * PlayerTracker - Current player label
 */
public class PlayerTracker extends JLabel {

	private static final long serialVersionUID = 1L;
	private Game game;

	/**
	 * Constructor - sets the label layout which displays whose turn it is
	 *
	 * @param Game	current game
	 *
	 */
	public PlayerTracker(Game g) {
		game = g;
		setBackground(Color.WHITE);
		setForeground(Color.BLACK);
		setFont(new Font("Arial", Font.PLAIN, 36));
		setHorizontalAlignment(SwingConstants.LEFT);
		setText(game.getCurrentPlayerName() + "'s turn");
		setVisible(true);
	}
	
	/**
	 *updateTracker -  Helps set the current player
	 */
	public void updateTracker() {
		setText(game.getCurrentPlayerName() + "'s turn");
	}
	
	/**
	 * gameOver - Helps set the winning player
	 */
	public void gameOver() {
		setText(game.getCurrentPlayerName() + " Wins!");
	}
}
