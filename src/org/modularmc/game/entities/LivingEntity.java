package org.modularmc.game.entities;

import org.modularmc.game.world.World;

/**
 * @author Caspar Nor�e Palm
 */
public class LivingEntity extends Entity {
	private double health;
	private String name;
	
	public LivingEntity(World w) {
		super(w);
	}
	
}
