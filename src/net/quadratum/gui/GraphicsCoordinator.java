package net.quadratum.gui;

import net.quadratum.core.*;

import java.awt.Color;
import java.awt.image.BufferedImage;

public interface GraphicsCoordinator {
	public Color getBackgroundColor();
	public Color getNeutralColor();
	public Color getForegroundColor();
	
	public Color getPlayerColor(int id);
	
	public BufferedImage getTerrainTile(int terrainValue, int size);
	public Color getTerrainColor(int terrainValue);
	
	public BufferedImage getUnitImage(Unit u, int blockSize);
	
	public BufferedImage getPieceImage(Piece p, int blockSize);
	
	public BufferedImage getBlockImage(Block b, int size);
	public BufferedImage getBlockMask(int size);
	public Color getBlockColor(Block b);
	public Color getBlockBaseColor(Block b);
}