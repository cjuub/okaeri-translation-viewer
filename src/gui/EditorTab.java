package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import translation_viewer.CharacterInterpreter;

@SuppressWarnings("serial")
public class EditorTab extends JPanel {
	private File translatedFile;
	private TextList translatedData;
	private StatusPanel statusPanel;
	private TextList originalData;
	
	public EditorTab(CharacterInterpreter ci, File originalFile, File translatedFile, StatusPanel statusPanel, int scale) {
		this.translatedFile = translatedFile;
		this.statusPanel = statusPanel;
		
		setLayout(new BorderLayout());
		
		ScreenView originalScreen = new ScreenView(ci, scale);
		ScreenView translatedScreen = new ScreenView(ci, scale);
		JPanel screenPanel = new JPanel();
		screenPanel.add(originalScreen, BorderLayout.WEST);
		screenPanel.add(translatedScreen, BorderLayout.EAST);
		add(screenPanel, BorderLayout.NORTH);
		
		TextEditArea originalText = new TextEditArea(originalScreen, scale);
		originalText.setEditable(false);
		TextEditArea translatedText = new TextEditArea(translatedScreen, scale);
		JPanel textPanel = new JPanel();
		textPanel.add(originalText, BorderLayout.WEST);
		textPanel.add(translatedText, BorderLayout.EAST);
		add(textPanel, BorderLayout.CENTER);
		
		originalData = new TextList(originalText, this, false);
		translatedData = new TextList(translatedText, this, true);

		originalData.setOther(translatedData);
		translatedData.setOther(originalData);
		JPanel dataPanel = new JPanel();
		JScrollPane scrollOriginal = new JScrollPane();
		JScrollPane scrollTranslated = new JScrollPane();
		scrollOriginal.setViewportView(originalData);
		// System.out.println((int)(770 / (double)3 / scale));
		scrollOriginal.setPreferredSize(new Dimension((int)(770 / ((double)3 / scale)), (int)(300 / ((double)3 / scale))));
		scrollTranslated.setViewportView(translatedData);
		scrollTranslated.setPreferredSize(new Dimension((int)(770 / ((double)3 / scale)), (int)(300 / ((double)3 / scale))));
		scrollOriginal.getVerticalScrollBar().setModel(scrollTranslated.getVerticalScrollBar().getModel());
		dataPanel.add(scrollOriginal, BorderLayout.WEST);
		dataPanel.add(scrollTranslated, BorderLayout.EAST);
		add(dataPanel, BorderLayout.SOUTH);
		
		translatedText.setTextList(translatedData);
		
		originalData.loadFile(originalFile);
		translatedData.loadFile(translatedFile);
		
		originalData.syncModelNewlines();
		
		translatedData.initializeIsTranslated();
	}
	
	public void reloadTranslatedData() {
		translatedData.loadFile(translatedFile);
		originalData.syncModelNewlines();
		translatedData.initializeIsTranslated();
	}

	public File getTranslatedFile() {
		return translatedFile;
	}
	
	public TextList getTranslatedData() {
		return translatedData;
	}
	
	public void setTabProgress() {
		statusPanel.setCurrentTabProgress(getNbrTranslated(), getNbrLines());
		statusPanel.setTabTotalProgress();
	}

	public int getNbrTranslated() {
		return translatedData.getNbrTranslated();
	}

	public int getNbrLines() {
		return translatedData.getNbrLines();
	}

	public TextList getOriginalData() {
		return originalData;
	}
}
