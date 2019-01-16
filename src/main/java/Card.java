import java.io.Serializable;

/**
 *  Card class
 */
public class Card implements Serializable {

    public final String color;
    public final String type;

    /**
     * Initializes a card.
     *
     * @param  color the color of the card
     * @param  type the type of the card, single or double
     *
     * @throws IllegalArgumentException if color and/or type is invalid
     */
    public Card(String color, String type) {

        if (!(color.equals("red") ||
              color.equals("yellow") ||
              color.equals("blue") ||
              color.equals("green") ||
              color.equals("orange") ||
			        color.equals("wild"))) throw new IllegalArgumentException("Card color is invalid");
        if (!(type.equals("single") ||
              type.equals("double") ||
			        type.equals("skip") ||
					type.equals("iceCream") ||
					type.equals("cottonCandy") ||
					type.equals("mint") ||
					type.equals("candyCane") ||
			        type.equals("lollipop"))) throw new IllegalArgumentException("Card type is invalid");

        this.color = color;
        this.type = type;
    }

    /**
     * Returns the color of this card.
     *
     * @return the color of this card
     */
    public String color() {
        return color;
    }

    /**
     * Returns the type of this card.
     *
     * @return the type of this card
     */
    public String type() {
        return type;
    }

    /**
     * Returns a string representation of this card.
     *
     * @return a string representation of this card
     */
    public String toString() {
        return color + " " + type;
    }
}
