package net.quadratum.gui;

import net.quadratum.core.Block.BonusType;
import net.quadratum.core.Unit;

import javax.swing.*;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import java.util.Map;

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
	
	public static String getUnitDescription(Unit unit) {
		if(unit==null)
			return "No unit selected.";
		else {
			String description = "";
			description += "Unit name: "+unit._name+"\n";
			description += "Owner: "+""+"\n";  //TODO
			description += "\n";
			description += "Stats";
			Map<BonusType, Integer> stats = unit._stats;
			for(BonusType stat : stats.keySet())
				description += "\n   "+stat+": "+stats.get(stat);
			return description;
		}
	}
}