package net.quadratum.core;

import java.util.Map;

public class Block {

	/** Bonuses given by this Block. */
	Map<BonusType,Integer> _bonuses;
	/** Health points this Block has. */
	int _health;
	
	enum BonusType {
		ATTACK,
		MOVEMENT
		// TODO add more
	}
}
