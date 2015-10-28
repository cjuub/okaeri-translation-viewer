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
	private BufferedImage fontImg;
	private int charSize;
	
	public CharacterInterpreter() {
		charMap = new HashMap<String, Character>();
	}
	
	public void loadFontInfo(String infoFilename, String imgFilename, int charSize) {
		this.charSize = charSize;

		try {
			fontImg = ImageIO.read(new File(imgFilename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
				
				BufferedImage img = createCharacterGraphics(x, y, width);
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
	
	public BufferedImage getStringGraphics(String s) {
		List<BufferedImage> res = new ArrayList<BufferedImage>();
		int padding = 0;
		int totWidth = 0;
		
		if (!s.equals("")) {
			for (int i = 0; i < s.length(); i++) {
				BufferedImage c = charMap.get(String.valueOf(s.charAt(i))).getImg();
				res.add(c);
				totWidth += c.getWidth() + padding;
			}
		} else {
			totWidth += 1 + padding;
		}
		
		BufferedImage all = new BufferedImage(totWidth, charSize + 2, BufferedImage.TYPE_INT_ARGB);
        int nextX = 0;
        int nextY = 0;
        Graphics g = all.getGraphics();
		for (BufferedImage bi : res) {
        	g.drawImage(bi, nextX, nextY, bi.getWidth(), bi.getHeight(), null);
        	nextX += bi.getWidth() + padding;
        }
		
		g.dispose();
		
		return all;
	}
	
	public BufferedImage createCharacterGraphics(int x, int y, int width) {
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
