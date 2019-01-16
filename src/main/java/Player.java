
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Player - Defines a player and the associated token and location
 */
public class Player {

    private String name;
    private int type;
    private BufferedImage img;
    private int x;
    private int y;
    private int boomerang = 3;

    /**
     * Constructor - Initializes the player's id
     *
     * @param String player's name
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * Gets player name
     *
     * @return String player's name
     */
    public String getName() {
        return name;
    }

    /**
     * setImage - sets the player's token
     *
     * @param String file name of token
     * @throws IOException if invalid fileName
     */
    public void setImage(String fileName) throws IOException {
        this.img = ImageIO.read(new File(fileName));
    }

    /**
     * getImage - Returns the image of token
     *
     * @return img
     */
    public BufferedImage getImage() {
        return img;
    }

    /**
     * getX - Returns the x coordinate of token
     *
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * getY - Returns the y coordinate of token
     *
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * setX - Sets the x coordinate of token
     *
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * setY - Sets the y coordinate of token
     *
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * getWidth - Returns the width
     *
     * @return width of img
     */
    public int getWidth() {
        return img.getWidth(null);
    }

    /**
     * getHeight - Returns the height
     *
     * @return height of img
     */
    public int getHeight() {
        return img.getHeight(null);
    }

    /**
     * getBoom - Returns the number of boomerangs
     *
     * @return number of boomerangs
     */
    public int getboomerang() {
        return boomerang;
    }

    /**
     * setBoom - sets the number of boomerangs
     *
     */
    public void setBoomerang(int n) {
        boomerang = n;
    }

    /**
     * setBoom - Sets the number of boomerangs
     *
     * @return number of boomerangs
     */
    public void decrementboomerang() {
        boomerang--;
    }

    /**
     * setType - Sets if the player is a normal or AI player
     *
     * @param i 0 for normal, 1 for AI
     */
    public void setType(int i) {
        type = i;
    }

    /**
     * getType - Gets if the player is normal or AI
     *
     * @return 0 for normal, 1 for AI
     */
    public int getType() {
        return type;
    }

}
