package net.quadratum.gui;

import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import net.quadratum.core.Piece;

public class PiecesPanel extends JPanel {
	private PieceListPanel _pieceListPanel;
	private JButton _scrollUp, _scrollDown;
	private JTextArea _pieceInfo;
	
	public PiecesPanel(DrawingMethods drawingMethods) {
		setLayout(new LineLayout(LineLayout.LEFT_TO_RIGHT));
		
		JPanel imagePanel = new JPanel();
		imagePanel.setLayout(new BorderLayout());
		
		_pieceListPanel = new PieceListPanel(drawingMethods);
		imagePanel.add(_pieceListPanel, BorderLayout.CENTER);
		
		ActionListener actionListener = new PiecesPanelActionListener();
		
		_scrollUp = new JButton("Previous");
		_scrollUp.addActionListener(actionListener);
		imagePanel.add(_scrollUp, BorderLayout.NORTH);
		
		_scrollDown = new JButton("Next");
		_scrollDown.addActionListener(actionListener);
		imagePanel.add(_scrollDown, BorderLayout.SOUTH);
		
		add(imagePanel);
		
		_pieceInfo = new JTextArea();
		JScrollPane scroll = StaticMethods.createScrollingTextDisplay(_pieceInfo);
		scroll.setBorder(StaticMethods.getTitleBorder("Piece Info"));
		
		add(scroll);
		
		_scrollUp.setEnabled(false);
		_scrollDown.setEnabled(false);
	}
	
	public void setPieces(List<Piece> pieces) {
		_pieceListPanel.setPieces(pieces);
		
		_scrollUp.setEnabled(true);
		_scrollDown.setEnabled(true);
		
		repaint();
	}
	
	private String getPieceString(Piece p) {
		if(p==null)
			return "";
		else
			return p._name + "\n\n" + p._description;
	}
	
	private class PiecesPanelActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			
			if(source==_scrollUp) {
				_pieceListPanel.scrollUp();
			} else if(source==_scrollDown) {
				_pieceListPanel.scrollDown();
			} else {
				return;
			}
			
			_pieceInfo.setText(getPieceString(_pieceListPanel.getCurrentPiece()));
			_pieceInfo.setCaretPosition(0);
			
			repaint();
		}
	}
}