package net.quadratum.core;

import java.util.Map;
import java.util.HashMap;

public class Block {

	/** Bonuses given by this Block. */
	public Map<BonusType,Integer> _bonuses;
	/** Health points this Block has. */
	public int _health, _totalHealth;
	
	public enum BonusType {
		ATTACK,
		RANGE,
		DEFENSE,
		MOVEMENT,
		SIGHT,
		WATER_MOVEMENT,
		CLOAK,
		JUMP
		// TODO add more
	}
	
	/**
	 * Copy constructor for Block.
	 * @param block the Block to copy
	 */
	public Block(Block block)
	{
		_bonuses = new HashMap<BonusType, Integer>();
		_health = block._health;
		_totalHealth = block._totalHealth;
		for(BonusType key : block._bonuses.keySet())
		{
			_bonuses.put(key, new Integer(block._bonuses.get(key)));
		}
	}
}
