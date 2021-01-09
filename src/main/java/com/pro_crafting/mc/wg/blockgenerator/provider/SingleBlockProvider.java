package com.pro_crafting.mc.wg.blockgenerator.provider;

import com.pro_crafting.mc.common.Point;
import com.pro_crafting.mc.wg.blockgenerator.BlockData;
import com.pro_crafting.mc.wg.blockgenerator.criteria.Criteria;
import org.bukkit.Material;

public class SingleBlockProvider implements Provider{
	private BlockData blockData;
	private Criteria criteria;
	
	public SingleBlockProvider(Criteria criteria, Material type, byte dataByte) {
		this.criteria = criteria;
		this.blockData = new BlockData(type, dataByte);
	}
	
	public BlockData getBlockData(Point point, BlockData block) {
		if (!criteria.matches(point, block)) {
			return block;
		}
		return this.blockData;
	}

	public Criteria getCriteria() {
		return this.criteria;
	}
}
