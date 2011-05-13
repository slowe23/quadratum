package net.quadratum.gui;

import java.util.List;

import javax.swing.JPanel;
import java.awt.*;

import net.quadratum.core.Piece;

/** A panel for displaying a certain piece out of a list of pieces */
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
	
	/** Displays the piece */
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
	
	/** Sets the piece list */
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
	
	/** Gets the ID of the currently displayed piece (null if nothing is selected) */
	public synchronized Integer getCurrentPieceID() {
		if(_pieces==null || _pieces.length==0)
			return null;
		
		return _index;
	}
	
	/** Gets the currently displayed piece (or null if nothing) */
	public synchronized Piece getCurrentPiece() {
		if(_pieces==null || _pieces.length==0)
			return null;
		
		return _pieces[_index];
	}
	
	/** Gets the orientation of the currently displayed piece (null if nothing) */
	public synchronized Integer getCurrentRotation() {
		if(_rotates==null || _rotates.length==0)
			return null;
		
		return _rotates[_index];
	}
	
	/** Rotates the current piece by the given amount (relative to its current orientation) */
	public synchronized void rotatePiece(int s) {
		if(_rotates!=null && _rotates.length>0) {
			int r = _rotates[_index] + s;
			_rotates[_index] = mod(r, 4);
		}
	}
	
	/** Sets the absolute rotation of the current piece*/
	public synchronized void setPieceRotation(int r) {
		if(_rotates!=null && _rotates.length>0)
			_rotates[_index] = mod(r, 4);
	}
	
	/** Displays the previous unit in the list from the one which is currently selected.  Loops around. */
	public synchronized void scrollUp() {
		scroll(_index-1);
	}
	
	/** Displays the next unit in the list from the one which is currently selected.  Loops around. */
	public synchronized void scrollDown() {
		scroll(_index+1);
	}
	
	/** Displays the given unit in the list */
	public synchronized void scrollTo(int index) {
		scroll(index);
	}
	
	/** Scrolls to the piece corresponding to the given double value between 0 (first piece) and 1 (last piece) along the list*/
	public synchronized void scrollTo(double amount) {
		if(_pieces!=null && _pieces.length>0)
			scroll(StaticMethods.round(amount*(_pieces.length)));
	}
	
	/** Scrolls to the given index in the list */
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