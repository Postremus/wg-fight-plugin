package com.pro_crafting.mc.wg.blockgenerator.criteria;


import com.pro_crafting.mc.common.Point;
import com.pro_crafting.mc.wg.blockgenerator.BlockData;

public class CuboidCriteria implements Criteria {

	private Criteria wraped;

	public boolean matches(Point point, BlockData block) {
		if (wraped != null) {
			return wraped.matches(point, block);
		}
		return true;
	}

	public void wrap(Criteria criteria) {
		if (wraped == null) {
			wraped = criteria;
		} else {
			wraped.wrap(criteria);
		}
	}
}
