package com.pro_crafting.mc.wg.blockgenerator.criteria;


import com.pro_crafting.mc.common.Point;
import org.bukkit.Material;

import java.util.ArrayList;

public class SingleBlockFindCreateria {
	private Criteria wraped;
	private Material type;
	private List<Point> matches;
	
	public SingleBlockFindCreateria(Material type) {
		this.type = type;
		this.matches = new ArrayList<Point>();
	}
	
	public boolean matches(Point point, BlockData block) {
		boolean shouldSet = block.getType() == this.type;
		if (wraped != null && shouldSet) {
			shouldSet = wraped.matches(point, block);
		}
		if (shouldSet) {
			this.matches.add(point);
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
	
	public List<Point> getMatches() {
		return this.matches;
	}
}
