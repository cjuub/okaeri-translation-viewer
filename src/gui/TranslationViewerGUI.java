package gui;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import translation_viewer.CharacterInterpreter;

@SuppressWarnings("serial")
public class TranslationViewerGUI extends JFrame {
	public TranslationViewerGUI(CharacterInterpreter ci) {
		super("okaeri-translation-viewer");
		
		JFileChooser fc = new JFileChooser(".");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int res = fc.showOpenDialog(null);
		
		String baseDir = null;
		if (res == JFileChooser.APPROVE_OPTION) {
			baseDir = fc.getSelectedFile().toString();
		} else {
			System.exit(0);			
		}
		
		String[] orgDir = new File(baseDir + "/text_original").list();
		String[] transDir = new File(baseDir + "/text_translated").list();
		
		JTabbedPane tabs = new JTabbedPane();
		
		for (int i = 0; i < orgDir.length; i++) {
			int j = 0;
			for (j = 0; j < transDir.length; j++) {
				if (transDir[j].substring(0, transDir[j].length() - 6).equals(orgDir[i].substring(0, orgDir[i].length() - 4))) {
					break;
				}
			}
			
			File originalFile = new File(baseDir + "/text_original/" + orgDir[i]);
			File translatedFile = new File(baseDir + "/text_translated/" + transDir[j]);
			EditorTab editorTab = new EditorTab(ci, originalFile, translatedFile);
			tabs.addTab(orgDir[i].substring(6, orgDir[i].length() - 4), null, editorTab, null);
		}
		
		add(tabs);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		
		SaveItem saveItem = new SaveItem("Save", tabs);
		saveItem.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.add(saveItem);
		
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	public static void main(String[] args) throws IOException {
		CharacterInterpreter ci = new CharacterInterpreter();
		ci.loadFontInfo("normal.xml", "normal_white.png", 11);
		new TranslationViewerGUI(ci);
	}
}
