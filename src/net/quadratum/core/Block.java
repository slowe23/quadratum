package net.quadratum.core;

import java.util.Map;
import java.util.HashMap;

public class Block {

	/** Bonuses given by this Block. */
	public Map<BonusType,Integer> _bonuses;
	/** Health points this Block has. */
	public int _health, _totalHealth;
	
	public enum BonusType {
		ATTACK("Attack", false),
		RANGE("Range", false),
		DEFENSE("Defense", false),
		MOVEMENT("Movement", false),
		SIGHT("Sight", false),
		WATER_MOVEMENT("Water Movement", true),
		CLOAK("Cloaking", true),
		JUMP("Jump", true);
		// TODO add more
		
		private final String _name;
		private final boolean _isAbility;
		BonusType(String name, boolean isAbility) {
			_name = name;
			_isAbility = isAbility;
		}
		
		public String toString() {
			return _name;
		}
		
		//Represents whether this bonus should be displayed as an ability or a stat
		public boolean isAbility() {
			return _isAbility;
		}
		
		public boolean isStat() {
			return !_isAbility;
		}
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
