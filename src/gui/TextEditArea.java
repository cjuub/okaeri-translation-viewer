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
				tef.setText(lines[i++].replace("<FakeN>", ""));
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
