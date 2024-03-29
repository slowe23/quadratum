package net.quadratum.gui.test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.quadratum.core.Action;
import net.quadratum.core.Core;
import net.quadratum.core.MapData;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Player;
import net.quadratum.core.Unit;

public class TestingCore implements Core {
	
	private Player _player;
	private int _index;
	
	private Map<MapPoint, Unit> _units;
	private Unit[] _uC;
	private int _toPlace;
	
	/**
	 * Adds a player to the game being run by this Core.
	 * @param p a player to be added
	 * @param playerName the player to be added
	 * @param maxUnits the maximum number of units this player can add
	 * @param startingResources the starting resources
	 */
	public void addPlayer(Player p, String playerName, int maxUnits, int startingResources) {
		_player = p;
		_units = new HashMap<MapPoint, Unit>();
		_toPlace = 3;
		_uC = new Unit[_toPlace];
	}
	
	public void addObserver(Player p) {
		// Nobody cares.
	}
	
	/**
	 * Starts the game.
	 */
	public void startGame() {
		_index = new Random().nextInt(8);
		_player.start(this, createRandomMapData(30, 40), _index, 8);
	}
	
	private static MapData createRandomMapData(int w, int h) {
		return new MapData(createRandomTerrain(w, h), createRandomPlacementArea(w, h));
	}
	
	private static int[][] createRandomTerrain(int w, int h) {
		Random r = new Random();
		int[][] toReturn = new int[w][h];
		for(int i = 0; i<toReturn.length; i++) {
			for(int j = 0; j<toReturn[i].length; j++) {
				toReturn[i][j] = r.nextInt(16);
			}
		}
		return toReturn;
	}
	
	private static Set<MapPoint> createRandomPlacementArea(int w, int h) {
		Random r = new Random();
		Set<MapPoint> set = new HashSet<MapPoint>();
		int cornerX = r.nextInt(w-(w/10)), cornerY = r.nextInt(h-(h/10));
		for(int x = 0; x<w/10; x++)
			for(int y = 0; y<h/10; y++)
				set.add(new MapPoint(cornerX+x, cornerY+y));
		return set;
	}
	
	/**
	 * Callback that alerts the core when a player is ready to start the game,
	 * that is, this is called by a player once all of their units have been
	 * placed.
	 * @param p the Player itself who is now ready
	 */
	public void ready(Player p) {
		_player.updateTurn(_index);
	}
	
	/**
	 * Callback which alerts the core when a player is done with their turn.
	 * @param p the Player itself who is now done
	 */
	public void endTurn(Player p) { }
	
	/**
	 * Callback which lets the core know that a player wants to make a unit
	 * take a particular action.
	 * @param p the Player itself
	 * @param unitID the ID of the unit
	 * @param coords the point on the map which is the destination of the action,
	 * the action itself will be inferred from this and the map
	 * @return true if the action succeeded, false if the action was invalid.
	 */
	public boolean unitAction(Player p, int unitID, MapPoint coords) {
		return false;
	}
	
	/**
	 * Gets the valid actions for the given player's given unit.
	 * @param p the Player itself
	 * @param unitID the ID of the unit
	 * @return a Map of locations to the action that can be taken at that location,
	 * or null if the given unit has already acted.
	 */
	public Map<MapPoint,Action.ActionType> getValidActions(Player p, int unitID) {
		return new HashMap<MapPoint, Action.ActionType>();
	}
	
	/**
	 * Callback that alerts the core that a player has left the game.
	 * @param p the Player itself
	 */
	public void quit(Player p) {
		System.out.println("Quit");
		System.exit(0);
	}
	
	/**
	 * Callback that alerts the core that a chat message should be sent
	 * @param p the Player itself who sent the message - can be this
	 * @param message the message that is being sent
	 */
	public void sendChatMessage(Player p, String message) {
		p.chatMessage(_index, message);
	}
	
	/**
	 * Callback that alerts the core that a player is placing a unit.
	 * @param p the Player itself
	 * @param coords the coordinates at which a player is placing the unit
	 * @param name the name of the unit to place
	 * @return true if the placement succeeded, false otherwise
	 */
	public int placeUnit(Player p, MapPoint coords, String name) {
		if(_toPlace==0 || _units.containsKey(coords))
			return -1;
		
		_toPlace--;
		_uC[_toPlace] = new Unit(name, _index, _toPlace);
		_units.put(coords, _uC[_toPlace]);
		return _toPlace;
	}
	
	/**
	 * Returns the number of units left to place for the given player
	 * @param p the Player itself
	 */
	public int getRemainingUnits(Player p) {
		return _toPlace;
	}
	
	/**
	 * Returns the unit with the given ID
	 * @param p the Player itself
	 * @param unitID The unit in question's id
	 */
	public Unit getUnit(Player p, int unitID) {
		if(unitID>=0 && unitID<_uC.length)
			return _uC[unitID];
		else
			return null;
	}
	
	/**
	 * Callback that alerts the core that a player is placing the given
	 * piece in the unit that is known by the given ID.
	 * @param p the Player itself
	 * @param unitID the ID of the unit
	 * @param pieceID the ID of the piece that the player wishes to place in the
	 * given unit
	 * @param coords the coordinates in the unit where the piece should be placed
	 * @param rotation the rotation of the piece
	 * @return true if the update succeeded, false otherwise
	 */
	public boolean updateUnit(Player p, int unitID, int pieceID, MapPoint coords, int rotation) {
		return false;
	}
	
	/**
	 * Returns the name of a player
	 * @param player the id of the player (i.e. 1-n where n is the number of players, not the player's secret id)
	 */
	public String getPlayerName(int player) {
		return "Player "+player;
	}
	
	/**
	 * Get's a player's resources.
	 * @param p the Player itself
	 */
	public int getResources(Player p) {
		return 0;
	}
	
	public boolean done()
	{
		return false;
	}

	public String getObjectives(Player p)
	{
		return new String("None");
	}
}