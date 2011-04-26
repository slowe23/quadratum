package net.quadratum.gui;

import net.quadratum.core.*;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class MapPanel extends JPanel implements MapView, MouseListener, MouseMotionListener, MouseWheelListener, ComponentListener {
	
	private static final int DEFAULT_SCALE = 3;  //Default scale level
	private static final int MIN_SCALE = 2, MAX_SCALE = 8;
	
	private UnitHandler _unitHandler;
	private GraphicsCoordinator _graphicsCoordinator;
	private MinimapPanel _minimap;
	
	private double _viewX, _viewY;  //Location in map squares currently displayed by the view
	private int _scaleLevel;  //Scale level

	private int[][] _terrain;
	private Set<MapPoint> _placement;
	
	private Point _press;  //The location where the mouse was pressed (null when the mouse is not currently pressed)
	private double _pvx, _pvy;  //The location of the view when the mouse was pressed
	
	public MapPanel(GUIPlayer player) {
		_unitHandler = player._unitHandler;
		_graphicsCoordinator = player._graphicsCoordinator;
		
		_scaleLevel = DEFAULT_SCALE;
		
		_minimap = new MinimapPanel(player, this);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addComponentListener(this);
		
		setBackground(_graphicsCoordinator.getBackgroundColor());
	}
	
	public MinimapPanel getMinimapPanel() {
		return _minimap;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(_terrain!=null) {
			int scale = getScale();
			double vX = getViewX(), vY = getViewY(), vW = getViewWidth(), vH = getViewHeight();
			int offx = CM.round(-1*vX*scale), offy = CM.round(-1*vY*scale);  //The pixel location of the top left map corner
			
			//Draw map border
			g.setColor(_graphicsCoordinator.getNeutralColor());
			g.drawRect(offx-1, offy-1, scale*_terrain.length+2-1, scale*_terrain[0].length+2-1);
			
			//Draw visible map tiles
			for(int i = Math.max(0, (int)vX); i<_terrain.length && i<vX+vW; i++) {
				for(int j = Math.max(0, (int)vY); j<_terrain[i].length && j<vY+vH; j++) {
					BufferedImage tile = _graphicsCoordinator.getTerrainTile(_terrain[i][j], scale);
					g.drawImage(tile, offx+scale*i, offy+scale*j, scale, scale, this);

					if(_placement!=null && _placement.contains(new MapPoint(i, j))) {
						g.setColor(new Color(255, 0, 0, 127));
						g.fillRect(offx+scale*i, offy+scale*j, scale, scale);
					}
				}
			}
			
			//Draw units
			Map<MapPoint, Unit> units = _unitHandler.getUnits();
			if(units!=null) {
				Unit selected = _unitHandler.getSelectedUnit();
				
				for(MapPoint p : units.keySet()) {
					if(p._x>=(int)vX && p._x <vX+vW && p._y>=(int)vY && p._y<vX+vW) {
						Unit unit = units.get(p);
						BufferedImage unitImage = _graphicsCoordinator.getUnitImage(unit, _scaleLevel);
						int dx = (scale-unitImage.getWidth())/2, dy = (scale-unitImage.getHeight())/2;
						g.drawImage(unitImage, offx + scale*p._x + dx, offy + scale*p._y + dy, this);
						
						if(unit==selected) {
							g.setColor(_graphicsCoordinator.getForegroundColor());
							g.drawRect(offx+scale*p._x, offy+scale*p._y, scale-1, scale-1);
						}
					}
				}
			}
			
		} else {
			//Draw waiting screen
			g.setColor(_graphicsCoordinator.getForegroundColor());
			FontMetrics fM = g.getFontMetrics();
			String waiting = "Waiting for game to start...";
			g.drawString(waiting, (getWidth()-fM.stringWidth(waiting))/2, (getHeight()-(fM.getAscent()+fM.getDescent()))/2+fM.getAscent());
		}
	}
	
	public void start(MapData m) {
		setTerrain(m._terrain);
		_placement = m._placementArea;
	}
	
	public void setTerrain(int[][] t) {
		_terrain = new int[t.length][t[0].length];
		for(int i = 0; i<_terrain.length; i++)
			for(int j = 0; j<_terrain[i].length; j++)
				_terrain[i][j] = t[i][j];
		
		_minimap.setTerrain(t);
	}
	
	private int getScale() {
		return 12*_scaleLevel;
	}
	
	public double getViewX() {
		return _viewX;
	}
	public double getViewY() {
		return _viewY;
	}
	public double getViewWidth() {
		return getWidth()/((double)(getScale()));
	}
	public double getViewHeight() {
		return getHeight()/((double)(getScale()));
	}
	
	public void setScaleLevel(int scaleLevel) {
		_scaleLevel = Math.max(MIN_SCALE, Math.min(scaleLevel, MAX_SCALE));
	}
	
	public void setViewPos(double x, double y) {
		_viewX = x;
		_viewY = y;
		
		fixViewPos();
	}
	
	private void fixViewPos() {
		if(_terrain!=null) {
			double vW = getViewWidth(), vH = getViewHeight();
			int tW = _terrain.length, tH = _terrain[0].length;
			
			if(vW<tW) {
				if(_viewX<-1)
					_viewX = -1;
				else if(_viewX+vW>tW+1)
					_viewX = tW+1-vW;
			} else
				_viewX = (tW-vW)/2.0;
			
			if(vH<tH) {
				if(_viewY<-1)
					_viewY = -1;
				else if(_viewY+vH>tH+1)
					_viewY = tH+1-vH;
			} else
				_viewY = (tH-vH)/2.0;
		}
	}
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(_terrain!=null) {
			int oldLevel = _scaleLevel;
			double centerX = _viewX + getViewWidth()/2.0, centerY = _viewY + getViewHeight()/2.0;
			
			setScaleLevel(_scaleLevel - e.getWheelRotation());			
			if(_scaleLevel!=oldLevel) {
				setViewPos(centerX - getViewWidth()/2.0, centerY - getViewHeight()/2.0);
				repaint();
				_minimap.repaint();
			}
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		//TODO: select the piece clicked on, if one exists
	}
	
	public void mouseMoved(MouseEvent e) {
		//TODO: implement scroll when mouse is near edge of map
	}
	
	public void mousePressed(MouseEvent e) {
		if(_terrain!=null) {
			_press = new Point(e.getX(), e.getY());
			_pvx = getViewX();
			_pvy = getViewY();
		}
	}
	
	public void mouseDragged(MouseEvent e) {
		if(_press!=null) {
			setViewPos(_pvx - (e.getX()-_press.x)/((double)(getScale())), _pvy - (e.getY()-_press.y)/((double)(getScale())));
			repaint();
			_minimap.repaint();
			
			_press = new Point(e.getX(), e.getY());
			_pvx = getViewX();
			_pvy = getViewY();
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		_press = null;
	}
	
	public void componentHidden(ComponentEvent e) { }
	public void componentShown(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }

	public void componentResized(ComponentEvent e) {
		if(_terrain!=null)
			fixViewPos();
		repaint();
		_minimap.repaint();
	}
}