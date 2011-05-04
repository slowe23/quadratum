package net.quadratum.gui;

import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.quadratum.core.Block.BonusType;
import net.quadratum.core.Core;
import net.quadratum.core.Unit;

public class UnitInfoPanel extends JPanel {
	private Core _core;
	
	private GUIPlayer _guiPlayer;
	
	private JTextArea _text;
	
	public UnitInfoPanel(GUIPlayer player) {
		_guiPlayer = player;
		
		setBorder(StaticMethods.getTitleBorder("Unit Info"));
		setLayout(new FillLayout());
		
		StaticMethods.STD std = StaticMethods.createScrollingTextDisplay(7);
		_text = std._jta;
		add(std._jsp);
	}
	
	public void start(Core core) {
		_core = core;
	}
	
	public void selectionUpdated() {
		synchronized(_guiPlayer._unitsData) {
			_text.setText(getDescription(_guiPlayer._unitsData.getSelectedUnit()));
		}
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