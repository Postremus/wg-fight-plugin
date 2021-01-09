package com.pro_crafting.mc.wg.blockgenerator.job;


import com.pro_crafting.mc.common.Point;
import com.pro_crafting.mc.common.Size;
import com.pro_crafting.mc.wg.blockgenerator.JobState;
import org.bukkit.World;

public interface Job
{
	public boolean next();
	public JobState getState();
	public void setState(JobState state);
	public World getWorld();
	public int getAffectedBlocks();
	public Point getOrigin();
	public Size getSize();
}
