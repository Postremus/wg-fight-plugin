package me.Postremus.WarGear.Arena.ui;

import me.Postremus.WarGear.DrawReason;
import me.Postremus.WarGear.WarGear;
import me.Postremus.WarGear.Arena.Arena;
import me.Postremus.WarGear.Events.DrawQuitEvent;
import me.Postremus.WarGear.Events.TeamWinQuitEvent;

public class ArenaTimer 
{
	private WarGear plugin;
	private Arena arena;
	private int taskId;
	private int countdownStartTime;
	private int fightDuration;
	
	public ArenaTimer(WarGear plugin, Arena arena)
	{
		this.plugin = plugin;
		this.arena = arena;
		taskId = -1;
		countdownStartTime = 60;
	}
	
	public void start()
	{
		this.stop();
		this.taskId = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable(){
			public void run()
			{
				updateTime();
			}
		}, 0, 1200);
	}
	
	public void stop()
	{
		if (taskId != -1)
		{
			this.plugin.getServer().getScheduler().cancelTask(taskId);
			taskId = -1;
		}
		this.fightDuration = 0;
	}
	
	public boolean getIsRunning()
	{
		return this.taskId != -1;
	}
	
	private void updateTime()
	{
		this.arena.getScore().updateTime(countdownStartTime-fightDuration);
		if (fightDuration == countdownStartTime)
		{
			DrawQuitEvent draw = new DrawQuitEvent(this.arena, "Zeit abgelaufen - Unentschieden", this.arena.getTeam().getTeam1(), this.arena.getTeam().getTeam2(), DrawReason.Time);
			this.plugin.getServer().getPluginManager().callEvent(draw);
		}
		this.fightDuration++;
	}
}
