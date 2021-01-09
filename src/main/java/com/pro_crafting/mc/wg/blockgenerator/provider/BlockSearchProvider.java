package com.pro_crafting.mc.wg.blockgenerator.provider;


import com.pro_crafting.mc.common.Point;
import com.pro_crafting.mc.wg.blockgenerator.BlockData;
import com.pro_crafting.mc.wg.blockgenerator.criteria.Criteria;

public class BlockSearchProvider implements Provider {
	private Criteria criteria;

	public BlockSearchProvider(Criteria criteria) {
		this.criteria = criteria;
	}
	
	public BlockData getBlockData(Point point, BlockData block) {
		criteria.matches(point, block);
		return block;
	}

	public Criteria getCriteria() {
		return criteria;
	}
}
