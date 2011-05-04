package net.quadratum.gui;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.util.*;

import net.quadratum.core.*;
import net.quadratum.core.Action.ActionType;

public class DrawingMethods {
	private static final Color[] PLAYER_COLORS = {Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.ORANGE, Color.PINK};
	public static final Color BACKGROUND_COLOR = Color.BLACK, NEUTRAL_COLOR = Color.GRAY, FOREGROUND_COLOR = Color.WHITE;
	
	public DrawingMethods() { }
	
	public Color getPlayerColor(int id) {
		if(id>=0 && id<PLAYER_COLORS.length)
			return PLAYER_COLORS[id];
		else
			return Color.WHITE;
	}
	
	public Color getTerrainTileColor(int terrainValue) {
		Color col;
		if(TerrainConstants.isOfType(terrainValue, TerrainConstants.WATER))
			col = new Color(0, 0, 191);
		else
			col = new Color(0, 127, 63);
		
		return col;
	}
	
	public void drawTerrainTile(Graphics g, int terrainValue, int size) {
		g.setColor(getTerrainTileColor(terrainValue));
		g.fillRect(0, 0, size, size);
		
		if(TerrainConstants.isOfType(terrainValue, TerrainConstants.MOUNTAIN)) {
			g.setColor(new Color(126, 72, 19));
			g.fillPolygon(new Polygon(new int[]{0, size/2, size}, new int[]{size, 0, size}, 3));
		}
		
		if(TerrainConstants.isOfType(terrainValue, TerrainConstants.BUNKER)) {
			g.setColor(Color.DARK_GRAY);
			g.fillPolygon(new Polygon(new int[]{0, size/4, (3*size)/4, size}, new int[]{(3*size)/4, size/4, size/4, (3*size)/4}, 4));
		}
		
		if(TerrainConstants.isOfType(terrainValue, TerrainConstants.RESOURCES)) {
			g.setColor(new Color(191,191, 0));
			g.fillPolygon(new Polygon(new int[] {0, size/2, size, (3*size)/4, size, size/2, 0, size/4}, new int[] {0, size/4, 0, size/2, size, (3*size)/4, size, size/2}, 8));
		}
	}
	
	public BufferedImage getTerrainTileImage(int terrainValue, int size) {
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		drawTerrainTile(img.getGraphics(), terrainValue, size);
		return img;
	}
	
	public Color getPlacementMaskColor(boolean placement) {
		if(placement)
			return new Color(255, 255, 255, 127);
		else
			return new Color(0, 0, 0, 63);
	}
	
	public void drawPlacementMask(Graphics g, Set<MapPoint> placementArea, MapPoint location, int size) {
		if(placementArea.contains(location)) {
			g.setColor(getPlacementMaskColor(true));
			
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
			g.setColor(getPlacementMaskColor(false));
			g.fillRect(0,0,size,size);
		}
	}
	
	public BufferedImage getPlacementMaskImage(Set<MapPoint> placementArea, MapPoint location, int size) {
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		drawPlacementMask(img.getGraphics(), placementArea, location, size);
		return img;
	}
	
	public Color getBlockBaseColor(Block b) {
		return new Color(127, 127, 127);  //TODO: change this
	}
	
	public Color getBlockColor(Block b) {
		Color c = getBlockBaseColor(b);
		double healthAmount = b._health/((double)(b._totalHealth));
		return new Color((int)(c.getRed()*healthAmount), (int)(c.getBlue()*healthAmount), (int)(c.getGreen()*healthAmount));
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
		else if(actionType==ActionType.GATHER_RESOURCES)
			return Color.YELLOW;
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