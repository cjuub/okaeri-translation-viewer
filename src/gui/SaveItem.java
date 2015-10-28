package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.DefaultListModel;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class SaveItem extends JMenuItem implements ActionListener {
	private JTabbedPane tabs;
	
	public SaveItem(String name, JTabbedPane tabs) {
		super(name);
		this.tabs = tabs;
		addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		BufferedWriter bw = null;
		try {
			EditorTab tab = (EditorTab) tabs.getComponentAt(tabs.getSelectedIndex());
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tab.getTranslatedFile())));
			DefaultListModel<String> listModel = tab.getTranslatedData().getListModel();
			for (int i = 0; i < listModel.size(); i++) {
				String line = listModel.get(i);
				bw.write(line.replace("<FakeN>", "") + "\n");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
