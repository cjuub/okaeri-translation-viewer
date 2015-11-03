package translation_viewer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

public class CharacterInterpreter {
	private HashMap<String, Character> charMap;
	private HashMap<String, Character> charMapLarge;
	private HashMap<String, Character> charMapSmall;
	private BufferedImage fontImg;
	private BufferedImage fontImgLarge;
	private BufferedImage fontImgSmall;
	private int charSize;
	private int charSizeLarge;
	private int charSizeSmall;
	private int color;
	private int size;
	
	public CharacterInterpreter() {
		charMap = new HashMap<String, Character>();
		charMapLarge = new HashMap<String, Character>();
		charMapSmall = new HashMap<String, Character>();
	}
	
	public void loadFontInfoNormal(String infoFilename, String imgFilename, int charSize) {
		this.charSize = charSize;
		this.color = 0xFFf8f8f8;

		try {
			fontImg = ImageIO.read(new File(imgFilename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		loadFontInfo(infoFilename, imgFilename, charSize, fontImg, charMap);
	}
	
	public void loadFontInfoLarge(String infoFilename, String imgFilename, int charSize) {
		this.charSizeLarge = charSize;
		this.color = 0xFFf8f8f8;

		try {
			fontImgLarge = ImageIO.read(new File(imgFilename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		loadFontInfo(infoFilename, imgFilename, charSize, fontImgLarge, charMapLarge);
	}
	
	public void loadFontInfoSmall(String infoFilename, String imgFilename, int charSize) {
		this.charSizeSmall = charSize;
		this.color = 0xFFf8f8f8;

		try {
			fontImgSmall = ImageIO.read(new File(imgFilename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		loadFontInfo(infoFilename, imgFilename, charSize, fontImgSmall, charMapSmall);
	}
	
	private void loadFontInfo(String infoFilename, String imgFilename, int charSize, BufferedImage fontImg, HashMap<String, Character> charMap) {
		InputStreamReader isr = null;
		BufferedReader reader = null;
		try {
			isr = new InputStreamReader(new FileInputStream(infoFilename), "Shift_JIS");
			reader = new BufferedReader(isr);
			
			String line;
			while ((line = reader.readLine()) != null) {
				int begChar = line.indexOf("Char=\"");
				int begWidth = line.indexOf("Width=\"");
				int endWidth = line.lastIndexOf("\"");
				
				if (begChar == -1) {
					continue;
				}
				
				int width = Integer.valueOf(line.substring(begWidth + 7, endWidth));
				String val = line.substring(begChar + 6, begChar + 7);
				
				int begIndex = line.indexOf("Index=\"");
				int endIndex = line.indexOf("\"", begIndex + 8);
				int index = Integer.valueOf(line.substring(begIndex + 7, endIndex));
				
				int x = 2 + (((index * (charSize + 2)) + (index / 16) * 2) % fontImg.getWidth());
				int y = 2 + (((index * (charSize + 2)) + (index / 16) * 2) / fontImg.getWidth()) * (charSize + 2);
				
				BufferedImage img = createCharacterGraphics(x, y, width, charSize, fontImg);
				Character c = new Character(x, y, width, val, img);
				charMap.put(val, c);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				isr.close();
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public BufferedImage getStringGraphics(String s) throws IllegalArgumentException {
		List<BufferedImage> res = new ArrayList<BufferedImage>();
		int padding = 0;
		int totWidth = 0;
		boolean hasLarge = false;
		boolean hasNormal = false;
		
		HashMap<String, Character> charMap = null;
		int charSize = 0;
		if (size == 0) {
			charMap = this.charMap;
			charSize = this.charSize;
			hasNormal = true;
		} else if (size == 1) {
			charMap = charMapLarge;
			charSize = charSizeLarge;
			hasLarge = true;
		} else if (size == 2) {
			charMap = charMapSmall;
			charSize = charSizeSmall;
		}
		
		
		if (!s.equals("")) {
			for (int i = 0; i < s.length(); i++) {
				if (s.substring(i).startsWith("<PURPLE>")) {
					color = 0xFF6060f8;
					i += 7;
					continue;
				} else if (s.substring(i).startsWith("<RED>")) {
					color = 0xFFf85050;
					i += 4;
					continue;
				} else if (s.substring(i).startsWith("<ORANGE>")) {
					color = 0xFFf8a030;
					i += 7;
					continue;
				} else if (s.substring(i).startsWith("<PINK>")) {
					color = 0xFFf830f8;
					i += 5;
					continue;
				} else if (s.substring(i).startsWith("<GREEN>")) {
					color = 0xFF40f840;
					i += 6;
					continue;
				} else if (s.substring(i).startsWith("<BLUE>")) {
					color = 0xFF20f8f8;
					i += 5;
					continue;
				} else if (s.substring(i).startsWith("<YELLOW>")) {
					color = 0xFFf8f820;
					i += 7;
					continue;
				} else if (s.substring(i).startsWith("<WHITE>")) {
					color = 0xFFf8f8f8;
					i += 6;
					continue;
				}
				
				if (s.substring(i).startsWith("<BIG>")) {
					i += 4;
					size = 1;
					charMap = charMapLarge;
					charSize = charSizeLarge;
					hasLarge = true;
					continue;
				} else if (s.substring(i).startsWith("<SIZENORMAL>")) {
					i += 11;
					size = 0;
					charMap = this.charMap;
					charSize = this.charSize;
					hasNormal = true;
					continue;
				} else if (s.substring(i).startsWith("<SMALL>")) {
					i += 6;
					size = 2;
					charMap = charMapSmall;
					charSize = charSizeSmall;
					continue;
				}

				if (charMap.get(String.valueOf(s.charAt(i))) == null) {
					throw new IllegalArgumentException();
				}
				
				BufferedImage c = charMap.get(String.valueOf(s.charAt(i))).getImg();
				
				BufferedImage tmp = new BufferedImage(c.getWidth(), c.getHeight(), c.getType());
				
				for (int y = 0; y < tmp.getHeight(); ++y) {
					for (int x = 0; x < tmp.getWidth(); ++x) {
						if (c.getRGB(x, y) == 0xFFFFFFFF) {
							tmp.setRGB(x, y, color);
						} else {
							tmp.setRGB(x, y, c.getRGB(x, y));
						}
					}
				}
				
				res.add(tmp);
				totWidth += tmp.getWidth() + padding;
			}
		} else {
			totWidth += 1 + padding;
		}
		
		if (hasLarge) {
			charSize = charSizeLarge;
		} else if (hasNormal) {
			charSize = this.charSize;
		} else {
			charSize = charSizeSmall;
		}
		
//		System.out.println(charSize);
//		System.out.println();
		
		BufferedImage all = new BufferedImage(totWidth, charSize + 2, BufferedImage.TYPE_INT_ARGB);
        int nextX = 0;
        int nextY = 0;
        Graphics g = all.getGraphics();
		for (BufferedImage bi : res) {
			if (hasLarge) { 
				if (bi.getHeight() == 13) { // 13 is height of normal size
					g.drawImage(bi, nextX, nextY + 4, bi.getWidth(), bi.getHeight(), null);
				} else if (bi.getHeight() == 11) {
					g.drawImage(bi, nextX, nextY + 5, bi.getWidth(), bi.getHeight(), null);
				} else {
					g.drawImage(bi, nextX, nextY, bi.getWidth(), bi.getHeight(), null);
				}
			} else if (hasNormal) {
				if (bi.getHeight() == 11) {
					g.drawImage(bi, nextX, nextY + 2, bi.getWidth(), bi.getHeight(), null);
				} else {
					g.drawImage(bi, nextX, nextY, bi.getWidth(), bi.getHeight(), null);
				}
			} else {
				g.drawImage(bi, nextX, nextY, bi.getWidth(), bi.getHeight(), null);
			}
        	nextX += bi.getWidth() + padding;
        }
		
		g.dispose();
		
		return all;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public BufferedImage createCharacterGraphics(int x, int y, int width, int charSize, BufferedImage fontImg) {
		BufferedImage res = fontImg.getSubimage(x, y, width, charSize);
		
		BufferedImage borderImg = new BufferedImage(res.getWidth() + 2, res.getHeight() + 2, res.getType());
		Graphics g = borderImg.getGraphics();
		g.drawImage(res, 1, 1, res.getWidth(), res.getHeight(), null);
		g.dispose();
		
		for (int i = 0; i < borderImg.getHeight(); ++i) {
			for (int j = 0; j < borderImg.getWidth(); ++j) {
				if (borderImg.getRGB(j, i) == 0xFF000000) {
					borderImg.setRGB(j, i, 0);
				}
				
				if (j == borderImg.getWidth() - 2) {
					borderImg.setRGB(j, i, 0);
				}
			}
		}

		for (int i = 0; i < borderImg.getHeight(); ++i) {
			for (int j = 0; j < borderImg.getWidth(); ++j) {
				if (borderImg.getRGB(j, i) == 0xFFFFFFFF) {
					if (borderImg.getRGB(j - 1, i - 1) != 0xFFFFFFFF) {
						borderImg.setRGB(j - 1, i - 1, 0xFF000000);
					}

					if (borderImg.getRGB(j, i - 1) != 0xFFFFFFFF) {
						borderImg.setRGB(j, i - 1, 0xFF000000);
					}

					if (borderImg.getRGB(j + 1, i - 1) != 0xFFFFFFFF) {
						borderImg.setRGB(j + 1, i - 1, 0xFF000000);
					}

					if (borderImg.getRGB(j - 1, i) != 0xFFFFFFFF) {
						borderImg.setRGB(j - 1, i, 0xFF000000);
					}

					if (borderImg.getRGB(j + 1, i) != 0xFFFFFFFF) {
						borderImg.setRGB(j + 1, i, 0xFF000000);
					}

					if (borderImg.getRGB(j - 1, i + 1) != 0xFFFFFFFF) {
						borderImg.setRGB(j - 1, i + 1, 0xFF000000);
					}

					if (borderImg.getRGB(j, i + 1) != 0xFFFFFFFF) {
						borderImg.setRGB(j, i + 1, 0xFF000000);
					}

					if (borderImg.getRGB(j + 1, i + 1) != 0xFFFFFFFF) {
						borderImg.setRGB(j + 1, i + 1, 0xFF000000);
					}
				}
			}
		}
		
		return borderImg;
	}
}
