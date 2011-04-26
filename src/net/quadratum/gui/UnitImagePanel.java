package net.quadratum.gui;

import net.quadratum.core.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UnitImagePanel extends JPanel implements MouseListener, MouseMotionListener {
	private UnitHolder _unit;
	
	public UnitImagePanel(UnitHolder u) {
		_unit = u;
		
		setBackground(Color.gray);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Unit u = _unit.getSelectedUnit();
		if(u!=null) {
			int uSize = u._size;
			int scale = Math.min(getWidth()/uSize, getHeight()/uSize);
			int uSSize = scale*uSize;
			int ox = (getWidth()-uSSize)/2, oy = (getHeight()-uSSize)/2;

			g.setColor(Color.white);
			g.fillRect(ox, oy, uSSize, uSSize);
			
			//Draw contents of piece
			
			g.setColor(Color.gray);
			for(int i = 1; i<uSize; i++) {
				int sI = scale*i;
				g.drawLine(ox+sI, oy, ox+sI, oy+uSSize);
				g.drawLine(ox, oy+sI, ox+uSSize, oy+sI);
			}
		}
	}
	
	private Color getBlockColor(Block b) {
		Color c = getBaseColor(b);
		double healthAmount = b._health/((double)(b._totalHealth));
		return new Color((int)(c.getRed()*healthAmount), (int)(c.getBlue()*healthAmount), (int)(c.getGreen()*healthAmount));
	}
	
	private Color getBaseColor(Block b) {
		return Color.black;
	}
	
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	public void mouseClicked(MouseEvent e) { }
	public void mousePressed(MouseEvent e) { }
	public void mouseDragged(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }
	public void mouseMoved(MouseEvent e) { }
}