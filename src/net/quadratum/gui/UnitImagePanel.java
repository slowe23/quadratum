package net.quadratum.gui;

import net.quadratum.core.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class UnitImagePanel extends JPanel implements MouseListener, MouseMotionListener {
	private UnitHandler _unitHandler;
	private GraphicsCoordinator _graphicsCoordinator;
	
	public UnitImagePanel(GUIPlayer player) {
		_unitHandler = player.getUnitHandler();
		_graphicsCoordinator = player.getGraphicsCoordinator();
		
		setBackground(_graphicsCoordinator.getBackgroundColor());
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Unit u = _unitHandler.getSelectedUnit();
		if(u!=null) {
			int uSize = u._size;
			int scale = Math.min(getWidth()/uSize, getHeight()/uSize);
			int uSSize = scale*uSize;
			int ox = (getWidth()-uSSize)/2, oy = (getHeight()-uSSize)/2;

			g.setColor(_graphicsCoordinator.getForegroundColor());
			g.fillRect(ox, oy, uSSize, uSSize);
			
			BufferedImage mask = _graphicsCoordinator.getBlockMask(scale);
			for(MapPoint p : u._blocks.keySet()) {
				Block b = u._blocks.get(p);
				
				g.setColor(_graphicsCoordinator.getBlockColor(b));
				g.fillRect(ox + p._x*scale, oy + p._y*scale, scale, scale);
				
				g.drawImage(mask, ox+p._x*scale, oy + p._y*scale, scale, scale, this);
			}
			
			g.setColor(_graphicsCoordinator.getNeutralColor());
			for(int i = 1; i<uSize; i++) {
				int sI = scale*i;
				g.drawLine(ox+sI, oy, ox+sI, oy+uSSize);
				g.drawLine(ox, oy+sI, ox+uSSize, oy+sI);
			}
		}
	}
	
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	public void mouseClicked(MouseEvent e) { }
	public void mousePressed(MouseEvent e) { }
	public void mouseDragged(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }
	public void mouseMoved(MouseEvent e) { }
}