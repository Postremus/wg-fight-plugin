package de.hrc_gaming.wg.arena;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.hrc_gaming.wg.WarGear;
import de.hrc_gaming.wg.event.ArenaStateChangedEvent;
import de.hrc_gaming.wg.event.FightQuitEvent;
import de.hrc_gaming.wg.event.WinQuitEvent;
import de.hrc_gaming.wg.modes.KitMode;
import de.hrc_gaming.wg.team.TeamNames;
import de.hrc_gaming.wg.team.WgTeam;

public class ArenaListener implements Listener
{
	private Arena arena;
	private WarGear plugin;
	
	public ArenaListener(WarGear plugin, Arena arena)
	{
		this.plugin = plugin;
		this.arena = arena;
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled=true)
	public void playerMoveHandler(PlayerMoveEvent event)
	{
		if (event.getTo().getBlockX() == event.getFrom().getBlockX() &&
				event.getTo().getBlockY() == event.getFrom().getBlockY() &&
				event.getTo().getBlockZ() == event.getFrom().getBlockZ())
		{
			return;
		}
		boolean isInArena = this.arena.contains(event.getTo());
		if (!isInArena)
		{
			arena.leave(event.getPlayer());
		}
		else
		{
			arena.join(event.getPlayer());
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled=true)
	public void playerJoinHandler(PlayerJoinEvent event)
	{
		if (this.arena.contains(event.getPlayer().getLocation()))
		{
			arena.join(event.getPlayer());
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled=true)
	public void playerQuitHandler(PlayerQuitEvent event)
	{
		if (this.arena.contains(event.getPlayer().getLocation()))
		{
			arena.leave(event.getPlayer());
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled=true)
	public void playerKickHandler(PlayerKickEvent event)
	{
		if (this.arena.contains(event.getPlayer().getLocation()))
		{
			arena.leave(event.getPlayer());
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled=true)
	public void playerTeleportHandler(PlayerTeleportEvent event)
	{
		if (this.arena.contains(event.getTo()))
		{
			arena.join(event.getPlayer());
		}
		else
		{
			arena.leave(event.getPlayer());
		}
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void entityDamgeHandler(EntityDamageEvent event)
	{
		if (!(event.getEntity() instanceof Player))
		{
			return;
		}
		final Player player = (Player)event.getEntity();
		if (this.arena.getTeam().getTeamOfPlayer(player) == null)
		{
			return;
		}
		if (this.arena.getState() != State.Running)
		{
			event.setCancelled(true);
			return;
		}
		if (event instanceof EntityDamageByEntityEvent)
		{
			this.checkTeamDamaging((EntityDamageByEntityEvent)event, player);
		}
		
		this.plugin.getServer().getScheduler().runTask(this.plugin, new Runnable(){
			public void run()
			{
				ArenaListener.this.arena.getScore().updateHealthOfPlayer(player);
			}
		});
	}
	
	private void checkTeamDamaging(EntityDamageByEntityEvent event, Player player)
	{
		Player damager = null;
		if (event.getDamager() instanceof Projectile
				&& ((Projectile)event.getDamager()).getShooter() instanceof Player)
		{
			damager = (Player) ((Projectile)event.getDamager()).getShooter();
		}
		else if (event.getDamager() instanceof Player)
		{
			damager = (Player) event.getDamager();
		}
		if (damager != null && this.arena.getTeam().getTeamOfPlayer(player).equals(this.arena.getTeam().getTeamOfPlayer(damager)))
		{
			damager.sendMessage("�7Du darfst keinen Spieler aus deinem eigenen Team Schaden zuf�gen.");
			event.setCancelled(true);
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled=true)
	public void entityRegainHealthHandler(EntityRegainHealthEvent event)
	{
		if (!(event.getEntity() instanceof Player))
		{
			return;
		}
		final Player player = (Player)event.getEntity();
		if (this.arena.getTeam().getTeamOfPlayer(player) != null)
		{
			this.plugin.getServer().getScheduler().runTask(this.plugin, new Runnable(){
				public void run()
				{
					ArenaListener.this.arena.getScore().updateHealthOfPlayer(player);
				}
			});
		}
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void quit(FightQuitEvent event)
	{
		if (!this.arena.equals(event.getArena()))
		{
			return;
		}
		if (this.arena.getState() != State.PreRunning && this.arena.getState() != State.Running)
		{
			return;
		}
		event.getArena().close();
		if (event.getMessage().length() > 0)
		{
			event.getArena().broadcastMessage(ChatColor.DARK_GREEN + event.getMessage());
		}
		if (event instanceof WinQuitEvent)
		{
			WinQuitEvent winEvent = (WinQuitEvent)event;
			event.getArena().getTeam().sendWinnerOutput(winEvent.getWinnerTeam().getTeamName());
		}
		event.getArena().getFightMode().stop();
		event.getArena().updateState(State.Spectate);
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void arenaStateChangedHandler(ArenaStateChangedEvent event)
	{
		if (!event.getArena().equals(this.arena))
		{
			return;
		}
		if (event.getTo() == State.Idle)
		{
			this.arena.getTeam().quitFight();
			this.arena.setFightMode(new KitMode(this.plugin, this.arena));
		}
		if (event.getTo() == State.Spectate)
		{
			this.arena.getSpectatorMode().start();
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void asyncPlayerChatHandler(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();
		if (!this.plugin.getRepo().isPrefixEnabled() || !this.arena.contains(player.getLocation()))
		{
			return;
		}
		WgTeam team = this.arena.getTeam().getTeamOfPlayer(player);
		String color = "�7";
		
		if (team != null)
		{
			if (team.getTeamName() == TeamNames.Team1)
			{
				color = "�c";
			}
			else if (team.getTeamName() == TeamNames.Team2)
			{
				color = "�1";
			}
		}
		event.setFormat(color+"["+this.arena.getName()+"]"+event.getFormat());
	}
	
	 @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled=true)
	 public void playerRespawnHandler(PlayerRespawnEvent event)
	 {
		 final Player respawned = event.getPlayer();
		 if (!this.arena.contains(respawned.getLocation()))
		 {
			 return;
		 }
		 
		 event.setRespawnLocation(this.arena.getSpawnLocation(respawned));
		 
		 this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable(){
			public void run() {
				respawned.getInventory().clear();
			}
		 }, 60);
	 }
}