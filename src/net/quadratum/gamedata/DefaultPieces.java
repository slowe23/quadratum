package net.quadratum.gamedata;

import java.util.ArrayList;

import net.quadratum.core.Block;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Piece;

public class DefaultPieces {
	
	public static ArrayList<Piece> getPieces() {
		ArrayList<Piece> pieces = new ArrayList<Piece>();

		// Attack block
		Block attackBlock = new Block(50);
		attackBlock._bonuses.put(Block.BonusType.ATTACK, 30);

		// Movement block
		Block movementBlock = new Block(50);
		movementBlock._bonuses.put(Block.BonusType.MOVEMENT, 30); // 4 for +1

		// Range block
		Block rangeBlock = new Block(50);
		rangeBlock._bonuses.put(Block.BonusType.RANGE, 24); // 5 for +1

		// Sight block
		Block sightBlock = new Block(50);
		sightBlock._bonuses.put(Block.BonusType.SIGHT, 30); // 4 for +1

		// Defense block
		Block defenseBlock = new Block(100);
		defenseBlock._bonuses.put(Block.BonusType.DEFENSE, 5); // 4 for +1

		// Water movement block
		Block waterBlock = new Block(30);
		waterBlock._bonuses.put(Block.BonusType.WATER_MOVEMENT, 1); // 1 for +1
		
		// Seer block
		Block seerBlock = new Block(35);
		seerBlock._bonuses.put(Block.BonusType.SIGHT, 20); // 6 for +1
		seerBlock._bonuses.put(Block.BonusType.RANGE, 20);
		
		// Archer block
		Block archerBlock = new Block(40);
		archerBlock._bonuses.put(Block.BonusType.ATTACK, 12); 
		archerBlock._bonuses.put(Block.BonusType.RANGE, 18); // 7 for +1
		
		// Beserker block
		Block beserkerBlock = new Block(55);
		beserkerBlock._bonuses.put(Block.BonusType.ATTACK, 15);
		beserkerBlock._bonuses.put(Block.BonusType.DEFENSE, 4);

		// Attack piece
		Piece attackPiece = new Piece(10, "Attack", "Provides +120 damage (50 hp per block)");
		attackPiece.addBlock(new MapPoint(0,0), new Block(attackBlock));
		attackPiece.addBlock(new MapPoint(0,1), new Block(attackBlock));
		attackPiece.addBlock(new MapPoint(1,1), new Block(attackBlock));
		attackPiece.addBlock(new MapPoint(1,2), new Block(attackBlock));
		pieces.add(attackPiece);

		// Movement piece
		Piece movementPiece = new Piece(40, "Movement", "Provides +1 movement radius (50 hp per block)");
		movementPiece.addBlock(new MapPoint(0,0), new Block(movementBlock));
		movementPiece.addBlock(new MapPoint(0,1), new Block(movementBlock));
		movementPiece.addBlock(new MapPoint(0,2), new Block(movementBlock));
		movementPiece.addBlock(new MapPoint(1,2), new Block(movementBlock));
		pieces.add(movementPiece);
		
		// Range piece
		Piece rangePiece = new Piece(30, "Range", "Provides +1 range radius (50 hp per block)");
		rangePiece.addBlock(new MapPoint(0,0), new Block(rangeBlock));
		rangePiece.addBlock(new MapPoint(2,0), new Block(rangeBlock));
		rangePiece.addBlock(new MapPoint(0,1), new Block(rangeBlock));
		rangePiece.addBlock(new MapPoint(1,1), new Block(rangeBlock));
		rangePiece.addBlock(new MapPoint(2,1), new Block(rangeBlock));
		pieces.add(rangePiece);

		// Sight piece
		Piece sightPiece = new Piece(20, "Sight", "Provides +1 sight radius (50 hp per block)");
		sightPiece.addBlock(new MapPoint(0,0), new Block(sightBlock));
		sightPiece.addBlock(new MapPoint(0,1), new Block(sightBlock));
		sightPiece.addBlock(new MapPoint(1,1), new Block(sightBlock));
		sightPiece.addBlock(new MapPoint(0,2), new Block(sightBlock));
		pieces.add(sightPiece);

		// Defense piece
		Piece defensePiece = new Piece(30, "Defense", "Provides +20 defense (100 hp per block)");
		defensePiece.addBlock(new MapPoint(0,0), new Block(defenseBlock));
		defensePiece.addBlock(new MapPoint(1,0), new Block(defenseBlock));
		defensePiece.addBlock(new MapPoint(0,1), new Block(defenseBlock));
		defensePiece.addBlock(new MapPoint(1,1), new Block(defenseBlock));
		pieces.add(defensePiece);

		// Water movement piece
		Piece waterPiece = new Piece(50, "Jesus", "Allows the unit to walk on water - resurrection not included (30 hp per block)");
		waterPiece.addBlock(new MapPoint(0,0), new Block(waterBlock));
		waterPiece.addBlock(new MapPoint(0,1), new Block(waterBlock));
		waterPiece.addBlock(new MapPoint(0,2), new Block(waterBlock));
		waterPiece.addBlock(new MapPoint(0,3), new Block(waterBlock));
		pieces.add(waterPiece);
		
		// Seer piece
		Piece seerPiece = new Piece(110, "Seer", "Provides +1 sight and range radius (35 hp per block)");
		seerPiece.addBlock(new MapPoint(0,0), new Block(seerBlock));
		seerPiece.addBlock(new MapPoint(1,0), new Block(seerBlock));
		seerPiece.addBlock(new MapPoint(2,0), new Block(seerBlock));
		seerPiece.addBlock(new MapPoint(3,0), new Block(seerBlock));
		seerPiece.addBlock(new MapPoint(0,1), new Block(seerBlock));
		seerPiece.addBlock(new MapPoint(1,1), new Block(seerBlock));
		pieces.add(seerPiece);
		
		// Archer piece
		Piece archerPiece = new Piece(100, "Archer", "Provides +84 attack and +1 range radius (40 hp per block)");
		archerPiece.addBlock(new MapPoint(1,0), new Block(archerBlock));
		archerPiece.addBlock(new MapPoint(0,1), new Block(archerBlock));
		archerPiece.addBlock(new MapPoint(1,1), new Block(archerBlock));
		archerPiece.addBlock(new MapPoint(2,1), new Block(archerBlock));
		archerPiece.addBlock(new MapPoint(1,2), new Block(archerBlock));
		archerPiece.addBlock(new MapPoint(1,3), new Block(archerBlock));
		archerPiece.addBlock(new MapPoint(2,3), new Block(archerBlock));
		pieces.add(archerPiece);
		
		// Beserker piece
		Piece beserkerPiece = new Piece(120, "Beserker", "Provides +75 attack and +20 defense");
		beserkerPiece.addBlock(new MapPoint(1,0), new Block(beserkerBlock));
		beserkerPiece.addBlock(new MapPoint(2,0), new Block(beserkerBlock));
		beserkerPiece.addBlock(new MapPoint(0,1), new Block(beserkerBlock));
		beserkerPiece.addBlock(new MapPoint(1,1), new Block(beserkerBlock));
		beserkerPiece.addBlock(new MapPoint(0,2), new Block(beserkerBlock));
		pieces.add(beserkerPiece);
		
		return pieces;
	}
}