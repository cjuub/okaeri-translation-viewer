package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class StatusPanel extends JPanel {
	private JLabel statusLabel;
	
	public StatusPanel() {
		setBorder(new LineBorder(Color.GRAY, 1));
		setPreferredSize(new Dimension(getWidth(), 24));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		statusLabel = new JLabel();
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		add(statusLabel);
	}
	
	public void setCurrentTabProgress(int translated, int total) {
		statusLabel.setText("Progress: " + translated + "/" + total + " (" + (int)((translated / (double)total) * 100) + "%)");
	}
}
