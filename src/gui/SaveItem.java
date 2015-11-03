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
				line = line.replace("<FakeN>", "");
				
				line = line.replace("<FAST2>", "<1a0680000000>");
				line = line.replace("<FAST1>", "<1a0680000001>");
				line = line.replace("<SPEEDNORMAL>", "<1a0680000003>");
				line = line.replace("<SLOW1>", "<1a0680000006>");
				line = line.replace("<SLOW2>", "<1a0680000007>");
				line = line.replace("<SLOW3>", "<1a068000000a>");
				line = line.replace("<SLOW4>", "<1a0680000014>");
				line = line.replace("<SLOW5>", "<1a068000001e>");
				
				line = line.replace("<SMALL>", "<1a0680010001>");
				line = line.replace("<SIZENORMAL>", "<1a0680010002>");
				line = line.replace("<BIG>", "<1a0680010003>");
				
				line = line.replace("<PURPLE>", "<1a06ff000001>");
				line = line.replace("<RED>", "<1a06ff000003>");
				line = line.replace("<ORANGE>", "<1a06ff000004>");
				line = line.replace("<PINK>", "<1a06ff000005>");
				line = line.replace("<GREEN>", "<1a06ff000007>");
				line = line.replace("<BLUE>", "<1a06ff000009>");
				line = line.replace("<YELLOW>", "<1a06ff00000b>");
				line = line.replace("<WHITE>", "<1a06ff00000d>");
				
				line = line.replace("<PAUSE1>", "<1a078004000000>");
				line = line.replace("<PAUSE2>", "<1a078004000500>");
				line = line.replace("<PAUSE3>", "<1a078004000a00>");
				line = line.replace("<PAUSE4>", "<1a078004000f00>");
				line = line.replace("<PAUSE5>", "<1a078004001400>");
				line = line.replace("<PAUSE6>", "<1a078004001900>");
				line = line.replace("<PAUSE7>", "<1a078004001e00>");
				line = line.replace("<PAUSE8>", "<1a078004002d00>");
				line = line.replace("<PAUSE9>", "<1a078004003c00>");
				
				line = line.replace("<NOSHAKE>", "<1a0a8002000000000000>");
				line = line.replace("<SHAKE1>", "<1a0a8002000101010304>");
				line = line.replace("<SHAKE2>", "<1a0a8002000102020304>");
				line = line.replace("<SHAKE3>", "<1a0a8002000103030203>");

				bw.write(line + "\n");
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
