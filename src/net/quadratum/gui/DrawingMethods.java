package net.quadratum.gui;

import net.quadratum.core.*;
import net.quadratum.core.Action.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DrawingMethods {
	private static final Color[] PLAYER_COLORS = {Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.ORANGE, Color.PINK};
	public static final Color BACKGROUND_COLOR = Color.BLACK, NEUTRAL_COLOR = Color.GRAY, FOREGROUND_COLOR = Color.WHITE;
	
	public DrawingMethods() { }
	
	public Color getPlayerColor(int id) {
		if(id>=0 && id<PLAYER_COLORS.length)
			return PLAYER_COLORS[id];
		else
			return NEUTRAL_COLOR;
	}
	
	public Color getTerrainColor(int terrainValue) {
		Color col;
		if(TerrainConstants.isOfType(terrainValue, TerrainConstants.WATER))
			col = new Color(0, 0, 255);
		else
			col = new Color(0, 127, 63);
		
		return col;
	}
	
	public void drawTerrainTile(Graphics g, int terrainValue, int size) {
		g.setColor(getTerrainColor(terrainValue));
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
	
	public Color getTerrainMaskColor(boolean placement) {
		if(placement)
			return new Color(255, 255, 255, 127);
		else
			return new Color(0, 0, 0, 0);
	}
	
	public void drawTerrainTileMask(Graphics g, boolean placement, int size) {
		g.setColor(getTerrainMaskColor(placement));
		g.fillRect(0, 0, size, size);
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
	
	public void drawBlockMask(Graphics g, int size) {
		int bevelSize = Math.min(5, size/10);
		
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
	
	public Color getActionTypeColor(ActionType actionType) {
		if(actionType==ActionType.MOVE)
			return Color.blue;
		else if(actionType==ActionType.ATTACK)
			return Color.red;
		else if(actionType==ActionType.GATHER_RESOURCES)
			return Color.yellow;
		else
			return Color.white;
	}
}