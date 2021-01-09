package com.pro_crafting.mc.wg.blockgenerator.criteria;


import com.pro_crafting.mc.common.Point;
import com.pro_crafting.mc.wg.blockgenerator.BlockData;
import org.bukkit.Material;

public class SingleBlockCriteria implements Criteria {
	
	Criteria wraped;
	Material type;
	
	public SingleBlockCriteria(Material type) {
		this.type = type;
	}
	
	public boolean matches(Point point, BlockData block) {
		boolean shouldSet = block.getType() == this.type;
		if (wraped != null && shouldSet) {
			return wraped.matches(point, block);
		}
		return shouldSet;
	}

	public void wrap(Criteria criteria) {
		if (wraped == null) {
			wraped = criteria;
		} else {
			wraped.wrap(criteria);
		}
	}

}
