package de.hrc_gaming.wg.commands;

import org.bukkit.entity.Player;

import de.hrc_gaming.commandframework.Command;
import de.hrc_gaming.commandframework.CommandArgs;
import de.hrc_gaming.wg.FightQuitReason;
import de.hrc_gaming.wg.WarGear;
import de.hrc_gaming.wg.WarGearUtil;
import de.hrc_gaming.wg.arena.Arena;
import de.hrc_gaming.wg.arena.ArenaState;
import de.hrc_gaming.wg.event.DrawQuitEvent;
import de.hrc_gaming.wg.event.WinQuitEvent;

public class WarGearCommands {
	private WarGear plugin;
	
	public WarGearCommands(WarGear plugin)
	{
		this.plugin = plugin;
	}
	
	@Command(name = "wgk", aliases = { "wgk.help" }, description = "Zeigt die Hilfe an.", usage = "/wgk", permission="wargear")
	public void WarGear(CommandArgs args)
	{
		args.getSender().sendMessage("�c�LKein passender Befehl gefunden!");
		args.getSender().sendMessage("�B/wgk team ...");
		args.getSender().sendMessage("�B/wgk arena ...");
		args.getSender().sendMessage("�B/wgk kit <kitName>");
		args.getSender().sendMessage("�B/wgk warp <arenaname> [playername]");
		args.getSender().sendMessage("�B/wgk quit [team1|team2]");
		args.getSender().sendMessage("�B/wgk reload");
	}
	
	@Command(name = "wgk.reload", description = "Reloadet die Config.", usage="/wgk reload", permission="wargear.reload")
	public void reload(CommandArgs args)
	{
		this.plugin.reloadConfig();
		this.plugin.getServer().getPluginManager().disablePlugin(plugin);
		this.plugin.getServer().getPluginManager().enablePlugin(plugin);
		args.getSender().sendMessage("Plugin wurde gereloadet.");
	}
	
	@Command(name = "wgk.warp", description = "Teleport zu der Arena.", usage="/wgk warp <arenaname> [player]", permission="wargear.warp")
	public void warp(CommandArgs args)
	{
		if (args.getArgs().length < 1)
		{
			args.getSender().sendMessage("�cEs muss eine Arena angegeben werden.");
			return;
		}
		String arenaName = args.getArgs()[0];
		Arena arena = this.plugin.getArenaManager().getArena(arenaName);
		if (arena == null)
		{
			args.getSender().sendMessage("�cDie Arena "+ arenaName+" existiert nicht.");
			return;
		}
		
		Player toWarp = args.getPlayer();
		if (args.getArgs().length >= 2)
		{
			if (!args.getSender().hasPermission("wargear.warp.other"))
			{
				args.getSender().sendMessage("�cDu nichts Rechte daf�r.");
				return;
			}
			if (this.plugin.getServer().getPlayer(args.getArgs()[1]) == null)
			{
				args.getSender().sendMessage("�c"+args.getArgs()[1]+" Ist nicht online.");
				return;
			}
			toWarp = this.plugin.getServer().getPlayer(args.getArgs()[1]);
		}
		
		if (toWarp == null)
		{
			args.getSender().sendMessage("�cEs muss ein Spieler angegeben werden.");
			return;
		}
		
		arena.teleport(toWarp);
	}
	
	@Command(name = "wgk.kit", description="Legt das Kit f�r den Fight fest.", usage="/wgk kit name", permission="wargear.kit")
	public void kit(CommandArgs args)
	{
		Arena arena = WarGearUtil.getArenaFromSender(plugin, args.getSender(), args.getArgs());
		if (arena == null)
		{
			args.getSender().sendMessage("�cDu stehst in keiner Arena, oder Sie existiert nicht.");
			return;
		}
		if (args.getArgs().length == 0)
		{
			args.getSender().sendMessage("�cDu hast kein Kit angegeben.");
			return;
		}
		if (arena.getState() != ArenaState.Setup)
		{
			args.getSender().sendMessage("�cEs muss bereits min. ein Team geben.");
			return;
		}
		String kitName = args.getArgs()[0];
		if (!this.plugin.getKitApi().existsKit(kitName))
		{
			args.getSender().sendMessage("�cDas Kit " + kitName + " gibt es nicht.");
			return;
		}
		arena.setKit(kitName);
	}
	
	@Command(name="wgk.quit", description="Beendet einen Fight.", usage="/wgk quit <team1|team2>",permission="wargear.quit")
	public void quit(CommandArgs args)
	{
		Arena arena = WarGearUtil.getArenaFromSender(plugin, args.getSender(), args.getArgs());
		if (arena == null)
		{
			args.getSender().sendMessage("�cDu stehst in keiner Arena, oder Sie existiert nicht.");
			return;
		}
		
		if (arena.getState() != ArenaState.PreRunning && arena.getState() != ArenaState.Running)
		{
			args.getSender().sendMessage("�cIn dieser Arena l�uft kein Fight.");
			return;
		}
		
		if (args.getArgs().length == 0)
		{
			DrawQuitEvent event = new DrawQuitEvent(arena, "Unentschieden", arena.getTeam().getTeam1(), arena.getTeam().getTeam2(), FightQuitReason.FightLeader);
			this.plugin.getServer().getPluginManager().callEvent(event);
		}
		else if (args.getArgs()[0].equalsIgnoreCase("team1"))
		{
			WinQuitEvent event = new WinQuitEvent(arena, "", arena.getTeam().getTeam1(), arena.getTeam().getTeam2(), FightQuitReason.FightLeader);
			this.plugin.getServer().getPluginManager().callEvent(event);
		}
		else if (args.getArgs()[0].equalsIgnoreCase("team2"))
		{
			WinQuitEvent event = new WinQuitEvent(arena, "", arena.getTeam().getTeam2(), arena.getTeam().getTeam1(), FightQuitReason.FightLeader);
			this.plugin.getServer().getPluginManager().callEvent(event);
		}
	}
}
