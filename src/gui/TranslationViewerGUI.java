package gui;


import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import translation_viewer.CharacterInterpreter;

@SuppressWarnings("serial")
public class TranslationViewerGUI extends JFrame {
	private boolean isExternalChange;
	
	public TranslationViewerGUI(CharacterInterpreter ci, int scale) {
		super("okaeri-translation-viewer");
		
		isExternalChange = false;
		
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
		

		StatusPanel statusPanel = new StatusPanel();
		add(statusPanel, BorderLayout.SOUTH);

		List<EditorTab> tabList = new ArrayList<EditorTab>();
		
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
			EditorTab editorTab = new EditorTab(ci, originalFile, translatedFile, statusPanel, scale);
			tabs.addTab(orgDir[i].substring(6, orgDir[i].length() - 4), null, editorTab, null);
			tabList.add(editorTab);
		}
		
		statusPanel.setTabList(tabList);
		
		tabs.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				statusPanel.update(tabs.getSelectedIndex());
			}
		});
		
		add(tabs);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		
		SaveItem saveItem = new SaveItem("Save", tabs);
		saveItem.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.add(saveItem);
		
		SearchItem searchItem = new SearchItem("Search", tabs);
		searchItem.setAccelerator(KeyStroke.getKeyStroke('F', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.add(searchItem);
		
		SearchAgainItem searchAgainItem = new SearchAgainItem("Search again", searchItem);
		searchAgainItem.setAccelerator(KeyStroke.getKeyStroke('D', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.add(searchAgainItem);
		
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
		
		FileChangedThread fileChangedThread = new FileChangedThread(baseDir + "/text_translated", this);
		fileChangedThread.start();
		
		addWindowFocusListener(new WindowAdapter() {
		    public void windowGainedFocus(WindowEvent e) {
		        if (isExternalChange) {
		        	JOptionPane.showMessageDialog(null, "External change detected. Text will be reloaded.");
	        		for (EditorTab t : tabList) {
	        			t.reloadTranslatedData();
	        		}

	        		setExternalChange(false);
		        }
		    }
		});
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	public synchronized void setExternalChange(boolean isExternalChange) {
		this.isExternalChange = isExternalChange;
	}
	
	public static void main(String[] args) throws IOException {
		CharacterInterpreter ci = new CharacterInterpreter();
		ci.loadFontInfoNormal("normal.xml", "normal_white.png", 11);
		ci.loadFontInfoLarge("large.xml", "large_white.png", 15);
		ci.loadFontInfoSmall("small.xml", "small_white.png", 9);
		
		int scale = 3;
		if (args.length != 0) {
			scale = Integer.parseInt(args[0]);
		}
		
		new TranslationViewerGUI(ci, scale);
	}
}
