package de.hrc_gaming.wg.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.hrc_gaming.wg.arena.Arena;
import de.hrc_gaming.wg.arena.State;

public class ArenaStateChangedEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private Arena arena;
	private State from;
	private State to;
	
	public ArenaStateChangedEvent(Arena arena, State from, State to)
	{
		this.arena = arena;
		this.from = from;
		this.to = to;
	}
	
    public Arena getArena()
    {
    	return this.arena;
    }
	
	public State getFrom()
	{
		return from;
	}
	
	public State getTo()
	{
		return to;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
    public static HandlerList getHandlerList() {
        return handlers;
    }

}