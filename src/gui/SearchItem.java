package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class SearchItem extends JMenuItem implements ActionListener {
	private JTabbedPane tabs;
	
	private String prevString;
	private int prevTab;
	private int prevLine;
	
	public SearchItem(String name, JTabbedPane tabs) {
		super(name);
		this.tabs = tabs;
		
		prevString = "";
		
		addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String str = ""; 
		
		int i = 0;
		int j = 0;
		
		if (e.getActionCommand().equals("Search")) {
			str = JOptionPane.showInputDialog("Search for:", str);
			prevString = str;
		} else if (e.getActionCommand().equals("Search again")) {
			str = prevString;
			j = prevLine + 1;
			i = prevTab;
		}
		
		for (; i < tabs.getComponentCount(); i++) {
			EditorTab tab = (EditorTab) tabs.getComponentAt(i);
			DefaultListModel<String> transModel = tab.getTranslatedData().getListModel();
			DefaultListModel<String> orgModel = tab.getOriginalData().getListModel();
			
			for (; j < transModel.size(); j++) {
				String transStr = transModel.getElementAt(j).replace("<N>", " ");
				transStr = transStr.replaceAll("<.*?>", "");
				String orgStr = orgModel.getElementAt(j).replace("<N>", "");
				orgStr = orgStr.replaceAll("<.*?>", "");
				System.out.println(transStr);
				if (transStr.contains(str) || orgStr.contains(str)) {
					tabs.setSelectedIndex(i);
					tab.getTranslatedData().setSelectedIndex(j);
					tab.getTranslatedData().ensureIndexIsVisible(tab.getTranslatedData().getSelectedIndex());
					prevLine = j;
					prevTab = i;
					return;
				}
			}
			
			j = 0;
		}
		
		prevLine = j;
		prevTab = i;
		
		if (prevTab == tabs.getComponentCount()) {
			JOptionPane.showMessageDialog(null, "No more results for: \"" + str + "\"");
		}
	}
}
