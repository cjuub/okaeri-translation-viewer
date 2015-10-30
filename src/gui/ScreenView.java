package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import translation_viewer.CharacterInterpreter;

@SuppressWarnings("serial")
public class ScreenView extends JPanel {
	private CharacterInterpreter ci;
	private String[] texts;
	private int scale = 3;
	
	public ScreenView(CharacterInterpreter ci) {
		this.ci = ci;
		
		texts = new String[0];
		
		setPreferredSize(new Dimension((int)(256 * scale), (int)(192 * scale)));
		setBackground(Color.ORANGE);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int yOff = 0;
        for (String s : texts) {
        	s = s.replaceAll("<.*?>", "");
        	BufferedImage textImage;
        	try {
        		textImage = ci.getStringGraphics(s);
        	} catch (IllegalArgumentException e) {
        		return;
        	}
        	
        	int x = ((getWidth()) / 2) - ((textImage.getWidth() * scale) / 2);
        	int y = getHeight() - ((getHeight() / 8)) - texts.length * textImage.getHeight() * scale + yOff - 1;
        	int w = (int)(textImage.getWidth() * scale);
        	int h = (int)(textImage.getHeight() * scale);
        	
        	g.drawImage(textImage, x, y, w, h, null);
        	
        	yOff += (textImage.getHeight() + 1) * scale;
        }
    }

	public void setText(String text) {
		texts = text.split("<N>");
		repaint();
	}
}
