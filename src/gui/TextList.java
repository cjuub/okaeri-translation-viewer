package gui;

import java.awt.Color;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class TextList extends JList<String> implements ListSelectionListener {
	private TextEditArea tea;
	private TextList other;
	private DefaultListModel<String> listModel;
	private boolean isRenderColors;
	private EditorTab tab;
	private HashMap<Integer, Boolean> isTranslated;
	
	public DefaultListModel<String> getListModel() {
		return listModel;
	}

	private CellRenderer cellRenderer;
	private int nbrTranslated;

	public TextList(TextEditArea tea, EditorTab tab, boolean isRenderColors) {
		this.tea = tea;
		this.tab = tab;
		this.isRenderColors = isRenderColors;
		
		isTranslated = new HashMap<Integer, Boolean>();
		nbrTranslated = 0;
		
		addListSelectionListener(this);
		
		cellRenderer = new CellRenderer();
		setCellRenderer(cellRenderer);

		listModel = new DefaultListModel<String>();
		setModel(listModel);
	}
	
	public void loadFile(File file) {
		listModel.clear();
		nbrTranslated = 0;
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
			String line;
			while ((line = br.readLine()) != null) {
				listModel.addElement(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//		setModel(listModel);
		repaint();
	}

	public void setOther(TextList other) {
		this.other = other;
	}
	
	public void update(int index) {
		setSelectedIndex(index);
	}
	
	public void updateNbrTranslated() {
		nbrTranslated = 0;
		for (Boolean b : isTranslated.values()) {
			if (b) {
				nbrTranslated++;
			}
		}
	}
	
	public void initializeIsTranslated() {
		for (int i = 0; i < listModel.size(); i++) {
			String value = listModel.get(i);
			if (isInJapanese(i, value) && (!value.equals(other.listModel.get(i)) || !other.listModel.get(i).equals(""))) {
				isTranslated.put(i, false);
			} else {
				isTranslated.put(i, true);
				nbrTranslated++;
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (getSelectedValue() != null) {
			tea.setFields(getSelectedValue().toString());			
			other.update(getSelectedIndices()[0]);
		} else {
			tea.setFields("");
		}
		
		tea.updateScreen();
		updateNbrTranslated();
		tab.setTabProgress();
	}
	
	public void syncModelNewlines(int index) {
		String val = listModel.get(index);
		String otherVal = "";
		if (index > other.listModel.size() - 1) {
			other.listModel.addElement("");
		} else {
			otherVal = other.listModel.get(index);			
		}
		
		if (val.contains("<FakeN>")) {
			val = val.replace("<FakeN>", "");
		}
		
		if (otherVal.contains("<FakeN>")) {
			otherVal = otherVal.replace("<FakeN>", "");
		}
		
		listModel.set(index, val);
		other.listModel.set(index, otherVal);
		
		int valCount = val.split("<N>").length - 1;
		int otherValCount = otherVal.split("<N>").length - 1;
		
		while (valCount < otherValCount) {
			val += "<FakeN>";
			listModel.set(index, val);
			valCount++;
		}
		while (otherValCount < valCount) {
			otherVal += "<FakeN>";
			other.listModel.set(index, otherVal);
			otherValCount++;
		}
	}
	
	private boolean isInJapanese(int index, String line) {
		String ok = "〜「」『』【】＜＞☆★○●◇◆□△▲▽▼※→←↑↓⇒⇔♪①②③④⑤⑥⑦⑧⑨⑩×％＆＃＊";
		int maxChar = 0;
		
		for (int i = 0; i < line.length(); i++) {
			if (!ok.contains("" + line.charAt(i))) {
				maxChar = (int) Math.max(maxChar, line.charAt(i));
			}
		}

		return listModel.get(index).replace("\n", "").replace("<FakeN>", "").equals("") || maxChar > 0x94;
	}
	
	public void syncModelNewlines() {
		for (int i = 0; i < listModel.size(); i++) {
			syncModelNewlines(i);
		}
	}
	
	private class CellRenderer implements ListCellRenderer<String> {
		private JTextArea contents = new JTextArea();
		
		@Override
		public Component getListCellRendererComponent(
				JList<? extends String> list, String value, int index,
				boolean isSelected, boolean cellHasFocus) {
			
			value = value.replaceAll("<N>", "\n");
			value = value.replaceAll("<FakeN>", "\n");
			value = value.replaceAll("<.*?>", "");
			
			if (isRenderColors) {
				if (isSelected) {
					contents.setBackground(new Color(0x4080D9FF, true));
				} else {
					if (isInJapanese(index, value) && (!value.equals(other.listModel.get(index)) || !other.listModel.get(index).equals(""))) {
						contents.setBackground(new Color(0x20F00000, true));
						isTranslated.put(index, false);
					} else {
						contents.setBackground(new Color(0x2000F000, true));
						isTranslated.put(index, true);
					}
				}
			} else {
				if (isSelected) {
					contents.setBackground(new Color(0x4080D9FF, true));
				} else {
					contents.setBackground(new Color(0x80F0F0F0, true));
				}
			}
			
			contents.setText(value);
			contents.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			return contents;
		}
	}

	public int getNbrTranslated() {
		return nbrTranslated;
	}

	public int getNbrLines() {
		return isTranslated.size();
	}
}
