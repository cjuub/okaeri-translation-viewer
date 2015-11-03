package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class TextEditArea extends JPanel {
	private ScreenView screen;
	private List<TextEditField> fields;
	private TextList textList;
	
	public TextEditArea(ScreenView screen) {
		this.screen = screen;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		fields = new ArrayList<TextEditField>();
		for (int i = 0; i < 7; i++) {
			TextEditField field = new TextEditField();
			fields.add(field);
			add(field);
		}
	}
	
	public void setTextList(TextList textList) {
		this.textList = textList;
	}

	public void setFields(String s) {
		String[] lines = s.split("<N>");
		int i = 0;
		for (TextEditField tef : fields) {
			if (i < lines.length) {
				String line = lines[i].replace("<FakeN>", "");
				
				line = line.replace("<1a0680000000>", "<FAST2>");
				line = line.replace("<1a0680000001>", "<FAST1>");
				line = line.replace("<1a0680000003>", "<SPEEDNORMAL>");
				line = line.replace("<1a0680000006>", "<SLOW1>");
				line = line.replace("<1a0680000007>", "<SLOW2>");
				line = line.replace("<1a068000000a>", "<SLOW3>");
				line = line.replace("<1a0680000014>", "<SLOW4>");
				line = line.replace("<1a068000001e>", "<SLOW5>");
				
				line = line.replace("<1a0680010001>", "<SMALL>");
				line = line.replace("<1a0680010002>", "<SIZENORMAL>");
				line = line.replace("<1a0680010003>", "<BIG>");
				
				line = line.replace("<1a06ff000001>", "<PURPLE>");
				line = line.replace("<1a06ff000003>", "<RED>");
				line = line.replace("<1a06ff000004>", "<ORANGE>");
				line = line.replace("<1a06ff000005>", "<PINK>");
				line = line.replace("<1a06ff000007>", "<GREEN>");
				line = line.replace("<1a06ff000009>", "<BLUE>");
				line = line.replace("<1a06ff00000b>", "<YELLOW>");
				line = line.replace("<1a06ff00000d>", "<WHITE>");
				
				line = line.replace("<1a078004000000>", "<PAUSE1>");
				line = line.replace("<1a078004000500>", "<PAUSE2>");
				line = line.replace("<1a078004000a00>", "<PAUSE3>");
				line = line.replace("<1a078004000f00>", "<PAUSE4>");
				line = line.replace("<1a078004001400>", "<PAUSE5>");
				line = line.replace("<1a078004001900>", "<PAUSE6>");
				line = line.replace("<1a078004001e00>", "<PAUSE7>");
				line = line.replace("<1a078004002d00>", "<PAUSE8>");
				line = line.replace("<1a078004003c00>", "<PAUSE9>");
				
				line = line.replace("<1a0a8002000000000000>", "<NOSHAKE>");
				line = line.replace("<1a0a8002000101010304>", "<SHAKE1>");
				line = line.replace("<1a0a8002000102020304>", "<SHAKE2>");
				line = line.replace("<1a0a8002000103030203>", "<SHAKE3>");
				
				tef.setText(line);
				i++;
			} else {
				tef.setText("");
			}
		}
	}
	
	public void updateScreen() {
		String text = "";
		for (TextEditField tef : fields) {
			if (!tef.getText().equals("")) {
				text += tef.getText() + "<N>";
			}
		}
		
		if (!text.equals("")) {
			screen.setText(text.substring(0, text.length() - 3));
		} else {
			screen.setText("");
		}
	}
	
	public void updateTextList() {
		if (textList == null) {
			return;
		}
		
		DefaultListModel<String> listModel = textList.getListModel();			
		String text = "";
		for (TextEditField tef : fields) {
			if (!tef.getText().equals("")) {
				text += tef.getText() + "<N>";
			}
		}
		if (text.equals("")) {
			listModel.set(textList.getSelectedIndex(), text);
		} else {
			listModel.set(textList.getSelectedIndex(), text.substring(0, text.length() - 3));
		}
		
		textList.syncModelNewlines(textList.getSelectedIndex());
	}
	
	private class TextEditField extends JTextField implements KeyListener {
		
		public TextEditField() {
			setColumns(45);
			setFont(new Font(getFont().getFontName(), Font.PLAIN, 18));
			addKeyListener(this);
		}

		@Override
		public void keyTyped(KeyEvent e) {}

		@Override
		public void keyPressed(KeyEvent e) {}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.KEY_LOCATION_LEFT ||
					e.getKeyCode() == KeyEvent.KEY_LOCATION_RIGHT) {
				return;
			}
			
			updateScreen();
			updateTextList();
		}
	}

	public void setEditable(boolean b) {
		for (JTextField f : fields) {
			f.setEditable(b);
			f.setBackground(Color.WHITE);
		}
	}
}
