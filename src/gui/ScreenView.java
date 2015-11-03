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
        ci.setColor(0xFFf8f8f8);
        
        for (String line : texts) {
        	line = line.replaceAll("<FakeN>", "");
        	
			line = line.replace("<FAST2>", "");
			line = line.replace("<FAST1>", "");
			line = line.replace("<SPEEDNORMAL>", "");
			line = line.replace("<SLOW1>", "");
			line = line.replace("<SLOW2>", "");
			line = line.replace("<SLOW3>", "");
			line = line.replace("<SLOW4>", "");
			line = line.replace("<SLOW5>", "");
			
			line = line.replace("<SMALL>", "");
			line = line.replace("<SIZENORMAL>", "");
			line = line.replace("<BIG>", "");
			
			line = line.replace("<PAUSE1>", "");
			line = line.replace("<PAUSE2>", "");
			line = line.replace("<PAUSE3>", "");
			line = line.replace("<PAUSE4>", "");
			line = line.replace("<PAUSE5>", "");
			line = line.replace("<PAUSE6>", "");
			line = line.replace("<PAUSE7>", "");
			line = line.replace("<PAUSE8>", "");
			line = line.replace("<PAUSE9>", "");
			
			line = line.replace("<NOSHAKE>", "");
			line = line.replace("<SHAKE1>", "");
			line = line.replace("<SHAKE2>", "");
			line = line.replace("<SHAKE3>", "");
        	
        	BufferedImage textImage;
        	try {
        		textImage = ci.getStringGraphics(line);
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
