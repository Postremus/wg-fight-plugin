package me.Postremus.Generator;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class JobStateChangedEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	IGeneratorJob job;
	GeneratorJobState from;
	GeneratorJobState to;
	
	public JobStateChangedEvent(IGeneratorJob job, GeneratorJobState from, GeneratorJobState to)
	{
		this.job = job;
		this.from = from;
		this.to = to;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public IGeneratorJob getJob()
    {
    	return this.job;
    }
    
    public GeneratorJobState getFrom()
    {
    	return this.from;
    }
    
    public GeneratorJobState getTo()
    {
    	return this.to;
    }
}