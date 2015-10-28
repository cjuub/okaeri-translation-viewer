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
	
	public EditorTab(CharacterInterpreter ci, File originalFile, File translatedFile, StatusPanel statusPanel) {
		this.translatedFile = translatedFile;
		this.statusPanel = statusPanel;
		
		setLayout(new BorderLayout());
		
		ScreenView originalScreen = new ScreenView(ci);
		ScreenView translatedScreen = new ScreenView(ci);
		JPanel screenPanel = new JPanel();
		screenPanel.add(originalScreen, BorderLayout.WEST);
		screenPanel.add(translatedScreen, BorderLayout.EAST);
		add(screenPanel, BorderLayout.NORTH);
		
		TextEditArea originalText = new TextEditArea(originalScreen);
		TextEditArea translatedText = new TextEditArea(translatedScreen);
		JPanel textPanel = new JPanel();
		textPanel.add(originalText, BorderLayout.WEST);
		textPanel.add(translatedText, BorderLayout.EAST);
		add(textPanel, BorderLayout.CENTER);
		
		TextList originalData = new TextList(originalText, this, false);
		translatedData = new TextList(translatedText, this, true);

		originalData.setOther(translatedData);
		translatedData.setOther(originalData);
		JPanel dataPanel = new JPanel();
		JScrollPane scrollOriginal = new JScrollPane();
		JScrollPane scrollTranslated = new JScrollPane();
		scrollOriginal.setViewportView(originalData);
		scrollOriginal.setPreferredSize(new Dimension(770, 300));
		scrollTranslated.setViewportView(translatedData);
		scrollTranslated.setPreferredSize(new Dimension(770, 300));
		scrollOriginal.getVerticalScrollBar().setModel(scrollTranslated.getVerticalScrollBar().getModel());
		dataPanel.add(scrollOriginal, BorderLayout.WEST);
		dataPanel.add(scrollTranslated, BorderLayout.EAST);
		add(dataPanel, BorderLayout.SOUTH);
		
		translatedText.setTextList(translatedData);
		
		originalData.loadFile(originalFile);
		translatedData.loadFile(translatedFile);
		
		originalData.syncModelNewlines();
	}

	public File getTranslatedFile() {
		return translatedFile;
	}
	
	public TextList getTranslatedData() {
		return translatedData;
	}
	
	public void setTabProgress(int translated, int total) {
		statusPanel.setCurrentTabProgress(translated, total);
	}
}
