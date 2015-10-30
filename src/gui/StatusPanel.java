package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class StatusPanel extends JPanel {
	private JLabel statusLabel;
	private JLabel tabTotalLabel;
	private List<EditorTab> tabList;
	
	public StatusPanel() {
		setBorder(new LineBorder(Color.GRAY, 1));
		setPreferredSize(new Dimension(getWidth(), 24));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		
		JPanel contents = new JPanel();
		contents.setLayout(new BorderLayout());
		
		statusLabel = new JLabel();
		tabTotalLabel = new JLabel();
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		tabTotalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		contents.add(statusLabel, BorderLayout.WEST);
		contents.add(tabTotalLabel, BorderLayout.EAST);
		
		add(contents);
	}
	
	public void setCurrentTabProgress(int translated, int total) {
		statusLabel.setText("Progress: " + translated + "/" + total + " (" + (int)((translated / (double)total) * 100) + "%)");
	}

	public void setTabTotalProgress() {
		int translated = 0;
		int total = 0;
		for (EditorTab t : tabList) {
			translated += t.getNbrTranslated();
			total += t.getNbrLines();
		}
		
		tabTotalLabel.setText("Total progress: " + translated + "/" + total + " (" + (int)((translated / (double)total) * 100) + "%)");
	}

	public void setTabList(List<EditorTab> tabList) {
		this.tabList = tabList;
	}

	public void update(int index) {
		tabList.get(index);
		setCurrentTabProgress(tabList.get(index).getNbrTranslated(), tabList.get(index).getNbrLines());
		setTabTotalProgress();
	}
}
