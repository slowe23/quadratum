package net.quadratum.gui;

import java.util.List;

import javax.swing.JPanel;
import java.awt.*;

import net.quadratum.core.Piece;

public class PieceListPanel extends JPanel {
	GUIPlayer _guiPlayer;
	
	private Piece[] _pieces;
	private int[] _rotates;
	private int _maxD;  //The maximum piece dimension in the list
	
	private int _index;  //The index of the currently-displayed piece
	
	public PieceListPanel(GUIPlayer player) {
		_guiPlayer = player;
		
		setBackground(DrawingMethods.BACKGROUND_COLOR);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Piece piece = null;
		int rotate = 0;
		int maxD;
		synchronized(this) {
			if(_pieces!=null && _pieces.length>0) {
				piece = _pieces[_index];
				rotate = _rotates[_index];
			}
			maxD = _maxD;
		}
		
		if(piece!=null) {
			int[] bounds = piece.getBounds(rotate);
			
			int blockSize = Math.min(getWidth()/(maxD+2), getHeight()/(maxD+2));  //Scale depends on whole list, not just current piece
			int pieceWidth = blockSize*(bounds[2]-bounds[0]+1), pieceHeight = blockSize*(bounds[3]-bounds[1]+1);
			int offx = (getWidth()-pieceWidth)/2, offy = (getHeight()-pieceHeight)/2;
			
			_guiPlayer._drawingMethods.drawPiece(g.create(offx, offy, pieceWidth, pieceHeight), piece, rotate, blockSize);
		}
	}
	
	public synchronized void setPieces(List<Piece> pieces) {
		if(pieces==null) {
			_pieces = null;
			_rotates = null;
			_index = 0;
			
			_maxD = 0;
		} else {
			_pieces = new Piece[pieces.size()];
			_rotates = new int[_pieces.length];
			_index = 0;
			
			_maxD = 1;
			
			int w, h;
			int[] bounds;
			
			int index = 0;
			for(Piece p : pieces) {
				bounds = (_pieces[index++] = p).getBounds(0);
				w = bounds[2]-bounds[0]+1;
				h = bounds[3]-bounds[1]+1;
				
				if(w>_maxD)
					_maxD = w;
				if(h>_maxD)
					_maxD = h;
			}
		}
	}
	
	public synchronized Integer getCurrentPieceID() {
		if(_pieces==null || _pieces.length==0)
			return null;
		
		return _index;
	}
	
	public synchronized Piece getCurrentPiece() {
		if(_pieces==null || _pieces.length==0)
			return null;
		
		return _pieces[_index];
	}
	
	public synchronized int getCurrentRotation() {
		if(_rotates==null || _rotates.length==0)
			return -1;
		
		return _rotates[_index];
	}
	
	/** Rotates the current piece by the given amount */
	public synchronized void rotatePiece(int s) {
		if(_rotates!=null && _rotates.length>0) {
			int r = _rotates[_index] + s;
			_rotates[_index] = mod(r, 4);
		}
	}
	
	/** Sets the rotation of the current piece to the given value */
	public synchronized void setPieceRotation(int r) {
		if(_rotates!=null && _rotates.length>0)
			_rotates[_index] = mod(r, 4);
	}
	
	public synchronized void scrollUp() {
		scroll(_index-1);
	}
	
	public synchronized void scrollDown() {
		scroll(_index+1);
	}
	
	public synchronized void scrollTo(int index) {
		scroll(index);
	}
	
	public synchronized void scrollTo(double amount) {
		if(_pieces!=null && _pieces.length>0)
			scroll(StaticMethods.round(amount*(_pieces.length)));
	}
	
	private synchronized void scroll(int index) {
		if(_pieces!=null && _pieces.length>0)
			_index = mod(index, _pieces.length);
	}
	
	private static int mod(int x, int modulus) {
		int r = x % modulus;
		if(r<0)
			r += modulus;
		return r;
	}
}