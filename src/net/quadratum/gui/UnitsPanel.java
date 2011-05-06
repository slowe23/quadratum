package net.quadratum.gui;

import java.awt.Dimension;
import javax.swing.*;

public class UnitsPanel extends JPanel {
	private ScrollableUnitsPanel _sUP;
	private JScrollPane _scroll;
	
	public UnitsPanel(GUIPlayer player) {
		setLayout(new FillLayout());
		
		_sUP = new ScrollableUnitsPanel(player);
		_scroll = new JScrollPane(_sUP, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(_scroll);
	}
	
	public void unitsUpdated() {
		_sUP.unitsUpdated();
		Dimension preferred = _sUP.getPreferredSize(), act = _scroll.getSize();
		_sUP.setSize(new Dimension(Math.max(preferred.width, act.width), Math.max(preferred.height, act.height)));
		_scroll.validate();
		repaint();
	}
	
	public void selectionUpdated() {
		_sUP.selectionUpdated();
		_sUP.repaint();
	}
}