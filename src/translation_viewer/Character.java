package translation_viewer;

import java.awt.image.BufferedImage;

public class Character {
	private int x;
	private int y;
	private int width;
	private String val;
	private BufferedImage img;
	
	public Character(int x, int y, int width, String val, BufferedImage img) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.val = val;
		this.img = img;
	}
	
	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}

	public String getChar() {
		return val;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}
