package net.quadratum.gui;

import net.quadratum.core.*;
import net.quadratum.core.Block.*;

import java.util.Map;

import javax.swing.*;

public class UnitInfoPanel extends JPanel {
	private Core _core;
	
	private UnitsInfo _unitsInfo;
	private JTextArea _text;
	
	public UnitInfoPanel(UnitsInfo unitsInfo) {
		_unitsInfo = unitsInfo;
		
		setBorder(StaticMethods.getTitleBorder("Unit Info"));
		setLayout(new FillLayout());
		
		_text = new JTextArea();
		
		JScrollPane textScroll = StaticMethods.createScrollingTextDisplay(_text);
		add(textScroll);
	}
	
	public void start(Core core) {
		_core = core;
		
		_text.setText(getDescription(null));
	}
	
	public void unitsUpdated() {
		_text.setText(getDescription(_unitsInfo.getSelected()));
		_text.setCaretPosition(0);
		repaint();
	}
	
	private String getDescription(Unit unit) {
		if(unit==null)
			return "No unit selected.";
		else {
			String description = "";
			description += "Unit name: "+unit._name+"\n";
			description += "Owner: "+_core.getPlayerName(unit._owner)+"\n";  //TODO
			
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