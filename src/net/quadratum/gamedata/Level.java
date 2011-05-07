package net.quadratum.gamedata;

import java.util.ArrayList;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;
import net.quadratum.core.WinCondition;

public interface Level
{
	String getMap();
	int getStartingResources();
	int getMaxUnits();
	Player getAI();
	WinCondition getWinCondition();
	ArrayList<Piece>[] getPieces();
}