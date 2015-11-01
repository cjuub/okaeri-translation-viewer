package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class SearchAgainItem extends JMenuItem implements ActionListener {
	private SearchItem searchItem;

	public SearchAgainItem(String name, SearchItem searchItem) {
		super(name);
		this.searchItem = searchItem;
		addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		searchItem.actionPerformed(new ActionEvent(this, 0, "Search again"));
	}
}
