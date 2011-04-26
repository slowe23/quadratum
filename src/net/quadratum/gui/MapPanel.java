package net.quadratum.gui;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class MapPanel extends JPanel implements MapView, MouseListener, MouseMotionListener, ComponentListener {
	private static final Color[] T_COLS = {new Color(0, 127, 0), new Color(100, 75, 20), new Color(0,0,127)};  //Temporary
	
	private static final int DEFAULT_SCALE = 50;
	
	private UnitHolder _units;
	private MinimapPanel _minimap;
	
	private double _viewX, _viewY;  //Location in map squares currently displayed by the view
	private int _scale;  //Pixels per map square  //TODO: zoom

	private int[][] _terrain;
	
	private Point _press;  //The location where the mouse was pressed (null when the mouse is not currently pressed)
	private double _pvx, _pvy;  //The location of the view when the mouse was pressed
	
	public MapPanel(UnitHolder u) {
		
		_units = u;
		
		_scale = DEFAULT_SCALE;
		
		_minimap = new MinimapPanel(this, _units);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addComponentListener(this);
		
		setBackground(Color.BLACK);
	}
	
	public MinimapPanel getMinimap() {
		return _minimap;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(_terrain!=null) {
			double vX = getViewX(), vY = getViewY(), vW = getViewWidth(), vH = getViewHeight();
			
			int offx = CM.round(-1*vX*_scale), offy = CM.round(-1*vY*_scale);  //The pixel location of the top left map corner
			
			for(int i = (int)vX; i<_terrain.length && i<vX+vW; i++) {
				for(int j = (int)vY; j<_terrain[i].length && j<vY+vH; j++) {
					BufferedImage tile = getTerrainTile(_terrain[i][j]);
					g.drawImage(tile, offx+_scale*i, offy+_scale*j, _scale, _scale, this);
				}
			}
			
			//TODO: draw units
		} else {
			//Draw waiting screen
		}
	}
	
	private BufferedImage getTerrainTile(int id) {
		Color c = Color.black;
		if(id>=0 && id<T_COLS.length)
			c = T_COLS[id];
		
		BufferedImage toRet = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		toRet.setRGB(0,0,c.getRGB());
		return toRet;
	}
	
	public void setTerrain(int[][] t) {
		_terrain = new int[t.length][t[0].length];
		for(int i = 0; i<_terrain.length; i++)
			for(int j = 0; j<_terrain[i].length; j++)
				_terrain[i][j] = t[i][j];
		
		_minimap.setTerrain(t);
	}
	
	public double getViewX() {
		return _viewX;
	}
	public double getViewY() {
		return _viewY;
	}
	public double getViewWidth() {
		return getWidth()/((double)_scale);
	}
	public double getViewHeight() {
		return getHeight()/((double)_scale);
	}
	
	public void setViewPos(double x, double y) {
		_viewX = x;
		_viewY = y;
		
		fixViewPos();
		
		repaint();
	}
	
	private void fixViewPos() {
		if(_terrain!=null) {
			double vW = getViewWidth(), vH = getViewHeight();
			int tW = _terrain.length, tH = _terrain[0].length;
			
			if(_viewX<0 && _viewX+vW<tW)
				_viewX = Math.min(tW-vW, 0.0);
			else if(_viewX+vW>tW && _viewX>0)
				_viewX = Math.max(tW-vW, 0.0);
			
			if(_viewY<0 && _viewY+vH<tH)
				_viewY = Math.min(tH-vH, 0.0);
			else if(_viewY+vH>tH && _viewY>0)
				_viewY = Math.max(tH-vH, 0.0);
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		
	}
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	
	public void mouseClicked(MouseEvent e) {
		//TODO
	}
	
	public void mousePressed(MouseEvent e) {
		_press = new Point(e.getX(), e.getY());
		_pvx = getViewX();
		_pvy = getViewY();
	}
	
	public void mouseDragged(MouseEvent e) {
		if(_press!=null) {
			setViewPos(_pvx - (e.getX()-_press.x)/((double)_scale), _pvy - (e.getY()-_press.y)/((double)_scale));
			_minimap.repaint();
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		_press = null;
	}
	
	public void componentHidden(ComponentEvent e) { }
	public void componentShown(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }

	public void componentResized(ComponentEvent e) {
		fixViewPos();
		repaint();
		_minimap.repaint();
	}
}