package net.quadratum.gui;

import java.util.List;

import javax.swing.JPanel;
import java.awt.*;

import net.quadratum.core.Piece;

public class PieceListPanel extends JPanel {
	private DrawingMethods _drawingMethods;
	
	private Piece[] _pieces;
	private int _maxW, _maxH;  //The maximum piece dimensions in the list
	
	private int _index;  //The index of the currently-displayed piece
	
	public PieceListPanel(DrawingMethods drawingMethods) {
		_drawingMethods = drawingMethods;
		
		setBackground(DrawingMethods.BACKGROUND_COLOR);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(_pieces!=null && _pieces.length>0) {
			Piece piece = _pieces[_index];
			int[] bounds = piece.getBounds();
			
			int blockSize = Math.min(getWidth()/(_maxW+2), getHeight()/(_maxH+2));  //Scale depends on whole list, not just current piece
			int pieceWidth = blockSize*(bounds[2]-bounds[0]), pieceHeight = blockSize*(bounds[3]-bounds[1]);
			int offx = (getWidth()-pieceWidth)/2, offy = (getHeight()-pieceHeight)/2;
			
			_drawingMethods.drawPiece(g.create(offx, offy, pieceWidth, pieceHeight), piece, blockSize);
		}
	}
	
	public void setPieces(List<Piece> pieces) {
		if(pieces==null) {
			_pieces = null;
			_index = 0;
			
			_maxW = 0;
			_maxH = 0;
		} else {
			_pieces = new Piece[pieces.size()];
			_index = 0;
			
			_maxW = 1;
			_maxH = 1;
			
			int w, h;
			int[] bounds;
			
			int index = 0;
			for(Piece p : pieces) {
				bounds = (_pieces[index++] = p).getBounds();
				w = bounds[2]-bounds[0];
				h = bounds[3]-bounds[1];
				
				if(w>_maxW)
					_maxW = w;
				if(h>_maxH)
					_maxH = h;
			}
		}
	}
	
	public Piece getCurrentPiece() {
		if(_pieces==null || _pieces.length==0)
			return null;
		
		return _pieces[_index];
	}
	
	public void scrollUp() {
		scroll(_index-1);
	}
	
	public void scrollDown() {
		scroll(_index+1);
	}
	
	public void scrollTo(int index) {
		scroll(index);
	}
	
	public void scrollTo(double amount) {
		if(_pieces!=null && _pieces.length>0)
			scroll(StaticMethods.round(amount*(_pieces.length)));
	}
	
	private void scroll(int index) {
		if(_pieces!=null && _pieces.length>0) {
			_index = index % (_pieces.length);
			if(_index<0)
				_index += _pieces.length;
		}
	}
}