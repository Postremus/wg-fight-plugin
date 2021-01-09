package com.pro_crafting.mc.wg.blockgenerator.criteria;


import com.pro_crafting.mc.common.Point;
import com.pro_crafting.mc.wg.blockgenerator.BlockData;

public interface Criteria {
	boolean matches(Point point, BlockData block);
	void wrap(Criteria criteria);
}
