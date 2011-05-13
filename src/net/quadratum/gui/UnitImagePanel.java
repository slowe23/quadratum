package net.quadratum.gui;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.event.*;

import net.quadratum.core.*;

/** Class for displaying the selected unit and placing pieces */
public class UnitImagePanel extends JPanel {
	private GUIPlayer _guiPlayer;
	
	private Unit _selected;
	
	private int _uSize, _scale, _ox, _oy;
	
	private boolean _showHover;
	private Piece _hover;
	private int _hIndex;
	private int _hRrrk;
	private MapPoint _hPos;
	
	public UnitImagePanel(GUIPlayer player) {
		_guiPlayer = player;
		
		_hPos = new MapPoint(0, 0);
		
		MouseInputListener mIL = new UnitImagePanelMouseInputListener();
		addMouseListener(mIL);
		addMouseMotionListener(mIL);
		
		setBackground(DrawingMethods.BACKGROUND_COLOR);
	}
	
	/** Displays an image of the selected unit, with an overlay of the currently selected piece if applicable */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Unit selected;
		int uSize = 0, scale = 0, uSSize = 0, ox = 0, oy = 0;
		Piece hover;
		int hRot;
		boolean show;
		MapPoint hPos;
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
			hRot = _hRrrk;
			hPos = _hPos;
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
			
			if(selected._owner==_guiPlayer.getID() && hover!=null && show) {
				int[] hB = hover.getBounds(hRot);
				for(Map.Entry<MapPoint, Block> entry : hover.getRotatedBlocks(hRot).entrySet()) {
					MapPoint p = new MapPoint(entry.getKey());
					p._x += hPos._x - hB[0];
					p._y += hPos._y - hB[1];
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
	
	/** Notifies this component that the selected unit may have changed */
	public void selectionUpdated() {
		synchronized(_guiPlayer._unitsData) {
			synchronized(this) {
				_selected = _guiPlayer._unitsData.getSelectedUnit();
			}
		}
		repaint();
	}
	
	/** Notifies this component that no piece is currently selected in the build tab */
	public synchronized void noPieceSelected() {
		_hover = null;
		repaint();
	}
	
	/** Notifies this component that the given piece has been selected in the building tab */
	public synchronized void pieceSelected(int index, Piece selPiece, int rotation) {
		_hover = selPiece;
		_hRrrk = rotation;
		_hIndex = index;
		repaint();
	}
	
	private class UnitImagePanelMouseInputListener extends MouseInputAdapter {
		/** Updates the location of the piece preview */
		public void mouseMoved(MouseEvent e) {
			adjustHoverPosition(e);
		}
		
		/** Updates the location of the piece preview */
		public void mouseDragged(MouseEvent e) {
			adjustHoverPosition(e);
		}
		
		/** Sets the position of the piece placement preview based on the mouse location */
		private void adjustHoverPosition(MouseEvent e) {
			synchronized(UnitImagePanel.this) {
				_showHover = true;
				if(_selected!=null && _hover!=null) {
					_hPos._x = StaticMethods.floor((e.getX()-_ox)/((double)_scale));
					_hPos._y = StaticMethods.floor((e.getY()-_oy)/((double)_scale));
				}
				repaint();
			}
		}
		
		/** Responds to a mouse exit event by hiding the piece placement preview */
		public void mouseExited(MouseEvent e) {
			synchronized(UnitImagePanel.this) {
				_showHover = false;
			}
			repaint();
		}
		
		/** Responds to a mouse click by attempting to place a piece, if possible */
		public void mouseClicked(MouseEvent e) {
			synchronized(UnitImagePanel.this) {
				if(_showHover && _hover!=null && _selected._owner==_guiPlayer.getID()) {
					int[] hB = _hover.getBounds(_hRrrk);
					_guiPlayer.placePiece(_selected, _hIndex, _hRrrk, new MapPoint(_hPos._x-hB[0], _hPos._y-hB[1]));
				}
			}
		}
	}
}