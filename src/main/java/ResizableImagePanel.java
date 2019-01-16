import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ResizableImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	private double scaleFactor;

	public ResizableImagePanel(String fileName) throws IOException {
		image = ImageIO.read(new File(fileName));
	}

	public void setImage(String fileName) throws IOException {
		image = ImageIO.read(new File(fileName));
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

		setBackground(Color.WHITE);

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

	}
}
