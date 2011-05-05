package net.quadratum.core;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.quadratum.main.Main;

public class GameCore implements Core
{
	private Main _main;
	private int[][] _terrain;
	private ArrayList<HashSet<MapPoint>> _startingLocations;
	private ArrayList<Unit> _units;
	private ArrayList<UnitInformation> _unitInformation;
	private ArrayList<Player> _players;
	private ArrayList<PlayerInformation> _playerInformation;
	private ArrayList<Piece> _pieces;
	private WinCondition _winCondition;
	private int _turn; // -1 = not started, -2 = game over
	private boolean _started;
	private Object _chatLockObject, _turnLockObject;
	private Writer _log;
	private HashSet<ObserverContainer> _observers;
	private int _maxPlayers;
	
	/**
	 * Constructor for GameCore.
	 * @param map the map file name
	 * @param winCondition the win condition
	 */
	public GameCore(Main m, String map, WinCondition winCondition, ArrayList<Piece> pieces)
	{
		_main = m;
		_startingLocations = new ArrayList<HashSet<MapPoint>>();
		_units = new ArrayList<Unit>();
		_unitInformation = new ArrayList<UnitInformation>();
		_players = new ArrayList<Player>();
		_playerInformation = new ArrayList<PlayerInformation>();
		_pieces = pieces;
		_winCondition = winCondition;
		_turn = -1;
		_started = false;
		_chatLockObject = new Object();
		_turnLockObject = new Object();
		_observers = new HashSet<ObserverContainer>();
		_maxPlayers = 0;
		try
		{
			if (Constants.DEBUG_TO_FILE) {
				_log = new FileWriter("log.txt");
			} else {
				_log = new OutputStreamWriter(System.out);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		readMap(map);
	}
		
	/**
	 * Reads in the map.
	 * @param map the map file name
	 */
	private void readMap(String map)
	{
		try
		{			
			FileReader file = new FileReader(map);
			BufferedReader reader = new BufferedReader(file);
			String line;
			int width = 0;
			int height = 0;
			String[] locations, temp;
			HashSet<MapPoint> points;
			HashSet<MapPoint> water = new HashSet<MapPoint>();
			HashSet<MapPoint> bunkers = new HashSet<MapPoint>();
			HashSet<MapPoint> mountains = new HashSet<MapPoint>();
			HashSet<MapPoint> resources = new HashSet<MapPoint>();
			while((line = reader.readLine()) != null)
			{
				if(line.startsWith("height|") && line.length() > 7)
				{
					height = new Integer(line.split("\\|")[1]).intValue();
				}
				else if(line.startsWith("width|") && line.length() > 6)
				{
					width = new Integer(line.split("\\|")[1]).intValue();
				}
				else if(line.startsWith("players|") && line.length() > 8)
				{
					_maxPlayers = new Integer(line.split("\\|")[1]).intValue();
				}
				else if(line.startsWith("start|") && line.length() > 6)
				{
					points = new HashSet<MapPoint>();
					locations = line.split("\\|");
					for(int i = 1; i < locations.length; i++)
					{
						temp = locations[i].split(",");
						if(temp.length != 2)
						{
							log("Invalid starting location in map file", 3);
							throw new RuntimeException();
						}
						points.add(new MapPoint((new Integer(temp[0])).intValue(), (new Integer(temp[1])).intValue()));
					}
					_startingLocations.add(points);
				}
				else if(line.startsWith("water|"))
				{
					locations = line.split("\\|");
					for(int i = 1; i < locations.length; i++)
					{
						temp = locations[i].split(",");
						if(temp.length != 2)
						{
							log("Invalid water location(s) in map file", 3);
							throw new RuntimeException();
						}
						water.add(new MapPoint((new Integer(temp[0])).intValue(), (new Integer(temp[1])).intValue()));
					}
				}
				else if(line.startsWith("bunkers|"))
				{
					locations = line.split("\\|");
					for(int i = 1; i < locations.length; i++)
					{
						temp = locations[i].split(",");
						if(temp.length != 2)
						{
							log("Invalid bunker location(s) in map file", 3);
							throw new RuntimeException();
						}
						bunkers.add(new MapPoint((new Integer(temp[0])).intValue(), (new Integer(temp[1])).intValue()));
					}
				}
				else if(line.startsWith("mountains|"))
				{
					locations = line.split("\\|");
					for(int i = 1; i < locations.length; i++)
					{
						temp = locations[i].split(",");
						if(temp.length != 2)
						{
							log("Invalid mountain location(s) in map file", 3);
							throw new RuntimeException();
						}
						mountains.add(new MapPoint((new Integer(temp[0])).intValue(), (new Integer(temp[1])).intValue()));
					}
				}
				else if(line.startsWith("resources|"))
				{
					locations = line.split("\\|");
					for(int i = 1; i < locations.length; i++)
					{
						temp = locations[i].split(",");
						if(temp.length != 2)
						{
							log("Invalid resources location(s) in map file", 3);
							throw new RuntimeException();
						}
						resources.add(new MapPoint((new Integer(temp[0])).intValue(), (new Integer(temp[1])).intValue()));
					}
				}
				else
				{
					log("Invalid input line in map file", 3);
					throw new RuntimeException();
				}
			}
			if(width == 0 || height == 0 || _maxPlayers == 0 || _maxPlayers != _startingLocations.size() || _maxPlayers > Constants.MAX_PLAYERS)
			{
				log("Map file was missing important data", 3);
				throw new RuntimeException();
			}
			_terrain = new int[width][height];
			for(int i = 0; i < width; i++)
			{
				for(int j = 0; j < height; j++)
				{
					_terrain[i][j] = 0;
				}
			}
			for(MapPoint point : water)
			{
				if(point._x < 0 || point._x >= width || point._y < 0 || point._y >= height)
				{
					log("Invalid water coordinates in map data", 3);
					throw new RuntimeException();
				}
				else
				{
					_terrain[point._x][point._y] = _terrain[point._x][point._y] ^ TerrainConstants.WATER;
				}
			}
			for(MapPoint point : bunkers)
			{
				if(point._x < 0 || point._x >= width || point._y < 0 || point._y >= height)
				{	
					log("Invalid bunker coordinates in map data", 3);
					throw new RuntimeException();
				}
				else
				{
					_terrain[point._x][point._y] = _terrain[point._x][point._y] ^ TerrainConstants.BUNKER;
				}
			}
			for(MapPoint point : mountains)
			{
				if(point._x < 0 || point._x >= width || point._y < 0 || point._y >= height)
				{
					log("Invalid mountain coordinates in map data", 3);
					throw new RuntimeException();
				}
				else
				{
					_terrain[point._x][point._y] = _terrain[point._x][point._y] ^ TerrainConstants.MOUNTAIN;
				}
			}
			for(MapPoint point : resources)
			{
				if(point._x < 0 || point._x >= width || point._y < 0 || point._y >= height)
				{
					log("Invalid resource coordinates in map data", 3);
					throw new RuntimeException();
				}
				else
				{
					_terrain[point._x][point._y] = _terrain[point._x][point._y] ^ TerrainConstants.RESOURCES;
				}
			}
			file.close();
		}
		catch(Exception e)
		{
			log("Invalid map file", 3);
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Adds a player to the game
	 * @param p the actual player
	 * @param playerName the name of the player
	 * @param maxUnits the max units a player can have
	 * @param startingResources the starting resources for this player
	 */
	@Override
	public synchronized void addPlayer(Player p, String playerName, int maxUnits, int startingResources)
	{
		if(!_started && _players.size() < _maxPlayers)
		{
			_players.add(p);
			_playerInformation.add(new PlayerInformation(new String(playerName), maxUnits, startingResources));
			if(_observers.contains(new Integer(_players.size() - 1)))
			{
				_playerInformation.get(_players.size() - 1)._lost = true;
			}
			log("Added player\n"
				+ "\tNumber: " + (_players.size() - 1) + "\n"
				+ "\tName: " + playerName + "\n"
				+ "\tMax Units: " + maxUnits, 1);
		}
		else
		{
			if(_started)
			{
				log("Tried to add a player but the game has already started", 2);
			}
			else
			{
				log("Tried to add more than the max number of players\n"
					+ "\tPlayers: " + _players.size() + "\n"
					+ "\tMax players: " + _maxPlayers, 2);
			}
		}
	}
	
	/**
	 * Adds an observer.
	 * @param p the actual player
	 */
	@Override
	public synchronized void addObserver(Player p)
	{
		if(_started)
		{
			log("Tried to add a player but the game has already started", 2);
		}
		else
		{
			log("Added an observer", 1);
			_observers.add(new ObserverContainer(p));
		}
	}
		
	/**
	 * Gets a player's id from a reference.
	 * @param p the Player
	 * @return the id of that player
	 */
	private int getPlayerId(Player p)
	{
		if(_observers.contains(p))
		{
			return -2;
		}
		for(int i = 0; i < _players.size(); i++)
		{
			if(_players.get(i) == p)
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Starts the game.
	 */
	@Override
	public void startGame()
	{
		if(_players.size() == 0)
		{
			log("Tried to start game with 0 players", 3);
			throw new RuntimeException();
		}
		if(!_started)
		{
			_started = true;
		}
		else
		{
			log("start() was called but the game had already started", 2);
			return;
		}
		MapData tempMap;
		ArrayList<Piece> tempPieces;
		PlayerStartThread playerStartThread;
		for(int i = 0; i < _players.size(); i++)
		{
			tempMap = new MapData(_terrain, _startingLocations.get(i));
			tempPieces = new ArrayList<Piece>();
			for(int j = 0; j < _pieces.size(); j++)
			{
				tempPieces.add(new Piece(_pieces.get(j)));
			}
			playerStartThread = new PlayerStartThread(_players.get(i), this, tempMap, i, _players.size());
			playerStartThread.start();
			_players.get(i).updatePieces(tempPieces);
		}
		for(ObserverContainer obv : _observers)
		{
			tempMap = new MapData(_terrain, new HashSet<MapPoint>());
			tempPieces = new ArrayList<Piece>();
			for(int j = 0; j < _pieces.size(); j++)
			{
				tempPieces.add(new Piece(_pieces.get(j)));
			}
			playerStartThread = new PlayerStartThread(obv._p, this, tempMap, -2, _players.size());
			playerStartThread.start();
			obv._p.updatePieces(tempPieces);
		}
		log("start() has been called, starting unit placement phase", 1);
	}
	
	/**
	 * Callback for notifying the GameCode that a player is ready.
	 * @param p the Player
	 */
	@Override
	public synchronized void ready(Player p)
	{
		int player = getPlayerId(p);
		if(player == -2)
		{
			return;
		}
		if(_turn != -1)
		{
			log("Player " + player + " called ready() but the game had already started", 2);
			return;
		}
		_playerInformation.get(player)._ready = true;
		for(int i = 0; i < _playerInformation.size(); i++)
		{
			if(!_playerInformation.get(i)._ready && !_playerInformation.get(i)._quit)
			{
				log("Player " + player + " called ready()", 1);
				return;
			}
		}
		log("Player " + player + " called ready() and every other player was ready, starting game", 1);
		updateMaps(new Action(Action.ActionType.GAME_START, null, null));
		nextTurn();
	}
	
	/**
	 * Callback for ending a turn.
	 * @param p the Player
	 */
	@Override
	public void endTurn(Player p)
	{
		synchronized(_turnLockObject)
		{
			int player = getPlayerId(p);
			if(player == -2)
			{
				return;
			}
			if(player == _turn)
			{
				log("Player " + player + " called endTurn(), moving on to next turn", 1);
				nextTurn();
			}
			else
			{
				log("Player " + player + " called endTurn() but it was not their turn", 2);
			}
		}
	}
	
	/**
	 * Performs a unit's action.
	 * @param p the player
	 * @param unitId the unit's id
	 * @param coords the point at which the action should be taken
	 * @return true if the action has been taken, false if otherwise
	 */
	@Override
	public boolean unitAction(Player p, int unitId, MapPoint coords)
	{
		synchronized(_turnLockObject)
		{
			int player = getPlayerId(p);
			if(player == -2)
			{
				return false;
			}
			if(_turn != player)
			{
				log("Player " + player + " called unitAction(unitId: " + unitId + ", coords: " + coords + ") but it was not their turn\n"
					+ "\tTurn: " + _turn, 2);
				return false;
			}
			if(unitId < 0 || unitId >= _units.size())
			{
				log("Player " + player + " called unitAction(unitId: " + unitId + ", coords: " + coords + ") but they used an invalid unitId", 2);
				return false;
			}
			int owner = _units.get(unitId)._owner;
			if(owner != player)
			{
				log("Player " + player + " called unitAction(unitId: " + unitId + ", coords: " + coords + ") but they did not own the unit\n"
					+ "\tOwner: " + owner, 2);
				return false;
			}
			if(_unitInformation.get(unitId)._position.equals(new MapPoint(-1, -1)))
			{
				log("Player " + player + " called unitAction(unitId: " + unitId + ", coords: " + coords + ") but the unit was dead", 2);
				return false;
			}
			coords = new MapPoint(coords);
			Set<MapPoint> valid;
			MapPoint oldCoords = new MapPoint(_unitInformation.get(unitId)._position);
			int unit = CoreActions.getUnitAtPoint(coords, _unitInformation);
			if(unit == -1) // Movement
			{
				if(_unitInformation.get(unitId)._hasMoved)
				{
					log("Player " + player + " called unitAction(unitId: " + unitId + ", coords: " + coords + ") but the unit had already moved", 2);
					return false;
				}
				valid = CoreActions.getAreaForUnit(unitId, 0, _units, _unitInformation, _terrain);
				if(!valid.contains(coords))
				{
					log("Player " + player + " called unitAction(unitId: " + unitId + ", coords: " + coords + ") but did provide a valid movement coordinate\n"
						+ "\tValid: " + valid, 2);
					return false;
				}
				_unitInformation.get(unitId)._hasMoved = true;
				_unitInformation.get(unitId)._position = coords;
				updateMaps(new Action(Action.ActionType.MOVE, oldCoords, coords));
				log("Player " + player + " called unitAction(unitId: " + unitId + ", coords: " + coords + ")\n"
					+ "\tAction taken: move\n"
					+ "\tFrom: " + oldCoords, 1);
			}
			else // Attacking
			{
				if(_unitInformation.get(unitId)._hasAttacked)
				{
					log("Player " + player + " called unitAction(unitId: " + unitId + ", coords: " + coords + ") but the unit had already attacked", 2);
					return false;
				}
				valid = CoreActions.getAreaForUnit(unitId, 1, _units, _unitInformation, _terrain);
				if(!valid.contains(coords))
				{
					log("Player " + player + " called unitAction(unitId: " + unitId + ", coords: " + coords + ") but did provide a valid attack coordinate\n"
						+ "\tValid: " + valid, 2);
					return false;
				}
				if(_units.get(unit)._owner == player)
				{
					log("Player " + player + " called unitAction(unitId: " + unitId + ", coords: " + coords + ") but tried to attack a unit that they owned\n"
						+ "\tUnit at coords: " + unit, 2);
					return false;
				}
				_unitInformation.get(unitId)._hasAttacked = true;
				
				// TODO factor out? also potentially increase the number of
				// attack lines
				
				// Grab some references
				
				Unit attacker = _units.get(unitId);
				Unit defender = _units.get(unit);
				
				// Calculate centers of attacker and defender in real coordinates
				// oldCoords = attacker
				// coords = defender
				
				RealPoint attackerPos = new RealPoint(oldCoords._x+0.5,oldCoords._y+0.5);
				RealPoint defenderPos = new RealPoint(coords._x+0.5,coords._y+0.5);
				
				// Get the attack line
				
				List<Map<MapPoint,Double>> line = getAttackLine(defender._size,
						attackerPos,defenderPos);
				
				// Distribute the damage
				
				int damageLeft = attacker._stats.get(Block.BonusType.ATTACK);
				int damageDone;
				for (Map<MapPoint,Double> current : line) {
					damageDone = 0;
					for (MapPoint point : current.keySet()) {
						
						Block b = defender._blocks.get(point);
						if (b == null) {
							continue;
						}
						// Get damage at this point and apply it to the block
						int damage = (int)(damageLeft*current.get(point)+0.5);
						b._health -= damage;
						if (b._health < 0) {
							damage += b._health;
							defender._blocks.remove(b);
						}
						damageDone += damage;
					}
					damageLeft -= damageDone;
					if (damageLeft == 0) {
						break;
					}
				}
				
				// We don't need to send out the unit died action unless the heart is totally gone
				
				updateCachedStats(defender);
				
				if (defender._stats.get(Block.BonusType.HEART) == 0) {
					updateMaps(new Action(Action.ActionType.UNIT_DIED, coords, coords));
				}
				
				log("Player " + player + " called unitAction(unitId: " + unitId + ", coords: " + coords + ")\n"
					+ "\tAction taken: attack\n"
					+ "\tFrom: " + oldCoords, 1);
			}
			return true;
		}
	}
	
	/**
	 * Adaptation of Xiaolin Wu's line algorithm to determine an attack line
	 * and corresponding damage spread.
	 * @param size the internal size of the defending unit
	 * @param start the point of origin
	 * @param end the destination point
	 * @return a representation of damage splits in sequence
	 */
	private List<Map<MapPoint,Double>> getAttackLine(int size, 
			RealPoint start, RealPoint end) {
		// Get start and end x,y
		double sx = start._x, sy = start._y;
		double ex = end._x, ey = end._y;
		// Get the differentials along the x and y axes
		double dx = end._x - start._x;
		double dy = end._y - start._y;
		// Make the line go at <45 degrees from the horizontal
		boolean steep = Math.abs(dx) < Math.abs(dy);
		double tmp;
		if (steep) {
			// Swap start x,y
			tmp = sx;
			sx = sy;
			sy = tmp;
			// Swap end x,y
			tmp = ex;
			ex = ey;
			ey = tmp;
			// Swap dx,dy
			tmp = dx;
			dx = dy;
			dy = tmp;
		}
		// Make the line go left to right
		boolean swap = dx < 0;
		if (swap) {
			// Swap start x, end x
			tmp = sx;
			sx = ex;
			ex = tmp;
			// Swap start y, end y
			tmp = sy;
			sy = ey;
			ey = tmp;
		}
		double grad = dy/dx;
		
		// Find the lower bound, accounting for all the swapping we just did
		int xbound, ybound1, ybound2;
		if (swap) {
			if (steep) {
				xbound = getFloorCellPosition((int)start._y,size);
				ybound1 = getFloorCellPosition((int)start._x,size);
				ybound2 = getFloorCellPosition(((int)start._x)+1,size)-1;
			} else {
				xbound = getFloorCellPosition(((int)start._x)+1,size)-1;
				ybound1 = getFloorCellPosition((int)start._y,size);
				ybound2 = getFloorCellPosition(((int)start._y)+1,size)-1;
			}
		} else {
			if (steep) {
				xbound = getFloorCellPosition(((int)end._y)+1,size)-1;
				ybound1 = getFloorCellPosition((int)end._x,size);
				ybound2 = getFloorCellPosition(((int)end._x)+1,size)-1;
			} else {
				xbound = getFloorCellPosition((int)end._x,size);
				ybound1 = getFloorCellPosition((int)end._y,size);
				ybound2 = getFloorCellPosition(((int)end._y)+1,size)-1;
			}
		}
		
		// Set up the data structures
		Map<MapPoint,Double> map;
		LinkedList<Map<MapPoint,Double>> list = new LinkedList<Map<MapPoint,Double>>();
		
		// first endpoint
		double xend = getRoundedCellPosition(sx, size);
		double yend = sy + grad * (xend - sx);
		double xgap = 1 - getFractionalCellPosition(sx + 0.5, size);
		int xpt1 = (int)xend;
		int ypt1 = getFloorCellPosition(yend,size);
		if (swap) {
			map = new HashMap<MapPoint,Double>();
			putPoint(map,xpt1%size,ypt1%size,
					1-getFractionalCellPosition(yend,size)*xgap,steep);
			putPoint(map,xpt1%size,(ypt1+1)%size,
					getFractionalCellPosition(yend,size)*xgap,steep);
			list.add(map);
		}
		double intery = yend + grad;
		
		// second endpoint, if inside defender
		xend = getRoundedCellPosition(ex, size);
		yend = sy + grad * (xend - ex);
		xgap = 1 - getFractionalCellPosition(ex + 0.5, size);
		int xpt2 = (int)xend;
		int ypt2 = getFloorCellPosition(yend,size);
		if (!swap) {
			map = new HashMap<MapPoint,Double>();
			putPoint(map,xpt2%size,ypt2%size,
					1-getFractionalCellPosition(yend,size)*xgap,steep);
			putPoint(map,xpt2%size,(ypt2+1)%size,
					getFractionalCellPosition(yend,size)*xgap,steep);
			list.add(map);
		}
		
		// main loop
		for (int x = xpt1+1; x < xpt2; x++) {
			boolean good = swap ? x <= xbound : x >= xbound;
			// If we are inside the defender, plot some points
			if (good) {
				map = new HashMap<MapPoint,Double>();
				int y = getFloorCellPosition(intery,size);
				// Check for inclusion in the bounds (fix for corner overlap)
				// One of these should always be in the bounds.
				if (ybound1 <= y && y <= ybound2) {
					putPoint(map,x % size,y % size,
							1-getFractionalCellPosition(yend,size),steep);
				}
				y++;
				if (ybound1 <= y && y <= ybound2) {
					putPoint(map,x % size,y % size,
							getFractionalCellPosition(yend,size),steep);
				}
				// Add the map.
				if (swap) {
					list.addFirst(map);
				} else {
					list.addLast(map);
				}
			}
			// Increment the y
			intery += grad;
		}
		return list;
	}
	
	/**
	 * Helper function that is used to get the closest cell position inside a 
	 * unit given the unit's internal size.
	 * @param d the real-valued location
	 * @param size the internal size of the unit
	 * @return which cell in the unit this point would be closest to.
	 */
	private int getRoundedCellPosition(double d, int size) {
		return (int)(d*size+0.5);
	}
	
	/**
	 * Helper function that is used to get the floor cell position inside a unit
	 * given the unit's internal size. 
	 * @param d the real-valued location
	 * @param size the internal size of the unit
	 * @return which cell in the unit this point is inside.
	 */
	private int getFloorCellPosition(double d, int size) {
		return (int)Math.floor(d*size);
	}
	
	/**
	 * Helper function that is used to get the fractional part of the cell
	 * position inside a unit given the unit's internal size.
	 * @param d the real-valued location
	 * @param size the internal size of the unit
	 * @return the fractional position inside the cell this point is in.
	 */
	private double getFractionalCellPosition(double d, int size) {
		return d*size-getFloorCellPosition(d,size);
	}
	
	/**
	 * Puts a point in the given map.
	 * @param map a map
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param d the value
	 * @param steep whether or not the coordinates should be transposed.
	 */
	private void putPoint(Map<MapPoint,Double> map, int x, int y, 
			double d, boolean steep) {
		if (steep) {
			map.put(new MapPoint(y,x),d);
		} else {
			map.put(new MapPoint(x,y),d);
		}
	}
	
	/**
	 * Updates a unit's cached stats.
	 * @param u the unit to update
	 */
	private void updateCachedStats(Unit u) {
		// Clear out old values
		for (Block.BonusType bonus : Block.BonusType.values()) {
			u._stats.put(bonus, 0);
		}
		// Put in new values
		for (Block b : u._blocks.values()) {
			for (Block.BonusType bonus : b._bonuses.keySet()) {
				u._stats.put(bonus, u._stats.get(bonus) + b._bonuses.get(bonus));
			}
		}
	}
	
	/**
	 * Calculates the valid actions for a given unit
	 * @param p the Player
	 * @param unitId the unit's id
	 * @return a map of MapPoints to Action.ActionTypes that represents what actions can be taken where
	 */
	@Override
	public Map<MapPoint, Action.ActionType> getValidActions(Player p, int unitId)
	{
		int player = getPlayerId(p);
		if(unitId < 0 || unitId >= _units.size() || _units.get(unitId)._owner != player || _turn != player)
		{
			if(player != -2)
			{
				String toLog;
				toLog = "Player " + player + " called getValidActions(unitId: " + unitId + ") but they did not own the unit, it was an invalid unit, or it was not their turn\n";
				if(unitId >= 0 && unitId < _units.size())
				{
					toLog += "\tOwner: " + _units.get(unitId)._owner + "\n";
				}
				else
				{
					toLog += "\tOwner: (none)\n";
				}
				toLog += "\tTurn: " + _turn + "\n";
				toLog += "\tAnswer: null";
				log(toLog, 2);
				return null;
			}
		}
		if(_unitInformation.get(unitId)._position.equals(new MapPoint(-1, -1)))
		{
			log("Player " + player + " called getValidActions(unitId: " + unitId + ") but the unit was dead\n"
				+ "\tAnswer: null", 2);
			return null;
		}
		Map<MapPoint, Action.ActionType> actions = CoreActions.getValidActions(unitId, player, _units, _unitInformation, _terrain);
		if(actions.size() == 0)
		{
			log("Player " + player + " called getValidActions(unitId: " + unitId + ") but the unit had already attacked/moved\n"
				+ "\tAnswer: (empty map)", 1);
			return actions;
		}
		else
		{
			log("Player " + player + " called getValidActions(unitId: " + unitId + ")\n"
				+ "\tAnswer: " + actions, 1);
			return actions;
		}
	}
		
	/**
	 * Generates what the units player can see.
	 * @param player the player
	 * @return a map of MapPoints to unit ids
	 */
	private HashMap<MapPoint, Integer> generateMapForPlayer(int player, HashSet<MapPoint> visible)
	{
		HashMap<MapPoint, Integer> units = new HashMap<MapPoint, Integer>();
 		for(int i = 0; i < _unitInformation.size(); i++)
		{
			if(visible.contains(_unitInformation.get(i)._position))
			{
				units.put(new MapPoint(_unitInformation.get(i)._position), new Integer(i));
			}
		}
		log("\tGenerated map for player " + player + ": " + units, 1);
		return units;
	}
	
	/**
	 * Generates new map information and sends it to all players.
	 * @param action the last Action that occured
	 */
	private void updateMaps(Action action)
	{
		log("Updating maps for all players", 1);
		HashMap<MapPoint, Integer> playerMap;
		HashSet<MapPoint> visible;
		MapPoint newDestination;
		HashMap<MapPoint, Integer> allUnits;
		for(ObserverContainer obv : _observers)
		{
			allUnits = new HashMap<MapPoint, Integer>();
			for(int i = 0; i < _unitInformation.size(); i++)
			{
				allUnits.put(new MapPoint(_unitInformation.get(i)._position), new Integer(i));
			}
			obv._p.updateMap(allUnits, new Action(action));
		}
		for(int i = 0; i < _players.size(); i++)
		{
			if(!_playerInformation.get(i)._quit)
			{
				visible = getVisible(i);
				if(visible.contains(action._dest) || action._action == Action.ActionType.GAME_START)
				{
					_players.get(i).updateMap(generateMapForPlayer(i, visible), new Action(action));
				}
				else if(visible.contains(action._source))
				{
					playerMap = generateMapForPlayer(i, visible);
					newDestination = new MapPoint(action._source);
					for(MapPoint point : visible)
					{
						// Find a new, closer, and visible destination
						if((Math.abs(point._x - action._dest._x) + Math.abs(point._y - action._dest._y)) < (Math.abs(newDestination._x - action._dest._x) + Math.abs(newDestination._y - action._dest._y)))
						{
							newDestination = new MapPoint(point);
						}
					}
					_players.get(i).updateMap(playerMap, new Action(action._action, action._source, newDestination));
				}
				else
				{
					_players.get(i).updateMap(generateMapForPlayer(i, visible), null);
				}
			}
		}
	}
	
	/**
	 * Callback for quitting a game.
	 * @param p the Player
	 */
	@Override
	public void quit(Player p)
	{
		synchronized(_turnLockObject)
		{
			int player = getPlayerId(p);
			if(player != -2)
			{
				log("Player " + player + " has quit", 1);
				_playerInformation.get(player)._lost = true;
				_playerInformation.get(player)._quit = true;
				if(_turn == player)
				{
					log("\tMoving on to next turn", 1);
					nextTurn();
				}
				else
				{
					log("Player " + player + " has quit", 1);
					int winner = checkWinLoss();
					if(winner != -1)
					{
						log("Player " + winner + " has won the game, ending...", 1);
						endGame(winner);
						return;
					}
				}
			}
			else
			{
				for(ObserverContainer obv : _observers)
				{
					if(obv._p == p)
					{
						log("Observer has quit", 1);
						obv._quit = true;
					}
				}
			}
		}
	}
	
	/**
	 * Checks if someone has won/lost
	 * @return the id of the player that has won, -1 if no one has won
	 */
	private int checkWinLoss()
	{
		log("Checking for winners/losers...", 1);
		HashMap<MapPoint, Unit> units;
		for(int i = 0; i < _players.size(); i++)
		{
			units = new HashMap<MapPoint, Unit>();
			// Generate list of units
			for(int j = 0; j < _units.size(); j++)
			{
				if(_units.get(j)._owner == i && !_unitInformation.get(j)._position.equals(new MapPoint(-1, -1)))
				{
					units.put(new MapPoint(_unitInformation.get(j)._position), new Unit(_units.get(j)));
				}
			}
			if(_winCondition.hasPlayerWon(units, new PlayerInformation(_playerInformation.get(i))))
			{
				return i;
			}
			if(_winCondition.hasPlayerLost(units, new PlayerInformation(_playerInformation.get(i))) && !_playerInformation.get(i)._lost && !_playerInformation.get(i)._quit)
			{
				log("Player " + i + " has lost", 1);
				_playerInformation.get(i)._lost = true;
				_players.get(i).lost();
			}
		}
		int notLost = 0;
		int winner = -1;
		for(int i = 0; i < _playerInformation.size(); i++)
		{
			if(!_playerInformation.get(i)._lost && !_playerInformation.get(i)._quit)
			{
				notLost++;
				winner = i;
			}
		}
		if(notLost == 1)
		{
			return winner;
		}
		log("No one has won", 1);
		return -1;
	}
	
	/**
	 * Moves onto the next turn and calculates wins
	 */
	private void nextTurn()
	{
		int winner = checkWinLoss();
		if(winner != -1)
		{
			log("Player " + winner + " has won the game, ending...", 1);
			endGame(winner);
			return;
		}
		for(int i = 0; i < _unitInformation.size(); i++)
		{
			_unitInformation.get(i)._hasMoved = false;
			_unitInformation.get(i)._hasAttacked = false;
		}
		for(int i = 0; i < _players.size() - 1; i++)
		{
			_turn++;
			if(_turn == _players.size())
			{
				_turn = 0;
			}
			if(_playerInformation.get(_turn)._quit || _playerInformation.get(_turn)._lost)
			{
				continue;
			}
			for(int j = 0; j < _players.size(); j++)
			{
				_players.get(j).updateTurn(_turn);
			}
			int resourcesGained = 0;
			MapPoint point;
			for(int j = 0; j < _unitInformation.size(); j++)
			{
				point = _unitInformation.get(j)._position;
				if(_units.get(j)._owner == _turn && !point.equals(new MapPoint(-1, -1)) && TerrainConstants.isOfType(_terrain[point._x][point._y], TerrainConstants.RESOURCES))
				{
					resourcesGained += Constants.RESOURCES_PER_TURN;
				}
			}
			_playerInformation.get(i)._resources += resourcesGained;
			sendChatMessage(_turn, "You have gained " + resourcesGained + " resources.");
			TurnStartThread turnStartThread = new TurnStartThread(_players.get(_turn));
			turnStartThread.start();
			log("Start of turn for player " + _turn + "\n"
				+ "\tResources gained: " + resourcesGained, 1);
			return;
		}
		// If we have reached here, the game should be over...
		log("Everyone has lost, ending game...", 1);
		endGame(-1);
	}
	
	/**
	 * Ends the game and sends stats.
	 * @param winner the winner of the game
	 */
	private void endGame(int winner)
	{
		_turn = -2;
		for(int i = 0; i < _players.size(); i++)
		{
			if(!_playerInformation.get(i)._quit)
			{
				_players.get(i).end(new GameStats(winner));
			}
		}
		for(ObserverContainer obv : _observers)
		{
			if(!obv._quit)
			{
				obv._p.end(new GameStats(winner));
			}
		}
		sendChatMessage("Game over! Player " + getPlayerName(winner) + " has won!");
		log("GAME OVER\n"
			+ "Player " + winner + " won!", 1);
		closeLog();
		// Return control to the main class.
		_main.returnControl();
	}
	
	/**
	 * Sends a chat message to a player from the system.
	 * @param to the player to send the message to
	 * @param message the message to send
	 */
	private void sendChatMessage(int to, String message)
	{
		synchronized(_chatLockObject)
		{
			_players.get(to).chatMessage(-1, new String(message));
			log("System chat message\n"
				+ "\tTo player: " + to + "\n"
				+ "\tMessage: " + message, 1);
		}
	}
	
	/**
	 * Sends a chat message to all players from the system.
	 * @param message the message to send
	 */
	private void sendChatMessage(String message)
	{
		synchronized(_chatLockObject)
		{
			for(int i = 0; i < _players.size(); i++)
			{
				_players.get(i).chatMessage(-1, new String(message));
			}
			for(ObserverContainer obv : _observers)
			{
				obv._p.chatMessage(-1, new String(message));
			}
			log("System chat message\n"
				+ "\tMessage: " + message, 1);
		}
	}
	
	/**
	 * Sends a chat message to all players.
	 * @param p the Player
	 * @param message the message to send
	 */
	@Override
	public void sendChatMessage(Player p, String message)
	{
		if(message.length() > 0)
		{
			synchronized(_chatLockObject)
			{
				message = new String(message);
				int from = getPlayerId(p);
				if(from == -2)
				{
					return;
				}
				for(int i = 0; i < _players.size(); i++)
				{
					_players.get(i).chatMessage(from, new String(message));
				}
				for(ObserverContainer obv : _observers)
				{
					obv._p.chatMessage(from, new String(message));
				}
				log("Chat message from player " + from + "\n"
					+ "\tMessage: " + message, 1);
			}
		}
		else
		{
			log("Player " + getPlayerId(p) + " tried to send an empty message", 1);
		}
	}
	
	/**
	 * Callback for placing a unit.
	 * @param p the Player
	 * @param coords the MapPoint at which the unit should be placed
	 * @param name the name of the unit
	 * @return the ID of the unit that has been placed, or -1 if placement fails
	 */
	@Override
	public int placeUnit(Player p, MapPoint coords, String name)
	{
		synchronized(_turnLockObject)
		{
			coords = new MapPoint(coords);
			int player = getPlayerId(p);
			if(player == -2)
			{
				return -1;
			}
			if(_turn != -1)
			{
				log("Player " + player + " called placeUnit(coords: " + coords + ", name: " + name + ") but the game had started\n"
					+ "\tTurn: " + _turn + "\n"
					+ "\tAnswer: -1", 2);
				return -1;
			}
			if(getRemainingUnits(p) == 0)
			{
				log("Player " + player + " called placeUnit(coords: " + coords + ", name: " + name + ") but did not have any units remaining to be placed\n"
					+ "\tAnswer: false", 2);
				return -1;
			}
			if(CoreActions.getUnitAtPoint(coords, _unitInformation) == -1 && _startingLocations.get(player).contains(coords) && !TerrainConstants.isOfType(_terrain[coords._x][coords._y], TerrainConstants.WATER))
			{
				Unit toAdd = new Unit(new String(name), player, _units.size());
				Block heartBlock = new Block(Constants.HEART_HEALTH);
				heartBlock._bonuses.put(Block.BonusType.HEART, 1);
				// Vary heart size based on unit size
				if(toAdd._size % 2 == 0)
				{
					// Add a 2x2 block
					for(int i = Constants.UNIT_SIZE / 2 - 1; i <= Constants.UNIT_SIZE / 2; i++)
					{
						for(int j = Constants.UNIT_SIZE / 2 - 1; j <= Constants.UNIT_SIZE / 2; j++)
						{
							toAdd._blocks.put(new MapPoint(i, j), new Block(heartBlock));
						}
					}
					toAdd._stats.put(Block.BonusType.HEART, 4);
				}
				else
				{
					// Add a 3x3 block
					for(int i = (int) Math.floor(Constants.UNIT_SIZE / 2) - 1; i <= (int) Math.floor(Constants.UNIT_SIZE / 2) + 1; i++)
					{
						for(int j = (int) Math.floor(Constants.UNIT_SIZE / 2) - 1; j <= (int) Math.floor(Constants.UNIT_SIZE / 2) + 1; j++)
						{
							toAdd._blocks.put(new MapPoint(i, j), new Block(heartBlock));
						}
					}
					toAdd._stats.put(Block.BonusType.HEART, 9);
				}
				_units.add(toAdd);
				_unitInformation.add(new UnitInformation(coords));
				log("Player " + player + " called placeUnit(coords: " + coords + ", name: " + name + ")\n"
					+ "\tAnswer: " + (_units.size() - 1), 1);
				return _units.size() - 1;
			}
			else
			{
				log("Player " + player + " called placeUnit(coords: " + coords + ", name: " + name + ") but tried to place a unit in an invalid location\n"
					+ "\tUnit at point: " + CoreActions.getUnitAtPoint(coords, _unitInformation) + "\n"
					+ "\tAnswer: -1", 2);
				return -1;
			}
		}
	}
	
	/**
	 * Callback for updating a unit.
	 * @param p the Player
	 * @param unitId the id of the unit
	 * @param pieceId the id of the piece to add
	 * @param coords the coordinates in the unit to place the piece
	 * @return true if the piece is added sucessfuly, false otherwise
	 */
	@Override
	public boolean updateUnit(Player p, int unitId, int pieceId, MapPoint coords)
	{
		synchronized(_turnLockObject)
		{
			int player = getPlayerId(p);
			if(player == -2)
			{
				return false;
			}
			coords = new MapPoint(coords);
			if(_turn != -1 && _turn != player)
			{
				log("Player " + player + " called updateUnit(unitId: " + unitId + ", pieceId: " + pieceId + ", coords: " + coords + ") but it was not their turn\n"
					+ "\tTurn was: " + _turn + "\n"
					+ "\tAnswer: false", 2);
				return false;
			}
			if(pieceId < 0 || pieceId >= _pieces.size() || unitId < 0 || unitId >= _units.size())
			{
				log("Player " + player + " called updateUnit(unitId: " + unitId + ", pieceId: " + pieceId + ", coords: " + coords + ") but there was an invalid piece or unit id\n"
					+ "\tAnswer: false", 2);
				return false;
			}
			Piece piece = _pieces.get(pieceId);
			Unit unit = _units.get(unitId);
			if(unit._owner != player)
			{
				log("Player " + player + " called updateUnit(unitId: " + unitId + ", pieceId: " + pieceId + ", coords: " + coords + ") but they did not own the unit\n"
					+ "\tAnswer: false", 2);
				return false;
			}
			int resources = _playerInformation.get(player)._resources;
			if(piece._cost > resources)
			{
				log("Player " + player + " called updateUnit(unitId: " + unitId + ", pieceId: " + pieceId + ", coords: " + coords + ") but they have enough resources\n"
					+ "\tResources: " + resources + "\n"
					+ "\tAnswer: false", 2);
				return false;
			}
			for(MapPoint key : piece._blocks.keySet())
			{
				if(unit._blocks.containsKey(new MapPoint(coords._x + key._x, coords._y + key._y)))
				{
					log("Player " + player + " called updateUnit(unitId: " + unitId + ", pieceId: " + pieceId + ", coords: " + coords + ") but there was a collision\n"
						+ "\tCollision location on the unit: " + (new MapPoint(coords._x + key._x, coords._y + key._y)) + "\n"
						+ "\tAnswer: false", 2);
					return false;
				}
				if((coords._x + key._x) < 0 || (coords._x + key._x) >= Constants.UNIT_SIZE || (coords._y + key._y) < 0 || (coords._y + key._y) >= Constants.UNIT_SIZE)
				{
					log("Player " + player + " called updateUnit(unitId: " + unitId + ", pieceId: " + pieceId + ", coords: " + coords + ") but piece went out of bounds\n"
						+ "\tLocation out of bounds: " + (new MapPoint(coords._x + key._x, coords._y + key._y)) + "\n"
						+ "\tAnswer: false", 2);
					return false;
				}
			}
			Block toAdd;
			for(MapPoint key : piece._blocks.keySet())
			{
				toAdd = new Block(piece._blocks.get(key));
				for(Block.BonusType bonus : toAdd._bonuses.keySet())
				{
					unit._stats.put(bonus, toAdd._bonuses.get(bonus) + unit._stats.get(bonus));
				}
				unit._blocks.put(new MapPoint(coords._x + key._x, coords._y + key._y), toAdd);
			}
			_playerInformation.get(player)._resources -= piece._cost;
			log("Player " + player + " called updateUnit(unitId: " + unitId + ", pieceId: " + pieceId + ", coords: " + coords + ")\n"
				+ "\tAnswer: true", 1);
			return true;
		}
	}
	
	/**
	 * Gets a player's name
	 * @param player the player's id
	 * @return the player's name
	 */
	@Override
	public String getPlayerName(int player)
	{
		String name;
		if(player == -1)
		{
			name = "System Message";
		}
		else if(player == -2)
		{
			name = "Observer";
		}
		else
		{
			name = new String(_playerInformation.get(player)._name);
		}
		log("getPlayerName(player: " + player + ")\n"
			+ "\tAnswer: " + name, 1);
		return name;
	}
	
	/**
	 * Get's a player's resources.
	 * @param p the Player
	 * @return the Player's recourses
	 */
	@Override
	public int getResources(Player p)
	{
		int player = getPlayerId(p);
		if(player == -2)
		{
			return 0;
		}
		int resources = _playerInformation.get(player)._resources;
		log("Player " + player + " called getResources()\n"
			+ "\tAnswer: " + resources, 1);
		return resources;
	}
	
	/**
	 * Returns information about a specific unit.
	 * @param p the Player who is requesting the information
	 * @param unitId the id of the unit
	 * @return a copy of the Unit
	 */
	@Override
	public Unit getUnit(Player p, int unitId)
	{
		int player = getPlayerId(p);
		if(unitId >= _units.size() && unitId < 0)
		{
			log("Player " + player + " called getUnit(unitId: " + unitId + ") on a unit that did not exist", 2);
			return null;
		}
		Unit unit = new Unit(_units.get(unitId));
		if(!getVisible(player).contains(_unitInformation.get(unitId)._position) && player != -2)
		{
			log("Player " + player + " called getUnit(unitId: " + unitId + ") on a unit that did belong to them", 2);
			return null;
		}
		log("Player " + player + " called getUnit(unitId: " + unitId + ")\n"
			+ "\tAnswer: (success)", 1);
		return unit;
		
	}
	
	/**
	 * Gets the visible area for a player.
	 * @param p the Player
	 * @return the MapPoints that the player can see
	 */
	private HashSet<MapPoint> getVisible(int player)
	{
		HashSet<MapPoint> visible = new HashSet<MapPoint>();
		Set<MapPoint> unitVisible;
		for(int i = 0; i < _units.size(); i++)
		{
			if(_units.get(i)._owner == player)
			{
				unitVisible = CoreActions.getAreaForUnit(i, 2, _units, _unitInformation, _terrain);
				for(MapPoint point : unitVisible)
				{
					visible.add(point);
				}
			}
		}
		/*log("getVisible(player: " + player + ")\n"
			+ "\tAnswer was: " + visible, 1);*/
			// TODO add back in when we are generating less data
		return visible;
	}
		
	/**
	 * Returns the number of units left to build for a given player.
	 * @param p the Player we are checking
	 * @return the number of units the Player can build
	 */
	@Override
	public int getRemainingUnits(Player p)
	{
		int built = 0;
		int player = getPlayerId(p);
		if(player == -2)
		{
			return 0;
		}
		for(int i = 0; i < _units.size(); i++)
		{
			if(_units.get(i)._owner == player)
			{
				built++;
			}
		}
		int remaining = _playerInformation.get(player)._maxUnits - built;
		log("Player " + player + " called getRemainingUnits()\n"
			+ "\tAnswer was: " + remaining, 1);
		return remaining;
	}
	
	/**
	 * Logs a message.
	 * @param message the message to log
	 * @param level the severity level: 1 = debug, 2 = warning, 3 = error
	 */
	private synchronized void log(String message, int level)
	{
		try
		{
			if(Constants.DEBUG_LEVEL == -1 || _turn == -2)
			{
				return;
			}
			if(level > Constants.DEBUG_LEVEL)
			{
				if(level == 2)
				{
					_log.write("WARNING: ");
				}
				else if(level == 3)
				{
					_log.write("ERROR: ");
				}
				_log.write(message + "\n");
			}
			_log.flush();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Flushes and closes the log.
	 */
	private void closeLog()
	{
		try
		{
			_log.flush();
			_log.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns if the game is done.
	 * @return true if the game is over, false otherwise
	 */
	@Override
	public boolean done()
	{
		return _turn == -2;
	}
}