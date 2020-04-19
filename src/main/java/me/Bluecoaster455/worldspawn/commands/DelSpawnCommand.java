package me.bluecoaster455.worldspawn.commands;

import me.bluecoaster455.worldspawn.config.WSConfig;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelSpawnCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)){
			sender.sendMessage(WSConfig.getErrorPrefix()+WSConfig.getMessage("not-a-player-error"));
			return true;
		}
		
		Player p = (Player)sender;
		
		if(p.hasPermission("worldspawn.admin")){
			
			String pWorld = p.getLocation().getWorld().getName();
			
			if(args.length > 0){
				World w = Bukkit.getWorld(args[0]);
				if(w == null){
					p.sendMessage(WSConfig.getErrorPrefix()+WSConfig.getMessage("specified-world-not-exist"));
					return true;
				}
				else{
					pWorld = w.getName();
				}
			}
			
			Location spawnloc = WSConfig.existsSpawn(pWorld) ? WSConfig.getWorldSpawn(pWorld).getLocation() : null;
			
			if(spawnloc == null){
				p.sendMessage(WSConfig.getAdminPrefix()+WSConfig.getMessage("no-spawn-world"));
				return true;
			}
			
			WSConfig.deleteSpawn(pWorld);
			
			p.sendMessage(WSConfig.getAdminPrefix()+WSConfig.getMessage("spawn-delete-success").replace("%w", spawnloc.getWorld().getName()));
		}
		else{
			sender.sendMessage(WSConfig.getErrorPrefix()+WSConfig.getMessage("command-no-permission"));
		}
		
		return true;
	}

}
