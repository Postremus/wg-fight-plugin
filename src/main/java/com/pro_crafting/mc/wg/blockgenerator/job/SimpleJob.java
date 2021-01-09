package com.pro_crafting.mc.wg.blockgenerator.job;


import com.pro_crafting.mc.common.Point;
import com.pro_crafting.mc.common.Size;
import com.pro_crafting.mc.wg.blockgenerator.BlockData;
import com.pro_crafting.mc.wg.blockgenerator.JobState;
import com.pro_crafting.mc.wg.blockgenerator.JobStateChangedCallback;
import com.pro_crafting.mc.wg.blockgenerator.provider.Provider;
import com.pro_crafting.mc.wg.blockgenerator.provider.SizeProvider;
import org.bukkit.World;
import org.bukkit.block.Block;

public class SimpleJob implements Job {
	private Point relativeLocation;
	
	private JobState jobState;
	private JobStateChangedCallback callback;
	private Provider provider;
	private World world;
	private int affected;
	private Point origin;
	private Size size;
	private boolean doPhysics;

	public SimpleJob(Point origin, World world, JobStateChangedCallback callback, SizeProvider provider, boolean doPhysics) {
		this(origin, provider.getSize(), world, callback, provider, doPhysics);
	}

	public SimpleJob(Point origin, World world, JobStateChangedCallback callback, SizeProvider provider) {
		this(origin, provider.getSize(), world, callback, provider, true);
	}

	public SimpleJob(Point origin, Size size, World world, JobStateChangedCallback callback, Provider provider) {
		this(origin, size, world, callback, provider, true);
	}

	public SimpleJob(Point origin, Size size, World world, JobStateChangedCallback callback, Provider provider, boolean doPhysics) {
		this.jobState = JobState.Unstarted;
		this.world = world;
		this.callback = callback;
		this.provider = provider;
		
		this.origin = origin;
		this.size = size;
		
		relativeLocation = new Point(0, size.getHeight(), 0);
		this.doPhysics = doPhysics;
	}
	
	public boolean next() {
		Block block = world.getBlockAt(relativeLocation.getX()+this.origin.getX(), relativeLocation.getY()+this.origin.getY(), relativeLocation.getZ()+this.origin.getZ());
		
		BlockData current = new BlockData(block.getType(), block.getData());
		BlockData ret = this.provider.getBlockData(relativeLocation, current);
		
		nextPosition();
		if (!apply(block, ret)) {
			affected++;	
			return true;
		}
		return false;
	}
	
	private boolean apply(Block currentBlock, BlockData data) {
		if (data == null) {
			return false;
		}
		return currentBlock.setTypeIdAndData(data.getType().getId(), data.getDataByte(), doPhysics);
	}
	
	private void nextPosition() {
		if (relativeLocation.getX() == getSize().getWidth()+1) {
			this.setState(JobState.Finished);
		}

		relativeLocation.setZ(relativeLocation.getZ()+1);
		
		if (relativeLocation.getZ() == getSize().getDepth()+1) {
			relativeLocation.setZ(0);
			relativeLocation.setY(relativeLocation.getY()-1);
		}
		
		if (relativeLocation.getY() == -1) {
			relativeLocation.setY(getSize().getHeight());
			relativeLocation.setX(relativeLocation.getX()+1);
		}
	}

	public JobState getState() {
		return this.jobState;
	}

	public void setState(JobState state) {
		JobState from = this.jobState;
		this.jobState = state;
		if (this.callback != null) {
			this.callback.jobStateChanged(this, from);
		}
	}

	public Point getOrigin() {
		return this.origin;
	}

	public Size getSize() {
		return this.size;
	}
	
	public World getWorld() {
		return this.world;
	}

	public int getAffectedBlocks() {
		return this.affected;
	}
}
