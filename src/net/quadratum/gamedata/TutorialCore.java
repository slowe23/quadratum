package net.quadratum.gamedata;

import java.util.ArrayList;

import net.quadratum.core.GameCore;
import net.quadratum.core.Piece;
import net.quadratum.core.WinCondition;
import net.quadratum.main.Main;

public class TutorialCore extends GameCore {

	/**
	 * Constructor for TutorialCore.
	 * @param m the main to return control to after the game is over
	 * @param map the map file name
	 * @param winCondition the win condition
	 * @param pieces the pieces for every player
	 */
	public TutorialCore(Main m, String map, WinCondition winCondition,
			ArrayList<Piece> pieces) {
		super(m, map, winCondition, pieces);
	}
	
	/**
	 * Constructor for TutorialCore.
	 * @param m the main to return control to after the game is over
	 * @param map the map file name
	 * @param winCondition the win condition
	 * @param pieces the pieces for each player
	 */
	public TutorialCore(Main m, String map, WinCondition winCondition,
			ArrayList<Piece>[] pieces) {
		super(m, map, winCondition, pieces);
	}
	
	/**
	 * Adds a piece to all players' piece lists.
	 * @param p the piece to add
	 */
	public void addPiece(Piece p) {
		for (int i = 0; i < _pieces.length; i++) {
			_pieces[i].add(p);
			ArrayList<Piece> tempPieces = new ArrayList<Piece>();
			for(int j = 0; j < _pieces[i].size(); j++)
			{
				tempPieces.add(new Piece(_pieces[i].get(j)));
			}
			_players.get(i).updatePieces(tempPieces);
		}
	}

}
