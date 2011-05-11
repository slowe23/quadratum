package net.quadratum.gui;

import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import net.quadratum.core.Piece;

public class PiecesPanel extends JPanel {
	private PieceListPanel _pieceListPanel;
	private JButton _scrollUp, _scrollDown, _rotate;
	private JTextArea _pieceInfo;

	private int _resources;
	
	private UnitImagePanel _unitImagePanel;
	
	public PiecesPanel(GUIPlayer player, UnitImagePanel unitImagePanel) {
		setLayout(new LineLayout(LineLayout.LEFT_TO_RIGHT));
		
		_unitImagePanel = unitImagePanel;
		
		JPanel imagePanel = new JPanel();
		imagePanel.setLayout(new BorderLayout());
		
		_pieceListPanel = new PieceListPanel(player);
		imagePanel.add(_pieceListPanel, BorderLayout.CENTER);
		
		ActionListener actionListener = new PiecesPanelActionListener();
		
		_scrollUp = new JButton("Previous");
		_scrollUp.addActionListener(actionListener);
		imagePanel.add(_scrollUp, BorderLayout.NORTH);
		
		_scrollDown = new JButton("Next");
		_scrollDown.addActionListener(actionListener);
		imagePanel.add(_scrollDown, BorderLayout.SOUTH);
		
		add(imagePanel);
		
		JPanel otherPanel = new JPanel();
		otherPanel.setLayout(new BorderLayout());
		
		StaticMethods.STD std = StaticMethods.createScrollingTextDisplay(5);
		_pieceInfo = std._jta;
		std._jsp.setBorder(StaticMethods.getTitleBorder("Piece Info"));
		otherPanel.add(std._jsp, BorderLayout.CENTER);
		
		_rotate = new JButton("Rotate Piece");
		_rotate.addActionListener(actionListener);
		otherPanel.add(_rotate, BorderLayout.SOUTH);
		
		add(otherPanel);
		
		_scrollUp.setEnabled(false);
		_scrollDown.setEnabled(false);
		
		addComponentListener(new PiecesPanelComponentListener());
	}
	
	public void setPieces(List<Piece> pieces) {
		synchronized(_pieceListPanel) {
			_pieceListPanel.setPieces(pieces);
			
			_pieceInfo.setText(getPieceString(_pieceListPanel.getCurrentPiece()));
			_pieceInfo.setCaretPosition(0);
			
			_scrollUp.setEnabled(pieces!=null && pieces.size()>1);
			_scrollDown.setEnabled(pieces!=null && pieces.size()>1);
			updateUIP();
		}
		
		repaint();
	}
	
	public void updateResources(int resources) {
		_resources = resources;
		updateUIP();
	}
	
	private void updateUIP() {
		if(isVisible()) {
			synchronized(_pieceListPanel) {
				Piece p = _pieceListPanel.getCurrentPiece();
				
				if(p!=null && p._cost<=_resources)
					_unitImagePanel.pieceSelected(_pieceListPanel.getCurrentPieceID(), _pieceListPanel.getCurrentPiece(), _pieceListPanel.getCurrentRotation());
				else
					_unitImagePanel.noPieceSelected();
			}
		} else {
			_unitImagePanel.noPieceSelected();
		}
	}
	
	private String getPieceString(Piece p) {
		if(p==null)
			return "";
		else
			return p._name + "\nCost: "+p._cost+"\n" + p._description;
	}
	
	private class PiecesPanelActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			synchronized(_pieceListPanel) {
				Object source = e.getSource();
				if(source==_scrollUp) {
					_pieceListPanel.scrollUp();
				} else if(source==_scrollDown) {
					_pieceListPanel.scrollDown();
				} else if(source==_rotate) {
					_pieceListPanel.rotatePiece(1);
					return;
				}
				
				_pieceInfo.setText(getPieceString(_pieceListPanel.getCurrentPiece()));
				_pieceInfo.setCaretPosition(0);

				updateUIP();
			}
			
			repaint();
		}
	}
	
	private class PiecesPanelComponentListener extends ComponentAdapter {
		public void componentShown(ComponentEvent e) {
			updateUIP();
		}
		
		public void componentHidden(ComponentEvent e) {
			updateUIP();
		}
	}
}