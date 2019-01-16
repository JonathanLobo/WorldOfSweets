import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	private static List<Player> players;
	private double scaleFactor;

	public BoardPanel(String fileName, int numPlayers) throws IOException {
		image = ImageIO.read(new File(fileName));

		players = new ArrayList<Player>();

		for (int i = 0; i < numPlayers; i++) {
			Player p = new Player("Player " + (i + 1));
			p.setX(160 + 20 * i);
			p.setY(200);
			players.add(p);
		}

	}

	public static void setPlayerImages(String[] colors, int numPlayers) throws IOException {
		for (int i = 0; i < numPlayers; i++) {
			Player p = players.get(i);
			p.setImage("assets/tokens/" + colors[i] + ".png");
		}
	}

	public List<Player> getPlayers() {
		return players;
	}

	@Override
	public Dimension getPreferredSize() {
		return image == null ? super.getPreferredSize() : new Dimension(image.getWidth(), image.getHeight());
	}

	@Override
	public Dimension getMinimumSize() {
		return image == null ? super.getMinimumSize() : new Dimension(image.getWidth(), image.getHeight());
	}

	public double getScaleFactor(int iMasterSize, int iTargetSize) {

		double dScale = 1;
		if (iMasterSize > iTargetSize) {

			dScale = (double) iTargetSize / (double) iMasterSize;

		} else {

			dScale = (double) iTargetSize / (double) iMasterSize;

		}

		return dScale;

	}

	public double getScaleFactorToFit(Dimension original, Dimension toFit) {

		double dScale = 1d;

		if (original != null && toFit != null) {

			double dScaleWidth = getScaleFactor(original.width, toFit.width);
			double dScaleHeight = getScaleFactor(original.height, toFit.height);

			dScale = Math.min(dScaleHeight, dScaleWidth);

		}

		return dScale;

	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		scaleFactor = Math.min(1d, getScaleFactorToFit(new Dimension(image.getWidth(), image.getHeight()), getSize()));

		int scaleWidth = (int) Math.round(image.getWidth() * scaleFactor);
		int scaleHeight = (int) Math.round(image.getHeight() * scaleFactor);

		Image scaled = image.getScaledInstance(scaleWidth, scaleHeight, Image.SCALE_SMOOTH);

		int width = getWidth() - 1;
		int height = getHeight() - 1;

		int x = (width - scaled.getWidth(this)) / 2;
		int y = (height - scaled.getHeight(this)) / 2;

		g.drawImage(scaled, x, y, this);

		for (Player p : this.players) {
			BufferedImage playerImg = p.getImage();

			double scaleFactorPlayer = Math.min(1d, getScaleFactorToFit(
					new Dimension(playerImg.getWidth(), playerImg.getHeight()), new Dimension(width / 8, height / 8)));

			int scaleWidthPlayer = (int) Math.round(playerImg.getWidth() * scaleFactorPlayer);
			int scaleHeightPlayer = (int) Math.round(playerImg.getHeight() * scaleFactorPlayer);

			Image scaledPlayer = playerImg.getScaledInstance(scaleWidthPlayer, scaleHeightPlayer, Image.SCALE_SMOOTH);

			int xPlayer = p.getX();
			int yPlayer = p.getY();

			g.drawImage(scaledPlayer, x + (int) (xPlayer * scaleFactor), y + (int) (yPlayer * scaleFactor), null);
		}

	}
}
