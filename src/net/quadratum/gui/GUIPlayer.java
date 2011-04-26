package net.quadratum.gui;

import net.quadratum.core.*;

import java.util.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class GUIPlayer implements Player {
	private static final Color[] PLAYER_COLORS = {Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.ORANGE, Color.PINK};
	
	private Core _core;
	private int _id;
	
	public final ChatHandler _chatHandler;
	public final GraphicsCoordinator _graphicsCoordinator;
	public final UnitHandler _unitHandler;
	public final GameplayHandler _gameplayHandler;
	
	public GUIPlayer() {
		_chatHandler = new GUIPlayerChatHandler();
		_graphicsCoordinator = new GUIPlayerGraphicsCoordinator();
		_unitHandler = new GUIPlayerUnitHandler();
		_gameplayHandler = new GUIPlayerGameplayHandler();
	}
	
	/**
	 * Notifies the player that there is a new game.
	 * @param core the game core
	 * @param id the ID for this Player
	 * @param mapData the MapData this game is using.
	 * @param totalPlayers the number of players in the game, including this one
	 */
	public void start(Core core, MapData mapData, int id, int totalPlayers) {
		GameWindow window = new GameWindow(this);
		window.setVisible(true);
		
		_core = core;
		_id = id;
		
		_gameplayHandler.startGame(mapData);
	}
	
	/**
	 * Notifies the player of the pieces that are now available
	 *
	 * @param pieces the Pieces that are available for use.
	 */
	public void updatePieces(List<Piece> pieces) {
		//TODO
	}
	
	/**
	 * Notifies the player that he has lost.
	 */
	public void lost() {
		//TODO
	}
	
	/**
	 * Notifies the player that the game has ended.
	 * @param stats Game stats for the game that was just played.
	 */
	public void end(GameStats stats) {
		//TODO
	}
	
	/**
	 * Notifies the player that their turn has started.
	 */
	public void turnStart() {
		//TODO
	}
	
	/**
	 * Updates the map data
	 * @note Currently unused but should be supported for future flexibility
	 */
	public void updateMapData(MapData mapData) {
		//TODO
	}
	
	/**
	 * Updates the position of units on the map.
	 * @param units new positions of units.
	 * @param lastAction The action that caused this update
	 */
	public void updateMap(Map<MapPoint, Integer> units, Action lastAction) {
		//TODO
	}
	
	/**
	 * Notifies the player of a chat message.
	 * @param from the ID of the player who this message was sent by.
	 * @param message the message that is being sent.
	 */
	public void chatMessage(int from, String message) {
		_chatHandler.getMessage(from, message);
	}
	
	private class GUIPlayerChatHandler implements ChatHandler {
		private ChatPanel _chat;
		
		public GUIPlayerChatHandler() { }
		
		public void setChatPanel(ChatPanel chien) {
			_chat = chien;
		}
		
		public void sendMessage(String message) {
			if(_core!=null)
				_core.sendChatMessage(GUIPlayer.this, message);
		}
		
		public void getMessage(int id, String message) {
			if(_chat!=null) {
				_chat.addMessage(id, message);
				_chat.repaint();
			}
		}
		
		public String getPlayerName(int id) {
			if(_core!=null)
				return _core.getPlayerName(id);
			else
				return "";
		}
	}
	
	private class GUIPlayerGraphicsCoordinator implements GraphicsCoordinator {
		public GUIPlayerGraphicsCoordinator() { }
		
		public Color getBackgroundColor() {
			return Color.BLACK;
		}
		
		public Color getNeutralColor() {
			return Color.GRAY;
		}
		
		public Color getForegroundColor() {
			return Color.WHITE;
		}
		
		public Color getPlayerColor(int id) {
			if(id>=0 && id < PLAYER_COLORS.length)
				return PLAYER_COLORS[id];
			else
				return Color.GRAY;
		}
		
		public BufferedImage getTerrainTile(int terrainValue, int size) {
			
			BufferedImage toRet = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);  //TODO: caching
			
			Graphics g = toRet.getGraphics();
			g.setColor(getTerrainColor(terrainValue));
			g.fillRect(0, 0, size, size);
			
			return toRet;
		}
		
		public Color getTerrainColor(int terrainValue) {
			if(TerrainConstants.isOfType(terrainValue, TerrainConstants.WATER)) {
				return new Color(0, 0, 127);
			} else {
				if(TerrainConstants.isOfType(terrainValue, TerrainConstants.BUNKER)) {
					return new Color(63, 63, 63);
				} else {
					if(TerrainConstants.isOfType(terrainValue, TerrainConstants.MOUNTAIN)) {
						return new Color(127, 63, 0);
					} else {
						if(TerrainConstants.isOfType(terrainValue, TerrainConstants.RESOURCES)) {
							return new Color(127, 127, 0);
						} else {
							return new Color(0, 127, 0);
						}
					}
				}
			}
		}
		
		public BufferedImage getUnitImage(Unit unit, int blockSize) {
			int size = unit._size*blockSize;
			
			BufferedImage toRet = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
			
			Graphics g = toRet.getGraphics();
			
			g.setColor(getForegroundColor());
			g.fillRect(0, 0, size, size);

			for(MapPoint p : unit._blocks.keySet()) {
				Block b = unit._blocks.get(p);
				g.setColor(getBlockBaseColor(b));
				g.fillRect(p._x*blockSize, p._y*blockSize, blockSize, blockSize);
			}
			
			g.setColor(getPlayerColor(unit._owner));
			g.drawRect(0, 0, size-1, size-1);

			return toRet;
		}
		
		public BufferedImage getPieceImage(Piece piece, int blockSize) {
			int[] bounds = piece.getBounds();
			int xsize = bounds[2]-bounds[0]+1, ysize = bounds[3]-bounds[1]+1;
			int offx = -1*bounds[0], offy = -1*bounds[1];
			
			BufferedImage toRet = new BufferedImage(xsize, ysize, BufferedImage.TYPE_INT_ARGB);
			
			Graphics g = toRet.getGraphics();
			BufferedImage mask = getBlockMask(blockSize);
			
			for(MapPoint mP : piece._blocks.keySet()) {
				Block b = piece._blocks.get(mP);
				int x = (offx+mP._x)*blockSize, y = (offy+mP._y)*blockSize;
				
				g.setColor(getBlockBaseColor(b));
				g.fillRect(x, y, blockSize, blockSize);
				
				g.drawImage(mask, x, y, blockSize, blockSize, null);
			}
			
			return toRet;
		}
		
		public BufferedImage getBlockImage(Block b, int size) {
			BufferedImage toRet = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
			
			Graphics g = toRet.getGraphics();
			g.setColor(getBlockColor(b));
			g.fillRect(0, 0, size, size);
			
			g.drawImage(getBlockMask(size), 0, 0, size, size, null);
			
			return toRet;
		}
		
		public BufferedImage getBlockMask(int size) {
			BufferedImage mask = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
			
			Graphics g = mask.getGraphics();
			
			int bevelSize = Math.min(5, size/10);
			
			if(bevelSize>0) {
				//Draw sides of bevel
				Color light = new Color(255, 255, 255, 63);
				g.setColor(light);
				g.fillRect(0, 0, size-bevelSize, bevelSize);
				g.fillRect(0, bevelSize, bevelSize, size-2*bevelSize);
				
				Color dark = new Color(0, 0, 0, 63);
				g.setColor(dark);
				g.fillRect(bevelSize, size-bevelSize, size-bevelSize, bevelSize);
				g.fillRect(size-bevelSize, bevelSize, bevelSize, size-2*bevelSize);
				
				//Draw corners of bevel (inefficient but the scale is small so it doesn't matter)
				int lrgb = light.getRGB(), drgb = dark.getRGB();
				for(int x = 0; x<bevelSize; x++) {
					for(int y = 0; y<bevelSize; y++) {
						int set = (x<y) ? lrgb : drgb;
						mask.setRGB(size-bevelSize+x, y, set);
						mask.setRGB(0, size-bevelSize+y, set);	
					}
				}
			}
			
			return mask;
		}
		
		public Color getBlockColor(Block b) {
			Color c = getBlockBaseColor(b);
			double healthAmount = b._health/((double)(b._totalHealth));
			return new Color((int)(c.getRed()*healthAmount), (int)(c.getBlue()*healthAmount), (int)(c.getGreen()*healthAmount));
		}
		
		public Color getBlockBaseColor(Block b) {
			return Color.blue;  //TODO: change
		}
	}
	
	private class GUIPlayerUnitHandler implements UnitHandler {
		private Map<MapPoint, Unit> _units;
		private Unit _selectedUnit;
		
		private UnitImagePanel _unitImagePanel;
		private MapPanel _mapPanel;
		
		public GUIPlayerUnitHandler() { }
		
		public Map<MapPoint, Unit> getUnits() {
			return _units;
		}
		
		public Unit getSelectedUnit() {
			return _selectedUnit;
		}
		
		public void setUnits(Map<MapPoint, Unit> units) {
			_units = units;
			//TODO: appropriate notifications
		}
		
		public void setSelectedUnit(Unit unit) {
			_selectedUnit = unit;
			//TODO: appropriate notifications
		}
	}
	
	private class GUIPlayerGameplayHandler implements GameplayHandler {
		private MapPanel _mapPanel;
		private MinimapPanel _minimapPanel;
		
		public GUIPlayerGameplayHandler() { }
		
		public void setMapPanel(MapPanel mapPanel) {
			if(_mapPanel==null) {
				_mapPanel = mapPanel;
				_minimapPanel = mapPanel.getMinimapPanel();
			} else
				throw new RuntimeException("Map panel can only be set once.");
		}
		
		public void startGame(MapData m) {
			if(_mapPanel==null)
				throw new RuntimeException("Map panel not set.");
			if(_minimapPanel==null)
				throw new RuntimeException("Minimap panel not set.");
			
			_mapPanel.setTerrain(m._terrain);  //This automatically sets the minimap terrain as well
			_mapPanel.repaint();
			_minimapPanel.repaint();
		}
	}
}