package net.quadratum.gui;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.util.*;
import java.util.Map.Entry;

import net.quadratum.core.*;
import net.quadratum.core.Action.ActionType;

/** A class that provides graphical methods that are used by the various GUI components */
public class DrawingMethods {
	public static final Color BACKGROUND_COLOR = Color.BLACK, NEUTRAL_COLOR = Color.GRAY, FOREGROUND_COLOR = Color.WHITE;
	
	public static final Color LAND_COLOR = new Color(0, 127, 63);
	public static final Color WATER_COLOR = new Color(0, 0, 191);
	public static final Color MOUNTAIN_COLOR = new Color(126, 72, 19);
	public static final Color BUNKER_COLOR = new Color(63, 63, 63);
	public static final Color RESOURCES_COLOR = new Color(191, 191, 0);
	public static final Color IMPASSIBLE_COLOR = new Color(255, 63, 0);
	
	private static final Color[] PLAYER_COLORS = {Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.ORANGE, Color.PINK};
	
	public DrawingMethods() { }
	
	/**
	 * Gets the Color of a given player
	 *
	 * @param player The id of the player in question.
	 */
	public Color getPlayerColor(int player) {
		if(player>=0 && player<PLAYER_COLORS.length)
			return PLAYER_COLORS[player];
		else
			return Color.WHITE;
	}
	
	/**
	 * Gets the color of a terrain tile
	 *
	 * @param terrainValue An integer representing the terrain type
	 */
	public Color getTerrainTileColor(int terrainValue) {
		if(TerrainConstants.isOfType(terrainValue, TerrainConstants.IMPASSABLE))
			return IMPASSIBLE_COLOR;
		else if(TerrainConstants.isOfType(terrainValue, TerrainConstants.WATER))
			return WATER_COLOR;
		else if(TerrainConstants.isOfType(terrainValue, TerrainConstants.RESOURCES))
			return RESOURCES_COLOR;
		else
			return LAND_COLOR;
	}
	
	/**
	 * Draws terrain features for a given terrain tile
	 *
	 * @param g A Graphics object whose origin is the upper left corner of the tile
	 * @param terrainValue An integer representing the terrain type
	 * @param size The size of the tile (and of the Graphics object's clip rectangle)
	 */
	public void drawTerrainFeatures(Graphics g, int terrainValue, int size) {
		
		int s = size-4;
		int sta = 2;
		int end = size-1-2;
		
		if(TerrainConstants.isOfType(terrainValue, TerrainConstants.WATER)) {
			g.setColor(WATER_COLOR);
			g.fillRoundRect(sta, sta, s, s, s/2, s/2);
		}
		   
		if(TerrainConstants.isOfType(terrainValue, TerrainConstants.MOUNTAIN)) {
			g.setColor(MOUNTAIN_COLOR);
			g.fillPolygon(new Polygon(new int[]{sta, sta+s/2, end}, new int[]{end, sta, end}, 3));
		}
		
		if(TerrainConstants.isOfType(terrainValue, TerrainConstants.BUNKER)) {
			g.setColor(BUNKER_COLOR);
			g.fillPolygon(new Polygon(new int[]{sta, sta+s/4, end-s/4, end}, new int[]{end-s/4, sta+s/4, sta+s/4, end-s/4}, 4));
		}
		
		if(TerrainConstants.isOfType(terrainValue, TerrainConstants.RESOURCES)) {
			g.setColor(RESOURCES_COLOR);
			g.fillPolygon(new Polygon(new int[]{sta, sta+s/2, end, sta+s/2}, new int[]{sta+s/2, sta, sta+s/2, end}, 4));
		}
		
		if(TerrainConstants.isOfType(terrainValue, TerrainConstants.IMPASSABLE)) {
			g.setColor(IMPASSIBLE_COLOR);
			g.fillRoundRect(sta, sta, s, s, s/2, s/2);
		}
	}
	
	/**
	 * Draws a single terrain tile
	 *
	 * @param g A Graphics object whose origin is the upper left corner of the tile
	 * @param terrainValue An integer representing the terrain type
	 * @param size The size of the tile (and of the Graphics object's clip rectangle)
	 * @param showGrid Whether or not to draw in grid lines for the tile
	 */
	public void drawTerrainTile(Graphics g, int terrainValue, int size, boolean showGrid) {
		g.setColor(LAND_COLOR);
		g.fillRect(0, 0, size, size);
		
		drawTerrainFeatures(g, terrainValue, size);
		
		if(showGrid) {
			g.setColor(StaticMethods.applyAlpha(NEUTRAL_COLOR, 191));
			g.drawRect(0, 0, size-1, size-1);
		}
	}
	
	/**
	 * Gets an image of a given terrain tile
	 *
	 * @param terrainValue An integer representing the terrain type
	 * @param size The size of the tile (and of the resulting image)
	 * @param showGrid Whether or not to draw in grid lines for the tile
	 */
	public BufferedImage getTerrainTileImage(int terrainValue, int size, boolean showGrid) {
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		drawTerrainTile(img.getGraphics(), terrainValue, size, showGrid);
		return img;
	}
	
	/**
	 * Gets the partially transparent color to be used as a mask over the placement area
	 *
	 * @param player The id of the player in question.
	 * @param placement Whether the mask is for the placement or non-placement area
	 */
	public Color getPlacementMaskColor(int player, boolean placement) {
		if(placement) {
			Color pCol = getPlayerColor(player);
			return new Color((255+pCol.getRed())/2, (255+pCol.getGreen())/2, (255+pCol.getBlue())/2, 127);
		} else
			return new Color(0, 0, 0, 63);
	}
	
	/**
	 * Draws the placement mask over a map tile
	 *
	 * @param g A Graphics object whose origin is the upper left corner of the tile
	 * @param player The id of the player in question.
	 * @param placementArea The set of all map points in the placement area
	 * @param location The map point where the tile to be drawn is located
	 * @param size The size of the tile (and of the Graphics object's clip rectangle)
	 */
	public void drawPlacementMask(Graphics g, int player, Set<MapPoint> placementArea, MapPoint location, int size) {
		if(placementArea.contains(location)) {
			g.setColor(getPlacementMaskColor(player, true));
			
			int th = Math.min(5, size/6);
			
			boolean north = !(placementArea.contains(new MapPoint(location._x, location._y-1)));
			boolean south = !(placementArea.contains(new MapPoint(location._x, location._y+1)));
			boolean east = !(placementArea.contains(new MapPoint(location._x+1, location._y)));
			boolean west = !(placementArea.contains(new MapPoint(location._x-1, location._y)));
			
			if(north)
				g.fillRect(th, 0, size-2*th, th);
			if(south)
				g.fillRect(th, size-th, size-2*th, th);
			if(east)
				g.fillRect(size-th, th, th, size-2*th);
			if(west)
				g.fillRect(0, th, th, size-2*th);
			
			if(north || east || !(placementArea.contains(new MapPoint(location._x+1, location._y-1))))
				g.fillRect(size-th, 0, th, th);
			if(north || west || !(placementArea.contains(new MapPoint(location._x-1, location._y-1))))
				g.fillRect(0, 0, th, th);
			if(south || east || !(placementArea.contains(new MapPoint(location._x+1, location._y+1))))
				g.fillRect(size-th, size-th, th, th);
			if(south || west || !(placementArea.contains(new MapPoint(location._x-1, location._y+1))))
				g.fillRect(0, size-th, th, th);
		} else {
			g.setColor(getPlacementMaskColor(player, false));
			g.fillRect(0,0,size,size);
		}
	}
	
	/**
	 * Gets an image of the placement mask for a map tile
	 *
	 * @param player The id of the player in question.
	 * @param placementArea The set of all map points in the placement area
	 * @param location The map point where the tile to be drawn is located
	 * @param size The size of the tile (and of the resulting image)
	 */
	public BufferedImage getPlacementMaskImage(int player, Set<MapPoint> placementArea, MapPoint location, int size) {
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		drawPlacementMask(img.getGraphics(), player, placementArea, location, size);
		return img;
	}
	
	/**
	 * Gets the partially transparent color to be used as a mask over the visible area
	 *
	 * @param sight Whether the mask is for the visible or non-visible area
	 */
	public Color getSightMaskColor(boolean sight) {
		if(sight)
			return new Color(0, 0, 0, 0);
		else
			return new Color(0, 0, 0, 127);
	}
	
	/**
	 * Draws the visible area mask over a tile
	 *
	 * @param g A Graphics object whose origin is the upper left corner of the tile
	 * @param sight Whether the mask is for the visible or non-visible area
	 * @param size The size of the tile (and of the Graphics object's clip rectangle)
	 */
	public void drawSightMask(Graphics g, boolean sight, int size) {
		g.setColor(getSightMaskColor(sight));
		g.fillRect(0, 0, size, size);
	}
	
	/**
	 * Creates an image of the visible area mask for a tile
	 *
	 * @param sight Whether the mask is for the visible or non-visible area
	 * @param size The size of the tile (and of the resulting image)
	 */
	public BufferedImage getSightMaskImage(boolean sight, int size) {
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		drawSightMask(img.getGraphics(), sight, size);
		return img;
	}
	
	/**
	 * Gets the Color corresponding to the given Block bonus type
	 *
	 * @param bonus The bonus type
	 */
	public Color getBonusTypeColor(Block.BonusType bonus) {
		switch(bonus) {
			case ATTACK:
				return Color.RED;
			case ATTACK_WIDTH:
				return new Color(127, 0, 255);
			case RANGE:
				return Color.ORANGE;
			case DEFENSE:
				return new Color(191, 191, 0);
			case MOVEMENT:
				return Color.CYAN;
			case SIGHT:
				return Color.GREEN;
			case WATER_MOVEMENT:
				return Color.BLUE;
			case HEART:
				return Color.MAGENTA;
			default:
				return Color.GRAY;
		}
	}
	
	/**
	 * Gets the Color for a given block before adjusting for health
	 *
	 * @param b the Block in question
	 */
	public Color getBlockBaseColor(Block b) {
		int red = 0, green = 0, blue = 0;
		for(Block.BonusType type : b._bonuses.keySet()) {
			Color c = getBonusTypeColor(type);
			red += c.getRed();
			green += c.getGreen();
			blue += c.getBlue();
		}
		int nBonuses = b._bonuses.size();
		if(nBonuses>0) {
			red /= nBonuses;
			green /= nBonuses;
			blue /= nBonuses;
		}
		return new Color(red, green, blue);
	}
	
	/**
	 * Gets the Color for a given block
	 *
	 * @param b The block in question
	 */
	public Color getBlockColor(Block b) {
		Color c = getBlockBaseColor(b);
		
		double healthAmount = b._health/((double)(b._totalHealth));
		if(healthAmount<0)
			healthAmount = 0;
		if(healthAmount>1)
			healthAmount = 1;
		
		return new Color((int)(c.getRed()*healthAmount), (int)(c.getGreen()*healthAmount), (int)(c.getBlue()*healthAmount));
	}
	
	/**
	 * Draws a block
	 *
	 * @param g A Graphics object whose origin is the upper left corner of the block
	 * @param b The block in question
	 * @param blockSize The size of the block (and of the Graphics object's clip rectangle)
	 */
	public void drawBlock(Graphics g, Block b, int blockSize) {
		g.setColor(getBlockColor(b));
		g.fillRect(0, 0, blockSize, blockSize);
		
		drawBlockMask(g, blockSize);
	}
	
	/**
	 * Gets an image of a given block
	 *
	 * @param b The block in question
	 * @param blockSize The size of the block (and of the resulting image)
	 */
	public BufferedImage getBlockImage(Block b, int blockSize) {
		BufferedImage img = new BufferedImage(blockSize, blockSize, BufferedImage.TYPE_INT_ARGB);
		drawBlock(img.getGraphics(), b, blockSize);
		return img;
	}
	
	/**
	 * Draws a decorative mask over a given block
	 *
	 * @param g A Graphics object whose origin is the upper left corner of the block
	 * @param blockSize The size of the block (and of the Graphics object's clip rectangle)
	 */
	public void drawBlockMask(Graphics g, int blockSize) {
		int bevelSize = Math.min(5, blockSize/6);
		
		if(bevelSize>0) {
			//Draw sides of bevel
			g.setColor(new Color(255, 255, 255, 127));
			for(int i = 0; i<bevelSize; i++) {
				g.drawLine(0, i, blockSize-1-i, i);
				g.drawLine(i, bevelSize, i, blockSize-1-i);
			}
			
			g.setColor(new Color(0, 0, 0, 127));
			for(int i = 0; i<bevelSize; i++) {
				g.drawLine(i+1, blockSize-1-i, blockSize-1, blockSize-1-i);
				g.drawLine(blockSize-1-i, i+1, blockSize-1-i, blockSize-1-bevelSize);
			}
		}
	}
	
	/**
	 * Gets an image of the mask to draw over a given block
	 *
	 * @param blockSize The size of the block (and of the Graphics object's clip rectangle)
	 */
	public BufferedImage getBlockMaskImage(int blockSize) {
		BufferedImage img = new BufferedImage(blockSize, blockSize, BufferedImage.TYPE_INT_ARGB);
		drawBlockMask(img.getGraphics(), blockSize);
		return img;
	}
	
	/**
	 * Draws a given unit
	 *
	 * @param g A Graphics object whose origin is the upper left corner of the tile where the unit is located
	 * @param unit The unit in question
	 * @param blockSize The size of the blocks making up the unit
	 * @param size The size of the tile where the unit is located (and of the Graphics object's clip rectangle)
	 */
	public void drawUnit(Graphics g, Unit unit, int blockSize, int size) {
		int uSize = (unit._size+2)*blockSize;

		int off = (size-uSize)/2;
		
		g.setColor(FOREGROUND_COLOR);
		g.fillRect(off, off, uSize, uSize);
		
		for(MapPoint p : unit._blocks.keySet()) {
			Block b = unit._blocks.get(p);
			g.setColor(getBlockColor(b));
			g.fillRect(off + (p._x+1)*blockSize, off + (p._y+1)*blockSize, blockSize, blockSize);
		}
		
		g.setColor(getPlayerColor(unit._owner));
		g.fillRect(off, off, uSize, blockSize);
		g.fillRect(off, off + uSize-blockSize, uSize, blockSize);
		g.fillRect(off, off + blockSize, blockSize, uSize-2*blockSize);
		g.fillRect(off + uSize-blockSize, off + blockSize, blockSize, uSize-2*blockSize);
	}
	
	/**
	 * Gets an image for a given unit
	 *
	 * @param unit The unit in question
	 * @param blockSize The size of the blocks making up the unit
	 * @param size The size of the tile where the unit is located (and of the resulting image)
	 */
	public BufferedImage getUnitImage(Unit unit, int blockSize, int size) {
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		drawUnit(img.getGraphics(), unit, blockSize, size);
		return img;
	}
	
	/**
	 * Draws a piece
	 *
	 * @param g A Graphics object whose origin is the upper left corner of the piece
	 * @param piece The piece in question
	 * @param rotation The orientation of the piece
	 * @param blockSize The size of the blocks making up the piece
	 */
	public void drawPiece(Graphics g, Piece piece, int rotation, int blockSize) {
		int[] bounds = piece.getBounds(rotation);
		int xsize = bounds[2]-bounds[0]+1, ysize = bounds[3]-bounds[1]+1;
		int offx = -1*bounds[0], offy = -1*bounds[1];
		
		BufferedImage mask = new BufferedImage(blockSize, blockSize, BufferedImage.TYPE_INT_ARGB);
		drawBlockMask(mask.getGraphics(), blockSize);
		
		for(Entry<MapPoint, Block> entry : piece.getRotatedBlocks(rotation).entrySet()) {
			MapPoint mP = entry.getKey();
			Block b = entry.getValue();
			
			int x = (offx+mP._x)*blockSize, y = (offy+mP._y)*blockSize;
			
			g.setColor(getBlockBaseColor(b));
			g.fillRect(x, y, blockSize, blockSize);
			
			g.drawImage(mask, x, y, blockSize, blockSize, null);
		}
	}
	
	/**
	 * Gets an image for a given piece
	 *
	 * @param piece The piece in question
	 * @param rotation The orientation of the piece
	 * @param blockSize The size of the blocks making up the piece
	 */
	public BufferedImage getPieceImage(Piece piece, int rotation, int blockSize) {
		int[] bounds = piece.getBounds(rotation);
		int xsize = bounds[2]-bounds[0]+1, ysize = bounds[3]-bounds[1]+1;
		
		BufferedImage img = new BufferedImage(xsize, ysize, BufferedImage.TYPE_INT_ARGB);
		drawPiece(img.getGraphics(), piece, rotation, blockSize);
		return img;
	}
	
	/**
	 * Gets the Color corresponding to a given action type
	 *
	 * @param actionType The action type
	 */
	public Color getActionTypeColor(ActionType actionType) {
		if(actionType==ActionType.MOVE)
			return Color.CYAN;
		else if(actionType==ActionType.ATTACK)
			return Color.RED;
		else
			return Color.WHITE;
	}
	
	/**
	 * Draws the given action type
	 *
	 * @param g A Graphics object whose origin is the upper left corner of the tile on which to display the action
	 * @param actionType The action type
	 * @param unitLocation The location of the unit performing the action
	 * @param actionLocation The location where the action is being performed
	 * @param size The size of the tile on which to display the action (and of the Graphics object's clip rectangle)
	 */
	public void drawActionType(Graphics g, ActionType actionType, MapPoint unitLocation, MapPoint actionLocation, int size) {
		g.setColor(getActionTypeColor(actionType));
		int dx = actionLocation._x-unitLocation._x, dy = actionLocation._y-unitLocation._y;
		double angle;
		if(dx==0) {
			if(dy>=0)
				angle = 0.5*Math.PI;
			else
				angle = 1.5*Math.PI;
		} else {
			angle = Math.atan(((double)dy)/((double) dx));
			if(dx<0)
				angle += Math.PI;
		}
		double angle2 = angle+(Math.PI*2.0/3.0);
		double angle3 = angle-(Math.PI*2.0/3.0);
		
		double radius = size/2.0;
		int[] xs = {StaticMethods.round(Math.cos(angle)*radius+radius), StaticMethods.round(Math.cos(angle2)*radius+radius), StaticMethods.round(radius), StaticMethods.round(Math.cos(angle3)*radius+radius)};
		int[] ys = {StaticMethods.round(Math.sin(angle)*radius+radius), StaticMethods.round(Math.sin(angle2)*radius+radius), StaticMethods.round(radius), StaticMethods.round(Math.sin(angle3)*radius+radius)};
		
		g.fillPolygon(new Polygon(xs, ys, 4));
	}
	
	/**
	 * Gets an image for a given action type
	 *
	 * @param actionType The action type
	 * @param unitLocation The location of the unit performing the action
	 * @param actionLocation The location where the action is being performed
	 * @param size The size of the tile on which to display the action (and of the resulting image)
	 */
	public BufferedImage getActionTypeImage(ActionType actionType, MapPoint unitLocation, MapPoint actionLocation, int size) {
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		drawActionType(img.getGraphics(), actionType, unitLocation, actionLocation, size);
		return img;
	}
}