package net.quadratum.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Block implements Serializable {

	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = -6999486864534816402L;
	
	/** Bonuses given by this Block. */
	public Map<BonusType,Integer> _bonuses;
	/** Health points this Block has. */
	public int _health, _totalHealth;
	
	public enum BonusType {
		ATTACK("Attack", false),
		ATTACK_WIDTH("Attack Width", false),
		RANGE("Range", false),
		DEFENSE("Defense", false),
		MOVEMENT("Movement", false),
		SIGHT("Sight", false),
		WATER_MOVEMENT("Water Movement", true),
		HEART("Life", true);
		/*CLOAK("Cloaking", true),
		JUMP("Jump", true);*/
		// TODO add support for these blocks
				
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
	 * Constructor for Block.
	 * @param health the max health of this block
	 */
	public Block(int health)
	{
		_health = health;
		_totalHealth = health;
		_bonuses = new HashMap<BonusType, Integer>();
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
