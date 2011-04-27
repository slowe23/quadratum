package net.quadratum.ai.test;

import java.util.Iterator;
import java.util.Set;

import net.quadratum.ai.AIPlayer;
import net.quadratum.core.Core;
import net.quadratum.core.MapData;
import net.quadratum.core.MapPoint;

public class TestAI_1 extends AIPlayer {
	
	public void start(Core core, MapData mapData, int id, int totalPlayers) {
		super.start(core, mapData, id, totalPlayers);
		Set<MapPoint> locs = mapData._placementArea;
		
		// Unit placement
		while (_core.getRemainingUnits(this) > 0) {
			int rand = (int)(Math.random()*locs.size());
			MapPoint placement = null;
			for (Iterator<MapPoint> iter = locs.iterator(); iter.hasNext(); 
					placement = iter.next()) {
				if (rand == 0) {
					iter.remove();
					break;
				}
			}
			// place the unit... counts backwards?
			_core.placeUnit(this, placement, "Dude #"+_core.getRemainingUnits(this));
		}
	}

	@Override
	public void turnStart() {
		// TODO do some thinking...
	}

}
