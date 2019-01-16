import java.io.Serializable;

public class Space implements Serializable {

	private final String color;
	private final String type;
	public int x_coordinate;
	public int y_coordinate;

	/**
	 * Space - Board Spaces
	 *
	 * @param int
	 *            Space color
	 */
	public Space(String color) {

		if (!(color.equals("start") || color.equals("finish") || color.equals("red") || color.equals("yellow")
				|| color.equals("blue") || color.equals("green") || color.equals("orange")))
			throw new IllegalArgumentException("Space color is invalid");

		this.color = color;
		this.type = null;
	}

	/**
	 * Space - Board Spaces
	 *
	 * @param int
	 *            Space color, Space type
	 */
	public Space(String color, String type) {

		if (!(color.equals("start") || color.equals("finish") || color.equals("red") || color.equals("yellow")
				|| color.equals("blue") || color.equals("green") || color.equals("orange") || color.equals("wild")))
			throw new IllegalArgumentException("Space color is invalid");

		if (!(type.equals("iceCream") || type.equals("cottonCandy") || type.equals("mint") || type.equals("candyCane")
				|| type.equals("lollipop")))
			throw new IllegalArgumentException("Space Type is invalid");

		this.color = color;
		this.type = type;
	}

	/**
	 * getColor - Returns the color of the space
	 *
	 * @return the space color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * getType - Returns the type of the space
	 *
	 * @return the space type
	 */
	public String getType() {
		return type;
	}

	public void setX(int x) {
		this.x_coordinate = x;
	}

	public void setY(int y) {
		this.y_coordinate = y;
	}

}
