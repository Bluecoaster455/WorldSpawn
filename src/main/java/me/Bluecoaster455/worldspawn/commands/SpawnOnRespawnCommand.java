package me.bluecoaster455.worldspawn.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.bluecoaster455.worldspawn.config.WSConfig;
import me.bluecoaster455.worldspawn.models.Permissions;
import me.bluecoaster455.worldspawn.services.WorldSpawnService;
import me.bluecoaster455.worldspawn.utils.Utils;

public class SpawnOnRespawnCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)){
			sender.sendMessage(WSConfig.getErrorPrefix()+WSConfig.getMessage("not-a-player-error"));
			return true;
		}
		
		Player p = (Player)sender;
		
		if(Permissions.hasPermission(p, Permissions.ADMIN)){
			
			String pWorld = p.getLocation().getWorld().getName();
			
			if(args.length > 0){
				World w = Bukkit.getWorld(args[0]);
				if(w == null){
					p.sendMessage(WSConfig.getErrorPrefix() + WSConfig.getMessage("specified-world-not-exist"));
					return true;
				}
				else{
					pWorld = w.getName();
				}
			}
			
			Location spawnloc = WorldSpawnService.getWorldSpawnLocation(pWorld);
			
			if(spawnloc == null){
				p.sendMessage(WSConfig.getAdminPrefix() + WSConfig.getMessage("no-spawn-world"));
				return true;
			}

      boolean respawn = false;

			if(args.length >= 2){
        try {
          respawn = Utils.parseBool(args[1]);
        } catch (IllegalArgumentException ex) {
          p.sendMessage(WSConfig.getAdminPrefix()+"§6/"+label+" <true|false>");
        }
			}
			
			boolean success = WorldSpawnService.setSpawnOnRespawn(pWorld, respawn);
			if(success) {
				p.sendMessage(WSConfig.getAdminPrefix() + WSConfig.getMessage("spawn-configure-success").replace("%w", spawnloc.getWorld().getName()));
			} else {
				p.sendMessage(WSConfig.getAdminPrefix() + WSConfig.getMessage("spawn-configure-fail").replace("%w", spawnloc.getWorld().getName()));
			}
			
		}
		else{
			sender.sendMessage(WSConfig.getErrorPrefix() + WSConfig.getMessage("command-no-permission"));
		}
		
		return true;
	}
  
}
