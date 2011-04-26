package net.quadratum.gui;

import javax.swing.*;
import java.awt.Color;
import javax.swing.border.TitledBorder;

//Static convenience methods
public class CM {
	// Rounds a double to the nearest int value
	public static int round(double d) {
		return longToInt(Math.round(d));
	}
	
	public static int longToInt(long l) {
		if(l<Integer.MIN_VALUE)
			return Integer.MIN_VALUE;
		
		if(l>Integer.MAX_VALUE)
			return Integer.MAX_VALUE;
		
		return (int)l;
	}
	
	public static JScrollPane createScrollingTextDisplay() {
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		
		JScrollPane scrollPane = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		return scrollPane;
	}
	
	public static JPanel createTitledPanel(String title) {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(null, title, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
		return panel;
	}
	
	public static Color applyAlpha(Color c, int alpha) {
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
	}
}