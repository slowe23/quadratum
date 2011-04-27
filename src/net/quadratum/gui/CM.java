package net.quadratum.gui;

import java.awt.Color;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import net.quadratum.core.Block.BonusType;
import net.quadratum.core.Unit;
import net.quadratum.core.Block;
import net.quadratum.core.Block.*;

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
			for(BonusType stat:stats.keySet())
				if(stat.isStat())
					description += "\n   "+stat+": "+stats.get(stat);
			description += "\n";
			
			description += "\n";
			
			description += "Abilities";
			for(BonusType stat:stats.keySet())
				if(stat.isAbility() && stats.get(stat)>0)
					description += "\n   "+stat;
			
			return description;
		}
	}
}