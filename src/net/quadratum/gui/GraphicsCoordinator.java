package net.quadratum.gui;

import net.quadratum.core.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.util.Map;
import java.util.HashMap;

public class GraphicsCoordinator {
	private static final Color[] PLAYER_COLORS = {Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.ORANGE, Color.PINK};
	private static Map<Integer, BufferedImage[]> _mapTileCache = new HashMap<Integer, BufferedImage[]>();

	public GraphicsCoordinator() { }
	
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
		if(!(_mapTileCache.containsKey(size)))
			_mapTileCache.put(size, new BufferedImage[16]);
		
		BufferedImage[] cache = _mapTileCache.get(size);
		
		if(cache[terrainValue]==null || cache[terrainValue].getWidth()!=size) {
			BufferedImage toRet = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
			
			Graphics g = toRet.getGraphics();
			
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
			
			cache[terrainValue] = toRet;
		}
		
		return cache[terrainValue];
	}
	
	public Color getTerrainColor(int terrainValue) {
		Color col;
		if(TerrainConstants.isOfType(terrainValue, TerrainConstants.WATER))
			col = new Color(0, 0, 255);
		else
			col = new Color(0, 127, 63);
		
		return col;
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