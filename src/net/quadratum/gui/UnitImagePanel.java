package net.quadratum.gui;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.event.*;

import net.quadratum.core.*;

//TODO: Only display piece when tabbed panel is on pieces tab

public class UnitImagePanel extends JPanel {
	private GUIPlayer _guiPlayer;
	
	private Unit _selected;
	
	private int _uSize, _scale, _ox, _oy;
	
	private boolean _showHover;
	private Piece _hover;
	private int _hx, _hy;
	
	public UnitImagePanel(GUIPlayer player) {
		_guiPlayer = player;
		
		MouseInputListener mIL = new UnitImagePanelMouseInputListener();
		addMouseListener(mIL);
		addMouseMotionListener(mIL);
		
		setBackground(DrawingMethods.BACKGROUND_COLOR);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Unit selected;
		int uSize = 0, scale = 0, uSSize = 0, ox = 0, oy = 0;
		Piece hover;
		boolean show;
		int hx, hy;
		synchronized(this) {
			selected = _selected;
			if(selected!=null) {
				uSize = _uSize = selected._size;
				scale = _scale = Math.min(getWidth()/(uSize+4), getHeight()/(uSize+4));
				uSSize = scale*uSize;
				ox = _ox = (getWidth()-uSSize)/2;
				oy = _oy = (getHeight()-uSSize)/2;
			}
			
			show = _showHover;
			hover = _hover;
			hx = _hx;
			hy = _hy;
		}
		
		if(selected!=null) {
			g.setColor(DrawingMethods.FOREGROUND_COLOR);
			g.fillRect(ox, oy, uSSize, uSSize);
			
			BufferedImage mask = _guiPlayer._drawingMethods.getBlockMaskImage(scale);
			
			for(Map.Entry<MapPoint, Block> entry : selected._blocks.entrySet()) {
				MapPoint p = entry.getKey();
				
				g.setColor(_guiPlayer._drawingMethods.getBlockColor(entry.getValue()));
				g.fillRect(ox + p._x*scale, oy + p._y*scale, scale, scale);
				
				g.drawImage(mask, ox+p._x*scale, oy + p._y*scale, scale, scale, this);
			}
			
			if(hover!=null && show) {
				for(Map.Entry<MapPoint, Block> entry : hover._blocks.entrySet()) {
					MapPoint p = new MapPoint(entry.getKey());
					p._x += hx;
					p._y += hy;
					if(p._x>=0 && p._x<uSize && p._y>=0 && p._y<uSize) {
						g.setColor(StaticMethods.applyAlpha(_guiPlayer._drawingMethods.getBlockColor(entry.getValue()), 127));
						g.fillRect(ox + p._x*scale, oy + p._y*scale, scale, scale);
					}
				}
			}
			
			g.setColor(_guiPlayer._drawingMethods.getPlayerColor(selected._owner));
			g.fillRect(ox-scale, oy-scale, uSSize+2*scale, scale);
			g.fillRect(ox-scale, oy+uSSize, uSSize+2*scale, scale);
			g.fillRect(ox-scale, oy, scale, uSSize);
			g.fillRect(ox+uSSize, oy, scale, uSSize);
		}
	}
	
	public void selectionUpdated() {
		synchronized(_guiPlayer._unitsData) {
			synchronized(this) {
				_selected = _guiPlayer._unitsData.getSelectedUnit();
			}
		}
		repaint();
	}
	
	public synchronized void pieceSelected(Piece p) {
		_hover = p;
		repaint();
	}
	
	private class UnitImagePanelMouseInputListener extends MouseInputAdapter {
		public void mouseMoved(MouseEvent e) {
			adjustHoverPosition(e);
		}
		
		public void mouseDragged(MouseEvent e) {
			adjustHoverPosition(e);
		}
		
		private void adjustHoverPosition(MouseEvent e) {
			synchronized(UnitImagePanel.this) {
				_showHover = true;
				if(_selected!=null && _hover!=null) {
					_hx = (e.getX()-_ox)/_scale;
					_hy = (e.getY()-_oy)/_scale;
					repaint();
				}
			}
		}
		
		public void mouseExited(MouseEvent e) {
			synchronized(UnitImagePanel.this) {
				_showHover = false;
			}
			repaint();
		}
		
		public void mouseClicked(MouseEvent e) {
			//TODO: place piece
		}
	}
}