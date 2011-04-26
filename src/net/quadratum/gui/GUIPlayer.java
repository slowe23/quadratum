package net.quadratum.gui;

import net.quadratum.core.*;

import java.util.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class GUIPlayer implements Player {
	private Core _core;
	private int _id;
	
	private ChatHandler _chatHandler;
	private GraphicsCoordinator _graphicsCoordinator;
	private UnitHandler _unitHandler;
	
	public GUIPlayer() {
		_chatHandler = new GUIPlayerChatHandler();
		_graphicsCoordinator = new GUIPlayerGraphicsCoordinator();
		_unitHandler = new GUIPlayerUnitHandler();
	}
	
	public void createWindow() {
		GameWindow window = new GameWindow(this);
		window.setVisible(true);
	}
	
	/**
	 * Notifies the player that there is a new game.
	 * @param core the game core
	 * @param id the ID for this Player
	 * @param mapData the MapData this game is using.
	 * @param totalPlayers the number of players in the game, including this one
	 */
	public void start(Core core, MapData mapData, int id, int totalPlayers) {
		_core = core;
		_id = id;
		
		//TODO: more stuff
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
	
	public ChatHandler getChatHandler() {
		return _chatHandler;
	}
	
	public GraphicsCoordinator getGraphicsCoordinator() {
		return _graphicsCoordinator;
	}
	
	public UnitHandler getUnitHandler() {
		return _unitHandler;
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
			return Color.RED;
		}
		
		public BufferedImage getTerrainTile(int terrainValue, int size) {
			Color c = getTerrainColor(terrainValue);
			
			BufferedImage toRet = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);  //TODO: caching
			
			Graphics g = toRet.getGraphics();
			g.fillRect(0, 0, size, size);
			
			return toRet;
		}
		
		public Color getTerrainColor(int terrainValue) {
			return Color.GREEN;
		}
		
		public BufferedImage getUnitImage(Unit u, int blockSize) {
			int size = u._size*blockSize;
			
			//TODO
			BufferedImage toRet = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
			
			Graphics g = toRet.getGraphics();
			
			g.setColor(getForegroundColor());
			g.fillRect(0, 0, size, size);

			for(MapPoint p : u._blocks.keySet()) {
				Block b = u._blocks.get(p);
				g.setColor(getBlockBaseColor(b));
				g.fillRect(p._x*blockSize, p._y*blockSize, blockSize, blockSize);
			}
			
			g.setColor(getPlayerColor(u._owner));
			g.drawRect(0, 0, size-1, size-1);

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
}