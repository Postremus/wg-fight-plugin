package com.pro_crafting.mc.wg.blockgenerator.criteria;


import com.pro_crafting.mc.common.Point;
import com.pro_crafting.mc.common.Size;
import com.pro_crafting.mc.wg.blockgenerator.BlockData;

public class EdgeOutlineCriteria implements Criteria {

	private Criteria wraped;
	private Size size;
	
	public EdgeOutlineCriteria(Size size) {
		this.size = size;
	}
	
	public boolean matches(Point point, BlockData block) {
		boolean shouldset = isSide(point, 0) || isSide(point, size.getHeight()) || isEdge(point);
		
		return wraped != null && shouldset ? wraped.matches(point, block) : shouldset;
	}
	
	private boolean isSide(Point p, int y) {
		if (p.getY() == y) {
			return p.getX() == 0 || p.getZ() == 0 || p.getX() == size.getWidth() || p.getZ() == size.getDepth();
		}
		return false;
	}
	
	private boolean isEdge(Point p) {
		return p.getX() == 0 && p.getZ() == 0 || 
				p.getX() == 0 && p.getZ() == size.getDepth() ||
				p.getX() == size.getWidth() && p.getZ() == 0 ||
				p.getX() == size.getWidth() && p.getZ() == size.getDepth();
	}
	
	public void wrap(Criteria criteria) {
		if (wraped == null) {
			wraped = criteria;
		}
		else {
			wraped.wrap(criteria);
		}
	}

}
