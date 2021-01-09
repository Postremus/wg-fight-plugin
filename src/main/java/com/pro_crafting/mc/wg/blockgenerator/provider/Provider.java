package com.pro_crafting.mc.wg.blockgenerator.provider;

import com.pro_crafting.mc.common.Point;
import com.pro_crafting.mc.wg.blockgenerator.BlockData;
import com.pro_crafting.mc.wg.blockgenerator.criteria.Criteria;

public interface Provider {
	BlockData getBlockData(Point point, BlockData block);
	Criteria getCriteria();
}
