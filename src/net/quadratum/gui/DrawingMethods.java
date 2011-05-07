package net.quadratum.gui;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.util.*;

import net.quadratum.core.*;
import net.quadratum.core.Action.ActionType;

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
	
	public Color getPlayerColor(int id) {
		if(id>=0 && id<PLAYER_COLORS.length)
			return PLAYER_COLORS[id];
		else
			return Color.WHITE;
	}
	
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
	
	public void drawTerrainTile(Graphics g, int terrainValue, int size, boolean showGrid) {
		g.setColor(LAND_COLOR);
		g.fillRect(0, 0, size, size);
		
		drawTerrainFeatures(g, terrainValue, size);
		
		if(showGrid) {
			g.setColor(StaticMethods.applyAlpha(NEUTRAL_COLOR, 191));
			g.drawRect(0, 0, size-1, size-1);
		}
	}
	
	public BufferedImage getTerrainTileImage(int terrainValue, int size, boolean showGrid) {
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		drawTerrainTile(img.getGraphics(), terrainValue, size, showGrid);
		return img;
	}
	
	public Color getPlacementMaskColor(int player, boolean placement) {
		if(placement) {
			Color pCol = getPlayerColor(player);
			return new Color((255+pCol.getRed())/2, (255+pCol.getGreen())/2, (255+pCol.getBlue())/2, 127);
		} else
			return new Color(0, 0, 0, 63);
	}
	
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
	
	public BufferedImage getPlacementMaskImage(int player, Set<MapPoint> placementArea, MapPoint location, int size) {
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		drawPlacementMask(img.getGraphics(), player, placementArea, location, size);
		return img;
	}
	
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
	
	public Color getBlockColor(Block b) {
		Color c = getBlockBaseColor(b);
		
		double healthAmount = b._health/((double)(b._totalHealth));
		if(healthAmount<0)
			healthAmount = 0;
		if(healthAmount>1)
			healthAmount = 1;
		
		return new Color((int)(c.getRed()*healthAmount), (int)(c.getGreen()*healthAmount), (int)(c.getBlue()*healthAmount));
	}
	
	public void drawBlock(Graphics g, Block b, int size) {
		g.setColor(getBlockColor(b));
		g.fillRect(0, 0, size, size);
		
		drawBlockMask(g, size);
	}
	
	public BufferedImage getBlockImage(Block b, int size) {
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		drawBlock(img.getGraphics(), b, size);
		return img;
	}
	
	public void drawBlockMask(Graphics g, int size) {
		int bevelSize = Math.min(5, size/6);
		
		if(bevelSize>0) {
			//Draw sides of bevel
			g.setColor(new Color(255, 255, 255, 63));
			for(int i = 0; i<bevelSize; i++) {
				g.drawLine(0, i, size-1-i, i);
				g.drawLine(i, bevelSize, i, size-1-i);
			}
			
			g.setColor(new Color(0, 0, 0, 63));
			for(int i = 0; i<bevelSize; i++) {
				g.drawLine(i+1, size-1-i, size-1, size-1-i);
				g.drawLine(size-1-i, i+1, size-1-i, size-1-bevelSize);
			}
		}
	}
	
	public BufferedImage getBlockMaskImage(int size) {
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		drawBlockMask(img.getGraphics(), size);
		return img;
	}
	
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
	
	public BufferedImage getUnitImage(Unit unit, int blockSize, int size) {
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		drawUnit(img.getGraphics(), unit, blockSize, size);
		return img;
	}
	
	public void drawPiece(Graphics g, Piece piece, int blockSize) {
		int[] bounds = piece.getBounds();
		int xsize = bounds[2]-bounds[0]+1, ysize = bounds[3]-bounds[1]+1;
		int offx = -1*bounds[0], offy = -1*bounds[1];
		
		BufferedImage mask = new BufferedImage(blockSize, blockSize, BufferedImage.TYPE_INT_ARGB);
		drawBlockMask(mask.getGraphics(), blockSize);
		
		for(MapPoint mP : piece._blocks.keySet()) {
			Block b = piece._blocks.get(mP);
			int x = (offx+mP._x)*blockSize, y = (offy+mP._y)*blockSize;
			
			g.setColor(getBlockBaseColor(b));
			g.fillRect(x, y, blockSize, blockSize);
			
			g.drawImage(mask, x, y, blockSize, blockSize, null);
		}
	}
	
	public BufferedImage getPieceImage(Piece piece, int blockSize) {
		int[] bounds = piece.getBounds();
		int xsize = bounds[2]-bounds[0]+1, ysize = bounds[3]-bounds[1]+1;
		
		BufferedImage img = new BufferedImage(xsize, ysize, BufferedImage.TYPE_INT_ARGB);
		drawPiece(img.getGraphics(), piece, blockSize);
		return img;
	}
	
	public Color getActionTypeColor(ActionType actionType) {
		if(actionType==ActionType.MOVE)
			return Color.CYAN;
		else if(actionType==ActionType.ATTACK)
			return Color.RED;
		else
			return Color.WHITE;
	}
	
	public void drawActionType(Graphics g, ActionType actionType, MapPoint unitLocation, MapPoint actionLocation, int size) {
		g.setColor(getActionTypeColor(actionType));
		int dx = actionLocation._x-unitLocation._x, dy = actionLocation._y-unitLocation._y;
		double angle;
		if(dx==0) {
			if(dy>0)
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
	
	public BufferedImage getActionTypeImage(ActionType actionType, MapPoint unitLocation, MapPoint actionLocation, int size) {
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		drawActionType(img.getGraphics(), actionType, unitLocation, actionLocation, size);
		return img;
	}
}